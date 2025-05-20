package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 品目施設情報のエラー区分を示す列挙
 *
 * @author tkawabata
 */
public enum ProdInsInfoErrKbn implements DbValue<String> {

	/**
	 * エラーメッセージを表示しない。
	 */
	NOT("0"),

	/**
	 * アラートメッセージを表示するが、「はい」を選択すると強制登録できる。
	 */
	ALERT("1"),

	/**
	 * エラーメッセージを表示し、強制登録もできない。
	 */
	ERROR("2");

	/**
	 * コンストラクタ
	 *
	 * @param value 品目カテゴリを表す文字
	 */
	private ProdInsInfoErrKbn(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 *  区分値を表す文字を表す文字を取得する。
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
	public static ProdInsInfoErrKbn getInstance(String code) {
		if (code == null) {
			return null;
		}
		for (ProdInsInfoErrKbn entry : ProdInsInfoErrKbn.values()) {
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
	public static class ProdInsInfoErrKbnHandler extends AbstractEnumTypeHandler {
		public ProdInsInfoErrKbnHandler() {
			super(new EnumType(ProdInsInfoErrKbn.class, java.sql.Types.CHAR));
		}
	}
}
