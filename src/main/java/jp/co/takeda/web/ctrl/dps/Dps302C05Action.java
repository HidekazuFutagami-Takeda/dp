package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.exp.ErrMessageKey.*;
import static jp.co.takeda.security.BusinessType.*;
import static jp.co.takeda.security.DpAuthority.AuthType.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.MrPlanForVacResultDto;
import jp.co.takeda.dto.MrPlanForVacUpdateDto;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsMrPlanForVacSearchService;
import jp.co.takeda.service.DpsMrPlanForVacService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps302C05Form;

/**
 * Dps302C05((ワ)担当者別計画編集画面(担当者別入力))のアクションクラス
 *
 * @author stakeuchi
 */
@Deprecated
@Controller("Dps302C05Action")
public class Dps302C05Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps302C05Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * (ワクチン)担当者別計画機能の検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsMrPlanForVacSearchService")
	protected DpsMrPlanForVacSearchService dpsMrPlanForVacSearchService;

	/**
	 * (ワクチン)担当者別計画機能の更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsMrPlanForVacService")
	protected DpsMrPlanForVacService dpsMrPlanForVacService;

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
	public Result dps302C05F00(DpContext ctx, Dps302C05Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C05F00");
		}

		// 初期化処理
		form.formInit();

		// 検索実行
		searchMrPlanList(form);
		return ActionResult.SUCCESS;
	}

	/**
	 * 登録処理時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType(businessType = VACCINE)
	@Permission( authType = EDIT)
	public Result dps302C05F05Execute(DpContext ctx, Dps302C05Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C05F05Execute");
		}
		try {
			// 更新実行
			List<MrPlanForVacUpdateDto> updateDtoList = form.convertMrPlanForVacUpdateDto();
			if (!updateDtoList.isEmpty()) {
				dpsMrPlanForVacService.updateMrPlan(updateDtoList);
			}
			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(updateDtoList.size())));
		} finally {
			// 再検索実行
			searchMrPlanList(form);
		}
		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理を実行する。
	 *
	 * @throws Exception 例外
	 */
	private void searchMrPlanList(Dps302C05Form form) throws Exception {

		// 計画対象品目一覧の検索実行
		List<MrPlanForVacResultDto> resultDtoList = dpsMrPlanForVacSearchService.searchMrPlan(form.getProdCode());

		// リクエストボックスに計画対象品目一覧をセット
		super.getRequestBox().put(Dps302C05Form.DPS302C05_DATA_R_SEARCH_RESULT, (ArrayList<MrPlanForVacResultDto>) resultDtoList);
		super.getRequestBox().put(Dps302C05Form.DPS302C05_DATA_R_SEARCH_RESULT_EXIST, Boolean.TRUE);
	}

	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * 登録処理時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws ValidateException 例外
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps302C05F05Validation(DpContext ctx, Dps302C05Form form) throws ValidateException {
		// 更新行のNullチェック
		String[] rowIdList = form.getRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		for (int i = 0; i < rowIdList.length; i++) {
			// パラーメータのサイズチェック
			String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);
			if (rowId.length != 4) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 計画値に差異がない場合は更新が行われないためチェックを行わない
			if (StringUtils.equals(rowId[2], rowId[3])) {
				continue;
			}
			// シーケンスキー
			if (StringUtils.isEmpty(rowId[0]) || !ValidationUtil.isLong(rowId[0])) {
				final String errMsg = "受信パラメータ(シーケンスキー)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 最終更新日
			if (StringUtils.isEmpty(rowId[1]) || !ValidationUtil.isLong(rowId[1])) {
				final String errMsg = "受信パラメータ(最終更新日)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
		}
	}
}
