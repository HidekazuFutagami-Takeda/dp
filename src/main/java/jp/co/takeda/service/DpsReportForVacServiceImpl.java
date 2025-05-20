package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.dao.InsWsPlanForVacSummaryByInsDao;
import jp.co.takeda.dao.InsWsPlanForVacSummaryByInsTytenDao;
import jp.co.takeda.dao.InsWsPlanForVacSummaryDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.MrDistMissDao;
import jp.co.takeda.dao.OutputFileDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.TmsTytenMstUnDAO;
import jp.co.takeda.dao.WsPlanForVacSummaryDao;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.WsPlanForVacSummaryScDto;
import jp.co.takeda.dto.WsPlanReferenceForVacScDto;
import jp.co.takeda.logic.DistMissListMrForVacExportLogic;
import jp.co.takeda.logic.InsWsCityListForVacExportLogic;
import jp.co.takeda.logic.WsPlanListForVacExportLogic;
import jp.co.takeda.logic.WsPlanListNonJissekiForVacExportLogic;
import jp.co.takeda.model.MrDistMiss;
import jp.co.takeda.model.OutputFile;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.WsPlanForVacSummary2;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.KaBaseKb;
import jp.co.takeda.model.div.OutputType;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.div.TytenKisLevel;
import jp.co.takeda.model.view.InsWsPlanForVacSummary;
import jp.co.takeda.model.view.InsWsPlanForVacSummaryByIns;
import jp.co.takeda.model.view.InsWsPlanForVacSummaryByInsTyten;
import jp.co.takeda.web.cmn.bean.ExportResult;

/**
 * (ワ)帳票サービス実装クラス
 *
 * @author stakeuchi
 */
@Transactional
@Service("dpsReportForVacService")
public class DpsReportForVacServiceImpl implements DpsReportForVacService {

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
	 * 出力ファイル情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("outputFileDao")
	protected OutputFileDao outputFileDao;

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
	 * 支援の計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * 担当者別配分ミス情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("mrDistMissDao")
	protected MrDistMissDao mrDistMissDao;

	/**
	 * 特約店基本統合DAO
	 */
	@Autowired(required = true)
	@Qualifier("tmsTytenMstUnDAO")
	protected TmsTytenMstUnDAO tmsTytenMstUnDAO;

	/**
	 * (ワ)特約店別計画サマリーDAO
	 */
	@Autowired(required = true)
	@Qualifier("wsPlanForVacSummaryDao")
	protected WsPlanForVacSummaryDao wsPlanForVacSummaryDao;

	/**
	 * (ワ)施設特約店別計画の市区町村別サマリーDAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanForVacSummaryDao")
	protected InsWsPlanForVacSummaryDao insWsPlanForVacSummaryDao;

	/**
	 * (ワ)施設特約店別計画の施設別サマリーDAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanForVacSummaryByInsDao")
	protected InsWsPlanForVacSummaryByInsDao insWsPlanForVacSummaryByInsDao;

	// add Start 2022/12/1  Y.Taniguchi バックログNo.31
	/**
	 * (ワ)施設特約店別計画の施設別サマリーDAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanForVacSummaryByInsTytenDao")
	protected InsWsPlanForVacSummaryByInsTytenDao insWsPlanForVacSummaryByInsTytenDao;
	// add End 2022/12/1  Y.Taniguchi バックログNo.31

	/**
	 * 汎用マスタ検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	// (ワ)配分ミスリスト
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
		// 担当者→施設特約店別計画配分
		// ----------------------------
		final boolean isMrInsWsPlanDist = outputFile.getOutputType().equals(OutputType.VAC_INS_WS_PLAN_DIST);
		if (isMrInsWsPlanDist) {
			// 担当者別配分ミス情報取得
			List<MrDistMiss> mrDistMissList = null;
			try {
				mrDistMissList = mrDistMissDao.searchList(MrDistMissDao.SORT_STRING, outputFileId);
			} catch (DataNotFoundException e) {
				// エラーにせずファイルには空欄で出力する。
			}
			// -------------------------
			// エクセルファイル生成
			// -------------------------
			final String templatePath = TemplateInfo.EXCEL_TEMPLATE_DPS400P0003.getTemplatePath(templateRootPath);
			DistMissListMrForVacExportLogic logic = new DistMissListMrForVacExportLogic(templatePath, commonDao.getSystemTime(), outputFile, mrDistMissList, messageSource);
			return logic.export();
		}
		final String errMsg = " 出力ファイル情報が不正";
		throw new SystemException(new Conveyance(FATAL_ERROR, errMsg));
	}

	// (ワ)特約店別計画
	public ExportResult outputWsPlanListNonJisseki(String templateRootPath, WsPlanReferenceForVacScDto refScDto) throws LogicalException {
		// -------------------------
		// 引数チェック
		// -------------------------
		if (templateRootPath == null) {
			final String errMsg = "テンプレート配置パスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (refScDto == null) {
			final String errMsg = "(ワ)特約店別計画参照画面の検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// -----------------------------
		// サマリー用の検索条件DTOの生成
		// -----------------------------
		final String tytenCdPart = refScDto.getTmsTytenCdPart();
		final TytenKisLevel lebel = refScDto.getTytenKisLevel();
// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		final KaBaseKb kaBase = refScDto.getKaBaseKb();
		WsPlanForVacSummaryScDto summaryScDto = new WsPlanForVacSummaryScDto(tytenCdPart, lebel, kaBase);
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		// -------------------------
		// 特約店別計画サマリー取得
		// -------------------------
		List<WsPlanForVacSummary2> summaryList = new ArrayList<WsPlanForVacSummary2>();
		try {
			summaryList = wsPlanForVacSummaryDao.searchList(WsPlanForVacSummaryDao.SORT_STRING_EXCEL, summaryScDto, false);
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")), e);
		}
		// -------------------------
		// エクセルファイル生成
		// -------------------------
		final String templatePath = TemplateInfo.EXCEL_TEMPLATE_DPS502P0004.getTemplatePath(templateRootPath);
		WsPlanListNonJissekiForVacExportLogic logic = new WsPlanListNonJissekiForVacExportLogic(templatePath, commonDao.getSystemTime(), refScDto, summaryList, tmsTytenMstUnDAO);
		return logic.export();
	}

	// (ワ)特約店別計画
	public ExportResult outputWsPlanList(String templateRootPath, WsPlanReferenceForVacScDto refScDto) throws LogicalException {
		// -------------------------
		// 引数チェック
		// -------------------------
		if (templateRootPath == null) {
			final String errMsg = "テンプレート配置パスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (refScDto == null) {
			final String errMsg = "(ワ)特約店別計画参照画面の検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// -----------------------------
		// サマリー用の検索条件DTOの生成
		// -----------------------------
		final String tytenCdPart = refScDto.getTmsTytenCdPart();
		final TytenKisLevel lebel = refScDto.getTytenKisLevel();
// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		final KaBaseKb kaBase = refScDto.getKaBaseKb();
		WsPlanForVacSummaryScDto summaryScDto = new WsPlanForVacSummaryScDto(tytenCdPart, lebel, kaBase);
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		// -------------------------
		// 特約店別計画サマリー取得
		// -------------------------
		List<WsPlanForVacSummary2> summaryList = new ArrayList<WsPlanForVacSummary2>();
		try {
			summaryList = wsPlanForVacSummaryDao.searchList(WsPlanForVacSummaryDao.SORT_STRING, summaryScDto, true);
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")), e);
		}
		// -------------------------
		// エクセルファイル生成
		// -------------------------
		final String templatePath = TemplateInfo.EXCEL_TEMPLATE_DPS502P0002.getTemplatePath(templateRootPath);
		WsPlanListForVacExportLogic logic = new WsPlanListForVacExportLogic(templatePath, commonDao.getSystemTime(), refScDto, summaryList, tmsTytenMstUnDAO);
		return logic.export();
	}

	// (ワ)施設計画市区郡町村別集計結果
	public ExportResult outputInsWsCityList(String templateRootPath, Integer jgiNo) throws LogicalException {
		// -------------------------
		// 引数チェック
		// -------------------------
		if (templateRootPath == null) {
			final String errMsg = "テンプレート配置パスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// ワクチンのカテゴリコード
		String vaccineCode = dpsCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.VAC.getDbValue()).get(0).getDataCd();
		// ------------------------------
		// 品目一覧取得
		// ------------------------------
		List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
		try {
			plannedProdList = plannedProdDAO.searchListByPlanLevel(PlannedProdDAO.SORT_STRING, Sales.VACCHIN, vaccineCode, ProdPlanLevel.INS_WS);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画品目情報が存在しない：";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}
		// -------------------------
		// サマリーデータ取得
		// -------------------------
		List<InsWsPlanForVacSummary> summaryList = new ArrayList<InsWsPlanForVacSummary>();
		try {
			summaryList = insWsPlanForVacSummaryDao.searchList(InsWsPlanForVacSummaryDao.SORT_STRING, jgiNo);
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")), e);
		}
		List<InsWsPlanForVacSummaryByIns> summaryByInsList = new ArrayList<InsWsPlanForVacSummaryByIns>();
		try {
			summaryByInsList = insWsPlanForVacSummaryByInsDao.searchList(InsWsPlanForVacSummaryByInsDao.SORT_STRING, jgiNo);
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")), e);
		}
		// add Start 2022/12/1  Y.Taniguchi バックログNo.31
		List<InsWsPlanForVacSummaryByInsTyten> summaryByInsTytenList = new ArrayList<InsWsPlanForVacSummaryByInsTyten>();
		try {
			summaryByInsTytenList = insWsPlanForVacSummaryByInsTytenDao.searchList(InsWsPlanForVacSummaryByInsTytenDao.SORT_STRING, jgiNo);
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")), e);
		}
		// add End 2022/12/1  Y.Taniguchi バックログNo.31

		// -------------------------
		// エクセルファイル生成
		// -------------------------
		final String templatePath = TemplateInfo.EXCEL_TEMPLATE_DPS401P0002.getTemplatePath(templateRootPath);
		// add Start 2022/12/1  Y.Taniguchi バックログNo.31
		//InsWsCityListForVacExportLogic logic = new InsWsCityListForVacExportLogic(templatePath, commonDao.getSystemTime(), plannedProdList, summaryList, summaryByInsList, jgiNo,
			//sosMstDAO, jgiMstDAO);
			InsWsCityListForVacExportLogic logic = new InsWsCityListForVacExportLogic(templatePath, commonDao.getSystemTime(), plannedProdList, summaryList, summaryByInsList
					, summaryByInsTytenList, jgiNo, sosMstDAO, jgiMstDAO);
		// add End 2022/12/1  Y.Taniguchi バックログNo.31
		return logic.export();
	}
}
