package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.exp.ErrMessageKey.AUTHORIZATION_ERROR;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.AuthenticationException;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.DocDistributionJgiDto;
import jp.co.takeda.dto.DocDistributionParamsDto;
import jp.co.takeda.dto.InsDocPlanProdListScDto;
import jp.co.takeda.dto.InsDocPlanProdListUpdateDto;
import jp.co.takeda.logic.div.InsDocStatusUpdateType;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcUserSearchService;
import jp.co.takeda.service.DpsDocDistributionProdSearchService;
import jp.co.takeda.service.DpsInsDocPlanSearchService;
import jp.co.takeda.service.DpsInsDocPlanService;
import jp.co.takeda.task.DocDistributionJob;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps601C01Form;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Dps601C01((医)施設医師別計画品目一覧画面)のアクションクラス
 *
 * @author siwamoto
 */
@Controller("Dps601C01Action")
public class Dps601C01Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps601C01Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * ユーザサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpcUserSearchService")
	protected DpcUserSearchService dpcUserSearchService;

	/**
	 * 配分機能 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsDocDistributionProdSearchService")
	protected DpsDocDistributionProdSearchService dpsDocDistributionProdSearchService;

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
	 * 配分ジョブ
	 */
	@Autowired(required = true)
	@Qualifier("docDistributionJob")
	protected DocDistributionJob docDistributionJob;

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
	public Result dps601C01F00(DpContext ctx, Dps601C01Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps601C01F00");
		}

		// クリック行ＩＤをクリア
		form.setClickRowId(null);

		// 初期化処理
		form.formInit();

		// 組織従業員初期化処理
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		SosMst defaultSosMst = userInfo.getDefaultSosMst();
		if (defaultSosMst != null) {
			if (defaultSosMst.getBumonRank() == BumonRank.OFFICE_TOKUYAKUTEN_G) {
				form.setSosCd3(defaultSosMst.getSosCd());
				form.setSosCd4(null);
				// ONC-MRの場合
				JgiMst defaultJgiMst = userInfo.getDefaultJgiMst();
				if (defaultJgiMst != null) {
					form.setJgiNo(defaultJgiMst.getJgiNo().toString());
				}
			} else if (defaultSosMst.getBumonRank() == BumonRank.TEAM) {
				form.setSosCd3(defaultSosMst.getUpSosCd());
				form.setSosCd4(defaultSosMst.getSosCd());
				JgiMst defaultJgiMst = userInfo.getDefaultJgiMst();
				if (defaultJgiMst != null) {
					form.setJgiNo(defaultJgiMst.getJgiNo().toString());
				}
			}
		}

		// 部門フラグ初期化
		DpUser settingUser = userInfo.getSettingUser();
		if (settingUser.isMatch(HONBU, SITEN_MASTER, SITEN_STAFF, OFFICE_MASTER, OFFICE_STAFF)) {
			form.setRank("1");
		} else if (settingUser.isMatch(IYAKU_AL)) {
			form.setRank("2");
		} else if (settingUser.isMatch(IYAKU_MR)) {
			form.setRank("3");
			form.setJgiNo(userInfo.getSettingUserJgiNo().toString());
		} else {
			form.setRank(null);
		}

		// 検索実行
		form.setTranField();
		searchInsWsPlan(form);

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
	public Result dps601C01F05(DpContext ctx, Dps601C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps601C01F05");
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
		searchInsWsPlan(form);

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
	@Permission( authType = EDIT)
	public Result dps601C01F15Execute(DpContext ctx, Dps601C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps601C01F15Execute");
		}

		// 副担当モードの場合はエラー
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		DpUser settingUser = userInfo.getSettingUser();
		Integer loginJgiNo = userInfo.getSettingUserJgiNo();
		if (settingUser.isMatch(IYAKU_MR) && !loginJgiNo.toString().equals(form.getJgiNoTran())) {
			final String errMsg = "施設副担当による更新は不可";
			throw new AuthenticationException(new Conveyance(AUTHORIZATION_ERROR, errMsg));
		}

		InsDocPlanProdListScDto scDto = form.convertInsDocPlanProdListScDto();
		String sosCd3 = scDto.getSosCd3();
		Integer jgiNo = scDto.getJgiNo();
//		boolean doPlanClear = form.isDoPlanClear();
		Date statusLastUpdate = null;
		if(StringUtils.isNotEmpty(form.getStatusLastUpdate())){
			statusLastUpdate = new Date(new Long(form.getStatusLastUpdate()));
		}

		try {

			// 配分対象の従業員情報を取得
			List<DocDistributionJgiDto> jgiList = dpsDocDistributionProdSearchService.createDocDistributionJgiList(sosCd3, jgiNo, false, true);

			// 配分処理実行
			DpUser dpUser = DpUserInfo.getDpUserInfo().getLoginUser();
			DocDistributionParamsDto dto = new DocDistributionParamsDto(ctx.getDpMetaInfo(), sosCd3, dpUser, statusLastUpdate, jgiList, false);
			docDistributionJob.execute(dto);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0008I", "施設医師別計画"));

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setSosCd3(form.getSosCd3Tran());
			form.setSosCd4(form.getSosCd4Tran());
			form.setJgiNo(form.getJgiNoTran());

			// 更新対象の情報で組織従業員を書き換える
			if (StringUtils.isNotBlank(form.getJgiNoTran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), form.getJgiNoTran());
			} else if (StringUtils.isNotBlank(form.getSosCd4Tran())) {
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
	public Result dps601C01F16Execute(DpContext ctx, Dps601C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps601C01F16Execute");
		}

		// 副担当モードの場合はエラー
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		DpUser settingUser = userInfo.getSettingUser();
		Integer loginJgiNo = userInfo.getSettingUserJgiNo();
		if (settingUser.isMatch(IYAKU_MR) && !loginJgiNo.toString().equals(form.getJgiNoTran())) {
			final String errMsg = "施設副担当による更新は不可";
			throw new AuthenticationException(new Conveyance(AUTHORIZATION_ERROR, errMsg));
		}

		InsDocPlanProdListScDto scDto = form.convertInsDocPlanProdListScDto();
		String sosCd3 = scDto.getSosCd3();
		String sosCd4 = scDto.getSosCd4();
		Integer jgiNo = scDto.getJgiNo();
		List<InsDocPlanProdListUpdateDto> insDocPlanUpdateDtoList = form.convertInsDocPlanProdListUpdateDtoList();

		try {
			// 更新実行
			dpsInsDocPlanService.updateStatus(sosCd3, sosCd4, jgiNo, insDocPlanUpdateDtoList, InsDocStatusUpdateType.OPEN);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(insDocPlanUpdateDtoList.size())));

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setSosCd3(form.getSosCd3Tran());
			form.setSosCd4(form.getSosCd4Tran());
			form.setJgiNo(form.getJgiNoTran());

			// 更新対象の情報で組織従業員を書き換える
			if (StringUtils.isNotBlank(form.getJgiNoTran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), form.getJgiNoTran());
			} else if (StringUtils.isNotBlank(form.getSosCd4Tran())) {
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
	public Result dps601C01F17Execute(DpContext ctx, Dps601C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps601C01F17Execute");
		}

		// 副担当モードの場合はエラー
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		DpUser settingUser = userInfo.getSettingUser();
		Integer loginJgiNo = userInfo.getSettingUserJgiNo();
		if (settingUser.isMatch(IYAKU_MR) && !loginJgiNo.toString().equals(form.getJgiNoTran())) {
			final String errMsg = "施設副担当による更新は不可";
			throw new AuthenticationException(new Conveyance(AUTHORIZATION_ERROR, errMsg));
		}

		InsDocPlanProdListScDto scDto = form.convertInsDocPlanProdListScDto();
		String sosCd3 = scDto.getSosCd3();
		String sosCd4 = scDto.getSosCd4();
		Integer jgiNo = scDto.getJgiNo();
		List<InsDocPlanProdListUpdateDto> insDocPlanUpdateDtoList = form.convertInsDocPlanProdListUpdateDtoList();

		try {
			// 更新実行
			dpsInsDocPlanService.updateStatus(sosCd3, sosCd4, jgiNo, insDocPlanUpdateDtoList, InsDocStatusUpdateType.OPEN_CANCEL);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(insDocPlanUpdateDtoList.size())));

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setSosCd3(form.getSosCd3Tran());
			form.setSosCd4(form.getSosCd4Tran());
			form.setJgiNo(form.getJgiNoTran());

			// 更新対象の情報で組織従業員を書き換える
			if (StringUtils.isNotBlank(form.getJgiNoTran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), form.getJgiNoTran());
			} else if (StringUtils.isNotBlank(form.getSosCd4Tran())) {
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
	@Permission( authType = EDIT)
	public Result dps601C01F18Execute(DpContext ctx, Dps601C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps601C01F18Execute");
		}

		// 副担当モードの場合はエラー
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		DpUser settingUser = userInfo.getSettingUser();
		Integer loginJgiNo = userInfo.getSettingUserJgiNo();
		if (settingUser.isMatch(IYAKU_MR) && !loginJgiNo.toString().equals(form.getJgiNoTran())) {
			final String errMsg = "施設副担当による更新は不可";
			throw new AuthenticationException(new Conveyance(AUTHORIZATION_ERROR, errMsg));
		}

		InsDocPlanProdListScDto scDto = form.convertInsDocPlanProdListScDto();
		String sosCd3 = scDto.getSosCd3();
		String sosCd4 = scDto.getSosCd4();
		Integer jgiNo = scDto.getJgiNo();
		List<InsDocPlanProdListUpdateDto> insDocPlanUpdateDtoList = form.convertInsDocPlanProdListUpdateDtoList();

		try {
			// 更新実行
			dpsInsDocPlanService.updateStatus(sosCd3, sosCd4, jgiNo, insDocPlanUpdateDtoList, InsDocStatusUpdateType.FIX);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(insDocPlanUpdateDtoList.size())));

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setSosCd3(form.getSosCd3Tran());
			form.setSosCd4(form.getSosCd4Tran());
			form.setJgiNo(form.getJgiNoTran());

			// 更新対象の情報で組織従業員を書き換える
			if (StringUtils.isNotBlank(form.getJgiNoTran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), form.getJgiNoTran());
			} else if (StringUtils.isNotBlank(form.getSosCd4Tran())) {
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
	@Permission( authType = EDIT)
	public Result dps601C01F19Execute(DpContext ctx, Dps601C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps601C01F19Execute");
		}

		// 副担当モードの場合はエラー
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		DpUser settingUser = userInfo.getSettingUser();
		Integer loginJgiNo = userInfo.getSettingUserJgiNo();
		if (settingUser.isMatch(IYAKU_MR) && !loginJgiNo.toString().equals(form.getJgiNoTran())) {
			final String errMsg = "施設副担当による更新は不可";
			throw new AuthenticationException(new Conveyance(AUTHORIZATION_ERROR, errMsg));
		}

		InsDocPlanProdListScDto scDto = form.convertInsDocPlanProdListScDto();
		String sosCd3 = scDto.getSosCd3();
		String sosCd4 = scDto.getSosCd4();
		Integer jgiNo = scDto.getJgiNo();
		List<InsDocPlanProdListUpdateDto> insDocPlanUpdateDtoList = form.convertInsDocPlanProdListUpdateDtoList();

		try {
			// 更新実行
			dpsInsDocPlanService.updateStatus(sosCd3, sosCd4, jgiNo, insDocPlanUpdateDtoList, InsDocStatusUpdateType.FIX_CANCEL);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(insDocPlanUpdateDtoList.size())));

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setSosCd3(form.getSosCd3Tran());
			form.setSosCd4(form.getSosCd4Tran());
			form.setJgiNo(form.getJgiNoTran());

			// 更新対象の情報で組織従業員を書き換える
			if (StringUtils.isNotBlank(form.getJgiNoTran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), form.getJgiNoTran());
			} else if (StringUtils.isNotBlank(form.getSosCd4Tran())) {
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
	private void searchInsWsPlan(Dps601C01Form form) throws Exception {

		InsDocPlanProdListScDto scDto = form.convertInsDocPlanProdListScDto();

		// データ有無フラグ初期化
		super.getRequestBox().put(Dps601C01Form.DATA_EXIST, false);

		// ログインユーザがMRで、検索対象が自分でない場合は副担当モード
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		DpUser settingUser = userInfo.getSettingUser();
		Integer loginJgiNo = userInfo.getSettingUserJgiNo();
		if (settingUser.isMatch(IYAKU_MR) && !loginJgiNo.toString().equals(form.getJgiNoTran())) {
			super.getRequestBox().put(Dps601C01Form.SUB_MR_MODE, true);
		}

		// -------------------------------
		// 検索処理実行
		// -------------------------------
		List<Map<String,Object>> resultList = null;

		// 組織コード(エリア特約店G)が設定されている場合は、検索表示
		if (!StringUtils.isEmpty(scDto.getSosCd3())) {
			resultList = dpsInsDocPlanSearchService.searchPlannedProdList(scDto);

			// データ有無フラグ設定
			super.getRequestBox().put(Dps601C01Form.DATA_EXIST, true);

			// リクエストに検索結果リストをセット
			super.getRequestBox().put(Dps601C01Form.DPS601C01_DATA_R, (ArrayList<Map<String, Object>>) resultList);
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
	public void dps601C01F05Validation(DpContext ctx, Dps601C01Form form) throws ValidateException {

		// 組織 必須チェック
		if (StringUtils.isEmpty(form.getSosCd3())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "組織")));
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
	public void dps601C01F10Validation(DpContext ctx, Dps601C01Form form) throws ValidateException {

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

			// 品目固定コード
			if (StringUtils.isEmpty(rowId[0])) {
				final String errMsg = "受信パラメータ(品目固定コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 品目名称
			if (StringUtils.isEmpty(rowId[1])) {
				final String errMsg = "受信パラメータ(品目名称)が不正：";
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
	 * 配分処理時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps601C01F15Validation(DpContext ctx, Dps601C01Form form) throws ValidateException {

		// 組織 必須チェック
		if (StringUtils.isEmpty(form.getSosCd3Tran())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "組織")));
		}

		// 従業員 必須チェック
		if (StringUtils.isEmpty(form.getJgiNoTran())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "従業員")));
		}

	}
}
