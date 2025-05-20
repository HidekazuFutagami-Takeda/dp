package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 特約店別計画配分ステータスを表す列挙
 * 
 * @author tkawabata
 */
public enum StatusForWs implements DbValue<String> {

	/**
	 * 配分前
	 */
	NONE("0"),

	/**
	 * 配分中
	 */
	DISTING("1"),

	/**
	 * 配分済
	 */
	DISTED("2");

	/**
	 * コンストラクタ
	 * 
	 * @param value 特約店別計画配分ステータスを表す文字
	 */
	private StatusForWs(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 特約店別計画配分ステータスを表す文字を取得する。
	 * 
	 * @return 特約店別計画配分ステータスを表す文字
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
	public static StatusForWs getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (StatusForWs entry : StatusForWs.values()) {
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
	public static class StatusForWsTypeHandler extends AbstractEnumTypeHandler {

		public StatusForWsTypeHandler() {
			super(new EnumType(StatusForWs.class, java.sql.Types.CHAR));
		}

	}
}
