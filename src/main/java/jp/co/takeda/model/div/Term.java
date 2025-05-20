package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 期を表す列挙
 * 
 * @author tkawabata
 */
public enum Term implements DbValue<String> {

	/**
	 * 上期
	 */
	FIRST("0"),

	/**
	 * 下期
	 */
	SECOND("1");

	/**
	 * 上期開始月
	 */
	public static final int FIRST_MONTH = 4;

	/**
	 * 下期開始月
	 */
	public static final int SECOND_MONTH = 10;

	/**
	 * コンストラクタ
	 * 
	 * @param value 期を表す文字
	 */
	private Term(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 期を表す文字を取得する。
	 * 
	 * @return 期を表す文字
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
	public static Term getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (Term entry : Term.values()) {
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
	public static class TermTypeHandler extends AbstractEnumTypeHandler {

		public TermTypeHandler() {
			super(new EnumType(Term.class, java.sql.Types.CHAR));
		}

	}
}
