package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 特定施設個別計画における計画立案区分を表す列挙
 * 
 * @author tkawabata
 */
public enum PlanType implements DbValue<String> {

	/**
	 * 営業所案
	 */
	OFFICE("1"),

	/**
	 * 担当者立案
	 */
	MR("2");

	/**
	 * コンストラクタ
	 * 
	 * @param value 計画立案区分を表す文字
	 */
	private PlanType(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 計画立案区分を表す文字を取得する。
	 * 
	 * @return 計画立案区分を表す文字
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
	public static PlanType getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (PlanType entry : PlanType.values()) {
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
	public static class PlanTypeTypeHandler extends AbstractEnumTypeHandler {

		public PlanTypeTypeHandler() {
			super(new EnumType(PlanType.class, java.sql.Types.CHAR));
		}

	}
}
