package jp.co.takeda.a.exp;

import jp.co.takeda.a.bean.MessageKey;

/**
 * 汎用的なエラーメッセージキーを管理するクラス
 *
 * @author tkawabata
 */
public abstract class ErrMessageKey {

	/**
	 * データが存在しない
	 */
	public static final MessageKey DATA_NOT_FOUND_ERROR = new MessageKey("ErrMessageKey.DATA_NOT_FOUND_ERROR");

	/**
	 * 入力値が不正
	 */
	public static final MessageKey VALIDATE_ERROR = new MessageKey("ErrMessageKey.VALIDATE_ERROR");

	/**
	 * ファイルが存在しない
	 */
	public static final MessageKey FILE_NOT_FOUND_ERROR = new MessageKey("ErrMessageKey.FILE_NOT_FOUND_ERROR");

	/**
	 * ファイルが正しくありません
	 */
	public static final MessageKey FILE_ILLEGAL_ERROR = new MessageKey("ErrMessageKey.FILE_ILLEGAL_ERROR");

	/**
	 * IOエラー
	 */
	public static final MessageKey IO_ERROR = new MessageKey("ErrMessageKey.IO_ERROR");

	/**
	 * パラメータが不正
	 */
	public static final MessageKey PARAMETER_ERROR = new MessageKey("ErrMessageKey.PARAMETER_ERROR");

	/**
	 * 解析中エラー
	 */
	public static final MessageKey PARSE_ERROR = new MessageKey("ErrMessageKey.PARSE_ERROR");

	/**
	 * 状態が不正
	 */
	public static final MessageKey STATE_ERROR = new MessageKey("ErrMessageKey.STATE_ERROR");

	/**
	 * 楽観的ロックエラー
	 */
	public static final MessageKey OPTIMISTIC_ROCK_ERROR = new MessageKey("ErrMessageKey.OPTIMISTIC_ROCK_ERROR");

	/**
	 * DBResourceBusyエラー
	 */
	public static final MessageKey DB_RESOURCE_BUSY_ERROR = new MessageKey("ErrMessageKey.DB_RESOURCE_BUSY_ERROR");

	/**
	 * DBステートメントタイムアウトエラー
	 */
	public static final MessageKey DB_STATEMENT_TIMEOUT_ERROR = new MessageKey("ErrMessageKey.DB_STATEMENT_TIMEOUT_ERROR");

	/**
	 * DB重複エラー
	 */
	public static final MessageKey DB_DUPLICATE_ERROR = new MessageKey("ErrMessageKey.DB_DUPLICATE_ERROR");

	/**
	 * DB致命的エラー
	 */
	public static final MessageKey DB_ERROR = new MessageKey("ErrMessageKey.DB_ERROR");

	/**
	 * トランザクションでのタイムアウトエラー
	 */
	public static final MessageKey TX_TIMEOUT_ERROR = new MessageKey("ErrMessageKey.TX_TIMEOUT_ERROR");

	/**
	 * トランザクションでの致命的エラー
	 */
	public static final MessageKey TX_ERROR = new MessageKey("ErrMessageKey.TX_ERROR");

	/**
	 * アクセス拒否エラー
	 */
	public static final MessageKey ACCESS_DENIED_ERROR = new MessageKey("ErrMessageKey.ACCESS_DENIED_ERROR");

	/**
	 * 認証エラー
	 */
	public static final MessageKey CERTIFICATION_ERROR = new MessageKey("ErrMessageKey.CERTIFICATION_ERROR");

	/**
	 * 認可エラー
	 */
	public static final MessageKey AUTHORIZATION_ERROR = new MessageKey("ErrMessageKey.AUTHORIZATION_ERROR");

	/**
	 * 論理例外が発生
	 */
	public static final MessageKey LOGICAL_ERROR = new MessageKey("ErrMessageKey.LOGICAL_ERROR");

	/**
	 * システム例外が発生
	 */
	public static final MessageKey SYSTEM_ERROR = new MessageKey("ErrMessageKey.SYSTEM_ERROR");

	/**
	 * 致命的例外が発生
	 */
	public static final MessageKey FATAL_ERROR = new MessageKey("ErrMessageKey.FATAL_ERROR");
}
