package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
//mod Start 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.POIBean;
import jp.co.takeda.model.DistPlanningListULSummary;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.cmn.bean.ExportResultExcelImpl;

/**
 * 営業所計画アップロード用のフォーマットファイル(Excelファイル)を生成するロジッククラス
 *
 * @author khashimoto
 */
public class DistPlanningErrListExportLogic {

	/**
	 * テンプレート絶対パス名
	 */
	private final String templatePath;

	/**
	 * システム日付
	 */
	private final Date systemDate;

	/**
	 * 営業所計画アップロード用のフォーマットファイルのサマリーリスト
	 */
	private final List<DistPlanningListULSummary> distPlanningListULSummaryList;

	/**
	 * 出力ファイル名
	 */
	private final String outputFileName;

	// -------------------------------------
	// 文字列定数
	// -------------------------------------
	/** 出力ファイル名ヘッダ */
	private static final String OUTPUT_FILE_NAME_HEADER = "ERROR";

	/** シート名 */
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
	private static final String SHEET_NAME = "エリア計画";
//	private static final String SHEET_NAME = "営業所計画";
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）

	// -------------------------------------
	// インデックス定数
	// -------------------------------------
	/** [行番号] 明細開始行 */
	private static final int ROW_IDX_START_LIST = 2;

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
	 * @param distPlanningListSummaryList 営業所計画アップロード用のフォーマットファイルのサマリーリスト
	 */
	public DistPlanningErrListExportLogic(String templatePath, Date systemDate, List<DistPlanningListULSummary> distPlanningListULSummaryList) {
		this.templatePath = templatePath;
		this.systemDate = (Date) systemDate.clone();
		this.distPlanningListULSummaryList = distPlanningListULSummaryList;
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
			// mod Start 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
			//POIFSFileSystem poiFS = new POIFSFileSystem(fileIn);
			//HSSFWorkbook workBook = new HSSFWorkbook(poiFS);
			XSSFWorkbook workBook = new XSSFWorkbook(fileIn);
			// mod End 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
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
	// mod Start 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
	//private void write(HSSFWorkbook workBook) {
	private void write(XSSFWorkbook workBook) {
	// mod End 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応

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
	}

	/**
	 * リスト情報の値をセルに書き込む
	 *
	 * @param poiBean POI設定BEAN
	 */
	private void writeListInfo(POIBean poiBean) {
		// 集計リストがNull,または空の場合はReturn
		if (distPlanningListULSummaryList == null || distPlanningListULSummaryList.isEmpty()) {
			return;
		}

		// 行インデックス
		int rowIdx = ROW_IDX_START_LIST;

		for (DistPlanningListULSummary distPlanningSummary : distPlanningListULSummaryList) {

			// 列インデックス
			int colIdx = COL_IDX_START_LIST;

			// 支店・営業所コード
			final String brDistCode = distPlanningSummary.getBrDistCd();
			// mod Start 2024/07/26  H.Futagami フォントずれ対応
			//poiBean.setCellData(brDistCode, rowIdx, colIdx++);
			poiBean.setCellDataStyleCopy(brDistCode, rowIdx, colIdx++);
			// mod End 2024/07/26  H.Futagami フォントずれ対応

			// 支店・営業所名
			final String brDistName = distPlanningSummary.getBrDistNm();
			// mod Start 2024/07/26  H.Futagami フォントずれ対応
			//poiBean.setCellData(brDistName, rowIdx, colIdx++);
			poiBean.setCellDataStyleCopy(brDistName, rowIdx, colIdx++);
			// mod End 2024/07/26  H.Futagami フォントずれ対応

			// 統計品目コード
			final String prodCode = distPlanningSummary.getStatProdCd();
			// mod Start 2024/07/26  H.Futagami フォントずれ対応
			//poiBean.setCellData(prodCode, rowIdx, colIdx++);
			poiBean.setCellDataStyleCopy(prodCode, rowIdx, colIdx++);
			// mod End 2024/07/26  H.Futagami フォントずれ対応

			// 品目名
			final String prodName = distPlanningSummary.getProdNm();
			// mod Start 2024/07/26  H.Futagami フォントずれ対応
			//poiBean.setCellData(prodName, rowIdx, colIdx++);
			poiBean.setCellDataStyleCopy(prodName, rowIdx, colIdx++);
			// mod End 2024/07/26  H.Futagami フォントずれ対応

			// 計画値UH(Y・B)
			final String plannedValueUHT = distPlanningSummary.getPlannedValue_UH_T();
			// mod Start 2024/07/26  H.Futagami フォントずれ対応
			//poiBean.setCellData(plannedValueUHT, rowIdx, colIdx++);
			poiBean.setCellDataStyleCopy(plannedValueUHT, rowIdx, colIdx++);
			// mod End 2024/07/26  H.Futagami フォントずれ対応

			// 計画値P(Y・B)
			final String plannedValuePT = distPlanningSummary.getPlannedValue_P_T();
			// mod Start 2024/07/26  H.Futagami フォントずれ対応
			//poiBean.setCellData(plannedValuePT, rowIdx, colIdx++);
			poiBean.setCellDataStyleCopy(plannedValuePT, rowIdx, colIdx++);
			// mod End 2024/07/26  H.Futagami フォントずれ対応

			// エラー内容
			final String errConts = distPlanningSummary.getErrConts();
			// mod Start 2024/07/26  H.Futagami フォントずれ対応
			//poiBean.setCellData(errConts, rowIdx, colIdx++);
			poiBean.setCellDataStyleCopy(errConts, rowIdx, colIdx++);
			// mod End 2024/07/26  H.Futagami フォントずれ対応

			rowIdx++;

		}

		// 印刷範囲を設定
		final int rowIdxPrintEnd = ROW_IDX_START_LIST + distPlanningListULSummaryList.size();
		final short fitWidth = 1; // 横は1固定
		final short fitHeigth = 0; // 縦は0固定
		poiBean.setPringArea(ROW_IDX_PRINT_START, rowIdxPrintEnd, COL_IDX_PRINT_START, COL_IDX_PRINT_END, fitWidth, fitHeigth);
	}

}
