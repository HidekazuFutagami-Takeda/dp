package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 市場区分を表す列挙
 * 
 * @author khashimoto
 */
public enum MarketType implements DbValue<String> {
	/**
	 * ""
	 */
	NONE("0"),

	/**
	 * 超大市場
	 */
	SUPER_LARGE_MARKET("1"),

	/**
	 * 大市場
	 */
	LARGE_MARKET("2");

	/**
	 * コンストラクタ
	 * 
	 * @param value 市場区分を表す文字
	 */
	private MarketType(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 市場区分を取得する。
	 * 
	 * @return 市場区分を表す文字
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
	public static MarketType getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (MarketType entry : MarketType.values()) {
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
	public static class MarketTypeTypeHandler extends AbstractEnumTypeHandler {

		public MarketTypeTypeHandler() {
			super(new EnumType(MarketType.class, java.sql.Types.VARCHAR));
		}

	}
}
