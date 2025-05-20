package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.WsPlan;

/**
 * 特約店別計画追加 明細DTO
 * 
 * @author yokokawa
 */
public class TmsTytenPlanAddDetailDto extends DpDto {

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
	 * @param plannedProd 計画対象品目
	 * @param wsPlan 特約店別計画
	 */
	public TmsTytenPlanAddDetailDto(PlannedProd plannedProd, WsPlan wsPlan) {
		this.plannedProd = plannedProd;
		this.wsPlan = wsPlan;
	}

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 計画対象品目
	 */
	private final PlannedProd plannedProd;

	/**
	 * 特約店別計画
	 */
	private final WsPlan wsPlan;

	// -------------------------------
	// getter
	// -------------------------------
	/**
	 * 計画対象品目を取得する
	 * 
	 * @return 計画対象品目
	 */
	public PlannedProd getPlannedProd() {
		return plannedProd;
	}

	/**
	 * 特約店別計画を取得する
	 * 
	 * @return 特約店別計画
	 */
	public WsPlan getWsPlan() {
		return wsPlan;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
