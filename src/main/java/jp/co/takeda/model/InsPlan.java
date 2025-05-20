package jp.co.takeda.model;

import jp.co.takeda.bean.DpModel;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設別計画を表すモデルクラス
 * 
 * @author tkawabata
 */
public class InsPlan extends DpModel<InsPlan> {

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
	 * 施設コード（本院）
	 */
	private String insNo;

	/**
	 * 増減理論値（Y）
	 */
	private Long theoreticalIncValueY;

	/**
	 * 増減修正値（Y）
	 */
	private Long plannedIncValueY;

	/**
	 * 理論値（Y）
	 */
	private Long theoreticalValueY;

	/**
	 * 確定値(Y)
	 */
	private Long plannedValueY;

	/**
	 * 施設内部コード（Ref[施設情報].〔施設内部コード〕）
	 */
	private String relnInsNo;

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
	 * 施設コードを取得する。
	 * 
	 * @return 施設コード
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 施設コードを設定する。
	 * 
	 * @param insNo 施設コード
	 */
	public void setInsNo(String insNo) {
		this.insNo = insNo;
	}

	/**
	 * 増減理論値（Y）を取得する。
	 * 
	 * @return 増減理論値（Y）
	 */
	public Long getTheoreticalIncValueY() {
		return theoreticalIncValueY;
	}

	/**
	 * 増減理論値（Y）を設定する。
	 * 
	 * @param theoreticalIncValueY 増減理論値（Y）
	 */
	public void setTheoreticalIncValueY(Long theoreticalIncValueY) {
		this.theoreticalIncValueY = theoreticalIncValueY;
	}

	/**
	 * 増減修正値（Y）を取得する。
	 * 
	 * @return 増減修正値（Y）
	 */
	public Long getPlannedIncValueY() {
		return plannedIncValueY;
	}

	/**
	 * 増減修正値（Y）を設定する。
	 * 
	 * @param plannedIncValueY 増減修正値（Y）
	 */
	public void setPlannedIncValueY(Long plannedIncValueY) {
		this.plannedIncValueY = plannedIncValueY;
	}

	/**
	 * 理論値（Y）を取得する。
	 * 
	 * @return 理論値（Y）
	 */
	public Long getTheoreticalValueY() {
		return theoreticalValueY;
	}

	/**
	 * 理論値（Y）を設定する。
	 * 
	 * @param theoreticalValueY 理論値（Y）
	 */
	public void setTheoreticalValueY(Long theoreticalValueY) {
		this.theoreticalValueY = theoreticalValueY;
	}

	/**
	 * 確定値(Y)を取得する。
	 * 
	 * @return 確定値(Y)
	 */
	public Long getPlannedValueY() {
		return plannedValueY;
	}

	/**
	 * 確定値(Y)を設定する。
	 * 
	 * @param plannedValueY 確定値(Y)
	 */
	public void setPlannedValueY(Long plannedValueY) {
		this.plannedValueY = plannedValueY;
	}

	/**
	 * 施設内部コードを取得する。
	 *
	 * @return 施設内部コード
	 */
	public String getRelnInsNo() {
		return relnInsNo;
	}

	/**
	 * 施設内部コードを設定する。
	 *
	 * @param relnInsNo 施設内部コード
	 */
	public void setRelnInsNo(String relnInsNo) {
		this.relnInsNo = relnInsNo;
	}

	@Override
	public int compareTo(InsPlan obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.insNo, obj.insNo).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && InsPlan.class.isAssignableFrom(entry.getClass())) {
			InsPlan obj = (InsPlan) entry;
			return new EqualsBuilder().append(this.insNo, obj.insNo).append(this.prodCode, obj.prodCode).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.insNo).append(this.prodCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
