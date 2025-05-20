package jp.co.takeda.web.cmn.velocity;

import static jp.co.takeda.model.div.JknGrpId.*;

import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.model.div.JknGrpId;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;

/**
 * 納入計画システム向けメニューツール
 *
 * @author tkawabata
 */
public class MenuTool {

	// --------------------------------------------------------
	// 支援[メニュー項目]
	// --------------------------------------------------------

	// 画面ID定義
	private static final String VIEW_ID_UPLOAD_PLAN = "dps930C00F00";
	private static final String VIEW_ID_DOWNLOAD = "dps920C00F00";
	private static final String VIEW_ID_MIKAKUTOKU = "dps203C00F00";
	private static final String VIEW_ID_HAIBUN_JYOGAI = "dps201C00F00";
	private static final String VIEW_ID_TOKUTEI_SISETU_EI = "dps202C00F00";
	private static final String VIEW_ID_EIGYOSHO = "dps300C00F00";
	private static final String VIEW_ID_SISAN = "dps301C00F00";
	private static final String VIEW_ID_TANTOU = "dps302C00F00";
	private static final String VIEW_ID_TOKUTEI_SISETU_TN = "dps202C02F00";
	private static final String VIEW_ID_SISETU_HAIBUN = "dps400C00F00";
	private static final String VIEW_ID_SISETU_TOKUYAKU_TN = "dps401C00F00"; // 施設特約店別計画担当者一覧画面
	private static final String VIEW_ID_SISETU_TOKUYAKU_HN = "dps401C01F00"; // 施設特約店別計画品目一覧画面
	private static final String VIEW_ID_TOKUYAKU_HAIBUN = "dps500C00F00";
	private static final String VIEW_ID_SLIDE = "dps501C00F00";
	private static final String VIEW_ID_TOKUYAKU = "dps502C00F00";

	// 画面と表示文字列の組み合わせ定義
	private static final CodeAndValue DOWNLOAD = new CodeAndValue(VIEW_ID_DOWNLOAD, "ダウンロード");
	private static final CodeAndValue UPLOAD_PLAN = new CodeAndValue(VIEW_ID_UPLOAD_PLAN, "計画のアップロード");
	private static final CodeAndValue MIKAKUTOKU = new CodeAndValue(VIEW_ID_MIKAKUTOKU, "未獲得市場の編集");
	private static final CodeAndValue HAIBUN_JYOGAI = new CodeAndValue(VIEW_ID_HAIBUN_JYOGAI, "試算・配分除外施設の登録");
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
	private static final CodeAndValue TOKUTEI_SISETU_EI  = new CodeAndValue(VIEW_ID_TOKUTEI_SISETU_EI, "特定施設の計画立案<br>(エリア案)");
	private static final CodeAndValue EIGYOSHO           = new CodeAndValue(VIEW_ID_EIGYOSHO, "エリア計画の登録");
	private static final CodeAndValue SISAN              = new CodeAndValue(VIEW_ID_SISAN, "エリア・担当者別計画の試算");
	private static final CodeAndValue TANTOU             = new CodeAndValue(VIEW_ID_TANTOU, "エリア・担当者別計画の編集");
//	private static final CodeAndValue TOKUTEI_SISETU_EI  = new CodeAndValue(VIEW_ID_TOKUTEI_SISETU_EI, "特定施設の計画立案<br>(営業所案)");
//	private static final CodeAndValue EIGYOSHO           = new CodeAndValue(VIEW_ID_EIGYOSHO, "営業所計画の登録");
//	private static final CodeAndValue SISAN              = new CodeAndValue(VIEW_ID_SISAN, "営業所エリア・担当者別計画の試算");
//	private static final CodeAndValue TANTOU             = new CodeAndValue(VIEW_ID_TANTOU, "営業所エリア・担当者別計画の編集");
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
	private static final CodeAndValue TOKUTEI_SISETU_TN  = new CodeAndValue(VIEW_ID_TOKUTEI_SISETU_TN, "特定施設の計画立案<br>(担当者案)");
	private static final CodeAndValue SISETU_HAIBUN      = new CodeAndValue(VIEW_ID_SISETU_HAIBUN, "施設特約店別計画への配分");
	private static final CodeAndValue SISETU_TOKUYAKU_TN = new CodeAndValue(VIEW_ID_SISETU_TOKUYAKU_TN, "施設特約店別計画の編集"); //施設特約店別計画担当者一覧画面
	private static final CodeAndValue SISETU_TOKUYAKU_HN = new CodeAndValue(VIEW_ID_SISETU_TOKUYAKU_HN, "施設特約店別計画の編集"); //施設特約店別計画品目一覧画面
	private static final CodeAndValue TOKUYAKU_HAIBUN    = new CodeAndValue(VIEW_ID_TOKUYAKU_HAIBUN, "特約店別計画への配分");
	private static final CodeAndValue SLIDE              = new CodeAndValue(VIEW_ID_SLIDE, "施設特約店別計画から<br>特約店別計画へのスライド");
	private static final CodeAndValue TOKUYAKU           = new CodeAndValue(VIEW_ID_TOKUYAKU, "特約店別計画の編集");

	// --------------------------------------------------------
	// 管理[メニュー項目]
	// --------------------------------------------------------
	// 画面ID定義
	private static final String VIEW_ID_SOSIKI_EACH_SOS = "dpm100C00F00";
	private static final String VIEW_ID_SOSIKI_EACH_HINMOKU = "dpm101C00F00";
	private static final String VIEW_ID_INS_EACH_SOS = "dpm200C00F00";
	private static final String VIEW_ID_INS_EACH_HINMOKU = "dpm201C00F00";
	private static final String VIEW_ID_INSWS_EACH_SOS = "dpm400C00F00";
	private static final String VIEW_ID_INSWS_EACH_HINMOKU = "dpm401C00F00";
	private static final String VIEW_ID_WS_EACH_SOS = "dpm300C00F00";
	private static final String VIEW_ID_WS_EACH_HINMOKU = "dpm301C00F00";
	private static final String VIEW_ID_MONTH_SOSIKI_EACH_SOS = "dpm500C00F00";
	private static final String VIEW_ID_MONTH_SOSIKI_EACH_HINMOKU = "dpm501C00F00";
	private static final String VIEW_ID_MONTH_INS_EACH_SOS = "dpm600C00F00";
	private static final String VIEW_ID_MONTH_INS_EACH_HINMOKU = "dpm601C00F00";

	// 画面と表示文字列の組み合わせ定義
	private static final CodeAndValue KANRI_IYAKU_SOSIKI = new CodeAndValue(VIEW_ID_SOSIKI_EACH_SOS, "組織別");
	private static final CodeAndValue KANRI_IYAKU_SOSIKI_HINMOKU = new CodeAndValue(VIEW_ID_SOSIKI_EACH_HINMOKU, "品目別");
	private static final CodeAndValue KANRI_IYAKU_INS = new CodeAndValue(VIEW_ID_INS_EACH_SOS, "組織別");
	private static final CodeAndValue KANRI_IYAKU_INS_HINMOKU = new CodeAndValue(VIEW_ID_INS_EACH_HINMOKU, "品目別");
	private static final CodeAndValue KANRI_IYAKU_INSWS = new CodeAndValue(VIEW_ID_INSWS_EACH_SOS, "組織別");
	private static final CodeAndValue KANRI_IYAKU_INSWS_HINMOKU = new CodeAndValue(VIEW_ID_INSWS_EACH_HINMOKU, "品目別");
	private static final CodeAndValue KANRI_IYAKU_WS = new CodeAndValue(VIEW_ID_WS_EACH_SOS, "組織別");
	private static final CodeAndValue KANRI_IYAKU_WS_HINMOKU = new CodeAndValue(VIEW_ID_WS_EACH_HINMOKU, "品目別");
	private static final CodeAndValue KANRI_IYAKU_SOSIKI_MONTHLY = new CodeAndValue(VIEW_ID_MONTH_SOSIKI_EACH_SOS, "組織別");
	private static final CodeAndValue KANRI_IYAKU_SOSIKI_HINMOKU_MONTHLY = new CodeAndValue(VIEW_ID_MONTH_SOSIKI_EACH_HINMOKU, "品目別");
	private static final CodeAndValue KANRI_IYAKU_INS_MONTHLY = new CodeAndValue(VIEW_ID_MONTH_INS_EACH_SOS, "組織別");
	private static final CodeAndValue KANRI_IYAKU_INS_HINMOKU_MONTHLY = new CodeAndValue(VIEW_ID_MONTH_INS_EACH_HINMOKU, "品目別");

	/**
	 * 組織別
	 */
	private final static List<CodeAndValue> LINKLIST_KANRI_SOSIKI;

	/**
	 * 施設別
	 */
	private final static List<CodeAndValue> LINKLIST_KANRI_INS;

	/**
	 * 施設特約店別
	 */
	private final static List<CodeAndValue> LINKLIST_KANRI_INSWS;

	/**
	 * 特約店別
	 */
	private final static List<CodeAndValue> LINKLIST_KANRI_WS;

	/**
	 * 月別計画の組織別
	 */
	private final static List<CodeAndValue> LINKLIST_KANRI_MONTH_SOSIKI;

	/**
	 * 月別計画の施設別
	 */
	private final static List<CodeAndValue> LINKLIST_KANRI_MONTH_INS;

	static {

		LINKLIST_KANRI_SOSIKI = new ArrayList<CodeAndValue>(2);
		LINKLIST_KANRI_SOSIKI.add(KANRI_IYAKU_SOSIKI);
		LINKLIST_KANRI_SOSIKI.add(KANRI_IYAKU_SOSIKI_HINMOKU);

		LINKLIST_KANRI_INS = new ArrayList<CodeAndValue>(2);
		LINKLIST_KANRI_INS.add(KANRI_IYAKU_INS);
		LINKLIST_KANRI_INS.add(KANRI_IYAKU_INS_HINMOKU);

		LINKLIST_KANRI_INSWS = new ArrayList<CodeAndValue>(2);
		LINKLIST_KANRI_INSWS.add(KANRI_IYAKU_INSWS);
		LINKLIST_KANRI_INSWS.add(KANRI_IYAKU_INSWS_HINMOKU);

		LINKLIST_KANRI_WS = new ArrayList<CodeAndValue>(2);
		LINKLIST_KANRI_WS.add(KANRI_IYAKU_WS);
		LINKLIST_KANRI_WS.add(KANRI_IYAKU_WS_HINMOKU);

		LINKLIST_KANRI_MONTH_SOSIKI = new ArrayList<CodeAndValue>(2);
		LINKLIST_KANRI_MONTH_SOSIKI.add(KANRI_IYAKU_SOSIKI_MONTHLY);
		LINKLIST_KANRI_MONTH_SOSIKI.add(KANRI_IYAKU_SOSIKI_HINMOKU_MONTHLY);

		LINKLIST_KANRI_MONTH_INS = new ArrayList<CodeAndValue>(2);
		LINKLIST_KANRI_MONTH_INS.add(KANRI_IYAKU_INS_MONTHLY);
		LINKLIST_KANRI_MONTH_INS.add(KANRI_IYAKU_INS_HINMOKU_MONTHLY);

	}


	/**
	 * [支援]計画立案準備のメニューを取得する。
	 *
	 * @return ]計画立案準備のメニューを取得する
	 */
	public static List<CodeAndValue> getPlanningPreparationList() {
		List<CodeAndValue> menulist = new ArrayList<CodeAndValue>();
		DpUserInfo dpUserInfo = DpUserInfo.getDpUserInfo();
		DpUser dpUser = dpUserInfo.getSettingUser();
		switch (dpUser.getJokenKbn(JknGrpId.DOWNLOAD.getDbValue())) {
		case DO_NOT_USE:
			break;
		default:
			menulist.add(DOWNLOAD);
			break;
		}
		switch (dpUser.getJokenKbn(PLAN_UPLOAD.getDbValue())) {
		case DO_NOT_USE:
			break;
		default:
			menulist.add(UPLOAD_PLAN);
			break;
		}
		if(menulist.size()<= 0) {
			return null;
		}
		return menulist;
	}

	/**
	 * [支援]組織立案1/2のメニューを取得する。
	 *
	 * @return 組織立案1/2のメニュー
	 */

	public static List<CodeAndValue> getSienSosikiList() {
		List<CodeAndValue> menulist = new ArrayList<CodeAndValue>();
		DpUserInfo dpUserInfo = DpUserInfo.getDpUserInfo();
		DpUser dpUser = dpUserInfo.getSettingUser();
		switch (dpUser.getJokenKbn(JknGrpId.HAIBUN_JYOGAI.getDbValue())) {
		case DO_NOT_USE:
			break;
		default:
			menulist.add(HAIBUN_JYOGAI);
			break;
		}
		switch (dpUser.getJokenKbn(JknGrpId.MIKAKUTOKU.getDbValue())) {
		case DO_NOT_USE:
			break;
		default:
			menulist.add(MIKAKUTOKU);
			break;
		}
		switch (dpUser.getJokenKbn(JknGrpId.TOKUTEI_SISETU_OFFICE_PLAN.getDbValue())) {
		case DO_NOT_USE:
			break;
		default:
			menulist.add(TOKUTEI_SISETU_EI);
			break;
		}
		switch (dpUser.getJokenKbn(JknGrpId.OFFICE_PLAN.getDbValue())) {
		case DO_NOT_USE:
			break;
		default:
			menulist.add(EIGYOSHO);
			break;
		}
		switch (dpUser.getJokenKbn(JknGrpId.SISAN.getDbValue())) {
		case DO_NOT_USE:
			break;
		default:
			menulist.add(SISAN);
			break;
		}
		switch (dpUser.getJokenKbn(JknGrpId.MR_PLAN.getDbValue())) {
		case DO_NOT_USE:
			break;
		default:
			menulist.add(TANTOU);
			break;
		}

		if(menulist.size()<= 0) {
			return null;
		}
		return menulist;
	}

	/**
	 * [支援]施設特約店別計画のメニューを取得する。
	 *
	 * @return 施設特約店別計画のメニュー
	 */
	public static List<CodeAndValue> getSienSisetuTokuyakuList() {
		List<CodeAndValue> menulist = new ArrayList<CodeAndValue>();
		DpUserInfo dpUserInfo = DpUserInfo.getDpUserInfo();
		DpUser dpUser = dpUserInfo.getSettingUser();
		switch (dpUser.getJokenKbn(JknGrpId.TOKUTEI_SISETU_MR_PLAN.getDbValue())) {
		case DO_NOT_USE:
			break;
		default:
			menulist.add(TOKUTEI_SISETU_TN);
			break;
		}

		switch (dpUser.getJokenKbn(JknGrpId.INS_TYTEN_PLAN_PROD_DIST_TARGET.getDbValue())) {
		case DO_NOT_USE:
			break;
		default:
			menulist.add(SISETU_HAIBUN);
			break;

		}

		switch (dpUser.getJokenKbn(JknGrpId.INS_TYTEN_PLAN.getDbValue())) {
		case DO_NOT_USE:
			break;
		default:
			switch (dpUser.getSosLvl(JknGrpId.INS_TYTEN_PLAN.getDbValue())) {
			case MR:
				menulist.add(SISETU_TOKUYAKU_HN);
				break;
			default:
				menulist.add(SISETU_TOKUYAKU_TN);
				break;
			}
			break;
		}

		if (menulist.size() <= 0) {
			return null;
		}
		return menulist;
	}

	/**
	 * [支援]特約店別計画のメニューを取得する。
	 *
	 * @return 特約店別計画のメニュー
	 */
	public static List<CodeAndValue> getSienTokuyakuList() {
		List<CodeAndValue> menulist = new ArrayList<CodeAndValue>();
		DpUserInfo dpUserInfo = DpUserInfo.getDpUserInfo();
		DpUser dpUser = dpUserInfo.getSettingUser();
		switch (dpUser.getJokenKbn(JknGrpId.TYTEN_PLAN_PROD.getDbValue())) {
		case DO_NOT_USE:
			break;
		default:
			menulist.add(TOKUYAKU_HAIBUN);
			break;
		}
		switch (dpUser.getJokenKbn(JknGrpId.TYTEN_PLAN_SLIDE.getDbValue())) {
		case DO_NOT_USE:
			break;
		default:
			menulist.add(SLIDE);
			break;
		}
		switch (dpUser.getJokenKbn(JknGrpId.TYTEN_PLAN.getDbValue())) {
		case DO_NOT_USE:
			break;
		default:
			menulist.add(TOKUYAKU);
			break;
		}

		if(menulist.size()<= 0) {
			return null;
		}
		return menulist;
	}



	/**
	 * [管理]組織別のメニューを取得する。
	 *
	 * @return 組織別のメニュー
	 */
	public static List<CodeAndValue> getKanriSosikiList() {
		DpUserInfo dpUserInfo = DpUserInfo.getDpUserInfo();
		DpUser dpUser = dpUserInfo.getSettingUser();
		switch (dpUser.getJokenKbn(VIEW_ID_SOSIKI_EACH_SOS)) {
		case DO_NOT_USE:
			return null;
		default:
			return LINKLIST_KANRI_SOSIKI;
		}
	}

	/**
	 * [管理]施設別のメニューを取得する。
	 *
	 * @return 施設別のメニュー
	 */
	public static List<CodeAndValue> getKanriInsList() {
		DpUserInfo dpUserInfo = DpUserInfo.getDpUserInfo();
		DpUser dpUser = dpUserInfo.getSettingUser();
		switch (dpUser.getJokenKbn(VIEW_ID_INS_EACH_SOS)) {
		case DO_NOT_USE:
			return null;
		default:
			return LINKLIST_KANRI_INS;
		}
	}

	/**
	 * [管理]施設特約店別のメニューを取得する。
	 *
	 * @return 施設特約店別のメニュー
	 */
	public static List<CodeAndValue> getKanriInsWsList() {
		DpUserInfo dpUserInfo = DpUserInfo.getDpUserInfo();
		DpUser dpUser = dpUserInfo.getSettingUser();
		switch (dpUser.getJokenKbn(VIEW_ID_INSWS_EACH_SOS)) {
		case DO_NOT_USE:
			return null;
		default:
			return LINKLIST_KANRI_INSWS;
		}
	}

	/**
	 * [管理]特約店別のメニューを取得する。
	 *
	 * @return 特約店別のメニュー
	 */
	public static List<CodeAndValue> getKanriWsList() {
		DpUserInfo dpUserInfo = DpUserInfo.getDpUserInfo();
		DpUser dpUser = dpUserInfo.getSettingUser();
		switch (dpUser.getJokenKbn(VIEW_ID_WS_EACH_SOS)) {
		case DO_NOT_USE:
			return null;
		default:
			return LINKLIST_KANRI_WS;
		}
	}


	/**
	 * [管理]月別計画の組織別のメニューを取得する。
	 *
	 * @return 月別計画の組織別のメニュー
	 */
	public static List<CodeAndValue> getMonthlyKanriSosikiList() {
		DpUserInfo dpUserInfo = DpUserInfo.getDpUserInfo();
		DpUser dpUser = dpUserInfo.getSettingUser();

		switch (dpUser.getJokenKbn(VIEW_ID_MONTH_SOSIKI_EACH_SOS)) {
		case DO_NOT_USE:
			return null;
		default:
			return LINKLIST_KANRI_MONTH_SOSIKI;
		}
	}

	/**
	 * [管理]月別計画の施設別のメニューを取得する。
	 *
	 * @return 月別計画の施設別のメニュー
	 */
	public static List<CodeAndValue> getMonthlyKanriInsList() {
		DpUserInfo dpUserInfo = DpUserInfo.getDpUserInfo();
		DpUser dpUser = dpUserInfo.getSettingUser();

		switch (dpUser.getJokenKbn(VIEW_ID_MONTH_INS_EACH_SOS)) {
		case DO_NOT_USE:
			return null;
		default:
			return LINKLIST_KANRI_MONTH_INS;
		}
	}

}
