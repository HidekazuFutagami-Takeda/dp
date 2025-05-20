package jp.co.takeda.security;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import jp.co.takeda.a.bean.Model;

/**
 * 納入計画システムのメタ情報
 *
 * @author tkawabata
 */
public class DpMetaInfo extends Model<DpMetaInfo> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * アクセスパス
	 */
	private final String path;

	/**
	 * システムID
	 */
	private final String sId;

	/**
	 * 業務コード
	 */
	private final String gCode;

	/**
	 * 機能コード
	 */
	private final String fCode;

	/**
	 * 共通/支援/管理
	 */
	private final BusinessTarget businessTarget;

	/**
	 * 共通/医薬/ワクチン
	 */
	private final BusinessType businessType;

	/**
	 * 要求権限リスト
	 */
	private final DpAuthority authority;

	/**
	 * サーバ区分
	 */
	private final String appServerKbn;

	/**
	 * ログ文字列
	 */
	private final StringBuilder logString;

	/**
	 * コンストラクタ
	 *
	 * @param path アクセスパス
	 * @param sId システムID
	 * @param gCode 業務コード
	 * @param fCode 機能コード
	 * @param businessTarget 共通/支援/管理
	 * @param businessType 共通/医薬/ワクチン
	 * @param authList 要求権限リスト
	 * @param appServerKbn サーバ区分
	 * @param logString ログ文字列
	 */
	public DpMetaInfo(final String path, final String sId, final String gCode, final String fCode, final BusinessTarget businessTarget, final BusinessType businessType,
		final DpAuthority authority, final String appServerKbn, final StringBuilder logString) {
		this.path = path;
		this.sId = sId;
		this.gCode = gCode;
		this.fCode = fCode;
		this.businessTarget = businessTarget;
		this.businessType = businessType;
		this.authority = authority;
		this.appServerKbn = appServerKbn;
		this.logString = logString;
	}

	/**
	 * アクセスパスを取得する。
	 *
	 * @return アクセスパス
	 */
	public String getPath() {
		return path;
	}

	/**
	 * システムIDを取得する。
	 *
	 * @return システムID
	 */
	public String getSId() {
		return sId;
	}

	/**
	 * 業務コードを取得する。
	 *
	 * @return 業務コード
	 */
	public String getGCode() {
		return gCode;
	}

	/**
	 * 機能コードを取得する。
	 *
	 * @return 機能コード
	 */
	public String getFCode() {
		return fCode;
	}

	/**
	 * 共通/支援/管理を取得する。
	 *
	 * @return 共通/支援/管理
	 */
	public BusinessTarget getBusinessTarget() {
		return businessTarget;
	}

	/**
	 * 共通/医薬/ワクチンを取得する。
	 *
	 * @return 共通/医薬/ワクチン
	 */
	public BusinessType getBusinessType() {
		return businessType;
	}

	/**
	 * 要求権限リストを取得する。
	 *
	 * @return 要求権限リスト
	 */
	public DpAuthority getAuthority() {
		return authority;
	}

	/**
	 * サーバ区分を取得する。
	 *
	 * @return appServerKbn サーバ区分
	 */
	public String getAppServerKbn() {
		return appServerKbn;
	}

	/**
	 * ログ文字列を取得する。
	 *
	 * @return logString ログ文字列
	 */
	public StringBuilder getLogString() {
		return logString;
	}

	@Override
	public int compareTo(DpMetaInfo obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.path, obj.path).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && DpMetaInfo.class.isAssignableFrom(entry.getClass())) {
			DpMetaInfo obj = (DpMetaInfo) entry;
			return new EqualsBuilder().append(this.path, obj.path).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.path).toHashCode();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("path=" + path + ",");
		sb.append("gCode=" + gCode + ",");
		sb.append("fCode=" + fCode + ",");
		sb.append("businessTarget=" + businessTarget + ",");
		sb.append("businessType=" + businessType + ",");
		sb.append("authList=" + authority + ",");
		sb.append("appServerKbn=" + appServerKbn);
		return sb.toString();
	}
}
