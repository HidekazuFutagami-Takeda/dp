package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.DeliveryResult;
import jp.co.takeda.model.DeliveryResultForVac;
import jp.co.takeda.model.view.MonNnuSummary;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.MathUtil;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設特約店別計画編集画面の検索結果用DTO（過去実績参照部）
 * 
 * @author siwamoto
 */
public class InsWsPlanUpDateResultMonNnuDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 実績参照品目番号
	 */
	private final Integer refProdNumber;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 品目名称
	 */
	private final String prodName;

	/**
	 * 品目製品区分
	 */
	private final String prodType;

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
	 * 当期計画（計画）
	 */
	private final Long currentPlanValue;

	/**
	 * 当期計画（実績）
	 */
	private final Long currentPeriod;

	/**
	 * 当期計画（遂行率）
	 */
	private final Double currentRate;

	/**
	 * コンストラクタ
	 * 
	 * @param refProdNumber 実績参照品目番号
	 * @param prodCode 品目コード
	 * @param prodName 品目名称
	 * @param prodType 品目製品区分
	 * @param monNnu (簡易版)納入実績
	 */
	public InsWsPlanUpDateResultMonNnuDto(Integer refProdNumber, String prodCode, String prodName, String prodType, MonNnuSummary monNnu) {
		super();
		this.refProdNumber = refProdNumber;
		this.prodCode = prodCode;
		this.prodName = prodName;
		this.prodType = prodType;
		this.preFarAdvancePeriod = ConvertUtil.parseMoneyToThousandUnit(monNnu.getPreFarAdvancePeriod());
		this.farAdvancePeriod = ConvertUtil.parseMoneyToThousandUnit(monNnu.getFarAdvancePeriod());
		this.advancePeriod = ConvertUtil.parseMoneyToThousandUnit(monNnu.getAdvancePeriod());
		this.currentPlanValue = ConvertUtil.parseMoneyToThousandUnit(monNnu.getCurrentPlanValue());
		this.currentPeriod = ConvertUtil.parseMoneyToThousandUnit(monNnu.getCurrentPeriod());
		this.currentRate = MathUtil.calcRatio(currentPeriod, currentPlanValue);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param refProdNumber 実績参照品目番号
	 * @param prodCode 品目コード
	 * @param prodName 品目名称
	 * @param prodType 品目製品区分
	 * @param result 納入実績
	 */
	public InsWsPlanUpDateResultMonNnuDto(Integer refProdNumber, String prodCode, String prodName, String prodType, DeliveryResult result) {
		super();
		this.refProdNumber = refProdNumber;
		this.prodCode = prodCode;
		this.prodName = prodName;
		this.prodType = prodType;
		this.preFarAdvancePeriod = ConvertUtil.parseMoneyToThousandUnit(result.getMonNnu().getPreFarAdvancePeriod());
		this.farAdvancePeriod = ConvertUtil.parseMoneyToThousandUnit(result.getMonNnu().getFarAdvancePeriod());
		this.advancePeriod = ConvertUtil.parseMoneyToThousandUnit(result.getMonNnu().getAdvancePeriod());
		this.currentPlanValue = ConvertUtil.parseMoneyToThousandUnit(result.getMonNnu().getCurrentPlanValue());
		this.currentPeriod = ConvertUtil.parseMoneyToThousandUnit(result.getMonNnu().getCurrentPeriod());
		this.currentRate = MathUtil.calcRatio(currentPeriod, currentPlanValue);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param refProdNumber 実績参照品目番号
	 * @param prodCode 品目コード
	 * @param prodName 品目名称
	 * @param prodType 品目製品区分
	 * @param result ワクチン用納入実績
	 */
	public InsWsPlanUpDateResultMonNnuDto(Integer refProdNumber, String prodCode, String prodName, String prodType, DeliveryResultForVac result) {
		super();
		this.refProdNumber = refProdNumber;
		this.prodCode = prodCode;
		this.prodName = prodName;
		this.prodType = prodType;
		this.preFarAdvancePeriod = ConvertUtil.parseMoneyToThousandUnit(result.getMonNnu().getPreFarAdvancePeriod());
		this.farAdvancePeriod = ConvertUtil.parseMoneyToThousandUnit(result.getMonNnu().getFarAdvancePeriod());
		this.advancePeriod = ConvertUtil.parseMoneyToThousandUnit(result.getMonNnu().getAdvancePeriod());
		this.currentPlanValue = ConvertUtil.parseMoneyToThousandUnit(result.getMonNnu().getCurrentPlanValue());
		this.currentPeriod = ConvertUtil.parseMoneyToThousandUnit(result.getMonNnu().getCurrentPeriod());
		this.currentRate = MathUtil.calcRatio(currentPeriod, currentPlanValue);
	}

	/**
	 * 実績参照品目番号を取得する。
	 * 
	 * @return refProdNumber
	 */
	public Integer getRefProdNumber() {
		return refProdNumber;
	}

	/**
	 * 品目固定コードを取得する。
	 * 
	 * @return prodName
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目名を取得する。
	 * 
	 * @return prodName
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 品目製品区分を取得する。
	 * 
	 * @return prodType
	 */
	public String getProdType() {
		return prodType;
	}

	/**
	 * 前々々期実績を取得する。
	 * 
	 * @return preFarAdvancePeriod 前々々期実績
	 */
	public Long getPreFarAdvancePeriod() {
		return preFarAdvancePeriod;
	}

	/**
	 * 前々期実績を取得する。
	 * 
	 * @return farAdvancePeriod 前々期実績
	 */
	public Long getFarAdvancePeriod() {
		return farAdvancePeriod;
	}

	/**
	 * 前期実績を取得する。
	 * 
	 * @return advancePeriod 前期実績
	 */
	public Long getAdvancePeriod() {
		return advancePeriod;
	}

	/**
	 * 当期計画（計画）を取得する。
	 * 
	 * @return currentPlanValue 当期計画（計画）
	 */
	public Long getCurrentPlanValue() {
		return currentPlanValue;
	}

	/**
	 * 当期計画（実績）を取得する。
	 * 
	 * @return currentPeriod 当期計画（実績）
	 */
	public Long getCurrentPeriod() {
		return currentPeriod;
	}

	/**
	 * 当期計画（遂行率）を取得する。
	 * 
	 * @return currentRate 当期計画（遂行率）
	 */
	public Double getCurrentRate() {
		return currentRate;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
