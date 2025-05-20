package jp.co.takeda.logic.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.div.StatusForMrPlan;

/**
 * ステータスチェック用の担当者別計画ステータスを表す列挙
 * 
 * @author nozaki
 */
public enum MrStatusForCheck implements StatusCheckValue<StatusForMrPlan, String> {

	/**
	 * 試算中
	 */
	ESTIMATING(StatusForMrPlan.ESTIMATING, "試算中"),

	/**
	 * 試算済
	 */
	ESTIMATED(StatusForMrPlan.ESTIMATED, "試算済"),

	/**
	 * チーム別計画公開
	 */
	TEAM_PLAN_OPENED(StatusForMrPlan.TEAM_PLAN_OPENED, "チーム別計画公開"),

	/**
	 * チーム別計画確定
	 */
	TEAM_PLAN_COMPLETED(StatusForMrPlan.TEAM_PLAN_COMPLETED, "チーム別計画確定"),

	/**
	 * 担当者別計画公開
	 */
	MR_PLAN_OPENED(StatusForMrPlan.MR_PLAN_OPENED, "担当者別計画公開"),

	/**
	 * 担当者別計画入力完了(AL修正)
	 */
	MR_PLAN_INPUT_FINISHED(StatusForMrPlan.MR_PLAN_INPUT_FINISHED, "担当者別計画入力完了(AL修正)"),

	/**
	 * 担当者別計画確定(所長確定)
	 */
	MR_PLAN_COMPLETED(StatusForMrPlan.MR_PLAN_COMPLETED, "担当者別計画確定(所長確定)"),

	/**
	 * ステータスなし
	 */
	NOTHING(null, "未登録");

	/**
	 * 担当者別計画ステータス
	 */
	private StatusForMrPlan status;

	/**
	 * ステータスの意味
	 */
	private String statusMean;

	/**
	 * コンストラクタ
	 * 
	 * @param status 担当者別計画ステータス
	 * @param statusMean ステータスの意味
	 */
	private MrStatusForCheck(StatusForMrPlan status, String statusMean) {
		this.status = status;
		this.statusMean = statusMean;
	}

	/**
	 * 担当者別計画ステータスを取得する。
	 * 
	 * @return 担当者別計画ステータス
	 */
	public StatusForMrPlan getStatus() {
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
	 * 担当者別計画ステータスから列挙を逆引きする。
	 * 
	 * @param status 担当者別計画ステータス
	 * @return 列挙
	 */
	public static MrStatusForCheck getInstance(StatusForMrPlan status) {

		if (status == null) {
			return null;
		}

		for (MrStatusForCheck entry : MrStatusForCheck.values()) {
			if (entry.getStatus() == status) {
				return entry;
			}
		}
		final String errMsg = "指定のコード値に対応する列挙が存在しない";
		throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
	}

}
