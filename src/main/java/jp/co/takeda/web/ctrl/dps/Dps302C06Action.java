package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.security.DpAuthority.AuthType.EDIT;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsSosJgiSearchService;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps302C06Form;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Dps302C06((医)計画値の一括コピー方法確認ダイアログ画面)のアクションクラス
 * 
 * @author khashimoto
 */
@Controller("Dps302C06Action")
public class Dps302C06Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps302C06Action.class);

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
	@Permission( authType = EDIT)
	public Result dps302C06F00(DpContext ctx, Dps302C06Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C06F00");
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
