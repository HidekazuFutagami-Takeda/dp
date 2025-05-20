package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * Attention用の外部システム一意キーを表す列挙
 * 
 * @author khashimoto
 */
public enum AttSystemKey implements DbValue<Integer> {

	/**
	 * 支援
	 */
	DPS_KEY(453),

	/**
	 * 管理
	 */
	DPM_KEY(454);

	/**
	 * コンストラクタ
	 * 
	 * @param value Attention用の外部システム一意キーを表す数値
	 */
	private AttSystemKey(Integer value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private Integer value;

	/**
	 * Attention用の外部システム一意キーを取得する。
	 * 
	 * @return Attention用の外部システム一意キーを表す数値
	 */
	public Integer getDbValue() {
		return value;
	}

	/**
	 * コード値から列挙を逆引きする。
	 * 
	 * @param code コード値
	 * @return 列挙
	 */
	public static AttSystemKey getInstance(Integer code) {

		if (code == null) {
			return null;
		}

		for (AttSystemKey entry : AttSystemKey.values()) {
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
	public static class AttSystemKeyTypeHandler extends AbstractEnumTypeHandler {

		public AttSystemKeyTypeHandler() {
			super(new EnumType(AttSystemKey.class, java.sql.Types.INTEGER));
		}

	}
}
