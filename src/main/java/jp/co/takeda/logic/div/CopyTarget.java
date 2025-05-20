package jp.co.takeda.logic.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;

/**
 * 計画値のコピー対象を表す列挙
 * 
 * @author khashimoto
 */
public enum CopyTarget {

	/**
	 * チーム別計画計画
	 */
	TEAM_PLAN("1"),

	/**
	 * 担当者別計画
	 */
	MR_PLAN("2");

	/**
	 * コンストラクタ
	 * 
	 * @param value 計画値のコピー対象を表す文字
	 */
	private CopyTarget(String value) {
		this.value = value;
	}

	/**
	 * コード値
	 */
	private String value;

	/**
	 * 計画値のコピー対象を取得する。
	 * 
	 * @return 計画値のコピー対象を表す文字
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
	public static CopyTarget getInstance(String code) {

		if (code == null || code.equals("")) {
			return null;
		}

		for (CopyTarget entry : CopyTarget.values()) {
			if (entry.getDbValue().equals(code)) {
				return entry;
			}
		}
		final String errMsg = "指定のコード値に対応する列挙が存在しない。code=" + code;
		throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
	}

}
