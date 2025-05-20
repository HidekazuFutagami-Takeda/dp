package jp.co.takeda.a.dao;

import org.springframework.util.Assert;

/**
 * SQLを保持するクラス<br> {@link DataAccess#executeBatch(java.util.List)}で利用する。
 * 
 * @see DataAccess#executeBatch(java.util.List)
 * @author tkawabata
 */
public class SqlHolder {

	/**
	 * エラーメッセージ
	 */
	private static final String ERROR_MSG = "バッチを特定するためのSqlHolder#id属性は必須";

	/**
	 * ID
	 */
	private final String id;

	/**
	 * SQLの引数
	 */
	private final Object bindParams;

	/**
	 * コンストラクタ
	 * 
	 * @param id ID
	 */
	public SqlHolder(final String id) {
		this(id, null);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param id ID
	 * @param bindParams SQLの引数
	 */
	public SqlHolder(final String id, final Object bindParams) {
		Assert.notNull(id, ERROR_MSG);
		this.id = id;
		this.bindParams = bindParams;
	}

	/**
	 * IDを取得する。
	 * 
	 * @return ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * SQLの引数を取得する。
	 * 
	 * @return SQLの引数
	 */
	public Object getBindParams() {
		return bindParams;
	}
}
