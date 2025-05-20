package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * MR種類を表す列挙
 * 
 * @author khashimoto
 */
public enum MrCat implements DbValue<String> {

	/**
	 * MR(COM)
	 */
	MR("01"),

	/**
	 * 小児科MR (J19-0010 対応・コメントのみ修正)
	 */
	VAC_MR("02"),

	/**
	 * MR(ONC)
	 */
	ONC_MR("05"),

	/**
	 * MR(CVM)
	 */
	CVM_MR("08"),

	/**
	 * MR(RS)
	 */
	RS_MR("09"),

	/**
	 * フォローMR
	 */
	FAVORITES("99");

	/**
	 * コンストラクタ
	 * 
	 * @param value MR種類を表す文字
	 */
	private MrCat(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * MR種類を取得する。
	 * 
	 * @return MR種類を表す文字
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
	public static MrCat getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (MrCat entry : MrCat.values()) {
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
	public static class MrCatTypeHandler extends AbstractEnumTypeHandler {

		public MrCatTypeHandler() {
			super(new EnumType(MrCat.class, java.sql.Types.VARCHAR));
		}
	}
}
