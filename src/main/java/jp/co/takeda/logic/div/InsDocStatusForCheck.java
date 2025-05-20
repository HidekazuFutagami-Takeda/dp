package jp.co.takeda.logic.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.div.StatusForInsDocPlan;

/**
 * ステータスチェック用の施設医師別計画ステータスを表す列挙
 * 
 * @author nozaki
 */
public enum InsDocStatusForCheck implements StatusCheckValue<StatusForInsDocPlan, String> {

	/**
	 * 配分中
	 */
	DISTING(StatusForInsDocPlan.DISTING, "配分中"),

	/**
	 * 配分済
	 */
	DISTED(StatusForInsDocPlan.DISTED, "配分済"),

	/**
	 * 施設医師別計画公開
	 */
	PLAN_OPENED(StatusForInsDocPlan.PLAN_OPENED, "施設医師別計画公開"),

	/**
	 * 施設医師別計画確定
	 */
	PLAN_COMPLETED(StatusForInsDocPlan.PLAN_COMPLETED, "施設医師別計画確定"),

	/**
	 * ステータスなし
	 */
	NOTHING(null, "未登録");

	/**
	 * 施設医師別計画ステータス
	 */
	private StatusForInsDocPlan status;

	/**
	 * ステータスの意味
	 */
	private String statusMean;

	/**
	 * コンストラクタ
	 * 
	 * @param status 施設医師別計画ステータス
	 * @param statusMean ステータスの意味
	 */
	private InsDocStatusForCheck(StatusForInsDocPlan status, String statusMean) {
		this.status = status;
		this.statusMean = statusMean;
	}

	/**
	 * 施設医師別計画ステータスを取得する。
	 * 
	 * @return 施設医師別計画ステータス
	 */
	public StatusForInsDocPlan getStatus() {
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
	 * 施設医師別計画ステータスから列挙を逆引きする。
	 * 
	 * @param status 施設医師別計画ステータス
	 * @return 列挙
	 */
	public static InsDocStatusForCheck getInstance(StatusForInsDocPlan status) {

		if (status == null) {
			return null;
		}

		for (InsDocStatusForCheck entry : InsDocStatusForCheck.values()) {
			if (entry.getStatus() == status) {
				return entry;
			}
		}
		final String errMsg = "指定のコード値に対応する列挙が存在しない";
		throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
	}
}
