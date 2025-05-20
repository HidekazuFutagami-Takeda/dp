package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.POIBean;
import jp.co.takeda.model.DeliveryResultMr;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.web.cmn.bean.ExportResultExcelImpl;

/**
 * 担当者別品目別計画一覧を出力するロジッククラス
 *
 * @author mtsuchida
 */
public class OutputReviewMrProdMMPListLogic {

	/**
	 * ロガー
	 */
	protected static final Log LOG = LogFactory.getLog(OutputReviewMrProdMMPListLogic.class);

	/**
	 * テンプレート絶対パス名
	 */
	private final String templatePath;

	/**
	 * システム日付
	 */
	private final Date systemDate;

	/**
	 * ダウンロードファイルパス
	 */
	private final String downloadFileTempDir;

	/**
	 * 営業所情報
	 */
	private final SosMst office;

	/**
	 * チーム×組織マップ(キー：組織コード(チーム)、値：組織情報(チーム))
	 */
	private final Map<String, SosMst> teamMap;

	/**
	 * チーム×従業員マップ(キー：組織コード(チーム)、値：対象チーム配下の従業員リスト)
	 */
	private final Map<String, List<JgiMst>> teamJgiListMap;

	/**
	 * 従業員×チームマップ(キー：従業員番号、値：組織コード(チーム))
	 */
	private final Map<Integer, String> jgiTeamMap;

	/**
	 * 品目リスト
	 */
	private final List<PlannedProd> plannedProdList;

	/**
	 * [従業員番号+品目固定コード]をキーに持つ担当者別計画のマップ
	 */
	private final Map<String, MrPlan> mrPlanMapByJgiProdMap;

	/**
	 * [従業員番号+品目固定コード+対象区分(UH/P)]をキーに持つ実績のマップ
	 */
	private final Map<String, DeliveryResultMr> deliveryResultByJgiProdInsTypeMap;

	/**
	 * ONC組織判別フラグ
	 */
	private final String prodName;

	/** ヘッダ_チーム名位置 */
	private static final int[] TEAM_NAME_POS = { 1, 0 };

	/** ヘッダ_従業員名位置 */
	private static final int[] JGI_NAME_POS = { 2, 0 };

	/** ヘッダ_データ作成日位置 */
	private static final int[] CREATE_DATE_POS = { 2, 33 };

	/** シート開始位置 */
	private static final int SHEET_START_INDEX = 0;

	/** 明細_行開始位置 */
	private static final int ROW_START_INDEX = 7;

	/** 明細_可変行最終位置 */
	private static final int ROW_END_INDEX = 56;

	/** 明細_品名列位置 */
	private static final int COL_PROD_POS = 0;

	/** 明細_UH列開始位置 */
	private static final int COL_UH_START_INDEX = 12;

	/** 明細_P列開始位置 */
	private static final int COL_P_START_INDEX = 23;

	/** プリント行範囲 */
	private static final int[] PRINT_ROW_AREA = { 0, 57 };

	/** プリント列範囲 */
	private static final int[] PRINT_COL_AREA = { 0, 33 };

	/** プリントサイズ縦×横 */
	private static final short[] FIT_SIZE = { 1, 1 };

	/** 出力可能_最大品目数(シート単位) */
	private static final int MAX_PROD_NUM = 50;

	/** 合計行品名（ＪＰＢＵ（ＳＴＡＲＳ）計 / ＯＮＣ品目計） */
	private static final int[] PROD_TOTAL_ROW_AREA = {57, 0};

	/**
	 * 単位名称
	 */
	private final String unitYen;

	/**
	 * ヘッダUH名称
	 */
	private final String headerUh;

	/**
	 * ヘッダP名称
	 */
	private final String headerP;

	/** 単位名称位置 */
	private static final int[] UNIT_YEN_NAME_POS = { 2, 2 };

	/** ヘッダUH名称位置 */
	private static final int[] HEADER_UH_NAME_POS = { 3, 12 };

	/** ヘッダP名称位置 */
	private static final int[] HEADER_P_NAME_POS = { 3, 23 };

	/**
	 * コンストラクタ
	 *
	 * @param templatePath テンプレート絶対パス名
	 * @param systemDate システム日付
	 * @param downloadFileTempDir ダウンロードファイルパス
	 * @param office 営業所情報
	 * @param teamMap チーム×組織マップ
	 * @param teamJgiListMap チーム×従業員マップ
	 * @param jgiTeamMap 従業員×チームマップ
	 * @param plannedProdList 品目リスト
	 * @param mrPlanMapByJgiProdMap [従業員番号+品目固定コード]をキーに持つ担当者別計画のマップ
	 * @param deliveryResultByJgiProdInsTypeMap [従業員番号+品目固定コード+対象区分(UH/P)]をキーに持つ実績のマップ
	 * @param prodName
	 */
	public OutputReviewMrProdMMPListLogic(String templatePath, Date systemDate, String downloadFileTempDir, SosMst office, Map<String, SosMst> teamMap,
		Map<String, List<JgiMst>> teamJgiListMap, Map<Integer, String> jgiTeamMap, List<PlannedProd> plannedProdList, Map<String, MrPlan> mrPlanMapByJgiProdMap,
		Map<String, DeliveryResultMr> deliveryResultByJgiProdInsTypeMap, String prodName, String unitYen, String headerUh, String headerP) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (StringUtils.isEmpty(templatePath)) {
			final String errMsg = "テンプレートパスがNull、または空";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (systemDate == null) {
			final String errMsg = "システム日付がNull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (StringUtils.isEmpty(downloadFileTempDir)) {
			final String errMsg = "ダウンロードファイルパスがNull、または空";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (office == null) {
			final String errMsg = "営業所情報がNull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (teamJgiListMap == null) {
			final String errMsg = "チーム×従業員マップがNull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (jgiTeamMap == null) {
			final String errMsg = "従業員×チームマップがNull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null) {
			final String errMsg = "品目リストがNull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (mrPlanMapByJgiProdMap == null) {
			final String errMsg = "担当者別計画のマップがNull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (deliveryResultByJgiProdInsTypeMap == null) {
			final String errMsg = "実績のマップがNull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// フィールドにセット
		// ----------------------
		this.templatePath = templatePath;
		this.systemDate = (Date) systemDate.clone();
		this.downloadFileTempDir = downloadFileTempDir;
		this.office = office;
		this.teamMap = teamMap;
		this.teamJgiListMap = teamJgiListMap;
		this.jgiTeamMap = jgiTeamMap;
		this.plannedProdList = plannedProdList;
		this.mrPlanMapByJgiProdMap = mrPlanMapByJgiProdMap;
		this.deliveryResultByJgiProdInsTypeMap = deliveryResultByJgiProdInsTypeMap;
		this.prodName = prodName;
		this.unitYen = unitYen;
		this.headerUh = headerUh;
		this.headerP = headerP;
	}

	/**
	 * Excelファイルへの出力を実行する。
	 *
	 * @return ファイル名称
	 */
	public String export() {
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
			return createFile(workBook);
		} catch (IOException e) {
			IOUtils.closeQuietly(fileIn);
			throw new SystemException(new Conveyance(PARAMETER_ERROR, "テンプレートパスが存在しない"));
		} finally {
			IOUtils.closeQuietly(fileIn);
		}
	}

	/**
	 * ファイルを生成する。
	 *
	 * @param workBook Excelワークブック
	 * @return ファイル名称
	 * @throws FileNotFoundException ファイルが見つからない場合にスロー
	 * @throws IOException IO例外発生時にスロー
	 */
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//	public String createFile(HSSFWorkbook workBook) throws FileNotFoundException, IOException {
	public String createFile(XSSFWorkbook workBook) throws FileNotFoundException, IOException {
	// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応

		// 出力ファイル名称
		final String dateTxt = DateUtil.toString(systemDate, "yyyyMMddHHmmss");
		// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//		String outputFileName = "Review" + "_" + office.getBrCode() + office.getDistCode() + "_MR_PROD_" + dateTxt + ".xls";
		String outputFileName = "Review" + "_" + office.getBrCode() + office.getDistCode() + "_MR_PROD_" + dateTxt + ".xlsx";
		// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応

		// 書込み
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(new File(downloadFileTempDir + "/" + outputFileName));
			ExportResultExcelImpl exportResult = new ExportResultExcelImpl(outputFileName, workBook);
			exportResult.export(outputStream);
			return exportResult.getName();
		} finally {
			IOUtils.closeQuietly(outputStream);
		}
	}

	/**
	 * Excelファイルへ書込みを行う。
	 *
	 * @param workBook Excelワークブック
	 */
	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//	private void write(HSSFWorkbook workBook) {
	private void write(XSSFWorkbook workBook) {
	// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応

		// --------------------------------------
		// エラーチェック
		// --------------------------------------
		// 出力可能な最大品目数以内かチェック
		if (plannedProdList != null && plannedProdList.size() > MAX_PROD_NUM) {
			throw new SystemException(new Conveyance(PARAMETER_ERROR, "品目数が上限を超えている"));
		}

		// --------------------------------------
		// ワークブック単位でのデータ準備
		// --------------------------------------
		// Bean初期化
		POIBean poiBean = new POIBean(workBook);

		// データ作成日(ファイル共通)
		StringBuilder createDate = new StringBuilder();
		createDate.append("データ作成日：");
		createDate.append(DateUtil.toString(systemDate, "yyyy/MM/dd HH:mm"));

		// 単位名称設定
		poiBean.setCellData(unitYen, UNIT_YEN_NAME_POS);
		// ヘッダUH名称設定
		poiBean.setCellData(headerUh, HEADER_UH_NAME_POS);
		// ヘッダP名称設定
		poiBean.setCellData(headerP, HEADER_P_NAME_POS);

		// --------------------------------------
		// テンプレートからシート作成
		// --------------------------------------
		// 該当営業所に所属するチーム数を取得
		int teamNum = 0;
		if (teamMap.values() != null) {
			teamNum = teamMap.values().size();
		}
		// チーム単位で繰返処理
		int teamIndex = 1;
		int memberNum = 0;
		for (SosMst sosMst : teamMap.values()) {
			// チームに所属する従業員数を取得
			if (teamJgiListMap.get(sosMst.getSosCd()) != null) {
				memberNum = teamJgiListMap.get(sosMst.getSosCd()).size();
			}
			//チームに所属する従業員単位でシート作成
			if (teamIndex < teamNum) {
				// 従業員数分シート作成
				for (int sheetNum = 0; sheetNum < memberNum; sheetNum++) {
					// テンプレートからシートを作成(末尾に作成)
					// mod Start 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
					//poiBean.cloneSheet(0);
					poiBean.cloneSheet(sheetNum);
					// mod End 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
				}
			} else if (teamIndex == teamNum) {
				// テンプレートを直接利用する為、(従業員数 - 1)分シート作成
				for (int sheetNum = 0; sheetNum < memberNum - 1; sheetNum++) {
					// テンプレートからシートを作成(末尾に作成)
					// mod Start 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
					//poiBean.cloneSheet(0);
					poiBean.cloneSheet(sheetNum);
					// mod End 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
				}
			}
			// チームインデックス増加
			teamIndex++;
		}

		// --------------------------------------
		// シート単位でデータ設定
		// --------------------------------------
		int sheetIndex = SHEET_START_INDEX;
		int rowIndex = ROW_START_INDEX;
		int colIndex = 0;
		StringBuilder rowKey = new StringBuilder();

		// 同姓同名対応
		Map<String, Integer> jgiNameCountMap = new HashMap<String, Integer>();
		for (SosMst sosMst : teamMap.values()) {
			for (JgiMst jgiMst : teamJgiListMap.get(sosMst.getSosCd())) {
				Integer jgiCnt = jgiNameCountMap.get(jgiMst.getJgiName());
				if (jgiCnt != null) {
					jgiCnt = jgiCnt + 1;
					jgiNameCountMap.put(jgiMst.getJgiName(), jgiCnt);
				} else {
					jgiNameCountMap.put(jgiMst.getJgiName(), Integer.valueOf(1));
				}
			}
		}

		// チーム単位で繰返処理
		Map<String, Integer> jgiNameCurrentNoMap = null;
		for (SosMst sosMst : teamMap.values()) {

			//チームに所属する従業員単位でシートにデータを設定
			for (JgiMst jgiMst : teamJgiListMap.get(sosMst.getSosCd())) {

				// シートインデックスを設定
				poiBean.setWorkSheetIdx(sheetIndex);
				// シート名を設定
				Integer jgiNameCnt = jgiNameCountMap.get(jgiMst.getJgiName());
				if (jgiNameCnt != null && jgiNameCnt > 1) {
					if (jgiNameCurrentNoMap == null) {
						jgiNameCurrentNoMap = new HashMap<String, Integer>();
					}
					Integer currentNo = jgiNameCurrentNoMap.get(jgiMst.getJgiName());
					if (currentNo == null) {
						currentNo = 0;
					}
					currentNo = currentNo + 1;
					poiBean.setSheetName(sheetIndex, jgiMst.getJgiName() + "(" + currentNo + ")");
					jgiNameCurrentNoMap.put(jgiMst.getJgiName(), currentNo);
				} else {
					poiBean.setSheetName(sheetIndex, jgiMst.getJgiName());
				}

				// --------------------------------------
				// ヘッダ情報を設定
				// --------------------------------------
				// チーム名
				poiBean.setCellData(sosMst.getBumonSeiName(), TEAM_NAME_POS);
				// 従業員名
				poiBean.setCellData(jgiMst.getJgiName() + "（" + jgiMst.getShokushuName() + "）", JGI_NAME_POS);
				// データ作成日
				poiBean.setCellData(createDate.toString(), CREATE_DATE_POS);

				// --------------------------------------
				// 行情報を設定
				// --------------------------------------
				// 品目単位で行作成
				for (PlannedProd prod : plannedProdList) {

// add start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定　最終品目（≒JPBUSTARS計）は最終行に出力する
					if(plannedProdList.size() == (rowIndex - ROW_START_INDEX + 1)){
						// rowIndexを最終行まで移動しながら途中行を非表示としていく
						while (rowIndex <= ROW_END_INDEX) {
							poiBean.getWorkSheet().getRow(rowIndex++).setHeight((short) 0);
						}
					}
// add end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定　最終品目（≒JPBUSTARS計）は最終行に出力する

					rowKey.append(jgiMst.getJgiNo());
					rowKey.append(prod.getProdCode());

					// --------------------------------------
					// データ元を取得
					// --------------------------------------
					// 担当者納入実績(UH)
					DeliveryResultMr resultMrUh = deliveryResultByJgiProdInsTypeMap.get(rowKey.toString() + InsType.UH);
					// 担当者納入実績(P)
					DeliveryResultMr resultMrP = deliveryResultByJgiProdInsTypeMap.get(rowKey.toString() + InsType.P);
					// 担当者別計画
					MrPlan mrPlan = mrPlanMapByJgiProdMap.get(rowKey.toString());
					// keyの初期化
					rowKey.delete(0, rowKey.length());

					// --------------------------------------
					// 共通情報を設定
					// --------------------------------------
					// 品名
					poiBean.setCellData(prod.getProdName(), rowIndex, COL_PROD_POS);

					// --------------------------------------
					// UH情報を設定 従業員-品目単位
					// --------------------------------------
					colIndex = COL_UH_START_INDEX;

					if (resultMrUh == null || resultMrUh.getMonNnu() == null) {
						// 前期実績
						poiBean.setCellData((Long) null, rowIndex, colIndex);
						// 当期計画
						poiBean.setCellData((Long) null, rowIndex, ++colIndex);
						// 当期実績
						poiBean.setCellData((Long) null, rowIndex, ++colIndex);
						// 遂行率(埋込関数)
						colIndex++;
					} else {
						// 前期実績
						if (resultMrUh.getMonNnu().getAdvancePeriod() != null) {
							setCellD1000(poiBean, resultMrUh.getMonNnu().getAdvancePeriod(), rowIndex, colIndex);
						} else {
							poiBean.setCellData((Long) null, rowIndex, colIndex);
						}
						// 当期計画
						if (resultMrUh.getMonNnu().getCurrentPlanValue() != null) {
							setCellD1000(poiBean, resultMrUh.getMonNnu().getCurrentPlanValue(), rowIndex, ++colIndex);
						} else {
							poiBean.setCellData((Long) null, rowIndex, ++colIndex);
						}
						// 当期実績
						if (resultMrUh.getMonNnu().getCurrentPeriod() != null) {
							setCellD1000(poiBean, resultMrUh.getMonNnu().getCurrentPeriod(), rowIndex, ++colIndex);
						} else {
							poiBean.setCellData((Long) null, rowIndex, ++colIndex);
						}
						// 遂行率(埋込関数)
						colIndex++;
					}

					if (mrPlan == null) {
						// 理論計画1-計画
						poiBean.setCellData((Long) null, rowIndex, ++colIndex);
						// 理論計画1-前同比(埋込関数)
						colIndex++;
						// 理論計画2-計画
						poiBean.setCellData((Long) null, rowIndex, ++colIndex);
						// 理論計画2-前同比(埋込関数)
						colIndex++;

						// 営業所案
						poiBean.setCellData((Long) null, rowIndex, ++colIndex);
						// 決定
						poiBean.setCellData((Long) null, rowIndex, ++colIndex);
						// 前同比(埋込関数)
						colIndex++;

					} else {
						// 理論計画1-計画
						if (mrPlan.getTheoreticalValueUh1() != null && mrPlan.getSpecialInsPlanValueUhY() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getTheoreticalValueUh1() + mrPlan.getSpecialInsPlanValueUhY()), rowIndex, ++colIndex);
						} else if (mrPlan.getTheoreticalValueUh1() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getTheoreticalValueUh1()), rowIndex, ++colIndex);
						} else if (mrPlan.getSpecialInsPlanValueUhY() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getSpecialInsPlanValueUhY()), rowIndex, ++colIndex);
						} else {
							poiBean.setCellData((Long) null, rowIndex, ++colIndex);
						}
						// 理論計画1-前同比(埋込関数)
						colIndex++;

						// 理論計画2-計画
						if (mrPlan.getTheoreticalValueUh2() != null && mrPlan.getSpecialInsPlanValueUhY() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getTheoreticalValueUh2() + mrPlan.getSpecialInsPlanValueUhY()), rowIndex, ++colIndex);
						} else if (mrPlan.getTheoreticalValueUh2() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getTheoreticalValueUh2()), rowIndex, ++colIndex);
						} else if (mrPlan.getSpecialInsPlanValueUhY() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getSpecialInsPlanValueUhY()), rowIndex, ++colIndex);
						} else {
							poiBean.setCellData((Long) null, rowIndex, ++colIndex);
						}

						// 理論計画2-前同比(埋込関数)
						colIndex++;

						// 営業所案
						if (mrPlan.getOfficeValueUhY() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getOfficeValueUhY()), rowIndex, ++colIndex);
						} else {
							poiBean.setCellData((Long) null, rowIndex, ++colIndex);
						}
						// 決定
						if (mrPlan.getPlannedValueUhY() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getPlannedValueUhY()), rowIndex, ++colIndex);
						} else {
							poiBean.setCellData((Long) null, rowIndex, ++colIndex);
						}
						// 前同比(埋込関数)
						colIndex++;
					}

					// --------------------------------------
					// P情報を設定 従業員-品目単位
					// --------------------------------------
					colIndex = COL_P_START_INDEX;

					if (resultMrP == null || resultMrP.getMonNnu() == null) {
						// 前期実績
						poiBean.setCellData((Long) null, rowIndex, colIndex);
						// 当期計画
						poiBean.setCellData((Long) null, rowIndex, ++colIndex);
						// 当期実績
						poiBean.setCellData((Long) null, rowIndex, ++colIndex);
						// 遂行率(埋込関数)
						colIndex++;
					} else {
						// 前期実績
						if (resultMrP.getMonNnu().getAdvancePeriod() != null) {
							setCellD1000(poiBean, resultMrP.getMonNnu().getAdvancePeriod(), rowIndex, colIndex);
						} else {
							poiBean.setCellData((Long) null, rowIndex, colIndex);
						}
						// 当期計画
						if (resultMrP.getMonNnu().getCurrentPlanValue() != null) {
							setCellD1000(poiBean, resultMrP.getMonNnu().getCurrentPlanValue(), rowIndex, ++colIndex);
						} else {
							poiBean.setCellData((Long) null, rowIndex, ++colIndex);
						}
						// 当期実績
						if (resultMrP.getMonNnu().getCurrentPeriod() != null) {
							setCellD1000(poiBean, resultMrP.getMonNnu().getCurrentPeriod(), rowIndex, ++colIndex);
						} else {
							poiBean.setCellData((Long) null, rowIndex, ++colIndex);
						}
						// 遂行率(埋込関数)
						colIndex++;
					}

					if (mrPlan == null) {
						// 理論計画1-計画
						poiBean.setCellData((Long) null, rowIndex, ++colIndex);
						// 理論計画1-前同比(埋込関数)
						colIndex++;
						// 理論計画2-計画
						poiBean.setCellData((Long) null, rowIndex, ++colIndex);
						// 理論計画2-前同比(埋込関数)
						colIndex++;

						// 営業所案
						poiBean.setCellData((Long) null, rowIndex, ++colIndex);
						// 決定
						poiBean.setCellData((Long) null, rowIndex, ++colIndex);
						// 前同比(埋込関数)
						colIndex++;

					} else {
						// 理論計画1-計画
						if (mrPlan.getTheoreticalValueP1() != null && mrPlan.getSpecialInsPlanValuePY() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getTheoreticalValueP1() + mrPlan.getSpecialInsPlanValuePY()), rowIndex, ++colIndex);
						} else if (mrPlan.getTheoreticalValueP1() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getTheoreticalValueP1()), rowIndex, ++colIndex);
						} else if (mrPlan.getSpecialInsPlanValuePY() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getSpecialInsPlanValuePY()), rowIndex, ++colIndex);
						} else {
							poiBean.setCellData((Long) null, rowIndex, ++colIndex);
						}
						// 理論計画1-前同比(埋込関数)
						colIndex++;

						// 理論計画2-計画
						if (mrPlan.getTheoreticalValueP2() != null && mrPlan.getSpecialInsPlanValuePY() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getTheoreticalValueP2() + mrPlan.getSpecialInsPlanValuePY()), rowIndex, ++colIndex);
						} else if (mrPlan.getTheoreticalValueP2() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getTheoreticalValueP2()), rowIndex, ++colIndex);
						} else if (mrPlan.getSpecialInsPlanValuePY() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getSpecialInsPlanValuePY()), rowIndex, ++colIndex);
						} else {
							poiBean.setCellData((Long) null, rowIndex, ++colIndex);
						}

						// 理論計画2-前同比(埋込関数)
						colIndex++;

						// 営業所案
						if (mrPlan.getOfficeValuePY() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getOfficeValuePY()), rowIndex, ++colIndex);
						} else {
							poiBean.setCellData((Long) null, rowIndex, ++colIndex);
						}
						// 決定
						if (mrPlan.getPlannedValuePY() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getPlannedValuePY()), rowIndex, ++colIndex);
						} else {
							poiBean.setCellData((Long) null, rowIndex, ++colIndex);
						}
						// 前同比(埋込関数)
						colIndex++;
					}

					// --------------------------------------
					// 行単位終了処理
					// --------------------------------------
					// 行送り
					rowIndex++;

				}
// del start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定　合計行もSQLレベルで取得しているため、名称の特別扱いも不要
//				// 合計行の品名計名
//				poiBean.setCellData(prodName, PROD_TOTAL_ROW_AREA[0], PROD_TOTAL_ROW_AREA[1]);
// del start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定　合計行もSQLレベルで取得しているため、名称の特別扱いも不要

				// --------------------------------------
				// シート単位終了処理
				// --------------------------------------
				// 印刷設定
				poiBean.setPringArea(PRINT_ROW_AREA[0], PRINT_ROW_AREA[1], PRINT_COL_AREA[0], PRINT_COL_AREA[1], FIT_SIZE[0], FIT_SIZE[1]);
				// 自動計算の設定
				poiBean.setForceFormulaRecalculation(true);
// del start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定　非表示処理は品目ループの先頭に移動
//				// 行の非表示処理
//				if (rowIndex < ROW_END_INDEX) {
//					for (int hiddenRowIndex = rowIndex; hiddenRowIndex <= ROW_END_INDEX; hiddenRowIndex++) {
//						poiBean.getWorkSheet().getRow(hiddenRowIndex).setHeight((short) 0);
//					}
//				}
// del end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定　非表示処理は品目ループの先頭に移動
				// シート送り
				sheetIndex++;
				// 初期化
				rowIndex = ROW_START_INDEX;

			}

		}

	}

	// --------------------------------------
	// PRIVATE METHOD
	// --------------------------------------

	/**
	 * 指定の値を/1000つき関数として指定セルに設定する。
	 *
	 * @param poiBean POIBean
	 * @param value 値(円単位)
	 * @param row 行番号
	 * @param col 列番号
	 */
	private void setCellD1000(POIBean poiBean, Long value, int row, int col) {
		final String str = "/ 1000";
		if (value == null) {
			poiBean.setCellData((Long) null, row, col);
		} else {
			poiBean.setCellFormula(value + str, row, col);
		}
	}
}
