package jp.co.takeda.model;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.div.InsType;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 組織別計画配分ミスを表すモデルクラス
 * 
 * @author tkawabata
 */
public class SosDistMiss extends DpModel<SosDistMiss> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 組織コード
	 */
	private String sosCd;

	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 施設区分
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
	 * 組織コードを取得する。
	 * 
	 * @return 組織コード
	 */
	public String getSosCd() {
		return sosCd;
	}

	/**
	 * 組織コードを設定する。
	 * 
	 * @param sosCd 組織コード
	 */
	public void setSosCd(String sosCd) {
		this.sosCd = sosCd;
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
	public int compareTo(SosDistMiss obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.seqKey, obj.seqKey).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && SosDistMiss.class.isAssignableFrom(entry.getClass())) {
			SosDistMiss obj = (SosDistMiss) entry;
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
