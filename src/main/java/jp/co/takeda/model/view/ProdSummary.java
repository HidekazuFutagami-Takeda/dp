package jp.co.takeda.model.view;

import jp.co.takeda.bean.DpModel;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 簡易版品目情報を表すクラス
 * 
 * @author tkawabata
 */
public class ProdSummary extends DpModel<ProdSummary> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 統計品名コード(品目)
	 */
	private String statProdCode;

	/**
	 * 品目名称
	 */
	private String prodName;

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
	 * 統計品名コード(品目)を取得する。
	 * 
	 * @return 統計品名コード(品目)
	 */
	public String getStatProdCode() {
		return statProdCode;
	}

	/**
	 * 統計品名コード(品目)を設定する。
	 * 
	 * @param statProdCode 統計品名コード(品目)
	 */
	public void setStatProdCode(String statProdCode) {
		this.statProdCode = statProdCode;
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

	@Override
	public int compareTo(ProdSummary obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && ProdSummary.class.isAssignableFrom(entry.getClass())) {
			ProdSummary obj = (ProdSummary) entry;
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
