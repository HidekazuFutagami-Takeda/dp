package jp.co.takeda.logic.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.div.StatusForInsWsPlan;

/**
 * ステータスチェック用の施設特約店別計画ステータスを表す列挙
 * 
 * @author nozaki
 */
public enum InsWsStatusForCheck implements StatusCheckValue<StatusForInsWsPlan, String> {

	/**
	 * 配分中
	 */
	DISTING(StatusForInsWsPlan.DISTING, "配分中"),

	/**
	 * 配分済
	 */
	DISTED(StatusForInsWsPlan.DISTED, "配分済"),

	/**
	 * 施設特約店別計画公開
	 * （改定により、ワクチンのみ利用）
	 */
	PLAN_OPENED(StatusForInsWsPlan.PLAN_OPENED, "施設特約店別計画公開"),

	/**
	 * 施設特約店別計画確定
	 */
	PLAN_COMPLETED(StatusForInsWsPlan.PLAN_COMPLETED, "施設特約店別計画確定"),

	/**
	 * ステータスなし
	 */
	NOTHING(null, "未登録");

	/**
	 * 施設特約店別計画ステータス
	 */
	private StatusForInsWsPlan status;

	/**
	 * ステータスの意味
	 */
	private String statusMean;

	/**
	 * コンストラクタ
	 * 
	 * @param status 施設特約店別計画ステータス
	 * @param statusMean ステータスの意味
	 */
	private InsWsStatusForCheck(StatusForInsWsPlan status, String statusMean) {
		this.status = status;
		this.statusMean = statusMean;
	}

	/**
	 * 施設特約店別計画ステータスを取得する。
	 * 
	 * @return 施設特約店別計画ステータス
	 */
	public StatusForInsWsPlan getStatus() {
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
	 * 施設特約店別計画ステータスから列挙を逆引きする。
	 * 
	 * @param status 施設特約店別計画ステータス
	 * @return 列挙
	 */
	public static InsWsStatusForCheck getInstance(StatusForInsWsPlan status) {

		if (status == null) {
			return null;
		}

		for (InsWsStatusForCheck entry : InsWsStatusForCheck.values()) {
			if (entry.getStatus() == status) {
				return entry;
			}
		}
		final String errMsg = "指定のコード値に対応する列挙が存在しない";
		throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
	}
}
