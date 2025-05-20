package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.dao.DpsCodeMasterDao;
import jp.co.takeda.dao.DpsPlannedCtgDao;
import jp.co.takeda.dao.EstParamHonbuDao;
import jp.co.takeda.dao.EstParamOfficeDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.MrPlanDao;
import jp.co.takeda.dao.MrPlanStatusDao;
import jp.co.takeda.dao.OfficePlanDao;
import jp.co.takeda.dao.OfficePlanStatusDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.SpecialInsPlanDao;
import jp.co.takeda.dao.TeamInputStatusDao;
import jp.co.takeda.dao.TeamPlanDao;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.EstimationResultDetailExecDto;
import jp.co.takeda.dto.MrPlanDto;
import jp.co.takeda.dto.MrPlanResultDto;
import jp.co.takeda.dto.MrPlanStatusUpdateDto;
import jp.co.takeda.dto.MrPlanUpdateDto;
import jp.co.takeda.dto.OfficeTeamPlanChoseiDto;
import jp.co.takeda.dto.PlannedValueCopyDto;
import jp.co.takeda.dto.TeamPlanDto;
import jp.co.takeda.dto.TeamPlanUpdateDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.CreateChoseiMsgLogic;
import jp.co.takeda.logic.EstimationResultDetailEstimationLogic;
import jp.co.takeda.logic.MrPlanStatusUpdateLogic;
import jp.co.takeda.logic.div.CopyTarget;
import jp.co.takeda.logic.div.CopyWay;
import jp.co.takeda.logic.div.InsWsStatusForCheck;
import jp.co.takeda.logic.div.MrStatusForCheck;
import jp.co.takeda.logic.div.OfficeStatusForCheck;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.model.MrPlanStatus;
import jp.co.takeda.model.OfficePlanStatus;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.TeamInputStatus;
import jp.co.takeda.model.TeamPlan;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.CalcType;
import jp.co.takeda.model.div.EstimationBasePlan;
import jp.co.takeda.model.div.InputStateForMrPlan;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.StatusForMrPlan;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.MathUtil;

/**
 * 担当者別計画機能の更新に関するサービス実装クラス
 *
 * @author stakeuchi
 */
@Transactional
@Service("dpsMrPlanService")
public class DpsMrPlanServiceImpl implements DpsMrPlanService {

	/**
	 * 営業所計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsOfficeStatusCheckService")
	protected DpsOfficeStatusCheckService dpsOfficeStatusCheckService;

	/**
	 * 担当者別計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsMrStatusCheckService")
	protected DpsMrStatusCheckService dpsMrStatusCheckService;

	/**
	 * 施設医師別計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsDocStatusCheckService")
	protected DpsInsDocStatusCheckService dpsInsDocStatusCheckService;

	/**
	 * 施設特約店別計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsWsStatusCheckService")
	protected DpsInsWsStatusCheckService dpsInsWsStatusCheckService;

	/**
	 * 試算実行サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsEstimationExecuteService")
	DpsEstimationExecuteService dpsEstimationExecuteService;

	/**
	 * 営業所計画ステータスDAO
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
	 * 営業所計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("officePlanDao")
	protected OfficePlanDao officePlanDao;

	/**
	 * 担当者計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("mrPlanDao")
	protected MrPlanDao mrPlanDao;

	/**
	 * チーム計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("teamPlanDao")
	protected TeamPlanDao teamPlanDao;

	/**
	 * チーム別入力状況DAO
	 */
	@Autowired(required = true)
	@Qualifier("teamInputStatusDao")
	protected TeamInputStatusDao teamInputStatusDao;

	/**
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

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
	 * 従業員マスタDAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;

	/**
	 * 特定施設個別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("specialInsPlanDao")
	protected SpecialInsPlanDao specialInsPlanDao;

	/**
	 * 計画対象品目取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * 共通DAO
	 */
	@Autowired(required = true)
	@Qualifier("commonDao")
	protected CommonDao commonDao;

	/**
	 * 計画対象カテゴリ領域DAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsPlannedCtgDao")
	protected DpsPlannedCtgDao dpsPlannedCtgDao;

	/**
	 * 計画支援汎用マスタDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterDao")
	protected DpsCodeMasterDao dpsCodeMasterDao;

	public void updateStatusTeamOpenRelease(String sosCd3, List<PlannedProd> plannedProdList, List<MrPlanStatusUpdateDto> updateDtoList) throws LogicalException {
		// チーム別計画を「公開解除」ステータスへ更新実行
		MrPlanStatusUpdateLogic statusUpdateLogic = new MrPlanStatusUpdateLogic(dpsOfficeStatusCheckService, dpsMrStatusCheckService, dpsInsDocStatusCheckService, dpsInsWsStatusCheckService, mrPlanStatusDao,
			officePlanDao, mrPlanDao, commonDao, sosMstDAO, dpsCodeMasterDao, dpsPlannedCtgDao);
		statusUpdateLogic.updateStatusTeamOpenRelease(sosCd3, plannedProdList, updateDtoList);
	}

	public void updateStatusTeamOpen(String sosCd3, List<PlannedProd> plannedProdList, List<MrPlanStatusUpdateDto> updateDtoList) throws LogicalException {
		// チーム別計画を「公開」ステータスへ更新実行
		MrPlanStatusUpdateLogic statusUpdateLogic = new MrPlanStatusUpdateLogic(dpsOfficeStatusCheckService, dpsMrStatusCheckService, dpsInsDocStatusCheckService, dpsInsWsStatusCheckService, mrPlanStatusDao,
			officePlanDao, mrPlanDao, commonDao, sosMstDAO, dpsCodeMasterDao, dpsPlannedCtgDao);
		statusUpdateLogic.updateStatusTeamOpen(sosCd3, plannedProdList, updateDtoList);
	}

	public void updateStatusTeamFixRelease(String sosCd3, List<PlannedProd> plannedProdList, List<MrPlanStatusUpdateDto> updateDtoList) throws LogicalException {
		// チーム別計画を「確定解除」ステータスへ更新実行
		MrPlanStatusUpdateLogic statusUpdateLogic = new MrPlanStatusUpdateLogic(dpsOfficeStatusCheckService, dpsMrStatusCheckService, dpsInsDocStatusCheckService, dpsInsWsStatusCheckService, mrPlanStatusDao,
			officePlanDao, mrPlanDao, commonDao, sosMstDAO, dpsCodeMasterDao, dpsPlannedCtgDao);
		statusUpdateLogic.updateStatusTeamFixRelease(sosCd3, plannedProdList, updateDtoList);
	}

	public void updateStatusTeamFix(String sosCd3, List<PlannedProd> plannedProdList, List<MrPlanStatusUpdateDto> updateDtoList) throws LogicalException {
		// ----------------------------
		// 引数チェック
		// ----------------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null || plannedProdList.size() == 0) {
			final String errMsg = "品目リストがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (updateDtoList == null || updateDtoList.size() == 0) {
			final String errMsg = "ステータスリストがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------------
		// チーム別計画の決定値チェック
		// ----------------------------
		List<MessageKey> errorList = new ArrayList<MessageKey>();
		List<SosMst> teamList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G, sosCd3);
		for (PlannedProd plannedProd : plannedProdList) {
			boolean errorFlg = false;
			for (SosMst sosMst : teamList) {
				try {
					TeamPlan teamPlan = teamPlanDao.searchUk(sosMst.getSosCd(), plannedProd.getProdCode());
					// 決定が未登録の場合、エラー
					if (teamPlan.getPlannedValueUhY() == null || teamPlan.getPlannedValuePY() == null) {
						errorFlg = true;
					}
				} catch (DataNotFoundException e) {
					// チーム別計画が未登録の場合、エラー
					errorFlg = true;
				}

			}
			if (errorFlg) {
				errorList.add(new MessageKey("DPS3331E", plannedProd.getProdName()));
			}
		}
		if (errorList.size() != 0) {
			// メッセージ作成
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3330E"));
			messageKeyList.addAll(errorList);
			throw new LogicalException(new Conveyance(messageKeyList));
		}

		// チーム別計画を「確定」ステータスへ更新実行
		MrPlanStatusUpdateLogic statusUpdateLogic = new MrPlanStatusUpdateLogic(dpsOfficeStatusCheckService, dpsMrStatusCheckService, dpsInsDocStatusCheckService, dpsInsWsStatusCheckService, mrPlanStatusDao,
			officePlanDao, mrPlanDao, commonDao, sosMstDAO, dpsCodeMasterDao, dpsPlannedCtgDao);
		statusUpdateLogic.updateStatusTeamFix(sosCd3, plannedProdList, updateDtoList);
	}

	public void updateStatusMrOpenRelease(String sosCd3, List<PlannedProd> plannedProdList, List<MrPlanStatusUpdateDto> updateDtoList) throws LogicalException {
		// 担当者別計画を「公開解除」ステータスへ更新実行
		MrPlanStatusUpdateLogic statusUpdateLogic = new MrPlanStatusUpdateLogic(dpsOfficeStatusCheckService, dpsMrStatusCheckService, dpsInsDocStatusCheckService, dpsInsWsStatusCheckService, mrPlanStatusDao,
			officePlanDao, mrPlanDao, commonDao, sosMstDAO, dpsCodeMasterDao, dpsPlannedCtgDao);
		statusUpdateLogic.updateStatusMrOpenRelease(sosCd3, plannedProdList, updateDtoList);

		// チーム別入力状況を削除する。
		deleteTeamInputState(sosCd3, plannedProdList);
	}

	public void updateStatusMrOpen(String sosCd3, List<PlannedProd> plannedProdList, List<MrPlanStatusUpdateDto> updateDtoList) throws LogicalException {
		// 担当者別計画を「公開」ステータスへ更新実行
		MrPlanStatusUpdateLogic statusUpdateLogic = new MrPlanStatusUpdateLogic(dpsOfficeStatusCheckService, dpsMrStatusCheckService, dpsInsDocStatusCheckService, dpsInsWsStatusCheckService, mrPlanStatusDao,
			officePlanDao, mrPlanDao, commonDao, sosMstDAO, dpsCodeMasterDao, dpsPlannedCtgDao);
		statusUpdateLogic.updateStatusMrOpen(sosCd3, plannedProdList, updateDtoList);
	}

	public void updateStatusMrFixRelease(String sosCd3, List<PlannedProd> plannedProdList, List<MrPlanStatusUpdateDto> updateDtoList) throws LogicalException {

		// 担当者別計画を「確定解除」ステータスへ更新実行
		MrPlanStatusUpdateLogic statusUpdateLogic = new MrPlanStatusUpdateLogic(dpsOfficeStatusCheckService, dpsMrStatusCheckService, dpsInsDocStatusCheckService, dpsInsWsStatusCheckService, mrPlanStatusDao,
			officePlanDao, mrPlanDao, commonDao, sosMstDAO, dpsCodeMasterDao, dpsPlannedCtgDao);
		statusUpdateLogic.updateStatusMrFixRelease(sosCd3, plannedProdList, updateDtoList);

		// チーム別入力状況を削除する。
		deleteTeamInputState(sosCd3, plannedProdList);
	}

	// 担当者別計画の確定処理
	public void updateStatusMrFix(String sosCd3, List<PlannedProd> plannedProdList, List<MrPlanStatusUpdateDto> updateDtoList) throws LogicalException {

//		// ---------------------------------------
//		// 担当者別計画と特定施設の逆ザヤ検証
//		// ---------------------------------------
//		new CheckMrPlanValueLogic(sosCd3, plannedProdList, mrPlanDao, jgiMstDAO).execute();

		// ---------------------------------------
		// 担当者別計画を[確定]ステータスへ更新
		// ---------------------------------------
		MrPlanStatusUpdateLogic statusUpdateLogic = new MrPlanStatusUpdateLogic(dpsOfficeStatusCheckService, dpsMrStatusCheckService, dpsInsDocStatusCheckService, dpsInsWsStatusCheckService, mrPlanStatusDao,
			officePlanDao, mrPlanDao, commonDao, sosMstDAO, dpsCodeMasterDao, dpsPlannedCtgDao);
		statusUpdateLogic.updateStatusMrFix(sosCd3, plannedProdList, updateDtoList);

		// ---------------------------------------
		// チーム別入力状況を入力完了にする
		// ---------------------------------------
		// チーム一覧を取得
		List<SosMst> teamList = null;
		try {
			teamList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST, null, sosCd3);
		} catch (DataNotFoundException e) {
//			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, "チーム一覧取得失敗"), e);
			teamList = new ArrayList<SosMst>();
		}
		for (PlannedProd plannedProd : plannedProdList) {
			String prodCode = plannedProd.getProdCode();
			for (SosMst team : teamList) {
				String sosCd4 = team.getSosCd();
				try {
					// 下書中の場合は、入力完了に更新する。
					TeamInputStatus teamInputStatus = teamInputStatusDao.search(sosCd4, prodCode);
					switch (teamInputStatus.getInputStateForMrPlan()) {
						case MR_PLAN_INPUTTING:
							teamInputStatus.setInputStateForMrPlan(InputStateForMrPlan.MR_PLAN_INPUT_FINISHED);
							teamInputStatus.setMrPlanInputDate(null);
							teamInputStatusDao.update(teamInputStatus);
							break;
					}
				} catch (DataNotFoundException e) {
					// 存在しない場合は、入力完了で登録する。
					TeamInputStatus teamInputStatus = new TeamInputStatus();
					teamInputStatus.setSosCd(sosCd4);
					teamInputStatus.setProdCode(prodCode);
					teamInputStatus.setInputStateForMrPlan(InputStateForMrPlan.MR_PLAN_INPUT_FINISHED);
					teamInputStatus.setMrPlanInputDate(null);
					teamInputStatusDao.insert(teamInputStatus);
				}
			}
		}
	}

	// 担当者別計画(営業所)の登録・更新を行う。
	public void updateMrPlan(String sosCd3, MrPlanUpdateDto mrPlanUpdateDto, String category, StatusForMrPlan statusForMrPlan) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		// 組織コード(営業所)チェック
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 担当者別計画更新用DTOチェック
		if (mrPlanUpdateDto == null) {
			final String errMsg = "担当者別計画更新用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -----------------------------------------------------------------
		// 逆鞘チェック「担当者別計画立案ステータスが担当者別計画公開以降」
		// -----------------------------------------------------------------
		if (statusForMrPlan.equals(StatusForMrPlan.MR_PLAN_OPENED) || statusForMrPlan.equals(StatusForMrPlan.MR_PLAN_INPUT_FINISHED)
				|| statusForMrPlan.equals(StatusForMrPlan.MR_PLAN_COMPLETED)) {
			List<String> jgiNameList = modifyReverseSheathCheck(mrPlanUpdateDto.getMrPlanDtoList(), mrPlanUpdateDto.getInsType());
			if (jgiNameList.size() > 0) {
				StringBuilder sb = new StringBuilder();
				for (String jgiName : jgiNameList) {
					sb.append("【" + jgiName + "】");
				}
				MessageKey messageKey = new MessageKey("DPS3301E", sb.toString());
				final String errMsg = "担当者別計画と特定施設個別計画の逆ザヤチェックで検証ＮＧ";
				throw new LogicalException(new Conveyance(messageKey, errMsg));
			}
		}

		// -----------------------------
		// 営業所計画立案ステータス取得
		// -----------------------------
		// 営業所計画ステータス取得
		OfficePlanStatus officePlanStatus = officePlanStatusDao.search(sosCd3, category);

		// ----------------------
		// ステータスチェック
		// ----------------------
		// チェック対象の品目リスト作成
		List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
		try {
			PlannedProd plannedProd = plannedProdDAO.search(mrPlanUpdateDto.getPlannedProd().getProdCode());
			plannedProdList.add(plannedProd);
			mrPlanUpdateDto.getPlannedProd().setPlanLevelInsDoc(plannedProd.getPlanLevelInsDoc());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目が取得できない：" + mrPlanUpdateDto.getPlannedProd().getProdCode();
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		try {
			// 許可しないステータスリスト作成
			List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
			unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
			unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
			unallowableMrStatusList.add(MrStatusForCheck.ESTIMATED);
			unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_OPENED);
			unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_COMPLETED);
			unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_COMPLETED);

			// ステータスチェック実行
			dpsMrStatusCheckService.execute(sosCd3, plannedProdList, unallowableMrStatusList);

		} catch (UnallowableStatusException e) {
			// ステータスエラー
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			if (statusForMrPlan.equals(StatusForMrPlan.MR_PLAN_COMPLETED)) {
				// 「確定済」の場合のみ下記メッセージを追加する
				messageKeyList.add(new MessageKey("DPS3254E", "登録"));
			}
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

//		// 施設医師別計画ステータスチェック(配分中はだめ)
//		if(BooleanUtils.isTrue(mrPlanUpdateDto.getPlannedProd().getPlanLevelInsDoc())){
//			try {
//				// 許可しないステータスリスト作成
//				List<InsDocStatusForCheck> unallowableInsDocStatusList = new ArrayList<InsDocStatusForCheck>();
//				unallowableInsDocStatusList.add(InsDocStatusForCheck.DISTING);
//
//				// チェック実行
//				List<JgiMst> jgiMstList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd3, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G);
//				dpsInsDocStatusCheckService.execute(jgiMstList, plannedProdList, unallowableInsDocStatusList);
//
//			} catch (UnallowableStatusException e) {
//
//				List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
//				messageKeyList.add(new MessageKey("DPS3256E"));
//				messageKeyList.addAll(e.getConveyance().getMessageKeyList());
//				throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
//			}
//		}

		// 施設特約店別計画ステータスチェック(配分中はだめ)
		try {

			// 許可しないステータスリスト作成
			List<InsWsStatusForCheck> unallowableInsWsStatusList = new ArrayList<InsWsStatusForCheck>();
			unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);

			// チェック実行
			List<JgiMst> jgiMstList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd3, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G);
			dpsInsWsStatusCheckService.execute(jgiMstList, plannedProdList, unallowableInsWsStatusList);

		} catch (UnallowableStatusException e) {

			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3256E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// 担当者別計画更新
		// ----------------------
		// 担当者別計画の更新
		List<MrPlanDto> mrPlanDtoList = mrPlanUpdateDto.getMrPlanDtoList();
		InsType insType = mrPlanUpdateDto.getInsType();
		for (MrPlanDto mrPlanDto : mrPlanDtoList) {
			mrPlanDao.update(mrPlanDto.convertMrPlan(insType), insType);
		}

		// -----------------------------------------
		// チーム別計画更新(試算が営⇒担の場合のみ）
		// -----------------------------------------
		if (CalcType.OFFICE_MR.equals(officePlanStatus.getCalcType())) {
			// ユーザ情報取得
			DpUser dpUser = DpUserInfo.getDpUserInfo().getLoginUser();

			// チームリスト取得、積上げ更新
			try {
				List<SosMst> teamList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G, sosCd3);
				for (SosMst team : teamList) {
					teamPlanDao.updateByMrPlanSum(team.getSosCd(), mrPlanUpdateDto.getPlannedProd().getProdCode(), dpUser);
				}
			} catch (DataNotFoundException e) {
				// チームが存在しない場合もNGにしない
			}
		}
	}

	// 担当者別計画(AL)の登録・更新を行う。
	public void updateMrPlan(String sosCd3, String sosCd4, boolean fixFlg, MrPlanUpdateDto mrPlanUpdateDto, String category, StatusForMrPlan statusForMrPlan) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		// 組織コード(営業所)チェック
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 組織コード(チーム)チェック
		if (sosCd4 == null) {
			final String errMsg = "組織コード(チーム)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 担当者別計画更新用DTOチェック
		if (mrPlanUpdateDto == null) {
			final String errMsg = "担当者別計画更新用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -----------------------------------------------------------------
		// 逆鞘チェック「担当者別計画立案ステータスが担当者別計画公開」
		// -----------------------------------------------------------------
		if (statusForMrPlan.equals(StatusForMrPlan.MR_PLAN_OPENED)) {
			List<String> jgiNameList = modifyReverseSheathCheck(mrPlanUpdateDto.getMrPlanDtoList(), mrPlanUpdateDto.getInsType());
			if (jgiNameList.size() > 0) {
				StringBuilder sb = new StringBuilder();
				for (String jgiName : jgiNameList) {
					sb.append("【" + jgiName + "】");
				}
				MessageKey messageKey = new MessageKey("DPS3301E", sb.toString());
				final String errMsg = "担当者別計画と特定施設個別計画の逆ザヤチェックで検証ＮＧ";
				throw new LogicalException(new Conveyance(messageKey, errMsg));
			}
		}

		// -----------------------------
		// 営業所計画立案ステータス取得
		// -----------------------------
		// 営業所計画ステータス取得
		OfficePlanStatus officePlanStatus = officePlanStatusDao.search(sosCd3, category);

		// ----------------------
		// ステータスチェック
		// ----------------------
		List<MrPlanStatus> mrPlanStatusList;
		try {
			// 許可しないステータスリスト作成
			List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
			unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
			unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
			unallowableMrStatusList.add(MrStatusForCheck.ESTIMATED);
			unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_OPENED);
			unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_COMPLETED);
			unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_COMPLETED);

			// チェック対象の品目リスト作成
			List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
			plannedProdList.add(mrPlanUpdateDto.getPlannedProd());

			// ステータスチェック実行
			mrPlanStatusList = dpsMrStatusCheckService.execute(sosCd3, plannedProdList, unallowableMrStatusList);

		} catch (UnallowableStatusException e) {
			// ステータスエラー
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			if (fixFlg) {
				messageKeyList.add(new MessageKey("DPS3254E", "登録"));
			} else {
				messageKeyList.add(new MessageKey("DPS3254E", "確定"));
			}
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// 担当者別計画更新
		// ----------------------
		// 担当者別計画の更新
		List<MrPlanDto> mrPlanDtoList = mrPlanUpdateDto.getMrPlanDtoList();
		InsType insType = mrPlanUpdateDto.getInsType();
		for (MrPlanDto mrPlanDto : mrPlanDtoList) {
			mrPlanDao.update(mrPlanDto.convertMrPlan(insType), insType);
		}

		// ----------------------
		// チーム別入力状況登録・更新
		// ----------------------
		TeamInputStatus teamInputStatus = null;
		String prodCode = mrPlanUpdateDto.getPlannedProd().getProdCode();
		DpUser dpUser = DpUserInfo.getDpUserInfo().getLoginUser();
		Date sysTime = commonDao.getSystemTime();
		boolean teamInputStatusIsExist = false;
		try {
			// チーム別入力状況を検索
			teamInputStatus = teamInputStatusDao.search(sosCd4, prodCode);

			// チーム別入力状況存在フラグを存在するに設定
			teamInputStatusIsExist = true;

		} catch (DataNotFoundException e) {
			// チーム別入力状況が存在しない場合
			// 新規作成
			teamInputStatus = new TeamInputStatus();
			teamInputStatus.setSosCd(sosCd4);
			teamInputStatus.setProdCode(prodCode);

			// 登録者情報を設定
			teamInputStatus.setIsJgiNo(dpUser.getJgiNo());
			teamInputStatus.setIsJgiName(dpUser.getJgiName());

			// チーム別入力状況存在フラグを存在しないに設定
			teamInputStatusIsExist = false;
		}

		// 確定処理か判定
		if (fixFlg) {
			// 確定処理の場合、入力状況を担当者別計画入力完了に設定
			teamInputStatus.setInputStateForMrPlan(InputStateForMrPlan.MR_PLAN_INPUT_FINISHED);

			// 担当者別計画入力完了日時を設定
			teamInputStatus.setMrPlanInputDate(sysTime);

		} else {
			// 登録処理の場合、入力状況を担当者別計画入力中に設定
			teamInputStatus.setInputStateForMrPlan(InputStateForMrPlan.MR_PLAN_INPUTTING);

		}

		// 更新者情報を設定
		teamInputStatus.setUpJgiNo(dpUser.getJgiNo());
		teamInputStatus.setUpJgiName(dpUser.getJgiName());

		// チーム別入力状況を登録・更新する
		try {
			if (teamInputStatusIsExist) {
				// チーム別入力状況が既に存在する場合
				// 更新実行
				teamInputStatusDao.update(teamInputStatus);

			} else {
				// チーム別入力状況が存在しない場合
				// 登録実行
				teamInputStatusDao.insert(teamInputStatus);
			}
		} catch (DuplicateException e) {
			// 一意制約違反が発生した場合
			final String errMsg = "チーム別入力状況の登録に失敗";
			throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg), e);
		}

		// 確定処理か判定
		if (fixFlg) {
			// 確定処理の場合

			// ----------------------
			// 同一営業所に属するチームの
			// チーム別入力状況をチェック
			// ----------------------
			// 確定処理の場合、チーム別入力状況をチェックする
			List<SosMst> sosMstList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G, sosCd3);

			// 全チームの入力状況が担当者別計画入力完了かチェック
			boolean isAllInputFinished = true;
			for (SosMst teamSosMst : sosMstList) {
				try {
					// チーム別入力状況を検索
					teamInputStatus = teamInputStatusDao.search(teamSosMst.getSosCd(), prodCode);

					// 入力状況が担当者別計画入力完了か判定
					if (!InputStateForMrPlan.MR_PLAN_INPUT_FINISHED.equals(teamInputStatus.getInputStateForMrPlan())) {
						// 入力状況が担当者別計画入力完了以外の場合
						// １チームでも担当者別計画入力完了以外であれば、以降のチームはチェックしない。
						isAllInputFinished = false;
						break;
					}
				} catch (DataNotFoundException e) {
					// チーム別入力状況が存在しない場合、入力完ではない。
					// １チームでも担当者別計画入力完了以外であれば、以降のチームはチェックしない。
					isAllInputFinished = false;
					break;
				}
			}

			// ----------------------
			// 担当者別計画立案ステータスの更新
			// ----------------------
			// 全チームの入力状況が担当者別計画入力完了か判定
			if (isAllInputFinished) {
				// 全チームの入力状況が担当者別計画入力完了の場合
				// 担当者別計画立案ステータスを更新する（実際には1品目）
				for (MrPlanStatus mrPlanStatus : mrPlanStatusList) {
					// ステータスを担当者別計画入力完了に設定
					mrPlanStatus.setStatusForMrPlan(StatusForMrPlan.MR_PLAN_INPUT_FINISHED);

					// 担当者別計画入力完了日時を設定
					mrPlanStatus.setMrPlanInputDate(sysTime);

					// 更新者情報を設定
					mrPlanStatus.setUpJgiNo(dpUser.getJgiNo());
					mrPlanStatus.setUpJgiName(dpUser.getJgiName());

					// 担当者別計画立案ステータス更新実行
					mrPlanStatusDao.update(mrPlanStatus);
				}
			}

		} else {

			// 担当者別計画立案ステータスがAL入力完了の場合は、担当者別計画公開に戻す（実際には1品目）
			for (MrPlanStatus mrPlanStatus : mrPlanStatusList) {

				if(mrPlanStatus.getStatusForMrPlan() == StatusForMrPlan.MR_PLAN_INPUT_FINISHED){

					// ステータスを担当者別計画公開に設定
					mrPlanStatus.setStatusForMrPlan(StatusForMrPlan.MR_PLAN_OPENED);
					mrPlanStatus.setUpJgiNo(dpUser.getJgiNo());
					mrPlanStatus.setUpJgiName(dpUser.getJgiName());
					mrPlanStatusDao.update(mrPlanStatus);
				}
			}

		}

		// -----------------------------------------
		// チーム別計画更新(試算が営⇒担の場合のみ）
		// -----------------------------------------
		if (CalcType.OFFICE_MR.equals(officePlanStatus.getCalcType())) {
			teamPlanDao.updateByMrPlanSum(sosCd4, mrPlanUpdateDto.getPlannedProd().getProdCode(), dpUser);
		}
	}

	// チーム別計画の登録・更新を行う。
	public void updateTeamPlan(String sosCd3, TeamPlanUpdateDto teamPlanUpdateDto) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		// 組織コード(営業所)チェック
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 担当者別計画更新用DTOチェック
		if (teamPlanUpdateDto == null) {
			final String errMsg = "チーム別計画更新用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// チーム別計画の登録・更新後に試算を実行する。
		executeEsimationUpdateTeamPlan(sosCd3, teamPlanUpdateDto);
	}

	// 画面に表示する調整金額用のメッセージを生成
	public String createChoseiMsg(MrPlanResultDto dto) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (dto == null) {
			final String errMsg = "検索結果がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 調整金額有無[UH]
		boolean uhFlg = false;

		// 調整金額有無[P]
		boolean pFlg = false;

		// 上位計画
		Long jUH = dto.getPreviousPlanValueUH();
		Long jP = dto.getPreviousPlanValueP();

		// 積上値
		Long tUH = dto.getTotalPlanValueUH();
		Long tP = dto.getTotalPlanValueP();

		if (jUH == null)
			jUH = 0L;
		if (jP == null)
			jP = 0L;
		if (tUH == null)
			tUH = 0L;
		if (tP == null)
			tP = 0L;

		if (!jUH.equals(tUH)) {
			uhFlg = true;
		}
		if (!jP.equals(tP)) {
			pFlg = true;
		}

		return new CreateChoseiMsgLogic(uhFlg, pFlg).createMsg();
	}

	// 調整金額用のメッセージを生成
	public String createChoseiMsg(OfficeTeamPlanChoseiDto dto) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (dto == null) {
			final String errMsg = "検索結果がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 調整金額有無[UH]
		boolean uhFlg = dto.getChoseiUhFlg();

		// 調整金額有無[P]
		boolean pFlg = dto.getChoseiPFlg();

		return new CreateChoseiMsgLogic(uhFlg, pFlg).createMsg();
	}

	// チーム別計画の登録・更新後に試算処理を実行する。
	private void executeEsimationUpdateTeamPlan(String sosCd3, TeamPlanUpdateDto teamPlanUpdateDto) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		// 組織コード(営業所)チェック
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// 担当者別計画更新用DTOチェック
		if (teamPlanUpdateDto == null) {
			final String errMsg = "チーム別計画更新用DTOがnull";
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

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　組織マスタ.OncSosFlgでのカテゴリー判定を、組織マスタ.category、subCategoryを参照に変更
//		// 組織からカテゴリ判定（MMP or ONC）
//		ProdCategory category = ProdCategory.getSosCategory(sosMst.getOncSosFlg());
		// 組織からカテゴリ判定
//		ProdCategory category = ProdCategory.getInstance(sosMst.getSosCategory());
		String category = sosMst.getSosCategory();
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　組織マスタ.OncSosFlgでのカテゴリー判定を、組織マスタ.category、subCategoryを参照に変更

		// ---------------------------------
		// 営業所計画ステータスチェック
		// ---------------------------------
		List<OfficeStatusForCheck> unallowableOfficeStatusList = new ArrayList<OfficeStatusForCheck>();
		unallowableOfficeStatusList.add(OfficeStatusForCheck.NOTHING);
		unallowableOfficeStatusList.add(OfficeStatusForCheck.DRAFT);
		try {
			dpsOfficeStatusCheckService.executeForOffice(sosMst,category, unallowableOfficeStatusList);

		} catch (UnallowableStatusException e) {
			// メッセージ作成
			throw new UnallowableStatusException(new Conveyance(new MessageKey("DPS3242E")));
		}

		// ---------------------------------
		// 担当者別計画ステータスチェック
		// ---------------------------------
		List<MrPlanStatus> mrPlanStatusList = null;
		try {
			// 許可しないステータスリスト作成
			List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
			unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
			unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
			unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_COMPLETED);
			unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_OPENED);
			unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_INPUT_FINISHED);
			unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_COMPLETED);

			// チェック対象の品目リスト作成
			List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
			plannedProdList.add(teamPlanUpdateDto.getPlannedProd());

			// ステータスチェック実行
			mrPlanStatusList = dpsMrStatusCheckService.execute(sosCd3, plannedProdList, unallowableMrStatusList);

		} catch (UnallowableStatusException e) {
			// ステータスエラー
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3251E", "試算処理を実行"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// チーム別計画更新
		// ----------------------
		// チーム別計画の更新
		InsType insType = teamPlanUpdateDto.getInsType();
		List<TeamPlanDto> teamPlanDtoList = teamPlanUpdateDto.getTeamPlanDtoList();
		if (insType != null) {
			for (TeamPlanDto teamPlanDto : teamPlanDtoList) {
				teamPlanDao.update(teamPlanDto.convertTeamPlan(insType), insType);
			}
		}

		// 試算開始日時取得
		Date startDate = commonDao.getSystemTime();

		// 試算処理実行
		final String prodCode = teamPlanUpdateDto.getPlannedProd().getProdCode();
		final DpUser execDpUser = DpUserInfo.getDpUserInfo().getLoginUser();
		for (TeamPlanDto teamPlanDto : teamPlanDtoList) {
			dpsEstimationExecuteService.execute(teamPlanDto.getSosCd(), prodCode, execDpUser);
		}

		// 試算終了日時取得
		Date endDate = commonDao.getSystemTime();

		// ステータスに開始日時、終了日時を設定
		for (MrPlanStatus mrPlanStatus : mrPlanStatusList) {
			mrPlanStatus.setEstStartDate(startDate);
			mrPlanStatus.setEstEndDate(endDate);
			mrPlanStatus.setEstimationBasePlan(EstimationBasePlan.TEAM_PLAN);
			mrPlanStatusDao.update(mrPlanStatus);
		}
	}

	// 本部案に戻して再試算
	public void executeEsimationDeleteOffice(EstimationResultDetailExecDto estimationResultDetailExecDto) throws LogicalException {
		EstimationResultDetailEstimationLogic updateLogic = new EstimationResultDetailEstimationLogic(estimationResultDetailExecDto, dpsOfficeStatusCheckService,
			dpsMrStatusCheckService, dpsEstimationExecuteService, estParamHonbuDao, estParamOfficeDao, commonDao, mrPlanStatusDao, sosMstDAO);
		updateLogic.executeEsimationDeleteOffice();
	}

	// 営業所案を登録して再試算
	public void executeEsimationUpdateOffice(EstimationResultDetailExecDto estimationResultDetailExecDto) throws LogicalException {
		EstimationResultDetailEstimationLogic updateLogic = new EstimationResultDetailEstimationLogic(estimationResultDetailExecDto, dpsOfficeStatusCheckService,
			dpsMrStatusCheckService, dpsEstimationExecuteService, estParamHonbuDao, estParamOfficeDao, commonDao, mrPlanStatusDao, sosMstDAO);
		updateLogic.executeEsimationUpdateOffice();
	}

	/**
	 * チーム別入力状況を削除する。
	 *
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProdList 削除対象の品目リスト
	 * @throws DataNotFoundException
	 */
	private void deleteTeamInputState(String sosCd3, List<PlannedProd> plannedProdList) throws DataNotFoundException {
		// チーム一覧を取得
		List<SosMst> teamList = null;
		try {
			teamList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST, null, sosCd3);
		} catch (DataNotFoundException e) {
//			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, "チーム一覧取得失敗"), e);
			teamList = new ArrayList<SosMst>();
		}

		for (PlannedProd plannedProd : plannedProdList) {
			String prodCode = plannedProd.getProdCode();
			for (SosMst team : teamList) {
				String sosCd4 = team.getSosCd();
				try {
					TeamInputStatus teamInputStatus = teamInputStatusDao.search(sosCd4, prodCode);
					teamInputStatusDao.delete(teamInputStatus.getSeqKey(), teamInputStatus.getUpDate());
				} catch (DataNotFoundException e) {
					// 存在しない場合もエラーにしない
				}
			}
		}
	}

	// 計画値の一括コピーを行う。
	public void executePlannedValueCopy(PlannedValueCopyDto copyDto) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (copyDto == null) {
			final String errMsg = "計画値の一括コピー処理実行用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		String sosCd3 = copyDto.getSosCd3();
		List<PlannedProd> plannedProdList = copyDto.getPlannedProdList();
		CopyTarget copyTarget = copyDto.getCopyTarget();
		CopyWay copyWay = copyDto.getCopyWay();
		String category = copyDto.getCategory();

		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null || plannedProdList.size() == 0) {
			final String errMsg = "品目リストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (copyTarget == null) {
			final String errMsg = "コピー対象がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (copyWay == null) {
			final String errMsg = "コピー方法がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}


		// ユーザ情報取得
		DpUser dpUser = DpUserInfo.getDpUserInfo().getLoginUser();

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

		// 営業所計画ステータス取得
		OfficePlanStatus officePlanStatus = officePlanStatusDao.search(sosCd3, category);

		// 試算タイプとコピー対象のチェック
		CalcType calcType = officePlanStatus.getCalcType();

		// 一括コピー時、試算タイプ未設定の場合
		if (calcType == null) {
			throw new LogicalException(new Conveyance(new MessageKey("DPS3245E")));
		}

		// 試算タイプと、コピー対象のチェック
		if (calcType.equals(CalcType.OFFICE_MR) && copyTarget.equals(CopyTarget.TEAM_PLAN)) {
			throw new LogicalException(new Conveyance(new MessageKey("DPS3246E")));
		}

		// ---------------------------------
		// 営業所計画ステータスチェック
		// ---------------------------------
		// コピー対象が、チーム別計画の場合は営業所計画のステータスチェックを行う。
		if (copyTarget.equals(CopyTarget.TEAM_PLAN)) {

			// 営業所計画ステータスチェック
			List<OfficeStatusForCheck> unallowableOfficeStatusList = new ArrayList<OfficeStatusForCheck>();
			unallowableOfficeStatusList.add(OfficeStatusForCheck.NOTHING);
			unallowableOfficeStatusList.add(OfficeStatusForCheck.DRAFT);
			try {
				dpsOfficeStatusCheckService.executeForOffice(sosMst, category, unallowableOfficeStatusList);

			} catch (UnallowableStatusException e) {
				// メッセージ作成
				throw new UnallowableStatusException(new Conveyance(new MessageKey("DPS3247E")));
			}
		}

		// ---------------------------------
		// 担当者別計画ステータスチェック
		// ---------------------------------
		List<MrPlanStatus> mrPlanStatusList = null;
		try {
			// 許可しないステータスリスト作成
			List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
			// 試算タイプ・コピー対象によって、許可ステータスが異なる
			switch (copyTarget) {
				case MR_PLAN:
					switch (calcType) {
						case OFFICE_TEAM_MR:
							unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
							unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
							unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_COMPLETED);
							break;
						case OFFICE_MR:
							unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
							unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
							unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_COMPLETED);
							break;
					}
					break;

				case TEAM_PLAN:
					unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
					unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
					unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_COMPLETED);
					unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_OPENED);
					unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_INPUT_FINISHED);
					unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_COMPLETED);
					break;
			}
			// ステータスチェック実行
			mrPlanStatusList = dpsMrStatusCheckService.execute(sosCd3, plannedProdList, unallowableMrStatusList);

		} catch (UnallowableStatusException e) {
			// ステータスエラー
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3248E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}

		// ----------------------
		// 計画値の一括コピー処理
		// ----------------------
		// チームリスト取得
		List<SosMst> teamList;
		try {
			teamList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G, sosCd3);
		} catch (DataNotFoundException e) {

			// チームがない場合もNGにしない（チーム別計画への積上げなし）
			teamList = new ArrayList<SosMst>();
		}

		switch (copyTarget) {
			case MR_PLAN:
				// 従業員リスト取得
				List<JgiMst> jgiList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd3, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G);
				for (PlannedProd plannedProd : plannedProdList) {
					String prodCode = plannedProd.getProdCode();
					// 計画値の一括コピー
					for (JgiMst jgiMst : jgiList) {
						Integer jgiNo = jgiMst.getJgiNo();
						MrPlan mrPlan;
						try {
							mrPlan = mrPlanDao.searchUk(jgiNo, prodCode);
						} catch (Exception e) {
							continue;
						}
						// 特定施設個別計画
						Long spInsSumUh = mrPlan.getSpecialInsPlanValueUhY();
						Long spInsSumP = mrPlan.getSpecialInsPlanValuePY();
						// コピー方法によって、更新項目が異なる
						switch (copyWay) {
							case THEORETICAL1_TO_OFFICE:
								mrPlan.setOfficeValueUhY(minusToZero(mrPlan.getTheoreticalValueUh1(), spInsSumUh));
								mrPlan.setOfficeValuePY(minusToZero(mrPlan.getTheoreticalValueP1(), spInsSumP));
								break;
							case THEORETICAL2_TO_OFFICE:
								mrPlan.setOfficeValueUhY(minusToZero(mrPlan.getTheoreticalValueUh2(), spInsSumUh));
								mrPlan.setOfficeValuePY(minusToZero(mrPlan.getTheoreticalValueP2(), spInsSumP));
								break;
							case THEORETICAL1_TO_PLAN:
								mrPlan.setPlannedValueUhY(minusToZero(mrPlan.getTheoreticalValueUh1(), spInsSumUh));
								mrPlan.setPlannedValuePY(minusToZero(mrPlan.getTheoreticalValueP1(), spInsSumP));
								break;
							case THEORETICAL2_TO_PLAN:
								mrPlan.setPlannedValueUhY(minusToZero(mrPlan.getTheoreticalValueUh2(), spInsSumUh));
								mrPlan.setPlannedValuePY(minusToZero(mrPlan.getTheoreticalValueP2(), spInsSumP));
								break;
							case OFFICE_TO_PLAN:
								mrPlan.setPlannedValueUhY(mrPlan.getOfficeValueUhY());
								mrPlan.setPlannedValuePY(mrPlan.getOfficeValuePY());
								break;
						}
						// 更新実行
						mrPlanDao.update(mrPlan, null);
					}
					// 試算方法が、営業所別計画⇒担当者別計画の場合は、チーム別計画に積上げ
					if (calcType.equals(CalcType.OFFICE_MR)) {
						for (SosMst team : teamList) {
							teamPlanDao.updateByMrPlanSum(team.getSosCd(), plannedProd.getProdCode(), dpUser);
						}
					}
				}
				break;
			case TEAM_PLAN:
				for (SosMst team : teamList) {
					String sosCd4 = team.getSosCd();
					for (PlannedProd plannedProd : plannedProdList) {
						String prodCode = plannedProd.getProdCode();
						// チーム別計画取得
						TeamPlan teamPlan;
						try {
							teamPlan = teamPlanDao.searchUk(sosCd4, prodCode);
						} catch (Exception e) {
							continue;
						}
						// チーム内の特定施設個別計画サマリー
						Long spInsSumUh = teamPlan.getSpecialInsPlanValueUhY();
						Long spInsSumP = teamPlan.getSpecialInsPlanValuePY();
						// 計画値のコピー
						// コピー方法によって、更新項目が異なる
						switch (copyWay) {
							case THEORETICAL1_TO_OFFICE:
								teamPlan.setOfficeValueUhY(minusToZero(teamPlan.getTheoreticalValueUh1(), spInsSumUh));
								teamPlan.setOfficeValuePY(minusToZero(teamPlan.getTheoreticalValueP1(), spInsSumP));
								break;
							case THEORETICAL2_TO_OFFICE:
								teamPlan.setOfficeValueUhY(minusToZero(teamPlan.getTheoreticalValueUh2(), spInsSumUh));
								teamPlan.setOfficeValuePY(minusToZero(teamPlan.getTheoreticalValueP2(), spInsSumP));
								break;
							case THEORETICAL1_TO_PLAN:
								teamPlan.setPlannedValueUhY(minusToZero(teamPlan.getTheoreticalValueUh1(), spInsSumUh));
								teamPlan.setPlannedValuePY(minusToZero(teamPlan.getTheoreticalValueP1(), spInsSumP));
								break;
							case THEORETICAL2_TO_PLAN:
								teamPlan.setPlannedValueUhY(minusToZero(teamPlan.getTheoreticalValueUh2(), spInsSumUh));
								teamPlan.setPlannedValuePY(minusToZero(teamPlan.getTheoreticalValueP2(), spInsSumP));
								break;
							case OFFICE_TO_PLAN:
								teamPlan.setPlannedValueUhY(teamPlan.getOfficeValueUhY());
								teamPlan.setPlannedValuePY(teamPlan.getOfficeValuePY());
								break;
						}
						teamPlanDao.update(teamPlan, null);
					}
				}
				// 試算開始日時取得
				Date startDate = commonDao.getSystemTime();
				for (SosMst team : teamList) {
					for (PlannedProd plannedProd : plannedProdList) {
						// 再計算処理（試算処理）
						dpsEstimationExecuteService.execute(team.getSosCd(), plannedProd.getProdCode(), dpUser);
					}
				}
				// 試算終了日時取得
				Date endDate = commonDao.getSystemTime();

				// ステータスに開始日時、終了日時を設定
				for (MrPlanStatus mrPlanStatus : mrPlanStatusList) {
					mrPlanStatus.setEstStartDate(startDate);
					mrPlanStatus.setEstEndDate(endDate);
					mrPlanStatus.setEstimationBasePlan(EstimationBasePlan.TEAM_PLAN);
					mrPlanStatusDao.update(mrPlanStatus);
				}
				break;
		}
	}

	/**
	 * 合計値がマイナスの場合、ゼロに置き換える。
	 *
	 * @param value1 値1
	 * @param value2 値2
	 * @return 合算した値(マイナスの場合0に変換)
	 */
	private Long minusToZero(Long value1, Long value2) {
		Long result = MathUtil.add(value1, value2);
		if (result != null && result < 0) {
			result = 0L;
		}
		return result;
	}

	/**
	 * 逆鞘チェック
	 *
	 *
	 * @param inputList 担当者別計画Dto
	 * @param planType 営業所案or担当者案
	 */
	private List<String> modifyReverseSheathCheck(List<MrPlanDto> inputList, InsType insType) {

		List<String> jgiNameList = new ArrayList<String>();

		for (MrPlanDto dto : inputList) {
			// 担当者別計画取得
			MrPlan mrPlan = new MrPlan();
			try {
				mrPlan = mrPlanDao.search(dto.getSeqKey());
			}catch (DataNotFoundException e){
				// 担当者別計画が存在しない場合でも0の計画としてエラーチェックする
			}
			Long plannedValue = ConvertUtil.parseMoneyToNormalUnit(dto.getPlannedValueDto().getPlannedValueY());

			// NULLの場合はゼロとして扱う
			if (plannedValue == null) {
				plannedValue = 0L;
			}

			// 特定施設個別計画取得
//			List<SpecialInsPlan> spInsList = new ArrayList<SpecialInsPlan>();
//			try {
//				spInsList = specialInsPlanDao.searchByJgiNo(null, mrPlan.getJgiNo(), mrPlan.getProdCode(), PlanType.MR);
//			} catch (DataNotFoundException e) {
//			}

			// NULLの場合はゼロとして扱うため、ゼロ宣言
			Long spInsValue = 0L;
//			for (SpecialInsPlan specialInsPlan : spInsList) {
//				spInsValue = MathUtil.add(spInsValue, specialInsPlan.getPlannedValueY());
//			}
			// 担当者別計画(DPS_I_MR_PLAN)の特定施設個別計画価
			if (insType.equals(InsType.UH)) {
				if(mrPlan.getSpecialInsPlanValueUhY() == null) {
					spInsValue = 0L;
				}else {
					spInsValue = mrPlan.getSpecialInsPlanValueUhY();
				}
			}else if (insType.equals(InsType.P)) {
				if (mrPlan.getSpecialInsPlanValuePY() == null) {
					spInsValue = 0L;
				}else {
					spInsValue = mrPlan.getSpecialInsPlanValuePY();
				}
			}

			if (spInsValue > plannedValue) {
				String jgiName = null;
				try {
					jgiName = jgiMstDAO.search(mrPlan.getJgiNo()).getJgiName();
				} catch (DataNotFoundException e) {
				}
				if (jgiName != null) {
					jgiNameList.add(jgiName);
				}
			}
		}

		return jgiNameList;
	}

}
