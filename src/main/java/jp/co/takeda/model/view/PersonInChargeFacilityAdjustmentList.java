package jp.co.takeda.model.view;

import java.io.Serializable;

/**
 * 担当者・施設特約店別計画調整一覧を表すモデルクラス
 *
 * @author hfutagami
 */
public class PersonInChargeFacilityAdjustmentList  implements Serializable {

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
	 * 部門名略式
	 */
	private String bumonRyakuName;

	/**
	 * 従業員氏名
	 */
	private String jgiName;

	/**
	 * 施設出力対象区分
	 */
	private String insType;

	/**
	 * ステータス
	 */
	private String status;

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
	 * ステータスを取得する。
	 *
	 * @return ステータス
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * ステータスを設定する。
	 *
	 * @param status ステータス
	 */
	public void setStatus(String status) {
		this.status = status;
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
