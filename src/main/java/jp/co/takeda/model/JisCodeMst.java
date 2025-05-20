package jp.co.takeda.model;

import jp.co.takeda.a.bean.Model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * [外部直接参照] JIS府県・市区町村を表すモデルクラス
 * 
 * @author tkawabata
 */
public class JisCodeMst extends Model<JisCodeMst> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 都道府県コード
	 */
	private String todoufukenCd;

	/**
	 * 市区町村コード
	 */
	private String shikuchosonCd;

	/**
	 * 府県名（漢字）
	 */
	private String fukenMeiKj;

	/**
	 * 府県名（カナ）
	 */
	private String fukenMeiKn;

	/**
	 * 市区郡町村名（漢字）
	 */
	private String shikuchosonMeiKj;

	/**
	 * 市区郡町村名（カナ）
	 */
	private String shikuchosonMeiKn;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 都道府県コードを取得する。
	 * 
	 * @return 都道府県コード
	 */
	public String getTodoufukenCd() {
		return todoufukenCd;
	}

	/**
	 * 都道府県コードを設定する。
	 * 
	 * @param todoufukenCd 都道府県コード
	 */
	public void setTodoufukenCd(String todoufukenCd) {
		this.todoufukenCd = todoufukenCd;
	}

	/**
	 * 市区町村コードを取得する。
	 * 
	 * @return 市区町村コード
	 */
	public String getShikuchosonCd() {
		return shikuchosonCd;
	}

	/**
	 * 市区町村コードを設定する。
	 * 
	 * @param shikuchosonCd 市区町村コード
	 */
	public void setShikuchosonCd(String shikuchosonCd) {
		this.shikuchosonCd = shikuchosonCd;
	}

	/**
	 * 府県名（漢字）を取得する。
	 * 
	 * @return 府県名（漢字）
	 */
	public String getFukenMeiKj() {
		return fukenMeiKj;
	}

	/**
	 * 府県名（漢字）を設定する。
	 * 
	 * @param fukenMeiKn 府県名（漢字）
	 */
	public void setFukenMeiKj(String fukenMeiKj) {
		this.fukenMeiKj = fukenMeiKj;
	}

	/**
	 * 府県名（カナ）を取得する。
	 * 
	 * @return 府県名（カナ）
	 */
	public String getFukenMeiKn() {
		return fukenMeiKn;
	}

	/**
	 * 府県名（カナ）を設定する。
	 * 
	 * @param fukenMeiKn 府県名（カナ）
	 */
	public void setFukenMeiKn(String fukenMeiKn) {
		this.fukenMeiKn = fukenMeiKn;
	}

	/**
	 * 市区郡町村名（漢字）を取得する。
	 * 
	 * @return 市区郡町村名（漢字）
	 */
	public String getShikuchosonMeiKj() {
		return shikuchosonMeiKj;
	}

	/**
	 * 市区郡町村名（漢字）を設定する。
	 * 
	 * @param shikuchosonMeiKj 市区郡町村名（漢字）
	 */
	public void setShikuchosonMeiKj(String shikuchosonMeiKj) {
		this.shikuchosonMeiKj = shikuchosonMeiKj;
	}

	/**
	 * 市区郡町村名（カナ）を取得する。
	 * 
	 * @return 市区郡町村名（カナ）
	 */
	public String getShikuchosonMeiKn() {
		return shikuchosonMeiKn;
	}

	/**
	 * 市区郡町村名（カナ）を設定する。
	 * 
	 * @param shikuchosonMeiKn 市区郡町村名（カナ）
	 */
	public void setShikuchosonMeiKn(String shikuchosonMeiKn) {
		this.shikuchosonMeiKn = shikuchosonMeiKn;
	}

	@Override
	public int compareTo(JisCodeMst obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.todoufukenCd, obj.todoufukenCd).append(this.shikuchosonCd, obj.shikuchosonCd).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && JisCodeMst.class.isAssignableFrom(entry.getClass())) {
			JisCodeMst obj = (JisCodeMst) entry;
			return new EqualsBuilder().append(this.todoufukenCd, obj.todoufukenCd).append(this.shikuchosonCd, obj.shikuchosonCd).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.todoufukenCd).append(this.shikuchosonCd).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
