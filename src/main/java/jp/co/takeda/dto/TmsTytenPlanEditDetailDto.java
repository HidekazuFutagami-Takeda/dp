package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.WsPlan;

/**
 * 特約店別計画編集 明細DTO
 * 
 * @author yokokawa
 */
public class TmsTytenPlanEditDetailDto extends DpDto {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// -------------------------------
	// constructor
	// -------------------------------
	/**
	 * コンストラクタ
	 * 
	 * @param wsPlanList
	 */
	public TmsTytenPlanEditDetailDto(WsPlan wsPlan, boolean isSummary) {
		this.wsPlan = wsPlan;
		this.isSummary = isSummary;
	}

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 特約店別計画
	 */
	private WsPlan wsPlan;

	/**
	 * 合計行フラグ
	 */
	private boolean isSummary;

	// -------------------------------
	// getter
	// -------------------------------
	/**
	 * 特約店計画を取得する
	 */
	public WsPlan getWsPlan() {
		return wsPlan;
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
