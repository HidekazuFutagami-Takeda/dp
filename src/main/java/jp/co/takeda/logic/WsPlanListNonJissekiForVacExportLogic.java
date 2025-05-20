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
import jp.co.takeda.dao.TmsTytenMstUnDAO;
import jp.co.takeda.dto.WsPlanReferenceForVacScDto;
import jp.co.takeda.model.WsPlanForVacSummary2;
import jp.co.takeda.model.div.TytenKisLevel;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.cmn.bean.ExportResultExcelImpl;

/**
 * (ワ)特約店別計画一覧【実績無し用】のExcelファイルを生成するロジッククラス
 *
 * @author khashimoto
 */
public class WsPlanListNonJissekiForVacExportLogic {

	/**
	 * テンプレート絶対パス名
	 */
	private final String templatePath;

	/**
	 * システム日付
	 */
	private final Date systemDate;

	/**
	 * (ワ)特約店別計画参照画面の検索条件DTO
	 */
	private final WsPlanReferenceForVacScDto refScDto;

	/**
	 * 特約店別計画のサマリーリスト
	 */
	private final List<WsPlanForVacSummary2> summaryList;

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
	private static final String SHEET_NAME = "(ワ)特約店別計画";

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

// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	/** 文字列「計画」 */
	private static final String TEXT_PLAN = "計画";

	/** 文字列「S」 */
	private static final String TEXT_S = "（S）";

	/** 文字列「B」 */
	private static final String TEXT_B = "（B）";
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする

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
	private static final int COL_IDX_DATE = 3;

	/** [行番号] 明細開始行 */
	private static final int ROW_IDX_START_LIST = 4;

	/** [列番号] 明細開始列 */
	private static final int COL_IDX_START_LIST = 0;

	/** [行番号] 印刷開始行 */
	private static final int ROW_IDX_PRINT_START = 0;

	/** [列番号] 印刷開始列 */
	private static final int COL_IDX_PRINT_START = 0;

	/** [列番号] 印刷終了列 */
	private static final int COL_IDX_PRINT_END = 4;

// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	/** [行番号] 明細ヘッダ2行目 */
	private static final int ROW_IDX_LIST_HEAD2 = 2;

	/** [列番号] 明細ヘッダ2行目 計画 */
	private static final int COL_IDX_LIST_HEAD2_PLAN = 4;
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする

	/**
	 * コンストラクタ
	 *
	 * @param templatePath テンプレート絶対パス名
	 * @param systemDate システム日付
	 * @param refScDto (ワ)特約店別計画参照画面の検索条件DTO
	 * @param summaryList 特約店別計画のサマリーリスト
	 * @param tmsTytenMstUnDAO 特約店基本統合DAO
	 */
	public WsPlanListNonJissekiForVacExportLogic(String templatePath, Date systemDate, WsPlanReferenceForVacScDto refScDto, List<WsPlanForVacSummary2> summaryList,
		TmsTytenMstUnDAO tmsTytenMstUnDAO) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (StringUtils.isEmpty(templatePath)) {
			final String errMsg = "テンプレート絶対パス名がNull、または空";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (systemDate == null) {
			final String errMsg = "システム日付がNull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (refScDto == null) {
			final String errMsg = "(ワ)特約店別計画参照画面の検索条件DTOがNull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (tmsTytenMstUnDAO == null) {
			final String errMsg = "特約店基本統合DAOがNull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// ----------------------
		// フィールドにセット
		// ----------------------
		this.templatePath = templatePath;
		this.systemDate = systemDate;
		this.refScDto = refScDto;
		this.summaryList = summaryList;
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
// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		// 明細ヘッダ
		String txtPlan = TEXT_PLAN;
		switch (refScDto.getKaBaseKb()) {
			case S_KA_BASE:
				txtPlan += TEXT_S;
				break;
			case Y_KA_BASE:
				txtPlan += TEXT_B;
				break;
		}
		poiBean.setCellData(txtPlan, ROW_IDX_LIST_HEAD2, COL_IDX_LIST_HEAD2_PLAN);
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	}

	/**
	 * リスト情報の値をセルに書き込む
	 *
	 * @param poiBean POI設定BEAN
	 */
	private void writeListInfo(POIBean poiBean) {
		// 集計リストがNull,または空の場合はReturn
		if (summaryList == null || summaryList.isEmpty()) {
			return;
		}

		// 集計リストのサイズ分行を追加
		poiBean.addRows(ROW_IDX_START_LIST, summaryList.size());

		// 行インデックス
		int rowIdx = ROW_IDX_START_LIST;

		for (WsPlanForVacSummary2 summary : summaryList) {

			// 列インデックス
			int colIdx = COL_IDX_START_LIST;

			// 特約店名
			final String tytenName = summary.getTmsTytenMeiKj();

			// 特約店コード
			final String tytenCode = summary.getTmsTytenCd();

			poiBean.setCellData(tytenName, rowIdx, colIdx++);
			poiBean.setCellData(tytenCode, rowIdx, colIdx++);

			// 品目名
			final String prodName = summary.getProdName();
			poiBean.setCellData(prodName, rowIdx, colIdx++);

			// 統計品目コード
			final String prodCode = summary.getStatProdCode();
			poiBean.setCellData(prodCode, rowIdx, colIdx++);

			// 翌期
			final Long subsequentPeriod = ConvertUtil.parseMoneyToThousandUnit(summary.getPlannedValue());
			poiBean.setCellData(subsequentPeriod, rowIdx, colIdx++);

			rowIdx++;
		}

		// 印刷範囲を設定
		final int rowIdxPrintEnd = ROW_IDX_START_LIST + summaryList.size();
		final short fitWidth = 1; // 横は1固定
		final short fitHeigth = 0; // 縦は0固定
		poiBean.setPringArea(ROW_IDX_PRINT_START, rowIdxPrintEnd, COL_IDX_PRINT_START, COL_IDX_PRINT_END, fitWidth, fitHeigth);
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
