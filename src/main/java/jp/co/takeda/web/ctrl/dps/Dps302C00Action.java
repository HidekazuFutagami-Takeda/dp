package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.exp.ErrMessageKey.*;
import static jp.co.takeda.a.web.bean.CorrespondType.*;

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
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.MrPlanStatusUpdateDto;
import jp.co.takeda.dto.OfficePlanStatusResultDto;
import jp.co.takeda.dto.OfficeTeamPlanChoseiDto;
import jp.co.takeda.dto.PlannedProdResultListDto;
import jp.co.takeda.dto.PlannedProdScDto;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.model.view.SosCtg;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcUserSearchService;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.service.DpsMrPlanSearchService;
import jp.co.takeda.service.DpsMrPlanService;
import jp.co.takeda.service.DpsPlannedCtgSearchService;
import jp.co.takeda.service.DpsSosCtgSearchService;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps302C00Form;

/**
 * Dps302C00((医)計画対象品目選択画面)のアクションクラス
 *
 * @author stakeuchi
 */
@Controller("Dps302C00Action")
public class Dps302C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps302C00Action.class);

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPS302C00";

	/**
	 * 計画（1：期別、2：月別）
	 */
	private static final String PLAN_ID = "1";

	// -------------------------------
	// injection field
	// -------------------------------

	/**
	 * 担当者別計画機能 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsMrPlanSearchService")
	protected DpsMrPlanSearchService dpsMrPlanSearchService;

	/**
	 * 担当者別計画機能 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsMrPlanService")
	protected DpsMrPlanService dpsMrPlanService;

	/**
	 * ユーザサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpcUserSearchService")
	protected DpcUserSearchService dpcUserSearchService;

	/**
	 * カテゴリ 検索サービス
	 */
    @Autowired(required = true)
    @Qualifier("dpsCodeMasterSearchService")
    protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	/**
	 * 計画対象カテゴリ領域の検索サービス
	 */
    @Autowired(required = true)
    @Qualifier("dpsPlannedCtgSearchService")
    protected DpsPlannedCtgSearchService dpsPlannedCtgSearchService;

	/**
	 * 計画支援の組織カテゴリコード検索
	 */
	@Autowired(required = true)
	@Qualifier("dpsSosCtgSearchService")
	protected DpsSosCtgSearchService dpsSosCtgSearchService;

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
	@RequestType
	@Permission(authType = AuthType.REFER)
	public Result dps302C00F00(DpContext ctx, Dps302C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C00F00");
		}

		// クリック行ＩＤをクリア
		form.setClickRowId(null);
		// 前画面にて選択されていたカテゴリの保持
		String categoryBackUp = form.getCategory();

		// 初期化処理
		form.formInit();

		// 組織従業員初期化処理
		// 担当者まで選ぶ画面なので組織従業員設定自体は不要
		SosMst defaultSosMst = DpUserInfo.getDpUserInfo().getDefaultSosMst();
		if (defaultSosMst != null) {
			if (defaultSosMst.getBumonRank() == BumonRank.OFFICE_TOKUYAKUTEN_G) {
				form.setSosCd3(defaultSosMst.getSosCd());
				form.setSosCd4(null);
				// ONC-MRの場合
				JgiMst jgiMst = DpUserInfo.getDpUserInfo().getDefaultJgiMst();
				if (jgiMst != null) {
					form.setJgiNo(jgiMst.getJgiNo().toString());
				}
			} else if (defaultSosMst.getBumonRank() == BumonRank.TEAM) {
				form.setSosCd3(defaultSosMst.getUpSosCd());
				form.setSosCd4(defaultSosMst.getSosCd());
				JgiMst jgiMst = DpUserInfo.getDpUserInfo().getDefaultJgiMst();
				if (jgiMst != null) {
					form.setJgiNo(jgiMst.getJgiNo().toString());
				}
			}
			//カテゴリリストの設定
			initCategoryList(form);
		}

		// ログインユーザの組織情報をセット
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {
				// 権限確認コード
				if (user.isSosLvl(SCREEN_ID, SosLvl.OFFICE)) {
					form.setSosCd3(user.getSosCd3());
				}
				else if (user.isSosLvl(SCREEN_ID, SosLvl.MR)) {
					form.setSosCd3(user.getSosCd3());
					form.setJgiNo(String.valueOf(user.getJgiNo()));
				}
			}
		}

		//カテゴリリストの設定
		initCategoryList(form);
		//前画面にて選択していたカテゴリを再設定（可能なら）
		List<String> categoryList = new ArrayList<String>();
		for (CodeAndValue codeAndValue : form.getProdCategoryList()) {
			categoryList.add(codeAndValue.getCode());
		}
		if(categoryBackUp != null && categoryList.contains(categoryBackUp)) {
			form.setCategory(categoryBackUp);
		}

		// 営業所以降が特定している場合は初期検索実行
		if (StringUtils.isNotBlank(form.getSosCd3()) && StringUtils.isNotBlank(form.getCategory())) {
			form.setTranField();
			searchPlannedProdList(form);
		}

		//グリッドのヘッダを設定
		setGridHeader(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.REFER)
	public Result dps302C00F05(DpContext ctx, Dps302C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C00F05");
			LOG.debug("SosCd3:" + form.getSosCd3());
			LOG.debug("SosCd4:" + form.getSosCd4());
			LOG.debug("jgiNo:" + form.getJgiNo());
		}

		// 検索実行
		form.setTranField();
		searchPlannedProdList(form);

		//カテゴリリストの設定
		initCategoryList(form);

		//グリッドのヘッダを設定
		setGridHeader(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * チーム別計画「公開解除」時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps302C00F10Execute(DpContext ctx, Dps302C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C00F10");
		}

		try {
			// 更新実行
			final String sosCd3 = form.getSosCd3Tran();
			final List<MrPlanStatusUpdateDto> updateDtoList = form.convertMrPlanStatusUpdateDto();
			final List<PlannedProd> plannedProdList = form.convertPlannedProdList();
			dpsMrPlanService.updateStatusTeamOpenRelease(sosCd3, plannedProdList, updateDtoList);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(updateDtoList.size())));

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setSosCd3(form.getSosCd3Tran());
			form.setSosCd4(form.getSosCd3Tran());
			form.setJgiNo(form.getJgiNoTran());

			// 更新対象の情報で組織従業員を書き換える
			if (StringUtils.isNotBlank(form.getJgiNoTran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), form.getJgiNoTran());
			} else if (StringUtils.isNotBlank(form.getSosCd4Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), null);
			} else if (StringUtils.isNotBlank(form.getSosCd3Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd3Tran(), null);
			}

			// 前回検索条件で再検索実行
			searchPlannedProdList(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * チーム別計画「公開」時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps302C00F11Execute(DpContext ctx, Dps302C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C00F11");
		}

		try {
			// 更新実行
			final String sosCd3 = form.getSosCd3Tran();
			final List<MrPlanStatusUpdateDto> updateDtoList = form.convertMrPlanStatusUpdateDto();
			final List<PlannedProd> plannedProdList = form.convertPlannedProdList();
			dpsMrPlanService.updateStatusTeamOpen(sosCd3, plannedProdList, updateDtoList);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(updateDtoList.size())));

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setSosCd3(form.getSosCd3Tran());
			form.setSosCd4(form.getSosCd3Tran());
			form.setJgiNo(form.getJgiNoTran());

			// 更新対象の情報で組織従業員を書き換える
			if (StringUtils.isNotBlank(form.getJgiNoTran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), form.getJgiNoTran());
			} else if (StringUtils.isNotBlank(form.getSosCd4Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), null);
			} else if (StringUtils.isNotBlank(form.getSosCd3Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd3Tran(), null);
			}

			// 前回検索条件で再検索実行
			searchPlannedProdList(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * チーム別計画「確定解除」時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps302C00F12Execute(DpContext ctx, Dps302C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C00F12");
		}

		try {
			// 更新実行
			final String sosCd3 = form.getSosCd3Tran();
			final List<MrPlanStatusUpdateDto> updateDtoList = form.convertMrPlanStatusUpdateDto();
			final List<PlannedProd> plannedProdList = form.convertPlannedProdList();
			dpsMrPlanService.updateStatusTeamFixRelease(sosCd3, plannedProdList, updateDtoList);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(updateDtoList.size())));

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setSosCd3(form.getSosCd3Tran());
			form.setSosCd4(form.getSosCd3Tran());
			form.setJgiNo(form.getJgiNoTran());

			// 更新対象の情報で組織従業員を書き換える
			if (StringUtils.isNotBlank(form.getJgiNoTran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), form.getJgiNoTran());
			} else if (StringUtils.isNotBlank(form.getSosCd4Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), null);
			} else if (StringUtils.isNotBlank(form.getSosCd3Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd3Tran(), null);
			}

			// 前回検索条件で再検索実行
			searchPlannedProdList(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * チーム別計画「確定」時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps302C00F13Execute(DpContext ctx, Dps302C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C00F13");
		}

		try {
			// 更新実行
			final String sosCd3 = form.getSosCd3Tran();
			final List<MrPlanStatusUpdateDto> updateDtoList = form.convertMrPlanStatusUpdateDto();
			final List<PlannedProd> plannedProdList = form.convertPlannedProdList();
			dpsMrPlanService.updateStatusTeamFix(sosCd3, plannedProdList, updateDtoList);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(updateDtoList.size())));

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setSosCd3(form.getSosCd3Tran());
			form.setSosCd4(form.getSosCd3Tran());
			form.setJgiNo(form.getJgiNoTran());

			// 更新対象の情報で組織従業員を書き換える
			if (StringUtils.isNotBlank(form.getJgiNoTran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), form.getJgiNoTran());
			} else if (StringUtils.isNotBlank(form.getSosCd4Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), null);
			} else if (StringUtils.isNotBlank(form.getSosCd3Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd3Tran(), null);
			}

			// 前回検索条件で再検索実行
			searchPlannedProdList(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 担当者別計画「公開解除」時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps302C00F14Execute(DpContext ctx, Dps302C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C00F10");
		}

		try {
			// 更新実行
			final String sosCd3 = form.getSosCd3Tran();
			final List<MrPlanStatusUpdateDto> updateDtoList = form.convertMrPlanStatusUpdateDto();
			final List<PlannedProd> plannedProdList = form.convertPlannedProdList();
			dpsMrPlanService.updateStatusMrOpenRelease(sosCd3, plannedProdList, updateDtoList);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(updateDtoList.size())));

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setSosCd3(form.getSosCd3Tran());
			form.setSosCd4(form.getSosCd3Tran());
			form.setJgiNo(form.getJgiNoTran());

			// 更新対象の情報で組織従業員を書き換える
			if (StringUtils.isNotBlank(form.getJgiNoTran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), form.getJgiNoTran());
			} else if (StringUtils.isNotBlank(form.getSosCd4Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), null);
			} else if (StringUtils.isNotBlank(form.getSosCd3Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd3Tran(), null);
			}

			// 前回検索条件で再検索実行
			searchPlannedProdList(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 担当者別計画「公開」時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps302C00F15Execute(DpContext ctx, Dps302C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C00F11");
		}

		try {
			// 更新実行
			final String sosCd3 = form.getSosCd3Tran();
			final List<MrPlanStatusUpdateDto> updateDtoList = form.convertMrPlanStatusUpdateDto();
			final List<PlannedProd> plannedProdList = form.convertPlannedProdList();
			dpsMrPlanService.updateStatusMrOpen(sosCd3, plannedProdList, updateDtoList);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(updateDtoList.size())));

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setSosCd3(form.getSosCd3Tran());
			form.setSosCd4(form.getSosCd3Tran());
			form.setJgiNo(form.getJgiNoTran());

			// 更新対象の情報で組織従業員を書き換える
			if (StringUtils.isNotBlank(form.getJgiNoTran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), form.getJgiNoTran());
			} else if (StringUtils.isNotBlank(form.getSosCd4Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), null);
			} else if (StringUtils.isNotBlank(form.getSosCd3Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd3Tran(), null);
			}

			// 前回検索条件で再検索実行
			searchPlannedProdList(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 担当者別計画「確定解除」時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps302C00F16Execute(DpContext ctx, Dps302C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C00F12");
		}

		try {
			// 更新実行
			final String sosCd3 = form.getSosCd3Tran();
			final List<MrPlanStatusUpdateDto> updateDtoList = form.convertMrPlanStatusUpdateDto();
			final List<PlannedProd> plannedProdList = form.convertPlannedProdList();
			dpsMrPlanService.updateStatusMrFixRelease(sosCd3, plannedProdList, updateDtoList);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(updateDtoList.size())));

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setSosCd3(form.getSosCd3Tran());
			form.setSosCd4(form.getSosCd3Tran());
			form.setJgiNo(form.getJgiNoTran());

			// 更新対象の情報で組織従業員を書き換える
			if (StringUtils.isNotBlank(form.getJgiNoTran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), form.getJgiNoTran());
			} else if (StringUtils.isNotBlank(form.getSosCd4Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), null);
			} else if (StringUtils.isNotBlank(form.getSosCd3Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd3Tran(), null);
			}

			// 前回検索条件で再検索実行
			searchPlannedProdList(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 担当者別計画「確定」時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps302C00F17Execute(DpContext ctx, Dps302C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C00F13");
		}

		try {
			// 更新実行
			final String sosCd3 = form.getSosCd3Tran();
			final List<MrPlanStatusUpdateDto> updateDtoList = form.convertMrPlanStatusUpdateDto();
			final List<PlannedProd> plannedProdList = form.convertPlannedProdList();
			dpsMrPlanService.updateStatusMrFix(sosCd3, plannedProdList, updateDtoList);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(updateDtoList.size())));

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setSosCd3(form.getSosCd3Tran());
			form.setSosCd4(form.getSosCd3Tran());
			form.setJgiNo(form.getJgiNoTran());

			// 更新対象の情報で組織従業員を書き換える
			if (StringUtils.isNotBlank(form.getJgiNoTran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), form.getJgiNoTran());
			} else if (StringUtils.isNotBlank(form.getSosCd4Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), null);
			} else if (StringUtils.isNotBlank(form.getSosCd3Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd3Tran(), null);
			}

			// 前回検索条件で再検索実行
			searchPlannedProdList(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 担当者別計画「確定」時の調整金額チェックに呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(correspondType = ASYNC)
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps302C00F18(DpContext ctx, Dps302C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C00F18");
		}

		String sosCd = form.getSosCd3Tran();
		String prodCode = form.getCheckProdCode();
		String category = form.getCategory();

		// チェック実行
		OfficeTeamPlanChoseiDto reultDto = dpsMrPlanSearchService.searchChosei(sosCd, prodCode, category);

		List<CodeAndValue> list = new ArrayList<CodeAndValue>();
		list.add(new CodeAndValue("choseiUhFlg", reultDto.getChoseiUhFlg().toString()));
		list.add(new CodeAndValue("choseiPFlg", reultDto.getChoseiPFlg().toString()));

		ctx.getRequest().setAttribute("resultList", list);
		return ActionResult.SUCCESS;

	}

	/**
	 * 計画値コピー処理時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps302C00F20Execute(DpContext ctx, Dps302C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C00F20Execute");
		}

		try {

			// 一括コピー実行
			dpsMrPlanService.executePlannedValueCopy(form.convertPlannedValueCopyDto());

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPS0009I"));

		} finally {
			// 更新対象の情報でFormを書き換える
			form.setSosCd3(form.getSosCd3Tran());
			form.setSosCd4(form.getSosCd3Tran());
			form.setJgiNo(form.getJgiNoTran());

			// 更新対象の情報で組織従業員を書き換える
			if (StringUtils.isNotBlank(form.getJgiNoTran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), form.getJgiNoTran());
			} else if (StringUtils.isNotBlank(form.getSosCd4Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), null);
			} else if (StringUtils.isNotBlank(form.getSosCd3Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd3Tran(), null);
			}

			// 前回検索条件で再検索実行
			searchPlannedProdList(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理を実行する。
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchPlannedProdList(Dps302C00Form form) throws Exception {

		// 計画対象品目一覧の検索実行
		PlannedProdScDto scDto = form.convertPlannedProdScDto();
		PlannedProdResultListDto plannedProdResultListDto = dpsMrPlanSearchService.searchPlannedProdList(scDto);

		// リクエストボックスに計画対象品目一覧をセット
		super.getRequestBox().put(Dps302C00Form.DPS302C00_DATA_R_SEARCH_RESULT, plannedProdResultListDto);

		// 営業所計画ステータスの検索(営業所まで特定した場合のみ)
		final String sosCd3 = scDto.getSosCd3();
		final String sosCd4 = scDto.getSosCd4();
		final Integer sosJgiNo = scDto.getJgiNo();
		if (StringUtils.isNotBlank(sosCd3) && StringUtils.isBlank(sosCd4) && sosJgiNo == null) {
			OfficePlanStatusResultDto officePlanStatusResultDto = dpsMrPlanSearchService.searchOfficePlanStatus(sosCd3,form.getCategory());

			// リクエストボックスに営業所計画ステータスをセット
			super.getRequestBox().put(Dps302C00Form.DPS302C00_DATA_R_OFFICE_PLAN, officePlanStatusResultDto);
		}
	}

	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * 検索処理時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps302C00F05Validation(DpContext ctx, Dps302C00Form form) throws ValidateException {

		// 組織 必須チェック
		if (StringUtils.isEmpty(form.getSosCd3())) {
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			throw new ValidateException(new Conveyance(new MessageKey("DPC1025E", "組織・従業員", "エリア", "従業員")));
//			throw new ValidateException(new Conveyance(new MessageKey("DPC1025E", "組織・従業員", "営業所", "従業員")));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
		}
	}

	/**
	 * チーム別計画「公開解除」処理時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps302C00F10Validation(DpContext ctx, Dps302C00Form form) throws ValidateException {
		executeValidation(ctx, form);
	}

	/**
	 * チーム別計画「公開」処理時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps302C00F11Validation(DpContext ctx, Dps302C00Form form) throws ValidateException {
		executeValidation(ctx, form);
	}

	/**
	 * チーム別計画「確定解除」処理時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps302C00F12Validation(DpContext ctx, Dps302C00Form form) throws ValidateException {
		executeValidation(ctx, form);
	}

	/**
	 * チーム別計画「確定」処理時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps302C00F13Validation(DpContext ctx, Dps302C00Form form) throws ValidateException {
		executeValidation(ctx, form);
	}

	/**
	 * 担当者別計画「公開解除」処理時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps302C00F14Validation(DpContext ctx, Dps302C00Form form) throws ValidateException {
		executeValidation(ctx, form);
	}

	/**
	 * 担当者別計画「公開」処理時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps302C00F15Validation(DpContext ctx, Dps302C00Form form) throws ValidateException {
		executeValidation(ctx, form);
	}

	/**
	 * 担当者別計画「確定解除」処理時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps302C00F16Validation(DpContext ctx, Dps302C00Form form) throws ValidateException {
		executeValidation(ctx, form);
	}

	/**
	 * 担当者別計画「確定」処理時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps302C00F17Validation(DpContext ctx, Dps302C00Form form) throws ValidateException {
		executeValidation(ctx, form);
	}

	/**
	 * 計画値コピー処理時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps302C00F20Validation(DpContext ctx, Dps302C00Form form) throws ValidateException {
	}

	/**
	 * ステータス更新時の共通Validation処理を実行する。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	private void executeValidation(DpContext ctx, Dps302C00Form form) throws ValidateException {

		// 組織 必須チェック
		if (StringUtils.isEmpty(form.getSosCd3Tran())) {
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			throw new ValidateException(new Conveyance(new MessageKey("DPC1024E", "組織・従業員", "エリア")));
//			throw new ValidateException(new Conveyance(new MessageKey("DPC1024E", "組織・従業員", "営業所")));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
		}

		// 更新パラーメータチェック
		String[] rowIdList = form.getRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}

		for (int i = 0; i < rowIdList.length; i++) {

			// パラーメータのサイズチェック
			String[] rowId = rowIdList[i].split(",", 4);
			if (rowId.length != 4) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// シーケンスキー(試算前はEMPTYがあり得るため型チェックのみ)
			if (StringUtils.isNotEmpty(rowId[0]) && !ValidationUtil.isLong(rowId[0])) {
				final String errMsg = "受信パラメータ(シーケンスキー)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 最終更新日(試算前はEMPTYがあり得るため型チェックのみ)
			if (StringUtils.isNotEmpty(rowId[1]) && !ValidationUtil.isLong(rowId[1])) {
				final String errMsg = "受信パラメータ(最終更新日)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 品目固定コード
			if (StringUtils.isEmpty(rowId[2])) {
				final String errMsg = "受信パラメータ(品目固定コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 品目名称
			if (StringUtils.isEmpty(rowId[3])) {
				final String errMsg = "受信パラメータ(品目名称)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
		}
	}

	/**
	 * グリッドのヘッダを設定する
	 * @param form
	 */
	private void setGridHeader(Dps302C00Form form) {

		// ヘッダ(営業所)の設定
		String headerOffice = null;
		// 追加ヘッダ(担当)の設定
		String attachHeaderMr = null;

		// データ区分
		String dataKbn = null;

		if(dpsCodeMasterSearchService.isVaccine(form.getCategory())) {
			//カテゴリ：ワクチンの場合
			dataKbn = DpsCodeMaster.ITV.getDbValue();
		}else {
			//カテゴリ：ワクチン以外の場合
			dataKbn = DpsCodeMaster.IT.getDbValue();
		}

		List<CodeAndValue> list = new ArrayList<CodeAndValue>();
		// 対象区分、対象区分リスト(ワクチン含む）リストを設定
		List<DpsCCdMst> insTypeList = (dpsCodeMasterSearchService.searchCodeByDataKbn(dataKbn));
		for (DpsCCdMst mst :insTypeList) {
			CodeAndValue cad = new CodeAndValue(mst.getDataCd(), mst.getDataName());
			list.add(cad);
		}

		if (list.size() != 0 && list.size() >= 2) {
			//ヘッダ（UH）
			form.setHeaderUh(list.get(0).getValue());
			//ヘッダ（P）
			form.setHeaderP(list.get(1).getValue());

			headerOffice = "選択,品目名称,担当者別計画,#cspan,#cspan,#cspan," + list.get(0).getValue() + ",#cspan,#cspan," + list.get(1).getValue() + ",#cspan,#cspan,#cspan,#cspan";

			attachHeaderMr = "#rspan,担当者別計画所長確定," + list.get(0).getValue() + "," + list.get(1).getValue();

	    }
		//ヘッダ(営業所)
		form.setHeaderOffice(headerOffice);
		//追加ヘッダ(担当)
		form.setAttachHeaderMr(attachHeaderMr);

	}

    /**
     * カテゴリリストの初期設定
     * @param form Dps302C00Form
     */
    private void initCategoryList(Dps302C00Form form) {
		// 汎用マスタのカテゴリリスト
		List<DpsCCdMst> categoryList = (dpsCodeMasterSearchService.searchCategoryList(null,PLAN_ID));

		// カテゴリリストに表示するリスト
		List<String> targetCategoryAry = new ArrayList<String>();
		// 計画対象カテゴリ領域の設定
		List<String> planLvCtgList = new ArrayList<String>();


//		if (StringUtils.isNotBlank(form.getJgiNo())) {
			// 計画立案レベル-担当
			planLvCtgList = dpsPlannedCtgSearchService.searchCategoryByPlanLevel(ProdPlanLevel.MR);
//		}else if(StringUtils.isNotBlank(form.getSosCd3())){
//			// 計画立案レベル-営業所
//			planLvCtgList = dpsPlannedCtgSearchService.searchCategoryByPlanLevel(ProdPlanLevel.OFFICE);
//		}else {
//			// 計画立案レベル-全て
//			planLvCtgList = dpsPlannedCtgSearchService.searchCategoryByPlanLevel(ProdPlanLevel.ALL);
//		}

		String sosCd = null;
		if (form.getJgiNo() != null ) {
			// 組織コード：担当者
			sosCd = form.getSosCd3();
		}else if(StringUtils.isNotBlank(form.getSosCd3())){
			// 組織コード：営業所
			sosCd = form.getSosCd3();
		}

		// 組織と紐づくカテゴリリスト
		List<SosCtg> ctgList = dpsSosCtgSearchService.searchDpsSosCtgList(sosCd, SCREEN_ID);

		for(SosCtg ctg : ctgList) {
			targetCategoryAry.add(ctg.getCategory());
		}

		List<CodeAndValue> list = new ArrayList<CodeAndValue>();

		boolean indexFlg = true;
		// 対象の立案レベルをもつ計画対象カテゴリ領域のカテゴリのみ設定
		for (DpsCCdMst mst :categoryList) {
			if (targetCategoryAry.contains(mst.getDataCd()) && planLvCtgList.contains(mst.getDataCd())) {
				CodeAndValue cad = new CodeAndValue(mst.getDataCd(), mst.getDataName());
				list.add(cad);
				if (form.getCategory() == null && indexFlg) {
					form.setCategory(cad.getCode());
					indexFlg = false;
				}
			}
		}
		form.setProdCategoryList(list);
    }

}
