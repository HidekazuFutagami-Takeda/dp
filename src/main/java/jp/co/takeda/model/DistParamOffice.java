package jp.co.takeda.model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.div.InsType;

/**
 * 配分基準（営業所）を表すモデルクラス
 * 
 * @author tkawabata
 */
public class DistParamOffice extends DpModel<DistParamOffice> {

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
	 * 施設出力対象区分
	 */
	private InsType insType;

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
	 * 施設出力対象区分を取得する。
	 * 
	 * @return 施設出力対象区分
	 */
	public InsType getInsType() {
		return insType;
	}

	/**
	 * 施設出力対象区分を設定する。
	 * 
	 * @param insType 施設出力対象区分
	 */
	public void setInsType(InsType insType) {
		this.insType = insType;
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
	public int compareTo(DistParamOffice obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.sosCd, obj.sosCd).append(this.prodCode, obj.prodCode).append(this.insType, obj.insType).append(
				this.distParam.getDistributionType(), obj.distParam.getDistributionType()).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && DistParamOffice.class.isAssignableFrom(entry.getClass())) {
			DistParamOffice obj = (DistParamOffice) entry;
			return new EqualsBuilder().append(this.sosCd, obj.sosCd).append(this.prodCode, obj.prodCode).append(this.insType, obj.insType).append(
				this.distParam.getDistributionType(), obj.distParam.getDistributionType()).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.sosCd).append(this.prodCode).append(this.insType).append(this.distParam.getDistributionType()).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
