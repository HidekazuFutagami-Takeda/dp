package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 売上所属を表す列挙
 * 
 * @author tkawabata
 */
public enum Sales implements DbValue<String> {

	/**
	 * 医薬
	 */
	IYAKU("01"),

	/**
	 * ワクチン
	 */
	VACCHIN("03");

	/**
	 * コンストラクタ
	 * 
	 * @param value 売上所属を表す文字
	 */
	private Sales(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 売上所属を取得する。
	 * 
	 * @return 売上所属を表す文字
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
	public static Sales getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (Sales entry : Sales.values()) {
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
	public static class SalesTypeHandler extends AbstractEnumTypeHandler {

		public SalesTypeHandler() {
			super(new EnumType(Sales.class, java.sql.Types.CHAR));
		}

	}
}
