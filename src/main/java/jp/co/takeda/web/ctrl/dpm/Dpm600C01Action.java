package jp.co.takeda.web.ctrl.dpm;

import static jp.co.takeda.a.exp.ErrMessageKey.*;
import static jp.co.takeda.security.BusinessType.*;
import static jp.co.takeda.security.DpAuthority.AuthType.*;

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
import jp.co.takeda.dto.InsPlanForVacHeaderDto;
import jp.co.takeda.dto.InsPlanForVacResultDetailTotalDto;
import jp.co.takeda.dto.InsPlanForVacResultDto;
import jp.co.takeda.dto.InsPlanForVacScDto;
import jp.co.takeda.dto.InsPlanForVacUpdateDto;
import jp.co.takeda.dto.ManagePlanForVacUpdateResultDto;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpmCodeMasterSearchService;
import jp.co.takeda.service.DpmInsPlanForVacSearchService;
import jp.co.takeda.service.DpmInsPlanForVacService;
import jp.co.takeda.service.DpmPlannedCtgSearchService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dpm.Dpm600C01Form;

/**
 * Dpm600C01((ワ)施設別計画編集画面)のアクションクラス
 *
 * @author stakeuchi
 */
@Controller("Dpm600C01Action")
public class Dpm600C01Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dpm600C01Action.class);

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPM600C01";

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

    /**
     * 計画対象カテゴリ領域検索サービス
     */
    @Autowired(required = true)
    @Qualifier("dpmPlannedCtgSearchService")
    DpmPlannedCtgSearchService dpmPlannedCtgSearchService;

	/**
	 * カテゴリ検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmCodeMasterSearchService")
	DpmCodeMasterSearchService dpmCodeMasterSearchService;

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
	public Result dpm600C01F00(DpContext ctx, Dpm600C01Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm600C01F00");
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
	public Result dpm600C01F05(DpContext ctx, Dpm600C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm600C01F05");
		}

		// 検索実行
		form.setTranField();
		searchInsPlanForVacList(form);

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
	@Permission(authType = EDIT)
	public Result dpm600C01F10Execute(DpContext ctx, Dpm600C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm600C01F10");
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
			searchInsPlanForVacList(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理の実行
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchInsPlanForVacList(Dpm600C01Form form) throws Exception {

		// 実行した検索条件を画面に設定
		InsPlanForVacScDto scDto = form.convertInsPlanVacScDto();
		form.setViewField();
		InsPlanForVacResultDto resultDto = null;
		try {
			resultDto = dpmInsPlanForVacSearchService.searchInsPlan(scDto);
			super.getRequestBox().put(Dpm600C01Form.DPM600C01_DATA_R_SEARCH_RESULT, resultDto);
		} finally {

			// 集計行取得
			InsPlanForVacResultDetailTotalDto totalDto = dpmInsPlanForVacSearchService.searchInsPlanTotal(scDto, resultDto);
			super.getRequestBox().put(Dpm600C01Form.DPM600C01_DATA_R_SEARCH_RESULT_TOTAL, totalDto);

			// 施設コードが入力された場合は、ヘッダー情報取得
			if (StringUtils.isNotEmpty(scDto.getInsNo())) {

				// ヘッダー情報取得
				InsPlanForVacHeaderDto headerDto = dpmInsPlanForVacSearchService.searchInsPlanHeader(scDto.getInsNo());

				// ヘッダー施設情報が取得できなかった場合は、ログイン情報設定
				if (headerDto == null) {
					setUserInfo(form);
				}

				// ヘッダー施設情報設定
				form.convertHeaderDto(headerDto);
			}
		}
	}

	/**
	 * ログインユーザの組織情報を設定する。
	 *
	 * @param form ActionForm
	 */
	private void setUserInfo(Dpm600C01Form form) {
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
	public void dpm600C01F05Validation(DpContext ctx, Dpm600C01Form form) throws ValidateException {

		String insNo = form.getInsNo();
		String jgiNo = form.getJgiNo();

		// 従業員番号、施設コードのいずれかは必須
		if (StringUtils.isEmpty(insNo) && StringUtils.isEmpty(jgiNo)) {
			final String errMsg = "施設コードまたは従業員番号がnull。insNo=" + insNo + ",jgiNo=" + jgiNo;
			throw new ValidateException(new Conveyance(new MessageKey("DPC1006E", errMsg)));
		}

		// 施設コードが入力されていない場合
		else if (StringUtils.isEmpty(insNo)) {
			// 活動区分が[一般]
			if (ActivityType.getInstance(form.getActivityType()).equals(ActivityType.IPPAN)) {

				// 市区町村が選択されていない
				if (StringUtils.isEmpty(form.getAddrCodePref()) || StringUtils.isEmpty(form.getAddrCodeCity())) {
					throw new ValidateException(new Conveyance(new MessageKey("DPC1006E", "市区町村")));
				}
			}
		}

		// [必須] 品目コード
		if (StringUtils.isEmpty(form.getProdCode())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "品目コード")));
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
	public void dpm600C01F10Validation(DpContext ctx, Dpm600C01Form form) throws ValidateException {
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
