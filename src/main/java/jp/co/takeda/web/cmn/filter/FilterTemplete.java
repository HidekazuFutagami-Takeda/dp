package jp.co.takeda.web.cmn.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.SystemException;

/**
 * サーブレットフィルターのテンプレート
 * 
 * @author tkawabata
 */
public abstract class FilterTemplete implements Filter {

	/**
	 * @see javax.servlet.FilterConfig
	 */
	protected FilterConfig filterConfig;

	/**
	 * 初期化処理を行う。
	 * 
	 * @param filterConfig 設定情報
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(final FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	/**
	 * 終了処理を行う。
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		this.filterConfig = null;
	}

	/**
	 * フィルター処理を行う。
	 * 
	 * @param req リクエスト情報
	 * @param res レスポンス情報
	 * @param chain 後続
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
	 * javax.servlet.FilterChain)
	 */
	public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request;
		HttpServletResponse response;
		if (req instanceof HttpServletRequest && res instanceof HttpServletResponse) {
			request = (HttpServletRequest) req;
			response = (HttpServletResponse) res;
		} else {
			final String errMsg = "This filter just supports HTTP requests";
			throw new SystemException(new Conveyance(ErrMessageKey.PARAMETER_ERROR, errMsg));
		}
		try {

			// 処理を呼び出し
			this.execute(request, response);

			// 後続の処理を呼び出す
			chain.doFilter(request, response);

		} finally {

			this.finallProcess(request, response);
		}
	}

	/**
	 * 具体的なフィルター処理を記述する。
	 * 
	 * @param request リクエスト情報
	 * @param response レスポンス情報
	 */
	public abstract void execute(HttpServletRequest request, HttpServletResponse response);

	/**
	 * 全ての後続のフィルター処理を呼び出した後のfinally節での処理を記述する。
	 * 
	 * @param request リクエスト情報
	 * @param response レスポンス情報
	 */
	public void finallProcess(final HttpServletRequest request, final HttpServletResponse response) {
	}
}
