package jp.co.takeda.web.ctrl.dpm;

import static jp.co.takeda.a.exp.ErrMessageKey.VALIDATE_ERROR;
import static jp.co.takeda.security.BusinessType.VACCINE;
import static jp.co.takeda.security.DpAuthority.AuthType.EDIT;
import static jp.co.takeda.security.DpAuthority.AuthType.REFER;

import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.ManageInsWsPlanForVacDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacEntryDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacHeaderDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacResultDetailTotalDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacScDto;
import jp.co.takeda.dto.ManagePlanForVacUpdateResultDto;
import jp.co.takeda.model.div.ActivityType;
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
import jp.co.takeda.web.in.dpm.Dpm400C01Form;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Dpm400C01((ワ)施設特約店別計画編集画面)のアクションクラス
 *
 * @author siwamoto
 */
@Controller("Dpm400C01Action")
public class Dpm400C01Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dpm400C01Action.class);

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPM400C01";

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
	public Result dpm400C01F00(DpContext ctx, Dpm400C01Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm400C01F00");
		}

		// 特約店検索モードをクリア
		form.setTmsSelectMode(null);

		// 初期化処理
		form.formInit();

		// ログイン情報設定
		setUserInfo(form);

		// 施設追加実行不可
		super.getRequestBox().put(Dpm400C01Form.DPM400C01_ENABLE_INS_ENTRY, Boolean.FALSE);

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
	public Result dpm400C01F05(DpContext ctx, Dpm400C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm400C01F05");
		}

		// 検索実行
		form.setTranField();
		searchInsWsPlanForVacList(form);

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
	@Permission(authType = EDIT)
	public Result dpm400C01F10Excecute(DpContext ctx, Dpm400C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm400C01F10Excecute");
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
			searchInsWsPlanForVacList(form);
		}
		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理の実行
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchInsWsPlanForVacList(Dpm400C01Form form) throws Exception {

		// 施設追加実行可
		super.getRequestBox().put(Dpm400C01Form.DPM400C01_ENABLE_INS_ENTRY, Boolean.FALSE);

		ManageInsWsPlanForVacScDto scDto = form.convertInsWsPlanScDto();
		form.setViewField();
		ManageInsWsPlanForVacDto resultDto = null;
		try {
			resultDto = dpmInsWsPlanForVacSearchService.searchInsWsPlan(scDto);
			super.getRequestBox().put(Dpm400C01Form.DPM400C01_DATA_R, resultDto);
		} finally {

			// 集計行取得
			ManageInsWsPlanForVacResultDetailTotalDto totalDto = dpmInsWsPlanForVacSearchService.searchInsWsPlanTotal(scDto);
			super.getRequestBox().put(Dpm400C01Form.DPM400C01_DATA_R_TOTAL, totalDto);

			// ヘッダー情報取得
			ManageInsWsPlanForVacHeaderDto headerDto = dpmInsWsPlanForVacSearchService.searchInsWsPlanHeader(scDto.getInsNo(), scDto.getTmsTytenCd());

			// 施設情報が取得できなかった場合は、ログイン情報設定
			if (StringUtils.isNotEmpty(scDto.getInsNo()) && headerDto.getInsMstResultDto() == null) {
				setUserInfo(form);
			}

			// ヘッダー施設情報設定
			form.convertHeaderDto(headerDto);

			// 施設コード、特約店コードが設定されていない場合は、施設追加実行可
			if (StringUtils.isEmpty(scDto.getInsNo()) && StringUtils.isEmpty(scDto.getTmsTytenCd())) {
				super.getRequestBox().put(Dpm400C01Form.DPM400C01_ENABLE_INS_ENTRY, Boolean.TRUE);
			}
		}

	}

	/**
	 * ログインユーザの組織情報を設定する。
	 *
	 * @param form ActionForm
	 */
	private void setUserInfo(Dpm400C01Form form) {
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
	public void dpm400C01F05Validation(DpContext ctx, Dpm400C01Form form) throws ValidateException {
		// [必須] 組織・担当者 or 施設コード
		if (StringUtils.isEmpty(form.getInsNo())) {
			if (StringUtils.isEmpty(form.getSosCd3())) {
				throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "組織・担当者")));
			}
			if (StringUtils.isNotEmpty(form.getActivityType()) && ActivityType.getInstance(form.getActivityType()).equals(ActivityType.IPPAN)) {
				if (StringUtils.isEmpty(form.getJgiNo())) {
					throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "組織・担当者")));
				}
				boolean addrCheck = StringUtils.isEmpty(form.getAddrCodePref()) || StringUtils.isEmpty(form.getAddrCodeCity());
				boolean tytenCheck = StringUtils.isEmpty(form.getTmsTytenCdPart());
				if (addrCheck && tytenCheck) {
					throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "市区町村または特約店コード")));
				}
			} else {
				if (StringUtils.isEmpty(form.getSosCd3())) {
					throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "組織・担当者")));
				}
			}
		}
		// [必須] 品目コード
		if (StringUtils.isEmpty(form.getProdCode())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "品目コード")));
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
	public void dpm400C01F10Validation(DpContext ctx, Dpm400C01Form form) throws ValidateException {
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
			// 施設コード
			if (StringUtils.isNotEmpty(rowId[0]) && !ValidationUtil.isLong(rowId[0])) {
				final String errMsg = "受信パラメータ(施設コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 特約店コード
			if (StringUtils.isEmpty(rowId[1])) {
				final String errMsg = "受信パラメータ(特約店コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 品目固定コード
			if (StringUtils.isNotEmpty(rowId[2]) && !ValidationUtil.isLong(rowId[2])) {
				final String errMsg = "受信パラメータ(品目固定コード)が不正：";
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
