package jp.co.takeda.web.ctrl.dpm;

import static jp.co.takeda.security.BusinessType.CMN;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.TmsTytenMstResultsTenkaiListDto;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpmTmsTytenMstSearchService;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dpm.Dpm912C05Form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Dpm912C05(特約店実績検索(特約店)画面)のアクションクラス
 * 
 * @author khashimoto
 */
@Controller("Dpm912C05Action")
public class Dpm912C05Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dpm912C05Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 特約店情報検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmTmsTytenMstSearchService")
	protected DpmTmsTytenMstSearchService dpmTmsTytenMstSearchService;

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
	public Result dpm912C05F00(DpContext ctx, Dpm912C05Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm912C05F00");
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
	public Result dpm912C05F05(DpContext ctx, Dpm912C05Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm912C05F05");
		}
		// 検索実行
		search(form);
		// ページ番号１に設定
		form.setCrntPageNo5(1);
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
	public Result dpm912C05F10(DpContext ctx, Dpm912C05Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm912C05F10");
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
	private void search(Dpm912C05Form form) throws Exception {
		// 検索実行
		TmsTytenMstResultsTenkaiListDto resultList = dpmTmsTytenMstSearchService.searchResultsTytenList(form.getTmsTytenCd(), form.convertTmsTytenMstScDto());
		// 結果格納DTOをリクエストに格納
		super.getRequestBox().put(Dpm912C05Form.TMS_LIST_KEY_R, resultList);
		// 結果をFORMに反映
		form.convertForm(resultList);
	}

	// -------------------------------
	// validation method
	// -------------------------------
}
