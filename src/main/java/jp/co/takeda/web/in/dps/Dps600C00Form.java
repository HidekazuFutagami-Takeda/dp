package jp.co.takeda.web.in.dps;

import java.util.HashMap;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;

import jp.co.takeda.web.cmn.action.DpActionForm;

/**
 * Dps600C00((医)施設医師別計画配分対象品目一覧画面)のフォームクラス
 * 
 * @author stakeuchi
 */
public class Dps600C00Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey DPS600C00_DATA_R_SEARCH_RESULT = new BoxKeyPerClassImpl(Dps600C00Form.class, HashMap.class);

	/**
	 * 検索結果有無取得キー(TRUE:1件以上,FALSE:0件)
	 */
	public static final BoxKey DPS600C00_DATA_R_SEARCH_RESULT_EXIST = new BoxKeyPerClassImpl(Dps600C00Form.class, Boolean.class);

	/**
	 * 編集権限((医)施設医師別計画(自)配分処理)
	 */
	public static final DpAuthority DPS600C00_MMP_EDIT_AUTH = new DpAuthority( AuthType.EDIT);

	/**
	 * 検索条件を処理用フィールドに設定する。
	 */
	public void setTranField() {
		this.sosCd3Tran = this.sosCd3;
	}

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 組織コード(営業所)
	 */
	private String sosCd3;

	/**
	 * 配分処理と同時にMRに公開フラグ
	 * <ul>
	 * <li>TRUE =公開する</li>
	 * <li>FALSE=公開しない</li>
	 * </ul>
	 */
	private boolean isMrOpenCheck;

	/**
	 * 医師別計画ステータス最終更新日
	 */
	private String statusLastUpdate;
	
	// 処理用フィールド
	/**
	 * 処理用組織コード(営業所)
	 */
	private String sosCd3Tran;

	/**
	 * 組織コード(営業所)を取得する。
	 * 
	 * @return sosCd3 組織コード(営業所)
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 組織コード(営業所)を設定する。
	 * 
	 * @param sosCd3 組織コード(営業所)
	 */
	public void setSosCd3(String sosCd3) {
		this.sosCd3 = sosCd3;
	}

	/**
	 * 配分処理と同時にMRに公開フラグを取得する。
	 * 
	 * @return isMrOpenCheck 配分処理と同時にMRに公開フラグ
	 */
	public boolean getIsMrOpenCheck() {
		return isMrOpenCheck;
	}

	/**
	 * 配分処理と同時にMRに公開フラグを設定する。
	 * 
	 * @param isMrOpenCheck 配分処理と同時にMRに公開フラグ
	 */
	public void setIsMrOpenCheck(boolean isMrOpenCheck) {
		this.isMrOpenCheck = isMrOpenCheck;
	}

	/**
	 * 医師別計画ステータス最終更新日 を取得する。
	 * 
	 * @return 医師別計画ステータス最終更新日
	 */
	public String getStatusLastUpdate() {
		return statusLastUpdate;
	}

	/**
	 * 医師別計画ステータス最終更新日 を設定する。
	 * @param statusLastUpdate 医師別計画ステータス最終更新日
	 */
	public void setStatusLastUpdate(String statusLastUpdate) {
		this.statusLastUpdate = statusLastUpdate;
	}

	/**
	 * 処理用組織コード(営業所)を取得する。
	 * 
	 * @return 処理用組織コード(営業所)
	 */
	public String getSosCd3Tran() {
		return sosCd3Tran;
	}

	/**
	 * 処理用組織コード(営業所)を設定する。
	 * 
	 * @param sosCd3Tran 処理用組織コード(営業所)
	 */
	public void setSosCd3Tran(String sosCd3Tran) {
		this.sosCd3Tran = sosCd3Tran;
	}

	@Override
	public void reset() {
		this.isMrOpenCheck = false;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.sosCd3 = null;
		this.sosCd3Tran = null;
		this.isMrOpenCheck = false;
		this.statusLastUpdate = null;
	}
}
