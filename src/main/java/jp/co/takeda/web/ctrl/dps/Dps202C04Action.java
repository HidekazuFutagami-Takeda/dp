package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.exp.ErrMessageKey.*;
import static jp.co.takeda.security.DpAuthority.AuthType.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.JisCodeMstResultDto;
import jp.co.takeda.dto.JisCodeMstScDto;
import jp.co.takeda.dto.SpecialInsPlanDeleteDto;
import jp.co.takeda.dto.SpecialInsPlanForVacResultDto;
import jp.co.takeda.dto.SpecialInsPlanForVacScDto;
import jp.co.takeda.model.div.PlanType;
import jp.co.takeda.security.BusinessType;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcUserSearchService;
import jp.co.takeda.service.DpsJisCodeMstSearchService;
import jp.co.takeda.service.DpsSpecialInsPlanForVacSearchService;
import jp.co.takeda.service.DpsSpecialInsPlanForVacService;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps202C04Form;

/**
 * Dps202C04((ワ)特定施設個別計画施設一覧画面(担当者立案))のアクションクラス
 * 【STEP4 医薬ワクチン統合対応により廃止】
 * @author stakeuchi
 */
@Controller("Dps202C04Action")
@Deprecated
public class Dps202C04Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps202C04Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * ワクチン用特定施設個別計画 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsSpecialInsPlanForVacSearchService")
	protected DpsSpecialInsPlanForVacSearchService dpsSpecialInsPlanForVacSearchService;

	/**
	 * JIS府県・市区町村 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsJisCodeMstSearchService")
	protected DpsJisCodeMstSearchService dpsJisCodeMstSearchService;

	/**
	 * ワクチン用特定施設個別計画 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsSpecialInsPlanForVacService")
	protected DpsSpecialInsPlanForVacService dpsSpecialInsPlanForVacService;

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
	@RequestType(businessType = BusinessType.VACCINE)
	@Permission( authType = REFER)
	public Result dps202C04F00(DpContext ctx, Dps202C04Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps202C04F00");
		}

		// 初期化処理
		form.formInit();

		return ActionResult.SUCCESS;
	}

	/**
	 * 都道府県プルダウン設定時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType(businessType = BusinessType.VACCINE)
	@Permission( authType = REFER)
	public Result dps202C04F05(DpContext ctx, Dps202C04Form form) throws Exception {

		// 検索実行
		searchAddrCodeCity(form);

		// 結果なしに設定
		form.setExistSearchDataFlag(false);

		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType(businessType = BusinessType.VACCINE)
	@Permission( authType = REFER)
	public Result dps202C04F10(DpContext ctx, Dps202C04Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps202C04F10");
			LOG.debug("SosCd3:" + form.getSosCd3());
			LOG.debug("JgiNo:" + form.getJgiNo());
			LOG.debug("ActivityType:" + form.getActivityType());
			LOG.debug("RegType:" + form.getRegType());
			LOG.debug("InsNameZenKana:" + form.getInsNameZenKana());
			LOG.debug("InsNameHanKanah:" + form.getInsNameHanKana());
		}

		// プルダウン検索
		searchAddrCodeCity(form);

		// 検索実行
		form.setTranField();
		searchSpecialInsPlan(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * 削除処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType(businessType = BusinessType.VACCINE)
	@Permission( authType = EDIT)
	public Result dps202C04F15Execute(DpContext ctx, Dps202C04Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps202C04F15Execute");
		}

		// 削除用DTOのリスト作成
		List<SpecialInsPlanDeleteDto> deleteDtoList = form.convertSpecialInsPlanDeleteDto();

		try {

			// 削除実行
			dpsSpecialInsPlanForVacService.deleteSpecialInsPlanForVac(deleteDtoList, PlanType.MR);

			// 削除完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0001I", String.valueOf(deleteDtoList.size())));

		} finally {

			// 削除対象の情報でFormを書き換える
			form.setSosCd3(form.getSosCd3Tran());
			form.setSosCd4(form.getSosCd4Tran());
			form.setJgiNo(form.getJgiNoTran());
			form.setRegType(form.getRegTypeTran());
			form.setInsNameZenKana(form.getInsNameZenKanaTran());
			form.setInsNameHanKana(form.getInsNameHanKanaTran());
			form.setActivityType(form.getActivityTypeTran());
			form.setAddrCodePref(form.getAddrCodePrefTran());
			form.setAddrCodeCity(form.getAddrCodeCityTran());

			// ------------------------------
			// 前回検索条件で再検索
			// ------------------------------
			String targetSosCd = null;

			// チームが指定されていた場合
			if (StringUtils.isNotBlank(form.getSosCd4Tran())) {
				targetSosCd = form.getSosCd4Tran();
			}

			// 営業所が指定されていた場合
			else if (StringUtils.isNotBlank(form.getSosCd3Tran())) {
				targetSosCd = form.getSosCd3Tran();
			}

			// 組織従業員の書き換え処理
			if (StringUtils.isNotBlank(form.getJgiNoTran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, targetSosCd, form.getJgiNoTran());
			} else if (StringUtils.isNotBlank(targetSosCd)) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, targetSosCd, null);
			}

			// プルダウン検索
			searchAddrCodeCity(form);

			// 再検索実行
			searchSpecialInsPlan(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 一覧テーブルの検索処理を実行する。
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchSpecialInsPlan(Dps202C04Form form) throws Exception {

		// 検索条件DTOの生成
		SpecialInsPlanForVacScDto scDto = form.convertSpecialInsPlanForVacScDto();

		// 検索実行
		List<SpecialInsPlanForVacResultDto> resultList;

		try {

			resultList = dpsSpecialInsPlanForVacSearchService.searchSpecialInsPlanForVac(scDto, PlanType.MR);
			form.setExistSearchDataFlag(true);

		} catch (LogicalException e) {

			form.setExistSearchDataFlag(false);
			throw e;
		}

		// リクエストBOXに検索結果リストをセット
		super.getRequestBox().put(Dps202C04Form.DPS202C04_DATA_R, (ArrayList<SpecialInsPlanForVacResultDto>) resultList);
	}

	/**
	 * 市区郡町村の検索処理を実行する。
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchAddrCodeCity(Dps202C04Form form) throws Exception {

		// 検索条件DTOの生成
		JisCodeMstScDto scDto = form.convertJisCodeMstScDto();

		// 検索実行・CodeAndValueに変換
		List<CodeAndValue> resultList = new ArrayList<CodeAndValue>();
		resultList.add(new CodeAndValue("", "")); // 空欄あり
		if (scDto.getAddrCodePref() != null) {
			for (JisCodeMstResultDto resultDto : dpsJisCodeMstSearchService.searchJisCodeMstList(scDto)) {
				CodeAndValue cv = new CodeAndValue(resultDto.getAddrCodeCity(), resultDto.getAddrNameCity());
				resultList.add(cv);
			}
		}

		// リクエストBOXに検索結果リストをセット
		super.getRequestBox().put(Dps202C04Form.DPS202C04_DATA_ADDR_CODE_CITY_LIST, (ArrayList<CodeAndValue>) resultList);
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
	public void dps202C04F10Validation(DpContext ctx, Dps202C04Form form) throws ValidateException {

		// 組織・従業員 必須チェック
		if (StringUtils.isEmpty(form.getSosCd3())) {
			List<String> srcHolderList = new ArrayList<String>();
			srcHolderList.add("組織・従業員");
			srcHolderList.add("エリア特約店G");
			srcHolderList.add("従業員");
			throw new ValidateException(new Conveyance(new MessageKey("DPC1025E", srcHolderList)));
		}

		// 重点先/一般先 必須チェック
		if (StringUtils.isEmpty(form.getActivityType())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "重点先/一般先")));
		}

		// 絞込条件 必須チェック
		if (StringUtils.isEmpty(form.getRegType())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "絞込条件")));
		}
	}

	/**
	 * 削除処理時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps202C04F15Validation(DpContext ctx, Dps202C04Form form) throws ValidateException {

		// 更新行のNullチェック
		String[] rowIdList = form.getSelectRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}

		for (int i = 0; i < rowIdList.length; i++) {

			// パラーメータのサイズチェック
			String[] rowId = rowIdList[i].split(",", 3);
			if (rowId.length != 3) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 施設コード
			if (StringUtils.isEmpty(rowId[0])) {
				final String errMsg = "受信パラメータ(施設コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 従業員番号
			if (StringUtils.isEmpty(rowId[1]) || !ValidationUtil.isInteger(rowId[1])) {
				final String errMsg = "受信パラメータ(従業員番号)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 最終更新日
			if (StringUtils.isEmpty(rowId[2]) || !ValidationUtil.isLong(rowId[2])) {
				final String errMsg = "受信パラメータ(最終更新日)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
		}
	}
}
