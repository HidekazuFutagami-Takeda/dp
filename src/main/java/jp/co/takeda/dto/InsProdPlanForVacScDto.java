package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.logic.div.PlanData;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設別計画の検索用DTO
 * 
 * @author stakeuchi
 */
public class InsProdPlanForVacScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * 計画
	 */
	private final PlanData planData;

	/**
	 * コンストラクタ
	 * 
	 * @param insNo 施設コード
	 * @param prodCode 計画
	 * 
	 */
	public InsProdPlanForVacScDto(String insNo, PlanData planData) {
		this.insNo = insNo;
		this.planData = planData;
	}

	/**
	 * 施設コードを取得する。
	 * 
	 * @return insNo 施設コード
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 計画を取得する。
	 * 
	 * @return planData 計画
	 */
	public PlanData getPlanData() {
		return planData;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
