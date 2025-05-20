package jp.co.takeda.model.view;

import jp.co.takeda.model.OfficeChosei;
import jp.co.takeda.model.OfficePlan;
import jp.co.takeda.model.div.StatusForMrPlan;

/**
 * 営業計画と営業所調整検を結合した検索結果を表すクラス
 *
 * @author mtsuchida
 */
public class OfficePlanWithChosei {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 営業計画検索結果
	 */
	private OfficePlan officePlan = new OfficePlan();

	/**
	 * 営業所調整検索結果
	 */
	private OfficeChosei officeChosei = new OfficeChosei();

	/**
	 * 担当者別計画ステータス
	 */
	private StatusForMrPlan statusForMrPlan;

	/**
	 * 営業計画検索結果を設定する。
	 *
	 * @param officePlan 営業計画検索結果
	 */
	public void setOfficePlan(OfficePlan officePlan) {
		this.officePlan = officePlan;
	}

	/**
	 * 営業所調整検索結果を設定する。
	 *
	 * @param officeChosei 営業所調整検索結果
	 */
	public void setOfficeChosei(OfficeChosei officeChosei) {
		this.officeChosei = officeChosei;
	}

	/**
	 * 営業計画検索結果を取得する。
	 *
	 * @return 営業計画検索結果リスト
	 */
	public OfficePlan getOfficePlan() {
		return officePlan;
	}

	/**
	 * 営業所調整検索結果を取得する。
	 *
	 * @return 営業所調整検索結果リスト
	 */
	public OfficeChosei getOfficeChosei() {
		return officeChosei;
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
}
