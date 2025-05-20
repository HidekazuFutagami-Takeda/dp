package jp.co.takeda.model.view;

import java.util.Date;

import jp.co.takeda.a.bean.Model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (ワ)組織単位の各種登録状況を表すモデルクラス
 * 
 * @author khashimoto
 */
public class SosEachKindInfoForVac extends Model<SosEachKindInfoForVac> {

	//---------------------
	// Data Field
	// --------------------

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 担当者別計画の最終更新日時
	 */
	private Date mrPlanUpDate;

	/**
	 * 特定施設個別計画の登録件数
	 */
	private Integer specialInsPlanCnt;

	/**
	 * 特定施設個別計画の最終更新日時
	 */
	private Date specialInsPlanUpDate;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 担当者別計画の最終更新日時を取得する。
	 * 
	 * @return 担当者別計画の最終更新日時
	 */
	public Date getMrPlanUpDate() {
		return mrPlanUpDate;
	}

	/**
	 * 担当者別計画の最終更新日時を設定する。
	 * 
	 * @param mrPlanUpDate 担当者別計画の最終更新日時
	 */
	public void setMrPlanUpDate(Date mrPlanUpDate) {
		this.mrPlanUpDate = mrPlanUpDate;
	}

	/**
	 * 特定施設個別計画の登録件数を取得する。
	 * 
	 * @return 特定施設個別計画の登録件数
	 */
	public Integer getSpecialInsPlanCnt() {
		return specialInsPlanCnt;
	}

	/**
	 * 特定施設個別計画の登録件数を設定する。
	 * 
	 * @param specialInsPlanCnt 特定施設個別計画の登録件数
	 */
	public void setSpecialInsPlanCnt(Integer specialInsPlanCnt) {
		this.specialInsPlanCnt = specialInsPlanCnt;
	}

	/**
	 * 特定施設個別計画の最終更新日時を取得する。
	 * 
	 * @return 特定施設個別計画の最終更新日時
	 */
	public Date getSpecialInsPlanUpDate() {
		return specialInsPlanUpDate;
	}

	/**
	 * 特定施設個別計画の最終更新日時を設定する。
	 * 
	 * @param specialInsPlanUpDate 特定施設個別計画の最終更新日時
	 */
	public void setSpecialInsPlanUpDate(Date specialInsPlanUpDate) {
		this.specialInsPlanUpDate = specialInsPlanUpDate;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

	@Override
	public int compareTo(SosEachKindInfoForVac obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this, obj).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && SosEachKindInfoForVac.class.isAssignableFrom(entry.getClass())) {
			SosEachKindInfoForVac obj = (SosEachKindInfoForVac) entry;
			return new EqualsBuilder().append(this, obj).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this).toHashCode();
	}
}
