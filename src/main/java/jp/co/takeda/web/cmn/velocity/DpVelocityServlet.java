package jp.co.takeda.web.cmn.velocity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.takeda.web.cmn.delegate.CertificateDelegate;
import jp.co.takeda.web.cmn.filter.SwitchCharacterEncodingFilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.servlet.VelocityLayoutServlet;

/**
 * VelocityLayoutServletのカスタマイズ
 *
 * @author tkawabata
 */
public class DpVelocityServlet extends VelocityLayoutServlet {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpVelocityServlet.class);

	/**
	 * コンテンツタイプ
	 */
	private static final String CONTENTS_TYPE = "application/xml; charset=UTF-8";

	/**
	 * VelocityServletのリクエスト毎の初期化処理を定義する。
	 *
	 * @param request リクエスト情報
	 * @param response レスポンス情報
	 * @throws ServletException サーブレット例外
	 * @throws IOException IO例外
	 */
	@Override
	protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CertificateDelegate.setDpuserInfo(super.getServletContext(), request);
		super.doRequest(request, response);
	}

	/**
	 * フラグが設定されている場合に、コンテンツタイプを書き換える。
	 *
	 * @param request リクエスト情報
	 * @param response レスポンス情報
	 */
	@Override
	protected void setContentType(HttpServletRequest request, HttpServletResponse response) {
		boolean switchFlg = SwitchCharacterEncodingFilter.isSwitch(request);
		if (switchFlg) {
			response.setContentType(CONTENTS_TYPE);
			if (LOG.isDebugEnabled()) {
				LOG.debug("*** ContentsType --> " + CONTENTS_TYPE);
			}
		}
	}

	/**
	 * Thread中のDpUserInfoを削除する。
	 *
	 * @param request リクエスト情報
	 * @param response レスポンス情報
	 * @param context Velocityコンテキスト情報
	 */
	@Override
	protected void requestCleanup(HttpServletRequest request, HttpServletResponse response, Context context) {
		CertificateDelegate.clearDpuserInfo(super.getServletContext(), request);
	}

	@Override
	protected Writer getResponseWriter(final HttpServletResponse response) throws UnsupportedEncodingException, IOException {
		if (super.getVelocityEngine() != null) {
			return new PerformanceWriter(super.getResponseWriter(response));
		}
		return super.getResponseWriter(response);
	}
}
