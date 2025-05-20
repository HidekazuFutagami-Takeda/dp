package jp.co.takeda.model;

import jp.co.takeda.bean.DpModel;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設特約店別計画を表すモデルクラス
 * 
 * @author tkawabata
 */
public class InsWsPlan extends DpModel<InsWsPlan> {

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
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 施設コード
	 */
	private String insNo;

	/**
	 * TMS特約店コード
	 */
	private String tmsTytenCd;

	/**
	 * 配分値(Y)
	 */
	private Long distValueY;

	/**
	 * 修正値(Y)
	 */
	private Long modifyValueY;

	/**
	 * 確定値(Y)
	 */
	private Long plannedValueY;

	/**
	 * 改定前計画値(Y)
	 */
	private Long befPlannedValueY;
	
	/**
	 * 確定値(T)
	 */
	private Long plannedValueT;

	/**
	 * 特定施設個別計画かを示すフラグ
	 */
	private Boolean specialInsPlanFlg;

	/**
	 * 配分除外施設かを示すフラグ
	 */
	private Boolean exceptDistInsFlg;

	/**
	 * 削除施設かを示すフラグ
	 */
	private Boolean delInsFlg;

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
	 * 施設コードを取得する。
	 * 
	 * @return 施設コード
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 施設コードを設定する。
	 * 
	 * @param insNo 施設コード
	 */
	public void setInsNo(String insNo) {
		this.insNo = insNo;
	}

	/**
	 * TMS特約店コードを取得する。
	 * 
	 * @return TMS特約店コード
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
	}

	/**
	 * TMS特約店コードを設定する。
	 * 
	 * @param tmsTytenCd TMS特約店コード
	 */
	public void setTmsTytenCd(String tmsTytenCd) {
		this.tmsTytenCd = tmsTytenCd;
	}

	/**
	 * 配分値(Y)を取得する。
	 * 
	 * @return 配分値(Y)
	 */
	public Long getDistValueY() {
		return distValueY;
	}

	/**
	 * 配分値(Y)を設定する。
	 * 
	 * @param distValueY 配分値(Y)
	 */
	public void setDistValueY(Long distValueY) {
		this.distValueY = distValueY;
	}

	/**
	 * 修正値(Y)を取得する。
	 * 
	 * @return 修正値(Y)
	 */
	public Long getModifyValueY() {
		return modifyValueY;
	}

	/**
	 * 修正値(Y)を設定する。
	 * 
	 * @param modifyValueY 修正値(Y)
	 */
	public void setModifyValueY(Long modifyValueY) {
		this.modifyValueY = modifyValueY;
	}

	/**
	 * 確定値(Y)を取得する。
	 * 
	 * @return 確定値(Y)
	 */
	public Long getPlannedValueY() {
		return plannedValueY;
	}

	/**
	 * 確定値(Y)を設定する。
	 * 
	 * @param plannedValueY 確定値(Y)
	 */
	public void setPlannedValueY(Long plannedValueY) {
		this.plannedValueY = plannedValueY;
	}

	/**
	 * 改定前計画値(Y)を取得する。
	 * 
	 * @return 改定前計画値(Y)
	 */
	public Long getBefPlannedValueY() {
		return befPlannedValueY;
	}

	/**
	 * 改定前計画値(Y)を設定する。
	 * 
	 * @param befPlannedValueY 改定前計画値(Y)
	 */
	public void setBefPlannedValueY(Long befPlannedValueY) {
		this.befPlannedValueY = befPlannedValueY;
	}
	
	/**
	 * 確定値(T)を取得する。
	 * 
	 * @return 確定値(T)
	 */
	public Long getPlannedValueT() {
		return plannedValueT;
	}

	/**
	 * 確定値(T)を設定する。
	 * 
	 * @param plannedValueT 確定値(T)
	 */
	public void setPlannedValueT(Long plannedValueT) {
		this.plannedValueT = plannedValueT;
	}
	
	/**
	 * 特定施設個別計画かを示すフラグを取得する。
	 * 
	 * @return 特定施設個別計画かを示すフラグ
	 */
	public Boolean getSpecialInsPlanFlg() {
		return specialInsPlanFlg;
	}

	/**
	 * 特定施設個別計画かを示すフラグを設定する。
	 * 
	 * @param specialInsPlanFlg 特定施設個別計画かを示すフラグ
	 */
	public void setSpecialInsPlanFlg(Boolean specialInsPlanFlg) {
		this.specialInsPlanFlg = specialInsPlanFlg;
	}

	/**
	 * 配分除外施設かを示すフラグを取得する。
	 * 
	 * @return 配分除外施設かを示すフラグ
	 */
	public Boolean getExceptDistInsFlg() {
		return exceptDistInsFlg;
	}

	/**
	 * 配分除外施設かを示すフラグを設定する。
	 * 
	 * @param expectDistInsFlg 配分除外施設かを示すフラグ
	 */
	public void setExpectDistInsFlg(Boolean expectDistInsFlg) {
		this.exceptDistInsFlg = expectDistInsFlg;
	}

	/**
	 * 削除施設かを示すフラグを取得する。
	 * 
	 * @return 削除施設かを示すフラグ
	 */
	public Boolean getDelInsFlg() {
		return delInsFlg;
	}

	/**
	 * 削除施設かを示すフラグを設定する。
	 * 
	 * @param delInsFlg 削除施設かを示すフラグ
	 */
	public void setDelInsFlg(Boolean delInsFlg) {
		this.delInsFlg = delInsFlg;
	}

	@Override
	public int compareTo(InsWsPlan obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.insNo, obj.insNo).append(this.tmsTytenCd, obj.tmsTytenCd).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && InsWsPlan.class.isAssignableFrom(entry.getClass())) {
			InsWsPlan obj = (InsWsPlan) entry;
			return new EqualsBuilder().append(this.insNo, obj.insNo).append(this.tmsTytenCd, obj.tmsTytenCd).append(this.prodCode, obj.prodCode).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.insNo).append(this.tmsTytenCd).append(this.prodCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
