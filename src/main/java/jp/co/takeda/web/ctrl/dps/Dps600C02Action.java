package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.security.DpAuthority.AuthType.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps600C02Form;

/**
 * Dps600C02((医)施設医師別計画配分処理起動確認画面)のアクションクラス
 *
 * @author stakeuchi
 */
@Controller("Dps600C02Action")
public class Dps600C02Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps600C02Action.class);

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
	public Result dps600C02F00(DpContext ctx, Dps600C02Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps600C02F00");
		}

		// 初期化処理
		form.formInit();

		return ActionResult.SUCCESS;
	}

	// -------------------------------
	// validation method
	// -------------------------------
}
