package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.MrPlanForVac;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン用担当者別計画組織単位のサマリーを表すDTO
 * 
 * @author nozaki
 */
public class MrPlanForVacSosSummaryDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 組織コード
	 */
	private final String sosCd;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 計画値(B)
	 */
	private final Long plannedValueB;

	/**
	 * コンストラクタ
	 * 
	 * @param sosCd 組織コード
	 * @param mrPlan 検索結果の担当者別計画サマリー
	 */
	public MrPlanForVacSosSummaryDto(String sosCd, MrPlanForVac mrPlan) {

		this.sosCd = sosCd;
		this.prodCode = mrPlan.getProdCode();
		this.plannedValueB = mrPlan.getPlannedValueB();
	}

	/**
	 * 組織コードを取得する。
	 * 
	 * @return 組織コード
	 */
	public String getSosCde() {
		return sosCd;
	}

	/**
	 * 品目固定コードを取得する。
	 * 
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 計画値(B)を取得する。
	 * 
	 * @return 計画値(B)
	 */
	public Long getPlannedValueB() {
		return plannedValueB;
	}

	/**
	 * 担当者別計画に変換する。
	 * 
	 * @return ワクチン用担当者別計画
	 */
	public MrPlanForVac convertMrPlan() {

		MrPlanForVac mrPlan = new MrPlanForVac();
		mrPlan.setProdCode(prodCode);
		mrPlan.setPlannedValueB(plannedValueB);
		return mrPlan;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
