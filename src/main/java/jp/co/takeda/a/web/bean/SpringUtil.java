package jp.co.takeda.a.web.bean;

import javax.servlet.ServletContext;

import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Springのユーティリティクラス
 * 
 * @author tkawabata
 */
public abstract class SpringUtil {

	/**
	 * 型変換をしてSpringBeanを返す。
	 * 
	 * @param <T> 任意の型T
	 * @param beanId Spring Bean Id
	 * @param servletContext ServletContext
	 * @return <T> 任意の型T
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanId, ServletContext servletContext) {
		return (T) WebApplicationContextUtils.getWebApplicationContext(servletContext).getBean(beanId);
	}
}
