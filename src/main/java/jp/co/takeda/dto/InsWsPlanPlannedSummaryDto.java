package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.MathUtil;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設特約店別計画サマリ情報
 * 
 * @author nozaki
 */
public class InsWsPlanPlannedSummaryDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 担当者別計画UH
	 */
	private final Long mrPlanPlannedValueUh;

	/**
	 * 施設特約店別計画積上UH
	 */
	private final Long insWsPlanPlannedValueUh;

	/**
	 * 調整金額UH
	 */
	private final Long sagakuUh;

	/**
	 * 担当者別計画P
	 */
	private final Long mrPlanPlannedValueP;

	/**
	 * 施設特約店別計画積上P
	 */
	private final Long insWsPlanPlannedValueP;

	/**
	 * 調整金額P
	 */
	private final Long sagakuP;

	/**
	 * コンストラクタ
	 * 
	 * @param mrPlanSummary 担当者別計画サマリ
	 * @param insWsPlanSummary 施設特約店別計画サマリ
	 */
	public InsWsPlanPlannedSummaryDto(MrPlanSosSummaryDto mrPlanSummary, InsWsPlanProdSummaryDto insWsPlanSummary) {

		if (mrPlanSummary == null) {
			mrPlanSummary = new MrPlanSosSummaryDto(null, new MrPlan());
		}
		if (insWsPlanSummary == null) {
			insWsPlanSummary = new InsWsPlanProdSummaryDto(null, null, null, null, null);
		}

		this.mrPlanPlannedValueUh = ConvertUtil.parseMoneyToThousandUnit(mrPlanSummary.getPlannedValueUhY());
		this.insWsPlanPlannedValueUh = ConvertUtil.parseMoneyToThousandUnit(insWsPlanSummary.getPlannedValueUhY());
		this.sagakuUh = MathUtil.sub(mrPlanPlannedValueUh, insWsPlanPlannedValueUh, true);
		this.mrPlanPlannedValueP = ConvertUtil.parseMoneyToThousandUnit(mrPlanSummary.getPlannedValuePY());
		this.insWsPlanPlannedValueP = ConvertUtil.parseMoneyToThousandUnit(insWsPlanSummary.getPlannedValuePY());
		this.sagakuP = MathUtil.sub(mrPlanPlannedValueP, insWsPlanPlannedValueP, true);
	}

	/**
	 * 担当者別計画UHを取得する。
	 * 
	 * @return 担当者別計画UH
	 */
	public Long getMrPlanPlannedValueUh() {
		return mrPlanPlannedValueUh;
	}

	/**
	 * 施設特約店別計画積上UHを取得する。
	 * 
	 * @return 施設特約店別計画積上UH
	 */
	public Long getInsWsPlanPlannedValueUh() {
		return insWsPlanPlannedValueUh;
	}

	/**
	 * 調整金額UHを取得する。
	 * 
	 * @return 調整金額UH
	 */
	public Long getSagakuUh() {
		return sagakuUh;
	}

	/**
	 * 担当者別計画Pを取得する。
	 * 
	 * @return 担当者別計画P
	 */
	public Long getMrPlanPlannedValueP() {
		return mrPlanPlannedValueP;
	}

	/**
	 * 施設特約店別計画積上Pを取得する。
	 * 
	 * @return 施設特約店別計画積上P
	 */
	public Long getInsWsPlanPlannedValueP() {
		return insWsPlanPlannedValueP;
	}

	/**
	 * 調整金額Pを取得する。
	 * 
	 * @return 調整金額P
	 */
	public Long getSagakuP() {
		return sagakuP;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
