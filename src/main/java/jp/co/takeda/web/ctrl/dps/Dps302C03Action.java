package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dao.div.RefDeliveryScope;
import jp.co.takeda.dto.MrPlanResultDto;
import jp.co.takeda.dto.MrPlanUpdateDto;
import jp.co.takeda.dto.SosJgiDto;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.JknGrp;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.JokenKbn;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.model.div.StatusForMrPlan;
import jp.co.takeda.model.view.SosJgiLblInfo;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.service.DpsMrPlanSearchService;
import jp.co.takeda.service.DpsMrPlanService;
import jp.co.takeda.service.DpsSosJgiSearchService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.action.DpActionForm;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps302C03Form;

/**
 * Dps302C03((医)担当者別計画編集画面(担当者別計画))のアクションクラス
 *
 * @author yokokawa
 */
@Controller("Dps302C03Action")
public class Dps302C03Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps302C03Action.class);

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPS302C03";

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

	/**
	 * カテゴリ 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	/**
	 * 組織・従業員検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsSosJgiSearchService")
	protected DpsSosJgiSearchService dpsSosJgiSearchService;

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
	@Permission(authType = AuthType.REFER)
	public Result dps302C03F00(DpContext ctx, Dps302C03Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C03F00");
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

		// ログインユーザの組織情報をセット
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {
				// 権限確認コード
				if (user.isSosLvl(SCREEN_ID, SosLvl.OFFICE)) {
					form.setSosCd3(user.getSosCd3());
				}
				else if (user.isSosLvl(SCREEN_ID, SosLvl.MR)) {
					form.setSosCd3(user.getSosCd3());
					form.setJgiNo(String.valueOf(user.getJgiNo()));
				}
			}
		}

		searchMrPlan(form);

		//グリッドのヘッダを設定
		setGridHeader(form);

		// ポップアップ用情報取得
		String sosMaxSrchGetValue = "";
		String sosCd = "";
		Integer jgiNo = null;
		if (!form.getIsTeamRank()) {
			sosMaxSrchGetValue = "02";
			sosCd = form.getSosCd3();
		} else {
			sosMaxSrchGetValue = "03";
			sosCd = form.getSosCd4();
		}

		// セッションに設定
		super.getSessionBox().put(DpActionForm.DATA_R_SOS_JGI_LBL_INFO, getDpsSosJgiLblInfo(sosCd, jgiNo, sosMaxSrchGetValue, false));

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
	@Permission(authType = AuthType.REFER)
	public Result dps302C03F05(DpContext ctx, Dps302C03Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C03F05");
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
	@Permission(authType = AuthType.EDIT)
	public Result dps302C03F10Execute(DpContext ctx, Dps302C03Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C03F10");
		}

		// サービスに渡すパラメータ作成
		String sosCd3 = form.getSosCd3();
		String sosCd4 = form.getSosCd4();
		String prodCode = form.getProdCode();
		String category = form.getCategory();
		MrPlanUpdateDto mrPlanUpdateDto = form.convertMrPlanUpdateDto();
		StatusForMrPlan statusForMrPlan = form.getStatusForMrPlan();

		boolean searchFlg = false;
		try {

			// ALの場合
			if (form.getIsTeamRank()) {

				dpsMrPlanService.updateMrPlan(sosCd3, sosCd4, false, mrPlanUpdateDto, category, statusForMrPlan);

				// 検索結果処理
				String thoseiMsg = dpsMrPlanService.createChoseiMsg(searchMrPlan(form));
				searchFlg = true;

				// 更新完了メッセージの追加
				addMessage(ctx, new MessageKey("DPS0006I", thoseiMsg));
			}

			// 営業所の場合
			else {

				dpsMrPlanService.updateMrPlan(sosCd3, mrPlanUpdateDto, category, statusForMrPlan);
				String choseiMsg = "";

				// 検索結果処理(担当者別計画公開以降の場合のみ調整金額有無を表示)
				MrPlanResultDto dto = searchMrPlan(form);
				searchFlg = true;
				StatusForMrPlan status = dto.getStatusForMrPlan();
				if (status != null) {
					Integer s1 = ConvertUtil.parseInteger(status.getDbValue());
					Integer s2 = ConvertUtil.parseInteger(StatusForMrPlan.MR_PLAN_OPENED.getDbValue());
					if (s1 >= s2) {
						choseiMsg = dpsMrPlanService.createChoseiMsg(dpsMrPlanSearchService.searchChosei(sosCd3, prodCode, category));
					}
				}

				// 更新完了メッセージの追加
				addMessage(ctx, new MessageKey("DPS0006I", choseiMsg));
			}

		} finally {

			if (!searchFlg) {
				// 再検索実行
				searchMrPlan(form);
			}
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 確定処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps302C03F15Execute(DpContext ctx, Dps302C03Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C03F15Execute");
		}

		// サービスに渡すパラメータ作成
		String sosCd3 = form.getSosCd3();
		String sosCd4 = form.getSosCd4();
		String category = form.getCategory();
		MrPlanUpdateDto mrPlanUpdateDto = form.convertMrPlanUpdateDto();
		StatusForMrPlan statusForMrPlan = form.getStatusForMrPlan();

		try {

			// 確定処理はALの場合のみ
			dpsMrPlanService.updateMrPlan(sosCd3, sosCd4, false, mrPlanUpdateDto, category, statusForMrPlan);

			// 検索結果処理
			String thoseiMsg = dpsMrPlanService.createChoseiMsg(searchMrPlan(form));

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPS0007I", thoseiMsg));

		} catch (LogicalException e) {

			// 再検索実行
			searchMrPlan(form);

			throw e;
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
	@Permission(authType = AuthType.REFER)
	public Result dps302C03F20(DpContext ctx, Dps302C03Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C03F20");
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
	private MrPlanResultDto searchMrPlan(Dps302C03Form form) throws Exception {

		String sosCd3 = form.getSosCd3();
		String sosCd4 = form.getSosCd4();
		String prodCode = form.getProdCode();
		InsType insType = form.convertInsType();
		String category = form.getCategory();
		RefDeliveryScope scope = form.convertRefDeliveryScope();

		// データ有無フラグ初期化
		super.getRequestBox().put(Dps302C03Form.DATA_EXIST, false);

		// -------------------------------
		// 検索処理実行
		// -------------------------------
		MrPlanResultDto mrPlanResultDto = null;
		// AL指定の場合
		if (form.getIsTeamRank()) {
			mrPlanResultDto = dpsMrPlanSearchService.searchMrPlan(insType, prodCode, sosCd3, sosCd4, scope);
			// 営業所指定の場合
		} else {
			mrPlanResultDto = dpsMrPlanSearchService.searchMrPlan(insType, prodCode, sosCd3, scope, category);
		}

		// データ有無フラグ設定
		super.getRequestBox().put(Dps302C03Form.DATA_EXIST, true);

		// リクエストに検索結果リストをセット
		super.getRequestBox().put(Dps302C03Form.DPS302C03_DATA_R, (MrPlanResultDto) mrPlanResultDto);

		form.setStatusForMrPlan(mrPlanResultDto.getStatusForMrPlan());

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
	public void dps302C03F10Validation(DpContext ctx, Dps302C03Form form) throws ValidateException {

		// 更新行のNullチェック
		String[] rowIdList = form.getRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}

		for (int i = 0; i < rowIdList.length; i++) {

			String[] rowId = rowIdList[i].split(",", 4);
			if (rowId.length != 4) {
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
	public void dps302C03F15Validation(DpContext ctx, Dps302C03Form form) throws ValidateException {

		// 更新行のNullチェック
		String[] rowIdList = form.getRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}

		for (int i = 0; i < rowIdList.length; i++) {

			String[] rowId = rowIdList[i].split(",", 4);
			if (rowId.length != 4) {
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
		// 最終更新日
		if (!StringUtils.isEmpty(rowIdArr[1]) && !ValidationUtil.isLong(rowIdArr[1])) {
			final String errMsg = "受信パラメータ(最終更新日)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		// 営業所案
		if (!StringUtils.isEmpty(rowIdArr[2]) && !ValidationUtil.isLong(rowIdArr[2])) {
			final String errMsg = "受信パラメータ(営業所案)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		// 決定
		if (!StringUtils.isEmpty(rowIdArr[3]) && !ValidationUtil.isLong(rowIdArr[3])) {
			final String errMsg = "受信パラメータ(決定)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
	}

	/**
	 * グリッドのヘッダ・対象区分を設定する
	 * @param form
	 */
	private void setGridHeader(Dps302C03Form form) {

		// ヘッダの設定
		String header = null;

		// データ区分
		String dataKbn = null;

		if(dpsCodeMasterSearchService.isVaccine(form.getCategory())) {
			//カテゴリ：ワクチンの場合
			dataKbn = DpsCodeMaster.ITV.getDbValue();
		}else {
			//カテゴリ：ワクチン以外の場合
			dataKbn = DpsCodeMaster.IT.getDbValue();
		}

		List<CodeAndValue> list = new ArrayList<CodeAndValue>();
		// 対象区分、対象区分リスト(ワクチン含む）リストを設定
		List<DpsCCdMst> insTypeList = (dpsCodeMasterSearchService.searchCodeByDataKbn(dataKbn));
		for (DpsCCdMst mst :insTypeList) {
			CodeAndValue cad = new CodeAndValue(mst.getDataCd(), mst.getDataName());
			list.add(cad);
		}

		//合計の追加
		list.add(new CodeAndValue("9", "合計"));

		if (list.size() != 0 && list.size() >= 2) {
			//ヘッダ（UH）の設定
			form.setHeaderUh(list.get(0).getValue());
			//ヘッダ（P）の設定
			form.setHeaderP(list.get(1).getValue());

			header = list.get(0).getValue() + ",#cspan,#cspan," + list.get(1).getValue() + ",#cspan,#cspan,合計,#cspan,#cspan";

		}
		//ヘッダの設定
		form.setHeader(header);

		//対象区分の設定
		form.setInsTypes(list);
	}

	@Override
	protected void finalProcessHandler(final DpContext ctx) {

		// リクエストに設定
		super.getRequestBox().put(DpActionForm.DATA_R_SOS_JGI_LBL_INFO, super.getSessionBox().get(DpActionForm.DATA_R_SOS_JGI_LBL_INFO));

		super.finalProcessHandler(ctx);
	}

	public SosJgiLblInfo getDpsSosJgiLblInfo(String sosCd, Integer jgiNo ,String sosMaxSrchGetValue, boolean defaultChangeFlg) throws Exception {

		SosJgiLblInfo retDto = new SosJgiLblInfo();

		// 検索サービス実行
		SosJgiDto sosJgiDto = dpsSosJgiSearchService.search(sosCd, jgiNo, sosMaxSrchGetValue);
		SosMst selectSosMst = sosJgiDto.getSosMstList().get(sosJgiDto.getSosMstList().size() - 1);
		JgiMst selectJgiMst = sosJgiDto.getJgiMst();
		if(selectJgiMst == null){
			retDto.setJgiNo(null);
		}

		retDto.setName(sosJgiDto.getSosJgiName());
		retDto.setSosCd2(sosJgiDto.getSosCd2());
		retDto.setSosCd3(sosJgiDto.getSosCd3());
		retDto.setSosCd4(sosJgiDto.getSosCd4());
		retDto.setJgiNo(retDto.getJgiNo());
		retDto.setInitSosCodeValue(selectSosMst.getSosCd());
		retDto.setBrCode(selectSosMst.getBrCode());
		retDto.setDistCode(selectSosMst.getDistCode());
		retDto.setOncSosFlg(Boolean.toString(BooleanUtils.isTrue(selectSosMst.getOncSosFlg())));
		retDto.setSosSubCategory(selectSosMst.getSosSubCategory());
		retDto.setUnderSosCnt(selectSosMst.getUnderSosCnt().toString());

		// 従業員がワクチンのに参照可能な場合は、ワクチンのみに絞る
		DpUser user = DpUserInfo.getDpUserInfo().getSettingUser();
		// 条件グループの特定
		JknGrp jknGrp = user.getJknGrpList().get(0);
		String vacCtg = null;
		if (jknGrp.getJokenKbn().equals(JokenKbn.VAC_REFER) || jknGrp.getJokenKbn().equals(JokenKbn.VAC_EDIT)) {
			List<DpsCCdMst> codeList = dpsCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.VAC.getDbValue());
			if (codeList.size() > 1) {
				final String errMsg = "計画管理汎用マスタにワクチンのカテゴリコードが複数登録されています。";
				throw new SystemException(new Conveyance(ErrMessageKey.DB_DUPLICATE_ERROR, errMsg));
			}
			vacCtg = codeList.get(0).getDataCd();
		}

		if (selectSosMst.getSosCategoryList() != null) {
			String sosCategoryTmp = "";
			for (int i = 0; i < selectSosMst.getSosCategoryList().size(); i++) {
				// ワクチンのみ参照・更新の場合
				if (vacCtg != null) {
					if (selectSosMst.getSosCategoryList().get(i).getCategoryCd().equals(vacCtg)) {
						sosCategoryTmp = selectSosMst.getSosCategoryList().get(i).getCategoryCd();
						break;
					}
				} else {
					if (i != 0) {
						sosCategoryTmp += ",";
					}
					sosCategoryTmp += selectSosMst.getSosCategoryList().get(i).getCategoryCd();
				}
			}
			retDto.setSosCategory(sosCategoryTmp);
		}

		// デフォルト組織の変更
		if (defaultChangeFlg) {
			BumonRank bumonRank = selectSosMst.getBumonRank();
			if (BumonRank.IEIHON.getDbValue() >= bumonRank.getDbValue()) {
				if (StringUtils.isNotBlank(sosCd) && StringUtils.isNotBlank(selectSosMst.getBrCode())) {
					DpUserInfo.getDpUserInfo().setDefaultSosJgi(null, null);
				}
			} else {
				if (StringUtils.isNotBlank(selectSosMst.getBrCode())) {
					DpUserInfo.getDpUserInfo().setDefaultSosJgi(selectSosMst, selectJgiMst);
				}
			}
		}
		return retDto;
	}
}
