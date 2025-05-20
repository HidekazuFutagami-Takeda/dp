package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.MrPlanForVac;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.MathUtil;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン用施設特約店別計画サマリ情報
 * 
 * @author nozaki
 */
public class InsWsPlanForVacPlannedSummaryDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 担当者別計画
	 */
	private final Long mrPlanPlannedValue;

	/**
	 * 施設特約店別計画積上
	 */
	private final Long insWsPlanPlannedValue;

	/**
	 * 調整金額
	 */
	private final Long sagaku;

	/**
	 * コンストラクタ
	 * 
	 * @param mrPlanSummary 担当者別計画サマリ
	 * @param insWsPlanSummary 施設特約店別計画サマリ
	 */
	public InsWsPlanForVacPlannedSummaryDto(MrPlanForVacSosSummaryDto mrPlanSummary, InsWsPlanForVacProdSummaryDto insWsPlanSummary) {

		Long _mrPlanPlannedValue = null;
		Long _insWsPlanPlannedValue = null;

		if (mrPlanSummary != null) {
			_mrPlanPlannedValue = mrPlanSummary.getPlannedValueB();
		}
		if (insWsPlanSummary != null) {
			_insWsPlanPlannedValue = insWsPlanSummary.getPlannedValueB();
		}

		this.mrPlanPlannedValue = ConvertUtil.parseMoneyToThousandUnit(_mrPlanPlannedValue);
		this.insWsPlanPlannedValue = ConvertUtil.parseMoneyToThousandUnit(_insWsPlanPlannedValue);
		this.sagaku = MathUtil.sub(mrPlanPlannedValue, insWsPlanPlannedValue, true);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param mrPlan 担当者別計画
	 * @param insWsPlanSummary 施設特約店別計画サマリ
	 */
	public InsWsPlanForVacPlannedSummaryDto(MrPlanForVac mrPlan, InsWsPlanForVacProdSummaryDto insWsPlanSummary) {

		Long _mrPlanPlannedValue = null;
		Long _insWsPlanPlannedValue = null;

		if (mrPlan != null) {
			_mrPlanPlannedValue = mrPlan.getPlannedValueB();
		}
		if (insWsPlanSummary != null) {
			_insWsPlanPlannedValue = insWsPlanSummary.getPlannedValueB();
		}

		this.mrPlanPlannedValue = ConvertUtil.parseMoneyToThousandUnit(_mrPlanPlannedValue);
		this.insWsPlanPlannedValue = ConvertUtil.parseMoneyToThousandUnit(_insWsPlanPlannedValue);
		this.sagaku = MathUtil.sub(mrPlanPlannedValue, insWsPlanPlannedValue, true);
	}

	/**
	 * 担当者別計画を取得する。
	 * 
	 * @return 担当者別計画
	 */
	public Long getMrPlanPlannedValue() {
		return mrPlanPlannedValue;
	}

	/**
	 * 施設特約店別計画積上を取得する。
	 * 
	 * @return 施設特約店別計画積上
	 */
	public Long getInsWsPlanPlannedValue() {
		return insWsPlanPlannedValue;
	}

	/**
	 * 調整金額を取得する。
	 * 
	 * @return 調整金額
	 */
	public Long getSagaku() {
		return sagaku;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
