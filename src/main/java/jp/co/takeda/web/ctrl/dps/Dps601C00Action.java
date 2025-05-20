package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.exp.ErrMessageKey.VALIDATE_ERROR;
import static jp.co.takeda.model.div.JokenSet.HONBU;
import static jp.co.takeda.model.div.JokenSet.IYAKU_AL;
import static jp.co.takeda.model.div.JokenSet.IYAKU_MR;
import static jp.co.takeda.model.div.JokenSet.OFFICE_MASTER;
import static jp.co.takeda.model.div.JokenSet.OFFICE_STAFF;
import static jp.co.takeda.model.div.JokenSet.SITEN_MASTER;
import static jp.co.takeda.model.div.JokenSet.SITEN_STAFF;
import static jp.co.takeda.security.DpAuthority.AuthType.EDIT;
import static jp.co.takeda.security.DpAuthority.AuthType.REFER;




import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.AuthenticationException;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.InsDocPlanJgiListScDto;
import jp.co.takeda.dto.InsDocPlanJgiListUpdateDto;
import jp.co.takeda.logic.div.InsDocStatusUpdateType;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcUserSearchService;
import jp.co.takeda.service.DpsInsDocPlanSearchService;
import jp.co.takeda.service.DpsInsDocPlanService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps601C00Form;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Dps601C00((医)施設医師別計画担当者一覧画面)のアクションクラス
 *
 * @author siwamoto
 */
@Controller("Dps601C00Action")
public class Dps601C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps601C00Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 施設医師別計画機能 検索系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsDocPlanSearchService")
	protected DpsInsDocPlanSearchService dpsInsDocPlanSearchService;

	/**
	 * 施設医師別計画機能 更新系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsDocPlanService")
	protected DpsInsDocPlanService dpsInsDocPlanService;

	/**
	 * ユーザサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpcUserSearchService")
	protected DpcUserSearchService dpcUserSearchService;

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
	public Result dps601C00F00(DpContext ctx, Dps601C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps601C00F00");
		}

		// MRの場合はエラー
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		DpUser settingUser = userInfo.getSettingUser();
		if (settingUser.isMatch(IYAKU_MR)) {
			throw new AuthenticationException(new Conveyance(ErrMessageKey.AUTHORIZATION_ERROR));
		}

		// クリック行ＩＤ(スクロール高さ）をクリア
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

		// 部門フラグ初期化
		if (settingUser.isMatch(HONBU, SITEN_MASTER, SITEN_STAFF, OFFICE_MASTER, OFFICE_STAFF)) {
			form.setRank("1");
		} else if (settingUser.isMatch(IYAKU_AL)) {
			form.setRank("2");
		} else if (settingUser.isMatch(IYAKU_MR)) {
			form.setRank("3");
		} else {
			form.setRank(null);
		}

		// 検索実行
		form.setTranField();
		searchInsDocPlan(form);

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
	@Permission( authType = REFER)
	public Result dps601C00F05(DpContext ctx, Dps601C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps601C00F05");
		}

		// 部門フラグ初期化
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		DpUser settingUser = userInfo.getSettingUser();
		if (settingUser.isMatch(HONBU, SITEN_MASTER, SITEN_STAFF, OFFICE_MASTER, OFFICE_STAFF)) {
			form.setRank("1");
		} else if (settingUser.isMatch(IYAKU_AL)) {
			form.setRank("2");
		} else if (settingUser.isMatch(IYAKU_MR)) {
			form.setRank("3");
		} else {
			form.setRank(null);
		}

		// 検索実行
		form.setTranField();
		searchInsDocPlan(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * 公開処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission( authType = EDIT)
	public Result dps601C00F16Execute(DpContext ctx, Dps601C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps601C00F16Execute");
		}

		InsDocPlanJgiListScDto scDto = form.convertInsDocPlanMrListScDto();
		String sosCd3 = scDto.getSosCd3();
		String prodCode = scDto.getProdCode();
		List<InsDocPlanJgiListUpdateDto> insDocPlanUpdateDtoList = form.convertInsDocPlanJgiListUpdateDto();

		try {
			// 更新実行
			dpsInsDocPlanService.updateStatus(sosCd3, prodCode, insDocPlanUpdateDtoList, InsDocStatusUpdateType.OPEN);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(insDocPlanUpdateDtoList.size())));

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
			searchInsDocPlan(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 公開解除処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission( authType = EDIT)
	public Result dps601C00F17Execute(DpContext ctx, Dps601C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps601C00F17Execute");
		}
		InsDocPlanJgiListScDto scDto = form.convertInsDocPlanMrListScDto();
		String sosCd3 = scDto.getSosCd3();
		String prodCode = scDto.getProdCode();
		List<InsDocPlanJgiListUpdateDto> insDocPlanUpdateDtoList = form.convertInsDocPlanJgiListUpdateDto();

		try {
			// 更新実行
			dpsInsDocPlanService.updateStatus(sosCd3, prodCode, insDocPlanUpdateDtoList, InsDocStatusUpdateType.OPEN_CANCEL);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(insDocPlanUpdateDtoList.size())));

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
			searchInsDocPlan(form);
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
	@Permission( authType = EDIT)
	public Result dps601C00F18Execute(DpContext ctx, Dps601C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps601C00F18Execute");
		}
		InsDocPlanJgiListScDto scDto = form.convertInsDocPlanMrListScDto();
		String sosCd3 = scDto.getSosCd3();
		String prodCode = scDto.getProdCode();
		List<InsDocPlanJgiListUpdateDto> insDocPlanUpdateDtoList = form.convertInsDocPlanJgiListUpdateDto();

		try {
			// 更新実行
			dpsInsDocPlanService.updateStatus(sosCd3, prodCode, insDocPlanUpdateDtoList, InsDocStatusUpdateType.FIX);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(insDocPlanUpdateDtoList.size())));

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
			searchInsDocPlan(form);
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
	@Permission( authType = EDIT)
	public Result dps601C00F19Execute(DpContext ctx, Dps601C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps601C00F19Execute");
		}

		InsDocPlanJgiListScDto scDto = form.convertInsDocPlanMrListScDto();
		String sosCd3 = scDto.getSosCd3();
		String prodCode = scDto.getProdCode();
		List<InsDocPlanJgiListUpdateDto> insDocPlanUpdateDtoList = form.convertInsDocPlanJgiListUpdateDto();

		try {
			// 更新実行
			dpsInsDocPlanService.updateStatus(sosCd3, prodCode, insDocPlanUpdateDtoList, InsDocStatusUpdateType.FIX_CANCEL);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(insDocPlanUpdateDtoList.size())));

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
			searchInsDocPlan(form);
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
	private void searchInsDocPlan(Dps601C00Form form) throws Exception {

		InsDocPlanJgiListScDto scDto = form.convertInsDocPlanMrListScDto();

		// データ有無フラグ初期化
		super.getRequestBox().put(Dps601C00Form.DATA_EXIST, false);

		// -------------------------------
		// 検索処理実行
		// -------------------------------
		List<Map<String,Object>> resultDto = null;
		// 組織コード（営業所）と品目固定コードが設定されている場合は、検索表示
		if (!StringUtils.isEmpty(scDto.getSosCd3()) && !StringUtils.isEmpty(scDto.getProdCode())) {
			resultDto = dpsInsDocPlanSearchService.searchMrList(scDto);

			// データ有無フラグ設定
			super.getRequestBox().put(Dps601C00Form.DATA_EXIST, true);

			// リクエストに検索結果リストをセット
			super.getRequestBox().put(Dps601C00Form.DPS601C00_DATA_R, (ArrayList<Map<String, Object>>)resultDto);
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
	public void dps601C00F05Validation(DpContext ctx, Dps601C00Form form) throws ValidateException {

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
	public void dps601C00F10Validation(DpContext ctx, Dps601C00Form form) throws ValidateException {

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
}
