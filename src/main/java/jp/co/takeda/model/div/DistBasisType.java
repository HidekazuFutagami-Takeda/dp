package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 施設特約店別計画配分元を表す列挙
 * 
 * @author tkawabata
 */
public enum DistBasisType implements DbValue<String> {

	/**
	 * 営業所計画
	 */
	OFFICE_PLAN("1"),

	/**
	 * 担当者別計画
	 */
	MR_PLAN("2");

	/**
	 * コンストラクタ
	 * 
	 * @param value 施設特約店別計画配分元を表す文字
	 */
	private DistBasisType(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 施設特約店別計画配分元を取得する。
	 * 
	 * @return 施設特約店別計画配分元を表す文字
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
	public static DistBasisType getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (DistBasisType entry : DistBasisType.values()) {
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
	public static class DistBasisTypeTypeHandler extends AbstractEnumTypeHandler {

		public DistBasisTypeTypeHandler() {
			super(new EnumType(DistBasisType.class, java.sql.Types.CHAR));
		}

	}
}
