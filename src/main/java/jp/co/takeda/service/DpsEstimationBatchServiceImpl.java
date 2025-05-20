package jp.co.takeda.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.dao.MrPlanStatusDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dto.ContactOperationsEntryDto;
import jp.co.takeda.dto.EstimationExecOrgDto;
import jp.co.takeda.logic.div.ContactOperationsType;
import jp.co.takeda.logic.div.ContactResultType;
import jp.co.takeda.logic.div.MrStatusForCheck;
import jp.co.takeda.logic.div.OfficeStatusForCheck;
import jp.co.takeda.model.MrPlanStatus;
import jp.co.takeda.model.OfficePlanStatus;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.CalcType;
//add Start 2022/6/2 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
import jp.co.takeda.model.div.CodeMaster;
//add end 2022/6/2 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
import jp.co.takeda.model.div.StatusForMrPlan;
import jp.co.takeda.security.DpUser;

/**
 * 試算実行バッチサービス実装クラス
 *
 * @author khashimoto
 */
@Transactional
@Service("dpsEstimationBatchService")
public class DpsEstimationBatchServiceImpl implements DpsEstimationBatchService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpsEstimationBatchServiceImpl.class);

	/**
	 * 共通DAO
	 */
	@Autowired(required = true)
	@Qualifier("commonDao")
	protected CommonDao commonDao;

	/**
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * 担当者別計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("mrPlanStatusDao")
	protected MrPlanStatusDao mrPlanStatusDao;

	/**
	 * 営業所計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsOfficeStatusCheckService")
	protected DpsOfficeStatusCheckService dpsOfficeStatusCheckService;

	/**
	 * 担当者別計画立案ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsMrStatusCheckService")
	protected DpsMrStatusCheckService dpsMrStatusCheckService;

	/**
	 * 業務連絡サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsContactOperationsService")
	protected DpsContactOperationsService dpsContactOperationsService;

	/**
	 * 試算実行サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsEstimationExecuteService")
	protected DpsEstimationExecuteService dpsEstimationExecuteService;

// add Start 2022/6/2 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
	/**
	 * カテゴリ・品目の検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;
// add end 2022/6/2 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void executeEstProd(SosMst sosMst, DpUser execDpUser, MrPlanStatus mrPlanStatus) throws LogicalException {

		// 品目情報の取得
		PlannedProd plannedProd = plannedProdDAO.search(mrPlanStatus.getProdCode());

		// ------------------
		// ステータスチェック
		// ------------------
		// 営業所計画ステータスチェック（ステータスなし、下書中はエラー）
		List<OfficeStatusForCheck> unallowableOfficeStatusList = new ArrayList<OfficeStatusForCheck>();
		unallowableOfficeStatusList.add(OfficeStatusForCheck.NOTHING);
		unallowableOfficeStatusList.add(OfficeStatusForCheck.DRAFT);
		List<OfficePlanStatus> officePlanStatusList = dpsOfficeStatusCheckService.executeForOffice(sosMst, plannedProd.getCategory(), unallowableOfficeStatusList);
		LOG.debug("【営業所計画ステータスチェック終了】");

		// 担当者別計画ステータス(試算中以外はエラー)
		List<MrStatusForCheck> unallowableMrStatusList = new ArrayList<MrStatusForCheck>();
		unallowableMrStatusList.add(MrStatusForCheck.NOTHING);
		unallowableMrStatusList.add(MrStatusForCheck.ESTIMATED);
		unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_OPENED);
		unallowableMrStatusList.add(MrStatusForCheck.TEAM_PLAN_COMPLETED);
		unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_OPENED);
		unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_INPUT_FINISHED);
		unallowableMrStatusList.add(MrStatusForCheck.MR_PLAN_COMPLETED);
		List<PlannedProd> prodList = new ArrayList<PlannedProd>();
		prodList.add(plannedProd);
		dpsMrStatusCheckService.execute(sosMst.getSosCd(), prodList, unallowableMrStatusList);
		LOG.debug("【担当者別計画ステータスチェック終了】");

		// 試算処理実行
		dpsEstimationExecuteService.execute(sosMst.getSosCd(), plannedProd.getProdCode(), execDpUser);
		LOG.debug("【品目単位の試算処理終了】");

		// 担当者別計画立案ステータス更新
		Date estEndDate = commonDao.getSystemTime();
		MrPlanStatus newMrPlanStatus = mrPlanStatusDao.search(sosMst.getSosCd(), plannedProd.getProdCode());
		// 試算タイプによって、ステータスの更新内容が変わる
		OfficePlanStatus officePlanStatus = officePlanStatusList.get(0);
		CalcType calcType = officePlanStatus.getCalcType();
		switch (calcType) {
			case OFFICE_TEAM_MR:
				newMrPlanStatus.setStatusForMrPlan(StatusForMrPlan.ESTIMATED);
				break;
			// チーム別計画公開・確定の日時も更新
			case OFFICE_MR:
				newMrPlanStatus.setStatusForMrPlan(StatusForMrPlan.TEAM_PLAN_COMPLETED);
				newMrPlanStatus.setTeamPlanOpenDate(estEndDate);
				newMrPlanStatus.setTeamPlanFixDate(estEndDate);
				break;
		}
		newMrPlanStatus.setEstEndDate(estEndDate);
		mrPlanStatusDao.update(newMrPlanStatus, execDpUser);
		LOG.debug("【担当者別計画立案ステータス更新終了】");

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void resumeStatus(String sosCd3, EstimationExecOrgDto estimationExecOrgDto, DpUser execDpUser) throws LogicalException {
		// ------------------------------------
		// 担当者別計画立案ステータスを元に戻す
		// ------------------------------------
		if (estimationExecOrgDto.getMrPlanStatus().getSeqKey() != null) {
			MrPlanStatus orgMrPlanStatus = estimationExecOrgDto.getMrPlanStatus();
			MrPlanStatus mrPlanStatus = mrPlanStatusDao.search(orgMrPlanStatus.getSeqKey());
			orgMrPlanStatus.setUpDate(mrPlanStatus.getUpDate());
			mrPlanStatusDao.update(estimationExecOrgDto.getMrPlanStatus(), execDpUser);
		} else {
			MrPlanStatus mrPlanStatus = mrPlanStatusDao.search(sosCd3, estimationExecOrgDto.getProdCode());
			mrPlanStatusDao.delete(mrPlanStatus.getSeqKey(), mrPlanStatus.getUpDate());
		}
	}

// mod Start 2022/6/2 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
//	public void entryContactOperations(String sosCd3, List<String> errProdList, Date startTime, ContactResultType resultType, DpUser execDpUser) throws LogicalException {
	public void entryContactOperations(String sosCd3, List<String> errProdList, Date startTime, ContactResultType resultType, DpUser execDpUser, String category) throws LogicalException {
// mod End 2022/6/2 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
		// --------------------------
		// アテンション・お知らせ登録
		// --------------------------
		// 処理終了時間
		Date endTime = commonDao.getSystemTime();
		ContactOperationsEntryDto entryDto;
// mod Start 2022/6/2 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
		String categoryName = dpsCodeMasterSearchService.searchDataName(CodeMaster.CAT.getDbValue(), category);
//		entryDto = new ContactOperationsEntryDto(sosCd3, execDpUser, ContactOperationsType.MR_PLAN_EST, resultType, startTime, endTime, errProdList, null);
		entryDto = new ContactOperationsEntryDto(sosCd3, execDpUser, ContactOperationsType.MR_PLAN_EST, resultType, startTime, endTime, errProdList, null, categoryName);
// mod End 2022/6/2 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
		dpsContactOperationsService.entryAtt(entryDto);
		dpsContactOperationsService.entryAnnounce(entryDto);
	}
/* DEL Start 2022/6/2 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
	@Override
	public void entryContactOperationsForVac(String sosCd3, List<String> errProdList, Date startTime, ContactResultType resultType, DpUser execDpUser) throws LogicalException {
		// --------------------------
		// アテンション・お知らせ登録
		// --------------------------
		// 処理終了時間
		Date endTime = commonDao.getSystemTime();
		ContactOperationsEntryDto entryDto;
		entryDto = new ContactOperationsEntryDto(sosCd3, execDpUser, ContactOperationsType.MR_PLAN_EST_VAC, resultType, startTime, endTime, errProdList, null);
		dpsContactOperationsService.entryAtt(entryDto);
		dpsContactOperationsService.entryAnnounce(entryDto);
	}
DEL End 2022/6/2 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更  */
}
