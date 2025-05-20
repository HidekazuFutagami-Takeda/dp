package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 施設特約店別向けステータスを表す列挙
 * 
 * @author tkawabata
 */
public enum StatusForInsWsPlan implements DbValue<String> {

	/**
	 * 配分中
	 */
	DISTING("1"),

	/**
	 * 配分済
	 */
	DISTED("2"),

	/**
	 * 施設特約店別計画公開
	 * （改定により、ワクチンのみ利用）
	 */
	PLAN_OPENED("3"),

	/**
	 * 施設特約店別計画確定
	 */
	PLAN_COMPLETED("4");

	/**
	 * コンストラクタ
	 * 
	 * @param value 施設特約店別向けステータスを表す文字
	 */
	private StatusForInsWsPlan(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 施設特約店別向けステータスを表す文字を取得する。
	 * 
	 * @return 施設特約店別向けステータスを表す文字
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
	public static StatusForInsWsPlan getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (StatusForInsWsPlan entry : StatusForInsWsPlan.values()) {
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
	public static class StatusForInsWsPlanTypeHandler extends AbstractEnumTypeHandler {

		public StatusForInsWsPlanTypeHandler() {
			super(new EnumType(StatusForInsWsPlan.class, java.sql.Types.CHAR));
		}

	}
}
