package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.POIBean;
import jp.co.takeda.model.CheckTool;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.cmn.bean.ExportResultExcelImpl;

/**
 * 営業所計画アップロード用のフォーマットファイル(Excelファイル)を生成するロジッククラス
 *
 * @author ksuzuki
 */
public class CheckToolExportLogic {

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
	private final List<CheckTool> checkTool;

	/**
	 * 出力ファイル名
	 */
	private final String outputFileName;

	/** 出力ファイル名ヘッダ */
	private final String OUTPUT_FILE_NAME_HEADER;

	// -------------------------------------
	// 文字列定数
	// -------------------------------------

	/** シート名 */
	private final String SHEET_NAME ;

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
	private static final int COL_IDX_PRINT_END = 14;

	/**
	 * コンストラクタ
	 *
	 * @param templatePath テンプレート絶対パス名
	 * @param systemDate システム日付
	 * @param distPlanningListSummaryList 営業所計画アップロード用のフォーマットファイルのサマリーリスト
	 */
	public CheckToolExportLogic(String templatePath, Date systemDate, List<CheckTool> checkTool, String fileName) {
		this.templatePath = templatePath;
		this.systemDate = (Date) systemDate.clone();
		this.checkTool = checkTool;
		this.OUTPUT_FILE_NAME_HEADER = fileName;
		this.SHEET_NAME = createSheetName(fileName);
		this.outputFileName = createOutputFileName(systemDate);

	}

	/**
	 * シート名の生成
	 *
	 * @param systemDate システム日付
	 * @return 出力ファイル名
	 */
	private String createSheetName(String fileName) {
		String sheetNm = "";
		int index1 =  fileName.indexOf("_");
		if (index1 > 0) {
			sheetNm = fileName.substring(0,index1);
		} else {
			sheetNm = fileName;
		}
		return sheetNm;
	}

	/**
	 * 出力ファイル名の生成
	 *
	 * @param systemDate システム日付
	 * @return 出力ファイル名
	 */
	private String createOutputFileName(Date systemDate) {
		String fileNm = "";
		try {
			fileNm = URLEncoder.encode(OUTPUT_FILE_NAME_HEADER, "UTF-8") + "_" + DateUtil.toString(systemDate, "yyyyMMddHHmmss") + ".xlsx";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return fileNm;
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
			XSSFWorkbook workBook = new XSSFWorkbook(fileIn);
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
	private void write(XSSFWorkbook workBook) {

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
		if (checkTool == null || checkTool.isEmpty()) {
			return;
		}

		String title ="計画支援システム　" + OUTPUT_FILE_NAME_HEADER +"　作成日時：" + DateUtil.toString(systemDate, "yyyy/MM/dd/ HH:mm");
		poiBean.setCellData(title, 0, 0);

		// 行インデックス
		int rowIdx = ROW_IDX_START_LIST;

		//文字列用のセル情報を保持
		XSSFCellStyle style = poiBean.createCellStyle();
		XSSFRow row = poiBean.getWorkSheet().getRow(0);
		XSSFCell cell = row.getCell(0);
		style.setFont(cell.getCellStyle().getFont());
		style.setDataFormat(cell.getCellStyle().getDataFormat());


		//数値用のセル情報を保持
		XSSFCellStyle style2 = poiBean.createCellStyle();
		XSSFRow row2 = poiBean.getWorkSheet().getRow(0);
		XSSFCell cell2 = row2.getCell(13);
		style2.setFont(cell2.getCellStyle().getFont());
		style2.setDataFormat(cell2.getCellStyle().getDataFormat());

		for (CheckTool checkTool : checkTool) {

			// 列インデックス
			int colIdx = COL_IDX_START_LIST;

			// リージョンコード
			poiBean.setCellData(checkTool.getUpSosCd(), rowIdx, colIdx++, style);

			// リージョン名
			poiBean.setCellData(checkTool.getUpBumonRyakuName(), rowIdx, colIdx++, style);

			// エリアコード
			poiBean.setCellData(checkTool.getSosCd(), rowIdx, colIdx++, style);

			// エリア名
			poiBean.setCellData(checkTool.getBumonRyakuName(), rowIdx, colIdx++, style);

			// MRコード
			poiBean.setCellData(checkTool.getMrNo(), rowIdx, colIdx++, style);

			// MR名
			poiBean.setCellData(checkTool.getJgiName(), rowIdx, colIdx++, style);

			// 品目コード
			poiBean.setCellData(checkTool.getProdCode(), rowIdx, colIdx++, style);

			// 品目名
			poiBean.setCellData(checkTool.getProdName(), rowIdx, colIdx++, style);

			// 対象(UHP)
			poiBean.setCellData(checkTool.getInsType(), rowIdx, colIdx++, style);

			// TMS特約店コード
			poiBean.setCellData(checkTool.getTmsTytenCd(), rowIdx, colIdx++, style);

			// TMS特約店名称(漢字)
			poiBean.setCellData(checkTool.getTmsTytenMeiKj(), rowIdx, colIdx++, style);

			// 施設固定コード
			poiBean.setCellData(checkTool.getInsNo(), rowIdx, colIdx++, style);

			// 施設名
			poiBean.setCellData(checkTool.getInsAbbrName(), rowIdx, colIdx++, style);

			// Y価格計画値(千円)
			poiBean.setCellData(checkTool.getYBasePlanSen(), rowIdx, colIdx++, style2);

			rowIdx++;

		}

		// 印刷範囲を設定
		final int rowIdxPrintEnd = ROW_IDX_START_LIST + checkTool.size();
		final short fitWidth = 1; // 横は1固定
		final short fitHeigth = 0; // 縦は0固定
		poiBean.setPringArea(ROW_IDX_PRINT_START, rowIdxPrintEnd, COL_IDX_PRINT_START, COL_IDX_PRINT_END, fitWidth, fitHeigth);
	}

}
