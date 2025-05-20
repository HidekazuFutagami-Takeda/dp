package jp.co.takeda.model.div;

import jp.co.takeda.a.dao.DbValue;

/**
 * 対象区分表示値を表す列挙
 *
 * @author rna8405
 *
 */
public enum DispInsKbn implements DbValue<String> {

	/**
	 * UH
	 */
	UH("UH"),

	/**
	 * P
	 */
	P("P"),

	/**
	 * Z
	 */
	Z("Z"),

	/**
	 * Z(ワクチン)
	 */
	ZV("ワクチン")
	;

	/**
	 * コンストラクタ
	 * @param value 対象区分表示値
	 */
	private DispInsKbn(String value) {
		this.value = value;
	}

	private String value;

	/**
	 * 対象区分表示値を取得する。
	 *
	 * @return 対象区分表示値
	 */
	public String getDbValue() {
		return value;
	}
}
