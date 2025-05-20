package jp.co.takeda.a.bean;

import java.io.Serializable;

/**
 * BOXの中のオブジェクトを判定するためのキー値を生成するインターフェイス<br>
 * 
 * <p>
 * <code>Box</code>から処理結果を受け取る際に、当インターフェイスの実装クラスを利用する。<br>
 * 当インターフェイスを実装するクラスは、<code>createKey()</code>を実装し、 <br>
 * キー値となる値を文字列で返す必要がある。
 * </p>
 * 
 * @see Box
 * @author tkawabata
 */
public interface BoxKey extends Serializable {

	/**
	 * キーを生成するための情報が不足している場合のエラーメッセージ
	 */
	public static final String ERROR_MSG = "BoxKeyを生成するための情報が不足している。";

	/**
	 * BOXの中のオブジェクトを判定するためのキー値を生成する。
	 * 
	 * @return キー値
	 */
	String createKey();
}
