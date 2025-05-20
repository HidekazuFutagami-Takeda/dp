package jp.co.takeda.model.div;

import java.sql.SQLException;

import com.ibatis.sqlmap.client.extensions.ResultGetter;

import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;

/**
 * 条件区分を表す列挙
 *
 * @author yyoshino
 */
public enum JokenKbn implements DbValue<Integer> {

	/**
	 * 全品目参照可能
	 */
	ALL_REFER(0, "参照"),

	/**
	 * 全品目更新可能
	 */
	ALL_EDIT(1, "編集"),

	/**
	 * ワクチンのみ参照可能
	 */
	VAC_REFER(2, "参照"),

	/**
	 * ワクチンのみ更新可能
	 */
	VAC_EDIT(3, "編集"),

	/**
	 * 使用不可
	 */
	DO_NOT_USE(9, "使用不可"),
	;

	/**
	 * コンストラクタ
	 *
	 * @param businessName 業務名
	 */
	private JokenKbn(Integer dbValue, String JokenKbnName) {
		this.dbValue = dbValue;
		this.JokenKbnName = JokenKbnName;
	}

	/**
	 * db値
	 */
	private final Integer dbValue;

	/**
	 * 条件区分名
	 */
	private final String JokenKbnName;


	@Override
	public Integer getDbValue() {
		return dbValue;
	}



	/**
	 * @return jokenKbnName
	 */
	public String getJokenKbnName() {
		return JokenKbnName;
	}



	/**
	 * コード値から列挙を逆引きする。
	 *
	 * @param code コード値
	 * @return 列挙
	 */
	public static JokenKbn getInstance(Integer code) {

		if (code == null) {
			return null;
		}

		for (JokenKbn entry : JokenKbn.values()) {
			if (entry.getDbValue().equals(code)) {
				return entry;
			}
		}
		// 未定義の条件セットの場合、エラーとしない
		return null;
	}

	/**
	 * DBとのマッピングを行うタイプハンドラークラス
	 *
	 * @author khashimoto
	 */
	public static class JokenKbnTypeHandler extends AbstractEnumTypeHandler {

		public JokenKbnTypeHandler() {
			super(new EnumType(JokenKbn.class, java.sql.Types.INTEGER));
		}

		@Override
		public Object getResult(ResultGetter resultGetter) throws SQLException {
			Object value = resultGetter.getInt();
			if (resultGetter.wasNull()) {
				return null;
			}
			return this.convertToEnumMap.get(value);
		}

	}

}
