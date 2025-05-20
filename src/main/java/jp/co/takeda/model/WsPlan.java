package jp.co.takeda.model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.div.KaBaseKb;

/**
 * 特約店別計画を表すモデルクラス
 *
 * @author tkawabata
 */
public class WsPlan extends DpModel<WsPlan> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * TMS特約店コード
	 */
	private String tmsTytenCd;

	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 組織コード
	 */
	private String sosCd;

	/**
	 * 価ベース区分
	 */
	private KaBaseKb kaBaseKb;

	/**
	 * 配分値UH
	 */
	private Long distValueUh;

	/**
	 * 積上値UH
	 */
	private Long stackValueUh;

	/**
	 * 計画値UH
	 */
	private Long plannedValueUh;

	/**
	 * 配分値P
	 */
	private Long distValueP;

	/**
	 * 積上値P
	 */
	private Long stackValueP;

	/**
	 * 計画値P
	 */
	private Long plannedValueP;

	/**
	 * 部門名正式（Ref[組織情報].〔部門名正式〕）<br>
	 * (該当階層の組織名)
	 */
	private String bumonSeiName;

	/**
	 * 支店名漢字
	 */
	private String shitenMeiKj;

	/**
	 * TMS特約店名称(漢字)
	 */
	private String tmsTytenMeiKj;

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
	 * 価ベース区分を取得する。
	 *
	 * @return 価ベース区分
	 */
	public KaBaseKb getKaBaseKb() {
		return kaBaseKb;
	}

	/**
	 * 価ベース区分を設定する。
	 *
	 * @param kaBaseKb 価ベース区分
	 */
	public void setKaBaseKb(KaBaseKb kaBaseKb) {
		this.kaBaseKb = kaBaseKb;
	}

	/**
	 * 配分値UHを取得する。
	 *
	 * @return 配分値UH
	 */
	public Long getDistValueUh() {
		return distValueUh;
	}

	/**
	 * 配分値UHを設定する。
	 *
	 * @param distValueUh 配分値UH
	 */
	public void setDistValueUh(Long distValueUh) {
		this.distValueUh = distValueUh;
	}

	/**
	 * 積上値UHを取得する。
	 *
	 * @return 積上値UH
	 */
	public Long getStackValueUh() {
		return stackValueUh;
	}

	/**
	 * 積上値UHを設定する。
	 *
	 * @param stackValueUh 積上値UH
	 */
	public void setStackValueUh(Long stackValueUh) {
		this.stackValueUh = stackValueUh;
	}

	/**
	 * 計画値UHを取得する。
	 *
	 * @return 計画値UH
	 */
	public Long getPlannedValueUh() {
		return plannedValueUh;
	}

	/**
	 * 計画値UHを設定する。
	 *
	 * @param plannedValueUh 計画値UH
	 */
	public void setPlannedValueUh(Long plannedValueUh) {
		this.plannedValueUh = plannedValueUh;
	}

	/**
	 * 配分値Pを取得する。
	 *
	 * @return 配分値P
	 */
	public Long getDistValueP() {
		return distValueP;
	}

	/**
	 * 配分値Pを設定する。
	 *
	 * @param distValueP 配分値P
	 */
	public void setDistValueP(Long distValueP) {
		this.distValueP = distValueP;
	}

	/**
	 * 積上値Pを取得する。
	 *
	 * @return 積上値P
	 */
	public Long getStackValueP() {
		return stackValueP;
	}

	/**
	 * 積上値Pを設定する。
	 *
	 * @param stackValueP 積上値P
	 */
	public void setStackValueP(Long stackValueP) {
		this.stackValueP = stackValueP;
	}

	/**
	 * 計画値Pを取得する。
	 *
	 * @return 計画値P
	 */
	public Long getPlannedValueP() {
		return plannedValueP;
	}

	/**
	 * 計画値Pを設定する。
	 *
	 * @param plannedValueP 計画値P
	 */
	public void setPlannedValueP(Long plannedValueP) {
		this.plannedValueP = plannedValueP;
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
	 * 支店名漢字を取得する。
	 *
	 * @return 支店名漢字
	 */
	public String getShitenMeiKj() {
		return shitenMeiKj;
	}

	/**
	 * 支店名漢字を設定する。
	 *
	 * @param bumonSeiName 支店名漢字
	 */
	public void setShitenMeiKj(String shitenMeiKj) {
		this.shitenMeiKj = shitenMeiKj;
	}

	/**
	 * TMS特約店名称(漢字)を取得する。
	 * @return tmsTytenMeiKj
	 */
	public String getTmsTytenMeiKj() {
		return tmsTytenMeiKj;
	}

	/**
	 * TMS特約店名称(漢字)を設定する。
	 * @param tmsTytenMeiKj セットする tmsTytenMeiKj
	 */
	public void setTmsTytenMeiKj(String tmsTytenMeiKj) {
		this.tmsTytenMeiKj = tmsTytenMeiKj;
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

	@Override
	public int compareTo(WsPlan obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.tmsTytenCd, obj.tmsTytenCd).append(this.prodCode, obj.prodCode).append(this.sosCd, obj.sosCd).append(this.kaBaseKb,
				obj.kaBaseKb).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && WsPlan.class.isAssignableFrom(entry.getClass())) {
			WsPlan obj = (WsPlan) entry;
			return new EqualsBuilder().append(this.tmsTytenCd, obj.tmsTytenCd).append(this.prodCode, obj.prodCode).append(this.sosCd, obj.sosCd)
				.append(this.kaBaseKb, obj.kaBaseKb).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.tmsTytenCd).append(this.prodCode).append(this.sosCd).append(this.kaBaseKb).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
