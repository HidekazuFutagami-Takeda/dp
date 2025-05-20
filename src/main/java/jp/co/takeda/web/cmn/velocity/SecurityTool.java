package jp.co.takeda.web.cmn.velocity;

import java.util.List;

import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.JknGrp;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.JknGrpId;
import jp.co.takeda.model.div.JokenKbn;
import jp.co.takeda.model.div.PlannedType;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.model.div.Term;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.ConvertUtil;

/**
 * ユーザ情報にアクセスするためのVelocityTool<br>
 *
 * <pre>
 * Velocity側では例外のハンドリングをしないので、例外が起こらないように返す。
 * 以下のルールを設ける。
 * <li>極力String型で返すようにする。null等の事情で返せない時は空文字列""を返す。</li>
 * <li>String以外のオブジェクト型を返す場合、値がない場合はnull値を返す。(NPEを発生させない）</li>
 * <li>極力ピンポイントで欲しい情報まで加工して返す。例えば従業員情報そのものではなく、氏名を返すなど。</li>
 * </pre>
 *
 * @author tkawabata
 */
public class SecurityTool {

	private static final String VIEW_ID_SOS_EACH_PROD = "DPM10";

	/**
	 * 期管理情報の有無を返す。
	 *
	 * @return 期管理情報が設定されている場合に真
	 */
	public boolean isSysManageExist() {
		SysManage sysManage = getSysManage();
		if (sysManage != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 年度を返す。
	 *
	 * @return 年度
	 */
	public String getSysYear() {
		final String YEAR_BLANK = "";
		final String YEAR = "年";
		SysManage sysManage = getSysManage();
		if (sysManage == null) {
			return YEAR_BLANK;
		}
		String sysYear = sysManage.getSysYear();
		if (sysYear != null) {
			return sysYear + YEAR;
		}
		return YEAR_BLANK;
	}

	/**
	 * 上期/下期を返す。
	 *
	 * @return 上期/下期
	 */
	public String getSysTerm() {
		final String TERM_FIRST = "上期";
		final String TERM_SECOND = "下期";
		final String TERM_BLANK = "";
		SysManage sysManage = getSysManage();
		if (sysManage == null) {
			return TERM_BLANK;
		}
		Term term = sysManage.getSysTerm();
		if (Term.FIRST == term) {
			return TERM_FIRST;
		}
		if (Term.SECOND == term) {
			return TERM_SECOND;
		}
		return TERM_BLANK;
	}

	/**
	 * 支援/管理を返す。
	 *
	 * @return 支援/管理
	 */
	public String getSysClass() {
		final String SYS_CLASS_DPS = "計画立案支援";
		final String SYS_CLASS_DPM = "計画立案管理";
		final String SYS_CLASS_BLANK = "";
		SysManage sysManage = getSysManage();
		if (sysManage == null) {
			return SYS_CLASS_BLANK;
		}
		SysClass sysClass = sysManage.getSysClass();
		if (SysClass.DPS == sysClass) {
			return SYS_CLASS_DPS;
		}
		if (SysClass.DPM == sysClass) {
			return SYS_CLASS_DPM;
		}
		return SYS_CLASS_BLANK;
	}

	/**
	 * ユーザが参照権限を持っているか確認
	 *
	 * @param strJknGrpId 条件グループID文字列（画面ID）
	 * @return 権限を保持している場合に真
	 */
	public boolean hasAuthRefer(String strJknGrpId) {
		if (strJknGrpId == null) {
			return false;
		}
		return hasAuth(strJknGrpId, AuthType.REFER);
	}

	/**
	 * ユーザが編集権限を持っているか確認
	 *
	 * @param strJknGrpId 条件グループID文字列（画面ID）
	 * @param authType 権限
	 * @return 権限を保持している場合に真
	 */
	public boolean hasAuthEdit(String strJknGrpId) {
		if (strJknGrpId == null) {
			return false;
		}
		return hasAuth(strJknGrpId, AuthType.EDIT);
	}

	/**
	 * 設定されているユーザの権限有無を返す。
	 *
	 * @param strJknGrpId 条件グループID文字列（画面ID）
	 * @param authType 権限
	 * @return 権限を保持している場合に真
	 */
	private boolean hasAuth(String strJknGrpId, AuthType authType) {
		if (strJknGrpId == null) {
			return false;
		}
		if (authType == null) {
			return false;
		}
		// ログインユーザ情報取得
		DpUser dpUser = getSettingDpUser();
		if (dpUser != null) {
			JknGrpId jknGrpId = JknGrpId.getInstance(strJknGrpId);

			/*
			 * ログインユーザの画面権限条件リストから
			 * 画面に合うものを見つけて、権限を確認する
			 */
			for(JknGrp jknGrp: dpUser.getJknGrpList()) {
				if(jknGrp.getJknGrpId().equals(jknGrpId)) {
					if(authType.equals(AuthType.REFER) || authType.equals(AuthType.OUTPUT)) {
						if(jknGrp.getJokenKbn().equals(JokenKbn.DO_NOT_USE) == false) {
							return true;
						}
					}
					else {
						if(jknGrp.getJokenKbn().equals(JokenKbn.ALL_EDIT) || jknGrp.getJokenKbn().equals(JokenKbn.VAC_EDIT)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * 代行ユーザリストを返す。
	 *
	 * @return 代行ユーザリスト
	 */
	public List<CodeAndValue> getAltUserList() {
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null && userInfo.isOfficialUser()) {
			return userInfo.getAltUserList();
		} else {
			return null;
		}
	}

	/**
	 * 医薬にログイン中かどうかを返す。
	 *
	 * @return 医薬にログイン中の場合に真
	 */
	public boolean hasIyaku() {
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			return SysType.IYAKU.equals(userInfo.getSysType());
		} else {
			return false;
		}
	}

	/**
	 * ワクチンにログイン中かどうかを返す。
	 *
	 * @return ワクチンにログイン中の場合に真
	 */
	public boolean hasVaccine() {
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			return SysType.VACCINE.equals(userInfo.getSysType());
		} else {
			return false;
		}
	}

	/**
	 * 計画支援にログイン中かどうかを返す。
	 *
	 * @return 計画支援にログイン中の場合に真
	 */
	public boolean hasDps() {
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			return SysClass.DPS.equals(userInfo.getSysClass());
		} else {
			return false;
		}
	}

	/**
	 * 計画管理にログイン中かどうかを返す。
	 *
	 * @return 計画管理にログイン中の場合に真
	 */
	public boolean hasDpm() {
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			return SysClass.DPM.equals(userInfo.getSysClass());
		} else {
			return false;
		}
	}

	/**
	 * ログイン情報と、画面ごとに設定された最小・最大検索範囲を元に、<br>
	 * 組織・従業員が選択可能かどうかを返す。
	 *
	 * @param sosMaxSrchGetValue 最大検索範囲
	 * @return 組織・従業員が選択可能の場合に真
	 */
	public boolean hasSosJgi(String sosMaxSrchGetValue, String screenId) {
		DpUser dpUser = getSettingDpUser();
		if (dpUser == null) {
			return false;
		}

        // 全社参照、支店参照の場合はtrue
        if(dpUser.isSosLvl(screenId, SosLvl.ALL) || dpUser.isSosLvl(screenId, SosLvl.BRANCH)) {
            return true;
        }
        // 最大検索範囲が支店(特約店部)の場合
        if ("01".equals(sosMaxSrchGetValue)) {
            return false;
        }

        // 営業所参照の場合はtrue
        if(dpUser.isSosLvl(screenId, SosLvl.OFFICE)) {
            return true;
        }
        // 最大検索範囲が営業所(エリア特約店G)の場合
        if ("02".equals(sosMaxSrchGetValue)) {
            return false;
        }

        // チーム参照の場合はtrue
        if(dpUser.isSosLvl(screenId, SosLvl.TEAM)) {
            return true;
        }
        // 最大検索範囲がチームの場合
        if ("03".equals(sosMaxSrchGetValue)) {
            return false;
        }

        // 担当者参照の場合はtrue
        if(dpUser.isSosLvl(screenId, SosLvl.MR)) {
            return false;
        }


		return false;
	}

	/**
	 * 参照レベルがMRより上か否か
	 * @param screenId
	 * @return
	 */
	public boolean isSosLvlGreaterThanMr(String screenId) {
		DpUser dpUser = getSettingDpUser();
		if (dpUser == null) {
			return false;
		}
        switch(dpUser.getSosLvl(screenId)) {
		case MR:
		case _ERROR:
			return false;

		default:
			return true;
        }
	}

	/**
	 * 設定されているユーザの従業員氏名を返す。
	 *
	 * @return 設定されているユーザの従業員氏名
	 */
	public String getSettingUserJgiName() {
		DpUser dpUser = getSettingDpUser();
		if (dpUser != null) {
			return dpUser.getJgiName();
		} else {
			return "";
		}
	}

	/**
	 * 設定されているユーザの従業員番号を返す。
	 *
	 * @return 設定されているユーザの従業員番号
	 */
	public String getSettingUserJgiNo() {
		DpUser dpUser = getSettingDpUser();
		if (dpUser != null) {
			return ConvertUtil.toString(dpUser.getJgiNo());
		} else {
			return "";
		}
	}

	/**
	 * デフォルト組織情報の組織コードを返す。
	 *
	 * @return デフォルト組織情報の組織コード
	 */
	public String getDefaultSosCd() {
		SosMst sosMst = getDefaultSosMst();
		if (sosMst != null) {
			return sosMst.getSosCd();
		} else {
			return "";
		}
	}

	/**
	 * デフォルト組織情報の組織名称を返す。
	 *
	 * @return デフォルト組織情報の組織名称
	 */
	public String getDefaultSosName() {
		SosMst sosMst = getDefaultSosMst();
		if (sosMst != null) {
			return sosMst.getSosName();
		} else {
			return "";
		}
	}

	/**
	 * デフォルト従業員情報の従業員コードを返す。
	 *
	 * @return デフォルト組織情報の組織コード
	 */
	public String getDefaultJgiNo() {
		JgiMst jgiMst = getDefaultJgiMst();
		if (jgiMst != null) {
			return jgiMst.getJgiNo().toString();
		} else {
			return "";
		}
	}

	/**
	 * デフォルト従業員情報の従業員氏名を返す。
	 *
	 * @return デフォルト従業員情報の従業員氏名
	 */
	public String getDefaultJgiName() {
		JgiMst jgiMst = getDefaultJgiMst();
		if (jgiMst != null) {
			return jgiMst.getJgiName();
		} else {
			return "";
		}
	}

	/**
	 * 営業所についての参照可否を返す
	 * @return
	 */
	public boolean getHasRefAuthEigyo() {
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo == null) {
			return false;
		}
		DpUser dpUser = userInfo.getSettingUser();
		switch (dpUser.getJokenKbn(VIEW_ID_SOS_EACH_PROD)) {
		case DO_NOT_USE:
			return false;
		case VAC_EDIT:
			return false;
		case VAC_REFER:
			return false;
		case ALL_EDIT:
			break;
		case ALL_REFER:
			break;
		default:
			return false;
		}
		SosMst sosMst = userInfo.getDefaultSosMst();
		if (sosMst == null) { // 組織がnullの場合は本社組織なので、参照可能(DpUserInfo.settingDefaultSosJgi参照)
			return true;
		}
		return sosMst.hasRefAuthEigyo();
	}

	/**
	 * 仕入についての参照可否を返す
	 * @return
	 */
	public boolean getHasRefAuthSiire() {
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo == null) {
			return false;
		}
		DpUser dpUser = userInfo.getSettingUser();
		switch (dpUser.getJokenKbn(VIEW_ID_SOS_EACH_PROD)) {
		case DO_NOT_USE:
			return false;
		case VAC_EDIT:
			return false;
		case VAC_REFER:
			return false;
		case ALL_EDIT:
			break;
		case ALL_REFER:
			break;
		default:
			return false;
		}
		SosMst sosMst = userInfo.getDefaultSosMst();
		if (sosMst == null) { // 組織がnullの場合は本社組織なので、参照可能(DpUserInfo.settingDefaultSosJgi参照)
			return true;
		}
		return sosMst.hasRefAuthSiire();
	}

	/**
	 * ワクチンについての参照可否を返す
	 * @return
	 */
	public boolean getHasRefAuthVaccine() {
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo == null) {
			return false;
		}
		DpUser dpUser = userInfo.getSettingUser();
		switch (dpUser.getJokenKbn(VIEW_ID_SOS_EACH_PROD)) {
		case DO_NOT_USE:
			return false;
		case VAC_EDIT:
			return true;
		case VAC_REFER:
			return true;
		case ALL_EDIT:
			break;
		case ALL_REFER:
			break;
		default:
			return false;
		}
		SosMst sosMst = userInfo.getDefaultSosMst();
		if (sosMst == null) { // 組織がnullの場合は本社組織なので、参照可能(DpUserInfo.settingDefaultSosJgi参照)
			return true;
		}
		return sosMst.hasRefAuthVaccine();
	}
	/**
	 * ワクチンのみ参照・編集可能の場合は、真を返す
	 * @param screenId
	 * @return
	 */
	public boolean isVaccineOnly(String screenId) {
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo == null) {
			return false;
		}
		DpUser dpUser = userInfo.getSettingUser();
		switch (dpUser.getJokenKbn(screenId)) {
		case VAC_EDIT:
			return true;
		case VAC_REFER:
			return true;
		default:
			return false;
		}
	}

	// ----------------------------------------
	// STATIC METHOD
	// ----------------------------------------

	/**
	 * 期管理情報を返す。
	 *
	 * @return 期管理情報
	 */
	public static SysManage getSysManage() {
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			return userInfo.getSysManage();
		} else {
			return null;
		}
	}

	/**
	 * 設定されているユーザ情報を返す。
	 *
	 * @return 設定されているユーザ情報
	 */
	public static DpUser getSettingDpUser() {
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			return userInfo.getSettingUser();
		} else {
			return null;
		}
	}

	/**
	 * デフォルト組織情報を返す。
	 *
	 * @return デフォルト組織情報
	 */
	public static SosMst getDefaultSosMst() {
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			return userInfo.getDefaultSosMst();
		} else {
			return null;
		}
	}

	/**
	 * デフォルト従業員情報を返す。
	 *
	 * @return デフォルト従業員情報
	 */
	public static JgiMst getDefaultJgiMst() {
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			return userInfo.getDefaultJgiMst();
		} else {
			return null;
		}
	}

	/**
	 * 実施計画区分を返す。
	 *
	 * @return 実施計画区分
	 */
	public static PlannedType getPlannedType() {
		return getSysManage().getPlannedType();
	}

	/**
	 * 実施計画名称を返す。
	 *
	 * @return 実施計画名称
	 */
	public static String getPlannedTypeName() {
		String name = "";
		switch (getPlannedType()) {
			case PLAN_SUPPORT:
				name = "計画支援";
				break;
			case PLANNED_1:
				name = "実施計画１";
				break;
			case PLANNED_2:
				name = "実施計画２";
				break;
		}
		return name;
	}
}
