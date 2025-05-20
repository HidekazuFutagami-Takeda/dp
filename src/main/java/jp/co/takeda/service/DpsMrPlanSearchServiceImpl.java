package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DeliveryResultMrDao;
import jp.co.takeda.dao.DpsCodeMasterDao;
import jp.co.takeda.dao.EstParamHonbuDao;
import jp.co.takeda.dao.EstParamOfficeDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.MrEstimationRatioDao;
import jp.co.takeda.dao.MrPlanDao;
import jp.co.takeda.dao.MrPlanStatusDao;
import jp.co.takeda.dao.OfficePlanDao;
import jp.co.takeda.dao.OfficePlanStatusDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.TeamInputStatusDao;
import jp.co.takeda.dao.TeamPlanDao;
import jp.co.takeda.dao.div.RefDeliveryScope;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.DeliveryResultSummaryDto;
import jp.co.takeda.dto.EstimationIndexTotalDto;
import jp.co.takeda.dto.EstimationResultDetailResultDto;
import jp.co.takeda.dto.EstimationResultDetailScDto;
import jp.co.takeda.dto.MrPlanResultDto;
import jp.co.takeda.dto.MrPlanSosSummaryDto;
import jp.co.takeda.dto.OfficePlanDto;
import jp.co.takeda.dto.OfficePlanStatusResultDto;
import jp.co.takeda.dto.OfficeTeamPlanChoseiDto;
import jp.co.takeda.dto.PlannedProdResultListDto;
import jp.co.takeda.dto.PlannedProdScDto;
import jp.co.takeda.dto.TeamPlanDto;
import jp.co.takeda.dto.TeamPlanSosSummaryDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.CreateMrPlanOfficeInfoLogic;
import jp.co.takeda.logic.EstimationIndexTotalDtoCreateLogic;
import jp.co.takeda.logic.EstimationResultDetailResultDtoCreateLogic;
import jp.co.takeda.logic.PlannedProdResultDtoCreateLogic;
import jp.co.takeda.logic.div.MrStatusForCheck;
import jp.co.takeda.model.BaseParam;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.EstParamHonbu;
import jp.co.takeda.model.EstParamOffice;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.model.MrPlanStatus;
import jp.co.takeda.model.OfficePlan;
import jp.co.takeda.model.OfficePlanStatus;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.TeamPlan;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.CalcType;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.RefPeriod;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.div.StatusForMrPlan;
import jp.co.takeda.model.view.EstimationRatio;
import jp.co.takeda.model.view.MrEstimationRatio;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.MathUtil;

/**
 * 担当者別計画機能の検索に関するサービス実装クラス
 *
 * @author stakeuchi
 */
@Transactional
@Service("dpsMrPlanSearchService")
public class DpsMrPlanSearchServiceImpl implements DpsMrPlanSearchService {

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * 従業員情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;

	/**
	 * 営業所別計画ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("officePlanStatusDao")
	protected OfficePlanStatusDao officePlanStatusDao;

	/**
	 * 担当者別計画ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("mrPlanStatusDao")
	protected MrPlanStatusDao mrPlanStatusDao;

	/**
	 * チーム別入力状況DAO
	 */
	@Autowired(required = true)
	@Qualifier("teamInputStatusDao")
	protected TeamInputStatusDao teamInputStatusDao;

	/**
	 * 営業所計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("officePlanDao")
	protected OfficePlanDao officePlanDao;

	/**
	 * チーム計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("teamPlanDao")
	protected TeamPlanDao teamPlanDao;

	/**
	 * 担当者計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("mrPlanDao")
	protected MrPlanDao mrPlanDao;

	/**
	 * 担当者別納入実績DAO
	 */
	@Autowired(required = true)
	@Qualifier("deliveryResultMrDao")
	protected DeliveryResultMrDao deliveryResultMrDao;

	/**
	 * 試算パラメータ（本部）DAO
	 */
	@Autowired(required = true)
	@Qualifier("estParamHonbuDao")
	protected EstParamHonbuDao estParamHonbuDao;

	/**
	 * 試算パラメータ（営業所）DAO
	 */
	@Autowired(required = true)
	@Qualifier("estParamOfficeDao")
	protected EstParamOfficeDao estParamOfficeDao;

	/**
	 * 担当者別計画立案ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsMrStatusCheckService")
	protected DpsMrStatusCheckService dpsMrStatusCheckService;

	/**
	 * 担当者別試算構成比取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("mrEstimationRatioDao")
	protected MrEstimationRatioDao mrEstimationRatioDao;

	/**
	 * 過去実績参照(担当者別計画モード)の検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsDeliverySummarySearchService")
	protected DpsDeliverySummarySearchService dpsDeliverySummarySearchService;

	/**
	 * 計画支援汎用マスタDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterDao")
	protected DpsCodeMasterDao dpsCodeMasterDao;

	public OfficePlanStatusResultDto searchOfficePlanStatus(String sosCd3, String category) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 組織情報
		// ----------------------
		SosMst sosMst;
		try {
			sosMst = sosMstDAO.search(sosCd3);

		} catch (DataNotFoundException e) {
			final String errMsg = "対象営業所が取得できない：" + sosCd3;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ------------------------------
		// 営業所計画ステータス取得
		// ------------------------------
		OfficePlanStatus officePlanStatus = null;
		try {
			officePlanStatus = officePlanStatusDao.search(sosCd3, category);
		} catch (DataNotFoundException e) {
			// エラーとしない
		}

		// ResultDtoを生成
		if (officePlanStatus != null && officePlanStatus.getStatusForOffice() != null) {
			boolean isDraft = false;
			boolean isCompleted = false;
			switch (officePlanStatus.getStatusForOffice()) {
				case DRAFT:
					isDraft = true;
					break;
				case COMPLETED:
					isCompleted = true;
					break;
				default:
					break;
			}
			Date upDate = officePlanStatus.getUpDate();

			return new OfficePlanStatusResultDto(isDraft, isCompleted, upDate);
		}

		return null;
	}

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBUで計画対象品目が取得できない場合のエラーメッセージの表示変更
//	public PlannedProdResultListDto searchPlannedProdList(PlannedProdScDto scDto) {
	public PlannedProdResultListDto searchPlannedProdList(PlannedProdScDto scDto) throws DataNotFoundException {
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBUで計画対象品目が取得できない場合のエラーメッセージの表示変更

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "検索条件オブジェクトがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final String sosCd3 = scDto.getSosCd3();
		if (sosCd3 == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		//画面で選択したカテゴリを設定
		String category = scDto.getCategory();

		// -----------------------------
		// ワクチンコードの取得
		// -----------------------------
		List<DpsCCdMst> searchList = new ArrayList<DpsCCdMst>();
		try {
			// カテゴリの検索
			searchList = dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.VAC.getDbValue());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + DpsCodeMaster.VAC.getDbValue() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		String vaccineCode = searchList.get(0).getDataCd();

		// -----------------------------
		// 売上所属の判定
		// -----------------------------
		Sales sales = null;
		if(category.equals(vaccineCode)) {
			sales = Sales.VACCHIN;
		}else{
			sales = Sales.IYAKU;
		}

		// ----------------------
		// 計画対象品目取得
		// ----------------------
		List<PlannedProd> plannedProdList = null;
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBUで計画対象品目が取得できない場合のエラーメッセージの表示変更
//	※えらーをここでキャッチせずそのまま上に投げると「システムエラー」の表示が「データが見つかりません」の表示に変わる
//		try {
			plannedProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING2, sales, category, null);
//		} catch (DataNotFoundException e) {
//			final String errMsg = "計画対象品目が存在しない";
//			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
//		}
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBUで計画対象品目が取得できない場合のエラーメッセージの表示変更

		// ------------------------------
		// 担当者別計画ステータス取得
		// ------------------------------
		List<MrPlanStatus> mrPlanStatusList = null;
		try {
			mrPlanStatusList = mrPlanStatusDao.searchList(scDto.getSosCd3());
		} catch (DataNotFoundException e) {
			// エラーとしない
		}

		// ------------------------------
		// 営業所計画取得
		// ------------------------------
		List<OfficePlan> officePlanList = null;
		try {
			officePlanList = officePlanDao.searchList(OfficePlanDao.SORT_STRING, scDto.getSosCd3(), category, sales);
		} catch (DataNotFoundException e) {
			// エラーとしない
		}

		// ------------------------------
		// 営業所計画ステータス取得
		// ------------------------------
		OfficePlanStatus officePlanStatus = null;
		CalcType calcType = null;
		try {
			officePlanStatus = officePlanStatusDao.search(scDto.getSosCd3(), category);
			calcType = officePlanStatus.getCalcType();
		} catch (DataNotFoundException e) {
			// エラーとしない
		}

		// ------------------------------
		// チーム計画取得
		// ------------------------------
		List<TeamPlan> teamPlanList = new ArrayList<TeamPlan>();
		if (scDto.getSosCd4() != null) {
			for (PlannedProd prod : plannedProdList) {
				try {
					teamPlanList.add(teamPlanDao.searchUk(scDto.getSosCd4(), prod.getProdCode()));
				} catch (DataNotFoundException e) {
					// エラーとしない
				}
			}
		} else {
			for (PlannedProd prod : plannedProdList) {
				try {
					teamPlanList.addAll(teamPlanDao.searchList(null, scDto.getSosCd3(), prod.getProdCode()));
				} catch (DataNotFoundException e) {
					// エラーとしない
				}
			}
		}

		// ------------------------------
		// 担当者計画取得
		// ------------------------------
		List<MrPlan> mrPlanList = new ArrayList<MrPlan>();
		for (PlannedProd prod : plannedProdList) {
			try {
				if (scDto.getJgiNo() == null) {
					if (scDto.getSosCd4() == null) {
						mrPlanList.addAll(mrPlanDao.searchBySosCd(MrPlanDao.SORT_STRING, scDto.getSosCd3(), prod.getProdCode(), category));
					} else {
						mrPlanList.addAll(mrPlanDao.searchByTeamCd(MrPlanDao.SORT_STRING, scDto.getSosCd4(), prod.getProdCode(), category));
					}
				} else {
					mrPlanList.add(mrPlanDao.searchUk(scDto.getJgiNo(), prod.getProdCode()));
				}

			} catch (DataNotFoundException e) {
				// エラーとしない
			}
		}

		// 検索が従業員基準
		if (scDto.getJgiNo() != null) {
			PlannedProdResultDtoCreateLogic logic = new PlannedProdResultDtoCreateLogic(plannedProdList, mrPlanStatusList, officePlanList, teamPlanList, mrPlanList, null,
				teamInputStatusDao, calcType);
			return new PlannedProdResultListDto(logic.createResultDtoList(), false, false, true, calcType);
		}
		// 検索がチーム基準
		if (scDto.getSosCd4() != null) {
			PlannedProdResultDtoCreateLogic logic = new PlannedProdResultDtoCreateLogic(plannedProdList, mrPlanStatusList, officePlanList, teamPlanList, mrPlanList, scDto
				.getSosCd4(), teamInputStatusDao, calcType);
			return new PlannedProdResultListDto(logic.createResultDtoList(), false, true, false, calcType);
		}
		// 上記以外
		PlannedProdResultDtoCreateLogic logic = new PlannedProdResultDtoCreateLogic(plannedProdList, mrPlanStatusList, officePlanList, teamPlanList, mrPlanList, null,
			teamInputStatusDao, calcType);
		return new PlannedProdResultListDto(logic.createResultDtoList(), true, false, false, calcType);
	}

	// 担当者別計画情報取得(営業所指定)
	public MrPlanResultDto searchMrPlan(InsType insType, String prodCode, String sosCd3, RefDeliveryScope scope, String category) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scope == null) {
			final String errMsg = "実績参照範囲がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 計画対象品目取得
		// ----------------------
		PlannedProd plannedProd = null;
		try {
			plannedProd = plannedProdDAO.search(prodCode);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目が存在しない：";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
		}

		// ----------------------
		// 組織情報取得
		// ----------------------
		SosMst officeSosMst = null;
		try {
			officeSosMst = sosMstDAO.search(sosCd3);
		} catch (DataNotFoundException e) {
			final String errMsg = "営業所組織情報が存在しない：";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + sosCd3), e);
		}

		// ----------------------
		// 試算タイプ取得
		// ----------------------
		CalcType calcType = null;
		try {
			OfficePlanStatus officePlanStatus = officePlanStatusDao.search(sosCd3, category);
			calcType = officePlanStatus.getCalcType();
		} catch (DataNotFoundException e) {
		   //データが存在しない場合、試算タイプを1:営業所別計画⇒チーム別計画⇒担当者別計画に設定
			calcType = CalcType.OFFICE_TEAM_MR;
		}

		// ----------------------
		// 担当者別計画ステータスチェック
		// ----------------------

		StatusForMrPlan mrPlanStatus = null;
		try {

			// 許可しないステータスリスト作成
			List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
			unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
			unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
			unallowableMrStatusList.add(MrStatusForCheck.ESTIMATED);
			unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_OPENED);

			// チェック実行
			List<MrPlanStatus> orgMrPlanStatusList = dpsMrStatusCheckService.execute(sosCd3, plannedProd, unallowableMrStatusList);
			mrPlanStatus = orgMrPlanStatusList.get(0).getStatusForMrPlan();

		} catch (UnallowableStatusException e) {

			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3253E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// 営業所配下計画情報取得
		// ----------------------
		OfficePlanDto officePlanDto = null;
		try {

			// (営業所配下のチーム一覧取得)
			List<SosMst> teamSosMstList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G, sosCd3);

			// チームがある場合
			CreateMrPlanOfficeInfoLogic createLogic = new CreateMrPlanOfficeInfoLogic(jgiMstDAO, sosMstDAO, officePlanDao, teamPlanDao, mrPlanDao, deliveryResultMrDao,
					getDeliveryResultSummaryDtoList(insType, scope, prodCode, sosCd3, null, category), mrPlanStatus);
			officePlanDto = createLogic.execute(insType, plannedProd, sosCd3, teamSosMstList);

		} catch (DataNotFoundException e) {
			// チームがない場合
			CreateMrPlanOfficeInfoLogic createLogic = new CreateMrPlanOfficeInfoLogic(jgiMstDAO, sosMstDAO, officePlanDao, teamPlanDao, mrPlanDao, deliveryResultMrDao,
					getDeliveryResultSummaryDtoList(insType, scope, prodCode, sosCd3, null, category), mrPlanStatus);
//			officePlanDto = createLogic.execute(insType, plannedProd, sosCd3, officeSosMst);
			officePlanDto = createLogic.execute(insType, plannedProd, sosCd3, officeSosMst, category);
		}

		// ----------------------
		// 上位計画取得、積み上げ取得
		// ----------------------
		Long previousPlanValueUH = 0L;
		Long previousPlanValueP = 0L;
		Long totalPlanValueUH = 0L;
		Long totalPlanValueP = 0L;
		try {

			// 試算タイプが営→チ→担の場合、チーム別計画を集計
			if (calcType == null || calcType == CalcType.OFFICE_TEAM_MR) {
				// チーム別計画の営業所内合計
				TeamPlanSosSummaryDto teamPlanSosSummaryDto = teamPlanDao.searchSosSummary(plannedProd.getProdCode(), sosCd3);
				previousPlanValueUH = teamPlanSosSummaryDto.getPlannedValueUhY();
				previousPlanValueP = teamPlanSosSummaryDto.getPlannedValuePY();
			}
			// 試算タイプが営→担の場合、営業所計画を表示
			else {
				OfficePlan officePlan = officePlanDao.searchUk(sosCd3, prodCode);
				previousPlanValueUH = officePlan.getPlannedValueUhY();
				previousPlanValueP = officePlan.getPlannedValuePY();
			}

			// 担当者別計画の営業所内合計
			MrPlanSosSummaryDto mrPlanSosSummaryDto = mrPlanDao.searchSosSummary(plannedProd.getProdCode(), sosCd3);
			totalPlanValueUH = mrPlanSosSummaryDto.getPlannedValueUhY();
			totalPlanValueP = mrPlanSosSummaryDto.getPlannedValuePY();
		} catch (DataNotFoundException e) {
			// エラーにしない
		}

		// ----------------------
		// 最新更新日、更新者取得
		// (担当者別計画から営業所内の最新情報を取得)
		// ----------------------
		Date lastUpdate = null;
		String lastUpdateUser = null;
		try {
			MrPlan lastUpdateData = mrPlanDao.getLastUpDateBySos(sosCd3, plannedProd.getProdCode());
			lastUpdate = lastUpdateData.getUpDate();
			lastUpdateUser = lastUpdateData.getUpJgiName();
		} catch (DataNotFoundException e) {
			// エラーにしない
		}

		// ----------------------
		// 結果DTO作成
		// ----------------------
		return new MrPlanResultDto(sosCd3, plannedProd, previousPlanValueUH, previousPlanValueP, totalPlanValueUH, totalPlanValueP, lastUpdate, lastUpdateUser, officePlanDto,
			mrPlanStatus, calcType);
	}

	// 担当者別計画情報取得(チーム指定)
	public MrPlanResultDto searchMrPlan(InsType insType, String prodCode, String sosCd3, String sosCd4, RefDeliveryScope scope) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosCd4 == null) {
			final String errMsg = "組織コード(チーム)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scope == null) {
			final String errMsg = "実績参照範囲がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 計画対象品目取得
		// ----------------------
		PlannedProd plannedProd = null;
		try {
			plannedProd = plannedProdDAO.search(prodCode);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目が存在しない：";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
		}

		// ----------------------
		// チーム情報取得
		// (自チーム取得)
		// ----------------------
		SosMst sosMst = sosMstDAO.search(sosCd4);
		List<SosMst> teamSosMstList = new ArrayList<SosMst>();
		teamSosMstList.add(sosMst);

		// ----------------------
		// 試算タイプ取得
		// ※ONC組織はこのルートがないため、MMP品固定
		// ----------------------
		CalcType calcType = null;
		try {
//			OfficePlanStatus officePlanStatus = officePlanStatusDao.search(sosCd3, ProdCategory.MMP);
			OfficePlanStatus officePlanStatus = officePlanStatusDao.search(sosCd3, plannedProd.getCategory());
			calcType = officePlanStatus.getCalcType();
		} catch (DataNotFoundException e) {
		}

		// ----------------------
		// 担当者別計画ステータスチェック
		// ----------------------
		StatusForMrPlan mrPlanStatus = null;
		try {

			// 許可しないステータスリスト作成
			List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
			unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
			unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
			unallowableMrStatusList.add(MrStatusForCheck.ESTIMATED);
			unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_OPENED);

			// チェック実行
			List<MrPlanStatus> orgMrPlanStatusList = dpsMrStatusCheckService.execute(sosCd3, plannedProd, unallowableMrStatusList);
			mrPlanStatus = orgMrPlanStatusList.get(0).getStatusForMrPlan();

		} catch (UnallowableStatusException e) {

			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3253E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// 営業所配下計画情報取得
		// ----------------------
		CreateMrPlanOfficeInfoLogic createLogic = new CreateMrPlanOfficeInfoLogic(jgiMstDAO, sosMstDAO, officePlanDao, teamPlanDao, mrPlanDao, deliveryResultMrDao,
			getDeliveryResultSummaryDtoList(insType, scope, prodCode, sosCd3, sosCd4, plannedProd.getCategory()), mrPlanStatus);
		OfficePlanDto officePlanDto = createLogic.execute(insType, plannedProd, sosCd3, teamSosMstList);

		// ----------------------
		// 上位計画取得、積み上げ取得
		// ----------------------
		Long previousPlanValueUH = 0L;
		Long previousPlanValueP = 0L;
		Long totalPlanValueUH = 0L;
		Long totalPlanValueP = 0L;
		try {
			// チーム別計画
			TeamPlan teamPlan = teamPlanDao.searchUk(sosCd4, plannedProd.getProdCode());
			previousPlanValueUH = teamPlan.getPlannedValueUhY();
			previousPlanValueP = teamPlan.getPlannedValuePY();
			// 担当者別計画のチーム内合計
			MrPlanSosSummaryDto mrPlanSosSummaryDto = mrPlanDao.searchTeamSummary(plannedProd.getProdCode(), sosCd4);
			totalPlanValueUH = mrPlanSosSummaryDto.getPlannedValueUhY();
			totalPlanValueP = mrPlanSosSummaryDto.getPlannedValuePY();
		} catch (DataNotFoundException e) {
			// エラーにしない
		}

		// AL、かつ担当者別計画が公開されていない場合は、積上げを0にする
		DpUserInfo dpUserInfo = DpUserInfo.getDpUserInfo();
		DpUser dpUser = dpUserInfo.getSettingUser();
		if (dpUser != null && dpUser.isMatch(JokenSet.IYAKU_AL)) {
			if(	(mrPlanStatus != StatusForMrPlan.MR_PLAN_OPENED ) &&
				(mrPlanStatus != StatusForMrPlan.MR_PLAN_INPUT_FINISHED) &&
				(mrPlanStatus != StatusForMrPlan.MR_PLAN_COMPLETED )){
				totalPlanValueUH = 0L;
				totalPlanValueP = 0L;
			}
		}

		// ----------------------
		// 最新更新日、更新者取得
		// (担当者別計画からチーム内の最新情報を取得)
		// ----------------------
		Date lastUpdate = null;
		String lastUpdateUser = null;
		try {
			MrPlan lastUpdateData = mrPlanDao.getLastUpDateByTeam(sosCd4, plannedProd.getProdCode());
			lastUpdate = lastUpdateData.getUpDate();
			lastUpdateUser = lastUpdateData.getUpJgiName();
		} catch (DataNotFoundException e) {
			// エラーにしない
		}

		// ----------------------
		// 結果DTO作成
		// ----------------------
		return new MrPlanResultDto(sosCd3, plannedProd, previousPlanValueUH, previousPlanValueP, totalPlanValueUH, totalPlanValueP, lastUpdate, lastUpdateUser, officePlanDto,
			mrPlanStatus, calcType);
	}

	// チーム別計画情報取得(営業所指定)
	public MrPlanResultDto searchTeamPlan(InsType insType, String prodCode, String sosCd3, RefDeliveryScope scope) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scope == null) {
			final String errMsg = "実績参照範囲がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 計画対象品目取得
		// ----------------------
		PlannedProd plannedProd = null;
		try {
			plannedProd = plannedProdDAO.search(prodCode);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目が存在しない：";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
		}

		// ----------------------
		// 組織情報取得
		// ----------------------
		SosMst officeSosMst = null;
		try {
			officeSosMst = sosMstDAO.search(sosCd3);
		} catch (DataNotFoundException e) {
			final String errMsg = "営業所組織情報が存在しない：";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + sosCd3), e);
		}

		// 品目からカテゴリ判定
		String category = plannedProd.getCategory();

		// ----------------------
		// 試算タイプ取得
		// ----------------------
		CalcType calcType = null;
		try {
			OfficePlanStatus officePlanStatus = officePlanStatusDao.search(sosCd3, category);
			calcType = officePlanStatus.getCalcType();
		} catch (DataNotFoundException e) {
		}

		// ----------------------
		// 担当者別計画ステータスチェック
		// ----------------------
		StatusForMrPlan mrPlanStatus = null;
		try {

			// 許可しないステータスリスト作成
			List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
			unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
			unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
			unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_COMPLETED);
			unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_OPENED);
			unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_INPUT_FINISHED);
			unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_COMPLETED);

			// チェック実行
			List<MrPlanStatus> orgMrPlanStatusList = dpsMrStatusCheckService.execute(sosCd3, plannedProd, unallowableMrStatusList);
			mrPlanStatus = orgMrPlanStatusList.get(0).getStatusForMrPlan();

		} catch (UnallowableStatusException e) {

			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3250E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// 営業所配下計画情報取得
		// ----------------------
		OfficePlanDto officePlanDto = null;
		try {

			// (営業所配下のチーム一覧取得)
			List<SosMst> teamSosMstList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G, sosCd3);

			// チームがある場合
			CreateMrPlanOfficeInfoLogic createLogic = new CreateMrPlanOfficeInfoLogic(jgiMstDAO, sosMstDAO, officePlanDao, teamPlanDao, mrPlanDao, deliveryResultMrDao,
					getDeliveryResultSummaryDtoList(insType, scope, prodCode, sosCd3, null, category), mrPlanStatus);
			officePlanDto = createLogic.execute(insType, plannedProd, sosCd3, teamSosMstList);

		} catch (DataNotFoundException e) {

			// チームが存在しない組織は、営→担ルートなのでここにこないはず
			final String errMsg = "配下チームが存在しない：" + sosCd3;
			throw new LogicalException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// ----------------------
		// 上位計画取得、積み上げ取得
		// ----------------------
		Long previousPlanValueUH = 0L;
		Long previousPlanValueP = 0L;
		Long totalPlanValueUH = 0L;
		Long totalPlanValueP = 0L;
		try {
			// 営業所計画
			OfficePlan officePlan = officePlanDao.searchUk(sosCd3, plannedProd.getProdCode());
			previousPlanValueUH = officePlan.getPlannedValueUhY();
			previousPlanValueP = officePlan.getPlannedValuePY();
			// チーム別計画の営業所内合計
			TeamPlanSosSummaryDto teamPlanSosSummaryDto = teamPlanDao.searchSosSummary(plannedProd.getProdCode(), sosCd3);
			totalPlanValueUH = teamPlanSosSummaryDto.getPlannedValueUhY();
			totalPlanValueP = teamPlanSosSummaryDto.getPlannedValuePY();
		} catch (DataNotFoundException e) {
			// エラーにしない
		}

		// ----------------------
		// 最新更新日、更新者取得
		// (チーム別計画から営業所内の最新情報を取得)
		// ----------------------
		Date lastUpdate = null;
		String lastUpdateUser = null;
		try {
			TeamPlan lastUpdateData = teamPlanDao.getLastUpDate(sosCd3, plannedProd.getProdCode());
			lastUpdate = lastUpdateData.getUpDate();
			lastUpdateUser = lastUpdateData.getUpJgiName();
		} catch (DataNotFoundException e) {
			// エラーにしない
		}

		// ----------------------
		// 結果DTO作成
		// ----------------------
		return new MrPlanResultDto(sosCd3, plannedProd, previousPlanValueUH, previousPlanValueP, totalPlanValueUH, totalPlanValueP, lastUpdate, lastUpdateUser, officePlanDto,
			mrPlanStatus, calcType);
	}

	// チーム別計画情報取得(チーム指定)
	public MrPlanResultDto searchTeamPlan(InsType insType, String prodCode, String sosCd3, String sosCd4, RefDeliveryScope scope) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosCd4 == null) {
			final String errMsg = "組織コード(チーム)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scope == null) {
			final String errMsg = "実績参照範囲がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 計画対象品目取得
		// ----------------------
		PlannedProd plannedProd = null;
		try {
			plannedProd = plannedProdDAO.search(prodCode);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目が存在しない：";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
		}

		// ----------------------
		// チーム一覧取得
		// (自チーム取得)
		// ----------------------
		SosMst sosMst = sosMstDAO.search(sosCd4);
		List<SosMst> teamSosMstList = new ArrayList<SosMst>();
		teamSosMstList.add(sosMst);

		// ----------------------
		// 試算タイプ取得
		// ※ONC組織はこのルートがないため、MMP固定
		// ----------------------
		CalcType calcType = null;
		try {
//			OfficePlanStatus officePlanStatus = officePlanStatusDao.search(sosCd3, ProdCategory.MMP);
			OfficePlanStatus officePlanStatus = officePlanStatusDao.search(sosCd3, plannedProd.getCategory());
			calcType = officePlanStatus.getCalcType();
		} catch (DataNotFoundException e) {
		}

		// ----------------------
		// 担当者別計画ステータスチェック
		// ----------------------
		StatusForMrPlan mrPlanStatus = null;
		try {

			// 許可しないステータスリスト作成
			List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
			unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
			unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
			unallowableMrStatusList.add(MrStatusForCheck.ESTIMATED);
			unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_COMPLETED);
			unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_OPENED);
			unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_INPUT_FINISHED);
			unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_COMPLETED);

			// チェック実行
			List<MrPlanStatus> orgMrPlanStatusList = dpsMrStatusCheckService.execute(sosCd3, plannedProd, unallowableMrStatusList);
			mrPlanStatus = orgMrPlanStatusList.get(0).getStatusForMrPlan();

		} catch (UnallowableStatusException e) {

			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3251E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// 営業所配下計画情報取得
		// ----------------------
		CreateMrPlanOfficeInfoLogic createLogic = new CreateMrPlanOfficeInfoLogic(jgiMstDAO, sosMstDAO, officePlanDao, teamPlanDao, mrPlanDao, deliveryResultMrDao,
			getDeliveryResultSummaryDtoList(insType, scope, prodCode, sosCd3, sosCd4, plannedProd.getCategory()), mrPlanStatus);
		OfficePlanDto officePlanDto = createLogic.execute(insType, plannedProd, sosCd3, teamSosMstList);

		// ----------------------
		// 上位計画取得、積み上げ取得
		// ----------------------
		Long previousPlanValueUH = 0L;
		Long previousPlanValueP = 0L;
		Long totalPlanValueUH = 0L;
		Long totalPlanValueP = 0L;
		try {
			// 営業所計画
			OfficePlan officePlan = officePlanDao.searchUk(sosCd3, plannedProd.getProdCode());
			previousPlanValueUH = officePlan.getPlannedValueUhY();
			previousPlanValueP = officePlan.getPlannedValuePY();
			// チーム別計画
			TeamPlan teamPlan = teamPlanDao.searchUk(sosCd4, plannedProd.getProdCode());
			totalPlanValueUH = teamPlan.getPlannedValueUhY();
			totalPlanValueP = teamPlan.getPlannedValuePY();
		} catch (DataNotFoundException e) {
			// エラーにしない
		}

		// ----------------------
		// 最新更新日、更新者取得
		// (自チーム情報を取得)
		// ----------------------
		Date lastUpdate = null;
		String lastUpdateUser = null;
		try {
			TeamPlan lastUpdateData = teamPlanDao.searchUk(sosCd4, plannedProd.getProdCode());
			lastUpdate = lastUpdateData.getUpDate();
			lastUpdateUser = lastUpdateData.getUpJgiName();
		} catch (DataNotFoundException e) {
			// エラーにしない
		}

		// ----------------------
		// 結果DTO作成
		// ----------------------
		return new MrPlanResultDto(sosCd3, plannedProd, previousPlanValueUH, previousPlanValueP, totalPlanValueUH, totalPlanValueP, lastUpdate, lastUpdateUser, officePlanDto,
			mrPlanStatus, calcType);
	}

	public EstimationResultDetailResultDto searchEstimationResultDetail(EstimationResultDetailScDto scDto) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "検索条件オブジェクトがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final String sosCd3 = scDto.getSosCd3();
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final String prodCode = scDto.getProdCode();
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 立案対象品目取得
		// ----------------------
		PlannedProd plannedProd = null;
		try {
			plannedProd = plannedProdDAO.search(prodCode);
		} catch (DataNotFoundException e) {
			final String errMsg = "立案対象品目が存在しない：";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
		}

		// ----------------------
		// 組織情報取得
		// ----------------------
		SosMst officeSosMst = null;
		try {
			officeSosMst = sosMstDAO.search(sosCd3);
		} catch (DataNotFoundException e) {
			final String errMsg = "営業所組織情報が存在しない：";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + sosCd3), e);
		}

		// ----------------------
		// 試算パラメータ取得
		// ----------------------
		EstParamOffice estParamOffice = null;
		EstParamHonbu estParamHonbu = null;
		BaseParam baseParam = null;
		try {
			// 試算パラメータ(営業所案)取得
			estParamOffice = estParamOfficeDao.search(sosCd3, prodCode);
			baseParam = estParamOffice.getBaseParam();
		} catch (DataNotFoundException e) {
			// 営業所案が存在しない場合は、試算パラメータ(本部案)を取得
			try {
				estParamHonbu = estParamHonbuDao.search(prodCode);
				baseParam = estParamHonbu.getBaseParam();
			} catch (DataNotFoundException e1) {
				final String errMsg = "試算パラメータ(本部案、営業所案)が存在しない：";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e1);
			}
		}

		// ------------------------------
		// 担当者別計画ステータス取得
		// ------------------------------
		MrPlanStatus mrPlanStatus = null;
		try {
			mrPlanStatus = mrPlanStatusDao.search(sosCd3, prodCode);
		} catch (DataNotFoundException e) {
			// エラーとしない
		}

		// -----------------------------------------------------------------
		// 営業所以下の各計画(営業所計画、チーム別計画、担当者別計画)を取得
		// -----------------------------------------------------------------
		//画面で選択したカテゴリを設定
		String category = scDto.getCategory();
		final InsType insType = scDto.getInsType();
		OfficePlanDto officePlanDto = null;
		try {

			// 指定営業所下のチーム一覧を取得
			List<SosMst> teamSosMstList;
			teamSosMstList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G, sosCd3);

			// チームがある場合
			CreateMrPlanOfficeInfoLogic createLogic = new CreateMrPlanOfficeInfoLogic(jgiMstDAO, sosMstDAO, officePlanDao, teamPlanDao, mrPlanDao, deliveryResultMrDao, null,mrPlanStatus.getStatusForMrPlan());
			officePlanDto = createLogic.execute(insType, plannedProd, sosCd3, teamSosMstList);

		} catch (DataNotFoundException e) {

			// チームがない場合
			CreateMrPlanOfficeInfoLogic createLogic = new CreateMrPlanOfficeInfoLogic(jgiMstDAO, sosMstDAO, officePlanDao, teamPlanDao, mrPlanDao, deliveryResultMrDao, null,mrPlanStatus.getStatusForMrPlan());
//			officePlanDto = createLogic.execute(insType, plannedProd, sosCd3, officeSosMst);
			officePlanDto = createLogic.execute(insType, plannedProd, sosCd3, officeSosMst, category);
		}

		// -------------------------------------------
		// 未獲得市場・納入実績・フリー項目の情報取得
		// -------------------------------------------
		final String refProdCode = baseParam.getRefProdCode();
		final RefPeriod refFrom = baseParam.getRefFrom();
		final RefPeriod refTo = baseParam.getRefTo();

		// 営業所母数の構成比
		List<MrEstimationRatio> officeMrEstimationRatioList = new ArrayList<MrEstimationRatio>();
		try {
			List<MrEstimationRatio> tmpList = mrEstimationRatioDao.searchList(sosCd3, prodCode, refProdCode, refFrom, refTo, category);
			officeMrEstimationRatioList.addAll(tmpList);
		} catch (DataNotFoundException e) {
			// エラーとしない
		}

		// チーム母数の構成比
		List<MrEstimationRatio> teamMrEstimationRatioList = new ArrayList<MrEstimationRatio>();
		if(officePlanDto.hasTeamSos()){
			// チームがある場合
			for (TeamPlanDto teamPlanDto : officePlanDto.getTeamPlanDtoList()) {

				final String teamSosCd = teamPlanDto.getSosCd();
				try {
					List<MrEstimationRatio> tmpList = mrEstimationRatioDao.searchListByTeam(teamSosCd, prodCode, refProdCode, refFrom, refTo, category);
					teamMrEstimationRatioList.addAll(tmpList);
				} catch (DataNotFoundException e) {
					// エラーとしない
				}
			}
		} else {
			// チームがない場合は営業所構成比
			try {
				List<MrEstimationRatio> tmpList = mrEstimationRatioDao.searchList(sosCd3, prodCode, refProdCode, refFrom, refTo, category);
				teamMrEstimationRatioList.addAll(tmpList);
			} catch (DataNotFoundException e) {
				// エラーとしない
			}
		}

		// 対象の施設のみをセット
		List<MrEstimationRatio> mrEstimationRatioList = new ArrayList<MrEstimationRatio>();
		mrEstimationRatioList.addAll(officeMrEstimationRatioList);
		mrEstimationRatioList.addAll(teamMrEstimationRatioList);
		for (MrEstimationRatio mrEstRatio : mrEstimationRatioList) {
			List<EstimationRatio> estimationRatioList = new ArrayList<EstimationRatio>();
			if (mrEstRatio.getEstimationRatioList() != null) {
				for (EstimationRatio estRatio : mrEstRatio.getEstimationRatioList()) {

					// 施設区分が指定されていない場合は無条件に追加
					if (insType == null) {
						if (estRatio.getInsType() == null) {
							estimationRatioList.add(estRatio);
						}
					}

					// 施設区分が指定されている場合は対象の施設区分のみを追加
					else {
						if (estRatio.getInsType() != null && estRatio.getInsType().equals(insType)) {
							estimationRatioList.add(estRatio);
						}
					}
				}
			}
			mrEstRatio.setEstimationRatioList(estimationRatioList);
		}

		// チーム内、または営業所内の担当者別計画の最新レコード取得
		MrPlan mrPlan = null;
		final String sosCd4 = scDto.getSosCd4();
		try {
			if (sosCd4 == null) {
				mrPlan = mrPlanDao.getLastUpDateBySos(sosCd3, prodCode);
			} else {
				mrPlan = mrPlanDao.getLastUpDateByTeam(sosCd4, prodCode);
			}
		} catch (DataNotFoundException e) {
			// エラーにしない
		}

		// ログインユーザがALの場合、ステータスチェックを行う（ステータス無・試算中・試算済みの場合、エラー)
		DpUser dpUser = DpUserInfo.getDpUserInfo().getSettingUser();
		if (dpUser.isMatch(JokenSet.IYAKU_AL)) {
			List<MrStatusForCheck> unallowableStatusList = new ArrayList<MrStatusForCheck>();
			unallowableStatusList.add(MrStatusForCheck.NOTHING);
			unallowableStatusList.add(MrStatusForCheck.ESTIMATED);
			unallowableStatusList.add(MrStatusForCheck.ESTIMATING);
			try {
				dpsMrStatusCheckService.execute(sosCd3, plannedProd, unallowableStatusList);
			} catch (UnallowableStatusException e) {

				// メッセージ作成
				throw new UnallowableStatusException(new Conveyance(new MessageKey("DPS3244E")));
			}
		}

		// ------------------------------
		// RsultDtoの生成
		// ------------------------------
		EstimationIndexTotalDtoCreateLogic logic1 = new EstimationIndexTotalDtoCreateLogic(estParamOffice, estParamHonbu, mrPlanStatus, officePlanDto, officeMrEstimationRatioList,
			teamMrEstimationRatioList);
		EstimationIndexTotalDto estimationIndexTotalDto = logic1.execute();
		EstimationResultDetailResultDtoCreateLogic logic2 = new EstimationResultDetailResultDtoCreateLogic(plannedProd, estParamOffice, estParamHonbu, mrPlanStatus, officePlanDto,
			officeMrEstimationRatioList, teamMrEstimationRatioList, mrPlan, sosCd4, estimationIndexTotalDto);
		return logic2.createResultDto();
	}

	/**
	 * 納入実績担当者期サマリ情報を取得する。
	 *
	 * @param insType 施設対象区分(NULL可)
	 * @param scope 取得有無(NULL可)
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @return 納入実績担当者期サマリ
	 */
	private List<DeliveryResultSummaryDto> getDeliveryResultSummaryDtoList(InsType insType, RefDeliveryScope scope, String prodCode, String sosCd3, String sosCd4, String category) {

		// 取得不要の場合はnullを返す。
		if (scope == null || RefDeliveryScope.NONE.equals(scope)) {
			return null;
		}

		// ----------------------
		// 試算パラメータ取得
		// ----------------------
		BaseParam baseParam = null;
		try {
			// 試算パラメータ(営業所案)取得
			EstParamOffice estParamOffice = estParamOfficeDao.search(sosCd3, prodCode);
			baseParam = estParamOffice.getBaseParam();
		} catch (DataNotFoundException e) {
			// 営業所案が存在しない場合は、試算パラメータ(本部案)を取得
			try {
				EstParamHonbu estParamHonbu = estParamHonbuDao.search(prodCode);
				baseParam = estParamHonbu.getBaseParam();
			} catch (DataNotFoundException e1) {
				return null;
//				final String errMsg = "試算パラメータ(本部案、営業所案)が存在しない：";
//				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e1);
			}
		}

		// 参照用品目1～4をリストに格納
		List<DeliveryResultSummaryDto> deliveryResultSummaryDtoList = null;
		List<String> prodCodeList = null;
		String prodCode1 = baseParam.getResultRefProdCode1();
		if (StringUtils.isNotBlank(prodCode1)) {
			prodCodeList = new ArrayList<String>();
			prodCodeList.add(prodCode1);
			String prodCode2 = baseParam.getResultRefProdCode2();
			if (StringUtils.isNotBlank(prodCode2)) {
				prodCodeList.add(prodCode2);
				String prodCode3 = baseParam.getResultRefProdCode3();
				if (StringUtils.isNotBlank(prodCode3)) {
					prodCodeList.add(prodCode3);
					String prodCode4 = baseParam.getResultRefProdCode4();
					if (StringUtils.isNotBlank(prodCode4)) {
						prodCodeList.add(prodCode4);
					}
				}
			}
		}
		if (prodCodeList != null && !prodCodeList.isEmpty()) {
			deliveryResultSummaryDtoList = new ArrayList<DeliveryResultSummaryDto>();
			for (String entryProdCode : prodCodeList) {
				try {
					DeliveryResultSummaryDto deliveryResultSummaryDto = dpsDeliverySummarySearchService.searchDeliveryResultSummaryDto(insType, entryProdCode, sosCd3, sosCd4, category);
					deliveryResultSummaryDtoList.add(deliveryResultSummaryDto);
				} catch (LogicalException e) {
					// データがなくてもエラーとしない
				}
			}
		}
		return deliveryResultSummaryDtoList;
	}

	// 調整金額有無取得
	public OfficeTeamPlanChoseiDto searchChosei(String sosCd, String prodCode, String category) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 試算タイプが指定されていない場合は、"営→チ→担"に設定
		// ----------------------
		CalcType calcType = null;
		try {
			OfficePlanStatus officePlanStatus = officePlanStatusDao.search(sosCd, category);
			calcType = officePlanStatus.getCalcType();
		} catch (DataNotFoundException e) {
			// エラーにしない
		}

		// 試算タイプが"営→担"ではない場合、チーム別計画と担当者別計画積上間の調整金額を検証
		if (calcType == null || CalcType.OFFICE_TEAM_MR.equals(calcType)) {
			return teamPlanDao.getChosei(sosCd, prodCode);
		}

		// 試算タイプが"営→担"である場合、営業所計画と担当者別計画積上間の調整金額を検証
		else {

			Long officePlanUh = null;
			Long officePlanP = null;
			try {
				OfficePlan officePlan = officePlanDao.searchUk(sosCd, prodCode);
				officePlanUh = officePlan.getPlannedValueUhY();
				officePlanP = officePlan.getPlannedValuePY();
			} catch (DataNotFoundException e) {
				// エラーにしない
			}

			Long mrPlanUh = null;
			Long mrPlanP = null;
			try {
				MrPlanSosSummaryDto summary = mrPlanDao.searchSosSummary(prodCode, sosCd);
				mrPlanUh = summary.getPlannedValueUhY();
				mrPlanP = summary.getPlannedValuePY();
			} catch (DataNotFoundException e) {
				// エラーにしない
			}
			// --------------------------------
			// UHの検証
			// --------------------------------
			Boolean choseiFlgUh = false;
			Long choseiUh = MathUtil.subEx(officePlanUh, mrPlanUh);
			if (choseiUh != null && choseiUh != 0L) {
				choseiFlgUh = true;
			}

			// --------------------------------
			// Pの検証
			// --------------------------------
			Boolean choseiFlgP = false;
			Long choseiP = MathUtil.subEx(officePlanP, mrPlanP);
			if (choseiP != null && choseiP != 0L) {
				choseiFlgP = true;
			}
			return new OfficeTeamPlanChoseiDto(choseiFlgUh, choseiFlgP);
		}
	}
}
