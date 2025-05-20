package jp.co.takeda.model.view;

import java.io.Serializable;
import java.util.Date;

import jp.co.takeda.model.div.CalcType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 営業所計画ステータスのサマリーを表すクラス
 * 
 * @author khashimoto
 */
public class OfficePlanStatSum implements Serializable {

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
	 * 下書の件数
	 */
	private Integer draft;

	/**
	 * 確定の件数
	 */
	private Integer completed;

	/**
	 * 営業所計画ステータスの母数（営業所数）
	 */
	private Integer totalCount;

	/**
	 * 最終更新日
	 */
	protected Date upDate;

	/**
	 * 試算タイプ
	 */
	private CalcType calcType;

	//add Start 2022/7/25 H.Futagami 2022年4月組織変更対応  トップ画面 エリア計画の表示修正
	/**
	 * 計画立案レベル・営業所
	 */
	private String planLevelOffice;
	//add End 2022/7/25 H.Futagami 2022年4月組織変更対応  トップ画面 エリア計画の表示修正

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
	 * 営業所計画・下書の件数を取得する。
	 * 
	 * @return 営業所計画・下書の件数
	 */
	public Integer getDraft() {
		return draft;
	}

	/**
	 * 営業所計画・下書の件数を設定する。
	 * 
	 * @param draft 営業所計画・下書の件数
	 */
	public void setDraft(Integer draft) {
		this.draft = draft;
	}

	/**
	 * 営業所計画・確定の件数を取得する。
	 * 
	 * @return 営業所計画・確定の件数
	 */
	public Integer getCompleted() {
		return completed;
	}

	/**
	 * 営業所計画・確定の件数を設定する。
	 * 
	 * @param completed 営業所計画・確定の件数
	 */
	public void setCompleted(Integer completed) {
		this.completed = completed;
	}

	/**
	 * 営業所計画ステータスの母数を取得する。
	 * 
	 * @return 営業所計画ステータスの母数
	 */
	public Integer getTotalCount() {
		return totalCount;
	}

	/**
	 * 営業所計画ステータスの母数を設定する。
	 * 
	 * @param totalCount 営業所計画ステータスの母数
	 */
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 試算タイプを取得する。
	 * 
	 * @return 試算タイプ
	 */
	public CalcType getCalcType() {
		return calcType;
	}

	/**
	 * 試算タイプを設定する。
	 * 
	 * @param calcType 試算タイプ
	 */
	public void setCalcType(CalcType calcType) {
		this.calcType = calcType;
	}

	/**
	 * 最終更新日を取得する。
	 * 
	 * @return 最終更新日
	 */
	public Date getUpDate() {
		return upDate;
	}

	/**
	 * 最終更新日を設定する。
	 * 
	 * @param upDate 最終更新日
	 */
	public void setUpDate(Date upDate) {
		this.upDate = upDate;
	}

	//add Start 2022/7/25 H.Futagami 2022年4月組織変更対応  トップ画面 エリア計画の表示修正
	/**
	 * 計画立案レベル・営業所を取得する。
	 *
	 * @param planLevelOffice 計画立案レベル・営業所
	 */
	public String getPlanLevelOffice() {
		return planLevelOffice;
	}

	/**
	 * 計画立案レベル・営業所を設定する。
	 *
	 * @param planLevelOffice 計画立案レベル・営業所
	 */
	public void setPlanLevelOffice(String planLevelOffice) {
		this.planLevelOffice = planLevelOffice;
	}
	//add End 2022/7/25 H.Futagami 2022年4月組織変更対応  トップ画面 エリア計画の表示修正

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
