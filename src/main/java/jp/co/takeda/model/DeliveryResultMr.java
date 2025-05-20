package jp.co.takeda.model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.div.InsType;

/**
 * 担当者単位の納入実績（サマリー）を表すモデルクラス
 *
 * @author tkawabata
 */
public class DeliveryResultMr extends DpModel<DeliveryResultMr> {

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

	//add Start 2022/5/11 H.Futagami バックログNo.13　過去実績参照で選択している品目を一覧に表示する
	/**
	 * 品目
	 */
	private String prodName;
	//add End 2022/5/11 H.Futagami バックログNo.13　過去実績参照で選択している品目を一覧に表示する

	/**
	 * 施設出力対象区分
	 */
	private InsType insType;

	/**
	 * 雑担当フラグ
	 */
	private Boolean sloppyChargeFlg;

	/**
	 * 納入実績
	 */
	private MonNnu monNnu;

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
	 * @param sosCd 従業員番号
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

	//add Start 2022/5/11 H.Futagami バックログNo.13　過去実績参照で選択している品目を一覧に表示する
	/**
	 * 品目を取得する。
	 *
	 * @return 品目
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 品目を設定する。
	 *
	 * @param prodName 品目
	 */
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	//add End 2022/5/11 H.Futagami バックログNo.13　過去実績参照で選択している品目を一覧に表示する

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
	 * 雑担当フラグを取得する。
	 *
	 * @return 雑担当フラグ
	 */
	public Boolean getSloppyChargeFlg() {
		return sloppyChargeFlg;
	}

	/**
	 * 雑担当フラグを設定する。
	 *
	 * @param sloppyChargeFlg 雑担当フラグ
	 */
	public void setSloppyChargeFlg(Boolean sloppyChargeFlg) {
		this.sloppyChargeFlg = sloppyChargeFlg;
	}

	/**
	 * 納入実績を取得する。
	 *
	 * @return 納入実績
	 */
	public MonNnu getMonNnu() {
		if (monNnu == null) {
			monNnu = new MonNnu();
		}
		return monNnu;
	}

	/**
	 * 納入実績を設定する。
	 *
	 * @param monNnu 納入実績
	 */
	public void setMonNnu(MonNnu monNnu) {
		this.monNnu = monNnu;
	}

	@Override
	public int compareTo(DeliveryResultMr obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.jgiNo, obj.jgiNo).append(this.insType, obj.insType).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && DeliveryResultMr.class.isAssignableFrom(entry.getClass())) {
			DeliveryResultMr obj = (DeliveryResultMr) entry;
			return new EqualsBuilder().append(this.jgiNo, obj.jgiNo).append(this.insType, obj.insType).append(this.prodCode, obj.prodCode).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.jgiNo).append(this.insType).append(this.prodCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
