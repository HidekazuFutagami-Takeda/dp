package jp.co.takeda.a.web.action;

import java.lang.reflect.Method;

import javax.servlet.ServletContext;

import jp.co.takeda.a.web.bean.Context;

import org.springframework.web.context.ServletContextAware;

/**
 * アクションの事前処理を定義する。<br>
 * 
 * <pre>
 * 共有情報である{@link Context}が生成された後、アクションメソッドが実行される前に呼び出される処理を定義する。
 * 共有情報に加え、アクションメソッドにもアクセス可能なため、例えばアクションメソッドにアノテーションを追加し、
 * メソッドから取得したアノテーションを元に権限チェックを行うなどの処理を定義する。
 * Spring管理を前提としているため、ApplicationContextに当クラスを定義するか、アノテーションでBean定義する必要がある。
 * </pre>
 * 
 * @author tkawabata
 */
public abstract class DelegateAction<T extends Context> implements ServletContextAware {

	/**
	 * ServletContext
	 */
	protected ServletContext servletContext;

	/**
	 * 事前処理を実行する。
	 * 
	 * @param ctx Context
	 * @param action MultiAction
	 * @throws Exception
	 * @see MultiAction
	 * @see AbstractAction
	 */
	public abstract void execute(final T ctx, final Method action) throws Exception;

	/**
	 * Set the ServletContext that this object runs in.
	 * 
	 * @param servletContext ServletContext object to be used by this object
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
}
