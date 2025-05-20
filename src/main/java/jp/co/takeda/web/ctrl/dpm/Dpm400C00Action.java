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
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.bean.Constants.TemplateInfo;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.ManageInsWsPlanDto;
import jp.co.takeda.dto.ManageInsWsPlanEntryDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacEntryDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacHeaderDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacResultDetailTotalDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacScDto;
import jp.co.takeda.dto.ManageInsWsPlanHeaderDto;
import jp.co.takeda.dto.ManageInsWsPlanScDto;
import jp.co.takeda.dto.ManagePlanForVacUpdateResultDto;
import jp.co.takeda.dto.ManagePlanUpdateResultDto;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.JknGrp;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.model.view.SosCtg;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpmCodeMasterSearchService;
import jp.co.takeda.service.DpmInsWsPlanForVacSearchService;
import jp.co.takeda.service.DpmInsWsPlanForVacService;
import jp.co.takeda.service.DpmInsWsPlanSearchService;
import jp.co.takeda.service.DpmInsWsPlanService;
import jp.co.takeda.service.DpmPlannedCtgSearchService;
import jp.co.takeda.service.DpmReportService;
import jp.co.takeda.service.DpmSosCtgSearchService;
import jp.co.takeda.service.DpmSosGrpSearchService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.in.dpm.Dpm400C00Form;

/**
 * Dpm400C00((医)施設特約店別計画編集画面)のアクションクラス
 *
 * @author siwamoto
 */
@Controller("Dpm400C00Action")
public class Dpm400C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dpm400C00Action.class);

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPM400C00";

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
	@Qualifier("dpmInsWsPlanSearchService")
	protected DpmInsWsPlanSearchService dpmInsWsPlanSearchService;

	/**
	 * （ワ）検索系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmInsWsPlanForVacSearchService")
	protected DpmInsWsPlanForVacSearchService dpmInsWsPlanForVacSearchService;


	/**
	 * 更新系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmInsWsPlanService")
	protected DpmInsWsPlanService dpmInsWsPlanService;

	/**
	 * （ワ）更新系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmInsWsPlanForVacService")
	protected DpmInsWsPlanForVacService dpmInsWsPlanForVacService;

	/**
	 * ダウンロード系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmReportService")
	protected DpmReportService dpmReportService;


    /**
     * カテゴリ検索サービス
     */
    @Autowired(required = true)
    @Qualifier("dpmCodeMasterSearchService")
    DpmCodeMasterSearchService dpmCodeMasterSearchService;

    /**
     * 組織カテゴリ検索サービス
     */
    @Autowired(required = true)
    @Qualifier("dpmSosCtgSearchService")
    protected DpmSosCtgSearchService dpmSosCtgSearchService;

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
	public Result dpm400C00F00(DpContext ctx, Dpm400C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm400C00F00");
		}

		// 特約店検索モードをクリア
		form.setTmsSelectMode(null);

		// 初期化処理
		form.formInit();

		// ログインユーザの組織情報をセット
		setUserInfo(form);

		// ログインユーザの組織情報をセット
		//全MRフラグを設定
		form.setMrFlg(false);
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
					//全MRフラグを設定
					form.setMrFlg(true);
				}

				// 汎用マスタのカテゴリリスト
				List<DpmCCdMst> categoryList = (dpmCodeMasterSearchService.searchCategorylist(null,PLAN_ID));
				// カテゴリリストに表示するリスト
				List<String> targetCategoryAry = new ArrayList<String>();
				// 計画対象カテゴリ領域の設定
				List<String> planLvCtgList = new ArrayList<String>();
				// 計画立案レベル-施設特約店
				planLvCtgList = dpmPlannedCtgSearchService.searchCategoryByPlanLevel(ProdPlanLevel.INS_WS);

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
				// 対象の立案レベルをもつ計画対象カテゴリ領域のカテゴリのみ設定
				for (DpmCCdMst mst :categoryList) {
					if (targetCategoryAry.contains(mst.getDataCd()) && planLvCtgList.contains(mst.getDataCd())) {
						CodeAndValue cad = new CodeAndValue(mst.getDataCd(), mst.getDataName());
						list.add(cad);
						if (indexFlg) {
							form.setProdCategory(cad.getCode());
							indexFlg = false;
						}
					}
				}
				form.setPlanId(PLAN_ID);
				form.setProdCategoryList(list);
			}
		}

		// 対象区分を設定
		setInsList(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * 表示ボタン押下時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.REFER)
	public Result dpm400C00F05(DpContext ctx, Dpm400C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm400C00F05");
		}
		form.setTranField();
		searchResultList(form);
		// 対象区分を設定
		setInsList(form);
		return ActionResult.SUCCESS;
	}

	/**
	 * （ワ）表示ボタン押下時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.REFER)
	public Result dpm400C00F25(DpContext ctx, Dpm400C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm400C00F25");
		}

		// 検索実行
		form.setTranFieldVac();
		searchInsWsPlanForVacList(form);

		return ActionResult.SUCCESS;
	}


	/**
	 * 登録ボタン押下時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dpm400C00F10Excecute(DpContext ctx, Dpm400C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm400C00F10Excecute");
		}
		try {
			// 更新実行
			List<ManageInsWsPlanEntryDto> entryDto = form.convertEntryDto();
			ManagePlanUpdateResultDto resultDto = new ManagePlanUpdateResultDto(0, 0, 0);
			if (!entryDto.isEmpty()) {
				resultDto = dpmInsWsPlanService.updateInsWsPlan(SCREEN_ID, entryDto);
			}
			// 更新完了メッセージの追加
			final int updateSize = resultDto.getUpdateTotalCount();
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(updateSize)));

		} finally {
			// 再検索実行
			searchResultList(form);
		}
		return ActionResult.SUCCESS;
	}

	/**
	 * （ワ）登録ボタン押下時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = EDIT)
	public Result dpm400C00F20Excecute(DpContext ctx, Dpm400C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm400C00F20Excecute");
		}
		try {
			// 更新DTO
			List<ManageInsWsPlanForVacEntryDto> updateDtoList = form.convertInsWsPlanUpdateDto();

			// 更新実行
			ManagePlanForVacUpdateResultDto resultDto = new ManagePlanForVacUpdateResultDto(0);
			if (!updateDtoList.isEmpty()) {
				resultDto = dpmInsWsPlanForVacService.updateInsWsPlan(SCREEN_ID, updateDtoList);
			}

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(resultDto.getUpdateTotalCount())));

		} finally {
			// 再検索実行
			searchInsWsPlanForVacList(form);
		}
		return ActionResult.SUCCESS;
	}

    /**
     * ダウンロードボタン押下時に呼ばれるアクションメソッド<br>
     *
     * @param ctx Context
     * @param form ActionForm
     * @return 処理結果
     * @throws Exception 例外
     */
    @ActionMethod(isDownloadable = true)
    @RequestType
    @Permission(authType = AuthType.OUTPUT)
    public Result dpm400C00F14Output(DpContext ctx, Dpm400C00Form form) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("dpm400C00F14Output");
        }
        try {
            // 帳票出力準備
        	ManageInsWsPlanScDto scDto = form.convertScDto();
            String templateRootPath = getRealPath(TemplateInfo.EXCEL_TEMPLATE_PATH.getValue());

            searchResultList(form);

            // 帳票出力実行
            ExportResult exportResult = dpmReportService.outputInsWsPlanList(templateRootPath, scDto, form.isRyutsu());
            form.setExportResult(exportResult);
            form.setDownLoadFileName(exportResult.getName());
        } finally {

        }
        return ActionResult.SUCCESS;
    }


    /**
     * （ワ）ダウンロードボタン押下時に呼ばれるアクションメソッド<br>
     *
     * @param ctx Context
     * @param form ActionForm
     * @return 処理結果
     * @throws Exception 例外
     */
    @ActionMethod(isDownloadable = true)
    @RequestType
    @Permission(authType = AuthType.OUTPUT)
    public Result dpm400C00F24Output(DpContext ctx, Dpm400C00Form form) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("dpm400C00F24Output");
        }
        try {
            // 帳票出力準備
        	ManageInsWsPlanForVacScDto scDto = form.convertInsWsPlanScDto();
            String templateRootPath = getRealPath(TemplateInfo.EXCEL_TEMPLATE_PATH.getValue());

            searchInsWsPlanForVacList(form);

            // 帳票出力実行
            ExportResult exportResult = dpmReportService.outputInsWsPlanForVacList(templateRootPath, scDto, form.isRyutsu());
            form.setExportResult(exportResult);
            form.setDownLoadFileName(exportResult.getName());
        } finally {

        }
        return ActionResult.SUCCESS;
    }



	/**
	 * 検索処理の実行
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchResultList(Dpm400C00Form form) throws Exception {
		// 実行した検索条件を画面に設定
		ManageInsWsPlanScDto scDto = form.convertScDto();
		form.setViewField();

		// 流通政策部かどうかを確認
		checkRyutsu(form);

		try {
			ManageInsWsPlanDto serviceResult = dpmInsWsPlanSearchService.searchList(scDto);
			super.getRequestBox().put(Dpm400C00Form.DPM400C00_DATA_R, serviceResult);
		} finally {
			ManageInsWsPlanHeaderDto headerDto = dpmInsWsPlanSearchService.searchHeader(scDto);
			super.getRequestBox().put(Dpm400C00Form.DPM400C00_INPUT_DATA_R, headerDto);
			form.convertHeaderDto(headerDto);
		}
	}

	/**
	 *  (ワ)検索処理の実行
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchInsWsPlanForVacList(Dpm400C00Form form) throws Exception {

		// 施設追加実行可
		super.getRequestBox().put(Dpm400C00Form.DPM400C00_ENABLE_INS_ENTRY, Boolean.FALSE);

		ManageInsWsPlanForVacScDto scDto = form.convertInsWsPlanScDto();
		form.setViewFieldVac();
		ManageInsWsPlanForVacDto resultDto = null;

		// 流通政策部かどうかを確認
		checkRyutsu(form);

		try {
			resultDto = dpmInsWsPlanForVacSearchService.searchInsWsPlan(scDto);
			super.getRequestBox().put(Dpm400C00Form.DPM400C01_DATA_R, resultDto);
		} finally {

			// 集計行取得
			ManageInsWsPlanForVacResultDetailTotalDto totalDto = dpmInsWsPlanForVacSearchService.searchInsWsPlanTotal(scDto);
			super.getRequestBox().put(Dpm400C00Form.DPM400C01_DATA_R_TOTAL, totalDto);

			// ヘッダー情報取得
			ManageInsWsPlanForVacHeaderDto headerDto = dpmInsWsPlanForVacSearchService.searchInsWsPlanHeader(scDto.getInsNo(), scDto.getTmsTytenCd());
            super.getRequestBox().put(Dpm400C00Form.DPM400C00_INPUT_DATA_R, headerDto);

			// 施設情報が取得できなかった場合は、ログイン情報設定
			if (StringUtils.isNotEmpty(scDto.getInsNo()) && headerDto.getInsMstResultDto() == null) {
				setUserInfo(form);
			}

			// ヘッダー施設情報設定
			form.convertHeaderDto(headerDto);

			// 施設コード、特約店コードが設定されていない場合は、施設追加実行可
			if (StringUtils.isEmpty(scDto.getInsNo()) && StringUtils.isEmpty(scDto.getTmsTytenCd())) {
				super.getRequestBox().put(Dpm400C00Form.DPM400C00_ENABLE_INS_ENTRY, Boolean.TRUE);
			}
		}
	}

	/**
	 * ログインユーザの組織情報を設定する。
	 *
	 * @param form アクションフォーム
	 */
	private void setUserInfo(Dpm400C00Form form) {

		// ユーザ関係情報をクリア
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
	 * 画面入力チェック
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dpm400C00F05Validation(DpContext ctx, Dpm400C00Form form) throws ValidateException {
		// [必須] 組織・担当者 or 施設コード
		if (StringUtils.isEmpty(form.getInsNo())) {
			if (StringUtils.isEmpty(form.getSosCd3())) {
				throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "組織・担当者")));
			}
			if (StringUtils.isNotEmpty(form.getInsType()) && InsType.getInstance(form.getInsType()).equals(InsType.P)) {
				if (StringUtils.isEmpty(form.getJgiNo())) {
					throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "組織・担当者")));
				}
			}
		}
		// [必須] 対象区分
		if (StringUtils.isEmpty(form.getInsType())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "対象区分")));
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

	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * （ワ）画面入力チェック
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dpm400C00F25Validation(DpContext ctx, Dpm400C00Form form) throws ValidateException {
		// [必須] 組織・担当者 or 施設コード
		if (StringUtils.isEmpty(form.getInsNo())) {
			if (StringUtils.isEmpty(form.getSosCd3())) {
				throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "組織・担当者")));
			}
			if (StringUtils.isNotEmpty(form.getActivityType()) && ActivityType.getInstance(form.getActivityType()).equals(ActivityType.IPPAN)) {
				if (StringUtils.isEmpty(form.getJgiNo())) {
					throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "組織・担当者")));
				}
				boolean addrCheck = StringUtils.isEmpty(form.getAddrCodePref()) || StringUtils.isEmpty(form.getAddrCodeCity());
				boolean tytenCheck = StringUtils.isEmpty(form.getTmsTytenCdPart());
				if (addrCheck && tytenCheck) {
					throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "市区町村または特約店コード")));
				}
			} else {
				if (StringUtils.isEmpty(form.getSosCd3())) {
					throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "組織・担当者")));
				}
			}
		}
		// [必須] 品目コード
		if (StringUtils.isEmpty(form.getProdCode())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "品目コード")));
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
	public void dpm400C00F10Validation(DpContext ctx, Dpm400C00Form form) throws ValidateException {
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
			if (rowId.length != 7) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 品目固定コード
			if (StringUtils.isNotEmpty(rowId[0]) && !ValidationUtil.isLong(rowId[0])) {
				final String errMsg = "受信パラメータ(品目固定コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 施設コード
			if (StringUtils.isNotEmpty(rowId[1]) && !ValidationUtil.isLong(rowId[1])) {
				final String errMsg = "受信パラメータ(施設コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 特約店コード
			if (StringUtils.isEmpty(rowId[2])) {
				final String errMsg = "受信パラメータ(特約店コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// シーケンスキー
			if (StringUtils.isNotEmpty(rowId[3]) && !ValidationUtil.isLong(rowId[3])) {
				final String errMsg = "受信パラメータ(シーケンスキー)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 最終更新日時
			if (StringUtils.isNotEmpty(rowId[4]) && !ValidationUtil.isLong(rowId[4])) {
				final String errMsg = "受信パラメータ(最終更新日時)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// T価ベース 更新前
			if (StringUtils.isNotEmpty(rowId[5]) && !ValidationUtil.isLong(rowId[5])) {
				final String errMsg = "受信パラメータ(T価ベース 更新前)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// T価ベース 更新後（入力値）
			if (StringUtils.isNotEmpty(rowId[6]) && !ValidationUtil.isLong(rowId[6])) {
				final String errMsg = "受信パラメータ(T価ベース 更新後（入力値）)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
		}
	}

	/**
	 * ユーザの組織が流通政策部であるかを確認する
	 * @param form ActionForm
	 */
	private void checkRyutsu(Dpm400C00Form form) {
        form.setRyutsu(false);
	    DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
	    if (userInfo != null) {
	        DpUser user = userInfo.getSettingUser();
            if (user != null) {
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
	    }
	}

	/**
	 * 対象区分リストを設定
	 * @param form Dpm400Form
	 */
	private void setInsList(Dpm400C00Form form) {

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
