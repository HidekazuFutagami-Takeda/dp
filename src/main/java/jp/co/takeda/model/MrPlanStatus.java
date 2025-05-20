package jp.co.takeda.model;

import java.util.Date;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.div.EstimationBasePlan;
import jp.co.takeda.model.div.StatusForMrPlan;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 担当者別計画ステータスを表すモデルクラス
 * 
 * @author tkawabata
 */
public final class MrPlanStatus extends DpModel<MrPlanStatus> implements Cloneable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 組織コード
	 */
	private String sosCd;

	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 担当者別計画ステータス
	 */
	private StatusForMrPlan statusForMrPlan;

	/**
	 * 非同期処理実行前ステータス
	 */
	private StatusForMrPlan asyncBefStatus;

	/**
	 * 試算の母数となる計画
	 */
	private EstimationBasePlan estimationBasePlan;

	/**
	 * サーバ区分
	 */
	private String appServerKbn;

	/**
	 * 非同期処理実行前試算開始日時
	 */
	private Date asyncBefEstStartDate;

	/**
	 * 試算開始日時
	 */
	private Date estStartDate;

	/**
	 * 試算終了日時
	 */
	private Date estEndDate;

	/**
	 * チーム別計画公開日時
	 */
	private Date teamPlanOpenDate;

	/**
	 * チーム別計画確定日時
	 */
	private Date teamPlanFixDate;

	/**
	 * 担当者別計画公開日時
	 */
	private Date mrPlanOpenDate;

	/**
	 * 担当者別計画入力完了日時
	 */
	private Date mrPlanInputDate;

	/**
	 * 担当者別計画確定日時
	 */
	private Date mrPlanFixDate;

	//---------------------
	// Getter & Setter
	// --------------------

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
	 * 担当者別計画ステータスを取得する。
	 * 
	 * @return 担当者別計画ステータス
	 */
	public StatusForMrPlan getStatusForMrPlan() {
		return statusForMrPlan;
	}

	/**
	 * 担当者別計画ステータスを設定する。
	 * 
	 * @param statusForMrPlan 担当者別計画ステータス
	 */
	public void setStatusForMrPlan(StatusForMrPlan statusForMrPlan) {
		this.statusForMrPlan = statusForMrPlan;
	}

	/**
	 * 非同期処理実行前ステータスを取得する。
	 * 
	 * @return 非同期処理実行前ステータス
	 */
	public StatusForMrPlan getAsyncBefStatus() {
		return asyncBefStatus;
	}

	/**
	 * 非同期処理実行前ステータスを設定する。
	 * 
	 * @param 非同期処理実行前ステータス
	 */
	public void setAsyncBefStatus(StatusForMrPlan asyncBefStatus) {
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
	 * 非同期処理実行前試算開始日時を取得する。
	 * 
	 * @return 非同期処理実行前試算開始日時
	 */
	public Date getAsyncBefEstStartDate() {
		return asyncBefEstStartDate;
	}

	/**
	 * 非同期処理実行前試算開始日時を設定する。
	 * 
	 * @param asyncBefdistStartDate 非同期処理実行前試算開始日時
	 */
	public void setAsyncBefEstStartDate(Date asyncBefEstStartDate) {
		this.asyncBefEstStartDate = asyncBefEstStartDate;
	}

	/**
	 * 試算開始日時を取得する。
	 * 
	 * @return 試算開始日時
	 */
	public Date getEstStartDate() {
		return estStartDate;
	}

	/**
	 * 試算開始日時を設定する。
	 * 
	 * @param estStartDate 試算開始日時
	 */
	public void setEstStartDate(Date estStartDate) {
		this.estStartDate = estStartDate;
	}

	/**
	 * 試算終了日時を取得する。
	 * 
	 * @return 試算終了日時
	 */
	public Date getEstEndDate() {
		return estEndDate;
	}

	/**
	 * 試算終了日時を設定する。
	 * 
	 * @param estEndDate 試算終了日時
	 */
	public void setEstEndDate(Date estEndDate) {
		this.estEndDate = estEndDate;
	}

	/**
	 * チーム別計画公開日時を取得する。
	 * 
	 * @return チーム別計画公開日時
	 */
	public Date getTeamPlanOpenDate() {
		return teamPlanOpenDate;
	}

	/**
	 * チーム別計画公開日時を設定する。
	 * 
	 * @param teamPlanOpenDate チーム別計画公開日時
	 */
	public void setTeamPlanOpenDate(Date teamPlanOpenDate) {
		this.teamPlanOpenDate = teamPlanOpenDate;
	}

	/**
	 * チーム別計画確定日時を取得する。
	 * 
	 * @return チーム別計画確定日時
	 */
	public Date getTeamPlanFixDate() {
		return teamPlanFixDate;
	}

	/**
	 * チーム別計画確定日時を設定する。
	 * 
	 * @param teamPlanFixDate チーム別計画確定日時
	 */
	public void setTeamPlanFixDate(Date teamPlanFixDate) {
		this.teamPlanFixDate = teamPlanFixDate;
	}

	/**
	 * 担当者別計画公開日時を取得する。
	 * 
	 * @return 担当者別計画公開日時
	 */
	public Date getMrPlanOpenDate() {
		return mrPlanOpenDate;
	}

	/**
	 * 担当者別計画公開日時を設定する。
	 * 
	 * @param mrPlanOpenDate 担当者別計画公開日時
	 */
	public void setMrPlanOpenDate(Date mrPlanOpenDate) {
		this.mrPlanOpenDate = mrPlanOpenDate;
	}

	/**
	 * 担当者別計画入力完了日時を取得する。
	 * 
	 * @return 担当者別計画入力完了日時
	 */
	public Date getMrPlanInputDate() {
		return mrPlanInputDate;
	}

	/**
	 * 担当者別計画入力完了日時を設定する。
	 * 
	 * @param mrPlanInputDate 担当者別計画入力完了日時
	 */
	public void setMrPlanInputDate(Date mrPlanInputDate) {
		this.mrPlanInputDate = mrPlanInputDate;
	}

	/**
	 * 担当者別計画確定日時を取得する。
	 * 
	 * @return 担当者別計画確定日時
	 */
	public Date getMrPlanFixDate() {
		return mrPlanFixDate;
	}

	/**
	 * 担当者別計画確定日時を設定する。
	 * 
	 * @param mrPlanFixDate 担当者別計画確定日時
	 */
	public void setMrPlanFixDate(Date mrPlanFixDate) {
		this.mrPlanFixDate = mrPlanFixDate;
	}

	/**
	 * 試算の母数となる計画を取得する。
	 * 
	 * @return estimationBasePlan 試算の母数となる計画
	 */
	public EstimationBasePlan getEstimationBasePlan() {
		return estimationBasePlan;
	}

	/**
	 * 試算の母数となる計画を設定する。
	 * 
	 * @param estimationBasePlan 試算の母数となる計画
	 */
	public void setEstimationBasePlan(EstimationBasePlan estimationBasePlan) {
		this.estimationBasePlan = estimationBasePlan;
	}

	@Override
	public int compareTo(MrPlanStatus obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.sosCd, obj.sosCd).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && MrPlanStatus.class.isAssignableFrom(entry.getClass())) {
			MrPlanStatus obj = (MrPlanStatus) entry;
			return new EqualsBuilder().append(this.sosCd, obj.sosCd).append(this.prodCode, obj.prodCode).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.sosCd).append(this.prodCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

	@Override
	public MrPlanStatus clone() {
		if (this == null) {
			return null;
		}
		MrPlanStatus mrPlanStatus = new MrPlanStatus();
		mrPlanStatus.setSeqKey(this.seqKey);
		mrPlanStatus.setSosCd(this.sosCd);
		mrPlanStatus.setProdCode(this.prodCode);
		mrPlanStatus.setStatusForMrPlan(this.statusForMrPlan);
		mrPlanStatus.setEstStartDate(this.estStartDate);
		mrPlanStatus.setEstEndDate(this.estEndDate);
		mrPlanStatus.setTeamPlanOpenDate(this.teamPlanOpenDate);
		mrPlanStatus.setTeamPlanFixDate(this.teamPlanFixDate);
		mrPlanStatus.setMrPlanOpenDate(this.mrPlanOpenDate);
		mrPlanStatus.setMrPlanInputDate(this.mrPlanInputDate);
		mrPlanStatus.setMrPlanFixDate(this.mrPlanFixDate);
		mrPlanStatus.setIsJgiNo(this.isJgiNo);
		mrPlanStatus.setIsJgiName(this.isJgiName);
		mrPlanStatus.setIsDate(this.isDate);
		mrPlanStatus.setUpJgiNo(this.isJgiNo);
		mrPlanStatus.setUpJgiName(this.upJgiName);
		mrPlanStatus.setUpDate(this.upDate);
		return mrPlanStatus;
	}
}
