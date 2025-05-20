package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.security.DpAuthority.AuthType.REFER;

import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps301C03Form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

/**
 * Dps301C03((医)試算処理起動確認画面)のアクションクラス
 * 
 * @author nozaki
 */
@Controller("Dps301C03Action")
public class Dps301C03Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps301C03Action.class);

	// -------------------------------
	// injection field
	// -------------------------------

	// -------------------------------
	// action method
	// -------------------------------

	/**
	 * 初期表示時に呼ばれるアクションメソッド<br>
	 * 
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission( authType = REFER)
	public Result dps301C03F00(DpContext ctx, Dps301C03Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps301C03F00");
		}

		// 初期化処理
		form.formInit();

		return ActionResult.SUCCESS;
	}

	// -------------------------------
	// validation method
	// -------------------------------
}
