package jp.co.takeda.model;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.div.Term;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 薬価改定指定率を表すモデルクラス
 * 
 * @author tkawabata
 */
public class ChangeParamYakka extends DpModel<ChangeParamYakka> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 年度
	 */
	private String calYear;

	/**
	 * 期
	 */
	private Term calTerm;

	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 変換率(UH)
	 */
	private Double changeRateUh;

	/**
	 * 変換率(P)
	 */
	private Double changeRateP;

	//---------------------
	// Getter & Setter
	// --------------------
	/**
	 * 年度を取得する。
	 * 
	 * @return 年度
	 */
	public String getCalYear() {
		return calYear;
	}

	/**
	 * 年度を設定する。
	 * 
	 * @param calYear 年度
	 */
	public void setCalYear(String calYear) {
		this.calYear = calYear;
	}

	/**
	 * 期を取得する。
	 * 
	 * @return 期
	 */
	public Term getCalTerm() {
		return calTerm;
	}

	/**
	 * 期を設定する。
	 * 
	 * @param calTerm 期
	 */
	public void setCalTerm(Term calTerm) {
		this.calTerm = calTerm;
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
	 * 変換率(UH)を取得する。
	 * 
	 * @return 変換率(UH)
	 */
	public Double getChangeRateUh() {
		return changeRateUh;
	}

	/**
	 * 変換率(UH)を設定する。
	 * 
	 * @param changeRateUh 変換率(UH)
	 */
	public void setChangeRateUh(Double changeRateUh) {
		this.changeRateUh = changeRateUh;
	}

	/**
	 * 変換率(P)を取得する。
	 * 
	 * @return 変換率(P)
	 */
	public Double getChangeRateP() {
		return changeRateP;
	}

	/**
	 * 変換率(P)を設定する。
	 * 
	 * @param changeRateP 変換率(P)
	 */
	public void setChangeRateP(Double changeRateP) {
		this.changeRateP = changeRateP;
	}

	@Override
	public int compareTo(ChangeParamYakka obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.calYear, obj.calYear).append(this.calTerm, obj.calTerm).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && ChangeParamYakka.class.isAssignableFrom(entry.getClass())) {
			ChangeParamYakka obj = (ChangeParamYakka) entry;
			return new EqualsBuilder().append(this.calYear, obj.calYear).append(this.calTerm, obj.calTerm).append(this.prodCode, obj.prodCode).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.calYear).append(this.calTerm).append(this.prodCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
