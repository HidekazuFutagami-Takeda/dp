package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 取引区分を表す列挙
 * 
 * @author khashimoto
 */
public enum TradeType implements DbValue<String> {

	/**
	 * 取引有
	 */
	TRADE_ON("1"),

	/**
	 * 取引無
	 */
	TRADE_OFF("2");

	/**
	 * コンストラクタ
	 * 
	 * @param value 取引区分を表す文字
	 */
	private TradeType(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 取引区分を表す文字を取得する。
	 * 
	 * @return 取引区分を表す文字
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
	public static TradeType getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (TradeType entry : TradeType.values()) {
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
	public static class TradeTypeTypeHandler extends AbstractEnumTypeHandler {

		public TradeTypeTypeHandler() {
			super(new EnumType(TradeType.class, java.sql.Types.VARCHAR));
		}

	}
}
