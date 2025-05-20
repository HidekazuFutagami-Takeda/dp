package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 従業員区分を表す列挙
 * 
 * @author khashimoto
 */
public enum JgiKb implements DbValue<String> {

	/**
	 * 従業員
	 */
	JGI("0"),

	/**
	 * コントラクト MR
	 */
	CONTRACT_MR("1"),

	/**
	 * 武田事務サービス
	 */
	TAKEDA_JIMU("2"),

	/**
	 * 支店雑担当
	 */
	SHITEN_ZATU("3"),

	/**
	 * 営業所雑担当
	 */
	EIGYOSHO_ZATU("4"),

	/**
	 * 代行ユーザ
	 */
	DAIKOU_USER("5"),

	/**
	 * 選択不可ユーザ
	 */
	NG_USER("6"),

	/**
	 * 仮従業員データ
	 */
	TMP_JGI("7"),

	/**
	 * ダミーユーザ
	 */
	DUMMY_MR("8");

	/**
	 * コンストラクタ
	 * 
	 * @param value 従業員区分を表す文字
	 */
	private JgiKb(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 従業員区分を取得する。
	 * 
	 * @return 従業員区分を表す文字
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
	public static JgiKb getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (JgiKb entry : JgiKb.values()) {
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
	public static class JgiKbTypeHandler extends AbstractEnumTypeHandler {

		public JgiKbTypeHandler() {
			super(new EnumType(JgiKb.class, java.sql.Types.VARCHAR));
		}

	}
}
