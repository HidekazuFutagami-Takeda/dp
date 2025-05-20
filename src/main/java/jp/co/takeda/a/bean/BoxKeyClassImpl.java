package jp.co.takeda.a.bean;

import org.springframework.util.Assert;

/**
 * 1組のClassでBoxのキー値を生成する。<br> {@link java.lang.Class#getName()}でBoxのキー値を生成する。
 * 
 * @see Box
 * @see BoxKey
 * @author tkawabata
 */
public class BoxKeyClassImpl implements BoxKey {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6233905598302775110L;

	/**
	 * 識別キー
	 */
	@SuppressWarnings("unchecked")
	private final Class key;

	/**
	 * コンストラクタ
	 * 
	 * @param key キーとなるクラス
	 */
	@SuppressWarnings("unchecked")
	public BoxKeyClassImpl(Class key) {
		Assert.notNull(key, ERROR_MSG);
		this.key = key;
	}

	/**
	 * キー値を生成する。
	 * 
	 * @return Classを元にしたキー値
	 * @see BoxKey#createKey()
	 */
	public String createKey() {
		return key.getName();
	}
}
