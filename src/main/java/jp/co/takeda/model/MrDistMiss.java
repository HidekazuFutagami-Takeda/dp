package jp.co.takeda.model;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.div.InsType;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 担当者別別配分ミスを表すモデルクラス
 * 
 * @author tkawabata
 */
public class MrDistMiss extends DpModel<MrDistMiss> {

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
	 * 施設出力対象区分
	 */
	private InsType insType;

	/**
	 * 計画値
	 */
	private Long plannedValue;

	/**
	 * 差額
	 */
	private Long diffValue;

	/**
	 * メッセージコード
	 */
	private String messageCode;

	/**
	 * 出力ファイル情報ID
	 */
	private Long outputFileId;

	/**
	 * 部門名正式 （Ref[組織基本].〔部門名正式〕）<br>
	 * (該当階層の組織名)
	 */
	private String bumonSeiName;

	/**
	 * 部門名略式<br>
	 * (該当階層の組織名)
	 */
	private String bumonRyakuName;

	/**
	 * 氏名（Ref[従業員基本].〔氏名〕）
	 */
	private String jgiName;

	/**
	 * 氏名（Ref[従業員基本].〔職種名〕）
	 */
	private String shokushuName;

	/**
	 * 統計品名コード(品目)（Ref[計画対象品目].〔統計品名コード〕）
	 */
	private String statProdCode;

	/**
	 * 品目名称（Ref[計画対象品目].〔品目名称〕）
	 */
	private String prodName;

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
	 * 施設区分を取得する。
	 * 
	 * @return 施設区分
	 */
	public InsType getInsType() {
		return insType;
	}

	/**
	 * 施設区分を設定する。
	 * 
	 * @param insType 施設区分
	 */
	public void setInsType(InsType insType) {
		this.insType = insType;
	}

	/**
	 * 計画値を取得する。
	 * 
	 * @return 計画値
	 */
	public Long getPlannedValue() {
		return plannedValue;
	}

	/**
	 * 計画値を設定する。
	 * 
	 * @param plannedValue 計画値
	 */
	public void setPlannedValue(Long plannedValue) {
		this.plannedValue = plannedValue;
	}

	/**
	 * 差額を取得する。
	 * 
	 * @return 差額
	 */
	public Long getDiffValue() {
		return diffValue;
	}

	/**
	 * 差額を設定する。
	 * 
	 * @param diffValue 差額
	 */
	public void setDiffValue(Long diffValue) {
		this.diffValue = diffValue;
	}

	/**
	 * メッセージコードを取得する。
	 * 
	 * @return メッセージコード
	 */
	public String getMessageCode() {
		return messageCode;
	}

	/**
	 * メッセージコードを設定する。
	 * 
	 * @param messageCode メッセージコード
	 */
	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	/**
	 * 出力ファイル情報IDを取得する。
	 * 
	 * @return 出力ファイル情報ID
	 */
	public Long getOutputFileId() {
		return outputFileId;
	}

	/**
	 * 出力ファイル情報IDを設定する。
	 * 
	 * @param outputFileId 出力ファイル情報ID
	 */
	public void setOutputFileId(Long outputFileId) {
		this.outputFileId = outputFileId;
	}

	/**
	 * 部門名正式を取得する。
	 * 
	 * @return 部門名正式
	 */
	public String getBumonSeiName() {
		return bumonSeiName;
	}

	/**
	 * 部門名正式を設定する。
	 * 
	 * @param bumonSeiName 部門名正式
	 */
	public void setBumonSeiName(String bumonSeiName) {
		this.bumonSeiName = bumonSeiName;
	}

	/**
	 * 部門名略式を取得する。
	 * 
	 * @return 部門名略式
	 */
	public String getBumonRyakuName() {
		return bumonRyakuName;
	}

	/**
	 * 部門名略式を設定する。
	 * 
	 * @param bumonRyakuName 部門名略式
	 */
	public void setBumonRyakuName(String bumonRyakuName) {
		this.bumonRyakuName = bumonRyakuName;
	}

	/**
	 * 氏名を取得する。
	 * 
	 * @return 氏名
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * 氏名を設定する。
	 * 
	 * @param jgiName 氏名
	 */
	public void setJgiName(String jgiName) {
		this.jgiName = jgiName;
	}

	/**
	 * 職種名を取得する。
	 * 
	 * @return 職種名
	 */
	public String getShokushuName() {
		return shokushuName;
	}

	/**
	 * 職種名を設定する。
	 * 
	 * @param shokushuName 職種名
	 */
	public void setShokushuName(String shokushuName) {
		this.shokushuName = shokushuName;
	}

	/**
	 * 統計品名コード(品目)を取得する。
	 * 
	 * @return 統計品名コード(品目)
	 */
	public String getStatProdCode() {
		return statProdCode;
	}

	/**
	 * 統計品名コード(品目)を設定する。
	 * 
	 * @param statProdCode 統計品名コード(品目)
	 */
	public void setStatProdCode(String statProdCode) {
		this.statProdCode = statProdCode;
	}

	/**
	 * 品目名称を取得する。
	 * 
	 * @return prodName 品目名称
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 品目名称を設定する。
	 * 
	 * @param prodName 品目名称
	 */
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	@Override
	public int compareTo(MrDistMiss obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.seqKey, obj.seqKey).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && MrDistMiss.class.isAssignableFrom(entry.getClass())) {
			MrDistMiss obj = (MrDistMiss) entry;
			return new EqualsBuilder().append(this.seqKey, obj.seqKey).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.seqKey).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
