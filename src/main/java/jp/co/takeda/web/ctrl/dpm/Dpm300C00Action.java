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
import jp.co.takeda.bean.Constants;
import jp.co.takeda.bean.Constants.TemplateInfo;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.ManagePlanForVacUpdateResultDto;
import jp.co.takeda.dto.ManagePlanUpdateResultDto;
import jp.co.takeda.dto.ManageWsPlanDto;
import jp.co.takeda.dto.ManageWsPlanEntryDto;
import jp.co.takeda.dto.ManageWsPlanForIyakuHeaderDto;
import jp.co.takeda.dto.ManageWsPlanForVacDto;
import jp.co.takeda.dto.ManageWsPlanForVacEntryDto;
import jp.co.takeda.dto.ManageWsPlanForVacHeaderDto;
import jp.co.takeda.dto.ManageWsPlanForVacScDto;
import jp.co.takeda.dto.ManageWsPlanScDto;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.JknGrp;
import jp.co.takeda.model.SosMstCtg;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpmCodeMasterSearchService;
import jp.co.takeda.service.DpmPlannedCtgSearchService;
import jp.co.takeda.service.DpmReportService;
import jp.co.takeda.service.DpmSosGrpSearchService;
import jp.co.takeda.service.DpmTmsTytenPlanForVacSearchService;
import jp.co.takeda.service.DpmTmsTytenPlanForVacService;
import jp.co.takeda.service.DpmTmsTytenPlanSearchService;
import jp.co.takeda.service.DpmTmsTytenPlanService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.in.dpm.Dpm300C00Form;

/**
 * Dpm300C00((医)特約店別計画編集画面)のアクションクラス
 *
 * @author siwamoto
 */
@Controller("Dpm300C00Action")
public class Dpm300C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dpm300C00Action.class);

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPM300C00";

	/**
	 * 計画（1：期別、2：月別）
	 */
	private static final String PLAN_ID = "1";

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 検索系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmTmsTytenPlanSearchService")
	protected DpmTmsTytenPlanSearchService dpmTmsTytenPlanSearchService;

	/**
	 * 更新系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmTmsTytenPlanService")
	protected DpmTmsTytenPlanService dpmTmsTytenPlanService;

	/**
	 * （ワ）検索系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmTmsTytenPlanForVacSearchService")
	protected DpmTmsTytenPlanForVacSearchService dpmTmsTytenPlanForVacSearchService;

	/**
	 * （ワ）更新系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmTmsTytenPlanForVacService")
	protected DpmTmsTytenPlanForVacService dpmTmsTytenPlanForVacService;

    /**
     * カテゴリ検索サービス
     */
    @Autowired(required = true)
    @Qualifier("dpmCodeMasterSearchService")
    DpmCodeMasterSearchService dpmCodeMasterSearchService;

	/**
	 * ダウンロード系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmReportService")
	protected DpmReportService dpmReportService;

    /**
     * 組織グループ検索サービス
     */
    @Autowired(required = true)
    @Qualifier("dpmSosGrpSearchService")
    DpmSosGrpSearchService dpmSosGrpSearchService;

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
	public Result dpm300C00F00(DpContext ctx, Dpm300C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm300C00F00");
		}

		// 特約店検索モードをクリア
		form.setTmsSelectMode(null);

		// 初期化処理
		form.formInit();

		// カテゴリリストの初期設定
		initCategoryList(form);

        setTitle(form);
		return ActionResult.SUCCESS;
	}

	/**
	 * 表示するボタンクリック時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.REFER)
	public Result dpm300C00F05(DpContext ctx, Dpm300C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm300C00F05");
		}

		// カテゴリリストの初期設定
		initCategoryList(form);
		// 検索実行
		form.setTranField();
		searchResultList(form);
		return ActionResult.SUCCESS;
	}

	/**
	 * （ワ）表示するボタンクリック時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.REFER)
	public Result dpm300C00F25(DpContext ctx, Dpm300C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm300C00F25");
		}
		// 検索実行
		form.setTranField();
		searchResultVacList(form);
		return ActionResult.SUCCESS;
	}


	/**
	 * 登録ボタンクリック時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dpm300C00F10Excecute(DpContext ctx, Dpm300C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm300C00F10Excecute");
		}
		try {
			// 更新実行
			List<ManageWsPlanEntryDto> entryDto = form.convertEntryDto();
			ManagePlanUpdateResultDto resultDto = new ManagePlanUpdateResultDto(0, 0, 0);
			if (!entryDto.isEmpty()) {
				resultDto = dpmTmsTytenPlanService.updateTmsTytenPlan(SCREEN_ID, entryDto);
			}
			// 更新完了メッセージの追加
			final int updateSizeUH = resultDto.getUpdateCountUh();
			final int updateSizeP = resultDto.getUpdateCountP();
			final int updateSizeZ = resultDto.getUpdateCountZ();
			addMessage(ctx, new MessageKey("DPC0007I", form.getTitleUH(), String.valueOf(updateSizeUH), form.getTitleP(), String.valueOf(updateSizeP), form.getTitleZ(), String.valueOf(updateSizeZ)));


		} finally {
			// 再検索実行
			searchResultList(form);
		}
		return ActionResult.SUCCESS;
	}

	/**
	 * （ワ）登録ボタンクリック時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dpm300C00F20Excecute(DpContext ctx, Dpm300C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm300C00F20Excecute");
		}
		try {
			// 更新実行
			List<ManageWsPlanForVacEntryDto> entryDto = form.convertEntryVacDto();
			ManagePlanForVacUpdateResultDto resultDto = new ManagePlanForVacUpdateResultDto(0);
			if (!entryDto.isEmpty()) {
				resultDto = dpmTmsTytenPlanForVacService.updateTmsTytenPlan(SCREEN_ID, entryDto);
			}
			// 更新完了メッセージの追加
			final int updateSize = resultDto.getUpdateTotalCount();
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(updateSize)));

		} finally {
			// 再検索実行
			searchResultVacList(form);
		}
		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理の実行
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchResultList(Dpm300C00Form form) throws Exception {
		// 実行した検索条件を画面に設定
		ManageWsPlanScDto scDto = form.convertScDto();
		form.setViewField();
		try {
			ManageWsPlanDto ServiceResult = dpmTmsTytenPlanSearchService.searchList(scDto);
			super.getRequestBox().put(Dpm300C00Form.DPM300C00_DATA_R, ServiceResult);
		} finally {
			ManageWsPlanForIyakuHeaderDto headerDto = dpmTmsTytenPlanSearchService.searchHeader(scDto);
			super.getRequestBox().put(Dpm300C00Form.DPM300C00_INPUT_DATA_R, headerDto);
			form.setTmsTytenName(headerDto.getTmsTytenName());
			setTitle(form);
		}
	}

	/**
	 * （ワ）検索処理の実行
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchResultVacList(Dpm300C00Form form) throws Exception {
		// 実行した検索条件を画面に設定
		ManageWsPlanForVacScDto scDto = form.convertScVacDto();
		form.setViewField();
		try {
			ManageWsPlanForVacDto ServiceResult = dpmTmsTytenPlanForVacSearchService.searchList(scDto);
			super.getRequestBox().put(Dpm300C00Form.DPM300C01_DATA_R, ServiceResult);
		} finally {
			ManageWsPlanForVacHeaderDto headerDto = dpmTmsTytenPlanForVacSearchService.searchHeader(scDto);
			super.getRequestBox().put(Dpm300C00Form.DPM300C01_INPUT_DATA_R, headerDto);
			form.setTmsTytenName(headerDto.getTmsTytenName());
		}
	}


	/**
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
	public Result dpm300C00F14Output(DpContext ctx, Dpm300C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm300C00F14Output");
		}

		// 帳票出力準備
		ManageWsPlanScDto scDto = form.convertScDto();
		String templateRootPath = getRealPath(TemplateInfo.EXCEL_TEMPLATE_PATH.getValue());

		searchResultList(form);

		dpmTmsTytenPlanSearchService.searchHeader(scDto);

		// 帳票出力実行
		ExportResult exportResult = dpmReportService.outputManageWsPlanList(templateRootPath, scDto, form.isRyutsu(), form.getTitleUH(), form.getTitleP(), form.getTitleZ());
		form.setExportResult(exportResult);
		form.setDownLoadFileName(exportResult.getName());

		return ActionResult.SUCCESS;
	}

	/**
	 * （ワ）出力する処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(isDownloadable = true)
	@RequestType
	@Permission(authType = AuthType.OUTPUT)
	public Result dpm300C00F24Output(DpContext ctx, Dpm300C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm300C00F24Output");
		}

		// 帳票出力準備
		ManageWsPlanForVacScDto scDto = form.convertScVacDto();
		String templateRootPath = getRealPath(TemplateInfo.EXCEL_TEMPLATE_PATH.getValue());

		searchResultVacList(form);

		dpmTmsTytenPlanForVacSearchService.searchHeader(scDto);

		// 帳票出力実行
		ExportResult exportResult = dpmReportService.outputManageWsVacPlanList(templateRootPath, scDto, form.isRyutsu());
		form.setExportResult(exportResult);
		form.setDownLoadFileName(exportResult.getName());

		return ActionResult.SUCCESS;
	}

	/**
	 * カテゴリリストの初期設定
	 * @param form Dpm300C00Form
	 */
	private void initCategoryList(Dpm300C00Form form) {
		// 汎用マスタのカテゴリリスト
		List<DpmCCdMst> categoryList = (dpmCodeMasterSearchService.searchCategorylist(null,PLAN_ID));

         List<CodeAndValue> list = new ArrayList<CodeAndValue>();

        boolean indexFlg = true;

		// 計画対象カテゴリ領域の設定
		List<String> planLvCtgList = new ArrayList<String>();
		// 計画立案レベル-特約店
		planLvCtgList = dpmPlannedCtgSearchService.searchCategoryByPlanLevel(ProdPlanLevel.WS);

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
                // 流通政策部であるかを確認
                checkRyutsu(form, user);
            }
        }
        // 上記で取得したコードのカテゴリのみ、カテゴリリストにセットする
        for (DpmCCdMst mst : categoryList) {
            if (sosCategoryList.contains(mst.getDataCd()) && planLvCtgList.contains(mst.getDataCd())) {
                CodeAndValue cad = new CodeAndValue(mst.getDataCd(), mst.getDataName());
                list.add(cad);
                if (form.getProdCategory() == null && indexFlg) {
                    form.setProdCategory(cad.getCode());
                    indexFlg = false;
                }
            }
        }
 		//add Start 2025/2/19  H.Futagami 202410要望:カテゴリに全て（医薬）追加
 		list.add(new CodeAndValue(Constants.CATEGORY_IYAKU_ALL, Constants.CATEGORY_IYAKU_ALL_NM));
 		//add End 2025/2/19  H.Futagami 202410要望:カテゴリに全て（医薬）追加
        form.setProdCategoryList(list);
	}

	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * （医）（ワ）検索処理時の入力パラメータチェック
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dpm300C00F05Validation(DpContext ctx, Dpm300C00Form form) throws ValidateException {
		// [必須] 特約店コード
		if (StringUtils.isEmpty(form.getTmsTytenCdPart())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "特約店コード")));
		}
		// [必須] 品目カテゴリ
		if (StringUtils.isEmpty(form.getProdCategory())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "品目カテゴリ")));
		}
		// [必須] 品目コード
		if (StringUtils.isEmpty(form.getProdCode())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "品目コード")));
		}
		// [必須] 計画
		if (StringUtils.isEmpty(form.getPlanData())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "計画")));
		}
	}

	/**
	 * 更新時のValidationメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dpm300C00F10Validation(DpContext ctx, Dpm300C00Form form) throws ValidateException {
		// 更新パラーメータチェック
		String[] rowIdList = form.getRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		// UHパラメータチェック
		for (int i = 0; i < rowIdList.length; i++) {
			// パラーメータのサイズチェック
			String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);
			if (rowId.length != 14) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 品目固定コード
			if (StringUtils.isNotEmpty(rowId[0]) && !ValidationUtil.isInteger(rowId[0])) {
				final String errMsg = "受信パラメータ(品目固定コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 特約店コード
			if (StringUtils.isEmpty(rowId[1])) {
				final String errMsg = "受信パラメータ(特約店コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [UH]シーケンスキー
			if (StringUtils.isNotEmpty(rowId[2]) && !ValidationUtil.isLong(rowId[2])) {
				final String errMsg = "受信パラメータ([UH]シーケンスキー)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [UH]最終更新日
			if (StringUtils.isNotEmpty(rowId[3]) && !ValidationUtil.isLong(rowId[3])) {
				final String errMsg = "受信パラメータ([UH]最終更新日)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [UH]更新前計画値
			if (StringUtils.isNotEmpty(rowId[4]) && !ValidationUtil.isLong(rowId[4])) {
				final String errMsg = "受信パラメータ([UH]計画値(更新前))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [UH]更新後計画値
			if (StringUtils.isNotEmpty(rowId[11]) && !ValidationUtil.isLong(rowId[11])) {
				final String errMsg = "受信パラメータ([UH]計画値(更新後))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [P]シーケンスキー
			if (StringUtils.isNotEmpty(rowId[5]) && !ValidationUtil.isLong(rowId[5])) {
				final String errMsg = "受信パラメータ([P]シーケンスキー)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [P]最終更新日
			if (StringUtils.isNotEmpty(rowId[6]) && !ValidationUtil.isLong(rowId[6])) {
				final String errMsg = "受信パラメータ([P]最終更新日)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [P]更新前計画値
			if (StringUtils.isNotEmpty(rowId[7]) && !ValidationUtil.isLong(rowId[7])) {
				final String errMsg = "受信パラメータ([P]計画値(更新前))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [P]更新後計画値
			if (StringUtils.isNotEmpty(rowId[12]) && !ValidationUtil.isLong(rowId[12])) {
				final String errMsg = "受信パラメータ([P]計画値(更新後))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [Z]シーケンスキー
			if (StringUtils.isNotEmpty(rowId[8]) && !ValidationUtil.isLong(rowId[8])) {
				final String errMsg = "受信パラメータ([Z]シーケンスキー)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [Z]最終更新日
			if (StringUtils.isNotEmpty(rowId[9]) && !ValidationUtil.isLong(rowId[9])) {
				final String errMsg = "受信パラメータ([Z]最終更新日)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [Z]更新前計画値
			if (StringUtils.isNotEmpty(rowId[10]) && !ValidationUtil.isLong(rowId[10])) {
				final String errMsg = "受信パラメータ([Z]計画値(更新前))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [Z]更新後計画値
			if (StringUtils.isNotEmpty(rowId[13]) && !ValidationUtil.isLong(rowId[13])) {
				final String errMsg = "受信パラメータ([Z]計画値(更新後))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
		}
	}

	/**
	 * （ワ）更新時のValidationメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dpm300C00F20Validation(DpContext ctx, Dpm300C00Form form) throws ValidateException {
		// 更新パラーメータチェック
		String[] rowIdList = form.getRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		// パラメータチェック
		for (int i = 0; i < rowIdList.length; i++) {
			// パラーメータのサイズチェック
			String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);
			if (rowId.length != 6) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 品目固定コード
			if (StringUtils.isNotEmpty(rowId[0]) && !ValidationUtil.isInteger(rowId[0])) {
				final String errMsg = "受信パラメータ(品目固定コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 特約店コード
			if (StringUtils.isEmpty(rowId[1])) {
				final String errMsg = "受信パラメータ(特約店コード)が不正：";
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
	 * ユーザの組織が流通政策部であるかを確認する
	 * @param form ActionForm
	 * @param user ログインユーザ
	 */
	private void checkRyutsu(Dpm300C00Form form, DpUser user) {
        // 流通政策部であるコード一覧を取得する
        List<DpmCCdMst> ryutsuList = dpmCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.RYUTSU.getDbValue());
        List<String> ryutsuCodeList = new ArrayList<String>();

        for(DpmCCdMst ryutsu : ryutsuList) {
            ryutsuCodeList.add(ryutsu.getDataCd());
        }

        for (JknGrp grp : user.getJknGrpList()) {
            if (ryutsuCodeList.contains(grp.getJokenSetCd())) {
                form.setRyutsu(true);
                break;
            }
        }

	}

	/**
	 * formにUH、P、Zのタイトルを設定する
	 * @param form
	 */
	private void setTitle(Dpm300C00Form form) {
		List<DpmCCdMst> mstList = new ArrayList<DpmCCdMst>();
		if (StringUtils.isNotBlank(form.getProdCategory())) {
			mstList = dpmCodeMasterSearchService.searchInsTypeTitle(form.getProdCategory());
		} else {
			// 未検索時は雑なし
			mstList = dpmCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.IT.getDbValue());
		}
		for(DpmCCdMst dpmCCdMst:mstList) {
			if (dpmCCdMst.getDataCd().equals(InsType.UH.getDbValue())) {
				form.setTitleUH(dpmCCdMst.getDataName());
			} else if (dpmCCdMst.getDataCd().equals(InsType.P.getDbValue())) {
				form.setTitleP(dpmCCdMst.getDataName());
			} else if (dpmCCdMst.getDataCd().equals(InsType.ZATU.getDbValue())) {
				form.setTitleZ(dpmCCdMst.getDataName());
			}
		}
	}

}
