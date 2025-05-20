package jp.co.takeda.web.ctrl.dpm;

import static jp.co.takeda.a.exp.ErrMessageKey.VALIDATE_ERROR;
import static jp.co.takeda.security.BusinessType.VACCINE;
import static jp.co.takeda.security.DpAuthority.AuthType.REFER;

import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.InsMstResultDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacEntryDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacHeaderDto;
import jp.co.takeda.dto.ManageInsWsPlanProdForVacResultDto;
import jp.co.takeda.dto.ManageInsWsProdPlanForVacScDto;
import jp.co.takeda.dto.ManagePlanForVacUpdateResultDto;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;

import jp.co.takeda.service.DpmInsWsPlanForVacSearchService;
import jp.co.takeda.service.DpmInsWsPlanForVacService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dpm.Dpm401C01Form;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Dpm401C01((ワ)施設特約店品目別計画編集画面)のアクションクラス
 *
 * @author siwamoto
 */
@Controller("Dpm401C01Action")
public class Dpm401C01Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dpm401C01Action.class);

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPM401C01";

	// -------------------------------
	// injection field
	// -------------------------------

	/**
	 * 検索系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmInsWsPlanForVacSearchService")
	protected DpmInsWsPlanForVacSearchService dpmInsWsPlanForVacSearchService;

	/**
	 * 更新系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmInsWsPlanForVacService")
	protected DpmInsWsPlanForVacService dpmInsWsPlanForVacService;

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
	@Permission(authType = REFER)
	public Result dpm401C01F00(DpContext ctx, Dpm401C01Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm401C01F00");
		}

		// 特約店検索モードをクリア
		form.setTmsSelectMode(null);

		// 初期化処理
		form.formInit();

		// ログイン情報設定
		setUserInfo(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * 表示ボタン押下時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType(businessType = VACCINE)
	@Permission(authType = REFER)
	public Result dpm401C01F05(DpContext ctx, Dpm401C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm401C01F05");
		}

		// 検索実行
		form.setTranField();
		searchInsWsPlanForVacList(ctx, form);

		return ActionResult.SUCCESS;
	}

	/**
	 * 登録ボタン押下時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType(businessType = VACCINE)
	@Permission(authType = REFER)
	public Result dpm401C01F10Excecute(DpContext ctx, Dpm401C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm401C01F10Excecute");
		}

		try {
			// 更新DTO
			List<ManageInsWsPlanForVacEntryDto> updateDtoList = form.convertInsWsPlanUpdateDto();

			// 更新実行
			ManagePlanForVacUpdateResultDto resultDto = new ManagePlanForVacUpdateResultDto(0);
			if (!updateDtoList.isEmpty()) {
				resultDto = dpmInsWsPlanForVacService.updateInsWsPlan(SCREEN_ID, updateDtoList);
			}

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(resultDto.getUpdateTotalCount())));

		} finally {
			// 再検索実行
			searchInsWsPlanForVacList(ctx, form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理の実行
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchInsWsPlanForVacList(DpContext ctx, Dpm401C01Form form) throws Exception {

		// 実行した検索条件を画面に設定
		ManageInsWsProdPlanForVacScDto scDto = form.convertInsWsPlanScDto();
		form.setViewField();
		try {

			ManageInsWsPlanProdForVacResultDto resultDto = dpmInsWsPlanForVacSearchService.searchInsWsProdPlan(scDto);
			super.getRequestBox().put(Dpm401C01Form.DPM401C01_DATA_R, resultDto);

		} finally {

			// ヘッダー情報取得
			ManageInsWsPlanForVacHeaderDto headerDto = dpmInsWsPlanForVacSearchService.searchInsWsPlanHeader(scDto.getInsNo(), scDto.getTmsTytenCd());
			super.getRequestBox().put(Dpm401C01Form.DPM401C01_INPUT_DATA_R, headerDto);

			// 削除施設の場合はメッセージを追加
			if (headerDto != null) {
				InsMstResultDto insMstResult = headerDto.getInsMstResultDto();
				if (insMstResult != null && insMstResult.getDelFlg()) {
					addErrorMessage(ctx, new MessageKey("DPM1005E"));
				}
			}

			// ヘッダー施設情報が取得できなかった場合は、ログイン情報設定
			if (headerDto == null) {
				setUserInfo(form);
			}

			form.convertHeaderDto(headerDto);
		}
	}

	/**
	 * ログインユーザの組織情報を設定する。
	 *
	 * @param form ActionForm
	 */
	private void setUserInfo(Dpm401C01Form form) {

		// ワクチンの場合は、組織情報初期化
		DpUser dpUser = DpUserInfo.getDpUserInfo().getSettingUser();
//		if (dpUser.isMatch(JokenSet.TOKUYAKUTEN_MASTER)) {
//			form.setSosCd2(dpUser.getSosCd2());
//			form.setSosCd3(null);
//			form.setSosCd4(null);
//			form.setJgiNo(null);
//		} else if (dpUser.isMatch(JokenSet.TOKUYAKUTEN_G_MASTER, JokenSet.TOKUYAKUTEN_G_STAFF, JokenSet.TOKUYAKUTEN_GYOUMU_G, JokenSet.WAKUTIN_AL)) {
		if (dpUser.isMatch(JokenSet.WAKUTIN_AL)) {
			form.setSosCd2(dpUser.getSosCd2());
			form.setSosCd3(dpUser.getSosCd3());
			form.setSosCd4(null);
			form.setJgiNo(null);
		} else {
			form.setSosCd2(null);
			form.setSosCd3(null);
			form.setSosCd4(null);
			form.setJgiNo(null);
		}
	}

	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * 画面入力チェック
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dpm401C01F05Validation(DpContext ctx, Dpm401C01Form form) throws ValidateException {
		// [必須] 施設コード
		if (StringUtils.isEmpty(form.getInsNo())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "施設コード")));
		}
		// [必須] 特約店コード
		if (StringUtils.isEmpty(form.getTmsTytenCdPart())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "特約店コード")));
		}
		// [必須] 計画
		if (StringUtils.isEmpty(form.getPlanData())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "計画")));
		}
	}

	/**
	 * 更新時のValidationメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dpm401C01F10Validation(DpContext ctx, Dpm401C01Form form) throws ValidateException {
		// 更新パラーメータチェック
		String[] rowIdList = form.getRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		// パラメータチェック
		for (int i = 0; i < rowIdList.length; i++) {
			// パラーメータのサイズチェック
			String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);
			if (rowId.length != 7) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 品目固定コード
			if (StringUtils.isEmpty(rowId[0])) {
				final String errMsg = "受信パラメータ(品目固定コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 施設コード
			if (StringUtils.isEmpty(rowId[1])) {
				final String errMsg = "受信パラメータ(施設コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 特約店コード
			if (StringUtils.isEmpty(rowId[2])) {
				final String errMsg = "受信パラメータ(特約店コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// シーケンスキー
			if (StringUtils.isNotEmpty(rowId[3]) && !ValidationUtil.isLong(rowId[3])) {
				final String errMsg = "受信パラメータ(シーケンスキー)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 最終更新日時
			if (StringUtils.isNotEmpty(rowId[4]) && !ValidationUtil.isLong(rowId[4])) {
				final String errMsg = "受信パラメータ(最終更新日時)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// T価ベース 更新前
			if (StringUtils.isNotEmpty(rowId[5]) && !ValidationUtil.isLong(rowId[5])) {
				final String errMsg = "受信パラメータ(T価ベース 更新前)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// T価ベース 更新後（入力値）
			if (StringUtils.isNotEmpty(rowId[6]) && !ValidationUtil.isLong(rowId[6])) {
				final String errMsg = "受信パラメータ(T価ベース 更新後（入力値）)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
		}
	}
}
