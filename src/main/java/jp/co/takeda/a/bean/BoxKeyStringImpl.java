package jp.co.takeda.a.bean;

import org.springframework.util.Assert;

/**
 * コンストラクタで渡されたキー文字列でBoxのキー値を生成する。<br>
 * 
 * @see Box
 * @see BoxKey
 * @author tkawabata
 */
public class BoxKeyStringImpl implements BoxKey {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5300310739928634241L;

	/**
	 * 識別キー
	 */
	private final String key;

	/**
	 * コンストラクタ
	 * 
	 * @param key キー値
	 */
	public BoxKeyStringImpl(String key) {
		Assert.notNull(key, ERROR_MSG);
		this.key = key;
	}

	/**
	 * キー値を生成する。
	 * 
	 * @return キー値
	 * @see BoxKey#createKey()
	 */
	public String createKey() {
		return this.key;
	}
}
