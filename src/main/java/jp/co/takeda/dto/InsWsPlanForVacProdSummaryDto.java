package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.InsWsPlanForVac;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設特約店別計画品目単位のサマリーを表すDTO
 * 
 * @author nozaki
 */
public class InsWsPlanForVacProdSummaryDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 配分値(B)
	 */
	private final Long distValueB;

	/**
	 * 計画値(B)
	 */
	private final Long plannedValueB;

	/**
	 * 
	 * コンストラクタ
	 * 
	 * @param prodCode 品目固定コード
	 * @param insWsPlan ワクチン用施設特約店別計画
	 */
	public InsWsPlanForVacProdSummaryDto(String prodCode, InsWsPlanForVac insWsPlan) {
		this.prodCode = prodCode;
		this.distValueB = insWsPlan.getDistValueB();
		this.plannedValueB = insWsPlan.getPlannedValueB();
	}

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 品目固定コードを取得する。
	 * 
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 配分値(B)を取得する。
	 * 
	 * @return 配分値(B)
	 */
	public Long getDistValueB() {
		return distValueB;
	}

	/**
	 * 計画値(B)を取得する。
	 * 
	 * @return 計画値(B)
	 */
	public Long getPlannedValueB() {
		return plannedValueB;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
