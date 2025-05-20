package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.WsPlanForVac;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン用特約店別計画追加 明細DTO
 * 
 * @author stakeuchi
 */
public class TmsTytenPlanAddForVacDetailDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 計画対象品目
	 */
	private final PlannedProd plannedProd;

	/**
	 * ワクチン用特約店別計画
	 */
	private final WsPlanForVac wsPlanForVac;

	/**
	 * コンストラクタ
	 * 
	 * @param plannedProd 計画対象品目
	 * @param wsPlanForVac ワクチン用特約店別計画
	 */
	public TmsTytenPlanAddForVacDetailDto(PlannedProd plannedProd, WsPlanForVac wsPlanForVac) {
		this.plannedProd = plannedProd;
		this.wsPlanForVac = wsPlanForVac;
	}

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
	 * ワクチン用特約店別計画を取得する
	 * 
	 * @return ワクチン用特約店別計画
	 */
	public WsPlanForVac getWsPlanForVac() {
		return wsPlanForVac;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
