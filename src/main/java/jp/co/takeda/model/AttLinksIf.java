package jp.co.takeda.model;

import jp.co.takeda.a.bean.Model;
import jp.co.takeda.model.div.AttSystemKey;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * [外部直接参照]Attention外部リンク情報I/Fを表すモデルクラス
 * 
 * @author tkawabata
 */
public class AttLinksIf extends Model<AttLinksIf> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 外部システムリンク一意キー
	 */
	public static final Integer IF_SEQ_NO = 1;

	/**
	 * バージョン番号
	 */
	public static final Integer IF_VERSION_NO = 1;

	/**
	 * 対象システム情報ID
	 */
	public static final Integer OBJ_SYSTEM_ID = 0;

	/**
	 * リンク表示文字
	 */
	public static final String LINK_DISP_CHAR = "納入計画支援システムへ";

	/**
	 * 処理区分
	 */
	public static final String PROC_KB = "I";

	/**
	 * 反映済みフラグ
	 */
	public static final String DONE_FLG = "0";

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 外部システム一意キー
	 */
	private AttSystemKey ifPKey;

	/**
	 * 外部システムID
	 */
	private Integer ifSysKey;

	/**
	 * 外部リンクURL
	 */
	private String extLink;

	//---------------------
	// Getter & Setter
	// --------------------
	/**
	 * 外部システム一意キーを取得する。
	 * 
	 * @return 外部システム一意キー
	 */
	public AttSystemKey getIfPKey() {
		return ifPKey;
	}

	/**
	 * 外部システム一意キーを設定する。
	 * 
	 * @param ifPKey 外部システム一意キー
	 */
	public void setIfPKey(AttSystemKey ifPKey) {
		this.ifPKey = ifPKey;
	}

	/**
	 * 外部システムIDを取得する。
	 * 
	 * @return 外部システムID
	 */
	public Integer getIfSysKey() {
		return ifSysKey;
	}

	/**
	 * 外部システムIDを設定する。
	 * 
	 * @param ifSysKey 外部システムID
	 */
	public void setIfSysKey(Integer ifSysKey) {
		this.ifSysKey = ifSysKey;
	}

	/**
	 * 外部リンクURLを取得する。
	 * 
	 * @return 外部リンクURL
	 */
	public String getExtLink() {
		return extLink;
	}

	/**
	 * 外部リンクURLを設定する。
	 * 
	 * @param extLink 外部リンクURL
	 */
	public void setExtLink(String extLink) {
		this.extLink = extLink;
	}

	@Override
	public int compareTo(AttLinksIf obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.ifPKey, obj.ifPKey).append(this.ifSysKey, obj.ifSysKey).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && AttLinksIf.class.isAssignableFrom(entry.getClass())) {
			AttLinksIf obj = (AttLinksIf) entry;
			return new EqualsBuilder().append(this.ifPKey, obj.ifPKey).append(this.ifSysKey, obj.ifSysKey).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.ifPKey).append(this.ifSysKey).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
