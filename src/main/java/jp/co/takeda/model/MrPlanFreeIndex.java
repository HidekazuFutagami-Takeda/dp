package jp.co.takeda.model;

import jp.co.takeda.bean.DpModel;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 担当者別計画フリー項目を表すモデルクラス
 *
 * @author tkawabata
 */
public class MrPlanFreeIndex extends DpModel<MrPlanFreeIndex> {

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
	 * 従業員氏名（Ref[ 従業員基本].〔氏名〕）
	 */
	private String jgiName;

	/**
	 * 職種名
	 */
	private String shokushuName;

	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * フリー項目値UH1
	 */
	private Long indexFreeValueUh1;

	/**
	 * フリー項目値UH2
	 */
	private Long indexFreeValueUh2;

	/**
	 * フリー項目値UH3
	 */
	private Long indexFreeValueUh3;

	/**
	 * フリー項目値P1
	 */
	private Long indexFreeValueP1;

	/**
	 * フリー項目値P2
	 */
	private Long indexFreeValueP2;

	/**
	 * フリー項目値P3
	 */
	private Long indexFreeValueP3;

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
	 * 従業員氏名を取得する。
	 *
	 * @return 従業員氏名
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * 従業員氏名を設定する。
	 *
	 * @param jgiName 従業員氏名
	 */
	public void setJgiName(String jgiName) {
		this.jgiName = jgiName;
	}

	/**
	 * 品目固定コードを取得する。
	 *
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目固定コードを設定する。
	 *
	 * @param prodCode 品目固定コード
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * フリー項目値UH1を取得する。
	 *
	 * @return フリー項目値UH1
	 */
	public Long getIndexFreeValueUh1() {
		return indexFreeValueUh1;
	}

	/**
	 * フリー項目値UH1を設定する。
	 *
	 * @param indexFreeValueUh1 フリー項目値UH1
	 */
	public void setIndexFreeValueUh1(Long indexFreeValueUh1) {
		this.indexFreeValueUh1 = indexFreeValueUh1;
	}

	/**
	 * フリー項目値UH2を取得する。
	 *
	 * @return フリー項目値UH2
	 */
	public Long getIndexFreeValueUh2() {
		return indexFreeValueUh2;
	}

	/**
	 * フリー項目値UH2を設定する。
	 *
	 * @param indexFreeValueUh2 フリー項目値UH2
	 */
	public void setIndexFreeValueUh2(Long indexFreeValueUh2) {
		this.indexFreeValueUh2 = indexFreeValueUh2;
	}

	/**
	 * フリー項目値UH3を取得する。
	 *
	 * @return フリー項目値UH3
	 */
	public Long getIndexFreeValueUh3() {
		return indexFreeValueUh3;
	}

	/**
	 * フリー項目値UH3を設定する。
	 *
	 * @param indexFreeValueUh3 フリー項目値UH3
	 */
	public void setIndexFreeValueUh3(Long indexFreeValueUh3) {
		this.indexFreeValueUh3 = indexFreeValueUh3;
	}

	/**
	 * フリー項目値P1を取得する。
	 *
	 * @return フリー項目値P1
	 */
	public Long getIndexFreeValueP1() {
		return indexFreeValueP1;
	}

	/**
	 * フリー項目値P1を設定する。
	 *
	 * @param indexFreeValueP1 フリー項目値P1
	 */
	public void setIndexFreeValueP1(Long indexFreeValueP1) {
		this.indexFreeValueP1 = indexFreeValueP1;
	}

	/**
	 * フリー項目値P2を取得する。
	 *
	 * @return フリー項目値P2
	 */
	public Long getIndexFreeValueP2() {
		return indexFreeValueP2;
	}

	/**
	 * フリー項目値P2を設定する。
	 *
	 * @param indexFreeValueP2 フリー項目値P2
	 */
	public void setIndexFreeValueP2(Long indexFreeValueP2) {
		this.indexFreeValueP2 = indexFreeValueP2;
	}

	/**
	 * フリー項目値P3を取得する。
	 *
	 * @return フリー項目値P3
	 */
	public Long getIndexFreeValueP3() {
		return indexFreeValueP3;
	}

	/**
	 * フリー項目値P3を設定する。
	 *
	 * @param indexFreeValueP3 フリー項目値P3
	 */
	public void setIndexFreeValueP3(Long indexFreeValueP3) {
		this.indexFreeValueP3 = indexFreeValueP3;
	}

	/**
	 * 職種名を取得する
	 * @return shokushuName
	 */
	public String getShokushuName() {
		return shokushuName;
	}

	/**
	 * 職種名を設定する
	 * @param shokushuName
	 */
	public void setShokushuName(String shokushuName) {
		this.shokushuName = shokushuName;
	}

	@Override
	public int compareTo(MrPlanFreeIndex obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.jgiNo, obj.jgiNo).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && MrPlanFreeIndex.class.isAssignableFrom(entry.getClass())) {
			MrPlanFreeIndex obj = (MrPlanFreeIndex) entry;
			return new EqualsBuilder().append(this.jgiNo, obj.jgiNo).append(this.prodCode, obj.prodCode).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.jgiNo).append(this.prodCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
