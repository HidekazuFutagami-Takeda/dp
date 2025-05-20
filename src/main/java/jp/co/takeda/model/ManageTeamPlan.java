package jp.co.takeda.model;

/**
 * 管理時のチーム別計画を表すモデルクラス
 * 
 * @author khashimoto
 */
public class ManageTeamPlan extends ManageSosPlan {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 雑組織フラグ
	 */
	private Boolean etcSosFlg;

	//---------------------
	// Getter & Setter
	// --------------------
	/**
	 * 雑組織フラグを取得する。
	 * 
	 * @return 雑組織フラグ
	 */
	public Boolean getEtcSosFlg() {
		return etcSosFlg;
	}

	/**
	 * 雑組織フラグを設定する。
	 * 
	 * @param etcSosFlg 雑組織フラグ
	 */
	public void setEtcSosFlg(Boolean etcSosFlg) {
		this.etcSosFlg = etcSosFlg;
	}

}
