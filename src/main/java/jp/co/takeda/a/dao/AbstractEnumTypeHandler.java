package jp.co.takeda.a.dao;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

/**
 * <b>列挙型へ変換するための抽象タイプハンドラークラス</b><br>
 * 
 * <p>
 * DBの値を列挙型に変換するための抽象クラス。<br>
 * 当クラスを実装し、コンストラクタ内で変換対象となる列挙を指定する。<br>
 * 以下にHOGE_DIVISIONというRDB TABLEの区分値をHoge列挙に変換するためのコーディング・設定手順を示す。<br>
 * </p>
 * 
 * <ol>
 * <li>AbstractEnumTypeHandlerを継承したクラスを作成する。</li>
 * <fieldset> <legend> com.xxx.XXXTypeHandler.java </legend> <code>
 * public class HogeTypeHandler extends AbstractEnumTypeHandler {<br>
 * 　　public HogeTypeHandler() {<br>
 * 　　　　super(new EnumType(Hoge.class, java.sql.Types.CHAR));<br>
 * 　　}<br>
 * }<br>
 * </code> </fieldset> <br>
 * <br>
 * <li>iBatisの設定ファイル(sqlmap-config.xml)にTypeHandlerを登録する。</li>
 * <fieldset> <legend> sqlmap-config.xml </legend> &lt;typeAlias alias="Hoge" type="com.xxx.model.div.Hoge" /&gt;<br>
 * &lt;typeHandler javaType="Hoge" callback="com.xxx.model.div.HogeTypeHandler" /&gt;<br>
 * </fieldset> <br>
 * <br>
 * <li>各SQLファイルにjavaTypeとしてhogeを指定する。</li>
 * <fieldset> <legend> iBatis SQL設定ファイル </legend> &lt;result property="hoge" javaType="Hoge" column="HOGE_DIVISION" /&gt;<br>
 * </fieldset>
 * </ol>
 * 
 * @author tkawabata
 */
public abstract class AbstractEnumTypeHandler implements TypeHandlerCallback {

	/**
	 * JDBC型({@link java.sql.Types})
	 * 
	 * @see java.sql.Types
	 */
	protected final int jdbcType;

	/**
	 * DB値をキーに列挙へ変換するためのMap
	 */
	@SuppressWarnings("unchecked")
	protected final Map<Object, Enum> convertToEnumMap;

	/**
	 * コンストラクタ
	 */
	@SuppressWarnings("unchecked")
	protected AbstractEnumTypeHandler(EnumType enumType) {
		Map<Object, Enum> tmpMap = new HashMap<Object, Enum>();
		jdbcType = enumType.getJdbcType();
		Class clazz = enumType.getEnumClass();
		for (Object o : clazz.getEnumConstants()) {
			Enum e = (Enum) o;
			DbValue v = (DbValue) o;
			Object dbValue = v.getDbValue();
			tmpMap.put(dbValue, e);
		}
		this.convertToEnumMap = Collections.unmodifiableMap(tmpMap);
	}

	/**
	 * オブジェクトから永続値への設定を行う。
	 * 
	 * @param parameterSetter ResultSetへ値の設定を行うオブジェクト
	 * @param object 保存対象のオブジェクト
	 * @throws SQLException
	 * @see com.ibatis.sqlmap.client.extensions.TypeHandlerCallback#setParameter(com.ibatis.sqlmap.client.extensions.ParameterSetter,
	 * java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public void setParameter(ParameterSetter parameterSetter, Object object) throws SQLException {
		if (object == null) {
			parameterSetter.setNull(this.jdbcType);
		} else {
			DbValue enumType = (DbValue) object;
			parameterSetter.setObject(enumType.getDbValue(), this.jdbcType);
		}
	}

	/**
	 * 文字列表現を指定の型に変換する。
	 * 
	 * @param string 文字列表現
	 * @return 指定の型
	 * @see com.ibatis.sqlmap.client.extensions.TypeHandlerCallback#valueOf(java.lang.String)
	 */
	public Object valueOf(String string) {
		return string;
	}

	/**
	 * {@link java.sql.ResultSet}から結果を取得する。
	 * 
	 * @param resultGetter ResultSetから値の取得を行うオブジェクト
	 * @return 結果
	 * @throws SQLException
	 * @see com.ibatis.sqlmap.client.extensions.TypeHandlerCallback#getResult(com.ibatis.sqlmap.client.extensions.ResultGetter)
	 */
	public Object getResult(ResultGetter resultGetter) throws SQLException {
		Object value = resultGetter.getObject();
		if (resultGetter.wasNull()) {
			return null;
		}
		return this.convertToEnumMap.get(value);
	}
}
