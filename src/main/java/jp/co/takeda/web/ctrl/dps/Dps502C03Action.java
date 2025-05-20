package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.exp.ErrMessageKey.*;
import static jp.co.takeda.security.BusinessType.*;
import static jp.co.takeda.security.DpAuthority.AuthType.*;

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
import jp.co.takeda.bean.Constants.TemplateInfo;
import jp.co.takeda.dto.TmsTytenPlanEditForVacInputDto;
import jp.co.takeda.dto.TmsTytenPlanReferenceForVacResultDto;
import jp.co.takeda.dto.TmsTytenPlanReferenceHeaderForVacResultDto;
import jp.co.takeda.dto.WsPlanReferenceForVacScDto;
import jp.co.takeda.model.div.TytenKisLevel;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsReportForVacService;
import jp.co.takeda.service.DpsTmsTytenPlanEditForVacService;
import jp.co.takeda.service.DpsTmsTytenPlanReferenceForVacService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.in.dps.Dps502C00Form;

/**
 * Dps502C03((ワ)特約店別計画参照画面)のアクションクラス
 *
 * @author stakeuchi
 */
@Controller("Dps502C03Action")
public class Dps502C03Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps502C03Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * ワクチン特約店別計画参照サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsTmsTytenPlanReferenceForVacService")
	DpsTmsTytenPlanReferenceForVacService dpsTmsTytenPlanReferenceForVacService;

	/**
	 * ワクチン帳票出力サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsReportForVacService")
	DpsReportForVacService dpsReportForVacService;

	/**
	 * ワクチン特約店別計画編集サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsTmsTytenPlanEditForVacService")
	DpsTmsTytenPlanEditForVacService dpsTmsTytenPlanEditForVacService;

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
//	@ActionMethod
//	@RequestType(businessType = VACCINE)
//	@Permission( authType = REFER)
//	public Result dps502C03F00(DpContext ctx, Dps502C00Form form) throws Exception {
//
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("dps502C03F00");
//		}
//
//		// 特約店検索モードをクリア
//		form.setTmsSelectMode(null);
//
//		// 初期化処理
//		form.formInit();
//
//		// 画面ヘッダ情報の取得とリクエストに設定
//		TmsTytenPlanReferenceHeaderForVacResultDto resultDto = dpsTmsTytenPlanReferenceForVacService.searchHeaderInfo(form.getTmsTytenCdPart());
//		form.setTmsTytenName(resultDto.getTmsTytenName());
//		super.getRequestBox().put(Dps502C00Form.DPS502C03_DATA_R_RESULT_HEADER, resultDto);
//
//		return ActionResult.SUCCESS;
//	}

	/**
	 * 表示する処理時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission( authType = REFER)
	public Result dps502C03F05(DpContext ctx, Dps502C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps502C03F05");
		}
		WsPlanReferenceForVacScDto scDto = form.convertWsPlanReferenceForVacScDto();
		try {
			TmsTytenPlanReferenceForVacResultDto resultDetailDto = dpsTmsTytenPlanReferenceForVacService.searchTmsTytenPlanSummary(scDto);
			super.getRequestBox().put(Dps502C00Form.DPS502C03_DATA_R_RESULT_DETAIL, resultDetailDto);
		} finally {

			// 画面ヘッダ情報の取得とリクエストに設定
			TmsTytenPlanReferenceHeaderForVacResultDto resultDto = dpsTmsTytenPlanReferenceForVacService.searchHeaderInfo(form.getTmsTytenCdPart());
			form.setTmsTytenName(resultDto.getTmsTytenName());
//			form.setCategory(resultDto.get);
			super.getRequestBox().put(Dps502C00Form.DPS502C03_DATA_R_RESULT_HEADER, resultDto);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 出力する処理時に呼ばれるアクションメソッド<br>
	 * 実績情報は取得しない。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(isDownloadable = true)
	@RequestType
	@Permission( authType = OUTPUT)
	public Result dps502C03F10Output(DpContext ctx, Dps502C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps502C03F10Output");
		}

		try {
			// 帳票出力
			String templateRootPath = getRealPath(TemplateInfo.EXCEL_TEMPLATE_PATH.getValue());
			WsPlanReferenceForVacScDto scDto = form.convertWsPlanReferenceForVacScDto();
			ExportResult exportResult = dpsReportForVacService.outputWsPlanListNonJisseki(templateRootPath, scDto);
			form.setExportResult(exportResult);
			form.setDownLoadFileName(exportResult.getName());

		} finally {
			// 画面ヘッダ情報の取得とリクエストに設定
			TmsTytenPlanReferenceHeaderForVacResultDto resultDto = dpsTmsTytenPlanReferenceForVacService.searchHeaderInfo(form.getTmsTytenCdPart());
			form.setTmsTytenName(resultDto.getTmsTytenName());
			super.getRequestBox().put(Dps502C00Form.DPS502C03_DATA_R_RESULT_HEADER, resultDto);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 出力する処理時に呼ばれるアクションメソッド<br>
	 * 実績情報も取得する。<br>
	 * struts-configには定義なしだが、実装は削除しないで残す。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(isDownloadable = true)
	@RequestType(businessType = VACCINE)
	@Permission( authType = OUTPUT)
	public Result dps502C03F15Output(DpContext ctx, Dps502C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps502C03F10Output");
		}

		try {
			// 帳票出力
			String templateRootPath = getRealPath(TemplateInfo.EXCEL_TEMPLATE_PATH.getValue());
			WsPlanReferenceForVacScDto scDto = form.convertWsPlanReferenceForVacScDto();
			ExportResult exportResult = dpsReportForVacService.outputWsPlanList(templateRootPath, scDto);
			form.setExportResult(exportResult);
			form.setDownLoadFileName(exportResult.getName());

		} finally {
			// 画面ヘッダ情報の取得とリクエストに設定
			TmsTytenPlanReferenceHeaderForVacResultDto resultDto = dpsTmsTytenPlanReferenceForVacService.searchHeaderInfo(form.getTmsTytenCdPart());
			form.setTmsTytenName(resultDto.getTmsTytenName());
			super.getRequestBox().put(Dps502C00Form.DPS502C03_DATA_R_RESULT_HEADER, resultDto);
		}

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
	@Permission(authType = EDIT)
	public Result dps502C03F05Execute(DpContext ctx, Dps502C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps502C03F05Execute");
		}

		try {
			// 特約店別計画更新
			TmsTytenPlanEditForVacInputDto inputDto = form.convertTmsTytenPlanEditForVacInputDto();
// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
//			dpsTmsTytenPlanEditForVacService.editWsPlan(inputDto);
			dpsTmsTytenPlanEditForVacService.editWsPlanRefer(inputDto);
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0004I", "特約店別計画"));
		} finally {
			// 特約店別計画再検索
//			searchWsPlan(form);
		}

		WsPlanReferenceForVacScDto scDto = form.convertWsPlanReferenceForVacScDto();
		try {
			TmsTytenPlanReferenceForVacResultDto resultDetailDto = dpsTmsTytenPlanReferenceForVacService.searchTmsTytenPlanSummary(scDto);
			super.getRequestBox().put(Dps502C00Form.DPS502C03_DATA_R_RESULT_DETAIL, resultDetailDto);
		} finally {

			// 画面ヘッダ情報の取得とリクエストに設定
			TmsTytenPlanReferenceHeaderForVacResultDto resultDto = dpsTmsTytenPlanReferenceForVacService.searchHeaderInfo(form.getTmsTytenCdPart());
			form.setTmsTytenName(resultDto.getTmsTytenName());
//			form.setCategory(resultDto.get);
			super.getRequestBox().put(Dps502C00Form.DPS502C03_DATA_R_RESULT_HEADER, resultDto);
		}


		return ActionResult.SUCCESS;
	}

	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * 検索時のパラーメタチェック
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps502C03F05Validation(DpContext ctx, Dps502C00Form form) throws ValidateException {
		searchValidation(ctx, form);
	}

	/**
	 * 出力時のパラーメタチェック
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps502C03F10Validation(DpContext ctx, Dps502C00Form form) throws ValidateException {
		searchValidation(ctx, form);
	}

	/**
	 * 検索時/出力時共通パラメータチェック
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	private void searchValidation(DpContext ctx, Dps502C00Form form) throws ValidateException {
		// 集約方法必須チェック
		if (StringUtils.isEmpty(form.getTytenKisLevel())) {
			final String errMsg = "集約方法が未選択";
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "集約方法"), errMsg));
		}
		// 特約店コード必須チェック（集約方法が支店、課、ブロック１、ブロック２の場合）
		if (TytenKisLevel.SHITEN.getDbValue().equals(form.getTytenKisLevel()) || TytenKisLevel.KA.getDbValue().equals(form.getTytenKisLevel())
			|| TytenKisLevel.BLK1.getDbValue().equals(form.getTytenKisLevel())
			|| TytenKisLevel.BLK2.getDbValue().equals(form.getTytenKisLevel())
			) {
			if (StringUtils.isEmpty(form.getTmsTytenCdPart())) {
				final String errMsg = "特約店コードが未入力または未選択";
				throw new ValidateException(new Conveyance(new MessageKey("DPS1029E"), errMsg));
			}
		}
		// 特約店コード数値チェック
		if (StringUtils.isNotEmpty(form.getTmsTytenCdPart())) {
			if (!ValidationUtil.isLong(form.getTmsTytenCdPart())) {
				final String errMsg = "特約店コードが半角数値以外";
				throw new ValidateException(new Conveyance(new MessageKey("DPC1004E", "特約店コード", "半角数値"), errMsg));
			}

			if (!ValidationUtil.isMinLength(form.getTmsTytenCdPart(), 3)) {
				final String errMsg = "特約店コードが3桁未満";
				throw new ValidateException(new Conveyance(new MessageKey("DPC1019E", "特約店コード", "3"), errMsg));
			}

			if (!ValidationUtil.isMaxLength(form.getTmsTytenCdPart(), 13)) {
				final String errMsg = "特約店コードが13桁を超えている";
				throw new ValidateException(new Conveyance(new MessageKey("DPC1020E", "特約店コード", "13"), errMsg));
			}
		}
	}
	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * 登録処理時入力チェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps502C03F05ExecuteValidation(DpContext ctx, Dps502C00Form form) throws ValidateException {
		// 更新行のNullチェック
		String[] rowIdList = form.getRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		for (int i = 0; i < rowIdList.length; i++) {
			// パラーメータのサイズチェック
			String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);
			if (rowId.length != 5) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 計画値に差異がない場合は更新が行われないためチェックを行わない
			if (StringUtils.equals(rowId[3], rowId[4])) {
				continue;
			}
			// シーケンスキー
			if (StringUtils.isEmpty(rowId[0]) || !ValidationUtil.isLong(rowId[0])) {
				final String errMsg = "受信パラメータ(シーケンスキー)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 最終更新日
			if (StringUtils.isEmpty(rowId[1]) || !ValidationUtil.isLong(rowId[1])) {
				final String errMsg = "受信パラメータ(最終更新日)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 組織コード
			if (StringUtils.isEmpty(rowId[2])) {
				final String errMsg = "受信パラメータ(組織コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
		}
	}

}
