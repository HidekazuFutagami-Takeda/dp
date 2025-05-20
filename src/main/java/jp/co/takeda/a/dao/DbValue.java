package jp.co.takeda.a.dao;

/**
 * DB値を取得するためのインターフェイス
 * 
 * @author tkawabata
 */
public interface DbValue<T> {

	/**
	 * DB値を取得する。
	 * 
	 * @return 値
	 */
	T getDbValue();
}
