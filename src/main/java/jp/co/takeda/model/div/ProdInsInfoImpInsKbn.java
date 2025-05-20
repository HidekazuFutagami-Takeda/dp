package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 重点先区分を示す列挙
 *
 * @author tkawabata
 */
public enum ProdInsInfoImpInsKbn implements DbValue<String> {

	/**
	 * 増分計画を配分しない
	 */
	NOT_INCREMENT_DISP("0"),

	/**
	 * 増分計画を配分する
	 */
	INCREMENT_DISP("1");

	/**
	 * コンストラクタ
	 *
	 * @param value 区分値を表す文字
	 */
	private ProdInsInfoImpInsKbn(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 重点先区分を表す文字を取得する。
	 *
	 * @return 重点先区分を表す文字
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
	public static ProdInsInfoImpInsKbn getInstance(String code) {
		if (code == null) {
			return null;
		}
		for (ProdInsInfoImpInsKbn entry : ProdInsInfoImpInsKbn.values()) {
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
	public static class ProdInsInfoImpInsKbnHandler extends AbstractEnumTypeHandler {
		public ProdInsInfoImpInsKbnHandler() {
			super(new EnumType(ProdInsInfoImpInsKbn.class, java.sql.Types.CHAR));
		}
	}
}
