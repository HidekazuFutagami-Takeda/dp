package jp.co.takeda.model;

import jp.co.takeda.bean.DpModel;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * お知らせ情報を表すモデルクラス
 * 
 * @author khashimoto
 */
public class Announce extends DpModel<Announce> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 従業員番号
	 */
	private Integer jgiNo;

	/**
	 * メッセージ
	 */
	private String message;

	/**
	 * 出力ファイル情報ID
	 */
	private Long outputFileId;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 従業員番号を取得する。
	 * 
	 * @return 従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 従業員番号を設定する。
	 * 
	 * @param jgiNo 従業員番号
	 */
	public void setJgiNo(Integer jgiNo) {
		this.jgiNo = jgiNo;
	}

	/**
	 * メッセージを取得する。
	 * 
	 * @return メッセージ
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * メッセージを設定する。
	 * 
	 * @param message メッセージ
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 出力ファイル情報IDを取得する。
	 * 
	 * @return 出力ファイル情報ID
	 */
	public Long getOutputFileId() {
		return outputFileId;
	}

	/**
	 * 出力ファイル情報IDを設定する。
	 * 
	 * @param outputFileId 出力ファイル情報ID
	 */
	public void setOutputFileId(Long outputFileId) {
		this.outputFileId = outputFileId;
	}

	@Override
	public int compareTo(Announce obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.seqKey, obj.seqKey).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && Announce.class.isAssignableFrom(entry.getClass())) {
			Announce obj = (Announce) entry;
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
