package jp.co.takeda.model;

import jp.co.takeda.a.bean.Model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * [外部直接参照]標準組織-特約店制限を表すモデルクラス
 * 
 * @author tkawabata
 */
public class SosWs extends Model<SosWs> {

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
	private String sosCode;

	/**
	 * TMS特約店本店コード
	 */
	private String wshqCode;

	/**
	 * TMS特約店支社コード
	 */
	private String wsbrCode;

	/**
	 * TMS特約店支店コード
	 */
	private String wsdiCode;

	/**
	 * TMS特約店課コード
	 */
	private String wsdeCode;

	/**
	 * TMS特約店ブロックコード
	 */
	private String wsbcCode;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 組織コードを取得する。
	 * 
	 * @return 組織コード
	 */
	public String getSosCode() {
		return sosCode;
	}

	/**
	 * 組織コードを設定する。
	 * 
	 * @param sosCode 組織コード
	 */
	public void setSosCode(String sosCode) {
		this.sosCode = sosCode;
	}

	/**
	 * TMS特約店本店コードを取得する。
	 * 
	 * @return TMS特約店本店コード
	 */
	public String getWshqCode() {
		return wshqCode;
	}

	/**
	 * TMS特約店本店コードを設定する。
	 * 
	 * @param wshqCode TMS特約店本店コード
	 */
	public void setWshqCode(String wshqCode) {
		this.wshqCode = wshqCode;
	}

	/**
	 * TMS特約店支社コードを取得する。
	 * 
	 * @return TMS特約店支社コード
	 */
	public String getWsbrCode() {
		return wsbrCode;
	}

	/**
	 * TMS特約店支社コードを設定する。
	 * 
	 * @param wsbrCode TMS特約店支社コード
	 */
	public void setWsbrCode(String wsbrCode) {
		this.wsbrCode = wsbrCode;
	}

	/**
	 * TMS特約店支店コードを取得する。
	 * 
	 * @return TMS特約店支店コード
	 */
	public String getWsdiCode() {
		return wsdiCode;
	}

	/**
	 * TMS特約店支店コードを設定する。
	 * 
	 * @param wsdiCode TMS特約店支店コード
	 */
	public void setWsdiCode(String wsdiCode) {
		this.wsdiCode = wsdiCode;
	}

	/**
	 * TMS特約店課コードを取得する。
	 * 
	 * @return TMS特約店課コード
	 */
	public String getWsdeCode() {
		return wsdeCode;
	}

	/**
	 * TMS特約店課コードを設定する。
	 * 
	 * @param wsdeCode TMS特約店課コード
	 */
	public void setWsdeCode(String wsdeCode) {
		this.wsdeCode = wsdeCode;
	}

	/**
	 * TMS特約店ブロックコードを取得する。
	 * 
	 * @return TMS特約店ブロックコード
	 */
	public String getWsbcCode() {
		return wsbcCode;
	}

	/**
	 * TMS特約店ブロックコードを設定する。
	 * 
	 * @param wsbcCode TMS特約店ブロックコード
	 */
	public void setWsbcCode(String wsbcCode) {
		this.wsbcCode = wsbcCode;
	}

	@Override
	public int compareTo(SosWs obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.sosCode, obj.sosCode).append(this.wshqCode, obj.wshqCode).append(this.wsbrCode, obj.wsbrCode).append(this.wsdiCode,
				obj.wsdiCode).append(this.wsdeCode, obj.wsdeCode).append(this.wsbcCode, obj.wsbcCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && SosWs.class.isAssignableFrom(entry.getClass())) {
			SosWs obj = (SosWs) entry;
			return new EqualsBuilder().append(this.sosCode, obj.sosCode).append(this.wshqCode, obj.wshqCode).append(this.wsbrCode, obj.wsbrCode)
				.append(this.wsdiCode, obj.wsdiCode).append(this.wsdeCode, obj.wsdeCode).append(this.wsbcCode, obj.wsbcCode).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.sosCode).append(this.wshqCode).append(this.wsbrCode).append(this.wsdiCode).append(this.wsdeCode).append(this.wsbcCode)
			.toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
