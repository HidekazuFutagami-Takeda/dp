package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
//mod Start 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.bean.POIBean;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.model.DelFacilitiesAndAdjustments;
import jp.co.takeda.model.DpsPlannedCtg;
import jp.co.takeda.model.view.AreaFacilityPersonInChargeAdjustment;
import jp.co.takeda.model.view.AreaPersonInChargeAdjustment;
import jp.co.takeda.model.view.DeletedFacilityPersonInChargeplan;
import jp.co.takeda.model.view.PersonInChargeFacilityAdjustmentList;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.cmn.bean.ExportResultExcelImpl;

/**
 * 削除施設・調整あり計画データファイル(Excelファイル)を生成するロジッククラス
 *
 * @author hfutagami
 */
public class DelFacilitiesAndAdjustmentsListExportLogic {

	/**
	 * テンプレート絶対パス名
	 */
	private final String templatePath;

	/**
	 * システム日付
	 */
	private final Date systemDate;

	/**
	 * 削除施設・調整あり計画データファイルのサマリーリスト
	 */
	private final List<DelFacilitiesAndAdjustments> delFacilitiesAndAdjustmentsList;

	/**
	 * カテゴリ名
	 */
	private final String categoryName;

	/**
	 * 選択されたカテゴリ
	 */
	private final String selectCategory;

	/**
	 * 出力ファイル名
	 */
	private final String outputFileName;

	/**
	 * 仕入れカテゴリ
	 */
	private final String shiireCategory;

	/**
	 * シートフラグ1
	 */
	private final boolean sheet1Flg;

	/**
	 * シートフラグ2
	 */
	private final boolean sheet2Flg;

	/**
	 * シートフラグ3
	 */
	private final boolean sheet3Flg;

	/**
	 * シートフラグ4
	 */
	private final boolean sheet4Flg;

	/**
	 * 計画支援カテゴリ領域マスタ
	 */
	private final List<DpsPlannedCtg> plannedCategory;

	// -------------------------------------
	// 文字列定数
	// -------------------------------------
	/** 出力ファイル名ヘッダ */
	private static final String OUTPUT_FILE_NAME_HEADER = "計画支援フォロー_";

	// -------------------------------------
	// インデックス定数
	// -------------------------------------
	/** [行番号] 明細開始行 */
	private static final int ROW_IDX_START_LIST = 2;

	/** [行番号] ヘッダ行 */
	private static final int ROW_IDX_header = 1;

	/** [列番号] ヘッダ製品名開始列1シート目 */
	private static final int COL_IDX_START_header1 = 4;

	/** [列番号] ヘッダ製品名開始列2シート目 */
	private static final int COL_IDX_START_header2 = 7;

	/** [列番号] ヘッダ製品名開始列3シート目 */
	private static final int COL_IDX_START_header3 = 5;

	/** [列番号] ヘッダ製品名開始列3シート目（仕入） */
	private static final int COL_IDX_START_header3SHIIRE = 4;

	/** [列番号] ヘッダ製品名開始列4シート目 */
	private static final int COL_IDX_START_header4 = 12;

	/** [列番号] 明細開始列1シート目 */
	private static final int COL_IDX_START_LIST1 = 4;

	/** [列番号] 明細開始列2シート目 */
	private static final int COL_IDX_START_LIST2 = 7;

	/** [列番号] 明細開始列3シート目 */
	private static final int COL_IDX_START_LIST3 = 5;

	/** [列番号] 明細開始列3シート目（仕入） */
	private static final int COL_IDX_START_LIST3SHIIRE = 4;

	/** [列番号] 明細開始列4シート目 */
	private static final int COL_IDX_START_LIST4 = 12;

	/** [行番号] 印刷開始行 */
	private static final int ROW_IDX_PRINT_START = 0;

	/** [列番号] 印刷開始列 */
	private static final int COL_IDX_PRINT_START = 0;

	/** [列番号] 印刷終了列 */
	private static final int COL_IDX_PRINT_END = 5;

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * コンストラクタ
	 *
	 * @param templatePath テンプレート絶対パス名
	 * @param systemDate システム日付
	 * @param distPlanningListSummaryList 営業所計画アップロード用のフォーマットファイルのサマリーリスト
	 */
	public DelFacilitiesAndAdjustmentsListExportLogic(String templatePath, Date systemDate
			, List<DelFacilitiesAndAdjustments> delFacilitiesAndAdjustmentsList,String categoryName, String selectCategory, String shiireCategory, List<DpsPlannedCtg> plannedCategory
			, boolean sheet1Flg, boolean sheet2Flg, boolean sheet3Flg, boolean sheet4Flg) {
		this.templatePath = templatePath;
		this.systemDate = (Date) systemDate.clone();
		this.outputFileName = createOutputFileName(systemDate,categoryName);
		this.delFacilitiesAndAdjustmentsList = delFacilitiesAndAdjustmentsList;
		this.categoryName = categoryName;
		this.selectCategory = selectCategory;
		this.shiireCategory = shiireCategory;
		this.plannedCategory = plannedCategory;
		this.sheet1Flg = sheet1Flg;
		this.sheet2Flg = sheet2Flg;
		this.sheet3Flg = sheet3Flg;
		this.sheet4Flg = sheet4Flg;
	}

	/**
	 * 出力ファイル名の生成
	 *
	 * @param systemDate システム日付
	 * @param category カテゴリ名
	 * @return 出力ファイル名
	 */
	private String createOutputFileName(Date systemDate,String category) {
		String fileNm = "";
		String fileNmHeader = OUTPUT_FILE_NAME_HEADER + category;
		try {
			// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//			fileNm = URLEncoder.encode(fileNmHeader, "UTF-8") + "_" + DateUtil.toString(systemDate, "yyyyMMddHHmmss") + ".xls";
			fileNm = URLEncoder.encode(fileNmHeader, "UTF-8") + "_" + DateUtil.toString(systemDate, "yyyyMMddHHmmss") + ".xlsx";
			// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return fileNm;
	}

	/**
	 * 検索結果データのExcelファイルへの出力を実行する。
	 *
	 * @return 出力結果
	 * @throws ValidateException
	 */
	public ExportResult export() throws ValidateException {
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

			if(sheet4Flg == true) {
				write4(workBook);
			}else {
				removeSheet(workBook,3);
			}
			if(sheet3Flg == true) {
				write3(workBook);
			}else {
				removeSheet(workBook,2);
			}
			if(sheet2Flg == true) {
				write2(workBook);
			}else {
				removeSheet(workBook,1);
			}
			if(sheet1Flg == true) {
				write1(workBook);
			}else {
				removeSheet(workBook,0);
			}

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
	//private void write1(HSSFWorkbook workBook) {
	private void write1(XSSFWorkbook workBook) {
	// mod End 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
		POIBean poiBean = new POIBean(workBook);

		// シート指定
		poiBean.setWorkSheetIdx(0);

		// ヘッダ情報のセット
		writeHeadInfo(poiBean,COL_IDX_START_header1);

		// リスト情報のセット
		writeListInfo1(poiBean);
	}

	/**
	 * ワークブックへ検索結果の書き込みを行う。
	 *
	 * @param workBook 書き込み対象のワークブック
	 */
	// mod Start 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
	//private void write2(HSSFWorkbook workBook) {
	private void write2(XSSFWorkbook workBook) {
	// mod End 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
		POIBean poiBean = new POIBean(workBook);

		// シート指定
		poiBean.setWorkSheetIdx(1);

		// ヘッダ情報のセット
		writeHeadInfo(poiBean,COL_IDX_START_header2);

		// リスト情報のセット
		writeListInfo2(poiBean);
	}

	/**
	 * ワークブックへ検索結果の書き込みを行う。
	 *
	 * @param workBook 書き込み対象のワークブック
	 */
	// mod Start 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
	//private void write3(HSSFWorkbook workBook) {
	private void write3(XSSFWorkbook workBook) {
	// mod End 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
		POIBean poiBean = new POIBean(workBook);

		// シート指定
		poiBean.setWorkSheetIdx(2);

		// ヘッダ情報のセット
		if(this.selectCategory.equals(shiireCategory)) {
			//仕入
			writeHeadInfo(poiBean,COL_IDX_START_header3SHIIRE);
		}else {
			//仕入以外
			writeHeadInfo(poiBean,COL_IDX_START_header3);
		}

		// リスト情報のセット
		writeListInfo3(poiBean);
	}

	/**
	 * ワークブックへ検索結果の書き込みを行う。
	 *
	 * @param workBook 書き込み対象のワークブック
	 */
	// mod Start 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
	//private void write4(HSSFWorkbook workBook) {
	private void write4(XSSFWorkbook workBook) {
	// mod End 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
		POIBean poiBean = new POIBean(workBook);

		// シート指定
		poiBean.setWorkSheetIdx(3);

		// ヘッダ情報のセット
		writeHeadInfo(poiBean,COL_IDX_START_header4);

		//リスト情報のセット
		writeListInfo4(poiBean);
	}

	/**
	 * シートの削除を行う。
	 *
	 * @param workBook 書き込み対象のワークブック
	 * @param sheetIdx 削除対象のシートインデックス
	 */
	// mod Start 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
	//private void removeSheet(HSSFWorkbook workBook,int sheetIdx) {
	private void removeSheet(XSSFWorkbook workBook,int sheetIdx) {
	// mod End 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
		POIBean poiBean = new POIBean(workBook);

		// シート削除
		poiBean.removeSheetAt(sheetIdx);

	}

	/**
	 * ヘッダ情報の値をセルに書き込む
	 *
	 * @param poiBean POI設定BEAN
	 */
	private void writeHeadInfo(POIBean poiBean,int setColIdx) {

		// 行インデックス
		int rowIdx = ROW_IDX_header;

		// 列インデックス
		int colIdx = setColIdx;

		for (DelFacilitiesAndAdjustments delFacilitiesAndAdjustments : delFacilitiesAndAdjustmentsList) {
			poiBean.setCellData(delFacilitiesAndAdjustments.getProdName(), rowIdx, colIdx);
			//品目の列幅指定
			poiBean.setColumnWidth(colIdx, delFacilitiesAndAdjustments.getProdName().length() + 1);

			colIdx++;
		}
	}

	/**
	 * リスト情報の値をセルに書き込む1シート目
	 *
	 * @param poiBean POI設定BEAN
	 */
	private void writeListInfo1(POIBean poiBean) {

		String title ="計画支援システム　営業所・担当者調整（営業所）　作成日時：" + DateUtil.toString(systemDate, "yyyy/MM/dd/ HH:mm");
		poiBean.setCellData(title, 0, 0);

		// 行インデックス
		int rowIdx = ROW_IDX_START_LIST;

		// 列インデックス
		int colIdx = COL_IDX_START_LIST1;


		for (DelFacilitiesAndAdjustments delFacilitiesAndAdjustments : delFacilitiesAndAdjustmentsList) {
			for (AreaPersonInChargeAdjustment areaPersonInChargeAdjustment : delFacilitiesAndAdjustments.getAreaPersonInChargeAdjustmentList()) {
				poiBean.setCellData(areaPersonInChargeAdjustment.getSumPlan(), rowIdx, colIdx);

				if(colIdx == COL_IDX_START_LIST1) {
					poiBean.setCellData(areaPersonInChargeAdjustment.getBrCode(), rowIdx, 0);
					poiBean.setCellData(areaPersonInChargeAdjustment.getDistCode(), rowIdx, 1);
					poiBean.setCellData(areaPersonInChargeAdjustment.getBumonRyakuName(), rowIdx, 2);
					poiBean.setCellData(areaPersonInChargeAdjustment.getInsType(), rowIdx, 3);
				}
				rowIdx++;
			}
			rowIdx = ROW_IDX_START_LIST;
			colIdx++;
		}

		int rowCount = delFacilitiesAndAdjustmentsList.get(0).getAreaPersonInChargeAdjustmentList().size();
	    int prodCount = delFacilitiesAndAdjustmentsList.size();

	    for(int i = rowCount - 1; i >= 0;i--) {
		    long sumPlanProdSum = 0;
		    for(int j = 0; j < prodCount;j++) {
		    	sumPlanProdSum = sumPlanProdSum + delFacilitiesAndAdjustmentsList.get(j).getAreaPersonInChargeAdjustmentList().get(i).getSumPlan();
		    }
		    if(sumPlanProdSum == 0) {
		    	//行削除
		    	poiBean.removeRow(i+3, 1);
		    }
	    }

		// 印刷範囲を設定
		final int rowIdxPrintEnd = ROW_IDX_START_LIST + rowIdx;
		final short fitWidth = 1; // 横は1固定
		final short fitHeigth = 0; // 縦は0固定
		poiBean.setPringArea(ROW_IDX_PRINT_START, rowIdxPrintEnd, COL_IDX_PRINT_START, COL_IDX_PRINT_END, fitWidth, fitHeigth);
	}

	/**
	 * リスト情報の値をセルに書き込む2シート目
	 *
	 * @param poiBean POI設定BEAN
	 */
	private void writeListInfo2(POIBean poiBean) {

		String title ="計画支援システム　担当者・施設特約店別計画調整（営業所）　作成日時：" + DateUtil.toString(systemDate, "yyyy/MM/dd/ HH:mm");
		poiBean.setCellData(title, 0, 0);

		// 行インデックス
		int rowIdx = ROW_IDX_START_LIST;

		// 列インデックス
		int colIdx = COL_IDX_START_LIST2;


		for (DelFacilitiesAndAdjustments delFacilitiesAndAdjustments : delFacilitiesAndAdjustmentsList) {
			for (PersonInChargeFacilityAdjustmentList personInChargeFacilityAdjustmentList : delFacilitiesAndAdjustments.getPersonInChargeFacilityAdjustmentList()) {
				poiBean.setCellData(personInChargeFacilityAdjustmentList.getSumPlan(), rowIdx, colIdx);

				if(colIdx == COL_IDX_START_LIST2) {
					poiBean.setCellData(personInChargeFacilityAdjustmentList.getBrCode(), rowIdx, 0);
					poiBean.setCellData(personInChargeFacilityAdjustmentList.getDistCode(), rowIdx, 1);
					poiBean.setCellData(personInChargeFacilityAdjustmentList.getJgiNo().toString(), rowIdx, 2);
					poiBean.setCellData(personInChargeFacilityAdjustmentList.getBumonRyakuName(), rowIdx, 3);
					poiBean.setCellData(personInChargeFacilityAdjustmentList.getJgiName(), rowIdx, 4);
					poiBean.setCellData(personInChargeFacilityAdjustmentList.getInsType(), rowIdx, 5);
					poiBean.setCellData(personInChargeFacilityAdjustmentList.getStatus(), rowIdx, 6);
				}
				rowIdx++;
			}
			rowIdx = ROW_IDX_START_LIST;
			colIdx++;
		}

		int rowCount = delFacilitiesAndAdjustmentsList.get(0).getPersonInChargeFacilityAdjustmentList().size();
	    int prodCount = delFacilitiesAndAdjustmentsList.size();

	    for(int i = rowCount - 1; i >= 0;i--) {
		    long sumPlanProdSum = 0;
		    for(int j = 0; j < prodCount;j++) {
		    	sumPlanProdSum = sumPlanProdSum + delFacilitiesAndAdjustmentsList.get(j).getPersonInChargeFacilityAdjustmentList().get(i).getSumPlan();
		    }
		    if(sumPlanProdSum == 0) {
		    	//行削除
		    	poiBean.removeRow(i+3, 1);
		    }
	    }

		// 印刷範囲を設定
		final int rowIdxPrintEnd = ROW_IDX_START_LIST + rowIdx;
		final short fitWidth = 1; // 横は1固定
		final short fitHeigth = 0; // 縦は0固定
		poiBean.setPringArea(ROW_IDX_PRINT_START, rowIdxPrintEnd, COL_IDX_PRINT_START, COL_IDX_PRINT_END, fitWidth, fitHeigth);
	}

	/**
	 * リスト情報の値をセルに書き込む3シート目
	 *
	 * @param poiBean POI設定BEAN
	 */
	private void writeListInfo3(POIBean poiBean) {

		String title ="計画支援システム　営業所・施設特約店別調整（営業所）　作成日時：" + DateUtil.toString(systemDate, "yyyy/MM/dd/ HH:mm");
		poiBean.setCellData(title, 0, 0);

		// 行インデックス
		int rowIdx = ROW_IDX_START_LIST;

		// 列インデックス
		int colIdx = COL_IDX_START_LIST3;
		if(this.selectCategory.equals(shiireCategory)) {
			colIdx = COL_IDX_START_LIST3SHIIRE;
		}

		for (DelFacilitiesAndAdjustments delFacilitiesAndAdjustments : delFacilitiesAndAdjustmentsList) {
			for (AreaFacilityPersonInChargeAdjustment areaFacilityPersonInChargeAdjustmentList : delFacilitiesAndAdjustments.getAreaFacilityPersonInChargeAdjustmentList()) {
				poiBean.setCellData(areaFacilityPersonInChargeAdjustmentList.getSumPlan(), rowIdx, colIdx);

				if((colIdx == COL_IDX_START_LIST3 && !this.selectCategory.equals(shiireCategory))
			        || (colIdx == COL_IDX_START_LIST3SHIIRE && this.selectCategory.equals(shiireCategory))
				) {
					poiBean.setCellData(areaFacilityPersonInChargeAdjustmentList.getBrCode(), rowIdx, 0);
					poiBean.setCellData(areaFacilityPersonInChargeAdjustmentList.getDistCode(), rowIdx, 1);
					poiBean.setCellData(areaFacilityPersonInChargeAdjustmentList.getBumonRyakuName(), rowIdx, 2);
					poiBean.setCellData(areaFacilityPersonInChargeAdjustmentList.getInsType(), rowIdx, 3);
					if(!this.selectCategory.equals(shiireCategory)) {
						poiBean.setCellData(areaFacilityPersonInChargeAdjustmentList.getStatus(), rowIdx, 4);
					}
				}
				rowIdx++;
			}
			rowIdx = ROW_IDX_START_LIST;
			colIdx++;
		}

		int rowCount = delFacilitiesAndAdjustmentsList.get(0).getAreaFacilityPersonInChargeAdjustmentList().size();
	    int prodCount = delFacilitiesAndAdjustmentsList.size();

	    for(int i = rowCount - 1; i >= 0;i--) {
		    long sumPlanProdSum = 0;
		    for(int j = 0; j < prodCount;j++) {
		    	sumPlanProdSum = sumPlanProdSum + delFacilitiesAndAdjustmentsList.get(j).getAreaFacilityPersonInChargeAdjustmentList().get(i).getSumPlan();
		    }
		    if(sumPlanProdSum == 0) {
		    	//行削除
		    	poiBean.removeRow(i+3, 1);
		    }
	    }

		// 印刷範囲を設定
		final int rowIdxPrintEnd = ROW_IDX_START_LIST + colIdx;
		final short fitWidth = 1; // 横は1固定
		final short fitHeigth = 0; // 縦は0固定
		poiBean.setPringArea(ROW_IDX_PRINT_START, rowIdxPrintEnd, COL_IDX_PRINT_START, COL_IDX_PRINT_END, fitWidth, fitHeigth);
	}

	/**
	 * リスト情報の値をセルに書き込む4シート目
	 *
	 * @param poiBean POI設定BEAN
	 */
	private void writeListInfo4(POIBean poiBean) {

		String title ="計画支援システム　削除施設・施設特約店別計画（営業所）　作成日時：" + DateUtil.toString(systemDate, "yyyy/MM/dd/ HH:mm");
		poiBean.setCellData(title, 0, 0);

		// 行インデックス
		int rowIdx = ROW_IDX_START_LIST;

		// 列インデックス
		int colIdx = COL_IDX_START_LIST4;


		for (DelFacilitiesAndAdjustments delFacilitiesAndAdjustments : delFacilitiesAndAdjustmentsList) {
			for (DeletedFacilityPersonInChargeplan deletedFacilityPersonInChargeplanList : delFacilitiesAndAdjustments.getDeletedFacilityPersonInChargeplanList()) {
				poiBean.setCellData(deletedFacilityPersonInChargeplanList.getSumPlan(), rowIdx, colIdx);

				if(colIdx == COL_IDX_START_LIST4) {
					poiBean.setCellData(deletedFacilityPersonInChargeplanList.getBrCode(), rowIdx, 0);
				    poiBean.setCellData(deletedFacilityPersonInChargeplanList.getDistCode(), rowIdx, 1);
				    poiBean.setCellData(deletedFacilityPersonInChargeplanList.getJgiNo().toString(), rowIdx, 2);
				    poiBean.setCellData(deletedFacilityPersonInChargeplanList.getInsType(), rowIdx, 3);
				    poiBean.setCellData(deletedFacilityPersonInChargeplanList.getInsNo(), rowIdx, 4);
				    poiBean.setCellData(deletedFacilityPersonInChargeplanList.getTmsTytenCd(), rowIdx, 5);
				    poiBean.setCellData(deletedFacilityPersonInChargeplanList.getDelFlg().toString(), rowIdx, 6);
				    poiBean.setCellData(deletedFacilityPersonInChargeplanList.getReqFlg().toString(), rowIdx, 7);
				    poiBean.setCellData(deletedFacilityPersonInChargeplanList.getBumonRyakuName(), rowIdx, 8);
				    poiBean.setCellData(deletedFacilityPersonInChargeplanList.getJgiName(), rowIdx, 9);
				    //mod Start 2022/9/14 H.Futagami 本番障害対応No.30　計画支援フォロー修正（SCTASK1718778）
				    //poiBean.setCellData(deletedFacilityPersonInChargeplanList.getInsFormalName(), rowIdx, 10);
				    poiBean.setCellData(deletedFacilityPersonInChargeplanList.getInsAbbrName(), rowIdx, 10);
				    //mod End 2022/9/14 H.Futagami 本番障害対応No.30　計画支援フォロー修正（SCTASK1718778）
				    poiBean.setCellData(deletedFacilityPersonInChargeplanList.getTmsName(), rowIdx, 11);

				}
				rowIdx++;
			}
			rowIdx = ROW_IDX_START_LIST;
			colIdx++;
		}

		int rowCount = delFacilitiesAndAdjustmentsList.get(0).getDeletedFacilityPersonInChargeplanList().size();
	    int prodCount = delFacilitiesAndAdjustmentsList.size();

	    for(int i = rowCount - 1; i >= 0;i--) {
		    long sumPlanProdSum = 0;
		    for(int j = 0; j < prodCount;j++) {
		    	sumPlanProdSum = sumPlanProdSum + delFacilitiesAndAdjustmentsList.get(j).getDeletedFacilityPersonInChargeplanList().get(i).getSumPlan();
		    }
		    if(sumPlanProdSum == 0) {
		    	//行削除
		    	poiBean.removeRow(i+3, 1);
		    }
	    }

		// 印刷範囲を設定
		final int rowIdxPrintEnd = ROW_IDX_START_LIST + colIdx;
		final short fitWidth = 1; // 横は1固定
		final short fitHeigth = 0; // 縦は0固定
		poiBean.setPringArea(ROW_IDX_PRINT_START, rowIdxPrintEnd, COL_IDX_PRINT_START, COL_IDX_PRINT_END, fitWidth, fitHeigth);
	}

}
