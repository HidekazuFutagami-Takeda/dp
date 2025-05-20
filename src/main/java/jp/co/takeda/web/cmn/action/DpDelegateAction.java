package jp.co.takeda.web.cmn.action;

import jp.co.takeda.a.bean.Box;
import jp.co.takeda.a.web.action.DelegateAction;
import jp.co.takeda.a.web.bean.SpringUtil;
import jp.co.takeda.bean.Constants.SpringBeanName;
import jp.co.takeda.web.cmn.bean.DpContext;

/**
 * 納入計画システム向けDelegateAction
 * 
 * @author tkawabata
 */
public abstract class DpDelegateAction extends DelegateAction<DpContext> {

	/**
	 * Get Request Box.
	 * 
	 * @return Box
	 */
	protected Box getRequestBox() {
		return SpringUtil.getBean(SpringBeanName.REQUEST_BOX.value, servletContext);
	}

	/**
	 * Get Session Box.
	 * 
	 * @return Box
	 */
	protected Box getSessionBox() {
		return SpringUtil.getBean(SpringBeanName.SESSION_BOX.value, servletContext);
	}
}
