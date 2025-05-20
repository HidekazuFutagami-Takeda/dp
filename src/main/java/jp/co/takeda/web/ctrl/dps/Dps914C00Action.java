package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.security.BusinessType.CMN;

import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsSosJgiSearchService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps914C00Form;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Dps914C00Action(施設主担当従業員検索)のアクションクラス
 * 
 * @author khashimoto
 */
@Controller("Dps914C00Action")
public class Dps914C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps914C00Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 組織・従業員検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsSosJgiSearchService")
	protected DpsSosJgiSearchService dpsSosJgiSearchService;

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
	public Result dps914C00F00(DpContext ctx, Dps914C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps914C00F00");
			LOG.debug("jgiNo:" + form.getJgiNo());
			LOG.debug("sosApplyFuncName:" + form.getSosApplyFuncName());
		}

		// 初期化処理
		form.formInit();

		// 従業員番号
		Integer jgiNo = ConvertUtil.parseInteger(form.getJgiNo());

		// 検索サービス実行
		List<JgiMst> listDto = dpsSosJgiSearchService.searchMainMrList(jgiNo);
		
		// 結果格納DTOをリクエストに格納
		super.getRequestBox().put(Dps914C00Form.SOS_JGI_LIST_DTO_KEY_R, (ArrayList<JgiMst>)listDto);
		return ActionResult.SUCCESS;
	}

	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * 起動時、入力パラメータを検証する。
	 * 
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps914C00F00Validation(DpContext ctx, Dps914C00Form form) throws Exception {
		// 従業員番号 必須チェック
		if (StringUtils.isBlank(form.getJgiNo())) {
			List<String> srcHolderList = new ArrayList<String>();
			srcHolderList.add("従業員番号");
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", srcHolderList)));
		}

		// 適用関数名 必須チェック
		if (StringUtils.isBlank(form.getSosApplyFuncName())) {
			List<String> srcHolderList = new ArrayList<String>();
			srcHolderList.add("適用関数名");
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", srcHolderList)));
		}

	}
}
