package jp.co.takeda.web.ctrl.dpm;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

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
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.ManagePlanUpdateResultDto;
import jp.co.takeda.dto.ProdPlanSummaryResultDto;
import jp.co.takeda.dto.ProdPlanSummaryScDto;
import jp.co.takeda.dto.SosPlanUpdateDto;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpmCodeMasterSearchService;
import jp.co.takeda.service.DpmMrPlanSearchService;
import jp.co.takeda.service.DpmMrPlanService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dpm.Dpm501C01Form;

/**
 * Dpm501C01((医)積上結果表示ダイアログ)のアクションクラス
 *
 * @author stakeuchi
 */
@Controller("Dpm501C01Action")
public class Dpm501C01Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dpm501C01Action.class);

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPM501C01";

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 組織別計画(担当者) 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmMrPlanSearchService")
	protected DpmMrPlanSearchService dpmMrPlanSearchService;

	/**
	 * 組織別計画(担当者) 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmMrPlanService")
	protected DpmMrPlanService dpmMrPlanService;

	/**
	 * コードマスタデータ取得サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmCodeMasterSearchService")
	protected DpmCodeMasterSearchService dpmCodeMasterSearchService;

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
	@Permission(authType = AuthType.EDIT)
	public Result dpm501C01F00(DpContext ctx, Dpm501C01Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm501C01F00");
			LOG.debug("JgiNo:" + form.getJgiNo());
		}

		// 初期化処理
		form.formInit();
		setTitle(form);

		// 初期検索実行
		searchProdPlanSummaryList(form);
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
	public Result dpm501C01F05Execute(DpContext ctx, Dpm501C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm501C01F10Execute");
			LOG.debug("JgiNo:" + form.getJgiNo());
			LOG.debug("ProdCategory:" + form.getProdCategory());
		}

		try {
			// 更新DTO
			List<SosPlanUpdateDto> updateDtoList = form.convertProdPlanSummaryUpdateDto();
			setTitle(form);

			// 更新実行
			ManagePlanUpdateResultDto resultDto = new ManagePlanUpdateResultDto(0, 0, 0);
			if (!updateDtoList.isEmpty()) {
				resultDto = dpmMrPlanService.updateMrPlan(SCREEN_ID, updateDtoList);
			}

			// 更新完了メッセージの追加
			final int updateSizeUH = resultDto.getUpdateCountUh();
			final int updateSizeP = resultDto.getUpdateCountP();
			final int updateSizeZ = resultDto.getUpdateCountZ();

			addMessage(ctx, new MessageKey("DPC0007I", form.getTitleUH(), String.valueOf(updateSizeUH),
					form.getTitleP(), String.valueOf(updateSizeP), form.getTitleZ(), String.valueOf(updateSizeZ)));

		} finally {
			// 再検索実行
			searchProdPlanSummaryList(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * formにUH、P、Zのタイトルを設定する
	 * @param form
	 */
	private void setTitle(Dpm501C01Form form) {
		// メッセージのタイトルを取得
		List<DpmCCdMst> mstList = dpmCodeMasterSearchService.searchInsTypeTitle(form.getProdCategory());
		for (DpmCCdMst dpmCCdMst : mstList) {
			if (dpmCCdMst.getDataCd().equals(InsType.UH.getDbValue())) {
				form.setTitleUH(dpmCCdMst.getDataName());
			} else if (dpmCCdMst.getDataCd().equals(InsType.P.getDbValue())) {
				form.setTitleP(dpmCCdMst.getDataName());
			} else if (dpmCCdMst.getDataCd().equals(InsType.ZATU.getDbValue())) {
				form.setTitleZ(dpmCCdMst.getDataName());
			}
		}
	}

	/**
	 * 検索処理の実行
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchProdPlanSummaryList(Dpm501C01Form form) throws Exception {

		// 組織別計画の検索実行
		ProdPlanSummaryScDto scDto = form.convertProdPlanSummaryScDto();
		ProdPlanSummaryResultDto resultDto = dpmMrPlanSearchService.searchSosProdPlanInsSummary(scDto);

		// リクエストボックスに組織別計画をセット
		super.getRequestBox().put(Dpm501C01Form.DPM501C01_DATA_R_SEARCH_RESULT, resultDto);
	}

	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * 登録処理時の入力パラメータチェック
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dpm501C01F05Validation(DpContext ctx, Dpm501C01Form form) throws ValidateException {
		executeValidation(ctx, form);
	}

	/**
	 * 登録処理時の共通Validationメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	private void executeValidation(DpContext ctx, Dpm501C01Form form) throws ValidateException {
		// 更新パラーメータチェック
		String[] rowIdList = form.getRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		for (int i = 0; i < rowIdList.length; i++) {
			// パラーメータのサイズチェック
			String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);
			if (rowId.length != 13) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 品目コード
			if (StringUtils.isNotEmpty(rowId[0]) && !ValidationUtil.isInteger(rowId[0])) {
				final String errMsg = "受信パラメータ(組織コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [UH]シーケンスキー
			if (StringUtils.isNotEmpty(rowId[1]) && !ValidationUtil.isLong(rowId[1])) {
				final String errMsg = "受信パラメータ([UH]シーケンスキー)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [UH]最終更新日
			if (StringUtils.isNotEmpty(rowId[2]) && !ValidationUtil.isLong(rowId[2])) {
				final String errMsg = "受信パラメータ([UH]最終更新日)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [UH]更新前計画値
			if (StringUtils.isNotEmpty(rowId[3]) && !ValidationUtil.isLong(rowId[3])) {
				final String errMsg = "受信パラメータ([UH]計画値(更新前))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [UH]更新後計画値
			if (StringUtils.isNotEmpty(rowId[10]) && !ValidationUtil.isLong(rowId[10])) {
				final String errMsg = "受信パラメータ([UH]計画値(更新後))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [P]シーケンスキー
			if (StringUtils.isNotEmpty(rowId[4]) && !ValidationUtil.isLong(rowId[4])) {
				final String errMsg = "受信パラメータ([P]シーケンスキー)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [P]最終更新日
			if (StringUtils.isNotEmpty(rowId[5]) && !ValidationUtil.isLong(rowId[5])) {
				final String errMsg = "受信パラメータ([P]最終更新日)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [P]更新前計画値
			if (StringUtils.isNotEmpty(rowId[6]) && !ValidationUtil.isLong(rowId[6])) {
				final String errMsg = "受信パラメータ([P]計画値(更新前))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [P]更新後計画値
			if (StringUtils.isNotEmpty(rowId[11]) && !ValidationUtil.isLong(rowId[11])) {
				final String errMsg = "受信パラメータ([P]計画値(更新後))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [Z]シーケンスキー
			if (StringUtils.isNotEmpty(rowId[7]) && !ValidationUtil.isLong(rowId[7])) {
				final String errMsg = "受信パラメータ([Z]シーケンスキー)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [Z]最終更新日
			if (StringUtils.isNotEmpty(rowId[8]) && !ValidationUtil.isLong(rowId[8])) {
				final String errMsg = "受信パラメータ([Z]最終更新日)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [Z]更新前計画値
			if (StringUtils.isNotEmpty(rowId[9]) && !ValidationUtil.isLong(rowId[9])) {
				final String errMsg = "受信パラメータ([Z]計画値(更新前))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [Z]更新後計画値
			if (StringUtils.isNotEmpty(rowId[12]) && !ValidationUtil.isLong(rowId[12])) {
				final String errMsg = "受信パラメータ([Z]計画値(更新後))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
		}
	}
}
