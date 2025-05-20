package jp.co.takeda.a.web.action;

import java.util.List;

import javax.servlet.ServletContext;

import jp.co.takeda.a.web.bean.Context;

import org.springframework.web.context.ServletContextAware;

/**
 * {@link DelegateAction}を管理・実行するマネージャークラス<br>
 * 
 * <pre>
 * メンバに{@link DelegateAction}のリストを保持する。
 * 保持しているリスト内の{@link DelegateAction}をリストの順序に従って実行する。
 * Spring管理を前提としているため、ApplicationContextに当クラスを定義するか、アノテーションでBean定義する必要がある。
 * 以下に{@link DelegateAction}を含めた定義例を示す。
 *
 * [applicationContext.xml]
 * <code>
 * &lt;code&gt;
 * &lt;bean id="delegateManager" class="jp.co.hoge.HogeDelegateManager"&gt;
 * &lt;property name="delegateActionList"&gt;
 *   &lt;list&gt;
 *     &lt;ref bean="hogeDelegate" /&gt;
 *     &lt;ref bean="fooDelegate" /&gt;
 *     &lt;ref bean="barDelegate" /&gt;
 *   &lt;/list&gt;
 * &lt;/property&gt;
 * &lt;/bean&gt;
 * &lt;/code&gt;
 * </code>
 *
 * [HogeDelegate.java]
 * <code>
 * @Controller("hogeDelegate")
 * public class HogeDelegate extends HogeDelegateActionExtends {
 *
 *     @Override
 *     public void execute(HogeContext ctx, Method action) throws Exception {
 *        :
 *     }
 * }
 * </code>
 *
 * Spring管理にした当クラスをアクションクラスから呼び出すために、{@link AbstractAction}を継承した
 * 各アプリケーションの規定アクションクラス内に以下の要領で設定を行う。
 * [HogeBaseAction.java]
 * <code>
 * public abstract class HogeBaseAction extends AbstractAction<HogeContext, HogeActionForm> {
 *     @Autowired(required = true)
 *     @Qualifier("delegateManager")
 *     protected DelegateManager<HogeContext> delegateManager;
 *
 *     @Override
 *     protected List<DelegateAction<HogeContext>> getDelegateActionList() {
           return this.delegateManager.getDelegateActionList();
 *     }
 * }
 * </code>
 * </pre>
 * 
 * @see DelegateAction
 * @author tkawabata
 */
public class DelegateManager<T extends Context> implements ServletContextAware {

	/**
	 * ServletContext
	 */
	protected ServletContext servletContext;

	/**
	 * 事前処理を行うDelegateActionのリスト
	 */
	protected List<DelegateAction<T>> delegateActionList;

	/**
	 * Set the ServletContext that this object runs in.
	 * 
	 * @param servletContext ServletContext object to be used by this object
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	/**
	 * Get the ServletContext.
	 * 
	 * @return servletContext
	 */
	public ServletContext getServletContext() {
		return servletContext;
	}

	/**
	 * DelegateActionのリストを取得する。
	 * 
	 * @return DelegateActionのリスト
	 */
	public List<DelegateAction<T>> getDelegateActionList() {
		return delegateActionList;
	}

	/**
	 * DelegateActionのリストを設定する。
	 * 
	 * @param delegateActionList DelegateActionのリスト
	 */
	public void setDelegateActionList(List<DelegateAction<T>> delegateActionList) {
		this.delegateActionList = delegateActionList;
	}
}
