package jp.co.takeda.model.view;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.Prefecture;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 参照用の(ワクチン)施設特約店別計画を表すモデルクラス
 * 
 * @author khashimoto
 */
public class InsWsPlanForVacForRef extends DpModel<InsWsPlanForVacForRef> {

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
	 * TMS特約店コード
	 */
	private String tmsTytenCd;

	/**
	 * 配分値(B)
	 */
	private Long distValueB;

	// add Start 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる
	/**
	 * 修正値(B)
	 */
	private Long modifyValueB;
	// add End 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる

	/**
	 * 計画値(B)
	 */
	private Long plannedValueB;

	/**
	 * 改定後計画値(B)
	 */
	private Long befPlannedValueB;

	/**
	 * 特定施設個別計画かを示すフラグ
	 */
	private Boolean specialInsPlanFlg;

	/**
	 * 配分除外施設かを示すフラグ
	 */
	private Boolean exceptDistInsFlg;

	/**
	 * 削除施設かを示すフラグ
	 */
	private Boolean delInsFlg;

	/**
	 * 活動区分
	 */
	private ActivityType activityType;

	/**
	 * 施設名（Ref[施設情報].〔施設略式漢字名〕）
	 */
	private String insAbbrName;

	/**
	 * TMS特約店名称（Ref[特約店情報].〔TMS特約店名称漢字〕）
	 */
	private String tmsTytenName;

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
	 * 立案品目の納入実績
	 */
	private MonNnuSummary monNnuSummary;

	/**
	 * 実績参照品目1の納入実績
	 */
	private MonNnuSummary monNnuSummary1;

	/**
	 * 実績参照品目2の納入実績
	 */
	private MonNnuSummary monNnuSummary2;

	/**
	 * 実績参照品目3の納入実績
	 */
	private MonNnuSummary monNnuSummary3;

	/**
	 * 計画立案対象外フラグ(当期用)
	 */
	private Boolean planTaiGaiFlgTok;

	/**
	 * 計画立案対象外フラグ(来期用)
	 */
	private Boolean planTaiGaiFlgRik;
	
	
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
	 * 配分値(B)を取得する。
	 * 
	 * @return 配分値(B)
	 */
	public Long getDistValueB() {
		return distValueB;
	}

	/**
	 * 配分値(B)を設定する。
	 * 
	 * @param distValueB 配分値(B)
	 */
	public void setDistValueB(Long distValueB) {
		this.distValueB = distValueB;
	}

	// add Start 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる
	/**
	 * 修正値(B)を取得する。
	 *
	 * @return 修正値(B)
	 */
	public Long getModifyValueB() {
		return modifyValueB;
	}

	/**
	 * 修正値(B)を設定する。
	 *
	 * @param modifyValueB 修正値(B)
	 */
	public void setModifyValueB(Long modifyValueB) {
		this.modifyValueB = modifyValueB;
	}
	// add End 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる

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
	 * 改定前計画値(B)を取得する。
	 * 
	 * @return 改定前計画値(B)
	 */
	public Long getBefPlannedValueB() {
		return befPlannedValueB;
	}

	/**
	 * 改定前計画値(B)を設定する。
	 * 
	 * @param befPlannedValueB 改定前計画値(B)
	 */
	public void setBefPlannedValueB(Long befPlannedValueB) {
		this.befPlannedValueB = befPlannedValueB;
	}

	/**
	 * 特定施設個別計画かを示すフラグを取得する。
	 * 
	 * @return 特定施設個別計画かを示すフラグ
	 */
	public Boolean getSpecialInsPlanFlg() {
		return specialInsPlanFlg;
	}

	/**
	 * 特定施設個別計画かを示すフラグを設定する。
	 * 
	 * @param specialInsPlanFlg 特定施設個別計画かを示すフラグ
	 */
	public void setSpecialInsPlanFlg(Boolean specialInsPlanFlg) {
		this.specialInsPlanFlg = specialInsPlanFlg;
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
	 * 立案品目の納入実績を取得する。
	 * 
	 * @return 立案品目の納入実績
	 */
	public MonNnuSummary getMonNnuSummary() {
		if (monNnuSummary == null) {
			monNnuSummary = new MonNnuSummary();
		}
		return monNnuSummary;
	}

	/**
	 * 立案品目の納入実績を設定する。
	 * 
	 * @param monNnuSummary 立案品目の納入実績
	 */
	public void setMonNnuSummary(MonNnuSummary monNnuSummary) {
		this.monNnuSummary = monNnuSummary;
	}

	/**
	 * 実績参照品目1の納入実績を取得する。
	 * 
	 * @return 実績参照品目1の納入実績
	 */
	public MonNnuSummary getMonNnuSummary1() {
		if (monNnuSummary1 == null) {
			monNnuSummary1 = new MonNnuSummary();
		}
		return monNnuSummary1;
	}

	/**
	 * 実績参照品目1の納入実績を設定する。
	 * 
	 * @param monNnuSummary1 実績参照品目1の納入実績
	 */
	public void setMonNnuSummary1(MonNnuSummary monNnuSummary1) {
		this.monNnuSummary1 = monNnuSummary1;
	}

	/**
	 * 実績参照品目2の納入実績を取得する。
	 * 
	 * @return 実績参照品目2の納入実績
	 */
	public MonNnuSummary getMonNnuSummary2() {
		if (monNnuSummary2 == null) {
			monNnuSummary2 = new MonNnuSummary();
		}
		return monNnuSummary2;
	}

	/**
	 * 実績参照品目2の納入実績を設定する。
	 * 
	 * @param monNnuSummary2 実績参照品目2の納入実績
	 */
	public void setMonNnuSummary2(MonNnuSummary monNnuSummary2) {
		this.monNnuSummary2 = monNnuSummary2;
	}

	/**
	 * 実績参照品目3の納入実績を取得する。
	 * 
	 * @return monNnuSummary3 実績参照品目3の納入実績
	 */
	public MonNnuSummary getMonNnuSummary3() {
		if (monNnuSummary3 == null) {
			monNnuSummary3 = new MonNnuSummary();
		}
		return monNnuSummary3;
	}

	/**
	 * 実績参照品目3の納入実績を設定する。
	 * 
	 * @param monNnuSummary3 実績参照品目3の納入実績
	 */
	public void setMonNnuSummary3(MonNnuSummary monNnuSummary3) {
		this.monNnuSummary3 = monNnuSummary3;
	}

	public Boolean getPlanTaiGaiFlgTok() {
		return planTaiGaiFlgTok;
	}

	public void setPlanTaiGaiFlgTok(Boolean planTaiGaiFlgTok) {
		this.planTaiGaiFlgTok = planTaiGaiFlgTok;
	}

	public Boolean getPlanTaiGaiFlgRik() {
		return planTaiGaiFlgRik;
	}

	public void setPlanTaiGaiFlgRik(Boolean planTaiGaiFlgRik) {
		this.planTaiGaiFlgRik = planTaiGaiFlgRik;
	}

	@Override
	public int compareTo(InsWsPlanForVacForRef obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.insNo, obj.insNo).append(this.tmsTytenCd, obj.tmsTytenCd).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && InsWsPlanForVacForRef.class.isAssignableFrom(entry.getClass())) {
			InsWsPlanForVacForRef obj = (InsWsPlanForVacForRef) entry;
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
