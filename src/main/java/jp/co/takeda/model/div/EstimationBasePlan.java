package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 試算の母数となる計画を表す列挙
 * 
 * @author tkawabata
 */
public enum EstimationBasePlan implements DbValue<String> {

	/**
	 * 営業所計画
	 */
	OFFICE_PLAN("1"),

	/**
	 * チーム別計画
	 */
	TEAM_PLAN("2");

	/**
	 * コンストラクタ
	 * 
	 * @param value 試算の母数となる計画を表す文字
	 */
	private EstimationBasePlan(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 試算の母数となる計画を表す文字を取得する。
	 * 
	 * @return 試算の母数となる計画を表す文字
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
	public static EstimationBasePlan getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (EstimationBasePlan entry : EstimationBasePlan.values()) {
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
	 * @author tkawabata
	 */
	public static class EstimationBasePlanTypeHandler extends AbstractEnumTypeHandler {

		public EstimationBasePlanTypeHandler() {
			super(new EnumType(EstimationBasePlan.class, java.sql.Types.CHAR));
		}

	}
}
