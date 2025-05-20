package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.exp.SystemException;

/**
 * 対象区分(雑あり)選択値を表す列挙
 *
 * @author rna8405
 *
 */
public enum SelectInsKbnZatsuAri implements DbValue<String> {

	/**
	 * UH
	 */
	UH("1", "UH"),

	/**
	 * P雑
	 */
	P("2", "P雑")

	;

	/**
	 * コンストラクタ
	 *
	 * @param code 対象区分コード
	 * @param name 対象区分名称
	 */
	private SelectInsKbnZatsuAri(String code, String name) {
		this.code = code;
		this.name = name;
	}

	/**
	 * 対象区分のコード
	 */
	private String code;

	/**
	 * 対象区分の名称
	 */
	private String name;

	/**
	 * 対象区分を表す名称を取得する。
	 *
	 * @return 対象区分を表す名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 対象区分を表すコード値を取得する。
	 *
	 * @return 対象区分を表すコード値
	 */
	public String getDbValue() {
		return code;
	}

	/**
	 * コード値から列挙を逆引きする。
	 *
	 * @param code コード値
	 * @return 列挙
	 */
	public static SelectInsKbnZatsuAri getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (SelectInsKbnZatsuAri entry : SelectInsKbnZatsuAri.values()) {
			if (entry.getDbValue().equals(code)) {
				return entry;
			}
		}
		final String errMsg = "指定のコード値に対応する列挙が存在しない。code=" + code;
		throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
	}
}
