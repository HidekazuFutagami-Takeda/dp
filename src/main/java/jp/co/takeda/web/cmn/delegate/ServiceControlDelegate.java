package jp.co.takeda.web.cmn.delegate;

import java.lang.reflect.Method;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyStringImpl;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.AccessDeniedException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.CorrespondType;
import jp.co.takeda.logic.ServiceStopAnnounceLogic;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.service.DpcSystemManageSearchService;
import jp.co.takeda.web.cmn.action.DpDelegateAction;
import jp.co.takeda.web.cmn.bean.DpContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

/**
 * 納入計画システムの管理を行うDelegate
 * 
 * @author tkawabata
 */
@Controller("serviceControlDelegate")
public class ServiceControlDelegate extends DpDelegateAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(ServiceControlDelegate.class);

	/**
	 * ServiceStopAnnounce_DATA_R
	 */
	public static final BoxKey ServiceStopAnnounce_DATA_R = new BoxKeyStringImpl("jp.co.takeda.web.cmn.delegate.ServiceControlDelegate.ServiceStopAnnounce_DATA_R");

	/**
	 * メッセージソース
	 */
	@Autowired(required = true)
	@Qualifier("messageSource")
	protected MessageSource messageSource;

	/**
	 * 納入計画管理検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpcSystemManageSearchService")
	protected DpcSystemManageSearchService dpcSystemManageSearchService;

	/**
	 * システムコントロール行う。
	 * 
	 * @param ctx Context
	 * @param action ActionMethod
	 * @throws Exception 例外
	 */
	@Override
	public void execute(DpContext ctx, Method action) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("ServiceControlDelegate.execute()");
		}

		// 期管理情報
		DpUserInfo dpUserInfo = DpUserInfo.getDpUserInfo();
		SysManage sysManage = dpcSystemManageSearchService.searchSysManage(dpUserInfo.getSysClass(), dpUserInfo.getSysType());

		// ユーザ情報に登録
		dpUserInfo.setSysManage(sysManage);

		// 同期通信時のみサービス停止予告設定
		if (action.isAnnotationPresent(ActionMethod.class)) {
			ActionMethod actionMethod = action.getAnnotation(ActionMethod.class);
			CorrespondType correspondType = actionMethod.correspondType();
			if (CorrespondType.ASYNC != correspondType) {
				ServiceStopAnnounceLogic logic = new ServiceStopAnnounceLogic(sysManage, messageSource);
				super.getRequestBox().put(ServiceStopAnnounce_DATA_R, logic.getAnnounceMessage());
			}
		}

		// サービス停止判定
		Boolean serviceStopFlg = sysManage.getServiceStopFlg();
		if (serviceStopFlg) {

			// セッションを無効化
			CertificateDelegate.invalidate(ctx);
			String msgCode = sysManage.getMessageCode();
			final String errMsg = "期管理情報がサービス停止に設定されているため、サービス提供しない";
			throw new AccessDeniedException(new Conveyance(new MessageKey(msgCode), errMsg));
		}
	}
}
