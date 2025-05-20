package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 配分対象区分を示す列挙
 *
 * @author tkawabata
 */
public enum ProdInsInfoDistKbn implements DbValue<String> {

	/**
	 * 配分計算品目の実績有無に関らず、配分計算対象外の施設とする。
	 */
	NOT_TARGET("0"),

	/**
	 * 配分計算対象の施設とする
	 */
	TARGET("1");

	/**
	 * コンストラクタ
	 *
	 * @param value  区分値を表す文字
	 */
	private ProdInsInfoDistKbn(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 *  区分値を表す文字を取得する。
	 *
	 * @return  区分値を表す文字
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
	public static ProdInsInfoDistKbn getInstance(String code) {
		if (code == null) {
			return null;
		}
		for (ProdInsInfoDistKbn entry : ProdInsInfoDistKbn.values()) {
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
	 * @author tkawabata
	 */
	public static class ProdInsInfoDistKbnHandler extends AbstractEnumTypeHandler {
		public ProdInsInfoDistKbnHandler() {
			super(new EnumType(ProdInsInfoDistKbn.class, java.sql.Types.CHAR));
		}
	}
}
