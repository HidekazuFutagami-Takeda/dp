package jp.co.takeda.web.ctrl.dps;

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
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.TmsTytenPlanSlideForVacResultDto;
import jp.co.takeda.dto.TmsTytenPlanSlideResultDto;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.SosMstCtg;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcSystemManageSearchService;
import jp.co.takeda.service.DpcUserSearchService;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.service.DpsPlannedCtgSearchService;
import jp.co.takeda.service.DpsSosCtgSearchService;
import jp.co.takeda.service.DpsSosJgiSearchService;
import jp.co.takeda.service.DpsTmsTytenPlanSearchSlideForVacService;
import jp.co.takeda.service.DpsTmsTytenPlanSearchSlideService;
import jp.co.takeda.service.DpsTmsTytenSlideForVacService;
import jp.co.takeda.service.DpsTmsTytenSlideService;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps501C00Form;

/**
 * Dps501C00((医)特約店別計画スライド画面)のアクションクラス
 *
 * @author yokokawa
 */
@Controller("Dps501C00Action")
public class Dps501C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps501C00Action.class);

	// -------------------------------
	// injection field
	// -------------------------------

	/**
	 * 特約店別計画スライド状態 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsTmsTytenPlanSearchSlideService")
	protected DpsTmsTytenPlanSearchSlideService dpsTmsTytenPlanSearchSlideService;

	/**
	 * 特約店別計画スライドサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsTmsTytenSlideService")
	protected DpsTmsTytenSlideService dpsTmsTytenSlideService;

	/**
	 * ユーザサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpcUserSearchService")
	protected DpcUserSearchService dpcUserSearchService;

	/**
	 * 組織・従業員に関するサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsSosJgiSearchService")
	protected DpsSosJgiSearchService dpsSosJgiSearchService;

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
	 * 計画支援の組織カテゴリコード検索
	 */
	@Autowired(required = true)
	@Qualifier("dpsSosCtgSearchService")
	protected DpsSosCtgSearchService dpsSosCtgSearchService;

	/**
	 * 納入計画管理検索サービス実装クラス
	 */
    @Autowired(required = true)
    @Qualifier("dpcSystemManageSearchService")
    protected DpcSystemManageSearchService dpcSystemManageSearchService;

	/**
	 * ワクチン用特約店別計画スライド状態 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsTmsTytenPlanSearchSlideForVacService")
	protected DpsTmsTytenPlanSearchSlideForVacService dpsTmsTytenPlanSearchSlideForVacService;

	/**
	 * ワクチン用特約店別計画スライドサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsTmsTytenSlideForVacService")
	protected DpsTmsTytenSlideForVacService dpsTmsTytenSlideForVacService;

	/**
	 * 計画（1：期別、2：月別）
	 */
	private static final String PLAN_ID = "1";


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
	@Permission( authType = REFER)
	public Result dps501C00F00(DpContext ctx, Dps501C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps501C00F00");
		}

		// 初期化処理
		form.formInit();
		initCategoryList(form);
		form.setSosCd2Tran(form.getSosCd2());
		form.setCategoryTran(form.getCategory());

		// スライド状態取得
		SysType sysType = null;
		if(dpsCodeMasterSearchService.isVaccine(form.getCategory())) {
			sysType = SysType.VACCINE;
			searchTmsTytenPlanSlideStatusForVac();
		}else{
			sysType = SysType.IYAKU;
			searchTmsTytenPlanSlideStatus(form.getSosCd2(), form.getCategory());
		}

		// 施設特約店別計画〆後のメッセージ設定
		SysManage sysManage = dpcSystemManageSearchService.searchSysManage(SysClass.DPS, sysType);
		if (!sysManage.getWsEndFlg()) {
			super.addErrorMessage(ctx, new MessageKey("DPS3291E"));
		}
		form.setWsEndFlg(sysManage.getWsEndFlg());

		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理(プルダウン/組織選択)時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission( authType = REFER)
	public Result dps501C00F05(DpContext ctx, Dps501C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps501C00F05");
		}

		// 施設特約店別計画〆後のメッセージ設定
		if (!form.getWsEndFlg()) {
			super.addErrorMessage(ctx, new MessageKey("DPS3291E"));
		}

		// 検索条件の保存
		form.setSosCd2Tran(form.getSosCd2());
		form.setCategoryTran(form.getCategory());
		if (dpsCodeMasterSearchService.isVaccine(form.getCategory())) {
			searchTmsTytenPlanSlideStatusForVac();
		} else {
			searchTmsTytenPlanSlideStatus(form.getSosCd2Tran(), form.getCategoryTran());
		}
		initCategoryList(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * スライドする処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission( authType = EDIT)
	public Result dps501C00F10Execute(DpContext ctx, Dps501C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps501C00F10Execute");
		}

		String sosCd2 = form.getSosCd2Tran();
		// ログインユーザの組織情報をセット
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {
				// 本部の人は、全支店対象となるため、支店コードを無し
				if (user.isMatch(JokenSet.HONBU) || user.isMatch(JokenSet.HONBU_WAKUTIN_G)) {
					sosCd2 = null;
				}
			}
		}

		// スライド処理実行
		try {
			dpsTmsTytenSlideService.tmsTytenSlide(sosCd2, form.getCategoryTran());

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0005I", "スライド処理"));

		} finally {

			// 再検索
			searchTmsTytenPlanSlideStatus(form.getSosCd2Tran(), form.getCategoryTran());
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * (ワ)スライドする処理時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission( authType = EDIT)
	public Result dps501C00F20Execute(DpContext ctx, Dps501C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps501C00F20Execute");
		}

		String sosCd2 = form.getSosCd2Tran();
		// ログインユーザの組織情報をセット
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {
				// 本部の人は、全支店対象となるため、支店コードを無し
				if (user.isMatch(JokenSet.HONBU) || user.isMatch(JokenSet.HONBU_WAKUTIN_G)) {
					sosCd2 = null;
				}
			}
		}

		// スライド処理実行
		try {
			dpsTmsTytenSlideForVacService.tmsTytenSlide(sosCd2);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0005I", "スライド処理"));

		} finally {

			// 再検索
			searchTmsTytenPlanSlideStatusForVac();
		}
		return ActionResult.SUCCESS;
	}

	// -------------------------------
	// private method
	// -------------------------------
	/**
	 * 特約店別計画スライド状態を検索する。
	 *
	 * @param sosCd2 組織コード(支店)
	 * @param category カテゴリ
	 */
	private void searchTmsTytenPlanSlideStatus(String sosCd2, String category) throws Exception {

		// 特約店別計画スライド状態を検索
		TmsTytenPlanSlideResultDto resultDto = dpsTmsTytenPlanSearchSlideService.searchSlideStatus(sosCd2, category);

		// 検索結果をリクエストに設定
		super.getRequestBox().put(Dps501C00Form.DPS501C00_DATA_R, resultDto);
	}

	/**
	 * (ワ)特約店別計画スライド状態を検索する。
	 */
	private void searchTmsTytenPlanSlideStatusForVac() {
		// 特約店別計画スライド状態を検索
		TmsTytenPlanSlideForVacResultDto resultDto = dpsTmsTytenPlanSearchSlideForVacService.searchSlideStatus();

		// 検索結果をリクエストに設定
		super.getRequestBox().put(Dps501C00Form.DPS501C01_DATA_R, resultDto);
	}

	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * 検索処理時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps501C00F05Validation(DpContext ctx, Dps501C00Form form) throws ValidateException {

		// カテゴリ 必須チェック
		if (StringUtils.isEmpty(form.getCategory())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "カテゴリ")));
		}
	}

	/**
	 * スライド実行時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps501C00F10Validation(DpContext ctx, Dps501C00Form form) throws ValidateException {

		// カテゴリ 必須チェック
		if (StringUtils.isEmpty(form.getCategoryTran())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "カテゴリ")));
		}
	}

	/**
	* カテゴリリストの初期設定
	* @param form Dps500C00Form
	*/
	private void initCategoryList(Dps501C00Form form) {
		// 汎用マスタのカテゴリリスト
		List<DpsCCdMst> categoryList = (dpsCodeMasterSearchService.searchCategoryList(null, PLAN_ID));

        List<CodeAndValue> list = new ArrayList<CodeAndValue>();

       boolean indexFlg = true;

		// 計画対象カテゴリ領域の設定
		List<String> planLvCtgList = new ArrayList<String>();
		// 計画立案レベル-特約店
		planLvCtgList = dpsPlannedCtgSearchService.searchCategoryByPlanLevel(ProdPlanLevel.WS);

        // ログインユーザの組織カテゴリコードを取得
        DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
        List<String> sosCategoryList = new ArrayList<String>();
        if (userInfo != null) {
            DpUser user = userInfo.getSettingUser();
            if (user != null) {
            	// 支店コード
            	form.setSosCd2(user.getSosCd2());
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
                if (form.getCategory() == null && indexFlg) {
                    form.setCategory(cad.getCode());
                    indexFlg = false;
                }
            }
        }
		form.setProdCategoryList(list);
	}
}
