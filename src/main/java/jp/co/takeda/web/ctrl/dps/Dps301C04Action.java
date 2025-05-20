package jp.co.takeda.web.ctrl.dps;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsSosJgiSearchService;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps301C04Form;

/**
 * Dps301C04((医)試算処理方法確認ダイアログ画面)のアクションクラス
 *
 * @author khashimoto
 */
@Controller("Dps301C04Action")
public class Dps301C04Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps301C04Action.class);

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
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps301C04F00(DpContext ctx, Dps301C04Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps301C04F00");
		}

		// 初期化処理
		form.formInit();

		String sosCd3 = form.getSosCd3();
		if(StringUtils.isNotEmpty(sosCd3)){

			// 営業所配下のチーム取得
			try {
				dpsSosJgiSearchService.searchUnderSosList(sosCd3, SosListType.SHITEN_LIST);
			} catch (DataNotFoundException e) {
				form.setExistsTeamSos(false);
			}
		}

		return ActionResult.SUCCESS;
	}

	// -------------------------------
	// validation method
	// -------------------------------
}
