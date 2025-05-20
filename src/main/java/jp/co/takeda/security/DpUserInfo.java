package jp.co.takeda.security;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.Model;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;

/**
 * 納入計画システムの一連のユーザ情報を管理するクラス<br>
 *
 * @author tkawabata
 */
public final class DpUserInfo extends Model<DpUserInfo> implements Cloneable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * スレッドにこのクラスを関連付ける
	 */
	private static final ThreadLocal<DpUserInfo> USER_INFO_POOL = new ThreadLocal<DpUserInfo>();

	/**
	 * スレッドに関連づけられたユーザ情報を取得する。
	 *
	 * @return スレッドに関連づけられたユーザ情報
	 */
	public static DpUserInfo getDpUserInfo() {
		return USER_INFO_POOL.get();
	}

	/**
	 * スレッドにユーザ情報を設定する。
	 *
	 * @param dpUserInfo ユーザ情報
	 */
	public static void setDpUserInfo(final DpUserInfo dpUserInfo) {
		USER_INFO_POOL.set(dpUserInfo);
	}

	/**
	 * スレッドに関連づけられたユーザ情報を削除する。<br>
	 */
	public static void clearDpUserInfo() {
		USER_INFO_POOL.remove();
	}

	/**
	 * コンストラクタ[正規の従業員でログインした場合]
	 *
	 * @param loginUser ログインした従業員
	 * @param altUserList 代行ユーザ従業員リスト
	 * @param sysClass 管理対象業務分類
	 * @param sysType 業務種別
	 * @param defaultSosMst デフォルト組織情報(NULL可)
	 * @param defaultJgiMst デフォルト従業員情報(NULL可)
	 */
	public DpUserInfo(final DpUser loginUser, final List<CodeAndValue> altUserList, SysClass sysClass, SysType sysType, SosMst defaultSosMst, JgiMst defaultJgiMst) {

		// Immutable Field
		this.loginUser = loginUser;
		this.altUserList = altUserList;
		this.officialUserFlg = true;
		this.sysClass = sysClass;
		this.sysType = sysType;
		this.dummyLoginFlg = false;

		// Mutable Field
		this.settingUser = loginUser;
		this.altUserSettingFlg = false;

		// Set Default SosMst, JgiMst
		switch(sysClass) {
		case DPM:
			settingDefaultSosJgi(this.loginUser.getJknGrpList().get(0).getSosLvl(), defaultSosMst, defaultJgiMst);
			break;
		case DPS:
			settingDefaultSosJgi_DPS(this.loginUser.getJknGrpList().get(0).getSosLvl(), defaultSosMst, defaultJgiMst);
			break;
		default:
			throw new SystemException(new Conveyance(LOGICAL_ERROR, "ありえないシステム区分"));
		}

	}

	/**
	 * コンストラクタ[代行ユーザでログインした場合]<br>
	 *
	 * <pre>
	 * 代行ユーザでログインした場合、代行ユーザを正規の従業員として扱う。
	 * また代行ユーザが本来の正規のユーザや他の代行ユーザに切り替えることは出来ない仕様。
	 * </pre>
	 *
	 * @param dpUser 正規のユーザ
	 * @param altUser 代行ユーザ
	 * @param sysClass 管理対象業務分類
	 * @param sysType 業務種別
	 * @param defaultSosMst デフォルト組織情報(NULL可)
	 * @param defaultJgiMst デフォルト従業員情報(NULL可)
	 */
	public DpUserInfo(final DpUser dpUser, final DpUser altUser, SysClass sysClass, SysType sysType, SosMst defaultSosMst, JgiMst defaultJgiMst) {

		// Immutable Field
		this.loginUser = dpUser;
		this.altUserList = null;
		this.officialUserFlg = false;
		this.sysClass = sysClass;
		this.sysType = sysType;
		this.dummyLoginFlg = false;

		// Mutable Field
		this.settingUser = altUser;
		this.altUserSettingFlg = true;
		this.defaultSosMst = defaultSosMst;
		this.defaultJgiMst = defaultJgiMst;

		// Set Default SosMst, JgiMst
		switch(sysClass) {
		case DPM:
			// [注意] 第1引数にloginuser（実ユーザ）の組織レベルを渡している。
			// 本来ならDPMもsettingUser（つまり代行ユーザ）の設定を渡すべきだと思われるが、
			// 2021年5月リリースのSTEP2にて特に問題が出ていない現状、変更は危険と判断。敢えてこのままとする。
			settingDefaultSosJgi(this.loginUser.getJknGrpList().get(0).getSosLvl(), defaultSosMst, defaultJgiMst);
			break;
		case DPS:
			// [注意] 第1引数にsettingUser（代行ユーザ）の組織レベルを渡している。DPMと差異があるが、こちらが正と思われる。
			settingDefaultSosJgi_DPS(this.settingUser.getJknGrpList().get(0).getSosLvl(), defaultSosMst, defaultJgiMst);
			break;
		default:
			throw new SystemException(new Conveyance(LOGICAL_ERROR, "ありえないシステム区分"));
		}
	}

	/**
	 * コンストラクタ[バッチでダミーログインする場合]<br>
	 *
	 * @param dummyUser ダミーユーザ
	 * @param sysClass 管理対象業務分類
	 * @param sysType 業務種別
	 */
	public DpUserInfo(final DpUser dummyUser, SysClass sysClass, SysType sysType) {

		// Immutable Field
		this.loginUser = dummyUser;
		this.altUserList = null;
		this.officialUserFlg = false;
		this.sysClass = sysClass;
		this.sysType = sysType;
		this.dummyLoginFlg = true;

		// Mutable Field
		this.settingUser = dummyUser;
		this.altUserSettingFlg = false;
		this.defaultSosMst = dummyUser.getSosMst();
		this.defaultJgiMst = dummyUser.getJgiMst();

		// Set Default SosMst, JgiMst
		switch(sysClass) {
		case DPM:
			settingDefaultSosJgi(this.loginUser.getJknGrpList().get(0).getSosLvl(), defaultSosMst, defaultJgiMst);
			break;
		case DPS:
			settingDefaultSosJgi_DPS(this.loginUser.getJknGrpList().get(0).getSosLvl(), defaultSosMst, defaultJgiMst);
			break;
		default:
			throw new SystemException(new Conveyance(LOGICAL_ERROR, "ありえないシステム区分"));
		}
}

	/**
	 * デフォルト組織従業員を再設定する。
	 *
	 * @param jokenSet 再設定する際の条件セット
	 * @param defaultSosMst 取得した組織情報
	 * @param defaultJgiMst 取得した従業員情報
	 */
	private void settingDefaultSosJgi(final SosLvl sosLvl, final SosMst defaultSosMst, final JgiMst defaultJgiMst) {

		// デフォルト組織の設定
		// 計画管理条件セットグループのSOS_LVLが全社以外の場合は設定する
		this.defaultSosMst = null;
		if(sosLvl.equals(SosLvl.ALL) == false) {
			this.defaultSosMst = defaultSosMst;
		}

		// デフォルト従業員の設定
		// 計画管理条件セットグループのSOS_LVLが担当者の場合は設定する
		this.defaultJgiMst = null;
		if(sosLvl.equals(SosLvl.MR) == false) { // （・・・falseになっているのは本来間違い。しかし、これでDPMのSTが完了してしまったので、このままでいく。DPS用に、本来あるべき条件のメソッドを別途追加した。
			this.defaultJgiMst = defaultJgiMst;
		}

		// 初期表示フラグの設定
		this.initDispFlg = true;
	}
	/**
	 * デフォルト組織従業員を再設定する。DPS用
	 *
	 * @param jokenSet 再設定する際の条件セット
	 * @param defaultSosMst 取得した組織情報
	 * @param defaultJgiMst 取得した従業員情報
	 */
	private void settingDefaultSosJgi_DPS(final SosLvl sosLvl, final SosMst defaultSosMst, final JgiMst defaultJgiMst) {

		// デフォルト組織の設定
		// 計画管理条件セットグループのSOS_LVLが全社以外の場合は設定する
		this.defaultSosMst = null;
		if(sosLvl.equals(SosLvl.ALL) == false) {
			this.defaultSosMst = defaultSosMst;
		}

		// デフォルト従業員の設定
		// 計画管理条件セットグループのSOS_LVLが担当者の場合は設定する
		this.defaultJgiMst = null;
		if(sosLvl.equals(SosLvl.MR) == true) {
			this.defaultJgiMst = defaultJgiMst;
		}

		// 初期表示フラグの設定
		this.initDispFlg = true;
	}

	//---------------------
	// Immutable Field
	// --------------------

	/**
	 * ログインした従業員
	 */
	private final DpUser loginUser;

	/**
	 * 代行ユーザ従業員リスト
	 */
	private final List<CodeAndValue> altUserList;

	/**
	 * 正規の従業員としてログインしたかを示すフラグ
	 */
	private final Boolean officialUserFlg;

	/**
	 * ダミーのログインかを示すフラグ
	 */
	private final Boolean dummyLoginFlg;

	/**
	 * 支援/管理
	 */
	private final SysClass sysClass;

	/**
	 * 医薬/ワクチン
	 */
	private final SysType sysType;

	//---------------------
	// Field
	// --------------------

	/**
	 * 設定中の従業員
	 */
	private DpUser settingUser;

	/**
	 * 代行ユーザに設定されているかを示すフラグ
	 */
	private Boolean altUserSettingFlg;

	/**
	 * デフォルト組織
	 */
	private SosMst defaultSosMst;

	/**
	 * デフォルト従業員
	 */
	private JgiMst defaultJgiMst;

	/**
	 * 期管理情報
	 */
	private SysManage sysManage;

	/**
	 * 初期表示フラグ
	 */
	private Boolean initDispFlg;


	//---------------------
	// Utility Method
	// --------------------

	/**
	 * 指定のユーザに切り替える。
	 *
	 * @param targetDpUser ユーザ
	 * @throws LogicalException 対象のユーザが切り替え対象に存在しない場合
	 */
	public void switchAltUserInfo(final DpUser targetDpUser) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (targetDpUser == null) {
			final String errMsg = "ユーザがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (!officialUserFlg) {
			final String errMsg = "正規の従業員ではないため、代行ユーザの取得は出来ない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (targetDpUser.getJgiNo().equals(this.loginUser.getJgiNo())) {
			final String errMsg = "正規の従業員を設定しようとしているためエラー";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		for (CodeAndValue codeAndValue : altUserList) {
			if (codeAndValue.getCode().equals(targetDpUser.getJgiNo().toString())) {
				this.settingUser = targetDpUser.clone();
				this.altUserSettingFlg = true;
				settingDefaultSosJgi(this.settingUser.getJknGrpList().get(0).getSosLvl(), this.settingUser.getSosMst().clone(), this.settingUser.getJgiMst().clone());
				return;
			}
		}

		final String errMsg = "対象の従業員が存在しない";
		throw new LogicalException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
	}

	/**
	 * 正規の従業員に戻す。
	 */
	public void switchOfficialUserInfo() {
		this.settingUser = this.loginUser;
		this.altUserSettingFlg = false;
		settingDefaultSosJgi(this.loginUser.getJknGrpList().get(0).getSosLvl(), this.loginUser.getSosMst().clone(), this.loginUser.getJgiMst().clone());
	}

	/**
	 * 代行ユーザの従業員リストを取得する。
	 *
	 * @return 代行ユーザの従業員リスト
	 */
	public List<CodeAndValue> getAltUserList() {
		if (altUserList != null) {
			return Collections.unmodifiableList(altUserList);
		} else {
			return null;
		}
	}

	/**
	 * デフォルト組織従業員の設定を元に戻す。
	 */
	public void reverseDefaultSosJgi() {
		DpUser reverseUser = this.getSettingUser();
		SosMst reverseSosMst = reverseUser.getSosMst().clone();
		JgiMst reverseJgiMst = reverseUser.getJgiMst().clone();
		settingDefaultSosJgi(reverseUser.getJknGrpList().get(0).getSosLvl(), reverseSosMst, reverseJgiMst);
	}

	/**
	 * デフォルト組織を設定する。
	 *
	 * @param defaultSosMst デフォルト組織
	 * @param defaultJgiMst デフォルト従業員
	 */
	public void setDefaultSosJgi(SosMst defaultSosMst, JgiMst defaultJgiMst) {
		this.defaultSosMst = defaultSosMst;
		this.defaultJgiMst = defaultJgiMst;
	}

	//---------------------
	// Getter
	// --------------------

	/**
	 * 正規の従業員番号を取得する。
	 *
	 * @return 正規の従業員の従業員番号
	 */
	public Integer getLoginUserJgiNo() {
		return this.loginUser.getJgiNo();
	}

	/**
	 * 設定中の従業員の従業員番号を取得する。
	 *
	 * @return 設定中の従業員の従業員番号
	 */
	public Integer getSettingUserJgiNo() {
		if (this.settingUser != null) {
			return this.settingUser.getJgiNo();
		} else {
			return null;
		}
	}

	/**
	 * 設定中の従業員を取得する。
	 *
	 * @return settingUser
	 */
	public DpUser getSettingUser() {
		if (this.settingUser != null) {
			return this.settingUser.clone();
		}
		return null;
	}

	/**
	 * ログイン中の従業員を取得する。
	 *
	 * @return settingUser
	 */
	public DpUser getLoginUser() {
		if (this.loginUser != null) {
			return this.loginUser.clone();
		}
		return null;
	}

	/**
	 * 代行ユーザが設定されているかを取得する。
	 *
	 * @return 代行ユーザが設定されているかを示すフラグ
	 */
	public Boolean isAltUserSetting() {
		return this.altUserSettingFlg;
	}

	/**
	 * 正規の従業員としてログインしたかを示すフラグを取得する。
	 *
	 * @return 正規の従業員としてログインしたかを示すフラグ
	 */
	public Boolean isOfficialUser() {
		return this.officialUserFlg;
	}

	/**
	 * 支援/管理を取得する。
	 *
	 * @return 支援/管理
	 */
	public SysClass getSysClass() {
		return sysClass;
	}

	/**
	 * 医薬/ワクチンを取得する。
	 *
	 * @return 医薬/ワクチン
	 */
	public SysType getSysType() {
		return sysType;
	}

	/**
	 * デフォルト組織を取得する。
	 *
	 * @return デフォルト組織
	 */
	public SosMst getDefaultSosMst() {
		return defaultSosMst;
	}

	/**
	 * デフォルト従業員を取得する。
	 *
	 * @return デフォルト従業員
	 */
	public JgiMst getDefaultJgiMst() {
		return defaultJgiMst;
	}

	/**
	 * ダミーのログインかを示すフラグを取得する。
	 *
	 * @return dummyLoginFlg ダミーのログインかを示すフラグ
	 */
	public Boolean getDummyLoginFlg() {
		return dummyLoginFlg;
	}

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 期管理情報を取得する。
	 *
	 * @return 期管理情報
	 */
	public SysManage getSysManage() {
		return sysManage;
	}

	/**
	 * 期管理情報を設定する。
	 *
	 * @param sysManage 期管理情報
	 */
	public void setSysManage(SysManage sysManage) {
		this.sysManage = sysManage;
	}

	/**
	 * 初期表示フラグを取得する。
	 *
	 * @return initDispFlg 初期表示フラグ
	 */
	public Boolean getInitDispFlg() {
		return initDispFlg;
	}

	/**
	 * 初期表示フラグを設定する。
	 *
	 * @param initDispFlg 初期表示フラグ
	 */
	public void setInitDispFlg(Boolean initDispFlg) {
		this.initDispFlg = initDispFlg;
	}

	@Override
	public DpUserInfo clone() {

		// 正規の従業員の場合
		if (this.officialUserFlg) {
			DpUser cLoginUser = this.loginUser.clone();

			SosMst cDefaultSosMst = this.getDefaultSosMst();
			if (cDefaultSosMst != null) {
				cDefaultSosMst = cDefaultSosMst.clone();
			}
			JgiMst cDefaultJgiMst = this.getDefaultJgiMst();
			if (cDefaultJgiMst != null) {
				cDefaultJgiMst = cDefaultJgiMst.clone();
			}
			DpUserInfo dpUserInfo = new DpUserInfo(cLoginUser, altUserList, this.sysClass, this.sysType, cDefaultSosMst, cDefaultJgiMst);
			dpUserInfo.initDispFlg = true;
			dpUserInfo.altUserSettingFlg = Boolean.valueOf(this.altUserSettingFlg);
			if (dpUserInfo.altUserSettingFlg) {
				DpUser cSettingUser = this.settingUser.clone();
				dpUserInfo.settingUser = cSettingUser;
			}
			return dpUserInfo;
		}

		// 代行の従業員の場合
		else {

			DpUser cLoginUser = this.loginUser.clone();
			DpUser cAltUser = this.settingUser.clone();
			SosMst cDefaultSosMst = this.getDefaultSosMst();
			if (cDefaultSosMst != null) {
				cDefaultSosMst = cDefaultSosMst.clone();
			}
			JgiMst cDefaultJgiMst = this.getDefaultJgiMst();
			if (cDefaultJgiMst != null) {
				cDefaultJgiMst = cDefaultJgiMst.clone();
			}
			DpUserInfo dpUserInfo = new DpUserInfo(cLoginUser, cAltUser, this.sysClass, this.sysType, cDefaultSosMst, cDefaultJgiMst);
			dpUserInfo.initDispFlg = true;
			return dpUserInfo;
		}
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

	@Override
	public int compareTo(DpUserInfo obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.loginUser, obj.loginUser).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && DpUserInfo.class.isAssignableFrom(entry.getClass())) {
			DpUserInfo obj = (DpUserInfo) entry;
			return new EqualsBuilder().append(this.loginUser, obj.loginUser).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.loginUser).toHashCode();
	}
}
