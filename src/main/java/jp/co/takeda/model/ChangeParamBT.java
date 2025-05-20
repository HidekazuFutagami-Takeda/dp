package jp.co.takeda.model;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.div.Term;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * T/B変換指定率を表すモデルクラス
 * 
 * @author tkawabata
 */
public class ChangeParamBT extends DpModel<ChangeParamBT> {

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
	 * TMS 特約店コード 本店
	 */
	private String tmsTytenCdHonten;

	/**
	 * T/B変換率
	 */
	private Double changeRate;

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
	 * TMS 特約店コード 本店を取得する。
	 * 
	 * @return TMS 特約店コード 本店
	 */
	public String getTmsTytenCdHonten() {
		return tmsTytenCdHonten;
	}

	/**
	 * TMS 特約店コード 本店を設定する。
	 * 
	 * @param tmsTytenCdHonten TMS 特約店コード 本店
	 */
	public void setTmsTytenCdHonten(String tmsTytenCdHonten) {
		this.tmsTytenCdHonten = tmsTytenCdHonten;
	}

	/**
	 * T/B変換率を取得する。
	 * 
	 * @return T/B変換率
	 */
	public Double getChangeRate() {
		return changeRate;
	}

	/**
	 * T/B変換率を設定する。
	 * 
	 * @param changeRate T/B変換率
	 */
	public void setChangeRate(Double changeRate) {
		this.changeRate = changeRate;
	}

	@Override
	public int compareTo(ChangeParamBT obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.calYear, obj.calYear).append(this.calTerm, obj.calTerm).append(this.prodCode, obj.prodCode).append(this.tmsTytenCdHonten,
				obj.tmsTytenCdHonten).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && ChangeParamBT.class.isAssignableFrom(entry.getClass())) {
			ChangeParamBT obj = (ChangeParamBT) entry;
			return new EqualsBuilder().append(this.calYear, obj.calYear).append(this.calTerm, obj.calTerm).append(this.prodCode, obj.prodCode).append(this.tmsTytenCdHonten,
				obj.tmsTytenCdHonten).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.calYear).append(this.calTerm).append(this.prodCode).append(this.tmsTytenCdHonten).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
