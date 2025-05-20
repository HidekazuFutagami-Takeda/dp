package jp.co.takeda.model;

import jp.co.takeda.bean.DpModel;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 試算パラメータ（本部）を表すモデルクラス
 * 
 * @author tkawabata
 */
public class EstParamHonbu extends DpModel<EstParamHonbu> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 試算配分パラメータ
	 */
	private BaseParam baseParam;

	/**
	 * 試算パラメータ
	 */
	private EstimationParam estimationParam;

	//---------------------
	// Getter & Setter
	// --------------------

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
	 * 試算配分パラメータを取得する。
	 * 
	 * @return 試算配分パラメータ
	 */
	public BaseParam getBaseParam() {
		return baseParam;
	}

	/**
	 * 試算配分パラメータを設定する。
	 * 
	 * @param baseParam 試算配分パラメータ
	 */
	public void setBaseParam(BaseParam baseParam) {
		this.baseParam = baseParam;
	}

	/**
	 * 試算パラメータを取得する。
	 * 
	 * @return 試算パラメータ
	 */
	public EstimationParam getEstimationParam() {
		return estimationParam;
	}

	/**
	 * 試算パラメータを設定する。
	 * 
	 * @param estimationBase 試算パラメータ
	 */
	public void setEstimationParam(EstimationParam estimationParam) {
		this.estimationParam = estimationParam;
	}

	@Override
	public int compareTo(EstParamHonbu obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && EstParamHonbu.class.isAssignableFrom(entry.getClass())) {
			EstParamHonbu obj = (EstParamHonbu) entry;
			return new EqualsBuilder().append(this.prodCode, obj.prodCode).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.prodCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
