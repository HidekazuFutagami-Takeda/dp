package jp.co.takeda.logic.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;

/**
 * 組織・従業員の検索範囲を示す列挙を表す列挙
 * 
 * @author khashimoto
 */
public enum SosSrchValue {

	/**
	 * 支店階層
	 */
	SHITEN("01"),

	/**
	 * 営業所階層
	 */
	OFFICE("02"),

	/**
	 * チーム階層
	 */
	TEAM("03"),

	/**
	 * 従業員
	 */
	JGI("04");

	/**
	 * コンストラクタ
	 * 
	 * @param value 組織・従業員の検索範囲を表す文字
	 */
	private SosSrchValue(String value) {
		this.value = value;
	}

	/**
	 * 値
	 */
	private String value;

	/**
	 * 組織・従業員の検索範囲を取得する。
	 * 
	 * @return 組織・従業員の検索範囲を表す文字
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
	public static SosSrchValue getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (SosSrchValue entry : SosSrchValue.values()) {
			if (entry.getDbValue().equals(code)) {
				return entry;
			}
		}
		final String errMsg = "指定のコード値に対応する列挙が存在しない";
		throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
	}
}
