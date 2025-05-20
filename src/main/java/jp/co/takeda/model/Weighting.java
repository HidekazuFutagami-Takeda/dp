package jp.co.takeda.model;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.div.MarketClass;
import jp.co.takeda.model.div.MarketType;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 配分重み付けを表すモデルクラス
 * 
 * @author khashimoto
 */
public class Weighting extends DpModel<Weighting> {

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
	 * 市場分類
	 */
	private MarketClass marketClass;

	/**
	 * 市場区分
	 */
	private MarketType marketType;

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
	 * 市場分類を取得する。
	 * 
	 * @return 市場分類
	 */
	public MarketClass getMarketClass() {
		return marketClass;
	}

	/**
	 * 市場分類を設定する。
	 * 
	 * @param marketClass 市場分類
	 */
	public void setMarketClass(MarketClass marketClass) {
		this.marketClass = marketClass;
	}

	/**
	 * 市場区分を取得する。
	 * 
	 * @return 市場区分
	 */
	public MarketType getMarketType() {
		return marketType;
	}

	/**
	 * 市場区分を設定する。
	 * 
	 * @param marketType 市場区分
	 */
	public void setMarketType(MarketType marketType) {
		this.marketType = marketType;
	}

	@Override
	public int compareTo(Weighting obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.prodCode, obj.prodCode).append(this.marketClass, obj.marketClass).append(this.marketType, obj.marketType).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && Weighting.class.isAssignableFrom(entry.getClass())) {
			Weighting obj = (Weighting) entry;
			return new EqualsBuilder().append(this.prodCode, obj.prodCode).append(this.marketClass, obj.marketClass).append(this.marketType, obj.marketType).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.prodCode).append(this.marketClass).append(this.marketType).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
