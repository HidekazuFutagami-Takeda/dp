package jp.co.takeda.web.cmn.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * コンテンツをキャッシュしない。
 * 
 * @author tkawabata
 */
public class NoCacheFilter extends FilterTemplete {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(NoCacheFilter.class);

	/**
	 * Expires
	 */
	private static final String EXPIRES_NAME = "Expires";

	/**
	 * Expires値
	 */
	private static final String EXPIRES_VALUE = "-1";

	/**
	 * Pragma
	 */
	private static final String PRAGMA_NAME = "Pragma";

	/**
	 * Pragma値
	 */
	private static final String PRAGMA_VALUE = "no-cache";

	/**
	 * Cache-Control
	 */
	private static final String CACHE_CONTROL_NAME = "Cache-Control";

	/**
	 * Cache-Control値
	 */
	private static final String CACHE_CONTROL_VALUE = "no-cache";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		if (LOG.isDebugEnabled()) {
			final String logMsg = "NoCache <--";
			LOG.debug(logMsg + request.getRequestURI());
		}
		response.setHeader(EXPIRES_NAME, EXPIRES_VALUE);
		response.setHeader(PRAGMA_NAME, PRAGMA_VALUE);
		response.setHeader(CACHE_CONTROL_NAME, CACHE_CONTROL_VALUE);
	}
}
