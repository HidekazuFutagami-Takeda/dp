package jp.co.takeda.model.view;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 担当者別計画ステータスのサマリーを表すクラス
 * 
 * @author khashimoto
 */
public class MrPlanStatSum implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 該当無の件数
	 */
	private Integer none;

	/**
	 * 試算中の件数
	 */
	private Integer estimating;

	/**
	 * 試算済の件数
	 */
	private Integer estimated;

	/**
	 * チーム別計画公開の件数
	 */
	private Integer teamPlanOpened;

	/**
	 * チーム別計画確定の件数
	 */
	private Integer teamPlanCompleted;

	/**
	 * 担当者別計画公開の件数
	 */
	private Integer mrPlanOpened;

	/**
	 * 担当者別計画入力完了(AL修正)の件数
	 */
	private Integer mrPlanInputFinished;

	/**
	 * 担当者別計画確定(所長確定)の件数
	 */
	private Integer mrPlanCompleted;

	/**
	 * 担当者別計画ステータスの母数（営業所×品目）
	 */
	private Integer totalCount;

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
	 * 該当無の件数を取得する。
	 * 
	 * @return 該当無の件数
	 */
	public Integer getNone() {
		return none;
	}

	/**
	 * 該当無の件数を設定する。
	 * 
	 * @param none 該当無の件数
	 */
	public void setNone(Integer none) {
		this.none = none;
	}

	/**
	 * 試算中の件数を取得する。
	 * 
	 * @return 試算中の件数
	 */
	public Integer getEstimating() {
		return estimating;
	}

	/**
	 * 試算中の件数を設定する。
	 * 
	 * @param estimating 試算中の件数
	 */
	public void setEstimating(Integer estimating) {
		this.estimating = estimating;
	}

	/**
	 * 試算済の件数を取得する。
	 * 
	 * @return 試算済の件数
	 */
	public Integer getEstimated() {
		return estimated;
	}

	/**
	 * 試算済の件数を設定する。
	 * 
	 * @param estimated 試算済の件数
	 */
	public void setEstimated(Integer estimated) {
		this.estimated = estimated;
	}

	/**
	 * チーム別計画公開の件数を取得する。
	 * 
	 * @return チーム別計画公開の件数
	 */
	public Integer getTeamPlanOpened() {
		return teamPlanOpened;
	}

	/**
	 * チーム別計画公開の件数を設定する。
	 * 
	 * @param teamPlanOpened チーム別計画公開の件数
	 */
	public void setTeamPlanOpened(Integer teamPlanOpened) {
		this.teamPlanOpened = teamPlanOpened;
	}

	/**
	 * チーム別計画確定の件数を取得する。
	 * 
	 * @return チーム別計画確定の件数
	 */
	public Integer getTeamPlanCompleted() {
		return teamPlanCompleted;
	}

	/**
	 * チーム別計画確定の件数を設定する。
	 * 
	 * @param teamPlanCompleted チーム別計画確定の件数
	 */
	public void setTeamPlanCompleted(Integer teamPlanCompleted) {
		this.teamPlanCompleted = teamPlanCompleted;
	}

	/**
	 * 担当者別計画公開の件数を取得する。
	 * 
	 * @return 担当者別計画公開の件数
	 */
	public Integer getMrPlanOpened() {
		return mrPlanOpened;
	}

	/**
	 * 担当者別計画公開の件数を設定する。
	 * 
	 * @param mrPlanOpened 担当者別計画公開の件数
	 */
	public void setMrPlanOpened(Integer mrPlanOpened) {
		this.mrPlanOpened = mrPlanOpened;
	}

	/**
	 * 担当者別計画入力完了(AL修正)の件数を取得する。
	 * 
	 * @return 担当者別計画入力完了(AL修正)の件数
	 */
	public Integer getMrPlanInputFinished() {
		return mrPlanInputFinished;
	}

	/**
	 * 担当者別計画入力完了(AL修正)の件数を設定する。
	 * 
	 * @param mrPlanInputFinished 担当者別計画入力完了(AL修正)の件数
	 */
	public void setMrPlanInputFinished(Integer mrPlanInputFinished) {
		this.mrPlanInputFinished = mrPlanInputFinished;
	}

	/**
	 * 担当者別計画確定(所長確定)の件数を取得する。
	 * 
	 * @return 担当者別計画確定(所長確定)の件数
	 */
	public Integer getMrPlanCompleted() {
		return mrPlanCompleted;
	}

	/**
	 * 担当者別計画確定(所長確定)の件数を設定する。
	 * 
	 * @param mrPlanCompleted 担当者別計画確定(所長確定)の件数
	 */
	public void setMrPlanCompleted(Integer mrPlanCompleted) {
		this.mrPlanCompleted = mrPlanCompleted;
	}

	/**
	 * 担当者別計画ステータスの母数（営業所×品目）を取得する。
	 * 
	 * @return 担当者別計画ステータスの母数
	 */
	public Integer getTotalCount() {
		return totalCount;
	}

	/**
	 * 担当者別計画ステータスの母数（営業所×品目）を設定する。
	 * 
	 * @param totalCount 担当者別計画ステータスの母数
	 */
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
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

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
