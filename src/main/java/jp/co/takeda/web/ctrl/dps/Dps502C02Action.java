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
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.TmsTytenPlanAddInputDto;
import jp.co.takeda.dto.TmsTytenPlanAddResultDto;
import jp.co.takeda.dto.TmsTytenPlanAddScDto;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.view.SosCtg;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.service.DpsPlannedCtgSearchService;
import jp.co.takeda.service.DpsSosCtgSearchService;
import jp.co.takeda.service.DpsTmsTytenPlanAddService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps502C02Form;

/**
 * Dps502C02((医)特約店別計画追加画面)のアクションクラス
 *
 * @author yokokawa
 */
@Controller("Dps502C02Action")
public class Dps502C02Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps502C02Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 特約店別計画追加サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsTmsTytenPlanAddService")
	DpsTmsTytenPlanAddService dpsTmsTytenPlanAddService;

	/**
	 * カテゴリ 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	/**
	 * 計画対象カテゴリ領域の検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsPlannedCtgSearchService")
	protected DpsPlannedCtgSearchService dpsPlannedCtgSearchService;

	/**
	 * 計画支援の組織カテゴリコード検索
	 */
	@Autowired(required = true)
	@Qualifier("dpsSosCtgSearchService")
	protected DpsSosCtgSearchService dpsSosCtgSearchService;

	/**
	 * 計画（1：期別、2：月別）
	 */
	private static final String PLAN_ID = "1";

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPS502C02";

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
	@RequestType
//	@Permission( authType = EDIT)
	@Permission(authType = EDIT)
	public Result dps502C02F00(DpContext ctx, Dps502C02Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps502C02F00");
		}

		// 特約店検索モードをクリア
		form.setTmsSelectMode(null);

		// 初期化処理
		form.formInit();
		//カテゴリリストの設定
		initCategoryList(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * 表示(特約店)処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = EDIT)
	public Result dps502C02F05(DpContext ctx, Dps502C02Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps502C02F05");
		}

		// 特約店名を検索
		String tmsTytenName = dpsTmsTytenPlanAddService.searchTmsTytenName(form.getTmsTytenCd());
		form.setTmsTytenName(tmsTytenName);

		return ActionResult.SUCCESS;
	}

	/**
	 * 表示(営業所)処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = EDIT)
	public Result dps502C02F10(DpContext ctx, Dps502C02Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps502C02F10");
		}

		// 営業所名を検索
		String officeName = dpsTmsTytenPlanAddService.searchOfficeName(form.getBrCodeInput(), form.getDistCodeInput());
		form.setOfficeName(officeName);

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
	@RequestType
	@Permission(authType = EDIT)
	public Result dps502C02F15(DpContext ctx, Dps502C02Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps502C02F15");
		}

		// 品目一覧検索実行
		TmsTytenPlanAddResultDto resultDto = null;
		TmsTytenPlanAddScDto scDto = form.convertTmsTytenPlanAddScDto();
		try {
			//			resultDto = dpsTmsTytenPlanAddService.searchProdList(scDto);
			resultDto = dpsTmsTytenPlanAddService.searchProdList(scDto, form.getCategory());

			// 営業所名を検索
			String officeName = dpsTmsTytenPlanAddService.searchOfficeName(scDto.getBrCodeInput(),
					scDto.getDistCodeInput());

			// FORMの設定
			form.setTmsTytenName(resultDto.getSearchTmsTytenMstUn().getTmsTytenMeiKj());
			form.setOfficeName(officeName);
			// SEARCH_FORMの設定
			form.setSearchTmsTytenCd(scDto.getTmsTytenCd());
			form.setSearchTmsTytenName(resultDto.getSearchTmsTytenMstUn().getTmsTytenMeiKj());
			form.setSearchBrCodeInput(scDto.getBrCodeInput());
			form.setSearchDistCodeInput(scDto.getDistCodeInput());
			form.setSearchOfficeName(resultDto.getSearchSosMst().getSosName());

			// 検索結果をリクエストに設定
			super.getRequestBox().put(Dps502C02Form.DPS502C02_DATA_R, resultDto);

		} catch (LogicalException e) {

			form.setSearchTmsTytenCd(null);
			form.setSearchTmsTytenName(null);
			form.setSearchBrCodeInput(null);
			form.setSearchDistCodeInput(null);
			form.setSearchOfficeName(null);

			List<MessageKey> errMessgeKeyList = new ArrayList<MessageKey>();
			if (e.getConveyance() != null && e.getConveyance().getMessageKeyList().size() > 0) {
				errMessgeKeyList.addAll(e.getConveyance().getMessageKeyList());
			}

			// 特約店
			try {
				String tmsTytenName = dpsTmsTytenPlanAddService.searchTmsTytenName(scDto.getTmsTytenCd());
				form.setTmsTytenName(tmsTytenName);
			} catch (LogicalException e1) {
				form.setTmsTytenName(null);
				// 検索時に同じエラーメッセージが設定されているはず
				//				errMessgeKeyList.addAll(e1.getConveyance().getMessageKeyList());
			}

			// 営業所
			try {
				String officeName = dpsTmsTytenPlanAddService.searchOfficeName(scDto.getBrCodeInput(),
						scDto.getDistCodeInput());
				form.setOfficeName(officeName);
			} catch (LogicalException e2) {
				form.setOfficeName(null);
				// 検索時に同じエラーメッセージが設定されているはず
				//				errMessgeKeyList.addAll(e2.getConveyance().getMessageKeyList());
			}

			if (errMessgeKeyList.size() > 0) {
				throw new LogicalException(new Conveyance(errMessgeKeyList));
			}
		}
		//カテゴリリストの設定
		initCategoryList(form);
		return ActionResult.SUCCESS;
	}

	/**
	 * 登録する処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = EDIT)
	public Result dps502C02F16Execute(DpContext ctx, Dps502C02Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps502C02F16Execute");
		}

		TmsTytenPlanAddInputDto inputDto = form.convertTmsTytenPlanAddInputDto();
		try {
			// 特約店別計画登録実行
			dpsTmsTytenPlanAddService.addWsPlanProdList(inputDto, form.getCategory());

			// 登録完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0006I", "特約店別計画"));

		} finally {

			// 検索時の値で、品目一覧検索実行(一度検索しているので成功するはず)
			TmsTytenPlanAddScDto scDto = new TmsTytenPlanAddScDto(form.getSearchTmsTytenCd(), form.getSearchBrCodeInput(), form.getSearchDistCodeInput(), inputDto.getKaBaseKb());
			TmsTytenPlanAddResultDto resultDto = dpsTmsTytenPlanAddService.searchProdList(scDto, form.getCategory());

			// 営業所名を検索
			String officeName = dpsTmsTytenPlanAddService.searchOfficeName(scDto.getBrCodeInput(), scDto.getDistCodeInput());


			// INPUT
			String tmsTytenCd = resultDto.getSearchTmsTytenMstUn().getTmsTytenCd();
			form.setTmsTytenCd(tmsTytenCd);
			form.setTmsTytenName(resultDto.getSearchTmsTytenMstUn().getTmsTytenMeiKj());
			form.setBrCodeInput(resultDto.getSearchSosMst().getBrCode());
			form.setDistCodeInput(resultDto.getSearchSosMst().getDistCode());
			form.setOfficeName(officeName);

			// SEARCHINPUT
			form.setSearchTmsTytenCd(scDto.getTmsTytenCd());
			form.setSearchTmsTytenName(resultDto.getSearchTmsTytenMstUn().getTmsTytenMeiKj());
			form.setSearchBrCodeInput(scDto.getBrCodeInput());
			form.setSearchDistCodeInput(scDto.getDistCodeInput());
			form.setSearchOfficeName(resultDto.getSearchSosMst().getSosName());

			// 検索結果をリクエストに設定
			super.getRequestBox().put(Dps502C02Form.DPS502C02_DATA_R, resultDto);
		}

		return ActionResult.SUCCESS;
	}

	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * 表示(特約店)処理時入力チェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps502C02F05Validation(DpContext ctx, Dps502C02Form form) throws ValidateException {
		// 特約店コードチェック
		if (StringUtils.isEmpty(form.getTmsTytenCd())) {
			final String errMsg = "特約店コードが未入力";
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "特約店コード"), errMsg));

		} else if (StringUtils.isNotEmpty(form.getTmsTytenCd())) {
			if (!ValidationUtil.isLong(form.getTmsTytenCd())) {
				final String errMsg = "特約店コードが半角数値以外";
				throw new ValidateException(new Conveyance(new MessageKey("DPC1004E", "特約店コード", "半角数値"), errMsg));
			}

			if (!ValidationUtil.isMinLength(form.getTmsTytenCd(), 13)) {
				final String errMsg = "特約店コードが13桁未満";
				throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "特約店コード", "13"), errMsg));
			}

			if (!ValidationUtil.isMaxLength(form.getTmsTytenCd(), 13)) {
				final String errMsg = "特約店コードが13桁を超えている";
				throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "特約店コード", "13"), errMsg));
			}
		}
	}

	/**
	 * 表示(営業所)処理時入力チェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps502C02F10Validation(DpContext ctx, Dps502C02Form form) throws ValidateException {
		// 支店コードチェック
		if (StringUtils.isEmpty(form.getBrCodeInput())) {
			final String errMsg = "支店コードが未入力";
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "支店コード"), errMsg));

		} else if (StringUtils.isNotEmpty(form.getBrCodeInput())) {
			if (!ValidationUtil.isLong(form.getBrCodeInput())) {
				final String errMsg = "支店コードが半角数値以外";
				throw new ValidateException(new Conveyance(new MessageKey("DPC1004E", "支店コード", "半角数値"), errMsg));
			}

			if (!ValidationUtil.isMinLength(form.getBrCodeInput(), 3)) {
				final String errMsg = "支店コードが3桁未満";
				throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "支店コード", "3"), errMsg));
			}

			if (!ValidationUtil.isMaxLength(form.getBrCodeInput(), 3)) {
				final String errMsg = "支店コードが3桁を超えている";
				throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "支店コード", "3"), errMsg));
			}
		}

		// 営業所コードチェック
		if (StringUtils.isEmpty(form.getDistCodeInput())) {
			final String errMsg = "営業所コードが未入力";
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "エリアコード"), errMsg));
//			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "営業所コード"), errMsg));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）

		} else if (StringUtils.isNotEmpty(form.getDistCodeInput())) {
			if (!ValidationUtil.isLong(form.getDistCodeInput())) {
				final String errMsg = "営業所コードが半角数値以外";
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
				throw new ValidateException(new Conveyance(new MessageKey("DPC1004E", "エリアコード", "半角数値"), errMsg));
//				throw new ValidateException(new Conveyance(new MessageKey("DPC1004E", "営業所コード", "半角数値"), errMsg));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			}

			if (!ValidationUtil.isMinLength(form.getDistCodeInput(), 3)) {
				final String errMsg = "営業所コードが3桁未満";
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
				throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "エリアコード", "3"), errMsg));
//				throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "営業所コード", "3"), errMsg));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			}

			if (!ValidationUtil.isMaxLength(form.getDistCodeInput(), 3)) {
				final String errMsg = "営業所コードが3桁を超えている";
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
				throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "エリアコード", "3"), errMsg));
//				throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "営業所コード", "3"), errMsg));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
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
	public void dps502C02F15Validation(DpContext ctx, Dps502C02Form form) throws ValidateException {
		// 特約店コードチェック
		if (StringUtils.isEmpty(form.getTmsTytenCd())) {
			final String errMsg = "特約店コードが未入力";
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "特約店コード"), errMsg));

		} else {
			if (!ValidationUtil.isLong(form.getTmsTytenCd())) {
				final String errMsg = "特約店コードが半角数値以外";
				throw new ValidateException(new Conveyance(new MessageKey("DPC1004E", "特約店コード", "半角数値"), errMsg));
			}

			if (!ValidationUtil.isMinLength(form.getTmsTytenCd(), 13)) {
				final String errMsg = "特約店コードが13桁未満";
				throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "特約店コード", "13"), errMsg));
			}

			if (!ValidationUtil.isMaxLength(form.getTmsTytenCd(), 13)) {
				final String errMsg = "特約店コードが13桁を超えている";
				throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "特約店コード", "13"), errMsg));
			}
		}

		// 支店コードチェック
		if (StringUtils.isEmpty(form.getBrCodeInput())) {
			final String errMsg = "支店コードが未入力";
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "リージョンコード"), errMsg));
//			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "支店コード"), errMsg));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）

		} else {
			if (!ValidationUtil.isLong(form.getBrCodeInput())) {
				final String errMsg = "支店コードが半角数値以外";
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
				throw new ValidateException(new Conveyance(new MessageKey("DPC1004E", "リージョンコード", "半角数値"), errMsg));
//				throw new ValidateException(new Conveyance(new MessageKey("DPC1004E", "支店コード", "半角数値"), errMsg));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			}

			if (!ValidationUtil.isMinLength(form.getBrCodeInput(), 3)) {
				final String errMsg = "支店コードが3桁未満";
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
				throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "リージョンコード", "3"), errMsg));
//				throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "支店コード", "3"), errMsg));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			}

			if (!ValidationUtil.isMaxLength(form.getBrCodeInput(), 3)) {
				final String errMsg = "支店コードが3桁を超えている";
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
				throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "リージョンコード", "3"), errMsg));
//				throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "支店コード", "3"), errMsg));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			}
		}

		// 営業所コードチェック
		if (StringUtils.isEmpty(form.getDistCodeInput())) {
			final String errMsg = "営業所コードが未入力";
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "エリアコード"), errMsg));
//			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "営業所コード"), errMsg));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）

		} else {
			if (!ValidationUtil.isLong(form.getDistCodeInput())) {
				final String errMsg = "営業所コードが半角数値以外";
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
				throw new ValidateException(new Conveyance(new MessageKey("DPC1004E", "エリアコード", "半角数値"), errMsg));
//				throw new ValidateException(new Conveyance(new MessageKey("DPC1004E", "営業所コード", "半角数値"), errMsg));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			}

			if (!ValidationUtil.isMinLength(form.getDistCodeInput(), 3)) {
				final String errMsg = "営業所コードが3桁未満";
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
				throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "エリアコード", "3"), errMsg));
//				throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "営業所コード", "3"), errMsg));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			}

			if (!ValidationUtil.isMaxLength(form.getDistCodeInput(), 3)) {
				final String errMsg = "営業所コードが3桁を超えている";
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
				throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "エリアコード", "3"), errMsg));
//				throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "営業所コード", "3"), errMsg));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			}
		}
	}

	/**
	 * 登録処理時入力チェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps502C02F16Validation(DpContext ctx, Dps502C02Form form) throws ValidateException {

		// 受信パラメータチェック
		if (form.getRowIdList() == null) {
			final String errMsg = "受信パラメータがnull";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}

		for (String rowData : form.getRowIdList()) {
			String[] data = ConvertUtil.splitConmma(rowData);

			// サイズチェック
			if (data.length != 3) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 計画値UHチェック
			String plannedValueUh = data[1];
			if (StringUtils.isNotEmpty(plannedValueUh)) {
				if (!ValidationUtil.isLong(plannedValueUh)) {
					final String errMsg = "特約店別計画UHが半角数値以外";
					throw new ValidateException(new Conveyance(new MessageKey("DPC1004E", "特約店別計画UH", "半角数値"), errMsg));
				}

				if (!ValidationUtil.isMaxLength(plannedValueUh, 10)) {
					final String errMsg = "特約店別計画UHが11桁未満";
					throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "特約店別計画UH", "10"), errMsg));
				}

				if (ConvertUtil.parseLong(plannedValueUh) < 0) {
					final String errMsg = "特約店別計画UHが0未満";
					throw new ValidateException(new Conveyance(new MessageKey("DPC1008E", "特約店別計画UH", "0"), errMsg));
				}
			}

			// 計画値Pチェック
			String plannedValueP = data[2];
			if (StringUtils.isNotEmpty(plannedValueP)) {
				if (!ValidationUtil.isLong(plannedValueP)) {
					final String errMsg = "特約店別計画Pが半角数値以外";
					throw new ValidateException(new Conveyance(new MessageKey("DPC1004E", "特約店別計画P", "半角数値"), errMsg));
				}

				if (!ValidationUtil.isMaxLength(plannedValueP, 10)) {
					final String errMsg = "特約店別計画Pが11桁未満";
					throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "特約店別計画P", "10"), errMsg));
				}

				if (ConvertUtil.parseLong(plannedValueP) < 0) {
					final String errMsg = "特約店別計画Pが0未満";
					throw new ValidateException(new Conveyance(new MessageKey("DPC1008E", "特約店別計画P", "0"), errMsg));
				}
			}
		}
	}

	/**
	* カテゴリリストの初期設定
	* @param form Dps400C00Form
	*/
	private void initCategoryList(Dps502C02Form form) {
		// 汎用マスタのカテゴリリスト
		List<DpsCCdMst> categoryList = (dpsCodeMasterSearchService.searchCategoryList(null, PLAN_ID));

		// カテゴリリストに表示するリスト
		List<String> targetCategoryAry = new ArrayList<String>();
		// 計画対象カテゴリ領域の設定
		List<String> planLvCtgList = new ArrayList<String>();
		// 計画立案レベル-担当者
		planLvCtgList = dpsPlannedCtgSearchService.searchCategoryByPlanLevel(ProdPlanLevel.WS);

		String sosCd = null;
		if (StringUtils.isNotBlank(form.getSosCd3())) {
			// 組織コード：営業所
			sosCd = form.getSosCd3();
		}
		// 組織と紐づくカテゴリリスト
		List<SosCtg> ctgList = dpsSosCtgSearchService.searchDpsSosCtgList(sosCd, SCREEN_ID);


	    // ワクチンのカテゴリ以外を表示する。
		String vaccineCd = dpsCodeMasterSearchService.searchCodeByDataKbn(DpsCodeMaster.VAC.getDbValue()).get(0).getDataCd();
		for (SosCtg ctg : ctgList) {
			if(ctg.getCategory().equals(vaccineCd) == false) {
				targetCategoryAry.add(ctg.getCategory());
			}
		}

		List<CodeAndValue> list = new ArrayList<CodeAndValue>();

		boolean indexFlg = true;
		// 対象の立案レベルをもつ計画対象カテゴリ領域のカテゴリのみ設定
		for (DpsCCdMst mst : categoryList) {
			if (targetCategoryAry.contains(mst.getDataCd()) && planLvCtgList.contains(mst.getDataCd())) {
				CodeAndValue cad = new CodeAndValue(mst.getDataCd(), mst.getDataName());
				list.add(cad);
				if (form.getCategory() == null && indexFlg) {
					form.setCategory(cad.getCode());
					indexFlg = false;
				}
			}
		}
		form.setProdCategoryList(list);
	}
}
