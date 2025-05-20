package jp.co.takeda.model;

import jp.co.takeda.bean.DpModel;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * チーム別計画を表すモデルクラス
 * 
 * @author tkawabata
 */
public class TeamPlan extends DpModel<TeamPlan> {

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
	private String sosCd;

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

	/**
	 * 特定施設個別計画値UH(T)
	 */
	private Long specialInsPlanValueUhT;

	/**
	 * 営業所案UH(T)
	 */
	private Long officeValueUhT;

	/**
	 * 計画値UH(T)
	 */
	private Long plannedValueUhT;

	/**
	 * 特定施設個別計画値P(T)
	 */
	private Long specialInsPlanValuePT;

	/**
	 * 営業所案P(T)
	 */
	private Long officeValuePT;

	/**
	 * 計画値P(T)
	 */
	private Long plannedValuePT;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 組織コードを取得する。
	 * 
	 * @return 組織コード
	 */
	public String getSosCd() {
		return sosCd;
	}

	/**
	 * 組織コードを設定する。
	 * 
	 * @param sosCd 組織コード
	 */
	public void setSosCd(String sosCd) {
		this.sosCd = sosCd;
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

	/**
	 * 特定施設個別計画値UH(T)を取得する。
	 * 
	 * @return 特定施設個別計画値UH(T)
	 */
	public Long getSpecialInsPlanValueUhT() {
		return specialInsPlanValueUhT;
	}

	/**
	 * 特定施設個別計画値UH(T)を設定する。
	 * 
	 * @param specialInsPlanValueUhT 特定施設個別計画値UH(T)
	 */
	public void setSpecialInsPlanValueUhT(Long specialInsPlanValueUhT) {
		this.specialInsPlanValueUhT = specialInsPlanValueUhT;
	}

	/**
	 * 営業所案UH(T)を取得する。
	 * 
	 * @return 営業所案UH(T)
	 */
	public Long getOfficeValueUhT() {
		return officeValueUhT;
	}

	/**
	 * 営業所案UH(T)を設定する。
	 * 
	 * @param officeValueUhT 営業所案UH(T)
	 */
	public void setOfficeValueUhT(Long officeValueUhT) {
		this.officeValueUhT = officeValueUhT;
	}

	/**
	 * 計画値UH(T)を取得する。
	 * 
	 * @return 計画値UH(T)
	 */
	public Long getPlannedValueUhT() {
		return plannedValueUhT;
	}

	/**
	 * 計画値UH(T)を設定する。
	 * 
	 * @param plannedValueUhT 計画値UH(T)
	 */
	public void setPlannedValueUhT(Long plannedValueUhT) {
		this.plannedValueUhT = plannedValueUhT;
	}

	/**
	 * 特定施設個別計画値P(T)を取得する。
	 * 
	 * @return 特定施設個別計画値P(T)
	 */
	public Long getSpecialInsPlanValuePT() {
		return specialInsPlanValuePT;
	}

	/**
	 * 特定施設個別計画値P(T)を設定する。
	 * 
	 * @param specialInsPlanValuePT 特定施設個別計画値P(T)
	 */
	public void setSpecialInsPlanValuePT(Long specialInsPlanValuePT) {
		this.specialInsPlanValuePT = specialInsPlanValuePT;
	}

	/**
	 * 営業所案P(T)を取得する。
	 * 
	 * @return 営業所案P(T)
	 */
	public Long getOfficeValuePT() {
		return officeValuePT;
	}

	/**
	 * 営業所案P(T)を設定する。
	 * 
	 * @param officeValuePT 営業所案P(T)
	 */
	public void setOfficeValuePT(Long officeValuePT) {
		this.officeValuePT = officeValuePT;
	}

	/**
	 * 計画値P(T)を取得する。
	 * 
	 * @return 計画値P(T)
	 */
	public Long getPlannedValuePT() {
		return plannedValuePT;
	}

	/**
	 * 計画値P(T)を設定する。
	 * 
	 * @param plannedValuePT 計画値P(T)
	 */
	public void setPlannedValuePT(Long plannedValuePT) {
		this.plannedValuePT = plannedValuePT;
	}

	@Override
	public int compareTo(TeamPlan obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.sosCd, obj.sosCd).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && TeamPlan.class.isAssignableFrom(entry.getClass())) {
			TeamPlan obj = (TeamPlan) entry;
			return new EqualsBuilder().append(this.sosCd, obj.sosCd).append(this.prodCode, obj.prodCode).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.sosCd).append(this.prodCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
