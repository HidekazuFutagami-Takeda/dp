package jp.co.takeda.model.div;

import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;

/**
 * 条件セットを表す列挙
 *
 * @author tkawabata
 */
public enum JokenSet implements DbValue<String> {

//	 2015年上期組織変更対応：全体的に名称変更、特約店GM、特約店業務G 利用権限削除

	/**
	 * 本部：JKN0255
	 */
	HONBU("JKN0255"),

	/**
	 * 本部（ワクチン）：JKN0256
	 */
	HONBU_WAKUTIN_G("JKN0256"),

	/**
	 * 全支店長：JKN0011
	 */
	SITEN_MASTER("JKN0011"),

	/**
	 * 全支店S：JKN0013
	 */
	SITEN_STAFF("JKN0013"),

	/**
	 * 全営業所長・GM：JKN0015
	 */
	OFFICE_MASTER("JKN0015"),

	/**
	 * 全営業副所長：JKN0509
	 */
	OFFICE_SUB_MASTER("JKN0509"),

	/**
	 * 全営業所S：JKN0016
	 */
	OFFICE_STAFF("JKN0016"),

	/**
	 * 全AL（ONC除く）/ AC・医薬：JKN0176
	 */
	IYAKU_AL("JKN0176"),

	/**
	 * AC・ワクチン ：JKN0250
	 */
	VACCINE_AC("JKN0250"),

	/**
	 * 全ONC-TL：JKN0340
	 */
	IYAKU_ONC_AL("JKN0340"),

	/**
	 * MR：JKN0023
	 */
	IYAKU_MR("JKN0023"),

	/**
	 * 整形MR：JKN0319
	 */
	@Deprecated
	IYAKU_SEIKEI_MR("JKN0319"),

	/**
	 * 全特約店部長・リージョナルGM・小児科ＡＣ：JKN0037 (J19-0010 対応・コメントのみ修正)
	 */
	WAKUTIN_AL("JKN0037"),

	/**
	 * 小児科MR：JKN0039 (J19-0010 対応・コメントのみ修正)
	 */
	WAKUTIN_MR("JKN0039"),

	/**
	 * 全特約店部長：JKN0036
	 */
	TOKUYAKUTEN_MASTER("JKN0036"),

	/**
	 * 特約店G内勤者：JKN0254
	 */
	TOKUYAKUTEN_G_STAFF("JKN0254"),

	/**
	 * 特約店GM：JKN0037
	 */
	@Deprecated
	TOKUYAKUTEN_G_MASTER("JKN0037"),

	/**
	 * 特約店業務G：JKN0251
	 */
	@Deprecated
	TOKUYAKUTEN_GYOUMU_G("JKN0251"),

	/**
	 * 全特担者：JKN0040
	 */
	TOKUYAKUTEN_G_TANTOU("JKN0040"),

	/**
	 * 特約店GM（兼務なし）
	 */
	TOKUYAKUTEN_GM_KENMU_NASHI("JKN0101"),

	/**
	 * 特約店GM（兼務あり）
	 */
	TOKUYAKUTEN_GM_KENMU_ARI("JKN0102");

	/**
	 * コンストラクタ
	 *
	 * @param value 条件セットを表す文字
	 */
	private JokenSet(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 条件セットを表す文字を取得する。
	 *
	 * @return 条件セットを表す文字
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
	public static JokenSet getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (JokenSet entry : JokenSet.values()) {
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
	public static class JokenSetTypeHandler extends AbstractEnumTypeHandler {

		public JokenSetTypeHandler() {
			super(new EnumType(JokenSet.class, java.sql.Types.VARCHAR));
		}
	}
}
