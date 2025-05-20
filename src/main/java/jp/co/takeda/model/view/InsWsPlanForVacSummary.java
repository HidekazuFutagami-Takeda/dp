package jp.co.takeda.model.view;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.Prefecture;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (ワクチン)施設特約店別計画の市区町村別サマリーを表すモデルクラス
 * 
 * @author khashimoto
 */
public class InsWsPlanForVacSummary extends DpModel<InsWsPlanForVacSummary> {

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
	 * JIS府県コード
	 */
	private Prefecture addrCodePref;

	/**
	 * JIS市区町村コード
	 */
	private String addrCodeCity;

	/**
	 * 活動区分
	 */
	private ActivityType activityType;

	/**
	 * 市区郡町村名（漢字）
	 */
	private String shikuchosonMeiKj;

	/**
	 * 配分値(B)
	 */
	private Long distValueB;

	/**
	 * 計画値(B)
	 */
	private Long plannedValueB;

	/**
	 * 前期実績
	 */
	private Long advancePeriod;

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
	 * JIS府県コードを取得する。
	 * 
	 * @return addrCodePref JIS府県コード
	 */
	public Prefecture getAddrCodePref() {
		return addrCodePref;
	}

	/**
	 * JIS府県コードを設定する。
	 * 
	 * @param addrCodePref JIS府県コード
	 */
	public void setAddrCodePref(Prefecture addrCodePref) {
		this.addrCodePref = addrCodePref;
	}

	/**
	 * JIS市区町村コードを取得する。
	 * 
	 * @return JIS市区町村コード
	 */
	public String getAddrCodeCity() {
		return addrCodeCity;
	}

	/**
	 * JIS市区町村コードを設定する。
	 * 
	 * @param addrCodeCity JIS市区町村コード
	 */
	public void setAddrCodeCity(String addrCodeCity) {
		this.addrCodeCity = addrCodeCity;
	}

	/**
	 * 市区郡町村名（漢字）を取得する。
	 * 
	 * @return 市区郡町村名（漢字）
	 */
	public String getShikuchosonMeiKj() {
		return shikuchosonMeiKj;
	}

	/**
	 * 市区郡町村名（漢字）を設定する。
	 * 
	 * @param shikuchosonMeiKj 市区郡町村名（漢字）
	 */
	public void setShikuchosonMeiKj(String shikuchosonMeiKj) {
		this.shikuchosonMeiKj = shikuchosonMeiKj;
	}

	/**
	 * 活動区分を取得する。
	 * 
	 * @return 活動区分
	 */
	public ActivityType getActivityType() {
		return activityType;
	}

	/**
	 * 活動区分を設定する。
	 * 
	 * @param activityType 活動区分
	 */
	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
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
	 * 配分値(B)を設定する。
	 * 
	 * @param distValueB 配分値(B)
	 */
	public void setDistValueB(Long distValueB) {
		this.distValueB = distValueB;
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

	@Override
	public int compareTo(InsWsPlanForVacSummary obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.jgiNo, obj.jgiNo).append(this.prodCode, obj.prodCode).append(this.addrCodePref, obj.addrCodePref).append(this.addrCodeCity,
				obj.addrCodeCity).append(this.activityType, obj.activityType).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && InsWsPlanForVacSummary.class.isAssignableFrom(entry.getClass())) {
			InsWsPlanForVacSummary obj = (InsWsPlanForVacSummary) entry;
			return new EqualsBuilder().append(this.jgiNo, obj.jgiNo).append(this.prodCode, obj.prodCode).append(this.addrCodePref, obj.addrCodePref).append(this.addrCodeCity,
				obj.addrCodeCity).append(this.activityType, obj.activityType).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.jgiNo).append(this.prodCode).append(this.addrCodePref).append(this.activityType).append(this.addrCodeCity).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
