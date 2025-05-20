package jp.co.takeda.model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpModel;

/**
 * 薬効市場マスタを表すモデルクラス
 *
 * @author tkawabata
 */
public class YakkouSijou extends DpModel<YakkouSijou> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 薬効市場コード
	 */
	private String yakkouSijouCode;

	/**
	 * 薬効市場ソートキー
	 */
	private Integer yakkouSijouSortKey;

	/**
	 * 薬効市場表示用名称
	 */
	private String yakkouSijouName;

	/**
	 * 削除フラグ
	 */
	private Boolean delFlg;

	/**
	 * カテゴリ
	 */
	private String category;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 薬効市場コードを取得する。
	 *
	 * @return 薬効市場コード
	 */
	public String getYakkouSijouCode() {
		return yakkouSijouCode;
	}

	/**
	 * 薬効市場コードを設定する。
	 *
	 * @param yakkouSijouCode 薬効市場コード
	 */
	public void setYakkouSijouCode(String yakkouSijouCode) {
		this.yakkouSijouCode = yakkouSijouCode;
	}

	/**
	 * 薬効市場ソートキーを取得する。
	 *
	 * @return 薬効市場ソートキー
	 */
	public Integer getYakkouSijouSortKey() {
		return yakkouSijouSortKey;
	}

	/**
	 * 薬効市場ソートキーを設定する。
	 *
	 * @param yakkouSijouSortKey 薬効市場ソートキー
	 */
	public void setYakkouSijouSortKey(Integer yakkouSijouSortKey) {
		this.yakkouSijouSortKey = yakkouSijouSortKey;
	}

	/**
	 * 薬効市場表示用名称を取得する。
	 *
	 * @return 薬効市場表示用名称
	 */
	public String getYakkouSijouName() {
		return yakkouSijouName;
	}

	/**
	 * 薬効市場表示用名称を設定する。
	 *
	 * @param yakkouSijouName 薬効市場表示用名称
	 */
	public void setYakkouSijouName(String yakkouSijouName) {
		this.yakkouSijouName = yakkouSijouName;
	}

	/**
	 * 削除フラグを取得する。
	 *
	 * @return 削除フラグ
	 */
	public Boolean getDelFlg() {
		return delFlg;
	}

	/**
	 * 削除フラグを設定する。
	 *
	 * @param delFlg 削除フラグ
	 */
	public void setDelFlg(Boolean delFlg) {
		this.delFlg = delFlg;
	}

	/**
	 * カテゴリを取得する。
	 *
	 * @return カテゴリ
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * カテゴリを設定する。
	 *
	 * @param category カテゴリ
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public int compareTo(YakkouSijou obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.yakkouSijouCode, obj.yakkouSijouCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && YakkouSijou.class.isAssignableFrom(entry.getClass())) {
			YakkouSijou obj = (YakkouSijou) entry;
			return new EqualsBuilder().append(this.yakkouSijouCode, obj.yakkouSijouCode).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.yakkouSijouCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
