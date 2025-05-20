package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.util.MathUtil;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 納入実績の組織単位のサマリの明細を表すＤＴＯクラス
 * 
 * @author tkawabata
 */
public class DeliveryResultSummaryDetailDto extends DpDto {

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
	 * 遂行率
	 */
	private final Double currentRate;

	/**
	 * コンストラクタ
	 * 
	 * @param preFarAdvancePeriod 前々々期実績
	 * @param farAdvancePeriod 前々期実績
	 * @param advancePeriod 前期実績
	 * @param currentPeriod 当期実績
	 * @param currentPlanValue 当期計画値
	 */
	public DeliveryResultSummaryDetailDto(Long preFarAdvancePeriod, Long farAdvancePeriod, Long advancePeriod, Long currentPeriod, Long currentPlanValue) {
		this.preFarAdvancePeriod = preFarAdvancePeriod;
		this.farAdvancePeriod = farAdvancePeriod;
		this.advancePeriod = advancePeriod;
		this.currentPeriod = currentPeriod;
		this.currentPlanValue = currentPlanValue;
		this.currentRate = MathUtil.calcRatio(currentPeriod, currentPlanValue);
	}

	//---------------------
	// Getter
	// --------------------

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
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
	 * 遂行率を取得する。
	 * 
	 * @return 遂行率
	 */
	public Double getCurrentRate() {
		return currentRate;
	}
}
