package jp.co.takeda.model.view;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (簡易版)納入実績を表すクラス
 * 
 * @author khashimoto
 */
public class MonNnuSummary implements Serializable {

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
	private Long preFarAdvancePeriod;

	/**
	 * 前々期実績
	 */
	private Long farAdvancePeriod;

	/**
	 * 前期実績
	 */
	private Long advancePeriod;

	/**
	 * 当期実績
	 */
	private Long currentPeriod;

	/**
	 * 当期計画値
	 */
	private Long currentPlanValue;

	/**
	 * コンストラクタ
	 */
	public MonNnuSummary() {

	}

	/**
	 * コンストラクタ
	 * 
	 * @param initValue 初期値
	 */
	public MonNnuSummary(Long initValue) {
		preFarAdvancePeriod = 0L;
		farAdvancePeriod = 0L;
		advancePeriod = 0L;
		currentPeriod = 0L;
		currentPlanValue = 0L;
	}

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 前々々期実績を取得する。
	 * 
	 * @return 前々々期実績
	 */
	public Long getPreFarAdvancePeriod() {
		return preFarAdvancePeriod;
	}

	/**
	 * 前々々期実績を設定する。
	 * 
	 * @param preFarAdvancePeriod 前々々期実績
	 */
	public void setPreFarAdvancePeriod(Long preFarAdvancePeriod) {
		this.preFarAdvancePeriod = preFarAdvancePeriod;
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
	 * 前々期実績を設定する。
	 * 
	 * @param farAdvancePeriod 前々期実績
	 */
	public void setFarAdvancePeriod(Long farAdvancePeriod) {
		this.farAdvancePeriod = farAdvancePeriod;
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
	 * 前期実績を設定する。
	 * 
	 * @param advancePeriod 前期実績
	 */
	public void setAdvancePeriod(Long advancePeriod) {
		this.advancePeriod = advancePeriod;
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
	 * 当期実績を設定する。
	 * 
	 * @param currentPeriod 当期実績
	 */
	public void setCurrentPeriod(Long currentPeriod) {
		this.currentPeriod = currentPeriod;
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
	 * 当期計画値を設定する。
	 * 
	 * @param currentPlanValue 当期計画値
	 */
	public void setCurrentPlanValue(Long currentPlanValue) {
		this.currentPlanValue = currentPlanValue;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
