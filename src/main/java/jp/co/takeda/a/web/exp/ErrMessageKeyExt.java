package jp.co.takeda.a.web.exp;

import jp.co.takeda.a.bean.MessageKey;

/**
 * エラーメッセージを管理するクラス<br>
 * 
 * @author shiota
 */
public abstract class ErrMessageKeyExt {

	/**
	 * トークンが不正
	 */
	public static final MessageKey TOKEN_ERROR = new MessageKey("ErrMessageKeyExt.TOKEN_ERROR");

	/**
	 * サポート外のブラウザが指定された
	 */
	public static final MessageKey NOT_SUPPORT_BROWSER_ERROR = new MessageKey("ErrMessageKeyExt.NOT_SUPPORT_BROWSER_ERROR");

	/**
	 * ブラウザのキャンセルボタンが押された
	 */
	public static final MessageKey BROWSER_CANCEL_ERROR = new MessageKey("ErrMessageKeyExt.BROWSER_CANCEL_ERROR");

	/**
	 * サポート外のメソッドが指定された
	 */
	public static final MessageKey NOT_SUPPORT_METHOD_ERROR = new MessageKey("ErrMessageKeyExt.NOT_SUPPORT_METHOD_ERROR");

}
