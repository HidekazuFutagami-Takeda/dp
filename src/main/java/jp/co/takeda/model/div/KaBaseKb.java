package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 価ベース区分を表す列挙
 *
 * @author khashimoto
 */
public enum KaBaseKb implements DbValue<String> {

	/**
	 * Y価
	 */
	Y_KA_BASE("1"),

	/**
	 * S価
	 */
	S_KA_BASE("2");

	/**
	 * コンストラクタ
	 *
	 * @param value 価ベース区分を表す文字
	 */
	private KaBaseKb(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 価ベース区分を表す文字を取得する。
	 *
	 * @return 価ベース区分を表す文字
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
	public static KaBaseKb getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (KaBaseKb entry : KaBaseKb.values()) {
			if (entry.getDbValue().equals(code)) {
				return entry;
			}
		}
		final String errMsg = "指定のコード値に対応する列挙が存在しない。code=" + code;
		throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
	}

	/**
	 * DBとのマッピングを行うタイプハンドラークラス
	 *
	 * @author khashimoto
	 */
	public static class KaBaseKbTypeHandler extends AbstractEnumTypeHandler {

		public KaBaseKbTypeHandler() {
			super(new EnumType(KaBaseKb.class, java.sql.Types.CHAR));
		}

	}

}
