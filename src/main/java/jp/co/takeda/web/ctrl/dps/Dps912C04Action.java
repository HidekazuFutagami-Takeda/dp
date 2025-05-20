package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.security.BusinessType.CMN;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.TmsTytenMstResultsListDto;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsTmsTytenMstSearchService;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps912C04Form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Dps912C04(特約店実績検索(本部・支社・支店)画面)のアクションクラス
 *
 * @author khashimoto
 */
@Controller("Dps912C04Action")
public class Dps912C04Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps912C04Action.class);

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
	public Result dps912C04F00(DpContext ctx, Dps912C04Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps912C04F00");
		}

		// 初期化処理
		form.formInit();

		// 検索実行
		search(form);
		return ActionResult.SUCCESS;
	}

	/**
	 * 検索時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType(businessType = CMN)
	public Result dps912C04F05(DpContext ctx, Dps912C04Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps912C04F05");
		}
		// 検索実行
		search(form);
		// ページ番号に、1を設定
		form.setCrntPageNo4(1);
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
	public Result dps912C04F10(DpContext ctx, Dps912C04Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps912C04F10");
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
	private void search(Dps912C04Form form) throws Exception {
		// 検索実行
		TmsTytenMstResultsListDto resultList = dpsTmsTytenMstSearchService.searchResultsList(form.getSosCd(), form.convertTmsTytenMstScDto());
		// 結果格納DTOをリクエストに格納
		super.getRequestBox().put(Dps912C04Form.TMS_LIST_KEY_R, resultList);
	}

	// -------------------------------
	// validation method
	// -------------------------------
}
