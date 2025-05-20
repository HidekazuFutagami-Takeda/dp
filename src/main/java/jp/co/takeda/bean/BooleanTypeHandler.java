package jp.co.takeda.bean;

import java.sql.SQLException;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

/**
 * フラグ(TRUE/FALSE)を<code>java.lang.Boolean</code>に変換するタイプハンドラ
 * 
 * @author tkawabata
 */
public class BooleanTypeHandler implements TypeHandlerCallback {

	/**
	 * TRUE
	 */
	private static final String TRUE = "1";

	/**
	 * FALSE
	 */
	private static final String FALSE = "0";

	public Object getResult(ResultGetter getter) throws SQLException {

		String code = getter.getString();
		if (code == null) {
			return null;
		}
		if (TRUE.equalsIgnoreCase(code)) {
			return Boolean.valueOf(true);
		} else {
			return Boolean.valueOf(false);
		}
	}

	public void setParameter(ParameterSetter setter, Object parameter) throws SQLException {
		boolean b = ((Boolean) parameter).booleanValue();
		if (b) {
			setter.setString(TRUE);
		} else {
			setter.setString(FALSE);
		}
	}

	public Object valueOf(String s) {
		if (TRUE.equalsIgnoreCase(s)) {
			return Boolean.valueOf(true);
		} else {
			return Boolean.valueOf(false);
		}
	}
}
