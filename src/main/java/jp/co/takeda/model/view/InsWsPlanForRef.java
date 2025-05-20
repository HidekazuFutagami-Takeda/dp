package jp.co.takeda.model.view;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.ProdInsInfoKanri;
import jp.co.takeda.model.div.HoInsType;
import jp.co.takeda.model.div.InsClass;
import jp.co.takeda.model.div.OldInsrFlg;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 参照用の施設特約店別計画を表すモデルクラス
 *
 * @author tkawabata
 */
public class InsWsPlanForRef extends DpModel<InsWsPlanForRef> {

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
	 * 配分値(Y)
	 */
	private Long distValueY;

	/**
	 * 修正値(Y)
	 */
	private Long modifyValueY;

	/**
	 * 確定値(Y)
	 */
	private Long plannedValueY;

	/**
	 * 改定前計画値(Y)
	 */
	private Long befPlannedValueY;

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
	 * 施設名（Ref[施設情報].〔施設略式漢字名〕）
	 */
	private String insAbbrName;

	/**
	 * 施設内部コード（Ref[施設情報].〔施設内部コード〕）
	 */
	private String relnInsNo;

	/**
	 * 施設分類（Ref[施設情報].〔施設分類〕）
	 */
	private InsClass insClass;

	/**
	 * サブコード分類（Ref[施設情報].〔サブコード分類〕）
	 */
	private OldInsrFlg oldInsrFlg;

	/**
	 * 対象区分（Ref[施設情報].〔対象区分〕）
	 */
	private HoInsType hoInsType;

	/**
	 * TMS特約店名称（Ref[特約店情報].〔TMS特約店名称漢字〕）
	 */
	private String tmsTytenName;

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
	 * 従業員名（Ref[従業員情報].〔氏名〕）
	 */
	private String jgiName;

	/**
	 * チームコード（Ref[組織情報].〔医薬チームＣ〕）
	 */
	private String teamCode;

	/**
	 * チーム名（Ref[組織情報].〔部門名略式〕）
	 */
	private String teamName;

	/**
	 * 計画立案対象外フラグ(当期用)
	 */
	private Boolean planTaiGaiFlgTok;

	/**
	 * 計画立案対象外フラグ(来期用)
	 */
	private Boolean planTaiGaiFlgRik;

	/**
	 * 品目施設情報
	 */
	private ProdInsInfoKanri prodIns;

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
	 * 配分値(Y)を取得する。
	 *
	 * @return 配分値(Y)
	 */
	public Long getDistValueY() {
		return distValueY;
	}

	/**
	 * 配分値(Y)を設定する。
	 *
	 * @param distValueY 配分値(Y)
	 */
	public void setDistValueY(Long distValueY) {
		this.distValueY = distValueY;
	}

	/**
	 * 修正値(Y)を取得する。
	 *
	 * @return 修正値(Y)
	 */
	public Long getModifyValueY() {
		return modifyValueY;
	}

	/**
	 * 修正値(Y)を設定する。
	 *
	 * @param modifyValueY 修正値(Y)
	 */
	public void setModifyValueY(Long modifyValueY) {
		this.modifyValueY = modifyValueY;
	}

	/**
	 * 確定値(Y)を取得する。
	 *
	 * @return 確定値(Y)
	 */
	public Long getPlannedValueY() {
		return plannedValueY;
	}

	/**
	 * 確定値(Y)を設定する。
	 *
	 * @param plannedValueY 確定値(Y)
	 */
	public void setPlannedValueY(Long plannedValueY) {
		this.plannedValueY = plannedValueY;
	}

	/**
	 * 改定前計画値(Y)を取得する。
	 *
	 * @return 改定前計画値(Y)
	 */
	public Long getBefPlannedValueY() {
		return befPlannedValueY;
	}

	/**
	 * 改定前計画値(Y)を設定する。
	 *
	 * @param befPlannedValueY 改定前計画値(Y)
	 */
	public void setBefPlannedValueY(Long befPlannedValueY) {
		this.befPlannedValueY = befPlannedValueY;
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
	 * 対象区分を取得する。
	 *
	 * @return hoInsType 対象区分
	 */
	public HoInsType getHoInsType() {
		return hoInsType;
	}

	/**
	 * 対象区分を設定する。
	 *
	 * @param hoInsType 対象区分
	 */
	public void setHoInsType(HoInsType hoInsType) {
		this.hoInsType = hoInsType;
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

	/**
	 * 従業員名を取得する。
	 *
	 * @return jgiName 従業員名
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * 従業員名を設定する。
	 *
	 * @param jgiName 従業員名
	 */
	public void setJgiName(String jgiName) {
		this.jgiName = jgiName;
	}

	/**
	 * チームコードを取得する。
	 *
	 * @return teamCode チームコード
	 */
	public String getTeamCode() {
		return teamCode;
	}

	/**
	 * チームコードを設定する。
	 *
	 * @param teamCd チームコード
	 */
	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}

	/**
	 * チーム名を取得する。
	 *
	 * @return teamName チーム名
	 */
	public String getTeamName() {
		return teamName;
	}

	/**
	 * チーム名を設定する。
	 *
	 * @param teamName チーム名
	 */
	public void setTeamName(String teamName) {
		this.teamName = teamName;
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
	 * @param planTaiGaiFlgTok 計画立案対象外フラグ(来期用)
	 */
	public void setPlanTaiGaiFlgRik(Boolean planTaiGaiFlgRik) {
		this.planTaiGaiFlgRik = planTaiGaiFlgRik;
	}

	/**
	 * 品目施設情報を取得する。
	 *
	 * @return 品目施設情報
	 */
	public ProdInsInfoKanri getProdIns() {
		if (prodIns == null) {
			prodIns = new ProdInsInfoKanri();
		}
		return prodIns;
	}

	/**
	 * 品目施設情報を設定する。
	 *
	 * @param prodIns 品目施設情報
	 */
	public void setProdIns(ProdInsInfoKanri prodIns) {
		this.prodIns = prodIns;
	}

	@Override
	public int compareTo(InsWsPlanForRef obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.insNo, obj.insNo).append(this.tmsTytenCd, obj.tmsTytenCd).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && InsWsPlanForRef.class.isAssignableFrom(entry.getClass())) {
			InsWsPlanForRef obj = (InsWsPlanForRef) entry;
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
