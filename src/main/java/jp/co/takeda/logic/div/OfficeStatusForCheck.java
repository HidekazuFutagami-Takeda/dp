package jp.co.takeda.logic.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.div.StatusForOffice;

/**
 * ステータスチェック用の営業所計画ステータスを表す列挙
 * 
 * @author nozaki
 */
public enum OfficeStatusForCheck implements StatusCheckValue<StatusForOffice, String> {

	/**
	 * 下書中
	 */
	DRAFT(StatusForOffice.DRAFT, "下書中"),

	/**
	 * 確定済
	 */
	COMPLETED(StatusForOffice.COMPLETED, "確定済"),

	/**
	 * ステータスなし
	 */
	NOTHING(null, "未登録");

	/**
	 * 営業所計画ステータス
	 */
	private StatusForOffice status;

	/**
	 * ステータスの意味
	 */
	private String statusMean;

	/**
	 * コンストラクタ
	 * 
	 * @param status 営業所計画ステータス
	 * @param statusMean ステータスの意味
	 */
	private OfficeStatusForCheck(StatusForOffice status, String statusMean) {
		this.status = status;
		this.statusMean = statusMean;
	}

	/**
	 * 営業所計画ステータスを取得する。
	 * 
	 * @return 営業所計画ステータス
	 */
	public StatusForOffice getStatus() {
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
	 * 営業所計画ステータスから列挙を逆引きする。
	 * 
	 * @param status 営業所計画ステータス
	 * @return 列挙
	 */
	public static OfficeStatusForCheck getInstance(StatusForOffice status) {

		if (status == null) {
			return null;
		}

		for (OfficeStatusForCheck entry : OfficeStatusForCheck.values()) {
			if (entry.getStatus() == status) {
				return entry;
			}
		}
		final String errMsg = "指定のコード値に対応する列挙が存在しない";
		throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
	}
}
