package jp.co.takeda.web.cmn.filter;

import java.util.Calendar;
import java.util.TimeZone;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * コンテンツをキャッシュする。
 *
 * @author tkawabata
 */
public class CacheFilter extends FilterTemplete {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(CacheFilter.class);

	/**
	 * Expires
	 */
	private static final String EXPIRES_NAME = "Expires";

	/**
	 * Expires値(48時間)
	 */
	private static final int EXPIRES_VALUE = 48 * 60 * 60 * 1000;

	/**
	 * Last-Modified
	 */
	private static final String LAST_MODIFTY_NAME = "Last-Modified";

	/**
	 * Last-Modified値
	 */
	private Long modTime = null;

	/**
	 * Cache-Control
	 */
	private static final String CACHE_CONTROL_NAME = "Cache-Control";

	/**
	 * Cache-Control値
	 */
	private static final String CACHE_CONTROL_VALUE = "public";

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		try {
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/London"));
			this.modTime = cal.getTimeInMillis() / 1000 * 1000;
		} catch (Exception e) {
		}
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		if (LOG.isDebugEnabled()) {
			final String logMsg = "Cache <--";
			LOG.debug(logMsg + request.getRequestURI());
		}
		try {
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/London"));
			if (cal != null) {
				response.setDateHeader(EXPIRES_NAME, cal.getTimeInMillis() + EXPIRES_VALUE);
			}
		} catch (Exception e) {
		}

		if (modTime != null) {
			response.setDateHeader(LAST_MODIFTY_NAME, modTime);
		}
		response.setHeader(CACHE_CONTROL_NAME, CACHE_CONTROL_VALUE);
	}
}
