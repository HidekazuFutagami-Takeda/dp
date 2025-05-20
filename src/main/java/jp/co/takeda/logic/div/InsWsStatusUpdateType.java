package jp.co.takeda.logic.div;

/**
 * 施設特約店別計画ステータス更新種別を表す列挙
 * 
 * @author nozaki
 */
public enum InsWsStatusUpdateType {

	/**
	 * 公開
	 * （改定により、ワクチンのみ利用）
	 */
	OPEN,

	/**
	 * 公開解除
	 * （改定により、ワクチンのみ利用）
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
