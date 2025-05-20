package jp.co.takeda.logic.div;

/**
 * 施設特約店別計画の種類を表す列挙
 * 
 * @author khashimoto
 */
public enum InsWsPlanType {

	/**
	 * 通常
	 */
	NORMAL,

	/**
	 * 重点先
	 */
	JTN,

	/**
	 * 一般先
	 */
	IPPAN,

	/**
	 * 特定施設
	 */
	SPECIAL,

	/**
	 * 配分除外
	 */
	EXCEPT,

	/**
	 * 削除施設
	 */
	DELETE,

	/**
	 * ゼロ配分
	 */
	ZERO;

}
