package jp.co.takeda.model.view;

import java.io.Serializable;

/**
 * 削除施設・施設特約店別計画一覧を表すモデルクラス
 *
 * @author hfutagami
 */
public class DeletedFacilityPersonInChargeplan  implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * リージョンＣ
	 */
	private String brCode;

	/**
	 * エリアＣ
	 */
	private String distCode;

	/**
	 * 従業員番号
	 */
	private Integer jgiNo;

	/**
	 * 施設出力対象区分
	 */
	private String insType;

	/**
	 * 施設コード
	 */
	private String insNo;

	/**
	 * TMS特約店コード
	 */
	private String tmsTytenCd;

	/**
	 * 削除フラグ
	 */
	private Boolean delFlg;

	/**
	 * 依頼中フラグ
	 */
	private Boolean reqFlg;

	/**
	 * 部門名略式
	 */
	private String bumonRyakuName;

	/**
	 * 従業員氏名
	 */
	private String jgiName;

	/**
	 * 施設正式漢字名
	 */
	private String insFormalName;

	/* add Start 2022/9/14 H.Futagami 本番障害対応No.30　計画支援フォロー修正（SCTASK1718778） */
	/**
	 * 施設略式漢字名
	 */
	private String insAbbrName;
	/* add End 2022/9/14 H.Futagami 本番障害対応No.30　計画支援フォロー修正（SCTASK1718778） */

	/**
	 * ＴＭＳ特約店名
	 */
	private String tmsName;

	/**
	 * 計画数合計
	 */
	private Long sumPlan;

	//---------------------
	// Getter & Setter
	// --------------------
	/**
	 * リージョンＣを取得する。
	 *
	 * @return 支店Ｃ
	 */
	public String getBrCode() {
		return brCode;
	}

	/**
	 * リージョンＣを設定する。
	 *
	 * @param brCode 支店Ｃ
	 */
	public void setBrCode(String brCode) {
		this.brCode = brCode;
	}

	/**
	 * エリアＣを取得する。
	 *
	 * @return 営業所Ｃ
	 */
	public String getDistCode() {
		return distCode;
	}

	/**
	 * エリア営業所Ｃを設定する。
	 *
	 * @param distCode 営業所Ｃ
	 */
	public void setDistCode(String distCode) {
		this.distCode = distCode;
	}

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
	 * 施設出力対象区分を取得する。
	 *
	 * @return 施設出力対象区分
	 */
	public String getInsType() {
		return insType;
	}

	/**
	 * 施設出力対象区分を設定する。
	 *
	 * @param insType 施設出力対象区分
	 */
	public void setInsType(String insType) {
		this.insType = insType;
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
	 * 部門名略式を取得する。
	 *
	 * @return 部門名略式
	 */
	public String getBumonRyakuName() {
		return bumonRyakuName;
	}

	/**
	 * 部門名略式を設定する。
	 *
	 * @param bumonRyakuName 部門名略式
	 */
	public void setBumonRyakuName(String bumonRyakuName) {
		this.bumonRyakuName = bumonRyakuName;
	}


	/**
	 * 従業員氏名を取得する。
	 *
	 * @return 従業員氏名
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * 従業員氏名を設定する。
	 *
	 * @param jgiName 従業員氏名
	 */
	public void setJgiName(String jgiName) {
		this.jgiName = jgiName;
	}

	/**
	 * 施設正式漢字名を取得する。
	 *
	 * @return 施設正式漢字名
	 */
	public String getInsFormalName() {
		return insFormalName;
	}

	/**
	 * 施設正式漢字名を設定する。
	 *
	 * @param insFormalName 施設正式漢字名
	 */
	public void setInsFormalName(String insFormalName) {
		this.insFormalName = insFormalName;
	}

	/* add Start 2022/9/14 H.Futagami 本番障害対応No.30　計画支援フォロー修正（SCTASK1718778） */
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
	/* add End 2022/9/14 H.Futagami 本番障害対応No.30　計画支援フォロー修正（SCTASK1718778） */


	/**
	 * ＴＭＳ特約店名を取得する。
	 *
	 * @return 施設正式漢字名
	 */
	public String getTmsName() {
		return tmsName;
	}

	/**
	 * ＴＭＳ特約店名を設定する。
	 *
	 * @param insFormalName 施設正式漢字名
	 */
	public void setTmsName(String tmsName) {
		this.tmsName = tmsName;
	}

	/**
	 * 計画数合計を取得する。
	 *
	 * @return 計画数合計
	 */
	public Long getSumPlan() {
		return sumPlan;
	}

	/**
	 * 計画数合計を設定する。
	 *
	 * @param sumPlan 計画数合計
	 */
	public void setSumPlan(Long sumPlan) {
		this.sumPlan = sumPlan;
	}

}
