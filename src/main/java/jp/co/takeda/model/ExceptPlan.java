package jp.co.takeda.model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpModel;

/**
 * 営業所計画を表すモデルクラス
 *
 * @author tkawabata
 */
public class ExceptPlan extends DpModel<ExceptPlan> {

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
	 * 従業員番号
	 */
	private String jgiNo;

	/**
	 * 従業員番号
	 */
	private String jgiName;

	/**
	 * メッセージ
	 */
	private String message;

	/**
	 * 施設コード
	 */
	private String insNo;

	/**
	 * TMS特約店コード
	 */
	private String tmsTytenCd;


	/**
	 * TMS特約店名または施設名
	 */
	private String insOrTytenName;


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
	 * @return jgiNo
	 */
	public String getJgiNo() {
		return jgiNo;
	}

	/**
	 * @param jgiNo セットする jgiNo
	 */
	public void setJgiNo(String jgiNo) {
		this.jgiNo = jgiNo;
	}

	/**
	 * @return jgiName
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * @param jgiName セットする jgiName
	 */
	public void setJgiName(String jgiName) {
		this.jgiName = jgiName;
	}

	/**
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 改行したメッセージ
	 * @return message
	 */
	public String getWrappedMessage() {
		return message.replace("対象品目","<br/>対象品目");
	}

	/**
	 * @param message セットする message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return insNo
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * @param insNo セットする insNo
	 */
	public void setInsNo(String insNo) {
		this.insNo = insNo;
	}

	/**
	 * @return tmsTytenCd
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
	}

	/**
	 * @param tmsTytenCd セットする tmsTytenCd
	 */
	public void setTmsTytenCd(String tmsTytenCd) {
		this.tmsTytenCd = tmsTytenCd;
	}

	/**
	 * @return insOrTytenName
	 */
	public String getInsOrTytenName() {
		return insOrTytenName;
	}

	/**
	 * @param insOrTytenName セットする insOrTytenName
	 */
	public void setInsOrTytenName(String insOrTytenName) {
		this.insOrTytenName = insOrTytenName;
	}

	@Override
	public int compareTo(ExceptPlan obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.seqKey, obj.seqKey).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && ExceptPlan.class.isAssignableFrom(entry.getClass())) {
			ExceptPlan obj = (ExceptPlan) entry;
			return new EqualsBuilder().append(this.seqKey, obj.seqKey).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.seqKey).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
