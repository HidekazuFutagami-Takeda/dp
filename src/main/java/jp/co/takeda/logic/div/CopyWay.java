package jp.co.takeda.logic.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;

/**
 * 計画値のコピー方法を表す列挙
 * 
 * @author khashimoto
 */
public enum CopyWay {

	/**
	 * 理論計画[1]を営業所案にコピー
	 */
	THEORETICAL1_TO_OFFICE("1"),

	/**
	 * 理論計画[2]を営業所案にコピー
	 */
	THEORETICAL2_TO_OFFICE("2"),

	/**
	 * 理論計画[1]を決定欄にコピー
	 */
	THEORETICAL1_TO_PLAN("3"),

	/**
	 * 理論計画[2]を決定欄にコピー
	 */
	THEORETICAL2_TO_PLAN("4"),

	/**
	 * 営業所案を決定欄にコピー
	 */
	OFFICE_TO_PLAN("5");

	/**
	 * コンストラクタ
	 * 
	 * @param value 計画値のコピー方法を表す文字
	 */
	private CopyWay(String value) {
		this.value = value;
	}

	/**
	 * コード値
	 */
	private String value;

	/**
	 * 計画値のコピー方法を取得する。
	 * 
	 * @return 計画値のコピー方法を表す文字
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
	public static CopyWay getInstance(String code) {

		if (code == null || code.equals("")) {
			return null;
		}

		for (CopyWay entry : CopyWay.values()) {
			if (entry.getDbValue().equals(code)) {
				return entry;
			}
		}
		final String errMsg = "指定のコード値に対応する列挙が存在しない。code=" + code;
		throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
	}

}
