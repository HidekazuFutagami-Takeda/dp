package jp.co.takeda.model.div;

import jp.co.takeda.a.dao.DbValue;

/**
 * 月を表す列挙
 *
 * @author rna8405
 */
public enum Month implements DbValue<String> {

	/**
	 * 4月
	 */
	APRIL("4月"),

	/**
	 * ５月
	 */
	MAY("5月"),

	/**
	 * ６月
	 */
	JUNE("6月"),

	/**
	 * ７月
	 */
	JULY("7月"),

	/**
	 * ８月
	 */
	AUGUST("8月"),

	/**
	 * ９月
	 */
	SEPTEMBER("9月"),

	/**
	 * １０月
	 */
	OCTOBER("10月"),

	/**
	 * １１月
	 */
	NOVEMBER("11月"),

	/**
	 * １２月
	 */
	DECEMBER("12月"),

	/**
	 * １月
	 */
	JANUARY("1月"),

	/**
	 * ２月
	 */
	FEBRUARY("2月"),

	/**
	 * ３月
	 */
	MARCH("3月"),
	;

	/**
	 * 月数（期）
	 */
	public static final int PERIOD_MONTH = 6;

	/**
	 * 年間月数
	 */
	public static final int YEAR_MONTH = 12;

	/**
	 * コンストラクタ
	 * @param value
	 */
	private Month(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	@Override
	public String getDbValue() {
		return value;
	}
}
