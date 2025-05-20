package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.exp.ErrMessageKey.VALIDATE_ERROR;
import static jp.co.takeda.model.div.JokenSet.HONBU;
import static jp.co.takeda.model.div.JokenSet.HONBU_WAKUTIN_G;
import static jp.co.takeda.model.div.JokenSet.TOKUYAKUTEN_G_STAFF;
import static jp.co.takeda.model.div.JokenSet.TOKUYAKUTEN_MASTER;
import static jp.co.takeda.model.div.JokenSet.WAKUTIN_AL;
import static jp.co.takeda.model.div.JokenSet.WAKUTIN_MR;
import static jp.co.takeda.security.BusinessType.VACCINE;
import static jp.co.takeda.security.DpAuthority.AuthType.EDIT;
import static jp.co.takeda.security.DpAuthority.AuthType.OUTPUT;
import static jp.co.takeda.security.DpAuthority.AuthType.REFER;






import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.bean.Constants.TemplateInfo;
import jp.co.takeda.dto.InsWsPlanForVacProdListResultDto;
import jp.co.takeda.dto.InsWsPlanForVacProdListScDto;
import jp.co.takeda.dto.InsWsPlanForVacProdListUpdateDto;
import jp.co.takeda.logic.div.InsWsStatusUpdateType;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcUserSearchService;
import jp.co.takeda.service.DpsInsWsPlanForVacSearchService;
import jp.co.takeda.service.DpsInsWsPlanForVacService;
import jp.co.takeda.service.DpsReportForVacService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.in.dps.Dps401C04Form;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Dps401C04((ワ)施設特約店別計画品目一覧画面)のアクションクラス
 *
 * @author nozaki
 */
@Controller("Dps401C04Action")
public class Dps401C04Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps401C04Action.class);

	// -------------------------------
	// injection field
	// -------------------------------

	/**
	 * 納入計画システムのユーザを扱うサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpcUserSearchService")
	protected DpcUserSearchService dpcUserSearchService;

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

	/**
	 * (ワ)帳票出力サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsReportForVacService")
	protected DpsReportForVacService dpsReportForVacService;

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
	@RequestType(businessType = VACCINE)
	@Permission( authType = REFER)
	public Result dps401C04F00(DpContext ctx, Dps401C04Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C04F00");
		}

		// 初期化処理
		form.formInit();

		// 組織従業員初期化処理
		// 担当者まで選ぶ画面なので不要

		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();

		// 部門フラグ初期化
		// 設定中ユーザ
		DpUser settingUser = userInfo.getSettingUser();

		if (settingUser.isMatch(HONBU, HONBU_WAKUTIN_G)) {
			form.setRank("1");
//		} else if (settingUser.isMatch(TOKUYAKUTEN_G_MASTER, TOKUYAKUTEN_GYOUMU_G)) {
		} else if (settingUser.isMatch(TOKUYAKUTEN_MASTER, TOKUYAKUTEN_G_STAFF)) {

			form.setRank("2");


		} else if (settingUser.isMatch(WAKUTIN_AL)) {
			// 小児科ACの場合 (J19-0010 対応・コメントのみ修正)
			form.setRank("3");
			form.setSosCd3(null);

		} else if (settingUser.isMatch(WAKUTIN_MR)) {
			// 小児科MRの場合、所属エリア特約店Gを取得 (J19-0010 対応・コメントのみ修正)
			form.setRank("4");
			form.setSosCd3(settingUser.getSosCd3());
		} else {
			form.setRank(null);
		}
		// デフォルト従業員
		JgiMst defaultJgiMst = userInfo.getDefaultJgiMst();
		if (defaultJgiMst != null) {
			form.setJgiNo(userInfo.getDefaultJgiMst().getJgiNo().toString());
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
	@RequestType(businessType = VACCINE)
	@Permission( authType = REFER)
	public Result dps401C04F05(DpContext ctx, Dps401C04Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C04F05");
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
	@RequestType(businessType = VACCINE)
	@Permission( authType = EDIT)
	public Result dps401C04F15Execute(DpContext ctx, Dps401C04Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C04F15");
		}

		Integer jgiNo = form.convertInsWsPlanForVacProdListScDto().getJgiNo();
		List<InsWsPlanForVacProdListUpdateDto> insWsPlanUpdateDtoList = form.convertInsWsPlanForVacProdListUpdateDto();

		try {
			// 更新実行
			dpsInsWsPlanForVacService.rehaibun(jgiNo, insWsPlanUpdateDtoList);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0005I", "再配分"));

		} finally {

			// 再検索
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
	@RequestType(businessType = VACCINE)
	@Permission( authType = EDIT)
	public Result dps401C04F16Execute(DpContext ctx, Dps401C04Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C04F16Execute");
		}

		InsWsPlanForVacProdListScDto scDto = form.convertInsWsPlanForVacProdListScDto();
		String sosCd3 = scDto.getSosCd();
		Integer jgiNo = scDto.getJgiNo();
		List<InsWsPlanForVacProdListUpdateDto> insWsPlanUpdateDtoList = form.convertInsWsPlanForVacProdListUpdateDto();

		try {
			// 更新実行
			dpsInsWsPlanForVacService.updateStatus(sosCd3, jgiNo, insWsPlanUpdateDtoList, InsWsStatusUpdateType.OPEN);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(insWsPlanUpdateDtoList.size())));

		} finally {

			// 再検索
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
	@RequestType(businessType = VACCINE)
	@Permission( authType = EDIT)
	public Result dps401C04F17Execute(DpContext ctx, Dps401C04Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C04F17Execute");
		}

		InsWsPlanForVacProdListScDto scDto = form.convertInsWsPlanForVacProdListScDto();
		String sosCd3 = scDto.getSosCd();
		Integer jgiNo = scDto.getJgiNo();
		List<InsWsPlanForVacProdListUpdateDto> insWsPlanUpdateDtoList = form.convertInsWsPlanForVacProdListUpdateDto();

		try {
			// 更新実行
			dpsInsWsPlanForVacService.updateStatus(sosCd3, jgiNo, insWsPlanUpdateDtoList, InsWsStatusUpdateType.OPEN_CANCEL);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(insWsPlanUpdateDtoList.size())));

		} finally {

			// 再検索
			searchInsWsPlan(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 確定(確定)処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType(businessType = VACCINE)
	@Permission( authType = EDIT)
	public Result dps401C04F18Execute(DpContext ctx, Dps401C04Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C04F18Execute");
		}

		InsWsPlanForVacProdListScDto scDto = form.convertInsWsPlanForVacProdListScDto();
		String sosCd3 = scDto.getSosCd();
		Integer jgiNo = scDto.getJgiNo();
		List<InsWsPlanForVacProdListUpdateDto> insWsPlanUpdateDtoList = form.convertInsWsPlanForVacProdListUpdateDto();

		try {
			// 更新実行
			dpsInsWsPlanForVacService.updateStatus(sosCd3, jgiNo, insWsPlanUpdateDtoList, InsWsStatusUpdateType.FIX);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(insWsPlanUpdateDtoList.size())));

		} finally {

			// 再検索
			searchInsWsPlan(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 解除(確定)処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType(businessType = VACCINE)
	@Permission( authType = EDIT)
	public Result dps401C04F19Execute(DpContext ctx, Dps401C04Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C04F19Execute");
		}

		InsWsPlanForVacProdListScDto scDto = form.convertInsWsPlanForVacProdListScDto();
		String sosCd3 = scDto.getSosCd();
		Integer jgiNo = scDto.getJgiNo();
		List<InsWsPlanForVacProdListUpdateDto> insWsPlanUpdateDtoList = form.convertInsWsPlanForVacProdListUpdateDto();

		try {
			// 更新実行
			dpsInsWsPlanForVacService.updateStatus(sosCd3, jgiNo, insWsPlanUpdateDtoList, InsWsStatusUpdateType.FIX_CANCEL);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(insWsPlanUpdateDtoList.size())));

		} finally {

			// 再検索
			searchInsWsPlan(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * (ワ)施設計画出力時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(isDownloadable = true)
	@RequestType(businessType = VACCINE)
	@Permission( authType = OUTPUT)
	public Result dps401C04F20Output(DpContext ctx, Dps401C04Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C04F20Output");
		}
		final String templateRootPath = getRealPath(TemplateInfo.EXCEL_TEMPLATE_PATH.getValue()); // 絶対パスに変換
		final Integer insWsVacJgiNo = Integer.parseInt(form.getJgiNo());
		final ExportResult exportResult = dpsReportForVacService.outputInsWsCityList(templateRootPath, insWsVacJgiNo);
		form.setExportResult(exportResult);
		form.setDownLoadFileName(exportResult.getName());
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
	private void searchInsWsPlan(Dps401C04Form form) throws Exception {

		InsWsPlanForVacProdListScDto scDto = form.convertInsWsPlanForVacProdListScDto();

		// データ有無フラグ初期化
		super.getRequestBox().put(Dps401C04Form.DATA_EXIST, false);
		List<InsWsPlanForVacProdListResultDto> resultList = null;

		// 組織コード（営業所）または従業員番号が設定されている場合は、検索表示
		if (StringUtils.isNotEmpty(scDto.getSosCd()) || scDto.getJgiNo() != null) {
			resultList = dpsInsWsPlanForVacSearchService.searchPlannedProdList(scDto);

			// データ有無フラグ設定
			super.getRequestBox().put(Dps401C04Form.DATA_EXIST, true);

			// リクエストに検索結果リストをセット
			super.getRequestBox().put(Dps401C04Form.DPS401C04_DATA_R, (ArrayList<InsWsPlanForVacProdListResultDto>) resultList);
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
	public void dps401C04F05Validation(DpContext ctx, Dps401C04Form form) throws ValidateException {

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
	public void dps401C04F15Validation(DpContext ctx, Dps401C04Form form) throws ValidateException {

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
}
