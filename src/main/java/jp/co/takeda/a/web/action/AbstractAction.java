package jp.co.takeda.a.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.takeda.a.web.bean.Context;
import jp.co.takeda.a.web.bean.Result;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.util.Assert;

/**
 * CS Framework(Web1) 基底Actionクラス<br>
 * 
 * <pre>
 * 処理の順序および例外処理方法を規定するCSWeb1基底アクションクラス
 * 内部的に{@link MultiAction}を利用する。
 * 一連の処理の中で情報を共有するために、{@link Context}を利用する。
 * {@link Context}は、内部に{@link HttpServletRequest}および{@link HttpServletResponse}を保持するが、
 * 当クラスを利用するアプリケーション側で自由にカスタマイズ(継承)することが可能。
 * カスタマイズが必要な場合、{@link MultiAction#createContext(HttpServletRequest, HttpServletResponse, AbstractActionForm)}を実装する。
 * 各アプリケーションでは、本クラスを継承するアクションクラスをラップして利用する想定。
 * </pre>
 * 
 * @param <T> Web1用コンテキスト型
 * @param <F> Web1用アクションフォーム型
 * @author shiota
 * @author tkawabata
 */
public abstract class AbstractAction<T extends Context, F extends AbstractActionForm> extends MultiAction<T, F> {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(AbstractAction.class);

	/**
	 * エラーを表すリザルトクラス
	 * 
	 * @author tkawabata
	 */
	private enum ErrorResult implements Result {

		/**
		 * エラー
		 */
		ERROR;

		/**
		 * エラーを表す文字列を返す。
		 * 
		 * @return エラーを表す文字列
		 * @see jp.co.takeda.a.web.bean.Result#getResult()
		 */
		public String getResult() {
			return ERROR.toString().toLowerCase();
		}
	}

	/**
	 * 処理を実行する。<br>
	 * 
	 * <pre>
	 * {@link org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)}を継承する。
	 * final宣言しているため、当関数は変更出来ない。
	 * 処理の実行順序や処理内容を変更したい場合、当関数が呼び出している<code>protected</code>関数をオーバーライドする。
	 * 当関数からは、<code>HogeProcessHandler()</code>と命名された関数がコールされ、各<code>HogeProcessHandler()</code>関数からは
	 * <code>HogeProcess()</code>と命名された関数がコールされる。
	 * <code>HogeProcessHandler()</code>には処理の実行順序や振り分け処理を記述し、<code>HogeProcess()</code>関数には実際の処理を記述する。
	 * </pre>
	 * 
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param req HttpServletRequest
	 * @param res HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {

		Result result = ErrorResult.ERROR;
		Assert.isInstanceOf(ActionMappingExt.class, mapping);
		Assert.isInstanceOf(AbstractActionForm.class, form);
		F actionForm = (F) form;
		T ctx = this.createContext(req, res, actionForm);

		try {

			if (LOG.isDebugEnabled()) {
				LOG.debug("execute action start.");
			}

			// Strutsマッピング情報をリクエストに設定
			req.setAttribute(MultiAction.ACTION_MAPPING_EXT_KEY_R, mapping);

			// 処理の実行
			result = this.actionProcessHandler(ctx, actionForm);

			if (LOG.isDebugEnabled()) {
				LOG.debug("execute action end. result=" + result);
			}
		} catch (final Exception e) {
			result = this.exceptionProcessHandler(ctx, actionForm, e);
		} finally {
			this.finalProcessHandler(ctx);
			req.removeAttribute(MultiAction.ACTION_MAPPING_EXT_KEY_R);
			ctx = null;
		}

		if (result != null) {
			return mapping.findForward(result.getResult());
		} else {
			return mapping.findForward(null);
		}
	}

	// ---------------------------------------------------------
	// HANDLER METHOD
	// ---------------------------------------------------------

	/**
	 * 正常系の処理を規定する。<br>
	 * 
	 * <pre>
	 * 以下の順序で処理を呼び出す。
	 * <li>事前処理(デリゲート)</li>
	 * <li>トークン処理</li>
	 * <li>入力チェック処理</li>
	 * <li>アクション処理</li>
	 * <li>ビュー処理</li>
	 * </pre>
	 * 
	 * @param ctx Context
	 * @param form AbstractActionForm
	 * @return Result
	 * @throws Exception
	 */
	protected Result actionProcessHandler(final T ctx, final F form) throws Exception {

		// delegate
		super.delegate(ctx);

		// token
		super.token(ctx);

		// validation
		super.validate(ctx, form);

		// action
		Result result = super.execute(ctx, form);

		// view
		this.viewProcess(ctx, form);

		return result;
	}

	/**
	 * 異常系の処理を規定する。<br>
	 * 
	 * @param ctx Context
	 * @param form AbstractActionForm
	 * @param e
	 * @return Result
	 * @throws Exception
	 */
	protected Result exceptionProcessHandler(final T ctx, final F form, final Exception e) throws Exception {
		return exceptionProcess(ctx, form, e);
	}

	/**
	 * 必ず実行される処理を規定する。<br>
	 * 実装は任意。
	 * 
	 * @param ctx Context
	 */
	protected void finalProcessHandler(final T ctx) {
	}

	// ---------------------------------------------------------
	// PROCESS METHOD
	// ---------------------------------------------------------

	/**
	 * ビュー側の処理を規定する。<br>
	 * 
	 * <pre>
	 * {@link #actionProcessHandler(Context, AbstractActionForm)}の処理が完了した後の、
	 * レスポンスへの書込み処理を想定している。
	 * 例えばファイルダウンロードを行う際に、{@link HttpServletResponse#getOutputStream()}に対して
	 * 書込みを行う処理を記載する。
	 * {@link MultiAction#isDownloadable(Context, AbstractActionForm)}と合わせて利用する。
	 * </pre>
	 * 
	 * @param ctx Context
	 * @param form AbstractActionForm
	 * @throws Exception
	 * @see MultiAction#isDownloadable(Context, AbstractActionForm)
	 */
	protected abstract void viewProcess(final T ctx, final F form) throws Exception;

	/**
	 * 例外発生時の処理を規定する。<br>
	 * 
	 * @param ctx Context
	 * @param e Exception
	 * @return ActionResult
	 * @throws Exception
	 */
	protected abstract Result exceptionProcess(final T ctx, final F form, final Exception e) throws Exception;
}
