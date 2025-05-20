package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.DATA_NOT_FOUND_ERROR;

import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.MrPlanForVacDao;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.MrPlanForVacResultDto;
import jp.co.takeda.dto.MrPlanResultValueDto;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MrPlanForVac;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.view.MonNnuSummary;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.MathUtil;

/**
 * ワクチン用計画対象品目選択画面の検索結果用DTOのリストを作成するロジッククラス
 *
 * @author khashimoto
 */
public class CreateMrPlanForVacResultDtoListLogic {

	/**
	 * 組織情報取得DAO
	 */
	private final SosMstDAO sosMstDAO;

	/**
	 * 従業員情報取得DAO
	 */
	private final JgiMstDAO jgiMstDAO;

	/**
	 * ワクチン用担当者別計画取得DAO
	 */
	private final MrPlanForVacDao mrPlanForVacDao;

	/**
	 * 計画対象品目
	 */
	private final PlannedProd plannedProd;

	/**
	 * コンストラクタ
	 *
	 * @param sosMstDAO 組織情報取得DAO
	 * @param jgiMstDAO 従業員情報取得DAO
	 * @param mrPlanForVacDao ワクチン用担当者別計画取得DAO
	 * @param plannedProd 計画対象品目
	 */
	public CreateMrPlanForVacResultDtoListLogic(SosMstDAO sosMstDAO, JgiMstDAO jgiMstDAO, MrPlanForVacDao mrPlanForVacDao, PlannedProd plannedProd) {
		this.sosMstDAO = sosMstDAO;
		this.jgiMstDAO = jgiMstDAO;
		this.mrPlanForVacDao = mrPlanForVacDao;
		this.plannedProd = plannedProd;
	}

	/**
	 * DTOリストを生成する。
	 *
	 * @return ワクチン用計画対象品目選択画面の検索結果用DTOのリスト
	 * @throws LogicalException
	 */
	public List<MrPlanForVacResultDto> execute() throws LogicalException {

		DpUser dpUser = DpUserInfo.getDpUserInfo().getSettingUser();
		String prodCode = plannedProd.getProdCode();

		List<MrPlanForVacResultDto> resultList = new ArrayList<MrPlanForVacResultDto>();

		// ---------------------------------------
		// 本部、本部ワクチンＧの場合
		// ---------------------------------------
		if (dpUser.isMatch(JokenSet.HONBU, JokenSet.HONBU_WAKUTIN_G)) {

			// エリア特約店G一覧取得
			List<SosMst> tokuGSosMstList = new ArrayList<SosMst>();
			try {
				tokuGSosMstList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.TOKUYAKUTEN_LIST, BumonRank.SITEN_TOKUYAKUTEN_BU, null);
			} catch (DataNotFoundException e) {
				final String errMsg = "エリア特約店G情報が存在しない：";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
			}

			// 担当者別計画取得
			List<MrPlanForVac> mrPlanList = new ArrayList<MrPlanForVac>();
			try {
				mrPlanList = mrPlanForVacDao.searchList(MrPlanForVacDao.SORT_STRING3, null, null, null, prodCode);
			} catch (DataNotFoundException e) {
				final String errMsg = "担当者別計画の取得に失敗。prodCode=" + prodCode;
				throw new LogicalException(new Conveyance(new MessageKey("DPS1003E", "担当者別計画"), errMsg));
			}

			int planIndex = 0;

			// エリア特約店G単位で繰り返し
			// 全サマリ
			MrPlanForVac mrPlanAllSum = getInitMrPlanForVac();
			for (SosMst sosMst : tokuGSosMstList) {
				// 組織コード
				String sosCd = sosMst.getSosCd();
				// 組織名
				String sosName = sosMst.getBumonSeiName();
				// 組織単位のサマリ
				MrPlanForVac mrPlanSosSum = getInitMrPlanForVac();

				// MRリスト取得
				List<JgiMst> mrList = new ArrayList<JgiMst>();
				try {
					mrList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd, SosListType.TOKUYAKUTEN_LIST, sosMst.getBumonRank());
				} catch (DataNotFoundException e) {
					// エラーにしない
				}

				// 小児科ＭＲのいない組織は省略 (J19-0010 対応・コメントのみ修正)
				if (mrList.isEmpty()) {
					continue;
				}

				// MR行生成
				// MR単位に実行
				boolean sosSumFlg = false;
				for (JgiMst jgiMst : mrList) {

					// チーム名
					String teamName = null;
					if (!(jgiMst.getSosCd4().equals(JgiMst.BLANK_SOS_CD))) {
						try {
							SosMst team = sosMstDAO.search(jgiMst.getSosCd4());
							teamName = team.getBumonSeiName();
						} catch (DataNotFoundException e) {
							// エラーにしない
						}
					}

					// 従業員番号
					Integer jgiNo = jgiMst.getJgiNo();
					// 従業員名
					String jgiName = jgiMst.getJgiName();
					// 担当者別計画
					MrPlanForVac mrPlan = null;
					// 実績値
					MonNnuSummary jisseki = null;
					// 担当者別計画
					if (planIndex < mrPlanList.size()) {
						MrPlanForVac mrPlanForVac = mrPlanList.get(planIndex);
						if (jgiNo.equals(mrPlanForVac.getJgiNo()) && prodCode.equals(mrPlanForVac.getProdCode())) {
							mrPlan = mrPlanForVac;
							jisseki = mrPlanForVac.getMonNnuSummary();
							planIndex++;
							sosSumFlg = true;
						}
					}
					if (mrPlan == null) {
						continue;
					}

					// 従業員行生成
					resultList.add(new MrPlanForVacResultDto(sosName, teamName, jgiName, mrPlan, new MrPlanResultValueDto(jisseki), false, false, false));

					// 組織サマリ
					mrPlanSosSum = add(mrPlanSosSum, mrPlan);
				}

				// 組織合計行生成
				if (mrList.isEmpty() || !sosSumFlg) {
					continue;
				}

				// 組織合計行生成
				resultList.add(new MrPlanForVacResultDto(sosName, null, null, mrPlanSosSum, new MrPlanResultValueDto(mrPlanSosSum.getMonNnuSummary()), false, true, false));

				// 全サマリ
				mrPlanAllSum = add(mrPlanAllSum, mrPlanSosSum);
			}
			// 全合計行生成
			// 全合計行生成(先頭行に挿入)
			resultList.add(0, new MrPlanForVacResultDto(null, null, null, mrPlanAllSum, new MrPlanResultValueDto(mrPlanAllSum.getMonNnuSummary()), false, false, true));
		}

		// ---------------------------------------
		// 小児科ＡＣの場合 (J19-0010 対応・コメントのみ修正)
		// ---------------------------------------
		else if (dpUser.isMatch(JokenSet.WAKUTIN_AL)) {

			// エリア情報取得
			SosMst tokuGSosMst = null;
			try {
				tokuGSosMst = sosMstDAO.search(dpUser.getSosCd3());
			} catch (DataNotFoundException e) {
				final String errMsg = "エリア情報が存在しない：";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
			}

			String sosName = tokuGSosMst.getBumonSeiName();

			// 組織配下の担当者別計画取得
			List<MrPlanForVac> mrPlanList = new ArrayList<MrPlanForVac>();
			try {
				mrPlanList = mrPlanForVacDao.searchList(MrPlanForVacDao.SORT_STRING3, null, dpUser.getSosCd3(), null, prodCode);
			} catch (DataNotFoundException e) {
				throw new LogicalException(new Conveyance(new MessageKey("DPS1003E", "担当者別計画")));
			}

			// MRリスト取得
			List<JgiMst> mrList = new ArrayList<JgiMst>();
			try {
				mrList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, tokuGSosMst.getSosCd(), SosListType.TOKUYAKUTEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G);
			} catch (DataNotFoundException e) {
				// エラーにしない
			}

			// 組織単位のサマリ
			MrPlanForVac mrPlanSosSum = getInitMrPlanForVac();

			int planIndex = 0;

			// MR行生成
			// MR単位に実行
			for (JgiMst jgiMst : mrList) {

				// 従業員番号
				Integer jgiNo = jgiMst.getJgiNo();
				// 従業員名
				String jgiName = jgiMst.getJgiName();
				// 担当者別計画
				MrPlanForVac mrPlan = null;
				// 実績値
				MonNnuSummary jisseki = null;
				// 担当者別計画
				if (planIndex < mrPlanList.size()) {
					MrPlanForVac mrPlanForVac = mrPlanList.get(planIndex);
					if (jgiNo.equals(mrPlanForVac.getJgiNo()) && prodCode.equals(mrPlanForVac.getProdCode())) {
						mrPlan = mrPlanForVac;
						jisseki = mrPlanForVac.getMonNnuSummary();
						planIndex++;
					}
				}

				if (mrPlan == null) {
					continue;
				}

				// 従業員行生成
				resultList.add(new MrPlanForVacResultDto(sosName, null, jgiName, mrPlan, new MrPlanResultValueDto(jisseki), false, false, false));

				// 組織サマリ
				mrPlanSosSum = add(mrPlanSosSum, mrPlan);
			}

			// 組織合計行生成
			resultList.add(new MrPlanForVacResultDto(sosName, null, null, mrPlanSosSum, new MrPlanResultValueDto(mrPlanSosSum.getMonNnuSummary()), true, false, false));
		}

		// ---------------------------------------
		// 小児科ＭＲの場合 (J19-0010 対応・コメントのみ修正)
		// ---------------------------------------
		else if (dpUser.isMatch(JokenSet.WAKUTIN_MR)) {

			// 従業員番号
			Integer jgiNo = dpUser.getJgiNo();

			// 従業員名
			String jgiName = dpUser.getJgiName();

			// 組織名
			String sosName = dpUser.getBumonSeiName();

			// チーム名
			String teamName = null;

			if (!(dpUser.getSosCd4().equals(JgiMst.BLANK_SOS_CD))) {
				try {
					SosMst tokuyakutenG = sosMstDAO.search(dpUser.getSosCd3());
					teamName = sosName;
					sosName = tokuyakutenG.getBumonSeiName();

				} catch (DataNotFoundException e) {
					// エラーにしない
				}
			}

			// 担当者別計画取得
			MrPlanForVac mrPlan = new MrPlanForVac();
			try {
				mrPlan = mrPlanForVacDao.searchUk(jgiNo, prodCode);
			} catch (DataNotFoundException e) {
				throw new LogicalException(new Conveyance(new MessageKey("DPS1003E", "担当者別計画")));
			}

			// MR行生成
			// 実績値
			MonNnuSummary jisseki = mrPlan.getMonNnuSummary();

			// 従業員行生成
			resultList.add(new MrPlanForVacResultDto(sosName, teamName, jgiName, mrPlan, new MrPlanResultValueDto(jisseki), false, false, false));
		}

		return resultList;
	}

	/**
	 * 各数値を0で初期化した担当者別計画を取得する。
	 *
	 * @return 担当者別計画
	 */
	private MrPlanForVac getInitMrPlanForVac() {
		MrPlanForVac mrPlanForVac = new MrPlanForVac();
		mrPlanForVac.setPlannedValueB(0L);
		MonNnuSummary monNnuSummary = new MonNnuSummary();
		monNnuSummary.setPreFarAdvancePeriod(0L);
		monNnuSummary.setFarAdvancePeriod(0L);
		monNnuSummary.setAdvancePeriod(0L);
		monNnuSummary.setCurrentPeriod(0L);
		monNnuSummary.setCurrentPlanValue(0L);
		mrPlanForVac.setMonNnuSummary(monNnuSummary);
		return mrPlanForVac;
	}

	/**
	 * 担当者別計画の加算を行う。
	 *
	 * @param mrPlanForVac1 担当者別計画
	 * @param mrPlanForVac2 担当者別計画
	 * @return 担当者別計画
	 */
	private MrPlanForVac add(MrPlanForVac mrPlanForVac1, MrPlanForVac mrPlanForVac2) {
		Long plannedValueB = MathUtil.add(mrPlanForVac1.getPlannedValueB(), mrPlanForVac2.getPlannedValueB());
		MonNnuSummary monNnuSummary = new MonNnuSummary();
		monNnuSummary.setPreFarAdvancePeriod(MathUtil.add(mrPlanForVac1.getMonNnuSummary().getPreFarAdvancePeriod(), mrPlanForVac2.getMonNnuSummary().getPreFarAdvancePeriod()));
		monNnuSummary.setFarAdvancePeriod(MathUtil.add(mrPlanForVac1.getMonNnuSummary().getFarAdvancePeriod(), mrPlanForVac2.getMonNnuSummary().getFarAdvancePeriod()));
		monNnuSummary.setAdvancePeriod(MathUtil.add(mrPlanForVac1.getMonNnuSummary().getAdvancePeriod(), mrPlanForVac2.getMonNnuSummary().getAdvancePeriod()));
		monNnuSummary.setCurrentPeriod(MathUtil.add(mrPlanForVac1.getMonNnuSummary().getCurrentPeriod(), mrPlanForVac2.getMonNnuSummary().getCurrentPeriod()));
		monNnuSummary.setCurrentPlanValue(MathUtil.add(mrPlanForVac1.getMonNnuSummary().getCurrentPlanValue(), mrPlanForVac2.getMonNnuSummary().getCurrentPlanValue()));
		MrPlanForVac mrPlanForVac = new MrPlanForVac();
		mrPlanForVac.setPlannedValueB(plannedValueB);
		mrPlanForVac.setMonNnuSummary(monNnuSummary);
		return mrPlanForVac;
	}
}
