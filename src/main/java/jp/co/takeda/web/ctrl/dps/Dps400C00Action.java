package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

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
import jp.co.takeda.dto.DistributionExecOrgDto;
import jp.co.takeda.dto.DistributionParamsDto;
import jp.co.takeda.dto.InsWsDistProdResultDto;
import jp.co.takeda.dto.InsWsDistProdUpdateDto;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.model.view.SosCtg;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcUserSearchService;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.service.DpsDistributionProdSearchService;
import jp.co.takeda.service.DpsDistributionProdService;
import jp.co.takeda.service.DpsPlannedCtgSearchService;
import jp.co.takeda.service.DpsSosCtgSearchService;
import jp.co.takeda.task.IyakuDistributionJob;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps400C00Form;

/**
 * Dps400C00((医)施設特約店別計画配分対象品目一覧画面)のアクションクラス
 *
 * @author stakeuchi
 */
@Controller("Dps400C00Action")
public class Dps400C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps400C00Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 配分機能 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsDistributionProdSearchService")
	protected DpsDistributionProdSearchService dpsDistributionProdSearchService;

	/**
	 * 配分機能 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsDistributionProdService")
	protected DpsDistributionProdService dpsDistributionProdService;

	/**
	 * ユーザサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpcUserSearchService")
	protected DpcUserSearchService dpcUserSearchService;

	/**
	 * 配分ジョブ
	 */
	@Autowired(required = true)
	@Qualifier("iyakuDistributionJob")
	protected IyakuDistributionJob iyakuDistributionJob;

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
	 * 計画（1：期別、2：月別）
	 */
	private static final String PLAN_ID = "1";

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPS400C00";

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
	public Result dps400C00F00(DpContext ctx, Dps400C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps400C00F00");
		}

		// 初期化処理
		form.formInit();

		// 組織従業員初期化処理
		SosMst defaultSosMst = DpUserInfo.getDpUserInfo().getDefaultSosMst();
		if (defaultSosMst != null) {
			if (defaultSosMst.getBumonRank() == BumonRank.OFFICE_TOKUYAKUTEN_G) {
				form.setSosCd3(defaultSosMst.getSosCd());
				dpcUserSearchService.changeDefaultSosJgi(null, null, defaultSosMst.getSosCd(), null);
			} else if (defaultSosMst.getBumonRank() == BumonRank.TEAM) {
				form.setSosCd3(defaultSosMst.getUpSosCd());
				dpcUserSearchService.changeDefaultSosJgi(null, null, defaultSosMst.getUpSosCd(), null);
			}
		}

//		// カテゴリ初期設定
//		if (StringUtils.isEmpty(form.getCategory())) {
//			String category = dpcUserSearchService.searchDefaultProdCategoryCode();
//			form.setCategory(category);
//		}


		initCategoryList(form);

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

		if (StringUtils.isNotBlank(form.getCategory())) {
			// 検索実行
			form.setTranField();
			searchInsWsDistProdList(form);
		}

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
	public Result dps400C00F05(DpContext ctx, Dps400C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps400C00F05");
		}

		// 検索実行
		form.setTranField();
		initCategoryList(form);
		searchInsWsDistProdList(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * 「配分処理を実行」時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps400C00F10Execute(DpContext ctx, Dps400C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps400C00F10Execute");
		}

		String sosCd = form.getSosCd3();
		if (StringUtils.isEmpty(form.getSosCd3()) && StringUtils.isEmpty(form.getSosCd3Tran())){
			// ワクチンの場合、組織未選択時は全社対象とするため
			sosCd = null;
		}else if(StringUtils.isEmpty(form.getSosCd3Tran())){
			// 組織変更していない場合
			sosCd = form.getSosCd3();
		}
		String category = form.getCategory();
		boolean isMrOpenCheck = form.getIsMrOpenCheck();
		List<InsWsDistProdUpdateDto> insWsDistProdUpdateDtoList = form.convertInsWsDistProdUpdateDto();

		try {
			// 配分前の情報処理
			List<DistributionExecOrgDto> execList = dpsDistributionProdSearchService.searchDistributionPreparation(sosCd, insWsDistProdUpdateDtoList);

			// 配分処理実行
			DpUser dpUser = DpUserInfo.getDpUserInfo().getLoginUser();
			DistributionParamsDto dto = new DistributionParamsDto(ctx.getDpMetaInfo(), sosCd, category, dpUser, execList, isMrOpenCheck);
			iyakuDistributionJob.execute(dto);

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setCategory(form.getCategoryTran());
			form.setSosCd3(form.getSosCd3Tran());

			// 更新対象の情報で組織従業員を書き換える
			dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd3Tran(), null);

			// 再検索
			searchInsWsDistProdList(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理を実行する。
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchInsWsDistProdList(Dps400C00Form form) throws Exception {

		String sosCd = form.getSosCd3Tran();
		if (StringUtils.isEmpty(sosCd)){
			// ワクチンの場合、組織未選択時は全社対象とするため
			sosCd = null;
		}

		super.getRequestBox().put(Dps400C00Form.DPS400C00_DATA_R_SEARCH_RESULT_EXIST, Boolean.FALSE);

		// 組織コード（営業所）が設定されている場合は、検索表示
		// ワクチンの場合、組織未選択時は全社対象とする
		if (!StringUtils.isEmpty(sosCd) || dpsCodeMasterSearchService.isVaccine(form.getCategory())) {

			// 配分対象品目一覧の検索実行
			List<InsWsDistProdResultDto> resultList = dpsDistributionProdSearchService.searchDistributionProdList(form.getCategory(), sosCd);

			// リクエストボックスに検索結果をセット
			super.getRequestBox().put(Dps400C00Form.DPS400C00_DATA_R_SEARCH_RESULT, (ArrayList<InsWsDistProdResultDto>) resultList);
			super.getRequestBox().put(Dps400C00Form.DPS400C00_DATA_R_SEARCH_RESULT_EXIST, Boolean.TRUE);
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
	public void dps400C00F05Validation(DpContext ctx, Dps400C00Form form) throws ValidateException {
		searchValidation(ctx, form);
	}

	/**
	 * 「配分処理を実行」時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps400C00F10Validation(DpContext ctx, Dps400C00Form form) throws ValidateException {
		executeValidation(ctx, form);
	}

	/**
	 * 検索処理時の共通Validationチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	private void searchValidation(DpContext ctx, Dps400C00Form form) throws ValidateException {
		// 組織コード(営業所) 必須チェック
		// ワクチンの場合、組織未選択時は全社対象とする
		if (StringUtils.isEmpty(form.getSosCd3()) && !dpsCodeMasterSearchService.isVaccine(form.getCategory())) {
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			throw new ValidateException(new Conveyance(new MessageKey("DPC1024E", "組織", "エリア")));
//			throw new ValidateException(new Conveyance(new MessageKey("DPC1024E", "組織", "営業所")));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
		}
		// カテゴリ 必須チェック
		if (StringUtils.isEmpty(form.getCategory())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "カテゴリ")));
		}
	}

	/**
	 * 更新処理時の共通Validationチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	private void executeValidation(DpContext ctx, Dps400C00Form form) throws ValidateException {
		// 更新パラーメータチェック
		String[] rowIdList = form.getRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		for (int i = 0; i < rowIdList.length; i++) {
			// パラーメータのサイズチェック
			String[] rowId = rowIdList[i].split(",", 2);
			if (rowId.length != 2) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 品目固定コード
			if (StringUtils.isEmpty(rowId[0])) {
				final String errMsg = "受信パラメータ(品目固定コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 品目名称
			if (StringUtils.isEmpty(rowId[1])) {
				final String errMsg = "受信パラメータ(品目名称)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
		}
	}

	/**
	* カテゴリリストの初期設定
	* @param form Dps400C00Form
	*/
	private void initCategoryList(Dps400C00Form form) {
		// 汎用マスタのカテゴリリスト
		List<DpsCCdMst> categoryList = (dpsCodeMasterSearchService.searchCategoryList(null, PLAN_ID));

		// カテゴリリストに表示するリスト
		List<String> targetCategoryAry = new ArrayList<String>();
		// 計画対象カテゴリ領域の設定
		List<String> planLvCtgList = new ArrayList<String>();
		// 計画立案レベル-担当者
		planLvCtgList = dpsPlannedCtgSearchService.searchCategoryByPlanLevel(ProdPlanLevel.INS_WS);

		String sosCd = null;
		if (StringUtils.isNotBlank(form.getSosCd3())) {
			// 組織コード：営業所
			sosCd = form.getSosCd3();
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
		if (StringUtils.isNotEmpty(form.getCategoryTran())){
			// 検索時のカテゴリが対象カテゴリリストにある場合、検索時のカテゴリをセットする
			for (CodeAndValue listcd : list) {
				if (form.getCategoryTran().equals(listcd.getCode())) {
					form.setCategory(form.getCategoryTran());
				}
			}
		}
		form.setProdCategoryList(list);
	}
}
