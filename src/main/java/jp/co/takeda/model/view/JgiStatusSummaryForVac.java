package jp.co.takeda.model.view;

import jp.co.takeda.a.bean.Model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (ワクチン)担当者単位のステータスのサマリーを表すモデルクラス
 * 
 * @author khashimoto
 */
public class JgiStatusSummaryForVac extends Model<JgiStatusSummaryForVac> {

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
	 * 氏名
	 */
	private String jgiName;

	/**
	 * 施設特約店別計画ステータスのサマリー
	 */
	private InsWsPlanStatSum insWsPlanStatSum;

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
	 * 氏名を取得する。
	 * 
	 * @return 氏名
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * 氏名を設定する。
	 * 
	 * @param jgiName 氏名
	 */
	public void setJgiName(String jgiName) {
		this.jgiName = jgiName;
	}

	/**
	 * 施設特約店別計画ステータスのサマリーを取得する。
	 * 
	 * @return 施設特約店別計画ステータスのサマリー
	 */
	public InsWsPlanStatSum getInsWsPlanStatSum() {
		return insWsPlanStatSum;
	}

	/**
	 * 施設特約店別計画ステータスのサマリーを設定する。
	 * 
	 * @param insWsPlanStatSum 施設特約店別計画ステータスのサマリー
	 */
	public void setInsWsPlanStatSum(InsWsPlanStatSum insWsPlanStatSum) {
		this.insWsPlanStatSum = insWsPlanStatSum;
	}

	@Override
	public int compareTo(JgiStatusSummaryForVac obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.jgiNo, obj.jgiNo).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && JgiStatusSummaryForVac.class.isAssignableFrom(entry.getClass())) {
			JgiStatusSummaryForVac obj = (JgiStatusSummaryForVac) entry;
			return new EqualsBuilder().append(this.jgiNo, obj.jgiNo).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.jgiNo).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
