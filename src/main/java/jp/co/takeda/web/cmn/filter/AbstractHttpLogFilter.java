package jp.co.takeda.web.cmn.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.NDC;

/**
 * 横断的にログをトレースするためのサーブレットフィルター<br>
 * LOG4Jの{@link org.apache.log4j.NDC}を利用して横断的なログ出力を可能にする。
 * 
 * @see org.apache.log4j.NDC
 * @author tkawabata
 */
public abstract class AbstractHttpLogFilter extends FilterTemplete {

	/**
	 * Log4Jの{@link NDC}を利用することにより、横断的にログをトレースできるようにする。<br>
	 * 
	 * <pre>
	 * {@link NDC}を利用することにより、横断的にログをトレースできるようにする。
	 * NDCクラスのメソッドは、カレントスレッドに対しての操作になるので、排他制御を意識する必要がない。
	 * </pre>
	 * 
	 * @param request リクエスト情報
	 * @param response レスポンス情報
	 */
	@Override
	public void execute(final HttpServletRequest request, final HttpServletResponse response) {
		NDC.push(logging((HttpServletRequest) request, (HttpServletResponse) response));
	}

	/**
	 * Log4Jの{@link NDC}を利用の後始末をする。<br>
	 * 
	 * @param request リクエスト情報
	 * @param response レスポンス情報
	 * @see AbstractHttpFilter#finallProcess(HttpServletRequest, HttpServletResponse)
	 */
	@Override
	public void finallProcess(final HttpServletRequest request, final HttpServletResponse response) {
		NDC.remove();
	}

	/**
	 * ログに付与する情報を生成する。<br>
	 * 
	 * <pre>
	 * 例えばSessionIdを返すようにすれば、log4JのメッセージにSessionIdが付与されるようになる。
	 * log4j.xml(またはlog4j.properties)に%xを記述することで出力位置を調整出来る。
	 * </pre>
	 * 
	 * @param request リクエスト情報
	 * @param response レスポンス情報
	 * @return ログに付与する情報
	 */
	protected abstract String logging(HttpServletRequest request, HttpServletResponse response);
}
