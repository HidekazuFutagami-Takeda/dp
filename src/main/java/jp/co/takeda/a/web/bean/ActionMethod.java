package jp.co.takeda.a.web.bean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * アクションのメソッド種別を表すアノテーション
 * 
 * @author tkawabata
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionMethod {

	/**
	 * メソッド種別を取得する。<br>
	 * デフォルトアクションメソッド
	 * 
	 * @return メソッド種別
	 */
	MethodType methodType() default MethodType.ACTION;

	/**
	 * 通信種別を取得する。<br>
	 * デフォルト同期通信
	 * 
	 * @return 通信種別
	 */
	CorrespondType correspondType() default CorrespondType.SYNC;

	/**
	 * ダウンロード処理判定を取得する。<br>
	 * デフォルトFALSE
	 * 
	 * @return ダウンロード形態ならば真
	 */
	boolean isDownloadable() default false;
}
