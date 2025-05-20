package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.POIBean;
import jp.co.takeda.dao.TmsTytenMstUnDAO;
import jp.co.takeda.dto.WsPlanReferenceScDto;
import jp.co.takeda.model.WsPlanSummary2;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.TytenKisLevel;
import jp.co.takeda.model.view.MonNnuSummary;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.cmn.bean.ExportResultExcelImpl;

/**
 * 特約店別計画一覧のExcelファイルを生成するロジッククラス
 *
 * @author stakeuchi
 */
public class WsPlanListExportLogic {

	/**
	 * カテゴリ 検索サービス
	 */
    @Autowired(required = true)
    @Qualifier("dpsCodeMasterSearchService")
    protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	/**
	 * テンプレート絶対パス名
	 */
	private final String templatePath;

	/**
	 * システム日付
	 */
	private final Date systemDate;

	/**
	 * 特約店別計画参照画面の検索条件DTO
	 */
	private final WsPlanReferenceScDto refScDto;

	/**
	 * 特約店別計画のサマリーリスト
	 */
	private final List<WsPlanSummary2> wsPlanSummaryList;

	/**
	 * 特約店基本統合DAO
	 */
	private final TmsTytenMstUnDAO tmsTytenMstUnDAO;

	/**
	 * 出力ファイル名
	 */
	private final String outputFileName;

	// -------------------------------------
	// 文字列定数
	// -------------------------------------
	/** 出力ファイル名ヘッダ */
	private static final String OUTPUT_FILE_NAME_HEADER = "wsPlan";

	/** シート名 */
	private static final String SHEET_NAME = "(医)特約店別計画";

	/** 文字列「実績」 */
	private static final String TEXT_FACT = "実績";

	/** 文字列「計画」 */
	private static final String TEXT_PLAN = "計画";

	/** 文字列「S」 */
	private static final String TEXT_S = "（S）";

	/** 文字列「Y」 */
	private static final String TEXT_Y = "（Y）";

	/** 文字列「表示条件」 */
	private static final String TEXT_CONDITION = "【表示条件】";

	/** 文字列「特約店コード」 */
	private static final String TEXT_TYTEN_CODE = "特約店コード";

	/** 文字列「特約店」 */
	private static final String TEXT_TYTEN = "特約店";

	/** 文字列「集約方法」 */
	private static final String TEXT_AGGREGATION = "集約方法";

	/** 文字列「本店」 */
	private static final String TEXT_HONTEN = "本店(3桁)";

	/** 文字列「支社」 */
	private static final String TEXT_SHISHA = "支社(5桁)";

	/** 文字列「支店」 */
	private static final String TEXT_SHITEN = "支店(7桁)";

	/** 文字列「課」 */
	private static final String TEXT_KA = "課(9桁)";

	/** 文字列「ブロック１」 */
	private static final String TEXT_BLK1 = "ブロック１(11桁)";

	/** 文字列「ブロック２」 */
	private static final String TEXT_BLK2 = "ブロック２(13桁)";

	/** 文字列「カテゴリ」 */
	private static final String TEXT_CATEGORY = "カテゴリ";

// del start 2018/07/30 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
//	/** 文字列「MMP品」 */
//	private static final String TEXT_MMP = "JPBU品";
//
//	/** 文字列「仕入品」 */
//	private static final String TEXT_SHIIRE = "仕入品（一般・麻薬）";
//
//	/** 文字列「ONC品」 */
//	private static final String TEXT_ONC = "ONC品";
// del end   2018/07/30 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

	/** 文字列「全て」 */
	private static final String TEXT_ALL = "全て";

	// -------------------------------------
	// インデックス定数
	// -------------------------------------
	/** [行番号] 表示条件 */
	private static final int ROW_IDX_DISP_CONDITION = 1;

	/** [列番号] 表示条件 */
	private static final int COL_IDX_DISP_CONDITION = 0;

	/** [行番号] 現在日付 */
	private static final int ROW_IDX_DATE = 0;

	/** [列番号] 現在日付 */
	private static final int COL_IDX_DATE = 26;

	/** [行番号] 明細ヘッダ2行目 */
	private static final int ROW_IDX_LIST_HEAD2 = 3;

	/** [列番号] 明細ヘッダ2行目 実績 UH */
	private static final int COL_IDX_LIST_HEAD2_FACT_UH = 4;

	/** [列番号] 明細ヘッダ2行目 計画 UH */
	private static final int COL_IDX_LIST_HEAD2_PLAN_UH = 9;

	/** [列番号] 明細ヘッダ2行目 実績 P */
	private static final int COL_IDX_LIST_HEAD2_FACT_P = 10;

	/** [列番号] 明細ヘッダ2行目 計画 P */
	private static final int COL_IDX_LIST_HEAD2_PLAN_P = 15;

	/** [列番号] 明細ヘッダ2行目 実績 雑 */
	private static final int COL_IDX_LIST_HEAD2_FACT_ZATU = 16;

	/** [列番号] 明細ヘッダ2行目 計画 雑 */
	private static final int COL_IDX_LIST_HEAD2_PLAN_ZATU = 21;

	/** [列番号] 明細ヘッダ2行目 実績 合計 */
	private static final int COL_IDX_LIST_HEAD2_FACT_SUM = 22;

	/** [列番号] 明細ヘッダ2行目 計画 合計 */
	private static final int COL_IDX_LIST_HEAD2_PLAN_SUM = 27;

	/** [行番号] 明細開始行 */
	private static final int ROW_IDX_START_LIST = 5;

	/** [列番号] 明細開始列 */
	private static final int COL_IDX_START_LIST = 0;

	/** [行番号] 印刷開始行 */
	private static final int ROW_IDX_PRINT_START = 0;

	/** [列番号] 印刷開始列 */
	private static final int COL_IDX_PRINT_START = 0;

	/** [列番号] 印刷終了列 */
	private static final int COL_IDX_PRINT_END = 27;

	/**
	 * コンストラクタ
	 *
	 * @param templatePath テンプレート絶対パス名
	 * @param systemDate システム日付
	 * @param scDto 特約店別計画参照画面の検索条件DTO
	 * @param wsPlanSummaryList 特約店別計画のサマリーリスト
	 * @param tmsTytenMstUnDAO 特約店基本統合DAO
	 */
	public WsPlanListExportLogic(String templatePath, Date systemDate, WsPlanReferenceScDto refScDto, List<WsPlanSummary2> wsPlanSummaryList, TmsTytenMstUnDAO tmsTytenMstUnDAO) {
		this.templatePath = templatePath;
		this.systemDate = (Date) systemDate.clone();
		this.refScDto = refScDto;
		this.wsPlanSummaryList = wsPlanSummaryList;
		this.tmsTytenMstUnDAO = tmsTytenMstUnDAO;
		this.outputFileName = createOutputFileName(systemDate);
	}

	/**
	 * 出力ファイル名の生成
	 *
	 * @param systemDate システム日付
	 * @return 出力ファイル名
	 */
	private String createOutputFileName(Date systemDate) {
		// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//		return OUTPUT_FILE_NAME_HEADER + "_" + DateUtil.toString(systemDate, "yyyyMMddHHmmss") + ".xls";
		return OUTPUT_FILE_NAME_HEADER + "_" + DateUtil.toString(systemDate, "yyyyMMddHHmmss") + ".xlsx";
		// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
	}

	/**
	 * 検索結果データのExcelファイルへの出力を実行する。
	 *
	 * @return 出力結果
	 */
	public ExportResult export() {
		FileInputStream fileIn = null;
		try {
			// テンプレートファイルの読込
			fileIn = new FileInputStream(templatePath);

			// テンプレートファイルへ書込
			// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//			POIFSFileSystem poiFS = new POIFSFileSystem(fileIn);
//			HSSFWorkbook workBook = new HSSFWorkbook(poiFS);
			XSSFWorkbook workBook = new XSSFWorkbook(fileIn);
			// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
			write(workBook);

			// ファイル出力実装クラスの生成
			return new ExportResultExcelImpl(outputFileName, workBook);

		} catch (IOException e) {
			IOUtils.closeQuietly(fileIn);
			throw new SystemException(new Conveyance(PARAMETER_ERROR, "テンプレートパスが存在しない"));
		} finally {
			IOUtils.closeQuietly(fileIn);
		}
	}

	/**
	 * ワークブックへ検索結果の書き込みを行う。
	 *
	 * @param workBook 書き込み対象のワークブック
	 */
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//	private void write(HSSFWorkbook workBook) {
	private void write(XSSFWorkbook workBook) {
	// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		POIBean poiBean = new POIBean(workBook);

		// ヘッダ情報のセット
		writeHeadInfo(poiBean);

		// リスト情報のセット
		writeListInfo(poiBean);
	}

	/**
	 * ヘッダ情報の値をセルに書き込む
	 *
	 * @param poiBean POI設定BEAN
	 */
	private void writeHeadInfo(POIBean poiBean) {
		// シート名
		poiBean.setSheetName(SHEET_NAME);
		// 表示条件
		poiBean.setCellData(createDisplayConditionTxt(), ROW_IDX_DISP_CONDITION, COL_IDX_DISP_CONDITION);
		// 現在日付
		poiBean.setCellData(systemDate, ROW_IDX_DATE, COL_IDX_DATE);
		// 明細ヘッダ
		String txtFact = TEXT_FACT;
		String txtPlan = TEXT_PLAN;
		switch (refScDto.getKaBaseKb()) {
			case S_KA_BASE:
				txtFact += TEXT_S;
				txtPlan += TEXT_S;
				break;
			case Y_KA_BASE:
				txtFact += TEXT_Y;
				txtPlan += TEXT_Y;
				break;
		}
		poiBean.setCellData(txtFact, ROW_IDX_LIST_HEAD2, COL_IDX_LIST_HEAD2_FACT_UH);
		poiBean.setCellData(txtPlan, ROW_IDX_LIST_HEAD2, COL_IDX_LIST_HEAD2_PLAN_UH);
		poiBean.setCellData(txtFact, ROW_IDX_LIST_HEAD2, COL_IDX_LIST_HEAD2_FACT_P);
		poiBean.setCellData(txtPlan, ROW_IDX_LIST_HEAD2, COL_IDX_LIST_HEAD2_PLAN_P);
		poiBean.setCellData(txtFact, ROW_IDX_LIST_HEAD2, COL_IDX_LIST_HEAD2_FACT_ZATU);
		poiBean.setCellData(txtPlan, ROW_IDX_LIST_HEAD2, COL_IDX_LIST_HEAD2_PLAN_ZATU);
		poiBean.setCellData(txtFact, ROW_IDX_LIST_HEAD2, COL_IDX_LIST_HEAD2_FACT_SUM);
		poiBean.setCellData(txtPlan, ROW_IDX_LIST_HEAD2, COL_IDX_LIST_HEAD2_PLAN_SUM);
	}

	/**
	 * リスト情報の値をセルに書き込む
	 *
	 * @param poiBean POI設定BEAN
	 */
	private void writeListInfo(POIBean poiBean) {
		// 集計リストがNull,または空の場合はReturn
		if (wsPlanSummaryList == null || wsPlanSummaryList.isEmpty()) {
			return;
		}

		// 集計リストのサイズ分行を追加
		poiBean.addRows(ROW_IDX_START_LIST, wsPlanSummaryList.size());

		// 行インデックス
		int rowIdx = ROW_IDX_START_LIST;

		for (WsPlanSummary2 wsPlanSummary : wsPlanSummaryList) {

			// 列インデックス
			int colIdx = COL_IDX_START_LIST;

			// 特約店名
			final String tytenName = wsPlanSummary.getTmsTytenMeiKj();

			// 特約店コード
			final String tytenCode = wsPlanSummary.getTmsTytenCd();

			poiBean.setCellData(tytenName, rowIdx, colIdx++);
			poiBean.setCellData(tytenCode, rowIdx, colIdx++);

			// 品目名
			final String prodName = wsPlanSummary.getProdName();
			poiBean.setCellData(prodName, rowIdx, colIdx++);

			// 統計品目コード
			final String prodCode = wsPlanSummary.getStatProdCode();
			poiBean.setCellData(prodCode, rowIdx, colIdx++);

			MonNnuSummary monNnuSummaryUH = wsPlanSummary.getMonNnuSummaryUh(); // 納入実績 UH
			// UH 前々々期
			final int preFarAdvancePeriodColIdxUH = colIdx;
			final Long preFarAdvancePeriodUH = ConvertUtil.parseMoneyToThousandUnit(monNnuSummaryUH.getPreFarAdvancePeriod());
			poiBean.setCellData(preFarAdvancePeriodUH, rowIdx, colIdx++);

			// UH 前々期
			final int farAdvancePeriodColIdxUH = colIdx;
			final Long farAdvancePeriodUH = ConvertUtil.parseMoneyToThousandUnit(monNnuSummaryUH.getFarAdvancePeriod());
			poiBean.setCellData(farAdvancePeriodUH, rowIdx, colIdx++);

			// UH 前期
			final int advancePeriodColIdxUH = colIdx;
			final Long advancePeriodUH = ConvertUtil.parseMoneyToThousandUnit(monNnuSummaryUH.getAdvancePeriod());
			poiBean.setCellData(advancePeriodUH, rowIdx, colIdx++);

			// UH 当期
			final int currentPeriodColIdxUH = colIdx;
			final Long currentPeriodUH = ConvertUtil.parseMoneyToThousandUnit(monNnuSummaryUH.getCurrentPeriod());
			poiBean.setCellData(currentPeriodUH, rowIdx, colIdx++);

			// UH 前同比
			final String ratioFormulaUH = createRatioFormula(poiBean, rowIdx, colIdx);
			poiBean.setCellFormula(ratioFormulaUH, rowIdx, colIdx++);

			// UH 翌期
			final int subsequentPeriodColIdxUH = colIdx;
			final Long subsequentPeriodUH = ConvertUtil.parseMoneyToThousandUnit(wsPlanSummary.getPlannedValueUh());
			poiBean.setCellData(subsequentPeriodUH, rowIdx, colIdx++);

			MonNnuSummary monNnuSummaryP = wsPlanSummary.getMonNnuSummaryP(); // 納入実績 P
			// P 前々々期
			final int preFarAdvancePeriodColIdxP = colIdx;
			final Long preFarAdvancePeriodP = ConvertUtil.parseMoneyToThousandUnit(monNnuSummaryP.getPreFarAdvancePeriod());
			poiBean.setCellData(preFarAdvancePeriodP, rowIdx, colIdx++);

			// P 前々期
			final int farAdvancePeriodColIdxP = colIdx;
			final Long farAdvancePeriodP = ConvertUtil.parseMoneyToThousandUnit(monNnuSummaryP.getFarAdvancePeriod());
			poiBean.setCellData(farAdvancePeriodP, rowIdx, colIdx++);

			// P 前期
			final int advancePeriodColIdxP = colIdx;
			final Long advancePeriodP = ConvertUtil.parseMoneyToThousandUnit(monNnuSummaryP.getAdvancePeriod());
			poiBean.setCellData(advancePeriodP, rowIdx, colIdx++);

			// P 当期
			final int currentPeriodColIdxP = colIdx;
			final Long currentPeriodP = ConvertUtil.parseMoneyToThousandUnit(monNnuSummaryP.getCurrentPeriod());
			poiBean.setCellData(currentPeriodP, rowIdx, colIdx++);

			// P 前同比
			final String ratioFormulaP = createRatioFormula(poiBean, rowIdx, colIdx);
			poiBean.setCellFormula(ratioFormulaP, rowIdx, colIdx++);

			// P 翌期
			final int subsequentPeriodColIdxP = colIdx;
			final Long subsequentPeriodP = ConvertUtil.parseMoneyToThousandUnit(wsPlanSummary.getPlannedValueP());
			poiBean.setCellData(subsequentPeriodP, rowIdx, colIdx++);

			MonNnuSummary monNnuSummaryZATU = wsPlanSummary.getMonNnuSummaryZatu(); // 納入実績 雑
			// 雑 前々々期
			final int preFarAdvancePeriodColIdxZATU = colIdx;
			final Long preFarAdvancePeriodZATU = ConvertUtil.parseMoneyToThousandUnit(monNnuSummaryZATU.getPreFarAdvancePeriod());
			poiBean.setCellData(preFarAdvancePeriodZATU, rowIdx, colIdx++);

			// 雑 前々期
			final int farAdvancePeriodColIdxZATU = colIdx;
			final Long farAdvancePeriodZATU = ConvertUtil.parseMoneyToThousandUnit(monNnuSummaryZATU.getFarAdvancePeriod());
			poiBean.setCellData(farAdvancePeriodZATU, rowIdx, colIdx++);

			// 雑 前期
			final int advancePeriodColIdxZATU = colIdx;
			final Long advancePeriodZATU = ConvertUtil.parseMoneyToThousandUnit(monNnuSummaryZATU.getAdvancePeriod());
			poiBean.setCellData(advancePeriodZATU, rowIdx, colIdx++);

			// 雑 当期
			final int currentPeriodColIdxZATU = colIdx;
			final Long currentPeriodZATU = ConvertUtil.parseMoneyToThousandUnit(monNnuSummaryZATU.getCurrentPeriod());
			poiBean.setCellData(currentPeriodZATU, rowIdx, colIdx++);

			// 雑 前同比
			final String ratioFormulaZATU = createRatioFormula(poiBean, rowIdx, colIdx);
			poiBean.setCellFormula(ratioFormulaZATU, rowIdx, colIdx++);

			// 雑 翌期
			final int subsequentPeriodColIdxZATU = colIdx;
			poiBean.setCellData("", rowIdx, colIdx++);

			// 合計 前々々期
			final String preFarAdvancePeriodFormulaSUM = createPeriodFormula(poiBean, rowIdx, preFarAdvancePeriodColIdxUH, preFarAdvancePeriodColIdxP,
				preFarAdvancePeriodColIdxZATU);
			poiBean.setCellFormula(preFarAdvancePeriodFormulaSUM, rowIdx, colIdx++);

			// 合計 前々期
			final String farAdvancePeriodFormulaSUM = createPeriodFormula(poiBean, rowIdx, farAdvancePeriodColIdxUH, farAdvancePeriodColIdxP, farAdvancePeriodColIdxZATU);
			poiBean.setCellFormula(farAdvancePeriodFormulaSUM, rowIdx, colIdx++);

			// 合計 前期
			final String advancePeriodFormulaSUM = createPeriodFormula(poiBean, rowIdx, advancePeriodColIdxUH, advancePeriodColIdxP, advancePeriodColIdxZATU);
			poiBean.setCellFormula(advancePeriodFormulaSUM, rowIdx, colIdx++);

			// 合計 当期
			final String currentPeriodFormulaSUM = createPeriodFormula(poiBean, rowIdx, currentPeriodColIdxUH, currentPeriodColIdxP, currentPeriodColIdxZATU);
			poiBean.setCellFormula(currentPeriodFormulaSUM, rowIdx, colIdx++);

			// 合計 前同比
			final String ratioFormulaSUM = createRatioFormula(poiBean, rowIdx, colIdx);
			poiBean.setCellFormula(ratioFormulaSUM, rowIdx, colIdx++);

			// 合計 翌期
			final String subsequentPeriodFormulaSUM = createPeriodFormula(poiBean, rowIdx, subsequentPeriodColIdxUH, subsequentPeriodColIdxP, subsequentPeriodColIdxZATU);
			poiBean.setCellFormula(subsequentPeriodFormulaSUM, rowIdx, colIdx++);

			rowIdx++;

		}

		// 印刷範囲を設定
		final int rowIdxPrintEnd = ROW_IDX_START_LIST + wsPlanSummaryList.size();
		final short fitWidth = 1; // 横は1固定
		final short fitHeigth = 0; // 縦は0固定
		poiBean.setPringArea(ROW_IDX_PRINT_START, rowIdxPrintEnd, COL_IDX_PRINT_START, COL_IDX_PRINT_END, fitWidth, fitHeigth);
	}

	/**
	 * 前同比の計算式を作成する。
	 *
	 * @param poiBean POI設定BEAN
	 * @param rowIdx 対象行インデックス
	 * @param colIdx 前同比列インデックス
	 * @return 前同比の計算式
	 */
	private String createRatioFormula(POIBean poiBean, int rowIdx, int colIdx) {
		final String cellAdvancePeriod = poiBean.getColumnNameString(colIdx - 2) + (rowIdx + 1);
		final String cellSubsequentPeriod = poiBean.getColumnNameString(colIdx + 1) + (rowIdx + 1);
		return "IF(ISERROR(" + cellSubsequentPeriod + "/" + cellAdvancePeriod + "),\"\"," + cellSubsequentPeriod + "/" + cellAdvancePeriod + "*100)";
	}

	/**
	 * 期の合計列の計算式を作成する。
	 *
	 * @param poiBean POI設定BEAN
	 * @param rowIdx 対象行インデックス
	 * @param colIdxUH UH列インデックス
	 * @param colIdxP P列インデックス
	 * @param colIdxZATU 雑列インデックス
	 * @return 期の合計列の計算式
	 */
	private String createPeriodFormula(POIBean poiBean, int rowIdx, int colIdxUH, int colIdxP, int colIdxZATU) {
		final String cellUH = poiBean.getColumnNameString(colIdxUH) + (rowIdx + 1);
		final String cellP = poiBean.getColumnNameString(colIdxP) + (rowIdx + 1);
		final String cellZATU = poiBean.getColumnNameString(colIdxZATU) + (rowIdx + 1);
		return "IF(COUNT(" + cellUH + "," + cellP + "," + cellZATU + ")>0,SUM(" + cellUH + "," + cellP + "," + cellZATU + "),\"\")";
	}

	/**
	 * 表示条件の文字列を作成する。
	 *
	 * @return 表示条件の文字列
	 */
	private String createDisplayConditionTxt() {
		String txt = TEXT_CONDITION;
		// 特約店コード
		final String tytenCodePart = refScDto.getTmsTytenCdPart();
		if (StringUtils.isNotBlank(tytenCodePart)) {
			txt += "　" + TEXT_TYTEN_CODE + "：" + tytenCodePart;
		}
		// 特約店名
		CreateTmsTytenCdLogic logic = new CreateTmsTytenCdLogic(refScDto.getTmsTytenCdPart());
		final String tytenCode = logic.execute();
		if (StringUtils.isNotBlank(tytenCode)) {
			final String name = searchTytenMst(tytenCode);
			txt += "　" + TEXT_TYTEN + "：" + name;
		}
		// 集約方法
		final TytenKisLevel tytenKisLevel = refScDto.getTytenKisLevel();
		if (tytenKisLevel != null) {
			txt += "　" + TEXT_AGGREGATION + "：";
			switch (tytenKisLevel) {
				case HONTEN:
					txt += TEXT_HONTEN;
					break;
				case SHISHA:
					txt += TEXT_SHISHA;
					break;
				case SHITEN:
					txt += TEXT_SHITEN;
					break;
				case KA:
					txt += TEXT_KA;
					break;
				case BLK1:
					txt += TEXT_BLK1;
					break;
				case BLK2:
					txt += TEXT_BLK2;
					break;
			}
		}
		// カテゴリ
//		final ProdCategory prodCategory = refScDto.getCategory();
		txt += "　" + TEXT_CATEGORY + "：" + dpsCodeMasterSearchService.searchDataName(DpsCodeMaster.CAT.getDbValue(),refScDto.getCategory());
//		if (prodCategory != null) {
//// mod start 2018/07/30 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
////			switch (prodCategory) {
////				case MMP:
////					txt += TEXT_MMP;
////					break;
////				case SHIIRE:
////					txt += TEXT_SHIIRE;
////					break;
////				case ONC:
////					txt += TEXT_ONC;
////					break;
////			}
//			txt += prodCategory.getProdCategoryNoStarsName();
//// mod end   2018/07/30 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
//		} else {
//			txt += TEXT_ALL;
//		}
		return txt;
	}

	/**
	 * 特約店情報を検索する。
	 *
	 * @param tytenCode 特約店コード
	 * @return 特約店名称
	 */
	private String searchTytenMst(String tytenCode) {
		try {
			return tmsTytenMstUnDAO.search(tytenCode).getTmsTytenMeiKj();
		} catch (DataNotFoundException e) {
			// エラーとしない。
			return "";
		}
	}
}
