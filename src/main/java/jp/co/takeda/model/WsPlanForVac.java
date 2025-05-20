package jp.co.takeda.model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpModel;
// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
import jp.co.takeda.model.div.KaBaseKb;
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする

/**
 * (ワクチン)特約店別計画を表すモデルクラス
 *
 * @author tkawabata
 */
public class WsPlanForVac extends DpModel<WsPlanForVac> {

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
//	private KaBaseKbForVac kaBaseKb;
// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	private KaBaseKb kaBaseKb;
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする

	/**
	 * 計画値
	 */
	private Long plannedValue;

	/**
	 * 施設積上
	 */
	private Long insStackValue;

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
	 * 施設積上を取得する。
	 *
	 * @return 施設積上
	 */
	public Long getInsStackValue() {
		return insStackValue;
	}

	/**
	 * 施設積上を設定する。
	 *
	 * @param insStackValue 施設積上
	 */
	public void setInsStackValue(Long insStackValue) {
		this.insStackValue = insStackValue;
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
	public int compareTo(WsPlanForVac obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.tmsTytenCd, obj.tmsTytenCd).append(this.prodCode, obj.prodCode).append(this.sosCd, obj.sosCd).append(this.kaBaseKb,
				obj.kaBaseKb).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && WsPlanForVac.class.isAssignableFrom(entry.getClass())) {
			WsPlanForVac obj = (WsPlanForVac) entry;
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
