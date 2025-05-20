package jp.co.takeda.web.cmn.action;

import java.io.File;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.co.takeda.a.bean.Box;
import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.AccessDeniedException;
import jp.co.takeda.a.exp.AuthenticationException;
import jp.co.takeda.a.exp.CertificationException;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.action.AbstractAction;
import jp.co.takeda.a.web.action.ActionMappingExt;
import jp.co.takeda.a.web.action.DelegateAction;
import jp.co.takeda.a.web.action.DelegateManager;
import jp.co.takeda.a.web.bean.CorrespondType;
import jp.co.takeda.a.web.bean.SpringUtil;
import jp.co.takeda.a.web.exp.TokenInvalidException;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.bean.Constants.SpringBeanName;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.exp.UserTimeoutException;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.cmn.bean.DpExceptionHandler;
import jp.co.takeda.web.cmn.delegate.CertificateDelegate;
import jp.co.takeda.web.cmn.delegate.ServiceControlDelegate;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionTimedOutException;

/**
 * 納入計画システム向け抽象アクションクラス
 * 
 * @author tkawabata
 */
public abstract class DpAction extends AbstractAction<DpContext, DpActionForm> {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpAction.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 例外ハンドラ
	 */
	@Autowired(required = true)
	@Qualifier("exceptionHandler")
	protected DpExceptionHandler exceptionHandler;

	/**
	 * 委譲処理一覧
	 */
	@Autowired(required = true)
	@Qualifier("delegateManager")
	protected DelegateManager<DpContext> delegateManager;

	/**
	 * 処理を委譲するDelegateを取得する。
	 * 
	 * @return DelegateList
	 * @see jp.co.takeda.a.web.action.MultiAction#getDelegateActionList()
	 */
	@Override
	protected List<DelegateAction<DpContext>> getDelegateActionList() {
		return this.delegateManager.getDelegateActionList();
	}

	/**
	 * ActionFormの初期化処理を行う。
	 * 
	 * @param ctx DpContext
	 */
	@SuppressWarnings("unchecked")
	protected void formInitlize(DpContext ctx) {

		if (LOG.isDebugEnabled()) {
			LOG.info("*** formInitlize start.");
		}
		HttpServletRequest request = ctx.getRequest();
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}

		Enumeration enu = session.getAttributeNames();
		if (enu == null) {
			return;
		}
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			Object obj = session.getAttribute(name);
			if (obj instanceof DpActionForm) {
				if (LOG.isDebugEnabled()) {
					LOG.info("form intialize object name=" + name);
				}
				DpActionForm form = (DpActionForm) obj;
				form.clearThisForm();
			}
		}
	}

	/**
	 * ビューのプロセスを規定する。
	 * 
	 * @param ctx 納入計画システムのコンテキスト
	 * @param form アクションフォームクラス
	 * @throws Exception 例外
	 */
	@Override
	protected void viewProcess(final DpContext ctx, final DpActionForm form) throws Exception {

		boolean isDownloadable = super.isDownloadable(ctx, form);
		if (isDownloadable) {
			try {
				ActionUtil.writeStream(form, ctx.getResponse());
				ActionMappingExt mapping = super.getMapping(ctx);
				mapping.openConfigured();
				mapping.addForwardConfig(new ActionForward());
				mapping.freeze();
			} catch (LogicalException e) {
				ActionMappingExt mapping = super.getMapping(ctx);
				mapping.openConfigured();
				mapping.addForwardConfig(new ActionForward());
				mapping.freeze();
			} finally {
				if (form != null) {
					form.setExportResult(null);
				}
			}
		} else {
			if (form != null) {
				form.setExportResult(null);
			}
		}
	}

	/**
	 * 納入計画システムのコンテキスト情報を作成する。
	 * 
	 * @param request リクエスト情報
	 * @param response レスポンス情報
	 * @param form アクションフォーム
	 * @return 納入計画システムのコンテキスト情報
	 * @throws Exception 例外
	 */
	@Override
	protected DpContext createContext(final HttpServletRequest request, final HttpServletResponse response, final DpActionForm form) throws Exception {
		return new DpContext(request, response);
	}

	/**
	 * 例外発生時のプロセスを規定する。
	 * 
	 * @param ctx 納入計画システムのコンテキスト
	 * @param e 例外
	 * @return 結果
	 * @throws Exception 例外
	 */
	@Override
	protected ActionResult exceptionProcess(final DpContext ctx, final DpActionForm form, final Exception e) throws Exception {

		ActionResult result = ActionResult.FAILURE;

		// DataNotFoundException
		if (e instanceof DataNotFoundException) {
			this.exceptionHandler.handleDataNotFoundException(ctx.getRequest(), (DataNotFoundException) e);
			result = ActionResult.FAILURE;
		}

		// ValidateException
		else if (e instanceof ValidateException) {
			this.exceptionHandler.handleValidateException(ctx.getRequest(), (ValidateException) e);
			result = ActionResult.FAILURE;
		}

		// UnallowableStatusException
		else if (e instanceof UnallowableStatusException) {
			this.exceptionHandler.handleUnallowableStatusException(ctx.getRequest(), (UnallowableStatusException) e);
			result = ActionResult.FAILURE;
		}

		// UserTimeoutException
		else if (e instanceof UserTimeoutException) {
			this.exceptionHandler.handleUserTimeoutException(ctx.getRequest(), (UserTimeoutException) e);			
			if(form != null){
				ctx.getRequest().setAttribute("DpAction.DIALOG_KEY", form.isDialogueFlg());
			}
			result = ActionResult.USER_TIMEOUT;
		}

		// TokenInvalidException
		else if (e instanceof TokenInvalidException) {
			this.exceptionHandler.handleTokenInvalidException(ctx.getRequest(), (TokenInvalidException) e);
			result = ActionResult.TOKENINVALID;
		}

		// CertificationException
		else if (e instanceof CertificationException) {
			this.exceptionHandler.handleCertificationException(ctx.getRequest(), (CertificationException) e);
			result = ActionResult.NOTCERTIFICATED;
		}

		// AuthenticationException
		else if (e instanceof AuthenticationException) {
			this.exceptionHandler.handleAuthenticationException(ctx.getRequest(), (AuthenticationException) e);
			result = ActionResult.AUTHORITYERROR;
		}

		// AccessDeniedException
		else if (e instanceof AccessDeniedException) {
			this.exceptionHandler.handleAccessDeniedException(ctx.getRequest(), (AccessDeniedException) e);
			result = ActionResult.ACCESS_DENIED;
		}

		// OptimisticLockingFailureException
		else if (e instanceof OptimisticLockingFailureException) {
			this.exceptionHandler.handleOptimisticLockingFailureException(ctx.getRequest(), (OptimisticLockingFailureException) e);
			result = ActionResult.FAILURE;
		}

		// DuplicateException
		else if (e instanceof DuplicateException) {
			this.exceptionHandler.handleDuplicateException(ctx.getRequest(), (DuplicateException) e);
			result = ActionResult.FAILURE;
		}

		// TransactionTimedOutException
		else if (e instanceof TransactionTimedOutException) {
			this.exceptionHandler.handleTransactionTimedOutException(ctx.getRequest(), (TransactionTimedOutException) e);
			result = ActionResult.ERROR;
		}

		// TransactionException
		else if (e instanceof TransactionException) {
			this.exceptionHandler.handleTransactionException(ctx.getRequest(), (TransactionException) e);
			result = ActionResult.ERROR;
		}

		// DataAccessException
		else if (e instanceof DataAccessException) {
			this.exceptionHandler.handleDataAccessException(ctx.getRequest(), (DataAccessException) e);
			result = ActionResult.ERROR;
		}

		// LogicalException
		else if (e instanceof LogicalException) {
			this.exceptionHandler.handleLogicalException(ctx.getRequest(), (LogicalException) e);
			result = ActionResult.FAILURE;
		}

		// SystemException
		else if (e instanceof SystemException) {
			SystemException se = (SystemException) e;
			this.exceptionHandler.handleSystemException(ctx.getRequest(), se);
			Conveyance conveyance = se.getConveyance();
			if (conveyance == null) {
				return ActionResult.ERROR;
			}
			List<MessageKey> messageKeyList = conveyance.getMessageKeyList();
			if (messageKeyList == null) {
				return ActionResult.ERROR;
			}
			for (MessageKey messageKey : messageKeyList) {
				if (ErrMessageKey.DB_DUPLICATE_ERROR.getKey().equals(messageKey.getKey()) || ErrMessageKey.OPTIMISTIC_ROCK_ERROR.getKey().equals(messageKey.getKey())) {
					return ActionResult.FAILURE;
				}
			}
			result = ActionResult.ERROR;
		}

		// Other exception
		else {
			this.exceptionHandler.handleException(ctx.getRequest(), e);
			result = ActionResult.FATAL;
		}

		// 非同期通信の場合
		CorrespondType correspondType = super.getCorrespondType(ctx, form);
		if (CorrespondType.ASYNC == correspondType) {
			result = ActionResult.ASYNC_ERROR;
		}
		return result;
	}

	/**
	 * Get Request Box.
	 * 
	 * @return Box
	 */
	protected Box getRequestBox() {
		return SpringUtil.getBean(SpringBeanName.REQUEST_BOX.value, this.getServlet().getServletContext());
	}

	/**
	 * Get Session Box.
	 * 
	 * @return Box
	 */
	protected Box getSessionBox() {
		return SpringUtil.getBean(SpringBeanName.SESSION_BOX.value, this.getServlet().getServletContext());
	}

	@Override
	protected void finalProcessHandler(final DpContext ctx) {
		modifyActionMessages(ctx);
		CertificateDelegate.clearDpuserInfo(this.getServlet().getServletContext(), ctx.getRequest());
	}

	/**
	 * 絶対パスに変換して返す。
	 * 
	 * @param entry 相対パス
	 * @return 絶対パス
	 */
	protected String getRealPath(String entry) {
		try {
			
			// 相対パスにセパレータがついているか
			boolean addSeparator = false;
			if(StringUtils.isNotEmpty(entry)){
				addSeparator = (entry.endsWith(File.separator) || entry.endsWith("/"));
			}
			
			// 絶対パス取得
			String realPath = this.getServlet().getServletContext().getRealPath(entry);
			
			// 絶対パスにセパレータがない場合は付与
			if(!realPath.endsWith(File.separator) && !realPath.endsWith("/") && addSeparator){
				realPath = realPath + File.separator;
			}
			
			return realPath;
			
		} catch (Exception e) {
			final String errMsg = "コンテキストパスを絶対パスに変換時にエラーが発生";
			throw new SystemException(new Conveyance(ErrMessageKey.FATAL_ERROR, errMsg), e);
		}
	}

	/**
	 * アクションメッセージの編集を行う。
	 * 
	 * @param ctx コンテキスト情報
	 */
	private void modifyActionMessages(final DpContext ctx) {

		final BoxKey ServiceStopAnnounce_DATA_R = ServiceControlDelegate.ServiceStopAnnounce_DATA_R;
		String msg = getRequestBox().get(ServiceStopAnnounce_DATA_R);
		if (StringUtils.isBlank(msg)) {
			return;
		}

		ActionMessages actionMessages = (ActionMessages) ctx.getRequest().getAttribute(Globals.MESSAGE_KEY);
		if (actionMessages != null && !actionMessages.isEmpty()) {
			getRequestBox().remove(ServiceStopAnnounce_DATA_R);
		}

		ActionErrors actionErrors = (ActionErrors) ctx.getRequest().getAttribute(Globals.ERROR_KEY);
		if (actionErrors != null && !actionErrors.isEmpty()) {
			getRequestBox().remove(ServiceStopAnnounce_DATA_R);
		}
	}
}
