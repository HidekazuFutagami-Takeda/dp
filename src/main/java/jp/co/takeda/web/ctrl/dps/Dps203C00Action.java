package jp.co.takeda.web.ctrl.dps;

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
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.MikakutokuSijouResultDto;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.model.view.SosCtg;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcUserSearchService;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.service.DpsMikakutokuSijouSearchService;
import jp.co.takeda.service.DpsPlannedCtgSearchService;
import jp.co.takeda.service.DpsSosCtgSearchService;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps203C00Form;

/**
 * Dps203C00((医)未獲得市場一覧画面)のアクションクラス
 *
 * @author stakeuchi
 */
@Controller("Dps203C00Action")
public class Dps203C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps203C00Action.class);

	/**
	 * 計画（1：期別、2：月別）
	 */
	private static final String PLAN_ID = "1";

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPS203C00";

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 未獲得市場 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsMikakutokuSijouSearchService")
	protected DpsMikakutokuSijouSearchService dpsMikakutokuSijouSearchService;

	/**
	 * ユーザサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpcUserSearchService")
	protected DpcUserSearchService dpcUserSearchService;

	/**
	 * カテゴリ 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	/**
	 * 計画支援の組織カテゴリコード検索
	 */
	@Autowired(required = true)
	@Qualifier("dpsSosCtgSearchService")
	protected DpsSosCtgSearchService dpsSosCtgSearchService;

	/**
	 * 計画対象カテゴリ領域の検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsPlannedCtgSearchService")
	protected DpsPlannedCtgSearchService dpsPlannedCtgSearchService;

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
	public Result dps203C00F00(DpContext ctx, Dps203C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps203C00F00");
		}

		// 初期化処理
		form.formInit();

		// 組織従業員初期化処理(営業所までに再設定)
		SosMst defaultSosMst = DpUserInfo.getDpUserInfo().getDefaultSosMst();
		if (defaultSosMst != null) {
			if (defaultSosMst.getBumonRank() == BumonRank.OFFICE_TOKUYAKUTEN_G) {
				form.setSosCd3(defaultSosMst.getSosCd());
				dpcUserSearchService.changeDefaultSosJgi(null, null, defaultSosMst.getSosCd(), null);
			} else if (defaultSosMst.getBumonRank() == BumonRank.TEAM) {
				form.setSosCd3(defaultSosMst.getUpSosCd());
				dpcUserSearchService.changeDefaultSosJgi(null, null, defaultSosMst.getUpSosCd(), null);
			}
		}

		// ログインユーザの組織情報をセット
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
				}
			}
		}

		// カテゴリ初期化処理
		form.setCategoryTran(form.getCategory());
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
	public Result dps203C00F05(DpContext ctx, Dps203C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps203C00F05");
			LOG.debug("SosCd3:" + form.getSosCd3());
			LOG.debug("YakkouSijouCode:" + form.getYakkouSijouCode());
		}

		List<MikakutokuSijouResultDto> resultDtoList;
		try {

			resultDtoList = dpsMikakutokuSijouSearchService.searchMikakutokuSijouSumList(form.convertMikakutokuSijouScDto());
			form.setExistSearchDataFlag(true);

		} catch (LogicalException e) {
			form.setExistSearchDataFlag(false);
			throw e;
		}

		super.getRequestBox().put(Dps203C00Form.DPS203C00_DATA_R, (ArrayList<MikakutokuSijouResultDto>) resultDtoList);

		return ActionResult.SUCCESS;
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
	public void dps203C00F05Validation(DpContext ctx, Dps203C00Form form) throws ValidateException {

		// 組織 必須チェック
		if (StringUtils.isEmpty(form.getSosCd3())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "組織")));
		}

		// 薬効市場 必須チェック
		if (StringUtils.isEmpty(form.getYakkouSijouCode())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "薬効市場")));
		}
	}

	/**
	* カテゴリリストの初期設定
	* @param form Dps203C00Form
	*/
	private void initCategoryList(Dps203C00Form form) {
		// 汎用マスタのカテゴリリスト
		List<DpsCCdMst> categoryList = (dpsCodeMasterSearchService.searchCategoryList(null, PLAN_ID));

		// カテゴリリストに表示するリスト
		List<String> targetCategoryAry = new ArrayList<String>();
//見る必要がないので、コメント削除
//		// 計画対象カテゴリ領域の設定
//		List<String> planLvCtgList = new ArrayList<String>();
//		// 計画立案レベル-支店
//		planLvCtgList = dpsPlannedCtgSearchService.searchCategoryByPlanLevel(ProdPlanLevel.SITEN);

		String sosCd = null;
		if (StringUtils.isNotBlank(form.getSosCd2())) {
			// 組織コード：支店
			sosCd = form.getSosCd2();
		}
		// 組織と紐づくカテゴリリスト
		List<SosCtg> ctgList = dpsSosCtgSearchService.searchDpsSosCtgList(sosCd, SCREEN_ID);

		for (SosCtg ctg : ctgList) {
			targetCategoryAry.add(ctg.getCategory());
		}

		List<CodeAndValue> list = new ArrayList<CodeAndValue>();

		boolean indexFlg = true;
		// 対象の立案レベルをもつ計画対象カテゴリ領域のカテゴリのみ設定
		for (DpsCCdMst mst : categoryList) {
			if (targetCategoryAry.contains(mst.getDataCd())) {
//			if (targetCategoryAry.contains(mst.getDataCd()) && planLvCtgList.contains(mst.getDataCd())) {
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
