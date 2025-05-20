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
import jp.co.takeda.bean.Constants.TemplateInfo;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.InsPlanUpdateDto;
import jp.co.takeda.dto.InsProdPlanResultDto;
import jp.co.takeda.dto.InsProdPlanResultHeaderDto;
import jp.co.takeda.dto.InsProdPlanScDto;
import jp.co.takeda.dto.ManagePlanUpdateResultDto;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.SosMstCtg;
import jp.co.takeda.model.div.SosLvl;
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
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.in.dpm.Dpm201C00Form;

/**
 * Dpm201C00((医)施設品目別計画編集画面)のアクションクラス
 *
 * @author stakeuchi
 */
@Controller("Dpm201C00Action")
public class Dpm201C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dpm201C00Action.class);

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPM201C00";

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
	 * 施設別計画 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmInsPlanService")
	protected DpmInsPlanService dpmInsPlanService;

	/**
	 * 帳票サービス
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
	 * （ワ）施設別計画 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmInsPlanForVacSearchService")
	protected DpmInsPlanForVacSearchService dpmInsPlanForVacSearchService;

	/**
	 * （ワ）施設別計画 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmInsPlanForVacService")
	protected DpmInsPlanForVacService dpmInsPlanForVacService;

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
	public Result dpm201C00F00(DpContext ctx, Dpm201C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm201C00F00");
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
	public Result dpm201C00F05(DpContext ctx, Dpm201C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm201C00F05");
			LOG.debug("JgiNo:" + form.getJgiNo());
			LOG.debug("InsNo:" + form.getInsNo());
			LOG.debug("ProdCategory:" + form.getProdCategory());
			LOG.debug("PlanData:" + form.getPlanData());
		}

		// カテゴリリストの初期設定
		initCategoryList(form);
		// 検索実行
		form.setTranField();
		searchInsProdPlanList(ctx, form);
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
	public Result dpm201C00F10Execute(DpContext ctx, Dpm201C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm201C00F10Execute");
			LOG.debug("JgiNo:" + form.getJgiNo());
			LOG.debug("InsNo:" + form.getInsNo());
			LOG.debug("ProdCategory:" + form.getProdCategory());
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
	private void searchInsProdPlanList(DpContext ctx, Dpm201C00Form form) throws Exception {

		// 実行した検索条件を画面に設定
		InsProdPlanScDto scDto = form.convertInsProdPlanScDto();
		form.setViewField();
		try {

			InsProdPlanResultDto resultDto = dpmInsPlanSearchService.searchInsProdPlan(scDto);
			super.getRequestBox().put(Dpm201C00Form.DPM201C00_DATA_R_SEARCH_RESULT, resultDto);

		} finally {

			// ヘッダー情報取得
			InsProdPlanResultHeaderDto headerDto = dpmInsPlanSearchService.searchInsPlanHeader(scDto.getInsNo(), null);
			super.getRequestBox().put(Dpm201C00Form.DPM201C00_DATA_R_HEADER, headerDto);

			// 削除施設の場合はメッセージを追加
			if (headerDto != null && headerDto.IsDeleteIns()) {
				addErrorMessage(ctx, new MessageKey("DPM1005E"));
			}

			// ヘッダー施設情報が取得できなかった場合は、ログイン情報設定
			if (headerDto == null) {
				setUserInfo(form);
			}

			form.convertHeaderDto(headerDto);

			setJgiNoOfMrLevelUser(form, headerDto);
		}
	}

	/**
	 * フォームに従業員番号を補完する
	 * 【留意事項 以下の修正の意味】
	 * 組織レベルMRの場合、施設検索画面での組織・担当者欄は担当者である必要がある(2021 STEP2案件)。
	 * 通常、従業員番号はheaderDtoに入ってきて、form.convertHeaderDtoにてformに設定される。
	 * しかし、
	 * 条件によっては取得メソッド内部で消される（dpmInsPlanSearchService.searchInsPlanHeader()）
	 *  既存処理を変更せず、従業員番号をformに設定するために、以下処理を追加した。

	 * @param form
	 * @param headerDto
	 */
	private void setJgiNoOfMrLevelUser(Dpm201C00Form form, InsProdPlanResultHeaderDto headerDto) {
		if( headerDto != null && headerDto.getJgiNo() == null) {
			DpUser user  = DpUserInfo.getDpUserInfo().getSettingUser();
			if(user.isSosLvl(SCREEN_ID, SosLvl.MR)) {
				form.setJgiNo(String.format("%07d",user.getJgiNo()));
				//全MRフラグを設定
				form.setMrFlg(true);
			}
		}
	}

	/**
	 * ログインユーザの組織情報を設定する。
	 *
	 * @param form ActionForm
	 */
	private void setUserInfo(Dpm201C00Form form) {

		// ログインユーザの組織情報をセット
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {
				form.setSosCd2(null);
				form.setSosCd3(null);
				form.setSosCd4(null);
				form.setJgiNo(null);

				switch(user.getSosLvl(SCREEN_ID)){
				case ALL:
					break;
				case BRANCH:
					form.setSosCd2(user.getSosCd2());
					break;
				case OFFICE:
					form.setSosCd2(user.getSosCd2());
					form.setSosCd3(user.getSosCd3());
					break;
				case MR:
					form.setSosCd2(user.getSosCd2());
					form.setSosCd3(user.getSosCd3());
					form.setJgiNo(String.valueOf(user.getJgiNo()));
					//全MRフラグを設定
					form.setMrFlg(true);
					break;
				default: // デフォルトは、安全をとって、従業員レベルに制限する
					form.setSosCd2(user.getSosCd2());
					form.setSosCd3(user.getSosCd3());
					form.setJgiNo(String.valueOf(user.getJgiNo()));
					//全MRフラグを設定
					form.setMrFlg(true);
					break;
				}
			}
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
	public Result dpm201C00F14Output(DpContext ctx, Dpm201C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm201C00F14Output");
		}

		// 帳票出力準備
		InsProdPlanScDto insProdPlanScDto = form.convertInsProdPlanScDto();
		String templateRootPath = getRealPath(TemplateInfo.EXCEL_TEMPLATE_PATH.getValue());

		searchInsProdPlanList(ctx, form);

		// 帳票出力実行
		ExportResult exportResult = dpmReportService.outputInsProdPlanList(templateRootPath, insProdPlanScDto);
		form.setExportResult(exportResult);
		form.setDownLoadFileName(exportResult.getName());

		return ActionResult.SUCCESS;
	}

	/**
	 * カテゴリリストの初期設定
	 * @param form Dpm201C00Form
	 */
	private void initCategoryList(Dpm201C00Form form) {
		// 汎用マスタのカテゴリリスト
		List<DpmCCdMst> categoryList = (dpmCodeMasterSearchService.searchCategorylist(null,PLAN_ID));

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
			}
		}

		// 上記で取得したコードのカテゴリの内、対象の立案レベルをもつ計画対象カテゴリ領域のカテゴリのみカテゴリリストにセットする
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
	public void dpm201C00F05Validation(DpContext ctx, Dpm201C00Form form) throws ValidateException {
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
	public void dpm201C00F10Validation(DpContext ctx, Dpm201C00Form form) throws ValidateException {
		executeValidation(ctx, form);
	}

	/**
	 * 登録処理時の共通Validationメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	private void executeValidation(DpContext ctx, Dpm201C00Form form) throws ValidateException {
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
				final String errMsg = "受信パラメータ(品目コード)が不正：";
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
}
