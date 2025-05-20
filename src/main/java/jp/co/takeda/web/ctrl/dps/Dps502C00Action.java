package jp.co.takeda.web.ctrl.dps;

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
import jp.co.takeda.dto.TmsTytenPlanReferenceHeaderResultDto;
import jp.co.takeda.dto.TmsTytenPlanReferenceResultDto;
import jp.co.takeda.dto.WsPlanReferenceScDto;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.SosMstCtg;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.JokenKbn;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.model.div.TytenKisLevel;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcSystemManageSearchService;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.service.DpsPlannedCtgSearchService;
import jp.co.takeda.service.DpsReportService;
import jp.co.takeda.service.DpsTmsTytenPlanEditService;
import jp.co.takeda.service.DpsTmsTytenPlanReferenceService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.in.dps.Dps502C00Form;

/**
 * Dps502C00((医)特約店別計画参照画面)のアクションクラス
 *
 * @author yokokawa
 */
@Controller("Dps502C00Action")
public class Dps502C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps502C00Action.class);

	private static final String SCREEN_ID = "DPS502C00";

	/**
	 * 計画（1：期別、2：月別）
	 */
	private static final String PLAN_ID = "1";

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 特約店別計画参照サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsTmsTytenPlanReferenceService")
	DpsTmsTytenPlanReferenceService dpsTmsTytenPlanReferenceService;

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
	 * 帳票サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsReportService")
	DpsReportService dpsReportService;

	/**
	 * 特約店別計画編集サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsTmsTytenPlanEditService")
	DpsTmsTytenPlanEditService dpsTmsTytenPlanEditService;

	/**
	 * 納入計画管理検索サービス実装クラス
	 */
    @Autowired(required = true)
    @Qualifier("dpcSystemManageSearchService")
    protected DpcSystemManageSearchService dpcSystemManageSearchService;

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
	@Permission(authType = REFER)
	public Result dps502C00F00(DpContext ctx, Dps502C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps502C00F00");
		}

		// 特約店検索モードをクリア
		form.setTmsSelectMode(null);

		// 初期化処理
		form.formInit();

		// 価ベースのデフォルト値設定
// mod 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		//form.setKaBaseKb(KaBaseKb.S_KA_BASE.getDbValue());

		//カテゴリリストの設定
		initCategoryList(form);

		// 施設特約店〆処理フラグ取得（ワクチン用）
		SysManage sysManage = dpcSystemManageSearchService.searchSysManage(SysClass.DPS, SysType.VACCINE);
		form.setWsEndFlg(sysManage.getWsEndFlg());

		// 画面ヘッダ情報の取得とリクエストに設定
		TmsTytenPlanReferenceHeaderResultDto resultDto = dpsTmsTytenPlanReferenceService.searchHeaderInfo(form.getTmsTytenCdPart());
		form.setTmsTytenName(resultDto.getTmsTytenName());
		super.getRequestBox().put(Dps502C00Form.DPS502C00_DATA_HEADER, resultDto);
		form.setTmsTytenName(resultDto.getTmsTytenName());
		return ActionResult.SUCCESS;
	}

	/**
	 * 表示する処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = REFER)
	public Result dps502C00F05(DpContext ctx, Dps502C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps502C00F05");
		}
// add Start 2022/7/22  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		//検索時の価ベースを保持
		form.setKaBaseKbTran(form.getKaBaseKb());
// add End 2022/7/22  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		// 検索実行
		WsPlanReferenceScDto wsPlanReferenceScDto = form.convertWsPlanReferenceScDto();
		try {
			TmsTytenPlanReferenceResultDto tmsTytenPlanReferenceResultDto = dpsTmsTytenPlanReferenceService.searchTmsTytenPlanSummary(wsPlanReferenceScDto);
			super.getRequestBox().put(Dps502C00Form.DPS502C00_DATA_R, tmsTytenPlanReferenceResultDto);

		} finally {
			// 画面ヘッダ情報の取得とリクエストに設定
			TmsTytenPlanReferenceHeaderResultDto resultDto = dpsTmsTytenPlanReferenceService.searchHeaderInfo(form.getTmsTytenCdPart());
			form.setTmsTytenName(resultDto.getTmsTytenName());
			super.getRequestBox().put(Dps502C00Form.DPS502C00_DATA_HEADER, resultDto);
		}
// add Start 2022/7/22  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		//保持した価ベースを画面の選択値にセット
		form.setKaBaseKb(form.getKaBaseKbTran());
// add End 2022/7/22  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
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
	@Permission(authType = REFER)
	public Result dps502C00F10Output(DpContext ctx, Dps502C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps502C00F10Output");
		}
		try {
			// 帳票出力準備
			WsPlanReferenceScDto wsPlanReferenceScDto = form.convertWsPlanReferenceScDto();
			String templateRootPath = getRealPath(TemplateInfo.EXCEL_TEMPLATE_PATH.getValue());

			// 帳票出力実行
			ExportResult exportResult = dpsReportService.outputWsPlanListNonJisseki(templateRootPath, wsPlanReferenceScDto);
			form.setExportResult(exportResult);
			form.setDownLoadFileName(exportResult.getName());
		} finally {
			// 画面ヘッダ情報の取得とリクエストに設定
			TmsTytenPlanReferenceHeaderResultDto resultDto = dpsTmsTytenPlanReferenceService.searchHeaderInfo(form.getTmsTytenCdPart());
			form.setTmsTytenName(resultDto.getTmsTytenName());
			super.getRequestBox().put(Dps502C00Form.DPS502C00_DATA_HEADER, resultDto);
		}
// add Start 2022/7/22  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		//保持した価ベースを画面の選択値にセット
		form.setKaBaseKb(form.getKaBaseKbTran());
// add End 2022/7/22  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
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
	@RequestType
	@Permission(authType = REFER)
	public Result dps502C00F15Output(DpContext ctx, Dps502C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps502C00F10Output");
		}
		try {
			// 帳票出力準備
			WsPlanReferenceScDto wsPlanReferenceScDto = form.convertWsPlanReferenceScDto();
			String templateRootPath = getRealPath(TemplateInfo.EXCEL_TEMPLATE_PATH.getValue());

			// 帳票出力実行
			ExportResult exportResult = dpsReportService.outputWsPlanList(templateRootPath, wsPlanReferenceScDto);
			form.setExportResult(exportResult);
			form.setDownLoadFileName(exportResult.getName());
		} finally {
			// 画面ヘッダ情報の取得とリクエストに設定
			TmsTytenPlanReferenceHeaderResultDto resultDto = dpsTmsTytenPlanReferenceService.searchHeaderInfo(form.getTmsTytenCdPart());
			form.setTmsTytenName(resultDto.getTmsTytenName());
			super.getRequestBox().put(Dps502C00Form.DPS502C00_DATA_HEADER, resultDto);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 登録処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = EDIT)
	public Result dps502C00F05Execute(DpContext ctx, Dps502C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps502C00F05Execute");
		}

		try {
			// 特約店別計画登録
//			dpsTmsTytenPlanEditService.editWsPlan(form.convertTmsTytenPlanEditInputDto());
			dpsTmsTytenPlanEditService.editWsPlanRefer(form.convertTmsTytenPlanEditInputDto());

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0004I", "特約店別計画"));

		} finally {
			// 特約店別計画再検索
//			searchWsPlan(form.convertTmsTytenPlanEditScDto());
		}

		// 検索実行
		WsPlanReferenceScDto wsPlanReferenceScDto = form.convertWsPlanReferenceScDto();
		try {
			TmsTytenPlanReferenceResultDto tmsTytenPlanReferenceResultDto = dpsTmsTytenPlanReferenceService.searchTmsTytenPlanSummary(wsPlanReferenceScDto);
			super.getRequestBox().put(Dps502C00Form.DPS502C00_DATA_R, tmsTytenPlanReferenceResultDto);

		} finally {
			// 画面ヘッダ情報の取得とリクエストに設定
			TmsTytenPlanReferenceHeaderResultDto resultDto = dpsTmsTytenPlanReferenceService.searchHeaderInfo(form.getTmsTytenCdPart());
			form.setTmsTytenName(resultDto.getTmsTytenName());
			super.getRequestBox().put(Dps502C00Form.DPS502C00_DATA_HEADER, resultDto);
		}
		form.setKaBaseKb(form.getKaBaseKbTran());

		return ActionResult.SUCCESS;
	}

	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * 検索時のパラーメタチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps502C00F05Validation(DpContext ctx, Dps502C00Form form) throws ValidateException {
		searchValidation(ctx, form);
	}

	/**
	 * 出力時のパラーメタチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps502C00F10Validation(DpContext ctx, Dps502C00Form form) throws ValidateException {
		searchValidation(ctx, form);
	}

	/**
	 * 検索時/出力時共通パラメータチェックを行う。
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

		// Y価/T価切替必須チェック
// mod 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
//		if (StringUtils.isEmpty(form.getKaBaseKb())) {
//			final String errMsg = "Y価/B価切替 がnullまたは空";
//			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "Y価/B価切替"), errMsg));
//		}
	}

	/**
	 * カテゴリリストの初期設定
	 * @param form Dps502C00Form
	 */
	private void initCategoryList(Dps502C00Form form) {
		// 汎用マスタのカテゴリリスト
		List<DpsCCdMst> categoryList = (dpsCodeMasterSearchService.searchCategoryList(null, PLAN_ID));

		// 計画対象カテゴリ領域の設定
		List<String> planLvCtgList = new ArrayList<String>();

		// 計画立案レベル-特約店
		planLvCtgList = dpsPlannedCtgSearchService.searchCategoryByPlanLevel(ProdPlanLevel.WS);
		List<CodeAndValue> list = new ArrayList<CodeAndValue>();

		// ログインユーザの組織カテゴリコードを取得
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		List<String> sosCategoryList = new ArrayList<String>();
		DpUser user = null;
		if (userInfo != null) {
			user = userInfo.getSettingUser();
			if (user != null) {

				if (user.getSosCategoryList() != null && !user.getSosCategoryList().isEmpty()) {
					for (SosMstCtg cat : user.getSosCategoryList()) {
						sosCategoryList.add(cat.getCategoryCd());
					}
				}
			}
		}
		// 上記で取得したコードのカテゴリのみ、カテゴリリストにセットする
		for (DpsCCdMst mst : categoryList) {
			if (sosCategoryList.contains(mst.getDataCd()) && planLvCtgList.contains(mst.getDataCd())) {
				CodeAndValue cad = new CodeAndValue(mst.getDataCd(), mst.getDataName());
				list.add(cad);
			}
		}
		if (user.getJokenKbn(SCREEN_ID.substring(0, 6)).equals(JokenKbn.ALL_REFER) ||
				user.getJokenKbn(SCREEN_ID.substring(0, 6)).equals(JokenKbn.ALL_EDIT)) {
			list.add(new CodeAndValue(form.CATEGORY_ALL, "全て（医薬）"));
			form.setCategory(form.CATEGORY_ALL);
		}
		form.setCategoryList(list);
	}

	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * 登録処理時入力チェック
	 *
	 * <pre>
	 * 画面入力にエラーがある場合に例外をスローします。
	 * </pre>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps502C00F05ExecuteValidation(DpContext ctx, Dps502C00Form form) throws ValidateException {
		// エラーメッセージ用に価ベース区分名を取得
// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
//		String kaBaseKbName = KaBaseKb.Y_KA_BASE.getDbValue().equals(form.getKaBaseKb()) ? "Y" : "T";
		String kaBaseKbName = null;
		if(form.getKaBaseKb() == "1") {
			kaBaseKbName = "Y/B";
		} else {
			kaBaseKbName = "S";
		}
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする

		// 受信パラメータチェック
		if (form.getRowIdList() == null) {
			final String errMsg = "受信パラメータがnull";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}

		for (String rowData : form.getRowIdList()) {
			String[] data = ConvertUtil.splitConmma(rowData);

			// サイズチェック
			if (data.length != 5) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 計画値UHチェック
			String plannedValueUh = data[3];
			if (StringUtils.isNotEmpty(plannedValueUh)) {
				if (!ValidationUtil.isLong(plannedValueUh)) {
					final String errMsg = "修正計画(" + kaBaseKbName + ")UHが半角数値以外";
					throw new ValidateException(new Conveyance(new MessageKey("DPC1004E", "修正計画(" + kaBaseKbName + ")UH", "半角数値"), errMsg));
				}
				if (!ValidationUtil.isMaxLength(plannedValueUh, 10)) {
					final String errMsg = "修正計画(" + kaBaseKbName + ")UHが10桁オーバー";
					throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "修正計画(" + kaBaseKbName + ")UH", "10"), errMsg));
				}
				if (ConvertUtil.parseLong(plannedValueUh) < 0) {
					final String errMsg = "修正計画(" + kaBaseKbName + ")UHが0未満";
					throw new ValidateException(new Conveyance(new MessageKey("DPC1008E", "修正計画(" + kaBaseKbName + ")UH", "0"), errMsg));
				}
			}

			// 計画値Pチェック
			String plannedValueP = data[4];
			if (StringUtils.isNotEmpty(plannedValueP)) {
				if (!ValidationUtil.isLong(plannedValueP)) {
					final String errMsg = "修正計画(" + kaBaseKbName + ")Pが半角数値以外";
					throw new ValidateException(new Conveyance(new MessageKey("DPC1004E", "修正計画(" + kaBaseKbName + ")P", "半角数値"), errMsg));
				}
				if (!ValidationUtil.isMaxLength(plannedValueP, 10)) {
					final String errMsg = "修正計画(" + kaBaseKbName + ")Pが10桁オーバー";
					throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "修正計画(" + kaBaseKbName + ")P", "10"), errMsg));
				}
				if (ConvertUtil.parseLong(plannedValueP) < 0) {
					final String errMsg = "修正計画(" + kaBaseKbName + ")Pが0未満";
					throw new ValidateException(new Conveyance(new MessageKey("DPC1008E", "修正計画(" + kaBaseKbName + ")P", "0"), errMsg));
				}
			}
		}
	}

}
