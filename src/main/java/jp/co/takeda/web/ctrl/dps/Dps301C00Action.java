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
import jp.co.takeda.dto.EstimationExecDto;
import jp.co.takeda.dto.EstimationExecOrgDto;
import jp.co.takeda.dto.EstimationParamsDto;
import jp.co.takeda.dto.EstimationProdListResultDto;
import jp.co.takeda.dto.EstimationProdResultDto;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.DpsPlannedCtg;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.CalcType;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.model.view.SosCtg;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcUserSearchService;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.service.DpsEstimationProdSearchService;
import jp.co.takeda.service.DpsEstimationProdService;
import jp.co.takeda.service.DpsPlannedCtgSearchService;
import jp.co.takeda.service.DpsSosCtgSearchService;
import jp.co.takeda.task.IyakuEstimationJob;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps301C00Form;

/**
 * Dps301C00((医)試算対象品目一覧画面)のアクションクラス
 *
 * @author nozaki
 */
@Controller("Dps301C00Action")
public class Dps301C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps301C00Action.class);

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPS301C00";

	/**
	 * 計画（1：期別、2：月別）
	 */
	private static final String PLAN_ID = "1";

	/**
	 * 計画立案レベル-担当者（0判定に使用）
	 */
	private static final String PLANLEVEL_MR_ZERO = "0";


	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 試算機能 検索系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsEstimationProdSearchService")
	protected DpsEstimationProdSearchService dpsEstimationProdSearchService;

	/**
	 * 試算機能 更新系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsEstimationProdService")
	protected DpsEstimationProdService dpsEstimationProdService;

	/**
	 * 試算ジョブ
	 */
	@Autowired(required = true)
	@Qualifier("iyakuEstimationJob")
	protected IyakuEstimationJob iyakuEstimationJob;

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
	 * 計画支援の組織カテゴリコード検索
	 */
	@Autowired(required = true)
	@Qualifier("dpsSosCtgSearchService")
	protected DpsSosCtgSearchService dpsSosCtgSearchService;

	/**
	 * 計画対象カテゴリ領域の検索サービス
	 */
    @Autowired(required = true)
    @Qualifier("dpsPlannedCtgSearchService")
    protected DpsPlannedCtgSearchService dpsPlannedCtgSearchService;

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
	public Result dps301C00F00(DpContext ctx, Dps301C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps301C00F00");
		}

		// 前画面にて選択されていたカテゴリの保持
		String categoryBackUp = form.getCategory();

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
			//カテゴリリストの設定
			initCategoryList(form);
		}

		// ログインユーザの組織情報をセット
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {
				// 権限確認コード
				if (user.isSosLvl(SCREEN_ID, SosLvl.BRANCH)) {
					if (!user.getSosCd3().equals(JgiMst.BLANK_SOS_CD)) {
						form.setSosCd3(user.getSosCd3());
					}
				}
				else if (user.isSosLvl(SCREEN_ID, SosLvl.OFFICE)) {
					if (!user.getSosCd3().equals(JgiMst.BLANK_SOS_CD)) {
						form.setSosCd3(user.getSosCd3());
					}
				}
				else if (user.isSosLvl(SCREEN_ID, SosLvl.MR)) {
					if (!user.getSosCd3().equals(JgiMst.BLANK_SOS_CD)) {
						form.setSosCd3(user.getSosCd3());
					}
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

		// 検索実行
		form.setTranField();
		searchEstimationProd(form);


		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.REFER)
	public Result dps301C00F05(DpContext ctx, Dps301C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps301C00F05");
		}

		//カテゴリリストの設定
		initCategoryList(form);

		// 検索実行
		form.setTranField();
		searchEstimationProd(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * 試算処理実行時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps301C00F10Execute(DpContext ctx, Dps301C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps301C00F10Execute");
		}

		String sosCd = form.getSosCd3Tran();
// add Start 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
		String category = form.getCategoryTran();
// add End 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
// add Start 2022/6/06 H.Futagami バックログNo.17　検索時と試算時にカテゴリが異なっている場合、エラーメッセージを表示する。
		String categorySearch = form.getCategoryTran();
		String categorySelect = form.getCategory();
// add End 2022/6/06 H.Futagami バックログNo.17　検索時と試算時にカテゴリが異なっている場合、エラーメッセージを表示する。
		List<EstimationExecDto> estimationExecDtoList = form.getEstimationExecList();

		try {

			// 試算前の情報処理
			List<EstimationExecOrgDto> execList = dpsEstimationProdSearchService.searchEstimationPreparation(sosCd, estimationExecDtoList);

			// 試算処理実行
			DpUser dpUser = DpUserInfo.getDpUserInfo().getLoginUser();
// mod Start 2022/6/06 H.Futagami バックログNo.17　検索時と試算時にカテゴリが異なっている場合、エラーメッセージを表示する。
// mod Start 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
//			EstimationParamsDto dto = new EstimationParamsDto(ctx.getDpMetaInfo(), sosCd, dpUser, execList, CalcType.getInstance(form.getCalcType()));
//			EstimationParamsDto dto = new EstimationParamsDto(ctx.getDpMetaInfo(), sosCd, dpUser, execList, CalcType.getInstance(form.getCalcType()), category);
// mod Start 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
			EstimationParamsDto dto = new EstimationParamsDto(ctx.getDpMetaInfo(), sosCd, dpUser, execList, CalcType.getInstance(form.getCalcType()), category, categorySearch, categorySelect);
// mod End 2022/6/06 H.Futagami バックログNo.17　検索時と試算時にカテゴリが異なっている場合、エラーメッセージを表示する。
			iyakuEstimationJob.execute(dto);

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setSosCd3(form.getSosCd3Tran());

			// 更新対象の情報で組織従業員を書き換える
			dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd3Tran(), null);


			// 前回検索条件で再検索実行
			searchEstimationProd(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 実績および未獲得市場チェック処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(correspondType = ASYNC)
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps301C00F15Execute(DpContext ctx, Dps301C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps301C00F15Execute");
		}

		String sosCd = form.getSosCd3Tran();
		String prodCodes = form.getCheckProdCode();
		String[] prodCodeArr = prodCodes.split(",");

		// 実績チェック実行
		List<String> resultMessageList = dpsEstimationProdSearchService.searchNoResultProdList(sosCd, prodCodeArr);

		// 結果の品目名をカンマ区切りで返す
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < resultMessageList.size(); i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append(resultMessageList.get(i));
		}

		List<CodeAndValue> list = new ArrayList<CodeAndValue>();
		list.add(new CodeAndValue("messageList", sb.toString()));

		ctx.getRequest().setAttribute("resultList", list);
		return ActionResult.SUCCESS;

	}

	/**
	 * 検索処理を実行する。
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchEstimationProd(Dps301C00Form form) throws Exception {

		EstimationProdListResultDto resultDto = null;
		form.setExistSearchDataFlag(false);

		// 組織コード（営業所）が設定されている場合は、検索表示
		if (!StringUtils.isEmpty(form.getSosCd3Tran()) && !StringUtils.isEmpty(form.getCategory())) {

			// カテゴリの計画立案レベルを取得
			DpsPlannedCtg dpsplannedctg = null;
			dpsplannedctg = dpsPlannedCtgSearchService.searchPlannedCtg(form.getCategory());
			//カテゴリの計画立案レベル-担当者が「0」の場合、データなしとする。
			if(dpsplannedctg.getPlanLevelMr() == PLANLEVEL_MR_ZERO) {
				// リクエストに検索結果リストをセット
				super.getRequestBox().put(Dps301C00Form.DPS301C00_DATA_R, (ArrayList<EstimationProdResultDto>) null);
			}else {
				// 検索実行
				resultDto = dpsEstimationProdSearchService.searchEstimationProdList(form.getSosCd3Tran(),form.getCategory());
				// リクエストに検索結果リストをセット
				super.getRequestBox().put(Dps301C00Form.DPS301C00_DATA_R, (ArrayList<EstimationProdResultDto>) resultDto.getEstimationProdResultDtoList());
				form.setExistSearchDataFlag(true);
				if (resultDto != null && resultDto.getCalcType() != null) {
					form.setCalcType(resultDto.getCalcType().getDbValue());
				} else {
					form.setCalcType(null);
				}
			}
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
	public void dps301C00F05Validation(DpContext ctx, Dps301C00Form form) throws ValidateException {

		// 組織 必須チェック
		if (StringUtils.isEmpty(form.getSosCd3())) {
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			throw new ValidateException(new Conveyance(new MessageKey("DPC1024E", "組織", "エリア")));
//			throw new ValidateException(new Conveyance(new MessageKey("DPC1024E", "組織", "営業所")));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
		}
	}

	/**
	 * 試算処理実行時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps301C00F10Validation(DpContext ctx, Dps301C00Form form) throws ValidateException {

		// 組織 必須チェック
		if (StringUtils.isEmpty(form.getSosCd3Tran())) {
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			throw new ValidateException(new Conveyance(new MessageKey("DPC1024E", "組織", "エリア")));
//			throw new ValidateException(new Conveyance(new MessageKey("DPC1024E", "組織", "営業所")));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
		}

		// 更新行のNullチェック
		String[] rowIdList = form.getRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}

		for (int i = 0; i < rowIdList.length; i++) {

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
     * @param form Dps301C00Form
     */
    private void initCategoryList(Dps301C00Form form) {
		// 汎用マスタのカテゴリリスト
		List<DpsCCdMst> categoryList = (dpsCodeMasterSearchService.searchCategoryList(null,PLAN_ID));

		// カテゴリリストに表示するリスト
		List<String> targetCategoryAry = new ArrayList<String>();
		// 計画対象カテゴリ領域の設定
		List<String> planLvCtgList = new ArrayList<String>();
		// 計画立案レベル-担当者
		planLvCtgList = dpsPlannedCtgSearchService.searchCategoryByPlanLevel(ProdPlanLevel.MR);

		String sosCd = null;
		if (StringUtils.isNotBlank(form.getSosCd3())) {
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
