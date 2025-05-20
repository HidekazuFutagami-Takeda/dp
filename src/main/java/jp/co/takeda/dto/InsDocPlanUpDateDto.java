package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設特約店別計画の追加・更新用DTO
 * 
 * @author siwamoto
 */
public class InsDocPlanUpDateDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * シーケンスキー(追加時null)
	 */
	private Long seqKey;

	/**
	 * 更新日時(追加時null)
	 */
	private Date plannedUpDate;

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
	 * 計画増減修正値
	 */
	private Long plannedIncValue;

	/**
	 * 計画理論値
	 */
	private Long theoreticalValueY;

	/**
	 * 計画確定値
	 */
	private Long plannedValue;

	/**
	 * 配分除外施設かを示すフラグ
	 */
	private Boolean exceptDistInsFlg;

	/**
	 * 削除施設かを示すフラグ
	 */
	private Boolean delInsFlg;

	/**
	 * シーケンスキー を取得する。
	 * @return シーケンスキー
	 */
	public Long getSeqKey() {
		return seqKey;
	}

	/**
	 * シーケンスキー を設定する。
	 * @param seqKey シーケンスキー
	 */
	public void setSeqKey(Long seqKey) {
		this.seqKey = seqKey;
	}

	/**
	 * 更新日時 を取得する。
	 * @return 更新日時
	 */
	public Date getPlannedUpDate() {
		return plannedUpDate;
	}

	/**
	 * 更新日時 を設定する。
	 * @param plannedUpDate 更新日時
	 */
	public void setPlannedUpDate(Date plannedUpDate) {
		this.plannedUpDate = plannedUpDate;
	}

	/**
	 * 従業員番号 を取得する。
	 * @return 従業員番号 
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 従業員番号 を設定する。
	 * @param jgiNo 従業員番号 
	 */
	public void setJgiNo(Integer jgiNo) {
		this.jgiNo = jgiNo;
	}

	/**
	 * 品目固定コード を取得する。
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目固定コード を設定する。
	 * @param prodCode 品目固定コード
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * 施設コード を取得する。
	 * @return 施設コード
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 施設コード を設定する。
	 * @param insNo 施設コード
	 */
	public void setInsNo(String insNo) {
		this.insNo = insNo;
	}

	/**
	 * 親施設コード を取得する。
	 * @return 親施設コード
	 */
	public String getMainInsNo() {
		return mainInsNo;
	}

	/**
	 * 親施設コード を設定する。
	 * @param mainInsNo 親施設コード
	 */
	public void setMainInsNo(String mainInsNo) {
		this.mainInsNo = mainInsNo;
	}

	/**
	 * 医師コード を取得する。
	 * @return 医師コード
	 */
	public String getDocNo() {
		return docNo;
	}

	/**
	 * 医師コード を設定する。
	 * @param docNo 医師コード
	 */
	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}
	
	/**
	 * 計画増減修正値 を取得する。
	 * @return 計画増減修正値
	 */
	public Long getPlannedIncValue() {
		return plannedIncValue;
	}

	/**
	 * 計画増減修正値 を設定する。
	 * @param plannedIncValue 計画増減修正値
	 */
	public void setPlannedIncValue(Long plannedIncValue) {
		this.plannedIncValue = plannedIncValue;
	}

	/**
	 * 計画理論値 を取得する。
	 * @return 計画理論値
	 */
	public Long getTheoreticalValueY() {
		return theoreticalValueY;
	}

	/**
	 * 計画理論値 を設定する。
	 * @param theoreticalValueY 計画理論値
	 */
	public void setTheoreticalValueY(Long theoreticalValueY) {
		this.theoreticalValueY = theoreticalValueY;
	}

	/**
	 * 計画確定値 を取得する。
	 * @return 計画確定値
	 */
	public Long getPlannedValue() {
		return plannedValue;
	}

	/**
	 * 計画確定値 を設定する。
	 * @param plannedValue 計画確定値
	 */
	public void setPlannedValue(Long plannedValue) {
		this.plannedValue = plannedValue;
	}

	/**
	 * 配分除外施設かを示すフラグ を取得する。
	 * @return 配分除外施設かを示すフラグ
	 */
	public Boolean getExceptDistInsFlg() {
		return exceptDistInsFlg;
	}

	/**
	 * 配分除外施設かを示すフラグ を設定する。
	 * @param exceptDistInsFlg 配分除外施設かを示すフラグ
	 */
	public void setExceptDistInsFlg(Boolean exceptDistInsFlg) {
		this.exceptDistInsFlg = exceptDistInsFlg;
	}

	/**
	 * 削除施設かを示すフラグ を取得する。
	 * @return 削除施設かを示すフラグ
	 */
	public Boolean getDelInsFlg() {
		return delInsFlg;
	}

	/**
	 * 削除施設かを示すフラグ を設定する。
	 * @param delInsFlg 削除施設かを示すフラグ
	 */
	public void setDelInsFlg(Boolean delInsFlg) {
		this.delInsFlg = delInsFlg;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
