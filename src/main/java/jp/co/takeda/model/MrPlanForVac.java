package jp.co.takeda.model;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.view.MonNnuSummary;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (ワクチン)担当者別計画を表すクラス
 * 
 * @author tkawabata
 */
public class MrPlanForVac extends DpModel<MrPlanForVac> {

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
	 * 計画値(B)
	 */
	private Long plannedValueB;

	/**
	 * 立案品目の納入実績
	 */
	private MonNnuSummary monNnuSummary;

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
	 * 計画値(B)を取得する。
	 * 
	 * @return 計画値(B)
	 */
	public Long getPlannedValueB() {
		return plannedValueB;
	}

	/**
	 * 計画値(B)を設定する。
	 * 
	 * @param plannedValueB 計画値(B)
	 */
	public void setPlannedValueB(Long plannedValueB) {
		this.plannedValueB = plannedValueB;
	}

	/**
	 * 立案品目の納入実績を取得する。
	 * 
	 * @return 立案品目の納入実績
	 */
	public MonNnuSummary getMonNnuSummary() {
		if (monNnuSummary == null) {
			monNnuSummary = new MonNnuSummary();
		}
		return monNnuSummary;
	}

	/**
	 * 立案品目の納入実績を設定する。
	 * 
	 * @param monNnuSummary 立案品目の納入実績
	 */
	public void setMonNnuSummary(MonNnuSummary monNnuSummary) {
		this.monNnuSummary = monNnuSummary;
	}

	@Override
	public int compareTo(MrPlanForVac obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.jgiNo, obj.jgiNo).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && MrPlanForVac.class.isAssignableFrom(entry.getClass())) {
			MrPlanForVac obj = (MrPlanForVac) entry;
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
