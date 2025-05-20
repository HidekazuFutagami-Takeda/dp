package jp.co.takeda.model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.div.InsType;

/**
 * 納入実績を表すモデルクラス
 *
 * @author tkawabata
 */
public class DeliveryResult extends DpModel<DeliveryResult> {

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
	 * TMS特約店コード
	 */
	private String tmsTytenCd;

	/**
	 * 品目固定コード
	 */
	private String prodCode;

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

	/**
	 * 施設名（Ref[施設情報].〔施設略式漢字名〕）
	 */
	private String insAbbrName;

	/**
	 * 依頼中フラグ（Ref[施設情報].〔依頼中フラグ〕）
	 */
	private Boolean reqFlg;

	/**
	 * 削除フラグ（Ref[施設情報].〔削除フラグ〕）
	 */
	private Boolean delFlg;

	/**
	 * TMS特約店名称（漢字）（Ref[特約店情報].〔TMS特約店名称漢字〕）
	 */
	private String tmsTytenMeiKj;

	/**
	 * 品目名称（Ref[計画対象品目].〔品目名称〕）
	 */
	private String prodName;

	/**
	 * 製品区分（Ref[計画対象品目].〔製品区分〕）
	 */
	private String prodType;

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

	/**
	 * 配分除外外フラグ
	 */
	private Boolean exceptFlg;

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
	 * 品目名称を取得する。
	 *
	 * @return 品目名称
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 品目名称を設定する。
	 *
	 * @param prodName 品目名称
	 */
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	/**
	 * 製品区分を取得する。
	 *
	 * @return 製品区分
	 */
	public String getProdType() {
		return prodType;
	}

	/**
	 * 製品区分を設定する。
	 *
	 * @param prodType 製品区分
	 */
	public void setProdType(String prodType) {
		this.prodType = prodType;
	}

	public Boolean getPlanTaiGaiFlgTok() {
		return planTaiGaiFlgTok;
	}

	public Boolean getPlanTaiGaiFlgRik() {
		return planTaiGaiFlgRik;
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

	public Boolean getExceptFlg() {
		return exceptFlg;
	}

	@Override
	public int compareTo(DeliveryResult obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.insNo, obj.insNo).append(this.tmsTytenCd, obj.tmsTytenCd).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && DeliveryResult.class.isAssignableFrom(entry.getClass())) {
			DeliveryResult obj = (DeliveryResult) entry;
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
