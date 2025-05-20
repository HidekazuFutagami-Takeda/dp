package jp.co.takeda.model;

import jp.co.takeda.bean.DpManageModel;
import jp.co.takeda.model.div.InsType;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 管理時の組織別計画を表すモデルクラス
 * 
 * @author khashimoto
 */
public class ManageSosPlan extends DpManageModel<ManageSosPlan> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 施設出力対象区分
	 */
	protected InsType insType;

	/**
	 * 品目固定コード
	 */
	protected String prodCode;

	/**
	 * 組織コード
	 */
	protected String sosCd;

	/**
	 * 実施計画
	 */
	protected ImplPlan implPlan;

	/**
	 * 下位計画積上げ値
	 */
	protected Long stackedValue;

	/**
	 * 品目名称（Ref[管理時の計画対象品目].〔品目名称〕）
	 */
	protected String prodName;

	/**
	 * 部門名正式（Ref[組織情報].〔部門名正式〕）
	 */
	protected String bumonSeiName;

	//---------------------
	// Getter & Setter
	// --------------------
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
	 * 実施計画を取得する。
	 * 
	 * @return 実施計画
	 */
	public ImplPlan getImplPlan() {
		if (implPlan == null) {
			implPlan = new ImplPlan();
		}
		return implPlan;
	}

	/**
	 * 実施計画を設定する。
	 * 
	 * @param implPlan 実施計画
	 */
	public void setImplPlan(ImplPlan implPlan) {
		this.implPlan = implPlan;
	}

	/**
	 * 下位計画積上げ値を取得する。
	 * 
	 * @return 下位計画積上げ値
	 */
	public Long getStackedValue() {
		return stackedValue;
	}

	/**
	 * 下位計画積上げ値を設定する。
	 * 
	 * @param stackedValue 下位計画積上げ値
	 */
	public void setStackedValue(Long stackedValue) {
		this.stackedValue = stackedValue;
	}

	/**
	 * 品目名称(漢字)を取得する。
	 * 
	 * @return 品目名称(漢字)
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 品目名称(漢字)を設定する。
	 * 
	 * @param prodName 品目名称(漢字)
	 */
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	/**
	 * 部門名正式を取得する。
	 * 
	 * @return 部門名正式
	 */
	public String getBumonSeiName() {
		return bumonSeiName;
	}

	/**
	 * 部門名正式を設定する。
	 * 
	 * @param bumonSeiName 部門名正式
	 */
	public void setBumonSeiName(String bumonSeiName) {
		this.bumonSeiName = bumonSeiName;
	}

	@Override
	public int compareTo(ManageSosPlan obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.insType, obj.insType).append(this.sosCd, obj.sosCd).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && ManageSosPlan.class.isAssignableFrom(entry.getClass())) {
			ManageSosPlan obj = (ManageSosPlan) entry;
			return new EqualsBuilder().append(this.insType, obj.insType).append(this.sosCd, obj.sosCd).append(this.prodCode, obj.prodCode).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.insType).append(this.sosCd).append(this.prodCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
