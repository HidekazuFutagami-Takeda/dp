package jp.co.takeda.model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.a.bean.Model;

/**
 * 組織基本を表すモデルクラス
 *
 * @author tkawabata
 */
public final class SosMstCtg extends Model<SosMstCtg> implements Cloneable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * カテゴリコード ※DBカラムはCATEGORY
	 */
	private String categoryCd;

	/**
	 * 組織コード
	 */
	private String categoryName;

	//---------------------
	// Getter & Setter
	// --------------------


	/**
	 * カテゴリを取得する。※DBカラムはCATEGORY
	 *
	 * @return カテゴリ
	 */
	public String getCategoryCd() {
		return categoryCd;
	}

	/**
	 * カテゴリを設定する。※DBカラムはCATEGORY
	 *
	 * @param oncSosFlg カテゴリ
	 */
	public void setCategoryCd(String categoryCd) {
		this.categoryCd = categoryCd;
	}

	/**
	 * @return categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * @param categoryName
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public int compareTo(SosMstCtg obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.categoryCd, obj.categoryCd).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && SosMstCtg.class.isAssignableFrom(entry.getClass())) {
			SosMstCtg obj = (SosMstCtg) entry;
			return new EqualsBuilder().append(this.categoryCd, obj.categoryCd).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.categoryCd).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

	@Override
	public SosMstCtg clone() {
		SosMstCtg sosMst = new SosMstCtg();
		sosMst.setCategoryCd(this.categoryCd);
		sosMst.setCategoryName(this.categoryName);
		return sosMst;
	}
}
