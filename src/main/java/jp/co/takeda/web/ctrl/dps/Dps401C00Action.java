package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.exp.ErrMessageKey.VALIDATE_ERROR;
import static jp.co.takeda.model.div.JokenSet.HONBU;
import static jp.co.takeda.model.div.JokenSet.HONBU_WAKUTIN_G;
import static jp.co.takeda.model.div.JokenSet.IYAKU_MR;
import static jp.co.takeda.model.div.JokenSet.OFFICE_MASTER;
import static jp.co.takeda.model.div.JokenSet.OFFICE_STAFF;
import static jp.co.takeda.model.div.JokenSet.SITEN_MASTER;
import static jp.co.takeda.model.div.JokenSet.SITEN_STAFF;
import static jp.co.takeda.model.div.JokenSet.TOKUYAKUTEN_G_STAFF;
import static jp.co.takeda.model.div.JokenSet.TOKUYAKUTEN_MASTER;
import static jp.co.takeda.security.DpAuthority.AuthType.EDIT;
import static jp.co.takeda.security.DpAuthority.AuthType.REFER;

import java.util.ArrayList;
import java.util.Arrays;
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
import jp.co.takeda.dto.InsWsPlanForVacJgiListUpdateDto;
import jp.co.takeda.dto.InsWsPlanJgiListResultDto;
import jp.co.takeda.dto.InsWsPlanJgiListScDto;
import jp.co.takeda.dto.InsWsPlanJgiListUpdateDto;
import jp.co.takeda.logic.div.InsWsStatusUpdateType;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.JknGrp;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.model.view.SosCtg;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcUserSearchService;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.service.DpsInsWsPlanForVacSearchService;
import jp.co.takeda.service.DpsInsWsPlanForVacService;
import jp.co.takeda.service.DpsInsWsPlanSearchService;
import jp.co.takeda.service.DpsInsWsPlanService;
import jp.co.takeda.service.DpsPlannedCtgSearchService;
import jp.co.takeda.service.DpsSosCtgSearchService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps401C00Form;

/**
 * Dps401C00((医)施設特約店別計画担当者一覧画面)のアクションクラス
 *
 * @author siwamoto
 */
@Controller("Dps401C00Action")
public class Dps401C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps401C00Action.class);

	/**
	 * 計画（1：期別、2：月別）
	 */
	private static final String PLAN_ID = "1";

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPS401C00";

	// -------------------------------
	// injection field
	// -------------------------------
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
	 * ユーザサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpcUserSearchService")
	protected DpcUserSearchService dpcUserSearchService;

	/**
	 * 計画支援の組織カテゴリコード検索
	 */
	@Autowired(required = true)
	@Qualifier("dpsSosCtgSearchService")
	protected DpsSosCtgSearchService dpsSosCtgSearchService;

	/**
	 * ワクチン用施設特約店別計画機能 検索系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsWsPlanForVacSearchService")
	protected DpsInsWsPlanForVacSearchService dpsInsWsPlanForVacSearchService;

	/**
	 * ワクチン用施設特約店別計画機能 更新系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsWsPlanForVacService")
	protected DpsInsWsPlanForVacService dpsInsWsPlanForVacService;

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
	public Result dps401C00F00(DpContext ctx, Dps401C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C00F00");
		}


		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
//		DpUser settingUser = userInfo.getSettingUser();
//		// MRの場合はエラー
//		if (settingUser.isMatch(IYAKU_MR, IYAKU_SEIKEI_MR)) {
//			throw new AuthenticationException(new Conveyance(ErrMessageKey.AUTHORIZATION_ERROR));
//		}

		// クリック行ＩＤをクリア
		form.setClickRowId(null);

		// 初期化処理
		form.formInit();

		// 組織従業員初期化処理
		SosMst defaultSosMst = userInfo.getDefaultSosMst();
		if (defaultSosMst != null) {
			if (defaultSosMst.getBumonRank() == BumonRank.OFFICE_TOKUYAKUTEN_G) {
				form.setSosCd3(defaultSosMst.getSosCd());
				form.setSosCd4(null);

				// 従業員設定をNULLにする
				dpcUserSearchService.changeDefaultSosJgi(null, null, defaultSosMst.getSosCd(), null);

			} else if (defaultSosMst.getBumonRank() == BumonRank.TEAM) {
				form.setSosCd3(defaultSosMst.getUpSosCd());
				form.setSosCd4(defaultSosMst.getSosCd());

				// 従業員設定をNULLにする
				dpcUserSearchService.changeDefaultSosJgi(null, null, defaultSosMst.getSosCd(), null);
			}
		}

		// ログインユーザの組織情報をセット
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {
				// 権限確認コード
				if (user.isSosLvl(SCREEN_ID, SosLvl.BRANCH)) {
					if (!user.getSosCd3().equals(JgiMst.BLANK_SOS_CD)) {
						form.setSosCd3(user.getSosCd3());
					}
				}
				else if (user.isSosLvl(SCREEN_ID, SosLvl.OFFICE)) {
					if (!user.getSosCd3().equals(JgiMst.BLANK_SOS_CD)) {
						form.setSosCd3(user.getSosCd3());
					}
				}
				else if (user.isSosLvl(SCREEN_ID, SosLvl.MR)) {
					if (!user.getSosCd3().equals(JgiMst.BLANK_SOS_CD)) {
						form.setSosCd3(user.getSosCd3());
					}
				}
				// 条件グループの特定
				JknGrp jknGrp = null;
				for(JknGrp tmpJknGrp : user.getJknGrpList()) {
					if(SCREEN_ID.toUpperCase().substring(0, 6).equals(tmpJknGrp.getJknGrpId().getDbValue())) {
						jknGrp = tmpJknGrp;
						break;
					}
				}

				// 部門フラグ初期化
				if (Arrays.asList(HONBU.getDbValue(), HONBU_WAKUTIN_G.getDbValue(), SITEN_MASTER.getDbValue(), SITEN_STAFF.getDbValue(), OFFICE_MASTER.getDbValue(), OFFICE_STAFF.getDbValue()).contains(jknGrp.getJokenSetCd())) {
					form.setRank("1");
				} else if (Arrays.asList(TOKUYAKUTEN_MASTER.getDbValue(), TOKUYAKUTEN_G_STAFF.getDbValue()).contains(jknGrp.getJokenSetCd())) {
					form.setRank("2");
				} else if (jknGrp.getJokenSetCd().equals(IYAKU_MR.getDbValue())) {
					form.setRank("3");
				} else {
					form.setRank(null);
				}

				form.setHonbuUser(Boolean.FALSE);
				// 本部・本部ワクチンGユーザであるか
				if (jknGrp.getJokenSetCd().equals(HONBU.getDbValue()) || jknGrp.getJokenSetCd().equals(HONBU_WAKUTIN_G.getDbValue())) {
					form.setHonbuUser(Boolean.TRUE);
				}
			}
		}

		// 検索実行
		form.setTranField();
		searchInsWsPlan(form);
		//カテゴリリストの設定
		initCategoryList(form);
		// ヘッダの設定
		setGridHeader(form);

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
	public Result dps401C00F05(DpContext ctx, Dps401C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C00F05");
		}

		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		DpUser user = userInfo.getSettingUser();
		if (user != null) {
			// 条件グループの特定
			JknGrp jknGrp = null;
			for(JknGrp tmpJknGrp : user.getJknGrpList()) {
				if(SCREEN_ID.toUpperCase().substring(0, 6).equals(tmpJknGrp.getJknGrpId().getDbValue())) {
					jknGrp = tmpJknGrp;
					break;
				}
			}

			// 部門フラグ初期化
			if (Arrays.asList(HONBU.getDbValue(), HONBU_WAKUTIN_G.getDbValue(), SITEN_MASTER.getDbValue(), SITEN_STAFF.getDbValue(), OFFICE_MASTER.getDbValue(), OFFICE_STAFF.getDbValue()).contains(jknGrp.getJokenSetCd())) {
				form.setRank("1");
			} else if (Arrays.asList(TOKUYAKUTEN_MASTER.getDbValue(), TOKUYAKUTEN_G_STAFF.getDbValue()).contains(jknGrp.getJokenSetCd())) {
				form.setRank("2");
			} else if (jknGrp.getJokenSetCd().equals(IYAKU_MR.getDbValue())) {
				form.setRank("3");
			} else {
				form.setRank(null);
			}
		}

		// 検索実行
		form.setTranField();
		searchInsWsPlan(form);
		//カテゴリリストの設定
		initCategoryList(form);
		// ヘッダの設定
		setGridHeader(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * 再配分(配分状況)処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = EDIT)
	public Result dps401C00F15Execute(DpContext ctx, Dps401C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C00F15Execute");
		}
		InsWsPlanJgiListScDto scDto = form.convertInsWsPlanMrListScDto();
		String sosCd3 = scDto.getSosCd3();
		String prodCode = scDto.getProdCode();
		List<InsWsPlanJgiListUpdateDto> insWsPlanUpdateDtoList = form.convertInsWsPlanJgiListUpdateDto();

		try {
			// 更新実行
			dpsInsWsPlanService.rehaibun(sosCd3, prodCode, insWsPlanUpdateDtoList);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0005I", "再配分"));

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setSosCd3(form.getSosCd3Tran());
			form.setSosCd4(form.getSosCd4Tran());

			// 更新対象の情報で組織従業員を書き換える
			if (StringUtils.isNotBlank(form.getSosCd4Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), null);
			} else if (StringUtils.isNotBlank(form.getSosCd3Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd3Tran(), null);
			}

			// 前回検索条件で再検索実行
			searchInsWsPlan(form);
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
	@Permission(authType = EDIT)
	public Result dps401C00F18Execute(DpContext ctx, Dps401C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C00F18Execute");
		}
		InsWsPlanJgiListScDto scDto = form.convertInsWsPlanMrListScDto();
		String sosCd3 = scDto.getSosCd3();
		String prodCode = scDto.getProdCode();
		List<InsWsPlanJgiListUpdateDto> insWsPlanUpdateDtoList = form.convertInsWsPlanJgiListUpdateDto();

		try {
			// 更新実行
			dpsInsWsPlanService.updateStatus(sosCd3, prodCode, insWsPlanUpdateDtoList, InsWsStatusUpdateType.FIX);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(insWsPlanUpdateDtoList.size())));

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setSosCd3(form.getSosCd3Tran());
			form.setSosCd4(form.getSosCd4Tran());

			// 更新対象の情報で組織従業員を書き換える
			if (StringUtils.isNotBlank(form.getSosCd4Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), null);
			} else if (StringUtils.isNotBlank(form.getSosCd3Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd3Tran(), null);
			}

			// 前回検索条件で再検索実行
			searchInsWsPlan(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 確定解除処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = EDIT)
	public Result dps401C00F19Execute(DpContext ctx, Dps401C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C00F19Execute");
		}

		InsWsPlanJgiListScDto scDto = form.convertInsWsPlanMrListScDto();
		String sosCd3 = scDto.getSosCd3();
		String prodCode = scDto.getProdCode();
		List<InsWsPlanJgiListUpdateDto> insWsPlanUpdateDtoList = form.convertInsWsPlanJgiListUpdateDto();

		try {
			// 更新実行
			dpsInsWsPlanService.updateStatus(sosCd3, prodCode, insWsPlanUpdateDtoList,
					InsWsStatusUpdateType.FIX_CANCEL);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(insWsPlanUpdateDtoList.size())));

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setSosCd3(form.getSosCd3Tran());
			form.setSosCd4(form.getSosCd4Tran());

			// 更新対象の情報で組織従業員を書き換える
			if (StringUtils.isNotBlank(form.getSosCd4Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), null);
			} else if (StringUtils.isNotBlank(form.getSosCd3Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd3Tran(), null);
			}

			// 前回検索条件で再検索実行
			searchInsWsPlan(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 公開(公開)処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = EDIT)
	public Result dps401C00F20Execute(DpContext ctx, Dps401C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C00F20Execute");
		}

		InsWsPlanJgiListScDto scDto = form.convertInsWsPlanMrListScDto();
		String prodCode = scDto.getProdCode();
		List<InsWsPlanForVacJgiListUpdateDto> insWsPlanUpdateDtoList = form.convertInsWsPlanForVacJgiListUpdateDto();

		try {
			// 更新実行
			dpsInsWsPlanForVacService.updateStatus(prodCode, insWsPlanUpdateDtoList, InsWsStatusUpdateType.OPEN);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(insWsPlanUpdateDtoList.size())));

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setSosCd3(form.getSosCd3Tran());
			form.setSosCd4(form.getSosCd4Tran());

			// 更新対象の情報で組織従業員を書き換える
			if (StringUtils.isNotBlank(form.getSosCd4Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), null);
			} else if (StringUtils.isNotBlank(form.getSosCd3Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd3Tran(), null);
			}

			// 前回検索条件で再検索実行
			searchInsWsPlan(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 解除(公開)処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = EDIT)
	public Result dps401C00F21Execute(DpContext ctx, Dps401C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C00F21Execute");
		}
		InsWsPlanJgiListScDto scDto = form.convertInsWsPlanMrListScDto();
		String prodCode = scDto.getProdCode();
		List<InsWsPlanForVacJgiListUpdateDto> insWsPlanUpdateDtoList = form.convertInsWsPlanForVacJgiListUpdateDto();

		try {
			// 更新実行
			dpsInsWsPlanForVacService.updateStatus(prodCode, insWsPlanUpdateDtoList, InsWsStatusUpdateType.OPEN_CANCEL);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(insWsPlanUpdateDtoList.size())));

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setSosCd3(form.getSosCd3Tran());
			form.setSosCd4(form.getSosCd4Tran());

			// 更新対象の情報で組織従業員を書き換える
			if (StringUtils.isNotBlank(form.getSosCd4Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), null);
			} else if (StringUtils.isNotBlank(form.getSosCd3Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd3Tran(), null);
			}

			// 前回検索条件で再検索実行
			searchInsWsPlan(form);
		}

		return ActionResult.SUCCESS;
	}

	// -------------------------------
	// private method
	// -------------------------------

	/**
	 * 検索処理を実行する。
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchInsWsPlan(Dps401C00Form form) throws Exception {

		InsWsPlanJgiListScDto scDto = form.convertInsWsPlanMrListScDto();

		// データ有無フラグ初期化
		super.getRequestBox().put(Dps401C00Form.DATA_EXIST, false);

		// -------------------------------
		// 検索処理実行
		// -------------------------------
		InsWsPlanJgiListResultDto resultDto = null;
		// 組織コード（営業所）と品目固定コードが設定されている場合は、検索表示
		if (!StringUtils.isEmpty(scDto.getSosCd3()) && !StringUtils.isEmpty(scDto.getProdCode())) {
			resultDto = dpsInsWsPlanSearchService.searchMrList(scDto);

			// データ有無フラグ設定
			super.getRequestBox().put(Dps401C00Form.DATA_EXIST, true);

			// リクエストに検索結果リストをセット
			super.getRequestBox().put(Dps401C00Form.DPS401C00_DATA_R, resultDto);
		}
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
	public void dps401C00F05Validation(DpContext ctx, Dps401C00Form form) throws ValidateException {

		// 組織 必須チェック
		if (StringUtils.isEmpty(form.getSosCd3())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "組織")));
		}
		// 品目 必須チェック
		if (StringUtils.isEmpty(form.getProdCode())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "品目")));
		}
	}

	/**
	 * 更新処理時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps401C00F10Validation(DpContext ctx, Dps401C00Form form) throws ValidateException {

		// 更新行のNullチェック
		String[] rowIdList = form.getSelectRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}

		for (int i = 0; i < rowIdList.length; i++) {

			String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);

			// パラーメータのサイズチェック
			if (rowId.length != 3) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 従業員番号
			if (StringUtils.isEmpty(rowId[0]) || !ValidationUtil.isInteger(rowId[0])) {
				final String errMsg = "受信パラメータ(従業員番号)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 従業員氏名
			if (StringUtils.isEmpty(rowId[1])) {
				final String errMsg = "受信パラメータ(従業員氏名)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 最終更新日
			if (!StringUtils.isEmpty(rowId[2]) && !ValidationUtil.isLong(rowId[2])) {
				final String errMsg = "受信パラメータ(最終更新日)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
		}
	}

	/**
	* カテゴリリストの初期設定
	* @param form Dps401C00Form
	*/
	private void initCategoryList(Dps401C00Form form) {
		// 汎用マスタのカテゴリリスト
		List<DpsCCdMst> categoryList = (dpsCodeMasterSearchService.searchCategoryList(null, PLAN_ID));

		// カテゴリリストに表示するリスト
		List<String> targetCategoryAry = new ArrayList<String>();
		// 計画対象カテゴリ領域の設定
		List<String> planLvCtgList = new ArrayList<String>();
		// 計画立案レベル-施設特約店
		planLvCtgList = dpsPlannedCtgSearchService.searchCategoryByPlanLevel(Dps401C00Form.DPS401C00_PLANLEVEL_INS_WS);

		String sosCd = null;
		if (StringUtils.isNotBlank(form.getSosCd3())) {
			// 組織コード：営業所
			sosCd = form.getSosCd3();
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
			if (targetCategoryAry.contains(mst.getDataCd()) && planLvCtgList.contains(mst.getDataCd())) {
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

	/**
	 * グリッドのヘッダを設定する
	 * @param form ActionForm
	 */
	private void setGridHeader(Dps401C00Form form) {

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

		if (list.size() != 0 && list.size() >= 2) {
			//ヘッダ（UH）
			form.setTitleUh(list.get(0).getValue());
			//ヘッダ（P）
			form.setTitleP(list.get(1).getValue());
	    }

		form.setHeader(form.getTitleUh() + ",#cspan,#cspan,#cspan,#cspan,#cspan,#cspan," + form.getTitleP() + ",#cspan,#cspan,#cspan,#cspan,#cspan,#cspan");
	}
}
