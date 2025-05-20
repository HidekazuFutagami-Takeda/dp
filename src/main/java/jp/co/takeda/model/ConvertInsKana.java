package jp.co.takeda.model;

import jp.co.takeda.a.bean.Model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設名カナを変換するためのクラス
 * 
 * @author tkawabata
 */
public class ConvertInsKana extends Model<ConvertInsKana> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 対象文字列
	 */
	private String entryString;

	/**
	 * 変換文字列
	 */
	private String replaceString;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 対象文字列を取得する。
	 * 
	 * @return 対象文字列
	 */
	public String getEntryString() {
		return entryString;
	}

	/**
	 * 対象文字列を設定する。
	 * 
	 * @param entryString 対象文字列
	 */
	public void setEntryString(String entryString) {
		this.entryString = entryString;
	}

	/**
	 * 変換文字列を取得する。
	 * 
	 * @return 変換文字列
	 */
	public String getReplaceString() {
		return replaceString;
	}

	/**
	 * 変換文字列を設定する。
	 * 
	 * @param replaceString 変換文字列
	 */
	public void setReplaceString(String replaceString) {
		this.replaceString = replaceString;
	}

	@Override
	public int compareTo(ConvertInsKana obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.entryString, obj.entryString).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && ConvertInsKana.class.isAssignableFrom(entry.getClass())) {
			ConvertInsKana obj = (ConvertInsKana) entry;
			return new EqualsBuilder().append(this.entryString, obj.entryString).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.entryString).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
