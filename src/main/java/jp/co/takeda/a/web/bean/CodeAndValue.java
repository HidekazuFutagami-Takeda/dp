package jp.co.takeda.a.web.bean;

import java.io.Serializable;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.SystemException;

/**
 * コードと値を表すオブジェクト
 *
 * @author tkawabata
 */
public class CodeAndValue implements Serializable, Comparable<CodeAndValue> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * コード
	 */
	private final String code;

	/**
	 * 値
	 */
	private final String value;

	/**
	 * コードと値を設定するコンストラクタ
	 *
	 * @param code コード(NULL不可)
	 * @param value 値(NULL可)
	 */
	public CodeAndValue(final String code, final String value) {
		if (code == null) {
			throw new SystemException(new Conveyance(ErrMessageKey.PARAMETER_ERROR));
		}
		this.code = code;
		this.value = value;
	}

	/**
	 * コードを取得する。
	 *
	 * @return コード
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * 値を取得する。
	 *
	 * @return 値
	 */
	public String getValue() {
		return this.value;
	}

	public int compareTo(CodeAndValue obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.code, obj.code).toComparison();
		}
		return -1;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("code=").append(this.code).append(",");
		sb.append("value=").append(value);
		return sb.toString();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj != null && CodeAndValue.class.isAssignableFrom(obj.getClass())) {
			CodeAndValue cv = (CodeAndValue) obj;
			return new EqualsBuilder().append(this.code, cv.code).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.code).toHashCode();
	}
}
