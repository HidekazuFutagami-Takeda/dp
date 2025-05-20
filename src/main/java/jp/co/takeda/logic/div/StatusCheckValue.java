package jp.co.takeda.logic.div;

/**
 * 立案ステータスと意味を取得するためのインターフェイス
 * 
 * ステータスチェック用に使用する。
 * 
 * @author nozaki
 */
public interface StatusCheckValue<T, K> {

	/**
	 * 立案ステータスを取得する。
	 * 
	 * @return 値
	 */
	T getStatus();

	/**
	 * ステータスの意味を取得する。
	 * 
	 * @return ステータスの意味
	 */
	K getStatusMean();
}
