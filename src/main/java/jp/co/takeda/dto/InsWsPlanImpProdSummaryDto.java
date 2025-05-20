package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設特約店別計画（重点品目）のサマリーを表すDTO
 * 
 * @author mizusawa
 */
public class InsWsPlanImpProdSummaryDto {
	/** 従業員番号 */
	Integer jgiNo;
	/** 品目固定コード */
	String prodCode;
	/** 施設コード */
	String insNo;
	/** 特約店コード */
	String tmsTytenCd;
	/** 親施設コード */
	String mainInsNo;
	/** 配分値Y */
	Double distValueY;
	/** 配分値Y（調整後） */
	Long distValueYRound;
	/** 修正値Y */
	Double modifyValueY;
	/** 確定値Y */
	Double plannedValueY;
	/** 確定値Y（調整後） */
	Long plannedValueYRound;
	/** 改定後計画値Y */
	Double befPlannedValueY;
	/** 確定値T */
	Double plannedValueT;
	/** 特定施設個別計画フラグ */
	String specialInsPlanFlg;
	/** 配分除外施設フラグ */
	String exceptDistInsFlg;
	/** 削除施設フラグ */
	String delInsFlg;
	
	/**
	 * コンストラクタ
	 * 
	 */
	public InsWsPlanImpProdSummaryDto(
			Integer jgiNo,
			String prodCode,
			String insNo,
			String tmsTytenCd,
			String mainInsNo,
			Double distValueY,
			Long distValueYRound,
			Double modifyValueY,
			Double plannedValueY,
			Long plannedValueYRound,
			Double befPlannedValueY,
			Double plannedValueT,
			String specialInsPlanFlg,
			String exceptDistInsFlg,
			String delInsFlg) {
		this.jgiNo = jgiNo;
		this.prodCode = prodCode;
		this.insNo = insNo;
		this.tmsTytenCd = tmsTytenCd;
		this.mainInsNo = mainInsNo;
		this.distValueY = distValueY;
		this.distValueYRound = distValueYRound;
		this.modifyValueY = modifyValueY;
		this.plannedValueY = plannedValueY;
		this.plannedValueYRound = plannedValueYRound;
		this.befPlannedValueY = befPlannedValueY;
		this.plannedValueT = plannedValueT;
		this.specialInsPlanFlg = specialInsPlanFlg;
		this.exceptDistInsFlg = exceptDistInsFlg;
		this.delInsFlg = delInsFlg;
	}
	
	public Integer getJgiNo() {
		return jgiNo;
	}
	public void setJgiNo(Integer jgiNo) {
		this.jgiNo = jgiNo;
	}
	public String getProdCode() {
		return prodCode;
	}
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
	public String getInsNo() {
		return insNo;
	}
	public void setInsNo(String insNo) {
		this.insNo = insNo;
	}
	public String getTmsTytenCd() {
		return tmsTytenCd;
	}
	public void setTmsTytenCd(String tmsTytenCd) {
		this.tmsTytenCd = tmsTytenCd;
	}
	public String getMainInsNo() {
		return mainInsNo;
	}
	public void setMainInsNo(String mainInsNo) {
		this.mainInsNo = mainInsNo;
	}
	public Double getDistValueY() {
		return distValueY;
	}
	public void setDistValueY(Double distValueY) {
		this.distValueY = distValueY;
	}
	public Long getDistValueYRound() {
		return distValueYRound;
	}
	public void setDistValueYRound(Long distValueYRound) {
		this.distValueYRound = distValueYRound;
	}
	public Double getModifyValueY() {
		return modifyValueY;
	}
	public void setModifyValueY(Double modifyValueY) {
		this.modifyValueY = modifyValueY;
	}
	public Double getPlannedValueY() {
		return plannedValueY;
	}
	public void setPlannedValueY(Double plannedValueY) {
		this.plannedValueY = plannedValueY;
	}
	public Long getPlannedValueYRound() {
		return plannedValueYRound;
	}
	public void setPlannedValueYRound(Long plannedValueYRound) {
		this.plannedValueYRound = plannedValueYRound;
	}
	public Double getBefPlannedValueY() {
		return befPlannedValueY;
	}
	public void setBefPlannedValueY(Double befPlannedValueY) {
		this.befPlannedValueY = befPlannedValueY;
	}
	public Double getPlannedValueT() {
		return plannedValueT;
	}
	public void setPlannedValueT(Double plannedValueT) {
		this.plannedValueT = plannedValueT;
	}
	public String getSpecialInsPlanFlg() {
		return specialInsPlanFlg;
	}
	public void setSpecialInsPlanFlg(String specialInsPlanFlg) {
		this.specialInsPlanFlg = specialInsPlanFlg;
	}
	public String getExceptDistInsFlg() {
		return exceptDistInsFlg;
	}
	public void setExceptDistInsFlg(String exceptDistInsFlg) {
		this.exceptDistInsFlg = exceptDistInsFlg;
	}
	public String getDelInsFlg() {
		return delInsFlg;
	}
	public void setDelInsFlg(String delInsFlg) {
		this.delInsFlg = delInsFlg;
	}
	
	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
