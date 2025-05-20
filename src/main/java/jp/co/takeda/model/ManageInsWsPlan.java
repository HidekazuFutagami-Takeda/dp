package jp.co.takeda.model;

import jp.co.takeda.bean.DpManageModel;
import jp.co.takeda.model.div.InsClass;
import jp.co.takeda.model.div.OldInsrFlg;
import jp.co.takeda.model.div.TradeType;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 管理時の施設特約店別計画を表すモデルクラス
 * 
 * @author khashimoto
 */
public class ManageInsWsPlan extends DpManageModel<ManageInsWsPlan> {

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
	 * 実施計画
	 */
	private ImplPlan implPlan;

	/**
	 * 品目名称（Ref[管理時の計画対象品目].〔品目名称〕）
	 */
	private String prodName;

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
	 * 削除フラグ（Ref[施設情報].〔削除フラグ〕）
	 */
	private Boolean insDelFlg;

	/**
	 * TMS特約店名称（Ref[特約店情報].〔TMS特約店名称漢字〕）
	 */
	private String tmsTytenName;

	/**
	 * 施設分類
	 */
	private InsClass insClass;

	/**
	 * サブコード分類
	 */
	private OldInsrFlg oldInsrFlg;

	/**
	 * 施設担当の従業員情報（Ref[MR施設品目またはMR施設].〔担当者番号〕に紐づく従業員情報）
	 */
	private JgiMst jgiMst;

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
	 * 実施計画を取得する。
	 * 
	 * @return 実施計画
	 */
	public ImplPlan getImplPlan() {
		if (implPlan == null) {
			implPlan = new ImplPlan();
		}
		return implPlan;
	}

	/**
	 * 実施計画を設定する。
	 * 
	 * @param implPlan 実施計画
	 */
	public void setImplPlan(ImplPlan implPlan) {
		this.implPlan = implPlan;
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
	 * 施設担当の従業員情報を取得する。
	 * 
	 * @return 施設担当の従業員情報
	 */
	public JgiMst getJgiMst() {
		return jgiMst;
	}

	/**
	 * 施設担当の従業員情報を設定する。
	 * 
	 * @param jgiMst 施設担当の従業員情報
	 */
	public void setJgiMst(JgiMst jgiMst) {
		this.jgiMst = jgiMst;
	}
	
	@Override
	public int compareTo(ManageInsWsPlan obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.insNo, obj.insNo).append(this.tmsTytenCd, obj.tmsTytenCd).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && ManageInsWsPlan.class.isAssignableFrom(entry.getClass())) {
			ManageInsWsPlan obj = (ManageInsWsPlan) entry;
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
