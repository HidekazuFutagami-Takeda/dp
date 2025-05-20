package jp.co.takeda.model;

import jp.co.takeda.bean.DpModel;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (ワクチン)配分基準(本部)を表すモデルクラス
 * 
 * @author tkawabata
 */
public class DistParamHonbuForVac extends DpModel<DistParamHonbuForVac> {

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
	 * 配分パラメータ
	 */
	private DistParam distParam;

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
	 * 配分パラメータを取得する。
	 * 
	 * @return 配分パラメータ
	 */
	public DistParam getDistParam() {
		return distParam;
	}

	/**
	 * 配分パラメータを設定する。
	 * 
	 * @param distParam 配分パラメータ
	 */
	public void setDistParam(DistParam distParam) {
		this.distParam = distParam;
	}

	@Override
	public int compareTo(DistParamHonbuForVac obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.prodCode, obj.prodCode).append(this.distParam.getDistributionType(), obj.distParam.getDistributionType()).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && DistParamHonbuForVac.class.isAssignableFrom(entry.getClass())) {
			DistParamHonbuForVac obj = (DistParamHonbuForVac) entry;
			return new EqualsBuilder().append(this.prodCode, obj.prodCode).append(this.distParam.getDistributionType(), obj.distParam.getDistributionType()).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.prodCode).append(this.distParam.getDistributionType()).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
