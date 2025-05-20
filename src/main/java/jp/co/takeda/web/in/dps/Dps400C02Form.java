package jp.co.takeda.web.in.dps;

import jp.co.takeda.web.cmn.action.DpActionForm;

/**
 * Dps400C02((医)施設特約店別計画配分処理起動確認画面)のフォームクラス
 * 
 * @author stakeuchi
 */
public class Dps400C02Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 組織コード(営業所)
	 */
	private String sosCd3;

	/**
	 * 組織コード(営業所)を取得する。
	 * 
	 * @return 組織コード(営業所)
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

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
	}
}
