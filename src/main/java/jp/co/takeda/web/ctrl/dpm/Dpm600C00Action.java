package jp.co.takeda.web.ctrl.dpm;

import static jp.co.takeda.a.exp.ErrMessageKey.*;
import static jp.co.takeda.security.DpAuthority.AuthType.*;

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
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dao.ManagePlannedProdDao;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.InsMonthPlanForVacHeaderDto;
import jp.co.takeda.dto.InsMonthPlanForVacResultDto;
import jp.co.takeda.dto.InsMonthPlanResultDto;
import jp.co.takeda.dto.InsMonthPlanScDto;
import jp.co.takeda.dto.InsMonthPlanUpdateDto;
import jp.co.takeda.dto.InsPlanForVacScDto;
import jp.co.takeda.dto.InsProdMonthPlanResultHeaderDto;
import jp.co.takeda.dto.ManagePlanUpdateResultDto;
import jp.co.takeda.model.Cal;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.Month;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.model.div.Term;
import jp.co.takeda.model.view.SosCtg;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpmCodeMasterSearchService;
import jp.co.takeda.service.DpmCommonService;
import jp.co.takeda.service.DpmInsMonthPlanSearchService;
import jp.co.takeda.service.DpmInsMonthPlanService;
import jp.co.takeda.service.DpmInsPlanForVacSearchService;
import jp.co.takeda.service.DpmInsPlanForVacService;
import jp.co.takeda.service.DpmInsPlanService;
import jp.co.takeda.service.DpmPlannedCtgSearchService;
import jp.co.takeda.service.DpmReportService;
import jp.co.takeda.service.DpmSosCtgSearchService;
import jp.co.takeda.service.DpmSosJgiSearchService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dpm.Dpm600C00Form;

/**
 * Dpm600C00((医)施設別計画編集画面)のアクションクラス
 *
 * @author stakeuchi
 */
@Controller("Dpm600C00Action")
public class Dpm600C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dpm600C00Action.class);

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPM600C00";

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
	 * （ワ）施設別計画 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmInsPlanForVacSearchService")
	protected DpmInsPlanForVacSearchService dpmInsPlanForVacSearchService;

	/**
	 * 施設別計画 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmInsPlanService")
	protected DpmInsPlanService dpmInsPlanService;

	/**
	 * （ワ）施設別計画 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmInsPlanForVacService")
	protected DpmInsPlanForVacService dpmInsPlanForVacService;

	/**
	 * 組織従業員検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmSosJgiSearchService")
	protected DpmSosJgiSearchService dpmSosJgiSearchService;

	/**
	 * カテゴリ 検索サービス
	 */
    @Autowired(required = true)
    @Qualifier("dpmCodeMasterSearchService")
    protected DpmCodeMasterSearchService dpmCodeMasterSearchService;

	 /**
     * 帳票サービス
     */
    @Autowired(required = true)
    @Qualifier("dpmReportService")
    protected DpmReportService dpmReportService;

    /**
     * 組織カテゴリ検索サービス
     */
    @Autowired(required = true)
    @Qualifier("dpmSosCtgSearchService")
    protected DpmSosCtgSearchService dpmSosCtgSearchService;

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


	/**
	 * 計画対象品目
	 */
	@Autowired(required = true)
	@Qualifier("managePlannedProdDao")
	private ManagePlannedProdDao managePlannedProdDao;



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
	public Result dpm600C00F00(DpContext ctx, Dpm600C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm600C00F00");
		}

		// 初期化処理
		form.formInit();

		// ログイン情報設定
		setUserInfo(form);

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

		// カテゴリリストの初期設定
		initCategoryList(form);
		// 対象区分を設定
		setInsList(form);
		// グリッドのヘッダを設定
		setGridHeader(form);
		// 編集可能リストを設定
		Cal today = dpmCommonService.searchToday();
		String calYear = today.getCalYear();
		String calMonth = today.getCalMonth();
		form.setEnableEditList(DpmMonthlyPlanHelper.getEnableEdit(calYear,calMonth));
		return ActionResult.SUCCESS;
	}

	/**
	 * トップ画面からのアクセス時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.REFER)
	public Result dpm600C00F01(DpContext ctx, Dpm600C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm600C00F01");
			LOG.debug("topJgiNo:" + form.getTopJgiNo());
		}

		// 初期化処理
		form.formInit();

		// ログイン情報設定
		setUserInfo(form);

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
				// 汎用マスタのカテゴリリスト
				List<DpmCCdMst> categoryList = (dpmCodeMasterSearchService.searchCategory(null));
				// カテゴリリストに表示するリスト
				List<String> targetCategoryAry = new ArrayList<String>();
				String sosCd = null;
				if (form.getSosCd3() != null) {
					// 組織コード：営業所
					sosCd = form.getSosCd3();
				} else if (form.getSosCd2() != null) {
					// 組織コード：支店
					sosCd = form.getSosCd2();
				}
				// 組織と紐づくカテゴリリスト
				List<SosCtg> ctgList = dpmSosCtgSearchService.searchDpmSosCtgList(sosCd, SCREEN_ID);

				for(SosCtg ctg : ctgList) {
					targetCategoryAry.add(ctg.getCategory());
				}

				List<CodeAndValue> list = new ArrayList<CodeAndValue>();

				boolean indexFlg = true;
				// 全組織表示可能の場合は全部のリストを表示
				for (DpmCCdMst mst :categoryList) {
					if (user.isSosLvl(SCREEN_ID, SosLvl.ALL) || targetCategoryAry.contains(mst.getDataCd())) {
						CodeAndValue cad = new CodeAndValue(mst.getDataCd(), mst.getDataName());
						list.add(cad);
						if (indexFlg) {
							form.setProdCategory(cad.getCode());
							indexFlg = false;
						}
					}
				}
				form.setProdCategoryList(list);
			}
		}

		// 対象区分を設定
		setInsList(form);
        //add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		form.setTougetuCount(TougetuHanteiJisseki());
        //add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		// グリッドのヘッダを設定
		setGridHeader(form);
		// 編集可能リストを設定
		Cal today = dpmCommonService.searchToday();
		String calYear = today.getCalYear();
		String calMonth = today.getCalMonth();
		form.setEnableEditList(DpmMonthlyPlanHelper.getEnableEdit(calYear,calMonth));

		// カテゴリリストの初期設定
		initCategoryList(form);
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
	public Result dpm600C00F05(DpContext ctx, Dpm600C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm600C00F05");
			LOG.debug("JgiNo:" + form.getJgiNo());
			LOG.debug("InsNo:" + form.getInsNo());
			LOG.debug("InsType:" + form.getInsType());
			LOG.debug("ProdCategory:" + form.getProdCategory());
			LOG.debug("ProdCode:" + form.getProdCode());
			LOG.debug("PlanData:" + form.getPlanData());
		}
		// カテゴリリストの初期設定
		initCategoryList(form);
		// 検索実行
		form.setTranField();
		searchInsMonthPlanList(form);
		// 対象区分を設定
		setInsList(form);
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
	 * （ワ）検索処理時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = REFER)
	public Result dpm600C00F25(DpContext ctx, Dpm600C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm600C00F25");
			LOG.debug("JgiNo:" + form.getJgiNo());
			LOG.debug("InsNo:" + form.getInsNo());
			LOG.debug("InsType:" + form.getInsType());
			LOG.debug("ProdCategory:" + form.getProdCategory());
			LOG.debug("ProdCode:" + form.getProdCode());
			LOG.debug("PlanData:" + form.getPlanData());
		}

		// 検索実行
		form.setTranFieldVac();
		searchInsPlanForVacList(form);
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
	public Result dpm600C00F10Execute(DpContext ctx, Dpm600C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm600C00F10Execute");
			LOG.debug("JgiNo:" + form.getJgiNo());
			LOG.debug("InsNo:" + form.getInsNo());
			LOG.debug("InsType:" + form.getInsType());
			LOG.debug("ProdCategory:" + form.getProdCategory());
			LOG.debug("ProdCode:" + form.getProdCode());
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
			searchInsMonthPlanList(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * （ワ）登録処理時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dpm600C00F20Execute(DpContext ctx, Dpm600C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm600C00F20Execute");
			LOG.debug("JgiNo:" + form.getJgiNo());
			LOG.debug("InsNo:" + form.getInsNo());
			LOG.debug("InsType:" + form.getInsType());
			LOG.debug("ProdCategory:" + form.getProdCategory());
			LOG.debug("ProdCode:" + form.getProdCode());
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
			searchInsPlanForVacList(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理の実行
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchInsMonthPlanList(Dpm600C00Form form) throws Exception {

		// 実行した検索条件を画面に設定
		InsMonthPlanScDto scDto = form.convertInsMonthPlanScDto(dpmCodeMasterSearchService,managePlannedProdDao);
		form.setViewField();
		try {
			InsMonthPlanResultDto resultDto = dpmInsMonthPlanSearchService.searchInsMonthPlan(scDto, true);
			super.getRequestBox().put(Dpm600C00Form.DPM600C00_DATA_R_SEARCH_RESULT, resultDto);
		} catch (DataNotFoundException e) {
			throw e;
		} finally {
			// 施設コードが入力された場合は、ヘッダー情報取得
			if (StringUtils.isNotEmpty(scDto.getInsNo())) {

				// ヘッダー情報取得
				InsProdMonthPlanResultHeaderDto headerDto = dpmInsMonthPlanSearchService.searchInsMonthPlanHeaderOyako(scDto.getInsNo(),scDto.getProdCode(),scDto.getProdCategory());

				// ヘッダー施設情報が取得できなかった場合は、ログイン情報設定
				if (headerDto == null) {
					setUserInfo(form);
				}

				// ヘッダー施設情報設定
				form.convertHeaderDto(headerDto);
			}
		}

	}

	/**
	 * （ワ）検索処理の実行
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchInsPlanForVacList(Dpm600C00Form form) throws Exception {

		// 実行した検索条件を画面に設定
		InsPlanForVacScDto scDto = form.convertInsPlanVacScDto(dpmCodeMasterSearchService,managePlannedProdDao);
		form.setViewFieldVac();
		InsMonthPlanForVacResultDto resultDto = null;
		try {
		    resultDto  = dpmInsMonthPlanSearchService.searchInsMonthPlanForVac(scDto);
			super.getRequestBox().put(Dpm600C00Form.DPM600C01_DATA_R_SEARCH_RESULT, resultDto);
		} catch (DataNotFoundException e) {
			throw e;
		} finally {
			// 施設コードが入力された場合は、ヘッダー情報取得
			if (StringUtils.isNotEmpty(scDto.getInsNo())) {

				// ヘッダー情報取得
				InsMonthPlanForVacHeaderDto headerDto = dpmInsMonthPlanSearchService.searchInsMonthPlanHeaderForVac(scDto.getInsNo());

				// ヘッダー施設情報が取得できなかった場合は、ログイン情報設定
				if (headerDto == null) {
					setUserInfo(form);
				}

				// ヘッダー施設情報設定
				form.convertHeaderVacDto(headerDto);
			}
		}
	}


	/**
	 * ログインユーザの組織情報を設定する
	 *
	 * @param form ActionForm
	 */
	private void setUserInfo(Dpm600C00Form form) {

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
	public void dpm600C00F05Validation(DpContext ctx, Dpm600C00Form form) throws ValidateException {
		// [必須] 担当者または施設コード
		if (StringUtils.isEmpty(form.getJgiNo()) && StringUtils.isEmpty(form.getInsNo())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "担当者または施設コード")));
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
	public void dpm600C00F10Validation(DpContext ctx, Dpm600C00Form form) throws ValidateException {
		executeValidation(ctx, form);
	}

	/**
	 * 登録処理時の共通Validationメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	private void executeValidation(DpContext ctx, Dpm600C00Form form) throws ValidateException {
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
	 * （ワ）検索処理時の入力パラメータチェック
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dpm600C00F25Validation(DpContext ctx, Dpm600C00Form form) throws ValidateException {

		String insNo = form.getInsNo();
		String jgiNo = form.getJgiNo();

		// 従業員番号、施設コードのいずれかは必須
		if (StringUtils.isEmpty(insNo) && StringUtils.isEmpty(jgiNo)) {
			final String errMsg = "施設コードまたは従業員番号がnull。insNo=" + insNo + ",jgiNo=" + jgiNo;
			throw new ValidateException(new Conveyance(new MessageKey("DPC1006E", errMsg)));
		}

		// 施設コードが入力されていない場合
		else if (StringUtils.isEmpty(insNo)) {
			// 活動区分が[一般]
			if (ActivityType.getInstance(form.getActivityType()).equals(ActivityType.IPPAN)) {

				// 市区町村が選択されていない
				if (StringUtils.isEmpty(form.getAddrCodePref()) || StringUtils.isEmpty(form.getAddrCodeCity())) {
					throw new ValidateException(new Conveyance(new MessageKey("DPC1006E", "市区町村")));
				}
			}
		}

		// [必須] 品目コード
		if (StringUtils.isEmpty(form.getProdCode())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "品目コード")));
		}
	}

	/**
	 * （ワ）登録処理時の入力パラメータチェック
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dpm600C00F20Validation(DpContext ctx, Dpm600C00Form form) throws ValidateException {
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
	private void setGridHeader(Dpm600C00Form form) {

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

// mod Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		//期別計画
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

		//各月
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
	 * 編集可能リストを設定
	 * @param form Dpm500Form
	 */
	private void setEnableEdit(Dpm600C00Form form) {
		Cal today = dpmCommonService.searchToday();
		// カレンダー年
		String calYear = today.getCalYear();
		// カレンダー月
		String calMonth = today.getCalMonth();
		// 今月以降
		boolean isThisMonth = false;

		// 編集可能リスト
		List<Boolean> enableEdit = new ArrayList<Boolean>();

		for (String month : form.getMonthList()) {
			if (!isThisMonth) {
				if (calYear.equals(form.getYear()) && calMonth.equals(month)) {
					isThisMonth = true;
					enableEdit.add(Boolean.TRUE);
				} else {
					enableEdit.add(Boolean.FALSE);
				}
			} else {
				enableEdit.add(Boolean.TRUE);
			}
		}
		form.setEnableEditList(enableEdit);
	}


	/**
	 * 対象区分リストを設定
	 * @param form Dpm600Form
	 */
	private void setInsList(Dpm600C00Form form) {

		List<CodeAndValue> list = new ArrayList<CodeAndValue>();
		// 対象区分リストを設定
		List<DpmCCdMst> insTypeList = (dpmCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.IT.getDbValue()));
		list.add(new CodeAndValue("0", "UHP"));
		for (DpmCCdMst mst :insTypeList) {
			CodeAndValue cad = new CodeAndValue(mst.getDataCd(), mst.getDataName());
			list.add(cad);
		}
		form.setInsTypeList(list);

		// 対象区分リスト（雑含む）を設定
		list = new ArrayList<CodeAndValue>();
		List<DpmCCdMst> insTypeZList = (dpmCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.ITZ.getDbValue()));
		list.add(new CodeAndValue("0", "UHP"));
		for (DpmCCdMst mst :insTypeZList) {
			CodeAndValue cad = new CodeAndValue(mst.getDataCd(), mst.getDataName());
			list.add(cad);
		}
		form.setInsTypeZList(list);
	}
	/**
	 * カテゴリリストの初期設定
	 * @param form Dpm600C00Form
	 */
	private void initCategoryList(Dpm600C00Form form) {
		// 汎用マスタのカテゴリリスト
		List<DpmCCdMst> categoryList = (dpmCodeMasterSearchService.searchCategorylist(null, PLAN_ID));
		// カテゴリリストに表示するリスト
		List<String> targetCategoryAry = new ArrayList<String>();
		// 計画対象カテゴリ領域の設定
		List<String> planLvCtgList = new ArrayList<String>();
		planLvCtgList = dpmPlannedCtgSearchService.searchCategoryByPlanLevel(ProdPlanLevel.INS);

		String sosCd = null;
		if(StringUtils.isNotBlank(form.getSosCd3())) {
			// 組織コード：営業所
			sosCd = form.getSosCd3();
		} else if (StringUtils.isNotBlank(form.getSosCd2())) {
			// 組織コード：支店
			sosCd = form.getSosCd2();
		}
		// 組織と紐づくカテゴリリスト
		List<SosCtg> ctgList = dpmSosCtgSearchService.searchDpmSosCtgList(sosCd, SCREEN_ID);

		for(SosCtg ctg : ctgList) {
			targetCategoryAry.add(ctg.getCategory());
		}

		List<CodeAndValue> list = new ArrayList<CodeAndValue>();

		// ログインユーザが支店スタッフ以上であるか
		form.setJrnsUser(Boolean.FALSE);
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {
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

		boolean indexFlg = true;
		// 対象の立案レベルをもつ計画対象カテゴリ領域のカテゴリのみ設定
		for (DpmCCdMst mst : categoryList) {
			// JRNSの場合、支店スタッフ以上である＆営業所・NSG・ワクチン・CVのいずれかがリストに存在する場合に、リストに追加する
			if (jrnsCode.equals(mst.getDataCd()) && form.getJrnsUser() && isExistJrnsCtg) {
				CodeAndValue cad = new CodeAndValue(mst.getDataCd(), mst.getDataName());
				list.add(cad);
			}
			else if (!jrnsCode.equals(mst.getDataCd())) {
				if (targetCategoryAry.contains(mst.getDataCd()) && planLvCtgList.contains(mst.getDataCd())) {
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
		}
		form.setProdCategoryList(list);
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
