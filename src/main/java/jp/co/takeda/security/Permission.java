package jp.co.takeda.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jp.co.takeda.security.DpAuthority.AuthType;

/**
 * 許可を表すアノテーション[1つ目]
 *
 * @author tkawabata
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {


	/**
	 * 権限種別を指定する。
	 *
	 * @return 権限種別
	 */
	AuthType authType();
}
