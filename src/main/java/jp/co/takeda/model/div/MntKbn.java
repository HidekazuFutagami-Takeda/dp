package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.exp.SystemException;

/**
 * メンテナンス区分を表す列挙
 * 
 * @author tkawabata
 */
public enum MntKbn implements DbValue<String> {

	/**
	 * 手動メンテナンス
	 */
	MANUAL("1"),

	/**
	 * 自動メンテナンス
	 */
	AUTO("2");

	/**
	 * コンストラクタ
	 * 
	 * @param value 施設特約店別計画配分元を表す文字
	 */
	private MntKbn(String value) {
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
	public static MntKbn getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (MntKbn entry : MntKbn.values()) {
			if (entry.getDbValue().equals(code)) {
				return entry;
			}
		}
		final String errMsg = "指定のコード値に対応する列挙が存在しない。code=" + code;
		throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
	}
}
