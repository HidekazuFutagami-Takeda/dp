package jp.co.takeda.model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.a.bean.Model;
import jp.co.takeda.model.div.JokenSet;

/**
 * [外部直接参照]従業員別条件を表すモデルクラス
 * 
 * @author tkawabata
 */
public class JgiJoken extends Model<JgiJoken> {

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
	 * 条件セット
	 */
	private JokenSet jokenSet;

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
	 * 条件セットを取得する。
	 * 
	 * @return 条件セット
	 */
	public JokenSet getJokenSet() {
		return jokenSet;
	}

	/**
	 * 条件セットを設定する。
	 * 
	 * @param jokenSet 条件セット
	 */
	public void setJokenSet(JokenSet jokenSet) {
		this.jokenSet = jokenSet;
	}

	@Override
	public int compareTo(JgiJoken obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.jgiNo, obj.jgiNo).append(this.jokenSet, obj.jokenSet).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && JgiJoken.class.isAssignableFrom(entry.getClass())) {
			JgiJoken obj = (JgiJoken) entry;
			return new EqualsBuilder().append(this.jgiNo, obj.jgiNo).append(this.jokenSet, obj.jokenSet).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
