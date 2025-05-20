package jp.co.takeda.web.ctrl.dps;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.DeliveryResultVacMrListDto;
import jp.co.takeda.dto.DeliveryResultVacMrScDto;
import jp.co.takeda.security.BusinessType;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsDeliveryResultVacMrSearchService;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps913C02Form;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Dps913C02((ワ)過去実績参照画面(担当者別))のアクションクラス
 * 
 * @author siwamoto
 */
@Controller("Dps913C02Action")
public class Dps913C02Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps913C02Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * サービス（検索系）
	 */
	@Autowired(required = true)
	@Qualifier("dpsDeliveryResultVacMrSearchService")
	protected DpsDeliveryResultVacMrSearchService dpsDeliveryResultVacMrSearchService;

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
	@RequestType(businessType = BusinessType.VACCINE)
	public Result dps913C02F00(DpContext ctx, Dps913C02Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps913C02F00");
		}

		// 初期化処理
		form.formInit();

		DeliveryResultVacMrScDto sosDto = form.convertDeliveryResultMrScDto();
		DeliveryResultVacMrListDto ServiceResult = dpsDeliveryResultVacMrSearchService.searchDeliveryResultVacMrList(sosDto);
		super.getRequestBox().put(Dps913C02Form.DPS913C02_DATA_R, ServiceResult);

		return ActionResult.SUCCESS;
	}

	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * 画面入力チェック
	 * 
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps913C02F00Validation(DpContext ctx, Dps913C02Form form) throws Exception {
		if (StringUtils.isEmpty(form.getProdCode())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "品目コード")));
		}
	}
}
