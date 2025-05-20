package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.exp.ErrMessageKey.VALIDATE_ERROR;
import static jp.co.takeda.model.div.JokenSet.HONBU;
import static jp.co.takeda.model.div.JokenSet.HONBU_WAKUTIN_G;
import static jp.co.takeda.model.div.JokenSet.TOKUYAKUTEN_G_STAFF;
import static jp.co.takeda.model.div.JokenSet.TOKUYAKUTEN_MASTER;
import static jp.co.takeda.model.div.JokenSet.WAKUTIN_AL;
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
import jp.co.takeda.dto.InsWsPlanForVacJgiListResultDto;
import jp.co.takeda.dto.InsWsPlanForVacJgiListScDto;
import jp.co.takeda.dto.InsWsPlanForVacJgiListUpdateDto;
import jp.co.takeda.logic.div.InsWsStatusUpdateType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsInsWsPlanForVacSearchService;
import jp.co.takeda.service.DpsInsWsPlanForVacService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps401C03Form;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Dps401C03((ワ)施設特約店別計画担当者一覧画面)のアクションクラス
 *
 * @author nozaki
 */
@Controller("Dps401C03Action")
public class Dps401C03Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps401C03Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
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
	@RequestType(businessType = VACCINE)
	@Permission( authType = REFER)
	public Result dps401C03F00(DpContext ctx, Dps401C03Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C03F00");
		}

		// 初期化処理
		form.formInit();

		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		DpUser settingUser = userInfo.getSettingUser();

		// 組織情報初期化

		// 部門フラグ初期化
		if (settingUser.isMatch(HONBU, HONBU_WAKUTIN_G)) {
			form.setRank("1");
			form.setSosCd2(null);
			form.setSosCd3(null);
			form.setSosCd4(null);
		} else if (settingUser.isMatch(TOKUYAKUTEN_MASTER, TOKUYAKUTEN_G_STAFF)) {
			form.setRank("2");
			form.setSosCd2(null);
			form.setSosCd3(null);
			form.setSosCd4(null);
//		} else if (settingUser.isMatch(TOKUYAKUTEN_G_STAFF, TOKUYAKUTEN_G_MASTER, TOKUYAKUTEN_GYOUMU_G)) {
		} else if (settingUser.isMatch(WAKUTIN_AL)) {
			form.setRank("3");
			form.setSosCd2(settingUser.getSosCd2());
			form.setSosCd3(settingUser.getSosCd3());
			form.setSosCd4(null);
//		} else if (settingUser.isMatch(WAKUTIN_AL)) {
//			form.setRank("3");
//			form.setSosCd2(settingUser.getSosCd2());
//			form.setSosCd3(settingUser.getSosCd3());
//			form.setSosCd4(settingUser.getSosCd4());
		} else {
			form.setRank(null);
			form.setSosCd2(null);
			form.setSosCd3(null);
			form.setSosCd4(null);
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
	public Result dps401C03F05(DpContext ctx, Dps401C03Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C03F05");
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
	public Result dps401C03F15Execute(DpContext ctx, Dps401C03Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C03F15");
		}

		String prodCode = form.convertInsWsPlanForVacJgiListScDto().getProdCode();
		List<InsWsPlanForVacJgiListUpdateDto> insWsPlanUpdateDtoList = form.convertInsWsPlanForVacJgiListUpdateDto();

		try {
			// 更新実行
			dpsInsWsPlanForVacService.rehaibun(prodCode, insWsPlanUpdateDtoList);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0005I", "再配分"));

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setSosCd2(form.getSosCd2Tran());
			form.setSosCd3(form.getSosCd3Tran());
			form.setProdCode(form.getProdCodeTran());

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
	@RequestType(businessType = VACCINE)
	@Permission( authType = EDIT)
	public Result dps401C03F16Execute(DpContext ctx, Dps401C03Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C03F16Execute");
		}

		String prodCode = form.convertInsWsPlanForVacJgiListScDto().getProdCode();
		List<InsWsPlanForVacJgiListUpdateDto> insWsPlanUpdateDtoList = form.convertInsWsPlanForVacJgiListUpdateDto();

		try {
			// 更新実行
			dpsInsWsPlanForVacService.updateStatus(prodCode, insWsPlanUpdateDtoList, InsWsStatusUpdateType.OPEN);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(insWsPlanUpdateDtoList.size())));

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setSosCd2(form.getSosCd2Tran());
			form.setSosCd3(form.getSosCd3Tran());
			form.setProdCode(form.getProdCodeTran());

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
	@RequestType(businessType = VACCINE)
	@Permission( authType = EDIT)
	public Result dps401C03F17Execute(DpContext ctx, Dps401C03Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C03F17Execute");
		}

		String prodCode = form.convertInsWsPlanForVacJgiListScDto().getProdCode();
		List<InsWsPlanForVacJgiListUpdateDto> insWsPlanUpdateDtoList = form.convertInsWsPlanForVacJgiListUpdateDto();

		try {
			// 更新実行
			dpsInsWsPlanForVacService.updateStatus(prodCode, insWsPlanUpdateDtoList, InsWsStatusUpdateType.OPEN_CANCEL);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(insWsPlanUpdateDtoList.size())));

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setSosCd2(form.getSosCd2Tran());
			form.setSosCd3(form.getSosCd3Tran());
			form.setProdCode(form.getProdCodeTran());

			// 前回検索条件で再検索実行
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
	public Result dps401C03F18Execute(DpContext ctx, Dps401C03Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C03F18Execute");
		}

		String prodCode = form.convertInsWsPlanForVacJgiListScDto().getProdCode();
		List<InsWsPlanForVacJgiListUpdateDto> insWsPlanUpdateDtoList = form.convertInsWsPlanForVacJgiListUpdateDto();

		try {
			// 更新実行
			dpsInsWsPlanForVacService.updateStatus(prodCode, insWsPlanUpdateDtoList, InsWsStatusUpdateType.FIX);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(insWsPlanUpdateDtoList.size())));

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setSosCd2(form.getSosCd2Tran());
			form.setSosCd3(form.getSosCd3Tran());
			form.setProdCode(form.getProdCodeTran());

			// 前回検索条件で再検索実行
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
	public Result dps401C03F19Execute(DpContext ctx, Dps401C03Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C03F19Execute");
		}

		String prodCode = form.convertInsWsPlanForVacJgiListScDto().getProdCode();
		List<InsWsPlanForVacJgiListUpdateDto> insWsPlanUpdateDtoList = form.convertInsWsPlanForVacJgiListUpdateDto();

		try {
			// 更新実行
			dpsInsWsPlanForVacService.updateStatus(prodCode, insWsPlanUpdateDtoList, InsWsStatusUpdateType.FIX_CANCEL);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(insWsPlanUpdateDtoList.size())));

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setSosCd2(form.getSosCd2Tran());
			form.setSosCd3(form.getSosCd3Tran());
			form.setProdCode(form.getProdCodeTran());

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
	private void searchInsWsPlan(Dps401C03Form form) throws Exception {

		InsWsPlanForVacJgiListScDto scDto = form.convertInsWsPlanForVacJgiListScDto();

		// データ有無フラグ初期化
		super.getRequestBox().put(Dps401C03Form.DATA_EXIST, false);

		// 品目固定コードが設定されている場合は、検索表示
		if (!StringUtils.isBlank(scDto.getProdCode())) {
			InsWsPlanForVacJgiListResultDto resultDto = dpsInsWsPlanForVacSearchService.searchMrList(scDto);

			// データ有無フラグ設定
			super.getRequestBox().put(Dps401C03Form.DATA_EXIST, true);

			// リクエストに検索結果リストをセット
			super.getRequestBox().put(Dps401C03Form.DPS401C03_DATA_R, (InsWsPlanForVacJgiListResultDto) resultDto);
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
	public void dps401C03F05Validation(DpContext ctx, Dps401C03Form form) throws ValidateException {

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
	public void dps401C03F15Validation(DpContext ctx, Dps401C03Form form) throws ValidateException {

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
