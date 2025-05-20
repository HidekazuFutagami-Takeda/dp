package jp.co.takeda.logic.div;

/**
 * 施設特約店別計画の配分種類を表す列挙
 *
 * @author khashimoto
 */
public enum InsWsDistType {

	/**
	 * 営業所計画の一括配分
	 */
	OFFICE_PLAN_DIST,

	/**
	 * 担当者別計画の一括配分
	 */
	MR_PLAN_DIST,

	/**
	 * 営業所計画の再配分
	 */
	OFFICE_PLAN_RE_DIST,

	/**
	 * 担当者別計画の再配分
	 */
	MR_PLAN_RE_DIST,

	/**
	 * ワクチン営業所計画の配分
	 */
	OFFICE_PLAN_DIST_FOR_VAC,

	/**
	 * ワクチン担当者別計画の配分
	 */
	MR_PLAN_DIST_FOR_VAC,

	/**
	 * 施設別計画の配分
	 */
	INS_PLAN_DIST
}
