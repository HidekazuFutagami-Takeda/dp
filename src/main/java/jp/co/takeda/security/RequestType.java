package jp.co.takeda.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * リクエスト内容を表すアノテーション
 * 
 * @author tkawabata
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestType {

	/**
	 * 業務種別を指定する。
	 * 
	 * @return 業務種別
	 */
	BusinessType businessType() default BusinessType.IYAKU;
}
