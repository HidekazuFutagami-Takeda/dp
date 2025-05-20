package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 活動区分(重点先/一般先)を表す列挙
 *
 * @author khashimoto
 */
public enum ActivityType implements DbValue<String> {

	/**
	 * 重点先
	 */
	JTN("1"),

	/**
	 * 一般先
	 */
	IPPAN("2"),

	/**
	 * 全て
	 */
	ALL("3");

	/**
	 * コンストラクタ
	 *
	 * @param value 活動区分を表す文字
	 */
	private ActivityType(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 活動区分を取得する。
	 *
	 * @return 活動区分を表す文字
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
	public static ActivityType getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (ActivityType entry : ActivityType.values()) {
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
	public static class ActivityTypeTypeHandler extends AbstractEnumTypeHandler {

		public ActivityTypeTypeHandler() {
			super(new EnumType(ActivityType.class, java.sql.Types.CHAR));
		}
	}
}
