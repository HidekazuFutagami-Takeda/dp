package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.security.BusinessType.VACCINE;
import static jp.co.takeda.security.DpAuthority.AuthType.REFER;

import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps400C04Form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

/**
 * Dps400C04((ワ)施設特約店別計画配分処理起動確認画面)のアクションクラス
 * 
 * @author siwamoto
 */
@Controller("Dps400C04Action")
public class Dps400C04Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps400C04Action.class);

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
	@RequestType(businessType = VACCINE)
	@Permission( authType = REFER)
	public Result dps400C04F00(DpContext ctx, Dps400C04Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps400C04F00");
		}

		// 初期化処理
		form.formInit();

		return ActionResult.SUCCESS;
	}

	// -------------------------------
	// validation method
	// -------------------------------
}
