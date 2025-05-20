package jp.co.takeda.security;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.a.bean.Model;

/**
 * 権限を表すクラス
 *
 * @author tkawabata
 */
public class DpAuthority extends Model<DpAuthority> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * serialVersionUID
	 */

	/**
	 * 権限種別
	 */
	private final AuthType authType;

	/**
	 * コンストラクタ
	 *
	 * @param business 業務
	 * @param authType 権限種別
	 */
	public DpAuthority(AuthType authType) {
		this.authType = authType;
	}

	/**
	 * 権限種別を取得する。
	 *
	 * @return authType 権限種別
	 */
	public AuthType getAuthType() {
		return authType;
	}


	/**
	 * 権限種別を表す列挙
	 *
	 * @author tkawabata
	 */
	public enum AuthType {

		/**
		 * 参照
		 */
		REFER("参照"),

		/**
		 * 編集
		 */
		EDIT("編集"),

		/**
		 * 出力
		 */
		OUTPUT("出力");


		/**
		 * コンストラクタ
		 *
		 * @param authTypeName 権限種別名
		 */
		private AuthType(String authTypeName) {
			this.authTypeName = authTypeName;
		}

		/**
		 * 権限種別名
		 */
		private String authTypeName;

		/**
		 * 権限種別名を取得する。
		 *
		 * @return 権限種別名
		 */
		public String getAuthTypeName() {
			return authTypeName;
		}

		/**
		 * 権限種別名を設定する。
		 *
		 * @param authTypeName 権限種別名
		 */
		public void setAuthTypeName(String authTypeName) {
			this.authTypeName = authTypeName;
		}
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

	@Override
	public int compareTo(DpAuthority obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.authType, obj.authType).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && DpAuthority.class.isAssignableFrom(entry.getClass())) {
			DpAuthority obj = (DpAuthority) entry;
			return new EqualsBuilder().append(this.authType, obj.authType).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.authType).toHashCode();
	}
}
