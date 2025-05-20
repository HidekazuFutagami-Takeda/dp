package jp.co.takeda.bean;

/**
 * 数値のスケールを表す列挙
 * 
 * @author wakamori
 */
public enum Scale {

	/**
	 * 千円単位のスケール
	 */
	THOUSAND(-3),

	/**
	 * 万円単位のスケール
	 */
	TEN_THOUSAND(-4),

	/**
	 * 百万単位のスケール
	 */
	MILLION(-6);

	/**
	 * 数値のスケール
	 */
	private final int value;

	/**
	 * コンストラクタ
	 * 
	 * @param unit 数値のスケール
	 */
	private Scale(final int unit) {
		this.value = unit;
	}

	/**
	 * 数値のスケールを取得する。
	 * 
	 * @return 数値のスケール
	 */
	public int scaleValue() {
		return this.value;
	}
}
