package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.dao.OptimisticLockingFailureException;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.dao.EstParamHonbuDao;
import jp.co.takeda.dao.EstParamOfficeDao;
import jp.co.takeda.dao.MrPlanStatusDao;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dto.EstimationResultDetailExecDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.div.MrStatusForCheck;
import jp.co.takeda.logic.div.OfficeStatusForCheck;
import jp.co.takeda.model.EstParamHonbu;
import jp.co.takeda.model.EstParamOffice;
import jp.co.takeda.model.MrPlanStatus;
import jp.co.takeda.model.OfficePlanStatus;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.CalcType;
import jp.co.takeda.model.div.EstimationBasePlan;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.service.DpsEstimationExecuteService;
import jp.co.takeda.service.DpsMrStatusCheckService;
import jp.co.takeda.service.DpsOfficeStatusCheckService;

/**
 * 試算結果詳細の試算処理実行ロジッククラス
 *
 * @author stakeuchi
 */
public class EstimationResultDetailEstimationLogic {

	/**
	 * 試算結果詳細の更新用DTO
	 */
	private final EstimationResultDetailExecDto estimationResultDetailExecDto;

	/**
	 * 営業所計画ステータスチェックサービス
	 */
	private final DpsOfficeStatusCheckService dpsOfficeStatusCheckService;

	/**
	 * 担当者別計画ステータスチェックサービス
	 */
	private final DpsMrStatusCheckService dpsMrStatusCheckService;

	/**
	 * 試算実行サービス
	 */
	private final DpsEstimationExecuteService dpsEstimationExecuteService;

	/**
	 * 試算パラメータ(本部)DAO
	 */
	private final EstParamHonbuDao estParamHonbuDao;

	/**
	 * 試算パラメータ(営業所)DAO
	 */
	private final EstParamOfficeDao estParamOfficeDao;

	/**
	 * DB共通情報DAO
	 */
	private final CommonDao commonDao;

	/**
	 * 担当者別計画立案ステータスDAO
	 */
	private final MrPlanStatusDao mrPlanStatusDao;

	/**
	 * 組織情報DAO
	 */
	private final SosMstDAO sosMstDao;

	/**
	 * 営業所案のシーケンスキー（営業所案がない場合、null)
	 */
	private final Long seqKey;

	/**
	 * コンストラクタ
	 *
	 * @param estimationResultDetailExecDto 試算結果詳細の更新用DTO
	 * @param dpsOfficeStatusCheckService 営業所計画ステータスチェックサービス
	 * @param dpsMrStatusCheckService 担当者別計画ステータスチェックサービス
	 * @param dpsEstimationExecuteService 試算実行サービス
	 * @param estParamHonbuDao 試算パラメータ(本部)DAO
	 * @param estParamOfficeDao 試算パラメータ(営業所)DAO
	 * @param commonDao DB共通情報DAO
	 * @param mrPlanStatusDao 担当者別計画立案ステータスDAO
	 */
	public EstimationResultDetailEstimationLogic(EstimationResultDetailExecDto estimationResultDetailExecDto, DpsOfficeStatusCheckService dpsOfficeStatusCheckService,
		DpsMrStatusCheckService dpsMrStatusCheckService, DpsEstimationExecuteService dpsEstimationExecuteService, EstParamHonbuDao estParamHonbuDao,
		EstParamOfficeDao estParamOfficeDao, CommonDao commonDao, MrPlanStatusDao mrPlanStatusDao, SosMstDAO sosMstDao) {
		this.estimationResultDetailExecDto = estimationResultDetailExecDto;
		this.dpsOfficeStatusCheckService = dpsOfficeStatusCheckService;
		this.dpsMrStatusCheckService = dpsMrStatusCheckService;
		this.dpsEstimationExecuteService = dpsEstimationExecuteService;
		this.estParamHonbuDao = estParamHonbuDao;
		this.estParamOfficeDao = estParamOfficeDao;
		this.commonDao = commonDao;
		this.mrPlanStatusDao = mrPlanStatusDao;
		this.sosMstDao = sosMstDao;
		this.seqKey = estimationResultDetailExecDto.getParamSeqKey();
	}

	/**
	 * 営業所案の試算パラメータ更新と試算処理を行う。
	 * <ul>
	 * <li>試算パラメータのステータスチェック</li>
	 * <li>試算パラメータの更新</li>
	 * <li>試算処理の実行</li>
	 * </ul>
	 *
	 * @throws LogicalException
	 */
	public void executeEsimationUpdateOffice() throws LogicalException {
		// フィールド値のチェック
		checkParameter();

		// 営業所計画ステータスチェック
		List<OfficeStatusForCheck> unallowableOfficeStatusList = new ArrayList<OfficeStatusForCheck>();
		unallowableOfficeStatusList.add(OfficeStatusForCheck.NOTHING);
		unallowableOfficeStatusList.add(OfficeStatusForCheck.DRAFT);
		OfficePlanStatus officePlanStatus = checkOfficeStatus(unallowableOfficeStatusList, "DPS3240E", estimationResultDetailExecDto.getCategory());
		CalcType calcType = officePlanStatus.getCalcType();
		if (calcType == null) {
			final String errMsg = "試算タイプが存在しない：";
			throw new SystemException(new Conveyance(new MessageKey(errMsg)));
		}

		// 担当者別計画ステータスチェック
		List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
		// 試算タイプによって、ステータスチェックを変える
		switch (calcType) {
			// 試算中、チーム確定以降はだめ
			case OFFICE_TEAM_MR:
				unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
				unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
				unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_COMPLETED);
				unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_OPENED);
				unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_INPUT_FINISHED);
				unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_COMPLETED);
				break;
			// 試算中、担当者別公開以降はだめ
			case OFFICE_MR:
				unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
				unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
				unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_OPENED);
				unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_INPUT_FINISHED);
				unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_COMPLETED);
				break;
		}
		List<MrPlanStatus> mrPlanStatusList = checkMrStatus(unallowableMrStatusList, "DPS3241E");

		// 試算パラメータの更新実行
		updateEstParamOffice();

		// 試算開始日時取得
		Date startDate = commonDao.getSystemTime();

		// 試算処理の実行
		executeEstimation();

		// 試算終了日時取得
		Date endDate = commonDao.getSystemTime();

		// ステータスに開始日時、終了日時を設定
		for (MrPlanStatus mrPlanStatus : mrPlanStatusList) {
			mrPlanStatus.setEstStartDate(startDate);
			mrPlanStatus.setEstEndDate(endDate);
			mrPlanStatus.setEstimationBasePlan(EstimationBasePlan.OFFICE_PLAN);
			mrPlanStatusDao.update(mrPlanStatus);
		}
	}

	/**
	 * 営業所案の試算パラメータ削除と試算処理を行う。(本部案に戻す)
	 * <ul>
	 * <li>試算パラメータのステータスチェック</li>
	 * <li>試算パラメータの削除</li>
	 * <li>試算処理の実行</li>
	 * </ul>
	 *
	 * @throws LogicalException
	 */
	public void executeEsimationDeleteOffice() throws LogicalException {
		// フィールド値のチェック
		checkParameter();

		// 営業所計画ステータスチェック
		List<OfficeStatusForCheck> unallowableOfficeStatusList = new ArrayList<OfficeStatusForCheck>();
		unallowableOfficeStatusList.add(OfficeStatusForCheck.NOTHING);
		unallowableOfficeStatusList.add(OfficeStatusForCheck.DRAFT);
		OfficePlanStatus officePlanStatus = checkOfficeStatus(unallowableOfficeStatusList, "DPS3242E", estimationResultDetailExecDto.getCategory());
		CalcType calcType = officePlanStatus.getCalcType();
		if (calcType == null) {
			final String errMsg = "試算タイプが存在しない：";
			throw new SystemException(new Conveyance(new MessageKey(errMsg)));
		}

		// 担当者別計画ステータスチェック
		List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
		// 試算タイプによって、ステータスチェックを変える
		switch (calcType) {
			// 試算中、チーム確定以降はだめ
			case OFFICE_TEAM_MR:
				unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
				unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
				unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_COMPLETED);
				unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_OPENED);
				unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_INPUT_FINISHED);
				unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_COMPLETED);
				break;
			// 試算中、担当者別公開以降はだめ
			case OFFICE_MR:
				unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
				unallowableMrStatusList.add(MrStatusForCheck.ESTIMATING);
				unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_OPENED);
				unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_INPUT_FINISHED);
				unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_COMPLETED);
				break;
		}
		List<MrPlanStatus> mrPlanStatusList = checkMrStatus(unallowableMrStatusList, "DPS3243E");

		// 試算パラメータの削除実行
		deleteEstParamOffice();

		// 試算開始日時取得
		Date startDate = commonDao.getSystemTime();

		// 試算処理の実行
		executeEstimation();

		// 試算終了日時取得
		Date endDate = commonDao.getSystemTime();

		// ステータスに開始日時、終了日時を設定
		for (MrPlanStatus mrPlanStatus : mrPlanStatusList) {
			mrPlanStatus.setEstStartDate(startDate);
			mrPlanStatus.setEstEndDate(endDate);
			mrPlanStatus.setEstimationBasePlan(EstimationBasePlan.OFFICE_PLAN);
			mrPlanStatusDao.update(mrPlanStatus);
		}
	}

	/**
	 * 試算パラメータ更新処理を実行する。
	 */
	private void updateEstParamOffice() {
		// 更新対象の試算パラメータを取得
		EstParamOffice estParamOffice = null;
		// 品目固定コード
		final String prodCode = estimationResultDetailExecDto.getProdCode();

		// 営業所案がない場合、本部案を取得する
		if (seqKey == null) {
			try {
				final EstParamHonbu estParamHonbu = estParamHonbuDao.search(prodCode);
				// 全情報を営業所案にコピー
				estParamOffice = new EstParamOffice();
				estParamOffice.setSosCd(estimationResultDetailExecDto.getSosCd3());
				estParamOffice.setProdCode(estimationResultDetailExecDto.getProdCode());
				estParamOffice.setBaseParam(estParamHonbu.getBaseParam());
				estParamOffice.setEstimationParam(estParamHonbu.getEstimationParam());
			} catch (DataNotFoundException e) {
				final String errMsg = "試算パラメータ(本部案)が存在しない：";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
			}

		} else {
			// 営業所案がある場合、取得する
			try {
				// 試算パラメータ(営業所案)取得
				estParamOffice = estParamOfficeDao.search(seqKey);
				estParamOffice.setUpDate(estimationResultDetailExecDto.getParamUpDate());
			} catch (DataNotFoundException e) {
				final String errMsg = "試算パラメータ(営業所案)が存在しない：";
				throw new OptimisticLockingFailureException(errMsg, e);
			}
		}

		// 更新対象の指数を挿入
// add start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		final Integer indexRyhRtsu = estimationResultDetailExecDto.getIndexRyhRtsu();
// add end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		final Integer indexMikakutoku = estimationResultDetailExecDto.getIndexMikakutoku();
		final Integer indexDelivery = estimationResultDetailExecDto.getIndexDelivery();
		final Integer indexFree1 = estimationResultDetailExecDto.getIndexFree1();
		final Integer indexFree2 = estimationResultDetailExecDto.getIndexFree2();
		final Integer indexFree3 = estimationResultDetailExecDto.getIndexFree3();
// add start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		estParamOffice.getEstimationParam().setIndexRyhRtsu(indexRyhRtsu);
// add end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		estParamOffice.getEstimationParam().setIndexMikakutoku(indexMikakutoku);
		estParamOffice.getEstimationParam().setIndexDelivery(indexDelivery);
		estParamOffice.getEstimationParam().setIndexFree1(indexFree1);
		estParamOffice.getEstimationParam().setIndexFree2(indexFree2);
		estParamOffice.getEstimationParam().setIndexFree3(indexFree3);

		// 更新実行
		try {
			// 営業所案があるの場合は更新
			if (seqKey != null) {
				estParamOfficeDao.update(estParamOffice);
			}
			// 本部案の場合は営業所案として新規登録
			else {
				estParamOfficeDao.insert(estParamOffice);
			}
		} catch (DuplicateException e) {
			final String errMsg = "試算パラメータ(営業所)の登録に失敗";
			throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg), e);
		}
	}

	/**
	 * 試算パラメータ削除処理を実行する。
	 *
	 * @return 削除成功=TRUE, 削除失敗=FALSE
	 */
	private void deleteEstParamOffice() {
		if (seqKey != null) {
			final Date upDate = estimationResultDetailExecDto.getParamUpDate();
			estParamOfficeDao.delete(seqKey, upDate);
		}
	}

	/**
	 * 試算処理の実行を実行する。
	 */
	private void executeEstimation() {
		final String sosCd3 = estimationResultDetailExecDto.getSosCd3();
		final String prodCode = estimationResultDetailExecDto.getProdCode();
		final DpUser execDpUser = DpUserInfo.getDpUserInfo().getLoginUser();
		dpsEstimationExecuteService.execute(sosCd3, prodCode, execDpUser);
	}

	/**
	 * 更新処理に必要なパラメータの検証を行う。
	 *
	 * @throws SystemException 例外
	 */
	private void checkParameter() throws SystemException {
		if (estimationResultDetailExecDto == null) {
			final String errMsg = "更新用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dpsOfficeStatusCheckService == null) {
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			final String errMsg = "エリア計画ステータスチェックサービスがnull";
//			final String errMsg = "営業所計画ステータスチェックサービスがnull";
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dpsMrStatusCheckService == null) {
			final String errMsg = "担当者別計画ステータスチェックサービスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dpsEstimationExecuteService == null) {
			final String errMsg = "試算実行サービスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (estParamHonbuDao == null) {
			final String errMsg = "試算パラメータ(本部)DAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (estParamOfficeDao == null) {
			final String errMsg = "試算パラメータ(営業所)DAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final String sosCd3 = estimationResultDetailExecDto.getSosCd3();
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final String prodCode = estimationResultDetailExecDto.getProdCode();
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final String prodName = estimationResultDetailExecDto.getProdName();
		if (prodName == null) {
			final String errMsg = "品目名称がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final Date paramUpDate = estimationResultDetailExecDto.getParamUpDate();
		if (paramUpDate == null) {
			final String errMsg = "試算パラメータの最終更新日時がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
	}

	/**
	 * 営業所計画ステータスの検証を行う。
	 *
	 * @param unallowableMrStatusList 営業所ステータスチェックリスト
	 * @param messageKey メッセージコード
	 * @throws SystemException 例外
	 */
	private OfficePlanStatus checkOfficeStatus(List<OfficeStatusForCheck> unallowableOfficeStatusList, String messageKey, String category) throws SystemException {
		try {

			SosMst sosMst = sosMstDao.search(estimationResultDetailExecDto.getSosCd3());

			List<OfficePlanStatus> list = dpsOfficeStatusCheckService.executeForOffice(sosMst, category, unallowableOfficeStatusList);
			return list.get(0);

		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象営業所が取得できない：" + estimationResultDetailExecDto.getSosCd3();
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));

		} catch (UnallowableStatusException e) {
			// メッセージ作成
			throw new UnallowableStatusException(new Conveyance(new MessageKey(messageKey)));
		}
	}

	/**
	 * 担当者別計画ステータスの検証を行う。
	 *
	 * @param unallowableMrStatusList 担当者ステータスチェックリスト
	 * @param messageKey メッセージコード
	 * @throws SystemException 例外
	 */
	private List<MrPlanStatus> checkMrStatus(List<MrStatusForCheck> unallowableMrStatusList, String messageKey) throws SystemException {
		final String sosCd3 = estimationResultDetailExecDto.getSosCd3();
		PlannedProd plannedProd = new PlannedProd();
		plannedProd.setProdCode(estimationResultDetailExecDto.getProdCode());
		plannedProd.setProdName(estimationResultDetailExecDto.getProdName());
		List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
		plannedProdList.add(plannedProd);

		try {
			return dpsMrStatusCheckService.execute(sosCd3, plannedProdList, unallowableMrStatusList);

		} catch (UnallowableStatusException e) {
			// メッセージ作成
			throw new UnallowableStatusException(new Conveyance(new MessageKey(messageKey)), e);
		}
	}
}
