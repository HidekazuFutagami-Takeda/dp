package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 管理対象業務区分を表す列挙
 * 
 * @author tkawabata
 */
public enum SysType implements DbValue<String> {

	/**
	 * 医薬
	 */
	IYAKU("0"),

	/**
	 * ワクチン
	 */
	VACCINE("1");

	/**
	 * コンストラクタ
	 * 
	 * @param value 管理対象業務区分を表す文字
	 */
	private SysType(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 管理対象業務区分を取得する。
	 * 
	 * @return 管理対象業務区分を表す文字
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
	public static SysType getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (SysType entry : SysType.values()) {
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
	public static class SysTypeTypeHandler extends AbstractEnumTypeHandler {

		public SysTypeTypeHandler() {
			super(new EnumType(SysType.class, java.sql.Types.CHAR));
		}

	}
}
