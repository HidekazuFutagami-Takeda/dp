package jp.co.takeda.bean;

/**
 * 圧縮方式を表す列挙
 * 
 * @author tkawabata
 */
public enum Compress {

	/**
	 * ZIP形式
	 */
	ZIP(".zip"),

	/**
	 * GZIP形式
	 */
	GZIP(".gz");

	/**
	 * 拡張子文字列
	 */
	public final String suffix;

	/**
	 * コンストラクタ
	 * 
	 * @param suffix 拡張子文字列
	 */
	private Compress(String suffix) {
		this.suffix = suffix;
	}
}
