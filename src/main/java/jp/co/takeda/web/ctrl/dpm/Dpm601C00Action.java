package jp.co.takeda.web.ctrl.dpm;

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
import jp.co.takeda.dto.InsMonthPlanUpdateDto;
import jp.co.takeda.dto.InsProdMonthPlanResultDto;
import jp.co.takeda.dto.InsProdMonthPlanResultHeaderDto;
import jp.co.takeda.dto.InsProdMonthPlanScDto;
import jp.co.takeda.dto.ManagePlanUpdateResultDto;
import jp.co.takeda.model.Cal;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.SosMstCtg;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.Month;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.model.div.Term;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpmCodeMasterSearchService;
import jp.co.takeda.service.DpmCommonService;
import jp.co.takeda.service.DpmInsMonthPlanSearchService;
import jp.co.takeda.service.DpmInsMonthPlanService;
import jp.co.takeda.service.DpmPlannedCtgSearchService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dpm.Dpm601C00Form;

/**
 * Dpm601C00((医)施設品目別計画編集画面)のアクションクラス
 *
 * @author stakeuchi
 */
@Controller("Dpm601C00Action")
public class Dpm601C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dpm601C00Action.class);

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPM601C00";

	/**
	 * 計画（1：期別、2：月別）
	 */
	private static final String PLAN_ID = "2";

	// -------------------------------
	// injection field
	// -------------------------------

	/**
	 * 施設別計画(月別) 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmInsMonthPlanSearchService")
	protected DpmInsMonthPlanSearchService dpmInsMonthPlanSearchService;

	/**
	 * 施設別計画(月別) 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmInsMonthPlanService")
	protected DpmInsMonthPlanService dpmInsMonthPlanService;

	/**
	 * カテゴリ検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmCodeMasterSearchService")
	DpmCodeMasterSearchService dpmCodeMasterSearchService;

    /**
     * 計画対象カテゴリ領域検索サービス
     */
    @Autowired(required = true)
    @Qualifier("dpmPlannedCtgSearchService")
    DpmPlannedCtgSearchService dpmPlannedCtgSearchService;

    /**
     * 管理の共通サービス
     */
    @Autowired(required = true)
    @Qualifier("dpmCommonService")
    DpmCommonService dpmCommonService;


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
	public Result dpm601C00F00(DpContext ctx, Dpm601C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm601C00F00");
		}

		// 初期化処理
		form.formInit();

		// ログインユーザの組織情報をセット
		//全MRフラグを設定
		form.setMrFlg(false);
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {
				// 権限確認コード
				if (user.isSosLvl(SCREEN_ID, SosLvl.MR)) {
					form.setSosCd2(user.getSosCd2());
					form.setSosCd3(user.getSosCd3());
					form.setJgiNo(String.valueOf(user.getJgiNo()));
					//全MRフラグを設定
					form.setMrFlg(true);
				}
			}
		}

		// カテゴリリストの初期設定
		initCategoryList(form);

        //add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		form.setTougetuCount(TougetuHanteiJisseki());
        //add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示

		// グリッドのヘッダを設定
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
	public Result dpm601C00F05(DpContext ctx, Dpm601C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm601C00F05");
			LOG.debug("JgiNo:" + form.getJgiNo());
			LOG.debug("InsNo:" + form.getInsNo());
			LOG.debug("ProdCategory:" + form.getProdCategory());
			LOG.debug("PlanData:" + form.getPlanData());
		}

		// カテゴリリストを初期設定する
		initCategoryList(form);
		// 検索実行
		form.setTranField();
		searchInsProdPlanList(ctx, form);
		// MR担当対応　ログインユーザの事業所番号をセット
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {
				if (user.isSosLvl(SCREEN_ID, SosLvl.MR)) {
					form.setJgiNo(String.valueOf(user.getJgiNo()));
					//全MRフラグを設定
					form.setMrFlg(true);
				}
			}
		}
        // グリッドのヘッダを設定
		setGridHeader(form);
		// 編集可能リストを設定
		Cal today = dpmCommonService.searchToday();
		String calYear = today.getCalYear();
		String calMonth = today.getCalMonth();
        //add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		form.setTougetuCount(TougetuHanteiJisseki());
        //add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		form.setEnableEditList(DpmMonthlyPlanHelper.getEnableEdit(calYear,calMonth));

		return ActionResult.SUCCESS;
	}

	/**
	 * 登録処理時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dpm601C00F10Execute(DpContext ctx, Dpm601C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm601C00F10Execute");
			LOG.debug("JgiNo:" + form.getJgiNo());
			LOG.debug("InsNo:" + form.getInsNo());
			LOG.debug("ProdCategory:" + form.getProdCategory());
			LOG.debug("PlanData:" + form.getPlanData());
		}

		try {
			// 更新DTO
			List<InsMonthPlanUpdateDto> updateDtoList = form.convertInsMonthPlanUpdateDto();

			// 更新実行
			ManagePlanUpdateResultDto resultDto = new ManagePlanUpdateResultDto(0, 0, 0);
			if (!updateDtoList.isEmpty()) {
				resultDto = dpmInsMonthPlanService.updateInsMonthPlan(SCREEN_ID, updateDtoList);
			}

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(resultDto.getUpdateTotalCount())));

		} finally {
			// 再検索実行
			searchInsProdPlanList(ctx, form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理を実行する。
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchInsProdPlanList(DpContext ctx, Dpm601C00Form form) throws Exception {

		// 実行した検索条件を画面に設定
		InsProdMonthPlanScDto scDto = form.convertInsProdPlanScDto();

		// JRNSに含まれるカテゴリリスト
		List<String> jrnsCtgList = createJrnsCategoryList(form);

		form.setViewField();
		try {

			InsProdMonthPlanResultDto resultDto = dpmInsMonthPlanSearchService.searchInsProdMonthPlan(scDto, jrnsCtgList);
			super.getRequestBox().put(Dpm601C00Form.DPM601C00_DATA_R_SEARCH_RESULT, resultDto);


		} finally {

			// ヘッダー情報取得
			InsProdMonthPlanResultHeaderDto headerDto = dpmInsMonthPlanSearchService.searchInsMonthPlanHeader(scDto.getInsNo(), null);
			super.getRequestBox().put(Dpm601C00Form.DPM601C00_DATA_R_HEADER, headerDto);

			// 削除施設の場合はメッセージを追加
			if (headerDto != null && headerDto.IsDeleteIns()) {
				addErrorMessage(ctx, new MessageKey("DPM1005E"));
			}

			// ヘッダー施設情報が取得できなかった場合は、ログイン情報設定
			if (headerDto == null) {
				setUserInfo(form);
			}

			form.convertHeaderDto(headerDto);
		}
	}

	/**
	 * ログインユーザの組織情報を設定する。
	 *
	 * @param form ActionForm
	 */
	private void setUserInfo(Dpm601C00Form form) {

		// ログインユーザの組織情報をセット
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {
				form.setSosCd2(null);
				form.setSosCd3(null);
				form.setSosCd4(null);
				form.setJgiNo(null);
				if (user.isMatch(JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF)) {
					form.setSosCd2(user.getSosCd2());
				} else if (user.isMatch(JokenSet.OFFICE_MASTER, JokenSet.OFFICE_STAFF)) {
					form.setSosCd2(user.getSosCd2());
					form.setSosCd3(user.getSosCd3());
				}
				if (user.isSosLvl(SCREEN_ID, SosLvl.MR)) {
					form.setSosCd2(user.getSosCd2());
					form.setSosCd3(user.getSosCd3());
					form.setJgiNo(String.valueOf(user.getJgiNo()));
					//全MRフラグを設定
					form.setMrFlg(true);
				}
			}
		}
	}

	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * 検索処理時の入力パラメータチェック
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dpm601C00F05Validation(DpContext ctx, Dpm601C00Form form) throws ValidateException {
		// [必須] 施設コード
		if (StringUtils.isEmpty(form.getInsNo())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "施設コード")));
		}
	}

	/**
	 * 登録処理時の入力パラメータチェック
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dpm601C00F10Validation(DpContext ctx, Dpm601C00Form form) throws ValidateException {
		executeValidation(ctx, form);
	}

	/**
	 * 登録処理時の共通Validationメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	private void executeValidation(DpContext ctx, Dpm601C00Form form) throws ValidateException {
		// 更新パラーメータチェック
		String[] rowIdList = form.getRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		for (int i = 0; i < rowIdList.length; i++) {
			// パラーメータのサイズチェック
			String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);
			if (rowId.length != 29) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 施設コード
			if (StringUtils.isEmpty(rowId[0])) {
				final String errMsg = "受信パラメータ(施設コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 対象区分
			if (StringUtils.isEmpty(rowId[1])) {
				final String errMsg = "受信パラメータ(対象区分)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 品目コード
			if (StringUtils.isEmpty(rowId[2])) {
				final String errMsg = "受信パラメータ(品目コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// シーケンスキー
			if (StringUtils.isNotEmpty(rowId[3]) && !ValidationUtil.isLong(rowId[3])) {
				final String errMsg = "受信パラメータ(シーケンスキー)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 最終更新日
			if (StringUtils.isNotEmpty(rowId[4]) && !ValidationUtil.isLong(rowId[4])) {
				final String errMsg = "受信パラメータ(最終更新日)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 更新前月初計画1（ＹB価）
			if (StringUtils.isNotEmpty(rowId[5]) && !ValidationUtil.isLong(rowId[5])) {
				final String errMsg = "受信パラメータ(更新前月初計画1)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 更新前月初計画2（ＹB価）
			if (StringUtils.isNotEmpty(rowId[6]) && !ValidationUtil.isLong(rowId[6])) {
				final String errMsg = "受信パラメータ(更新前月初計画2)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 更新前月初計画3（ＹB価）
			if (StringUtils.isNotEmpty(rowId[7]) && !ValidationUtil.isLong(rowId[7])) {
				final String errMsg = "受信パラメータ(更新前月初計画3)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 更新前月初計画4（ＹB価）
			if (StringUtils.isNotEmpty(rowId[8]) && !ValidationUtil.isLong(rowId[8])) {
				final String errMsg = "受信パラメータ(更新前月初計画4)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 更新前月初計画5（ＹB価）
			if (StringUtils.isNotEmpty(rowId[9]) && !ValidationUtil.isLong(rowId[9])) {
				final String errMsg = "受信パラメータ(更新前月初計画5)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 更新前月初計画6（ＹB価）
			if (StringUtils.isNotEmpty(rowId[10]) && !ValidationUtil.isLong(rowId[10])) {
				final String errMsg = "受信パラメータ(更新前月初計画6)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 更新前月末見込1（ＹB価）
			if (StringUtils.isNotEmpty(rowId[11]) && !ValidationUtil.isLong(rowId[11])) {
				final String errMsg = "受信パラメータ(更新前月末見込1)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 更新前月末見込2（ＹB価）
			if (StringUtils.isNotEmpty(rowId[12]) && !ValidationUtil.isLong(rowId[12])) {
				final String errMsg = "受信パラメータ(更新前月末見込2)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 更新前月末見込3（ＹB価）
			if (StringUtils.isNotEmpty(rowId[13]) && !ValidationUtil.isLong(rowId[13])) {
				final String errMsg = "受信パラメータ(更新前月末見込3)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 更新前月末見込4（ＹB価）
			if (StringUtils.isNotEmpty(rowId[14]) && !ValidationUtil.isLong(rowId[14])) {
				final String errMsg = "受信パラメータ(更新前月末見込4)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 更新前月末見込5（ＹB価）
			if (StringUtils.isNotEmpty(rowId[15]) && !ValidationUtil.isLong(rowId[15])) {
				final String errMsg = "受信パラメータ(更新前月末見込5)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 更新前月末見込6（ＹB価）
			if (StringUtils.isNotEmpty(rowId[16]) && !ValidationUtil.isLong(rowId[16])) {
				final String errMsg = "受信パラメータ(更新前月末見込6)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 月初計画1（ＹB価）
			if (StringUtils.isNotEmpty(rowId[17]) && !ValidationUtil.isLong(rowId[17])) {
				final String errMsg = "受信パラメータ(月初計画1)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 月初計画2（ＹB価）
			if (StringUtils.isNotEmpty(rowId[18]) && !ValidationUtil.isLong(rowId[18])) {
				final String errMsg = "受信パラメータ(月初計画2)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 月初計画3（ＹB価）
			if (StringUtils.isNotEmpty(rowId[19]) && !ValidationUtil.isLong(rowId[19])) {
				final String errMsg = "受信パラメータ(月初計画3)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 月初計画4（ＹB価）
			if (StringUtils.isNotEmpty(rowId[20]) && !ValidationUtil.isLong(rowId[20])) {
				final String errMsg = "受信パラメータ(月初計画4)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 月初計画5（ＹB価）
			if (StringUtils.isNotEmpty(rowId[21]) && !ValidationUtil.isLong(rowId[21])) {
				final String errMsg = "受信パラメータ(月初計画5)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 月初計画6（ＹB価）
			if (StringUtils.isNotEmpty(rowId[22]) && !ValidationUtil.isLong(rowId[22])) {
				final String errMsg = "受信パラメータ(月初計画6)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 月末見込1（ＹB価）
			if (StringUtils.isNotEmpty(rowId[23]) && !ValidationUtil.isLong(rowId[23])) {
				final String errMsg = "受信パラメータ(月末見込1)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 月末見込2（ＹB価）
			if (StringUtils.isNotEmpty(rowId[24]) && !ValidationUtil.isLong(rowId[24])) {
				final String errMsg = "受信パラメータ(月末見込2)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 月末見込3（ＹB価）
			if (StringUtils.isNotEmpty(rowId[25]) && !ValidationUtil.isLong(rowId[25])) {
				final String errMsg = "受信パラメータ(月末見込3)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 月末見込4（ＹB価）
			if (StringUtils.isNotEmpty(rowId[26]) && !ValidationUtil.isLong(rowId[26])) {
				final String errMsg = "受信パラメータ(月末見込4)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 月末見込5（ＹB価）
			if (StringUtils.isNotEmpty(rowId[27]) && !ValidationUtil.isLong(rowId[27])) {
				final String errMsg = "受信パラメータ(月末見込5)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 月末見込6（ＹB価）
			if (StringUtils.isNotEmpty(rowId[28]) && !ValidationUtil.isLong(rowId[28])) {
				final String errMsg = "受信パラメータ(月末見込6)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
		}
	}

	/**
	 * グリッドのヘッダを設定する
	 * @param form
	 */
	private void setGridHeader(Dpm601C00Form form) {

		// 年度・期の設定
		SysManage sysManage = DpUserInfo.getDpUserInfo().getSysManage();

		String sysYear = sysManage.getSysYear();
		Term sysTerm = sysManage.getSysTerm();

		String headerDetail = null;
		String headerSum = null;
		String attachHeader1row = null;
		int month = 0;
		String attachHeader2row = null;
		String attachHeader3row = null;
		String initWidths = null;
		String colAlign = null;
		String colTypes = null;
		String enableResizing = null;
		String enableTooltips = null;

		//入力制御用の月リスト
		List<String> monthList = new ArrayList<String>();
		//入力制御用の年
		form.setYear(sysYear);

		//期別計画
// mod Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
//		headerDetail = ",";
		headerDetail = ",,,";
//		headerSum = "#cspan";
		headerSum = "#cspan,#cspan,#cspan";
		//上期
//		if (sysTerm != null && Term.FIRST == sysTerm) {
		if (sysTerm != null && Term.FIRST == sysTerm) {
//			attachHeader1row = sysYear.concat("年上期");
			attachHeader1row = sysYear.concat("年上期,#cspan,#cspan");
//		}
		}
		//下期
//		if (sysTerm != null && Term.SECOND == sysTerm) {
		if (sysTerm != null && Term.SECOND == sysTerm) {
//			attachHeader1row = sysYear.concat("年下期");
			attachHeader1row = sysYear.concat("年下期,#cspan,#cspan");
//		}
		}
//		attachHeader2row = "#rspan";
		attachHeader2row = "#rspan,,";
//		if(dpmCodeMasterSearchService.isVaccine(form.getProdCategory())) {
		if(dpmCodeMasterSearchService.isVaccine(form.getProdCategory())) {
//			attachHeader3row = "期別計画(B)";
			attachHeader3row = "期別計画(B),累計実績(B),当月実績(B)";
//		}else{
		}else{
//			attachHeader3row = "期別計画(Y)";
			attachHeader3row = "期別計画(Y),累計実績(Y),当月実績(Y)";
//		}
		}
//		initWidths = "90";
		initWidths = "90,90,90";
//		colAlign = "right";
		colAlign = "right,right,right";
//		colTypes = "ron";
		colTypes = "ron,ron,ron";
//		enableResizing = "false";
		enableResizing = "false,false,false";
//		enableTooltips = "false";
		enableTooltips = "false,false,false";

//		//各月
//		for (int count = 0; count < Month.PERIOD_MONTH; count++){
		for (int count = 0; count < Month.PERIOD_MONTH; count++){
//			headerDetail = headerDetail.concat(",,,,");
			headerDetail = headerDetail.concat(",,,,,");
//			headerSum = headerSum.concat(",#cspan,#cspan,#cspan,#cspan");
			headerSum = headerSum.concat(",#cspan,#cspan,#cspan,#cspan,#cspan");
//			attachHeader1row = attachHeader1row.concat(",");
			attachHeader1row = attachHeader1row.concat(",");
			//上期
//			if (sysTerm != null && Term.FIRST == sysTerm) {
			if (sysTerm != null && Term.FIRST == sysTerm) {
//				month = Term.FIRST_MONTH + count;
				month = Term.FIRST_MONTH + count;
//			}
			}
			//下期
//			if (sysTerm != null && Term.SECOND == sysTerm) {
			if (sysTerm != null && Term.SECOND == sysTerm) {
//				month = Term.SECOND_MONTH + count;
				month = Term.SECOND_MONTH + count;
//			}
			}
//			if (month > Month.YEAR_MONTH) {
			if (month > Month.YEAR_MONTH) {
//				attachHeader1row = attachHeader1row.concat(Integer.toString(month -  Month.YEAR_MONTH));
				attachHeader1row = attachHeader1row.concat(Integer.toString(month -  Month.YEAR_MONTH));
			//入力制御用の月リストを生成
//				monthList.add(Integer.toString(month -  Month.YEAR_MONTH));
				monthList.add(Integer.toString(month -  Month.YEAR_MONTH));
//			}else {
			}else {
//				attachHeader1row = attachHeader1row.concat(Integer.toString(month));
				attachHeader1row = attachHeader1row.concat(Integer.toString(month));
				//入力制御用の月リストを生成
//				monthList.add(Integer.toString(month));
				monthList.add(Integer.toString(month));
//			}
			}
//			attachHeader1row = attachHeader1row.concat("月,#cspan,#cspan,#cspan");
			attachHeader1row = attachHeader1row.concat("月,#cspan,#cspan,#cspan,#cspan");
//			attachHeader2row = attachHeader2row.concat(",月初計画,#cspan,月末見込,#cspan");
//			if(dpmCodeMasterSearchService.isVaccine(form.getProdCategory())) {
			if(dpmCodeMasterSearchService.isVaccine(form.getProdCategory())) {
				attachHeader2row = attachHeader2row.concat(",月初計画,#cspan,月末見込,#cspan,実績(B)");
//				attachHeader3row = attachHeader3row.concat(",計画(B),遂行率(計画),見込(B),遂行率(見込)");
				attachHeader3row = attachHeader3row.concat(",計画(B),遂行率(計画),見込(B),遂行率(見込),#rspan");
//			}else {
			}else {
				attachHeader2row = attachHeader2row.concat(",月初計画,#cspan,月末見込,#cspan,実績(Y)");
//				attachHeader3row = attachHeader3row.concat(",計画(Y),遂行率(計画),見込(Y),遂行率(見込)");
				attachHeader3row = attachHeader3row.concat(",計画(Y),遂行率(計画),見込(Y),遂行率(見込),#rspan");
//			}
			}
//			initWidths = initWidths.concat(",90,90,90,90");
			initWidths = initWidths.concat(",90,90,90,90,90");
//			colAlign = colAlign.concat(",right,right,right,right");
			colAlign = colAlign.concat(",right,right,right,right,right");
//			colTypes = colTypes.concat(",ron,ron,ron,ron");
			colTypes = colTypes.concat(",ron,ron,ron,ron,ron");
//			enableResizing = enableResizing.concat(",false,false,false,false");
			enableResizing = enableResizing.concat(",false,false,false,false,false");
//			enableTooltips = enableTooltips.concat(",false,false,false,false");
			enableTooltips = enableTooltips.concat(",false,false,false,false,false");
//		}
		}
// mod End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		//入力制御用の月リストを生成
		form.setMonthList(monthList);


		//合計
		headerDetail = headerDetail.concat(",,,,");
		headerSum = headerSum.concat(",#cspan,#cspan,#cspan,#cspan");
		attachHeader1row = attachHeader1row.concat(",合計,#cspan,#cspan,#cspan");
		attachHeader2row = attachHeader2row.concat(",月初計画,#cspan,月末見込,#cspan");
		if(dpmCodeMasterSearchService.isVaccine(form.getProdCategory())) {
			attachHeader3row = attachHeader3row.concat(",計画(B),遂行率(計画),見込(B),遂行率(見込)");
		}else {
			attachHeader3row = attachHeader3row.concat(",計画(Y),遂行率(計画),見込(Y),遂行率(見込)");
		}
		initWidths = initWidths.concat(",90,90,90,90");
		colAlign = colAlign.concat(",right,right,right,right");
		colTypes = colTypes.concat(",ron,ron,ron,ron");
		enableResizing = enableResizing.concat(",false,false,false,false");
		enableTooltips = enableTooltips.concat(",false,false,false,false");

		form.setHeaderDetail(headerDetail);
		form.setHeaderSum(headerSum);
		form.setAttachHeader1row(attachHeader1row);
		form.setAttachHeader2row(attachHeader2row);
		form.setAttachHeader3row(attachHeader3row);
		form.setInitWidths(initWidths);
		form.setColAlign(colAlign);
		form.setColTypes(colTypes);
		form.setEnableResizing(enableResizing);
		form.setEnableTooltips(enableTooltips);

	}


	/**
	 * カテゴリリストの初期設定
	 * @param form Dpm601C00Form
	 */
	private void initCategoryList(Dpm601C00Form form) {

		// 品目カテゴリ一覧の取得と設定
		List<DpmCCdMst> categoryList = (dpmCodeMasterSearchService.searchCategorylist(null, PLAN_ID));

		List<CodeAndValue> list = new ArrayList<CodeAndValue>();

		boolean indexFlg = true;

		// 計画対象カテゴリ領域の設定
		List<String> planLvCtgList = new ArrayList<String>();
		// 計画立案レベル-施設
		planLvCtgList = dpmPlannedCtgSearchService.searchCategoryByPlanLevel(ProdPlanLevel.INS);


		// ログインユーザの組織カテゴリコードを取得
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		List<String> sosCategoryList = new ArrayList<String>();
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {
				if (user.getSosCategoryList() != null && !user.getSosCategoryList().isEmpty()) {
					for (SosMstCtg cat : user.getSosCategoryList()) {
						sosCategoryList.add(cat.getCategoryCd());
					}
				}
				if (user.isSosLvl(SCREEN_ID, SosLvl.BRANCH) || user.isSosLvl(SCREEN_ID, SosLvl.ALL)) {
					// 支店スタッフ以上の場合、JRNS選択可能
					form.setJrnsUser(Boolean.TRUE);
				}
			}
		}

		// JRNSの対象となるカテゴリコード
		String jrnsCode = dpmCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.JRNS.getDbValue()).get(0).getDataCd();

		// JRNSに含まれるカテゴリリスト
		List<String> jrnsCategoryList = new ArrayList<String>();
		jrnsCategoryList.add(dpmCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.OFFICE.getDbValue()).get(0).getDataCd());
		jrnsCategoryList.add(dpmCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.NSG.getDbValue()).get(0).getDataCd());
		jrnsCategoryList.add(dpmCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.VAC.getDbValue()).get(0).getDataCd());
		jrnsCategoryList.add(dpmCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.CV.getDbValue()).get(0).getDataCd());

		form.setJrnsCategoryList(jrnsCategoryList);

		// カテゴリリストに、JRNSに含まれるカテゴリが表示されているか
		boolean isExistJrnsCtg = Boolean.FALSE;

		// 上記で取得したコードのカテゴリの内、対象の立案レベルをもつ計画対象カテゴリ領域のカテゴリのみカテゴリリストにセットする
		for (DpmCCdMst mst : categoryList) {
			// JRNSの場合、支店スタッフ以上である＆営業所・NSG・ワクチン・CVのいずれかがリストに存在する場合に、リストに追加する
			if (jrnsCode.equals(mst.getDataCd()) && form.getJrnsUser() && isExistJrnsCtg) {
				CodeAndValue cad = new CodeAndValue(mst.getDataCd(), mst.getDataName());
				list.add(cad);
			}
			else if (sosCategoryList.contains(mst.getDataCd()) && planLvCtgList.contains(mst.getDataCd())) {
				CodeAndValue cad = new CodeAndValue(mst.getDataCd(), mst.getDataName());
				list.add(cad);
				if (jrnsCategoryList.contains(mst.getDataCd())) {
					isExistJrnsCtg = Boolean.TRUE;
				}
				if (form.getProdCategory() == null && indexFlg) {
					form.setProdCategory(cad.getCode());
					indexFlg = false;
				}
			}
		}

		form.setProdCategoryList(list);
	}

	/**
	 * JRNSに含まれるカテゴリリストを生成する
	 *
	 * @param form Dpm601Form
	 * @return JRNSに含まれるカテゴリリスト
	 */
	private List<String> createJrnsCategoryList(Dpm601C00Form form) {
		// JRNSに含まれるカテゴリリスト
		List<String> jrnsCtgList = new ArrayList<String>();
		String jrnsCode = dpmCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.JRNS.getDbValue()).get(0).getDataCd();
		if (jrnsCode.equals(form.getProdCategory())) {
			String officeCode = dpmCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.OFFICE.getDbValue()).get(0).getDataCd();
			String nsgCode = dpmCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.NSG.getDbValue()).get(0).getDataCd();
			String vacCode = dpmCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.VAC.getDbValue()).get(0).getDataCd();
			String cvCode = dpmCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.CV.getDbValue()).get(0).getDataCd();

			List<String> categoryList = new ArrayList<String>();

			for (CodeAndValue cat : form.getProdCategoryList()) {
				categoryList.add(cat.getCode());
			}

			// 表示されているカテゴリリストに存在している場合のみリストに追加
			if (categoryList.contains(officeCode)) {
				jrnsCtgList.add(officeCode);
			}
			if (categoryList.contains(nsgCode)) {
				jrnsCtgList.add(nsgCode);
			}
			if (categoryList.contains(vacCode)) {
				jrnsCtgList.add(vacCode);
			}
			if (categoryList.contains(cvCode)) {
				jrnsCtgList.add(cvCode);
			}
		}

		return jrnsCtgList;
	}

	//add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
	private int TougetuHanteiJisseki() {
		// カレンダーから今期の何月目かを設定（falseCountに何月目かを記録（1月目なら0が入る）
		Cal today = dpmCommonService.searchToday();
		String calYear = today.getCalYear();
		String calMonth = today.getCalMonth();
		String calDay = today.getCalDay();

		//第3営業日を検索
		Cal bizDays = dpmCommonService.searchBizDays(calYear,calMonth,3);
		String calDayBiz3 = bizDays.getCalDay();

		int calYearInt = Integer.parseInt(calYear);
		int calMonthInt = Integer.parseInt(calMonth);

		SysManage sysManage = DpUserInfo.getDpUserInfo().getSysManage();

		String sysYear = sysManage.getSysYear();
		int sysYearInt = Integer.parseInt(sysYear);
		Term sysTerm = sysManage.getSysTerm();

		int falseCount = 6;

		switch (sysTerm) {
		case FIRST: // 4,5,6,7,8,9
			if(!sysYear.equals(calYear)) {
				break;
			}
			falseCount = calMonthInt - 4;
			break;
		case SECOND:// 10,11,12,1,2,3
			Integer.parseInt(sysYear);
			if(calYearInt == sysYearInt || calYearInt == sysYearInt + 1) {
				/* do nothing */
			}else {
				break;
			}
			if (calMonthInt < 10) calMonthInt = calMonthInt + 6;
			falseCount = calMonthInt - 10;
			break;
		default:
			break;
		}
		//第3営業日までは前々月の実績とする（第3営業日までは実績反映されないため）
		if(Integer.parseInt(calDay) <= Integer.parseInt(calDayBiz3) && falseCount > 0) {
			falseCount = falseCount - 1;
		}
		return falseCount;
	}
	//add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
}
