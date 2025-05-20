package jp.co.takeda.logic;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.POIBean;
import jp.co.takeda.model.DeliveryResultMr;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MonNnu;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.model.OfficePlan;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.TeamPlan;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.util.MathUtil;

/**
 * (医)チーム担当者計画検討表のExcelファイルを生成するロジッククラス<br>
 *
 * <pre>
 * ロジッククラスの処理順序は以下の(1)～(3)の通り。
 *
 * (1)共通設定({@link #createTemplete())
 * ＭＭＰ品シートとその他シートで、共通の設定を行う。
 * (品目名、チーム名、従業員名、行のhidden化等)
 *
 * (2)品目シートの共通設定({@link #clearFunc(int, int)})
 * 各品目の設定(串刺し関数の無効化)を行う。
 *
 * (3)品目シートの作成({@link #execute(int, PlannedProd, OfficePlan, List, Map, Map)})
 * 各品目の処理を実行する。
 * (呼び出し元のサービスクラスで品目毎に呼び出される。)
 *
 * (4)ＭＭＰ品シートの設定({@link #execute(int))
 * ＭＭＰ品シートの処理を実行する。
 * </pre>
 *
 * @author tkawabata
 */
public class MrTeamReportExportLogic {

	/**
	 * ロガー
	 */
	private static final Log LOG = LogFactory.getLog(MrTeamReportExportLogic.class);

	/**
	 * 作成日時
	 */
	private final Date sysDate;

	/**
	 * ワークブック
	 */
	private final POIBean poiBean;

// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	/**
	 * 営業所組織情報
	 */
	private final SosMst office;
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

	/**
	 * チームマップ(組織コードをキー、組織情報を値に持つマップ)
	 */
	private final Map<String, SosMst> teamMap;

	/**
	 * 従業員リストマップ(チーム組織コードをキー、従業員情報リストを値に持つマップ)
	 */
	private final Map<String, List<JgiMst>> jgiListMap;

	/**
	 * 従業員番号から組織コード(チームまたは営業所・エリア)を逆引きするマップ
	 */
	private final Map<Integer, String> jgiTeamMap;

	/** プリント列範囲 */
	private static final int[] PRINT_COL_AREA = { 0, 34 };

	/** プリントサイズ縦×横 */
	private static final short[] FIT_SIZE = { 1, 0 };

	/** 品目名称位置 */
	private static final int[] PROD_NAME_POS = { 1, 0 };

	/** 単位名称位置 */
	private static final int[] UNIT_YEN_NAME_POS = { 1, 2 };

	/** データ作成日位置 */
	private static final int[] CREATE_DATE_POS = { 1, 34 };

	/** ヘッダUH名称位置 */
	private static final int[] HEADER_UH_NAME_POS = { 2, 13 };

	/** ヘッダP名称位置 */
	private static final int[] HEADER_P_NAME_POS = { 2, 24 };

	/** 営業所開始位置 */
	private static final int[] OFFICE_START_POS = { 6, 0 };

	/** チーム名開始位置 */
	private static final int[] TEAM_START_POS = { 8, 0 };

	/** 従業員名開始位置 */
	private static final int[] JGI_START_POS = { 22, 1 };

	/** 従業員名横のチーム名開始位置[列] */
	private static final int JGI_TEAM_START_COL_IDX = 0;

	/** チーム最大数 */
	private static final int TEAM_MAX_CNT = 12;

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	/** 1チーム当りの最大従業員数 */
//	private static final int TEAM_PER_JGI_MAX_CNT = 15;
	private static final int TEAM_PER_JGI_MAX_CNT = 30;
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

	/** 前期実績UH[列番号] */
	private static final int AD_PERIOD_COL_UH = 13;

	/** 当期計画UH[列番号] */
	private static final int CR_PLAN_COL_UH = 14;

	/** 当期実績UH[列番号] */
	private static final int CR_PERIOD_COL_UH = 15;

	/** 理論計画①UH[列番号] */
	private static final int T1_PLAN_COL_UH = 17;

	/** 理論計画②UH[列番号] */
	private static final int T2_PLAN_COL_UH = 19;

	/** 営業所案UH[列番号] */
	private static final int OFFICE_PLAN_COL_UH = 21;

	/** 決定欄UH[列番号] */
	private static final int FINAL_PLAN_COL_UH = 22;

	/** 前期実績P[列番号] */
	private static final int AD_PERIOD_COL_P = 24;

	/** 当期計画P[列番号] */
	private static final int CR_PLAN_COL_P = 25;

	/** 当期実績P[列番号] */
	private static final int CR_PERIOD_COL_P = 26;

	/** 理論計画①P[列番号] */
	private static final int T1_PLAN_COL_P = 28;

	/** 理論計画②P[列番号] */
	private static final int T2_PLAN_COL_P = 30;

	/** 営業所案P[列番号] */
	private static final int OFFICE_PLAN_COL_P = 32;

	/** 決定欄P[列番号] */
	private static final int FINAL_PLAN_COL_P = 33;

	/** 結合セルの場所 */
	private static final int[][] REGIONS_IDX = {{ 198, 0 }, { 182, 0 }, { 166, 0 }, { 150, 0 }, { 134, 0 }, { 118, 0 }, { 102, 0 }, { 86, 0 }, { 70, 0 }, { 54, 0 }, { 38, 0 }, { 22, 0 } };

	/**
	 * コンストラクタ
	 *
	 * @param office 営業所情報
	 * @param sysDate 作成日時
	 * @param workBook ワークブック
	 * @param teamMap チームマップ(組織コードをキー、組織情報を値に持つマップ)
	 * @param jgiTeamMap 従業員番号から組織コード(チーム)を逆引きするマップ(キー：従業員番号、値：組織コード)
	 */
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
//	public MrTeamReportExportLogic(Date sysDate, POIBean poiBean, Map<String, SosMst> teamMap, Map<String, List<JgiMst>> jgiListMap, Map<Integer, String> jgiTeamMap) {
//	public MrTeamReportExportLogic(SosMst office, Date sysDate, POIBean poiBean, Map<String, SosMst> teamMap, Map<String, List<JgiMst>> jgiListMap, Map<Integer, String> jgiTeamMap) {
	public MrTeamReportExportLogic(SosMst office, Date sysDate, POIBean poiBean, Map<String, SosMst> teamMap, Map<String, List<JgiMst>> jgiListMap,
			Map<Integer, String> jgiTeamMap) {
		this.office = office;
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
		this.poiBean = poiBean;
		this.sysDate = sysDate;
		this.teamMap = teamMap;
		this.jgiListMap = jgiListMap;
		this.jgiTeamMap = jgiTeamMap;
		if (LOG.isDebugEnabled()) {
			LOG.debug("office=" + office); //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
			LOG.debug("poiBean=" + poiBean);
			LOG.debug("sysDate=" + sysDate);
			LOG.debug("teamMap=" + teamMap);
			LOG.debug("jgiListMap=" + jgiListMap);
		}
	}

	/**
	 * テンプレートファイルに共通情報を書き込む。
	 *
	 * @return allRowCnt 行数
	 */
	public int createTemplete() {

		// データ作成日時設定
		String createDateString = "データ作成日：" + DateUtil.toString(sysDate, "yyyy/MM/dd HH:mm");
		poiBean.setCellData(createDateString, CREATE_DATE_POS);

		// チーム名称の開始行、列
		int tRow = TEAM_START_POS[0];
		int jRow = JGI_START_POS[0];

		int teamSize = teamMap.size();
		if (teamSize > TEAM_MAX_CNT) {
			final String errMsg = "チーム数の上限値を超えているため、エラー。teamSize=" + teamSize;
			throw new SystemException(new Conveyance(ErrMessageKey.STATE_ERROR, errMsg));
		}
		for (SosMst team : teamMap.values()) {
// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
			// チーム階層組織が無い場合、帳票の「チーム」を「営業所エリア」に変更
			boolean initial = true;
			if(initial){
				if(office.getUnderSosCnt() == 0){
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
					poiBean.setCellData("エリア・担当者計画検討表", 0, 0);
					poiBean.setCellData("エリア", 2, 0);
//					poiBean.setCellData("営業所エリア・担当者計画検討表", 0, 0);	//「チーム・担当者計画検討表」→「営業所エリア・担当者計画検討表」
//					poiBean.setCellData("営業所\n・エリア", 2, 0);				//「チーム」→「営業所・エリア」
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
				}
				initial = false;
			}
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

			// チーム名(略称)を設定
			poiBean.setCellData(team.getBumonRyakuName(), tRow, TEAM_START_POS[1]);
			tRow++;

			// チーム内の従業員
			List<JgiMst> jgiList = jgiListMap.get(team.getSosCd());
			int jgiSize = jgiList.size();
			if (jgiSize > TEAM_PER_JGI_MAX_CNT) {
				final String errMsg = "1チームあたりの従業員の上限値を超えているため、エラー。jgiSize=" + jgiSize;
				throw new SystemException(new Conveyance(ErrMessageKey.STATE_ERROR, errMsg));
			}
			for (int i = 0; i < TEAM_PER_JGI_MAX_CNT; i++) {

				if (i < jgiSize) {
					if (i == 0) {
						// チーム名(略称)
						poiBean.setCellData(team.getBumonRyakuName(), jRow, JGI_TEAM_START_COL_IDX);
					}
					// 従業員名
					poiBean.setCellData(jgiList.get(i).getJgiName() + "（" + jgiList.get(i).getShokushuName() + "）", jRow, JGI_START_POS[1]);
				} else {
					poiBean.hideRow(jRow);
				}

				// 次行へ送る
				jRow++;
			}

			// 次行へ送る(合計行)
			jRow++;
		}

		// 次行へ送る(以降を削除)
		jRow++;

		// 不要な行をクリアまたは削除
		if (teamSize < TEAM_MAX_CNT) {

			// 差分
			int BLANK_TEAM_CNT = TEAM_MAX_CNT - teamSize;

			// チーム集計部分の処理(クリア)
			int blankTeamRowStartIdx = TEAM_START_POS[0] + teamSize;
			for (int i = 0; i < BLANK_TEAM_CNT; i++) {
				poiBean.clearFormula(blankTeamRowStartIdx + i);
				poiBean.hideRow(blankTeamRowStartIdx + i);
			}

			// 結合セルを解除
			// 結合セル情報は上から順番に保持しているわけではないので、
			// アドレスを見て、削除対象のセルかを判定する必要がある
			// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//			final HSSFSheet sheet = poiBean.getWorkSheet();
			final XSSFSheet sheet = poiBean.getWorkSheet();
			// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
			for (int i = 0; i < BLANK_TEAM_CNT; i++) {

				// REGIONS_IDXは第７→第１の順に結合セルのアドレスが入っている
				int[] entry = REGIONS_IDX[i];
				for (int k = 0; k < sheet.getNumMergedRegions(); k++) {
					// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//					CellRangeAddress address = sheet.getMergedRegion(k);
					CellRangeAddress address =  sheet.getMergedRegion(k);
					// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
					int row = address.getFirstRow();
					int col = address.getFirstColumn();
					if (row == entry[0] && col == entry[1]) {
						sheet.removeMergedRegion(k);
					}
				}
			}

			// 従業員明細部分の処理(削除)
			int delRowCount = BLANK_TEAM_CNT * (TEAM_PER_JGI_MAX_CNT + 1); // +1は合計行
			// mod Start 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
			//poiBean.removeRow(jRow, delRowCount);
			poiBean.removeEndRow(jRow);
			// mod End 2022/9/12  H.Futagami バックログNo.13　POIバージョンアップ対応
		}
		return jRow;
	}

// del start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定　テンプレートから串刺し関数削除したため本関数は不要
//	/**
//	 * 串刺し関数をクリアする。
//	 */
//	public void clearFunction() {
//
//		// 営業所理論計画UH①
//		clearFunc(OFFICE_START_POS[0], T1_PLAN_COL_UH);
//
//		// 営業所理論計画UH②
//		clearFunc(OFFICE_START_POS[0], T2_PLAN_COL_UH);
//
//		// 営業所営業所案UH
//		clearFunc(OFFICE_START_POS[0], OFFICE_PLAN_COL_UH);
//
//		// 営業所決定欄UH
//		clearFunc(OFFICE_START_POS[0], FINAL_PLAN_COL_UH);
//
//		// 営業所理論計画P①
//		clearFunc(OFFICE_START_POS[0], T1_PLAN_COL_P);
//
//		// 営業所理論計画P②
//		clearFunc(OFFICE_START_POS[0], T2_PLAN_COL_P);
//
//		// 営業所営業所案P
//		clearFunc(OFFICE_START_POS[0], OFFICE_PLAN_COL_P);
//
//		// 営業所決定欄P
//		clearFunc(OFFICE_START_POS[0], FINAL_PLAN_COL_P);
//
//		// 営業所前期実績UH
//		clearFunc(OFFICE_START_POS[0], AD_PERIOD_COL_UH);
//
//		// 営業所当期計画UH
//		clearFunc(OFFICE_START_POS[0], CR_PLAN_COL_UH);
//
//		// 営業所当期実績UH
//		clearFunc(OFFICE_START_POS[0], CR_PERIOD_COL_UH);
//
//		// 営業所前期実績P
//		clearFunc(OFFICE_START_POS[0], AD_PERIOD_COL_P);
//
//		// 営業所当期計画P
//		clearFunc(OFFICE_START_POS[0], CR_PLAN_COL_P);
//
//		// 営業所当期実績P
//		clearFunc(OFFICE_START_POS[0], CR_PERIOD_COL_P);
//
//		// チーム名称の開始行、列
//		int tRow = TEAM_START_POS[0];
//		int jRow = JGI_START_POS[0];
//
//		for (SosMst team : teamMap.values()) {
//
//			if (LOG.isDebugEnabled()) {
//				LOG.debug("clearTeam tCode=" + team.getTeamCode());
//			}
//
//			// チーム計理論計画UH①
//			clearFunc(tRow, T1_PLAN_COL_UH);
//
//			// チーム計理論計画UH②
//			clearFunc(tRow, T2_PLAN_COL_UH);
//
//			// チーム計営業所UH
//			clearFunc(tRow, OFFICE_PLAN_COL_UH);
//
//			// チーム計決定欄UH
//			clearFunc(tRow, FINAL_PLAN_COL_UH);
//
//			// チーム計理論計画P①
//			clearFunc(tRow, T1_PLAN_COL_P);
//
//			// チーム計理論計画P②
//			clearFunc(tRow, T2_PLAN_COL_P);
//
//			// チーム計営業所P
//			clearFunc(tRow, OFFICE_PLAN_COL_P);
//
//			// チーム計決定欄P
//			clearFunc(tRow, FINAL_PLAN_COL_P);
//
//			// チーム前期実績UH
//			clearFunc(tRow, AD_PERIOD_COL_UH);
//
//			// チーム当期計画UH
//			clearFunc(tRow, CR_PLAN_COL_UH);
//
//			// チーム当期実績UH
//			clearFunc(tRow, CR_PERIOD_COL_UH);
//
//			// チーム前期実績P
//			clearFunc(tRow, AD_PERIOD_COL_P);
//
//			// チーム当期計画P
//			clearFunc(tRow, CR_PLAN_COL_P);
//
//			// チーム当期実績P
//			clearFunc(tRow, CR_PERIOD_COL_P);
//
//			// 行送り
//			tRow++;
//
//			// チーム内の従業員
//			for (int i = 0; i < TEAM_PER_JGI_MAX_CNT; i++) {
//
//				// 従業員理論計画UH①
//				clearFunc(jRow, T1_PLAN_COL_UH);
//
//				// 従業員理論計画UH②
//				clearFunc(jRow, T2_PLAN_COL_UH);
//
//				// 従業員営業所案UH
//				clearFunc(jRow, OFFICE_PLAN_COL_UH);
//
//				// 従業員決定欄UH
//				clearFunc(jRow, FINAL_PLAN_COL_UH);
//
//				// 従業員理論計画P①
//				clearFunc(jRow, T1_PLAN_COL_P);
//
//				// 従業員理論計画P②
//				clearFunc(jRow, T2_PLAN_COL_P);
//
//				// 従業員営業所案P
//				clearFunc(jRow, OFFICE_PLAN_COL_P);
//
//				// 従業員決定欄P
//				clearFunc(jRow, FINAL_PLAN_COL_P);
//
//				// 従業員前期実績UH
//				clearFunc(jRow, AD_PERIOD_COL_UH);
//
//				// 従業員当期計画UH
//				clearFunc(jRow, CR_PLAN_COL_UH);
//
//				// 従業員当期実績UH
//				clearFunc(jRow, CR_PERIOD_COL_UH);
//
//				// 従業員前期実績P
//				clearFunc(jRow, AD_PERIOD_COL_P);
//
//				// 従業員当期計画P
//				clearFunc(jRow, CR_PLAN_COL_P);
//
//				// 従業員当期実績P
//				clearFunc(jRow, CR_PERIOD_COL_P);
//
//				// 次行へ送る
//				jRow++;
//			}
//
//			// 次行へ送る(合計行)
//			jRow++;
//		}
//	}
// del end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定　テンプレートから串刺し関数削除したため本関数は不要

	/**
	 * 検索結果データのExcelファイルへの出力を実行する。<br>
	 * 品目毎のシートを作成する。
	 *
	 * @param allRowCnt 行数
	 * @param prod 対象品目
	 * @param officePlan 対象品目の営業所計画
	 * @param dList 該当営業所配下の、納入実績(雑担当込)のリスト
	 * @param teamPlanMap チーム別計画を格納したマップ(キー：組織コード(チーム)、値：チーム別計画)
	 * @param mrPlanMap 担当者別計画を格納したマップ(キー：従業員番号、値：担当者別計画)
	 */
	public void execute(int allRowCnt, PlannedProd prod, OfficePlan officePlan, List<DeliveryResultMr> dList, Map<String, TeamPlan> teamPlanMap
			, Map<Integer, MrPlan> mrPlanMap, String unitYen, String headerUh, String headerP) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("MrTeamReportExportLogic start...");
			LOG.debug("allRowCnt=" + allRowCnt);
			LOG.debug("prod=" + prod);
			LOG.debug("officePlan=" + officePlan);
			LOG.debug("dList=" + dList);
			LOG.debug("teamPlanMap=" + teamPlanMap);
			LOG.debug("mrPlanMap=" + mrPlanMap);
		}

		// 品目名称設定
		String prodName = prod.getProdName();
		poiBean.setSheetName(prodName);
		poiBean.setCellData(prodName, PROD_NAME_POS);
		// 単位名称設定
		poiBean.setCellData(unitYen, UNIT_YEN_NAME_POS);
		// ヘッダUH名称設定
		poiBean.setCellData(headerUh, HEADER_UH_NAME_POS);
		// ヘッダP名称設定
		poiBean.setCellData(headerP, HEADER_P_NAME_POS);


		// 担当者別納入実績マップを元にチーム別納入実績マップを作成する
		// チーム別納入実績を格納する
		// キー：組織コード(チーム)
		// 値：[0]=前期実績UH、[1]=当期計画UH、[2]=当期実績UH、[3]=前期実績P、[4]=当期計画P、[5]=当期実績P
		Map<String, Long[]> teamDMap = new LinkedHashMap<String, Long[]>();

		// 従業員別納入実績を格納する(キー：従業員番号、値：担当者別納入実績[0]=UH[1]=P)
		Map<Integer, MonNnu[]> mrDMap = new LinkedHashMap<Integer, MonNnu[]>();

		// ----------------------------
		// 営業所計行
		// ----------------------------

		Long officeValueUH = null;
		Long officeValueP = null;
		if (officePlan != null) {
			officeValueUH = ConvertUtil.parseMoneyToThousandUnit(officePlan.getPlannedValueUhY());
			officeValueP = ConvertUtil.parseMoneyToThousandUnit(officePlan.getPlannedValuePY());
		}

		// 営業所理論計画UH①
		setCell(officeValueUH, OFFICE_START_POS[0], T1_PLAN_COL_UH);

		// 営業所理論計画UH②
		setCell(officeValueUH, OFFICE_START_POS[0], T2_PLAN_COL_UH);

		// 営業所営業所案UH
		setCell(officeValueUH, OFFICE_START_POS[0], OFFICE_PLAN_COL_UH);

		// 営業所決定欄UH
		setCell(officeValueUH, OFFICE_START_POS[0], FINAL_PLAN_COL_UH);

		// 営業所理論計画P①
		setCell(officeValueP, OFFICE_START_POS[0], T1_PLAN_COL_P);

		// 営業所理論計画P②
		setCell(officeValueP, OFFICE_START_POS[0], T2_PLAN_COL_P);

		// 営業所営業所案P
		setCell(officeValueP, OFFICE_START_POS[0], OFFICE_PLAN_COL_P);

		// 営業所決定欄P
		setCell(officeValueP, OFFICE_START_POS[0], FINAL_PLAN_COL_P);

		// --------------------------------------------------------------------------
		// 以下、実績の集計。雑担当込みの実績を担当者単位で保持。
		// 営業所計は雑担込みの値。チーム別と従業員別は雑担を含まない値。
		// 実績リストをループして営業所別、チーム別、担当者別の値を作成している。
		// --------------------------------------------------------------------------
		// 営業所集計用変数
		Long adPeriodUH = null;
		Long crPlanUH = null;
		Long crPeriodUH = null;
		Long adPeriodP = null;
		Long crPlanP = null;
		Long crPeriodP = null;

		if (dList != null) {

			for (DeliveryResultMr d : dList) {
				MonNnu m = d.getMonNnu();
				if (m == null) {
					continue;
				}

				String sosCd = jgiTeamMap.get(d.getJgiNo());
				if (d.getInsType() == InsType.UH) {

					// 営業所納入実績
					adPeriodUH = MathUtil.add(adPeriodUH, m.getAdvancePeriod());
					crPlanUH = MathUtil.add(crPlanUH, m.getCurrentPlanValue());
					crPeriodUH = MathUtil.add(crPeriodUH, m.getCurrentPeriod());

					// チーム別と従業員別は雑担当分を除外
					if (d.getSloppyChargeFlg() == false) {

						// チーム別納入実績
						if (StringUtils.isNotBlank(sosCd)) {
							Long[] tArray = teamDMap.get(sosCd);
							if (tArray == null) {
								tArray = new Long[6];
							}
							tArray[0] = MathUtil.add(tArray[0], m.getAdvancePeriod());
							tArray[1] = MathUtil.add(tArray[1], m.getCurrentPlanValue());
							tArray[2] = MathUtil.add(tArray[2], m.getCurrentPeriod());
							teamDMap.put(sosCd, tArray);
						}

						// 従業員別納入実績
						MonNnu[] mArray = mrDMap.get(d.getJgiNo());
						if (mArray == null) {
							mArray = new MonNnu[2];
						}
						mArray[0] = m;
						mrDMap.put(d.getJgiNo(), mArray);
					}

				} else if (d.getInsType() == InsType.P) {

					// 営業所納入実績
					adPeriodP = MathUtil.add(adPeriodP, m.getAdvancePeriod());
					crPlanP = MathUtil.add(crPlanP, m.getCurrentPlanValue());
					crPeriodP = MathUtil.add(crPeriodP, m.getCurrentPeriod());

					// チーム別と従業員別は雑担当分を除外
					if (d.getSloppyChargeFlg() == false) {

						// チーム別納入実績
						if (StringUtils.isNotBlank(sosCd)) {
							Long[] tArray = teamDMap.get(sosCd);
							if (tArray == null) {
								tArray = new Long[6];
							}
							tArray[3] = MathUtil.add(tArray[3], m.getAdvancePeriod());
							tArray[4] = MathUtil.add(tArray[4], m.getCurrentPlanValue());
							tArray[5] = MathUtil.add(tArray[5], m.getCurrentPeriod());
							teamDMap.put(sosCd, tArray);
						}

						// 従業員別納入実績
						MonNnu[] mArray = mrDMap.get(d.getJgiNo());
						if (mArray == null) {
							mArray = new MonNnu[2];
						}
						mArray[1] = m;
						mrDMap.put(d.getJgiNo(), mArray);
					}
				}
			}

			// 営業所前期実績UH
			setCellD1000(adPeriodUH, OFFICE_START_POS[0], AD_PERIOD_COL_UH);

			// 営業所当期計画UH
			setCellD1000(crPlanUH, OFFICE_START_POS[0], CR_PLAN_COL_UH);

			// 営業所当期実績UH
			setCellD1000(crPeriodUH, OFFICE_START_POS[0], CR_PERIOD_COL_UH);

			// 営業所前期実績P
			setCellD1000(adPeriodP, OFFICE_START_POS[0], AD_PERIOD_COL_P);

			// 営業所当期計画P
			setCellD1000(crPlanP, OFFICE_START_POS[0], CR_PLAN_COL_P);

			// 営業所当期実績P
			setCellD1000(crPeriodP, OFFICE_START_POS[0], CR_PERIOD_COL_P);
		}

		// ----------------------------
		// チーム計行、従業員行
		// ----------------------------

		// チーム名称の開始行、列
		int tRow = TEAM_START_POS[0];
		int jRow = JGI_START_POS[0];
		for (SosMst team : teamMap.values()) {

			TeamPlan tPlan = teamPlanMap.get(team.getSosCd());

			if (tPlan != null) {

				Long value = null;

				// チーム計特定施設個別計画UH
				Long sValueUH = tPlan.getSpecialInsPlanValueUhY();

				// チーム計特定施設個別計画P
				Long sValueP = tPlan.getSpecialInsPlanValuePY();

				// チーム計理論計画UH①
				value = ConvertUtil.parseMoneyToThousandUnit(MathUtil.add(tPlan.getTheoreticalValueUh1(), sValueUH));
				setCell(value, tRow, T1_PLAN_COL_UH);

				// チーム計理論計画UH②
				value = ConvertUtil.parseMoneyToThousandUnit(MathUtil.add(tPlan.getTheoreticalValueUh2(), sValueUH));
				setCell(value, tRow, T2_PLAN_COL_UH);

				// チーム計営業所UH
				value = ConvertUtil.parseMoneyToThousandUnit(tPlan.getOfficeValueUhY());
				setCell(value, tRow, OFFICE_PLAN_COL_UH);

				// チーム計決定欄UH
				value = ConvertUtil.parseMoneyToThousandUnit(tPlan.getPlannedValueUhY());
				setCell(value, tRow, FINAL_PLAN_COL_UH);

				// チーム計理論計画P①
				value = ConvertUtil.parseMoneyToThousandUnit(MathUtil.add(tPlan.getTheoreticalValueP1(), sValueP));
				setCell(value, tRow, T1_PLAN_COL_P);

				// チーム計理論計画P②
				value = ConvertUtil.parseMoneyToThousandUnit(MathUtil.add(tPlan.getTheoreticalValueP2(), sValueP));
				setCell(value, tRow, T2_PLAN_COL_P);

				// チーム計営業所P
				value = ConvertUtil.parseMoneyToThousandUnit(tPlan.getOfficeValuePY());
				setCell(value, tRow, OFFICE_PLAN_COL_P);

				// チーム計決定欄P
				value = ConvertUtil.parseMoneyToThousandUnit(tPlan.getPlannedValuePY());
				setCell(value, tRow, FINAL_PLAN_COL_P);
			}

			Long[] teamDArray = teamDMap.get(team.getSosCd());
			if (teamDArray != null) {

				Long value = null;

				// チーム前期実績UH
				value = teamDArray[0];
				setCellD1000(value, tRow, AD_PERIOD_COL_UH);

				// チーム当期計画UH
				value = teamDArray[1];
				setCellD1000(value, tRow, CR_PLAN_COL_UH);

				// チーム当期実績UH
				value = teamDArray[2];
				setCellD1000(value, tRow, CR_PERIOD_COL_UH);

				// チーム前期実績P
				value = teamDArray[3];
				setCellD1000(value, tRow, AD_PERIOD_COL_P);

				// チーム当期計画P
				value = teamDArray[4];
				setCellD1000(value, tRow, CR_PLAN_COL_P);

				// チーム当期実績P
				value = teamDArray[5];
				setCellD1000(value, tRow, CR_PERIOD_COL_P);
			}

			// 行送り
			tRow++;

			// チーム内の従業員
			List<JgiMst> jgiList = jgiListMap.get(team.getSosCd());
			int jgiSize = jgiList.size();
			for (int i = 0; i < TEAM_PER_JGI_MAX_CNT; i++) {
				if (i < jgiSize) {

					// 対象の従業員
					JgiMst jgi = jgiList.get(i);

					Integer jgiNo = jgi.getJgiNo();
					MrPlan mPlan = mrPlanMap.get(jgiNo);

					if (mPlan != null) {

						Long value = null;

						// チーム計特定施設個別計画UH
						Long sValueUH = mPlan.getSpecialInsPlanValueUhY();

						// チーム計特定施設個別計画P
						Long sValueP = mPlan.getSpecialInsPlanValuePY();

						// 従業員理論計画UH①
						value = ConvertUtil.parseMoneyToThousandUnit(MathUtil.add(mPlan.getTheoreticalValueUh1(), sValueUH));
						setCell(value, jRow, T1_PLAN_COL_UH);

						// 従業員理論計画UH②
						value = ConvertUtil.parseMoneyToThousandUnit(MathUtil.add(mPlan.getTheoreticalValueUh2(), sValueUH));
						setCell(value, jRow, T2_PLAN_COL_UH);

						// 従業員営業所案UH
						value = ConvertUtil.parseMoneyToThousandUnit(mPlan.getOfficeValueUhY());
						setCell(value, jRow, OFFICE_PLAN_COL_UH);

						// 従業員決定欄UH
						value = ConvertUtil.parseMoneyToThousandUnit(mPlan.getPlannedValueUhY());
						setCell(value, jRow, FINAL_PLAN_COL_UH);

						// 従業員理論計画P①
						value = ConvertUtil.parseMoneyToThousandUnit(MathUtil.add(mPlan.getTheoreticalValueP1(), sValueP));
						setCell(value, jRow, T1_PLAN_COL_P);

						// 従業員理論計画P②
						value = ConvertUtil.parseMoneyToThousandUnit(MathUtil.add(mPlan.getTheoreticalValueP2(), sValueP));
						setCell(value, jRow, T2_PLAN_COL_P);

						// 従業員営業所案P
						value = ConvertUtil.parseMoneyToThousandUnit(mPlan.getOfficeValuePY());
						setCell(value, jRow, OFFICE_PLAN_COL_P);

						// 従業員決定欄P
						value = ConvertUtil.parseMoneyToThousandUnit(mPlan.getPlannedValuePY());
						setCell(value, jRow, FINAL_PLAN_COL_P);
					}

					MonNnu[] mrDArray = mrDMap.get(jgiNo);
					if (mrDArray != null) {
						MonNnu mUH = mrDArray[0];
						MonNnu mP = mrDArray[1];

						Long value = null;

						if (mUH != null) {

							// 従業員前期実績UH
							value = mUH.getAdvancePeriod();
							setCellD1000(value, jRow, AD_PERIOD_COL_UH);

							// 従業員当期計画UH
							value = mUH.getCurrentPlanValue();
							setCellD1000(value, jRow, CR_PLAN_COL_UH);

							// 従業員当期実績UH
							value = mUH.getCurrentPeriod();
							setCellD1000(value, jRow, CR_PERIOD_COL_UH);
						}

						if (mP != null) {

							// 従業員前期実績P
							value = mP.getAdvancePeriod();
							setCellD1000(value, jRow, AD_PERIOD_COL_P);

							// 従業員当期計画P
							value = mP.getCurrentPlanValue();
							setCellD1000(value, jRow, CR_PLAN_COL_P);

							// 従業員当期実績P
							value = mP.getCurrentPeriod();
							setCellD1000(value, jRow, CR_PERIOD_COL_P);
						}
					}
				}

				// 次行へ送る
				jRow++;
			}

			// 次行へ送る(合計行)
			jRow++;
		}

		// 最後の共通設定
		this.lastSetting(allRowCnt);

		if (LOG.isDebugEnabled()) {
			LOG.debug("MrTeamReportExportLogic end...");
		}
	}

	/**
	 * 検索結果データのExcelファイルへの出力を実行する。<br>
	 *
	 * @param allRowCnt 行数
	 * @param oncSosFlg ONC組織フラグ
	 */
	public void execute(int allRowCnt, String prodName) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("MrTeamReportExportLogic MMP start...");
		}
		poiBean.setSheetName(prodName);
		poiBean.setCellData(prodName, PROD_NAME_POS);

		// 最後の共通設定
		this.lastSetting(allRowCnt);

		if (LOG.isDebugEnabled()) {
			LOG.debug("MrTeamReportExportLogic MMP end...");
		}
	}

	/**
	 * 印刷設定等、最後に設定する共通処理を定義する。
	 *
	 * @param allRowCount 全行数
	 */
	private void lastSetting(Integer allRowCount) {

		// 印刷設定
		poiBean.setPringArea(0, allRowCount, PRINT_COL_AREA[0], PRINT_COL_AREA[1], FIT_SIZE[0], FIT_SIZE[1]);

		// 自動計算の設定
		poiBean.setForceFormulaRecalculation(true);
	}

	/**
	 * 指定の値を指定セルに設定する。
	 *
	 * @param value 値
	 * @param row 行番号
	 * @param col 列番号
	 */
	private void setCell(Long value, int row, int col) {
		poiBean.setCellData(value, row, col);
	}

	/**
	 * 指定の値を/1000つき関数として指定セルに設定する。
	 *
	 * @param value 値(円単位)
	 * @param row 行番号
	 * @param col 列番号
	 */
	private void setCellD1000(Long value, int row, int col) {
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
	 * @param row 行番号
	 * @param col 列番号
	 */
	private void clearFunc(int row, int col) {
		poiBean.setCellFormula(null, row, col);
	}
}
