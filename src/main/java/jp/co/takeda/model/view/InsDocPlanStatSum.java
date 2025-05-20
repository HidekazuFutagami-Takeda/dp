package jp.co.takeda.model.view;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設医師別計画ステータスのサマリーを表すクラス
 * 
 * @author khashimoto
 */
public class InsDocPlanStatSum implements Serializable {

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
	 * 配分中の件数
	 */
	private Integer disting;

	/**
	 * 配分済の件数
	 */
	private Integer disted;

	/**
	 * 施設医師別計画公開の件数
	 */
	private Integer planOpened;

	/**
	 * 施設医師別計画確定の件数
	 */
	private Integer planCompleted;

	/**
	 * 施設医師別計画ステータスの母数（担当者×品目）
	 */
	private Integer totalCount;

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
	 * 配分中の件数を取得する。
	 * 
	 * @return 配分中の件数
	 */
	public Integer getDisting() {
		return disting;
	}

	/**
	 * 配分中の件数を設定する。
	 * 
	 * @param disting 配分中の件数
	 */
	public void setDisting(Integer disting) {
		this.disting = disting;
	}

	/**
	 * 配分済の件数を取得する。
	 * 
	 * @return disted 配分済の件数
	 */
	public Integer getDisted() {
		return disted;
	}

	/**
	 * 配分済の件数を設定する。
	 * 
	 * @param disted 配分済の件数
	 */
	public void setDisted(Integer disted) {
		this.disted = disted;
	}

	/**
	 * 施設医師別計画公開の件数を取得する。
	 * 
	 * @return 施設医師別計画公開の件数
	 */
	public Integer getPlanOpened() {
		return planOpened;
	}

	/**
	 * 施設医師別計画公開の件数を設定する。
	 * 
	 * @param planOpened 施設医師別計画公開の件数
	 */
	public void setPlanOpened(Integer planOpened) {
		this.planOpened = planOpened;
	}

	/**
	 * 施設医師別計画確定の件数を取得する。
	 * 
	 * @return 施設医師別計画確定の件数
	 */
	public Integer getPlanCompleted() {
		return planCompleted;
	}

	/**
	 * 施設医師別計画確定の件数を設定する。
	 * 
	 * @param planCompleted 施設医師別計画確定の件数
	 */
	public void setPlanCompleted(Integer planCompleted) {
		this.planCompleted = planCompleted;
	}

	/**
	 * 施設医師別計画ステータスの母数（担当者×品目）を取得する。
	 * 
	 * @return 施設医師別計画ステータスの母数
	 */
	public Integer getTotalCount() {
		return totalCount;
	}

	/**
	 * 施設医師別計画ステータスの母数（担当者×品目）を設定する。
	 * 
	 * @param totalCount 施設医師別計画ステータスの母数
	 */
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
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

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
