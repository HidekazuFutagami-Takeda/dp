package jp.co.takeda.web.cmn.delegate;

import java.lang.reflect.Method;

import javax.servlet.http.HttpSession;

import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.security.DpMetaInfo;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.web.cmn.action.DpDelegateAction;
import jp.co.takeda.web.cmn.bean.DpContext;

import org.apache.log4j.NDC;
import org.springframework.stereotype.Controller;

/**
 * ロギング処理を行うデリゲートクラス
 * 
 * @author tkawabata
 */
@Controller("loggingDelegate")
public class LoggingDelegate extends DpDelegateAction {

	/**
	 * Sessionがない場合の識別子
	 */
	public static final String NO_SESSION = "????????????????????????????????????????????";

	@Override
	public void execute(DpContext ctx, Method action) throws Exception {
		StringBuilder sb = ctx.getDpMetaInfo().getLogString();
		DpMetaInfo metaInfo = ctx.getDpMetaInfo();
		if (metaInfo != null) {
			sb.append("sId=" + metaInfo.getSId());
			sb.append(",gCd=" + metaInfo.getGCode());
			sb.append(",fCd=" + metaInfo.getFCode());
		} else {
			sb.append("sId=" + "xxx");
			sb.append(",gCd=" + "xxx");
			sb.append(",fCd=" + "xxx");
		}
		HttpSession session = ctx.getRequest().getSession(false);
		if (session != null) {
			sb.append("," + session.getId());
		} else {
			sb.append("," + NO_SESSION);
		}
		DpUserInfo dpUserInfo = DpUserInfo.getDpUserInfo();
		if (dpUserInfo != null) {
			DpUser settingUser = dpUserInfo.getSettingUser();
			if (dpUserInfo.isOfficialUser()) {
				sb.append(",oJgiNo=" + dpUserInfo.getLoginUserJgiNo());
				sb.append(",sJgiNo=" + settingUser.getJgiNo());
			} else {
				sb.append(",aJgiNo=" + dpUserInfo.getLoginUserJgiNo());
				sb.append(",sJgiNo=" + settingUser.getJgiNo());
			}
			SosMst sosMst = dpUserInfo.getDefaultSosMst();
			if (sosMst != null) {
				sb.append(",dSosCd=" + sosMst.getSosCd());
			} else {
				sb.append(",dSosCd=xxxxx");
			}
			JgiMst jgiMst = dpUserInfo.getDefaultJgiMst();
			if (jgiMst != null) {
				sb.append(",dJgiNo=" + jgiMst.getJgiNo());
			} else {
				sb.append(",dJgiNo=xxxxxxx");
			}
		}
		String logString = sb.toString();
		NDC.push(logString);
	}
}
