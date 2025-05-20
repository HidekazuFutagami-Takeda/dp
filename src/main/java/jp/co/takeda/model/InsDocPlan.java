package jp.co.takeda.model;

import jp.co.takeda.bean.DpModel;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設医師別計画を表すモデルクラス
 * 
 * @author tkawabata
 */
public class InsDocPlan extends DpModel<InsDocPlan> {

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
	 * 親施設コード
	 */
	private String mainInsNo;

	/**
	 * 医師コード
	 */
	private String docNo;
	
	/**
	 * 配分除外施設かを示すフラグ
	 */
	private Boolean exceptDistInsFlg;

	/**
	 * 削除施設かを示すフラグ
	 */
	private Boolean delInsFlg;

	/**
	 * 未獲得市場
	 */
	private Integer nonPatientCnt;

	/**
	 * 薬効市場
	 */
	private Integer totPatientCnt;

	/**
	 * 症例数
	 */
	private Integer tkdPatientCnt;
	
	/**
	 * 当期計画
	 */
	private Long currentPeriod;

	/**
	 * 納入実績
	 */
	private Long advancePeriod;

	/**
	 * 計画増減理論値（Y）
	 */
	private Long theoreticalIncValueY;

	/**
	 * 計画増減修正値（Y）
	 */
	private Long plannedIncValueY;

	/**
	 * 計画理論値（Y）
	 */
	private Long theoreticalValueY;

	/**
	 * 計画確定値（Y）
	 */
	private Long plannedValueY;

	/**
	 * 計画確定値（T）
	 */
	private Long plannedValueT;
	
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
	 * 親施設コードを取得する。
	 * 
	 * @return 親施設コード
	 */
	public String getMainInsNo() {
		return mainInsNo;
	}

	/**
	 * 親施設コードを設定する。
	 * 
	 * @param mainInsNo 親施設コード
	 */
	public void setMainInsNo(String mainInsNo) {
		this.mainInsNo = mainInsNo;
	}
	
	/**
	 * 医師コードを取得する。
	 * 
	 * @return 医師コード
	 */
	public String getDocNo() {
		return docNo;
	}

	/**
	 * 医師コードを設定する。
	 * 
	 * @param docNo 医師コード
	 */
	public void setDocNo(String docNo) {
		this.docNo = docNo;
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
	public void setExceptDistInsFlg(Boolean expectDistInsFlg) {
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
	
	/**
	 * 未獲得市場を取得する。
	 * 
	 * @return 未獲得市場
	 */
	public Integer getNonPatientCnt() {
		return nonPatientCnt;
	}

	/**
	 * 未獲得市場を設定する。
	 * 
	 * @param nonPatientCnt 未獲得市場
	 */
	public void setNonPatientCnt(Integer nonPatientCnt) {
		this.nonPatientCnt = nonPatientCnt;
	}

	/**
	 * 薬効市場を取得する。
	 * 
	 * @return 薬効市場
	 */
	public Integer getTotPatientCnt() {
		return totPatientCnt;
	}

	/**
	 * 薬効市場を設定する。
	 * 
	 * @param totPatientCnt 薬効市場
	 */
	public void setTotPatientCnt(Integer totPatientCnt) {
		this.totPatientCnt = totPatientCnt;
	}

	/**
	 * 症例数を取得する。
	 * 
	 * @return 症例数
	 */
	public Integer getTkdPatientCnt() {
		return tkdPatientCnt;
	}

	/**
	 * 症例数を設定する。
	 * 
	 * @param tkdPatientCnt 症例数
	 */
	public void setTkdPatientCnt(Integer tkdPatientCnt) {
		this.tkdPatientCnt = tkdPatientCnt;
	}

	/**
	 * 当期計画を取得する。
	 * 
	 * @return 当期計画
	 */
	public Long getCurrentPeriod() {
		return currentPeriod;
	}

	/**
	 * 当期計画を設定する。
	 * 
	 * @param currentPeriod 当期計画
	 */
	public void setCurrentPeriod(Long currentPeriod) {
		this.currentPeriod = currentPeriod;
	}

	/**
	 * 納入実績を取得する。
	 * 
	 * @return 納入実績
	 */
	public Long getAdvancePeriod() {
		return advancePeriod;
	}

	/**
	 * 納入実績を設定する。
	 * 
	 * @param advancePeriod 納入実績
	 */
	public void setAdvancePeriod(Long advancePeriod) {
		this.advancePeriod = advancePeriod;
	}

	/**
	 * 計画増減理論値（Y）を取得する。
	 * 
	 * @return 計画増減理論値（Y）
	 */
	public Long getTheoreticalIncValueY() {
		return theoreticalIncValueY;
	}

	/**
	 * 計画増減理論値（Y）を設定する。
	 * 
	 * @param theoreticalIncValueY 計画増減理論値（Y）
	 */
	public void setTheoreticalIncValueY(Long theoreticalIncValueY) {
		this.theoreticalIncValueY = theoreticalIncValueY;
	}

	/**
	 * 計画増減修正値（Y）を取得する。
	 * 
	 * @return 計画増減修正値（Y）
	 */
	public Long getPlannedIncValueY() {
		return plannedIncValueY;
	}

	/**
	 * 計画増減修正値（Y）を設定する。
	 * 
	 * @param plannedIncValueY 計画増減修正値（Y）
	 */
	public void setPlannedIncValueY(Long plannedIncValueY) {
		this.plannedIncValueY = plannedIncValueY;
	}

	/**
	 * 計画理論値（Y）を取得する。
	 * 
	 * @return 計画理論値（Y）
	 */
	public Long getTheoreticalValueY() {
		return theoreticalValueY;
	}

	/**
	 * 計画理論値（Y）を設定する。
	 * 
	 * @param theoreticalValueY 計画理論値（Y）
	 */
	public void setTheoreticalValueY(Long theoreticalValueY) {
		this.theoreticalValueY = theoreticalValueY;
	}

	/**
	 * 計画確定値（Y）を取得する。
	 * 
	 * @return 計画確定値（Y）
	 */
	public Long getPlannedValueY() {
		return plannedValueY;
	}

	/**
	 * 計画確定値（Y）を設定する。
	 * 
	 * @param plannedValueY 計画確定値（Y）
	 */
	public void setPlannedValueY(Long plannedValueY) {
		this.plannedValueY = plannedValueY;
	}

	/**
	 * 計画確定値（T）を取得する。
	 * 
	 * @return 計画確定値（T）
	 */
	public Long getPlannedValueT() {
		return plannedValueT;
	}

	/**
	 * 計画確定値（T）を設定する。
	 * 
	 * @param plannedValueT 計画確定値（T）
	 */
	public void setPlannedValueT(Long plannedValueT) {
		this.plannedValueT = plannedValueT;
	}

	@Override
	public int compareTo(InsDocPlan obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.insNo, obj.insNo).append(this.docNo, obj.docNo).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && InsDocPlan.class.isAssignableFrom(entry.getClass())) {
			InsDocPlan obj = (InsDocPlan) entry;
			return new EqualsBuilder().append(this.insNo, obj.insNo).append(this.docNo, obj.docNo).append(this.prodCode, obj.prodCode).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.insNo).append(this.docNo).append(this.prodCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
