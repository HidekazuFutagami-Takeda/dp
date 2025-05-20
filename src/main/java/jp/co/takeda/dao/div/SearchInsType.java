package jp.co.takeda.dao.div;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;

/**
 * 検索用対象区分を示す列挙
 *
 * @author khashimoto
 */
public enum SearchInsType {

	/**
	 * U
	 */
	U("1"),

	/**
	 * H
	 */
	H("2"),

	/**
	 * UH
	 */
	UH("1,2"),

	/**
	 * P
	 */
	P("3"),

	/**
	 * P(含むワクチン)
	 */
	PV("3,7"),

	/**
	 * ワクチン
	 */
	VAC("7"),

	/**
	 * UHP
	 */
	UHP("1,2,3");

	/**
	 * コンストラクタ
	 *
	 * @param value 検索用対象区分を表す文字
	 */
	private SearchInsType(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 検索用対象区分を取得する。
	 *
	 * @return 検索用対象区分を表す文字
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
	public static SearchInsType getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (SearchInsType entry : SearchInsType.values()) {
			if (entry.getDbValue().equals(code)) {
				return entry;
			}
		}
		final String errMsg = "指定のコード値に対応する列挙が存在しない。code=" + code;
		throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
	}
}
