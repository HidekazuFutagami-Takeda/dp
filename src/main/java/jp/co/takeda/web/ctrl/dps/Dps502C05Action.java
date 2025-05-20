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
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.TmsTytenPlanAddForVacInputDto;
import jp.co.takeda.dto.TmsTytenPlanAddForVacResultDto;
import jp.co.takeda.dto.TmsTytenPlanAddForVacScDto;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcSystemManageSearchService;
import jp.co.takeda.service.DpsTmsTytenPlanAddForVacService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps502C05Form;

/**
 * Dps502C05((ワ)特約店別計画追加画面)のアクションクラス
 *
 * @author stakeuchi
 */
@Controller("Dps502C05Action")
public class Dps502C05Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps502C05Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * ワクチン特約店別計画追加サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsTmsTytenPlanAddForVacService")
	DpsTmsTytenPlanAddForVacService dpsTmsTytenPlanAddForVacService;

	/**
	 * 納入計画管理検索サービス実装クラス
	 */
    @Autowired(required = true)
    @Qualifier("dpcSystemManageSearchService")
    protected DpcSystemManageSearchService dpcSystemManageSearchService;

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
	@RequestType
	@Permission(authType = EDIT)
//	@Permission(authType = AuthType.EDIT)

	public Result dps502C05F00(DpContext ctx, Dps502C05Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps502C05F00");
		}

		// 特約店検索モードをクリア
		form.setTmsSelectMode(null);

		// 初期化処理
		form.formInit();

		// 施設特約店〆処理フラグ取得（ワクチン用）
		SysManage sysManage = dpcSystemManageSearchService.searchSysManage(SysClass.DPS, SysType.VACCINE);
		form.setWsEndFlg(sysManage.getWsEndFlg());

		return ActionResult.SUCCESS;
	}

	/**
	 * 表示(特約店)処理時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = EDIT)
	public Result dps502C05F05(DpContext ctx, Dps502C05Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps502C05F05");
		}
		// 特約店名を検索
		final String tytenName = dpsTmsTytenPlanAddForVacService.searchTmsTytenName(form.getTytenCd());
		form.setTytenName(tytenName);
		return ActionResult.SUCCESS;
	}

	/**
	 * 表示(エリア特約店G)処理時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = EDIT)
	public Result dps502C05F10(DpContext ctx, Dps502C05Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps502C05F10");
		}
		// エリア特約店G名を検索
		final String areaName = dpsTmsTytenPlanAddForVacService.searchAreaGName(form.getBrCd(), form.getDistCd());
		form.setAreaName(areaName);
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
	@RequestType
	@Permission(authType = EDIT)
	public Result dps502C05F15(DpContext ctx, Dps502C05Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps502C05F15");
		}

		// 検索処理実行
		TmsTytenPlanAddForVacResultDto resultDto = null;
		TmsTytenPlanAddForVacScDto scDto = form.convertTmsTytenPlanAddForVacScDto();

		try {
			resultDto = dpsTmsTytenPlanAddForVacService.searchProdList(scDto);

			// 検索結果をリクエストに設定
			super.getRequestBox().put(Dps502C05Form.DPS502C05_DATA_R_SEARCH_RESULT, resultDto);

			// FORMの設定
			form.setTytenCd(scDto.getTmsTytenCd());
			form.setTytenName(resultDto.getSearchTmsTytenMstUn().getTmsTytenMeiKj());
			// SEARCH_FORMの設定
			form.setAreaName(resultDto.getSearchSosMst().getSosName());
			form.setSearchAreaName(resultDto.getSearchSosMst().getSosName());
			form.setSearchBrCd(scDto.getBrCodeInput());
			form.setSearchDistCd(scDto.getDistCodeInput());
			form.setSearchTytenCd(scDto.getTmsTytenCd());
			form.setSearchTytenName(resultDto.getSearchTmsTytenMstUn().getTmsTytenMeiKj());

		} catch (LogicalException e) {
			form.setSearchAreaName(null);
			form.setSearchBrCd(null);
			form.setSearchDistCd(null);
			form.setSearchTytenCd(null);
			form.setSearchTytenName(null);

			List<MessageKey> errMessgeKeyList = new ArrayList<MessageKey>();
			// 特約店
			try {
				final String areaName = dpsTmsTytenPlanAddForVacService.searchAreaGName(form.getBrCd(), form.getDistCd());
				form.setAreaName(areaName);
			} catch (LogicalException e1) {
				form.setTytenName(null);
				errMessgeKeyList.addAll(e1.getConveyance().getMessageKeyList());
			}

			// エリア特約店Ｇ
			try {
				final String areaName = dpsTmsTytenPlanAddForVacService.searchAreaGName(form.getBrCd(), form.getDistCd());
				form.setAreaName(areaName);
			} catch (LogicalException e2) {
				form.setAreaName(null);
				errMessgeKeyList.addAll(e2.getConveyance().getMessageKeyList());
			}

			if (errMessgeKeyList.size() > 0) {
				throw new LogicalException(new Conveyance(errMessgeKeyList));
			}
		}
		return ActionResult.SUCCESS;
	}

	/**
	 * 登録する処理時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = EDIT)
	public Result dps502C05F16Execute(DpContext ctx, Dps502C05Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps502C05F16Execute");
		}

		TmsTytenPlanAddForVacInputDto inputDto = form.convertTmsTytenPlanAddForVacInputDto();
		try {
			// 特約店別計画登録実行
// mod 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
			dpsTmsTytenPlanAddForVacService.addWsPlanProdList(inputDto, form.getCategory());

			// 登録完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0006I", "特約店別計画"));

		} finally {

			// 検索時の値で、品目一覧検索実行(一度検索しているので成功するはず)
// mod 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
			TmsTytenPlanAddForVacScDto scDto = new TmsTytenPlanAddForVacScDto(form.getSearchTytenCd(), form.getSearchBrCd(), form.getSearchDistCd(), inputDto.getKaBaseKb());
			TmsTytenPlanAddForVacResultDto resultDto = dpsTmsTytenPlanAddForVacService.searchProdList(scDto);

			//  INPUT
			String tmsTytenCd = resultDto.getSearchTmsTytenMstUn().getTmsTytenCd();
			form.setTytenCd(tmsTytenCd);
			form.setTytenName(resultDto.getSearchTmsTytenMstUn().getTmsTytenMeiKj());
			form.setBrCd(resultDto.getSearchSosMst().getBrCode());
			form.setDistCd(resultDto.getSearchSosMst().getDistCode());
			form.setAreaName(resultDto.getSearchSosMst().getSosName());

			// SEARCHINPUT
			form.setSearchTytenCd(scDto.getTmsTytenCd());
			form.setSearchTytenName(resultDto.getSearchTmsTytenMstUn().getTmsTytenMeiKj());
			form.setSearchBrCd(scDto.getBrCodeInput());
			form.setSearchDistCd(scDto.getDistCodeInput());
			form.setSearchAreaName(resultDto.getSearchSosMst().getSosName());

			// 検索結果をリクエストに設定
			super.getRequestBox().put(Dps502C05Form.DPS502C05_DATA_R_SEARCH_RESULT, resultDto);
		}
		return ActionResult.SUCCESS;
	}

	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * 表示（特約店）処理時入力チェック
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps502C05F05Validation(DpContext ctx, Dps502C05Form form) throws ValidateException {
		// 特約店コードチェック
		if (StringUtils.isEmpty(form.getTytenCd())) {
			final String errMsg = "特約店コードが未入力";
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "特約店コード"), errMsg));
		} else {
			if (!ValidationUtil.isLong(form.getTytenCd())) {
				final String errMsg = "特約店コードが半角数値以外";
				throw new ValidateException(new Conveyance(new MessageKey("DPC1004E", "特約店コード", "半角数値"), errMsg));
			}
			if (!ValidationUtil.isMinLength(form.getTytenCd(), 13)) {
				final String errMsg = "特約店コードが13桁未満";
				throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "特約店コード", "13"), errMsg));
			}
			if (!ValidationUtil.isMaxLength(form.getTytenCd(), 13)) {
				final String errMsg = "特約店コードが13桁を超えている";
				throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "特約店コード", "13"), errMsg));
			}
		}
	}

	/**
	 * 表示(エリア特約店G)処理時入力チェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps502C05F10Validation(DpContext ctx, Dps502C05Form form) throws ValidateException {
		// 特約店部コードチェック
		if (StringUtils.isEmpty(form.getBrCd())) {
			final String errMsg = "特約店部コードが未入力";
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "特約店部コード"), errMsg));
		} else {
			if (!ValidationUtil.isLong(form.getBrCd())) {
				final String errMsg = "特約店部コードが半角数値以外";
				throw new ValidateException(new Conveyance(new MessageKey("DPC1004E", "特約店部コード", "半角数値"), errMsg));
			}
			if (!ValidationUtil.isMinLength(form.getBrCd(), 3)) {
				final String errMsg = "特約店部コードが3桁未満";
				throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "特約店部コード", "3"), errMsg));
			}
			if (!ValidationUtil.isMaxLength(form.getBrCd(), 3)) {
				final String errMsg = "特約店部コードが3桁を超えている";
				throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "特約店部コード", "3"), errMsg));
			}
		}
		// エリア特約店Gコードチェック
		if (StringUtils.isEmpty(form.getDistCd())) {
			final String errMsg = "エリア特約店Gコードが未入力";
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "エリア特約店Gコード"), errMsg));
		} else {
			if (!ValidationUtil.isLong(form.getDistCd())) {
				final String errMsg = "エリア特約店Gコードが半角数値以外";
				throw new ValidateException(new Conveyance(new MessageKey("DPC1004E", "エリア特約店Gコード", "半角数値"), errMsg));
			}
			if (!ValidationUtil.isMinLength(form.getDistCd(), 3)) {
				final String errMsg = "エリア特約店Gコードが3桁未満";
				throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "エリア特約店Gコード", "3"), errMsg));
			}
			if (!ValidationUtil.isMaxLength(form.getDistCd(), 3)) {
				final String errMsg = "エリア特約店Gコードが3桁を超えている";
				throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "エリア特約店Gコード", "3"), errMsg));
			}
		}
	}

	/**
	 * 検索時入力チェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps502C05F15Validation(DpContext ctx, Dps502C05Form form) throws ValidateException {
		dps502C05F05Validation(ctx, form);
		dps502C05F10Validation(ctx, form);
	}

	/**
	 * 登録処理時入力チェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps502C05F16Validation(DpContext ctx, Dps502C05Form form) throws ValidateException {
		// 更新行のNullチェック
		String[] rowIdList = form.getRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		for (int i = 0; i < rowIdList.length; i++) {
			// パラーメータのサイズチェック
			String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);
			if (rowId.length != 3) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 品目コード
			if (StringUtils.isEmpty(rowId[1])) {
				final String errMsg = "受信パラメータ(品目コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 計画値チェック
			String plannedValue = rowId[2];
			if (StringUtils.isNotEmpty(plannedValue)) {
				if (!ValidationUtil.isLong(plannedValue)) {
					final String errMsg = "特約店別計画が半角数値以外";
					throw new ValidateException(new Conveyance(new MessageKey("DPC1004E", "特約店別計画", "半角数値"), errMsg));
				}
				if (!ValidationUtil.isMaxLength(plannedValue, 10)) {
					final String errMsg = "特約店別計画が10桁以上";
					throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "特約店別計画", "10"), errMsg));
				}
				if (ConvertUtil.parseLong(plannedValue) < 0) {
					final String errMsg = "特約店別計画が0未満";
					throw new ValidateException(new Conveyance(new MessageKey("DPC1008E", "特約店別計画", "0"), errMsg));
				}
			}
		}
	}
}
