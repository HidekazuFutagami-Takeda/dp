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
import jp.co.takeda.dao.DpsPlannedCtgDao;
import jp.co.takeda.dao.InsWsPlanStatusDao;
import jp.co.takeda.dao.MrDistMissDao;
import jp.co.takeda.dao.OutputFileDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosDistMissDao;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dto.ContactOperationsEntryDto;
import jp.co.takeda.dto.DistributionExecOrgDto;
import jp.co.takeda.dto.DistributionMissDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.div.ContactOperationsType;
import jp.co.takeda.logic.div.ContactResultType;
import jp.co.takeda.logic.div.InsWsStatusForCheck;
import jp.co.takeda.model.DpsPlannedCtg;
import jp.co.takeda.model.InsWsPlanStatus;
import jp.co.takeda.model.MrDistMiss;
import jp.co.takeda.model.OutputFile;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosDistMiss;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.OutputType;
import jp.co.takeda.model.div.StatusForInsWsPlan;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.util.DateUtil;

/**
 * 配分実行バッチサービス実装クラス
 *
 * @author khashimoto
 */
@Transactional
@Service("dpsDistributionBatchService")
public class DpsDistributionBatchServiceImpl implements DpsDistributionBatchService {

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
	 * 施設特約店別計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanStatusDao")
	protected InsWsPlanStatusDao insWsPlanStatusDao;

	/**
	 * 出力ファイル情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("outputFileDao")
	protected OutputFileDao outputFileDao;

	/**
	 * 組織別配分ミス情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosDistMissDao")
	protected SosDistMissDao sosDistMissDao;

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
	 * 配分実行サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsDistributionExecuteService")
	protected DpsDistributionExecuteService dpsDistributionExecuteService;

	/**
	 * 営業所ステータスチェックサービス
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
	 * 施設特約店別計画立案ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsWsStatusCheckService")
	protected DpsInsWsStatusCheckService dpsInsWsStatusCheckService;

	/**
	 * カテゴリ・品目の検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	/**
	 * 計画対象カテゴリ領域DAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsPlannedCtgDao")
	protected DpsPlannedCtgDao dpsPlannedCtgDao;

	// 配分処理
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<DistributionMissDto> executeDistProd(SosMst sosMst, String category, DpUser execDpUser, DistributionExecOrgDto execOrgDto, boolean isMrOpenCheck)
		throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		// ワクチンの場合、組織未選択時は全社対象とする
//		if (sosMst == null) {
//			final String errMsg = "組織情報がnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}
		String sosCd = null;
		if (sosMst != null) {
			sosCd = sosMst.getSosCd();
		}
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
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
		//①DPS_I_INS_WS_PLAN_STATUSに配分中ステータスを登録と
		//②全従業員（MR）×品目の件数と①で作られている件数の比較チェック
		//ステータスなしチェックをエラーにすると、該当するためコメント削除
//		unallowableInsWsStatusList.add(InsWsStatusForCheck.NOTHING);
		unallowableInsWsStatusList.add(InsWsStatusForCheck.DISTED);
		unallowableInsWsStatusList.add(InsWsStatusForCheck.PLAN_COMPLETED);
		// チェック実行
		dpsInsWsStatusCheckService.execute(sosCd, plannedProd, unallowableInsWsStatusList);

		// -----------------------------
		// 売上所属の判定
		// -----------------------------
		SysType stype = null;
		if(dpsCodeMasterSearchService.isVaccine(category)) {
			stype = SysType.VACCINE;
		}else{
			stype = SysType.IYAKU;
		}

		// 施設特約店別計画〆フラグ
		SysManage sysManage = dpsSystemManageSearchService.searchSysManage(SysClass.DPS, stype);
		if (sysManage.getWsEndFlg()) {
			final String errMsg = "施設特約店別計画〆フラグがON";
			throw new UnallowableStatusException(new Conveyance(STATE_ERROR, errMsg));
		}

		// ------------------
		// 配分実行
		// ------------------
		List<DistributionMissDto> missDtoList = dpsDistributionExecuteService.execute(sosCd, execOrgDto.getProdCode(), category, execDpUser);

		// ------------------
		// ステータス更新
		// ------------------
		Date distEndDate = commonDao.getSystemTime();
		for (InsWsPlanStatus status : execOrgDto.getInsWsPlanStatusList()) {
			InsWsPlanStatus record = insWsPlanStatusDao.search(status.getJgiNo(), status.getProdCode());
			// 配分処理と同時にMRに公開フラグONの場合、公開
			if (isMrOpenCheck) {
				record.setStatusForInsWsPlan(StatusForInsWsPlan.PLAN_OPENED);
				record.setInsWsOpenDate(distEndDate);
			} else {
				record.setStatusForInsWsPlan(StatusForInsWsPlan.DISTED);
			}
			record.setDistEndDate(distEndDate);
			insWsPlanStatusDao.update(record);
		}

		return missDtoList;
	}

	// ステータス復旧
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void resumeStatus(DpUser execDpUser, DistributionExecOrgDto execOrgDto) throws LogicalException {

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
		for (InsWsPlanStatus status : execOrgDto.getInsWsPlanStatusList()) {
			if (status.getSeqKey() != null) {
				// 元のステータスが存在する場合、ステータスを更新
				InsWsPlanStatus record = insWsPlanStatusDao.search(status.getJgiNo(), status.getProdCode());
				status.setUpDate(record.getUpDate());
				insWsPlanStatusDao.update(status, execDpUser);
			} else {
				// 元のステータスが存在しない場合、ステータスを削除
				InsWsPlanStatus record = insWsPlanStatusDao.search(status.getJgiNo(), status.getProdCode());
				insWsPlanStatusDao.delete(record.getSeqKey(), record.getUpDate());
			}
		}
	}

	// 配分ミス・業務連絡情報登録
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void entryMissListContactOperations(SosMst sosMst, List<String> errProdList, Date startTime, ContactResultType resultType, DpUser execDpUser, String category,
		List<DistributionMissDto> missList) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		// ワクチンの場合、組織未選択時は全社対象とする
//		if (sosMst == null) {
//			final String errMsg = "組織情報がnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}
		String sosCd3 = SosMst.SOS_CD1;
		if(sosMst != null) {
			sosCd3 = sosMst.getSosCd();
		}
		if (startTime == null) {
			final String errMsg = "開始時間がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (execDpUser == null) {
			final String errMsg = "実行者情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (missList == null || missList.size() == 0) {
			final String errMsg = "配分ミス情報がなし";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 支店情報取得
		SosMst sosMst_s = null;
		if(sosMst != null) {
			sosMst_s = sosMstDAO.search(sosMst.getUpSosCd());
		}

		// --------------------------
		// 出力ファイル情報登録
		// --------------------------
		// 出力ファイルID取得
		Long outputFileId = outputFileDao.getSeqKey();
		OutputFile outputFile = new OutputFile();
		outputFile.setSeqKey(outputFileId);
		OutputType outputType = null;
		String fileNameHeader = "";
//		switch (category) {
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBUを追加する意図で、仕入品→Defaultという分岐に変更
//			case MMP:
//				outputType = OutputType.MR_INS_WS_PLAN_DIST;
//				fileNameHeader = OutputType.convertFileName(SysType.IYAKU, outputType) + "JPBU(STARS)";
//				break;
//			case ONC:
//				outputType = OutputType.MR_INS_WS_PLAN_DIST;
//				fileNameHeader = OutputType.convertFileName(SysType.IYAKU, outputType) + "ONC";
//				break;
//			case SHIIRE:
//				outputType = OutputType.OFFICE_INS_WS_PLAN_DIST;
//				fileNameHeader = OutputType.convertFileName(SysType.IYAKU, outputType) + "Shiire";
//				break;
//			default:
				String categoryName = dpsCodeMasterSearchService.searchDataName(CodeMaster.CAT.getDbValue(), category);

				// --------------------------------------------------------
				// 計画支援カテゴリ領域の計画立案・担当者で判断
				// ----------------------------------------------------------
				DpsPlannedCtg plannedCtg = new DpsPlannedCtg();
				plannedCtg = dpsPlannedCtgDao.search(category);

				if (plannedCtg.getPlanLevelMr().equals("0")) {
					//担当者計画がない場合は1
					outputType = OutputType.OFFICE_INS_WS_PLAN_DIST;
				}else {
					//担当者計画がある場合は2
					outputType = OutputType.MR_INS_WS_PLAN_DIST;
				}
				fileNameHeader = OutputType.convertFileName(SysType.IYAKU, outputType) + categoryName.replaceAll("品", "");
//				break;
//		}
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBUを追加する意図で、仕入品→Defaultという分岐に変更
		outputFile.setOutputType(outputType);
		// ファイル名(ファイル種類/医薬営業所C/現在日付)
		String nowTime = DateUtil.toString(commonDao.getSystemTime(), "yyyyMMddHHmm");
		if (dpsCodeMasterSearchService.isVaccine(category)) {
			// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//			outputFile.setOutputFileName(OutputType.convertFileName(SysType.VACCINE, OutputType.VAC_INS_WS_PLAN_DIST) + "_" + nowTime + ".xls");
			outputFile.setOutputFileName(OutputType.convertFileName(SysType.VACCINE, OutputType.VAC_INS_WS_PLAN_DIST) + "_" + nowTime + ".xlsx");
		} else {
			if (sosMst == null) {
//				outputFile.setOutputFileName(fileNameHeader + "_" + SosMst.SOS_CD1 + "_" + nowTime + ".xls");
				outputFile.setOutputFileName(fileNameHeader + "_" + SosMst.SOS_CD1 + "_" + nowTime + ".xlsx");
			}else {
//				outputFile.setOutputFileName(fileNameHeader + "_" + sosMst.getDistCode() + "_" + nowTime + ".xls");
				outputFile.setOutputFileName(fileNameHeader + "_" + sosMst.getDistCode() + "_" + nowTime + ".xlsx");
			}
			// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		}
		if (sosMst == null) {
			outputFile.setFreeData("全社　ワクチン　配分開始日時：" + DateUtil.toString(startTime, "yyyy/MM/dd HH:mm"));
		}else {
			// 汎用項目(支店名/営業所名/配分開始日時)
			outputFile.setFreeData(sosMst_s.getBumonSeiName() + "　" + sosMst.getBumonSeiName() + "　配分開始日時：" + DateUtil.toString(startTime, "yyyy/MM/dd HH:mm"));
		}
		outputFileDao.insert(outputFile, execDpUser);

		// --------------------------
		// 配分ミス情報登録
		// --------------------------
//		switch (category) {
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBUを追加する意図で、仕入品→Defaultという分岐に変更
//			case SHIIRE:
//				for (DistributionMissDto missDto : missList) {
//					SosDistMiss distMiss = new SosDistMiss();
//					distMiss.setSosCd(missDto.getSosCd());
//					distMiss.setProdCode(missDto.getProdCode());
//					distMiss.setInsType(missDto.getInsType());
//					distMiss.setPlannedValue(missDto.getPlannedValue());
//					distMiss.setDiffValue(missDto.getDiffValue());
//					distMiss.setMessageCode(missDto.getMessageCode());
//					distMiss.setOutputFileId(outputFileId);
//
//					sosDistMissDao.insert(distMiss, execDpUser);
//				}
//				break;
//			case MMP:
//			case ONC:
//			default:
//				for (DistributionMissDto missDto : missList) {
//					MrDistMiss distMiss = new MrDistMiss();
//					distMiss.setJgiNo(missDto.getJgiNo());
//					distMiss.setProdCode(missDto.getProdCode());
//					distMiss.setInsType(missDto.getInsType());
//					distMiss.setPlannedValue(missDto.getPlannedValue());
//					distMiss.setDiffValue(missDto.getDiffValue());
//					distMiss.setMessageCode(missDto.getMessageCode());
//					distMiss.setOutputFileId(outputFileId);
//
//					mrDistMissDao.insert(distMiss, execDpUser);
//				}
//				break;
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBUを追加する意図で、仕入品→Defaultという分岐に変更
//		}

//		if(dpsCodeMasterSearchService.isSiire(category)) {
		if (plannedCtg.getPlanLevelMr().equals("0")) {
			for (DistributionMissDto missDto : missList) {
				SosDistMiss distMiss = new SosDistMiss();
				distMiss.setSosCd(missDto.getSosCd());
				distMiss.setProdCode(missDto.getProdCode());
				distMiss.setInsType(missDto.getInsType());
				distMiss.setPlannedValue(missDto.getPlannedValue());
				distMiss.setDiffValue(missDto.getDiffValue());
				distMiss.setMessageCode(missDto.getMessageCode());
				distMiss.setOutputFileId(outputFileId);

				sosDistMissDao.insert(distMiss, execDpUser);
			}
		}else {
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
		}

		// --------------------------
		// アテンション・お知らせ登録
		// --------------------------
		entryContactOperations(sosCd3, errProdList, startTime, resultType, execDpUser, category, outputFileId);
	}

	// 業務連絡情報登録
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void entryContactOperations(String sosCd3, List<String> errProdList, Date startTime, ContactResultType resultType, DpUser execDpUser, String category,
		Long outputFileId) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		// ワクチンの場合、組織未選択時は全社対象とする
//		if (sosCd3 == null) {
//			final String errMsg = "組織コードがnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}
		if(sosCd3 == null) {
			sosCd3 = SosMst.SOS_CD1;
		}
		if (startTime == null) {
			final String errMsg = "開始時間がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (execDpUser == null) {
			final String errMsg = "実行者情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// --------------------------
		// アテンション・お知らせ登録
		// --------------------------
		// 処理終了時間
		Date endTime = commonDao.getSystemTime();
		ContactOperationsEntryDto entryDto;
		ContactOperationsType operationsType = null;
		String categoryName = dpsCodeMasterSearchService.searchDataName(CodeMaster.CAT.getDbValue(), category);
//		switch (category) {
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBUを追加する意図で、仕入品→Defaultという分岐に変更。MMPとONCとSPBUを”INS_WS_DIST”というタイプにまとめる。
//			case MMP:
//				operationsType = ContactOperationsType.INS_WS_DIST_MMP;
//				break;
//			case ONC:
//				operationsType = ContactOperationsType.INS_WS_DIST_ONC;
//				break;
//			case SHIIRE:
//				operationsType = ContactOperationsType.INS_WS_DIST_SHIIRE;
//				break;
//			default:
//				operationsType = ContactOperationsType.INS_WS_DIST;
//				break;
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBUを追加する意図で、仕入品→Defaultという分岐に変更。MMPとONCとSPBUを”INS_WS_DIST”というタイプにまとめる。
//		}

		//アテンション通知対象の従業員情報取得で必須。無いと通知されない。
		if(dpsCodeMasterSearchService.isVaccine(category)) {
			//カテゴリーがワクチンの場合
			operationsType = ContactOperationsType.INS_WS_DIST_VAC;
		}else if(dpsCodeMasterSearchService.isSiire(category)) {
			operationsType = ContactOperationsType.INS_WS_DIST_SHIIRE;
		}else {
			operationsType = ContactOperationsType.INS_WS_DIST;
		}

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　配分処理の"お知らせ"内容の"カテゴリ名"のべた書き回避のため、カテゴリ名も渡すよう変更
//		entryDto = new ContactOperationsEntryDto(sosCd3, execDpUser, operationsType, resultType, startTime, endTime, errProdList, outputFileId);
		entryDto = new ContactOperationsEntryDto(sosCd3, execDpUser, operationsType, resultType, startTime, endTime, errProdList, outputFileId, categoryName);
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　配分処理の"お知らせ"内容の"カテゴリ名"のべた書き回避のため、カテゴリ名も渡すよう変更
		dpsContactOperationsService.entryAtt(entryDto);
		dpsContactOperationsService.entryAnnounce(entryDto);
	}
}
