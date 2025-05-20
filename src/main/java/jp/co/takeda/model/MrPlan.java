package jp.co.takeda.model;

import jp.co.takeda.bean.DpModel;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 担当者別計画を表すモデルクラス
 * 
 * @author tkawabata
 */
public class MrPlan extends DpModel<MrPlan> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 従業員番号
	 */
	private Integer jgiNo;

	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 特定施設個別計画値UH(Y)
	 */
	private Long specialInsPlanValueUhY;

	/**
	 * 理論値UH1
	 */
	private Long theoreticalValueUh1;

	/**
	 * 理論値UH2
	 */
	private Long theoreticalValueUh2;

	/**
	 * 営業所案UH(Y)
	 */
	private Long officeValueUhY;

	/**
	 * 計画値UH(Y)
	 */
	private Long plannedValueUhY;

	/**
	 * 特定施設個別計画値P(Y)
	 */
	private Long specialInsPlanValuePY;

	/**
	 * 理論値P1
	 */
	private Long theoreticalValueP1;

	/**
	 * 理論値P2
	 */
	private Long theoreticalValueP2;

	/**
	 * 営業所案P(Y)
	 */
	private Long officeValuePY;

	/**
	 * 計画値P(Y)
	 */
	private Long plannedValuePY;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 従業員番号を取得する。
	 * 
	 * @return 従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 従業員番号を設定する。
	 * 
	 * @param jgiNo 従業員番号
	 */
	public void setJgiNo(Integer jgiNo) {
		this.jgiNo = jgiNo;
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
	 * 品目固定コードを設定する。
	 * 
	 * @param prodCode 品目固定コード
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * 特定施設個別計画値UH(Y)を取得する。
	 * 
	 * @return 特定施設個別計画値UH(Y)
	 */
	public Long getSpecialInsPlanValueUhY() {
		return specialInsPlanValueUhY;
	}

	/**
	 * 特定施設個別計画値UH(Y)を設定する。
	 * 
	 * @param specialInsPlanValueUhY 特定施設個別計画値UH(Y)
	 */
	public void setSpecialInsPlanValueUhY(Long specialInsPlanValueUhY) {
		this.specialInsPlanValueUhY = specialInsPlanValueUhY;
	}

	/**
	 * 理論値UH1を取得する。
	 * 
	 * @return 理論値UH1
	 */
	public Long getTheoreticalValueUh1() {
		return theoreticalValueUh1;
	}

	/**
	 * 理論値UH1を設定する。
	 * 
	 * @param theoreticalValueUh1 理論値UH1
	 */
	public void setTheoreticalValueUh1(Long theoreticalValueUh1) {
		this.theoreticalValueUh1 = theoreticalValueUh1;
	}

	/**
	 * 理論値UH2を取得する。
	 * 
	 * @return 理論値UH2
	 */
	public Long getTheoreticalValueUh2() {
		return theoreticalValueUh2;
	}

	/**
	 * 理論値UH2を設定する。
	 * 
	 * @param theoreticalValueUh2 理論値UH2
	 */
	public void setTheoreticalValueUh2(Long theoreticalValueUh2) {
		this.theoreticalValueUh2 = theoreticalValueUh2;
	}

	/**
	 * 営業所案UH(Y)を取得する。
	 * 
	 * @return 営業所案UH(Y)
	 */
	public Long getOfficeValueUhY() {
		return officeValueUhY;
	}

	/**
	 * 営業所案UH(Y)を設定する。
	 * 
	 * @param officeValueUhY 営業所案UH(Y)
	 */
	public void setOfficeValueUhY(Long officeValueUhY) {
		this.officeValueUhY = officeValueUhY;
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
	 * 計画値UH(Y)を設定する。
	 * 
	 * @param plannedValueUhY 計画値UH(Y)
	 */
	public void setPlannedValueUhY(Long plannedValueUhY) {
		this.plannedValueUhY = plannedValueUhY;
	}

	/**
	 * 特定施設個別計画値P(Y)を取得する。
	 * 
	 * @return 特定施設個別計画値P(Y)
	 */
	public Long getSpecialInsPlanValuePY() {
		return specialInsPlanValuePY;
	}

	/**
	 * 特定施設個別計画値P(Y)を設定する。
	 * 
	 * @param specialInsPlanValuePY 特定施設個別計画値P(Y)
	 */
	public void setSpecialInsPlanValuePY(Long specialInsPlanValuePY) {
		this.specialInsPlanValuePY = specialInsPlanValuePY;
	}

	/**
	 * 理論値P1を取得する。
	 * 
	 * @return 理論値P1
	 */
	public Long getTheoreticalValueP1() {
		return theoreticalValueP1;
	}

	/**
	 * 理論値P1を設定する。
	 * 
	 * @param theoreticalValueP1 理論値P1
	 */
	public void setTheoreticalValueP1(Long theoreticalValueP1) {
		this.theoreticalValueP1 = theoreticalValueP1;
	}

	/**
	 * 理論値P2を取得する。
	 * 
	 * @return 理論値P2
	 */
	public Long getTheoreticalValueP2() {
		return theoreticalValueP2;
	}

	/**
	 * 理論値P2を設定する。
	 * 
	 * @param theoreticalValueP2 理論値P2
	 */
	public void setTheoreticalValueP2(Long theoreticalValueP2) {
		this.theoreticalValueP2 = theoreticalValueP2;
	}

	/**
	 * 営業所案P(Y)を取得する。
	 * 
	 * @return 営業所案P(Y)
	 */
	public Long getOfficeValuePY() {
		return officeValuePY;
	}

	/**
	 * 営業所案P(Y)を設定する。
	 * 
	 * @param officeValuePY 営業所案P(Y)
	 */
	public void setOfficeValuePY(Long officeValuePY) {
		this.officeValuePY = officeValuePY;
	}

	/**
	 * 計画値P(Y)を取得する。
	 * 
	 * @return 計画値P(Y)
	 */
	public Long getPlannedValuePY() {
		return plannedValuePY;
	}

	/**
	 * 計画値P(Y)を設定する。
	 * 
	 * @param plannedValuePY 計画値P(Y)
	 */
	public void setPlannedValuePY(Long plannedValuePY) {
		this.plannedValuePY = plannedValuePY;
	}

	@Override
	public int compareTo(MrPlan obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.jgiNo, obj.jgiNo).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && MrPlan.class.isAssignableFrom(entry.getClass())) {
			MrPlan obj = (MrPlan) entry;
			return new EqualsBuilder().append(this.jgiNo, obj.jgiNo).append(this.prodCode, obj.prodCode).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.jgiNo).append(this.prodCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
