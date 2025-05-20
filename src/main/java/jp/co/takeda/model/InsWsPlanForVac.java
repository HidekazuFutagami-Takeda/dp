package jp.co.takeda.model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpModel;

/**
 * (ワクチン)施設特約店別計画を表すモデルクラス
 * 
 * @author tkawabata
 */
public class InsWsPlanForVac extends DpModel<InsWsPlanForVac> {

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
	 * 施設コード
	 */
	private String insNo;

	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * TMS特約店コード
	 */
	private String tmsTytenCd;

	/**
	 * 配分値(B)
	 */
	private Long distValueB;

	/**
	 * 計画値(B)
	 */
	private Long plannedValueB;

	/**
	 * 改定後計画値(B)
	 */
	private Long befPlannedValueB;

	/**
	 * 計画値(T)
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
	 * 配分値(B)を取得する。
	 * 
	 * @return 配分値(B)
	 */
	public Long getDistValueB() {
		return distValueB;
	}

	/**
	 * 配分値(B)を設定する。
	 * 
	 * @param distValueB 配分値(B)
	 */
	public void setDistValueB(Long distValueB) {
		this.distValueB = distValueB;
	}

	/**
	 * 計画値(B)を取得する。
	 * 
	 * @return 計画値(B)
	 */
	public Long getPlannedValueB() {
		return plannedValueB;
	}

	/**
	 * 計画値(B)を設定する。
	 * 
	 * @param plannedValueB 計画値(B)
	 */
	public void setPlannedValueB(Long plannedValueB) {
		this.plannedValueB = plannedValueB;
	}

	/**
	 * 改定後計画値(B)を取得する。
	 * 
	 * @return 改定後計画値(B)
	 */
	public Long getBefPlannedValueB() {
		return befPlannedValueB;
	}

	/**
	 * 改定後計画値(B)を設定する。
	 * 
	 * @param befPlannedValueB 改定後計画値(B)
	 */
	public void setBefPlannedValueB(Long befPlannedValueB) {
		this.befPlannedValueB = befPlannedValueB;
	}

	/**
	 * 計画値(T)を取得する。
	 * 
	 * @return 計画値(T)
	 */
	public Long getPlannedValueT() {
		return plannedValueT;
	}

	/**
	 * 計画値(T)を設定する。
	 * 
	 * @param plannedValueT 計画値(T)
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
	public int compareTo(InsWsPlanForVac obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.insNo, obj.insNo).append(this.tmsTytenCd, obj.tmsTytenCd).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && InsWsPlanForVac.class.isAssignableFrom(entry.getClass())) {
			InsWsPlanForVac obj = (InsWsPlanForVac) entry;
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
