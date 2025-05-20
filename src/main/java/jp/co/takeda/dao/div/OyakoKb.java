package jp.co.takeda.dao.div;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;

/**
 * 親子区分を示す列挙
 *
 * @author khashimoto
 */

public enum OyakoKb {
	/**
	 * 武田紐
	 */
	TAKEDA("00001"),

	/**
	 * RG紐
	 */
	RG("02"),

	/**
	 * 品目毎紐づけ
	 */
	EACH_PROD("ZZZZZ");

	/**
	 * コンストラクタ
	 *
	 * @param value 親子区分を表す文字列
	 */
	private OyakoKb(String value) {
		this.value = value;
	}

	/**
	 * 親子区分の種類を表す文字
	 */
	private String value;

	/**
	 * 親子区分の種類を表す文字を取得する。
	 *
	 * @return 親子区分の種類を表す文字
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
	public static OyakoKb getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (OyakoKb entry : OyakoKb.values()) {
			if (entry.getValue().equals(code)) {
				return entry;
			}
		}
		final String errMsg = "指定のコード値に対応する列挙が存在しない。code=" + code;
		throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
	}

}
