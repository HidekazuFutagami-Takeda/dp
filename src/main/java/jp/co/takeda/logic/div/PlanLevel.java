package jp.co.takeda.logic.div;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;

/**
 * 計画立案レベルを示す列挙
 * @author UJU3389
 *
 */
public enum PlanLevel {

	FALSE("0"),
	TRUE("1");

	/**
	 * コンストラクタ
	 *
	 * @param value
	 */
	private PlanLevel(String value) {
		this.value = value;
	}

	/**
	 * 値
	 */
	private String value;

	/**
	 *
	 *
	 * @return
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
	public static SosSrchValue getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (SosSrchValue entry : SosSrchValue.values()) {
			if (entry.getDbValue().equals(code)) {
				return entry;
			}
		}
		final String errMsg = "指定のコード値に対応する列挙が存在しない";
		throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
	}
}
