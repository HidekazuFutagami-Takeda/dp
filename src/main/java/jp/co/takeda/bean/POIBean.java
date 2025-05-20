package jp.co.takeda.bean;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFName;
import org.apache.poi.xssf.usermodel.XSSFPrintSetup;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;

/**
 * POIでのExcel操作を行うビーンクラス
 *
 * @author stakeuchi
 */
public class POIBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 操作対象のワークブック
	 */
//	private transient HSSFWorkbook workbook = null;
	private transient XSSFWorkbook workbook = null;

	/**
	 * 操作対象のワークシートインデックス
	 */
	private int workSheetIdx = 0;

	/**
	 * 行オフセット
	 */
	private int rowNumOffset = 0;

	/**
	 * 列オフセット
	 */
	private int colNumOffset = 0;

	/**
	 * コンストラクタ
	 *
	 * @param hssfWorkbook 操作対象のワークブック
	 */
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//	public POIBean(HSSFWorkbook workbook) {
	public POIBean(XSSFWorkbook workbook) {
		// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		if (workbook == null) {
			final String errMsg = "POI初期設定エラー。workbookがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		this.workbook = workbook;
	}

	/**
	 * 指定されたセルに値をセットする。
	 *
	 * @param value String値
	 * @param rowIdx 行番号
	 * @param colIdx 列番号
	 */
	public void setCellData(String value, int rowIdx, int colIdx) {
		setCellData(value, rowIdx, colIdx, null);
	}

	// add Start 2024/07/26  H.Futagami フォントずれ対応
	/**
	 * 指定されたセルに値をセットする。スタイルコピー
	 *
	 * @param value String値
	 * @param rowIdx 行番号
	 * @param colIdx 列番号
	 */
	public void setCellDataStyleCopy(String value, int rowIdx, int colIdx) {
		setCellDataStyleCopy(value, rowIdx, colIdx, null);
	}
	// add End 2024/07/26  H.Futagami フォントずれ対応

	/**
	 * 指定されたセルに値をセットする。
	 *
	 * @param value String値
	 * @param positions 行列を格納した配列(0:行、1:列)
	 */
	public void setCellData(String value, int[] positions) {
		this.setCellData(value, positions[0], positions[1]);
	}

	/**
	 * 指定されたセルに値をセットする。
	 *
	 * @param value String値
	 * @param rowIdx 行番号
	 * @param colIdx 列番号
	 * @param style セルスタイル
	 */
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//	public void setCellData(String value, int rowIdx, int colIdx, HSSFCellStyle style) {
	public void setCellData(String value, int rowIdx, int colIdx, XSSFCellStyle style) {
		try {
//			final HSSFSheet sheet = getWorkSheet();
//			HSSFRow row = sheet.getRow(rowIdx);
			final XSSFSheet sheet = getWorkSheet();
			XSSFRow row = sheet.getRow(rowIdx);
			if (row == null) {
				row = sheet.createRow(rowIdx);
			}
//			HSSFCell cell = row.getCell(colIdx);
			XSSFCell cell = row.getCell(colIdx);
			if (cell == null) {
				cell = row.createCell(colIdx);
			}
			if (style != null) {
				cell.setCellStyle(style);
			}
//			cell.setCellValue(new HSSFRichTextString(value));
			cell.setCellValue(new XSSFRichTextString(value));
			// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		} catch (Exception e) {
			StringBuilder errMsgSB = new StringBuilder("指定セルに値を設定中に例外が発生。");
			errMsgSB.append("value=" + value);
			errMsgSB.append(",rowIdx=" + rowIdx);
			errMsgSB.append(",colIdx=" + colIdx);
			errMsgSB.append(",style=" + style);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}

	// add Start 2024/07/26  H.Futagami フォントずれ対応
	/**
	 * 指定されたセルに値をセットする。スタイルコピー
	 *
	 * @param value String値
	 * @param rowIdx 行番号
	 * @param colIdx 列番号
	 */
	public void setCellDataStyleCopy(String value, int rowIdx, int colIdx, XSSFCellStyle style) {
		try {
			final XSSFSheet sheet = getWorkSheet();
			XSSFRow row = sheet.getRow(rowIdx);
			if (row == null) {
				row = sheet.createRow(rowIdx);
			}
			XSSFCell cell = row.getCell(colIdx);
			if (cell == null) {
				cell = row.createCell(colIdx);
			}

			cell.setCellStyle(sheet.getRow(0).getCell(0).getCellStyle());

			cell.setCellValue(new XSSFRichTextString(value));
		} catch (Exception e) {
			StringBuilder errMsgSB = new StringBuilder("指定セルに値を設定中に例外が発生。");
			errMsgSB.append("value=" + value);
			errMsgSB.append(",rowIdx=" + rowIdx);
			errMsgSB.append(",colIdx=" + colIdx);
			errMsgSB.append(",style=" + style);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}
	// add End 2024/07/26  H.Futagami フォントずれ対応

	/**
	 * 指定されたセルに値をセットする。
	 *
	 * @param value String値
	 * @param positions 行列を格納した配列(0:行、1:列)
	 * @param style セルスタイル
	 */
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//	public void setCellData(String value, int[] positions, HSSFCellStyle style) {
	public void setCellData(String value, int[] positions, XSSFCellStyle style) {
	// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		this.setCellData(value, positions[0], positions[1], style);
	}

	/**
	 * 指定されたセルに値をセットする。
	 *
	 * @param value Long値
	 * @param rowIdx 行番号
	 * @param colIdx 列番号
	 */
	public void setCellData(Long value, int rowIdx, int colIdx) {
		if (value != null) {
			setCellData(value.doubleValue(), rowIdx, colIdx, null);
		}
	}

	/**
	 * 指定されたセルに値をセットする。
	 *
	 * @param value Long値
	 * @param positions 行列を格納した配列(0:行、1:列)
	 */
	public void setCellData(Long value, int[] positions) {
		this.setCellData(value, positions[0], positions[1]);
	}

	/**
	 * 指定されたセルに値をセットする。
	 *
	 * @param value Long値
	 * @param rowIdx 行番号
	 * @param colIdx 列番号
	 * @param style セルスタイル
	 */
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//	public void setCellData(Long value, int rowIdx, int colIdx, HSSFCellStyle style) {
	public void setCellData(Long value, int rowIdx, int colIdx, XSSFCellStyle style) {
	// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		if (value != null) {
			setCellData(value.doubleValue(), rowIdx, colIdx, style);
		}
	}

	/**
	 * 指定されたセルに値をセットする。
	 *
	 * @param value Long値
	 * @param positions 行列を格納した配列(0:行、1:列)
	 * @param style セルスタイル
	 */
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//	public void setCellData(Long value, int[] positions, HSSFCellStyle style) {
	public void setCellData(Long value, int[] positions, XSSFCellStyle style) {
	// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		this.setCellData(value, positions[0], positions[1], style);
	}

	/**
	 * 指定されたセルに値をセットする。
	 *
	 * @param value Double値
	 * @param rowIdx 行番号
	 * @param colIdx 列番号
	 */
	public void setCellData(Double value, int rowIdx, int colIdx) {
		if (value != null) {
			setCellData(value.doubleValue(), rowIdx, colIdx, null);
		}
	}

	/**
	 * 指定されたセルに値をセットする。
	 *
	 * @param value Double値
	 * @param positions 行列を格納した配列(0:行、1:列)
	 */
	public void setCellData(Double value, int[] positions) {
		this.setCellData(value, positions[0], positions[1]);
	}

	/**
	 * 指定されたセルに値をセットする。
	 *
	 * @param value Double値
	 * @param rowIdx 行番号
	 * @param colIdx 列番号
	 * @param style セルスタイル
	 */
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//	public void setCellData(Double value, int rowIdx, int colIdx, HSSFCellStyle style) {
	public void setCellData(Double value, int rowIdx, int colIdx, XSSFCellStyle style) {
		try {
//			final HSSFSheet sheet = getWorkSheet();
//			HSSFRow row = sheet.getRow(rowIdx);
			final XSSFSheet sheet = getWorkSheet();
			XSSFRow row = sheet.getRow(rowIdx);
			if (row == null) {
				row = sheet.createRow(rowIdx);
			}
//			HSSFCell cell = row.getCell(colIdx);
			XSSFCell cell = row.getCell(colIdx);
	// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
			if (cell == null) {
				cell = row.createCell(colIdx);
			}
			if (style != null) {
				cell.setCellStyle(style);
			}
			if (value != null) {
				cell.setCellValue(value.doubleValue());
			}
		} catch (Exception e) {
			StringBuilder errMsgSB = new StringBuilder("指定セルに値を設定中に例外が発生。");
			errMsgSB.append("value=" + value);
			errMsgSB.append(",rowIdx=" + rowIdx);
			errMsgSB.append(",colIdx=" + colIdx);
			errMsgSB.append(",style=" + style);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}

	/**
	 * 指定されたセルに値をセットする。
	 *
	 * @param value Double値
	 * @param positions 行列を格納した配列(0:行、1:列)
	 * @param style セルスタイル
	 */
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//	public void setCellData(Double value, int[] positions, HSSFCellStyle style) {
	public void setCellData(Double value, int[] positions, XSSFCellStyle style) {
	// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		this.setCellData(value, positions[0], positions[1], style);
	}

	/**
	 * 指定されたセルに値をセットする。
	 *
	 * @param value Date値
	 * @param rowIdx 行番号
	 * @param colIdx 列番号
	 */
	public void setCellData(Date value, int rowIdx, int colIdx) {
		if (value != null) {
			setCellData(value, rowIdx, colIdx, null);
		}
	}

	/**
	 * 指定されたセルに値をセットする。
	 *
	 * @param value Date値
	 * @param positions 行列を格納した配列(0:行、1:列)
	 */
	public void setCellData(Date value, int[] positions) {
		this.setCellData(value, positions[0], positions[1]);
	}

	/**
	 * 指定されたセルに値をセットする。
	 *
	 * @param value Date値
	 * @param rowIdx 行番号
	 * @param colIdx 列番号
	 * @param style セルスタイル
	 */
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//	public void setCellData(Date value, int rowIdx, int colIdx, HSSFCellStyle style) {
	public void setCellData(Date value, int rowIdx, int colIdx, XSSFCellStyle style) {
		try {
//			final HSSFSheet sheet = getWorkSheet();
//			HSSFRow row = sheet.getRow(rowIdx);
			final XSSFSheet sheet = getWorkSheet();
			XSSFRow row = sheet.getRow(rowIdx);
			if (row == null) {
				row = sheet.createRow(rowIdx);
			}
//			HSSFCell cell = row.getCell(colIdx);
			XSSFCell cell = row.getCell(colIdx);
	// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
			if (cell == null) {
				cell = row.createCell(colIdx);
			}
			if (style != null) {
				cell.setCellStyle(style);
			}
			if (value != null) {
				cell.setCellValue(value);
			}
		} catch (Exception e) {
			StringBuilder errMsgSB = new StringBuilder("指定セルに値を設定中に例外が発生。");
			errMsgSB.append("value=" + value);
			errMsgSB.append(",rowIdx=" + rowIdx);
			errMsgSB.append(",colIdx=" + colIdx);
			errMsgSB.append(",style=" + style);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}

	/**
	 * 指定されたセルに値をセットする。
	 *
	 * @param value Date値
	 * @param positions 行列を格納した配列(0:行、1:列)
	 * @param style セルスタイル
	 */
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//	public void setCellData(Date value, int[] positions, HSSFCellStyle style) {
	public void setCellData(Date value, int[] positions, XSSFCellStyle style) {
	// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		this.setCellData(value, positions[0], positions[1], style);
	}

	/**
	 * 指定されたセルに式をセットする。
	 *
	 * @param formula 式
	 * @param rowIdx 行番号
	 * @param colIdx 列番号
	 */
	public void setCellFormula(String formula, int rowIdx, int colIdx) {
		try {
			// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//			HSSFRow row = getWorkSheet().getRow(rowIdx);
//			HSSFCell cell = row.getCell(colIdx);
			XSSFRow row = getWorkSheet().getRow(rowIdx);
			XSSFCell cell = row.getCell(colIdx);
			// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
			cell.setCellFormula(formula);
		} catch (Exception e) {
			StringBuilder errMsgSB = new StringBuilder("指定されたセルに式を設定中に例外が発生。");
			errMsgSB.append("formula=" + formula);
			errMsgSB.append(",rowIdx=" + rowIdx);
			errMsgSB.append(",colIdx=" + colIdx);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}

	/**
	 * 指定されたセルに式をセットする。
	 *
	 * @param formula 式
	 * @param positions 行列を格納した配列(0:行、1:列)
	 */
	public void setCellFormula(String formula, int[] positions) {
		this.setCellFormula(formula, positions[0], positions[1]);
	}

	/**
	 * 列番号名を取得する。
	 *
	 * @param colIdx 列番号
	 * @return Excel列名
	 */
	public String getColumnNameString(int colIdx) {
		try {
			if (colIdx > 255) {
				throw new SystemException(new Conveyance(SYSTEM_ERROR, "列インデックスがオーバー。colIdx=" + colIdx));
			}
			StringBuilder result = new StringBuilder();
			int ch1 = colIdx / 26;
			if (ch1 > 0) {
				result.append((char) (ch1 + 64));
			}
			int ch2 = colIdx % 26;
			result.append((char) (ch2 + 65));
			return result.toString();
		} catch (Exception e) {
			StringBuilder errMsgSB = new StringBuilder("列番号名を取得中に例外が発生。");
			errMsgSB.append("colIdx=" + colIdx);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}

	/**
	 * セルスタイルを作成する。
	 *
	 * @return セルスタイル
	 */
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//	public HSSFCellStyle createCellStyle() {
	public XSSFCellStyle createCellStyle() {
	// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		return getWorkbook().createCellStyle();
	}

	/**
	 * セルスタイルを作成する。
	 *
	 * @param border 罫線
	 * @param foreColor 前景色
	 * @param fontColor 文字色
	 * @return セルスタイル
	 */
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//	public HSSFCellStyle createCellStyle(short[] border, short foreColor, short fontColor) {
	public XSSFCellStyle createCellStyle(short[] border, short foreColor, short fontColor) {
		try {
//			HSSFCellStyle style = getWorkbook().createCellStyle();
			XSSFCellStyle style = getWorkbook().createCellStyle();
			// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
			// 罫線
			if (border != null && border.length == 4) {
				style.setBorderTop(border[0]);
				style.setBorderBottom(border[1]);
				style.setBorderLeft(border[2]);
				style.setBorderRight(border[3]);
			}
			// 前景色
			style.setFillForegroundColor(foreColor);
			// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//			if (foreColor == HSSFColor.WHITE.index) {
//				style.setFillPattern(HSSFCellStyle.NO_FILL);
//			} else {
//				style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//			}
			if (foreColor == IndexedColors.WHITE.getIndex()) {
				style.setFillPattern(XSSFCellStyle.NO_FILL);
			} else {
				style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			}
			// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
			// 文字色
			// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//			HSSFFont font = getWorkbook().createFont();
			XSSFFont font = getWorkbook().createFont();
			// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
			font.setColor(fontColor);
			style.setFont(font);
			return style;

		} catch (Exception e) {
			StringBuilder errMsgSB = new StringBuilder("列番号名を取得中に例外が発生。");
			errMsgSB.append("foreColor=" + foreColor);
			errMsgSB.append(",fontColor=" + fontColor);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}

	/**
	 * Excelの名前定義を利用してセルに値を挿入する。
	 *
	 * @param nameMap (MAP<定義名, 挿入名>)
	 */
	public void putNameIdx(Map<String, String> nameMap) {
		try {
			if (nameMap == null) {
				return;
			}
			for (Entry<String, String> entry : nameMap.entrySet()) {
				final int index = getWorkbook().getNameIndex(entry.getKey());
				if (index == -1) {
					continue;
				}
				// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//				final HSSFName hssfName = getWorkbook().getNameAt(index);
//				final CellReference reference = new CellReference(hssfName.getReference());
				final XSSFName xssfName = getWorkbook().getNameAt(index);
				final CellReference reference = new CellReference(xssfName.getRefersToFormula());
				// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
				final short row = (short) reference.getRow();
				final short col = reference.getCol();
				final String insertName = nameMap.get(entry.getKey());
				if (StringUtils.isNotEmpty(insertName)) {
					setCellData(insertName, row, col);
				}
			}
		} catch (Exception e) {
			StringBuilder errMsgSB = new StringBuilder("Excelの名前定義を利用してセルに値を挿入中に例外が発生。");
			errMsgSB.append("nameMap=" + nameMap);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}

	/**
	 * 操作対象のワークブックを取得する。
	 *
	 * @return workbook 操作対象のワークブック
	 */
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//	public HSSFWorkbook getWorkbook() {
	public XSSFWorkbook getWorkbook() {
	// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		return workbook;
	}

	/**
	 * 操作対象のワークシートを取得する。
	 *
	 * @return workSheet 操作対象のワークシート
	 */
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//	public HSSFSheet getWorkSheet() {
//		HSSFSheet workSheet = getWorkbook().getSheetAt(getWorkSheetIdx());
	public XSSFSheet getWorkSheet() {
		XSSFSheet workSheet = getWorkbook().getSheetAt(getWorkSheetIdx());
	// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		return workSheet;
	}

	/**
	 * 操作対象のワークシートインデックスを取得する。
	 *
	 * @return workSheetIdx 操作対象のワークシートインデックスを取得する。
	 */
	public int getWorkSheetIdx() {
		return workSheetIdx;
	}

	/**
	 * 操作対象のワークシートインデックスを設定する。
	 *
	 * @param workSheetIdx 操作対象のワークシートインデックス
	 */
	public void setWorkSheetIdx(int workSheetIdx) {
		this.workSheetIdx = workSheetIdx;
	}

	/**
	 * 列オフセットを取得する。
	 *
	 * @return colNumOffset 列オフセット
	 */
	public int getColNumOffset() {
		return colNumOffset;
	}

	/**
	 * 列オフセットを設定する。
	 *
	 * @param colNumOffset 列オフセット
	 */
	public void setColNumOffset(int colNumOffset) {
		this.colNumOffset = colNumOffset;
	}

	/**
	 * 行オフセットを取得する。
	 *
	 * @return rowNumOffset 行オフセット
	 */
	public int getRowNumOffset() {
		return rowNumOffset;
	}

	/**
	 * 行オフセットを設定する。
	 *
	 * @param rowNumOffset 行オフセット
	 */
	public void setRowNumOffset(int rowNumOffset) {
		this.rowNumOffset = rowNumOffset;
	}

	/**
	 * 指定したワークシートインデックスのシート名を取得する。
	 *
	 * @param sheetIdx シートインデックス
	 */
	public void getSheetName(int sheetIdx) {
		try {
			getWorkbook().getSheetName(sheetIdx);

		} catch (Exception e) {
			StringBuilder errMsgSB = new StringBuilder("指定したワークシートインデックスのシート名を取得中に例外が発生。");
			errMsgSB.append("sheetIdx=" + sheetIdx);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}

	/**
	 * 操作中のワークシート名を設定する。
	 *
	 * @param sheetName シート名
	 */
	public void setSheetName(String sheetName) {
		try {
			getWorkbook().setSheetName(getWorkSheetIdx(), sheetName);

		} catch (Exception e) {
			StringBuilder errMsgSB = new StringBuilder("操作中のワークシート名を設定中に例外が発生。");
			errMsgSB.append("sheetName=" + sheetName);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}

	/**
	 * 指定したワークシートインデックスにシート名を設定する。
	 *
	 * @param sheetIdx シートインデックス
	 * @param sheetName シート名
	 */
	public void setSheetName(int sheetIdx, String sheetName) {
		try {
			getWorkbook().setSheetName(sheetIdx, sheetName);
		} catch (Exception e) {
			StringBuilder errMsgSB = new StringBuilder("操作中のワークシート名を設定中に例外が発生。");
			errMsgSB.append("sheetIdx=" + sheetIdx);
			errMsgSB.append("sheetName=" + sheetName);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}

	/**
	 * 指定したシートインデックスのシートを新規シートにコピーする。
	 *
	 * @param sheetIdx コピー元シートインデックス
	 */
	public void cloneSheet(int sheetIdx) {
		try {
			getWorkbook().cloneSheet(sheetIdx);
		} catch (Exception e) {
			StringBuilder errMsgSB = new StringBuilder("指定したシートインデックスのシートを新規シートにコピー中に例外が発生。");
			errMsgSB.append("sheetIdx=" + sheetIdx);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}

	/**
	 * 指定行の関数をクリアする。
	 *
	 * @param rowIdx 行インデックス
	 */
	@SuppressWarnings("unchecked")
	public void clearFormula(int rowIdx) {
		// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//		final HSSFSheet sheet = getWorkSheet();
//		HSSFRow row = sheet.getRow(rowIdx);
		final XSSFSheet sheet = getWorkSheet();
		XSSFRow row = sheet.getRow(rowIdx);
		// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		if (row != null) {
			Iterator cellItr = row.cellIterator();
			while (cellItr.hasNext()) {
				// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//				HSSFCell cell = (HSSFCell) cellItr.next();
				XSSFCell cell = (XSSFCell) cellItr.next();
				// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
				cell.setCellFormula(null);
			}
		}
	}

	/**
	 * 指定行を非表示にする。<br>
	 * 非表示行の関数は全てクリアする。
	 *
	 * @param rowIdx 行インデックス
	 */
	@SuppressWarnings("unchecked")
	public void hideRow(int rowIdx) {
		// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//		final HSSFSheet sheet = getWorkSheet();
//		HSSFRow row = sheet.getRow(rowIdx);
		final XSSFSheet sheet = getWorkSheet();
		XSSFRow row = sheet.getRow(rowIdx);
		// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		if (row != null) {
			Iterator cellItr = row.cellIterator();
			while (cellItr.hasNext()) {
				// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//				HSSFCell cell = (HSSFCell) cellItr.next();
				XSSFCell cell = (XSSFCell) cellItr.next();
				// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
				cell.setCellFormula(null);
			}
			row.setZeroHeight(true);
		}
	}

	/**
	 * 指定行番号から指定行数を削除する。
	 *
	 * @param rowIdx 行インデックス
	 * @param delCount 削除数
	 */
	public void removeRow(int rowIdx, int delCount) {
		// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//		final HSSFSheet sheet = getWorkSheet();
		final XSSFSheet sheet = getWorkSheet();
		// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		for (int i = 0; i < delCount; i++) {
			int lastRowNum = sheet.getLastRowNum();
			if (rowIdx <= lastRowNum) {
				sheet.shiftRows(rowIdx, lastRowNum, -1);
			}
		}
	}

	// add Start 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
	/**
	 * 指定行番号から最終を削除する。
	 *
	 * @param rowIdx 行インデックス
	 */
	public void removeEndRow(int rowIdx) {
		final XSSFSheet sheet = getWorkSheet();

		int lastRowNum = sheet.getLastRowNum();
		for (int i = rowIdx - 1; i <= lastRowNum; i++) {

			XSSFRow row = sheet.getRow(i);
			clearFormula(i);
			sheet.removeRow(row);
		}
	}
	// add End 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応

	/**
	 * テンプレートの計算式で自動計算を行うかどうかを設定する。
	 *
	 * @param doCalc 自動計算を行う=TRUE, 自動計算を行わない=FALSE
	 */
	public void setForceFormulaRecalculation(boolean doCalc) {
		// mod Start 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
//		getWorkSheet().setForceFormulaRecalculation(doCalc);
		XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
		// mod End 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
	}

	/**
	 * 指定したシートインデックスのシートを削除する。
	 *
	 * @param sheetIdx 削除対象のシートインデックス
	 */
	public void removeSheetAt(int sheetIdx) {
		try {
			getWorkbook().removeSheetAt(sheetIdx);

		} catch (Exception e) {
			StringBuilder errMsgSB = new StringBuilder("指定したシートインデックスのシートを削除中に例外が発生。");
			errMsgSB.append("sheetIdx=" + sheetIdx);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}

	/**
	 * 指定した名前のシートを指定位置に移動する。
	 *
	 * @param name シート名
	 * @param sheetIdx 指定位置
	 */
	public void setSheetOrder(String name, int sheetIdx) {
		try {
			getWorkbook().setSheetOrder(name, sheetIdx);
		} catch (Exception e) {
			StringBuilder errMsgSB = new StringBuilder("指定した名前のシートを指定位置に移動中に例外が発生。");
			errMsgSB.append("sheetIdx=" + sheetIdx);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}

	/**
	 * 指定した名前のシートを末尾に移動する。
	 *
	 * @param name シート名
	 */
	public void setSheetOrderToEnd(String name) {
		try {
			int sheetNums = getWorkbook().getNumberOfSheets();
			getWorkbook().setSheetOrder(name, sheetNums - 1);
		} catch (Exception e) {
			StringBuilder errMsgSB = new StringBuilder("指定した名前のシートを末尾に移動中に例外が発生。");
			errMsgSB.append("name=" + name);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}

	/**
	 * 指定した位置のシートを非表示にする。
	 *
	 * @param sheetIdx シート位置
	 */
	public void hideSheet(int sheetIdx) {
		try {
			getWorkbook().setSheetHidden(sheetIdx, true);
		} catch (Exception e) {
			StringBuilder errMsgSB = new StringBuilder("指定した名前のシートを末尾に移動中に例外が発生。");
			errMsgSB.append("sheetIdx=" + sheetIdx);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}

	/**
	 * 行を挿入する。<br>
	 * 挿入行は挿入元のスタイルが全てコピーされる。
	 *
	 * @param addRow 挿入行
	 * @param addCount 挿入数
	 */
	public void addRows(int addRow, int addCount) {
		try {
			for (int i = 0; i < addCount; i++) {
				final int rowIdx = addRow + i;
				getWorkSheet().shiftRows(rowIdx, getWorkSheet().getLastRowNum(), 1, true, false);
				getWorkSheet().createRow(rowIdx);
				copyRowStyle(rowIdx + 1, rowIdx);
			}
		} catch (Exception e) {
			StringBuilder errMsgSB = new StringBuilder("行を挿入中に例外が発生。");
			errMsgSB.append("addRow=" + addRow);
			errMsgSB.append(",addCount=" + addCount);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}

	/**
	 * 行を挿入する。<br>
	 * 挿入行は挿入元のスタイルが全てコピーされる。
	 *
	 * @param addRow 挿入行
	 * @param addCount 挿入数
	 */
	public void addRowsExt(int addRow, int addCount) {
		try {
			for (int i = 0; i < addCount; i++) {
				final int rowIdx = addRow + i;
				setRowCopy(rowIdx);
			}
		} catch (Exception e) {
			StringBuilder errMsgSB = new StringBuilder("行を挿入中に例外が発生。");
			errMsgSB.append("addRow=" + addRow);
			errMsgSB.append(",addCount=" + addCount);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}

	/**
	 * 挿入行の設定<br>
	 * 指定した行をコピーして、1行下に追加する。
	 *
	 * @param addRow 指定した行
	 */
	private void setRowCopy(int addRow) {
		// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		// テンプレート行の取得
//		HSSFRow templateRow = getWorkSheet().getRow(addRow);
		XSSFRow templateRow = getWorkSheet().getRow(addRow);
		// コピー先の行の取得
//		HSSFRow row = getWorkSheet().getRow(addRow + 1);
		XSSFRow row = getWorkSheet().getRow(addRow + 1);
		if (row == null) {
			row = getWorkSheet().createRow(addRow + 1);
			row.setHeight(templateRow.getHeight());
		}
		int length = templateRow.getLastCellNum();
		for (int i = 0; i < length; i++) {
//			HSSFCell firstCell = templateRow.getCell(i);
//			HSSFCell cell = row.createCell(i);
			XSSFCell firstCell = templateRow.getCell(i);
			XSSFCell cell = row.createCell(i);
			if (firstCell != null) {
				// 値を取得
				switch (firstCell.getCellType()) {
//					case HSSFCell.CELL_TYPE_BLANK:
					case XSSFCell.CELL_TYPE_BLANK:
						break;
//					case HSSFCell.CELL_TYPE_BOOLEAN:
					case XSSFCell.CELL_TYPE_BOOLEAN:
						cell.setCellValue(firstCell.getBooleanCellValue());
						break;
//					case HSSFCell.CELL_TYPE_ERROR:
					case XSSFCell.CELL_TYPE_ERROR:
						cell.setCellValue(firstCell.getErrorCellValue());
						break;
//					case HSSFCell.CELL_TYPE_NUMERIC:
					case XSSFCell.CELL_TYPE_NUMERIC:
						cell.setCellValue(firstCell.getNumericCellValue());
						break;
//					case HSSFCell.CELL_TYPE_STRING:
					case XSSFCell.CELL_TYPE_STRING:
		// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
						cell.setCellValue(firstCell.getRichStringCellValue());
						break;
					default:
				}
				// スタイルを取得
				cell.setCellStyle(firstCell.getCellStyle());
			}
		}
	}

	/**
	 * 行に属する列すべての書式をコピーする。
	 *
	 * @param sourceRowIdx コピー元の行
	 * @param copyRowIdx コピー先の行
	 */
	public void copyRowStyle(int sourceRowIdx, int copyRowIdx) {
		try {
			// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//			HSSFRow sourceRow = getWorkSheet().getRow(sourceRowIdx);
//			HSSFRow copyRow = getWorkSheet().getRow(copyRowIdx);
			XSSFRow sourceRow = getWorkSheet().getRow(sourceRowIdx);
			XSSFRow copyRow = getWorkSheet().getRow(copyRowIdx);
			// 行の高さの複製
			copyRow.setHeight(sourceRow.getHeight());
			copyRow.setHeightInPoints(sourceRow.getHeightInPoints());
			for (int i = sourceRow.getFirstCellNum(); i < sourceRow.getLastCellNum(); i++) {
				if (i < 0) {
					// 列に所属する１セル目が見つからない場合は処理を中断する
					break;
				}
				// コピー元セルの取得
//				HSSFCell sourceCell = sourceRow.getCell(i);
				XSSFCell sourceCell = sourceRow.getCell(i);
				if (sourceCell == null) {
					// セル情報がない場合はContinue
					continue;
				}
				// セルスタイルの複製
//				HSSFCell copyCell = copyRow.createCell(i);
				XSSFCell copyCell = copyRow.createCell(i);
			// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
				copyCell.setCellStyle(sourceCell.getCellStyle());
			}
		} catch (Exception e) {
			StringBuilder errMsgSB = new StringBuilder("行に属する列すべての書式をコピー中に例外が発生。");
			errMsgSB.append("sourceRowIdx=" + sourceRowIdx);
			errMsgSB.append(",copyRowIdx=" + copyRowIdx);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}

	/**
	 * プリント範囲の設定を行う。
	 *
	 * @param startRow 印刷開始行
	 * @param endRow 印刷終了行
	 * @param startColumn 印刷開始列
	 * @param endColumn 印刷終了列
	 * @param fitWidth 拡大縮小サイズ横
	 * @param fitHeigth 拡大縮小サイズ縦
	 */
	public void setPringArea(int startRow, int endRow, int startColumn, int endColumn, short fitWidth, short fitHeigth) {
		try {
			getWorkbook().setPrintArea(getWorkSheetIdx(), startColumn, endColumn, startRow, endRow);
			getWorkSheet().setAutobreaks(true);
			// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//			HSSFPrintSetup printSetup = getWorkSheet().getPrintSetup();
			XSSFPrintSetup printSetup = getWorkSheet().getPrintSetup();
			// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
			printSetup.setFitWidth(fitWidth);
			printSetup.setFitHeight(fitHeigth);
		} catch (Exception e) {
			StringBuilder errMsgSB = new StringBuilder("プリント範囲の設定中に例外が発生。");
			errMsgSB.append("startRow=" + startRow);
			errMsgSB.append(",endRow=" + endRow);
			errMsgSB.append(",startColumn=" + startColumn);
			errMsgSB.append(",endColumn=" + endColumn);
			errMsgSB.append(",fitWidth=" + fitWidth);
			errMsgSB.append(",fitHeigth=" + fitHeigth);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}

	/**
	 * SUM関数をセットする。
	 *
	 * @param targetRowIdx セット対象の行
	 * @param targetColIdx セット対象の列
	 * @param fromRowIdx 集計範囲FROMの行インデックス
	 * @param fromColIdx 集計範囲FROMの列インデックス
	 * @param toRowIdx 集計範囲TOの行インデックス
	 * @param toColIdx 集計範囲TOの列インデックス
	 */
	public void setSumFormula(int targetRowIdx, int targetColIdx, int fromRowIdx, int fromColIdx, int toRowIdx, int toColIdx) {
		try {
			// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//			HSSFRow targetRow = getWorkSheet().getRow(targetRowIdx);
//			HSSFCell targetCell = targetRow.getCell(targetColIdx);
			XSSFRow targetRow = getWorkSheet().getRow(targetRowIdx);
			XSSFCell targetCell = targetRow.getCell(targetColIdx);
			// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
			final String fromCellStr = getColumnNameString(fromColIdx) + (fromRowIdx + 1);
			final String toCellStr = getColumnNameString(toColIdx) + (toRowIdx + 1);
			final String sumFormula = "SUM(" + fromCellStr + ":" + toCellStr + ")";
			final String countFormula = "COUNT(" + fromCellStr + ":" + toCellStr + ")";
			targetCell.setCellFormula("IF(" + countFormula + ">0," + sumFormula + ",\"\")");
		} catch (Exception e) {
			StringBuilder errMsgSB = new StringBuilder("SUM関数をセット中に例外が発生。");
			errMsgSB.append("targetRowIdx=" + targetRowIdx);
			errMsgSB.append(",targetColIdx=" + targetColIdx);
			errMsgSB.append(",fromRowIdx=" + fromRowIdx);
			errMsgSB.append(",fromColIdx=" + fromColIdx);
			errMsgSB.append(",toRowIdx=" + toRowIdx);
			errMsgSB.append(",toColIdx=" + toColIdx);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}

	/**
	 * SUM関数をセットする。
	 *
	 * @param targetRowIdx セット対象の行
	 * @param targetColIdx セット対象の列
	 * @param fromRowIdx 集計範囲FROMの行インデックス
	 * @param fromColIdx 集計範囲FROMの列インデックス
	 * @param toRowIdx 集計範囲TOの行インデックス
	 * @param toColIdx 集計範囲TOの列インデックス
	 * @param target 検索対象
	 * @param targetFromRowIdx 検索対象範囲FROMの行インデックス
	 * @param targetFromColIdx 検索対象範囲FROMの列インデックス
	 * @param targetToRowIdx 検索対象範囲TOの行インデックス
	 * @param targetToColIdx 検索対象範囲TOの列インデックス
	 */
	public void setSumFormula(int targetRowIdx, int targetColIdx, int fromRowIdx, int fromColIdx, int toRowIdx, int toColIdx, String target, int targetFromRowIdx,
		int targetFromColIdx, int targetToRowIdx, int targetToColIdx) {

		StringBuilder sb = new StringBuilder();
		try {
			// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//			HSSFRow targetRow = getWorkSheet().getRow(targetRowIdx);
//			HSSFCell targetCell = targetRow.getCell(targetColIdx);
			XSSFRow targetRow = getWorkSheet().getRow(targetRowIdx);
			XSSFCell targetCell = targetRow.getCell(targetColIdx);
			// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
			final String fromCellStr = getColumnNameString(fromColIdx) + (fromRowIdx + 1);
			final String toCellStr = getColumnNameString(toColIdx) + (toRowIdx + 1);
			final String targetFromCellStr = getColumnNameString(targetFromColIdx) + (targetFromRowIdx + 1);
			final String targetToCellStr = getColumnNameString(targetToColIdx) + (targetToRowIdx + 1);
			sb.append("IF(COUNT(");
			sb.append(fromCellStr + ":" + toCellStr + ")>0,");
			sb.append("SUMIF(");
			sb.append(targetFromCellStr + ":" + targetToCellStr + ",");
			sb.append("\"" + target + "\",");
			sb.append(fromCellStr + ":" + toCellStr + "),\"\")");
			targetCell.setCellFormula("" + sb.toString());
		} catch (Exception e) {

			StringBuilder errMsgSB = new StringBuilder("SUM関数をセット中に例外が発生。" + sb.toString());
			errMsgSB.append("targetRowIdx=" + targetRowIdx);
			errMsgSB.append(",targetColIdx=" + targetColIdx);
			errMsgSB.append(",fromRowIdx=" + fromRowIdx);
			errMsgSB.append(",fromColIdx=" + fromColIdx);
			errMsgSB.append(",toRowIdx=" + toRowIdx);
			errMsgSB.append(",toColIdx=" + toColIdx);
			errMsgSB.append(",target=" + target);
			errMsgSB.append(",targetFromRowIdx=" + targetFromRowIdx);
			errMsgSB.append(",targetFromColIdx=" + targetFromColIdx);
			errMsgSB.append(",targetToRowIdx=" + targetToRowIdx);
			errMsgSB.append(",targetToColIdx=" + targetToColIdx);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}

	/**
	 * 指定された列を非表示にする。
	 *
	 * @param colIdx 列番号
	 */
	public void setColumnHidden(int colIdx) {
		try {
			// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//			final HSSFSheet sheet = getWorkSheet();
			final XSSFSheet sheet = getWorkSheet();
			// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
			sheet.setColumnHidden(colIdx, true);

		} catch (Exception e) {
			StringBuilder errMsgSB = new StringBuilder("列を非表示中に例外が発生。");
			errMsgSB.append(",colIdx=" + colIdx);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}

	/**
	 * セルを削除する。
	 *
	 * @param rowIdx 行
	 * @param colIdx 列
	 */
	public void removeCell(int rowIdx, int colIdx) {
		try {
			// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//			HSSFRow targetRow = getWorkSheet().getRow(rowIdx);
//			HSSFCell targetCell = targetRow.getCell(colIdx);
			XSSFRow targetRow = getWorkSheet().getRow(rowIdx);
			XSSFCell targetCell = targetRow.getCell(colIdx);
			// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
			if (targetCell == null) {
				return;
			}
			targetRow.removeCell(targetCell);
		} catch (Exception e) {
			StringBuilder errMsgSB = new StringBuilder("セルを削除中に例外が発生。");
			errMsgSB.append(",colIdx=" + colIdx);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}

	/**
	 * セルの書式をコピーする。
	 *
	 * @param sourceRowIdx コピー元の行
	 * @param sourceColIdx コピー元の列
	 * @param copyRowIdx コピー先の行
	 * @param copyColIdx コピー先の列
	 */
	public void copyCellStyle(int sourceRowIdx, int sourceColIdx, int copyRowIdx, int copyColIdx) {
		try {
			// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//			HSSFRow sourceRow = getWorkSheet().getRow(sourceRowIdx);
//			HSSFRow copyRow = getWorkSheet().getRow(copyRowIdx);
			XSSFRow sourceRow = getWorkSheet().getRow(sourceRowIdx);
			XSSFRow copyRow = getWorkSheet().getRow(copyRowIdx);
			// コピー元セルの取得
//			HSSFCell sourceCell = sourceRow.getCell(sourceColIdx);
			XSSFCell sourceCell = sourceRow.getCell(sourceColIdx);
			// セルスタイルの複製
//			HSSFCell copyCell;
			XSSFCell copyCell;
			// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
			copyCell = copyRow.getCell(copyColIdx);
			if (copyCell == null) {
				copyCell = copyRow.createCell(copyColIdx);
			}
			copyCell.setCellStyle(sourceCell.getCellStyle());
		} catch (Exception e) {
			StringBuilder errMsgSB = new StringBuilder("セルの書式をコピー中に例外が発生。");
			errMsgSB.append("sourceRowIdx=" + sourceRowIdx);
			errMsgSB.append(",copyRowIdx=" + copyRowIdx);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}

	/**
	 * セルの値をコピーする。
	 *
	 * @param sourceRowIdx コピー元の行
	 * @param sourceColIdx コピー元の列
	 * @param copyRowIdx コピー先の行
	 * @param copyColIdx コピー先の列
	 */
	public void copyCellValue(int sourceRowIdx, int sourceColIdx, int copyRowIdx, int copyColIdx) {
		try {
			// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//			HSSFRow sourceRow = getWorkSheet().getRow(sourceRowIdx);
//			HSSFRow copyRow = getWorkSheet().getRow(copyRowIdx);
			XSSFRow sourceRow = getWorkSheet().getRow(sourceRowIdx);
			XSSFRow copyRow = getWorkSheet().getRow(copyRowIdx);
			// コピー元セルの取得
//			HSSFCell sourceCell = sourceRow.getCell(sourceColIdx);
			XSSFCell sourceCell = sourceRow.getCell(sourceColIdx);
			// セル値の複製
//			HSSFCell copyCell;
			XSSFCell copyCell;
			copyCell = copyRow.getCell(copyColIdx);
			if (copyCell == null) {
				copyCell = copyRow.createCell(copyColIdx);
			}
			switch (sourceCell.getCellType()) {
//				case HSSFCell.CELL_TYPE_NUMERIC:
				case XSSFCell.CELL_TYPE_NUMERIC:
					copyCell.setCellValue(sourceCell.getNumericCellValue());
					break;
//				case HSSFCell.CELL_TYPE_BOOLEAN:
				case XSSFCell.CELL_TYPE_BOOLEAN:
					copyCell.setCellValue(sourceCell.getBooleanCellValue());
					break;
//				case HSSFCell.CELL_TYPE_FORMULA:
				case XSSFCell.CELL_TYPE_FORMULA:
					copyCell.setCellFormula(sourceCell.getCellFormula());
					break;
//				case HSSFCell.CELL_TYPE_STRING:
//				case HSSFCell.CELL_TYPE_BLANK:
//				case HSSFCell.CELL_TYPE_ERROR:
				case XSSFCell.CELL_TYPE_STRING:
				case XSSFCell.CELL_TYPE_BLANK:
				case XSSFCell.CELL_TYPE_ERROR:
			// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
				default:
					copyCell.setCellValue(sourceCell.getRichStringCellValue());
					break;
			}
		} catch (Exception e) {
			StringBuilder errMsgSB = new StringBuilder("セルの書式をコピー中に例外が発生。");
			errMsgSB.append("sourceRowIdx=" + sourceRowIdx);
			errMsgSB.append(",copyRowIdx=" + copyRowIdx);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}

	/**
	 * セルを結合する。
	 *
	 * @param firstRow 開始行
	 * @param lastRow 終了行
	 * @param firstCol 開始列
	 * @param lastCol 終了列
	 */
	public void rangeCell(int firstRow, int lastRow, int firstCol, int lastCol) {
		try {
			getWorkSheet().addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
		} catch (Exception e) {
			StringBuilder errMsgSB = new StringBuilder("セルを結合中に例外が発生。");
			errMsgSB.append("firstRow=" + firstRow);
			errMsgSB.append(",lastRow=" + lastRow);
			errMsgSB.append(",firstCol=" + firstCol);
			errMsgSB.append(",lastCol=" + lastCol);
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsgSB.toString()), e);
		}
	}

	// add Start 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
	/**
	 * 指定列の幅を設定する
	 *
	 * @param rowIdx 行インデックス
	 * @param colWidth 列幅
	 */
	public void setColumnWidth(int colIdx, int colWidth) {
		// mod Start 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
		//final HSSFSheet sheet = getWorkSheet();
		final XSSFSheet sheet = getWorkSheet();
		// mod End 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応


		sheet.setColumnWidth(colIdx, colWidth * 512);
	}
	// add End 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能


}
