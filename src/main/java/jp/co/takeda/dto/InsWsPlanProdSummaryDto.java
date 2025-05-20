package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設特約店別計画品目単位のサマリーを表すDTO
 * 
 * @author khashimoto
 */
public class InsWsPlanProdSummaryDto extends DpDto {

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
	 * 配分値UH(Y)
	 */
	private final Long distValueUhY;

	/**
	 * 計画値UH(Y)
	 */
	private final Long plannedValueUhY;

	/**
	 * 配分値P(Y)
	 */
	private final Long distValuePY;

	/**
	 * 計画値P(Y)
	 */
	private final Long plannedValuePY;

	/**
	 * 
	 * コンストラクタ
	 * 
	 * @param prodCode 品目固定コード
	 * @param distValueUhY 配分値UH(Y)
	 * @param plannedValueUhY 計画値UH(Y)
	 * @param distValuePY 配分値P(Y)
	 * @param plannedValuePY 計画値P(Y)
	 */
	public InsWsPlanProdSummaryDto(String prodCode, Long distValueUhY, Long plannedValueUhY, Long distValuePY, Long plannedValuePY) {
		this.prodCode = prodCode;
		this.distValueUhY = distValueUhY;
		this.plannedValueUhY = plannedValueUhY;
		this.distValuePY = distValuePY;
		this.plannedValuePY = plannedValuePY;
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
	 * 配分値UH(Y)を取得する。
	 * 
	 * @return 配分値UH(Y)
	 */
	public Long getDistValueUhY() {
		return distValueUhY;
	}

	/**
	 * 計画値UH(Y)を取得する。
	 * 
	 * @return 計画値UH(Y)
	 */
	public Long getPlannedValueUhY() {
		return plannedValueUhY;
	}

	/**
	 * 配分値P(Y)を取得する。
	 * 
	 * @return 配分値P(Y)
	 */
	public Long getDistValuePY() {
		return distValuePY;
	}

	/**
	 * 計画値P(Y)を取得する。
	 * 
	 * @return 計画値P(Y)
	 */
	public Long getPlannedValuePY() {
		return plannedValuePY;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
