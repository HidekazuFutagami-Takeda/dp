package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 試算タイプを表す列挙
 * 
 * @author khashimoto
 */
public enum CalcType implements DbValue<String> {

	/**
	 * 営業所別計画⇒チーム別計画⇒担当者別計画
	 */
	OFFICE_TEAM_MR("1"),

	/**
	 * 営業所別計画⇒担当者別計画
	 */
	OFFICE_MR("2");

	/**
	 * コンストラクタ
	 * 
	 * @param value 試算タイプを表す文字
	 */
	private CalcType(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 試算タイプを取得する。
	 * 
	 * @return 試算タイプを表す文字
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
	public static CalcType getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (CalcType entry : CalcType.values()) {
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
	public static class CalcTypeTypeHandler extends AbstractEnumTypeHandler {

		public CalcTypeTypeHandler() {
			super(new EnumType(CalcType.class, java.sql.Types.CHAR));
		}
	}
}
