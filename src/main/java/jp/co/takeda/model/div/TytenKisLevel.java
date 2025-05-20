package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 特約店の階層レベルを表す列挙
 * 
 * @author khashimoto
 */
public enum TytenKisLevel implements DbValue<String> {

	/**
	 * 本店
	 */
	HONTEN("01"),

	/**
	 * 支社
	 */
	SHISHA("02"),

	/**
	 * 支店
	 */
	SHITEN("03"),

	/**
	 * 課
	 */
	KA("04"),

	/**
	 * ブロック１
	 */
	BLK1("05"),

	/**
	 * ブロック２（拡張用）
	 */
	BLK2("06"),

	/**
	 * その他
	 */
	OTHER("99");

	/**
	 * コンストラクタ
	 * 
	 * @param value 特約店の階層レベルを表す文字
	 */
	private TytenKisLevel(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 特約店の階層レベルを取得する。
	 * 
	 * @return 特約店の階層レベルを表す文字
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
	public static TytenKisLevel getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (TytenKisLevel entry : TytenKisLevel.values()) {
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
	public static class TytenKisLevelTypeHandler extends AbstractEnumTypeHandler {

		public TytenKisLevelTypeHandler() {
			super(new EnumType(TytenKisLevel.class, java.sql.Types.VARCHAR));
		}

	}
}
