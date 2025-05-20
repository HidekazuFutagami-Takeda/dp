package jp.co.takeda.dao.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;

/**
 * 支援・検索用対象区分を示す列挙
 *
 * @author khashimoto
 */
public enum DpsSearchInsType {

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
	 * ワクチン
	 */
	VAC("4"),

	/**
	 * P(含むワクチン)
	 */
	PV("3,4"),

	/**
	 * UHP
	 */
	UHP("1,2,3");

	/**
	 * コンストラクタ
	 *
	 * @param value 検索用対象区分を表す文字
	 */
	private DpsSearchInsType(String value) {
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
	public static DpsSearchInsType getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (DpsSearchInsType entry : DpsSearchInsType.values()) {
			if (entry.getDbValue().equals(code)) {
				return entry;
			}
		}
		final String errMsg = "指定のコード値に対応する列挙が存在しない。code=" + code;
		throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
	}
}
