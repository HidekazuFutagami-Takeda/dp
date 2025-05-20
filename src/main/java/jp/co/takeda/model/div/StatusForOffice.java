package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 営業所計画ステータスを表す列挙
 * 
 * @author tkawabata
 */
public enum StatusForOffice implements DbValue<String> {

	/**
	 * 下書
	 */
	DRAFT("1"),

	/**
	 * 確定
	 */
	COMPLETED("2");

	/**
	 * コンストラクタ
	 * 
	 * @param value 営業所計画ステータスを表す文字
	 */
	private StatusForOffice(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 営業所計画ステータスを表す文字を取得する。
	 * 
	 * @return 営業所計画ステータスを表す文字
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
	public static StatusForOffice getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (StatusForOffice entry : StatusForOffice.values()) {
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
	public static class StatusForOfficeTypeHandler extends AbstractEnumTypeHandler {

		public StatusForOfficeTypeHandler() {
			super(new EnumType(StatusForOffice.class, java.sql.Types.CHAR));
		}

	}
}
