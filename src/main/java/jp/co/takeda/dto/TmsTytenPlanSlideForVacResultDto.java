package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.StatusForWsSlide;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン用特約店別計画スライドの検索結果格納DTO
 * 
 * @author stakeuchi
 */
public class TmsTytenPlanSlideForVacResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * スライドステータス
	 */
	private final StatusForWsSlide statusSlideForWs;

	/**
	 * スライド開始日時
	 */
	private final Date slideStartDate;

	/**
	 * スライド終了日時
	 */
	private final Date slideEndDate;

	/**
	 * 最終更新者
	 */
	private final String upUserName;

	/**
	 * 最終更新日
	 */
	private final Date upDate;

	/**
	 * コンストラクタ
	 * 
	 * @param statusSlideForWs スライドステータス
	 * @param slideStartDate スライド開始日時
	 * @param slideEndDate スライド終了日時
	 * @param upUserName 最終更新者
	 * @param upDate 最終更新日
	 */
	public TmsTytenPlanSlideForVacResultDto(StatusForWsSlide statusSlideForWs, Date slideStartDate, Date slideEndDate, String upUserName, Date upDate) {
		this.statusSlideForWs = statusSlideForWs;
		this.slideStartDate = slideStartDate;
		this.slideEndDate = slideEndDate;
		this.upUserName = upUserName;
		this.upDate = upDate;
	}

	// -------------------------------
	// getter
	// -------------------------------
	/**
	 * スライドステータスを取得する。
	 * 
	 * @return スライドステータス
	 */
	public StatusForWsSlide getStatusSlideForWs() {
		return statusSlideForWs;
	}

	/**
	 * スライド開始日時を取得する。
	 * 
	 * @return スライド開始日時
	 */
	public Date getSlideStartDate() {
		return slideStartDate;
	}

	/**
	 * スライド終了日時を取得する。
	 * 
	 * @return スライド終了日時
	 */
	public Date getSlideEndDate() {
		return slideEndDate;
	}

	/**
	 * 最終更新者を取得する。
	 * 
	 * @return 最終更新者
	 */
	public String getUpUserName() {
		return upUserName;
	}

	/**
	 * 最終更新日を取得する。
	 * 
	 * @return 最終更新日
	 */
	public Date getUpDate() {
		return upDate;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
