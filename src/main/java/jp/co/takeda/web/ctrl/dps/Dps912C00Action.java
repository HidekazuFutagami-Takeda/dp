package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.security.BusinessType.CMN;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.SosListDto;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsTmsTytenMstSearchService;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps912C00Form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Dps912C00(特約店検索(特約店部)画面)のアクションクラス
 *
 * @author khashimoto
 */
@Controller("Dps912C00Action")
public class Dps912C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps912C00Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 特約店情報検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsTmsTytenMstSearchService")
	protected DpsTmsTytenMstSearchService dpsTmsTytenMstSearchService;

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
	@RequestType(businessType = CMN)
	public Result dps912C00F00(DpContext ctx, Dps912C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps912C00F00");
		}

		// 初期化処理
		form.formInit();

		SosListDto resultList = dpsTmsTytenMstSearchService.search();
		// 結果格納DTOをリクエストに格納
		super.getRequestBox().put(Dps912C00Form.SOS_LIST_KEY_R, resultList);
		return ActionResult.SUCCESS;
	}

	// -------------------------------
	// validation method
	// -------------------------------
}
