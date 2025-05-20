package jp.co.takeda.web.ctrl.dps;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps998C00Form;

/**
 * Dps998C00(ファイル選択画面)のアクションクラス
 *
 * @author kibe
 */
@Controller("Dps998C00Action")
public class Dps998C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps998C00Action.class);

	/**
	 * 初期表示<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 */
	@ActionMethod(isDownloadable = false)
	@RequestType
	public Result dps998C00F00(DpContext ctx, Dps998C00Form form) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps998C00F00");
			LOG.debug("outputSosCd=" + form.getOutputSosCd());
			LOG.debug("category=" + form.getCategory());
		}

		// 初期化処理
		form.formInit();

		return ActionResult.SUCCESS;
	}
}
