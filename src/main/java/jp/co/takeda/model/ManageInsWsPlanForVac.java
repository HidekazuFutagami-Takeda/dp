package jp.co.takeda.model;

import jp.co.takeda.bean.DpManageModel;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.Prefecture;
import jp.co.takeda.model.div.TradeType;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 管理時のワクチン用施設特約店別計画を表すモデルクラス
 * 
 * @author khashimoto
 */
public class ManageInsWsPlanForVac extends DpManageModel<ManageInsWsPlanForVac> {

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
	 * 施設コード
	 */
	private String insNo;

	/**
	 * TMS特約店コード
	 */
	private String tmsTytenCd;

	/**
	 * ワクチン用実施計画
	 */
	private ImplPlanForVac implPlanForVac;

	/**
	 * 品目名称（Ref[管理時の計画対象品目].〔品目名称〕）
	 */
	private String prodName;

	/**
	 * 活動区分
	 */
	private ActivityType activityType;

	/**
	 * 施設名（Ref[施設情報].〔施設略式漢字名〕）
	 */
	private String insAbbrName;

	/**
	 * 取引区分（Ref[施設情報].〔取引区分〕）
	 */
	private TradeType tradeType;

	/**
	 * 依頼中フラグ（Ref[施設情報].〔依頼中フラグ〕）
	 */
	private Boolean reqFlg;

	/**
	 * 施設削除フラグ（Ref[施設情報].〔削除フラグ〕）
	 */
	private Boolean insDelFlg;

	/**
	 * JIS府県コード（Ref[施設情報].〔JIS府県コード〕）
	 */
	private Prefecture addrCodePref;

	/**
	 * JIS市区町村コード（Ref[施設情報].〔JIS市区町村コード〕）
	 */
	private String addrCodeCity;

	/**
	 * 市区郡町村名（漢字）（Ref[JIS府県・市区町村].〔市区郡町村名（漢字）〕）
	 */
	private String shikuchosonMeiKj;

	/**
	 * TMS特約店名称（Ref[特約店情報].〔TMS特約店名称漢字〕）
	 */
	private String tmsTytenName;

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
	 * ワクチン用実施計画を取得する。
	 * 
	 * @return ワクチン用実施計画
	 */
	public ImplPlanForVac getImplPlanForVac() {
		if (implPlanForVac == null) {
			implPlanForVac = new ImplPlanForVac();
		}
		return implPlanForVac;
	}

	/**
	 * ワクチン用実施計画を設定する。
	 * 
	 * @param implPlanForVac ワクチン用実施計画
	 */
	public void setImplPlanForVac(ImplPlanForVac implPlanForVac) {
		this.implPlanForVac = implPlanForVac;
	}

	/**
	 * 品目名称(漢字)を取得する。
	 * 
	 * @return 品目名称(漢字)
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 品目名称(漢字)を設定する。
	 * 
	 * @param prodName 品目名称(漢字)
	 */
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	/**
	 * 活動区分を取得する。
	 * 
	 * @return 活動区分
	 */
	public ActivityType getActivityType() {
		return activityType;
	}

	/**
	 * 活動区分を設定する。
	 * 
	 * @param activityType 活動区分
	 */
	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	/**
	 * 施設略式漢字名を取得する。
	 * 
	 * @return 施設略式漢字名
	 */
	public String getInsAbbrName() {
		return insAbbrName;
	}

	/**
	 * 施設略式漢字名を設定する。
	 * 
	 * @param insAbbrName 施設略式漢字名
	 */
	public void setInsAbbrName(String insAbbrName) {
		this.insAbbrName = insAbbrName;
	}

	/**
	 * 取引区分を取得する。
	 * 
	 * @return 取引区分
	 */
	public TradeType getTradeType() {
		return tradeType;
	}

	/**
	 * 取引区分を設定する。
	 * 
	 * @param tradeType 取引区分
	 */
	public void setTradeType(TradeType tradeType) {
		this.tradeType = tradeType;
	}

	/**
	 * 依頼中フラグを取得する。
	 * 
	 * @return 依頼中フラグ
	 */
	public Boolean getReqFlg() {
		return reqFlg;
	}

	/**
	 * 依頼中フラグを設定する。
	 * 
	 * @param reqFlg 依頼中フラグ
	 */
	public void setReqFlg(Boolean reqFlg) {
		this.reqFlg = reqFlg;
	}

	/**
	 * 施設削除フラグを取得する。
	 * 
	 * @return 施設削除フラグ
	 */
	public Boolean getInsDelFlg() {
		return insDelFlg;
	}

	/**
	 * 施設削除フラグを設定する。
	 * 
	 * @param insDelFlg 施設削除フラグ
	 */
	public void setInsDelFlg(Boolean insDelFlg) {
		this.insDelFlg = insDelFlg;
	}

	/**
	 * JIS府県コードを取得する。
	 * 
	 * @return addrCodePref JIS府県コード
	 */
	public Prefecture getAddrCodePref() {
		return addrCodePref;
	}

	/**
	 * JIS府県コードを設定する。
	 * 
	 * @param addrCodePref JIS府県コード
	 */
	public void setAddrCodePref(Prefecture addrCodePref) {
		this.addrCodePref = addrCodePref;
	}

	/**
	 * JIS市区町村コードを取得する。
	 * 
	 * @return JIS市区町村コード
	 */
	public String getAddrCodeCity() {
		return addrCodeCity;
	}

	/**
	 * JIS市区町村コードを設定する。
	 * 
	 * @param addrCodeCity JIS市区町村コード
	 */
	public void setAddrCodeCity(String addrCodeCity) {
		this.addrCodeCity = addrCodeCity;
	}

	/**
	 * 市区郡町村名（漢字）を取得する。
	 * 
	 * @return 市区郡町村名（漢字）
	 */
	public String getShikuchosonMeiKj() {
		return shikuchosonMeiKj;
	}

	/**
	 * 市区郡町村名（漢字）を設定する。
	 * 
	 * @param shikuchosonMeiKj 市区郡町村名（漢字）
	 */
	public void setShikuchosonMeiKj(String shikuchosonMeiKj) {
		this.shikuchosonMeiKj = shikuchosonMeiKj;
	}

	/**
	 * TMS特約店名称を取得する。
	 * 
	 * @return TMS特約店名称
	 */
	public String getTmsTytenName() {
		return tmsTytenName;
	}

	/**
	 * TMS特約店名称を設定する。
	 * 
	 * @param tmsTytenName TMS特約店名称
	 */
	public void setTmsTytenName(String tmsTytenName) {
		this.tmsTytenName = tmsTytenName;
	}

	@Override
	public int compareTo(ManageInsWsPlanForVac obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.insNo, obj.insNo).append(this.tmsTytenCd, obj.tmsTytenCd).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && ManageInsWsPlanForVac.class.isAssignableFrom(entry.getClass())) {
			ManageInsWsPlanForVac obj = (ManageInsWsPlanForVac) entry;
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
