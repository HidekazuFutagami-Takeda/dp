package jp.co.takeda.logic.div;

/**
 * 支店に紐づく営業所計画確定状態を示す列挙を表す列挙
 * 
 * @author yokokawa
 */
public enum OfficePlanStatusForBranch {

	/**
	 * 確定前
	 */
	NONE_COMPLETED,

	/**
	 * 一部確定
	 */
	PORTION_COMPLETED,

	/**
	 * 確定済
	 */
	COMPLETED;
}
