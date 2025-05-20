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
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.dao.InsDocPlanStatusDao;
import jp.co.takeda.dao.MrDistMissDao;
import jp.co.takeda.dao.OutputFileDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dto.ContactOperationsEntryDto;
import jp.co.takeda.dto.DistributionMissDto;
import jp.co.takeda.dto.InsDocHaibunDto;
import jp.co.takeda.logic.div.ContactOperationsType;
import jp.co.takeda.logic.div.ContactResultType;
import jp.co.takeda.logic.div.InsDocStatusForCheck;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MrDistMiss;
import jp.co.takeda.model.OutputFile;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.OutputType;
import jp.co.takeda.model.div.ProdCategory;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.div.StatusForInsDocPlan;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.util.DateUtil;

/**
 * 医師別計画配分実行バッチサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpsDocDistributionBatchService")
public class DpsDocDistributionBatchServiceImpl implements DpsDocDistributionBatchService {

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
	 * 施設医師別計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("insDocPlanStatusDao")
	protected InsDocPlanStatusDao insDocPlanStatusDao;

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
	 * 業務連絡サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsContactOperationsService")
	protected DpsContactOperationsService dpsContactOperationsService;

	/**
	 * 配分実行サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsDocDistributionExecuteService")
	protected DpsDocDistributionExecuteService dpsDocDistributionExecuteService;

	/**
	 * 担当者別計画立案ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsMrStatusCheckService")
	protected DpsMrStatusCheckService dpsMrStatusCheckService;

	/**
	 * 施設特医師計画立案ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsDocStatusCheckService")
	protected DpsInsDocStatusCheckService dpsInsDocStatusCheckService;

	// 配分処理
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<DistributionMissDto> executeDistProd(SosMst sosMst, JgiMst jgiMst, boolean doMrOpen, boolean doPlanClear, boolean isMainHaibun, DpUser execDpUser) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosMst == null) {
			final String errMsg = "組織情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (jgiMst == null) {
			final String errMsg = "従業員情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (execDpUser == null) {
			final String errMsg = "実行者情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ------------------
		// ステータスチェック
		// ------------------

		// 組織からカテゴリ判定（MMP or ONC）
		//
		// TODO 本来このクラス自体廃止すべきだが、エラーを抑止するために以下のような対応をしている
		//ProdCategory category = ProdCategory.getSosCategory(sosMst.getOncSosFlg());
		ProdCategory category = ProdCategory.MMP;
		// TODO ↑

		// チェック対象の品目情報作成
		List<PlannedProd> plannedProdList = null;
		try {
			// TODO 本来このクラス自体廃止すべきだが、エラーを抑止するために以下のような対応をしている
			//plannedProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.IYAKU,category, true);
			plannedProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.IYAKU,"01", true);
			// TODO ↑

		} catch (DataNotFoundException e) {
			final String errMsg = "配分対象品目がない（重点品）";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		// 施設医師別計画(配分中以外はだめ)
		List<JgiMst> jgiList = new ArrayList<JgiMst>();
		jgiList.add(jgiMst);
		List<InsDocStatusForCheck> unallowableInsDocStatusList = new ArrayList<InsDocStatusForCheck>();
		unallowableInsDocStatusList.add(InsDocStatusForCheck.NOTHING);
		unallowableInsDocStatusList.add(InsDocStatusForCheck.DISTED);
		unallowableInsDocStatusList.add(InsDocStatusForCheck.PLAN_OPENED);
		unallowableInsDocStatusList.add(InsDocStatusForCheck.PLAN_COMPLETED);
		dpsInsDocStatusCheckService.execute(jgiList, plannedProdList, unallowableInsDocStatusList);

		// ------------------
		// 配分実行
		// ------------------
		InsDocHaibunDto insDocHaibunDto = new InsDocHaibunDto(sosMst, jgiMst, doPlanClear, execDpUser, category);
		List<DistributionMissDto> missDtoList = dpsDocDistributionExecuteService.execute(insDocHaibunDto);

		// ------------------
		// ステータス更新 (配分中 →確定)
		// ------------------
		Date distEndDate = commonDao.getSystemTime();
		insDocPlanStatusDao.haibunEnd(jgiMst.getJgiNo(), StatusForInsDocPlan.PLAN_COMPLETED, distEndDate, execDpUser);

		return missDtoList;
	}

	// ステータス復旧
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void resumeStatus(JgiMst jgiMst, DpUser execDpUser) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiMst == null) {
			final String errMsg = "従業員情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (execDpUser == null) {
			final String errMsg = "実行者情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 実行前のステータスがない場合は削除
		int countDelete = 0;
		int countUpdate = 0;
		countDelete = insDocPlanStatusDao.haibunRecoverDelete(jgiMst.getJgiNo());

		// 実行前のステータスがある場合は更新
		countUpdate = insDocPlanStatusDao.haibunRecoverUpdate(jgiMst.getJgiNo(), execDpUser);

		if (countDelete == 0 && countUpdate == 0) {
			final String errMsg = "配分ステータスの復旧に失敗";
			throw new SystemException(new Conveyance(STATE_ERROR, errMsg));

		}
	}

	// 配分ミス・業務連絡情報登録
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void entryMissListContactOperations(SosMst sosMst, Date startTime, ContactResultType resultType, DpUser execDpUser, List<DistributionMissDto> missList,
		ContactOperationsType opeType) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosMst == null) {
			final String errMsg = "組織情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
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

		// 支店情報取得
		SosMst sosMst_s = sosMstDAO.search(sosMst.getUpSosCd());

		// --------------------------
		// 出力ファイル情報登録
		// --------------------------
		// 出力ファイルID取得
		Long outputFileId = outputFileDao.getSeqKey();
		OutputFile outputFile = new OutputFile();
		outputFile.setSeqKey(outputFileId);
		outputFile.setOutputType(OutputType.INS_DOC_PLAN_DIST);
		// ファイル名(ファイル種類/医薬営業所C/現在日付)
		String nowTime = DateUtil.toString(commonDao.getSystemTime(), "yyyyMMddHHmm");
		// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//		outputFile.setOutputFileName(OutputType.convertFileName(SysType.IYAKU, OutputType.INS_DOC_PLAN_DIST) + "_" + sosMst.getDistCode() + "_" + nowTime + ".xls");
		outputFile.setOutputFileName(OutputType.convertFileName(SysType.IYAKU, OutputType.INS_DOC_PLAN_DIST) + "_" + sosMst.getDistCode() + "_" + nowTime + ".xlsx");
		// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応

		// 汎用項目(支店名/営業所名/配分開始日時)
		outputFile.setFreeData(sosMst_s.getBumonSeiName() + "　" + sosMst.getBumonSeiName() + "　配分開始日時：" + DateUtil.toString(startTime, "yyyy/MM/dd HH:mm"));
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
		entryContactOperations(sosMst.getSosCd(), startTime, resultType, execDpUser, outputFileId, opeType);
	}

	// 業務連絡情報登録
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void entryContactOperations(String sosCd3, Date startTime, ContactResultType resultType, DpUser execDpUser, Long outputFileId, ContactOperationsType opeType)
		throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
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
		ContactOperationsEntryDto entryDto = new ContactOperationsEntryDto(sosCd3, execDpUser, opeType, resultType, startTime, endTime, null, outputFileId);
		dpsContactOperationsService.entryAtt(entryDto);
		dpsContactOperationsService.entryAnnounce(entryDto);
	}
}
