package jp.co.takeda.model;

import jp.co.takeda.bean.DpManageModel;
import jp.co.takeda.model.div.InsType;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 調整額（全社調整）を表すモデルクラス
 * 
 * @author khashimoto
 */
public class ChoseiIyaku extends DpManageModel<ChoseiIyaku> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 施設出力対象区分
	 */
	private InsType insType;

	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 調整額（Y価）
	 */
	private Long choseiValueY;

	/**
	 * 調整額（T価）
	 */
	private Long choseiValueT;

	//---------------------
	// Getter & Setter
	// --------------------
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
	 * 調整額（Y価）を取得する。
	 * 
	 * @return 調整額（Y価）
	 */
	public Long getChoseiValueY() {
		return choseiValueY;
	}

	/**
	 * 調整額（Y価）を設定する。
	 * 
	 * @param choseiValueY 調整額（Y価）
	 */
	public void setChoseiValueY(Long choseiValueY) {
		this.choseiValueY = choseiValueY;
	}

	/**
	 * 調整額（T価）を取得する。
	 * 
	 * @return 調整額（T価）
	 */
	public Long getChoseiValueT() {
		return choseiValueT;
	}

	/**
	 * 調整額（T価）を設定する。
	 * 
	 * @param choseiValueT 調整額（T価）
	 */
	public void setChoseiValueT(Long choseiValueT) {
		this.choseiValueT = choseiValueT;
	}

	@Override
	public int compareTo(ChoseiIyaku obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.insType, obj.insType).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && ChoseiIyaku.class.isAssignableFrom(entry.getClass())) {
			ChoseiIyaku obj = (ChoseiIyaku) entry;
			return new EqualsBuilder().append(this.insType, obj.insType).append(this.prodCode, obj.prodCode).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.insType).append(this.prodCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
