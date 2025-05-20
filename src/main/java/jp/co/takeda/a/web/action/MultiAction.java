package jp.co.takeda.a.web.action;

import static jp.co.takeda.a.exp.ErrMessageKey.FATAL_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.PARSE_ERROR;
import static jp.co.takeda.a.web.exp.ErrMessageKeyExt.TOKEN_ERROR;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.Context;
import jp.co.takeda.a.web.bean.CorrespondType;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.a.web.exp.TokenInvalidException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * 基盤アクションクラス<br>
 *
 * <pre>
 * StrutsのActionクラスは1クラス１実行メソッドだが、1クラスで複数の実行メソッドが受けられるように拡張されたアクションクラスの実装。
 * またSpring-Strutsの連携を行い、当クラスを継承したアクションクラスをSpring管理下に置く。
 * Spring管理となることにより、各アクションクラスにSpringのBeanをインジェクションすることが可能である。
 * ※ ただし本クラスはシングルトンであり、インジェクション可能なBeanはシングルトンである必要がある。
 *
 * 各アクションメソッドには{@link ActionMethod}アノテーションを定義する必要がある。
 * {@link ActionMethod}アノテーションには以下の属性がある。
 * <li>メソッド種別({@link ActionMethod#methodType()})</li>
 * <li>通信種別({@link ActionMethod#correspondType()})</li>
 * <li>ダウンロード処理判定({@link ActionMethod#isDownloadable()})</li>
 *
 * [メソッド種別]
 * {@link ActionMethod#methodType()}を設定する。
 * 対象メソッドがアクションメソッドなのかバリデーションメソッドなのかを指定する。
 * デフォルトはアクションメソッドが設定されている。
 * アクションメソッドを指定する場合、アクションメソッドは以下のシグニチャである必要がある。
 * <code>public Result actionMethodName(T, F) throws Exception</code>
 *
 * バリデーションメソッドを指定する場合、バリデーションメソッドは以下のシグニチャである必要がある。
 * <code>public void validationMethodName(T, F) throws Exception</code>
 *
 * [通信種別]
 * {@link ActionMethod#correspondType()}を設定する。
 * 対象メソッドがアクションメソッドが同期通信なのか非同期通信(Ajax)なのかを指定する。
 * デフォルトは同期通信が設定されている。
 * 本属性の利用方法は、各アプリケーションに委譲される。
 *
 * [ダウンロード処理判定]
 * {@link ActionMethod#isDownloadable()}を設定する。
 * 対象メソッドがレンダリング処理なのかダウンロード処理なのかを指定する。
 * デフォルトはレンダリング処理が設定されている。
 * 本属性の利用方法は、各アプリケーションに委譲される。
 * また[メソッド種別]が{@link MethodType#ACTION}の場合、{@link #isDownloadable(Context, AbstractActionForm)}を
 * 呼び出すことで当属性にアクセスしなくてもダウンロード処理かを判定可能である。
 *
 * 以下にアクションクラスの定義例を示す。
 * 例：対象処理がアクションメソッドでダウンロード処理の場合
 * <code>
 * @ActionMethod(isDownloadable = true)
 * public Result hogeDownload(HogeContext ctx, HogeForm form) throws Exception {
 *   :
 * }
 * </code>
 *
 * また通常のStrutsと同様、Strutsの設定ファイル(Struts-config.xml)を記述し、各パスとアクションメソッドとの紐付けを定義する必要がある。
 * 以下にStrutsの設定ファイルの記述例を示す。
 *
 * <code>
 * &lt;action path="/hoge"
 *  scope="request"
 *  name="hogeActionForm"
 *  validate="true"&gt;
 *     &lt;set-property property="actionMethod" value="hogeActionMethod"/&gt;
 *     &lt;set-property property="validationMethod" value="hogeValidationMethod"/&gt;
 *     &lt;set-property property="generateToken" value="true"/&gt;
 *     &lt;forward name="success" path="/WEB-INF/view/sample/sample1.vm" /&gt;
 *     &lt;forward name="failure" path="/WEB-INF/view/sample/sample1.vm" /&gt;
 * &lt;/action&gt;
 * </code>
 * actionMethodで指定しているのがアクションメソッド名になる。
 * またhogeValidationMethodで指定しているのがバリデーションメソッド名になる。
 * </pre>
 *
 * @author tkawabata
 */
public abstract class MultiAction<T extends Context, F extends AbstractActionForm> extends Action implements BeanNameAware, InitializingBean, DisposableBean {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(MultiAction.class);

	/**
	 * アクションマッピング情報をリクエスト情報に格納する際のキー値
	 */
	protected static final String ACTION_MAPPING_EXT_KEY_R = "MultiAction.ACTION_MAPPING_EXT_KEY_R";

	/**
	 * アクションメソッドを格納するマップ
	 */
	protected Map<String, Method> actionMethodMap;

	/**
	 * バリデーションメソッドを格納するマップ
	 */
	protected Map<String, Method> validationMethodMap;

	/**
	 * SpringBean名
	 */
	protected String beanName;

	/**
	 * Springが本オブジェクトを生成する際のBeanIdを設定する。
	 *
	 * @param name the name of the bean in the factory.
	 * @see org.springframework.beans.factory.BeanNameAware#setBeanName(java.lang.String)
	 */
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	/**
	 * 本オブジェクトの初期化処理を定義する。<br>
	 *
	 * @throws Exception in the event of misconfiguration.
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@SuppressWarnings("unchecked")
	public void afterPropertiesSet() throws Exception {
		ActionMethod actionMethod = null;
		Map<String, Method> actionMap = new HashMap<String, Method>();
		Map<String, Method> validationMap = new HashMap<String, Method>();

		Method[] methods = this.getClass().getMethods();
		for (Method method : methods) {

			if (!method.isAnnotationPresent(ActionMethod.class)) {
				continue;
			}
			actionMethod = method.getAnnotation(ActionMethod.class);
			if (actionMethod.methodType() == MethodType.ACTION) {
				Class[] params = method.getParameterTypes();
				final String errMsg = "** action method signature is invalid **";
				Assert.isTrue(method.getReturnType().equals((Result.class)) && params.length == 2, errMsg);
				if (LOG.isDebugEnabled()) {
					LOG.debug("Found action method [" + method + "]");
				}
				actionMap.put(method.getName(), method);
			} else if (actionMethod.methodType() == MethodType.VALIDATION) {
				Class[] params = method.getParameterTypes();
				final String errMsg = "** validation method signature is invalid **";
				Assert.isTrue(method.getReturnType().equals((Void.TYPE)) && params.length == 2, errMsg);
				if (LOG.isDebugEnabled()) {
					LOG.debug("Found validation method [" + method + "]");
				}
				validationMap.put(method.getName(), method);
			}
		}
		// 変更不能マップに格納
		this.actionMethodMap = Collections.unmodifiableMap(actionMap);
		this.validationMethodMap = Collections.unmodifiableMap(validationMap);
	}

	/**
	 * 本オブジェクトの終了時処理を定義する。<br>
	 *
	 * @throws Exception in case of shutdown errors.
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	public void destroy() throws Exception {
		this.actionMethodMap = null;
		this.validationMethodMap = null;
	}

	/**
	 * アクションメソッドを実行する。<br>
	 * 抽象メソッドであり、実行内容は継承先のクラスに委譲する。
	 *
	 * @param mapping アクションマッピング情報
	 * @param form アクションフォーム情報
	 * @param req リクエスト情報
	 * @param res レスポンス情報
	 * @return フォワード先情報
	 * @throws Exception Exception
	 */
	public abstract ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception;

	/**
	 * 管理メソッドを取得する。
	 *
	 * @param methodType メソッドタイプ
	 * @param methodName メソッド名
	 * @return 管理メソッド
	 */
	protected Method getMethod(MethodType methodType, String methodName) {
		final String errMsg1 = "メソッドタイプが指定されていない";
		Assert.notNull(methodType, errMsg1);
		final String errMsg2 = "メソッド名が指定されていない";
		Assert.notNull(methodName, errMsg2);

		if (methodType == MethodType.ACTION) {
			return this.actionMethodMap.get(methodName);
		} else if (methodType == MethodType.VALIDATION) {
			return this.validationMethodMap.get(methodName);
		} else {
			throw new SystemException(new Conveyance(PARSE_ERROR, "unknown method type MethodType=" + methodType));
		}
	}

	/**
	 * 事前処理リストを取得する。
	 *
	 * @return 事前処理リスト
	 * @see DelegateAction
	 * @see DelegateManager
	 */
	protected abstract List<DelegateAction<T>> getDelegateActionList();

	/**
	 * 事前処理を実行する。
	 *
	 * @param ctx コンテキスト情報
	 * @throws Exception
	 */
	protected void delegate(T ctx) throws Exception {
		String methodName = getMapping(ctx).getActionMethod();
		Method method = this.getMethod(MethodType.ACTION, methodName);
		List<DelegateAction<T>> delegateActionList = getDelegateActionList();
		if (delegateActionList != null) {
			for (DelegateAction<T> delegate : delegateActionList) {
				delegate.execute(ctx, method);
			}
		}
	}

	/**
	 * トークン処理を実行する。<br>
	 *
	 * <pre>
	 * 以下にStrutsの設定ファイルの記述例を示す。
	 * [トークンの生成]
	 * <code>
	 * &lt;action path="/hoge"
	 *  scope="request"
	 *  name="hogeActionForm"
	 *  validate="false"&gt;
	 *     &lt;set-property property="actionMethod" value="hogeActionMethod"/&gt;
	 *     &lt;set-property property="generateToken" value="true"/&gt;
	 *     &lt;forward name="success" path="/WEB-INF/view/sample/sample1.vm" /&gt;
	 *     &lt;forward name="failure" path="/WEB-INF/view/sample/sample1.vm" /&gt;
	 * &lt;/action&gt;
	 * </code>
	 *
	 * [トークンの検証]
	 * <code>
	 * &lt;action path="/hoge"
	 *  scope="request"
	 *  name="hogeActionForm"
	 *  validate="false"&gt;
	 *     &lt;set-property property="actionMethod" value="hogeActionMethod"/&gt;
	 *     &lt;set-property property="checkToken" value="true"/&gt;
	 *     &lt;forward name="success" path="/WEB-INF/view/sample/sample1.vm" /&gt;
	 *     &lt;forward name="failure" path="/WEB-INF/view/sample/sample1.vm" /&gt;
	 *     &lt;forward name="input" path="/WEB-INF/view/sample/sample1.vm" /&gt;
	 *     &lt;forward name="tokenInvalid" path="/WEB-INF/view/sample/tokenInvalid.vm" /&gt;
	 * &lt;/action&gt;
	 * </code>
	 * </pre>
	 *
	 * @param コンテキスト情報
	 * @throws TokenInvalidException
	 */
	protected void token(T ctx) throws TokenInvalidException {

		ActionMappingExt mapping = getMapping(ctx);
		HttpServletRequest request = ctx.getRequest();

		// generate token
		final boolean generateToken = mapping.isGenerateToken();
		if (generateToken) {
			if (LOG.isDebugEnabled()) {
				final String msg = "generate and save token into this request";
				LOG.debug(msg);
			}
			super.saveToken(request);
			return;
		}

		// token check
		final boolean checkToken = mapping.isCheckToken();
		if (checkToken) {
			if (LOG.isDebugEnabled()) {
				final String msg = "check token";
				LOG.debug(msg);
			}
			final boolean result = super.isTokenValid(request, true);
			if (result == false) {
				final String errMsg = "token invalid";
				throw new TokenInvalidException(new Conveyance(TOKEN_ERROR, errMsg));
			}
			this.saveToken(request);
		}
	}

	/**
	 * バリデーションメソッドを実行し、入力検証を行う。
	 *
	 * <pre>
	 * 以下にStrutsの設定ファイルの記述例を示す。
	 * <code>
	 * &lt;action path="/hoge"
	 *  scope="request"
	 *  name="hogeActionForm"
	 *  validate="true"&gt;
	 *     &lt;set-property property="actionMethod" value="hogeActionMethod" /&gt;
	 *     &lt;set-property property="checkToken" value="true" /&gt;
	 *     &lt;set-property property="validationMethod" value="hogeValidationMethod" /&gt;
	 *     &lt;forward name="success" path="/WEB-INF/view/sample/sample1.vm" /&gt;
	 *     &lt;forward name="failure" path="/WEB-INF/view/sample/sample1.vm" /&gt;
	 *     &lt;forward name="input" path="/WEB-INF/view/sample/sample1.vm" /&gt;
	 *     &lt;forward name="tokenInvalid" path="/WEB-INF/view/sample/tokenInvalid.vm" /&gt;
	 * &lt;/action&gt;
	 * </code>
	 * </pre>
	 *
	 * @param ctx コンテキスト情報
	 * @param mapping ActionMappingExt
	 * @param form ActionForm
	 * @throws Exception
	 */
	protected void validate(T ctx, F form) throws Exception {
		ActionMappingExt mapping = getMapping(ctx);
		if (!mapping.getValidate()) {
			return;
		}
		try {
			String methodName = mapping.getValidationMethod();
			Method method = this.getMethod(MethodType.VALIDATION, methodName);
			method.invoke(this, new Object[] { ctx, form });
		} catch (final Exception e) {
			throw findException(e);
		}
	}

	/**
	 * アクション処理を実行する。<br>
	 *
	 * <pre>
	 * <code>
	 * &lt;action path="/hoge"
	 *  scope="request"
	 *  name="hogeActionForm"
	 *  validate="true"&gt;
	 *     &lt;set-property property="actionMethod" value="hogeActionMethod" /&gt;
	 *     &lt;set-property property="checkToken" value="true" /&gt;
	 *     &lt;set-property property="validationMethod" value="hogeValidationMethod" /&gt;
	 *     &lt;forward name="success" path="/WEB-INF/view/sample/sample1.vm" /&gt;
	 *     &lt;forward name="failure" path="/WEB-INF/view/sample/sample1.vm" /&gt;
	 *     &lt;forward name="input" path="/WEB-INF/view/sample/sample1.vm" /&gt;
	 *     &lt;forward name="tokenInvalid" path="/WEB-INF/view/sample/tokenInvalid.vm" /&gt;
	 * &lt;/action&gt;
	 * </code>
	 * </pre>
	 *
	 * @param ctx コンテキスト情報
	 * @param form ActionForm
	 * @return Result
	 * @throws Exception
	 */
	protected Result execute(T ctx, F form) throws Exception {
		try {
			String methodName = getMapping(ctx).getActionMethod();
			Method method = this.getMethod(MethodType.ACTION, methodName);
			return (Result) method.invoke(this, new Object[] { ctx, form });
		} catch (final Exception e) {
			throw findException(e);
		}
	}

	/**
	 * リフレクション経由で処理を呼び出した際に、InvocationTargetExceptionから発生元の例外を取得する。
	 *
	 * @param e original exception 発生した例外
	 * @return Exception 発生元の例外
	 */
	protected Exception findException(final Exception e) {
		Exception cause = e;
		if (e instanceof InvocationTargetException) {
			if (e.getCause() != null && e.getCause() instanceof Exception) {
				cause = (Exception) e.getCause();
			} else {
				throw new SystemException(new Conveyance(FATAL_ERROR, "fatal error"), e);
			}
		}
		return cause;
	}

	/**
	 * コンテキスト情報(共有情報)を生成する。
	 *
	 * @param request リクエスト情報
	 * @param response レスポンス情報
	 * @param form アクションフォーム
	 * @return T (任意の型)
	 * @throws Exception
	 */
	protected abstract T createContext(final HttpServletRequest request, final HttpServletResponse response, final F form) throws Exception;

	/**
	 * 通信種別を取得する。
	 *
	 * @param ctx コンテキスト情報
	 * @param form アクションフォーム
	 * @return CorrespondType 通信種別
	 */
	protected CorrespondType getCorrespondType(T ctx, F form) {
		ActionMappingExt mapping = getMapping(ctx);
		String methodName = mapping.getActionMethod();
		Method method = this.getMethod(MethodType.ACTION, methodName);
		if (method.isAnnotationPresent(ActionMethod.class)) {
			ActionMethod actionMethod = method.getAnnotation(ActionMethod.class);
			return actionMethod.correspondType();
		} else {
			return null;
		}
	}

	/**
	 * ダウンロード処理かを返す。<br>
	 *
	 * @param ctx コンテキスト情報
	 * @param form アクションフォーム
	 * @return ダウンロード処理ならば真
	 */
	protected boolean isDownloadable(T ctx, F form) {
		ActionMappingExt mapping = getMapping(ctx);
		String methodName = mapping.getActionMethod();
		Method method = this.getMethod(MethodType.ACTION, methodName);
		if (method.isAnnotationPresent(ActionMethod.class)) {
			ActionMethod actionMethod = method.getAnnotation(ActionMethod.class);
			return actionMethod.isDownloadable();
		} else {
			return false;
		}
	}

	/**
	 * メッセージをリクエスト情報に設定する。
	 *
	 * @param ctx コンテキスト情報
	 * @param messageKey メッセージキー
	 */
	protected void addMessage(final T ctx, final MessageKey messageKey) {
		final HttpServletRequest request = ctx.getRequest();
		MessageUtil.addMessage(request, messageKey);
	}

	/**
	 * メッセージをリクエスト情報に設定する。
	 *
	 * @param ctx コンテキスト情報
	 * @param conveyance 伝達オブジェクト
	 * @param altKey 代替キー
	 */
	protected void addMessage(final T ctx, Conveyance conveyance, MessageKey altKey) {
		List<MessageKey> msgKeyList = conveyance.getMessageKeyList();
		if (msgKeyList != null && msgKeyList.size() > 0) {
			for (MessageKey msgKey : msgKeyList) {
				addMessage(ctx, msgKey);
			}
		} else {
			if (altKey != null) {
				addMessage(ctx, altKey);
			}
		}
	}

	/**
	 * エラーメッセージをリクエスト情報に設定する。
	 *
	 * @param ctx コンテキスト情報
	 * @param messageKey メッセージキー
	 */
	protected void addErrorMessage(final T ctx, final MessageKey messageKey) {
		final HttpServletRequest request = ctx.getRequest();
		MessageUtil.addErrorMessage(request, messageKey);
	}

	/**
	 * エラーメッセージをリクエスト情報に設定する。
	 *
	 * @param ctx コンテキスト情報
	 * @param conveyance 伝達オブジェクト
	 * @param altKey 代替キー
	 */
	protected void addErrorMessage(final T ctx, Conveyance conveyance, MessageKey altKey) {
		List<MessageKey> msgKeyList = conveyance.getMessageKeyList();
		if (msgKeyList != null && msgKeyList.size() > 0) {
			for (MessageKey msgKey : msgKeyList) {
				addErrorMessage(ctx, msgKey);
			}
		} else {
			addErrorMessage(ctx, altKey);
		}
	}

	/**
	 * アクションマッピング情報をリクエスト情報から取得する。
	 *
	 * @param ctx コンテキスト情報
	 * @return ActionMappingExt アクションマッピング情報
	 */
	protected ActionMappingExt getMapping(T ctx) {
		return (ActionMappingExt) ctx.getRequest().getAttribute(ACTION_MAPPING_EXT_KEY_R);
	}
}
