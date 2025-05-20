package jp.co.takeda.logic.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.div.StatusForWs;

/**
 * ステータスチェック用の特約店別計画配分ステータス
 * 
 * @author nozaki
 */
public enum WsDistStatusForCheck implements StatusCheckValue<StatusForWs, String> {

	/**
	 * 配分前
	 */
	NONE(StatusForWs.NONE, "配分前"),

	/**
	 * 配分中
	 */
	DISTING(StatusForWs.DISTING, "配分中"),

	/**
	 * 配分済
	 */
	DISTED(StatusForWs.DISTED, "配分済"),

	/**
	 * ステータスなし
	 */
	NOTHING(null, "未登録");

	/**
	 * 特約店別計画配分ステータス
	 */
	private StatusForWs status;

	/**
	 * ステータスの意味
	 */
	private String statusMean;

	/**
	 * コンストラクタ
	 * 
	 * @param status 特約店別計画配分ステータス
	 * @param statusMean ステータスの意味
	 */
	private WsDistStatusForCheck(StatusForWs status, String statusMean) {
		this.status = status;
		this.statusMean = statusMean;
	}

	/**
	 * 特約店別計画配分ステータスを取得する。
	 * 
	 * @return 特約店別計画配分ステータス
	 */
	public StatusForWs getStatus() {
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
	 * 特約店別計画配分ステータスから列挙を逆引きする。
	 * 
	 * @param status 特約店別計画配分ステータス
	 * @return 列挙
	 */
	public static WsDistStatusForCheck getInstance(StatusForWs status) {

		if (status == null) {
			return null;
		}

		for (WsDistStatusForCheck entry : WsDistStatusForCheck.values()) {
			if (entry.getStatus() == status) {
				return entry;
			}
		}
		final String errMsg = "指定のコード値に対応する列挙が存在しない";
		throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
	}
}
