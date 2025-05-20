package jp.co.takeda.model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.div.CalcType;
import jp.co.takeda.model.div.StatusForOffice;

/**
 * 営業所計画ステータスを表すモデルクラス
 *
 * @author tkawabata
 */
public class OfficePlanStatus extends DpModel<OfficePlanStatus> {

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
	 * 品目カテゴリ
	 */
	private String prodCategory;

	/**
	 * ステータス
	 */
	private StatusForOffice statusForOffice;

	/**
	 * 試算タイプ
	 */
	private CalcType calcType;

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
	 * 品目カテゴリを取得する。
	 *
	 * @return 品目カテゴリ
	 */
	public String getProdCategory() {
		return prodCategory;
	}

	/**
	 * 品目カテゴリを設定する。
	 *
	 * @param prodCategory 品目カテゴリ
	 */
	public void setProdCategory(String prodCategory) {
		this.prodCategory = prodCategory;
	}

	/**
	 * ステータスを取得する。
	 *
	 * @return ステータス
	 */
	public StatusForOffice getStatusForOffice() {
		return statusForOffice;
	}

	/**
	 * ステータスを設定する。
	 *
	 * @param statusForOffice ステータス
	 */
	public void setStatusForOffice(StatusForOffice statusForOffice) {
		this.statusForOffice = statusForOffice;
	}

	/**
	 * 試算タイプを取得する。
	 *
	 * @return 試算タイプ
	 */
	public CalcType getCalcType() {
		return calcType;
	}

	/**
	 * 試算タイプを設定する。
	 *
	 * @param calcType 試算タイプ
	 */
	public void setCalcType(CalcType calcType) {
		this.calcType = calcType;
	}

	@Override
	public int compareTo(OfficePlanStatus obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.sosCd, obj.sosCd).append(this.prodCategory, obj.prodCategory).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && OfficePlanStatus.class.isAssignableFrom(entry.getClass())) {
			OfficePlanStatus obj = (OfficePlanStatus) entry;
			return new EqualsBuilder().append(this.sosCd, obj.sosCd).append(this.prodCategory, obj.prodCategory).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.sosCd).append(this.prodCategory).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
