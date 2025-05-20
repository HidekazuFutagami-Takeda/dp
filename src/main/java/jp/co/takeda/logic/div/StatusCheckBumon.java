package jp.co.takeda.logic.div;

/**
 * ステータスチェックの対象範囲を示す列挙
 * 
 * @author nozaki
 */
public enum StatusCheckBumon {

	/**
	 * 全部
	 */
	ALL,

	/**
	 * 支店、特約店部
	 */
	SITEN_TOKUYAKUTEN_BU,

	/**
	 * 営業所、特約店G
	 */
	OFFICE_TOKUYAKUTEN_G,

	/**
	 * チーム
	 */
	TEAM,

	/**
	 * 担当者
	 */
	MR;
}
