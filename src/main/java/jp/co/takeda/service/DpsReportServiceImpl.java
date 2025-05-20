package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;
import static jp.co.takeda.model.div.BumonRank.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.Constants.TemplateInfo;
import jp.co.takeda.bean.POIBean;
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.dao.CprodXlsDAO;	// add 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
import jp.co.takeda.dao.DeliveryResultMrDao;
import jp.co.takeda.dao.DistPlanningListDLSummaryDao;
import jp.co.takeda.dao.DpsCodeMasterDao;
import jp.co.takeda.dao.DpsPlannedCtgDao;
import jp.co.takeda.dao.DpsSupportFollowDao;
import jp.co.takeda.dao.InsWsPlanForRefDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.MrDistMissDao;
import jp.co.takeda.dao.MrPlanDao;
import jp.co.takeda.dao.OfficePlanDao;
import jp.co.takeda.dao.OutputFileDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosDistMissDao;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.CheckToolDao;
import jp.co.takeda.dao.TeamPlanDao;
import jp.co.takeda.dao.TmsTytenMstUnDAO;
import jp.co.takeda.dao.WsPlanSummaryDao;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.DistPlanningListSummaryScDto;
import jp.co.takeda.dto.MrPlanSosSummaryDto;
import jp.co.takeda.dto.WsPlanReferenceScDto;
import jp.co.takeda.dto.WsPlanSummaryScDto;
import jp.co.takeda.logic.DelFacilitiesAndAdjustmentsListExportLogic;
import jp.co.takeda.logic.DistMissListInsDocExportLogic;
import jp.co.takeda.logic.DistMissListMrExportLogic;
import jp.co.takeda.logic.DistMissListOfficeExportLogic;
import jp.co.takeda.logic.DistMissListWsExportLogic;
import jp.co.takeda.logic.DistPlanningErrListExportLogic;
import jp.co.takeda.logic.DistPlanningListExportLogic;
import jp.co.takeda.logic.MrTeamReportExportLogic;
import jp.co.takeda.logic.OutputMrPlanMMPListLogic;
import jp.co.takeda.logic.OutputReviewMrProdMMPListLogic;
import jp.co.takeda.logic.OutputReviewProdMrMMPListLogic;
import jp.co.takeda.logic.RelationCompanyInsWsListExportLogic;
import jp.co.takeda.logic.CheckToolExportLogic;
import jp.co.takeda.logic.WsPlanListExportLogic;
import jp.co.takeda.logic.WsPlanListNonJissekiExportLogic;
import jp.co.takeda.model.DelFacilitiesAndAdjustments;
import jp.co.takeda.model.DeliveryResultMr;
import jp.co.takeda.model.DistPlanningListDLSummary;
import jp.co.takeda.model.DistPlanningListULSummary;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.DpsPlannedCtg;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MrDistMiss;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.model.OfficePlan;
import jp.co.takeda.model.OutputFile;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosDistMiss;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.CheckTool;
import jp.co.takeda.model.TeamPlan;
import jp.co.takeda.model.WsPlanSummary2;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.KaBaseKb;
import jp.co.takeda.model.div.OutputType;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.div.TytenKisLevel;
import jp.co.takeda.model.view.AreaFacilityPersonInChargeAdjustment;
import jp.co.takeda.model.view.AreaPersonInChargeAdjustment;
import jp.co.takeda.model.view.DeletedFacilityPersonInChargeplan;
import jp.co.takeda.model.view.PersonInChargeFacilityAdjustmentList;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.cmn.bean.ExportResultExcelImpl;
import jp.co.takeda.web.cmn.velocity.ConstantsTool;

/**
 * 計画支援の帳票サービス実装クラス
 *
 * @author tkawabata
 */
@Transactional
@Service("dpsReportService")
public class DpsReportServiceImpl implements DpsReportService {

	/**
	 * メッセージソース
	 */
	@Autowired(required = true)
	@Qualifier("messageSource")
	protected MessageSource messageSource;

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
	 * 従業員情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

// add start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
	/**
	 * 計画検討表EXCEL出力対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("cprodXlsDAO")
	protected CprodXlsDAO cprodXlsDAO;
// add end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定

	/**
	 * 営業所計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("officePlanDao")
	protected OfficePlanDao officePlanDao;

	/**
	 * 担当者別計画DAO
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
	 * 参照用の施設特約店別計画取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanForRefDao")
	protected InsWsPlanForRefDao insWsPlanForRefDao;

	/**
	 * 特約店基本統合DAO
	 */
	@Autowired(required = true)
	@Qualifier("tmsTytenMstUnDAO")
	protected TmsTytenMstUnDAO tmsTytenMstUnDAO;

	/**
	 * 特約店別計画サマリーDAO
	 */
	@Autowired(required = true)
	@Qualifier("wsPlanSummaryDao")
	protected WsPlanSummaryDao wsPlanSummaryDao;

	/**
	 * ワクチン用計画支援の帳票サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsReportForVacService")
	protected DpsReportForVacService dpsReportForVacService;

	/**
	 * チーム計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("teamPlanDao")
	protected TeamPlanDao teamPlanDao;

	/**
	 * 営業所計画アップロード用のフォーマットファイルサマリーDAO
	 */
	@Autowired(required = true)
	@Qualifier("distPlanningListDLSummaryDao")
	protected DistPlanningListDLSummaryDao distPlanningListDLSummaryDao;

	// add Start 2024/2/26 K.Suzuki 2024年2月　チェックツール追加
	/**
	 * 施設特約店計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("checkToolDao")
	protected CheckToolDao checkToolDao;
	// add End 2024/2/26 K.Suzuki 2024年2月　チェックツール追加
	/**
	 * 汎用マスタ検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	/**
	 * 計画支援汎用マスタDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterDao")
	protected DpsCodeMasterDao dpsCodeMasterDao;

	/**
	 * 計画対象カテゴリ領域取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsPlannedCtgDao")
	protected DpsPlannedCtgDao dpsPlannedCtgDao;

// add Start 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
	/**
	 * エリア・担当者調整一覧を取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("DpsSupportFollowDao")
	protected DpsSupportFollowDao dpsSupportFollowDao;
// add End 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能

	/**
	 * テンプレート切替の品目数(メモリ対策)
	 */
	protected int DPS302P0003_CHANGE_SIZE = 35;

// del start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　※DBから取得に変更
//	/** ＯＮＣ品目計 */
//	private final String ONC_PROD = "ＯＮＣ品目計";
//
//	/** ＪＰＢＵ（ＳＴＡＲＳ）計 (旧 ＭＭＰ計) */
//	private final String JPBU_STARS = "ＪＰＢＵ（ＳＴＡＲＳ）計";
// del end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

// add Start 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
	/** 出力ファイル名ヘッダ */
	private static final String OUTPUT_FILE_NAME_HEADER = "OFFICE_PLAN_UPLOAD";
// add End 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能

	/**
	 * 計画（1：期別、2：月別）
	 */
	private static final String PLAN_ID = "1";

	// 配分ミスリスト
	public ExportResult outputDistMissList(String templateRootPath, Long outputFileId) {
		// -------------------------
		// 引数チェック
		// -------------------------
		if (StringUtils.isEmpty(templateRootPath)) {
			final String errMsg = "テンプレート配置パスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (outputFileId == null) {
			final String errMsg = "出力ファイル情報IDがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// -------------------------
		// 出力ファイル情報取得
		// -------------------------
		OutputFile outputFile = null;
		try {
			outputFile = outputFileDao.search(outputFileId);
		} catch (DataNotFoundException e) {
			final String errMsg = " 出力ファイル情報取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}
		// ----------------------------
		// 営業所→施設特約店別計画配分
		// ----------------------------
		final boolean isOfficeInsWsPlanDist = outputFile.getOutputType().equals(OutputType.OFFICE_INS_WS_PLAN_DIST);
		if (isOfficeInsWsPlanDist) {
			// 組織別配分ミス情報取得
			List<SosDistMiss> sosDistMissList = null;
			try {
				sosDistMissList = sosDistMissDao.searchList(SosDistMissDao.SORT_STRING, outputFileId);
			} catch (DataNotFoundException e) {
				// エラーにせずファイルには空欄で出力する。
			}
			// エクセルファイル生成
			final String templatePath = TemplateInfo.EXCEL_TEMPLATE_DPS400P0001.getTemplatePath(templateRootPath);
			DistMissListOfficeExportLogic logic = new DistMissListOfficeExportLogic(templatePath, commonDao.getSystemTime(), outputFile, sosDistMissList, messageSource);
			return logic.export();
		}
		// ----------------------------
		// 担当者→施設特約店別計画配分
		// ----------------------------
		final boolean isMrInsWsPlanDist = outputFile.getOutputType().equals(OutputType.MR_INS_WS_PLAN_DIST);
		if (isMrInsWsPlanDist) {
			// 担当者別配分ミス情報取得
			List<MrDistMiss> mrDistMissList = null;
			try {
				mrDistMissList = mrDistMissDao.searchList(MrDistMissDao.SORT_STRING, outputFileId);
			} catch (DataNotFoundException e) {
				// エラーにせずファイルには空欄で出力する。
			}
			// エクセルファイル生成
			final String templatePath = TemplateInfo.EXCEL_TEMPLATE_DPS400P0002.getTemplatePath(templateRootPath);
			DistMissListMrExportLogic logic = new DistMissListMrExportLogic(templatePath, commonDao.getSystemTime(), outputFile, mrDistMissList, messageSource);
			return logic.export();
		}
		// ----------------------------
		// 特約店別計画配分
		// ----------------------------
		final boolean isWsPlanDist = outputFile.getOutputType().equals(OutputType.WS_PLAN_DIST);
		if (isWsPlanDist) {
			// 組織別配分ミス情報取得
			List<SosDistMiss> sosDistMissList = null;
			try {
				sosDistMissList = sosDistMissDao.searchList(SosDistMissDao.SORT_STRING2, outputFileId);
			} catch (DataNotFoundException e) {
				// エラーにせずファイルには空欄で出力する。
			}
			// エクセルファイル生成
			final String templatePath = TemplateInfo.EXCEL_TEMPLATE_DPS400P0001.getTemplatePath(templateRootPath);
			DistMissListWsExportLogic logic = new DistMissListWsExportLogic(templatePath, commonDao.getSystemTime(), outputFile, sosDistMissList, sosMstDAO, messageSource);
			return logic.export();
		}
		// ----------------------------
		// ワクチン施設特約店別計画配分
		// ----------------------------
		final boolean isVacInsWsPlanDist = outputFile.getOutputType().equals(OutputType.VAC_INS_WS_PLAN_DIST);
		if (isVacInsWsPlanDist) {
			return dpsReportForVacService.outputDistMissList(templateRootPath, outputFileId);
		}

		// ----------------------------
		// 施設医師別計画配分
		// ----------------------------
		final boolean isInsDocPlanDist = outputFile.getOutputType().equals(OutputType.INS_DOC_PLAN_DIST);
		if (isInsDocPlanDist) {
			// 担当者別配分ミス情報取得
			List<MrDistMiss> mrDistMissList = null;
			try {
				mrDistMissList = mrDistMissDao.searchList(MrDistMissDao.SORT_STRING, outputFileId);
			} catch (DataNotFoundException e) {
				// エラーにせずファイルには空欄で出力する。
			}
			// エクセルファイル生成
			final String templatePath = TemplateInfo.EXCEL_TEMPLETE_DPS600P0001.getTemplatePath(templateRootPath);
			DistMissListInsDocExportLogic logic = new DistMissListInsDocExportLogic(templatePath, commonDao.getSystemTime(), outputFile, mrDistMissList, messageSource);
			return logic.export();
		}

		final String errMsg = " 出力ファイル情報が不正";
		throw new SystemException(new Conveyance(FATAL_ERROR, errMsg));
	}

	// 関係会社別施設特約店別一覧
	public String outputRelationCompanyInsWsList(String templateRootPath, String sosCd, String downloadFileTempDir) {
		// 仕入れのカテゴリコード
		String shiireCd = dpsCodeMasterSearchService.searchCodeByDataKbn(DpsCodeMaster.SII.getDbValue()).get(0).getDataCd();

		// -------------------------
		// 引数チェック
		// -------------------------
		if (templateRootPath == null) {
			final String errMsg = "テンプレート配置パスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// -------------------------
		// 仕入品目取得
		// -------------------------
		List<PlannedProd> plannedProdList = null;
		try {
			plannedProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.IYAKU, shiireCd, null);
		} catch (DataNotFoundException e) {
			final String errMsg = "仕入品目取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}
		// -------------------------
		// 組織情報取得
		// -------------------------
		List<SosMst> sosMstList = null; // 組織リスト(営業所)
		String outputCd = null; // 支店/営業所3桁コード
		SosMst sosMst = null;
		try {
			sosMst = sosMstDAO.search(sosCd);
			// 支店の場合は下位の営業所を全て取得しリストに挿入
			if (sosMst.getBumonRank().equals(BumonRank.SITEN_TOKUYAKUTEN_BU)) {
				sosMstList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST, BumonRank.SITEN_TOKUYAKUTEN_BU, sosCd);
				outputCd = sosMst.getBrCode();
			}
			// 営業所の場合はそのままリストに挿入
			else {
				sosMstList = new ArrayList<SosMst>();
				sosMstList.add(sosMst);
				outputCd = sosMst.getDistCode();
				// 支店情報を取得する
				sosMst = sosMstDAO.search(sosMst.getUpSosCd());
			}
		} catch (DataNotFoundException e) {
			final String errMsg = "組織取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}
		// -------------------------
		// エクセルファイル生成
		// -------------------------
		final String templatePath = TemplateInfo.EXCEL_TEMPLATE_DPS401P0001.getTemplatePath(templateRootPath);
		RelationCompanyInsWsListExportLogic logic = new RelationCompanyInsWsListExportLogic(templatePath, commonDao.getSystemTime(), plannedProdList, sosMstList, outputCd,
			insWsPlanForRefDao, downloadFileTempDir, sosMst, officePlanDao, dpsPlannedCtgDao);
		return logic.export();
	}

	// 実績なし特約店別計画
	public ExportResult outputWsPlanListNonJisseki(String templateRootPath, WsPlanReferenceScDto refScDto) throws LogicalException {
		// -------------------------
		// 引数チェック
		// -------------------------
		if (templateRootPath == null) {
			final String errMsg = "テンプレート配置パスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (refScDto == null) {
			final String errMsg = "特約店別計画参照画面の検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// -----------------------------
		// サマリー用の検索条件DTOの生成
		// -----------------------------
		final String tytenCdPart = refScDto.getTmsTytenCdPart();
		final TytenKisLevel lebel = refScDto.getTytenKisLevel();
		final String category = refScDto.getCategory();
		final KaBaseKb kaBase = refScDto.getKaBaseKb();
		WsPlanSummaryScDto summaryScDto = new WsPlanSummaryScDto(tytenCdPart, null, lebel, category, kaBase);
		// -------------------------
		// 特約店別計画サマリー取得
		// -------------------------
		List<WsPlanSummary2> wsPlanSummaryList = null;
		try {
			wsPlanSummaryList = wsPlanSummaryDao.searchList(WsPlanSummaryDao.SORT_STRING_EXCEL, summaryScDto, false);
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")), e);
		}

		// -------------------------
		// エクセルファイル生成
		// -------------------------
		final String templatePath = TemplateInfo.EXCEL_TEMPLATE_DPS502P0003.getTemplatePath(templateRootPath);
		WsPlanListNonJissekiExportLogic logic = new WsPlanListNonJissekiExportLogic(templatePath, commonDao.getSystemTime(), refScDto, wsPlanSummaryList, tmsTytenMstUnDAO,dpsCodeMasterDao);
		return logic.export();
	}

	// 特約店別計画
	public ExportResult outputWsPlanList(String templateRootPath, WsPlanReferenceScDto refScDto) throws LogicalException {
		// -------------------------
		// 引数チェック
		// -------------------------
		if (templateRootPath == null) {
			final String errMsg = "テンプレート配置パスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (refScDto == null) {
			final String errMsg = "特約店別計画参照画面の検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// -----------------------------
		// サマリー用の検索条件DTOの生成
		// -----------------------------
		final String tytenCdPart = refScDto.getTmsTytenCdPart();
		final TytenKisLevel lebel = refScDto.getTytenKisLevel();
		final String category = refScDto.getCategory();
		final KaBaseKb kaBase = refScDto.getKaBaseKb();
		WsPlanSummaryScDto summaryScDto = new WsPlanSummaryScDto(tytenCdPart, null, lebel, category, kaBase);
		// -------------------------
		// 特約店別計画サマリー取得
		// -------------------------
		List<WsPlanSummary2> wsPlanSummaryList = null;
		try {
			wsPlanSummaryList = wsPlanSummaryDao.searchList(WsPlanSummaryDao.SORT_STRING, summaryScDto, true);
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")), e);
		}

		// -------------------------
		// エクセルファイル生成
		// -------------------------
		final String templatePath = TemplateInfo.EXCEL_TEMPLATE_DPS502P0001.getTemplatePath(templateRootPath);
		WsPlanListExportLogic logic = new WsPlanListExportLogic(templatePath, commonDao.getSystemTime(), refScDto, wsPlanSummaryList, tmsTytenMstUnDAO);
		return logic.export();
	}

	// ①担当者別品目別計画一覧
	public String outputMrPlanMMPList(String templateRootPath, String sosCd, MrPlanOutputDivision mrPlanOutputDivision, String downloadFileTempDir, String category) {
		// -------------------------
		// 引数チェック
		// -------------------------
		if (templateRootPath == null) {
			final String errMsg = "テンプレート配置パスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosCd == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (mrPlanOutputDivision == null) {
			final String errMsg = "担当者別品目別計画一覧の列挙がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (StringUtils.isBlank(downloadFileTempDir)) {
			final String errMsg = "ダウンロードファイル一時格納場所がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -------------------------
		// 営業所組織情報
		// -------------------------
		SosMst office = null;
		try {
			office = sosMstDAO.search(sosCd);
		} catch (DataNotFoundException e) {
			final String errMsg = "営業所取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// -------------------------
		// チームマップの取得
		// -------------------------
		Map<String, SosMst> teamMap = null;
		try {
			List<SosMst> teamList = null;
			teamList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST, OFFICE_TOKUYAKUTEN_G, sosCd);
			for (SosMst team : teamList) {
				if (teamMap == null) {
					teamMap = new LinkedHashMap<String, SosMst>(teamList.size());
				}
				teamMap.put(team.getSosCd(), team);
			}
		} catch (DataNotFoundException e) {
			// ONC組織の場合　→　チーム階層が無い第３階層組織の場合、第３階層の組織自体をteamMapに入れておく　//コメント変更：2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
			SosMst team;
			teamMap = new LinkedHashMap<String, SosMst>(1);
			try {
				team = sosMstDAO.search(sosCd);
				teamMap.put(team.getSosCd(), team);
			} catch (DataNotFoundException e1) {
				final String errMsg = "チーム一覧取得に失敗";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
			}
		}

		// -------------------------
		// 従業員リストマップの取得
		// -------------------------
		List<JgiMst> allJgiList = null;
		Map<String, List<JgiMst>> jgiListMap = null;
		try {
			List<JgiMst> jgiList = null;
			for (SosMst team : teamMap.values()) {
				String sosCd4 = team.getSosCd();
				jgiList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd4, SosListType.SHITEN_LIST, TEAM);
				if (allJgiList == null) {
					allJgiList = new ArrayList<JgiMst>();
				}
				allJgiList.addAll(jgiList);
				if (jgiListMap == null) {
					jgiListMap = new LinkedHashMap<String, List<JgiMst>>(teamMap.values().size());
				}
				jgiListMap.put(sosCd4, jgiList);
			}
		} catch (DataNotFoundException e) {
			// ONC組織の場合　→　チーム階層が無い第３階層組織の場合　//コメント変更：2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
			List<JgiMst> jgiList = null;
			try {
				for (SosMst area : teamMap.values()) {
					String sosCd3 = area.getSosCd();
					jgiList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd3, SosListType.SHITEN_LIST, OFFICE_TOKUYAKUTEN_G);
					if (allJgiList == null) {
						allJgiList = new ArrayList<JgiMst>();
					}
					allJgiList.addAll(jgiList);
					if (jgiListMap == null) {
						jgiListMap = new LinkedHashMap<String, List<JgiMst>>(teamMap.values().size());
					}
					jgiListMap.put(sosCd3, jgiList);
				}
			} catch (DataNotFoundException e1) {
				final String errMsg = "従業員一覧取得に失敗";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
			}
		}

		// -------------------------
		// 従業員×チーム検索マップ
		// -------------------------
		Map<Integer, String> jgiTeamMap = new HashMap<Integer, String>();
		if (allJgiList != null) {
			for (JgiMst jgiMst : allJgiList) {
				if (StringUtils.isNotBlank(jgiMst.getSosCd4())) {
					jgiTeamMap.put(jgiMst.getJgiNo(), jgiMst.getSosCd4());
				}
			}
		}

		// -------------------------
		// ワクチンのカテゴリ取得
		// -------------------------
		String vaccineCode = dpsCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.VAC.getDbValue()).get(0)
				.getDataCd();
		// -------------------------
		// ヘッダ情報の設定
		// -------------------------
		Sales sales = null;
		String dataKbn = null;
		String headerUh = null;
		String headerP = null;
		if (category.equals(vaccineCode)) {
			sales = Sales.VACCHIN;
			dataKbn = DpsCodeMaster.ITV.getDbValue();
		} else {
			sales = Sales.IYAKU;
			dataKbn = DpsCodeMaster.IT.getDbValue();
		}

		// 検索結果一覧
		List<DpsCCdMst> searchList = new ArrayList<DpsCCdMst>();
		try {
			// データ区分の検索
			searchList = dpsCodeMasterDao.searchCodeByDataKbn(dataKbn);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + dataKbn + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		//ヘッダの設定
		if (searchList.size() != 0 && searchList.size() >= 2) {
			//ヘッダ（UH）
			headerUh = searchList.get(0).getDataName();
			//ヘッダ（P）
			headerP = searchList.get(1).getDataName();
	    }

		// -------------------------
		// 計画対象品目
		// -------------------------
		List<PlannedProd> plannedProdList = null;
		try {
			plannedProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING2, sales, category, null);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目(MMP)リストの取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// -------------------------
		// 担当者別計画マップ
		// -------------------------
		Map<String, MrPlan> mrPlanMapByJgiProdMap = null;
		try {
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）ProdCategoryを引数で渡すように変更
// mod start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
//			mrPlanMapByJgiProdMap = createMrPlanMapByJgiProdMap(sosCd);
//			mrPlanMapByJgiProdMap = createMrPlanMapByJgiProdMap(sosCd, false);
			mrPlanMapByJgiProdMap = createMrPlanMapByJgiProdMap(sosCd, category, false);
// mod end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
		} catch (DataNotFoundException e) {
			mrPlanMapByJgiProdMap = new LinkedHashMap<String, MrPlan>();
		}

		// -------------------------
		// 実績マップ
		// -------------------------
		Map<String, DeliveryResultMr> deliveryResultByJgiProdInsTypeMap = null;
		try {
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）ProdCategoryを引数で渡すように変更
// mod start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
//			deliveryResultByJgiProdInsTypeMap = createDeliveryResultByJgiProdInsTypeMap(sosCd);
//			deliveryResultByJgiProdInsTypeMap = createDeliveryResultByJgiProdInsTypeMap(sosCd, false);
			deliveryResultByJgiProdInsTypeMap = createDeliveryResultByJgiProdInsTypeMap(sosCd, category, false);
// mod end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
		} catch (DataNotFoundException e) {
			deliveryResultByJgiProdInsTypeMap = new LinkedHashMap<String, DeliveryResultMr>();
		}

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　※DBから取得に変更
//		// 品目名称設定
//		String prodName = JPBU_STARS;
//		// ONC組織である場合
//		if(office.getOncSosFlg()) {
//			prodName = ONC_PROD;
//		}
		// -------------------------
		// 合計品目名称
		// -------------------------
		String prodName = "";
		try {
			//集計品目込みの品目一覧[DPS_C_VI_CPROD_XLS]を取得し、その最終行の品目名を合計品目名称とする
			List<PlannedProd> tmpProdList = cprodXlsDAO.searchList(CprodXlsDAO.SORT_STRING, sales, category, null);
//			List<PlannedProd> tmpProdList = cprodXlsDAO.searchList(CprodXlsDAO.SORT_STRING, Sales.IYAKU, category, null);
			prodName = tmpProdList.get(tmpProdList.size()-1).getProdName();
		} catch (DataNotFoundException e) {
			//万一、合計品目名称の取得に失敗した場合はエラーとせず続行。合計品目名称は空欄で表示する。
		}
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

		// -------------------------
		// ロジック呼出
		// -------------------------
		final String templatePath;
		int prodSize = plannedProdList.size();
		if (prodSize <= DPS302P0003_CHANGE_SIZE) {
			templatePath = TemplateInfo.EXCEL_TEMPLATE_DPS302P0003_1.getTemplatePath(templateRootPath);
		} else {
			templatePath = TemplateInfo.EXCEL_TEMPLATE_DPS302P0003_2.getTemplatePath(templateRootPath);
		}
		OutputMrPlanMMPListLogic logic = new OutputMrPlanMMPListLogic(templatePath, commonDao.getSystemTime(), downloadFileTempDir, mrPlanOutputDivision, office, teamMap, jgiListMap, jgiTeamMap,
			plannedProdList, mrPlanMapByJgiProdMap, deliveryResultByJgiProdInsTypeMap, prodName, headerUh, headerP);
		return logic.export();
	}

	// ②品目別担当者別計画検討表
	public String outputReviewProdMrMMPList(String templateRootPath, String sosCd, String downloadFileTempDir, String category ) {
		// -------------------------
		// 引数チェック
		// -------------------------
		if (templateRootPath == null) {
			final String errMsg = "テンプレート配置パスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosCd == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (StringUtils.isBlank(downloadFileTempDir)) {
			final String errMsg = "ダウンロードファイル一時格納場所がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -------------------------
		// 営業所組織情報
		// -------------------------
		SosMst office = null;
		try {
			office = sosMstDAO.search(sosCd);
		} catch (DataNotFoundException e) {
			final String errMsg = "営業所取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// -------------------------
		// チームマップの取得
		// -------------------------
		Map<String, SosMst> teamMap = null;
		try {
			List<SosMst> teamList = null;
			teamList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST, OFFICE_TOKUYAKUTEN_G, sosCd);
			for (SosMst team : teamList) {
				if (teamMap == null) {
					teamMap = new LinkedHashMap<String, SosMst>(teamList.size());
				}
				teamMap.put(team.getSosCd(), team);
			}
		} catch (DataNotFoundException e) {
			// ONC組織の場合　→　チーム階層が無い第３階層組織の場合、第３階層の組織自体をteamMapに入れておく　//コメント変更：2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
			SosMst team;
			teamMap = new LinkedHashMap<String, SosMst>(1);
			try {
				team = sosMstDAO.search(sosCd);
				teamMap.put(team.getSosCd(), team);
			} catch (DataNotFoundException e1) {
				final String errMsg = "チーム一覧取得に失敗";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
			}
		}

		// -------------------------
		// 従業員リストマップの取得
		// -------------------------
		List<JgiMst> allJgiList = null;
		Map<String, List<JgiMst>> jgiListMap = null;
		try {
			List<JgiMst> jgiList = null;
			for (SosMst team : teamMap.values()) {
				String sosCd4 = team.getSosCd();
				jgiList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd4, SosListType.SHITEN_LIST, TEAM);
				if (allJgiList == null) {
					allJgiList = new ArrayList<JgiMst>();
				}
				allJgiList.addAll(jgiList);
				if (jgiListMap == null) {
					jgiListMap = new LinkedHashMap<String, List<JgiMst>>(teamMap.values().size());
				}
				jgiListMap.put(sosCd4, jgiList);
			}
		} catch (DataNotFoundException e) {
			// ONC組織の場合　→　チーム階層が無い第３階層組織の場合　//コメント変更：2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
			List<JgiMst> jgiList = null;
			try {
				for (SosMst area : teamMap.values()) {
					String sosCd3 = area.getSosCd();
					jgiList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd3, SosListType.SHITEN_LIST, OFFICE_TOKUYAKUTEN_G);
					if (allJgiList == null) {
						allJgiList = new ArrayList<JgiMst>();
					}
					allJgiList.addAll(jgiList);
					if (jgiListMap == null) {
						jgiListMap = new LinkedHashMap<String, List<JgiMst>>(teamMap.values().size());
					}
					jgiListMap.put(sosCd3, jgiList);
				}
			} catch (DataNotFoundException e1) {
				final String errMsg = "従業員一覧取得に失敗";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
			}
		}

		// -------------------------
		// 従業員×チーム検索マップ
		// -------------------------
		Map<Integer, String> jgiTeamMap = new HashMap<Integer, String>();
		if (allJgiList != null) {
			for (JgiMst jgiMst : allJgiList) {
				if (StringUtils.isNotBlank(jgiMst.getSosCd4())) {
					jgiTeamMap.put(jgiMst.getJgiNo(), jgiMst.getSosCd4());
				}
			}
		}

		// -------------------------
		// ワクチンのカテゴリ取得
		// -------------------------
		String vaccineCode = dpsCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.VAC.getDbValue()).get(0)
				.getDataCd();
		// -------------------------
		// ヘッダ情報と単価の設定
		// -------------------------
		Sales sales = null;
		String dataKbn = null;
		String unitYen = null;
		String headerUh = null;
		String headerP = null;
		if (category.equals(vaccineCode)) {
			sales = Sales.VACCHIN;
			dataKbn = DpsCodeMaster.ITV.getDbValue();
			unitYen = ConstantsTool.FILE_TABLE_HEADER_B;
		} else {
			sales = Sales.IYAKU;
			dataKbn = DpsCodeMaster.IT.getDbValue();
			unitYen = ConstantsTool.FILE_TABLE_HEADER_Y;
		}

		// 検索結果一覧
		List<DpsCCdMst> searchList = new ArrayList<DpsCCdMst>();
		try {
			// データ区分の検索
			searchList = dpsCodeMasterDao.searchCodeByDataKbn(dataKbn);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + dataKbn + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		//ヘッダの設定
		if (searchList.size() != 0 && searchList.size() >= 2) {
			//ヘッダ（UH）
			headerUh = searchList.get(0).getDataName();
			//ヘッダ（P）
			headerP = searchList.get(1).getDataName();
	    }

		// -------------------------
		// 計画対象品目(MMP)
		// -------------------------
		List<PlannedProd> plannedProdList = null;
		try {
// mod start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
			plannedProdList = cprodXlsDAO.searchList(PlannedProdDAO.SORT_STRING2, sales, category, null);
//			plannedProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING2, sales, category, null);

// mod end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目(MMP)リストの取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// -------------------------
		// 担当者別計画マップ
		// -------------------------
		Map<String, MrPlan> mrPlanMapByJgiProdMap = null;
		try {
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）ProdCategoryを引数で渡すように変更
// mod start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
//			mrPlanMapByJgiProdMap = createMrPlanMapByJgiProdMap(sosCd);
//			mrPlanMapByJgiProdMap = createMrPlanMapByJgiProdMap(sosCd, true);
			mrPlanMapByJgiProdMap = createMrPlanMapByJgiProdMap(sosCd, category, true);
// mod end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
		} catch (DataNotFoundException e) {
			mrPlanMapByJgiProdMap = new LinkedHashMap<String, MrPlan>();
		}

		// -------------------------
		// 実績マップ
		// -------------------------
		Map<String, DeliveryResultMr> deliveryResultByJgiProdInsTypeMap = null;
		try {
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）ProdCategoryを引数で渡すように変更
// mod start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
//			deliveryResultByJgiProdInsTypeMap = createDeliveryResultByJgiProdInsTypeMap(sosCd);
//			deliveryResultByJgiProdInsTypeMap = createDeliveryResultByJgiProdInsTypeMap(sosCd, true);
			deliveryResultByJgiProdInsTypeMap = createDeliveryResultByJgiProdInsTypeMap(sosCd, category, true);
// mod end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
		} catch (DataNotFoundException e) {
			deliveryResultByJgiProdInsTypeMap = new LinkedHashMap<String, DeliveryResultMr>();
		}

// mod start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定　合計行もSQLレベルで取得しているため、名称の特別扱いも不要
//		// 品目名称設定
//		String prodName = JPBU_STARS;
//		// ONC組織である場合
//		if(office.getOncSosFlg()) {
//			prodName = ONC_PROD;
//		}
		String prodName = "";//未使用
// mod end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定　合計行もSQLレベルで取得しているため、名称の特別扱いも不要

		// -------------------------
		// ロジック呼出
		// -------------------------
		final String templatePath = TemplateInfo.EXCEL_TEMPLATE_DPS302P0004.getTemplatePath(templateRootPath);
		OutputReviewProdMrMMPListLogic logic = new OutputReviewProdMrMMPListLogic(templatePath, commonDao.getSystemTime(), downloadFileTempDir, office, teamMap, jgiListMap,
			jgiTeamMap, plannedProdList, mrPlanMapByJgiProdMap, deliveryResultByJgiProdInsTypeMap, prodName, unitYen, headerUh, headerP);
		return logic.export();
	}

	// ③担当者別品目別計画検討表
	public String outputReviewMrProdMMPList(String templateRootPath, String sosCd, String downloadFileTempDir, String category) {
		// -------------------------
		// 引数チェック
		// -------------------------
		if (templateRootPath == null) {
			final String errMsg = "テンプレート配置パスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosCd == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (StringUtils.isBlank(downloadFileTempDir)) {
			final String errMsg = "ダウンロードファイル一時格納場所がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -------------------------
		// 営業所組織情報
		// -------------------------
		SosMst office = null;
		try {
			office = sosMstDAO.search(sosCd);
		} catch (DataNotFoundException e) {
			final String errMsg = "営業所取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// -------------------------
		// チームマップの取得
		// -------------------------
		Map<String, SosMst> teamMap = null;
		try {
			List<SosMst> teamList = null;
			teamList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST, OFFICE_TOKUYAKUTEN_G, sosCd);
			for (SosMst team : teamList) {
				if (teamMap == null) {
					teamMap = new LinkedHashMap<String, SosMst>(teamList.size());
				}
				teamMap.put(team.getSosCd(), team);
			}
		} catch (DataNotFoundException e) {
			// ONC組織の場合
			SosMst team;
			teamMap = new LinkedHashMap<String, SosMst>(1);
			try {
				team = sosMstDAO.search(sosCd);
				teamMap.put(team.getSosCd(), team);
			} catch (DataNotFoundException e1) {
				final String errMsg = "チーム一覧取得に失敗";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
			}
		}

		// -------------------------
		// 従業員リストマップの取得
		// -------------------------
		List<JgiMst> allJgiList = null;
		Map<String, List<JgiMst>> jgiListMap = null;
		try {
			List<JgiMst> jgiList = null;
			for (SosMst team : teamMap.values()) {
				String sosCd4 = team.getSosCd();
				jgiList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd4, SosListType.SHITEN_LIST, TEAM);
				if (allJgiList == null) {
					allJgiList = new ArrayList<JgiMst>();
				}
				allJgiList.addAll(jgiList);
				if (jgiListMap == null) {
					jgiListMap = new LinkedHashMap<String, List<JgiMst>>(teamMap.values().size());
				}
				jgiListMap.put(sosCd4, jgiList);
			}
		} catch (DataNotFoundException e) {
			// ONC組織の場合
			List<JgiMst> jgiList = null;
			try {
				for (SosMst area : teamMap.values()) {
					String sosCd3 = area.getSosCd();
					jgiList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd3, SosListType.SHITEN_LIST, OFFICE_TOKUYAKUTEN_G);
					if (allJgiList == null) {
						allJgiList = new ArrayList<JgiMst>();
					}
					allJgiList.addAll(jgiList);
					if (jgiListMap == null) {
						jgiListMap = new LinkedHashMap<String, List<JgiMst>>(teamMap.values().size());
					}
					jgiListMap.put(sosCd3, jgiList);
				}
			} catch (DataNotFoundException e1) {
				final String errMsg = "従業員一覧取得に失敗";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
			}
		}

		// -------------------------
		// 従業員×チーム検索マップ
		// -------------------------
		Map<Integer, String> jgiTeamMap = new HashMap<Integer, String>();
		if (allJgiList != null) {
			for (JgiMst jgiMst : allJgiList) {
				if (StringUtils.isNotBlank(jgiMst.getSosCd4())) {
					jgiTeamMap.put(jgiMst.getJgiNo(), jgiMst.getSosCd4());
				}
			}
		}

		// -------------------------
		// ワクチンのカテゴリ取得
		// -------------------------
		String vaccineCode = dpsCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.VAC.getDbValue()).get(0)
				.getDataCd();
		// -------------------------
		// ヘッダ情報と単価の設定
		// -------------------------
		Sales sales = null;
		String dataKbn = null;
		String unitYen = null;
		String headerUh = null;
		String headerP = null;
		if (category.equals(vaccineCode)) {
			sales = Sales.VACCHIN;
			dataKbn = DpsCodeMaster.ITV.getDbValue();
			unitYen = ConstantsTool.FILE_TABLE_HEADER_B;
		} else {
			sales = Sales.IYAKU;
			dataKbn = DpsCodeMaster.IT.getDbValue();
			unitYen = ConstantsTool.FILE_TABLE_HEADER_Y;
		}

		// 検索結果一覧
		List<DpsCCdMst> searchList = new ArrayList<DpsCCdMst>();
		try {
			// データ区分の検索
			searchList = dpsCodeMasterDao.searchCodeByDataKbn(dataKbn);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + dataKbn + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		//ヘッダの設定
		if (searchList.size() != 0 && searchList.size() >= 2) {
			//ヘッダ（UH）
			headerUh = searchList.get(0).getDataName();
			//ヘッダ（P）
			headerP = searchList.get(1).getDataName();
	    }

		// -------------------------
		// 計画対象品目(MMP)
		// -------------------------
		List<PlannedProd> plannedProdList = null;
		try {
// mod start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
//			plannedProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.IYAKU, category, null);
//			plannedProdList = cprodXlsDAO.searchList(CprodXlsDAO.SORT_STRING, Sales.IYAKU, category, null);
// mod end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
			// STEP4　
			plannedProdList = cprodXlsDAO.searchList(PlannedProdDAO.SORT_STRING2, sales, category, null);

		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目(MMP)リストの取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// -------------------------
		// 担当者別計画マップ
		// -------------------------
		Map<String, MrPlan> mrPlanMapByJgiProdMap = null;
		try {
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）ProdCategoryを引数で渡すように変更
// mod start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
//			mrPlanMapByJgiProdMap = createMrPlanMapByJgiProdMap(sosCd);
//			mrPlanMapByJgiProdMap = createMrPlanMapByJgiProdMap(sosCd, true);
			mrPlanMapByJgiProdMap = createMrPlanMapByJgiProdMap(sosCd, category, true);
// mod end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
		} catch (DataNotFoundException e) {
			mrPlanMapByJgiProdMap = new LinkedHashMap<String, MrPlan>();
		}

		// -------------------------
		// 実績マップ
		// -------------------------
		Map<String, DeliveryResultMr> deliveryResultByJgiProdInsTypeMap = null;
		try {
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）ProdCategoryを引数で渡すように変更
// mod start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
//			deliveryResultByJgiProdInsTypeMap = createDeliveryResultByJgiProdInsTypeMap(sosCd);
//			deliveryResultByJgiProdInsTypeMap = createDeliveryResultByJgiProdInsTypeMap(sosCd, true);
			deliveryResultByJgiProdInsTypeMap = createDeliveryResultByJgiProdInsTypeMap(sosCd, category, true);
// mod end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
		} catch (DataNotFoundException e) {
			deliveryResultByJgiProdInsTypeMap = new LinkedHashMap<String, DeliveryResultMr>();
		}

// mod start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定　合計行もSQLレベルで取得しているため、名称の特別扱いも不要
//		// 品目名称設定
//		String prodName = JPBU_STARS;
//		// ONC組織である場合
//		if(office.getOncSosFlg()) {
//			prodName = ONC_PROD;
//		}
		String prodName = "";	//未使用
// mod end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定　合計行もSQLレベルで取得しているため、名称の特別扱いも不要

		// -------------------------
		// ロジック呼出
		// -------------------------
		final String templatePath = TemplateInfo.EXCEL_TEMPLATE_DPS302P0005.getTemplatePath(templateRootPath);
		OutputReviewMrProdMMPListLogic logic = new OutputReviewMrProdMMPListLogic(templatePath, commonDao.getSystemTime(), downloadFileTempDir, office, teamMap, jgiListMap,
			jgiTeamMap, plannedProdList, mrPlanMapByJgiProdMap, deliveryResultByJgiProdInsTypeMap, prodName, unitYen, headerUh, headerP);
		return logic.export();
	}

	// ④チーム担当者計画検討表を出力
	public String outputTeamMrReport(String templateRootPath, String sosCd, String downloadFileTempDir, String category) {

		// -------------------------
		// 引数チェック
		// -------------------------
		if (StringUtils.isBlank(templateRootPath)) {
			final String errMsg = "テンプレート配置パスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (StringUtils.isBlank(sosCd)) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (StringUtils.isBlank(downloadFileTempDir)) {
			final String errMsg = "ダウンロードファイル一時格納場所がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -------------------------
		// 営業所組織情報
		// -------------------------
		SosMst office = null;
		try {
			office = sosMstDAO.search(sosCd);
		} catch (DataNotFoundException e) {
			final String errMsg = "営業所取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// -------------------------
		// チームマップの取得
		// -------------------------
		Map<String, SosMst> teamMap = null;
		try {
			List<SosMst> teamList = null;
			teamList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST, OFFICE_TOKUYAKUTEN_G, sosCd);

			for (SosMst team : teamList) {
				if (teamMap == null) {
					teamMap = new LinkedHashMap<String, SosMst>(teamList.size());
				}
				teamMap.put(team.getSosCd(), team);
			}

		} catch (DataNotFoundException e) {
			// チームがない場合は営業所組織を設定
			SosMst team;
			teamMap = new LinkedHashMap<String, SosMst>(1);
			try {
				team = sosMstDAO.search(sosCd);
				teamMap.put(team.getSosCd(), team);
			} catch (DataNotFoundException e1) {
				final String errMsg = "組織取得に失敗:" + sosCd;
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
			}
		}

		// -------------------------
		// 従業員リストマップの取得
		// -------------------------
		List<JgiMst> allJgiList = null;
		Map<String, List<JgiMst>> jgiListMap = null;
		try {
			List<JgiMst> jgiList = null;
			for (SosMst team : teamMap.values()) {
				String _sosCd = team.getSosCd();

				if(team.getBumonRank() == BumonRank.TEAM){
					// 組織がチームの場合
					jgiList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, _sosCd, SosListType.SHITEN_LIST, TEAM);
				} else {
					// 組織が営業所・エリアの場合（チーム不在）
					jgiList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, _sosCd, SosListType.SHITEN_LIST, OFFICE_TOKUYAKUTEN_G);
				}

				if (allJgiList == null) {
					allJgiList = new ArrayList<JgiMst>();
				}
				allJgiList.addAll(jgiList);
				if (jgiListMap == null) {
					jgiListMap = new LinkedHashMap<String, List<JgiMst>>(teamMap.values().size());
				}
				jgiListMap.put(_sosCd, jgiList);
			}

		} catch (DataNotFoundException e) {
			final String errMsg = "従業員一覧取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// -------------------------
		// 従業員×チーム検索マップ
		// -------------------------
		Map<Integer, String> jgiTeamMap = new HashMap<Integer, String>();
		if (allJgiList != null) {
			for (JgiMst jgiMst : allJgiList) {
				if (StringUtils.isNotBlank(jgiMst.getSosCd4()) && !SosMst.BRANK_SOS_CD.equals(jgiMst.getSosCd4())) {
					jgiTeamMap.put(jgiMst.getJgiNo(), jgiMst.getSosCd4());
				} else {
					jgiTeamMap.put(jgiMst.getJgiNo(), jgiMst.getSosCd3());
				}
			}
		}

		// -------------------------
		// ワクチンのカテゴリ取得
		// -------------------------
		String vaccineCode = dpsCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.VAC.getDbValue()).get(0)
				.getDataCd();
		// -------------------------
		// ヘッダ情報と単価の設定
		// -------------------------
		Sales sales = null;
		String dataKbn = null;
		String unitYen = null;
		String headerUh = null;
		String headerP = null;
		if (category.equals(vaccineCode)) {
			sales = Sales.VACCHIN;
			dataKbn = DpsCodeMaster.ITV.getDbValue();
			unitYen = ConstantsTool.FILE_TABLE_HEADER_B;
		} else {
			sales = Sales.IYAKU;
			dataKbn = DpsCodeMaster.IT.getDbValue();
			unitYen = ConstantsTool.FILE_TABLE_HEADER_Y;
		}

		// 検索結果一覧
		List<DpsCCdMst> searchList = new ArrayList<DpsCCdMst>();
		try {
			// データ区分の検索
			searchList = dpsCodeMasterDao.searchCodeByDataKbn(dataKbn);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + dataKbn + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		//ヘッダの設定
		if (searchList.size() != 0 && searchList.size() >= 2) {
			//ヘッダ（UH）
			headerUh = searchList.get(0).getDataName();
			//ヘッダ（P）
			headerP = searchList.get(1).getDataName();
	    }

		// -------------------------
		// 計画対象品目(MMP)
		// -------------------------
		List<PlannedProd> plannedProdList = null;
		try {
// mod start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
//			plannedProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.IYAKU, category, null);
//			plannedProdList = cprodXlsDAO.searchList(CprodXlsDAO.SORT_STRING, Sales.IYAKU, category, null);
// mod end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
			plannedProdList = cprodXlsDAO.searchList(PlannedProdDAO.SORT_STRING2, sales, category, null);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目(MMP)リストの取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// -------------------------
		// 営業所計画
		// -------------------------
		Map<String, OfficePlan> officePlanMap = new LinkedHashMap<String, OfficePlan>();
		try {
			List<OfficePlan> officePlanList = null;
// mod start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
//			officePlanList = officePlanDao.searchList(OfficePlanDao.SORT_STRING, sosCd, category);
			officePlanList = officePlanDao.searchListReport(OfficePlanDao.SORT_STRING, sosCd, category);
// mod end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
			for (OfficePlan officePlan : officePlanList) {
				officePlanMap.put(officePlan.getProdCode(), officePlan);
			}
		} catch (DataNotFoundException e) {
			// NotError
		}

		FileInputStream fileIn = null;
		try {

			// -------------------------
			// ファイル生成準備
			// -------------------------

			// テンプレートファイルパス
			final String templatePath = TemplateInfo.EXCEL_TEMPLETE_DPS302P0002.getTemplatePath(templateRootPath);

			// テンプレートファイルの読込
			fileIn = new FileInputStream(templatePath);

			// テンプレートファイル用意
			// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//			POIFSFileSystem poiFS = new POIFSFileSystem(fileIn);
//			HSSFWorkbook workBook = new HSSFWorkbook(poiFS);
			XSSFWorkbook workBook = new XSSFWorkbook(fileIn);
			// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応

			// 出力日付の取得
			Date sysDate = commonDao.getSystemTime();

			// Bean初期化
			POIBean poiBean = new POIBean(workBook);

			// -------------------------
			// シート設定
			// -------------------------
// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
//			MrTeamReportExportLogic logic = new MrTeamReportExportLogic(sysDate, poiBean, teamMap, jgiListMap, jgiTeamMap);
			MrTeamReportExportLogic logic = new MrTeamReportExportLogic(office, sysDate, poiBean, teamMap, jgiListMap, jgiTeamMap);
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

			// 共通情報埋め込み
			poiBean.setWorkSheetIdx(0);
			int allRowCnt = logic.createTemplete();

// mod start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定　テンプレートから串刺し関数削除、S、Eシートの削除を行ったため、シートのコピー方法を簡略化
//			// 1品目目のシートを作成(末尾に作成される)
//			poiBean.cloneSheet(0);
//
//			// 1品目目のシートから串刺し関数を削除([ＭＭＰ計]、[S]、[E]、[1品目]の順なので(3)となる)
//			poiBean.setWorkSheetIdx(3);
//			logic.clearFunction();
//
//			// 品目分シートを作成([ＭＭＰ計]、[S]、[E]、[1品目]の順なので(3)となる)
			for (int i = 1; i < plannedProdList.size(); i++) {
//				poiBean.cloneSheet(3);
				// mod Start 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
				//poiBean.cloneSheet(0);
				poiBean.cloneSheet(i-1);
				// mod End 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
			}
// mod end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定　テンプレートから串刺し関数削除、S、Eシートの削除を行ったため、シートのコピー方法を簡略化

			// 各シートに値を埋め込む
			for (int i = 0; i < plannedProdList.size(); i++) {

// mod start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定　テンプレートからS、Eシートの削除、MMP品シートの特別扱いを廃止
//				// [ＭＭＰ品]、[S]、[E]に各品目が続くので + 3
//				poiBean.setWorkSheetIdx(i + 3);
				poiBean.setWorkSheetIdx(i);
// mod end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定　テンプレートからS、Eシートの削除、MMP品シートの特別扱いを廃止

				PlannedProd prod = plannedProdList.get(i);
				String prodCode = prod.getProdCode();

				// ------------------------
				// 納入実績（従業員単位）
				// ------------------------
				List<DeliveryResultMr> dList = null;
				try {
					// 実績の取得(雑担込)
// mod start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
//					dList = deliveryResultMrDao.searchByProd(DeliveryResultMrDao.SORT_STRING, prodCode, sosCd, null, null);
					// mod start 2021/8/19 h.kaneko
					//dList = deliveryResultMrDao.searchByProdReport(DeliveryResultMrDao.SORT_STRING, prodCode, sosCd, null, null, Sales.IYAKU, category);
					dList = deliveryResultMrDao.searchByProdReport(DeliveryResultMrDao.SORT_STRING, prodCode, sosCd, null, null, sales, category);
					// mod end 2021/8/19
// mod end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
				} catch (DataNotFoundException e) {
					// NotError
				}

				// ------------------------
				// チーム別計画マップ
				// ------------------------
				Map<String, TeamPlan> teamPlanMap = new LinkedHashMap<String, TeamPlan>();
				for (SosMst team : teamMap.values()) {

					if(team.getBumonRank() == BumonRank.TEAM){

						try {
// mod start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
//							TeamPlan teamPlan = teamPlanDao.searchUk(team.getSosCd(), prodCode);
							TeamPlan teamPlan = teamPlanDao.searchUkReport(team.getSosCd(), prodCode);
// mod end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
							teamPlanMap.put(teamPlan.getSosCd(), teamPlan);

						} catch (DataNotFoundException e) {
							// NotError
						}

					} else {

						MrPlanSosSummaryDto mrPlanTeamSummary = null;
						try {
// mod start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
//							mrPlanTeamSummary = mrPlanDao.searchSosSummary(prodCode,team.getSosCd());
							mrPlanTeamSummary = mrPlanDao.searchSosSummaryReport(prodCode,team.getSosCd());
// mod end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定

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
							teamPlanMap.put(teamPlan.getSosCd(), teamPlan);

						} catch (DataNotFoundException e) {
							// NotError
						}
					}
				}

				// ------------------------
				// 担当者別計画マップ
				// ------------------------
				Map<Integer, MrPlan> mrPlanMap = new LinkedHashMap<Integer, MrPlan>();
				for (SosMst team : teamMap.values()) {
					try {
						List<MrPlan> mPList;
						if(team.getBumonRank() == BumonRank.TEAM){
							// 組織がチームの場合
// mod start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
//							mPList = mrPlanDao.searchByTeamCd(MrPlanDao.SORT_STRING, team.getSosCd(), prodCode, category);
							mPList = mrPlanDao.searchByTeamCdReport(MrPlanDao.SORT_STRING, team.getSosCd(), prodCode, category);
// mod end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
						} else {
							// 組織が営業所・エリアの場合（チーム不在）
// mod start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
//							mPList = mrPlanDao.searchBySosCd(MrPlanDao.SORT_STRING, team.getSosCd(), prodCode, category);
							mPList = mrPlanDao.searchBySosCdReport(MrPlanDao.SORT_STRING, team.getSosCd(), prodCode, category);
// mod end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
						}
						for (MrPlan mPlan : mPList) {
							mrPlanMap.put(mPlan.getJgiNo(), mPlan);
						}
					} catch (DataNotFoundException e) {
						// NotError
					}
				}
//				logic.execute(allRowCnt, prod, officePlanMap.get(prodCode), dList, teamPlanMap, mrPlanMap);
				logic.execute(allRowCnt, prod, officePlanMap.get(prodCode), dList, teamPlanMap, mrPlanMap, unitYen, headerUh, headerP);
			}

// del start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
//			// ＭＭＰ計のシートに設定
//			poiBean.setWorkSheetIdx(0);
//
//			// 品目名称設定
//			String prodName = JPBU_STARS;
//			// ONC組織である場合
//			if(office.getOncSosFlg()) {
//				prodName = ONC_PROD;
//			}
//			logic.execute(allRowCnt, prodName);
// del end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定

			// -------------------------
			// ファイル出力
			// -------------------------

			// 出力ファイル名称
			final String dateTxt = DateUtil.toString(sysDate, "yyyyMMddHHmmss");
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
//			String outputFileName = "TeamMR" + "_" + office.getBrCode() + office.getDistCode() + "_" + dateTxt + ".xls";
			String outputFileName = "OfficeMR" + "_" + office.getBrCode() + office.getDistCode() + "_" + dateTxt + ".xlsx";
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

			// 書込み
			FileOutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(new File(downloadFileTempDir + "/" + outputFileName));
				ExportResultExcelImpl exportResult = new ExportResultExcelImpl(outputFileName, workBook);
				exportResult.export(outputStream);
				return exportResult.getName();
			} finally {
				IOUtils.closeQuietly(outputStream);
			}
		} catch (IOException e) {
			IOUtils.closeQuietly(fileIn);
			throw new SystemException(new Conveyance(PARAMETER_ERROR, "チーム担当者計画検討表作成中にIOErrorが発生"), e);
		} finally {
			IOUtils.closeQuietly(fileIn);
		}
	}

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　ProdCategoryを引数で渡すように変更
// mod start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
	/**
	 * [従業員番号+品目固定コード]をキーに持つ担当者別計画のマップを作成する。<br>
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param forReview　検討表用フラグ
	 * @return 担当者別計画のマップ
	 * @throws DataNotFoundException データが見つからない場合にスロー
	 */
//	private Map<String, MrPlan> createMrPlanMapByJgiProdMap(String sosCd3) throws DataNotFoundException {
//	private Map<String, MrPlan> createMrPlanMapByJgiProdMap(String sosCd3, boolean forReview) throws DataNotFoundException {
//  private Map<String, MrPlan> createMrPlanMapByJgiProdMap(String sosCd3, ProdCategory category, boolean forReview) throws DataNotFoundException {
	private Map<String, MrPlan> createMrPlanMapByJgiProdMap(String sosCd3, String category, boolean forReview) throws DataNotFoundException {
// mod end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定

//		// 組織からカテゴリ判定（MMP or ONC）
//		SosMst sosMst = sosMstDAO.search(sosCd3);
//		ProdCategory category = ProdCategory.getSosCategory(sosMst.getOncSosFlg());
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

// mod start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
		List<MrPlan> mrPlanList = null;
		if(!forReview){
			mrPlanList = mrPlanDao.searchBySosCd(MrPlanDao.SORT_STRING2, sosCd3, null, category);
		}else{
			mrPlanList = mrPlanDao.searchBySosCdReport(MrPlanDao.SORT_STRING2, sosCd3, null, category);
		}
// mod end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
		Map<String, MrPlan> mrPlanMapByJgiProd = new LinkedHashMap<String, MrPlan>();
		for (MrPlan mrPlan : mrPlanList) {
			Integer jgiNo = mrPlan.getJgiNo();
			String prodCode = mrPlan.getProdCode();
			mrPlanMapByJgiProd.put(jgiNo.toString() + prodCode, mrPlan);
		}
		return mrPlanMapByJgiProd;
	}

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　ProdCategoryを引数で渡すように変更
// mod start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
	/**
	 * [従業員番号+品目固定コード+対象区分(UH/P)]をキーに持つ実績のマップを作成する。<br>
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param forReview　検討表用フラグ
	 * @return 実績のマップ
	 * @throws DataNotFoundException データが見つからない場合にスロー
	 */
//	private Map<String, DeliveryResultMr> createDeliveryResultByJgiProdInsTypeMap(String sosCd3) throws DataNotFoundException {
//	private Map<String, DeliveryResultMr> createDeliveryResultByJgiProdInsTypeMap(String sosCd3, boolean forReview) throws DataNotFoundException {
//	private Map<String, DeliveryResultMr> createDeliveryResultByJgiProdInsTypeMap(String sosCd3, ProdCategory category, boolean forReview) throws DataNotFoundException {
	private Map<String, DeliveryResultMr> createDeliveryResultByJgiProdInsTypeMap(String sosCd3, String category, boolean forReview) throws DataNotFoundException {
// mod end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定

//		// 組織からカテゴリ判定（MMP or ONC）
//		SosMst sosMst = sosMstDAO.search(sosCd3);
//		ProdCategory category = ProdCategory.getSosCategory(sosMst.getOncSosFlg());
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

// mod start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
		List<DeliveryResultMr> deliveryResultMrList = null;
		if(!forReview){
			deliveryResultMrList = deliveryResultMrDao.searchBySosNonZatu(DeliveryResultMrDao.SORT_STRING2, sosCd3, null, null, category);
		}else{
			deliveryResultMrList = deliveryResultMrDao.searchBySosNonZatuReport(DeliveryResultMrDao.SORT_STRING2, sosCd3, null, null, category);
		}
// mod end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
		Map<String, DeliveryResultMr> deliveryResultByJgiProdInsType = new LinkedHashMap<String, DeliveryResultMr>();
		for (DeliveryResultMr deliveryResultMr : deliveryResultMrList) {
			Integer jgiNo = deliveryResultMr.getJgiNo();
			String prodCode = deliveryResultMr.getProdCode();
			InsType insType = deliveryResultMr.getInsType();
			deliveryResultByJgiProdInsType.put(jgiNo.toString() + prodCode + insType, deliveryResultMr);
		}
		return deliveryResultByJgiProdInsType;
	}

	// 計画立案準備 営業所計画アップロード用のフォーマットファイル(指定したカテゴリ)
	public ExportResult outputDistPlanningList(String templateRootPath, DistPlanningListSummaryScDto scDto) throws LogicalException {
		// -------------------------
		// 引数チェック
		// -------------------------
		if (templateRootPath == null) {
			final String errMsg = "テンプレート配置パスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto == null) {
			final String errMsg = "営業所計画アップロード用のフォーマットファイルの検索条件がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// -------------------------
		// 営業所計画アップロード用のフォーマットファイルサマリー取得
		// -------------------------
		List<DistPlanningListDLSummary> distPlanningListDLSummaryList = null;
		try {
			distPlanningListDLSummaryList = distPlanningListDLSummaryDao.searchList(DistPlanningListDLSummaryDao.SORT_STRING_EXCEL, scDto);

		} catch (DataNotFoundException e) {
			// mod Start 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
			//throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")), e);
			return null;
			// mod End 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
		}

		// -------------------------
		// エクセルファイル生成
		// -------------------------
		final String templatePath = TemplateInfo.EXCEL_TEMPLETE_DPS920P0001.getTemplatePath(templateRootPath);
		DistPlanningListExportLogic logic = new DistPlanningListExportLogic(templatePath, commonDao.getSystemTime(),
				distPlanningListDLSummaryList);
		return logic.export();
	}

	// 計画立案準備 営業所計画アップロード用のフォーマットファイル(指定したカテゴリ以外)
	public ExportResult outputDistPlanningListExceptCategory(String templateRootPath, DistPlanningListSummaryScDto scDto) throws LogicalException {
		// -------------------------
		// 引数チェック
		// -------------------------
		if (templateRootPath == null) {
			final String errMsg = "テンプレート配置パスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto == null) {
			final String errMsg = "営業所計画アップロード用のフォーマットファイルの検索条件がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// -------------------------
		// 営業所計画アップロード用のフォーマットファイルサマリー取得
		// -------------------------
		List<DistPlanningListDLSummary> distPlanningListDLSummaryListNotSii = null;
		try {
			distPlanningListDLSummaryListNotSii = distPlanningListDLSummaryDao.searchExceptCategoryList(DistPlanningListDLSummaryDao.SORT_STRING_EXCEL, scDto);

		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")), e);
		}

		// -------------------------
		// エクセルファイル生成
		// -------------------------
		final String templatePath = TemplateInfo.EXCEL_TEMPLETE_DPS920P0001.getTemplatePath(templateRootPath);
		 DistPlanningListExportLogic logic = new DistPlanningListExportLogic(templatePath, commonDao.getSystemTime(),
				 distPlanningListDLSummaryListNotSii);
        return logic.export();
	}

	// 本部ログインユーザの場合、仕入のみと仕入以外のファイルを作成する
	public ExportResult outputDistPlanningListHonbu(String templateRootPath, DistPlanningListSummaryScDto scDto)
			throws LogicalException {
		// 本部ログインユーザの場合、仕入のみと仕入以外のファイルを作成する
		ExportResultExcelImpl planningListSii = (ExportResultExcelImpl)outputDistPlanningList(templateRootPath, scDto);
		ExportResultExcelImpl planningList = (ExportResultExcelImpl)outputDistPlanningListExceptCategory(templateRootPath, scDto);
		// ZIPファイル名
		String zipFileName = OUTPUT_FILE_NAME_HEADER + "_" + DateUtil.toString(commonDao.getSystemTime(), "yyyyMMddHHmmss") + ".zip";
		// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//		Map<String ,HSSFWorkbook> wbMap = new HashMap<String, HSSFWorkbook>();
		Map<String ,XSSFWorkbook> wbMap = new HashMap<String, XSSFWorkbook>();
		// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		String fileNm;

		// add Start 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
		if (planningListSii == null) {
			fileNm = planningList.getName().replace(OUTPUT_FILE_NAME_HEADER,OUTPUT_FILE_NAME_HEADER.concat("_iyaku"));
			return new ExportResultExcelImpl(fileNm, planningList.getWorkBookMap().get(planningList.getName()));
		}
		// add End 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能

		// 仕入れのみ
		if (planningListSii.getWorkBookMap() != null) {
			// APサーバー上で動かすと最後のエクセルファイルのみとなるので、putAllを使わない
			//wbMap.putAll(planningListSii.getWorkBookMap());
			fileNm = planningListSii.getName().replace(OUTPUT_FILE_NAME_HEADER,OUTPUT_FILE_NAME_HEADER.concat("_siire"));
			wbMap.put(fileNm, planningListSii.getWorkBookMap().get(planningListSii.getName()));
		}
		// 仕入れ以外
		if (planningList.getWorkBookMap() != null) {
			// APサーバー上で動かすと最後のエクセルファイルのみとなるので、putAllを使わない
			//wbMap.putAll(planningList.getWorkBookMap());
			fileNm = planningList.getName().replace(OUTPUT_FILE_NAME_HEADER,OUTPUT_FILE_NAME_HEADER.concat("_iyaku"));
			wbMap.put(fileNm, planningList.getWorkBookMap().get(planningList.getName()));
		}
		ExportResultExcelImpl result = new ExportResultExcelImpl(zipFileName);
		// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//		for (Map.Entry<String, HSSFWorkbook> set : wbMap.entrySet()) {
		for (Map.Entry<String, XSSFWorkbook> set : wbMap.entrySet()) {
		// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
			result.addWorkBook(set.getKey(), set.getValue());
		}
		return result;

	}

	// 計画立案準備 アップロードファイルのエラー内容
	public ExportResult outputDistPlanningErrList(String templateRootPath, List<DistPlanningListULSummary> distPlanningListErrSummaryList) throws LogicalException {
		// -------------------------
		// 引数チェック
		// -------------------------
		if (templateRootPath == null) {
			final String errMsg = "テンプレート配置パスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (distPlanningListErrSummaryList == null) {
			final String errMsg = "営業所計画アップロードファイルのエラー内容がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// -------------------------
		// エクセルファイル生成
		// -------------------------
		final String templatePath = TemplateInfo.EXCEL_TEMPLETE_DPS920P0001.getTemplatePath(templateRootPath);
		DistPlanningErrListExportLogic logic = new DistPlanningErrListExportLogic(templatePath, commonDao.getSystemTime(),
				distPlanningListErrSummaryList);
		return logic.export();
	}

// add Start 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
	// 計画支援フォロー用のフォーマットファイル
	public ExportResult outputDelFacilitiesAndAdjustmentsList(String templateRootPath, DistPlanningListSummaryScDto scDto) throws LogicalException {
		// -------------------------
		// 引数チェック
		// -------------------------
		if (templateRootPath == null) {
			final String errMsg = "テンプレート配置パスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto == null) {
			final String errMsg = "営業所計画アップロード用のフォーマットファイルの検索条件がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		List<DpsCCdMst> codeList = dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.SII.getDbValue());
        if(codeList.size() > 1) {
            final String errMsg = "計画支援汎用マスタに仕入れのカテゴリコードが複数登録されています。";
            throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg));
        }
        String shiireCategory = codeList.get(0).getDataCd();


		List<String> dataCds = new ArrayList<String>();
		dataCds.add(scDto.getSelectCategory());

		// 検索結果一覧
		List<DpsCCdMst> searchList = new ArrayList<DpsCCdMst>();
		try {
			// カテゴリの検索
			searchList = dpsCodeMasterDao.searchCategoryList(DpsCodeMaster.CAT.getDbValue(), dataCds, PLAN_ID);
		} catch (DataNotFoundException e) {
			final String errMsg = "カテゴリ名取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		List<DpsPlannedCtg> plannedCategory = new ArrayList<DpsPlannedCtg>();
		try {
			plannedCategory = dpsPlannedCtgDao.searchTermCtg(scDto.getSelectCategory());
		}catch (DataNotFoundException e) {
			final String errMsg = "計画対象カテゴリ領域に、" + scDto.getSelectCategory() + "のデータが登録されていません";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		List<PlannedProd> plannedProdList = null;
		try {
			plannedProdList = plannedProdDAO.selectListOfCategory(scDto.getSelectCategory());
		} catch (DataNotFoundException e) {
			final String errMsg = "品目取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

	    boolean sheet1Flg = true;
	    boolean sheet2Flg = true;
		boolean sheet3Flg = true;
		boolean sheet4Flg = true;

		//仕入の場合、1、2シート目を出力しない
		if(scDto.getSelectCategory().contentEquals(shiireCategory)) {
			sheet1Flg = false;
			sheet2Flg = false;
		}

		//エリア別計画、担当者別計画の両方が「1」
		//if(plannedCategory.get(0).getPlanLevelOffice().equals("1")
		//		&& plannedCategory.get(0).getPlanLevelMr().equals("1")) {
		//	sheet1Flg = true;
		//}
		//担当者別計画、施設特約店別計画の両方が「1」
		//if(plannedCategory.get(0).getPlanLevelMr().equals("1")
		//		&& plannedCategory.get(0).getPlanLevelInsWs().equals("1")) {
		//	sheet2Flg = true;
		//}
		//エリア別計画、施設特約店別計画の両方が「1」
		//if(plannedCategory.get(0).getPlanLevelOffice().equals("1")
		//		&& plannedCategory.get(0).getPlanLevelInsWs().equals("1")) {
		//	sheet3Flg = true;
		//}
		//施設特約店別計画が「1」
		//if(plannedCategory.get(0).getPlanLevelInsWs().equals("1")) {
		//	sheet4Flg = true;
		//}

		//if(sheet1Flg == false && sheet2Flg == false && sheet3Flg == false && sheet4Flg == false) {
		//	throw new ValidateException(new Conveyance(new MessageKey("DPC1023E")));
		//}


		List<DelFacilitiesAndAdjustments> delFacilitiesAndAdjustmentsList = new ArrayList<DelFacilitiesAndAdjustments>();
		List<PersonInChargeFacilityAdjustmentList> personInChargeFacilityAdjustmentList = new ArrayList<PersonInChargeFacilityAdjustmentList>();
		List<AreaFacilityPersonInChargeAdjustment> areaFacilityPersonInChargeAdjustmentList = new ArrayList<AreaFacilityPersonInChargeAdjustment>();
		List<DeletedFacilityPersonInChargeplan> deletedFacilityPersonInChargeplanList = new ArrayList<DeletedFacilityPersonInChargeplan>();

		for (PlannedProd plannedProd : plannedProdList) {
			List<AreaPersonInChargeAdjustment> areaPersonInChargeAdjustmentList = null;

			if(sheet1Flg == true) {
				try {
					//1シート目sql
					areaPersonInChargeAdjustmentList = dpsSupportFollowDao.searchAreaPersonInChargeAdjustmentList(scDto.getSelectCategory(),plannedProd.getStatProdCode());
				} catch (DataNotFoundException e) {

				}
			}

			if(sheet2Flg == true) {
				try {
					//2シート目sql
					personInChargeFacilityAdjustmentList = dpsSupportFollowDao.searchPersonInChargeFacilityAdjustmentList(scDto.getSelectCategory(),plannedProd.getStatProdCode(),plannedProd.getProdCode());
				} catch (DataNotFoundException e) {

				}
			}

			if(!scDto.getSelectCategory().contentEquals(shiireCategory)) {
				if(sheet3Flg == true) {
					try {
						//3シート目sql
						areaFacilityPersonInChargeAdjustmentList = dpsSupportFollowDao.searchAreaFacilityPersonInChargeAdjustmentList(scDto.getSelectCategory(),plannedProd.getProdCode());
					} catch (DataNotFoundException e) {

					}
				}

				if(sheet4Flg == true) {
					try {
						//4シート目sql
						deletedFacilityPersonInChargeplanList = dpsSupportFollowDao.searchDeletedFacilityPersonInChargeplanList(scDto.getSelectCategory(),plannedProd.getStatProdCode());
					} catch (DataNotFoundException e) {

					}
				}
		    }else {
		    	if(sheet3Flg == true) {
			    	try {
						//3シート目sql（仕入）
						areaFacilityPersonInChargeAdjustmentList = dpsSupportFollowDao.searchAreaFacilityPersonInChargeAdjustmentShiireList(scDto.getSelectCategory(),plannedProd.getStatProdCode());
					} catch (DataNotFoundException e) {

					}
		    	}

		    	if(sheet4Flg == true) {
					try {
						//4シート目sql（仕入）
						deletedFacilityPersonInChargeplanList = dpsSupportFollowDao.searchDeletedFacilityPersonInChargeplanShiireList(scDto.getSelectCategory(),plannedProd.getStatProdCode());
					} catch (DataNotFoundException e) {

					}
		    	}
		    }

			DelFacilitiesAndAdjustments delFacilitiesAndAdjustments = new DelFacilitiesAndAdjustments();

			//製品名
			delFacilitiesAndAdjustments.setProdName(plannedProd.getProdName());

			//1シート目
			delFacilitiesAndAdjustments.setAreaPersonInChargeAdjustmentList(areaPersonInChargeAdjustmentList);
			//2シート目
			delFacilitiesAndAdjustments.setPersonInChargeFacilityAdjustmentList(personInChargeFacilityAdjustmentList);
			//3シート目
			delFacilitiesAndAdjustments.setAreaFacilityPersonInChargeAdjustmentList(areaFacilityPersonInChargeAdjustmentList);
			//4シート目
			delFacilitiesAndAdjustments.setDeletedFacilityPersonInChargeplanList(deletedFacilityPersonInChargeplanList);

			delFacilitiesAndAdjustmentsList.add(delFacilitiesAndAdjustments);

		}

		// -------------------------
		// エクセルファイル生成
		// -------------------------
		String templatePath = "";
		if(!scDto.getSelectCategory().contentEquals(shiireCategory)) {
			templatePath = TemplateInfo.EXCEL_TEMPLETE_DPS920P0002.getTemplatePath(templateRootPath);
		}else {
			templatePath = TemplateInfo.EXCEL_TEMPLETE_DPS920P0002SHIIRE.getTemplatePath(templateRootPath);
		}
		DelFacilitiesAndAdjustmentsListExportLogic logic = new DelFacilitiesAndAdjustmentsListExportLogic(templatePath, commonDao.getSystemTime(),
				 delFacilitiesAndAdjustmentsList,searchList.get(0).getDataName(),scDto.getSelectCategory(),shiireCategory,plannedCategory
				 ,sheet1Flg,sheet2Flg,sheet3Flg,sheet4Flg);
        return logic.export();
	}
// add End 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
// add Start 2024/2/26 K.Suzuki 2024年2月　チェックツール追加
	public ExportResult outputCheckTool(String templateRootPath,String fileName) throws LogicalException {
		// -------------------------
		// 引数チェック
		// -------------------------
		if (templateRootPath == null) {
			final String errMsg = "テンプレート配置パスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -------------------------
		// 営業所計画アップロード用のフォーマットファイルサマリー取得
		// -------------------------
		List<CheckTool> CheckTool = null;
		try {
			CheckTool = checkToolDao.searchList(DistPlanningListDLSummaryDao.SORT_STRING_EXCEL);

		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")), e);
		}

		// -------------------------
		// エクセルファイル生成
		// -------------------------
		final String templatePath = TemplateInfo.EXCEL_TEMPLETE_DPS920P0003.getTemplatePath(templateRootPath);
		CheckToolExportLogic logic = new CheckToolExportLogic(templatePath, commonDao.getSystemTime(),
				 CheckTool,fileName);
        return logic.export();
	}
// add End 2024/2/26 K.Suzuki 2024年2月　チェックツール追加
}
