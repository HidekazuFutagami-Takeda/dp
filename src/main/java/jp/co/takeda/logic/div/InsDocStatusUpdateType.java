package jp.co.takeda.logic.div;

/**
 * 施設特約店別計画ステータス更新種別を表す列挙
 * 
 * @author nozaki
 */
public enum InsDocStatusUpdateType {

	/**
	 * 公開
	 */
	OPEN,

	/**
	 * 公開解除
	 */
	OPEN_CANCEL,

	/**
	 * 確定
	 */
	FIX,

	/**
	 * 確定解除
	 */
	FIX_CANCEL
}
