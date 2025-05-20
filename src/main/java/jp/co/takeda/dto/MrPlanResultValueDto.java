package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.MonNnu;
import jp.co.takeda.model.view.MonNnuSummary;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.MathUtil;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 担当者別計画　実績情報DTO
 * 
 * @author nozaki
 */
public class MrPlanResultValueDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 前々々期実績
	 */
	private final Long preFarAdvancePeriod;

	/**
	 * 前々期実績
	 */
	private final Long farAdvancePeriod;

	/**
	 * 前期実績
	 */
	private final Long advancePeriod;

	/**
	 * 当期実績
	 */
	private final Long currentPeriod;

	/**
	 * 当期計画値
	 */
	private final Long currentPlanValue;

	/**
	 * 当期遂行率
	 */
	private final Double currentRate;

	/**
	 * コンストラクタ
	 * 
	 * @param monNnuUh 納入実績
	 */
	public MrPlanResultValueDto(MonNnu monNnu) {

		if (monNnu == null) {
			monNnu = new MonNnu();
		}
		this.currentRate = MathUtil.calcRatio(monNnu.getCurrentPeriod(), monNnu.getCurrentPlanValue());
		this.preFarAdvancePeriod = ConvertUtil.parseMoneyToThousandUnit(monNnu.getPreFarAdvancePeriod());
		this.farAdvancePeriod = ConvertUtil.parseMoneyToThousandUnit(monNnu.getFarAdvancePeriod());
		this.advancePeriod = ConvertUtil.parseMoneyToThousandUnit(monNnu.getAdvancePeriod());
		this.currentPeriod = ConvertUtil.parseMoneyToThousandUnit(monNnu.getCurrentPeriod());
		this.currentPlanValue = ConvertUtil.parseMoneyToThousandUnit(monNnu.getCurrentPlanValue());
	}

	/**
	 * コンストラクタ
	 * 
	 * @param monNnuUh 納入実績
	 */
	public MrPlanResultValueDto(MonNnuSummary monNnu) {

		if (monNnu == null) {
			monNnu = new MonNnuSummary();
		}
		this.currentRate = MathUtil.calcRatio(monNnu.getCurrentPeriod(), monNnu.getCurrentPlanValue());
		this.preFarAdvancePeriod = ConvertUtil.parseMoneyToThousandUnit(monNnu.getPreFarAdvancePeriod());
		this.farAdvancePeriod = ConvertUtil.parseMoneyToThousandUnit(monNnu.getFarAdvancePeriod());
		this.advancePeriod = ConvertUtil.parseMoneyToThousandUnit(monNnu.getAdvancePeriod());
		this.currentPeriod = ConvertUtil.parseMoneyToThousandUnit(monNnu.getCurrentPeriod());
		this.currentPlanValue = ConvertUtil.parseMoneyToThousandUnit(monNnu.getCurrentPlanValue());
	}

	/**
	 * 前々々期実績を取得する。
	 * 
	 * @return 前々々期実績
	 */
	public Long getPreFarAdvancePeriod() {
		return preFarAdvancePeriod;
	}

	/**
	 * 前々期実績を取得する。
	 * 
	 * @return 前々期実績
	 */
	public Long getFarAdvancePeriod() {
		return farAdvancePeriod;
	}

	/**
	 * 前期実績を取得する。
	 * 
	 * @return 前期実績
	 */
	public Long getAdvancePeriod() {
		return advancePeriod;
	}

	/**
	 * 当期実績を取得する。
	 * 
	 * @return 当期実績
	 */
	public Long getCurrentPeriod() {
		return currentPeriod;
	}

	/**
	 * 当期計画値を取得する。
	 * 
	 * @return 当期計画値
	 */
	public Long getCurrentPlanValue() {
		return currentPlanValue;
	}

	/**
	 * 遂行率（当期実績/当期計画）を取得する。
	 * 
	 * @return 遂行率（当期実績/当期計画）
	 */
	public Double getCurrentRate() {
		return currentRate;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
