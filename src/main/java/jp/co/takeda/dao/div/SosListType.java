package jp.co.takeda.dao.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;

/**
 * 組織一覧の種類を示す列挙
 * 
 * @author khashimoto
 */
public enum SosListType {

	/**
	 * 支店一覧
	 */
	SHITEN_LIST("01"),

	/**
	 * 特約店部一覧
	 */
	TOKUYAKUTEN_LIST("02");

	/**
	 * コンストラクタ
	 * 
	 * @param value 組織一覧の種類を表す文字
	 */
	private SosListType(String value) {
		this.value = value;
	}

	/**
	 * 組織一覧の種類を表す文字
	 */
	private String value;

	/**
	 * 組織一覧の種類を表す文字を取得する。
	 * 
	 * @return 組織一覧の種類を表す文字
	 */
	public String getValue() {
		return value;
	}

	/**
	 * コード値から列挙を逆引きする。
	 * 
	 * @param code コード値
	 * @return 列挙
	 */
	public static SosListType getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (SosListType entry : SosListType.values()) {
			if (entry.getValue().equals(code)) {
				return entry;
			}
		}
		final String errMsg = "指定のコード値に対応する列挙が存在しない。code=" + code;
		throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
	}
}
