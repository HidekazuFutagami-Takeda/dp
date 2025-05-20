package jp.co.takeda.logic.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.div.InputStateForMrPlan;

/**
 * ステータスチェック用のチーム別入力状況を表す列挙
 * 
 * @author nozaki
 */
public enum TeamInputStatusForCheck implements StatusCheckValue<InputStateForMrPlan, String> {

	/**
	 * 担当者別計画入力中
	 */
	MR_PLAN_INPUTTING(InputStateForMrPlan.MR_PLAN_INPUTTING, "担当者別計画入力中"),

	/**
	 * 担当者別計画入力完了
	 */
	MR_PLAN_INPUT_FINISHED(InputStateForMrPlan.MR_PLAN_INPUT_FINISHED, "担当者別計画入力完了"),

	/**
	 * ステータスなし
	 */
	NOTHING(null, "未登録");

	/**
	 * チーム別入力状況
	 */
	private InputStateForMrPlan status;

	/**
	 * ステータスの意味
	 */
	private String statusMean;

	/**
	 * コンストラクタ
	 * 
	 * @param status チーム別入力状況
	 * @param statusMean ステータスの意味
	 */
	private TeamInputStatusForCheck(InputStateForMrPlan status, String statusMean) {
		this.status = status;
		this.statusMean = statusMean;
	}

	/**
	 * チーム別入力状況を取得する。
	 * 
	 * @return チーム別入力状況
	 */
	public InputStateForMrPlan getStatus() {
		return status;
	}

	/**
	 * ステータスの意味を取得する。
	 * 
	 * @return ステータスの意味
	 */
	public String getStatusMean() {
		return statusMean;
	}

	/**
	 * チーム別入力状況から列挙を逆引きする。
	 * 
	 * @param status チーム別入力状況
	 * @return 列挙
	 */
	public static TeamInputStatusForCheck getInstance(InputStateForMrPlan status) {

		if (status == null) {
			return null;
		}

		for (TeamInputStatusForCheck entry : TeamInputStatusForCheck.values()) {
			if (entry.getStatus() == status) {
				return entry;
			}
		}
		final String errMsg = "指定のコード値に対応する列挙が存在しない";
		throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
	}
}
