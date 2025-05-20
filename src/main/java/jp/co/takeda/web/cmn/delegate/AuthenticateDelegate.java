package jp.co.takeda.web.cmn.delegate;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import jp.co.takeda.a.bean.Box;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.AuthenticationException;
import jp.co.takeda.a.web.bean.SpringUtil;
import jp.co.takeda.bean.Constants;
import jp.co.takeda.bean.Constants.SpringBeanName;
import jp.co.takeda.model.JknGrp;
import jp.co.takeda.model.div.JknGrpId;
import jp.co.takeda.model.div.JokenKbn;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.web.cmn.action.DpDelegateAction;
import jp.co.takeda.web.cmn.bean.DpContext;

/**
 * 認可処理を実行するDelegate
 *
 * @author tkawabata
 */
@Controller("authenticateDelegate")
public class AuthenticateDelegate extends DpDelegateAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(AuthenticateDelegate.class);


	/**
	 * 認可処理を実行する。
	 *
	 * @param ctx Context
	 * @param action ActionMethod
	 * @throws Exception 例外
	 */
	@Override
	public void execute(DpContext ctx, Method action) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("AuthenticateDelegate.execute()");
		}

		// Actionのメソッドに制約が記述されていない場合、チェックしない
		DpAuthority requiredAuth = ctx.getDpMetaInfo().getAuthority();
		if (LOG.isDebugEnabled()) {
			LOG.debug("requiredAuthList...");
		}
		// 要求権限が存在しない場合はチェックしない
		if(requiredAuth == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("menu --> nocheck");
			}
			return;
		}

		// 要求権限
		AuthType authType = requiredAuth.getAuthType();

		AuthResult result = new AuthResult(null, false);
		// 「計画管理条件セットグループ」テーブルを参照し、
		String displayID ;
		if(action.getName().toUpperCase().substring(0, 3).
				equals(SysClass.DPM.getName())) {
			displayID = action.getName().toUpperCase().substring(0, 5); // DPMの場合は先頭５桁で比較

			// メニューの場合はチェックしない
			if(displayID.equals(JknGrpId.MENU.getDbValue())) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("menu --> nocheck");
				}
				return;
			}
			result = getAuthResult(displayID, authType);
			if(result.isAuthOk()) return;

		}else { // DPSの場合
			// まず画面アクションから画面IDを取得してチェック
			result = getAuthResult(action.getName().toUpperCase().substring(0, 9), authType);
			if(result.isAuthOk()) return;

			// FULL-IDでなかった場合は、6桁でチェック
			displayID = action.getName().toUpperCase().substring(0, 6);
			result = getAuthResult(displayID, authType);
			if(result.isAuthOk()) return;

		}

		if(result.isAuthOk() == false ) {
			JknGrpId jknGrpId = JknGrpId.getInstance(displayID);
			String jknGrpName = "不明";
			if(jknGrpId != null) {
				jknGrpName = jknGrpId.getJknGrpName();
			}
			String authTypeName = "不明";
			if(authType != null) {
				authTypeName = authType.getAuthTypeName();
			}

			// DPC0102E={0}業務の{1}権限がありません。
			MessageKey messageKey = new MessageKey("DPC0102E", jknGrpName, authTypeName);
			throw new AuthenticationException(new Conveyance(messageKey, "認可エラーが発生"));
		}

	}
	/**
	 * @param displayID
	 * @param authType
	 * @return
	 */
	private AuthResult getAuthResult(String displayID, AuthType authType) {
		AuthResult result = new AuthResult(null, false);

		// 画面に応じたjknGrpを取得
		JknGrp jknGrp = null;
		boolean authTypeCheck = false;
		Box sessionBox = SpringUtil.getBean(SpringBeanName.SESSION_BOX.value, super.servletContext);
		DpUserInfo dpUserInfo = sessionBox.get(Constants.DP_LOGIN_USER_INFO_KEY_S);
		List<JknGrp> jkGrpList = dpUserInfo.getLoginUser().getJknGrpList();

		for(JknGrp tmpJknGrp: jkGrpList) {
			if(displayID.equals(tmpJknGrp.getJknGrpId().getDbValue())) {
				jknGrp = tmpJknGrp;

				if(authType.equals(AuthType.REFER) || authType.equals(AuthType.OUTPUT)) {
					if(jknGrp.getJokenKbn().equals(JokenKbn.DO_NOT_USE) == false) {
						authTypeCheck = true;
						result =  new AuthResult(jknGrp,authTypeCheck);
					}
				}
				else {
					if(jknGrp.getJokenKbn().equals(JokenKbn.ALL_EDIT) || jknGrp.getJokenKbn().equals(JokenKbn.VAC_EDIT)) {
						authTypeCheck = true;
						result =  new AuthResult(jknGrp,authTypeCheck);
					}
				}
				break;
			}
		}
		return result;
	}
	private class AuthResult {
		private final JknGrp jknGrp;
		private final boolean authTypeCheck;
		public AuthResult(JknGrp jknGrp, boolean authTypeCheck) {
			super();
			this.jknGrp = jknGrp;
			this.authTypeCheck = authTypeCheck;
		}
		/**
		 * @return jknGrp
		 */
		public JknGrp getJknGrp() {
			return jknGrp;
		}
		/**
		 * @return authTypeCheck
		 */
		public boolean isAuthTypeCheck() {
			return authTypeCheck;
		}

		public boolean isAuthOk() {
			return (jknGrp != null && authTypeCheck == true);
		}
	}
}
