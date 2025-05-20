package jp.co.takeda.web.ctrl.dpm;

import static jp.co.takeda.a.web.bean.CorrespondType.*;
import static jp.co.takeda.security.BusinessType.*;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.ChoseiDataParamsDto;
import jp.co.takeda.dto.ChoseiDataResultDto;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcUserSearchService;
import jp.co.takeda.service.DpmChoseiDataSearchService;
import jp.co.takeda.service.DpsSosJgiSearchService;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dpm.Dpm000C00Form;

/**
 * Dpm000C00(トップ画面)のアクションクラス
 *
 * @author tkawabata
 */
@Controller("Dpm000C00Action")
public class Dpm000C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dpm000C00Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 組織・従業員検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsSosJgiSearchService")
	protected DpsSosJgiSearchService dpsSosJgiSearchService;

	/**
	 * ユーザ情報を取得するサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpcUserSearchService")
	protected DpcUserSearchService dpcUserSearchService;

	/**
	 * 調整金額テーブルを検索するサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmChoseiDataSearchService")
	protected DpmChoseiDataSearchService dpmChoseiDataSearchService;

	// -------------------------------
	// action method
	// -------------------------------

	/**
	 * (医)初期表示時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	public Result dpm000C00F00(DpContext ctx, Dpm000C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm000C00F00");
		}

		// 初期化処理
		form.formInit();

		// 全フォームの初期化
		formInitlize(ctx);

		// 調整金額更新日時の設定
		this.dispChoseiUpdate();

		// 組織一覧の初期化
		super.getSessionBox().remove(Dpm000C00Form.DPM000C00F25_PARAMS_S);

		// フォームリフレッシュ
		form.reflash();

		return ActionResult.SUCCESS;
	}

	/**
	 * (医)組織一覧の切替時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	public Result dpm000C00F05(DpContext ctx, Dpm000C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm000C00F05");
		}

		// 調整金額更新日時の設定
		this.dispChoseiUpdate();

		// 組織一覧検索条件の保存(セッションを利用)
		ChoseiDataParamsDto beforeParams = super.getSessionBox().get(Dpm000C00Form.DPM000C00F25_PARAMS_S);

		// 組織コードと部門ランクを保存
		String sosCd = form.getTargetSosCd();
		String bumonRank = form.getTargetBumonRank();

		// 新条件を旧条件群に追加
		ChoseiDataParamsDto params = dpmChoseiDataSearchService.createParams(sosCd, bumonRank, beforeParams);
		super.getSessionBox().put(Dpm000C00Form.DPM000C00F25_PARAMS_S, params);

		// フォームリフレッシュ
		form.reflash();

		return ActionResult.SUCCESS;
	}

//	/**
//	 * (医)代行ユーザ切替時に呼ばれるアクションメソッド<br>
//	 *
//	 * @param ctx Context
//	 * @param form ActionForm
//	 * @return 処理結果
//	 * @throws Exception 例外
//	 */
//	@ActionMethod
//	@RequestType
//	public Result dpm000C00F10(DpContext ctx, Dpm000C00Form form) throws Exception {
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("dpm000C00F10");
//			LOG.debug("applyJgiNo=" + form.getApplyJgiNo());
//		}
//		Integer jgiNo = ConvertUtil.parseInteger(form.getApplyJgiNo());
//		dpcUserSearchService.switchUser(jgiNo, ctx.getDpMetaInfo());
//
//		// 調整金額更新日時の設定
//		this.dispChoseiUpdate();
//
//		// 組織一覧の初期化
//		super.getSessionBox().remove(Dpm000C00Form.DPM000C00F25_PARAMS_S);
//
//		// フォームリフレッシュ
//		form.reflash();
//
//		return ActionResult.SUCCESS;
//	}

	/**
	 * (医)調整金額テーブル取得時に呼ばれるアクションメソッド[非同期処理]<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(correspondType = ASYNC)
	@RequestType
	public Result dpm000C00F25(DpContext ctx, Dpm000C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm000C00F25");
		}

		// セッション中に保存してある条件の取得(無ければ生成)
		ChoseiDataParamsDto params = super.getSessionBox().get(Dpm000C00Form.DPM000C00F25_PARAMS_S);
		if (params == null) {
			params = dpmChoseiDataSearchService.createParams(null, null, null);
			super.getSessionBox().put(Dpm000C00Form.DPM000C00F25_PARAMS_S, params);
		}

		// 調整金額テーブル検索処理
		ChoseiDataResultDto choseiDataResultDto = dpmChoseiDataSearchService.search(params);
		super.getRequestBox().put(Dpm000C00Form.DPM000C00F25_DATA_R, choseiDataResultDto);

		return ActionResult.SUCCESS;
	}

	/**
	 * (ワ)初期表示時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType(businessType = VACCINE)
	public Result dpm000C00F50(DpContext ctx, Dpm000C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm000C00F50");
		}

		// 初期化処理
		form.formInit();

		// 全フォームの初期化
		formInitlize(ctx);

		// フォームリフレッシュ
		form.reflash();

		return ActionResult.SUCCESS;
	}

//	/**
//	 * (ワ)代行ユーザ切替時に呼ばれるアクションメソッド<br>
//	 *
//	 * @param ctx Context
//	 * @param form ActionForm
//	 * @return 処理結果
//	 * @throws Exception 例外
//	 */
//	@ActionMethod
//	@RequestType(businessType = VACCINE)
//	public Result dpm000C00F60(DpContext ctx, Dpm000C00Form form) throws Exception {
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("dpm000C00F60");
//			LOG.debug("applyJgiNo=" + form.getApplyJgiNo());
//		}
//		Integer jgiNo = ConvertUtil.parseInteger(form.getApplyJgiNo());
//		dpcUserSearchService.switchUser(jgiNo, ctx.getDpMetaInfo());
//
//		// フォームリフレッシュ
//		form.reflash();
//
//		return ActionResult.SUCCESS;
//	}

	// -------------------------------
	// validation method
	// -------------------------------

	// -------------------------------
	// private method
	// -------------------------------
	/**
	 * 組織一覧の表示権限がある場合、調整金額の更新日時を取得し、リクエストに設定する。
	 */
	private void dispChoseiUpdate() {

		DpUser dpUser = DpUserInfo.getDpUserInfo().getSettingUser();
		if (dpUser.isMatch(JokenSet.HONBU, JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF, JokenSet.OFFICE_MASTER, JokenSet.OFFICE_STAFF)) {
			Calendar cal = dpmChoseiDataSearchService.getChoseiLastUpDate();
			super.getRequestBox().put(Dpm000C00Form.DPM000C00_DATE_DATA_R, cal);
		}
	}
}
