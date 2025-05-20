package jp.co.takeda.logic.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.div.StatusForWsSlide;

/**
 * ステータスチェック用の特約店別計画スライドステータスを表す列挙
 * 
 * @author nozaki
 */
public enum WsSlideStatusForCheck implements StatusCheckValue<StatusForWsSlide, String> {

	/**
	 * StatusForWs
	 */
	NONE(StatusForWsSlide.NONE, "スライド前"),

	/**
	 * StatusForWs
	 */
	SLIDING(StatusForWsSlide.SLIDING, "スライド中"),

	/**
	 * スライド済
	 */
	SLIDED(StatusForWsSlide.SLIDED, "スライド済"),

	/**
	 * ステータスなし
	 */
	NOTHING(null, "未登録");

	/**
	 * 特約店別計画スライドステータス
	 */
	private StatusForWsSlide status;

	/**
	 * ステータスの意味
	 */
	private String statusMean;

	/**
	 * コンストラクタ
	 * 
	 * @param status 特約店別計画スライドステータス
	 * @param statusMean ステータスの意味
	 */
	private WsSlideStatusForCheck(StatusForWsSlide status, String statusMean) {
		this.status = status;
		this.statusMean = statusMean;
	}

	/**
	 * 特約店別計画スライドステータスを取得する。
	 * 
	 * @return 特約店別計画スライドステータス
	 */
	public StatusForWsSlide getStatus() {
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
	 * 特約店別計画スライドステータスから列挙を逆引きする。
	 * 
	 * @param status 特約店別計画スライドステータス
	 * @return 列挙
	 */
	public static WsSlideStatusForCheck getInstance(StatusForWsSlide status) {

		if (status == null) {
			return null;
		}

		for (WsSlideStatusForCheck entry : WsSlideStatusForCheck.values()) {
			if (entry.getStatus() == status) {
				return entry;
			}
		}
		final String errMsg = "指定のコード値に対応する列挙が存在しない";
		throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
	}
}
