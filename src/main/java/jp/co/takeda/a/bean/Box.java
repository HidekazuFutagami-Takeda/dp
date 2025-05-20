package jp.co.takeda.a.bean;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * 直列化可能な処理結果を格納するクラス<br>
 * 
 * <p>
 * メンバとして{@link #resultMap}を持つ。<br>
 * 値の格納に際しては、キー値のnullを許容しないが、値のnullを許容する。<br>
 * また値部分は必ず<code>Serializable</code>をimplementsしている必要がある。<br>
 * </p>
 * <br>
 * <h5>[利用方法]</h5> 例えばWeb系のシステムで、REQUEST,SESSIONスコープに<code>Serializable</code>型の<br>
 * オブジェクトをコントローラが一元的に管理するために利用する。<br>
 * 以下に例を示す。<br>
 * <br>
 * <ol>
 * <li>Springの設定</li>
 * <fieldset> <legend> applicationContext.xml </legend> &lt;bean id="requestBox" class="jp.co.takeda.a.bean.Box" scope="request"
 * /&gt;<br>
 * &lt;bean id="sessionBox" class="jp.co.takeda.a.bean.Box" scope="session" /&gt;<br>
 * </fieldset> <br>
 * <br>
 * <li>Boxの取得例</li>
 * <fieldset> <legend>HogeController.java</legend> BeanFactory factory = ...<br>
 * Box sessionBox = (Box) factory.getBean("sessionBox"); </fieldset> <br>
 * <br>
 * <li>Box下のオブジェクトの設定例</li>
 * <fieldset> <legend>HogeController.java</legend> public static final BoxKey S_HOGE_KEY = new BoxKeyClassImpl(HogeClass.class);
 * <br>
 * public String someCtrlMethod () {<br>
 * 　　sessionBox.put(S_HOGE_KEY, targetObject);<br>
 * }<br>
 * </fieldset> <br>
 * <br>
 * <li>Box下のオブジェクトの取得例</li>
 * <fieldset> <legend>HogeController.java</legend> public static final BoxKey S_HOGE_KEY = new BoxKeyClassImpl(HogeClass.class);
 * <br>
 * public String someCtrlMethod () {<br>
 * 　　HogeClass hoge = sessionBox.get(S_HOGE_KEY);<br>
 * }<br>
 * </fieldset> <br>
 * <br>
 * <li>除去例</li>
 * <fieldset> <legend>HogeController.java</legend> public static final BoxKey S_HOGE_KEY = new BoxKeyClassImpl(HogeClass.class);
 * <br>
 * public String someCtrlMethod () {<br>
 * 　　sessionBox.remove(S_HOGE_KEY);<br>
 * }<br>
 * </fieldset> <br>
 * <br>
 * </ol>
 * 
 * @author tkawabata
 */
public class Box implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3688064145873163880L;

	/**
	 * 処理結果を格納するMap<br>
	 */
	private Map<String, Object> resultMap;

	/**
	 * 指定キーに紐づくオブジェクトが存在するかを取得する。<br>
	 * 
	 * @param key 処理結果を識別するキーオブジェクト
	 * @return 紐づくオブジェクトが存在する場合に真
	 */
	public boolean isExist(final BoxKey key) {

		if (this.get(key) != null) {
			return true;
		}
		return false;
	}

	/**
	 * 処理結果を格納する。<br>
	 * 
	 * <p>
	 * キーに<code>BoxKey</code>、値に<code>Serializable</code>を継承したオブジェクトを受け取る。<br>
	 * このメソッドは同期化されており処理中は<code>resultMap</code>を保護する。
	 * </p>
	 * 
	 * @param <T> 任意の型 ただし<code>Serializable</code>を<code>implements</code>していること
	 * @param key 処理結果を識別するキーオブジェクト
	 * @param t 任意の型
	 */
	public synchronized <T extends Serializable> void put(final BoxKey key, final T t) {

		final String ERROR_MSG = "BoxKeyがnull";
		Assert.notNull(key, ERROR_MSG);
		if (this.resultMap == null) {
			this.resultMap = new HashMap<String, Object>();
		}
		this.resultMap.put(key.createKey(), t);
	}

	/**
	 * 処理結果を取得する。<br>
	 * 
	 * <p>
	 * キーに<code>BoxKey</code>を受け取り、<code>resultMap</code>から値を取得する。<br>
	 * 該当する値が存在しない場合はnullを返す。<br>
	 * このメソッドは同期化されており処理中は<code>resultMap</code>を保護する。
	 * </p>
	 * 
	 * @param <T> 任意の型 ただし{@link Serializable}をimplementsしていること
	 * @param key 処理結果を識別するキーオブジェクト
	 * @return keyに紐づく任意の型T
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T extends Serializable> T get(final BoxKey key) {

		if (key == null || this.resultMap == null) {
			return null;
		} else {
			return (T) this.resultMap.get(key.createKey());
		}
	}

	/**
	 * 指定キーのオブジェクトを除去する。<br>
	 * 
	 * <p>
	 * キーに<code>BoxKey</code>の配列を受け取り、<code>resultMap</code>から値を除去する。<br>
	 * キーに該当する値が存在しない場合は何もしない。<br>
	 * このメソッドは同期化されており処理中は<code>resultMap</code>を保護する。
	 * </p>
	 * 
	 * @param keys 処理結果を識別するキー文字列
	 */
	public synchronized void remove(final BoxKey... keys) {
		if ((this.resultMap != null) && (keys != null) && (keys.length > 0)) {
			for (final BoxKey key : keys) {
				this.resultMap.remove(key.createKey());
			}
		}
	}

	/**
	 * Boxの中身のスナップショットを取得する。<br>
	 * 
	 * <pre>
	 * [重要]
	 * デバック用途を想定している。読取専用Mapにラップしてあるが、
	 * Mapからオブジェクトを取得し、変更を加えると、変更は適用されてしまう。
	 * Boxの中身をデバック用に確認するなどの用途に限定して使う。
	 * </pre>
	 * 
	 * @return Boxの中身のスナップショット
	 * @deprecated 非推奨。デバック用に限定的利用する。
	 */
	@Deprecated
	public synchronized Map<String, Object> getSnapshot() {
		if (this.resultMap == null) {
			return null;
		}
		return Collections.unmodifiableMap(this.resultMap);
	}
}
