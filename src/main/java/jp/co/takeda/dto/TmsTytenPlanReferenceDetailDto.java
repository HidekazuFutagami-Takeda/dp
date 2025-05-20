package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.WsPlanSummary2;

/**
 * 特約店別計画参照画面 明細行DTO
 *
 * @author yokokawa
 */
public class TmsTytenPlanReferenceDetailDto extends DpDto {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// -------------------------------
	// constructor
	// -------------------------------
	/**
	 * コンストラクタ
	 */
	public TmsTytenPlanReferenceDetailDto(WsPlanSummary2 wsPlanSummary, boolean isSummary) {
		this.wsPlanSummary = wsPlanSummary;
		this.isSummary = isSummary;
	}

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 特約店別計画サマリー
	 */
	private WsPlanSummary2 wsPlanSummary;

	/**
	 * 合計行フラグ
	 */
	private boolean isSummary;

	// -------------------------------
	// getter
	// -------------------------------
	/**
	 * 特約店別計画サマリーを取得する
	 *
	 * @return wsPlanSummary
	 */
	public WsPlanSummary2 getWsPlanSummary() {
		return wsPlanSummary;
	}

	/**
	 * 合計行フラグを取得する
	 *
	 * @return summary
	 */
	public boolean getIsSummary() {
		return isSummary;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
