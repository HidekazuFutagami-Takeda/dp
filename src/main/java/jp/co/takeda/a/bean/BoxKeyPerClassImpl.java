package jp.co.takeda.a.bean;

import org.springframework.util.Assert;

/**
 * 2組のClassでBoxのキー値を生成する。<br>
 * 2組のClassの{@link java.lang.Class#getName()}の組み合わせでキー値を生成する。
 * 
 * @see Box
 * @see BoxKey
 * @author tkawabata
 */
public class BoxKeyPerClassImpl implements BoxKey {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -9094098041314498304L;

	/**
	 * 区切文字列
	 */
	public static final String SEPARATER = ":";

	/**
	 * 識別キー1
	 */
	@SuppressWarnings("unchecked")
	private final Class key1;

	/**
	 * 識別キー2
	 */
	@SuppressWarnings("unchecked")
	private final Class key2;

	/**
	 * コンストラクタ
	 * 
	 * @param key1 キーとなるクラス1
	 * @param key2 キーとなるクラス2
	 */
	@SuppressWarnings("unchecked")
	public BoxKeyPerClassImpl(Class key1, Class key2) {
		Assert.notNull(key1, ERROR_MSG);
		Assert.notNull(key2, ERROR_MSG);
		this.key1 = key1;
		this.key2 = key2;
	}

	/**
	 * キー値を生成する。
	 * 
	 * @return Classを元にしたキー値
	 * @see BoxKey#createKey()
	 */
	public String createKey() {
		return this.key1.getName() + SEPARATER + this.key2.getName();
	}
}
