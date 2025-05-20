package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.security.DpAuthority.AuthType.*;

import java.util.ArrayList;
import java.util.Arrays;
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
import jp.co.takeda.dto.TmsTytenDistParamDto;
import jp.co.takeda.dto.TmsTytenDistParamResultDto;
import jp.co.takeda.dto.TmsTytenPlanDistProdResultDto;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.DpsPlannedCtg;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.model.view.SosCtg;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcSystemManageSearchService;
import jp.co.takeda.service.DpcUserSearchService;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.service.DpsPlannedCtgSearchService;
import jp.co.takeda.service.DpsSosCtgSearchService;
import jp.co.takeda.service.DpsSosJgiSearchService;
import jp.co.takeda.service.DpsTmsTytenPlanSearchService;
import jp.co.takeda.task.IyakuTmsTytenDistJob;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps500C00Form;

/**
 * Dps500C00((医)特約店別計画配分対象品目一覧画面)のアクションクラス
 *
 * @author yokokawa
 */
@Controller("Dps500C00Action")
public class Dps500C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps500C00Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 特約店別計画配分対象品目一覧 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsTmsTytenPlanSearchService")
	protected DpsTmsTytenPlanSearchService dpsTmsTytenPlanSearchService;

	/**
	 * 特約店別計画配分ジョブ
	 */
	@Autowired(required = true)
	@Qualifier("iyakuTmsTytenDistJob")
	protected IyakuTmsTytenDistJob iyakuTmsTytenDistJob;

	/**
	 * ユーザサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpcUserSearchService")
	protected DpcUserSearchService dpcUserSearchService;

	/**
	 * 組織・従業員に関するサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsSosJgiSearchService")
	protected DpsSosJgiSearchService dpsSosJgiSearchService;

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

	/**
	 * 納入計画管理検索サービス実装クラス
	 */
    @Autowired(required = true)
    @Qualifier("dpcSystemManageSearchService")
    protected DpcSystemManageSearchService dpcSystemManageSearchService;

	/**
	 * 計画（1：期別、2：月別）
	 */
	private static final String PLAN_ID = "1";

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPS500C00";

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
	@Permission(authType = AuthType.REFER)
	public Result dps500C00F00(DpContext ctx, Dps500C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps500C00F00");
		}

		// 初期化処理
		form.formInit();

		// T価変換後のメッセージ設定
		SysManage sysManage = DpUserInfo.getDpUserInfo().getSysManage();
		if (sysManage.getTransTFlg()) {
			super.addErrorMessage(ctx, new MessageKey("DPS3322E"));
		}

		// 組織従業員初期化処理
		SosMst defaultSosMst = DpUserInfo.getDpUserInfo().getDefaultSosMst();
		String searchSosCd = null;
		if (defaultSosMst != null) {
			if (defaultSosMst.getBumonRank() == BumonRank.SITEN_TOKUYAKUTEN_BU) {
				form.setSosCd2(defaultSosMst.getSosCd());
				form.setSosCd2Tran(defaultSosMst.getSosCd());
				dpcUserSearchService.changeDefaultSosJgi(null, null, defaultSosMst.getSosCd(), null);
				searchSosCd = defaultSosMst.getSosCd();
			} else if (defaultSosMst.getBumonRank() == BumonRank.OFFICE_TOKUYAKUTEN_G) {
				form.setSosCd2(defaultSosMst.getUpSosCd());
				form.setSosCd2Tran(defaultSosMst.getUpSosCd());
				dpcUserSearchService.changeDefaultSosJgi(null, null, defaultSosMst.getUpSosCd(), null);
				searchSosCd = defaultSosMst.getUpSosCd();
			} else if (defaultSosMst.getBumonRank() == BumonRank.TEAM) {
				String upupSosCd = dpsSosJgiSearchService.searchUpSosCode(defaultSosMst.getUpSosCd());
				form.setSosCd2(upupSosCd);
				form.setSosCd2Tran(upupSosCd);
				dpcUserSearchService.changeDefaultSosJgi(null, null, upupSosCd, null);
				searchSosCd = upupSosCd;
			}
			// カテゴリ初期化処理
			initCategoryList(form);
			form.setCategoryTran(form.getCategory());
			searchTmsTytenPlanDistProdList(searchSosCd, form.getCategory());
		}

		// ログインユーザの組織情報をセット
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {
				// 権限確認コード
				if (user.isSosLvl(SCREEN_ID, SosLvl.BRANCH)) {
					form.setSosCd2(user.getSosCd2());
				}
				else if (user.isSosLvl(SCREEN_ID, SosLvl.OFFICE)) {
					form.setSosCd2(user.getSosCd2());
					form.setSosCd3(user.getSosCd3());
				}
				else if (user.isSosLvl(SCREEN_ID, SosLvl.MR)) {
					form.setSosCd2(user.getSosCd2());
					form.setSosCd3(user.getSosCd3());
					form.setJgiNo(String.valueOf(user.getJgiNo()));
				}
			}
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理(プルダウン/組織選択)時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission( authType = REFER)
	public Result dps500C00F05(DpContext ctx, Dps500C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps500C00F05");
		}

//		initCategoryList(form);
		SysType sysType = SysType.IYAKU;
		if (dpsCodeMasterSearchService.isVaccine(form.getCategory())) {
			sysType = SysType.VACCINE;
		}
		// T価変換後のメッセージ設定
		SysManage sysManage = dpcSystemManageSearchService.searchSysManage(SysClass.DPS, sysType);
		if (sysManage.getTransTFlg()) {
			super.addErrorMessage(ctx, new MessageKey("DPS3322E"));
		}

		// 検索条件の保存
		form.setSosCd2Tran(form.getSosCd2());
		form.setCategoryTran(form.getCategory());
		searchTmsTytenPlanDistProdList(form.getSosCd2Tran(), form.getCategoryTran());

		initCategoryList(form);
		return ActionResult.SUCCESS;
	}

	/**
	 * 配分を実行するボタンクリック時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission( authType = EDIT)
	public Result dps500C00F10Execute(DpContext ctx, Dps500C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps500C00F10Execute");
		}

		try {

			// 特約店別計画配分処理に必要な情報を検索
			List<String> prodCodeList = Arrays.asList(form.getProdCode());
			TmsTytenDistParamResultDto tmsTytenDistParamResultDto = dpsTmsTytenPlanSearchService.searchTmsTytenDistParam(form.getSosCd2Tran(), prodCodeList, form.getCategoryTran());

			DpUser loginUser = DpUserInfo.getDpUserInfo().getLoginUser();

			// 特約店別計画配分用のパラメータを作成
			TmsTytenDistParamDto tmsTytenDistParamDto = new TmsTytenDistParamDto(ctx.getDpMetaInfo(), loginUser, tmsTytenDistParamResultDto);

			// 配分処理実行
			iyakuTmsTytenDistJob.execute(tmsTytenDistParamDto);

		} finally {

			// 特約店別計画配分対象品目一覧を検索(検索後リクエストに設定)
			searchTmsTytenPlanDistProdList(form.getSosCd2Tran(), form.getCategoryTran());
		}
		return ActionResult.SUCCESS;
	}

	// -------------------------------
	// private method
	// -------------------------------
	/**
	 * 特約店別計画配分対象品目一覧を検索する。
	 *
	 * @param sosCd2 組織コード(支店)
	 * @param category カテゴリ
	 */
	private void searchTmsTytenPlanDistProdList(String sosCd2, String category) throws Exception {

		// 特約店別計画配分対象品目一覧を検索
		TmsTytenPlanDistProdResultDto resultDto = dpsTmsTytenPlanSearchService.searchDistProdList(sosCd2, category);

		// 検索結果をリクエストに設定
		super.getRequestBox().put(Dps500C00Form.DPS500C00_DATA_R, resultDto);
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
	public void dps500C00F05Validation(DpContext ctx, Dps500C00Form form) throws ValidateException {
		// 組織コード(支店) 必須チェック
		if (StringUtils.isEmpty(form.getSosCd2())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "組織")));
		}

		// カテゴリ 必須チェック
		if (StringUtils.isEmpty(form.getCategory())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "カテゴリ")));
		}
	}

	/**
	 * 配分実行時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps500C00F10Validation(DpContext ctx, Dps500C00Form form) throws ValidateException {
		// 組織コード(支店) 必須チェック
		if (StringUtils.isEmpty(form.getSosCd2Tran())) {
			final String errMsg = "組織コード(支店) がnullまたは空";
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "組織"), errMsg));
		}

		// 品目必須チェック
		if (form.getProdCode() == null || form.getProdCode().length <= 0) {
			final String errMsg = "品目固定コードリストがnullまたは空";
			throw new ValidateException(new Conveyance(new MessageKey("DPC1006E", "品目"), errMsg));
		}

		// カテゴリの計画立案レベルを取得
		if (StringUtils.isEmpty(form.getCategory())) {
			final String errMsg = "カテゴリがnullまたは空";
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "カテゴリ"), errMsg));
		}else {
			DpsPlannedCtg dpsplannedctg = null;
			dpsplannedctg = dpsPlannedCtgSearchService.searchPlannedCtg(form.getCategory());
			if(dpsplannedctg.getPlanLevelOffice().equals("0")) {
				final String errMsg = "計画支援カテゴリ領域の営業所立案レベルなし";
				throw new ValidateException(new Conveyance(new MessageKey("DPS3327E"), errMsg));
			}
		}

	}

	/**
	* カテゴリリストの初期設定
	* @param form Dps500C00Form
	*/
	private void initCategoryList(Dps500C00Form form) {
		// 汎用マスタのカテゴリリスト
		List<DpsCCdMst> categoryList = (dpsCodeMasterSearchService.searchCategoryList(null, PLAN_ID));

		// カテゴリリストに表示するリスト
		List<String> targetCategoryAry = new ArrayList<String>();
		// 計画対象カテゴリ領域の設定
		List<String> planLvCtgList = new ArrayList<String>();
		// 計画立案レベル-特約店
		planLvCtgList = dpsPlannedCtgSearchService.searchCategoryByPlanLevel(ProdPlanLevel.WS);

		String sosCd = null;
		if (StringUtils.isNotBlank(form.getSosCd2())) {
			// 組織コード：支店
			sosCd = form.getSosCd2();
		}
		// 組織と紐づくカテゴリリスト
		List<SosCtg> ctgList = dpsSosCtgSearchService.searchDpsSosCtgList(sosCd, SCREEN_ID);

		for (SosCtg ctg : ctgList) {
			targetCategoryAry.add(ctg.getCategory());
		}

		List<CodeAndValue> list = new ArrayList<CodeAndValue>();

		boolean indexFlg = true;
		// 対象の立案レベルをもつ計画対象カテゴリ領域のカテゴリのみ設定
		for (DpsCCdMst mst : categoryList) {
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
