package jp.co.takeda.web.in.dps;

import jp.co.takeda.web.cmn.action.DpActionForm;

/**
 * Dps301C04((医)試算処理方法確認ダイアログ画面)のフォームクラス
 * 
 * @author khashimoto
 */
public class Dps301C04Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 試算タイプ
	 */
	private String calcType;

	/**
	 * 全品目選択済みかどうかを表すフラグ
	 */
	private boolean allProdOnFlg;

	/**
	 * 組織コード(営業所)
	 */
	private String sosCd3;

	/**
	 * チームがあるかを表すフラグ
	 */
	private boolean existsTeamSos;

	/**
	 * 試算タイプを取得する。
	 * 
	 * @return 試算タイプ
	 */
	public String getCalcType() {
		return calcType;
	}

	/**
	 * 試算タイプを設定する。
	 * 
	 * @param calcType 試算タイプ
	 */
	public void setCalcType(String calcType) {
		this.calcType = calcType;
	}

	/**
	 * 全品目選択済みかどうかを表すフラグを取得する。
	 * 
	 * @return 全品目選択済みかどうかを表すフラグ
	 */
	public boolean getAllProdOnFlg() {
		return allProdOnFlg;
	}

	/**
	 * 全品目選択済みかどうかを表すフラグを設定する。
	 * 
	 * @param allProdOnFlg 全品目選択済みかどうかを表すフラグ
	 */
	public void setAllProdOnFlg(boolean allProdOnFlg) {
		this.allProdOnFlg = allProdOnFlg;
	}

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

	/**
	 * チームがあるかを表すフラグ を取得する。
	 * 
	 * @return チームがあるかを表すフラグ
	 */
	public boolean isExistsTeamSos() {
		return existsTeamSos;
	}

	/**
	 * チームがあるかを表すフラグ を設定する。
	 * 
	 * @param existsTeamSos チームがあるかを表すフラグ
	 */
	public void setExistsTeamSos(boolean existsTeamSos) {
		this.existsTeamSos = existsTeamSos;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		existsTeamSos = true;
	}
}
