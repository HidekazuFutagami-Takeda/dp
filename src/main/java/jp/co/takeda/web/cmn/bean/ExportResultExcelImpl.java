package jp.co.takeda.web.cmn.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jp.co.takeda.a.exp.LogicalException;

/**
 * ExportResultのExcelファイル出力実装
 *
 * @author stakeuchi
 */
public class ExportResultExcelImpl implements ExportResult {

	/**
	 * ZIPファイル名
	 */
	private final String zipFileName;

	/**
	 * 出力対象のワークブック格納MAP<br>
	 * キー＝Excelファイル名, 値＝Excelワークブック
	 */
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//	private Map<String, HSSFWorkbook> workBookMap = new LinkedHashMap<String, HSSFWorkbook>();
	private Map<String, XSSFWorkbook> workBookMap = new LinkedHashMap<String, XSSFWorkbook>();
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
	// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応

	/**
	 * コンストラクタ
	 *
	 * @param zipFileName 圧縮ファイル名称
	 */
	public ExportResultExcelImpl(final String zipFileName) {
		this.zipFileName = zipFileName;
	}

	/**
	 * コンストラクタ
	 *
	 * @param excelFileName Excelファイル名
	 * @param excelWorkBook Excelワークブック
	 */
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//	public ExportResultExcelImpl(final String excelFileName, final HSSFWorkbook excelWorkBook) {
	public ExportResultExcelImpl(final String excelFileName, final XSSFWorkbook excelWorkBook) {
	// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		this.zipFileName = null;
		this.workBookMap.put(excelFileName, excelWorkBook);
	}

	/**
	 * 出力対象のワークブックを追加する。
	 *
	 * @param excelFileName Excelファイル名
	 * @param excelWorkBook Excelワークブック
	 */
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//	public void addWorkBook(final String excelFileName, final HSSFWorkbook excelWorkBook) {
	public void addWorkBook(final String excelFileName, final XSSFWorkbook excelWorkBook) {
	// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		workBookMap.put(excelFileName, excelWorkBook);
	}

	/**
	 * ファイル名称を返す。
	 *
	 * @return ファイル名称
	 * @see jp.co.takeda.web.cmn.bean.ExportResult#getName()
	 */
	public String getName() {
		// ZIPファイルの場合
		if (StringUtils.isNotEmpty(zipFileName)) {
			if (StringUtils.contains(zipFileName, ".zip")) {
				return zipFileName;
			} else {
				return zipFileName + ".zip";
			}
		}
		// Excelファイルの場合
		else {
			final String excelFileName = workBookMap.keySet().iterator().next();
			// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//			if (StringUtils.contains(excelFileName, ".xls")) {
			if (StringUtils.contains(excelFileName, ".xlsx")) {
			// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
				return excelFileName;
			} else {
				// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//				return excelFileName + ".xls";
				return excelFileName + ".xlsx";
				// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
			}
		}
	}

	/**
	 * 出力対象のワークブック格納MAPを返却
	 */
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//	public Map<String, HSSFWorkbook> getWorkBookMap() {
	public Map<String, XSSFWorkbook> getWorkBookMap() {
	// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		return this.workBookMap;
	}

	/**
	 * 出力対象のストリームにEXCELデータを出力する。
	 *
	 * @param outputStream 出力対象のストリーム
	 * @throws IOException IO例外
	 * @throws LogicalException 論理例外
	 */
	public void export(final OutputStream outputStream) throws IOException {
		// ZIPファイルの場合
		if (StringUtils.isNotEmpty(zipFileName)) {
			if (!workBookMap.isEmpty()) {
				ZipOutputStream zos = null;
				try {
					zos = new ZipOutputStream(outputStream);
					for (Entry<String, XSSFWorkbook> entry : workBookMap.entrySet()) {
						ByteArrayOutputStream baos = null;
						try {
							baos = new ByteArrayOutputStream();
							entry.getValue().write(baos);
							zos.putNextEntry(new ZipEntry(entry.getKey()));
							zos.write(baos.toByteArray(), 0, baos.toByteArray().length);
							zos.closeEntry();
						} finally {
							IOUtils.closeQuietly(baos);
						}
					}
					zos.flush();
				} finally {
					IOUtils.closeQuietly(zos);
				}
			}
		}
		// Excelファイルの場合
		else {
			if (!workBookMap.isEmpty()) {
				for (Entry<String, XSSFWorkbook> entry : workBookMap.entrySet()) {
					entry.getValue().write(outputStream);
					break; // ひとつめのファイルのみ出力
				}
			}
		}
	}

	/**
	 * リソースの開放処理を行う。
	 *
	 * @throws IOException IO例外
	 * @see java.io.Closeable#close()
	 */
	public void close() throws IOException {
		if (workBookMap != null) {
			workBookMap.clear();
			workBookMap = null;
		}
	}
}
