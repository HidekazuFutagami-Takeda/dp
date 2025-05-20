package jp.co.takeda.model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.InsClass;
import jp.co.takeda.model.div.OldInsrFlg;
import jp.co.takeda.model.div.PlanType;
import jp.co.takeda.model.div.Prefecture;

/**
 * (ワクチン用)特定施設個別計画を表すモデルクラス
 *
 * @author khashimoto
 */
public class SpecialInsPlanForVac extends DpModel<SpecialInsPlanForVac> {

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
	 * 計画立案区分
	 */
	private PlanType planType;

	/**
	 * 計画値(B)
	 */
	private Long plannedValueB;

	/**
	 * 計画値(B2)
	 */
	private Long plannedValueB2;

	/**
	 * 氏名（Ref[従業員情報].〔氏名〕）
	 */
	private String jgiName;

	/**
	 * 施設内部コード（Ref[施設情報].〔施設内部コード〕）
	 */
	private String relnInsNo;

	/**
	 * 施設分類（Ref[施設情報].〔対象分類〕）
	 */
	private InsClass insClass;

	/**
	 * サブコード分類（Ref[施設情報].〔サブコード分類〕）
	 */
	private OldInsrFlg oldInsrFlg;

	/**
	 * 施設名（Ref[施設情報].〔施設略式漢字名〕）
	 */
	private String insAbbrName;

	/**
	 * JIS府県コード（Ref[施設情報].〔JIS府県コード〕）
	 */
	private Prefecture addrCodePref;

	/**
	 * JIS市区町村コード（Ref[施設情報].〔JIS市区町村コード〕）
	 */
	private String addrCodeCity;

	/**
	 * 依頼中フラグ（Ref[施設情報].〔依頼中フラグ〕）
	 */
	private Boolean reqFlg;

	/**
	 * 削除フラグ（Ref[施設情報].〔削除フラグ〕）
	 */
	private Boolean delFlg;

	/**
	 * TMS特約店名称（Ref[特約店情報].〔TMS特約店名称漢字〕）
	 */
	private String tmsTytenName;

	/**
	 * 計画立案対象外フラグ(当期用)
	 */
	private Boolean planTaiGaiFlgTok;

	/**
	 * 計画立案対象外フラグ(来期用)
	 */
	private Boolean planTaiGaiFlgRik;

	/**
	 * 活動区分
	 */
	private ActivityType activityType;

	/**
	 * 品目共通情報
	 */
	private ProdInfo prodInfo;

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
	 * 計画立案区分を取得する。
	 *
	 * @return 計画立案区分
	 */
	public PlanType getPlanType() {
		return planType;
	}

	/**
	 * 計画立案区分を設定する。
	 *
	 * @param planType 計画立案区分
	 */
	public void setPlanType(PlanType planType) {
		this.planType = planType;
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
	 * 計画値(B2)を取得する。
	 *
	 * @return 計画値(B2)
	 */
	public Long getPlannedValueB2() {
		return plannedValueB2;
	}

	/**
	 * 計画値(B2)を設定する。
	 *
	 * @param plannedValueB 計画値(B2)
	 */
	public void setPlannedValueB2(Long plannedValueB2) {
		this.plannedValueB2 = plannedValueB2;
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
	 * 施設内部コードを取得する。
	 *
	 * @return 施設内部コード
	 */
	public String getRelnInsNo() {
		return relnInsNo;
	}

	/**
	 * 施設内部コードを設定する。
	 *
	 * @param relnInsNo 施設内部コード
	 */
	public void setRelnInsNo(String relnInsNo) {
		this.relnInsNo = relnInsNo;
	}

	/**
	 * 施設分類を取得する。
	 *
	 * @return 施設分類
	 */
	public InsClass getInsClass() {
		return insClass;
	}

	/**
	 * 施設分類を設定する。
	 *
	 * @param insClass 施設分類
	 */
	public void setInsClass(InsClass insClass) {
		this.insClass = insClass;
	}

	/**
	 * サブコード分類を取得する。
	 *
	 * @return サブコード分類
	 */
	public OldInsrFlg getOldInsrFlg() {
		return oldInsrFlg;
	}

	/**
	 * サブコード分類を設定する。
	 *
	 * @param oldInsrFlg サブコード分類
	 */
	public void setOldInsrFlg(OldInsrFlg oldInsrFlg) {
		this.oldInsrFlg = oldInsrFlg;
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
	 * 削除フラグを取得する。
	 *
	 * @return 削除フラグ
	 */
	public Boolean getDelFlg() {
		return delFlg;
	}

	/**
	 * 削除フラグを設定する。
	 *
	 * @param delFlg 削除フラグ
	 */
	public void setDelFlg(Boolean delFlg) {
		this.delFlg = delFlg;
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

	/**
	 * 計画立案対象外フラグ(当期用)を取得する。
	 *
	 * @return 計画立案対象外フラグ(当期用)
	 */
	public Boolean getPlanTaiGaiFlgTok() {
		return planTaiGaiFlgTok;
	}

	/**
	 * 計画立案対象外フラグ(当期用)を設定する。
	 *
	 * @param planTaiGaiFlgTok 計画立案対象外フラグ(当期用)
	 */
	public void setPlanTaiGaiFlgTok(Boolean planTaiGaiFlgTok) {
		this.planTaiGaiFlgTok = planTaiGaiFlgTok;
	}

	/**
	 * 計画立案対象外フラグ(来期用)を取得する。
	 *
	 * @return 計画立案対象外フラグ(来期用)
	 */
	public Boolean getPlanTaiGaiFlgRik() {
		return planTaiGaiFlgRik;
	}

	/**
	 * 計画立案対象外フラグ(来期用)を設定する。
	 *
	 * @param planTaiGaiFlgRik 計画立案対象外フラグ(来期用)
	 */
	public void setPlanTaiGaiFlgRik(Boolean planTaiGaiFlgRik) {
		this.planTaiGaiFlgRik = planTaiGaiFlgRik;
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
	 * 品目共通情報を取得する。
	 *
	 * @return 品目共通情報
	 */
	public ProdInfo getProdInfo() {
		return prodInfo;
	}

	/**
	 * 品目共通情報を設定する。
	 *
	 * @param prodInfo 品目共通情報
	 */
	public void setProdInfo(ProdInfo prodInfo) {
		this.prodInfo = prodInfo;
	}

	@Override
	public int compareTo(SpecialInsPlanForVac obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.insNo, obj.insNo).append(this.tmsTytenCd, obj.tmsTytenCd).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && SpecialInsPlanForVac.class.isAssignableFrom(entry.getClass())) {
			SpecialInsPlanForVac obj = (SpecialInsPlanForVac) entry;
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
