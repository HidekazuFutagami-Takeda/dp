package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.POIBean;
import jp.co.takeda.dao.DpsCodeMasterDao;
import jp.co.takeda.dao.TmsTytenMstUnDAO;
import jp.co.takeda.dto.WsPlanReferenceScDto;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.WsPlanSummary2;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.TytenKisLevel;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.cmn.bean.ExportResultExcelImpl;

/**
 * 特約店別計画一覧【実績無】のExcelファイルを生成するロジッククラス
 *
 * @author khashimoto
 */
public class WsPlanListNonJissekiExportLogic {

	/**
	 * 計画支援汎用マスタDAO
	 */
	private final DpsCodeMasterDao dpsCodeMasterDao;

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
	private static final int COL_IDX_DATE = 5;

	/** [行番号] 明細ヘッダ2行目 */
	private static final int ROW_IDX_LIST_HEAD2 = 3;

	/** [列番号] 明細ヘッダ2行目 計画 UH */
	private static final int COL_IDX_LIST_HEAD2_PLAN_UH = 4;

	/** [列番号] 明細ヘッダ2行目 計画 P */
	private static final int COL_IDX_LIST_HEAD2_PLAN_P = 5;

	/** [列番号] 明細ヘッダ2行目 計画 合計 */
	private static final int COL_IDX_LIST_HEAD2_PLAN_SUM = 6;

	/** [行番号] 明細開始行 */
	private static final int ROW_IDX_START_LIST = 5;

	/** [列番号] 明細開始列 */
	private static final int COL_IDX_START_LIST = 0;

	/** [行番号] 印刷開始行 */
	private static final int ROW_IDX_PRINT_START = 0;

	/** [列番号] 印刷開始列 */
	private static final int COL_IDX_PRINT_START = 0;

	/** [列番号] 印刷終了列 */
	private static final int COL_IDX_PRINT_END = 6;

	/**
	 * コンストラクタ
	 *
	 * @param templatePath テンプレート絶対パス名
	 * @param systemDate システム日付
	 * @param scDto 特約店別計画参照画面の検索条件DTO
	 * @param wsPlanSummaryList2 特約店別計画のサマリーリスト
	 * @param tmsTytenMstUnDAO 特約店基本統合DAO
	 *
	 */
	public WsPlanListNonJissekiExportLogic(String templatePath, Date systemDate, WsPlanReferenceScDto refScDto, List<WsPlanSummary2> wsPlanSummaryList2,
		TmsTytenMstUnDAO tmsTytenMstUnDAO,DpsCodeMasterDao dpsCodeMasterDao ) {
		this.templatePath = templatePath;
		this.systemDate = (Date) systemDate.clone();
		this.refScDto = refScDto;
		this.wsPlanSummaryList = wsPlanSummaryList2;
		this.tmsTytenMstUnDAO = tmsTytenMstUnDAO;
		this.outputFileName = createOutputFileName(systemDate);
		this.dpsCodeMasterDao = dpsCodeMasterDao;
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
//			IOUtils.closeQuietly(fileIn);
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
		String txtPlan = TEXT_PLAN;
		switch (refScDto.getKaBaseKb()) {
			case S_KA_BASE:
				txtPlan += TEXT_S;
				break;
			case Y_KA_BASE:
				txtPlan += TEXT_Y;
				break;
		}
		poiBean.setCellData(txtPlan, ROW_IDX_LIST_HEAD2, COL_IDX_LIST_HEAD2_PLAN_UH);
		poiBean.setCellData(txtPlan, ROW_IDX_LIST_HEAD2, COL_IDX_LIST_HEAD2_PLAN_P);
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

			// UH 翌期
			final int subsequentPeriodColIdxUH = colIdx;
			final Long subsequentPeriodUH = ConvertUtil.parseMoneyToThousandUnit(wsPlanSummary.getPlannedValueUh());
			poiBean.setCellData(subsequentPeriodUH, rowIdx, colIdx++);

			// P 翌期
			final int subsequentPeriodColIdxP = colIdx;
			final Long subsequentPeriodP = ConvertUtil.parseMoneyToThousandUnit(wsPlanSummary.getPlannedValueP());
			poiBean.setCellData(subsequentPeriodP, rowIdx, colIdx++);

			// 合計 翌期
			final String subsequentPeriodFormulaSUM = createPeriodFormula(poiBean, rowIdx, subsequentPeriodColIdxUH, subsequentPeriodColIdxP);
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
	 * 期の合計列の計算式を作成する。
	 *
	 * @param poiBean POI設定BEAN
	 * @param rowIdx 対象行インデックス
	 * @param colIdxUH UH列インデックス
	 * @param colIdxP P列インデックス
	 * @return 期の合計列の計算式
	 */
	private String createPeriodFormula(POIBean poiBean, int rowIdx, int colIdxUH, int colIdxP) {
		final String cellUH = poiBean.getColumnNameString(colIdxUH) + (rowIdx + 1);
		final String cellP = poiBean.getColumnNameString(colIdxP) + (rowIdx + 1);
		return "IF(COUNT(" + cellUH + "," + cellP + ")>0,SUM(" + cellUH + "," + cellP + "),\"\")";
	}


	private String searchDataName(String dataKbn, String dataCd ) {

		if(dataCd == null) {
			return "全て（医薬）";
		}

		// 検索結果一覧
		DpsCCdMst dpsCCdMst = new DpsCCdMst();
		try {
			// データ区分の検索
			dpsCCdMst = dpsCodeMasterDao.searchDataKbnAndCd(dataKbn, dataCd);
		} catch (DataNotFoundException e) {
			final String errMsg = "汎用マスタに、「DATA_KBN,DATA_CD=" + dataKbn + ","  + dataCd + "」が登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		return dpsCCdMst.getDataName();
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
		txt += "　" + TEXT_CATEGORY + "："  + searchDataName(DpsCodeMaster.CAT.getDbValue(),refScDto.getCategory());
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
