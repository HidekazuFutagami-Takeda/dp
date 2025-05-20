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
import jp.co.takeda.service.DpsReportService.MrPlanOutputDivision;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.web.cmn.bean.ExportResultExcelImpl;

/**
 * 担当者別品目別計画一覧を出力するロジッククラス
 *
 * @author kibe
 */
public class OutputMrPlanMMPListLogic {

	/**
	 * ロガー
	 */
	protected static final Log LOG = LogFactory.getLog(OutputMrPlanMMPListLogic.class);

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
	 * 出力区分
	 */
	private final MrPlanOutputDivision mrPlanOutputDivision;

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
	 * 合計行品名計名称
	 */
	private final String prodName;

	/**
	 * ヘッダUH名称
	 */
	private final String headerUh;
	/**
	 * ヘッダP名称
	 */
	private final String headerP;

	/** 出力可能_最大品目数 */
	private static final int MAX_PROD_NUM = 50;

	/** 出力可能_最大チーム数 */
	private static final int MAX_TEAM_NUM = 15;

// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	/** 出力可能_ユーザ数/1組織 */
	private static final int MAX_JGI_NUM_PER_SOS = 30;
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

	/** データ作成日位置 */
	private static final int[] CREATE_DATE_POS = { 0, 0 };

	/** 品目名称スタート位置 */
	private static final int[] PROD_NAME_START = { 2, 6 };

	/** 計画値名称スタート位置 */
	private static final int[] KEIKAKU_NAME_START = { 3, 7 };

	/** チーム名称スタート位置（合計） */
	private static final int[] TEAM_NAME_START_SUM = { 4, 1 };

	/** チーム名称スタート位置（UH） */
//	private static final int[] TEAM_NAME_START_UH = { 245, 1 }; //mod 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　テンプレートの1組織内の最大人数を変更：15→30
	private static final int[] TEAM_NAME_START_UH = { 470, 1 };

	/** チーム名称スタート位置（P） */
//	private static final int[] TEAM_NAME_START_P = { 486, 1 }; //mod 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　テンプレートの1組織内の最大人数を変更：15→30
	private static final int[] TEAM_NAME_START_P = { 936, 1 };

// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	/** チームタイトル位置 */
	private static final int[] TEAM_TITLE_AREA = {2, 1};
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

	/** 合計行品名計名称 */
	private static final int[] PROD_NAME_AREA = {2, 3};

	/** ヘッダUH名称位置 */
	private static final int[] HEADER_UH_NAME_POS = { 470, 0 };

	/** ヘッダP名称位置 */
	private static final int[] HEADER_P_NAME_POS = { 936, 0 };

	/** 明細スタート列位置 */
	private static final int KEIKAKU_DETAIL_START_COL = 6;

	/** 「合計」「UH」「P」の各行数 */
//	private static final int ROW_NUM = 241; //mod 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　テンプレートの1組織内の最大人数を変更：15→30
	private static final int ROW_NUM = TEAM_NAME_START_UH[0] - TEAM_NAME_START_SUM[0];

	/** 最大列数 */
	private static final int MAX_COL_NUM = 156;

	/**
	 * コンストラクタ
	 *
	 * @param templatePath テンプレート絶対パス名
	 * @param systemDate システム日付
	 * @param mrPlanOutputDivision 出力区分
	 * @param downloadFileTempDir ダウンロードファイルパス
	 * @param office 営業所情報
	 * @param teamMap チーム×組織マップ
	 * @param teamJgiListMap チーム×従業員マップ
	 * @param jgiTeamMap 従業員×チームマップ
	 * @param plannedProdList 品目リスト
	 * @param mrPlanMapByJgiProdMap [従業員番号+品目固定コード]をキーに持つ担当者別計画のマップ
	 * @param deliveryResultByJgiProdInsTypeMap [従業員番号+品目固定コード+対象区分(UH/P)]をキーに持つ実績のマップ
	 */
	public OutputMrPlanMMPListLogic(String templatePath, Date systemDate, String downloadFileTempDir, MrPlanOutputDivision mrPlanOutputDivision, SosMst office,
		Map<String, SosMst> teamMap, Map<String, List<JgiMst>> teamJgiListMap, Map<Integer, String> jgiTeamMap, List<PlannedProd> plannedProdList,
		Map<String, MrPlan> mrPlanMapByJgiProdMap, Map<String, DeliveryResultMr> deliveryResultByJgiProdInsTypeMap, String prodName,
		String headerUh, String headerP) {

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
		if (mrPlanOutputDivision == null) {
			final String errMsg = "出力区分がNull";
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
		this.mrPlanOutputDivision = mrPlanOutputDivision;
		this.office = office;
		this.teamMap = teamMap;
		this.teamJgiListMap = teamJgiListMap;
		this.jgiTeamMap = jgiTeamMap;
		this.plannedProdList = plannedProdList;
		this.mrPlanMapByJgiProdMap = mrPlanMapByJgiProdMap;
		this.deliveryResultByJgiProdInsTypeMap = deliveryResultByJgiProdInsTypeMap;
		this.prodName = prodName;
		this.headerUh = headerUh;
		this.headerP = headerP;
	}

	/**
	 * Excelファイルへの出力を実行する。
	 *
	 * @return 書込み結果
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
		StringBuilder outputFileName = new StringBuilder();
		outputFileName.append("ProdList");
		outputFileName.append("_");
		outputFileName.append(office.getBrCode());
		outputFileName.append(office.getDistCode());
		outputFileName.append("_");
		if (MrPlanOutputDivision.RIRON_1 == mrPlanOutputDivision) {
			outputFileName.append("T1_");
		} else if (MrPlanOutputDivision.RIRON_2 == mrPlanOutputDivision) {
			outputFileName.append("T2_");
		} else if (MrPlanOutputDivision.OFFICE == mrPlanOutputDivision) {
			outputFileName.append("O_");
		} else if (MrPlanOutputDivision.KETTEI == mrPlanOutputDivision) {
			outputFileName.append("P_");
		}
		outputFileName.append(DateUtil.toString(systemDate, "yyyyMMddHHmmss"));
		// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//		outputFileName.append(".xls");
		outputFileName.append(".xlsx");
		// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応

		// 書込み
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(new File(downloadFileTempDir + "/" + outputFileName));
			ExportResultExcelImpl exportResult = new ExportResultExcelImpl(outputFileName.toString(), workBook);
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

		// 出力可能な最大品目数以内かチェック
		if (plannedProdList != null && plannedProdList.size() > MAX_PROD_NUM) {
			throw new SystemException(new Conveyance(PARAMETER_ERROR, "品目数が上限を超えている"));
		}
		// 出力可能な最大チーム数以内かチェック
		if (teamMap.values().size() > MAX_TEAM_NUM) {
			throw new SystemException(new Conveyance(PARAMETER_ERROR, "チーム数が上限を超えている"));
		}

		// Poibeanの初期化
		POIBean poiBean = new POIBean(workBook);

		// シート名設定
		poiBean.setSheetName(office.getSosName());

		// データ作成日時設定
		String createDateString = DateUtil.toString(systemDate, "yyyy/MM/dd HH:mm");
		poiBean.setCellData(createDateString, CREATE_DATE_POS);
		// ヘッダUH名称設定
		poiBean.setCellData(headerUh, HEADER_UH_NAME_POS);
		// ヘッダP名称設定
		poiBean.setCellData(headerP, HEADER_P_NAME_POS);

		// 計画値名称判定
		String keikaku = null;
		if (MrPlanOutputDivision.RIRON_1 == mrPlanOutputDivision) {
			keikaku = "理論計画①";
		} else if (MrPlanOutputDivision.RIRON_2 == mrPlanOutputDivision) {
			keikaku = "理論計画②";
		} else if (MrPlanOutputDivision.OFFICE == mrPlanOutputDivision) {
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			keikaku = "エリア案";
//			keikaku = "営業所案";
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
		} else if (MrPlanOutputDivision.KETTEI == mrPlanOutputDivision) {
			keikaku = "決定計画";
		}

//add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
		//チームが無い営業所の場合、テンプレートの「チーム」を「営業所・エリア」に変更
		if(office.getUnderSosCnt() == 0){
			//「チーム」→「営業所エリア」、サイズ変更
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			poiBean.setCellData("エリア", TEAM_TITLE_AREA[0], TEAM_TITLE_AREA[1]);
//			poiBean.setCellData("エリア\n営業所", TEAM_TITLE_AREA[0], TEAM_TITLE_AREA[1]);
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			int nowWidth = poiBean.getWorkSheet().getColumnWidth(TEAM_TITLE_AREA[1]);
			poiBean.getWorkSheet().setColumnWidth(TEAM_TITLE_AREA[1], (int)Math.round(nowWidth * 2.2)); //２行になるため横幅を２倍にする。".2"はゆとり。

			//「チーム計」→「営業所・エリア計」
			for(int i = 1; i <= MAX_TEAM_NUM; i++){
				//合計
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
				poiBean.setCellData("エリア計", TEAM_NAME_START_SUM[0] - 1 + (MAX_JGI_NUM_PER_SOS + 1) * i, TEAM_NAME_START_SUM[1] + 1);
//				poiBean.setCellData("営業所・エリア計", TEAM_NAME_START_SUM[0] - 1 + (MAX_JGI_NUM_PER_SOS + 1) * i, TEAM_NAME_START_SUM[1] + 1);
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
				//UH
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
				poiBean.setCellData("エリア計", TEAM_NAME_START_UH[0] - 1 + (MAX_JGI_NUM_PER_SOS + 1) * i, TEAM_NAME_START_SUM[1] + 1);
//				poiBean.setCellData("営業所・エリア計", TEAM_NAME_START_UH[0] - 1 + (MAX_JGI_NUM_PER_SOS + 1) * i, TEAM_NAME_START_SUM[1] + 1);
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
				//P
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
				poiBean.setCellData("エリア計", TEAM_NAME_START_P[0] - 1 + (MAX_JGI_NUM_PER_SOS + 1) * i, TEAM_NAME_START_SUM[1] + 1);
//				poiBean.setCellData("営業所・エリア計", TEAM_NAME_START_P[0] - 1 + (MAX_JGI_NUM_PER_SOS + 1) * i, TEAM_NAME_START_SUM[1] + 1);
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			}
		}
//add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

		// ヘッダ（品目名称、計画値名称）出力
		// 品目名称出力開始列
		int col3_prod = PROD_NAME_START[1];
		// 計画値名称出力開始列
		int col3_keikaku = KEIKAKU_NAME_START[1];

		// 先に「MMP計」に計画値名称出力
		poiBean.setCellData(keikaku, KEIKAKU_NAME_START[0], col3_keikaku - 3);
		poiBean.setCellData(prodName, PROD_NAME_AREA[0], PROD_NAME_AREA[1] );

		for (int i = 0; i < plannedProdList.size(); i++) {
			// 品目名称出力
			poiBean.setCellData(plannedProdList.get(i).getProdName(), PROD_NAME_START[0], col3_prod);
			// 計画値名称出力
			poiBean.setCellData(keikaku, KEIKAKU_NAME_START[0], col3_keikaku);

			col3_prod = col3_prod + 3;
			col3_keikaku = col3_keikaku + 3;
		}

		// 営業所内チームループ
		// チーム出力行単位
		int teamRowIndex = 0;
		for (SosMst team : teamMap.values()) {

			// チーム出力行設定
			int teamNameRowSUM = TEAM_NAME_START_SUM[0] + teamRowIndex;
			int teamNameRowUH = TEAM_NAME_START_UH[0] + teamRowIndex;
			int teamNameRowP = TEAM_NAME_START_P[0] + teamRowIndex;

			// チーム名称出力
			poiBean.setCellData(team.getBumonRyakuName(), teamNameRowSUM, TEAM_NAME_START_SUM[1]);
			poiBean.setCellData(team.getBumonRyakuName(), teamNameRowUH, TEAM_NAME_START_UH[1]);
			poiBean.setCellData(team.getBumonRyakuName(), teamNameRowP, TEAM_NAME_START_P[1]);

			// 従業員出力行単位
			int jgiRowIndex = 0;

			// チーム内従業員ループ
			for (JgiMst jgi : teamJgiListMap.get(team.getSosCd())) {

				// 従業員出力行設定
				int jgiRowSUM = teamNameRowSUM + jgiRowIndex;
				int jgiRowUH = teamNameRowUH + jgiRowIndex;
				int jgiRowP = teamNameRowP + jgiRowIndex;

				// 従業員名称出力
				poiBean.setCellData(jgi.getJgiName() + "（" + jgi.getShokushuName() + "）", jgiRowSUM, TEAM_NAME_START_SUM[1] + 1);
				poiBean.setCellData(jgi.getJgiName() + "（" + jgi.getShokushuName() + "）", jgiRowUH, TEAM_NAME_START_UH[1] + 1);
				poiBean.setCellData(jgi.getJgiName() + "（" + jgi.getShokushuName() + "）", jgiRowP, TEAM_NAME_START_P[1] + 1);

				// 明細出力列設定
				int planColIndex = KEIKAKU_DETAIL_START_COL;

				// 従業員内品目ループ
				for (PlannedProd prod : plannedProdList) {

					// 担当者別計画取得コード (従業員番号＋品目固定コード)
					String keikakuCd = jgi.getJgiNo() + prod.getProdCode();
					// 担当者別納入計画取得コード(UH) (担当者別計画取得コード＋対象区分UH)
					String jissekiCdUH = keikakuCd + InsType.UH;
					// 担当者別納入計画取得コード(P) (担当者別計画取得コード＋対象区分P)
					String jissekiCdP = keikakuCd + InsType.P;

					// 担当者別計画オブジェクト取得
					MrPlan keikakuObj = mrPlanMapByJgiProdMap.get(keikakuCd);
					// 担当者別納入計画(UH)オブジェクト
					DeliveryResultMr jissekiObjUH = deliveryResultByJgiProdInsTypeMap.get(jissekiCdUH);
					// 担当者別納入計画(P)オブジェクト
					DeliveryResultMr jissekiObjP = deliveryResultByJgiProdInsTypeMap.get(jissekiCdP);

					// ---------
					// 前期実績
					// ---------
					// 前期実績(UH)出力
					long jissekiNumUH = 0L;
					if (jissekiObjUH != null && jissekiObjUH.getMonNnu() != null) {
						jissekiNumUH = jissekiObjUH.getMonNnu().getAdvancePeriod();
						setCellD1000(poiBean, jissekiNumUH, jgiRowUH, planColIndex);
					} else {
						poiBean.setCellData((Long) null, jgiRowUH, planColIndex);
					}
					// 前期実績(P)出力
					long jissekiNumP = 0L;
					if (jissekiObjP != null && jissekiObjP.getMonNnu() != null) {
						jissekiNumP = jissekiObjP.getMonNnu().getAdvancePeriod();
						setCellD1000(poiBean, jissekiNumP, jgiRowP, planColIndex);
					} else {
						poiBean.setCellData((Long) null, jgiRowP, planColIndex);
					}
					// 前期実績(合計)出力
					String jissekiColName = poiBean.getColumnNameString(planColIndex);
					String jissekiFormulaSUM = "SUM(" + jissekiColName + (jgiRowUH + 1) + "," + jissekiColName + (jgiRowP + 1) + ")";
					poiBean.setCellFormula(jissekiFormulaSUM, jgiRowSUM, planColIndex);

					planColIndex++;

					// ---------
					// 計画値
					// ---------
					if (keikakuObj != null) {
						// 理論計画①
						if (MrPlanOutputDivision.RIRON_1 == mrPlanOutputDivision) {
							// 計画値(UH)出力
							if (keikakuObj.getSpecialInsPlanValueUhY() != null && keikakuObj.getTheoreticalValueUh1() != null) {
								setCellD1000(poiBean, keikakuObj.getSpecialInsPlanValueUhY() + keikakuObj.getTheoreticalValueUh1(), jgiRowUH, planColIndex);
							} else if (keikakuObj.getSpecialInsPlanValueUhY() != null) {
								setCellD1000(poiBean, keikakuObj.getSpecialInsPlanValueUhY(), jgiRowUH, planColIndex);
							} else if (keikakuObj.getTheoreticalValueUh1() != null) {
								setCellD1000(poiBean, keikakuObj.getTheoreticalValueUh1(), jgiRowUH, planColIndex);
							} else {
								poiBean.setCellData((Long) null, jgiRowUH, planColIndex);
							}

							// 計画値(P)出力
							if (keikakuObj.getSpecialInsPlanValuePY() != null && keikakuObj.getTheoreticalValueP1() != null) {
								setCellD1000(poiBean, keikakuObj.getSpecialInsPlanValuePY() + keikakuObj.getTheoreticalValueP1(), jgiRowP, planColIndex);
							} else if (keikakuObj.getSpecialInsPlanValuePY() != null) {
								setCellD1000(poiBean, keikakuObj.getSpecialInsPlanValuePY(), jgiRowP, planColIndex);
							} else if (keikakuObj.getTheoreticalValueP1() != null) {
								setCellD1000(poiBean, keikakuObj.getTheoreticalValueP1(), jgiRowP, planColIndex);
							} else {
								poiBean.setCellData((Long) null, jgiRowP, planColIndex);
							}
						}
						// 理論計画②
						else if (MrPlanOutputDivision.RIRON_2 == mrPlanOutputDivision) {
							// 計画値(UH)出力
							if (keikakuObj.getSpecialInsPlanValueUhY() != null && keikakuObj.getTheoreticalValueUh2() != null) {
								setCellD1000(poiBean, keikakuObj.getSpecialInsPlanValueUhY() + keikakuObj.getTheoreticalValueUh2(), jgiRowUH, planColIndex);
							} else if (keikakuObj.getSpecialInsPlanValueUhY() != null) {
								setCellD1000(poiBean, keikakuObj.getSpecialInsPlanValueUhY(), jgiRowUH, planColIndex);
							} else if (keikakuObj.getTheoreticalValueUh2() != null) {
								setCellD1000(poiBean, keikakuObj.getTheoreticalValueUh2(), jgiRowUH, planColIndex);
							} else {
								poiBean.setCellData((Long) null, jgiRowUH, planColIndex);
							}

							// 計画値(P)出力
							if (keikakuObj.getSpecialInsPlanValuePY() != null && keikakuObj.getTheoreticalValueP2() != null) {
								setCellD1000(poiBean, keikakuObj.getSpecialInsPlanValuePY() + keikakuObj.getTheoreticalValueP2(), jgiRowP, planColIndex);
							} else if (keikakuObj.getSpecialInsPlanValuePY() != null) {
								setCellD1000(poiBean, keikakuObj.getSpecialInsPlanValuePY(), jgiRowP, planColIndex);
							} else if (keikakuObj.getTheoreticalValueP2() != null) {
								setCellD1000(poiBean, keikakuObj.getTheoreticalValueP2(), jgiRowP, planColIndex);
							} else {
								poiBean.setCellData((Long) null, jgiRowP, planColIndex);
							}
						}

						// 営業所案
						else if (MrPlanOutputDivision.OFFICE == mrPlanOutputDivision) {
							// 計画値(UH)出力
							if (keikakuObj.getOfficeValueUhY() != null) {
								setCellD1000(poiBean, keikakuObj.getOfficeValueUhY(), jgiRowUH, planColIndex);
							} else {
								poiBean.setCellData((Long) null, jgiRowUH, planColIndex);
							}

							// 計画値(P)出力
							if (keikakuObj.getOfficeValuePY() != null) {
								setCellD1000(poiBean, keikakuObj.getOfficeValuePY(), jgiRowP, planColIndex);
							} else {
								poiBean.setCellData((Long) null, jgiRowP, planColIndex);
							}
						}

						// 決定計画
						else if (MrPlanOutputDivision.KETTEI == mrPlanOutputDivision) {
							// 計画値(UH)出力
							if (keikakuObj.getPlannedValueUhY() != null) {
								setCellD1000(poiBean, keikakuObj.getPlannedValueUhY(), jgiRowUH, planColIndex);
							} else {
								poiBean.setCellData((Long) null, jgiRowUH, planColIndex);
							}

							// 計画値(P)出力
							if (keikakuObj.getPlannedValuePY() != null) {
								setCellD1000(poiBean, keikakuObj.getPlannedValuePY(), jgiRowP, planColIndex);
							} else {
								poiBean.setCellData((Long) null, jgiRowP, planColIndex);
							}
						}

						// 計画値(合計)出力
						String keikakuColName = poiBean.getColumnNameString(planColIndex);
						String keikakuFormulaSUM = "SUM(" + keikakuColName + (jgiRowUH + 1) + "," + keikakuColName + (jgiRowP + 1) + ")";
						poiBean.setCellFormula(keikakuFormulaSUM, jgiRowSUM, planColIndex);

					} else {
						poiBean.setCellData((Long) null, jgiRowUH, planColIndex);
						poiBean.setCellData((Long) null, jgiRowP, planColIndex);
						poiBean.setCellData((Long) null, jgiRowSUM, planColIndex);
					}

					planColIndex++;

					// ---------
					// 前同比
					// ---------
					// 前期実績と計画値のカラム名取得
					String[] rateColName = { poiBean.getColumnNameString(planColIndex - 2), poiBean.getColumnNameString(planColIndex - 1) };

					// 前同比(UH)出力
					String rateUH = rateColName[1] + (jgiRowUH + 1) + "/" + rateColName[0] + (jgiRowUH + 1) + "*100";
					String rateFormulaUH = "IF(ISERROR(" + rateUH + "),0," + rateUH + ")";
					poiBean.setCellFormula(rateFormulaUH, jgiRowUH, planColIndex);
					// 前同比(P)出力
					String rateP = rateColName[1] + (jgiRowP + 1) + "/" + rateColName[0] + (jgiRowP + 1) + "*100";
					String rateFormulaP = "IF(ISERROR(" + rateP + "),0," + rateP + ")";
					poiBean.setCellFormula(rateFormulaP, jgiRowP, planColIndex);
					// 前同比(合計)出力
					String rateSUM = rateColName[1] + (jgiRowSUM + 1) + "/" + rateColName[0] + (jgiRowSUM + 1) + "*100";
					String rateFormulaSUM = "IF(ISERROR(" + rateSUM + "),0," + rateSUM + ")";
					poiBean.setCellFormula(rateFormulaSUM, jgiRowSUM, planColIndex);

					planColIndex++;
				}
				// 担当者1人分移動
				jgiRowIndex++;
			}
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　1組織内の最大ユーザ数を変更：15→30
			// チーム行1チーム分移動
//			teamRowIndex = teamRowIndex + 16;
			teamRowIndex = teamRowIndex + MAX_JGI_NUM_PER_SOS + 1; //+1は合計行
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

			// 空白従業員行非表示
			for (int i = teamNameRowSUM + jgiRowIndex; i < TEAM_NAME_START_SUM[0] + teamRowIndex - 1; i++) {
				// 合計
				poiBean.getWorkSheet().getRow(i).setHeight((short) 0);
				//UH
				poiBean.getWorkSheet().getRow(i + ROW_NUM).setHeight((short) 0);
				//P
				poiBean.getWorkSheet().getRow(i + ROW_NUM * 2).setHeight((short) 0);
			}
		}

		// 空白チーム行非表示
		for (int i = TEAM_NAME_START_SUM[0] + teamRowIndex; i < TEAM_NAME_START_UH[0] - 1; i++) {
			//合計
			poiBean.getWorkSheet().getRow(i).setHeight((short) 0);
			//UH
			poiBean.getWorkSheet().getRow(i + ROW_NUM).setHeight((short) 0);
			//P
			poiBean.getWorkSheet().getRow(i + ROW_NUM * 2).setHeight((short) 0);
		}

		// 空白品目列非表示
		int hiddenColIndex = plannedProdList.size() * 3 + 6;
		for (int i = hiddenColIndex; i < MAX_COL_NUM; i++) {
			poiBean.setColumnHidden(i);
		}

		// Excel関数自動計算設定
		poiBean.setForceFormulaRecalculation(true);
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
