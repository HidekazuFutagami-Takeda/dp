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
import jp.co.takeda.dto.SosMonthPlanResultDto;
import jp.co.takeda.dto.SosMonthPlanUpdateDto;
import jp.co.takeda.dto.SosPlanScDto;
import jp.co.takeda.model.Cal;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.BumonRank;
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
import jp.co.takeda.service.DpcSystemManageSearchService;
import jp.co.takeda.service.DpmBranchMonthPlanSearchService;
import jp.co.takeda.service.DpmBranchMonthPlanService;
import jp.co.takeda.service.DpmCodeMasterSearchService;
import jp.co.takeda.service.DpmCommonService;
import jp.co.takeda.service.DpmMrMonthPlanSearchService;
import jp.co.takeda.service.DpmMrMonthPlanService;
import jp.co.takeda.service.DpmOfficeMonthPlanSearchService;
import jp.co.takeda.service.DpmOfficeMonthPlanService;
import jp.co.takeda.service.DpmPlannedCtgSearchService;
import jp.co.takeda.service.DpmReportService;
import jp.co.takeda.service.DpmSosCtgSearchService;
import jp.co.takeda.service.DpmSosJgiSearchService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.in.dpm.Dpm500C00Form;

/**
 * Dpm500C00(組織・担当者別月別計画編集画面)のアクションクラス
 *
 * @author stakeuchi
 */
@Controller("Dpm500C00Action")
public class Dpm500C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dpm500C00Action.class);

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPM500C00";

	/**
	 * 計画（1：期別、2：月別）
	 */
	private static final String PLAN_ID = "2";

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 組織別月別計画(支店) 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmBranchMonthPlanSearchService")
	protected DpmBranchMonthPlanSearchService dpmBranchMonthPlanSearchService;

	/**
	 * 組織別月別計画(営業所) 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmOfficeMonthPlanSearchService")
	protected DpmOfficeMonthPlanSearchService dpmOfficeMonthPlanSearchService;

	/**
	 * 組織別月別計画(担当者) 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmMrMonthPlanSearchService")
	protected DpmMrMonthPlanSearchService dpmMrMonthPlanSearchService;

	/**
	 * 組織別計画(支店) 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmBranchMonthPlanService")
	protected DpmBranchMonthPlanService dpmBranchMonthPlanService;

	/**
	 * 組織別計画(営業所) 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmOfficeMonthPlanService")
	protected DpmOfficeMonthPlanService dpmOfficeMonthPlanService;

	/**
	 * 組織別計画(担当者) 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmMrMonthPlanService")
	protected DpmMrMonthPlanService dpmMrMonthPlanService;

	/**
	 * 組織従業員検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmSosJgiSearchService")
	protected DpmSosJgiSearchService dpmSosJgiSearchService;

	/**
	 * 帳票サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmReportService")
	DpmReportService dpmReportService;

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
	 * 納入計画管理検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpcSystemManageSearchService")
	protected DpcSystemManageSearchService systemManageSearchService;

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
	public Result dpm500C00F00(DpContext ctx, Dpm500C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm500C00F00");
		}

		// 初期化処理
		form.formInit();

		// ログインユーザの組織情報をセット
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {
				form.setSosCd2(null);
				form.setSosCd3(null);
				form.setSosCd4(null);
				form.setOncSosFlg(true);

				// 権限確認コード
				if (user.isSosLvl(SCREEN_ID, SosLvl.BRANCH)) {
					form.setSosCd2(user.getSosCd2());
				} else if (user.isSosLvl(SCREEN_ID, SosLvl.OFFICE)) {
					form.setSosCd2(user.getSosCd2());
					form.setSosCd3(user.getSosCd3());
				}
				else if (user.isSosLvl(SCREEN_ID, SosLvl.MR)) {
					form.setSosCd2(user.getSosCd2());
					form.setSosCd3(user.getSosCd3());
					if (user.getJgiNo() != null) {
						form.setMrJgiNo(String.valueOf(user.getJgiNo()));
					}
				}
			}
		}

		// カテゴリリストを初期設定する
		initCategoryList(form);
		// グリッドのヘッダを設定
		setGridHeader(form);
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
	public Result dpm500C00F01(DpContext ctx, Dpm500C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm500C00F01");
			LOG.debug("topSosCd:" + form.getTopSosCd());
			LOG.debug("topBumonRank:" + form.getTopBumonRank());
		}

		// 初期化
		form.setSosCd2(null);
		form.setSosCd3(null);
		form.setSosCd4(null);
		form.setEtcSosFlg(false);
		form.setOncSosFlg(true);
		form.setProdCategory(null);

		// 支店を設定
		if (String.valueOf(BumonRank.SITEN_TOKUYAKUTEN_BU.getDbValue()).equals(form.getTopBumonRank())) {
			form.setSosCd2(form.getTopSosCd());
		// 営業所を設定
		} else if (String.valueOf(BumonRank.OFFICE_TOKUYAKUTEN_G.getDbValue()).equals(form.getTopBumonRank())) {
			form.setSosCd3(form.getTopSosCd());
		}

		// カテゴリリストを初期設定する
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
	public Result dpm500C00F05(DpContext ctx, Dpm500C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm500C00F05");
			LOG.debug("SosCd2:" + form.getSosCd2());
			LOG.debug("SosCd3:" + form.getSosCd3());
			LOG.debug("SosCd4:" + form.getSosCd4());
			LOG.debug("ProdCategory:" + form.getProdCategory());
			LOG.debug("ProdCode:" + form.getProdCode());
		}

		// カテゴリリストを初期設定する
		initCategoryList(form);
		// グリッドのヘッダを設定
		setGridHeader(form);
		// 検索実行
		form.setTranField();
		searchSosPlanList(form);


		// 編集可能リストを設定
		Cal today = dpmCommonService.searchToday();
		String calYear = today.getCalYear();
		String calMonth = today.getCalMonth();
        //add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		form.setTougetuCount(TougetuHanteiJisseki());
        //add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		form.setEnableEdit(DpmMonthlyPlanHelper.getEnableEdit(calYear,calMonth));
		return ActionResult.SUCCESS;
	}

	/**
	 * 支店計画登録時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dpm500C00F10Execute(DpContext ctx, Dpm500C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm500C00F10Execute");
			LOG.debug("SosCd2:" + form.getSosCd2());
			LOG.debug("SosCd3:" + form.getSosCd3());
			LOG.debug("SosCd4:" + form.getSosCd4());
			LOG.debug("ProdCategory:" + form.getProdCategory());
			LOG.debug("ProdCode:" + form.getProdCode());
		}
		try {
			// 更新DTO
			List<SosMonthPlanUpdateDto> updateDtoList = form.convertSosPlanUpdateDto();

			// 更新実行
			int result = 0;
			if (!updateDtoList.isEmpty()) {
				result = dpmBranchMonthPlanService.updateBranchPlan(SCREEN_ID, updateDtoList);
			}

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(result)));

		} finally {
			// 再検索実行
			searchSosPlanList(form);
		}
		return ActionResult.SUCCESS;
	}

	/**
	 * 営業所計画登録時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dpm500C00F11Execute(DpContext ctx, Dpm500C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm500C00F11Execute");
			LOG.debug("SosCd2:" + form.getSosCd2());
			LOG.debug("SosCd3:" + form.getSosCd3());
			LOG.debug("SosCd4:" + form.getSosCd4());
			LOG.debug("ProdCategory:" + form.getProdCategory());
			LOG.debug("ProdCode:" + form.getProdCode());
		}
		try {
			// 更新DTO
			List<SosMonthPlanUpdateDto> updateDtoList = form.convertSosPlanUpdateDto();

			// 更新実行
			int result = 0;
			if (!updateDtoList.isEmpty()) {
				result = dpmOfficeMonthPlanService.updateOfficePlan(SCREEN_ID, updateDtoList);
			}

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(result)));

		} finally {
			// 再検索実行
			searchSosPlanList(form);
		}
		return ActionResult.SUCCESS;
	}

	/**
	 * 担当者計画登録時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dpm500C00F13Execute(DpContext ctx, Dpm500C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm500C00F13Execute");
			LOG.debug("SosCd2:" + form.getSosCd2());
			LOG.debug("SosCd3:" + form.getSosCd3());
			LOG.debug("SosCd4:" + form.getSosCd4());
			LOG.debug("ProdCategory:" + form.getProdCategory());
			LOG.debug("ProdCode:" + form.getProdCode());
		}
		try {
			// 更新DTO
			List<SosMonthPlanUpdateDto> updateDtoList = form.convertSosPlanUpdateDto();

			// 更新実行
			int result = 0;
			if (!updateDtoList.isEmpty()) {
				result = dpmMrMonthPlanService.updateMrPlan(SCREEN_ID, updateDtoList);
			}

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(result)));

		} finally {
			// 再検索実行
			searchSosPlanList(form);
		}
		return ActionResult.SUCCESS;
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
	public Result dpm500C00F14Output(DpContext ctx, Dpm500C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm500C00F14Output");
		}

		// 帳票出力準備
		SosPlanScDto sosPlanScDto = form.convertSosPlanScDto();
		String templateRootPath = getRealPath(TemplateInfo.EXCEL_TEMPLATE_PATH.getValue());

        //add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		form.setTougetuCount(TougetuHanteiJisseki());
        //add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示

		// 帳票出力実行
		//mod Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		//ExportResult exportResult = dpmReportService.outputSosMonthPlanList(templateRootPath, sosPlanScDto, form.getMrJgiNo());
		ExportResult exportResult = dpmReportService.outputSosMonthPlanList(templateRootPath, sosPlanScDto, form.getMrJgiNo(), form.getTougetuCount());
		//mod End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		form.setExportResult(exportResult);
		form.setDownLoadFileName(exportResult.getName());

		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理の実行
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchSosPlanList(Dpm500C00Form form) throws Exception {

		// 組織別計画の検索実行
		SosPlanScDto scDto = form.convertSosPlanScDto();

		// 計画レベル:組織別では選択された組織の下位組織計画を表示するので、立案レベルも１つ下を使用する
		try {

			SosMonthPlanResultDto resultDto = null;
			if (scDto.getSosCd3() != null) {

				// 組織コード(営業所)が設定されている場合、担当者別計画一覧を作成
				resultDto = dpmMrMonthPlanSearchService.searchSosPlan(scDto, form.getMrJgiNo());

			} else if (scDto.getSosCd2() != null) {

				// 組織コード(支店)が設定されている場合、営業所別計画一覧を作成
				resultDto = dpmOfficeMonthPlanSearchService.searchSosPlan(scDto);

			} else {

				// 組織コードが設定されていない場合、支店別計画一覧を作成
				resultDto = dpmBranchMonthPlanSearchService.searchSosPlan(scDto);
			}

			// リクエストボックスに検索結果をセット
			super.getRequestBox().put(Dpm500C00Form.DPM500C00_DATA_R_SEARCH_RESULT, resultDto);

			// 部門ランクフラグをリクエストボックスにセット
			DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
			boolean bumonFlag = false;
			if (userInfo != null) {
				DpUser user = userInfo.getSettingUser();
				if (user != null) {

					if (scDto.getSosCd4() != null) {
						// 組織コード4が設定されている場合、全ロール(本部・支店・営業所)OK
						bumonFlag = true;
					} else if (scDto.getSosCd3() != null) {
						// 組織コード3が設定されている場合、本部・支店OK
						if (user.isMatch(JokenSet.HONBU, JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF)) {
							bumonFlag = true;
						}
					} else if (scDto.getSosCd2() != null) {
						// 組織コード2が設定されている場合、本部のみOK
						if (user.isMatch(JokenSet.HONBU)) {
							bumonFlag = true;
						}
					}
				}
			}

			super.getRequestBox().put(Dpm500C00Form.DPM500C00_DATA_R_BUMON_FLAG, bumonFlag);

		} finally {
			// 実行した検索条件を画面に設定
			form.setViewField();
		}
	}

	/**
	 * カテゴリリストの初期設定
	 * @param form ActionForm
	 */
	private void initCategoryList(Dpm500C00Form form) {
		// 汎用マスタのカテゴリリスト
		List<DpmCCdMst> categoryList = (dpmCodeMasterSearchService.searchCategorylist(null, PLAN_ID));
		// カテゴリリストに表示するリスト
		List<String> targetCategoryAry = new ArrayList<String>();
		// 計画対象カテゴリ領域の設定
		List<String> planLvCtgList = new ArrayList<String>();
		if (StringUtils.isNotBlank(form.getSosCd3())) {
			// 計画立案レベル-担当者
			planLvCtgList = dpmPlannedCtgSearchService.searchCategoryByPlanLevel(ProdPlanLevel.MR);
		} else if(StringUtils.isNotBlank(form.getSosCd2())) {
			// 計画立案レベル-営業所
			planLvCtgList = dpmPlannedCtgSearchService.searchCategoryByPlanLevel(ProdPlanLevel.OFFICE);
		} else {
			// 計画立案レベル-支店
			planLvCtgList = dpmPlannedCtgSearchService.searchCategoryByPlanLevel(ProdPlanLevel.SITEN);
		}
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

		for (SosCtg ctg : ctgList) {
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

	/**
	 * グリッドのヘッダを設定する
	 * @param form
	 */
	private void setGridHeader(Dpm500C00Form form) {

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

		// 編集可不可判定用の年度を設定
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

//		List<String> monthList = new ArrayList<String>();
		List<String> monthList = new ArrayList<String>();
		//各月
//		for (int count = 0; count < Month.PERIOD_MONTH; count++){
		for (int count = 0; count < Month.PERIOD_MONTH; count++){
//			headerDetail = headerDetail.concat(",,,,,,,,");
			headerDetail = headerDetail.concat(",,,,,,,,,");
//			headerSum = headerSum.concat(",#cspan,#cspan,#cspan,#cspan,#cspan,#cspan,#cspan,#cspan");
			headerSum = headerSum.concat(",#cspan,#cspan,#cspan,#cspan,#cspan,#cspan,#cspan,#cspan,#cspan");
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
				// 編集可不可判定用の月リストを設定
//				monthList.add(Integer.toString(month -  Month.YEAR_MONTH));
				monthList.add(Integer.toString(month -  Month.YEAR_MONTH));
//			}else {
			}else {
//				attachHeader1row = attachHeader1row.concat(Integer.toString(month));
				attachHeader1row = attachHeader1row.concat(Integer.toString(month));
				// 編集可不可判定用の月リストを設定
//				monthList.add(Integer.toString(month));
				monthList.add(Integer.toString(month));
//			}
			}
//			attachHeader1row = attachHeader1row.concat("月,#cspan,#cspan,#cspan,#cspan,#cspan,#cspan,#cspan");
			attachHeader1row = attachHeader1row.concat("月,#cspan,#cspan,#cspan,#cspan,#cspan,#cspan,#cspan,#cspan");
//			attachHeader2row = attachHeader2row.concat(",月初計画,#cspan,#cspan,#cspan,月末見込,#cspan,#cspan,#cspan");
//			if(dpmCodeMasterSearchService.isVaccine(form.getProdCategory())) {
			if(dpmCodeMasterSearchService.isVaccine(form.getProdCategory())) {
				attachHeader2row = attachHeader2row.concat(",月初計画,#cspan,#cspan,#cspan,月末見込,#cspan,#cspan,#cspan,実績(B)");
//				attachHeader3row = attachHeader3row.concat(",積上(B),遂行率(積上),計画(B),遂行率(計画),積上(B),遂行率(積上),見込(B),遂行率(見込)");
				attachHeader3row = attachHeader3row.concat(",積上(B),遂行率(積上),計画(B),遂行率(計画),積上(B),遂行率(積上),見込(B),遂行率(見込),#rspan");
//			}else {
			}else {
				attachHeader2row = attachHeader2row.concat(",月初計画,#cspan,#cspan,#cspan,月末見込,#cspan,#cspan,#cspan,実績(Y)");
//				attachHeader3row = attachHeader3row.concat(",積上(Y),遂行率(積上),計画(Y),遂行率(計画),積上(Y),遂行率(積上),見込(Y),遂行率(見込)");
				attachHeader3row = attachHeader3row.concat(",積上(Y),遂行率(積上),計画(Y),遂行率(計画),積上(Y),遂行率(積上),見込(Y),遂行率(見込),#rspan");
//			}
			}
//			initWidths = initWidths.concat(",90,90,90,90,90,90,90,90,90");
			initWidths = initWidths.concat(",90,90,90,90,90,90,90,90,90");
//			colAlign = colAlign.concat(",right,right,right,right,right,right,right,right,right");
			colAlign = colAlign.concat(",right,right,right,right,right,right,right,right,right");
//			colTypes = colTypes.concat(",ron,ron,ron,ron,ron,ron,ron,ron,ron");
			colTypes = colTypes.concat(",ron,ron,ron,ron,ron,ron,ron,ron,ron");
//			enableResizing = enableResizing.concat(",false,false,false,false,false,false,false,false,false");
			enableResizing = enableResizing.concat(",false,false,false,false,false,false,false,false,false");
//			enableTooltips = enableTooltips.concat(",false,false,false,false,false,false,false,false,false");
			enableTooltips = enableTooltips.concat(",false,false,false,false,false,false,false,false,false");
//		}
		}
// mod Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

		form.setMonthList(monthList);

		//合計
		headerDetail = headerDetail.concat(",,,,,,,,");
		headerSum = headerSum.concat(",#cspan,#cspan,#cspan,#cspan,#cspan,#cspan,#cspan,#cspan");
		attachHeader1row = attachHeader1row.concat(",合計,#cspan,#cspan,#cspan,#cspan,#cspan,#cspan,#cspan");
		attachHeader2row = attachHeader2row.concat(",月初計画,#cspan,#cspan,#cspan,月末見込,#cspan,#cspan,#cspan");
		if(dpmCodeMasterSearchService.isVaccine(form.getProdCategory())) {
			attachHeader3row = attachHeader3row.concat(",積上(B),遂行率(積上),計画(B),遂行率(計画),積上(B),遂行率(積上),見込(B),遂行率(見込)");
		}else {
			attachHeader3row = attachHeader3row.concat(",積上(Y),遂行率(積上),計画(Y),遂行率(計画),積上(Y),遂行率(積上),見込(Y),遂行率(見込)");
		}
		initWidths = initWidths.concat(",90,90,90,90,90,90,90,90,90");
		colAlign = colAlign.concat(",right,right,right,right,right,right,right,right,right");
		colTypes = colTypes.concat(",ron,ron,ron,ron,ron,ron,ron,ron,ron");
		enableResizing = enableResizing.concat(",false,false,false,false,false,false,false,false,false");
		enableTooltips = enableTooltips.concat(",false,false,false,false,false,false,false,false,false");

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
	 * 編集可否リストを返す
	 * 当月と、当月より未来の月を編集可能とする
	 * @return 半期の編集可否リスト
	 */
//	private List<Boolean> getEnableEdit(String calYear,String calMonth) {
//
//		Boolean[] allDisable = { false, false, false, false, false, false };
//		List<Boolean> ALL_DISABLE = Arrays.asList(allDisable);
//
//		int calYearInt = Integer.parseInt(calYear);
//		int calMonthInt = Integer.parseInt(calMonth);
//
//		// 年度・期の設定
//		SysManage sysManage = DpUserInfo.getDpUserInfo().getSysManage();
//
//		String sysYear = sysManage.getSysYear();
//		int sysYearInt = Integer.parseInt(sysYear);
//		Term sysTerm = sysManage.getSysTerm();
//
//		int falseCount = 6;
//
//		switch (sysTerm) {
//		case FIRST: // 4,5,6,7,8,9
//			if(!sysYear.equals(calYear)) {
//				return ALL_DISABLE;
//
//			}
//			falseCount = calMonthInt - 4;
//			break;
//
//		case SECOND:// 10,11,12,1,2,3
//			Integer.parseInt(sysYear);
//			if(calYearInt == sysYearInt || calYearInt == sysYearInt + 1) {
//				/* do nothing */
//			}else {
//				return ALL_DISABLE;
//			}
//			if (calMonthInt < 10) calMonthInt = calMonthInt + 12;
//			falseCount = calMonthInt - 10;
//			break;
//
//		default:
//			return ALL_DISABLE;
//		}
//
//		Boolean[] editableFlags = { true, true, true, true, true, true };
//
//		for (int i = 0; i < falseCount; i++) {
//			editableFlags[i] = false;
//		}
//		return Arrays.asList(editableFlags);
//
//	}



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
	public void dpm500C00F05Validation(DpContext ctx, Dpm500C00Form form) throws ValidateException {
		// [必須] 品目カテゴリ
		if (StringUtils.isEmpty(form.getProdCategory())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "カテゴリ")));
		}
		// [必須] 品目コード
		if (StringUtils.isEmpty(form.getProdCode())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "品目")));
		}
	}

	/**
	 * 支店計画登録時の入力パラメータチェック
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dpm500C00F10Validation(DpContext ctx, Dpm500C00Form form) throws ValidateException {
		executeValidation(ctx, form);
	}

	/**
	 * 営業所計画登録時の入力パラメータチェック
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dpm500C00F11Validation(DpContext ctx, Dpm500C00Form form) throws ValidateException {
		executeValidation(ctx, form);
	}

	/**
	 * チーム計画登録時の入力パラメータチェック
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dpm500C00F12Validation(DpContext ctx, Dpm500C00Form form) throws ValidateException {
		executeValidation(ctx, form);
	}

	/**
	 * 担当者計画登録時の入力パラメータチェック
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dpm500C00F13Validation(DpContext ctx, Dpm500C00Form form) throws ValidateException {
		executeValidation(ctx, form);
	}

	/**
	 * 出力時の入力パラメータチェック
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dpm500C00F14Validation(DpContext ctx, Dpm500C00Form form) throws ValidateException {
		dpm500C00F05Validation(ctx, form);
	}

	/**
	 * 登録処理時の共通Validationメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	private void executeValidation(DpContext ctx, Dpm500C00Form form) throws ValidateException {
		// 更新パラーメータチェック
		String[] rowIdList = form.getRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		for (int i = 0; i < rowIdList.length; i++) {
			String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);

			// パラーメータのサイズチェック
			if (rowId.length != 29) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 組織コード
			if (StringUtils.isNotEmpty(rowId[0]) && !ValidationUtil.isLong(rowId[0])) {
				final String errMsg = "受信パラメータ(組織コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 従業員番号
			if (StringUtils.isNotEmpty(rowId[1]) && !ValidationUtil.isLong(rowId[1])) {
				final String errMsg = "受信パラメータ(従業員番号)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 品目コード
			if (StringUtils.isEmpty(rowId[2]) || !ValidationUtil.isLong(rowId[2])) {
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
			// 更新前月初計画1
			if (StringUtils.isNotEmpty(rowId[5]) && !ValidationUtil.isLong(rowId[5])) {
				final String errMsg = "受信パラメータ(月初計画1(更新前))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新前月末見込1
			if (StringUtils.isNotEmpty(rowId[6]) && !ValidationUtil.isLong(rowId[6])) {
				final String errMsg = "受信パラメータ(月末見込1(更新前))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新前月初計画2
			if (StringUtils.isNotEmpty(rowId[7]) && !ValidationUtil.isLong(rowId[7])) {
				final String errMsg = "受信パラメータ(月初計画2(更新前))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新前月末見込2
			if (StringUtils.isNotEmpty(rowId[8]) && !ValidationUtil.isLong(rowId[8])) {
				final String errMsg = "受信パラメータ(月末見込2(更新前))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新前月初計画3
			if (StringUtils.isNotEmpty(rowId[9]) && !ValidationUtil.isLong(rowId[9])) {
				final String errMsg = "受信パラメータ(月初計画3(更新前))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新前月末見込3
			if (StringUtils.isNotEmpty(rowId[10]) && !ValidationUtil.isLong(rowId[10])) {
				final String errMsg = "受信パラメータ(月末見込3(更新前))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新前月初計画4
			if (StringUtils.isNotEmpty(rowId[11]) && !ValidationUtil.isLong(rowId[11])) {
				final String errMsg = "受信パラメータ(月初計画4(更新前))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新前月末見込4
			if (StringUtils.isNotEmpty(rowId[12]) && !ValidationUtil.isLong(rowId[12])) {
				final String errMsg = "受信パラメータ(月末見込4(更新前))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新前月初計画5
			if (StringUtils.isNotEmpty(rowId[13]) && !ValidationUtil.isLong(rowId[13])) {
				final String errMsg = "受信パラメータ(月初計画5(更新前))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新前月末見込5
			if (StringUtils.isNotEmpty(rowId[14]) && !ValidationUtil.isLong(rowId[14])) {
				final String errMsg = "受信パラメータ(月末見込5(更新前))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新前月初計画6
			if (StringUtils.isNotEmpty(rowId[15]) && !ValidationUtil.isLong(rowId[15])) {
				final String errMsg = "受信パラメータ(月初計画6(更新前))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新前月末見込6
			if (StringUtils.isNotEmpty(rowId[16]) && !ValidationUtil.isLong(rowId[16])) {
				final String errMsg = "受信パラメータ(月末見込6(更新前))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新後月初計画1
			if (StringUtils.isNotEmpty(rowId[17]) && !ValidationUtil.isLong(rowId[17])) {
				final String errMsg = "受信パラメータ(月初計画1(更新後))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新後月末見込1
			if (StringUtils.isNotEmpty(rowId[18]) && !ValidationUtil.isLong(rowId[18])) {
				final String errMsg = "受信パラメータ(月末見込1(更新後))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新後月初計画2
			if (StringUtils.isNotEmpty(rowId[19]) && !ValidationUtil.isLong(rowId[19])) {
				final String errMsg = "受信パラメータ(月初計画1(更新後))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新後月末見込2
			if (StringUtils.isNotEmpty(rowId[20]) && !ValidationUtil.isLong(rowId[20])) {
				final String errMsg = "受信パラメータ(月末見込2(更新後))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新後月初計画3
			if (StringUtils.isNotEmpty(rowId[21]) && !ValidationUtil.isLong(rowId[21])) {
				final String errMsg = "受信パラメータ(月初計画3(更新後))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新後月末見込3
			if (StringUtils.isNotEmpty(rowId[22]) && !ValidationUtil.isLong(rowId[22])) {
				final String errMsg = "受信パラメータ(月末見込3(更新後))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新後月初計画4
			if (StringUtils.isNotEmpty(rowId[23]) && !ValidationUtil.isLong(rowId[23])) {
				final String errMsg = "受信パラメータ(月初計画4(更新後))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新後月末見込4
			if (StringUtils.isNotEmpty(rowId[24]) && !ValidationUtil.isLong(rowId[24])) {
				final String errMsg = "受信パラメータ(月末見込4(更新後))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新後月初計画5
			if (StringUtils.isNotEmpty(rowId[25]) && !ValidationUtil.isLong(rowId[25])) {
				final String errMsg = "受信パラメータ(月初計画5(更新後))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新後月末見込5
			if (StringUtils.isNotEmpty(rowId[26]) && !ValidationUtil.isLong(rowId[26])) {
				final String errMsg = "受信パラメータ(月末見込5(更新後))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新後月初計画6
			if (StringUtils.isNotEmpty(rowId[27]) && !ValidationUtil.isLong(rowId[27])) {
				final String errMsg = "受信パラメータ(月初計画6(更新後))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新後月末見込6
			if (StringUtils.isNotEmpty(rowId[28]) && !ValidationUtil.isLong(rowId[28])) {
				final String errMsg = "受信パラメータ(月末見込6(更新後))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
		}
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
