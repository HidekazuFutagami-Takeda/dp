package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.exp.ErrMessageKey.VALIDATE_ERROR;
import static jp.co.takeda.security.DpAuthority.AuthType.EDIT;
import static jp.co.takeda.security.DpAuthority.AuthType.REFER;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Controller;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.DistParamResultDto;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcUserSearchService;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.service.DpsDistributionProdSearchService;
import jp.co.takeda.service.DpsDistributionProdService;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps400C01Form;

/**
 * Dps400C01((医)施設特約店別計画配分基準編集画面)のアクションクラス
 *
 * @author stakeuchi
 */
@Controller("Dps400C01Action")
public class Dps400C01Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps400C01Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 配分機能 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsDistributionProdSearchService")
	DpsDistributionProdSearchService dpsDistributionProdSearchService;

	/**
	 * 配分機能 実行サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsDistributionProdService")
	DpsDistributionProdService dpsDistributionProdService;

	/**
	 * ユーザサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpcUserSearchService")
	protected DpcUserSearchService dpcUserSearchService;

	/**
	 * カテゴリ 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

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
	@Permission(authType = REFER)
	public Result dps400C01F00(DpContext ctx, Dps400C01Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps400C01F00");
			LOG.debug("sosCd=" + form.getSosCd3());
			LOG.debug("prodCode=" + form.getProdCode());
		}

		// 初期化処理
		form.formInit();

		// 組織従業員初期化処理
		dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd3(), null);

		// 検索実行
		searchDistParam(form);
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
	public Result dps400C01F05Execute(DpContext ctx, Dps400C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps400C01F05Execute");
		}
		try {

			dpsDistributionProdService.updateDistParamOffice(form.convertDistParamUpdateDto());

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0004I", "配分パラメータ"));

		} finally {

			// 再検索実行
			searchDistParam(form);
		}
		return ActionResult.SUCCESS;
	}

	/**
	 * 本部案に戻す処理時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = EDIT)
	public Result dps400C01F10Execute(DpContext ctx, Dps400C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps400C01F10Execute");
		}
		try {

			dpsDistributionProdService.deleteDistParamOffice(form.convertDistParamUpdateDto());
			super.addMessage(ctx, new MessageKey("DPS0003I"));

		} finally {

			// 再検索実行
			searchDistParam(form);
		}
		return ActionResult.SUCCESS;
	}

	/**
	 * 本部案から本部案に戻す処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = EDIT)
	public Result dps400C01F11(DpContext ctx, Dps400C01Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps400C01F11");
		}

		// 本部案に戻す
		final String sosCd = form.getSosCd3();
		final String prodCode = form.getProdCode();
		final String category = form.getCategory();
		final DistParamResultDto resultDto = dpsDistributionProdSearchService.searchDistributionParam(sosCd, prodCode, category);
		super.getRequestBox().put(Dps400C01Form.Dps400C01_DATA_R_SEARCH_RESULT, resultDto);

		// 本部案から本部案に戻そうとした際に、営業所案になってしまっていた場合の対応
		if (resultDto.getDistParamOfficeUH() != null && resultDto.getDistParamOfficeUH().getSeqKey() != null && resultDto.getDistParamOfficeP() != null
			&& resultDto.getDistParamOfficeP().getSeqKey() != null) {
			// 楽観的ロックと同じメッセージを設定する仕様
			final String errMsg = "本部案から本部案初期値に戻す際に、既に営業所案が登録されているためエラー";
			throw new OptimisticLockingFailureException(errMsg);
		} else {
			super.addMessage(ctx, new MessageKey("DPS0003I"));
		}
		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理を実行する。
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchDistParam(Dps400C01Form form) throws Exception {
		// 検索実行
		final String sosCd = form.getSosCd3();
		final String prodCode = form.getProdCode();
		final String category = form.getCategory();
		final DistParamResultDto resultDto = dpsDistributionProdSearchService.searchDistributionParam(sosCd, prodCode, category);
		super.getRequestBox().put(Dps400C01Form.Dps400C01_DATA_R_SEARCH_RESULT, resultDto);
	}

	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * 登録処理時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps400C01F05Validation(DpContext ctx, Dps400C01Form form) throws ValidateException {
		// 入力必須チェック
		if (StringUtils.isEmpty(form.getOfficeRefProdCodeUH())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1006E", "[UH]配分品目")));
		}
		if (StringUtils.isEmpty(form.getOfficeRefFromUH())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1006E", "[UH]実績参照期間(FROM)")));
		}
		if (StringUtils.isEmpty(form.getOfficeRefToUH())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1006E", "[UH]実績参照期間(TO)")));
		}
		if (StringUtils.isEmpty(form.getOfficeRefProdCodeP())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1006E", "[P]配分品目")));
		}
		if (StringUtils.isEmpty(form.getOfficeRefFromP())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1006E", "[P]実績参照期間(FROM)")));
		}
		if (StringUtils.isEmpty(form.getOfficeRefToP())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1006E", "[P]実績参照期間(TO)")));
		}
		// 非表示、非入力項目チェック
		if (StringUtils.isEmpty(form.getSosCd3())) {
			final String errMsg = "受信パラメータ(組織コード)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isEmpty(form.getProdCode())) {
			final String errMsg = "受信パラメータ(品目固定コード)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isEmpty(form.getProdName())) {
			final String errMsg = "受信パラメータ(品目名)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isNotEmpty(form.getOfficeSeqKeyUH()) && !ValidationUtil.isLong(form.getOfficeSeqKeyUH())) {
			final String errMsg = "受信パラメータ([UH]営業所案 シーケンスキー)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isNotEmpty(form.getOfficeUpDateUH()) && !ValidationUtil.isLong(form.getOfficeUpDateUH())) {
			final String errMsg = "受信パラメータ([UH]営業所案 最終更新日)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isEmpty(form.getOfficeDistributionTypeUH())) {
			final String errMsg = "受信パラメータ([UH]営業所案 配布先区分)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isNotEmpty(form.getOfficeSeqKeyP()) && !ValidationUtil.isLong(form.getOfficeSeqKeyP())) {
			final String errMsg = "受信パラメータ([P]営業所案 シーケンスキー)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isNotEmpty(form.getOfficeUpDateP()) && !ValidationUtil.isLong(form.getOfficeUpDateP())) {
			final String errMsg = "受信パラメータ([P]営業所案 最終更新日)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isEmpty(form.getOfficeDistributionTypeP())) {
			final String errMsg = "受信パラメータ([P]営業所案 配布先区分)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
	}

	/**
	 * 本部案に戻す処理時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps400C01F10Validation(DpContext ctx, Dps400C01Form form) throws ValidateException {

		// 非表示、非入力項目チェック
		if (StringUtils.isEmpty(form.getSosCd3())) {
			final String errMsg = "受信パラメータ(組織コード)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isEmpty(form.getProdCode())) {
			final String errMsg = "受信パラメータ(品目固定コード)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isNotEmpty(form.getOfficeSeqKeyUH()) && !ValidationUtil.isLong(form.getOfficeSeqKeyUH())) {
			final String errMsg = "受信パラメータ([UH]営業所案 シーケンスキー)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isNotEmpty(form.getOfficeUpDateUH()) && !ValidationUtil.isLong(form.getOfficeUpDateUH())) {
			final String errMsg = "受信パラメータ([UH]営業所案 最終更新日)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isNotEmpty(form.getOfficeSeqKeyP()) && !ValidationUtil.isLong(form.getOfficeSeqKeyP())) {
			final String errMsg = "受信パラメータ([P]営業所案 シーケンスキー)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isNotEmpty(form.getOfficeUpDateP()) && !ValidationUtil.isLong(form.getOfficeUpDateP())) {
			final String errMsg = "受信パラメータ([P]営業所案 最終更新日)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
	}
}
