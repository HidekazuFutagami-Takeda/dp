package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 担当者別計画のチーム別入力状況を表す列挙
 * 
 * @author tkawabata
 */
public enum InputStateForMrPlan implements DbValue<String> {

	/**
	 * 担当者別計画入力中
	 */
	MR_PLAN_INPUTTING("1"),

	/**
	 * 担当者別計画入力完了
	 */
	MR_PLAN_INPUT_FINISHED("2");

	/**
	 * コンストラクタ
	 * 
	 * @param value 担当者別向けステータスを表す文字
	 */
	private InputStateForMrPlan(String value) {
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
	public static InputStateForMrPlan getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (InputStateForMrPlan entry : InputStateForMrPlan.values()) {
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
	public static class InputStateForMrPlanTypeHandler extends AbstractEnumTypeHandler {

		public InputStateForMrPlanTypeHandler() {
			super(new EnumType(InputStateForMrPlan.class, java.sql.Types.CHAR));
		}

	}
}
