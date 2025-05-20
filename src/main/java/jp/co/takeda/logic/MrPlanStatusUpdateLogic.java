package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.dao.DpsCodeMasterDao;
import jp.co.takeda.dao.DpsPlannedCtgDao;
import jp.co.takeda.dao.MrPlanDao;
import jp.co.takeda.dao.MrPlanStatusDao;
import jp.co.takeda.dao.OfficePlanDao;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dto.MrPlanStatusUpdateDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.div.InsDocStatusForCheck;
import jp.co.takeda.logic.div.InsWsStatusForCheck;
import jp.co.takeda.logic.div.MrStatusForCheck;
import jp.co.takeda.logic.div.OfficeStatusForCheck;
import jp.co.takeda.logic.div.PlanLevel;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.DpsPlannedCtg;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.model.MrPlanStatus;
import jp.co.takeda.model.OfficePlan;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.div.StatusForMrPlan;
import jp.co.takeda.service.DpsInsDocStatusCheckService;
import jp.co.takeda.service.DpsInsWsStatusCheckService;
import jp.co.takeda.service.DpsMrStatusCheckService;
import jp.co.takeda.service.DpsOfficeStatusCheckService;

/**
 * 担当者別計画ステータスの更新ロジッククラス
 *
 * @author stakeuchi
 */
public class MrPlanStatusUpdateLogic {

	/**
	 * 営業所計画ステータスチェックサービス
	 */
	private final DpsOfficeStatusCheckService dpsOfficeStatusCheckService;

	/**
	 * 担当者別計画ステータスチェックサービス
	 */
	private final DpsMrStatusCheckService dpsMrStatusCheckService;

	/**
	 * 施設医師別計画ステータスチェックサービス
	 */
	private final DpsInsDocStatusCheckService dpsInsDocStatusCheckService;

	/**
	 * 施設特約店別計画ステータスチェックサービス
	 */
	private final DpsInsWsStatusCheckService dpsInsWsStatusCheckService;

	/**
	 * 担当者別計画ステータスDAO
	 */
	private final MrPlanStatusDao mrPlanStatusDao;

	/**
	 * 営業所計画DAO
	 */
	private final OfficePlanDao officePlanDao;

	/**
	 * 担当者計画DAO
	 */
	private final MrPlanDao mrPlanDao;

	/**
	 * 共通DAO
	 */
	private final CommonDao commonDao;

	/**
	 * 組織情報DAO
	 */
	private final SosMstDAO sosMstDao;

	/**
	 * 計画支援汎用マスタDAO
	 */
	private final DpsCodeMasterDao dpsCodeMasterDao;

	/**
	 * 計画対象カテゴリ領域DAO
	 */

	private final DpsPlannedCtgDao dpsPlannedCtgDao;
	/**
	 * コンストラクタ
	 *
	 * @param dpsOfficeStatusCheckService 営業所計画ステータスチェックサービス
	 * @param dpsMrStatusCheckService 担当者別計画ステータスチェックサービス
	 * @param dpsInsDocStatusCheckService 施設医師別計画ステータスチェックサービス
	 * @param dpsInsWsStatusCheckService 施設特約店別計画ステータスチェックサービス
	 * @param mrPlanStatusDao 担当者別計画ステータスDAO
	 * @param commonDao 共通DAO
	 * @param sosMstDao 組織情報DAO
	 * @param dpsCodeMasterDao 計画支援汎用マスタDAO
	 * @param dpsPlannedCtgDao 計画対象カテゴリ領域DAO
	 */
	public MrPlanStatusUpdateLogic(DpsOfficeStatusCheckService dpsOfficeStatusCheckService, DpsMrStatusCheckService dpsMrStatusCheckService,DpsInsDocStatusCheckService dpsInsDocStatusCheckService,
		DpsInsWsStatusCheckService dpsInsWsStatusCheckService, MrPlanStatusDao mrPlanStatusDao, OfficePlanDao officePlanDao, MrPlanDao mrPlanDao, CommonDao commonDao, SosMstDAO sosMstDao, DpsCodeMasterDao dpsCodeMasterDao, DpsPlannedCtgDao dpsPlannedCtgDao
		) {
		this.dpsOfficeStatusCheckService = dpsOfficeStatusCheckService;
		this.dpsMrStatusCheckService = dpsMrStatusCheckService;
		this.dpsInsDocStatusCheckService = dpsInsDocStatusCheckService;
		this.dpsInsWsStatusCheckService = dpsInsWsStatusCheckService;
		this.mrPlanStatusDao = mrPlanStatusDao;
		this.officePlanDao = officePlanDao;
		this.mrPlanDao = mrPlanDao;
		this.commonDao = commonDao;
		this.sosMstDao = sosMstDao;
		this.dpsCodeMasterDao = dpsCodeMasterDao;
		this.dpsPlannedCtgDao = dpsPlannedCtgDao;
	}

	/**
	 * ステータスの更新を行う。<br>
	 * 以下の処理を行ない、チーム別計画を「公開解除」状態に更新する。
	 * <ul>
	 * <li>担当者別計画のステータスチェック</li>
	 * <li>担当者別計画ステータスの更新</li>
	 * </ul>
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProdList 計画対象品目リスト
	 * @param updateDtoList 担当者別計画ステータス更新用DTOリスト
	 * @throws LogicalException
	 */
	public void updateStatusTeamOpenRelease(String sosCd3, List<PlannedProd> plannedProdList, List<MrPlanStatusUpdateDto> updateDtoList) throws LogicalException {

		// 引数チェック
		checkParameter(sosCd3, plannedProdList, updateDtoList);

		// 担当者別計画ステータスチェック
		List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
		unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
		unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
		unallowableMrStatusList.add(MrStatusForCheck.ESTIMATED);
		unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_COMPLETED);
		unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_OPENED);
		unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_INPUT_FINISHED);
		unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_COMPLETED);
		checkMrStatus(sosCd3, plannedProdList, unallowableMrStatusList, "DPS3220E");

		// ----------------------
		// 更新実行
		// ----------------------
		for (MrPlanStatusUpdateDto updateDto : updateDtoList) {
			checkUpdateDto(updateDto);
			final Long seqKey = updateDto.getStatusSeqKey();
			final Date upDate = updateDto.getStatusUpDate();
			MrPlanStatus mrPlanStatus = null;
			mrPlanStatus = mrPlanStatusDao.search(seqKey);
			mrPlanStatus.setUpDate(upDate);
			mrPlanStatus.setStatusForMrPlan(StatusForMrPlan.ESTIMATED);
			mrPlanStatusDao.update(mrPlanStatus);
		}
	}

	/**
	 * ステータスの更新を行う。<br>
	 * 以下の処理を行ない、チーム別計画を「公開」状態に更新する。
	 * <ul>
	 * <li>担当者別計画のステータスチェック</li>
	 * <li>担当者別計画ステータスの更新</li>
	 * </ul>
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProdList 計画対象品目リスト
	 * @param updateDtoList 担当者別計画ステータス更新用DTOリスト
	 * @throws LogicalException
	 */
	public void updateStatusTeamOpen(String sosCd3, List<PlannedProd> plannedProdList, List<MrPlanStatusUpdateDto> updateDtoList) throws LogicalException {

		// 引数チェック
		checkParameter(sosCd3, plannedProdList, updateDtoList);

		// 担当者別計画ステータスチェック
		List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
		unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
		unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
		unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_OPENED);
		unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_COMPLETED);
		unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_OPENED);
		unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_INPUT_FINISHED);
		unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_COMPLETED);
		checkMrStatus(sosCd3, plannedProdList, unallowableMrStatusList, "DPS3221E");

		// ----------------------
		// 更新実行
		// ----------------------
		for (MrPlanStatusUpdateDto updateDto : updateDtoList) {
			checkUpdateDto(updateDto);
			final Long seqKey = updateDto.getStatusSeqKey();
			final Date upDate = updateDto.getStatusUpDate();
			MrPlanStatus mrPlanStatus = null;
			mrPlanStatus = mrPlanStatusDao.search(seqKey);
			mrPlanStatus.setUpDate(upDate);
			mrPlanStatus.setStatusForMrPlan(StatusForMrPlan.TEAM_PLAN_OPENED);
			mrPlanStatus.setTeamPlanOpenDate(commonDao.getSystemTime());
			mrPlanStatusDao.update(mrPlanStatus);
		}
	}

	/**
	 * ステータスの更新を行う。<br>
	 * 以下の処理を行ない、チーム別計画を「確定解除」状態に更新する。
	 * <ul>
	 * <li>担当者別計画のステータスチェック</li>
	 * <li>担当者別計画ステータスの更新</li>
	 * </ul>
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProdList 計画対象品目リスト
	 * @param updateDtoList 担当者別計画ステータス更新用DTOリスト
	 * @throws LogicalException
	 */
	public void updateStatusTeamFixRelease(String sosCd3, List<PlannedProd> plannedProdList, List<MrPlanStatusUpdateDto> updateDtoList) throws LogicalException {

		// 引数チェック
		checkParameter(sosCd3, plannedProdList, updateDtoList);

		// 担当者別計画ステータスチェック
		List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
		unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
		unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
		unallowableMrStatusList.add(MrStatusForCheck.ESTIMATED);
		unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_OPENED);
		unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_OPENED);
		unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_INPUT_FINISHED);
		unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_COMPLETED);
		checkMrStatus(sosCd3, plannedProdList, unallowableMrStatusList, "DPS3222E");

		// ----------------------
		// 更新実行
		// ----------------------
		for (MrPlanStatusUpdateDto updateDto : updateDtoList) {
			checkUpdateDto(updateDto);
			final Long seqKey = updateDto.getStatusSeqKey();
			final Date upDate = updateDto.getStatusUpDate();
			MrPlanStatus mrPlanStatus = null;
			mrPlanStatus = mrPlanStatusDao.search(seqKey);
			mrPlanStatus.setUpDate(upDate);
			mrPlanStatus.setStatusForMrPlan(StatusForMrPlan.TEAM_PLAN_OPENED);
			mrPlanStatusDao.update(mrPlanStatus);
		}
	}

	/**
	 * ステータスの更新を行う。<br>
	 * 以下の処理を行ない、チーム別計画を「確定」状態に更新する。
	 * <ul>
	 * <li>営業所計画のステータスチェック</li>
	 * <li>担当者別計画のステータスチェック</li>
	 * <li>担当者別計画ステータスの更新</li>
	 * </ul>
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProdList 計画対象品目リスト
	 * @param updateDtoList 担当者別計画ステータス更新用DTOリスト
	 * @throws LogicalException
	 */
	public void updateStatusTeamFix(String sosCd3, List<PlannedProd> plannedProdList, List<MrPlanStatusUpdateDto> updateDtoList) throws LogicalException {

		// 引数チェック
		checkParameter(sosCd3, plannedProdList, updateDtoList);

		// 調整金額チェック
		// checkTuneAmount(sosCd3, plannedProdList);

		// 営業所計画ステータスチェック
		List<OfficeStatusForCheck> unallowableOfficeStatusList = new ArrayList<OfficeStatusForCheck>();
		unallowableOfficeStatusList.add(OfficeStatusForCheck.NOTHING);
		unallowableOfficeStatusList.add(OfficeStatusForCheck.DRAFT);
		checkOfficeStatus(sosCd3, plannedProdList, unallowableOfficeStatusList, "DPS3223E");

		// 担当者別計画ステータスチェック
		List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
		unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
		unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
		unallowableMrStatusList.add(MrStatusForCheck.ESTIMATED);
		unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_COMPLETED);
		unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_OPENED);
		unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_INPUT_FINISHED);
		unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_COMPLETED);
		checkMrStatus(sosCd3, plannedProdList, unallowableMrStatusList, "DPS3224E");

		// ----------------------
		// 更新実行
		// ----------------------
		for (MrPlanStatusUpdateDto updateDto : updateDtoList) {
			checkUpdateDto(updateDto);
			final Long seqKey = updateDto.getStatusSeqKey();
			final Date upDate = updateDto.getStatusUpDate();
			MrPlanStatus mrPlanStatus = null;
			mrPlanStatus = mrPlanStatusDao.search(seqKey);
			mrPlanStatus.setUpDate(upDate);
			mrPlanStatus.setStatusForMrPlan(StatusForMrPlan.TEAM_PLAN_COMPLETED);
			mrPlanStatus.setTeamPlanFixDate(commonDao.getSystemTime());
			mrPlanStatusDao.update(mrPlanStatus);
		}
	}

	/**
	 * ステータスの更新を行う。<br>
	 * 以下の処理を行ない、担当者別計画を「公開解除」状態に更新する。
	 * <ul>
	 * <li>担当者別計画のステータスチェック</li>
	 * <li>担当者別計画ステータスの更新</li>
	 * </ul>
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProdList 計画対象品目リスト
	 * @param updateDtoList 担当者別計画ステータス更新用DTOリスト
	 * @throws LogicalException
	 */
	public void updateStatusMrOpenRelease(String sosCd3, List<PlannedProd> plannedProdList, List<MrPlanStatusUpdateDto> updateDtoList) throws LogicalException {

		// 引数チェック
		checkParameter(sosCd3, plannedProdList, updateDtoList);

		// 担当者別計画ステータスチェック
		List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
		unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
		unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
		unallowableMrStatusList.add(MrStatusForCheck.ESTIMATED);
		unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_OPENED);
		unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_COMPLETED);
		unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_COMPLETED);
		checkMrStatus(sosCd3, plannedProdList, unallowableMrStatusList, "DPS3225E");

		// ----------------------
		// 更新実行
		// ----------------------
		for (MrPlanStatusUpdateDto updateDto : updateDtoList) {
			checkUpdateDto(updateDto);
			final Long seqKey = updateDto.getStatusSeqKey();
			final Date upDate = updateDto.getStatusUpDate();
			MrPlanStatus mrPlanStatus = null;
			mrPlanStatus = mrPlanStatusDao.search(seqKey);
			mrPlanStatus.setUpDate(upDate);
			mrPlanStatus.setStatusForMrPlan(StatusForMrPlan.TEAM_PLAN_COMPLETED);
			mrPlanStatusDao.update(mrPlanStatus);
		}
	}

	/**
	 * ステータスの更新を行う。<br>
	 * 以下の処理を行ない、担当者別計画を「公開」状態に更新する。
	 * <ul>
	 * <li>担当者別計画のステータスチェック</li>
	 * <li>担当者別計画ステータスの更新</li>
	 * </ul>
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProdList 計画対象品目リスト
	 * @param updateDtoList 担当者別計画ステータス更新用DTOリスト
	 * @throws LogicalException
	 */
	public void updateStatusMrOpen(String sosCd3, List<PlannedProd> plannedProdList, List<MrPlanStatusUpdateDto> updateDtoList) throws LogicalException {

		// 引数チェック
		checkParameter(sosCd3, plannedProdList, updateDtoList);

		// 担当者別計画ステータスチェック
		List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
		unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
		unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
		unallowableMrStatusList.add(MrStatusForCheck.ESTIMATED);
		unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_OPENED);
		unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_OPENED);
		unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_INPUT_FINISHED);
		unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_COMPLETED);
		checkMrStatus(sosCd3, plannedProdList, unallowableMrStatusList, "DPS3226E");

		// ----------------------
		// 更新実行
		// ----------------------
		for (MrPlanStatusUpdateDto updateDto : updateDtoList) {
			checkUpdateDto(updateDto);
			final Long seqKey = updateDto.getStatusSeqKey();
			final Date upDate = updateDto.getStatusUpDate();
			MrPlanStatus mrPlanStatus = null;
			mrPlanStatus = mrPlanStatusDao.search(seqKey);
			mrPlanStatus.setUpDate(upDate);
			mrPlanStatus.setStatusForMrPlan(StatusForMrPlan.MR_PLAN_OPENED);
			mrPlanStatus.setMrPlanOpenDate(commonDao.getSystemTime());
			mrPlanStatusDao.update(mrPlanStatus);
		}
	}

	/**
	 * ステータスの更新を行う。<br>
	 * 以下の処理を行ない、担当者別計画を「確定解除」状態に更新する。
	 * <ul>
	 * <li>担当者別計画のステータスチェック</li>
	 * <li>施設特約店別計画のステータスチェック</li>
	 * <li>担当者別計画ステータスの更新</li>
	 * </ul>
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProdList 計画対象品目リスト
	 * @param updateDtoList 担当者別計画ステータス更新用DTOリスト
	 * @throws LogicalException
	 */
	public void updateStatusMrFixRelease(String sosCd3, List<PlannedProd> plannedProdList, List<MrPlanStatusUpdateDto> updateDtoList) throws LogicalException {

		// 引数チェック
		checkParameter(sosCd3, plannedProdList, updateDtoList);

		// 担当者別計画ステータスチェック
		List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
		unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
		unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
		unallowableMrStatusList.add(MrStatusForCheck.ESTIMATED);
		unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_OPENED);
		unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_COMPLETED);
		unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_OPENED);
		unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_INPUT_FINISHED);
		checkMrStatus(sosCd3, plannedProdList, unallowableMrStatusList, "DPS3227E");

		// 施設医師別計画ステータスチェック
		List<InsDocStatusForCheck> unallowableInsDocStatusList = new ArrayList<InsDocStatusForCheck>();
		unallowableInsDocStatusList.add(InsDocStatusForCheck.DISTING);
		unallowableInsDocStatusList.add(InsDocStatusForCheck.PLAN_OPENED);
//		unallowableInsDocStatusList.add(InsDocStatusForCheck.PLAN_COMPLETED);
		checkInsDocStatus(sosCd3, plannedProdList, unallowableInsDocStatusList, "DPS3339E");

		// 施設特約店別計画ステータスチェック
		List<InsWsStatusForCheck> unallowableInsWsStatusList = new ArrayList<InsWsStatusForCheck>();
		unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTING);
		unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_COMPLETED);
		checkInsWsStatus(sosCd3, plannedProdList, unallowableInsWsStatusList, "DPS3228E");

		// ----------------------
		// 更新実行
		// ----------------------
		for (MrPlanStatusUpdateDto updateDto : updateDtoList) {
			checkUpdateDto(updateDto);
			final Long seqKey = updateDto.getStatusSeqKey();
			final Date upDate = updateDto.getStatusUpDate();
			MrPlanStatus mrPlanStatus = null;
			mrPlanStatus = mrPlanStatusDao.search(seqKey);
			mrPlanStatus.setUpDate(upDate);

			// 担当者別計画入力状況:入力完了ではない場合：担当者別計画立案ステータス ⇒ 担当者別計画公開
			mrPlanStatus.setStatusForMrPlan(StatusForMrPlan.MR_PLAN_OPENED);
			mrPlanStatus.setMrPlanInputDate(null);
			mrPlanStatusDao.update(mrPlanStatus);
		}
	}

	/**
	 * ステータスの更新を行う。<br>
	 * 以下の処理を行ない、担当者別計画を「確定」状態に更新する。
	 * <ul>
	 * <li>営業所計画のステータスチェック</li>
	 * <li>担当者別計画のステータスチェック</li>
	 * <li>担当者別計画ステータスの更新</li>
	 * </ul>
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProdList 計画対象品目リスト
	 * @param updateDtoList 担当者別計画ステータス更新用DTOリスト
	 * @throws LogicalException
	 */
	public void updateStatusMrFix(String sosCd3, List<PlannedProd> plannedProdList, List<MrPlanStatusUpdateDto> updateDtoList) throws LogicalException {

		// 引数チェック
		checkParameter(sosCd3, plannedProdList, updateDtoList);

		// 調整金額チェック
		// checkTuneAmount(sosCd3, plannedProdList);

		// 営業所別計画ステータスチェック
		List<OfficeStatusForCheck> unallowableOfficeStatusList = new ArrayList<OfficeStatusForCheck>();
		unallowableOfficeStatusList.add(OfficeStatusForCheck.NOTHING);
		unallowableOfficeStatusList.add(OfficeStatusForCheck.DRAFT);
		checkOfficeStatus(sosCd3, plannedProdList, unallowableOfficeStatusList, "DPS3229E");

		// 担当者別計画ステータスチェック
		List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
		unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
		unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
		unallowableMrStatusList.add(MrStatusForCheck.ESTIMATED);
		unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_OPENED);
		unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_COMPLETED);
		checkMrStatus(sosCd3, plannedProdList, unallowableMrStatusList, "DPS3230E");

		// ----------------------
		// 更新実行
		// ----------------------
		for (MrPlanStatusUpdateDto updateDto : updateDtoList) {
			checkUpdateDto(updateDto);
			final Long seqKey = updateDto.getStatusSeqKey();
			final Date upDate = updateDto.getStatusUpDate();
			MrPlanStatus mrPlanStatus = null;
			mrPlanStatus = mrPlanStatusDao.search(seqKey);
			mrPlanStatus.setUpDate(upDate);
			mrPlanStatus.setStatusForMrPlan(StatusForMrPlan.MR_PLAN_COMPLETED);
			Date date = commonDao.getSystemTime();
			mrPlanStatus.setMrPlanFixDate(date);
			if (mrPlanStatus.getMrPlanInputDate() == null) {
				mrPlanStatus.setMrPlanInputDate(date);
			}
			mrPlanStatusDao.update(mrPlanStatus);
		}
	}

	/**
	 * 更新処理呼び出し時の引数チェック
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProdList 計画対象品目リスト
	 * @param updateDtoList 担当者別計画ステータス更新用DTOリスト
	 * @throws SystemException 例外
	 */
	private void checkParameter(String sosCd3, List<PlannedProd> plannedProdList, List<MrPlanStatusUpdateDto> updateDtoList) throws SystemException {
		if (dpsOfficeStatusCheckService == null) {
			final String errMsg = "営業所計画ステータスチェックにアクセスするサービスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dpsMrStatusCheckService == null) {
			final String errMsg = "担当者別計画ステータスチェックにアクセスするサービスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dpsInsDocStatusCheckService == null) {
			final String errMsg = "施設医師別計画ステータスチェックにアクセスするサービスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dpsInsWsStatusCheckService == null) {
			final String errMsg = "施設特約店別計画ステータスチェックにアクセスするサービスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (mrPlanStatusDao == null) {
			final String errMsg = "担当者別計画ステータスにアクセスするDAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (officePlanDao == null) {
			final String errMsg = "営業所計画にアクセスするDAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (mrPlanDao == null) {
			final String errMsg = "担当者計画にアクセスするDAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (commonDao == null) {
			final String errMsg = "共通でアクセスするDAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (StringUtils.isBlank(sosCd3)) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null || plannedProdList.size() == 0) {
			final String errMsg = "計画対象品目リストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (updateDtoList == null || updateDtoList.size() == 0) {
			final String errMsg = "担当者別計画ステータスの更新用DTOリストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
	}

	/**
	 * 営業所計画ステータスチェック
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProdList 計画対象品目リスト
	 * @param unallowableMrStatusList 営業所ステータスチェックリスト
	 * @param messageKey メッセージコード
	 * @throws SystemException 例外
	 */
	private void checkOfficeStatus(String sosCd3, List<PlannedProd> plannedProdList, List<OfficeStatusForCheck> unallowableOfficeStatusList, String messageKey)
		throws SystemException {
		try {

			SosMst sosMst = sosMstDao.search(sosCd3);
			// 計画対象品目リストからカテゴリ判定
			String category = plannedProdList.get(0).getCategory();
			DpsPlannedCtg ctg = dpsPlannedCtgDao.search(category);
			//計画立案対象外ならステータスチェックしない。
			if(ctg.getPlanLevelOffice().equals(PlanLevel.FALSE.getDbValue())) {
				return;
			}

			dpsOfficeStatusCheckService.executeForOffice(sosMst, category, unallowableOfficeStatusList);

		} catch (DataNotFoundException e) {
			final String errMsg = "カテゴリ領域または計画対象営業所が取得できない：" + sosCd3;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));

		} catch (UnallowableStatusException e) {
			// メッセージ作成
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey(messageKey));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList));
		}
	}

	/**
	 * 担当者別計画ステータスチェック
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProdList 計画対象品目リスト
	 * @param unallowableMrStatusList 担当者ステータスチェックリスト
	 * @param messageKey メッセージコード
	 * @throws SystemException 例外
	 */
	private void checkMrStatus(String sosCd3, List<PlannedProd> plannedProdList, List<MrStatusForCheck> unallowableMrStatusList, String messageKey) throws SystemException {
		try {
			dpsMrStatusCheckService.execute(sosCd3, plannedProdList, unallowableMrStatusList);

		} catch (UnallowableStatusException e) {
			// メッセージ作成
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey(messageKey));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}
	}

	/**
	 * 施設医師別計画ステータスチェック
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProdList 計画対象品目リスト
	 * @param unallowableMrStatusList 担当者ステータスチェックリスト
	 * @param messageKey メッセージコード
	 * @throws SystemException 例外
	 */
	private void checkInsDocStatus(String sosCd3, List<PlannedProd> plannedProdList, List<InsDocStatusForCheck> unallowableInsDocStatusList, String messageKey) throws SystemException {
		try {
			dpsInsDocStatusCheckService.execute(sosCd3, plannedProdList, unallowableInsDocStatusList);

		} catch (UnallowableStatusException e) {
			// メッセージ作成
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey(messageKey));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}
	}

	/**
	 * 施設特約店別計画ステータスチェック
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProdList 計画対象品目リスト
	 * @param unallowableMrStatusList 担当者ステータスチェックリスト
	 * @param messageKey メッセージコード
	 * @throws SystemException 例外
	 */
	private void checkInsWsStatus(String sosCd3, List<PlannedProd> plannedProdList, List<InsWsStatusForCheck> unallowableInsWsStatusList, String messageKey) throws SystemException {
		try {
			dpsInsWsStatusCheckService.execute(sosCd3, plannedProdList, unallowableInsWsStatusList);

		} catch (UnallowableStatusException e) {
			// メッセージ作成
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey(messageKey));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList), e);
		}
	}

	/**
	 * 調整金額のチェック
	 *
	 * <pre>
	 * 営業所計画と担当者別計画積上の差額(調整金額)が0以外の場合はエラーを出力する。<br>
	 * </pre>
	 *
	 * @throws SystemException
	 */
	@SuppressWarnings("unused")
	private void checkTuneAmount(String sosCd3, List<PlannedProd> plannedProdList) throws SystemException {

		SosMst sosMst;
		try {
			sosMst = sosMstDao.search(sosCd3);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象営業所が取得できない：" + sosCd3;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));

		}

		// 組織からカテゴリ判定
		//ProdCategory category = ProdCategory.getSosCategory(sosMst.getOncSosFlg());
//		String category = ProdCategory.getSosCategory(sosMst.getOncSosFlg()).toString();
		String category = plannedProdList.get(0).getCategory();

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

		// 営業所計画取得
		List<OfficePlan> officePlanList = null;
		try {
			officePlanList = officePlanDao.searchList(OfficePlanDao.SORT_STRING, sosCd3, category, sales);
			if (officePlanList == null || officePlanList.isEmpty()) {
				return;
			}
		} catch (DataNotFoundException e) {
		}

		// 担当者計画取得
		List<MrPlan> mrPlanList = new ArrayList<MrPlan>();
		for (PlannedProd prod : plannedProdList) {
			try {
				mrPlanList.addAll(mrPlanDao.searchBySosCd(MrPlanDao.SORT_STRING, sosCd3, prod.getProdCode(), category));
			} catch (DataNotFoundException e) {
			}
		}
		if (mrPlanList == null || mrPlanList.isEmpty()) {
			return;
		}

		for (PlannedProd prod : plannedProdList) {
			// UH 営業所計画金額
			Long uhOfficePlanAmount = null;
			// P  営業所計画金額
			Long pOfficePlanAmount = null;
			for (OfficePlan officePlan : officePlanList) {
				if (StringUtils.equals(officePlan.getProdCode(), prod.getProdCode())) {
					uhOfficePlanAmount = officePlan.getPlannedValueUhY();
					pOfficePlanAmount = officePlan.getPlannedValuePY();
					break;
				}
			}
			// UH 担当者別計画金額
			Long uhMrPlanAmount = null;
			// P  担当者別計画金額
			Long pMrPlanAmount = null;
			for (MrPlan mrPlan : mrPlanList) {
				if (StringUtils.equals(mrPlan.getProdCode(), prod.getProdCode())) {
					if (mrPlan.getPlannedValueUhY() != null) {
						if (uhMrPlanAmount == null) {
							uhMrPlanAmount = 0L;
						}
						uhMrPlanAmount += mrPlan.getPlannedValueUhY();
					}
					if (mrPlan.getPlannedValuePY() != null) {
						if (pMrPlanAmount == null) {
							pMrPlanAmount = 0L;
						}
						pMrPlanAmount += mrPlan.getPlannedValuePY();
					}
				}
			}
			// 調整金額のチェック
			boolean isError = false;
			if (uhOfficePlanAmount != null && uhMrPlanAmount != null) {
				if ((uhMrPlanAmount - uhOfficePlanAmount) != 0L) {
					isError = true;
				}
			}
			if (pOfficePlanAmount != null && pMrPlanAmount != null) {
				if ((pMrPlanAmount - pOfficePlanAmount) != 0L) {
					isError = true;
				}
			}
			if (isError) {
				final String errMsg = prod.getProdName() + "は調整金額が一致していません。";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}
	}

	/**
	 * 更新用DTOチェック
	 *
	 * @param updateDto 担当者別計画ステータス更新用DTO
	 * @throws SystemException 例外
	 */
	private void checkUpdateDto(MrPlanStatusUpdateDto updateDto) throws SystemException {
		if (updateDto.getStatusSeqKey() == null) {
			final String errMsg = "シーケンスキーがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (updateDto.getStatusUpDate() == null) {
			final String errMsg = "最終更新日時がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
	}
}
