package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.security.BusinessType.*;
import static jp.co.takeda.security.DpAuthority.AuthType.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.TmsTytenPlanSlideForVacResultDto;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsTmsTytenPlanSearchSlideForVacService;
import jp.co.takeda.service.DpsTmsTytenSlideForVacService;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps501C02Form;

/**
 * Dps501C02((ワ)特約店別計画スライド画面)のアクションクラス
 *
 * @author stakeuchi
 */
@Controller("Dps501C02Action")
public class Dps501C02Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps501C02Action.class);

	// -------------------------------
	// injection field
	// -------------------------------

	/**
	 * ワクチン用特約店別計画スライド状態 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsTmsTytenPlanSearchSlideForVacService")
	protected DpsTmsTytenPlanSearchSlideForVacService dpsTmsTytenPlanSearchSlideForVacService;

	/**
	 * ワクチン用特約店別計画スライドサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsTmsTytenSlideForVacService")
	protected DpsTmsTytenSlideForVacService dpsTmsTytenSlideForVacService;

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
	public Result dps501C02F00(DpContext ctx, Dps501C02Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps501C02F00");
		}

		// 初期化処理
		form.formInit();

		// 施設特約店別計画〆後のメッセージ設定
		SysManage sysManage = DpUserInfo.getDpUserInfo().getSysManage();
		if (!sysManage.getWsEndFlg()) {
			super.addErrorMessage(ctx, new MessageKey("DPS3291E"));
		}

		// スライド状態を検索
		searchTmsTytenPlanSlideStatus();
		return ActionResult.SUCCESS;
	}

	/**
	 * スライドする処理時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType(businessType = VACCINE)
	@Permission( authType = EDIT)
	public Result dps501C02F05Execute(DpContext ctx, Dps501C02Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps501C02F05Execute");
		}

		String sosCd2 = form.getSosCd2();
		// ログインユーザの組織情報をセット
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {
				// 本部の人は、全支店対象となるため、支店コードを無し
				if (user.isMatch(JokenSet.HONBU) || user.isMatch(JokenSet.HONBU_WAKUTIN_G)) {
					sosCd2 = null;
				}
			}
		}

		// スライド処理実行
		try {
			dpsTmsTytenSlideForVacService.tmsTytenSlide(sosCd2);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0005I", "スライド処理"));

		} finally {

			// 再検索
			searchTmsTytenPlanSlideStatus();
		}
		return ActionResult.SUCCESS;
	}

	// -------------------------------
	// private method
	// -------------------------------
	/**
	 * 特約店別計画スライド状態を検索する。
	 */
	private void searchTmsTytenPlanSlideStatus() {
		// 特約店別計画スライド状態を検索
		TmsTytenPlanSlideForVacResultDto resultDto = dpsTmsTytenPlanSearchSlideForVacService.searchSlideStatus();

		// 検索結果をリクエストに設定
		super.getRequestBox().put(Dps501C02Form.DPS501C02_DATA_R, resultDto);
	}
}
