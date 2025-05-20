package jp.co.takeda.model;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.div.ActivityType;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 活動区分情報を表すモデルクラス
 * 
 * @author khashimoto
 */
public class ActivityTypeForVac extends DpModel<ActivityTypeForVac> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 施設コード
	 */
	private String insNo;

	/**
	 * 活動区分
	 */
	private ActivityType activityType;

	//---------------------
	// Getter & Setter
	// --------------------

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
	public void setIActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	@Override
	public int compareTo(ActivityTypeForVac obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.insNo, obj.insNo).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && ActivityTypeForVac.class.isAssignableFrom(entry.getClass())) {
			ActivityTypeForVac obj = (ActivityTypeForVac) entry;
			return new EqualsBuilder().append(this.insNo, obj.insNo).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.insNo).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
