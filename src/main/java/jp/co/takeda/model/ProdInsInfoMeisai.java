package jp.co.takeda.model;

import jp.co.takeda.bean.DpModel;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 品目施設情報明細を表すモデルクラス
 *
 * @author tkawabata
 */
public class ProdInsInfoMeisai extends DpModel<ProdInsInfoMeisai> {

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
	 * 品目シーケンス番号
	 */
	private String prodSeq;

	/**
	 * 施設コード
	 */
	private String insNo;

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
	 * 施設コードを取得する。
	 *
	 * @return 施設コード
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 施設コードを設定する。
	 *
	 * @param insNo 施設コード
	 */
	public void setInsNo(String insNo) {
		this.insNo = insNo;
	}

	/**
	 * 品目シーケンス番号を取得する。
	 *
	 * @return 品目シーケンス番号
	 */
	public String getProdSeq() {
		return prodSeq;
	}

	/**
	 * 品目シーケンス番号を設定する。
	 *
	 * @param prodSeq 品目シーケンス番号
	 */
	public void setProdSeq(String prodSeq) {
		this.prodSeq = prodSeq;
	}

	@Override
	public int compareTo(ProdInsInfoMeisai obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.prodCode, obj.prodCode).append(this.insNo, obj.insNo).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && ProdInsInfoMeisai.class.isAssignableFrom(entry.getClass())) {
			ProdInsInfoMeisai obj = (ProdInsInfoMeisai) entry;
			return new EqualsBuilder().append(this.prodCode, obj.prodCode).append(this.insNo, obj.insNo).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.prodCode).append(this.insNo).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
