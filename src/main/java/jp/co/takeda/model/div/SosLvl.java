package jp.co.takeda.model.div;

import java.sql.SQLException;

import com.ibatis.sqlmap.client.extensions.ResultGetter;

import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;

/**
 * 参照可能組織レベルを表す列挙
 *
 * @author yyoshino
 */
public enum SosLvl implements DbValue<Integer> {

	/**
	 * 全品目参照可能, 0
	 */
	ALL(0, "全社"),

	/**
	 * 支店, 1
	 */
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
	BRANCH(1, "リージョン"),
//	BRANCH(1, "支店"),
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）

	/**
	 * 営業所, 2
	 */
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
	OFFICE(2, "エリア"),
//	OFFICE(2, "営業所"),
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）

	/**
	 * チーム, 3
	 */
	TEAM(3, "チーム"),

	/**
	 * 担当, 4
	 */
	MR(4, "担当"),

	/**
	 * エラー , 999
	 */

	_ERROR(999,"エラー")
	;


	/**
	 * コンストラクタ
	 * @param dbValue
	 * @param JokenKbnName
	 */
	private SosLvl(Integer dbValue, String JokenKbnName) {
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
	public static SosLvl getInstance(Integer code) {

		if (code == null) {
			return null;
		}

		for (SosLvl entry : SosLvl.values()) {
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
	public static class SosLvlTypeHandler extends AbstractEnumTypeHandler {

		public SosLvlTypeHandler() {
			super(new EnumType(SosLvl.class, java.sql.Types.INTEGER));
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
