package jp.co.takeda.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.view.AreaPersonInChargeAdjustment;

/**
 * 営業所計画アップロード用のフォーマットファイルのサマリー情報を表すモデルクラス
 *
 * @author kSuzuki
 */
public class CheckTool implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * リージョンコード
	 */
	private String upSosCd;

	/**
	 * リージョン名
	 */
	private String upBumonRyakuName;

	/**
	 * エリアコード
	 */
	private String sosCd;

	/**
	 * エリア名
	 */
	private String bumonRyakuName;

	/**
	 * 品目コード
	 */
	private String prodCode;

	/**
	 * 品目名
	 */
	private String prodName;

	/**
	 * MRコード
	 */
	private String mrNo;

	/**
	 * MR名
	 */
	private String jgiName;

	/**
	 * 対象(UHP)
	 */
	private String insType;

	/**
	 * TMS特約店コード
	 */
	private String tmsTytenCd;

	/**
	 * TMS特約店名称(漢字)
	 */
	private String tmsTytenMeiKj;

	/**
	 * 施設固定コード
	 */
	private String insNo;

	/**
	 * 施設名
	 */
	private String insAbbrName;

	/**
	 * Y価格計画値(千円)
	 */
	private long yBasePlanSen;
	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * リージョンコードを取得する。
	 *
	 * @return リージョンコード
	 */
	public String getUpSosCd() {
		return upSosCd;
	}

	/**
	 * リージョンコードを設定する。
	 *
	 * @param upSosCd リージョンコード
	 */
	public void setUpSosCd(String upSosCd) {
		this.upSosCd = upSosCd;
	}

	/**
	 * リージョン名を取得する。
	 *
	 * @return リージョン名
	 */
	public String getUpBumonRyakuName() {
		return upBumonRyakuName;
	}

	/**
	 * リージョン名を設定する。
	 *
	 * @param upBumonRyakuName リージョン名
	 */
	public void setUpBumonRyakuName(String upBumonRyakuName) {
		this.upBumonRyakuName = upBumonRyakuName;
	}

	/**
	 * エリアコードを取得する。
	 *
	 * @return エリアコード
	 */
	public String getSosCd() {
		return sosCd;
	}

	/**
	 * エリアコードを設定する。
	 *
	 * @param sosCd エリアコード
	 */
	public void setSosCd(String sosCd) {
		this.sosCd = sosCd;
	}

	/**
	 * エリア名を取得する。
	 *
	 * @return エリア名
	 */
	public String getBumonRyakuName() {
		return bumonRyakuName;
	}

	/**
	 * エリア名を設定する。
	 *
	 * @param bumonRyakuName エリア名
	 */
	public void setBumonRyakuName(String bumonRyakuName) {
		this.bumonRyakuName = bumonRyakuName;
	}

	/**
	 * 品目コードを取得する。
	 *
	 * @return 品目コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目コードを設定する。
	 *
	 * @param prodCode 品目コード
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * 品目名を取得する。
	 *
	 * @return 品目名
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 品目名を設定する。
	 *
	 * @param prodName 品目名
	 */
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	/**
	 * MRコードを取得する。
	 *
	 * @return MRコード
	 */
	public String getMrNo() {
		return mrNo;
	}

	/**
	 * MRコードを設定する。
	 *
	 * @param mrNo MRコード
	 */
	public void setMrNo(String mrNo) {
		this.mrNo = mrNo;
	}

	/**
	 * MR名を取得する。
	 *
	 * @return MR名
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * MR名を設定する。
	 *
	 * @param jgiName MR名
	 */
	public void setJgiName(String jgiName) {
		this.jgiName = jgiName;
	}

	/**
	 * 対象(UHP)を取得する。
	 *
	 * @return 対象(UHP)
	 */
	public String getInsType() {
		return insType;
	}

	/**
	 * 対象(UHP)を設定する。
	 *
	 * @param InsType 対象(UHP)
	 */
	public void setInsType(String insType) {
		this.insType = insType;
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
	 * TMS特約店名称(漢字)を取得する。
	 *
	 * @return TMS特約店名称(漢字)
	 */
	public String getTmsTytenMeiKj() {
		return tmsTytenMeiKj;
	}

	/**
	 * TMS特約店名称(漢字)を設定する。
	 *
	 * @param tmsTytenMeiKj TMS特約店名称(漢字)
	 */
	public void setTmsTytenMeiKj(String tmsTytenMeiKj) {
		this.tmsTytenMeiKj = tmsTytenMeiKj;
	}

	/**
	 * 施設固定コードを取得する。
	 *
	 * @return 施設固定コード
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 施設固定コードを設定する。
	 *
	 * @param insNo 施設固定コード
	 */
	public void setInsNo(String insNo) {
		this.insNo = insNo;
	}

	/**
	 * 施設名を取得する。
	 *
	 * @return 施設名
	 */
	public String getInsAbbrName() {
		return insAbbrName;
	}

	/**
	 * 施設名を設定する。
	 *
	 * @param insAbbrName 施設名
	 */
	public void setInsAbbrName(String insAbbrName) {
		this.insAbbrName = insAbbrName;
	}

	/**
	 * Y価格計画値(千円)を取得する。
	 *
	 * @return Y価格計画値(千円)
	 */
	public long getYBasePlanSen() {
		return yBasePlanSen;
	}

	/**
	 * Y価格計画値(千円)を設定する。
	 *
	 * @param yBasePlanSen Y価格計画値(千円)
	 */
	public void setYBasePlanSen(long yBasePlanSen) {
		this.yBasePlanSen = yBasePlanSen;
	}


}
