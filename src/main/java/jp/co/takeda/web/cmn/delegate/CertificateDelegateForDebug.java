package jp.co.takeda.web.cmn.delegate;

import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import jp.co.takeda.a.bean.Box;
import jp.co.takeda.a.exp.CertificationException;
import jp.co.takeda.a.web.bean.SpringUtil;
import jp.co.takeda.bean.Constants;
import jp.co.takeda.bean.Constants.SpringBeanName;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.bean.DpContext;

/**
 * 認証処理を実行するDelegate
 *
 * @author tkawabata
 */
@Controller("certificateDelegateForDebug")
public class CertificateDelegateForDebug extends CertificateDelegate {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(CertificateDelegateForDebug.class);

	@Override
	protected Integer getJgiNo(DpContext ctx) throws CertificationException {
		// CertificateDelegateの同名処理との違いは以下
		// ・リクエストパラメータ：loginJgiNoooooo から取得 (テスト用ログイン画面の入力値)
		// ・リクエストパラメータ：loginJgiNoooooo から取得できなければ、セッションのログイン情報から取得
		// ・従業員番号の数値チェック無し

		// HTTPヘッダーからの取得は行わない
		String jgiNo = "";
		if (StringUtils.isEmpty(jgiNo)) {
			jgiNo = ctx.getRequest().getParameter("loginJgiNoooooo");
			if (LOG.isDebugEnabled()) {
				LOG.debug("***Login Start.JgiNo=" + jgiNo);
			}
			if (StringUtils.isEmpty(jgiNo)) {
				Box sessionBox = SpringUtil.getBean(SpringBeanName.SESSION_BOX.value, super.servletContext);
				DpUserInfo dpUserInfo = sessionBox.get(Constants.DP_LOGIN_USER_INFO_KEY_S);
				if (dpUserInfo != null) {
					return dpUserInfo.getLoginUserJgiNo();
				}
			}
			// 従業員番号をCookieから取得
			// また、Cookieが改竄されていないかも併せて確認
			// ※リクエストパラメータ、セッションからの取得を優先するため
			// 　このタイミングで実施
			if (StringUtils.isEmpty(jgiNo)) {
				jgiNo = super.getJgiNoAfterVerifing(ctx);
			}
		}
		return ConvertUtil.parseInteger(jgiNo);
	}

	@Override
	protected Integer getAltJgiNo(final DpContext ctx) {
		// CertificateDelegateの同名処理との違いは以下
		// ・リクエストパラメータ：altJgiNoooooo から取得し、Cookie：CookieActJgino に保存
		// ※リクエストパラメータ：altJgiNoooooo から取得できなければ、CertificateDelegate と同等
		String altJgiNo = ctx.getRequest().getParameter("altJgiNoooooo");
		if (StringUtils.isNotBlank(altJgiNo)) {
			Cookie cookie = new Cookie(ALT_USER_COOKIE_KEY, altJgiNo);
			ctx.getResponse().addCookie(cookie);
			return ConvertUtil.parseInteger(altJgiNo);
		} else {
			return super.getAltJgiNo(ctx);
		}
	}
}
