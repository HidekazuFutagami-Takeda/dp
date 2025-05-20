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
import jp.co.takeda.bean.Constants.TemplateInfo;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.InsPlanForVacHeaderDto;
import jp.co.takeda.dto.InsPlanForVacResultDetailTotalDto;
import jp.co.takeda.dto.InsPlanForVacResultDto;
import jp.co.takeda.dto.InsPlanForVacScDto;
import jp.co.takeda.dto.InsPlanForVacUpdateDto;
import jp.co.takeda.dto.InsPlanResultDto;
import jp.co.takeda.dto.InsPlanScDto;
import jp.co.takeda.dto.InsPlanUpdateDto;
import jp.co.takeda.dto.InsProdPlanResultHeaderDto;
import jp.co.takeda.dto.ManagePlanForVacUpdateResultDto;
import jp.co.takeda.dto.ManagePlanUpdateResultDto;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.model.view.SosCtg;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpmCodeMasterSearchService;
import jp.co.takeda.service.DpmInsPlanForVacSearchService;
import jp.co.takeda.service.DpmInsPlanForVacService;
import jp.co.takeda.service.DpmInsPlanSearchService;
import jp.co.takeda.service.DpmInsPlanService;
import jp.co.takeda.service.DpmPlannedCtgSearchService;
import jp.co.takeda.service.DpmReportService;
import jp.co.takeda.service.DpmSosCtgSearchService;
import jp.co.takeda.service.DpmSosJgiSearchService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.in.dpm.Dpm200C00Form;

/**
 * Dpm200C00((医)施設別計画編集画面)のアクションクラス
 *
 * @author stakeuchi
 */
@Controller("Dpm200C00Action")
public class Dpm200C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dpm200C00Action.class);

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPM200C00";

	/**
	 * 計画（1：期別、2：月別）
	 */
	private static final String PLAN_ID = "1";

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 施設別計画 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmInsPlanSearchService")
	protected DpmInsPlanSearchService dpmInsPlanSearchService;

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
	public Result dpm200C00F00(DpContext ctx, Dpm200C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm200C00F00");
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
	public Result dpm200C00F01(DpContext ctx, Dpm200C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm200C00F01");
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
				}else if (user.isSosLvl(SCREEN_ID, SosLvl.MR)) {
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
	public Result dpm200C00F05(DpContext ctx, Dpm200C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm200C00F05");
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
		// 対象区分を設定
		setInsList(form);
		searchInsPlanList(form);
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
	public Result dpm200C00F25(DpContext ctx, Dpm200C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm200C00F25");
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
	public Result dpm200C00F10Execute(DpContext ctx, Dpm200C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm200C00F10Execute");
			LOG.debug("JgiNo:" + form.getJgiNo());
			LOG.debug("InsNo:" + form.getInsNo());
			LOG.debug("InsType:" + form.getInsType());
			LOG.debug("ProdCategory:" + form.getProdCategory());
			LOG.debug("ProdCode:" + form.getProdCode());
			LOG.debug("PlanData:" + form.getPlanData());
		}

		try {
			// 更新DTO
			List<InsPlanUpdateDto> updateDtoList = form.convertInsPlanUpdateDto();

			// 更新実行
			ManagePlanUpdateResultDto resultDto = new ManagePlanUpdateResultDto(0, 0, 0);
			if (!updateDtoList.isEmpty()) {
				resultDto = dpmInsPlanService.updateInsPlan(SCREEN_ID, updateDtoList);
			}

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(resultDto.getUpdateTotalCount())));

		} finally {
			// 再検索実行
			searchInsPlanList(form);
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
	public Result dpm200C00F20Execute(DpContext ctx, Dpm200C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm200C00F20Execute");
			LOG.debug("JgiNo:" + form.getJgiNo());
			LOG.debug("InsNo:" + form.getInsNo());
			LOG.debug("InsType:" + form.getInsType());
			LOG.debug("ProdCategory:" + form.getProdCategory());
			LOG.debug("ProdCode:" + form.getProdCode());
			LOG.debug("PlanData:" + form.getPlanData());
		}

		try {
			// 更新DTO
			List<InsPlanForVacUpdateDto> updateDtoList = form.convertInsPlanVacUpdateDto();

			// 更新実行
			ManagePlanForVacUpdateResultDto resultDto = new ManagePlanForVacUpdateResultDto(0);
			if (!updateDtoList.isEmpty()) {
				resultDto = dpmInsPlanForVacService.updateInsPlan(SCREEN_ID, updateDtoList);
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
	private void searchInsPlanList(Dpm200C00Form form) throws Exception {

		// 実行した検索条件を画面に設定
		InsPlanScDto scDto = form.convertInsPlanScDto();
		form.setViewField();
		try {
			InsPlanResultDto resultDto = dpmInsPlanSearchService.searchInsPlan(scDto, true);
			super.getRequestBox().put(Dpm200C00Form.DPM200C00_DATA_R_SEARCH_RESULT, resultDto);
		} catch (DataNotFoundException e) {
			InsPlanResultDto resultDto = dpmInsPlanSearchService.searchInsPlan(scDto, false);
			super.getRequestBox().put(Dpm200C00Form.DPM200C00_DATA_R_SEARCH_RESULT, resultDto);
			throw e;

		} finally {

			// 施設コードが入力された場合は、ヘッダー情報取得
			if (StringUtils.isNotEmpty(scDto.getInsNo())) {

				// ヘッダー情報取得
				InsProdPlanResultHeaderDto headerDto = dpmInsPlanSearchService.searchInsPlanHeaderOyako(scDto.getInsNo(),scDto.getProdCode(),scDto.getProdCategory());

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
	private void searchInsPlanForVacList(Dpm200C00Form form) throws Exception {

		// 実行した検索条件を画面に設定
		InsPlanForVacScDto scDto = form.convertInsPlanVacScDto();
		form.setViewFieldVac();
		InsPlanForVacResultDto resultDto = null;
		try {
			resultDto = dpmInsPlanForVacSearchService.searchInsPlan(scDto);
			super.getRequestBox().put(Dpm200C00Form.DPM200C01_DATA_R_SEARCH_RESULT, resultDto);
		} finally {

			// 集計行取得
			InsPlanForVacResultDetailTotalDto totalDto = dpmInsPlanForVacSearchService.searchInsPlanTotal(scDto, resultDto);
			super.getRequestBox().put(Dpm200C00Form.DPM200C01_DATA_R_SEARCH_RESULT_TOTAL, totalDto);

			// 施設コードが入力された場合は、ヘッダー情報取得
			if (StringUtils.isNotEmpty(scDto.getInsNo())) {

				// ヘッダー情報取得
				InsPlanForVacHeaderDto headerDto = dpmInsPlanForVacSearchService.searchInsPlanHeader(scDto.getInsNo());

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
	private void setUserInfo(Dpm200C00Form form) {

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
				}
			}
		}
	}
	 /**
     * (医)施設別計画編集画面
     * 出力する処理時に呼ばれるアクションメソッド<br>
     *
     * @param ctx Context
     * @param form ActionForm
     * @return 処理結果
     * @throws Exception 例外
     */
    @ActionMethod(isDownloadable = true)
    @RequestType
    @Permission(authType = AuthType.OUTPUT)
    public Result dpm200C01F14Output(DpContext ctx, Dpm200C00Form form) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("dpm200C01F14Output");
        }
            // 帳票出力準備
        	InsPlanScDto insPlanScDto = form.convertInsPlanScDto();
            String templateRootPath = getRealPath(TemplateInfo.EXCEL_TEMPLATE_PATH.getValue());

            searchInsPlanList(form);

            // 帳票出力実行
            ExportResult exportResult = dpmReportService.outputInsPlanList(templateRootPath, insPlanScDto);
            form.setExportResult(exportResult);
            form.setDownLoadFileName(exportResult.getName());

        return ActionResult.SUCCESS;
    }
	 /**
     * (ワ)施設別計画編集画面
     * 出力する処理時に呼ばれるアクションメソッド<br>
     *
     * @param ctx Context
     * @param form ActionForm
     * @return 処理結果
     * @throws Exception 例外
     */
    @ActionMethod(isDownloadable = true)
    @RequestType
    @Permission(authType = AuthType.OUTPUT)
    public Result dpm200C02F14Output(DpContext ctx, Dpm200C00Form form) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("dpm200C02F14Output");
        }
            // 帳票出力準備
        	InsPlanForVacScDto insPlanForVacScDto = form.convertInsPlanVacScDto();
            String templateRootPath = getRealPath(TemplateInfo.EXCEL_TEMPLATE_PATH.getValue());

            searchInsPlanForVacList(form);

            // 帳票出力実行
            ExportResult exportResult = dpmReportService.outputInsPlanForVacList(templateRootPath, insPlanForVacScDto);
            form.setExportResult(exportResult);
            form.setDownLoadFileName(exportResult.getName());

        return ActionResult.SUCCESS;
    }

    /**
     * カテゴリリストの初期設定
     * @param form Dpm200C00Form
     */
    private void initCategoryList(Dpm200C00Form form) {
		// 汎用マスタのカテゴリリスト
		List<DpmCCdMst> categoryList = (dpmCodeMasterSearchService.searchCategorylist(null,PLAN_ID));

		// カテゴリリストに表示するリスト
		List<String> targetCategoryAry = new ArrayList<String>();
		// 計画対象カテゴリ領域の設定
		List<String> planLvCtgList = new ArrayList<String>();
		// 計画立案レベル-施設
		planLvCtgList = dpmPlannedCtgSearchService.searchCategoryByPlanLevel(ProdPlanLevel.INS);

		String sosCd = null;
		if (StringUtils.isNotBlank(form.getSosCd3())) {
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

		boolean indexFlg = true;
		// 対象の立案レベルをもつ計画対象カテゴリ領域のカテゴリのみ設定
		for (DpmCCdMst mst :categoryList) {
			if (targetCategoryAry.contains(mst.getDataCd()) && planLvCtgList.contains(mst.getDataCd())) {
				CodeAndValue cad = new CodeAndValue(mst.getDataCd(), mst.getDataName());
				list.add(cad);
				if (form.getProdCategory() == null && indexFlg) {
					form.setProdCategory(cad.getCode());
					indexFlg = false;
				}
			}
		}
		form.setPlanId(PLAN_ID);
		form.setProdCategoryList(list);
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
	public void dpm200C00F05Validation(DpContext ctx, Dpm200C00Form form) throws ValidateException {
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
	public void dpm200C00F10Validation(DpContext ctx, Dpm200C00Form form) throws ValidateException {
		executeValidation(ctx, form);
	}

	/**
	 * 登録処理時の共通Validationメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	private void executeValidation(DpContext ctx, Dpm200C00Form form) throws ValidateException {
		// 更新パラーメータチェック
		String[] rowIdList = form.getRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		for (int i = 0; i < rowIdList.length; i++) {
			// パラーメータのサイズチェック
			String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);
			if (rowId.length != 7) {
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
			// 更新前計画値
			if (StringUtils.isNotEmpty(rowId[5]) && !ValidationUtil.isLong(rowId[5])) {
				final String errMsg = "受信パラメータ(計画値(更新前))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新後計画値
			if (StringUtils.isNotEmpty(rowId[6]) && !ValidationUtil.isLong(rowId[6])) {
				final String errMsg = "受信パラメータ(計画値(更新後))が不正：";
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
	public void dpm200C00F25Validation(DpContext ctx, Dpm200C00Form form) throws ValidateException {

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
	public void dpm200C00F20Validation(DpContext ctx, Dpm200C00Form form) throws ValidateException {
		// 更新パラーメータチェック
		String[] rowIdList = form.getRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		for (int i = 0; i < rowIdList.length; i++) {
			// パラーメータのサイズチェック
			String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);
			if (rowId.length != 6) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 施設コード
			if (StringUtils.isEmpty(rowId[0])) {
				final String errMsg = "受信パラメータ(施設コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 品目コード
			if (StringUtils.isEmpty(rowId[1])) {
				final String errMsg = "受信パラメータ(品目コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// シーケンスキー
			if (StringUtils.isNotEmpty(rowId[2]) && !ValidationUtil.isLong(rowId[2])) {
				final String errMsg = "受信パラメータ(シーケンスキー)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 最終更新日
			if (StringUtils.isNotEmpty(rowId[3]) && !ValidationUtil.isLong(rowId[3])) {
				final String errMsg = "受信パラメータ(最終更新日)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新前計画値
			if (StringUtils.isNotEmpty(rowId[4]) && !ValidationUtil.isLong(rowId[4])) {
				final String errMsg = "受信パラメータ(計画値(更新前))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新後計画値
			if (StringUtils.isNotEmpty(rowId[5]) && !ValidationUtil.isLong(rowId[5])) {
				final String errMsg = "受信パラメータ(計画値(更新後))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
		}
	}


	/**
	 * 対象区分リストを設定
	 * @param form Dpm200Form
	 */
	private void setInsList(Dpm200C00Form form) {

		List<CodeAndValue> list = new ArrayList<CodeAndValue>();
		// 対象区分リストを設定
		List<DpmCCdMst> insTypeList = (dpmCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.IT.getDbValue()));
		for (DpmCCdMst mst :insTypeList) {
			CodeAndValue cad = new CodeAndValue(mst.getDataCd(), mst.getDataName());
			list.add(cad);
		}
		form.setInsTypeList(list);

		// 対象区分リスト（雑含む）を設定
		list = new ArrayList<CodeAndValue>();
		List<DpmCCdMst> insTypeZList = (dpmCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.ITZ.getDbValue()));
		for (DpmCCdMst mst :insTypeZList) {
			CodeAndValue cad = new CodeAndValue(mst.getDataCd(), mst.getDataName());
			list.add(cad);
		}
		form.setInsTypeZList(list);
	}

}
