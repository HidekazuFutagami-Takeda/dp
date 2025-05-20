package jp.co.takeda.model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.div.KaBaseKbForVac;
import jp.co.takeda.model.view.MonNnuSummary;

/**
 * (ワクチン)特約店別計画のサマリー情報を表すモデルクラス
 *
 * @author khashimoto
 */
public class WsPlanForVacSummary2 extends DpModel<WsPlanForVacSummary2> {

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
	private KaBaseKbForVac kaBaseKb;

	/**
	 * 計画値
	 */
	private Long plannedValue;

	/**
	 * 積上値
	 */
	private Long insStackValue;

	/**
	 * TMS特約店名称（漢字）
	 */
	private String tmsTytenMeiKj;

	/**
	 * 統計品名コード(品目)
	 */
	private String statProdCode;

	/**
	 * 品目名称
	 */
	private String prodName;

	/**
	 * 納入実績
	 */
	private MonNnuSummary monNnuSummary;


	/**
	 * 計画立案対象外フラグ(来期用)
	 */
	private String planTaiGaiFlgRik;

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
	public KaBaseKbForVac getKaBaseKb() {
		return kaBaseKb;
	}

	/**
	 * 価ベース区分を設定する。
	 *
	 * @param kaBaseKb 価ベース区分
	 */
	public void setKaBaseKb(KaBaseKbForVac kaBaseKb) {
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
	 * 積上値を取得する。
	 *
	 * @return 積上値
	 */
	public Long getInsStackValue() {
		return insStackValue;
	}

	/**
	 * 積上値を設定する。
	 *
	 * @param insStackValue 積上値
	 */
	public void setInsStackValue(Long insStackValue) {
		this.insStackValue = insStackValue;
	}

	/**
	 * TMS特約店名称（漢字）を取得する。
	 *
	 * @return TMS特約店名称（漢字）
	 */
	public String getTmsTytenMeiKj() {
		return tmsTytenMeiKj;
	}

	/**
	 * TMS特約店名称（漢字）を設定する。
	 *
	 * @param tmsTytenMeiKj TMS特約店名称（漢字）
	 */
	public void setTmsTytenMeiKj(String tmsTytenMeiKj) {
		this.tmsTytenMeiKj = tmsTytenMeiKj;
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
	 * 納入実績を取得する。
	 *
	 * @return 納入実績
	 */
	public MonNnuSummary getMonNnuSummary() {
		if (monNnuSummary == null) {
			monNnuSummary = new MonNnuSummary();
		}
		return monNnuSummary;
	}

	/**
	 * 納入実績を設定する。
	 *
	 * @param monNnuSummary 納入実績
	 */
	public void setMonNnuSummary(MonNnuSummary monNnuSummary) {
		this.monNnuSummary = monNnuSummary;
	}

	/**
	 * 計画立案対象外フラグ(来期用)を取得する。
	 *
	 * @return 計画立案対象外フラグ(来期用)
	 */
	public String getPlanTaiGaiFlgRik() {
		return planTaiGaiFlgRik;
	}

	/**
	 * 計画立案対象外フラグ(来期用)を設定する。
	 *
	 * @param planTaiGaiFlgRik 計画立案対象外フラグ(来期用)
	 */
	public void setPlanTaiGaiFlgRik(String planTaiGaiFlgRik) {
		Integer  i = Integer.parseInt(planTaiGaiFlgRik);
		if(i > 1) { // flagをsumした値が入ってくることがあるので1にする。
			i = 1;
		}
		String flag = i.toString();
		this.planTaiGaiFlgRik = flag;
	}

	@Override
	public int compareTo(WsPlanForVacSummary2 obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.tmsTytenCd, obj.tmsTytenCd).append(this.prodCode, obj.prodCode).append(this.kaBaseKb, obj.kaBaseKb).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && WsPlanForVacSummary2.class.isAssignableFrom(entry.getClass())) {
			WsPlanForVacSummary2 obj = (WsPlanForVacSummary2) entry;
			return new EqualsBuilder().append(this.tmsTytenCd, obj.tmsTytenCd).append(this.prodCode, obj.prodCode).append(this.kaBaseKb, obj.kaBaseKb).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.tmsTytenCd).append(this.prodCode).append(this.kaBaseKb).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
