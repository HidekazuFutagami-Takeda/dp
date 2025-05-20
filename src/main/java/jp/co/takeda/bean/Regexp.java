package jp.co.takeda.bean;

/**
 * 正規表現を表す列挙
 * 
 * @author wakamori
 */
public enum Regexp {

	/**
	 * 半角英数字
	 */
	ALPHABET_OR_DIGIT("^[0-9a-zA-Z]*$"),

	/**
	 * 半角数字
	 */
	DIGIT("^[0-9]*$"),

	/**
	 * ミリ秒
	 */
	MILLI_SECOND("^\\d{1,3}$");

	/**
	 * 正規表現
	 */
	private String value;

	/**
	 * コンストラクタ
	 * 
	 * @param value 正規表現
	 */
	Regexp(final String value) {
		this.value = value;
	}

	/**
	 * 正規表現を取得する。
	 * 
	 * @return 正規表現
	 */
	public String regexpValue() {
		return this.value;
	}
}
