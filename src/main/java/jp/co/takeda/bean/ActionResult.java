package jp.co.takeda.bean;

import jp.co.takeda.a.web.bean.Result;

/**
 * アクションの結果を示す列挙
 * 
 * @author tkawabata
 */
public enum ActionResult implements Result {

	/**
	 * 処理の成功
	 */
	SUCCESS("success"),

	/**
	 * 処理の失敗
	 */
	FAILURE("failure"),

	/**
	 * 入力エラー
	 */
	INPUT("input"),

	/**
	 * トークンが不正
	 */
	TOKENINVALID("tokenInvalid"),

	/**
	 * 認証エラー
	 */
	NOTCERTIFICATED("notCertificated"),

	/**
	 * 認可エラー
	 */
	AUTHORITYERROR("authorityError"),

	/**
	 * アクセス拒否
	 */
	ACCESS_DENIED("accessDenied"),

	/**
	 * ユーザタイムアウト
	 */
	USER_TIMEOUT("userTimeout"),

	/**
	 * エラー
	 */
	ERROR("error"),

	/**
	 * 非同期エラー
	 */
	ASYNC_ERROR("asyncError"),

	/**
	 * 復旧不能
	 */
	FATAL("fatal");

	/**
	 * 小文字表現を格納
	 */
	private final String value;

	/**
	 * コンストラクタ
	 * 
	 * @param value 小文字表現
	 */
	private ActionResult(final String value) {
		this.value = value;
	}

	/**
	 * 処理結果を表す文字列を返す。
	 * 
	 * @return 処理結果
	 */
	public String getResult() {
		return this.value;
	}
}
