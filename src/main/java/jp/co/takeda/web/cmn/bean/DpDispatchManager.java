package jp.co.takeda.web.cmn.bean;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 転送処理を実行するマネージャークラス
 * 
 * @author tkawabata
 */
public class DpDispatchManager implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpDispatchManager.class);

	/**
	 * リダイレクト時のパス
	 */
	private String redirectPath;

	/**
	 * 指定URIにフォワードする。
	 * 
	 * @param uri フォワード先URI
	 * @param servletContext サーブレットコンテキスト
	 * @param request リクエスト情報
	 * @param response レスポンス情報
	 */
	public void handleAsForward(final String uri, final ServletContext servletContext, final HttpServletRequest request, final HttpServletResponse response) {

		try {
			final RequestDispatcher rd = servletContext.getRequestDispatcher(uri);

			if (LOG.isDebugEnabled()) {
				LOG.debug("Forwarding to " + uri);
			}
			rd.forward(request, response);
		} catch (Exception e) {
			throw new RuntimeException("Can't foward.");
		}
	}

	/**
	 * 指定URLにリダイレクトする。
	 * 
	 * @param url URL
	 * @param request リクエスト情報
	 * @param response レスポンス情報
	 * @throws IOException IO例外
	 */
	public void handleAsRedirect(final String url, final HttpServletRequest request, final HttpServletResponse response) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Redirecting to " + url);
		}
		try {
			response.sendRedirect(response.encodeRedirectURL(url));
		} catch (Exception e) {
			throw new RuntimeException("Can't redirect.");
		}
	}

	/**
	 * リダイレクトを行うパスを生成する。<br>
	 * 
	 * <pre>
	 * DELEGATEサーバ等、中間にサーバが配置される場合、
	 * 絶対パスでないと不都合が生じる可能性があるための対処。
	 * クライアントが要求したパスの末尾を書き換えて、リダイレクト先のパスとする。
	 * </pre>
	 * 
	 * @param strutsPath Strutsのパス
	 * @param request リクエスト情報
	 * @return リダイレクト先パス
	 */
	public String createRedirectPath(final String strutsPath, final HttpServletRequest request) {
		return this.redirectPath + strutsPath;
	}

	/**
	 * リダイレクト時のパスを設定する。
	 * 
	 * @param redirectPath リダイレクト時のパス
	 */
	public void setRedirectPath(String redirectPath) {
		this.redirectPath = redirectPath;
	}
}
