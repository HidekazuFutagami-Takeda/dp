package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.exp.ErrMessageKey.*;
import static jp.co.takeda.security.DpAuthority.AuthType.*;

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
import jp.co.takeda.dto.SpecialInsPlanSearchForVacResultListDto;
import jp.co.takeda.model.div.PlanType;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsSpecialInsPlanForVacSearchService;
import jp.co.takeda.service.DpsSpecialInsPlanForVacService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps202C05Form;

/**
 * Dps202C05((ワ)特定施設個別計画立案画面(担当者立案))のアクションクラス
 * 【STEP4 医薬ワクチン統合対応により廃止】
 * @author siwamoto
 */
@Controller("Dps202C05Action")
@Deprecated
public class Dps202C05Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps202C05Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * ワクチン用特定施設個別計画 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsSpecialInsPlanForVacSearchService")
	protected DpsSpecialInsPlanForVacSearchService dpsSpecialInsPlanForVacSearchService;

	/**
	 * ワクチン用特定施設個別計画 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsSpecialInsPlanForVacService")
	protected DpsSpecialInsPlanForVacService dpsSpecialInsPlanForVacService;

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
	@RequestType
	@Permission(authType = REFER)
	public Result dps202C05F00(DpContext ctx, Dps202C05Form form) throws Exception {

		Integer jgiNo = ConvertUtil.parseInteger(form.getJgiNo());
		String insNo = form.getInsNo();

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps202C05F00");
			LOG.debug("jgiNo=" + jgiNo);
			LOG.debug("insNo=" + insNo);
		}

		// 特約店検索モードをクリア
		form.setTmsSelectMode(null);

		// 初期化処理
		form.formInit();

		// サービス呼び出し
		SpecialInsPlanSearchForVacResultListDto r = dpsSpecialInsPlanForVacSearchService.searchMrProdList(jgiNo, insNo);
		super.getRequestBox().put(Dps202C05Form.DPS202C05_DATA_R, r);
		return ActionResult.SUCCESS;
	}

	/**
	 * 登録するボタンクリック時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = EDIT)
	public Result dps202C05F05Excecute(DpContext ctx, Dps202C05Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps202C05F05");
		}

		try {

			dpsSpecialInsPlanForVacService.modifyVac(form.getSpecialInsPlanModifyForVacDto(), PlanType.MR, null);
			addMessage(ctx, new MessageKey("DPC0006I", "特定施設個別計画（担当者立案）"));

		} finally {

			// 登録後の再検索
			Integer jgiNo = ConvertUtil.parseInteger(form.getJgiNo());
			String insNo = form.getInsNo();
			SpecialInsPlanSearchForVacResultListDto r = dpsSpecialInsPlanForVacSearchService.searchMrProdList(jgiNo, insNo);
			super.getRequestBox().put(Dps202C05Form.DPS202C05_DATA_R, r);
		}
		return ActionResult.SUCCESS;
	}

	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * 入力チェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps202C05F05Validation(DpContext ctx, Dps202C05Form form) throws Exception {
		if (form.getPara() == null) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1034E")));
		}
		for (int i = 0; i < form.getPara().length; i++) {
			String[] paraArray = ConvertUtil.splitConmma(form.getPara()[i]);
			if (paraArray.length > 3) {
				String valueStr = paraArray[3];
				if (valueStr.equals("")) {
					valueStr = null;
				} else {
					final String errMsg = "受信パラメータ(特定施設個別計画計画値)が不正：";
					// 登録値 数値チェック
					if (!ValidationUtil.isDouble(valueStr)) {
						throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
					}
					// 登録値 整数チェック
					if (!ValidationUtil.isLong(valueStr)) {
						throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
					}
					// 登録値 桁数チェック
					if (!ValidationUtil.isMaxLength(valueStr, 10)) {
						throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
					}
					// 登録値 最小値チェック
					Long value = ConvertUtil.parseLong(valueStr);
					if (value < 0L) {
						throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
					}
				}
			}
		}
	}
}
