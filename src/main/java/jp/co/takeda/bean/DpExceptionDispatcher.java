package jp.co.takeda.bean;

import jp.co.takeda.a.exp.AccessDeniedException;
import jp.co.takeda.a.exp.AuthenticationException;
import jp.co.takeda.a.exp.CertificationException;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.exp.TokenInvalidException;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.exp.UserTimeoutException;
import jp.co.takeda.web.cmn.bean.DpExceptionHandler;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionTimedOutException;

/**
 *納入計画システム向け例外振り分けクラス
 * 
 * @author tkawabata
 */
public class DpExceptionDispatcher {

	/**
	 * 例外処理ハンドラークラス
	 */
	private DpExceptionHandler exceptionHandler;

	/**
	 * 例外ハンドラーを取得する。
	 * 
	 * @return exceptionHandler 例外ハンドラー
	 */
	public DpExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	/**
	 * 例外ハンドラーを設定する。
	 * 
	 * @param exceptionHandler 例外ハンドラー
	 */
	public void setExceptionHandler(DpExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	/**
	 * 例外発生時の挙動を規定する。<br>
	 * 
	 * @param cause 発生した例外
	 */
	public void dispatch(final Exception e) {

		// DataNotFoundException
		if (e instanceof DataNotFoundException) {
			this.exceptionHandler.handleDataNotFoundException((DataNotFoundException) e);
		}

		// ValidateException
		else if (e instanceof ValidateException) {
			this.exceptionHandler.handleValidateException((ValidateException) e);
		}

		// TokenInvalidException
		else if (e instanceof TokenInvalidException) {
			this.exceptionHandler.handleTokenInvalidException((TokenInvalidException) e);
		}

		// CertificationException
		else if (e instanceof CertificationException) {
			this.exceptionHandler.handleCertificationException((CertificationException) e);
		}

		// AuthenticationException
		else if (e instanceof AuthenticationException) {
			this.exceptionHandler.handleAuthenticationException((AuthenticationException) e);
		}

		// AccessDeniedException
		else if (e instanceof AccessDeniedException) {
			this.exceptionHandler.handleAccessDeniedException((AccessDeniedException) e);
		}

		// OptimisticLockingFailureException
		else if (e instanceof OptimisticLockingFailureException) {
			this.exceptionHandler.handleOptimisticLockingFailureException((OptimisticLockingFailureException) e);
		}

		// DuplicateException
		else if (e instanceof DuplicateException) {
			this.exceptionHandler.handleDuplicateException((DuplicateException) e);
		}

		// TransactionTimedOutException
		else if (e instanceof TransactionTimedOutException) {
			this.exceptionHandler.handleTransactionTimedOutException((TransactionTimedOutException) e);
		}

		// TransactionException
		else if (e instanceof TransactionException) {
			this.exceptionHandler.handleTransactionException((TransactionException) e);
		}

		// DataAccessException
		else if (e instanceof DataAccessException) {
			this.exceptionHandler.handleDataAccessException((DataAccessException) e);
		}

		// UnallowableStatusException
		else if (e instanceof UnallowableStatusException) {
			this.exceptionHandler.handleUnallowableStatusException((UnallowableStatusException) e);
		}

		// UnallowableStatusException
		else if (e instanceof UnallowableStatusException) {
			this.exceptionHandler.handleUnallowableStatusException((UnallowableStatusException) e);
		}

		// UserTimeoutException
		else if (e instanceof UserTimeoutException) {
			this.exceptionHandler.handleUserTimeoutException((UserTimeoutException) e);
		}

		// SystemException
		else if (e instanceof SystemException) {
			this.exceptionHandler.handleSystemException((SystemException) e);
		}

		// Other exception
		else {
			this.exceptionHandler.handleException(e);
		}
	}
}
