package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.exp.ErrMessageKey.*;
import static jp.co.takeda.a.web.bean.CorrespondType.*;
import static jp.co.takeda.security.BusinessType.*;
import static jp.co.takeda.security.DpAuthority.AuthType.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.MessageResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.action.MessageUtil;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.bean.Constants.TemplateInfo;
//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
import jp.co.takeda.dao.DpsKakuteiErrMsgDao;
//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dto.InsWsPlanJgiListResultDto;
import jp.co.takeda.dto.InsWsPlanJgiListUpdateDto;
import jp.co.takeda.dto.InsWsPlanMrResultDto;
import jp.co.takeda.dto.InsWsPlanTeamResultDto;
import jp.co.takeda.dto.ProgressEachKindInfoDto;
import jp.co.takeda.dto.ProgressEachKindInfoForVacDto;
import jp.co.takeda.dto.ProgressParamDto;
import jp.co.takeda.dto.ProgressParamForVacDto;
import jp.co.takeda.dto.ProgressPlanStatusDto;
import jp.co.takeda.dto.ProgressPlanStatusForVacDto;
import jp.co.takeda.dto.ProgressPlanWsStatusDto;
import jp.co.takeda.dto.ProgressPlanWsStatusForVacDto;
import jp.co.takeda.exp.UnallowableStatusException;
//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
import jp.co.takeda.model.DpsKakuteiErrMsg;
//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.view.SosStatusSummary;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcFileCreateService;
import jp.co.takeda.service.DpcUserSearchService;
import jp.co.takeda.service.DpsBusinessProgressSearchService;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.service.DpsContactOperationsService;
import jp.co.takeda.service.DpsInsWsPlanSearchService;
import jp.co.takeda.service.DpsInsWsPlanService;
//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
import jp.co.takeda.service.DpsKakuteiErrMsgSearchService;
//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
import jp.co.takeda.service.DpsReportForVacService;
import jp.co.takeda.service.DpsReportService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.cmn.bean.DpDispatchManager;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.in.dps.Dps000C00Form;

/**
 * Dps000C00(トップ画面)のアクションクラス
 *
 * @author tkawabata
 */
@Controller("Dps000C00Action")
public class Dps000C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps000C00Action.class);

	// -------------------------------
	// injection field
	// -------------------------------

	/**
	 * ユーザ情報を取得するサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpcUserSearchService")
	protected DpcUserSearchService dpcUserSearchService;

	/**
	 * お知らせ情報を取得するサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsContactOperationsService")
	protected DpsContactOperationsService dpsContactOperationsService;

	/**
	 * 業務進捗サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsBusinessProgressSearchService")
	protected DpsBusinessProgressSearchService dpsBusinessProgressSearchService;

	/**
	 * (医)帳票出力サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsReportService")
	protected DpsReportService dpsReportService;

	/**
	 * (ワ)帳票出力サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsReportForVacService")
	protected DpsReportForVacService dpsReportForVacService;

	/**
	 * 転送サービス
	 */
	@Autowired(required = true)
	@Qualifier("dispatchManager")
	protected DpDispatchManager dispatchManager;

	/**
	 * ファイル作成サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpcFileCreateService")
	protected DpcFileCreateService dpcFileCreateService;

	/**
	 * コードマスタ検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;


	/**
	 * ダウンロードファイル作成ディレクトリ
	 */
	@Autowired(required = true)
	@Qualifier("downloadFileTempDir")
	protected String downloadFileTempDir;


		/**
		 * 施設特約店別計画機能 検索系サービス
		 */
		@Autowired(required = true)
		@Qualifier("dpsInsWsPlanSearchService")
		protected DpsInsWsPlanSearchService dpsInsWsPlanSearchService;

	/**
	 * 施設特約店別計画機能 更新系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsWsPlanService")
	protected DpsInsWsPlanService dpsInsWsPlanService;

	/**
	 * 計画品目ＤＡＯ
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

// add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
	/**
	 * 一括確定エラー情報ＤＡＯ
	 */
	@Autowired(required = true)
	@Qualifier("dpsKakuteiErrMsgDao")
	protected DpsKakuteiErrMsgDao dpsKakuteiErrMsgDao;

	/**
	 * 一括確定エラー情報ＤＡＯ
	 */
	@Autowired(required = true)
	@Qualifier("dpsKakuteiErrMsgSearchService")
	protected DpsKakuteiErrMsgSearchService dpsKakuteiErrMsgSearchService;
// add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応

	// -------------------------------
	// action method
	// -------------------------------

	/**
	 * (医)初期表示時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	public Result dps000C00F00(DpContext ctx, Dps000C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps000C00F00");
			LOG.debug("defFlg=" + form.getDefFlg());
			LOG.debug("initDispFlg=" + form.getInitDispFlg());
			LOG.debug("settingSosCd=" + form.getSettingSosCd());
			LOG.debug("settingJgiNo=" + form.getSettingJgiNo());
			LOG.debug("redirectPath=" + form.getRedirectPath());
		}

		// 初期化処理
		form.formInit();

		// 全フォームの初期化
		formInitlize(ctx);

		// デフォルト組織従業員再設定処理
		dpcUserSearchService.changeDefaultSosJgi(form.getDefFlg(), form.getInitDispFlg(), form.getSettingSosCd(), form.getSettingJgiNo());

		// リダイレクト処理
		String rPath = form.getRedirectPath();
		if (StringUtils.isNotBlank(rPath)) {
			String path = dispatchManager.createRedirectPath(rPath, ctx.getRequest());
			dispatchManager.handleAsRedirect(path, ctx.getRequest(), ctx.getResponse());
		}
		form.reflash();
		return ActionResult.SUCCESS;
	}

	/**
	 * (医)お知らせ出力時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(isDownloadable = true)
	@RequestType(businessType = CMN)
	public Result dps000C00F01Output(DpContext ctx, Dps000C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps000C00F01Output");
		}
		final String templateRootPath = getRealPath(TemplateInfo.EXCEL_TEMPLATE_PATH.getValue()); // 絶対パスに変換
		final Long outputFileId = Long.parseLong(form.getAnnounceOutputFileId());
		final ExportResult exportResult = dpsReportService.outputDistMissList(templateRootPath, outputFileId);
		form.setExportResult(exportResult);
		form.setDownLoadFileName(exportResult.getName());
		return ActionResult.SUCCESS;
	}

	/**
	 * (医・ワ)お知らせ削除時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType(businessType = CMN)
	public Result dps000C00F02Execute(DpContext ctx, Dps000C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps000C00F02");
			LOG.debug("announceSeqKey=" + form.getAnnounceSeqKey());
			LOG.debug("announceUpDate=" + form.getAnnounceUpDate());
		}
		try {
			Long seqKey = ConvertUtil.parseLong(form.getAnnounceSeqKey());
			Date upDate = ConvertUtil.parseDate(form.getAnnounceUpDate());
			dpsContactOperationsService.deleteAnnounce(seqKey, upDate);

		} finally {
			form.reflash();
		}
		return ActionResult.SUCCESS;
	}

	/**
	 * 仕入  施設特約店計画 一括確定メソッド
	 * @param ctx
	 * @param form
	 * @return
	 * @throws Exception
	 */
	@ActionMethod
	@RequestType(businessType = CMN)
	public Result dps000C00F03Execute(DpContext ctx, Dps000C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps000C00F03Execute");
		}
//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
//		エラーテーブル情報のテーブル情報を削除(洗い替え処理の為、登録は各エラーチェック時に実施）
		dpsKakuteiErrMsgSearchService.delete();
//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応

		String siireCd = dpsCodeMasterSearchService.searchCodeByDataKbn(DpsCodeMaster.SII.getDbValue()).get(0).getDataCd();
		//組織一覧を取得
		ProgressPlanStatusDto resultDto = dpsBusinessProgressSearchService.searchProgressPlanStatus(siireCd);
		if(resultDto == null ) {
			return ActionResult.SUCCESS;
		}

		boolean hasError = false;
		// 品目 x 組織 で、施設特約店計画を取得 → insWsPlanUpdateDtoList
		for( SosStatusSummary sos:resultDto.getSosStatusSummaryList()) {

			List<PlannedProd> prods = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.IYAKU, siireCd, null);

			for( PlannedProd prod : prods) {
//				InsWsPlanJgiListResultDto insWsPlanJgiListResultDto = dpsInsWsPlanSearchService.searchMrList(new InsWsPlanJgiListScDto(sos.getSosCd(), null, prod.getProdCode()));
//				List<InsWsPlanJgiListUpdateDto> insWsPlanUpdateDtoList = convertViewDtoToUpdateDtoList(insWsPlanJgiListResultDto);
				// 以下メソッドを実行 品目 x 組織分 繰り返し
				try {
					dpsInsWsPlanService.updateStatusWithDiffCheck(sos.getSosCd(), prod.getProdCode());
				} catch (UnallowableStatusException e) {
					// 確定できない場合は、その理由が例外で通知される。
					// 営業所個別に確定する際は画面にて表示しているメッセージをここではあえて画面に渡さすログに出力している。
					// なお、既に確定済み とのメッセージはログに出力しない。
					MessageResources resources = this.getResources(ctx.getRequest());
					for (MessageKey messageKey :  e.getConveyance().getMessageKeyList()) {
						if(messageKey.getKey().equals("DPS3110E")) { // 既に確定済みというメッセージの場合は、エラーとみなさず、出力しない
							continue;
						}
						LOG.error("組織：" +  sos.getBumonSeiName()  +
								  " 品目："+ prod.getProdName() +
								  "[一括確定処理エラー]" + MessageUtil.getMessageString(resources, messageKey));
						hasError = true;
					}
//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
					for (DpsKakuteiErrMsg dpsKakuteiErrMsg :  e.getConveyance().getDpsKakuteiErrMsgList()) {
						dpsKakuteiErrMsgDao.insert(dpsKakuteiErrMsg.getSosCd(), dpsKakuteiErrMsg.getIsJgiNo(), dpsKakuteiErrMsg.getProdCode(), MessageUtil.getMessageString(resources, dpsKakuteiErrMsg.getMessageKey()));
					}
//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
				}
			}
		}
		form.setShiireFixAllFinished(true);
		form.setShiireFixAllFinishedWithError(hasError);

		return ActionResult.SUCCESS;

	}


	private List<InsWsPlanJgiListUpdateDto> convertViewDtoToUpdateDtoList(InsWsPlanJgiListResultDto insWsPlanJgiListResultDto) {

		List<InsWsPlanTeamResultDto> teamResultList = insWsPlanJgiListResultDto.getTeamResultList();
		List<InsWsPlanJgiListUpdateDto> resultList = new ArrayList<InsWsPlanJgiListUpdateDto>();
	    for( InsWsPlanTeamResultDto team :teamResultList) {
	    	for ( InsWsPlanMrResultDto mr :team.getMrList()) {
	    		InsWsPlanJgiListUpdateDto e = new InsWsPlanJgiListUpdateDto(mr.getInsWsPlanStatus().getJgiNo(), mr.getInsWsPlanStatus().getJgiName(),mr.getInsWsPlanStatus().getUpDate());
	    		resultList.add(e);
	    	}
	    }
		return resultList;
	}

//	/**
//	 * (医)代行ユーザ切替時に呼ばれるアクションメソッド<br>
//	 *
//	 * @param ctx Context
//	 * @param form ActionForm
//	 * @return 処理結果
//	 * @throws Exception 例外
//	 */
//	@ActionMethod
//	@RequestType
//	public Result dps000C00F10(DpContext ctx, Dps000C00Form form) throws Exception {
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("dps000C00F10");
//			LOG.debug("applyJgiNo=" + form.getApplyJgiNo());
//		}
//		Integer jgiNo = ConvertUtil.parseInteger(form.getApplyJgiNo());
//		dpcUserSearchService.switchUser(jgiNo, ctx.getDpMetaInfo());
//		return ActionResult.SUCCESS;
//	}

	/**
	 * (医)T/Y変換指定率および薬価改定率テーブルの取得[非同期処理]<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(correspondType = ASYNC)
	@RequestType
	public Result dps000C00F25(DpContext ctx, Dps000C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps000C00F25");
		}
		SysManage sm = DpUserInfo.getDpUserInfo().getSysManage();
		ProgressParamDto resultDto = dpsBusinessProgressSearchService.searchProgressParam(sm.getSysYear(), sm.getSysTerm());
		getRequestBox().put(Dps000C00Form.DPS000C00F25_DATA_R, resultDto);
		return ActionResult.SUCCESS;
	}

	/**
	 * (医)各種登録状況テーブルの取得[非同期処理]<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(correspondType = ASYNC)
	@RequestType
	public Result dps000C00F26(DpContext ctx, Dps000C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps000C00F26");
		}
		ProgressEachKindInfoDto resultDto = dpsBusinessProgressSearchService.searchProgressEachKindInfo();
		getRequestBox().put(Dps000C00Form.DPS000C00F26_DATA_R, resultDto);
		return ActionResult.SUCCESS;
	}

// mod start 2018/6/5 M.Wada J18-0002_【DPX】2018年4月組織変更対応（計画支援）	カテゴリごとに別れていたActionMethodを１つにまとめる
//	/**
//	 * (医)営業所計画－施設特約店別計画(MMP品)テーブルの取得[非同期処理]<br>
//	 *
//	 * @param ctx Context
//	 * @param form ActionForm
//	 * @return 処理結果
//	 * @throws Exception 例外
//	 */
//	@ActionMethod(correspondType = ASYNC)
//	@RequestType
//	public Result dps000C00F27(DpContext ctx, Dps000C00Form form) throws Exception {
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("dps000C00F27");
//		}
//		ProgressPlanMMPStatusDto resultDto = dpsBusinessProgressSearchService.searchProgressPlanMMPStatus();
//		getRequestBox().put(Dps000C00Form.DPS000C00F27_DATA_R, resultDto);
//		return ActionResult.SUCCESS;
//	}
//
//	/**
//	 * (医)営業所計画－施設特約店別計画(ONC品)テーブルの取得[非同期処理]<br>
//	 *
//	 * @param ctx Context
//	 * @param form ActionForm
//	 * @return 処理結果
//	 * @throws Exception 例外
//	 */
//	@ActionMethod(correspondType = ASYNC)
//	@RequestType
//	public Result dps000C00F30(DpContext ctx, Dps000C00Form form) throws Exception {
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("dps000C00F30");
//		}
//		ProgressPlanONCStatusDto resultDto = dpsBusinessProgressSearchService.searchProgressPlanONCStatus();
//		getRequestBox().put(Dps000C00Form.DPS000C00F30_DATA_R, resultDto);
//		return ActionResult.SUCCESS;
//	}
//
//// add start 2018/6/5 M.Wada J18-0002_【DPX】2018年4月組織変更対応（計画支援）
//	/**
//	 * (医)営業所計画－施設特約店別計画(SPBU品)テーブルの取得[非同期処理]<br>
//	 *
//	 * @param ctx Context
//	 * @param form ActionForm
//	 * @return 処理結果
//	 * @throws Exception 例外
//	 */
//	@ActionMethod(correspondType = ASYNC)
//	@RequestType
//	public Result dps000C00F31(DpContext ctx, Dps000C00Form form) throws Exception {
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("dps000C00F31");
//		}
//		ProgressPlanSPBUStatusDto resultDto = dpsBusinessProgressSearchService.searchProgressPlanSPBUStatus();
//		getRequestBox().put(Dps000C00Form.DPS000C00F31_DATA_R, resultDto);
//		return ActionResult.SUCCESS;
//	}
////add end   2018/6/5 M.Wada J18-0002_【DPX】2018年4月組織変更対応（計画支援）

	/**
	 * (医)営業所計画－施設特約店別計画(カテゴリ共通)テーブルの取得[非同期処理]<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(correspondType = ASYNC)
	@RequestType
	public Result dps000C00F20(DpContext ctx, Dps000C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps000C00F20");
		}
		ProgressPlanStatusDto resultDto = dpsBusinessProgressSearchService.searchProgressPlanStatus(form.getCategory());
		getRequestBox().put(Dps000C00Form.DPS000C00F20_DATA_R, resultDto);
		return ActionResult.SUCCESS;
	}

	/**
	 * (医)営業所計画－施設特約店別計画(仕入品(一般・麻薬))テーブルの取得[非同期処理]<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(correspondType = ASYNC)
	@RequestType
	public Result dps000C00F28(DpContext ctx, Dps000C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps000C00F28");
		}

		// DPS000C00s28.vmからカテゴリ：仕入が渡ってくるので、それを使用する
//		String siireCd = dpsCodeMasterSearchService.searchCodeByDataKbn(DpsCodeMaster.SII.getDbValue()).get(0).getDataCd();
		ProgressPlanStatusDto resultDto = dpsBusinessProgressSearchService.searchProgressPlanStatus(form.getCategory());
		getRequestBox().put(Dps000C00Form.DPS000C00F20_DATA_R, resultDto); //データは全カテゴリ共通のため"20"でOK
		return ActionResult.SUCCESS;
	}

// mod end 2018/6/5 M.Wada J18-0002_【DPX】2018年4月組織変更対応（計画支援）	カテゴリごとに別れていたActionMethodを１つにまとめる

	/**
	 * (医)特約店別計画テーブルの取得[非同期処理]<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(correspondType = ASYNC)
	@RequestType
	public Result dps000C00F29(DpContext ctx, Dps000C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps000C00F29");
		}
		ProgressPlanWsStatusDto resultDto = dpsBusinessProgressSearchService.searchProgressPlanWsStatus();
		getRequestBox().put(Dps000C00Form.DPS000C00F29_DATA_R, resultDto);
		return ActionResult.SUCCESS;
	}

	/**
	 * (ワ)初期表示時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType(businessType = VACCINE)
	public Result dps000C00F50(DpContext ctx, Dps000C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps000C00F50");
			LOG.debug("defFlg=" + form.getDefFlg());
			LOG.debug("initDispFlg=" + form.getInitDispFlg());
			LOG.debug("settingSosCd=" + form.getSettingSosCd());
			LOG.debug("settingJgiNo=" + form.getSettingJgiNo());
			LOG.debug("redirectPath=" + form.getRedirectPath());
		}

		// 初期化処理
		form.formInit();

		// 全フォームの初期化
		formInitlize(ctx);

		// デフォルト組織従業員再設定処理
		dpcUserSearchService.changeDefaultSosJgi(form.getDefFlg(), form.getInitDispFlg(), form.getSettingSosCd(), form.getSettingJgiNo());

		// リダイレクト処理
		String rPath = form.getRedirectPath();
		if (StringUtils.isNotBlank(rPath)) {
			String path = dispatchManager.createRedirectPath(rPath, ctx.getRequest());
			dispatchManager.handleAsRedirect(path, ctx.getRequest(), ctx.getResponse());
		}
		form.reflash();
		return ActionResult.SUCCESS;
	}

	/**
	 * (ワ)お知らせ出力時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(isDownloadable = true)
	@RequestType(businessType = CMN)
	public Result dps000C00F51Output(DpContext ctx, Dps000C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps000C00F51Output");
		}
		final String templateRootPath = getRealPath(TemplateInfo.EXCEL_TEMPLATE_PATH.getValue()); // 絶対パスに変換
		final Long outputFileId = Long.parseLong(form.getAnnounceOutputFileId());
		final ExportResult exportResult = dpsReportService.outputDistMissList(templateRootPath, outputFileId);
		form.setExportResult(exportResult);
		form.setDownLoadFileName(exportResult.getName());
		return ActionResult.SUCCESS;
	}

	/**
	 * (ワ)施設計画市区郡町村別集計結果出力時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(isDownloadable = true)
	@RequestType(businessType = VACCINE)
	@Permission( authType = OUTPUT)
	public Result dps000C00F52Output(DpContext ctx, Dps000C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps000C00F52Output");
		}
		final String templateRootPath = getRealPath(TemplateInfo.EXCEL_TEMPLATE_PATH.getValue()); // 絶対パスに変換
		final Integer insWsVacJgiNo = Integer.parseInt(form.getInsWsVacJgiNo());
		final ExportResult exportResult = dpsReportForVacService.outputInsWsCityList(templateRootPath, insWsVacJgiNo);
		form.setExportResult(exportResult);
		form.setDownLoadFileName(exportResult.getName());
		return ActionResult.SUCCESS;

	}

//	/**
//	 * (ワ)代行ユーザ切替時に呼ばれるアクションメソッド<br>
//	 *
//	 * @param ctx Context
//	 * @param form ActionForm
//	 * @return 処理結果
//	 * @throws Exception 例外
//	 */
//	@ActionMethod
//	@RequestType(businessType = VACCINE)
//	public Result dps000C00F60(DpContext ctx, Dps000C00Form form) throws Exception {
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("dps000C00F60");
//			LOG.debug("applyJgiNo=" + form.getApplyJgiNo());
//		}
//		Integer jgiNo = ConvertUtil.parseInteger(form.getApplyJgiNo());
//		dpcUserSearchService.switchUser(jgiNo, ctx.getDpMetaInfo());
//		return ActionResult.SUCCESS;
//	}

	/**
	 * (ワ)ＴＢ変換指定率テーブルの取得[非同期処理]<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(correspondType = ASYNC)
	@RequestType(businessType = VACCINE)
	public Result dps000C00F75(DpContext ctx, Dps000C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps000C00F75");
		}
		SysManage sm = DpUserInfo.getDpUserInfo().getSysManage();
		ProgressParamForVacDto resultDto = dpsBusinessProgressSearchService.searchProgressParamForVac(sm.getSysYear(), sm.getSysTerm());
		getRequestBox().put(Dps000C00Form.DPS000C00F75_DATA_R, resultDto);
		return ActionResult.SUCCESS;
	}

	/**
	 * (ワ)各種登録状況テーブルの取得[非同期処理]<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(correspondType = ASYNC)
	@RequestType(businessType = VACCINE)
	public Result dps000C00F76(DpContext ctx, Dps000C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps000C00F76");
		}
		ProgressEachKindInfoForVacDto resultDto = dpsBusinessProgressSearchService.searchProgressEachKindInfoForVac();
		getRequestBox().put(Dps000C00Form.DPS000C00F76_DATA_R, resultDto);
		return ActionResult.SUCCESS;
	}

	/**
	 * (ワ)施設特約店別計画テーブルの取得[非同期処理]<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(correspondType = ASYNC)
	@RequestType(businessType = VACCINE)
	public Result dps000C00F77(DpContext ctx, Dps000C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps000C00F77");
		}
		ProgressPlanStatusForVacDto resultDto = dpsBusinessProgressSearchService.searchProgressPlanStatusForVac();
		getRequestBox().put(Dps000C00Form.DPS000C00F77_DATA_R, resultDto);
		return ActionResult.SUCCESS;
	}

	/**
	 * (ワ)特約店別計画テーブルの取得[非同期処理]<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(correspondType = ASYNC)
	@RequestType(businessType = VACCINE)
	public Result dps000C00F78(DpContext ctx, Dps000C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps000C00F78");
		}
		ProgressPlanWsStatusForVacDto resultDto = dpsBusinessProgressSearchService.searchProgressPlanWsStatusForVac();
		getRequestBox().put(Dps000C00Form.DPS000C00F78_DATA_R, resultDto);
		return ActionResult.SUCCESS;
	}

	// -------------------------------
	// validation method
	// -------------------------------

	/**
	 * (医)お知らせ出力時に呼ばれるバリデーションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws ValidateException
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps000C00F01Validation(DpContext ctx, Dps000C00Form form) throws ValidateException {
		announceOutputFileIdValidation(form);
	}

	/**
	 * (ワ)お知らせ出力時に呼ばれるバリデーションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws ValidateException
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps000C00F51Validation(DpContext ctx, Dps000C00Form form) throws ValidateException {
		announceOutputFileIdValidation(form);
	}

	/**
	 * お知らせ情報の出力ファイル情報IDが指定されているかを検証する。
	 *
	 * @param form ActionForm
	 * @throws ValidateException
	 */
	private void announceOutputFileIdValidation(Dps000C00Form form) throws ValidateException {
		if (StringUtils.isEmpty(form.getAnnounceOutputFileId()) || !ValidationUtil.isLong(form.getAnnounceOutputFileId())) {
			final String errMsg = "受信パラメータ(出力ファイル情報ID)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
	}
}
