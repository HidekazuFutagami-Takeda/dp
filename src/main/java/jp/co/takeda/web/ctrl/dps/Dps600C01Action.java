package jp.co.takeda.web.ctrl.dps;

import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.DocDistParamResultDto;
import jp.co.takeda.security.DpAuthority.AuthType;

import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcUserSearchService;
import jp.co.takeda.service.DpsDocDistributionProdSearchService;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps600C01Form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Dps600C01((医)施設医師別計画配分基準編集画面)のアクションクラス
 * 現在未使用
 * 
 * @author stakeuchi
 */
@Controller("Dps600C01Action")
@Deprecated
public class Dps600C01Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps600C01Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 配分機能 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsDocDistributionProdSearchService")
	protected DpsDocDistributionProdSearchService dpsDocDistributionProdSearchService;

	/**
	 * ユーザサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpcUserSearchService")
	protected DpcUserSearchService dpcUserSearchService;

	// -------------------------------
	// action method
	// -------------------------------
	/**
	 * 初期表示時に呼ばれるアクションメソッド
	 * 
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission( authType = AuthType.EDIT)
	public Result dps600C01F00(DpContext ctx, Dps600C01Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps600C01F00");
			LOG.debug("sosCd=" + form.getSosCd3());
			LOG.debug("prodCode=" + form.getProdCode());
		}

		// 初期化処理
		form.formInit();

		// 組織従業員初期化処理
		dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd3(), null);

		// 検索実行
		searchDistParam(form);
		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理を実行する。
	 * 
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchDistParam(Dps600C01Form form) throws Exception {
		// 検索実行
		final String sosCd = form.getSosCd3();
		final String prodCode = form.getProdCode();
//		final DocDistParamResultDto resultDto = dpsDocDistributionProdSearchService.searchDistributionParam(sosCd, prodCode);
//		super.getRequestBox().put(Dps600C01Form.Dps600C01_DATA_R_SEARCH_RESULT, resultDto);
	}

	// -------------------------------
	// validation method
	// -------------------------------
}
