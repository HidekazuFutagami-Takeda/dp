package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 担当者別計画向けステータスを表す列挙
 * 
 * @author tkawabata
 */
public enum StatusForMrPlan implements DbValue<String> {

	/**
	 * 試算中
	 */
	ESTIMATING("1"),

	/**
	 * 試算済
	 */
	ESTIMATED("2"),

	/**
	 * チーム別計画公開
	 */
	TEAM_PLAN_OPENED("3"),

	/**
	 * チーム別計画確定
	 */
	TEAM_PLAN_COMPLETED("4"),

	/**
	 * 担当者別計画公開
	 */
	MR_PLAN_OPENED("5"),

	/**
	 * 担当者別計画入力完了(AL修正)
	 */
	MR_PLAN_INPUT_FINISHED("6"),

	/**
	 * 担当者別計画確定(所長確定)
	 */
	MR_PLAN_COMPLETED("7");

	/**
	 * コンストラクタ
	 * 
	 * @param value 担当者別向けステータスを表す文字
	 */
	private StatusForMrPlan(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 担当者別向けステータスを表す文字を取得する。
	 * 
	 * @return 担当者別向けステータスを表す文字
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
	public static StatusForMrPlan getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (StatusForMrPlan entry : StatusForMrPlan.values()) {
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
	public static class StatusForMrPlanTypeHandler extends AbstractEnumTypeHandler {

		public StatusForMrPlanTypeHandler() {
			super(new EnumType(StatusForMrPlan.class, java.sql.Types.CHAR));
		}

	}
}
