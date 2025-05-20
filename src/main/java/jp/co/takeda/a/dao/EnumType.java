package jp.co.takeda.a.dao;

/**
 * 列挙のクラスおよびJDBC型({@link java.sql.Types})を特定するクラス
 * 
 * @author tkawabata
 */
public class EnumType {

	/**
	 * コンストラクタ
	 * 
	 * @param enumClass 列挙のクラス
	 * @param jdbcType JDBC型
	 * @see java.sql.Types
	 */
	@SuppressWarnings("unchecked")
	public EnumType(Class enumClass, int jdbcType) {
		this.enumClass = enumClass;
		this.jdbcType = jdbcType;
	}

	/**
	 * 列挙のクラス
	 */
	@SuppressWarnings("unchecked")
	private final Class enumClass;

	/**
	 * JDBC型({@link java.sql.Types})
	 */
	private final int jdbcType;

	/**
	 * 列挙のクラスを取得する。
	 * 
	 * @return 列挙のクラス
	 */
	@SuppressWarnings("unchecked")
	public Class getEnumClass() {
		return enumClass;
	}

	/**
	 * JDBC型を取得する。
	 * 
	 * @return JDBC型
	 */
	public int getJdbcType() {
		return jdbcType;
	}
}
