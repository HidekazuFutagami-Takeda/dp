package jp.co.takeda.web.cmn.bean;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.AccessDeniedException;
import jp.co.takeda.a.exp.AuthenticationException;
import jp.co.takeda.a.exp.CertificationException;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.action.MessageUtil;
import jp.co.takeda.a.web.exp.ErrMessageKeyExt;
import jp.co.takeda.a.web.exp.TokenInvalidException;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.exp.UserTimeoutException;
import jp.co.takeda.util.ExceptionUtil;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionTimedOutException;

/**
 * 納入計画システムの例外ハンドラークラス
 * 
 * @author tkawabata
 */
public class DpExceptionHandler implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpExceptionHandler.class);

	/**
	 * WEBではないことを示す文字列
	 */
	private static final String NOT_WEB_STRING = "＋＋＋";

	// --------------------------
	// INFO LEVEL
	// --------------------------

	/**
	 * データが見つからない場合の処理を実行する。
	 * 
	 * @param e DataNotFound例外
	 */
	public void handleDataNotFoundException(DataNotFoundException e) {
		Conveyance conveyance = e.getConveyance();
		if (LOG.isInfoEnabled()) {
			LOG.info(NOT_WEB_STRING + conveyance.getMessage());
		}
	}

	/**
	 * [WEB用]データが見つからない場合の処理を実行する。
	 * 
	 * @param request リクエスト情報
	 * @param e DataNotFound例外
	 */
	public void handleDataNotFoundException(HttpServletRequest request, DataNotFoundException e) {
		Conveyance conveyance = e.getConveyance();
		MessageUtil.addErrorMessage(request, conveyance, ErrMessageKey.DATA_NOT_FOUND_ERROR);
		if (LOG.isInfoEnabled()) {
			LOG.info(conveyance.getMessage(), e);
		}
	}

	/**
	 * 入力チェックエラーの場合の処理を実行する。
	 * 
	 * @param e 入力チェック例外
	 */
	public void handleValidateException(ValidateException e) {
		Conveyance conveyance = e.getConveyance();
		if (LOG.isInfoEnabled()) {
			LOG.info(NOT_WEB_STRING + conveyance.getMessage(), e);
		}
	}

	/**
	 * [WEB用]入力チェックエラーの場合の処理を実行する。
	 * 
	 * @param request リクエスト情報
	 * @param e 入力チェック例外
	 */
	public void handleValidateException(HttpServletRequest request, ValidateException e) {
		Conveyance conveyance = e.getConveyance();
		MessageUtil.addErrorMessage(request, conveyance, ErrMessageKey.VALIDATE_ERROR);
		if (LOG.isInfoEnabled()) {
			LOG.info(conveyance.getMessage(), e);
		}
	}

	/**
	 * 不許可ステータスの場合の処理を実行する。
	 * 
	 * @param e 不許可ステータス例外
	 */
	public void handleUnallowableStatusException(UnallowableStatusException e) {
		Conveyance conveyance = e.getConveyance();
		if (LOG.isInfoEnabled()) {
			LOG.info(NOT_WEB_STRING + conveyance.getMessage(), e);
		}
	}

	/**
	 * [WEB用]不許可ステータスの場合の処理を実行する。
	 * 
	 * @param request リクエスト情報
	 * @param e 不許可ステータス例外
	 */
	public void handleUnallowableStatusException(HttpServletRequest request, UnallowableStatusException e) {
		Conveyance conveyance = e.getConveyance();
		MessageUtil.addErrorMessage(request, conveyance, ErrMessageKey.STATE_ERROR);
		if (LOG.isInfoEnabled()) {
			LOG.info(conveyance.getMessage(), e);
		}
	}

	/**
	 * タイムアウトエラーの場合の処理を実行する。
	 * 
	 * @param e タイムアウト例外
	 */
	public void handleUserTimeoutException(UserTimeoutException e) {
		Conveyance conveyance = e.getConveyance();
		if (LOG.isInfoEnabled()) {
			LOG.info(NOT_WEB_STRING + conveyance.getMessage(), e);
		}
	}

	/**
	 * [WEB用]タイムアウトエラーの場合の処理を実行する。
	 * 
	 * @param request リクエスト情報
	 * @param e タイムアウト例外
	 */
	public void handleUserTimeoutException(HttpServletRequest request, UserTimeoutException e) {
		Conveyance conveyance = e.getConveyance();
		MessageUtil.addErrorMessage(request, conveyance, ErrMessageKey.ACCESS_DENIED_ERROR);
		if (LOG.isInfoEnabled()) {
			LOG.info(conveyance.getMessage(), e);
		}
	}

	// --------------------------
	// WARN LEVEL
	// --------------------------

	/**
	 * トークンエラー発生時の処理を実行する。
	 * 
	 * @param e トークン例外
	 */
	public void handleTokenInvalidException(TokenInvalidException e) {
		Conveyance conveyance = e.getConveyance();
		if (LOG.isWarnEnabled()) {
			LOG.warn(NOT_WEB_STRING + conveyance.getMessage(), e);
		}
	}

	/**
	 * [WEB用]トークンエラー発生時の処理を実行する。
	 * 
	 * @param request リクエスト情報
	 * @param e トークン例外
	 */
	public void handleTokenInvalidException(HttpServletRequest request, TokenInvalidException e) {
		Conveyance conveyance = e.getConveyance();
		MessageUtil.addErrorMessage(request, conveyance, ErrMessageKeyExt.TOKEN_ERROR);
		if (LOG.isWarnEnabled()) {
			LOG.warn(conveyance.getMessage(), e);
		}
	}

	/**
	 * 論理例外発生時の処理を実行する。
	 * 
	 * @param e 論理例外
	 */
	public void handleLogicalException(LogicalException e) {
		Conveyance conveyance = e.getConveyance();
		if (LOG.isWarnEnabled()) {
			LOG.warn(NOT_WEB_STRING + conveyance.getMessage(), e);
		}
	}

	/**
	 * [WEB用]論理例外発生時の処理を実行する。
	 * 
	 * @param request リクエスト情報
	 * @param e 論理例外
	 */
	public void handleLogicalException(HttpServletRequest request, LogicalException e) {
		Conveyance conveyance = e.getConveyance();
		MessageUtil.addErrorMessage(request, conveyance, ErrMessageKey.LOGICAL_ERROR);
		if (LOG.isWarnEnabled()) {
			LOG.warn(conveyance.getMessage(), e);
		}
	}

	// --------------------------
	// ERROR LEVEL
	// --------------------------

	/**
	 * 認証エラー発生時の処理を実行する。
	 * 
	 * @param e 認証例外
	 */
	public void handleCertificationException(CertificationException e) {
		Conveyance conveyance = e.getConveyance();
		if (LOG.isWarnEnabled()) {
			LOG.warn(NOT_WEB_STRING + conveyance.getMessage(), e);
		}
	}

	/**
	 * [WEB用]認証エラー発生時の処理を実行する。
	 * 
	 * @param request リクエスト情報
	 * @param e 認証例外
	 */
	public void handleCertificationException(HttpServletRequest request, CertificationException e) {
		Conveyance conveyance = e.getConveyance();
		MessageUtil.addErrorMessage(request, conveyance, ErrMessageKey.CERTIFICATION_ERROR);
		if (LOG.isWarnEnabled()) {
			LOG.warn(conveyance.getMessage(), e);
		}
	}

	/**
	 * 認可エラー発生時の処理を実行する。
	 * 
	 * @param e 認可例外
	 */
	public void handleAuthenticationException(AuthenticationException e) {
		Conveyance conveyance = e.getConveyance();
		if (LOG.isWarnEnabled()) {
			LOG.warn(NOT_WEB_STRING + conveyance.getMessage(), e);
		}
	}

	/**
	 * [WEB用]認可エラー発生時の処理を実行する。
	 * 
	 * @param request リクエスト情報
	 * @param e 認可例外
	 */
	public void handleAuthenticationException(HttpServletRequest request, AuthenticationException e) {
		Conveyance conveyance = e.getConveyance();
		MessageUtil.addErrorMessage(request, conveyance, ErrMessageKey.AUTHORIZATION_ERROR);
		if (LOG.isWarnEnabled()) {
			LOG.warn(conveyance.getMessage(), e);
		}
	}

	/**
	 * アクセス拒否例外発生時の処理を実行する。
	 * 
	 * @param e アクセス拒否例外
	 */
	public void handleAccessDeniedException(AccessDeniedException e) {
		Conveyance conveyance = e.getConveyance();
		if (LOG.isWarnEnabled()) {
			LOG.warn(NOT_WEB_STRING + conveyance.getMessage(), e);
		}
	}

	/**
	 * [WEB用]アクセス拒否例外発生時の処理を実行する。
	 * 
	 * @param request リクエスト情報
	 * @param e アクセス拒否例外
	 */
	public void handleAccessDeniedException(HttpServletRequest request, AccessDeniedException e) {
		Conveyance conveyance = e.getConveyance();
		MessageUtil.addErrorMessage(request, conveyance, ErrMessageKey.ACCESS_DENIED_ERROR);
		if (LOG.isWarnEnabled()) {
			LOG.warn(conveyance.getMessage(), e);
		}
	}

	/**
	 * データアクセス例外発生時の処理を実行する。
	 * 
	 * @param e データアクセス例外
	 */
	public void handleDataAccessException(DataAccessException e) {
		if (LOG.isErrorEnabled()) {
			DataAccessException de = (DataAccessException) e;
			LOG.error(NOT_WEB_STRING + de.getClass().getName() + "Find SQLException...");
			Exception targetException = ExceptionUtil.findException(de);
			String msg = "DataAccessException: " + e.getClass().getName() + " ";
			String targetMsg = msg + ExceptionUtil.buildLogMessage(targetException);
			LOG.error(NOT_WEB_STRING + targetMsg);
			LOG.error(ExceptionUtils.getFullStackTrace(e));
		}
	}

	/**
	 * [WEB用]データアクセス例外発生時の処理を実行する。
	 * 
	 * @param request リクエスト情報
	 * @param e データアクセス例外
	 */
	public void handleDataAccessException(HttpServletRequest request, DataAccessException e) {
		MessageUtil.addErrorMessage(request, ErrMessageKey.DB_ERROR);
		if (LOG.isErrorEnabled()) {
			DataAccessException de = (DataAccessException) e;
			LOG.error(de.getClass().getName() + "Find SQLException...");
			Exception targetException = ExceptionUtil.findException(de);
			String msg = "DataAccessException: " + e.getClass().getName() + " ";
			String targetMsg = msg + ExceptionUtil.buildLogMessage(targetException);
			LOG.error(targetMsg);
			LOG.error(ExceptionUtils.getFullStackTrace(e));
		}
	}

	/**
	 * 重複例外時の処理を実行する。
	 * 
	 * @param e 重複例外
	 */
	public void handleDuplicateException(DuplicateException e) {
		Conveyance conveyance = e.getConveyance();
		if (LOG.isErrorEnabled()) {
			LOG.error(NOT_WEB_STRING + conveyance.getMessage());
			LOG.error(ExceptionUtils.getFullStackTrace(e));
		}
	}

	/**
	 * [WEB用]重複例外時の処理を実行する。
	 * 
	 * @param request リクエスト情報
	 * @param e 重複例外
	 */
	public void handleDuplicateException(HttpServletRequest request, DuplicateException e) {
		Conveyance conveyance = e.getConveyance();
		MessageUtil.addErrorMessage(request, conveyance, ErrMessageKey.DB_DUPLICATE_ERROR);
		if (LOG.isErrorEnabled()) {
			LOG.error(conveyance.getMessage());
			LOG.error(ExceptionUtils.getFullStackTrace(e));
		}
	}

	/**
	 * トランザクションタイムアウト発生時の処理を実行する。
	 * 
	 * @param e 例外
	 */
	public void handleTransactionTimedOutException(TransactionTimedOutException e) {
		if (LOG.isErrorEnabled()) {
			LOG.error(NOT_WEB_STRING + e);
			LOG.error(ExceptionUtils.getFullStackTrace(e));
		}
	}

	/**
	 * [WEB用]トランザクションタイムアウト発生時の処理を実行する。
	 * 
	 * @param request リクエスト情報
	 * @param e 例外
	 */
	public void handleTransactionTimedOutException(HttpServletRequest request, TransactionTimedOutException e) {
		MessageUtil.addErrorMessage(request, ErrMessageKey.TX_TIMEOUT_ERROR);
		if (LOG.isErrorEnabled()) {
			LOG.error(e);
			LOG.error(ExceptionUtils.getFullStackTrace(e));
		}
	}

	/**
	 * トランザクション例外発生時の処理を実行する。
	 * 
	 * @param e 例外
	 */
	public void handleTransactionException(TransactionException e) {
		if (LOG.isErrorEnabled()) {
			LOG.error(NOT_WEB_STRING + e);
			LOG.error(ExceptionUtils.getFullStackTrace(e));
		}
	}

	/**
	 * [WEB用]トランザクション例外発生時の処理を実行する。
	 * 
	 * @param request リクエスト情報
	 * @param e 例外
	 */
	public void handleTransactionException(HttpServletRequest request, TransactionException e) {
		MessageUtil.addErrorMessage(request, ErrMessageKey.TX_ERROR);
		if (LOG.isErrorEnabled()) {
			LOG.error(e);
			LOG.error(ExceptionUtils.getFullStackTrace(e));
		}
	}

	/**
	 * 楽観的ロック発生時の処理を実行する。
	 * 
	 * @param e 例外
	 */
	public void handleOptimisticLockingFailureException(OptimisticLockingFailureException e) {
		if (LOG.isErrorEnabled()) {
			LOG.error(NOT_WEB_STRING + e);
			LOG.error(ExceptionUtils.getFullStackTrace(e));
		}
	}

	/**
	 * [WEB用]楽観的ロック発生時の処理を実行する。
	 * 
	 * @param request リクエスト情報
	 * @param e 例外
	 */
	public void handleOptimisticLockingFailureException(HttpServletRequest request, OptimisticLockingFailureException e) {
		MessageUtil.addErrorMessage(request, ErrMessageKey.OPTIMISTIC_ROCK_ERROR);
		if (LOG.isErrorEnabled()) {
			LOG.error(e);
			LOG.error(ExceptionUtils.getFullStackTrace(e));
		}
	}

	/**
	 * システム例外発生時の処理を実行する。
	 * 
	 * @param e システム例外
	 */
	public void handleSystemException(SystemException e) {
		Conveyance conveyance = e.getConveyance();
		if (LOG.isErrorEnabled()) {
			LOG.error(NOT_WEB_STRING + conveyance.getMessage());
			LOG.error(ExceptionUtils.getFullStackTrace(e));
		}
	}

	/**
	 * [WEB用]システム例外発生時の処理を実行する。
	 * 
	 * @param request リクエスト情報
	 * @param e システム例外
	 */
	public void handleSystemException(HttpServletRequest request, SystemException e) {
		Conveyance conveyance = e.getConveyance();
		MessageUtil.addErrorMessage(request, conveyance, ErrMessageKey.SYSTEM_ERROR);
		if (LOG.isErrorEnabled()) {
			LOG.error(conveyance.getMessage());
			LOG.error(ExceptionUtils.getFullStackTrace(e));
		}
	}

	// --------------------------
	// FATAL LEVEL
	// --------------------------

	/**
	 * その他例外発生時の処理を実行する。
	 * 
	 * @param e その他例外
	 */
	public void handleException(Exception e) {
		if (LOG.isFatalEnabled()) {
			LOG.fatal(NOT_WEB_STRING + e);
			LOG.fatal(ExceptionUtils.getFullStackTrace(e));
		}
	}

	/**
	 * [WEB用]その他例外発生時の処理を実行する。
	 * 
	 * @param request リクエスト情報
	 * @param e その他例外
	 */
	public void handleException(HttpServletRequest request, Exception e) {
		MessageUtil.addErrorMessage(request, ErrMessageKey.FATAL_ERROR);
		if (LOG.isFatalEnabled()) {
			LOG.fatal(e);
			LOG.fatal(ExceptionUtils.getFullStackTrace(e));
		}
	}
}
