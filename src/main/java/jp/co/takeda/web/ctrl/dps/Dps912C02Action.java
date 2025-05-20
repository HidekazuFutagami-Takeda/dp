package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.security.BusinessType.CMN;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.TmsTytenMstListDto;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsTmsTytenMstSearchService;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps912C02Form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Dps912C02(特約店検索(支社)画面)のアクションクラス
 * 
 * @author khashimoto
 */
@Controller("Dps912C02Action")
public class Dps912C02Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps912C02Action.class);

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
	public Result dps912C02F00(DpContext ctx, Dps912C02Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps912C02F00");
		}

		// 初期化処理
		form.formInit();

		// 検索実行
		search(form);
		return ActionResult.SUCCESS;
	}

	/**
	 * ページ遷移時に呼ばれるアクションメソッド<br>
	 * 
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType(businessType = CMN)
	public Result dps912C02F05(DpContext ctx, Dps912C02Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps912C02F05");
		}

		// 検索実行
		search(form);
		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理の実行
	 * 
	 * 
	 * @param form ActionForm
	 * @throws Exception
	 */
	private void search(Dps912C02Form form) throws Exception {
		// 検索実行
		TmsTytenMstListDto resultList = dpsTmsTytenMstSearchService.search(form.getHontenTmsTytenCd(), form.getTakedaSosCd2());
		// 結果格納DTOをリクエストに格納
		super.getRequestBox().put(Dps912C02Form.TMS_LIST_KEY_R, resultList);
		// 結果をFORMに反映
		form.convertForm(resultList);
	}

	// -------------------------------
	// validation method
	// -------------------------------
}
