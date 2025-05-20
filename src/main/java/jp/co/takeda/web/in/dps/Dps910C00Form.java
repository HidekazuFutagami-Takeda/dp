package jp.co.takeda.web.in.dps;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.SosJgiListDto;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

/**
 * Dps910C00(組織・従業員検索画面)のフォームクラス
 *
 * @author khashimoto
 */
public class Dps910C00Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織・従業員検索結果のキー値
	 */
	public static final BoxKey SOS_JGI_LIST_DTO_KEY_R = new BoxKeyPerClassImpl(Dps910C00Form.class, SosJgiListDto.class);

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 適用関数名
	 */
	private String sosApplyFuncName;

	/**
	 * 検索パターン区分
	 */
	private String sosSrchPtnType;

	/**
	 * 検索最小階層取得値
	 */
	private String sosMinSrchValue;

	/**
	 * 検索最大階層取得値
	 */
	private String sosMaxSrchGetValue;

	/**
	 * 初期表示組織コード
	 */
	private String sosInitSosCodeValue;

	/**
	 * 全組織表示可能フラグ
	 */
	private boolean sosAllDispFlg;

	/**
	 * デフォルト組織変更フラグ
	 */
	private boolean defaultChangeFlg;

	/**
	 * 整形フラグ
	 */
	private boolean includeSeikei;

	/**
	 * ログインユーザ情報/デフォルト組織情報
	 */
	private DpUser dpUser;

	/**
	 * 参照レベル
	 */
	private SosLvl sosLvl;


	/**
	 * 整形を含めるかを取得する。
	 *
	 * @return true：含める、false：含めない
	 */
	public boolean isIncludeSeikei() {
		return includeSeikei;
	}

	/**
	 * 整形を含めるかを設定する。
	 *
	 * @param includeSeikei true：含める、false：含めない
	 */
	public void setIncludeSeikei(boolean includeSeikei) {
		this.includeSeikei = includeSeikei;
	}

	// OUTパラメータ
	/**
	 * 組織コード
	 */
	private String sosCd;

	/**
	 * 従業員番号
	 */
	private String jgiNo;

	// -------------------------------
	// getter & setter
	// -------------------------------

	/**
	 * 適用関数名を取得する。
	 *
	 * @return 適用関数名
	 */
	public String getSosApplyFuncName() {
		return sosApplyFuncName;
	}

	/**
	 * 適用関数名を設定する。
	 *
	 * @param sosApplyFuncName 適用関数名
	 */
	public void setSosApplyFuncName(String sosApplyFuncName) {
		this.sosApplyFuncName = sosApplyFuncName;
	}

	/**
	 * 検索パターン区分を取得する。
	 *
	 * @return 検索パターン区分
	 */
	public String getSosSrchPtnType() {
		return sosSrchPtnType;
	}

	/**
	 * 検索パターン区分を設定する。
	 *
	 * @param sosSrchPtnType 検索パターン区分
	 */
	public void setSosSrchPtnType(String sosSrchPtnType) {
		this.sosSrchPtnType = sosSrchPtnType;
	}

	/**
	 * 検索最小階層取得値を取得する。
	 *
	 * @return 検索最小階層取得値
	 */
	public String getSosMinSrchValue() {
		return sosMinSrchValue;
	}

	/**
	 * 検索最小階層取得値を設定する。
	 *
	 * @param sosMinSrchValue 検索最小階層取得値
	 */
	public void setSosMinSrchValue(String sosMinSrchValue) {
		this.sosMinSrchValue = sosMinSrchValue;
	}

	/**
	 * 検索最大階層取得値を取得する。
	 *
	 * @return 検索最大階層取得値
	 */
	public String getSosMaxSrchGetValue() {
		return sosMaxSrchGetValue;
	}

	/**
	 * 検索最大階層取得値を設定する。
	 *
	 * @param sosMaxSrchGetValue 検索最大階層取得値
	 */
	public void setSosMaxSrchGetValue(String sosMaxSrchGetValue) {
		this.sosMaxSrchGetValue = sosMaxSrchGetValue;
	}

	/**
	 * 初期表示組織コードを取得する。
	 *
	 * @return 初期表示組織コード
	 */
	public String getSosInitSosCodeValue() {
		return sosInitSosCodeValue;
	}

	/**
	 * 初期表示組織コードを設定する。
	 *
	 * @param sosInitSosCodeValue 初期表示組織コード
	 */
	public void setSosInitSosCodeValue(String sosInitSosCodeValue) {
		this.sosInitSosCodeValue = sosInitSosCodeValue;
	}

	/**
	 * 全組織表示可能フラグを取得する。
	 *
	 * @return 全組織表示可能フラグ
	 */
	public boolean getSosAllDispFlg() {
		return sosAllDispFlg;
	}

	/**
	 * 全組織表示可能フラグを設定する。
	 *
	 * @param sosAllDispFlg 全組織表示可能フラグ
	 */
	public void setSosAllDispFlg(boolean sosAllDispFlg) {
		this.sosAllDispFlg = sosAllDispFlg;
	}

	/**
	 * デフォルト組織変更フラグを取得する。
	 *
	 * @return デフォルト組織変更フラグ
	 */
	public boolean isDefaultChangeFlg() {
		return defaultChangeFlg;
	}

	/**
	 * デフォルト組織変更フラグを設定する。
	 *
	 * @param defaultChangeFlg デフォルト組織変更フラグ
	 */
	public void setDefaultChangeFlg(boolean defaultChangeFlg) {
		this.defaultChangeFlg = defaultChangeFlg;
	}

	/**
	 * 組織コードを取得する。
	 *
	 * @return 組織コード
	 */
	public String getSosCd() {
		return sosCd;
	}

	/**
	 * 組織コードを設定する。
	 *
	 * @param sosCd 組織コード
	 */
	public void setSosCd(String sosCd) {
		this.sosCd = sosCd;
	}

	/**
	 * 従業員番号を取得する。
	 *
	 * @return 従業員番号
	 */
	public String getJgiNo() {
		return jgiNo;
	}

	/**
	 * 従業員番号を設定する。
	 *
	 * @param jgiNo 従業員番号
	 */
	public void setJgiNo(String jgiNo) {
		this.jgiNo = jgiNo;
	}

	/**
	 * @return dpUser
	 */
	public DpUser getDpUser() {
		return dpUser;
	}

	/**
	 * @param dpUser
	 */
	public void setDpUser(DpUser dpUser) {
		this.dpUser = dpUser;
	}

	/**
	 * @return sosLvl
	 */
	public SosLvl getSosLvl() {
		return sosLvl;
	}

	/**
	 * @param sosLvl
	 */
	public void setSosLvl(SosLvl sosLvl) {
		this.sosLvl = sosLvl;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		// REQUEST管理のため不要
	}
}
