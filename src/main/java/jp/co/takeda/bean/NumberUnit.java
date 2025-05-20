package jp.co.takeda.bean;

/**
 * 数値の単位を表す列挙
 * 
 * @author tkawabata
 */
public enum NumberUnit {

	/**
	 * 千円単位
	 */
	THOUSAND(1000),

	/**
	 * 百万単位
	 */
	MILLION(1000000),

	/**
	 * 億単位
	 */
	HUNDRED_MILLION(100000000);

	/**
	 * 数値の単位
	 */
	private final long value;

	/**
	 * コンストラクタ
	 * 
	 * @param unit 数値の単位
	 */
	NumberUnit(final long unit) {
		this.value = unit;
	}

	/**
	 * 数値の単位を取得する。
	 * 
	 * @return 数値の単位
	 */
	public long unitValue() {
		return this.value;
	}
}
