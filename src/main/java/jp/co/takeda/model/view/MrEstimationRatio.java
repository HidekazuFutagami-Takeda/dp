package jp.co.takeda.model.view;

import java.util.List;

import jp.co.takeda.a.bean.Model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 担当者別試算構成比を表すモデルクラス
 * 
 * @author khashimoto
 */
public class MrEstimationRatio extends Model<MrEstimationRatio> {

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
	 * 試算構成比のリスト
	 */
	private List<EstimationRatio> estimationRatioList;

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
	 * 試算構成比のリストを取得する。
	 * 
	 * @return 試算構成比のリスト
	 */
	public List<EstimationRatio> getEstimationRatioList() {
		return estimationRatioList;
	}

	/**
	 * 試算構成比のリストを設定する。
	 * 
	 * @param estimationRatioList 試算構成比のリスト
	 */
	public void setEstimationRatioList(List<EstimationRatio> estimationRatioList) {
		this.estimationRatioList = estimationRatioList;
	}

	@Override
	public int compareTo(MrEstimationRatio obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.jgiNo, obj.jgiNo).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && MrEstimationRatio.class.isAssignableFrom(entry.getClass())) {
			MrEstimationRatio obj = (MrEstimationRatio) entry;
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
