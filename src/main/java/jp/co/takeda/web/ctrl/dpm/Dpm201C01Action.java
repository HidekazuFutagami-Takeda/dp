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
import jp.co.takeda.dto.InsPlanForVacHeaderDto;
import jp.co.takeda.dto.InsPlanForVacUpdateDto;
import jp.co.takeda.dto.InsProdPlanForVacResultDto;
import jp.co.takeda.dto.InsProdPlanForVacScDto;
import jp.co.takeda.dto.ManagePlanForVacUpdateResultDto;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;

import jp.co.takeda.service.DpmInsPlanForVacSearchService;
import jp.co.takeda.service.DpmInsPlanForVacService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dpm.Dpm201C01Form;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Dpm201C01((ワ)施設品目別計画編集画面)のアクションクラス
 * 
 * @author stakeuchi
 */
@Controller("Dpm201C01Action")
public class Dpm201C01Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dpm201C01Action.class);

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPM201C01";

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 施設別計画 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmInsPlanForVacSearchService")
	protected DpmInsPlanForVacSearchService dpmInsPlanForVacSearchService;

	/**
	 * 施設別計画 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmInsPlanForVacService")
	protected DpmInsPlanForVacService dpmInsPlanForVacService;

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
	@RequestType(businessType = VACCINE)
	@Permission(authType = REFER)
	public Result dpm201C01F00(DpContext ctx, Dpm201C01Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm201C01F00");
		}

		// 初期化処理
		form.formInit();

		// ログイン情報設定
		setUserInfo(form);

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
	@RequestType(businessType = VACCINE)
	@Permission(authType = REFER)
	public Result dpm201C01F05(DpContext ctx, Dpm201C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm201C01F05");
		}
		form.setTranField();
		searchInsPlanForVacList(ctx, form);

		return ActionResult.SUCCESS;
	}

	/**
	 * 登録処理時に呼ばれるアクションメソッド
	 * 
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType(businessType = VACCINE)
	@Permission(authType = REFER)
	public Result dpm201C01F10Execute(DpContext ctx, Dpm201C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm201C01F10");
		}

		try {
			// 更新DTO
			List<InsPlanForVacUpdateDto> updateDtoList = form.convertInsPlanUpdateDto();

			// 更新実行
			ManagePlanForVacUpdateResultDto resultDto = new ManagePlanForVacUpdateResultDto(0);
			if (!updateDtoList.isEmpty()) {
				resultDto = dpmInsPlanForVacService.updateInsPlan(SCREEN_ID, updateDtoList);
			}

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(resultDto.getUpdateTotalCount())));

		} finally {
			// 再検索実行
			searchInsPlanForVacList(ctx, form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理を実行する。
	 * 
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchInsPlanForVacList(DpContext ctx, Dpm201C01Form form) throws Exception {

		// 実行した検索条件を画面に設定
		InsProdPlanForVacScDto scDto = form.convertInsProdPlanScDto();
		form.setViewField();
		try {

			InsProdPlanForVacResultDto resultDto = dpmInsPlanForVacSearchService.searchInsProdPlan(scDto);
			super.getRequestBox().put(Dpm201C01Form.DPM201C01_DATA_R_SEARCH_RESULT, resultDto);

		} finally {

			// ヘッダー情報取得
			InsPlanForVacHeaderDto headerDto = dpmInsPlanForVacSearchService.searchInsPlanHeader(scDto.getInsNo());
			super.getRequestBox().put(Dpm201C01Form.DPM201C01_DATA_R_HEADER, headerDto);

			// 削除施設の場合はメッセージを追加
			if (headerDto != null && headerDto.IsDeleteIns()) {
				addErrorMessage(ctx, new MessageKey("DPM1005E"));
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
	private void setUserInfo(Dpm201C01Form form) {

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
	 * 検索処理時の入力パラメータチェック
	 * 
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dpm201C01F05Validation(DpContext ctx, Dpm201C01Form form) throws ValidateException {
		// [必須] 施設コード
		if (StringUtils.isEmpty(form.getInsNo())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "施設コード")));
		}
	}

	/**
	 * 登録処理時の入力パラメータチェック
	 * 
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dpm201C01F10Validation(DpContext ctx, Dpm201C01Form form) throws ValidateException {
		// 更新パラーメータチェック
		String[] rowIdList = form.getRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		for (int i = 0; i < rowIdList.length; i++) {
			// パラーメータのサイズチェック
			String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);
			if (rowId.length != 6) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 施設コード
			if (StringUtils.isEmpty(rowId[0])) {
				final String errMsg = "受信パラメータ(施設コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 品目コード
			if (StringUtils.isEmpty(rowId[1])) {
				final String errMsg = "受信パラメータ(品目コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// シーケンスキー
			if (StringUtils.isNotEmpty(rowId[2]) && !ValidationUtil.isLong(rowId[2])) {
				final String errMsg = "受信パラメータ(シーケンスキー)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 最終更新日
			if (StringUtils.isNotEmpty(rowId[3]) && !ValidationUtil.isLong(rowId[3])) {
				final String errMsg = "受信パラメータ(最終更新日)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新前計画値
			if (StringUtils.isNotEmpty(rowId[4]) && !ValidationUtil.isLong(rowId[4])) {
				final String errMsg = "受信パラメータ(計画値(更新前))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 更新後計画値
			if (StringUtils.isNotEmpty(rowId[5]) && !ValidationUtil.isLong(rowId[5])) {
				final String errMsg = "受信パラメータ(計画値(更新後))が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
		}
	}
}
