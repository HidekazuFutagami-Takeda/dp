package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.security.BusinessType.VACCINE;
import static jp.co.takeda.security.DpAuthority.AuthType.REFER;


import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.PlannedProdForVacResultDto;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsMrPlanForVacSearchService;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps302C04Form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Dps302C04((ワ)計画対象品目選択画面)のアクションクラス
 * 
 * @author stakeuchi
 */
@Controller("Dps302C04Action")
public class Dps302C04Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps302C04Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * (ワクチン)担当者別計画機能の検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsMrPlanForVacSearchService")
	protected DpsMrPlanForVacSearchService dpsMrPlanForVacSearchService;

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
	@RequestType(businessType = VACCINE)
	@Permission( authType = REFER)
	public Result dps302C04F00(DpContext ctx, Dps302C04Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C04F00");
		}

		// 初期化処理
		form.formInit();

		// 検索実行
		searchPlannedProdList();

		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理の実行
	 * 
	 * @throws Exception 例外
	 */
	private void searchPlannedProdList() throws Exception {

		// 計画対象品目一覧の検索実行
		List<PlannedProdForVacResultDto> resultDtoList = dpsMrPlanForVacSearchService.searchPlannedProdList();

		// リクエストボックスに計画対象品目一覧をセット
		super.getRequestBox().put(Dps302C04Form.DPS302C04_DATA_R_SEARCH_RESULT, (ArrayList<PlannedProdForVacResultDto>) resultDtoList);
	}
}
