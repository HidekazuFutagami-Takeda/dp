package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 配分先区分を表す列挙
 * 
 * @author tkawabata
 */
public enum DistributionType implements DbValue<String> {

	/**
	 * 施設特約店別計画
	 */
	INS_WS_PLAN("1"),

	/**
	 * 特約店別計画
	 */
	WS_PLAN("2");

	/**
	 * コンストラクタ
	 * 
	 * @param value 配分先区分を表す文字
	 */
	private DistributionType(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 期を表す文字を取得する。
	 * 
	 * @return 期を表す文字
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
	public static DistributionType getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (DistributionType entry : DistributionType.values()) {
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
	public static class DistributionTypeTypeHandler extends AbstractEnumTypeHandler {

		public DistributionTypeTypeHandler() {
			super(new EnumType(DistributionType.class, java.sql.Types.CHAR));
		}

	}
}
