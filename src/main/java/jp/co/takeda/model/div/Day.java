package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.exp.SystemException;

/**
 * 曜日を表す列挙
 * 
 * @author tkawabata
 */
public enum Day implements DbValue<String> {

	/**
	 * 日曜日
	 */
	SUNDAY("1"),

	/**
	 * 月曜日
	 */
	MONDAY("2"),

	/**
	 * 火曜日
	 */
	TUESDAY("3"),

	/**
	 * 水曜日
	 */
	WEDNESDAY("4"),

	/**
	 * 木曜日
	 */
	THURSDAY("5"),

	/**
	 * 金曜日
	 */
	FRIDAY("6"),

	/**
	 * 土曜日
	 */
	SATURDAY("7");

	/**
	 * コンストラクタ
	 * 
	 * @param value 曜日を表す文字
	 */
	private Day(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 曜日を表す文字を取得する。
	 * 
	 * @return 曜日を表す文字
	 */
	public String getDbValue() {
		return value;
	}

	/**
	 * コード値から列挙を逆引きする。
	 * 
	 * @param code コード値
	 * @return 列挙
	 */
	public static Day getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (Day entry : Day.values()) {
			if (entry.getDbValue().equals(code)) {
				return entry;
			}
		}
		final String errMsg = "指定のコード値に対応する列挙が存在しない。code=" + code;
		throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
	}
}
