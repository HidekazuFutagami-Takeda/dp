package jp.co.takeda.web.in.dps;

import jp.co.takeda.web.cmn.action.DiaDpActionForm;

/**
 * Dps998C00(ファイル選択画面)のフォームクラス
 *
 * @author kibe
 */
public class Dps998C00Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 組織コード
	 */
	private String outputSosCd;

	/**
	 * カテゴリ
	 */
	private String category;

	// -------------------------------
	// getter/setter
	// -------------------------------
	/**
	 * 組織コードを取得する。
	 *
	 * @return 組織コード
	 */
	public String getOutputSosCd() {
		return outputSosCd;
	}

	/**
	 * 組織コードを設定する。
	 *
	 * @param outputSosCd
	 */
	public void setOutputSosCd(String outputSosCd) {
		this.outputSosCd = outputSosCd;
	}

	/**
	 * カテゴリを取得する。
	 *
	 * @return カテゴリ
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * カテゴリを設定する。
	 *
	 * @param category カテゴリ
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		// REQUEST管理のため、不要
	}
}
