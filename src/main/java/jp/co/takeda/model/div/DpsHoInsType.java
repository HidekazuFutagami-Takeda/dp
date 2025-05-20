package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * (支援・詳細版)対象区分を表す列挙
 *
 * @author tkawabata
 */
public enum DpsHoInsType implements DbValue<String> {

	/**
	 * U
	 */
	U("1"),

	/**
	 * H
	 */
	H("2"),

	/**
	 * P
	 */
	P("3"),

	/**
	 * ワクチン
	 */
	VAC("4");

	/**
	 * コンストラクタ
	 *
	 * @param value 対象区分を表す文字
	 */
	private DpsHoInsType(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 対象区分を表す文字を取得する。
	 *
	 * @return 対象区分を表す文字
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
	public static DpsHoInsType getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (DpsHoInsType entry : DpsHoInsType.values()) {
			if (entry.getDbValue().equals(code)) {
				return entry;
			}
		}
		final String errMsg = "指定のコード値に対応する列挙が存在しない。code=" + code;
		throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
	}

	/**
	 * 対象区分の名称を取得する。
	 *
	 *
	 * @param hoInsType 対象区分
	 * @return 対象区分の名称
	 */
	public static String getHoInsTypeName(final DpsHoInsType hoInsType) {
		if (hoInsType == null) {
			return null;
		}
		String name = null;
		switch (hoInsType) {
			case U:
				name = "U";
				break;
			case H:
				name = "H";
				break;
			case P:
				name = "P";
				break;
			case VAC:
				name = "ワクチン";
				break;
		}
		return name;
	}

	/**
	 * DBとのマッピングを行うタイプハンドラークラス
	 *
	 * @author khashimoto
	 */
	public static class DpsHoInsTypeTypeHandler extends AbstractEnumTypeHandler {

		public DpsHoInsTypeTypeHandler() {
			super(new EnumType(DpsHoInsType.class, java.sql.Types.VARCHAR));
		}

	}
}
