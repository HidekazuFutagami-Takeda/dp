package jp.co.takeda.web.ctrl.dpm;

import static jp.co.takeda.security.BusinessType.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.TmsTytenMstHontenListDto;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpmTmsTytenMstSearchService;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dpm.Dpm912C01Form;

/**
 * Dpm912C01(特約店検索(本店)画面)のアクションクラス
 * 
 * @author khashimoto
 */
@Controller("Dpm912C01Action")
public class Dpm912C01Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dpm912C01Action.class);

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
	public Result dpm912C01F00(DpContext ctx, Dpm912C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm912C01F00");
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
	public Result dpm912C01F05(DpContext ctx, Dpm912C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm912C01F05");
		}

		// 検索実行
		search(form);
		// ページ番号１に設定
		form.setCrntPageNo(1);
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
	public Result dpm912C01F10(DpContext ctx, Dpm912C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm912C01F10");
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
	private void search(Dpm912C01Form form) throws Exception {
		// 検索実行
		TmsTytenMstHontenListDto resultList = dpmTmsTytenMstSearchService.search(form.convertTmsTytenMstScDto());
		// 結果格納DTOをリクエストに格納
		super.getRequestBox().put(Dpm912C01Form.TMS_LIST_KEY_R, resultList);
		// 結果をFORMに反映
		form.convertForm(resultList);
		// 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
		// 手動で全画面に遷移できるように都道府県コードをリセット
		form.setaddrCodePref(null);
	}

	// -------------------------------
	// validation method
	// -------------------------------
}
