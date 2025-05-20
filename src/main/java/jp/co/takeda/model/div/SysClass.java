package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 管理対象業務分類を表す列挙
 * 
 * @author tkawabata
 */
public enum SysClass implements DbValue<String> {

	/**
	 * 支援
	 */
	DPS("0", "DPS"),

	/**
	 * 管理
	 */
	DPM("1", "DPM");

	/**
	 * コンストラクタ
	 * 
	 * @param value 管理対象業務分類を表す文字
	 */
	private SysClass(String value, String name) {
		this.value = value;
		this.name = name;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 管理対象業務分類を取得する。
	 * 
	 * @return 管理対象業務分類を表す文字
	 */
	public String getDbValue() {
		return value;
	}

	/**
	 * 管理対象業務分類を取得する。
	 * 
	 * @return 管理対象業務分類を表す名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * コード値から列挙を逆引きする。
	 * 
	 * @param code コード値
	 * @return 列挙
	 */
	public static SysClass getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (SysClass entry : SysClass.values()) {
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
	public static class SysClassTypeHandler extends AbstractEnumTypeHandler {

		public SysClassTypeHandler() {
			super(new EnumType(SysClass.class, java.sql.Types.CHAR));
		}

	}
}
