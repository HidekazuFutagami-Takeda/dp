package jp.co.takeda.logic;

/**
 * 削除施設を判定するロジッククラス
 * 
 * @author tkawabata
 */
public class DelInsLogic {

	/**
	 * 依頼中フラグ
	 */
	private final Boolean reqFlg;

	/**
	 * 削除フラグ
	 */
	private final Boolean delFlg;

	/**
	 * コンストラクタ
	 * 
	 * @param reqFlg 依頼中フラグ
	 * @param delFlg 削除フラグ
	 */
	public DelInsLogic(Boolean reqFlg, Boolean delFlg) {
		this.reqFlg = reqFlg;
		this.delFlg = delFlg;
	}

	/**
	 * 依頼中フラグと削除フラグから納入計画システム用の削除フラグを取得する。
	 * 
	 * @param reqFlg 依頼中フラグ
	 * @param delFlg 削除フラグ
	 * @return 納入計画システム用の削除フラグ
	 */
	public Boolean getDelFlg() {

		if (reqFlg != null && reqFlg) {
			return true;
		}
		if (delFlg != null && delFlg) {
			return true;
		}
		return false;
	}
}
