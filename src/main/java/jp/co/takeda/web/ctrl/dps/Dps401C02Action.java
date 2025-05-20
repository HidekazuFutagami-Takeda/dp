package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

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
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.InsWsPlanForVacUpDateModifyAllDto;
import jp.co.takeda.dto.InsWsPlanForVacUpDateResultDto;
import jp.co.takeda.dto.InsWsPlanForVacUpDateResultListDto;
import jp.co.takeda.dto.InsWsPlanForVacUpDateScDto;
import jp.co.takeda.dto.InsWsPlanUpDateModifyDto;
import jp.co.takeda.dto.InsWsPlanUpDateMonNnuScDto;
import jp.co.takeda.dto.InsWsPlanUpDateResultDto;
import jp.co.takeda.dto.InsWsPlanUpDateResultListDto;
import jp.co.takeda.dto.InsWsPlanUpDateScDto;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcSystemManageSearchService;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.service.DpsInsWsPlanForVacSearchService;
import jp.co.takeda.service.DpsInsWsPlanForVacService;
import jp.co.takeda.service.DpsInsWsPlanSearchService;
import jp.co.takeda.service.DpsInsWsPlanService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps401C02Form;

/**
 * Dps401C02((医)施設特約店別計画編集画面)のアクションクラス
 *
 * @author siwamoto
 */
@Controller("Dps401C02Action")
public class Dps401C02Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps401C02Action.class);

	// -------------------------------
	// injection field
	// -------------------------------

	/**
	 * 施設特約店別計画機能 検索系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsWsPlanSearchService")
	protected DpsInsWsPlanSearchService dpsInsWsPlanSearchService;

	/**
	 * 施設特約店別計画機能 更新系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsWsPlanService")
	protected DpsInsWsPlanService dpsInsWsPlanService;

	/**
	 * 施設特約店別計画機能 検索系サービス(ワクチン)
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsWsPlanForVacSearchService")
	protected DpsInsWsPlanForVacSearchService dpsInsWsPlanForVacSearchService;

	/**
	 * カテゴリ 検索サービス
	 */
    @Autowired(required = true)
    @Qualifier("dpsCodeMasterSearchService")
    protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	/**
	 * 納入計画管理検索サービス実装クラス
	 */
    @Autowired(required = true)
    @Qualifier("dpcSystemManageSearchService")
    protected DpcSystemManageSearchService dpcSystemManageSearchService;

	/**
	 * 施設特約店別計画機能 更新系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsWsPlanForVacService")
	protected DpsInsWsPlanForVacService dpsInsWsPlanForVacService;

	// -------------------------------
	// action method
	// -------------------------------

	/**
	 * （非重点品・仕入品）初期表示時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.REFER)
	public Result dps401C02F00(DpContext ctx, Dps401C02Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C02F00");
		}

		// 施設検索モードをクリア
		form.setInsSelectMode(null);

		// 特約店検索モードをクリア
		form.setTmsSelectMode(null);

		// 初期化処理
		form.formInit();

		// 施設特約店〆処理後のメッセージ設定
		SysType sysType = null;
		if(dpsCodeMasterSearchService.isVaccine(form.getCategory())) {
			sysType = SysType.VACCINE;
		}else{
			sysType = SysType.IYAKU;
		}
		SysManage sysManage = dpcSystemManageSearchService.searchSysManage(SysClass.DPS, sysType);
		form.setWsEndFlg(sysManage.getWsEndFlg());
		if (form.getWsEndFlg()) {
			super.addMessage(ctx, new MessageKey("DPS3282E"));
		}

		// 検索処理を実行
		if(dpsCodeMasterSearchService.isVaccine(form.getCategory())) {
			//カテゴリ：ワクチンの場合
			// 検索実行
			InsWsPlanForVacUpDateResultListDto serviceResult = searchForVac(ctx, form);

			// 1件（合計行）のみの場合はデータなし
			List<InsWsPlanForVacUpDateResultDto> detailList = serviceResult.getInsWsPlanForVacUpDateResultDto();
			if(detailList != null && detailList.size() ==1){
				addErrorMessage(ctx, ErrMessageKey.DATA_NOT_FOUND_ERROR);
			}
		}else {
			//カテゴリ：ワクチン以外の場合
			// 検索実行
			InsWsPlanUpDateResultListDto serviceResult = search(ctx, form);

			// 1件（合計行）のみの場合はデータなし
			List<InsWsPlanUpDateResultDto> detailList = serviceResult.getInsWsPlanUpDateResultDto();
			if(detailList != null && detailList.size() ==1){
				addErrorMessage(ctx, ErrMessageKey.DATA_NOT_FOUND_ERROR);
			}
		}

		//グリッドのヘッダを設定
		setGridHeader(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * （非重点品・仕入品）検索時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.REFER)
	public Result dps401C02F01(DpContext ctx, Dps401C02Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C02F01");
		}

		// 施設特約店〆処理後のメッセージ設定
		SysType sysType = null;
		if(dpsCodeMasterSearchService.isVaccine(form.getCategory())) {
			sysType = SysType.VACCINE;
		}else{
			sysType = SysType.IYAKU;
		}
		SysManage sysManage = dpcSystemManageSearchService.searchSysManage(SysClass.DPS, sysType);
		form.setWsEndFlg(sysManage.getWsEndFlg());
		if (form.getWsEndFlg()) {
			super.addMessage(ctx, new MessageKey("DPS3282E"));
		}

		if(dpsCodeMasterSearchService.isVaccine(form.getCategory())) {
			//カテゴリ：ワクチンの場合
			// 検索実行
			InsWsPlanForVacUpDateResultListDto serviceResult = searchForVac(ctx, form);

			// 1件（合計行）のみの場合はデータなし
			List<InsWsPlanForVacUpDateResultDto> detailList = serviceResult.getInsWsPlanForVacUpDateResultDto();
			if(detailList != null && detailList.size() ==1){
				addErrorMessage(ctx, ErrMessageKey.DATA_NOT_FOUND_ERROR);
			}
		}else {
			//カテゴリ：ワクチン以外の場合
			// 検索実行
			InsWsPlanUpDateResultListDto serviceResult = search(ctx, form);

			// 1件（合計行）のみの場合はデータなし
			List<InsWsPlanUpDateResultDto> detailList = serviceResult.getInsWsPlanUpDateResultDto();
			if(detailList != null && detailList.size() ==1){
				addErrorMessage(ctx, ErrMessageKey.DATA_NOT_FOUND_ERROR);
			}
		}

		//グリッドのヘッダを設定
		setGridHeader(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * （重点品）初期表示時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.REFER)
	public Result dps401C02F02(DpContext ctx, Dps401C02Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C02F02");
		}

		// 施設検索モードをクリア
		form.setInsSelectMode(null);

		// 特約店検索モードをクリア
		form.setTmsSelectMode(null);

		// 初期化処理
		form.formInit();

		// 施設特約店〆処理後のメッセージ設定
		SysType sysType = null;
		if(dpsCodeMasterSearchService.isVaccine(form.getCategory())) {
			sysType = SysType.VACCINE;
		}else{
			sysType = SysType.IYAKU;
		}
		SysManage sysManage = dpcSystemManageSearchService.searchSysManage(SysClass.DPS, sysType);
		form.setWsEndFlg(sysManage.getWsEndFlg());
		if (form.getWsEndFlg()) {
			super.addMessage(ctx, new MessageKey("DPS3282E"));
		}

		if(dpsCodeMasterSearchService.isVaccine(form.getCategory())) {
			//カテゴリ：ワクチンの場合
			// 検索実行
			InsWsPlanForVacUpDateResultListDto serviceResult = searchForVac(ctx, form);

			// 1件（合計行）のみの場合はデータなし
			List<InsWsPlanForVacUpDateResultDto> detailList = serviceResult.getInsWsPlanForVacUpDateResultDto();
			if(detailList != null && detailList.size() ==1){
				addErrorMessage(ctx, ErrMessageKey.DATA_NOT_FOUND_ERROR);
			}
		}else {
			//カテゴリ：ワクチン以外の場合
			// 検索実行
			InsWsPlanUpDateResultListDto serviceResult = search(ctx, form);

			// 1件（合計行）のみの場合はデータなし
			List<InsWsPlanUpDateResultDto> detailList = serviceResult.getInsWsPlanUpDateResultDto();
			if(detailList != null && detailList.size() ==1){
				addErrorMessage(ctx, ErrMessageKey.DATA_NOT_FOUND_ERROR);
			}
		}

		//グリッドのヘッダを設定
		setGridHeader(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * （重点品）検索時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.REFER)
	public Result dps401C02F03(DpContext ctx, Dps401C02Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C02F03");
		}

		// 施設特約店〆処理後のメッセージ設定
		SysManage sysManage = DpUserInfo.getDpUserInfo().getSysManage();
		if (sysManage.getWsEndFlg()) {
			super.addMessage(ctx, new MessageKey("DPS3282E"));
		}


		if(dpsCodeMasterSearchService.isVaccine(form.getCategory())) {
			//カテゴリ：ワクチンの場合
			// 検索実行
			InsWsPlanForVacUpDateResultListDto serviceResult = searchForVac(ctx, form);

			// 1件（合計行）のみの場合はデータなし
			List<InsWsPlanForVacUpDateResultDto> detailList = serviceResult.getInsWsPlanForVacUpDateResultDto();
			if(detailList != null && detailList.size() ==1){
				addErrorMessage(ctx, ErrMessageKey.DATA_NOT_FOUND_ERROR);
			}
		}else {
			//カテゴリ：ワクチン以外の場合
			// 検索実行
			InsWsPlanUpDateResultListDto serviceResult = search(ctx, form);

			// 1件（合計行）のみの場合はデータなし
			List<InsWsPlanUpDateResultDto> detailList = serviceResult.getInsWsPlanUpDateResultDto();
			if(detailList != null && detailList.size() ==1){
				addErrorMessage(ctx, ErrMessageKey.DATA_NOT_FOUND_ERROR);
			}
		}

		//グリッドのヘッダを設定
		setGridHeader(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * 特約店追加時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.REFER)
	public Result dps401C02F05(DpContext ctx, Dps401C02Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C02F05");
		}

		// サービス呼び出し
		List<InsWsPlanUpDateResultDto> list = new ArrayList<InsWsPlanUpDateResultDto>();
		InsWsPlanUpDateMonNnuScDto monNnuScDto = form.convertInsWsPlanUpDateMonNnuScDto();
		InsWsPlanUpDateResultDto dto = dpsInsWsPlanSearchService.searchJisseki(monNnuScDto);
		list.add(dto);

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C02F05:dto:" + dto);
		}

		// 結果の設定
		super.getRequestBox().put(Dps401C02Form.DPS401C02F05_DATA_R, (ArrayList<InsWsPlanUpDateResultDto>) list);
		return ActionResult.SUCCESS;
	}

	/**
	 * 特約店追加時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@Permission(authType = AuthType.REFER)
	public Result dps401C02F15(DpContext ctx, Dps401C02Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C05F05");
		}

		// サービス呼び出し
		List<InsWsPlanForVacUpDateResultDto> list = new ArrayList<InsWsPlanForVacUpDateResultDto>();
		InsWsPlanUpDateMonNnuScDto monNnuScDto = form.convertInsWsPlanUpDateMonNnuScDto();
		InsWsPlanForVacUpDateResultDto dto = dpsInsWsPlanForVacSearchService.searchJisseki(monNnuScDto);
		list.add(dto);

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C05F05:dto:" + dto);
		}

		// 結果の設定
		super.getRequestBox().put(Dps401C02Form.DPS401C05F05_DATA_R, (ArrayList<InsWsPlanForVacUpDateResultDto>) list);
		return ActionResult.SUCCESS;
	}


	/**
	 * 登録する処理時(MMP非重点品)に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps401C02F10Execute(DpContext ctx, Dps401C02Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C02F10Execute");
		}
		return execute(ctx, form);
	}

	/**
	 * 登録する処理時(仕入)に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps401C02F11Execute(DpContext ctx, Dps401C02Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C02F11Execute");
		}
		return execute(ctx, form);

	}

	/**
	 * 登録する処理時(MMP重点品)に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps401C02F13Execute(DpContext ctx, Dps401C02Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C02F13Execute");
		}

		return execute(ctx, form);
	}

	/**
	 * 登録する処理時(ワクチン)に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps401C02F15Execute(DpContext ctx, Dps401C02Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C02F15Execute");
		}

		return executeVac(ctx, form);
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
	public void dps401C02F15Validation(DpContext ctx, Dps401C02Form form) throws Exception {
		if (form.getSelectRowIdList() != null) {
			for (int i = 0; i < form.getSelectRowIdList().length; i++) {
				String[] paraArray = ConvertUtil.splitConmma(form.getSelectRowIdList()[i]);
				if (paraArray.length > 6) {
					String valueStr = paraArray[7];
					if (StringUtils.isEmpty(valueStr)) {
						valueStr = null;
					} else {
						final String errMsg = "受信パラメータ(修正計画)が不正：";
						// 登録値 数値チェック
						if (!ValidationUtil.isDouble(valueStr)) {
							throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
						}
						// 登録値 整数チェック
						if (!ValidationUtil.isLong(valueStr)) {
							throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
						}
						// 登録値 桁数チェック
						if (!ValidationUtil.isMaxLength(valueStr, 10)) {
							throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
						}
						// 登録値 最小値チェック
						Long value = ConvertUtil.parseLong(valueStr);
						if (value < 0L) {
							throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
						}
					}
				}
			}
		}
	}

	/**
	 * 検索処理を実行する。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return InsWsPlanUpDateResultListDto
	 * @throws Exception 例外
	 */
	private InsWsPlanUpDateResultListDto search(DpContext ctx, Dps401C02Form form) throws Exception {

		InsWsPlanUpDateScDto scDto = form.convertInsWsPlanUpDateScDto();

		// データ有無フラグ初期化
		super.getRequestBox().put(Dps401C02Form.DATA_EXIST, false);

		InsWsPlanUpDateResultListDto serviceResult = dpsInsWsPlanSearchService.searchInsWsPlanList(scDto);

		// データ有無フラグ設定
		super.getRequestBox().put(Dps401C02Form.DATA_EXIST, true);

		// 結果の設定
		super.getRequestBox().put(Dps401C02Form.DPS401C02_DATA_R, serviceResult);

		return serviceResult;
	}

	/**
	 * 検索処理を実行する。(ワクチン)
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return InsWsPlanUpDateResultListDto
	 * @throws Exception 例外
	 */
	private InsWsPlanForVacUpDateResultListDto searchForVac(DpContext ctx, Dps401C02Form form) throws Exception {

		InsWsPlanForVacUpDateScDto scDto = form.convertInsWsPlanForVacUpDateScDto();

		// データ有無フラグ初期化
		super.getRequestBox().put(Dps401C02Form.DATA_EXIST, false);

		InsWsPlanForVacUpDateResultListDto serviceResult = dpsInsWsPlanForVacSearchService.searchInsWsPlanList(scDto);

		// データ有無フラグ設定
		super.getRequestBox().put(Dps401C02Form.DATA_EXIST, true);

		// 結果の設定
		super.getRequestBox().put(Dps401C02Form.DPS401C05_DATA_R, serviceResult);

		return serviceResult;
	}

	/**
	 * 登録処理を実行する。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @param category 品目カテゴリ
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	private Result execute(DpContext ctx, Dps401C02Form form) throws Exception {

		boolean searchFlg = false;
		try {

			// 登録実行
			InsWsPlanUpDateModifyDto upDateDto = form.convertInsWsPlanUpDateModifyDto();
			dpsInsWsPlanService.entryInsWsPlan(upDateDto);

			// 再検索
			InsWsPlanUpDateResultListDto dto = search(ctx, form);
			searchFlg = true;
			String choseiMsg = dpsInsWsPlanService.createChoseiMsg(dto);

			// 登録完了メッセージの追加
			addMessage(ctx, new MessageKey("DPS0008I", choseiMsg));

		} finally {

			// 再検索
			if (!searchFlg) {
				search(ctx, form);
			}
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 登録処理を実行する。（ワクチン）
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @param category 品目カテゴリ
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	private Result executeVac(DpContext ctx, Dps401C02Form form) throws Exception {

		boolean searchFlg = false;
		try {

			// 登録実行
			InsWsPlanForVacUpDateModifyAllDto upDateDto = form.convertInsWsPlanForVacUpDateModifyAllDto();
			dpsInsWsPlanForVacService.entryAllInsWsPlan(upDateDto);

			InsWsPlanForVacUpDateScDto scDto = form.convertInsWsPlanForVacUpDateScDto();
			InsWsPlanForVacUpDateResultListDto serviceResult = dpsInsWsPlanForVacSearchService.searchInsWsPlanList(scDto);

			// 結果の設定
			super.getRequestBox().put(Dps401C02Form.DPS401C05_DATA_R, serviceResult);

			// 登録完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0006I", "施設特約店別計画"));

		} finally {

			// 再検索
			if (!searchFlg) {
				searchForVac(ctx, form);
			}
		}

		return ActionResult.SUCCESS;
	}

	// -------------------------------
	// validation method
	// -------------------------------

	/**
	 * 画面入力チェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps401C02F10Validation(DpContext ctx, Dps401C02Form form) throws Exception {
		if (form.getSelectRowIdList() == null) {
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, "受信パラメータが不正："));
		}
		for (int i = 0; i < form.getSelectRowIdList().length; i++) {
			String[] paraArray = ConvertUtil.splitConmma(form.getSelectRowIdList()[i]);
			if (paraArray.length > 6) {
				String valueStr = paraArray[7];
				if (StringUtils.isEmpty(valueStr)) {
					valueStr = null;
				} else {
					final String errMsg = "受信パラメータ(修正計画)が不正：";
					// 登録値 数値チェック
					if (!ValidationUtil.isDouble(valueStr)) {
						throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
					}
					// 登録値 整数チェック
					if (!ValidationUtil.isLong(valueStr)) {
						throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
					}
					// 登録値 桁数チェック
					if (!ValidationUtil.isMaxLength(valueStr, 10)) {
						throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
					}
					// 登録値 最小値チェック
					Long value = ConvertUtil.parseLong(valueStr);
					if (value < 0L) {
						throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
					}
				}
			}
		}
	}

	/**
	 * グリッドのヘッダを設定する
	 * @param form
	 */
	private void setGridHeader(Dps401C02Form form) {

		// データ区分
		String dataKbn = null;

		if(dpsCodeMasterSearchService.isVaccine(form.getCategory())) {
			//カテゴリ：ワクチンの場合
			dataKbn = DpsCodeMaster.ITV.getDbValue();
		}else {
			//カテゴリ：ワクチン以外の場合
			dataKbn = DpsCodeMaster.IT.getDbValue();
		}

		List<CodeAndValue> list = new ArrayList<CodeAndValue>();
		// 対象区分、対象区分リスト(ワクチン含む）リストを設定
		List<DpsCCdMst> insTypeList = (dpsCodeMasterSearchService.searchCodeByDataKbn(dataKbn));
		for (DpsCCdMst mst :insTypeList) {
			CodeAndValue cad = new CodeAndValue(mst.getDataCd(), mst.getDataName());
			list.add(cad);
		}

		if (list.size() != 0 && list.size() >= 2) {
			//ヘッダ（UH）
			form.setHeaderUh(list.get(0).getValue());
			//ヘッダ（P）
			form.setHeaderP(list.get(1).getValue());
	    }
	}

}
