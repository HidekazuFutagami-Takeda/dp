package jp.co.takeda.model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.a.bean.Model;

/**
 * 計画支援汎用マスタを表すモデルクラス
 * @author
 *
 */
public final class DpsCCdMst extends Model<DpsCCdMst> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * データ区分
	 */
	private String dataKbn;

	/**
	 * コード
	 */
	private String dataCd;

	/**
	 * 名称
	 */
	private String dataName;

	/**
	 * 値
	 */
	private Long dataValue;

	/**
	 * 表示順
	 */
	private Long dataSeq;

	/**
	 * デフォルトフラグ
	 */
	private Long defFlg;

	/**
	 * @return dataKbn
	 */
	public String getDataKbn() {
		return dataKbn;
	}

	/**
	 * @return dataCd
	 */
	public String getDataCd() {
		return dataCd;
	}

	/**
	 * @return dataName
	 */
	public String getDataName() {
		return dataName;
	}

	/**
	 * @return dataValue
	 */
	public Long getDataValue() {
		return dataValue;
	}

	/**
	 * @return dataSeq
	 */
	public Long getDataSeq() {
		return dataSeq;
	}

	/**
	 * @return defFlg
	 */
	public Long getDefFlg() {
		return defFlg;
	}

	/**
	 * @param dataKbn セットする dataKbn
	 */
	public void setDataKbn(String dataKbn) {
		this.dataKbn = dataKbn;
	}

	/**
	 * @param dataCd セットする dataCd
	 */
	public void setDataCd(String dataCd) {
		this.dataCd = dataCd;
	}

	/**
	 * @param dataName セットする dataName
	 */
	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	/**
	 * @param dataValue セットする dataValue
	 */
	public void setDataValue(Long dataValue) {
		this.dataValue = dataValue;
	}

	/**
	 * @param dataSeq セットする dataSeq
	 */
	public void setDataSeq(Long dataSeq) {
		this.dataSeq = dataSeq;
	}

	/**
	 * @param defFlg セットする defFlg
	 */
	public void setDefFlg(Long defFlg) {
		this.defFlg = defFlg;
	}

	@Override
	public int compareTo(DpsCCdMst obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.dataKbn, obj.dataKbn).append(this.dataCd, obj.dataCd)
					.toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && DpsCCdMst.class.isAssignableFrom(entry.getClass())) {
			DpsCCdMst obj = (DpsCCdMst) entry;
			return new EqualsBuilder().append(this.dataKbn, obj.dataKbn).append(this.dataCd, obj.dataCd).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.dataKbn).append(this.dataCd).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
