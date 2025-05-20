package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
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
 * 品目別担当者別計画一覧を出力するロジッククラス
 *
 * @author mtsuchida
 */
public class OutputReviewProdMrMMPListLogic {

	/**
	 * ロガー
	 */
	protected static final Log LOG = LogFactory.getLog(OutputReviewProdMrMMPListLogic.class);

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
	 * 合計行品名計名
	 */
	private final String prodName;

	/**
	 * [従業員番号+品目固定コード+対象区分(UH/P)]をキーに持つ実績のマップ
	 */
	private final Map<String, DeliveryResultMr> deliveryResultByJgiProdInsTypeMap;

	/** 出力可能_最大品目数(シート単位) */
	private static final int MAX_PROD_NUM = 50;

	/** 出力可能_最大従業員数(表単位) */
//	private static final int MAX_JGI_NUM = 15; // mod 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）１組織あたりのMAX従業員数の変更(15→30)にともなう定数変更
	private static final int MAX_JGI_NUM = 30;

	/** ヘッダ_チーム名位置 */
	private static final int[] TEAM_NAME_POS = { 1, 0 };

	/** ヘッダ_データ作成日位置 */
	private static final int[] CREATE_DATE_POS = { 1, 33 };

	/** シート開始位置 */
	private static final int SHEET_START_INDEX = 0;

	/** 表_品名行_開始位置 */
	private static final int PROD_ROW_START_INDEX = 6;

	/** 表_品名列_位置 */
	private static final int PROD_COL_POS = 0;

	/** 表_品名行_間隔(次の表の開始位置までの間隔) */
//	private static final int PROD_ROW_SPACE = 17; // mod 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）１組織あたりのMAX従業員数の変更(15→30)にともなう定数変更
	private static final int PROD_ROW_SPACE = MAX_JGI_NUM + 1 + 1; //MAX従業員数 + 品名行 + 合計行

	/** 明細_従業員行_開始位置 */
	private static final int JGI_ROW_START_INDEX = 7;

	/** 明細_従業員列_位置 */
	private static final int JGI_COL_POS = 0;

	/** 明細_従業員行_間隔(次の表の開始位置までの間隔) */
	private static final int JGI_ROW_SPACE = 3;

	/** 明細_可変行最終_開始位置 */
//	private static final int JGI_ROW_END_INDEX = 21; // mod 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）１組織あたりのMAX従業員数の変更(15→30)にともなう定数変更
	private static final int JGI_ROW_END_INDEX = JGI_ROW_START_INDEX + MAX_JGI_NUM - 1;

	/** 明細_可変行最終_間隔(次の表の位置までの間隔) */
//	private static final int JGI_ROW_END_SPACE = 17; // mod 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）１組織あたりのMAX従業員数の変更(15→30)にともなう定数変更
	private static final int JGI_ROW_END_SPACE = MAX_JGI_NUM + 1 + 1; //MAX従業員数 + 品名行 + 合計行

	/** 明細_UH列開始位置 */
	private static final int COL_UH_START_INDEX = 12;

	/** 明細_P列開始位置 */
	private static final int COL_P_START_INDEX = 23;

// mod start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定　MMP計の特別扱い廃止
	/** 明細_最終行 */
//	private static final int SHEET_ROW_END_POS = 855;
//	private static final int SHEET_ROW_END_POS = 872; // mod 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）１組織あたりのMAX従業員数の変更(15→30)にともなう定数変更
	private static final int SHEET_ROW_END_POS = PROD_ROW_START_INDEX + PROD_ROW_SPACE * (MAX_PROD_NUM + 1) - 1; //明細_最終行 = 開始位置 + 品目毎の行数 * (MAX品目数 + 1) -1　　※(MAX品目数 + 1)の"+1"意味 → 過去の経緯的にテンプレートEXCELにはMAX品目数＋"合計品目用"の枠が用意されているため。なおJ16-0015の改訂により合計品目用の枠が利用されることはなくなったが、テンプレートEXCELに枠はそのまま残っている。

//	/** MMP計_開始行 */
//	private static final int MMP_ROW_START_POS = 857;
//
//	/** MMP計_可変行最終行 */
//	private static final int MMP_ROW_END_POS = 871;
// mod end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定　MMP計の特別扱い廃止

	/** プリント行範囲 */
//	private static final int[] PRINT_ROW_AREA = { 0, 872 }; // mod 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）１組織あたりのMAX従業員数の変更(15→30)にともなう定数変更
	private static final int[] PRINT_ROW_AREA = { 0, SHEET_ROW_END_POS };

	/** プリント列範囲 */
	private static final int[] PRINT_COL_AREA = { 0, 33 };

	/** プリントサイズ横×縦 */
	private static final short[] FIT_SIZE = { 1, 0 };

	/** 従業員識別_開始番号 */
	private static final long JGI_ID_START_INDEX = 1L;

	/** 従業員識別番号_配置列 */
	private static final int JGI_ID_POS = 34;

	/** 列、行タイトル位置 */
	private static final int[] COL_ROW_TITLE_IDX = { -1, -1, 0, 5 };

// del end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定　MMP計の特別扱い廃止　にともない未使用
//	/** 合計行品名計名称 */
//	private static final int[] PROD_NAME_AREA = {856, 0};
// del end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定　MMP計の特別扱い廃止

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
	private static final int[] UNIT_YEN_NAME_POS = { 1, 2 };

	/** ヘッダUH名称位置 */
	private static final int[] HEADER_UH_NAME_POS = { 2, 12 };

	/** ヘッダP名称位置 */
	private static final int[] HEADER_P_NAME_POS = { 2, 23 };

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
	 * @param deliveryResultMrDao 担当者別納入実績DAO
	 * @param mrPlanMapByJgiProdMap [従業員番号+品目固定コード]をキーに持つ担当者別計画のマップ
	 * @param deliveryResultByJgiProdInsTypeMap [従業員番号+品目固定コード+対象区分(UH/P)]をキーに持つ実績のマップ
	 */
	public OutputReviewProdMrMMPListLogic(String templatePath, Date systemDate, String downloadFileTempDir, SosMst office, Map<String, SosMst> teamMap,
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
//		String outputFileName = "Review" + "_" + office.getBrCode() + office.getDistCode() + "_PROD_MR_" + dateTxt + ".xls";
		String outputFileName = "Review" + "_" + office.getBrCode() + office.getDistCode() + "_PROD_MR_" + dateTxt + ".xlsx";
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

		// 出力可能な最大従業員数以内かチェック
		for (SosMst sosMst : teamMap.values()) {
			if (teamJgiListMap.get(sosMst.getSosCd()) != null) {
				if (teamJgiListMap.get(sosMst.getSosCd()).size() > MAX_JGI_NUM) {
					throw new SystemException(new Conveyance(PARAMETER_ERROR, "従業員数が上限を超えている"));
				}
			}
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
		//テンプレートを直接利用する為、(チーム数 - 1)分シート作成
		for (int teamIndex = 1; teamIndex < teamNum; teamIndex++) {
			// mod Start 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
			//poiBean.cloneSheet(0);
			poiBean.cloneSheet(teamIndex-1);
			// mod End 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
		}

		// 行タイトル設定
		for (int i = 0; i < teamMap.size(); i++) {
			workBook.setRepeatingRowsAndColumns(i, COL_ROW_TITLE_IDX[0], COL_ROW_TITLE_IDX[1], COL_ROW_TITLE_IDX[2], COL_ROW_TITLE_IDX[3]);
		}

		// --------------------------------------
		// シート単位でデータ設定
		// --------------------------------------
		int sheetIndex = SHEET_START_INDEX;
		int prodRowIndex = PROD_ROW_START_INDEX;
		int jgiRowIndex = JGI_ROW_START_INDEX;
		int jgiLastRowIndex = JGI_ROW_END_INDEX;
		long jgiIdIndex = JGI_ID_START_INDEX;
		int colIndex = 0;
		StringBuilder rowKey = new StringBuilder();

		// チーム単位でシートにデータを設定
		for (SosMst sosMst : teamMap.values()) {

			// シートインデックスを設定
			poiBean.setWorkSheetIdx(sheetIndex);
			// シート名を設定
			poiBean.setSheetName(sheetIndex, sosMst.getBumonSeiName());

			// --------------------------------------
			// ヘッダ情報を設定
			// --------------------------------------
			// チーム名
			poiBean.setCellData(sosMst.getBumonSeiName(), TEAM_NAME_POS);
			// データ作成日
			poiBean.setCellData(createDate.toString(), CREATE_DATE_POS);

			// --------------------------------------
			// 表情報を設定
			// --------------------------------------
			// 品目単位で表作成
			for (PlannedProd prod : plannedProdList) {
				// 品名
				poiBean.setCellData(prod.getProdName(), prodRowIndex, PROD_COL_POS);

				// --------------------------------------
				// 行情報を設定
				// --------------------------------------
				// 従業員単位で作成
				for (JgiMst jgiMst : teamJgiListMap.get(sosMst.getSosCd())) {

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
					// 従業員情報を設定
					// --------------------------------------
					// 従業員名
					poiBean.setCellData(jgiMst.getJgiName() + "（" + jgiMst.getShokushuName() + "）", jgiRowIndex, JGI_COL_POS);

					// --------------------------------------
					// UH情報を設定 品目-従業員単位
					// --------------------------------------
					colIndex = COL_UH_START_INDEX;

					if (resultMrUh == null || resultMrUh.getMonNnu() == null) {
						// 前期実績
						poiBean.setCellData((Long) null, jgiRowIndex, colIndex);
						// 当期計画
						poiBean.setCellData((Long) null, jgiRowIndex, ++colIndex);
						// 当期実績
						poiBean.setCellData((Long) null, jgiRowIndex, ++colIndex);
						// 遂行率(埋込関数)
						colIndex++;
					} else {
						// 前期実績
						if (resultMrUh.getMonNnu().getAdvancePeriod() != null) {
							setCellD1000(poiBean, resultMrUh.getMonNnu().getAdvancePeriod(), jgiRowIndex, colIndex);
						} else {
							poiBean.setCellData((Long) null, jgiRowIndex, colIndex);
						}
						// 当期計画
						if (resultMrUh.getMonNnu().getCurrentPlanValue() != null) {
							setCellD1000(poiBean, resultMrUh.getMonNnu().getCurrentPlanValue(), jgiRowIndex, ++colIndex);
						} else {
							poiBean.setCellData((Long) null, jgiRowIndex, ++colIndex);
						}
						// 当期実績
						if (resultMrUh.getMonNnu().getCurrentPeriod() != null) {
							setCellD1000(poiBean, resultMrUh.getMonNnu().getCurrentPeriod(), jgiRowIndex, ++colIndex);
						} else {
							poiBean.setCellData((Long) null, jgiRowIndex, ++colIndex);
						}
						// 遂行率(埋込関数)
						colIndex++;
					}

					if (mrPlan == null) {
						// 理論計画1-計画
						poiBean.setCellData((Long) null, jgiRowIndex, ++colIndex);
						// 理論計画1-前同比(埋込関数)
						colIndex++;
						// 理論計画2-計画
						poiBean.setCellData((Long) null, jgiRowIndex, ++colIndex);
						// 理論計画2-前同比(埋込関数)
						colIndex++;

						// 営業所案
						poiBean.setCellData((Long) null, jgiRowIndex, ++colIndex);
						// 決定
						poiBean.setCellData((Long) null, jgiRowIndex, ++colIndex);
						// 前同比(埋込関数)
						colIndex++;

					} else {
						// 理論計画1-計画
						if (mrPlan.getTheoreticalValueUh1() != null && mrPlan.getSpecialInsPlanValueUhY() != null) {
							poiBean
								.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getTheoreticalValueUh1() + mrPlan.getSpecialInsPlanValueUhY()), jgiRowIndex, ++colIndex);
						} else if (mrPlan.getTheoreticalValueUh1() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getTheoreticalValueUh1()), jgiRowIndex, ++colIndex);
						} else if (mrPlan.getSpecialInsPlanValueUhY() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getSpecialInsPlanValueUhY()), jgiRowIndex, ++colIndex);
						} else {
							poiBean.setCellData((Long) null, jgiRowIndex, ++colIndex);
						}
						// 理論計画1-前同比(埋込関数)
						colIndex++;

						// 理論計画2-計画
						if (mrPlan.getTheoreticalValueUh2() != null && mrPlan.getSpecialInsPlanValueUhY() != null) {
							poiBean
								.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getTheoreticalValueUh2() + mrPlan.getSpecialInsPlanValueUhY()), jgiRowIndex, ++colIndex);
						} else if (mrPlan.getTheoreticalValueUh2() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getTheoreticalValueUh2()), jgiRowIndex, ++colIndex);
						} else if (mrPlan.getSpecialInsPlanValueUhY() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getSpecialInsPlanValueUhY()), jgiRowIndex, ++colIndex);
						} else {
							poiBean.setCellData((Long) null, jgiRowIndex, ++colIndex);
						}
						// 理論計画2-前同比(埋込関数)
						colIndex++;

						// 営業所案
						if (mrPlan.getOfficeValueUhY() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getOfficeValueUhY()), jgiRowIndex, ++colIndex);
						} else {
							poiBean.setCellData((Long) null, jgiRowIndex, ++colIndex);
						}
						// 決定
						if (mrPlan.getPlannedValueUhY() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getPlannedValueUhY()), jgiRowIndex, ++colIndex);
						} else {
							poiBean.setCellData((Long) null, jgiRowIndex, ++colIndex);
						}
						// 前同比(埋込関数)
						colIndex++;
					}

					// --------------------------------------
					// P情報を設定 品目-従業員単位
					// --------------------------------------
					colIndex = COL_P_START_INDEX;

					if (resultMrP == null || resultMrP.getMonNnu() == null) {
						// 前期実績
						poiBean.setCellData((Long) null, jgiRowIndex, colIndex);
						// 当期計画
						poiBean.setCellData((Long) null, jgiRowIndex, ++colIndex);
						// 当期実績
						poiBean.setCellData((Long) null, jgiRowIndex, ++colIndex);
						// 遂行率(埋込関数)
						colIndex++;
					} else {
						// 前期実績
						if (resultMrP.getMonNnu().getAdvancePeriod() != null) {
							setCellD1000(poiBean, resultMrP.getMonNnu().getAdvancePeriod(), jgiRowIndex, colIndex);
						} else {
							poiBean.setCellData((Long) null, jgiRowIndex, colIndex);
						}
						// 当期計画
						if (resultMrP.getMonNnu().getCurrentPlanValue() != null) {
							setCellD1000(poiBean, resultMrP.getMonNnu().getCurrentPlanValue(), jgiRowIndex, ++colIndex);
						} else {
							poiBean.setCellData((Long) null, jgiRowIndex, ++colIndex);
						}
						// 当期実績
						if (resultMrP.getMonNnu().getCurrentPeriod() != null) {
							setCellD1000(poiBean, resultMrP.getMonNnu().getCurrentPeriod(), jgiRowIndex, ++colIndex);
						} else {
							poiBean.setCellData((Long) null, jgiRowIndex, ++colIndex);
						}
						// 遂行率(埋込関数)
						colIndex++;
					}

					if (mrPlan == null) {
						// 理論計画1-計画
						poiBean.setCellData((Long) null, jgiRowIndex, ++colIndex);
						// 理論計画1-前同比(埋込関数)
						colIndex++;
						// 理論計画2-計画
						poiBean.setCellData((Long) null, jgiRowIndex, ++colIndex);
						// 理論計画2-前同比(埋込関数)
						colIndex++;

						// 営業所案
						poiBean.setCellData((Long) null, jgiRowIndex, ++colIndex);
						// 決定
						poiBean.setCellData((Long) null, jgiRowIndex, ++colIndex);
						// 前同比(埋込関数)
						colIndex++;

					} else {
						// 理論計画1-計画
						if (mrPlan.getTheoreticalValueP1() != null && mrPlan.getSpecialInsPlanValuePY() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getTheoreticalValueP1() + mrPlan.getSpecialInsPlanValuePY()), jgiRowIndex, ++colIndex);
						} else if (mrPlan.getTheoreticalValueP1() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getTheoreticalValueP1()), jgiRowIndex, ++colIndex);
						} else if (mrPlan.getSpecialInsPlanValuePY() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getSpecialInsPlanValuePY()), jgiRowIndex, ++colIndex);
						} else {
							poiBean.setCellData((Long) null, jgiRowIndex, ++colIndex);
						}
						// 理論計画1-前同比(埋込関数)
						colIndex++;

						// 理論計画2-計画
						if (mrPlan.getTheoreticalValueP2() != null && mrPlan.getSpecialInsPlanValuePY() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getTheoreticalValueP2() + mrPlan.getSpecialInsPlanValuePY()), jgiRowIndex, ++colIndex);
						} else if (mrPlan.getTheoreticalValueP2() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getTheoreticalValueP2()), jgiRowIndex, ++colIndex);
						} else if (mrPlan.getSpecialInsPlanValuePY() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getSpecialInsPlanValuePY()), jgiRowIndex, ++colIndex);
						} else {
							poiBean.setCellData((Long) null, jgiRowIndex, ++colIndex);
						}
						// 理論計画2-前同比(埋込関数)
						colIndex++;

						// 営業所案
						if (mrPlan.getOfficeValuePY() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getOfficeValuePY()), jgiRowIndex, ++colIndex);
						} else {
							poiBean.setCellData((Long) null, jgiRowIndex, ++colIndex);
						}
						// 決定
						if (mrPlan.getPlannedValuePY() != null) {
							poiBean.setCellData(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getPlannedValuePY()), jgiRowIndex, ++colIndex);
						} else {
							poiBean.setCellData((Long) null, jgiRowIndex, ++colIndex);
						}
						// 前同比(埋込関数)
						colIndex++;
					}

					// 集計用に従業員識別番号を隠し列に設定
					poiBean.setCellData(jgiIdIndex++, jgiRowIndex, JGI_ID_POS);

					// --------------------------------------
					// 行単位終了処理
					// --------------------------------------
					// 行送り
					jgiRowIndex++;
				}

				// --------------------------------------
				// 表単位終了処理
				// --------------------------------------
				// 表の非表示処理
				if (jgiRowIndex < jgiLastRowIndex) {
					for (int hiddenRowIndex = jgiRowIndex; hiddenRowIndex <= jgiLastRowIndex; hiddenRowIndex++) {
						poiBean.getWorkSheet().getRow(hiddenRowIndex).setHeight((short) 0);
						// 行送り
						if (jgiRowIndex != jgiLastRowIndex) {
							jgiRowIndex++;
						}
					}
				}
				// 表送り
				prodRowIndex = prodRowIndex + PROD_ROW_SPACE;
				// 行送り
				jgiRowIndex = jgiRowIndex + JGI_ROW_SPACE;
				jgiLastRowIndex = jgiLastRowIndex + JGI_ROW_END_SPACE;
				// 初期化
				jgiIdIndex = JGI_ID_START_INDEX;

			}

// del start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定　合計行もSQLレベルで取得するよう改定
//			// --------------------------------------
//			// MMP計表設定
//			// --------------------------------------
//			int mmpRowIndex = MMP_ROW_START_POS;
//			for (JgiMst jgiMst : teamJgiListMap.get(sosMst.getSosCd())) {
//				// 従業員名
//				poiBean.setCellData(jgiMst.getJgiName() + "（" + jgiMst.getShokushuName() + "）", mmpRowIndex, JGI_COL_POS);
//				// 集計用に従業員識別番号を隠し列に設定
//				poiBean.setCellData(jgiIdIndex++, mmpRowIndex, JGI_ID_POS);
//				// 行送り
//				mmpRowIndex++;
//			}
//			// 表の非表示処理
//			if (mmpRowIndex < MMP_ROW_END_POS) {
//				for (int hiddenRowIndex = mmpRowIndex; hiddenRowIndex <= MMP_ROW_END_POS; hiddenRowIndex++) {
//					poiBean.getWorkSheet().getRow(hiddenRowIndex).setHeight((short) 0);
//				}
//			}
//
//			// 合計行品名計名称
//			poiBean.setCellData(prodName, PROD_NAME_AREA[0], PROD_NAME_AREA[1]);
// del end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定　合計行もSQLレベルで取得するよう改定

			// --------------------------------------
			// シート単位終了処理
			// --------------------------------------
			// 自動計算の設定
			poiBean.setForceFormulaRecalculation(true);
			// 行の非表示処理
			if (prodRowIndex < SHEET_ROW_END_POS) {
				for (int hiddenRowIndex = prodRowIndex; hiddenRowIndex <= SHEET_ROW_END_POS; hiddenRowIndex++) {
					poiBean.getWorkSheet().getRow(hiddenRowIndex).setHeight((short) 0);
				}
			}
			// 印刷設定
			poiBean.setPringArea(PRINT_ROW_AREA[0], PRINT_ROW_AREA[1], PRINT_COL_AREA[0], PRINT_COL_AREA[1], FIT_SIZE[0], FIT_SIZE[1]);
			// シート送り
			sheetIndex++;
			// 初期化
			prodRowIndex = PROD_ROW_START_INDEX;
			jgiRowIndex = JGI_ROW_START_INDEX;
			jgiLastRowIndex = JGI_ROW_END_INDEX;
			jgiIdIndex = JGI_ID_START_INDEX;

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

	/**
	 * 指定セルの関数をクリアする。
	 *
	 * @param poiBean POIBean
	 * @param row 行番号
	 * @param col 列番号
	 */
	private void clearFunc(POIBean poiBean, int row, int col) {
		poiBean.setCellFormula(null, row, col);
	}
}
