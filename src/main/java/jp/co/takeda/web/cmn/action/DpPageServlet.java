package jp.co.takeda.web.cmn.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.co.takeda.security.DpPageInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 画面情報を生成するサーブレット
 *
 * @author tkawabata
 */
public class DpPageServlet extends HttpServlet {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpPageServlet.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		DpPageInfo pageInfo = (DpPageInfo) session.getAttribute("jp.co.takeda.web.cmn.action.DpPageServlet");
		if (pageInfo != null) {
			if (LOG.isInfoEnabled()) {
				LOG.info("pageInfo exist..." + pageInfo.toString());
			}
		}
		String height = request.getParameter("gHeight");
		if (StringUtils.isBlank(height)) {
			if (pageInfo == null) {
				height = "900";
			} else {
				height = pageInfo.getHeight();
			}
		}
		pageInfo = new DpPageInfo(height);
		if (LOG.isInfoEnabled()) {
			LOG.info("create pageInfo ..." + pageInfo.toString());
		}
		session.setAttribute("jp.co.takeda.web.cmn.action.DpPageServlet", pageInfo);
	}
}
