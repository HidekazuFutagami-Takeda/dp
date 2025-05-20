package jp.co.takeda.web.cmn.bean;

/**
 * content-dispositionモードを示す列挙
 * 
 * @author tkawabata
 */
public enum ContentDispositionMode {

	/**
	 * アタッチメント
	 */
	ATTACHMENT("attachment"),

	/**
	 * インライン
	 */
	INLINE("inline");

	/**
	 * モードを示す文字列
	 */
	public final String mode;

	/**
	 * コンストラクタ
	 * 
	 * @param mode モード
	 */
	private ContentDispositionMode(final String mode) {
		this.mode = mode;
	}
}
