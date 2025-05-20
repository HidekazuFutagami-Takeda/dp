package jp.co.takeda.web.cmn.delegate;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.AccessDeniedException;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.Constants;
import jp.co.takeda.security.BusinessTarget;
import jp.co.takeda.security.BusinessType;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpMetaInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.web.cmn.action.DpDelegateAction;
import jp.co.takeda.web.cmn.bean.DpContext;

/**
 * アクセス解析を行い、<code>DpMetaInfo</code>を生成・設定するDelegateクラス <br>
 *
 * <pre>
 * {@link #execute(DpContext, Method)}を実行することにより、パス情報やアクションメソッドのアノテーション情報を解析し、
 * {@link DpMetaInfo}を生成する。生成した<code>DpMetaInfo</code>は、{@link DpContext}情報に格納され、後続の処理では、
 * <code>DpContext</code>にアクセスすることにより、<code>DpMetaInfo</code>で定義した属性にアクセスすることが可能である。
 * ※{@link #execute(DpContext, Method)}が実行されるまでは、<code>DpContext</code>から<code>DpMetaInfo</code>を取得出来ない。
 * </pre>
 *
 * @see #execute(DpContext, Method)
 * @see jp.co.takeda.security.DpMetaInfo
 * @see jp.co.takeda.web.cmn.bean.DpContext
 * @author tkawabata
 */
@Controller("parseDelegate")
public class ParseDelegate extends DpDelegateAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(ParseDelegate.class);

	/**
	 * アプリケーションディレクトリを表す文字列
	 */
	private static final String APP_DIRECTORY = "/" + Constants.APPLICATION_DIRECTORY + "/";

	/**
	 * アクセス解析を行い、DpMetaInfoを生成し、DpContextに設定する。
	 *
	 * @param ctx Context
	 * @param action ActionMethod
	 * @throws Exception 例外
	 */
	@Override
	public void execute(DpContext ctx, Method action) throws Exception {

		final String path = ctx.getRequest().getRequestURI();
		if (LOG.isDebugEnabled()) {
			LOG.debug("ParseDelegate.execute()");
			LOG.debug("path=" + path);
		}

		final String app = ctx.getRequest().getContextPath() + APP_DIRECTORY;
		if (!path.startsWith(app)) {
			final String errMsg = APP_DIRECTORY + "以外での要求は処理出来ない";
			throw new AccessDeniedException(new Conveyance(ErrMessageKey.ACCESS_DENIED_ERROR, errMsg));
		}

		// appPath
		final String appPath = path.substring(app.length());
		if (appPath.length() < 12) {
			final String errMsg = "想定している形式のパスではない。appPath=" + appPath;
			throw new AccessDeniedException(new Conveyance(ErrMessageKey.ACCESS_DENIED_ERROR, errMsg));
		}

		// sId
		final String sId = appPath.substring(0, 3);

		// gCode
		final String gCode = appPath.substring(3, 6);

		// fCode
		final String fCode = appPath.substring(6, 12);

		// businessTarget
		final String dps = "dps";
		final String dpm = "dpm";
		final String dpc = "dpc";
		final BusinessTarget bTarget;
		if (dps.equals(sId)) {
			bTarget = BusinessTarget.DPS;
		} else if (dpm.equals(sId)) {
			bTarget = BusinessTarget.DPM;
		} else if (dpc.equals(sId)) {
			bTarget = BusinessTarget.DPC;
		} else {
			final String errMsg = "sIdが想定外";
			throw new AccessDeniedException(new Conveyance(ErrMessageKey.ACCESS_DENIED_ERROR, errMsg));
		}

		// BusinessType
		final BusinessType bType;
		final RequestType requestType = action.getAnnotation(RequestType.class);
		if (requestType != null) {
			bType = requestType.businessType();
		} else {
			final String errMsg = "BusinessTypeが指定されていない";
			throw new SystemException(new Conveyance(ErrMessageKey.PARAMETER_ERROR, errMsg));
		}

		// authList
		DpAuthority authority = null;
		Permission permission = action.getAnnotation(Permission.class);
		if (permission != null) {
			authority = new DpAuthority(permission.authType());
		}

		// srvKbn
		String appServerKbn = System.getProperty("hostname","1");

		// logging
		StringBuilder sb = new StringBuilder();

		ctx.setDpMetaInfo(new DpMetaInfo(appPath, sId, gCode, fCode, bTarget, bType, authority, appServerKbn, sb));
	}
}
