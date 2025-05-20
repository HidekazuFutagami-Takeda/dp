package jp.co.takeda.web.ctrl.dpm;

import static jp.co.takeda.a.exp.ErrMessageKey.VALIDATE_ERROR;
import static jp.co.takeda.security.BusinessType.VACCINE;
import static jp.co.takeda.security.DpAuthority.AuthType.EDIT;
import static jp.co.takeda.security.DpAuthority.AuthType.REFER;

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
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.ManagePlanForVacUpdateResultDto;
import jp.co.takeda.dto.ManageWsPlanForVacDto;
import jp.co.takeda.dto.ManageWsPlanForVacEntryDto;
import jp.co.takeda.dto.ManageWsPlanForVacHeaderDto;
import jp.co.takeda.dto.ManageWsPlanForVacScDto;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpmTmsTytenPlanForVacSearchService;
import jp.co.takeda.service.DpmTmsTytenPlanForVacService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dpm.Dpm300C01Form;

/**
 * Dpm300C01((ワ)特約店別計画編集画面)のアクションクラス
 *
 * @author siwamoto
 */
@Controller("Dpm300C01Action")
public class Dpm300C01Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dpm300C01Action.class);

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPM300C01";

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 検索系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmTmsTytenPlanForVacSearchService")
	protected DpmTmsTytenPlanForVacSearchService dpmTmsTytenPlanForVacSearchService;

	/**
	 * 更新系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmTmsTytenPlanForVacService")
	protected DpmTmsTytenPlanForVacService dpmTmsTytenPlanForVacService;

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
	public Result dpm300C01F00(DpContext ctx, Dpm300C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm300C01F00");
		}

		// 特約店検索モードをクリア
		form.setTmsSelectMode(null);

		// 初期化処理
		form.formInit();

		return ActionResult.SUCCESS;
	}

	/**
	 * 表示するボタンクリック時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType(businessType = VACCINE)
	@Permission(authType = REFER)
	public Result dpm300C01F05(DpContext ctx, Dpm300C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm300C01F05");
		}
		// 検索実行
		form.setTranField();
		searchResultList(form);
		return ActionResult.SUCCESS;
	}

	/**
	 * 登録ボタンクリック時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType(businessType = VACCINE)
	@Permission(authType = EDIT)
	public Result dpm300C01F10Excecute(DpContext ctx, Dpm300C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm300C01F10Excecute");
		}
		try {
			// 更新実行
			List<ManageWsPlanForVacEntryDto> entryDto = form.convertEntryDto();
			ManagePlanForVacUpdateResultDto resultDto = new ManagePlanForVacUpdateResultDto(0);
			if (!entryDto.isEmpty()) {
				resultDto = dpmTmsTytenPlanForVacService.updateTmsTytenPlan(SCREEN_ID, entryDto);
			}
			// 更新完了メッセージの追加
			final int updateSize = resultDto.getUpdateTotalCount();
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(updateSize)));

		} finally {
			// 再検索実行
			searchResultList(form);
		}
		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理の実行
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchResultList(Dpm300C01Form form) throws Exception {
		// 実行した検索条件を画面に設定
		ManageWsPlanForVacScDto scDto = form.convertScDto();
		form.setViewField();
		try {
			ManageWsPlanForVacDto ServiceResult = dpmTmsTytenPlanForVacSearchService.searchList(scDto);
			super.getRequestBox().put(Dpm300C01Form.DPM300C01_DATA_R, ServiceResult);
		} finally {
			ManageWsPlanForVacHeaderDto headerDto = dpmTmsTytenPlanForVacSearchService.searchHeader(scDto);
			super.getRequestBox().put(Dpm300C01Form.DPM300C01_INPUT_DATA_R, headerDto);
			form.setTmsTytenName(headerDto.getTmsTytenName());
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
	public void dpm300C01F05Validation(DpContext ctx, Dpm300C01Form form) throws ValidateException {
		// [必須] 特約店コード
		if (StringUtils.isEmpty(form.getTmsTytenCdPart())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "特約店コード")));
		}
		// [必須] 品目コード
		if (StringUtils.isEmpty(form.getProdCode())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "品目コード")));
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
	public void dpm300C01F10Validation(DpContext ctx, Dpm300C01Form form) throws ValidateException {
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
			if (rowId.length != 6) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 品目固定コード
			if (StringUtils.isNotEmpty(rowId[0]) && !ValidationUtil.isInteger(rowId[0])) {
				final String errMsg = "受信パラメータ(品目固定コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 特約店コード
			if (StringUtils.isEmpty(rowId[1])) {
				final String errMsg = "受信パラメータ(特約店コード)が不正：";
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
