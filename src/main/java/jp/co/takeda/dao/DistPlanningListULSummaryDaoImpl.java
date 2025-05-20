package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.POIBean;
import jp.co.takeda.model.DistPlanningListULSummary;

/**
 * 計画立案準備の営業所計画アップロードファイルサマリー情報を取得するDAO実装クラス
 *
 * @author Takahashi
 */
@Repository("distPlanningListULSummaryDao")
public class DistPlanningListULSummaryDaoImpl implements DistPlanningListULSummaryDao {

	// 営業所計画アップロードファイルから営業所計画サマリー情報リストを取得
	public List<DistPlanningListULSummary> excelList(InputStream fileIn) throws DataNotFoundException {
		try {
			// アップロードファイルからデータ取得
			// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//			POIFSFileSystem poiFS = new POIFSFileSystem(fileIn);
//			HSSFWorkbook workBook = new HSSFWorkbook(poiFS);
			XSSFWorkbook workBook = new XSSFWorkbook(fileIn);
			POIBean poiBean = new POIBean(workBook);
//			HSSFSheet sheet = poiBean.getWorkSheet();
			XSSFSheet sheet = poiBean.getWorkSheet();
			// 最大行
			int rowMax = sheet.getLastRowNum() -1;
			// ヘッダ項目数
			int colCnt = 0;
//			HSSFRow headRow = sheet.getRow(0);
//			HSSFCell headCell = headRow.getCell(colCnt);
			XSSFRow headRow = sheet.getRow(0);
			XSSFCell headCell = headRow.getCell(colCnt);
			while(headCell != null) {
				if (headCell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
			// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
					//ヘッダが空白の場合、ヘッダ項目なしと判断する
					break;
				}
				colCnt++;
				headCell = headRow.getCell(colCnt);
			}
			//ヘッダ項目なしの列までカウントしたので１引く
			colCnt = colCnt - 1;

			List<DistPlanningListULSummary> distPlanningListULSummaryList = new ArrayList<DistPlanningListULSummary>();

	        if (rowMax == 0) {
	        	//値がない場合
	        	return distPlanningListULSummaryList = null;
	        }
	        if (colCnt != 5) {
	        	//営業所計画アップロード用ファイルと項目数が異なる
	        	return distPlanningListULSummaryList = null;
	        }

			for (int rowIdx = 2; rowIdx - 2 < rowMax; rowIdx++) {

				// 列インデックス
				int colIdx = 0;

				// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//				HSSFRow row = sheet.getRow(rowIdx);
//				HSSFCell cell = row.getCell(colIdx);
				XSSFRow row = sheet.getRow(rowIdx);
				XSSFCell cell = row.getCell(colIdx);
				// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応

				// 支店・営業所コード
				String brDistCode = "";
				if (cell == null) {
					//セルが空白の場合、セル取得でNullPointerExceptionでおちる。
				}else {
					brDistCode = cell.getRichStringCellValue().getString();
				}
				colIdx++;

				// 支店・営業所名
				String brDistName = "";
				cell = row.getCell(colIdx);
				if (cell == null) {
					//セルが空白の場合、セル取得でNullPointerExceptionでおちる
				}else {
					brDistName = cell.getRichStringCellValue().getString();
				}
				colIdx++;

				// 統計品目コード
				String prodCode = "";
				cell = row.getCell(colIdx);
				if (cell == null) {
					//セルが空白の場合、セル取得でNullPointerExceptionでおちる
				}else {
					prodCode = cell.getRichStringCellValue().getString();
				}
				colIdx++;

				// 品目名
				String prodName = "";
				cell = row.getCell(colIdx);
				if (cell == null) {
					//セルが空白の場合、セル取得でNullPointerExceptionでおちる
				}else {
					prodName = cell.getRichStringCellValue().getString();
				}
				colIdx++;

				// 計画値UH(Y・B)
				String plannedValue_UH_T = "";
				cell = row.getCell(colIdx);
				if (cell == null) {
					//セルが空白の場合、セル取得でNullPointerExceptionでおちる
				}else {
					if(cell.getCellType() == 0) {
						//POIが数値型であると判断した場合
						plannedValue_UH_T = BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
						if (plannedValue_UH_T.contains(".0")) {
							//絶対値が小さい場合、小数点がないのに、".0"がつくので削除する
							plannedValue_UH_T = plannedValue_UH_T.replace(".0", "");
						}
					}else {
						//数値にカンマ区切りで入力されることが想定される。
						//数値でないチェックでエラー扱いとなるため、カンマをなくす
						plannedValue_UH_T = cell.getRichStringCellValue().getString().replace(",", "");
					}
				}
				colIdx++;

				// 計画値P(Y・B)
				String plannedValue_P_T = "";
				cell = row.getCell(colIdx);
				if (cell == null) {
					//セルが空白の場合、セル取得でNullPointerExceptionでおちる
				}else {
					if(cell.getCellType() == 0) {
						//POIが数値型であると判断した場合
						plannedValue_P_T = BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
						if (plannedValue_P_T.contains(".0")) {
							//絶対値が小さい場合、小数点がないのに、".0"がつくので削除する
							plannedValue_P_T = plannedValue_P_T.replace(".0", "");
						}
					}else {
						//数値にカンマ区切りで入力されることが想定される。
						//数値でないチェックでエラー扱いとなるため、カンマをなくす
						plannedValue_P_T = cell.getRichStringCellValue().getString().replace(",", "");
					}
				}
				colIdx++;

				//ヘッダ項目数をチェックするようにしたので、コメント削除
//				cell = row.getCell(colIdx);
//				if (cell == null) {
//					//７列目に値がないので、同じ項目数
//				}else {
//					if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
//						//７列目に値がないので、同じ項目数
//					}else {
//						//営業所計画アップロード用ファイルと項目数が異なる
//			        	return distPlanningListULSummaryList = null;
//					}
//				}

				distPlanningListULSummaryList.add(new DistPlanningListULSummary(brDistCode, brDistName, prodCode, prodName, plannedValue_UH_T, plannedValue_P_T, null));
			}

			// ファイル出力実装クラスの生成
			return distPlanningListULSummaryList;

		} catch (IOException e) {
			IOUtils.closeQuietly(fileIn);
			throw new SystemException(new Conveyance(PARAMETER_ERROR, "テンプレートパスが存在しない"));

		} catch (NullPointerException e) {
			IOUtils.closeQuietly(fileIn);
			throw new SystemException(new Conveyance(PARAMETER_ERROR, "空白セルが存在する"));

		} finally {
			IOUtils.closeQuietly(fileIn);
		}
	}

}
