package jp.co.takeda.logic.div;

/**
 * 業務連絡の処理結果区分を表す列挙
 * 
 * @author khashimoto
 */
public enum ContactResultType {

	/**
	 * 処理成功
	 */
	SUCCESS,

	/**
	 * 処理成功（配分ミスあり）
	 */
	SUCCESS_DIST_MISS,

	/**
	 * 処理失敗
	 */
	FAILURE;
}
