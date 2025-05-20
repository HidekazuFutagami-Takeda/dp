package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 市場分類を表す列挙
 * 
 * @author khashimoto
 */
public enum MarketClass implements DbValue<String> {
	/**
	 * 高血圧
	 */
	HYPERTENSION("001"),

	/**
	 * 酸抑制剤
	 */
	SUPPRESSANT("002"),

	/**
	 * 高脂血症
	 */
	HYPERLIPEMIA("003"),

	/**
	 * RA系
	 */
	RA_SYSTEM("004"),

	/**
	 * Ca計
	 */
	CA_TOTAL("005"),

	/**
	 * 糖尿病
	 */
	DIABETES("006"),

	/**
	 * 骨粗鬆症
	 */
	OSTEOPOROSIS("007"),

	/**
	 * 麻薬注射
	 */
	NARCOTIC_INJECTION("008");

	/**
	 * コンストラクタ
	 * 
	 * @param value 部門ランクを表す文字
	 */
	private MarketClass(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 市場分類を取得する。
	 * 
	 * @return 市場分類を表す文字
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
	public static MarketClass getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (MarketClass entry : MarketClass.values()) {
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
	public static class MarketClassTypeHandler extends AbstractEnumTypeHandler {

		public MarketClassTypeHandler() {
			super(new EnumType(MarketClass.class, java.sql.Types.VARCHAR));
		}

	}
}
