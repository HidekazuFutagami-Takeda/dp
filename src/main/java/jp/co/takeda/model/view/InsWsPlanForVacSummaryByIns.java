package jp.co.takeda.model.view;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.div.ActivityType;

/**
 * (ワクチン)施設特約店別計画の施設別サマリーを表すモデルクラス
 * 
 * @author khashimoto
 */
public class InsWsPlanForVacSummaryByIns extends DpModel<InsWsPlanForVacSummaryByIns> {

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
	 * 施設コード
	 */
	private String insNo;

	/**
	 * 施設略式漢字名
	 */
	private String insAbbrName;

	/**
	 * 施設正式漢字名
	 */
	private String insFormalName;

	// add Start 2022/12/1  Y.Taniguchi バックログNo.31
	/**
	 * 対象区分
	 */
	private String hoInsType;

	// add End 2022/12/1  Y.Taniguchi バックログNo.31

	/**
	 * 活動区分
	 */
	private ActivityType activityType;

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
	 * 施設略式漢字名を取得する。
	 * 
	 * @return 施設略式漢字名
	 */
	public String getInsAbbrName() {
		return insAbbrName;
	}

	/**
	 * 施設略式漢字名を設定する。
	 * 
	 * @param insAbbrName 施設略式漢字名
	 */
	public void setInsAbbrName(String insAbbrName) {
		this.insAbbrName = insAbbrName;
	}

	/**
	 * 施設正式漢字名を取得する。
	 * 
	 * @return 施設正式漢字名
	 */
	public String getInsFormalName() {
		return insFormalName;
	}

	/**
	 * 施設正式漢字名を設定する。
	 * 
	 * @param insFormalName 施設正式漢字名
	 */
	public void setInsFormalName(String insFormalName) {
		this.insFormalName = insFormalName;
	}

	// add Start 2022/12/1  Y.Taniguchi バックログNo.31
	/**
	 * 対象区分を取得する。
	 *
	 * @return 対象区分
	 */
	public String getHoInsType() {
		return hoInsType;
	}

	/**
	 * 対象区分を設定する。
	 *
	 * @param insAbbrName 対象区分
	 */
	public void setHoInsType(String hoInsType) {
		this.hoInsType = hoInsType;
	}
	// End Start 2022/12/1  Y.Taniguchi バックログNo.31

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
	public int compareTo(InsWsPlanForVacSummaryByIns obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.jgiNo, obj.jgiNo).append(this.prodCode, obj.prodCode).append(this.insNo, obj.insNo).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && InsWsPlanForVacSummaryByIns.class.isAssignableFrom(entry.getClass())) {
			InsWsPlanForVacSummaryByIns obj = (InsWsPlanForVacSummaryByIns) entry;
			return new EqualsBuilder().append(this.jgiNo, obj.jgiNo).append(this.prodCode, obj.prodCode).append(this.insNo, obj.insNo).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.jgiNo).append(this.prodCode).append(this.insNo).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
