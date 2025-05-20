package jp.co.takeda.model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpModel;

/**
 * 営業所計画アップロード用のフォーマットファイルのサマリー情報を表すモデルクラス
 *
 * @author khashimoto
 */
public class DistPlanningListDLSummary extends DpModel<DistPlanningListDLSummary> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 支店・営業所コード
	 */
	private String brDistCd;

	/**
	 * 支店・営業所名
	 */
	private String brDistNm;

	/**
	 * 統計品名コード
	 */
	private String statProdCd;

	/**
	 * 品目名
	 */
	private String prodNm;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 支店・営業所コードを取得する。
	 *
	 * @return 支店・営業所コード
	 */
	public String getBrDistCd() {
		return brDistCd;
	}

	/**
	 * 支店・営業所コードを設定する。
	 *
	 * @param brDistCd 支店・営業所コード
	 */
	public void setBrDistCd(String brDistCd) {
		this.brDistCd = brDistCd;
	}

	/**
	 * 支店・営業所名を取得する。
	 *
	 * @return 支店・営業所名
	 */
	public String getBrDistNm() {
		return brDistNm;
	}

	/**
	 * 支店・営業所名を設定する。
	 *
	 * @param brDistNm 支店・営業所名
	 */
	public void setBrDistNm(String brDistNm) {
		this.brDistNm = brDistNm;
	}

	/**
	 * 統計品名コードを取得する。
	 *
	 * @return 統計品名コード
	 */
	public String getStatProdCd() {
		return statProdCd;
	}

	/**
	 * 統計品名コードを設定する。
	 *
	 * @param statProdCd 統計品名コード
	 */
	public void setStatProdCd(String statProdCd) {
		this.statProdCd = statProdCd;
	}

	/**
	 * 品目名を取得する。
	 *
	 * @return 品目名
	 */
	public String getProdNm() {
		return prodNm;
	}

	/**
	 * 品目名を設定する。
	 *
	 * @param prodNm 品目名
	 */
	public void setProdNm(String prodNm) {
		this.prodNm = prodNm;
	}

	@Override
	public int compareTo(DistPlanningListDLSummary obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.brDistCd, obj.brDistCd).append(this.statProdCd, obj.statProdCd).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && DistPlanningListDLSummary.class.isAssignableFrom(entry.getClass())) {
			DistPlanningListDLSummary obj = (DistPlanningListDLSummary) entry;
			return new EqualsBuilder().append(this.brDistCd, obj.brDistCd).append(this.statProdCd, obj.statProdCd).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.brDistCd).append(this.statProdCd).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
