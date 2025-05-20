package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.dao.InsWsPlanStatusForVacDao;
import jp.co.takeda.dao.MrDistMissDao;
import jp.co.takeda.dao.OutputFileDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dto.ContactOperationsEntryDto;
import jp.co.takeda.dto.DistributionForVacExecOrgDto;
import jp.co.takeda.dto.DistributionMissDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.div.ContactOperationsType;
import jp.co.takeda.logic.div.ContactResultType;
import jp.co.takeda.logic.div.InsWsStatusForCheck;
import jp.co.takeda.model.InsWsPlanStatusForVac;
import jp.co.takeda.model.MrDistMiss;
import jp.co.takeda.model.OutputFile;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.OutputType;
import jp.co.takeda.model.div.StatusForInsWsPlan;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.util.DateUtil;

/**
 * (ワクチン)配分実行バッチサービス実装クラス
 *
 * @author khashimoto
 */
@Transactional
@Service("dpsDistributionForVacBatchService")
public class DpsDistributionForVacBatchServiceImpl implements DpsDistributionForVacBatchService {

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
	 * (ワクチン)施設特約店別計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanStatusForVacDao")
	protected InsWsPlanStatusForVacDao insWsPlanStatusForVacDao;

	/**
	 * 出力ファイル情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("outputFileDao")
	protected OutputFileDao outputFileDao;

	/**
	 * 担当者別配分ミス情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("mrDistMissDao")
	protected MrDistMissDao mrDistMissDao;

	/**
	 * 納入計画管理検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpcSystemManageSearchService")
	protected DpcSystemManageSearchService dpsSystemManageSearchService;

	/**
	 * 業務連絡サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsContactOperationsService")
	protected DpsContactOperationsService dpsContactOperationsService;

	/**
	 * (ワクチン)配分実行サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsDistributionForVacExecuteService")
	protected DpsDistributionForVacExecuteService dpsDistributionForVacExecuteService;

	/**
	 * (ワクチン)施設特約店別計画立案ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsWsStatusCheckForVacService")
	protected DpsInsWsStatusCheckForVacService dpsInsWsStatusCheckForVacService;

	// 配分処理
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<DistributionMissDto> executeDistProd(DpUser execDpUser, DistributionForVacExecOrgDto execOrgDto, boolean isMrOpenCheck) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (execDpUser == null) {
			final String errMsg = "実行者情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (execOrgDto == null) {
			final String errMsg = "配分実行用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 品目情報の取得
		PlannedProd plannedProd = plannedProdDAO.search(execOrgDto.getProdCode());

		// ------------------
		// ステータスチェック
		// ------------------

		// 施設特約店別計画ステータスチェック(配分中以外はだめ)
		// 許可しないステータスリスト作成
		List<InsWsStatusForCheck> unallowableInsWsStatusList = new ArrayList<InsWsStatusForCheck>();
		unallowableInsWsStatusList.add(InsWsStatusForCheck.NOTHING);
		unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTED);
		unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_OPENED);
		unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_COMPLETED);
		// チェック実行
		List<PlannedProd> prodList = new ArrayList<PlannedProd>();
		prodList.add(plannedProd);
		dpsInsWsStatusCheckForVacService.execute(prodList, unallowableInsWsStatusList);

		// 施設特約店別計画〆フラグ
		SysManage sysManage = dpsSystemManageSearchService.searchSysManage(SysClass.DPS, SysType.VACCINE);
		if (sysManage.getWsEndFlg()) {
			final String errMsg = "施設特約店別計画〆フラグがON";
			throw new UnallowableStatusException(new Conveyance(STATE_ERROR, errMsg));
		}

		// ------------------
		// 配分実行
		// ------------------
		List<DistributionMissDto> missDtoList = dpsDistributionForVacExecuteService.execute(execOrgDto.getProdCode(), execDpUser);

		// ------------------
		// ステータス更新
		// ------------------
		Date distEndDate = commonDao.getSystemTime();
		for (InsWsPlanStatusForVac status : execOrgDto.getInsWsPlanStatusForVacList()) {
			InsWsPlanStatusForVac record = insWsPlanStatusForVacDao.search(status.getJgiNo(), status.getProdCode());
			// 配分処理と同時にMRに公開フラグONの場合、公開
			if (isMrOpenCheck) {
				record.setStatusForInsWsPlan(StatusForInsWsPlan.PLAN_OPENED);
				record.setInsWsOpenDate(distEndDate);
			} else {
				record.setStatusForInsWsPlan(StatusForInsWsPlan.DISTED);
			}
			record.setDistEndDate(distEndDate);
			insWsPlanStatusForVacDao.update(record);
		}

		return missDtoList;
	}

	// ステータス復旧
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void resumeStatus(DpUser execDpUser, DistributionForVacExecOrgDto execOrgDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (execDpUser == null) {
			final String errMsg = "実行者情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (execOrgDto == null) {
			final String errMsg = "配分実行用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ステータス更新/削除
		for (InsWsPlanStatusForVac status : execOrgDto.getInsWsPlanStatusForVacList()) {
			if (status.getSeqKey() != null) {
				// 元のステータスが存在する場合、ステータスを更新
				InsWsPlanStatusForVac record = insWsPlanStatusForVacDao.search(status.getJgiNo(), status.getProdCode());
				status.setUpDate(record.getUpDate());
				insWsPlanStatusForVacDao.update(status, execDpUser);
			} else {
				// 元のステータスが存在しない場合、ステータスを削除
				InsWsPlanStatusForVac record = insWsPlanStatusForVacDao.search(status.getJgiNo(), status.getProdCode());
				insWsPlanStatusForVacDao.delete(record.getSeqKey(), record.getUpDate());
			}
		}
	}

	// 配分ミス・業務連絡情報登録
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void entryMissListContactOperations(List<String> errProdList, Date startTime, ContactResultType resultType, DpUser execDpUser, List<DistributionMissDto> missList)
		throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (startTime == null) {
			final String errMsg = "開始時間がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (execDpUser == null) {
			final String errMsg = "実行者情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (missList == null || missList.size() == 0) {
			final String errMsg = "配分ミス情報がなし";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// --------------------------
		// 出力ファイル情報登録
		// --------------------------
		// 出力ファイルID取得
		Long outputFileId = outputFileDao.getSeqKey();
		OutputFile outputFile = new OutputFile();
		outputFile.setSeqKey(outputFileId);
		OutputType outputType = OutputType.VAC_INS_WS_PLAN_DIST;
		outputFile.setOutputType(outputType);
		// ファイル名(ファイル種類/医薬営業所C/現在日付)
		String nowTime = DateUtil.toString(commonDao.getSystemTime(), "yyyyMMddHHmm");
		// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//		outputFile.setOutputFileName(OutputType.convertFileName(SysType.VACCINE, outputType) + "_" + nowTime + ".xls");
		outputFile.setOutputFileName(OutputType.convertFileName(SysType.VACCINE, outputType) + "_" + nowTime + ".xlsx");
		// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		// ワクチン汎用項目(配分開始日時)
		outputFile.setFreeData("配分開始日時：" + DateUtil.toString(startTime, "yyyy/MM/dd HH:mm"));
		outputFileDao.insert(outputFile, execDpUser);

		// --------------------------
		// 配分ミス情報登録
		// --------------------------
		for (DistributionMissDto missDto : missList) {
			MrDistMiss distMiss = new MrDistMiss();
			distMiss.setJgiNo(missDto.getJgiNo());
			distMiss.setProdCode(missDto.getProdCode());
			distMiss.setInsType(missDto.getInsType());
			distMiss.setPlannedValue(missDto.getPlannedValue());
			distMiss.setDiffValue(missDto.getDiffValue());
			distMiss.setMessageCode(missDto.getMessageCode());
			distMiss.setOutputFileId(outputFileId);

			mrDistMissDao.insert(distMiss, execDpUser);
		}

		// --------------------------
		// アテンション・お知らせ登録
		// --------------------------
		entryContactOperations(errProdList, startTime, resultType, execDpUser, outputFileId);
	}

	// 業務連絡情報登録
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void entryContactOperations(List<String> errProdList, Date startTime, ContactResultType resultType, DpUser execDpUser, Long outputFileId) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (startTime == null) {
			final String errMsg = "開始時間がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (execDpUser == null) {
			final String errMsg = "実行者情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// --------------------------
		// アテンション・お知らせ登録
		// --------------------------
		// 処理終了時間
		Date endTime = commonDao.getSystemTime();
		ContactOperationsEntryDto entryDto;
		ContactOperationsType operationsType = ContactOperationsType.INS_WS_DIST_VAC;

		entryDto = new ContactOperationsEntryDto(null, execDpUser, operationsType, resultType, startTime, endTime, errProdList, outputFileId);
		dpsContactOperationsService.entryAtt(entryDto);
		dpsContactOperationsService.entryAnnounce(entryDto);
	}
}
