package jp.co.takeda.model.view;

import jp.co.takeda.a.bean.Model;
import jp.co.takeda.model.div.InsType;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 納入実績の施設出力対象区分別集計を表すモデルクラス
 * 
 * @author nozaki
 */
public class DeliveryResultInsTypeSummary extends Model<DeliveryResultInsTypeSummary> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 施設出力対象区分
	 */
	private InsType insType;

	/**
	 * 納入実績合計
	 */
	private Long sumDeliveryRecord;

	//---------------------
	// Getter & Setter
	// --------------------

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
	 * 施設出力対象区分を取得する。
	 * 
	 * @return 施設出力対象区分
	 */
	public InsType getInsType() {
		return insType;
	}

	/**
	 * 施設出力対象区分を設定する。
	 * 
	 * @param insType 施設出力対象区分
	 */
	public void setInsType(InsType insType) {
		this.insType = insType;
	}

	/**
	 * 納入実績合計を取得する。
	 * 
	 * @return 納入実績合計
	 */
	public Long getSumDeliveryRecord() {
		return sumDeliveryRecord;
	}

	/**
	 * 納入実績合計を設定する。
	 * 
	 * @param sumDeliveryRecord 納入実績合計
	 */
	public void setSumDeliveryRecord(Long sumDeliveryRecord) {
		this.sumDeliveryRecord = sumDeliveryRecord;
	}

	@Override
	public int compareTo(DeliveryResultInsTypeSummary obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.prodCode, obj.prodCode).append(this.insType, obj.insType).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && DeliveryResultInsTypeSummary.class.isAssignableFrom(entry.getClass())) {
			DeliveryResultInsTypeSummary obj = (DeliveryResultInsTypeSummary) entry;
			return new EqualsBuilder().append(this.prodCode, obj.prodCode).append(this.insType, obj.insType).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.prodCode).append(this.insType).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
