package jp.co.takeda.model;

import java.util.Date;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.div.StatusForInsDocPlan;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設医師別計画ステータスを表すモデルクラス
 *
 * @author tkawabata
 */
public class InsDocPlanStatus extends DpModel<InsDocPlanStatus> {

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
	 * 施設医師別計画ステータス
	 */
	private StatusForInsDocPlan statusForInsDocPlan;

	/**
	 * 非同期処理実行前ステータス
	 */
	private StatusForInsDocPlan asyncBefStatus;

	/**
	 * サーバ区分
	 */
	private String appServerKbn;

	/**
	 * 非同期処理実行前配分開始日時
	 */
	private Date asyncBefDistStartDate;

	/**
	 * 配分開始日時
	 */
	private Date distStartDate;

	/**
	 * 配分終了日時
	 */
	private Date distEndDate;

	/**
	 * 施設医師別計画公開日時
	 */
	private Date insDocOpenDate;

	/**
	 * 施設医師別計画確定日時
	 */
	private Date insDocFixDate;

	/**
	 * 修正計画クリアフラグ
	 */
	private Boolean plannedClearFlg;

	/**
	 * 同時公開フラグ
	 */
	private Boolean openFlg;

	/**
	 * 氏名（Ref[従業員情報].〔氏名〕）
	 */
	private String jgiName;

	/**
	 * 職種名
	 */
	private String shokushuName;


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
	 * 施設医師別計画ステータスを取得する。
	 *
	 * @return 施設医師別計画ステータス
	 */
	public StatusForInsDocPlan getStatusForInsDocPlan() {
		return statusForInsDocPlan;
	}

	/**
	 * 施設医師別計画ステータスを設定する。
	 *
	 * @param statusForInsDocPlan 施設医師別計画ステータス
	 */
	public void setStatusForInsDocPlan(StatusForInsDocPlan statusForInsDocPlan) {
		this.statusForInsDocPlan = statusForInsDocPlan;
	}

	/**
	 * 非同期処理実行前ステータスを取得する。
	 *
	 * @return 非同期処理実行前ステータス
	 */
	public StatusForInsDocPlan getAsyncBefStatus() {
		return asyncBefStatus;
	}

	/**
	 * 非同期処理実行前ステータスを設定する。
	 *
	 * @param 非同期処理実行前ステータス
	 */
	public void setAsyncBefStatus(StatusForInsDocPlan asyncBefStatus) {
		this.asyncBefStatus = asyncBefStatus;
	}

	/**
	 * サーバー区分を取得する。
	 *
	 * @return サーバー区分
	 */
	public String getAppServerKbn() {
		return appServerKbn;
	}

	/**
	 * サーバー区分を設定する。
	 *
	 * @param appServerKbn サーバー区分
	 */
	public void setAppServerKbn(String appServerKbn) {
		this.appServerKbn = appServerKbn;
	}

	/**
	 * 非同期処理実行前配分開始日時を取得する。
	 *
	 * @return 非同期処理実行前配分開始日時
	 */
	public Date getAsyncBefDistStartDate() {
		return asyncBefDistStartDate;
	}

	/**
	 * 非同期処理実行前配分開始日時を設定する。
	 *
	 * @param asyncBefDistStartDate 非同期処理実行前配分開始日時
	 */
	public void setAsyncBefDistStartDate(Date asyncBefDistStartDate) {
		this.asyncBefDistStartDate = asyncBefDistStartDate;
	}

	/**
	 * 配分開始日時を取得する。
	 *
	 * @return 配分開始日時
	 */
	public Date getDistStartDate() {
		return distStartDate;
	}

	/**
	 * 配分開始日時を設定する。
	 *
	 * @param distStartDate 配分開始日時
	 */
	public void setDistStartDate(Date distStartDate) {
		this.distStartDate = distStartDate;
	}

	/**
	 * 配分終了日時を取得する。
	 *
	 * @return 配分終了日時
	 */
	public Date getDistEndDate() {
		return distEndDate;
	}

	/**
	 * 配分終了日時を設定する。
	 *
	 * @param distEndDate 配分終了日時
	 */
	public void setDistEndDate(Date distEndDate) {
		this.distEndDate = distEndDate;
	}

	/**
	 * 施設医師別計画公開日時を取得する。
	 *
	 * @return 施設医師別計画公開日時
	 */
	public Date getInsDocOpenDate() {
		return insDocOpenDate;
	}

	/**
	 * 施設医師別計画公開日時を設定する。
	 *
	 * @param insDocOpenDate 施設医師別計画公開日時
	 */
	public void setInsDocOpenDate(Date insDocOpenDate) {
		this.insDocOpenDate = insDocOpenDate;
	}

	/**
	 * 施設医師別計画確定日時を取得する。
	 *
	 * @return 施設医師別計画確定日時
	 */
	public Date getInsDocFixDate() {
		return insDocFixDate;
	}

	/**
	 * 施設医師別計画確定日時を設定する。
	 *
	 * @param insDocFixDate 施設医師別計画確定日時
	 */
	public void setInsDocFixDate(Date insDocFixDate) {
		this.insDocFixDate = insDocFixDate;
	}

	/**
	 * 修正計画クリアフラグ を取得する。
	 *
	 * @return 修正計画クリアフラグ
	 */
	public Boolean getPlannedClearFlg() {
		return plannedClearFlg;
	}

	/**
	 * 修正計画クリアフラグ を設定する。
	 *
	 * @param plannedClearFlg 修正計画クリアフラグ
	 */
	public void setPlannedClearFlg(Boolean plannedClearFlg) {
		this.plannedClearFlg = plannedClearFlg;
	}

	/**
	 * 同時公開フラグ を取得する。
	 *
	 * @return 同時公開フラグ
	 */
	public Boolean getOpenFlg() {
		return openFlg;
	}

	/**
	 * 同時公開フラグ を設定する。
	 *
	 * @param openFlg 同時公開フラグ
	 */
	public void setOpenFlg(Boolean openFlg) {
		this.openFlg = openFlg;
	}

	/**
	 * 氏名を取得する。
	 *
	 * @return 氏名
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * 氏名を設定する。
	 *
	 * @param jgiName 氏名
	 */
	public void setJgiName(String jgiName) {
		this.jgiName = jgiName;
	}

	/**
	 * 職種名を取得する
	 * @return shokushuName
	 */
	public String getShokushuName() {
		return shokushuName;
	}

	/**
	 * 職種名を設定する
	 * @param shokushuName
	 */
	public void setShokushuName(String shokushuName) {
		this.shokushuName = shokushuName;
	}

	@Override
	public int compareTo(InsDocPlanStatus obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.jgiNo, obj.jgiNo).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && InsDocPlanStatus.class.isAssignableFrom(entry.getClass())) {
			InsDocPlanStatus obj = (InsDocPlanStatus) entry;
			return new EqualsBuilder().append(this.jgiNo, obj.jgiNo).append(this.prodCode, obj.prodCode).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.jgiNo).append(this.prodCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

	@Override
	public InsDocPlanStatus clone() {
		if (this == null) {
			return null;
		}
		InsDocPlanStatus insDocPlanStatus = new InsDocPlanStatus();
		insDocPlanStatus.setSeqKey(this.seqKey);
		insDocPlanStatus.setJgiNo(this.jgiNo);
		insDocPlanStatus.setProdCode(this.prodCode);
		insDocPlanStatus.setStatusForInsDocPlan(this.statusForInsDocPlan);
		insDocPlanStatus.setDistStartDate(this.distStartDate);
		insDocPlanStatus.setDistEndDate(this.distEndDate);
		insDocPlanStatus.setInsDocOpenDate(this.insDocOpenDate);
		insDocPlanStatus.setInsDocFixDate(this.insDocFixDate);
		insDocPlanStatus.setIsJgiNo(this.isJgiNo);
		insDocPlanStatus.setIsJgiName(this.isJgiName);
		insDocPlanStatus.setIsDate(this.isDate);
		insDocPlanStatus.setUpJgiNo(this.isJgiNo);
		insDocPlanStatus.setUpJgiName(this.upJgiName);
		insDocPlanStatus.setUpDate(this.upDate);
		return insDocPlanStatus;
	}
}
