package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 画面表示区分を示す列挙
 *
 * @author tkawabata
 */
public enum ProdInsInfoScanDispKbn implements DbValue<String> {

	/**
	 * 施設一覧に施設を表示せず、施設追加もできない。
	 */
	NOT("0"),

	/**
	 * 実績/計画があれば施設一覧に施設を表示し、実績/計画がなければ施設追加から追加できる。
	 */
	ALL("1");

	/**
	 * コンストラクタ
	 *
	 * @param value 区分値を表す文字
	 */
	private ProdInsInfoScanDispKbn(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 画面表示区分を表す文字を取得する。
	 *
	 * @return 画面表示区分を表す文字
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
	public static ProdInsInfoScanDispKbn getInstance(String code) {
		if (code == null) {
			return null;
		}
		for (ProdInsInfoScanDispKbn entry : ProdInsInfoScanDispKbn.values()) {
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
	public static class ProdInsInfoScanDispKbnHandler extends AbstractEnumTypeHandler {
		public ProdInsInfoScanDispKbnHandler() {
			super(new EnumType(ProdInsInfoScanDispKbn.class, java.sql.Types.CHAR));
		}
	}
}
