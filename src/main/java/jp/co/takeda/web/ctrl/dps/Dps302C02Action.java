package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.exp.ErrMessageKey.VALIDATE_ERROR;
import static jp.co.takeda.security.DpAuthority.AuthType.EDIT;
import static jp.co.takeda.security.DpAuthority.AuthType.REFER;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dao.div.RefDeliveryScope;
import jp.co.takeda.dto.MrPlanResultDto;
import jp.co.takeda.dto.TeamPlanUpdateDto;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.JokenSet;

import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsMrPlanSearchService;
import jp.co.takeda.service.DpsMrPlanService;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps302C02Form;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Dps302C02((医)担当者別計画編集画面(チーム別計画入力))のアクションクラス
 * 
 * @author nozaki
 */
@Controller("Dps302C02Action")
public class Dps302C02Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps302C02Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 試算機能 検索系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsMrPlanSearchService")
	protected DpsMrPlanSearchService dpsMrPlanSearchService;

	/**
	 * 試算機能 更新系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsMrPlanService")
	protected DpsMrPlanService dpsMrPlanService;

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
	public Result dps302C02F00(DpContext ctx, Dps302C02Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C02F00");
			LOG.debug("SosCd3:" + form.getSosCd3());
			LOG.debug("SosCd4:" + form.getSosCd4());
			LOG.debug("ProdCode:" + form.getProdCode());
			LOG.debug("InsType" + form.getInsType());
		}

		// 初期化処理
		form.formInit();

		// 部門フラグ初期化
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			DpUser settingUser = userInfo.getSettingUser();
			if (settingUser != null) {
				final BumonRank rank = settingUser.getBumonRank();
				switch (rank) {
					case TEAM:
						form.setIsTeamRank(true);
						break;
					default:
						form.setIsTeamRank(false);
						break;
				}
				// 本部ユーザの場合は営業所
				if(settingUser.isMatch(JokenSet.HONBU)){
					form.setIsTeamRank(false);
				}
			}
		}
		searchMrPlan(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * UH/P/合計検索時に呼ばれるアクションメソッド<br>
	 * 
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission( authType = REFER)
	public Result dps302C02F05(DpContext ctx, Dps302C02Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C02F05");
		}

		searchMrPlan(form);

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
	@Permission( authType = EDIT)
	public Result dps302C02F10Execute(DpContext ctx, Dps302C02Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C02F10");
		}

		String sosCd = form.getSosCd3();
		TeamPlanUpdateDto teamPlanUpdateDto = form.convertTeamPlanUpdateDto();

		boolean searchFlg = false;
		try {
			// 更新処理実行
			dpsMrPlanService.updateTeamPlan(sosCd, teamPlanUpdateDto);

			// 再検索実行
			String choseiMsg = dpsMrPlanService.createChoseiMsg(searchMrPlan(form));
			searchFlg = true;

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPS0004I", choseiMsg));

		} finally {

			if (!searchFlg) {
				// 再検索実行
				searchMrPlan(form);
			}
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 「参照無」/「ＡＬＬ」切替時に呼ばれるアクションメソッド
	 * 
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission( authType = REFER)
	public Result dps302C02F15(DpContext ctx, Dps302C02Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C02F15");
		}

		// 検索処理
		searchMrPlan(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理を実行する。
	 * 
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private MrPlanResultDto searchMrPlan(Dps302C02Form form) throws Exception {

		String sosCd3 = form.getSosCd3();
		String sosCd4 = form.getSosCd4();
		String prodCode = form.getProdCode();
		InsType insType = form.convertInsType();
		RefDeliveryScope scope = form.convertRefDeliveryScope();

		// データ有無フラグ初期化
		super.getRequestBox().put(Dps302C02Form.DATA_EXIST, false);

		// -------------------------------
		// 検索処理実行
		// -------------------------------
		MrPlanResultDto mrPlanResultDto = null;

		// AL指定の場合
		if (form.getIsTeamRank()) {
			mrPlanResultDto = dpsMrPlanSearchService.searchTeamPlan(insType, prodCode, sosCd3, sosCd4, scope);

		}
		// 営業所指定の場合
		else {
			mrPlanResultDto = dpsMrPlanSearchService.searchTeamPlan(insType, prodCode, sosCd3, scope);
		}

		// データ有無フラグ設定
		super.getRequestBox().put(Dps302C02Form.DATA_EXIST, true);

		// リクエストに検索結果リストをセット
		super.getRequestBox().put(Dps302C02Form.DPS302C02_DATA_R, (MrPlanResultDto) mrPlanResultDto);

		return mrPlanResultDto;
	}

	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * 登録処理処理時の入力パラメータチェックを行う。
	 * 
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps302C02F10Validation(DpContext ctx, Dps302C02Form form) throws ValidateException {

		// 更新行のNullチェック
		String[] rowIdList = form.getRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}

		for (int i = 0; i < rowIdList.length; i++) {

			String[] rowId = rowIdList[i].split(",", 5);
			if (rowId.length != 5) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 入力値チェック
			commonDataCheck(rowId);
		}
	}

	/**
	 * 試算処理処理時の入力パラメータチェックを行う。
	 * 
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps302C02F15Validation(DpContext ctx, Dps302C02Form form) throws ValidateException {

		// 更新行のNullチェック
		String[] rowIdList = form.getRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}

		for (int i = 0; i < rowIdList.length; i++) {

			String[] rowId = rowIdList[i].split(",", 5);
			if (rowId.length != 5) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 入力値チェック
			commonDataCheck(rowId);
		}
	}

	/**
	 * 入力値のチェック処理を行う。
	 * 
	 * @param rowIdArr 入力行の配列
	 * @throws ValidateException エラーがあった場合
	 */
	private void commonDataCheck(String[] rowIdArr) throws ValidateException {

		// シーケンスキー
		if (!StringUtils.isEmpty(rowIdArr[0]) && !ValidationUtil.isLong(rowIdArr[0])) {
			final String errMsg = "受信パラメータ(シーケンスキー)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		// 組織コード
		if (!StringUtils.isEmpty(rowIdArr[1]) && !ValidationUtil.isLong(rowIdArr[1])) {
			final String errMsg = "受信パラメータ(組織コード)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		// 最終更新日
		if (!StringUtils.isEmpty(rowIdArr[2]) && !ValidationUtil.isLong(rowIdArr[2])) {
			final String errMsg = "受信パラメータ(最終更新日)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		// 営業所案
		if (!StringUtils.isEmpty(rowIdArr[3]) && !ValidationUtil.isLong(rowIdArr[3])) {
			final String errMsg = "受信パラメータ(営業所案)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		// 決定
		if (!StringUtils.isEmpty(rowIdArr[4]) && !ValidationUtil.isLong(rowIdArr[4])) {
			final String errMsg = "受信パラメータ(決定)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
	}
}
