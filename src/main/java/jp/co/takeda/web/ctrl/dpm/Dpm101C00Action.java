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
import jp.co.takeda.dto.ManagePlanUpdateResultDto;
import jp.co.takeda.dto.ProdPlanResultDto;
import jp.co.takeda.dto.ProdPlanScDto;
import jp.co.takeda.dto.SosPlanUpdateDto;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.DispInsKbn;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.model.view.SosCtg;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpmBranchPlanSearchService;
import jp.co.takeda.service.DpmBranchPlanService;
import jp.co.takeda.service.DpmCodeMasterSearchService;
import jp.co.takeda.service.DpmIyakuPlanSearchService;
import jp.co.takeda.service.DpmIyakuPlanService;
import jp.co.takeda.service.DpmMrPlanSearchService;
import jp.co.takeda.service.DpmMrPlanService;
import jp.co.takeda.service.DpmOfficePlanSearchService;
import jp.co.takeda.service.DpmOfficePlanService;
import jp.co.takeda.service.DpmPlannedCtgSearchService;
import jp.co.takeda.service.DpmReportService;
import jp.co.takeda.service.DpmSosCtgSearchService;
import jp.co.takeda.service.DpmSosJgiSearchService;
import jp.co.takeda.service.DpmTeamPlanSearchService;
import jp.co.takeda.service.DpmTeamPlanService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.in.dpm.Dpm101C00Form;

/**
 * Dpm101C00((医)品目別計画編集画面)のアクションクラス
 *
 * @author stakeuchi
 */
@Controller("Dpm101C00Action")
public class Dpm101C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dpm101C00Action.class);

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPM101C00";

	/**
	 * 計画（1：期別、2：月別）
	 */
	private static final String PLAN_ID = "1";

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 組織別計画(全社) 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmIyakuPlanSearchService")
	protected DpmIyakuPlanSearchService dpmIyakuPlanSearchService;

	/**
	 * 組織別計画(支店) 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmBranchPlanSearchService")
	protected DpmBranchPlanSearchService dpmBranchPlanSearchService;

	/**
	 * 組織別計画(営業所) 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmOfficePlanSearchService")
	protected DpmOfficePlanSearchService dpmOfficePlanSearchService;

	/**
	 * 組織別計画(チーム) 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmTeamPlanSearchService")
	protected DpmTeamPlanSearchService dpmTeamPlanSearchService;

	/**
	 * 組織別計画(担当者) 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmMrPlanSearchService")
	protected DpmMrPlanSearchService dpmMrPlanSearchService;

	/**
	 * 組織別計画(全社) 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmIyakuPlanService")
	protected DpmIyakuPlanService dpmIyakuPlanService;

	/**
	 * 組織別計画(支店) 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmBranchPlanService")
	protected DpmBranchPlanService dpmBranchPlanService;

	/**
	 * 組織別計画(営業所) 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmOfficePlanService")
	protected DpmOfficePlanService dpmOfficePlanService;

	/**
	 * 組織別計画(チーム) 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmTeamPlanService")
	protected DpmTeamPlanService dpmTeamPlanService;

	/**
	 * 組織別計画(担当者) 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmMrPlanService")
	protected DpmMrPlanService dpmMrPlanService;

	/**
	 * 組織従業員検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmSosJgiSearchService")
	protected DpmSosJgiSearchService dpmSosJgiSearchService;

	/**
	 * コードマスタデータ取得サービス
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
	public Result dpm101C00F00(DpContext ctx, Dpm101C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm101C00F00");
		}

		// フォーム情報クリア
		form.formInit();

		// ログインユーザの組織情報をセット
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {
				form.setSosCd2(null);
				form.setSosCd3(null);
				form.setSosCd4(null);
				form.setJgiNo(null);

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
		// カテゴリリストを初期設定する
		initCategoryList(form);
		setTitle(form);
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
	public Result dpm101C00F01(DpContext ctx, Dpm101C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm101C00F01");
			LOG.debug("SosCd2:" + form.getSosCd2());
			LOG.debug("SosCd3:" + form.getSosCd3());
			LOG.debug("SosCd4:" + form.getSosCd4());
			LOG.debug("isSearchFlg:" + form.isSearchFlg());
		}
		// 初期化
		form.setSosCd2(null);
		form.setSosCd3(null);
		form.setSosCd4(null);
		form.setJgiNo(null);
		form.setEtcSosFlg(false);
		form.setOncSosFlg(false);

		// 再設定
		if (StringUtils.isNotBlank(form.getTopSosCd())) {
			SosMst sosMst = dpmSosJgiSearchService.searchSosMstWithEtcSos(form.getTopSosCd(), form.getTopBumonRank());
			if (sosMst != null) {
				if (BumonRank.SITEN_TOKUYAKUTEN_BU == sosMst.getBumonRank()) {
					form.setSosCd2(sosMst.getSosCd());
				} else if (BumonRank.OFFICE_TOKUYAKUTEN_G == sosMst.getBumonRank()) {
					form.setSosCd2(sosMst.getUpSosCd());
					form.setSosCd3(sosMst.getSosCd());
				} else if (BumonRank.TEAM == sosMst.getBumonRank()) {
					form.setSosCd3(sosMst.getUpSosCd());
					form.setSosCd4(sosMst.getSosCd());
					form.setEtcSosFlg(sosMst.getEtcSosFlg());
				}
			}
		} else if (StringUtils.isNotBlank(form.getTopJgiNo())) {// TopSosCdがない場合とは、メニュー画面より従業員毎の品目別リンクが押された場合
			JgiMst jgiMst = dpmSosJgiSearchService.searchJgiMst(ConvertUtil.parseInteger(form.getTopJgiNo()));
			form.setJgiNo(jgiMst.getJgiNo().toString());
			form.setSosCd2(jgiMst.getSosCd2());
			form.setSosCd3(jgiMst.getSosCd3());
			form.setSosCd4(jgiMst.getSosCd4());
		}


		// 検索フラグが立っていれば、検索済みの状態にする
		if (form.isSearchFlg()) {
			// カテゴリリストを初期設定する
			initCategoryList(form);
			form.setTranField();
			setTitle(form);
			searchProdPlanList(form);
		} else {
			// カテゴリリストを初期設定する
			initCategoryList(form);
			setTitle(form);
		}
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
	public Result dpm101C00F05(DpContext ctx, Dpm101C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm101C00F05");
			LOG.debug("SosCd2:" + form.getSosCd2());
			LOG.debug("SosCd3:" + form.getSosCd3());
			LOG.debug("SosCd4:" + form.getSosCd4());
			LOG.debug("ProdCategory:" + form.getProdCategory());
		}
		// カテゴリリストを初期設定する
		initCategoryList(form);
		// 検索実行
		form.setTranField();
		form.setTitleUH("");
		form.setTitleP("");
		form.setTitleZ("");
		setTitle(form);
		searchProdPlanList(form);
		return ActionResult.SUCCESS;
	}

	/**
	 * 全社計画登録時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dpm101C00F10Execute(DpContext ctx, Dpm101C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm101C00F10Execute");
			LOG.debug("SosCd2:" + form.getSosCd2());
			LOG.debug("SosCd3:" + form.getSosCd3());
			LOG.debug("SosCd4:" + form.getSosCd4());
			LOG.debug("ProdCategory:" + form.getProdCategory());
		}
		try {
			// 更新DTO
			List<SosPlanUpdateDto> updateDtoList = form.convertSosPlanUpdateDto();

			// 更新実行
			ManagePlanUpdateResultDto resultDto = new ManagePlanUpdateResultDto(0, 0, 0);
			if (!updateDtoList.isEmpty()) {
				resultDto = dpmIyakuPlanService.updateIyakuPlan(SCREEN_ID, updateDtoList);
			}

			// 更新完了メッセージの追加
			final int updateSizeUH = resultDto.getUpdateCountUh();
			final int updateSizeP = resultDto.getUpdateCountP();
			final int updateSizeZ = resultDto.getUpdateCountZ();
			String zName = "";
			if (dpmCodeMasterSearchService.isVaccine(form.getProdCategory())) {
				zName = DispInsKbn.ZV.getDbValue();
			} else {
				zName = DispInsKbn.Z.getDbValue();
			}
			addMessage(ctx, new MessageKey("DPC0007I", DispInsKbn.UH.getDbValue(), String.valueOf(updateSizeUH), DispInsKbn.P.getDbValue(), String.valueOf(updateSizeP), zName, String.valueOf(updateSizeZ)));

		} finally {
			// 再検索実行
			searchProdPlanList(form);
		}
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
	public Result dpm101C00F11Execute(DpContext ctx, Dpm101C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm101C00F11Execute");
			LOG.debug("SosCd2:" + form.getSosCd2());
			LOG.debug("SosCd3:" + form.getSosCd3());
			LOG.debug("SosCd4:" + form.getSosCd4());
			LOG.debug("ProdCategory:" + form.getProdCategory());
		}
		try {
			// 更新DTO
			List<SosPlanUpdateDto> updateDtoList = form.convertSosPlanUpdateDto();

			// 更新実行
			ManagePlanUpdateResultDto resultDto = new ManagePlanUpdateResultDto(0, 0, 0);
			if (!updateDtoList.isEmpty()) {
				resultDto = dpmBranchPlanService.updateBranchPlan(SCREEN_ID, updateDtoList);
			}

			// 更新完了メッセージの追加
			final int updateSizeUH = resultDto.getUpdateCountUh();
			final int updateSizeP = resultDto.getUpdateCountP();
			final int updateSizeZ = resultDto.getUpdateCountZ();
			String zName = "";
			if (dpmCodeMasterSearchService.isVaccine(form.getProdCategory())) {
				zName = DispInsKbn.ZV.getDbValue();
			} else {
				zName = DispInsKbn.Z.getDbValue();
			}
			addMessage(ctx, new MessageKey("DPC0007I", DispInsKbn.UH.getDbValue(), String.valueOf(updateSizeUH), DispInsKbn.P.getDbValue(), String.valueOf(updateSizeP), zName, String.valueOf(updateSizeZ)));

		} finally {
			// 再検索実行
			searchProdPlanList(form);
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
	public Result dpm101C00F12Execute(DpContext ctx, Dpm101C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm101C00F12Execute");
			LOG.debug("SosCd2:" + form.getSosCd2());
			LOG.debug("SosCd3:" + form.getSosCd3());
			LOG.debug("SosCd4:" + form.getSosCd4());
			LOG.debug("ProdCategory:" + form.getProdCategory());
		}
		try {
			// 更新DTO
			List<SosPlanUpdateDto> updateDtoList = form.convertSosPlanUpdateDto();

			// 更新実行
			ManagePlanUpdateResultDto resultDto = new ManagePlanUpdateResultDto(0, 0, 0);
			if (!updateDtoList.isEmpty()) {
				resultDto = dpmOfficePlanService.updateOfficePlan(SCREEN_ID, updateDtoList);
			}

			// 更新完了メッセージの追加
			final int updateSizeUH = resultDto.getUpdateCountUh();
			final int updateSizeP = resultDto.getUpdateCountP();
			final int updateSizeZ = resultDto.getUpdateCountZ();
			String zName = "";
			if (dpmCodeMasterSearchService.isVaccine(form.getProdCategory())) {
				zName = DispInsKbn.ZV.getDbValue();
			} else {
				zName = DispInsKbn.Z.getDbValue();
			}
			addMessage(ctx, new MessageKey("DPC0007I", DispInsKbn.UH.getDbValue(), String.valueOf(updateSizeUH), DispInsKbn.P.getDbValue(), String.valueOf(updateSizeP), zName, String.valueOf(updateSizeZ)));

		} finally {
			// 再検索実行
			searchProdPlanList(form);
		}
		return ActionResult.SUCCESS;
	}

	/**
	 * チーム計画登録時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dpm101C00F13Execute(DpContext ctx, Dpm101C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm101C00F13Execute");
			LOG.debug("SosCd2:" + form.getSosCd2());
			LOG.debug("SosCd3:" + form.getSosCd3());
			LOG.debug("SosCd4:" + form.getSosCd4());
			LOG.debug("ProdCategory:" + form.getProdCategory());
		}
		try {
			// 更新DTO
			List<SosPlanUpdateDto> updateDtoList = form.convertSosPlanUpdateDto();

			// 更新実行
			ManagePlanUpdateResultDto resultDto = new ManagePlanUpdateResultDto(0, 0, 0);
			if (!updateDtoList.isEmpty()) {
				resultDto = dpmTeamPlanService.updateTeamPlan(SCREEN_ID, updateDtoList);
			}

			// 更新完了メッセージの追加
			final int updateSizeUH = resultDto.getUpdateCountUh();
			final int updateSizeP = resultDto.getUpdateCountP();
			final int updateSizeZ = resultDto.getUpdateCountZ();
			String zName = "";
			if (dpmCodeMasterSearchService.isVaccine(form.getProdCategory())) {
				zName = DispInsKbn.ZV.getDbValue();
			} else {
				zName = DispInsKbn.Z.getDbValue();
			}
			addMessage(ctx, new MessageKey("DPC0007I", DispInsKbn.UH.getDbValue(), String.valueOf(updateSizeUH), DispInsKbn.P.getDbValue(), String.valueOf(updateSizeP), zName, String.valueOf(updateSizeZ)));

		} finally {
			// 再検索実行
			searchProdPlanList(form);
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
	public Result dpm101C00F14Execute(DpContext ctx, Dpm101C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm101C00F13Execute");
			LOG.debug("SosCd2:" + form.getSosCd2());
			LOG.debug("SosCd3:" + form.getSosCd3());
			LOG.debug("SosCd4:" + form.getSosCd4());
			LOG.debug("ProdCategory:" + form.getProdCategory());
		}

		try {
			// 更新DTO
			List<SosPlanUpdateDto> updateDtoList = form.convertSosPlanUpdateDto();

			// 更新実行
			ManagePlanUpdateResultDto resultDto = new ManagePlanUpdateResultDto(0, 0, 0);
			if (!updateDtoList.isEmpty()) {
				resultDto = dpmMrPlanService.updateMrPlan(SCREEN_ID, updateDtoList);
			}

			// 更新完了メッセージの追加
			final int updateSizeUH = resultDto.getUpdateCountUh();
			final int updateSizeP = resultDto.getUpdateCountP();
			final int updateSizeZ = resultDto.getUpdateCountZ();
			String zName = "";
			if (dpmCodeMasterSearchService.isVaccine(form.getProdCategory())) {
				zName = DispInsKbn.ZV.getDbValue();
			} else {
				zName = DispInsKbn.Z.getDbValue();
			}
			addMessage(ctx, new MessageKey("DPC0007I", DispInsKbn.UH.getDbValue(), String.valueOf(updateSizeUH), DispInsKbn.P.getDbValue(), String.valueOf(updateSizeP), zName, String.valueOf(updateSizeZ)));

		} finally {
			// 再検索実行
			searchProdPlanList(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理の実行
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchProdPlanList(Dpm101C00Form form) throws Exception {

		// 品目別計画の検索実行
		ProdPlanScDto scDto = form.convertProdPlanScDto();

		// 計画レベル:品目別では選択された組織の計画を表示するので、立案レベルは該当のものを使用する
		try {

			ProdPlanResultDto resultDto;
			if (scDto.getJgiNo() != null) {

				// 従業員番号が設定されている場合、担当者別計画一覧を作成
				resultDto = dpmMrPlanSearchService.searchSosProdPlan(scDto);

			} else if (scDto.getSosCd4() != null) {

				// 組織コード(チーム)が設定されている場合、チーム別計画一覧を作成
				resultDto = dpmTeamPlanSearchService.searchSosProdPlan(scDto);

			} else if (scDto.getSosCd3() != null) {

				// 組織コード(営業所)が設定されている場合、営業所別計画一覧を作成
				resultDto = dpmOfficePlanSearchService.searchSosProdPlan(scDto);

			} else if (scDto.getSosCd2() != null) {

				// 組織コード(支店)が設定されている場合、支店別計画一覧を作成
				resultDto = dpmBranchPlanSearchService.searchSosProdPlan(scDto);

			} else {

				// 組織コードが設定されていない場合、全社計画一覧を作成
				resultDto = dpmIyakuPlanSearchService.searchSosProdPlan(scDto);
			}

			// リクエストボックスに検索結果をセット
			super.getRequestBox().put(Dpm101C00Form.DPM101C00_DATA_R_SEARCH_RESULT, resultDto);

			// 部門ランクフラグをリクエストボックスにセット
			DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
			boolean bumonFlag = false;
			if (userInfo != null) {
				DpUser user = userInfo.getSettingUser();
				if (user != null) {

					if (scDto.getJgiNo() != null) {
						// 従業員が設定されている場合、全ロール(本部・支店・営業所)OK
						bumonFlag = true;
					} else if (scDto.getSosCd4() != null) {
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

			super.getRequestBox().put(Dpm101C00Form.DPM101C00_DATA_R_BUMON_FLAG, bumonFlag);

		} finally {

			// 実行した検索条件を画面に設定
			form.setViewField();
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
	public void dpm101C00F05Validation(DpContext ctx, Dpm101C00Form form) throws ValidateException {
		// [必須] 品目カテゴリ
		if (StringUtils.isEmpty(form.getProdCategory())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "カテゴリ")));
		}
	}

	/**
	 * 全社計画登録時の入力パラメータチェック
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dpm101C00F10Validation(DpContext ctx, Dpm101C00Form form) throws ValidateException {
		executeValidation(ctx, form);
	}

	/**
	 * 支店計画登録時の入力パラメータチェック
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dpm101C00F11Validation(DpContext ctx, Dpm101C00Form form) throws ValidateException {
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
	public void dpm101C00F12Validation(DpContext ctx, Dpm101C00Form form) throws ValidateException {
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
	public void dpm101C00F13Validation(DpContext ctx, Dpm101C00Form form) throws ValidateException {
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
	public void dpm101C00F14Validation(DpContext ctx, Dpm101C00Form form) throws ValidateException {
		executeValidation(ctx, form);
	}

	/**
	 * formにUH、P、Zのタイトルを設定する
	 * @param form
	 */
	private void setTitle(Dpm101C00Form form) {
		// メッセージのタイトルを取得
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

	/**
	 * 登録処理時の共通Validationメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	private void executeValidation(DpContext ctx, Dpm101C00Form form) throws ValidateException {
		// 更新パラーメータチェック
		String[] rowIdList = form.getRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		for (int i = 0; i < rowIdList.length; i++) {
			// パラーメータのサイズチェック
			String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);

			if (rowId.length != 18) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 組織コード(支店)
			if (StringUtils.isNotEmpty(rowId[0]) && !ValidationUtil.isLong(rowId[0])) {
				final String errMsg = "受信パラメータ(組織コード(支店))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 組織コード(営業所)
			if (StringUtils.isNotEmpty(rowId[1]) && !ValidationUtil.isLong(rowId[1])) {
				final String errMsg = "受信パラメータ(組織コード(営業所))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 組織コード(チーム)
			if (StringUtils.isNotEmpty(rowId[2]) && !ValidationUtil.isLong(rowId[2])) {
				final String errMsg = "受信パラメータ(組織コード(チーム))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 雑組織区分はなし
			// 従業員番号
			if (StringUtils.isNotEmpty(rowId[4]) && !ValidationUtil.isLong(rowId[4])) {
				final String errMsg = "受信パラメータ(従業員番号)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 品目コード
			if (StringUtils.isNotEmpty(rowId[5]) && !ValidationUtil.isLong(rowId[5])) {
				final String errMsg = "受信パラメータ(品目コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [UH]シーケンスキー
			if (StringUtils.isNotEmpty(rowId[6]) && !ValidationUtil.isLong(rowId[6])) {
				final String errMsg = "受信パラメータ([UH]シーケンスキー)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [UH]最終更新日
			if (StringUtils.isNotEmpty(rowId[7]) && !ValidationUtil.isLong(rowId[7])) {
				final String errMsg = "受信パラメータ([UH]最終更新日)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [UH]更新前計画値
			if (StringUtils.isNotEmpty(rowId[8]) && !ValidationUtil.isLong(rowId[8])) {
				final String errMsg = "受信パラメータ([UH]計画値(更新前))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [UH]更新後計画値
			if (StringUtils.isNotEmpty(rowId[15]) && !ValidationUtil.isLong(rowId[15])) {
				final String errMsg = "受信パラメータ([UH]計画値(更新後))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [P]シーケンスキー
			if (StringUtils.isNotEmpty(rowId[9]) && !ValidationUtil.isLong(rowId[9])) {
				final String errMsg = "受信パラメータ([P]シーケンスキー)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [P]最終更新日
			if (StringUtils.isNotEmpty(rowId[10]) && !ValidationUtil.isLong(rowId[10])) {
				final String errMsg = "受信パラメータ([P]最終更新日)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [P]更新前計画値
			if (StringUtils.isNotEmpty(rowId[11]) && !ValidationUtil.isLong(rowId[11])) {
				final String errMsg = "受信パラメータ([P]計画値(更新前))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [P]更新後計画値
			if (StringUtils.isNotEmpty(rowId[16]) && !ValidationUtil.isLong(rowId[16])) {
				final String errMsg = "受信パラメータ([P]計画値(更新後))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [Z]シーケンスキー
			if (StringUtils.isNotEmpty(rowId[12]) && !ValidationUtil.isLong(rowId[12])) {
				final String errMsg = "受信パラメータ([P]シーケンスキー)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [Z]最終更新日
			if (StringUtils.isNotEmpty(rowId[13]) && !ValidationUtil.isLong(rowId[13])) {
				final String errMsg = "受信パラメータ([P]最終更新日)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [Z]更新前計画値
			if (StringUtils.isNotEmpty(rowId[14]) && !ValidationUtil.isLong(rowId[14])) {
				final String errMsg = "受信パラメータ([P]計画値(更新前))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// [Z]更新後計画値
			if (StringUtils.isNotEmpty(rowId[17]) && !ValidationUtil.isLong(rowId[17])) {
				final String errMsg = "受信パラメータ([P]計画値(更新後))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
		}
	}
	/**
	 *
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
	public Result dpm101C00F15Output(DpContext ctx, Dpm101C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm101C00F15Output");
		}

		// 帳票出力準備
		ProdPlanScDto prodPlanScDto = form.convertProdPlanScDto();
		String templateRootPath = getRealPath(TemplateInfo.EXCEL_TEMPLATE_PATH.getValue());

		searchProdPlanList(form);

		// 帳票出力実行
		ExportResult exportResult = dpmReportService.outputProdPlanList(templateRootPath, prodPlanScDto);
		form.setExportResult(exportResult);
		form.setDownLoadFileName(exportResult.getName());

		return ActionResult.SUCCESS;
	}


	/**
	 * 出力時の入力パラメータチェック
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dpm101C00F15Validation(DpContext ctx, Dpm101C00Form form) throws ValidateException {
		dpm101C00F05Validation(ctx, form);
	}

	/**
	 * カテゴリリストの初期設定
	 * @param form ActionForm
	 */
	private void initCategoryList(Dpm101C00Form form) {
		// 汎用マスタのカテゴリリスト
		List<DpmCCdMst> categoryList = (dpmCodeMasterSearchService.searchCategorylist(null,PLAN_ID));
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

		for(SosCtg ctg : ctgList) {
			targetCategoryAry.add(ctg.getCategory());
		}

		// SosCategoryがない場合に補完する
		String sosCategory = form.getSosCategory();
		if(sosCategory == null || sosCategory.isEmpty()) {
			Object[] tmpAry = targetCategoryAry.toArray();
			String joinedCategory = StringUtils.join(tmpAry,",");
			form.setSosCategory(joinedCategory);
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
}
