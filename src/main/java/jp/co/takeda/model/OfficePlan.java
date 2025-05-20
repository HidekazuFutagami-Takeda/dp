package jp.co.takeda.model;

import jp.co.takeda.bean.DpModel;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 営業所計画を表すモデルクラス
 * 
 * @author tkawabata
 */
public class OfficePlan extends DpModel<OfficePlan> {

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
	 * 計画値UH(Y)
	 */
	private Long plannedValueUhY;

	/**
	 * 計画値P(Y)
	 */
	private Long plannedValuePY;

	/**
	 * 品目共通情報
	 */
	private ProdInfo prodInfo;

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
	 * 品目共通情報を取得する。
	 * 
	 * @return 品目共通情報
	 */
	public ProdInfo getProdInfo() {
		return prodInfo;
	}

	/**
	 * 品目共通情報を設定する。
	 * 
	 * @param prodInfo 品目共通情報
	 */
	public void setProdInfo(ProdInfo prodInfo) {
		this.prodInfo = prodInfo;
	}

	@Override
	public int compareTo(OfficePlan obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.sosCd, obj.sosCd).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && OfficePlan.class.isAssignableFrom(entry.getClass())) {
			OfficePlan obj = (OfficePlan) entry;
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
