package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DeliveryResultMrDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.MrPlanDao;
import jp.co.takeda.dao.OfficePlanDao;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.TeamPlanDao;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.DeliveryResultSosSummaryDto;
import jp.co.takeda.dto.DeliveryResultSummaryDto;
import jp.co.takeda.dto.MrPlanDto;
import jp.co.takeda.dto.MrPlanSosSummaryDto;
import jp.co.takeda.dto.OfficePlanDto;
import jp.co.takeda.dto.TeamPlanDto;
import jp.co.takeda.dto.TeamPlanSosSummaryDto;
import jp.co.takeda.model.DeliveryResultMr;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MonNnu;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.model.OfficePlan;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.TeamPlan;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.StatusForMrPlan;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.MathUtil;

/**
 * 担当者別計画および納入実績の営業所情報を作成するロジッククラス
 *
 * @author nozaki
 */
public class CreateMrPlanOfficeInfoLogic {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(CreateMrPlanOfficeInfoLogic.class);

	/**
	 * 従業員情報DAO
	 */
	private final JgiMstDAO jgiMstDAO;

	/**
	 * 組織情報DAO
	 */
	private final SosMstDAO sosMstDAO;

	/**
	 * 営業所計画DAO
	 */
	private final OfficePlanDao officePlanDao;

	/**
	 * チーム計画DAO
	 */
	private final TeamPlanDao teamPlanDao;

	/**
	 * 担当者計画DAO
	 */
	private final MrPlanDao mrPlanDao;

	/**
	 * 担当者別納入実績DAO
	 */
	private final DeliveryResultMrDao deliveryResultMrDao;

	/**
	 * 参照用品目の実績リスト
	 */
	private final List<DeliveryResultSummaryDto> deliveryResultSummaryDtoList;

	/**
	 * 担当者別計画ステータス
	 */
	private final StatusForMrPlan mrPlanStatus;

	/**
	 * コンストラクタ
	 *
	 * @param jgiMstDAO 従業員情報DAO
	 * @param sosMstDAO 組織情報DAO
	 * @param officePlanDao 営業所計画DAO
	 * @param teamPlanDao チーム計画DAO
	 * @param mrPlanDao 担当者計画DAO
	 * @param deliveryResultMrDao 担当者別納入実績DAO
	 * @param deliveryResultSummaryDtoList 参照用品目の実績リスト
	 * @param mrPlanStatus 担当者別計画ステータス
	 */
	public CreateMrPlanOfficeInfoLogic(JgiMstDAO jgiMstDAO, SosMstDAO sosMstDAO, OfficePlanDao officePlanDao, TeamPlanDao teamPlanDao, MrPlanDao mrPlanDao, DeliveryResultMrDao deliveryResultMrDao,
		List<DeliveryResultSummaryDto> deliveryResultSummaryDtoList, StatusForMrPlan mrPlanStatus ) {

		this.jgiMstDAO = jgiMstDAO;
		this.sosMstDAO = sosMstDAO;
		this.officePlanDao = officePlanDao;
		this.teamPlanDao = teamPlanDao;
		this.mrPlanDao = mrPlanDao;
		this.deliveryResultMrDao = deliveryResultMrDao;
		this.deliveryResultSummaryDtoList = deliveryResultSummaryDtoList;
		this.mrPlanStatus = mrPlanStatus;
	}

	/**
	 * 営業所以下の各計画(営業所計画、チーム別計画、担当者別計画)を取得する。<br>
	 * チーム別計画、担当者別計画、共通。
	 *
	 * @param insType 施設出力対象区分(UH,P以外の場合は合計)
	 * @param prodCode 品目固定コード
	 * @param sosCd3 営業所組織コード
	 * @param teamSosMstList 営業所配下のチーム情報リスト
	 */
	public OfficePlanDto execute(InsType insType, PlannedProd plannedProd, String sosCd3, List<SosMst> teamSosMstList) {


		// 品目からカテゴリ判定
		String category = plannedProd.getCategory();

		// 調整金額有無フラグ
		boolean choseiFlg = false;
		// チーム単位に繰り返し
		List<TeamPlanDto> mrPlanTeamInfoList = new ArrayList<TeamPlanDto>();
		for (SosMst teamSosMst : teamSosMstList) {

			// ----------------------
			// 担当者別計画公開確認
			// ----------------------
			boolean isMrPlanOpened = true;

			// AL、かつ担当者別計画が公開されていない場合は、担当者別計画を非表示にする
			DpUserInfo dpUserInfo = DpUserInfo.getDpUserInfo();
			DpUser dpUser = dpUserInfo.getSettingUser();
			if (dpUser != null && dpUser.isMatch(JokenSet.IYAKU_AL)) {
				if(	(mrPlanStatus != StatusForMrPlan.MR_PLAN_OPENED ) &&
					(mrPlanStatus != StatusForMrPlan.MR_PLAN_INPUT_FINISHED) &&
					(mrPlanStatus != StatusForMrPlan.MR_PLAN_COMPLETED )){
					isMrPlanOpened = false;
				}
			}

			// ----------------------
			// 担当者別計画情報作成
			// ----------------------
			// 従業員一覧取得
			List<JgiMst> jgiMstList = null;
			try {
				jgiMstList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, teamSosMst.getSosCd(), SosListType.SHITEN_LIST, BumonRank.TEAM);
			} catch (DataNotFoundException e) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("チーム配下の従業員が存在しない：" + teamSosMst.getSosCd());
				}
				jgiMstList = new ArrayList<JgiMst>();
			}

			// 納入実績取得
			List<DeliveryResultMr> deliveryResultMrList;
			try {
				deliveryResultMrList = deliveryResultMrDao.searchByProdNonZatu(DeliveryResultMrDao.SORT_STRING, plannedProd.getProdCode(), sosCd3, teamSosMst.getSosCd(), insType, category);
			} catch (DataNotFoundException e) {
				// エラーにしない
				deliveryResultMrList = new ArrayList<DeliveryResultMr>();
			}

			// 担当者別計画取得
			List<MrPlan> mrPlanList;
			try {
				mrPlanList = mrPlanDao.searchByTeamCd(MrPlanDao.SORT_STRING, teamSosMst.getSosCd(), plannedProd.getProdCode(), category);
			} catch (DataNotFoundException e) {
				// エラーにしない
				mrPlanList = new ArrayList<MrPlan>();
			}

			// 担当者別計画非公開の場合は、営業所案、決定案をnull
			if(!isMrPlanOpened){
				for (MrPlan mrPlan : mrPlanList) {
					mrPlan.setOfficeValueUhY(null);
					mrPlan.setOfficeValuePY(null);
					mrPlan.setPlannedValueUhY(null);
					mrPlan.setPlannedValuePY(null);
				}
			}

			// 担当者別計画情報DTOを作成
			Collections.sort(deliveryResultMrList, DeliveryResultMrComparator.getInstance());
			Collections.sort(mrPlanList, MrPlanComparator.getInstance());

			List<MrPlanDto> mrPlanDtoList = new ArrayList<MrPlanDto>();
			for (JgiMst jgiMst : jgiMstList) {

				DeliveryResultMr targetResultMr;
				if (insType != null) {
					// 施設出力区分を指定
					targetResultMr = getTargetResultMr(deliveryResultMrList, jgiMst.getJgiNo(), insType, plannedProd.getProdCode());
				} else {
					// UH・P合計
					targetResultMr = getTargetResultMr(deliveryResultMrList, jgiMst.getJgiNo(), plannedProd.getProdCode());
				}

				// 取得済みのリストから担当者別を検索
				MrPlan targetMrPlan = new MrPlan();
				targetMrPlan.setJgiNo(jgiMst.getJgiNo());
				targetMrPlan.setProdCode(plannedProd.getProdCode());

				int indexM = Collections.binarySearch(mrPlanList, targetMrPlan, MrPlanComparator.getInstance());
				if (indexM >= 0) {
					targetMrPlan = mrPlanList.get(indexM);
				}

				// 担当者別計画DTOを作成し、リストに追加
				MrPlanDto mrPlanDto = new MrPlanDto(insType, jgiMst, plannedProd, targetResultMr.getMonNnu(), targetMrPlan);
				mrPlanDtoList.add(mrPlanDto);
			}

			// ----------------------
			// チーム内担当者サマリ情報取得
			// ----------------------
			// チーム内担当者の納入実績サマリ、担当者別計画サマリ取得
			DeliveryResultSosSummaryDto resultTeamSum = null;
			MrPlanSosSummaryDto mrPlanTeamSummary = null;
			try {
				resultTeamSum = deliveryResultMrDao.searchTeamSummary(plannedProd.getProdCode(), teamSosMst.getSosCd(), insType, category);
				mrPlanTeamSummary = mrPlanDao.searchTeamSummary(plannedProd.getProdCode(), teamSosMst.getSosCd());
			} catch (DataNotFoundException e) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("チーム内担当者の納入実績サマリまたは担当者別計画サマリの取得に失敗：" + teamSosMst.getSosCd());
				}
				resultTeamSum = new DeliveryResultSosSummaryDto(teamSosMst.getSosCd(), plannedProd.getProdCode(), insType, new MonNnu());
				mrPlanTeamSummary = new MrPlanSosSummaryDto(teamSosMst.getSosCd(), new MrPlan());
			}

			// 担当者別計画非公開の場合は、営業所案、決定案をnull
			if(!isMrPlanOpened){
				MrPlan mrPlan = mrPlanTeamSummary.convertMrPlan();
				mrPlan.setOfficeValueUhY(null);
				mrPlan.setOfficeValuePY(null);
				mrPlan.setPlannedValueUhY(null);
				mrPlan.setPlannedValuePY(null);
				mrPlanTeamSummary = new MrPlanSosSummaryDto(teamSosMst.getSosCd(), mrPlan);
			}
			MrPlanDto totalMrPlanDto = new MrPlanDto(insType, resultTeamSum.getMonNnu(), mrPlanTeamSummary);



			// ----------------------
			// チーム別計画情報作成
			// ----------------------
			TeamPlan teamPlan;
			try {
				teamPlan = teamPlanDao.searchUk(teamSosMst.getSosCd(), plannedProd.getProdCode());
			} catch (DataNotFoundException e) {
				// エラーにしない
				teamPlan = new TeamPlan();
			}
			TeamPlanDto teamPlanDto = new TeamPlanDto(insType, teamSosMst, plannedProd, resultTeamSum.getMonNnu(), teamPlan, mrPlanDtoList, totalMrPlanDto);

			// 担当者別計画　チーム別計画情報を作成し、リストに追加
			mrPlanTeamInfoList.add(teamPlanDto);

			if (insType != null) {
				// 調整金額の有無チェック
				Long teamPlanValue = 0L;
				Long mrPlanSumValue = 0L;
				if (insType.equals(InsType.UH)) {
					if (teamPlan.getPlannedValuePY() != null) {
						teamPlanValue = teamPlan.getPlannedValuePY();
					}
					if (mrPlanTeamSummary.getPlannedValuePY() != null) {
						mrPlanSumValue = mrPlanTeamSummary.getPlannedValuePY();
					}
				} else {
					if (teamPlan.getPlannedValueUhY() != null) {
						teamPlanValue = teamPlan.getPlannedValueUhY();
					}
					if (mrPlanTeamSummary.getPlannedValueUhY() != null) {
						mrPlanSumValue = mrPlanTeamSummary.getPlannedValueUhY();
					}
				}
				if (!teamPlanValue.equals(mrPlanSumValue)) {
					choseiFlg = true;
				}
			}
		}

		// ----------------------
		// 営業所内チームサマリ情報取得
		// ----------------------
		// 営業所内チームの納入実績サマリ、担当者別計画サマリ取得
		DeliveryResultSosSummaryDto resultTeamSum = null;
		TeamPlanSosSummaryDto teamPlanTeamSummary = null;
		try {
			resultTeamSum = deliveryResultMrDao.searchSosSummary(plannedProd.getProdCode(), sosCd3, false, insType, category);
			teamPlanTeamSummary = teamPlanDao.searchSosSummary(plannedProd.getProdCode(), sosCd3);
		} catch (DataNotFoundException e) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("営業所内チームの納入実績サマリまたは担当者別計画サマリの取得に失敗：" + sosCd3);
			}
			resultTeamSum = new DeliveryResultSosSummaryDto(sosCd3, plannedProd.getProdCode(), insType, new MonNnu());
			teamPlanTeamSummary = new TeamPlanSosSummaryDto(sosCd3, new TeamPlan());
		}
		TeamPlanDto totalTeamPlanDto = new TeamPlanDto(insType, resultTeamSum.getMonNnu(), teamPlanTeamSummary);

		// ----------------------
		// 営業所別計画情報作成
		// ----------------------
		OfficePlan officePlan = null;
		try {
			officePlan = officePlanDao.searchUk(sosCd3, plannedProd.getProdCode());
		} catch (DataNotFoundException e) {
			final String errMsg = "営業所計画の取得に失敗：";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + sosCd3), e);
		}

		OfficePlanDto officePlanDto = new OfficePlanDto(insType, resultTeamSum.getMonNnu(), officePlan, teamPlanTeamSummary, mrPlanTeamInfoList, totalTeamPlanDto, choseiFlg,
			deliveryResultSummaryDtoList, true);

		return officePlanDto;
	}

	/**
	 * 営業所以下の各計画(営業所計画、担当者別計画)を取得する。<br>
	 * 営業所配下にチームが存在しない（営業所直下MR）場合。
	 * チーム別の情報は担当者情報の積上げを利用する。
	 *
	 * @param insType 施設出力対象区分(UH,P以外の場合は合計)
	 * @param prodCode 品目固定コード
	 * @param sosCd3 営業所組織コード
	 * @param officeSosMst 営業所情報
	 */
	public OfficePlanDto execute(InsType insType, PlannedProd plannedProd, String sosCd3, SosMst officeSosMst, String category) {

		// 調整金額有無フラグ
		boolean choseiFlg = false;

		// ----------------------
		// 担当者別計画公開確認
		// ----------------------
		boolean isMrPlanOpened = true;

		// AL、かつ担当者別計画が公開されていない場合は、担当者別計画を非表示にする
		DpUserInfo dpUserInfo = DpUserInfo.getDpUserInfo();
		DpUser dpUser = dpUserInfo.getSettingUser();
		if (dpUser != null && dpUser.isMatch(JokenSet.IYAKU_AL)) {
			if(	(mrPlanStatus != StatusForMrPlan.MR_PLAN_OPENED ) &&
				(mrPlanStatus != StatusForMrPlan.MR_PLAN_INPUT_FINISHED) &&
				(mrPlanStatus != StatusForMrPlan.MR_PLAN_COMPLETED )){
				isMrPlanOpened = false;
			}
		}

		// ----------------------
		// 担当者別計画情報作成
		// ----------------------
		// 従業員一覧取得
		List<JgiMst> jgiMstList = null;
		try {
			//add Start 2022/7/13 H.Futagami 2022年4月組織変更対応 検索条件にカテゴリ追加（結合条件：職種コード）
			//jgiMstList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd3, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G);
			jgiMstList = jgiMstDAO.searchBySosCdCategory(JgiMstDAO.SORT_STRING, sosCd3, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G, plannedProd.getCategory());
			//add End 2022/7/13 H.Futagami 2022年4月組織変更対応 検索条件にカテゴリ追加（結合条件：職種コード）
		} catch (DataNotFoundException e) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("営業所配下の従業員が存在しない：" + sosCd3);
			}
			jgiMstList = new ArrayList<JgiMst>();
		}

		// 納入実績取得
		List<DeliveryResultMr> deliveryResultMrList;
		try {
			deliveryResultMrList = deliveryResultMrDao.searchByProdNonZatu(DeliveryResultMrDao.SORT_STRING, plannedProd.getProdCode(), sosCd3, null, insType, category);
		} catch (DataNotFoundException e) {
			// エラーにしない
			deliveryResultMrList = new ArrayList<DeliveryResultMr>();
		}

		// 担当者別計画取得
		List<MrPlan> mrPlanList;
		try {
			mrPlanList = mrPlanDao.searchBySosCd(MrPlanDao.SORT_STRING, sosCd3, plannedProd.getProdCode(), category);
		} catch (DataNotFoundException e) {
			// エラーにしない
			mrPlanList = new ArrayList<MrPlan>();
		}

		// 担当者別計画非公開の場合は、営業所案、決定案をnull
		if(!isMrPlanOpened){
			for (MrPlan mrPlan : mrPlanList) {
				mrPlan.setOfficeValueUhY(null);
				mrPlan.setOfficeValuePY(null);
				mrPlan.setPlannedValueUhY(null);
				mrPlan.setPlannedValuePY(null);
			}
		}

		// 担当者別計画情報DTOを作成
		Collections.sort(deliveryResultMrList, DeliveryResultMrComparator.getInstance());
		Collections.sort(mrPlanList, MrPlanComparator.getInstance());

		List<MrPlanDto> mrPlanDtoList = new ArrayList<MrPlanDto>();
		for (JgiMst jgiMst : jgiMstList) {

			DeliveryResultMr targetResultMr;
			if (insType != null) {
				// 施設出力区分を指定
				targetResultMr = getTargetResultMr(deliveryResultMrList, jgiMst.getJgiNo(), insType, plannedProd.getProdCode());
			} else {
				// UH・P合計
				targetResultMr = getTargetResultMr(deliveryResultMrList, jgiMst.getJgiNo(), plannedProd.getProdCode());
			}

			// 取得済みのリストから担当者別を検索
			MrPlan targetMrPlan = new MrPlan();
			targetMrPlan.setJgiNo(jgiMst.getJgiNo());
			targetMrPlan.setProdCode(plannedProd.getProdCode());

			int indexM = Collections.binarySearch(mrPlanList, targetMrPlan, MrPlanComparator.getInstance());
			if (indexM >= 0) {
				targetMrPlan = mrPlanList.get(indexM);
			}

			// 担当者別計画DTOを作成し、リストに追加
			MrPlanDto mrPlanDto = new MrPlanDto(insType, jgiMst, plannedProd, targetResultMr.getMonNnu(), targetMrPlan);
			mrPlanDtoList.add(mrPlanDto);
		}

		// ----------------------
		// 営業所内担当者サマリ情報取得
		// ----------------------
		// 営業所内担当者の納入実績サマリ、担当者別計画サマリ取得
		DeliveryResultSosSummaryDto resultTeamSum = null;
		MrPlanSosSummaryDto mrPlanTeamSummary = null;
		try {
			resultTeamSum = deliveryResultMrDao.searchSosSummary(plannedProd.getProdCode(), sosCd3, false, insType, category);
			mrPlanTeamSummary = mrPlanDao.searchSosSummary(plannedProd.getProdCode(),sosCd3);
		} catch (DataNotFoundException e) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("営業所内担当者の納入実績サマリまたは担当者別計画サマリの取得に失敗：" + sosCd3);
			}
			resultTeamSum = new DeliveryResultSosSummaryDto(sosCd3, plannedProd.getProdCode(), insType, new MonNnu());
			mrPlanTeamSummary = new MrPlanSosSummaryDto(sosCd3, new MrPlan());
		}

		// 担当者別計画非公開の場合は、営業所案、決定案をnull
		if(!isMrPlanOpened){
			MrPlan mrPlan = mrPlanTeamSummary.convertMrPlan();
			mrPlan.setOfficeValueUhY(null);
			mrPlan.setOfficeValuePY(null);
			mrPlan.setPlannedValueUhY(null);
			mrPlan.setPlannedValuePY(null);
			mrPlanTeamSummary = new MrPlanSosSummaryDto(sosCd3, mrPlan);
		}
		MrPlanDto totalMrPlanDto = new MrPlanDto(insType, resultTeamSum.getMonNnu(), mrPlanTeamSummary);

		// ----------------------
		// チーム別計画情報作成（担当者サマリから複製）
		// ----------------------
		TeamPlan teamPlan = new TeamPlan();
		teamPlan.setSosCd(mrPlanTeamSummary.getSosCde());
		teamPlan.setProdCode(mrPlanTeamSummary.getProdCode());
		teamPlan.setSpecialInsPlanValueUhY(mrPlanTeamSummary.getSpecialInsPlanValueUhY());
		teamPlan.setTheoreticalValueUh1(mrPlanTeamSummary.getTheoreticalValueUh1());
		teamPlan.setTheoreticalValueUh2(mrPlanTeamSummary.getTheoreticalValueUh2());
		teamPlan.setOfficeValueUhY(mrPlanTeamSummary.getOfficeValueUhY());
		teamPlan.setPlannedValueUhY(mrPlanTeamSummary.getPlannedValueUhY());
		teamPlan.setSpecialInsPlanValuePY(mrPlanTeamSummary.getSpecialInsPlanValuePY());
		teamPlan.setTheoreticalValueP1(mrPlanTeamSummary.getTheoreticalValueP1());
		teamPlan.setTheoreticalValueP2(mrPlanTeamSummary.getTheoreticalValueP2());
		teamPlan.setOfficeValuePY(mrPlanTeamSummary.getOfficeValuePY());
		teamPlan.setPlannedValuePY(mrPlanTeamSummary.getPlannedValuePY());
		TeamPlanDto teamPlanDto = new TeamPlanDto(insType, officeSosMst, plannedProd, resultTeamSum.getMonNnu(), teamPlan, mrPlanDtoList, totalMrPlanDto);

		// 担当者別計画　チーム別計画情報を作成し、リストに追加
		List<TeamPlanDto> mrPlanTeamInfoList = new ArrayList<TeamPlanDto>();
		mrPlanTeamInfoList.add(teamPlanDto);

		if (insType != null) {
			// 調整金額の有無チェック
			Long teamPlanValue = 0L;
			Long mrPlanSumValue = 0L;
			if (insType.equals(InsType.UH)) {
				if (teamPlan.getPlannedValuePY() != null) {
					teamPlanValue = teamPlan.getPlannedValuePY();
				}
				if (mrPlanTeamSummary.getPlannedValuePY() != null) {
					mrPlanSumValue = mrPlanTeamSummary.getPlannedValuePY();
				}
			} else {
				if (teamPlan.getPlannedValueUhY() != null) {
					teamPlanValue = teamPlan.getPlannedValueUhY();
				}
				if (mrPlanTeamSummary.getPlannedValueUhY() != null) {
					mrPlanSumValue = mrPlanTeamSummary.getPlannedValueUhY();
				}
			}
			if (!teamPlanValue.equals(mrPlanSumValue)) {
				choseiFlg = true;
			}
		}

		// ----------------------
		// 営業所内チームサマリ情報取得（チームサマリから複製）
		// ----------------------
		TeamPlanSosSummaryDto teamPlanTeamSummary = new TeamPlanSosSummaryDto(sosCd3, teamPlan);
		TeamPlanDto totalTeamPlanDto = new TeamPlanDto(insType, resultTeamSum.getMonNnu(), teamPlanTeamSummary);

		// ----------------------
		// 営業所別計画情報作成
		// ----------------------
		OfficePlan officePlan = null;
		try {
			officePlan = officePlanDao.searchUk(sosCd3, plannedProd.getProdCode());
		} catch (DataNotFoundException e) {
			final String errMsg = "営業所計画の取得に失敗：";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + sosCd3), e);
		}

		OfficePlanDto officePlanDto = new OfficePlanDto(insType, resultTeamSum.getMonNnu(), officePlan, teamPlanTeamSummary, mrPlanTeamInfoList, totalTeamPlanDto, choseiFlg,
			deliveryResultSummaryDtoList,false);

		return officePlanDto;
	}

	/**
	 * DBから取得済みの納入実績リストから、施設出力区分を指定して、担当者の納入実績を取得する。
	 *
	 * @param deliveryResultMrList 納入実績リスト
	 * @param jgiNo 従業員番号
	 * @param insType 施設出力区分
	 * @param prodCode 品目固定コード
	 * @return 担当者の納入実績
	 */
	private DeliveryResultMr getTargetResultMr(List<DeliveryResultMr> deliveryResultMrList, Integer jgiNo, InsType insType, String prodCode) {

		// 取得済みのリストから納入実績を検索
		DeliveryResultMr targetResultMr = new DeliveryResultMr();
		targetResultMr.setJgiNo(jgiNo);
		targetResultMr.setInsType(insType);
		targetResultMr.setProdCode(prodCode);

		int indexD = Collections.binarySearch(deliveryResultMrList, targetResultMr, DeliveryResultMrComparator.getInstance());
		if (indexD >= 0) {
			targetResultMr = deliveryResultMrList.get(indexD);
		}

		return targetResultMr;
	}

	/**
	 * DBから取得済みの納入実績リストから、UH・Pを合計した担当者の納入実績を取得する。
	 *
	 * @param deliveryResultMrList 納入実績リスト
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @return 担当者の納入実績
	 */
	private DeliveryResultMr getTargetResultMr(List<DeliveryResultMr> deliveryResultMrList, Integer jgiNo, String prodCode) {

		// UH
		DeliveryResultMr targetResultMrUh = getTargetResultMr(deliveryResultMrList, jgiNo, InsType.UH, prodCode);
		MonNnu monNnuUh = targetResultMrUh.getMonNnu();
		if (monNnuUh == null) {
			monNnuUh = new MonNnu();
		}

		// P
		DeliveryResultMr targetResultMrP = getTargetResultMr(deliveryResultMrList, jgiNo, InsType.P, prodCode);
		MonNnu monNnuP = targetResultMrP.getMonNnu();
		if (monNnuP == null) {
			monNnuP = new MonNnu();
		}

		// 合計
		MonNnu monNnu = new MonNnu();
		monNnu.setPreFarAdvancePeriod(MathUtil.add(monNnuUh.getPreFarAdvancePeriod(), monNnuP.getPreFarAdvancePeriod()));
		monNnu.setFarAdvancePeriod(MathUtil.add(monNnuUh.getFarAdvancePeriod(), monNnuP.getFarAdvancePeriod()));
		monNnu.setAdvancePeriod(MathUtil.add(monNnuUh.getAdvancePeriod(), monNnuP.getAdvancePeriod()));
		monNnu.setCurrentPeriod(MathUtil.add(monNnuUh.getCurrentPeriod(), monNnuP.getCurrentPeriod()));
		monNnu.setCurrentPlanValue(MathUtil.add(monNnuUh.getCurrentPlanValue(), monNnuP.getCurrentPlanValue()));

		DeliveryResultMr targetResultMr = new DeliveryResultMr();
		targetResultMr.setJgiNo(jgiNo);
		targetResultMr.setProdCode(prodCode);
		targetResultMr.setMonNnu(monNnu);

		return targetResultMr;
	}
}
