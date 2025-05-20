package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.POIBean;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.view.InsWsPlanForVacSummary;
import jp.co.takeda.model.view.InsWsPlanForVacSummaryByIns;
import jp.co.takeda.model.view.InsWsPlanForVacSummaryByInsTyten;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.util.MathUtil;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.cmn.bean.ExportResultExcelImpl;

/**
 * (ワ)施設計画市区郡町村別集計結果のExcelファイルを生成するロジッククラス
 *
 * @author stakeuchi
 */
public class InsWsCityListForVacExportLogic {

	/**
	 * テンプレート絶対パス名
	 */
	private final String templatePath;

	/**
	 * システム日付
	 */
	private final Date systemDate;

	/**
	 * 計画対象品目リスト
	 */
	private final List<PlannedProd> plannedProdList;

	/**
	 * (ワ)施設特約店別計画の市区町村別 集計MAP<br>
	 * キー=[都道府県コード＋JIS市区町村コード] 値＝(ワ)施設特約店別計画の市区町村別 集計LIST
	 */
	private final Map<String, List<InsWsPlanForVacSummary>> summaryMap;

	/**
	 * (ワ)施設特約店別計画の施設別集計
	 */
	private final List<InsWsPlanForVacSummaryByIns> summaryByInsList;

	// add Start 2022/12/1  Y.Taniguchi バックログNo.31
	/**
	 * (ワ)施設特約店別計画の施設特約店別集計
	 */
	private final List<InsWsPlanForVacSummaryByInsTyten> summaryByInsTytenList;
	// add End 2022/12/1  Y.Taniguchi バックログNo.31

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 組織情報DAO
	 */
	private final SosMstDAO sosMstDAO;

	/**
	 * 従業員情報DAO
	 */
	private final JgiMstDAO jgiMstDAO;

	/**
	 * 出力ファイル名
	 */
	private final String outputFileName;

	/**
	 * 表示品目数の差分(MAX-表示品目数）
	 */
	private final int offProdNumber;

	// -------------------------------------
	// 文字列定数
	// -------------------------------------
	/** 出力ファイル名ヘッダ */
	private static final String OUTPUT_FILE_NAME_HEADER = "INS_PLAN";

	/** 文字列「前期」 */
	private static final String TEXT_ADVANCE_PERIOD = "前期";

	/** 文字列「理論計画」 */
	private static final String TEXT_DIST_VALUE = "理論計画";

	/** 文字列「翌期計画」 */
	private static final String TEXT_PLANNED_VALUE = "翌期計画";

	// -------------------------------------
	// インデックス定数
	// -------------------------------------
	/** [シート番号] 市区町村別集計 */
	private static final int SHEET_IDX_PRF_LIST = 0;

	/** [シート番号] 施設別集計 */
	private static final int SHEET_IDX_INS_LIST = 1;

	// add Start 2022/12/1  Y.Taniguchi バックログNo.31
	/** [シート番号] 施設特約店別集計 */
	private static final int SHEET_IDX_INS_LIST_TYTEN = 2;
	// add End 2022/12/1  Y.Taniguchi バックログNo.31

	/** [行番号] 表示条件 */
	private static final int ROW_IDX_SOS_JGI = 1;

	/** [列番号] 表示条件 */
	private static final int COL_IDX_SOS_JGI = 0;

	/** [行番号] 現在日付 */
	private static final int ROW_IDX_DATE = 0;

	/** [列番号] 現在日付 */
	// mod Start // add Start 2023/1/26  Y.Taniguchi バックログNo.31追加要望

	// add Start 2022/12/1  Y.Taniguchi バックログNo.31
	//private static final int COL_IDX_DATE = 16;
	private static final int COL_IDX_DATE_PRF = 16;
//	private static final int COL_IDX_DATE_INS = 17;
//	private static final int COL_IDX_DATE_INS_TYTEN = 19;
	// add End 2022/12/1  Y.Taniguchi バックログNo.31

	private static final int COL_IDX_DATE_INS = 24;
	private static final int COL_IDX_DATE_INS_TYTEN = 26;
	// mod End // add Start 2023/1/26  Y.Taniguchi バックログNo.31追加要望
	/** [行番号] 品目名開始行 */
	private static final int ROW_IDX_PROD_NAME = 2;

	/** [列番号] 品目名開始列 */
	// mod Start // add Start 2023/1/26  Y.Taniguchi バックログNo.31追加要望

	// add Start 2022/12/1  Y.Taniguchi バックログNo.31
	//	private static final int COL_IDX_PROD_NAME = 3;
	private static final int COL_IDX_PROD_NAME_PRF = 3;
//	private static final int COL_IDX_PROD_NAME_INS = 4;
//	private static final int COL_IDX_PROD_NAME_INS_TYTEN = 6;
	// add End 2022/12/1  Y.Taniguchi バックログNo.31

	private static final int COL_IDX_PROD_NAME_INS = 11;
	private static final int COL_IDX_PROD_NAME_INS_TYTEN = 13;
	// mod End // add Start 2023/1/26  Y.Taniguchi バックログNo.31追加要望

	/** [行番号] 担当者計開始行 */
	private static final int ROW_IDX_SUM_MR = 3;

	/** [行番号] 一般先開始行 */
	private static final int ROW_IDX_SUM_IPPAN = 6;

	/** [行番号] 重点先開始行 */
	private static final int ROW_IDX_SUM_JTN = 9;

	/** [列番号] 計列 */
	// mod Start // add Start 2023/1/20  Y.Taniguchi バックログNo.31追加要望
	// add Start 2022/12/1  Y.Taniguchi バックログNo.31
	//private static final int COL_IDX_SUM = 2;
//	private static final int COL_IDX_Center = 1;
	private static final int COL_IDX_SUM_PRF = 2;
//	private static final int COL_IDX_SUM_INS = 3;
//	private static final int COL_IDX_SUM_INS_TYTEN= 5;
	// add End 2022/12/1  Y.Taniguchi バックログNo.31
	private static final int COL_IDX_Center = 8;
	private static final int COL_IDX_SUM_INS = 10;
	private static final int COL_IDX_SUM_INS_TYTEN= 12;
	// mod End // add Start 2023/1/20  Y.Taniguchi バックログNo.31追加要望

	/** [行番号] 明細開始行 */
	private static final int ROW_IDX_START_LIST = 12;

	/** [列番号] 明細開始列 */
	private static final int COL_IDX_START_LIST = 0;

	/** [行番号] 印刷開始行 */
	private static final int ROW_IDX_PRINT_START = 0;

	/** [列番号] 印刷開始列 */
	private static final int COL_IDX_PRINT_START = 0;

	/** [列番号] 印刷終了列 */
	// mod Start // add Start 2023/1/26  Y.Taniguchi バックログNo.31追加要望

	// add Start 2022/12/1  Y.Taniguchi バックログNo.31
	private static final int COL_IDX_PRINT_END_PRF = 17;
//	private static final int COL_IDX_PRINT_END_INS = 18;
//	private static final int COL_IDX_PRINT_END_INS_TYTEN = 20;
	// add End 2022/12/1  Y.Taniguchi バックログNo.31

	private static final int COL_IDX_PRINT_END_INS = 25;
	private static final int COL_IDX_PRINT_END_INS_TYTEN = 27;
	// mod End // add Start 2023/1/26  Y.Taniguchi バックログNo.31追加要望

	// -------------------------------------
	// その他定数
	// -------------------------------------
	/**
	 * 品目最大数(テンプレート設定最大数)
	 */
	private static final int PROD_MAX_SIZE = 15;

	/**
	 * コンストラクタ
	 *
	 * @param templatePath テンプレート絶対パス名
	 * @param systemDate システム日付
	 * @param plannedProdList 計画対象品目リスト
	 * @param summaryList (ワ)施設特約店別計画の市区町村別 集計LIST
	 * @param summaryByInsList (ワ)施設特約店別計画の施設別 集計LIST
	 * @param jgiNo 従業員番号
	 * @param sosMstDAO 組織情報DAO
	 * @param jgiMstDAO 従業員情報DAO
	 */
	// add Start 2022/12/1  Y.Taniguchi バックログNo.31
	//public InsWsCityListForVacExportLogic(String templatePath, Date systemDate, List<PlannedProd> plannedProdList, List<InsWsPlanForVacSummary> summaryList,
		//List<InsWsPlanForVacSummaryByIns> summaryByInsList, Integer jgiNo, SosMstDAO sosMstDAO, JgiMstDAO jgiMstDAO) {
	public InsWsCityListForVacExportLogic(String templatePath, Date systemDate, List<PlannedProd> plannedProdList, List<InsWsPlanForVacSummary> summaryList,
			List<InsWsPlanForVacSummaryByIns> summaryByInsList, List<InsWsPlanForVacSummaryByInsTyten> summaryByInsTytenList, Integer jgiNo, SosMstDAO sosMstDAO, JgiMstDAO jgiMstDAO) {
	// add End 2022/12/1  Y.Taniguchi バックログNo.31
		// ----------------------
		// 引数チェック
		// ----------------------
		if (StringUtils.isEmpty(templatePath)) {
			final String errMsg = "テンプレート絶対パス名がNullまたは空";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (systemDate == null) {
			final String errMsg = "システム日付がNull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null) {
			final String errMsg = "計画品目情報がNull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (jgiNo == null) {
			final String errMsg = "従業員番号がNull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosMstDAO == null) {
			final String errMsg = "組織情報DAOがNull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (jgiMstDAO == null) {
			final String errMsg = "従業員情報DAOがNull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// ----------------------
		// フィールドにセット
		// ----------------------
		this.templatePath = templatePath;
		this.systemDate = (Date) systemDate.clone();
		this.plannedProdList = plannedProdList;
		this.summaryMap = createSummaryMap(summaryList);
		this.summaryByInsList = summaryByInsList;
		// add Start 2022/12/1  Y.Taniguchi バックログNo.31
		this.summaryByInsTytenList = summaryByInsTytenList;
		// add End 2022/12/1  Y.Taniguchi バックログNo.31
		this.jgiNo = jgiNo;
		this.sosMstDAO = sosMstDAO;
		this.jgiMstDAO = jgiMstDAO;
		this.outputFileName = createOutputFileName(jgiNo, systemDate);
		this.offProdNumber = PROD_MAX_SIZE - plannedProdList.size();
	}

	/**
	 * 出力ファイル名を生成する。
	 *
	 * @param systemDate システム日付
	 * @return 出力ファイル名
	 */
	private String createOutputFileName(Integer jgiNo, Date systemDate) {
		final String dateTxt = DateUtil.toString(systemDate, "yyyyMMddHHmmss");
		// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//		return OUTPUT_FILE_NAME_HEADER + "_" + jgiNo.toString() + "_" + dateTxt + ".xls";
		return OUTPUT_FILE_NAME_HEADER + "_" + jgiNo.toString() + "_" + dateTxt + ".xlsx";
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
			// 市区町村別集計シートの書込
			write(workBook);
			// 施設別集計シートの書込
			writeInsList(workBook);
			// add Start 2022/12/1  Y.Taniguchi バックログNo.31
			// 施設特約店別集計シートの書込
			writeInsListTyten(workBook);
			// add End 2022/12/1  Y.Taniguchi バックログNo.31
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
	 * ワークブックへ市区町村別集計シートの書き込みを行う。
	 *
	 * @param workBook 書き込み対象のワークブック
	 */
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//	private void write(HSSFWorkbook workBook) {
	private void write(XSSFWorkbook workBook) {
	// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		POIBean poiBean = new POIBean(workBook);
		// 操作対象のシート番号設定
		poiBean.setWorkSheetIdx(SHEET_IDX_PRF_LIST);
		// add Start 2022/12/1  Y.Taniguchi バックログNo.31
		// ヘッダ情報のセット
		//writeHeadInfo(poiBean);
		writeHeadInfo(poiBean, COL_IDX_PROD_NAME_PRF, COL_IDX_DATE_PRF, COL_IDX_PRINT_END_PRF);
		// 不要セルの削除
		//removeCell(poiBean);
		removeCell(poiBean, COL_IDX_PROD_NAME_PRF, COL_IDX_PRINT_END_PRF);
		// リスト情報(合計行)のセット
		//writeListInfoSumRow(poiBean);
		writeListInfoSumRow(poiBean, COL_IDX_SUM_PRF);
		// リスト情報のセット
		//writeListInfo(poiBean);
		writeListInfo(poiBean, COL_IDX_SUM_PRF);
		// add End 2022/12/1  Y.Taniguchi バックログNo.31
	}

	/**
	 * ヘッダ情報の値をセルに書き込む。(市区町村別集計)
	 *
	 * @param poiBean POI設定BEAN
	 */
	// add Start 2022/12/1  Y.Taniguchi バックログNo.31
	//private void writeHeadInfo(POIBean poiBean) {
	private void writeHeadInfo(POIBean poiBean, int colIdxProd, int colIdxDate, int colIdxEnd) {
		// ユーザ情報
		writeHeadInfoUser(poiBean);
		// 現在日付
		//int colDateIdx = COL_IDX_DATE - offProdNumber;
		int colDateIdx = colIdxDate - offProdNumber;
		poiBean.rangeCell(ROW_IDX_DATE, ROW_IDX_DATE, colDateIdx, colDateIdx + 1);
		//poiBean.copyCellStyle(ROW_IDX_DATE, COL_IDX_DATE, ROW_IDX_DATE, colDateIdx);
		poiBean.copyCellStyle(ROW_IDX_DATE, colIdxDate, ROW_IDX_DATE, colDateIdx);
		poiBean.setCellData(systemDate, ROW_IDX_DATE, colDateIdx);
		// 単位
		//int colUnitIdx = COL_IDX_PRINT_END - offProdNumber;
		//poiBean.copyCellStyle(ROW_IDX_SOS_JGI, COL_IDX_PRINT_END, ROW_IDX_SOS_JGI, colUnitIdx);
		//poiBean.copyCellValue(ROW_IDX_SOS_JGI, COL_IDX_PRINT_END, ROW_IDX_SOS_JGI, colUnitIdx);
		int colUnitIdx = colIdxEnd - offProdNumber;
		poiBean.copyCellStyle(ROW_IDX_SOS_JGI, colIdxEnd, ROW_IDX_SOS_JGI, colUnitIdx);
		poiBean.copyCellValue(ROW_IDX_SOS_JGI, colIdxEnd, ROW_IDX_SOS_JGI, colUnitIdx);
		// 品目名
		//int colIdx = COL_IDX_PROD_NAME;
		int colIdx = colIdxProd;
	// add End 2022/12/1  Y.Taniguchi バックログNo.31
		for (PlannedProd plannedProd : plannedProdList) {
			final String prodName = plannedProd.getProdName();
			poiBean.setCellData(prodName, ROW_IDX_PROD_NAME, colIdx++);
		}
	}

	/**
	 * ヘッダ情報のユーザ情報の値をセルに書き込む。
	 *
	 * @param poiBean POI設定BEAN
	 */
	private void writeHeadInfoUser(POIBean poiBean) {
		// 従業員名
		final JgiMst jgiMst = searchJgiMst(jgiNo);
		final String jgiName = jgiMst.getJgiName();
		// 特約店部名
		final SosMst sosMst2 = searchSosMst(jgiMst.getSosCd2());
		final String tytenBName = sosMst2.getBumonSeiName();
		// 特約店G名
		final SosMst sosMst3 = searchSosMst(jgiMst.getSosCd3());
		final String tytenGName = sosMst3.getBumonSeiName();
		// チーム名
		String teamName = "";
		if (!(jgiMst.getSosCd4().equals(JgiMst.BLANK_SOS_CD))) {
			try {
				SosMst team = sosMstDAO.search(jgiMst.getSosCd4());
				teamName = team.getBumonSeiName();
			} catch (DataNotFoundException e) {
				// エラーにしない
			}
		}
		// 組織名・担当者名
		poiBean.setCellData(tytenBName + tytenGName + teamName + "　" + jgiName, ROW_IDX_SOS_JGI, COL_IDX_SOS_JGI);
	}

	/**
	 * リスト情報の値をセルに書き込む。
	 *
	 * @param poiBean POI設定BEAN
	 */
	// add Start 2022/12/1  Y.Taniguchi バックログNo.31
	//private void writeListInfo(POIBean poiBean) {
	private void writeListInfo(POIBean poiBean, int colIdxSum) {
	// add Start 2022/12/1  Y.Taniguchi バックログNo.31
		// 集計MAPがNull,または空の場合はReturn
		if (summaryMap == null || summaryMap.isEmpty()) {
			return;
		}
		// 集計リストのサイズ分行を追加
		final int expandSize = (summaryMap.size() * 3) - 3;
		poiBean.addRows(ROW_IDX_START_LIST, expandSize);

		// 上罫線スタイル
		// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//		HSSFCellStyle topBorderStyle = createTopBorderStyle(poiBean);
		// add Start 2022/12/1  Y.Taniguchi バックログNo.31
		//XSSFCellStyle topBorderStyle = createTopBorderStyle(poiBean);
		// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		XSSFCellStyle topBorderStyle = createTopBorderStyle(poiBean, colIdxSum);
		// add End 2022/12/1  Y.Taniguchi バックログNo.31

		// 行インデックス
		int rowIdx = ROW_IDX_START_LIST;
		for (Entry<String, List<InsWsPlanForVacSummary>> entry : summaryMap.entrySet()) {

			// 列インデックス
			int colIdx = COL_IDX_START_LIST;

			// 市区町村名
			final String cityName = entry.getValue().get(0).getShikuchosonMeiKj();
			poiBean.setCellData(cityName, rowIdx, colIdx++, topBorderStyle);

			// 文字列(前期・理論値・当期計画)
			poiBean.setCellData(TEXT_ADVANCE_PERIOD, rowIdx, colIdx, topBorderStyle);
			poiBean.setCellData(TEXT_DIST_VALUE, rowIdx + 1, colIdx);
			poiBean.setCellData(TEXT_PLANNED_VALUE, rowIdx + 2, colIdx++);

			// 合計関数
			poiBean.setCellData("", rowIdx, colIdx, topBorderStyle);
			setSumRowFormula(poiBean, rowIdx, colIdx);
			setSumRowFormula(poiBean, rowIdx + 1, colIdx);
			setSumRowFormula(poiBean, rowIdx + 2, colIdx++);

			// 品目ループカウント
			int prodCount = 0;
			for (PlannedProd plannedProd : plannedProdList) {
				boolean isProdDataExist = false;
				// 一般先と重点先を合算するため変数外出し
				Long advancePeriod = null;
				Long distValue = null;
				Long plannedValue = null;
				for (InsWsPlanForVacSummary summary : entry.getValue()) {
					if (StringUtils.equals(plannedProd.getProdCode(), summary.getProdCode())) {
						isProdDataExist = true;
						// 前期
						advancePeriod = MathUtil.add(advancePeriod, summary.getAdvancePeriod());
						if (advancePeriod == null) {
							poiBean.setCellData("", rowIdx, colIdx + prodCount, topBorderStyle);
						} else {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(advancePeriod), rowIdx, colIdx + prodCount, topBorderStyle);
						}
						// 理論値
						distValue = MathUtil.add(distValue, summary.getDistValueB());
						poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(distValue), rowIdx + 1, colIdx + prodCount);
						// 当期計画
						plannedValue = MathUtil.add(plannedValue, summary.getPlannedValueB());
						poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(plannedValue), rowIdx + 2, colIdx + prodCount);
					}
				}
				// データが存在しない場合は上罫線のみセット
				if (!isProdDataExist) {
					poiBean.setCellData("", rowIdx, colIdx + prodCount, topBorderStyle);
				}
				prodCount++;
			}
			rowIdx += 3; // 3行分進める
		}
		// 印刷範囲を設定
		final short fitWidth = 1; // 横は1固定
		final short fitHeigth = 0; // 縦は0固定
		poiBean.setPringArea(ROW_IDX_PRINT_START, rowIdx - 1, COL_IDX_PRINT_START, COL_IDX_PRINT_END_PRF - offProdNumber, fitWidth, fitHeigth);
	}

	/**
	 * リスト情報の合計値をセルに書き込む。
	 *
	 * @param poiBean POI設定BEAN
	 */
	// add Start 2022/12/1  Y.Taniguchi バックログNo.31
	//private void writeListInfoSumRow(POIBean poiBean) {
	private void writeListInfoSumRow(POIBean poiBean, int colIdxSum) {
	// add End 2022/12/1  Y.Taniguchi バックログNo.31
		// 集計MAPがNull,または空の場合はReturn
		if (summaryMap == null || summaryMap.isEmpty()) {
			return;
		}
		// 計列の計算式をセット
		// add Start 2022/12/1  Y.Taniguchi バックログNo.31
		setMrRowFormula(poiBean, colIdxSum);
		//setIppanRowFormula(poiBean);
		setIppanRowFormula(poiBean, colIdxSum);
		//setJtnRowFormula(poiBean);
		setJtnRowFormula(poiBean, colIdxSum);
		// add End 2022/12/1  Y.Taniguchi バックログNo.31

		// 各品目列の計算式・合計値をセット
		int prodCount = 1;
		for (PlannedProd plannedProd : plannedProdList) {
			// add Start 2022/12/1  Y.Taniguchi バックログNo.31
			//final int colIdx = COL_IDX_SUM + prodCount;
			final int colIdx = colIdxSum + prodCount;
			// add End 2022/12/1  Y.Taniguchi バックログNo.31
			// 担当者計
			setMrRowFormula(poiBean, colIdx);
			// 一般先・重点先の合計を算出
			Long advancePeriodIppan = null;
			Long distValueIppan = null;
			Long plannedValueIppan = null;
			Long advancePeriodJtn = null;
			Long distValueJtn = null;
			Long plannedValueJtn = null;
			for (Entry<String, List<InsWsPlanForVacSummary>> entry : summaryMap.entrySet()) {
				for (InsWsPlanForVacSummary summary : entry.getValue()) {
					if (StringUtils.equals(plannedProd.getProdCode(), summary.getProdCode())) {
						// 前期
						final Long advancePeriod = summary.getAdvancePeriod();
						// 理論値
						final Long distValue = summary.getDistValueB();
						// 当期計画
						final Long plannedValue = summary.getPlannedValueB();
						// 一般先合計値に加算
						if (summary.getActivityType().equals(ActivityType.IPPAN)) {
							advancePeriodIppan = MathUtil.add(advancePeriodIppan, advancePeriod);
							distValueIppan = MathUtil.add(distValueIppan, distValue);
							plannedValueIppan = MathUtil.add(plannedValueIppan, plannedValue);
						}
						// 重点先合計値に加算
						if (summary.getActivityType().equals(ActivityType.JTN)) {
							advancePeriodJtn = MathUtil.add(advancePeriodJtn, advancePeriod);
							distValueJtn = MathUtil.add(distValueJtn, distValue);
							plannedValueJtn = MathUtil.add(plannedValueJtn, plannedValue);
						}
					}
				}
			}
			poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(advancePeriodIppan), ROW_IDX_SUM_IPPAN, colIdx);
			poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(distValueIppan), ROW_IDX_SUM_IPPAN + 1, colIdx);
			poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(plannedValueIppan), ROW_IDX_SUM_IPPAN + 2, colIdx);
			poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(advancePeriodJtn), ROW_IDX_SUM_JTN, colIdx);
			poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(distValueJtn), ROW_IDX_SUM_JTN + 1, colIdx);
			poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(plannedValueJtn), ROW_IDX_SUM_JTN + 2, colIdx);
			prodCount++;
		}
	}

	/**
	 * 合計行の計算式をセットする。
	 *
	 * @param poiBean POI設定BEAN
	 * @param rowIdx 対象行インデックス
	 * @param colIdx 対象列インデックス
	 */
	private void setSumRowFormula(POIBean poiBean, int rowIdx, int colIdx) {
		poiBean.setSumFormula(rowIdx, colIdx, rowIdx, colIdx + 1, rowIdx, colIdx + plannedProdList.size());
	}

	/**
	 * 担当者合計行の計算式をセットする。
	 *
	 * @param poiBean POI設定BEAN
	 * @param colIdx @param colIdx 対象列インデックス
	 */
	private void setMrRowFormula(POIBean poiBean, int colIdx) {
		// 前期
		setSumColumnFormulaMr(poiBean, ROW_IDX_SUM_MR, colIdx);
		// 理論値
		setSumColumnFormulaMr(poiBean, ROW_IDX_SUM_MR + 1, colIdx);
		// 当期計画
		setSumColumnFormulaMr(poiBean, ROW_IDX_SUM_MR + 2, colIdx);
	}

	/**
	 * 一般先合計行の計算式をセットする。
	 *
	 * @param poiBean POI設定BEAN
	 * @param colIdx @param colIdx 対象列インデックス
	 */
	// add Start 2022/12/1  Y.Taniguchi バックログNo.31
	//private void setIppanRowFormula(POIBean poiBean) {
	private void setIppanRowFormula(POIBean poiBean, int colIdx) {
		// 前期
		//setSumRowFormula(poiBean, ROW_IDX_SUM_IPPAN, COL_IDX_SUM);
		setSumRowFormula(poiBean, ROW_IDX_SUM_IPPAN, colIdx);
		// 理論値
		//setSumRowFormula(poiBean, ROW_IDX_SUM_IPPAN + 1, COL_IDX_SUM);
		setSumRowFormula(poiBean, ROW_IDX_SUM_IPPAN + 1, colIdx);
		// 当期計画
		//setSumRowFormula(poiBean, ROW_IDX_SUM_IPPAN + 2, COL_IDX_SUM);
		setSumRowFormula(poiBean, ROW_IDX_SUM_IPPAN + 2, colIdx);
	// add End 2022/12/1  Y.Taniguchi バックログNo.31
	}

	/**
	 * 重点先合計行の計算式をセットする。
	 *
	 * @param poiBean POI設定BEAN
	 * @param colIdx @param colIdx 対象列インデックス
	 */
	// add Start 2022/12/1  Y.Taniguchi バックログNo.31
	//private void setJtnRowFormula(POIBean poiBean) {
	private void setJtnRowFormula(POIBean poiBean, int colIdx) {
		// 前期
		//setSumRowFormula(poiBean, ROW_IDX_SUM_JTN, COL_IDX_SUM);
		setSumRowFormula(poiBean, ROW_IDX_SUM_JTN, colIdx);
		// 理論値
		//setSumRowFormula(poiBean, ROW_IDX_SUM_JTN + 1, COL_IDX_SUM);
		setSumRowFormula(poiBean, ROW_IDX_SUM_JTN + 1, colIdx);
		// 当期計画
		//setSumRowFormula(poiBean, ROW_IDX_SUM_JTN + 2, COL_IDX_SUM);
		setSumRowFormula(poiBean, ROW_IDX_SUM_JTN + 2, colIdx);
	// add End 2022/12/1  Y.Taniguchi バックログNo.31
	}

	/**
	 * 担当者合計列の計算式をセットする。
	 *
	 * @param poiBean POI設定BEAN
	 * @param rowIdx 対象行インデックス
	 * @param colIdx 対象列インデックス
	 */
	private void setSumColumnFormulaMr(POIBean poiBean, int rowIdx, int colIdx) {
		final String cellStr1 = poiBean.getColumnNameString(colIdx) + (rowIdx + 4);
		final String cellStr2 = poiBean.getColumnNameString(colIdx) + (rowIdx + 7);
		final String sumFormula = "SUM(" + cellStr1 + "," + cellStr2 + ")";
		final String countFormula = "COUNT(" + cellStr1 + "," + cellStr2 + ")";
		final String formula = "IF(" + countFormula + ">0," + sumFormula + ",\"\")";
		poiBean.setCellFormula(formula, rowIdx, colIdx);
	}

	/**
	 * 上罫線のスタイルを生成する。
	 *
	 * @param poiBean POI設定BEAN
	 * @return セルスタイル
	 */
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
	// add Start 2022/12/1  Y.Taniguchi バックログNo.31
//	private HSSFCellStyle createTopBorderStyle(POIBean poiBean) {
	private XSSFCellStyle createTopBorderStyle(POIBean poiBean, int colIdxSum) {
	// add End 2022/12/1  Y.Taniguchi バックログNo.31
//		HSSFRow row = poiBean.getWorkSheet().getRow(ROW_IDX_START_LIST);
//		HSSFCell cell = row.getCell(COL_IDX_SUM);
//		HSSFCellStyle style = poiBean.createCellStyle();
//		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//		style.setBorderBottom(HSSFCellStyle.BORDER_DOTTED);
//		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
//		style.setFont(cell.getCellStyle().getFont(poiBean.getWorkbook()));
		XSSFRow row = poiBean.getWorkSheet().getRow(ROW_IDX_START_LIST);
		XSSFCell cell = row.getCell(colIdxSum);
		XSSFCellStyle style = poiBean.createCellStyle();
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setBorderBottom(XSSFCellStyle.BORDER_DOTTED);
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style.setFont(cell.getCellStyle().getFont());
	// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		style.setDataFormat(cell.getCellStyle().getDataFormat());
		return style;
	}
	// add Start 2022/12/1  Y.Taniguchi バックログNo.31
	/**
	 * 上罫線のスタイル(中央揃え)を生成する。
	 *
	 * @param poiBean POI設定BEAN
	 * @return セルスタイル
	 */
	private XSSFCellStyle createTopBorderStyleCenter(POIBean poiBean, int colIdxSum) {
		XSSFRow row = poiBean.getWorkSheet().getRow(ROW_IDX_START_LIST);
		XSSFCell cell = row.getCell(colIdxSum);
		XSSFCellStyle style = poiBean.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setBorderBottom(XSSFCellStyle.BORDER_DOTTED);
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style.setFont(cell.getCellStyle().getFont());
		style.setDataFormat(cell.getCellStyle().getDataFormat());
		return style;
	}
	// add End 2022/12/1  Y.Taniguchi バックログNo.31

	/**
	 * 集計LISTから集計MAPを生成する。
	 *
	 * @param summaryList 集計LIST
	 * @return 集計MAP [キー=[都道府県コード＋JIS市区町村コード] 値＝(ワ)施設特約店別計画の市区町村別 集計LIST]
	 */
	private Map<String, List<InsWsPlanForVacSummary>> createSummaryMap(List<InsWsPlanForVacSummary> summaryList) {
		Map<String, List<InsWsPlanForVacSummary>> summaryMap = new LinkedHashMap<String, List<InsWsPlanForVacSummary>>();
		if (summaryList != null) {
			for (InsWsPlanForVacSummary summary : summaryList) {
				// キーは[都道府県コード＋JIS市区町村コード]
				final String key = summary.getAddrCodePref() + summary.getAddrCodeCity();
				// MAPに既にリストが追加されている場合はリストに追加
				if (summaryMap.containsKey(key)) {
					summaryMap.get(key).add(summary);
				}
				// MAPに追加されていない場合は新規リストを作成しPUT
				else {
					List<InsWsPlanForVacSummary> newSummaryList = new ArrayList<InsWsPlanForVacSummary>();
					newSummaryList.add(summary);
					summaryMap.put(key, newSummaryList);
				}
			}
		}
		return summaryMap;
	}

	/**
	 * 組織情報を検索する。
	 *
	 * @param sosCd 組織コード
	 * @return 組織情報
	 */
	private SosMst searchSosMst(String sosCd) {
		try {
			return sosMstDAO.search(sosCd);
		} catch (DataNotFoundException e) {
			final String errMsg = "組織取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}
	}

	/**
	 * 従業員情報を検索する。
	 *
	 * @param jgiNo 従業員番号
	 * @return 従業員情報
	 */
	private JgiMst searchJgiMst(Integer jgiNo) {
		try {
			return jgiMstDAO.search(jgiNo);
		} catch (DataNotFoundException e) {
			final String errMsg = "従業員取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}
	}

	/**
	 * ワークブックへ施設別集計シートの書き込みを行う。
	 *
	 * @param workBook 書き込み対象のワークブック
	 */
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//	private void writeInsList(HSSFWorkbook workBook) {
	private void writeInsList(XSSFWorkbook workBook) {
	// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
		POIBean poiBean = new POIBean(workBook);
		// 操作対象のシート番号設定
		poiBean.setWorkSheetIdx(SHEET_IDX_INS_LIST);
		// add Start 2022/12/1  Y.Taniguchi バックログNo.31
		// ヘッダ情報のセット
		//writeHeadInfo(poiBean);
		writeHeadInfo(poiBean, COL_IDX_PROD_NAME_INS, COL_IDX_DATE_INS, COL_IDX_PRINT_END_INS);
		// 不要セルの削除
		//removeCell(poiBean);
		removeCell(poiBean, COL_IDX_PROD_NAME_INS, COL_IDX_PRINT_END_INS);
		// リスト情報(合計行)のセット
		//writeListInfoSumRow(poiBean);
		writeListInfoSumRow(poiBean, COL_IDX_SUM_INS);
		// リスト情報のセット
		//writeListInfoForInsList(poiBean);
		writeListInfoForInsList(poiBean, COL_IDX_SUM_INS);
		// add End 2022/12/1  Y.Taniguchi バックログNo.31
	}

	/**
	 * 施設別集計リスト情報の値をセルに書き込む。
	 *
	 * @param poiBean POI設定BEAN
	 */
	// add Start 2022/12/1  Y.Taniguchi バックログNo.31
	//private void writeListInfoForInsList(POIBean poiBean) {
	private void writeListInfoForInsList(POIBean poiBean, int colIdxSum) {
	// add End 2022/12/1  Y.Taniguchi バックログNo.31

		// (ワ)施設特約店別計画の施設別集計MAPを生成
		Map<String, List<InsWsPlanForVacSummaryByIns>> summaryListMap = createSummaryByInsMap(this.summaryByInsList);
		// 集計ListがNull,または空の場合はReturn
		if (summaryListMap == null || summaryListMap.isEmpty()) {
			return;
		}
		// 集計リストのサイズ分行を追加
		final int expandSize = (summaryListMap.size() * 3) - 3;
		poiBean.addRows(ROW_IDX_START_LIST, expandSize);

		// 上罫線スタイル
		// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//		HSSFCellStyle topBorderStyle = createTopBorderStyle(poiBean);
		// add Start 2022/12/1  Y.Taniguchi バックログNo.31
		//XSSFCellStyle topBorderStyle = createTopBorderStyle(poiBean);
		XSSFCellStyle topBorderStyle = createTopBorderStyle(poiBean, colIdxSum);

		// 上罫線スタイル(中央揃え)
		XSSFCellStyle topBorderStyleCenter = createTopBorderStyleCenter(poiBean, COL_IDX_Center);
		// add End 2022/12/1  Y.Taniguchi バックログNo.31

		// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応

		// add Start 2023/1/26  Y.Taniguchi バックログNo.31追加要望
		// 従業員情報取得
		final JgiMst jgiMst = searchJgiMst(jgiNo);
		// 従業員名
		final String jgiName = jgiMst.getJgiName();

		// 特約店部情報取得
		final SosMst sosMst2 = searchSosMst(jgiMst.getSosCd2());
		// 特約店部名
		final String tytenBName = sosMst2.getBumonSeiName();
		// リージョンコード
		final String brCode = sosMst2.getBrCode();

		//特約店G情報取得
		final SosMst sosMst3 = searchSosMst(jgiMst.getSosCd3());
		// 特約店G名
		final String tytenGName = sosMst3.getBumonSeiName();
		// エリアコード
		final String distCode = sosMst3.getDistCode();
		// add End 2023/1/26  Y.Taniguchi バックログNo.31追加要望


		// 行インデックス
		int rowIdx = ROW_IDX_START_LIST;

		for (Entry<String, List<InsWsPlanForVacSummaryByIns>> entry : summaryListMap.entrySet()) {

			// 列インデックス
			int colIdx = COL_IDX_START_LIST;

			// add Start 2023/1/26  Y.Taniguchi バックログNo.31追加要望
			// 特約店部名
			poiBean.setCellData(tytenBName, rowIdx, colIdx++, topBorderStyle);

			// リージョンコード
			poiBean.setCellData(brCode, rowIdx, colIdx++, topBorderStyle);

			// 特約店G名
			poiBean.setCellData(tytenGName, rowIdx, colIdx++, topBorderStyle);

			// エリアコード
			poiBean.setCellData(distCode, rowIdx, colIdx++, topBorderStyle);

			// 従業員名
			poiBean.setCellData(jgiName, rowIdx, colIdx++, topBorderStyle);

			// 従業員コード
			poiBean.setCellData(jgiNo.toString(), rowIdx, colIdx++, topBorderStyle);
			// add End 2023/1/26  Y.Taniguchi バックログNo.31追加要望

			// 施設名
			final String insName = entry.getValue().get(0).getInsAbbrName();
			poiBean.setCellData(insName, rowIdx, colIdx++, topBorderStyle);

			// add Start 2023/1/20  Y.Taniguchi バックログNo.31追加要望
			// 固定施設コード
			final String insNo = entry.getValue().get(0).getInsNo();
			poiBean.setCellData(insNo, rowIdx, colIdx++, topBorderStyle);
			// add End 2023/1/20  Y.Taniguchi バックログNo.31追加要望

			// add Start 2022/12/1  Y.Taniguchi バックログNo.31
			// 対象区分
			final String hoType = entry.getValue().get(0).getHoInsType();
			poiBean.setCellData(hoType, rowIdx, colIdx++, topBorderStyleCenter);
			// add End 2022/12/1  Y.Taniguchi バックログNo.31

			// 文字列(前期・理論値・当期計画)
			poiBean.setCellData(TEXT_ADVANCE_PERIOD, rowIdx, colIdx, topBorderStyle);
			poiBean.setCellData(TEXT_DIST_VALUE, rowIdx + 1, colIdx);
			poiBean.setCellData(TEXT_PLANNED_VALUE, rowIdx + 2, colIdx++);

			// 合計関数
			poiBean.setCellData("", rowIdx, colIdx, topBorderStyle);
			setSumRowFormula(poiBean, rowIdx, colIdx);
			setSumRowFormula(poiBean, rowIdx + 1, colIdx);
			setSumRowFormula(poiBean, rowIdx + 2, colIdx++);

			// 品目ループカウント
			int prodCount = 0;
			for (PlannedProd plannedProd : plannedProdList) {
				boolean isProdDataExist = false;
				for (InsWsPlanForVacSummaryByIns summary : entry.getValue()) {
					if (StringUtils.equals(plannedProd.getProdCode(), summary.getProdCode())) {
						isProdDataExist = true;
						// 前期
						Long advancePeriod = summary.getAdvancePeriod();
						if (advancePeriod == null) {
							poiBean.setCellData("", rowIdx, colIdx + prodCount, topBorderStyle);
						} else {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(advancePeriod), rowIdx, colIdx + prodCount, topBorderStyle);
						}
						// 理論値
						Long distValue = summary.getDistValueB();
						poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(distValue), rowIdx + 1, colIdx + prodCount);
						// 当期計画
						Long plannedValue = summary.getPlannedValueB();
						poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(plannedValue), rowIdx + 2, colIdx + prodCount);
					}
				}
				// データが存在しない場合は上罫線のみセット
				if (!isProdDataExist) {
					poiBean.setCellData("", rowIdx, colIdx + prodCount, topBorderStyle);
				}
				prodCount++;
			}
			rowIdx += 3; // 3行分進める
		}
		// 印刷範囲を設定
		final short fitWidth = 1; // 横は1固定
		final short fitHeigth = 0; // 縦は0固定

		// add Start 2022/12/1  Y.Taniguchi バックログNo.31
		//poiBean.setPringArea(ROW_IDX_PRINT_START, rowIdx - 1, COL_IDX_PRINT_START, COL_IDX_PRINT_END - offProdNumber, fitWidth, fitHeigth);
		poiBean.setPringArea(ROW_IDX_PRINT_START, rowIdx - 1, COL_IDX_PRINT_START, COL_IDX_PRINT_END_INS - offProdNumber, fitWidth, fitHeigth);
		// add End 2022/12/1  Y.Taniguchi バックログNo.31
	}

	/**
	 * 集計LISTから集計MAP(キー：施設コード）を生成する。<br>
	 * 集計MAPには重点先のみ含まれる。
	 *
	 * @param summaryList 集計LIST
	 * @return 集計MAP
	 */
	private Map<String, List<InsWsPlanForVacSummaryByIns>> createSummaryByInsMap(List<InsWsPlanForVacSummaryByIns> summaryList) {
		Map<String, List<InsWsPlanForVacSummaryByIns>> summaryMap = new LinkedHashMap<String, List<InsWsPlanForVacSummaryByIns>>();
		if (summaryList != null) {
			for (InsWsPlanForVacSummaryByIns summary : summaryList) {

				// 重点先以外の場合、明細からは排除
				ActivityType activityType = summary.getActivityType();
				if (activityType == null || !activityType.equals(ActivityType.JTN)) {
					continue;
				}

				// キーは[施設コード]
				final String key = summary.getInsNo();
				// MAPに既にリストが追加されている場合はリストに追加
				if (summaryMap.containsKey(key)) {
					summaryMap.get(key).add(summary);
				}
				// MAPに追加されていない場合は新規リストを作成しPUT
				else {
					List<InsWsPlanForVacSummaryByIns> newSummaryList = new ArrayList<InsWsPlanForVacSummaryByIns>();
					newSummaryList.add(summary);
					summaryMap.put(key, newSummaryList);
				}
			}
		}
		return summaryMap;
	}

	// add Start 2022/12/1  Y.Taniguchi バックログNo.31
	/**
	 * ワークブックへ施設特約店別集計シートの書き込みを行う。
	 *
	 * @param workBook 書き込み対象のワークブック
	 */
	private void writeInsListTyten(XSSFWorkbook workBook) {
		POIBean poiBean = new POIBean(workBook);
		// 操作対象のシート番号設定
		poiBean.setWorkSheetIdx(SHEET_IDX_INS_LIST_TYTEN);
		// ヘッダ情報のセット
		writeHeadInfo(poiBean, COL_IDX_PROD_NAME_INS_TYTEN, COL_IDX_DATE_INS_TYTEN, COL_IDX_PRINT_END_INS_TYTEN);
		// 不要セルの削除
		removeCell(poiBean, COL_IDX_PROD_NAME_INS_TYTEN, COL_IDX_PRINT_END_INS_TYTEN);
		// リスト情報(合計行)のセット
		writeListInfoSumRow(poiBean, COL_IDX_SUM_INS_TYTEN);
		// リスト情報のセット
		writeListInfoForInsListTyten(poiBean, COL_IDX_SUM_INS_TYTEN);
	}

	/**
	 * 施設特約店別集計リスト情報の値をセルに書き込む。
	 *
	 * @param poiBean POI設定BEAN
	 */
	private void writeListInfoForInsListTyten(POIBean poiBean, int colIdxSum) {

		// (ワ)施設特約店別計画の施設別集計MAPを生成
		Map<String, List<InsWsPlanForVacSummaryByInsTyten>> summaryListMap = createSummaryByInsTytenMap(this.summaryByInsTytenList);
		// 集計ListがNull,または空の場合はReturn
		if (summaryListMap == null || summaryListMap.isEmpty()) {
			return;
		}
		// 集計リストのサイズ分行を追加
		final int expandSize = (summaryListMap.size() * 3) - 3;
		poiBean.addRows(ROW_IDX_START_LIST, expandSize);

		// 上罫線スタイル
		XSSFCellStyle topBorderStyle = createTopBorderStyle(poiBean, colIdxSum);

		// 上罫線スタイル(中央揃え)
		XSSFCellStyle topBorderStyleCenter = createTopBorderStyleCenter(poiBean, COL_IDX_Center);

		// add Start 2023/1/26  Y.Taniguchi バックログNo.31追加要望
		// 従業員情報取得
		final JgiMst jgiMst = searchJgiMst(jgiNo);
		// 従業員名
		final String jgiName = jgiMst.getJgiName();

		// 特約店部情報取得
		final SosMst sosMst2 = searchSosMst(jgiMst.getSosCd2());
		// 特約店部名
		final String tytenBName = sosMst2.getBumonSeiName();
		// リージョンコード
		final String brCode = sosMst2.getBrCode();

		//特約店G情報取得
		final SosMst sosMst3 = searchSosMst(jgiMst.getSosCd3());
		// 特約店G名
		final String tytenGName = sosMst3.getBumonSeiName();
		// エリアコード
		final String distCode = sosMst3.getDistCode();
		// add End 2023/1/26  Y.Taniguchi バックログNo.31追加要望

		// 行インデックス
		int rowIdx = ROW_IDX_START_LIST;

		for (Entry<String, List<InsWsPlanForVacSummaryByInsTyten>> entry : summaryListMap.entrySet()) {

			// 列インデックス
			int colIdx = COL_IDX_START_LIST;

			// 施設名(略名)
			final String insNameAbbr = entry.getValue().get(0).getInsAbbrName();

			// add Start 2023/1/20  Y.Taniguchi バックログNo.31追加要望
			// 固定施設コード
			final String insNo = entry.getValue().get(0).getInsNo();
			// add End 2023/1/20  Y.Taniguchi バックログNo.31追加要望

			// 対象区分
			final String hoType = entry.getValue().get(0).getHoInsType();

			// add Start 2023/1/26  Y.Taniguchi バックログNo.31追加要望
			// 特約店部名
			poiBean.setCellData(tytenBName, rowIdx, colIdx++, topBorderStyle);
			// リージョンコード
			poiBean.setCellData(brCode, rowIdx, colIdx++, topBorderStyle);
			// 特約店G名
			poiBean.setCellData(tytenGName, rowIdx, colIdx++, topBorderStyle);
			// エリアコード
			poiBean.setCellData(distCode, rowIdx, colIdx++, topBorderStyle);
			// 従業員名
			poiBean.setCellData(jgiName, rowIdx, colIdx++, topBorderStyle);
			// 従業員コード
			poiBean.setCellData(jgiNo.toString(), rowIdx, colIdx++, topBorderStyle);
			// add End 2023/1/26  Y.Taniguchi バックログNo.31追加要望

			poiBean.setCellData(insNameAbbr, rowIdx, colIdx++, topBorderStyle);

			// add Start 2023/1/20  Y.Taniguchi バックログNo.31追加要望
			poiBean.setCellData(insNo, rowIdx, colIdx++, topBorderStyle);
			// add End 2023/1/20  Y.Taniguchi バックログNo.31追加要望

			poiBean.setCellData(hoType, rowIdx, colIdx++, topBorderStyleCenter);


			// TMS特約店名称（漢字）
			final String tmsTytenMeiKj = entry.getValue().get(0).getTmsTytenMeiKj();
			poiBean.setCellData(tmsTytenMeiKj, rowIdx, colIdx++, topBorderStyle);

			// 特約店コード
			final String tmsTytenCd = entry.getValue().get(0).getTmsTytenCd();
			poiBean.setCellData(tmsTytenCd, rowIdx, colIdx++, topBorderStyle);

			// 文字列(前期・理論値・当期計画)
			poiBean.setCellData(TEXT_ADVANCE_PERIOD, rowIdx, colIdx, topBorderStyle);
			poiBean.setCellData(TEXT_DIST_VALUE, rowIdx + 1, colIdx);
			poiBean.setCellData(TEXT_PLANNED_VALUE, rowIdx + 2, colIdx++);

			// 合計関数
			poiBean.setCellData("", rowIdx, colIdx, topBorderStyle);
			setSumRowFormula(poiBean, rowIdx, colIdx);
			setSumRowFormula(poiBean, rowIdx + 1, colIdx);
			setSumRowFormula(poiBean, rowIdx + 2, colIdx++);

			// 品目ループカウント
			int prodCount = 0;
			for (PlannedProd plannedProd : plannedProdList) {
				boolean isProdDataExist = false;
				for (InsWsPlanForVacSummaryByInsTyten summary : entry.getValue()) {
					if (StringUtils.equals(plannedProd.getProdCode(), summary.getProdCode())) {
						isProdDataExist = true;
						// 前期
						Long advancePeriod = summary.getAdvancePeriod();
						if (advancePeriod == null) {
							poiBean.setCellData("", rowIdx, colIdx + prodCount, topBorderStyle);
						} else {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(advancePeriod), rowIdx, colIdx + prodCount, topBorderStyle);
						}
						// 理論値
						Long distValue = summary.getDistValueB();
						poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(distValue), rowIdx + 1, colIdx + prodCount);
						// 当期計画
						Long plannedValue = summary.getPlannedValueB();
						poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(plannedValue), rowIdx + 2, colIdx + prodCount);
					}
				}
				// データが存在しない場合は上罫線のみセット
				if (!isProdDataExist) {
					poiBean.setCellData("", rowIdx, colIdx + prodCount, topBorderStyle);
				}
				prodCount++;
			}
			rowIdx += 3; // 3行分進める
		}
		// 印刷範囲を設定
		final short fitWidth = 1; // 横は1固定
		final short fitHeigth = 0; // 縦は0固定

		poiBean.setPringArea(ROW_IDX_PRINT_START, rowIdx - 1, COL_IDX_PRINT_START, COL_IDX_PRINT_END_INS_TYTEN - offProdNumber, fitWidth, fitHeigth);
	}

	/**
	 * 集計LISTから集計MAP(キー：施設コード）を生成する。<br>
	 * 集計MAPには重点先のみ含まれる。
	 *
	 * @param summaryList 集計LIST
	 * @return 集計MAP
	 */
	private Map<String, List<InsWsPlanForVacSummaryByInsTyten>> createSummaryByInsTytenMap(List<InsWsPlanForVacSummaryByInsTyten> summaryList) {
		Map<String, List<InsWsPlanForVacSummaryByInsTyten>> summaryMap = new LinkedHashMap<String, List<InsWsPlanForVacSummaryByInsTyten>>();
		if (summaryList != null) {
			for (InsWsPlanForVacSummaryByInsTyten summary : summaryList) {

				// 重点先以外の場合、明細からは排除
				ActivityType activityType = summary.getActivityType();
				if (activityType == null || !activityType.equals(ActivityType.JTN)) {
					continue;
				}

				// キーは[施設コード]&[特約店コード]
				final String key = summary.getInsNo() + summary.getTmsTytenCd();
				// MAPに既にリストが追加されている場合はリストに追加
				if (summaryMap.containsKey(key)) {
					summaryMap.get(key).add(summary);
				}
				// MAPに追加されていない場合は新規リストを作成しPUT
				else {
					List<InsWsPlanForVacSummaryByInsTyten> newSummaryList = new ArrayList<InsWsPlanForVacSummaryByInsTyten>();
					newSummaryList.add(summary);
					summaryMap.put(key, newSummaryList);
				}
			}
		}
		return summaryMap;
	}
	// add End 2022/12/1  Y.Taniguchi バックログNo.31


	/**
	 * 不要セルを削除する。
	 *
	 *
	 * @param poiBean
	 */
	// add Start 2022/12/1  Y.Taniguchi バックログNo.31
	//private void removeCell(POIBean poiBean) {
		//for (int colIdx = COL_IDX_PROD_NAME + plannedProdList.size(); colIdx <= COL_IDX_PRINT_END; colIdx++) {
	private void removeCell(POIBean poiBean, int colIdxProd, int colIdxEnd) {
		for (int colIdx = colIdxProd + plannedProdList.size(); colIdx <= colIdxEnd; colIdx++) {
	// add End 2022/12/1  Y.Taniguchi バックログNo.31
			for (int rowIdx = 0; rowIdx < ROW_IDX_START_LIST + 3; rowIdx++) {
				poiBean.removeCell(rowIdx, colIdx);
			}
		}
	}
}
