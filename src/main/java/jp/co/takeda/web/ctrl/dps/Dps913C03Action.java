package jp.co.takeda.web.ctrl.dps;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.DeliveryResultVacInsListDto;
import jp.co.takeda.dto.DeliveryResultVacInsScDto;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsDeliveryResultVacInsSearchService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps913C03Form;

/**
 * Dps913C03((ワ)過去実績参照画面(施設特約店別))のアクションクラス
 *
 * @author siwamoto
 */
@Controller("Dps913C03Action")
public class Dps913C03Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps913C03Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * サービス（検索系）
	 */
	@Autowired(required = true)
	@Qualifier("dpsDeliveryResultVacInsSearchService")
	protected DpsDeliveryResultVacInsSearchService dpsDeliveryResultVacInsSearchService;

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
	public Result dps913C03F00(DpContext ctx, Dps913C03Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps913C03F00");
		}

		// 初期化処理
		form.formInit();

		DeliveryResultVacInsScDto sosDto = form.convertDeliveryResultVacInsScDto();
		try {
			DeliveryResultVacInsListDto ServiceResult = dpsDeliveryResultVacInsSearchService.searchDeliveryResultVacInsList(sosDto);
			super.getRequestBox().put(Dps913C03Form.DPS913C03_DATA_R, ServiceResult);
		} catch (LogicalException e) {
			DeliveryResultVacInsListDto ServiceResult = dpsDeliveryResultVacInsSearchService.searchDeliveryResultVacInsList2(sosDto);
			super.getRequestBox().put(Dps913C03Form.DPS913C03_DATA_R, ServiceResult);
			throw e;
		}
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
	public void dps913C03F00Validation(DpContext ctx, Dps913C03Form form) throws Exception {
		String jgiNo = ConvertUtil.toString(form.getJgiNo());
		if (StringUtils.isEmpty(jgiNo)) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "担当者番号")));
		}
		if (StringUtils.isEmpty(form.getProdCode())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "品目コード")));
		}
	}
}
