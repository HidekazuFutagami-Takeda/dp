package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.exp.ErrMessageKey.*;
import static jp.co.takeda.a.web.bean.CorrespondType.*;

import java.util.ArrayList;
import java.util.List;

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
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.EstimationParamResultDto;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcUserSearchService;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.service.DpsEstimationProdSearchService;
import jp.co.takeda.service.DpsEstimationProdService;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps301C01Form;

/**
 * Dps301C01((医)試算パラメータ編集画面)のアクションクラス
 *
 * @author nozaki
 */
@Controller("Dps301C01Action")
public class Dps301C01Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps301C01Action.class);

	/**
	 * 計画（1：期別、2：月別）
	 */
	private static final String PLAN_ID = "1";

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 試算機能 検索系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsEstimationProdSearchService")
	protected DpsEstimationProdSearchService dpsEstimationProdSearchService;

	/**
	 * 試算機能 更新系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsEstimationProdService")
	protected DpsEstimationProdService dpsEstimationProdService;

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
	 * 初期表示時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.REFER)
	public Result dps301C01F00(DpContext ctx, Dps301C01Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps301C01F00");
			LOG.debug("sosCd=" + form.getSosCd3());
			LOG.debug("prodCode=" + form.getProdCode());
		}

		// 初期化処理
		form.formInit();

		// 組織従業員初期化処理
		dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd3(), null);

		// 検索実行
		searchEstimationParam(form);
		//カテゴリリストの設定
		initCategoryList(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * 登録処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps301C01F05Execute(DpContext ctx, Dps301C01Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps301C01F05");
		}

		try {

			// 試算パラメータ更新
			dpsEstimationProdService.updateEstParamOffice(form.getEstimationParamUpdateData());

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0004I", "試算パラメータ"));

		} finally {

			// 検索実行
			searchEstimationParam(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 登録処理時の事前チェックで呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(correspondType = ASYNC)
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps301C01F06Execute(DpContext ctx, Dps301C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps301C01F06Execute");
		}

		// 実績チェック実行
		List<String> resultMessageList = dpsEstimationProdSearchService.paramUpdateCheck(form.getEstimationParamUpdateData());

		// 結果メッセージをカンマ区切りで返す
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < resultMessageList.size(); i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append(resultMessageList.get(i));
		}

		List<CodeAndValue> list = new ArrayList<CodeAndValue>();
		list.add(new CodeAndValue("messageList", sb.toString()));

		ctx.getRequest().setAttribute("resultList", list);
		return ActionResult.SUCCESS;

	}

	/**
	 * 本部案に戻す処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps301C01F10Execute(DpContext ctx, Dps301C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps301C01F10Execute");
		}

		try {

			// 本部案に戻す
			dpsEstimationProdService.deleteEstParamOffice(form.getEstimationParamUpdateData());

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPS0003I"));

		} finally {

			// 検索実行
			searchEstimationParam(form);
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
	@Permission(authType = AuthType.REFER)
	public Result dps301C01F11(DpContext ctx, Dps301C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps301C01F11");
		}

		// 本部案に戻す
		EstimationParamResultDto resultDto = dpsEstimationProdSearchService.searchEstimationParam(form.getSosCd3(), form.getProdCode());
		super.getRequestBox().put(Dps301C01Form.DPS301C01_DATA_R, resultDto);

		// 本部案から本部案に戻そうとした際に、営業所案になってしまっていた場合の対応
		if (resultDto.getEstParamOffice() != null && resultDto.getEstParamOffice().getSeqKey() != null) {
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
	private void searchEstimationParam(Dps301C01Form form) throws Exception {

		String sosCd = form.getSosCd3();
		String prodCode = form.getProdCode();

		// 検索実行
		EstimationParamResultDto resultDto = dpsEstimationProdSearchService.searchEstimationParam(sosCd, prodCode);

		super.getRequestBox().put(Dps301C01Form.DPS301C01_DATA_R, resultDto);
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
	public void dps301C01F05Validation(DpContext ctx, Dps301C01Form form) throws ValidateException {

		// 入力必須チェック
		if (StringUtils.isEmpty(form.getOfficeRefProdCode())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1006E", "試算品目")));
		}
		if (StringUtils.isEmpty(form.getOfficeRefFrom())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1006E", "実績参照期間(FROM)")));
		}
		if (StringUtils.isEmpty(form.getOfficeRefTo())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1006E", "実績参照期間(TO)")));
		}
		if (StringUtils.isEmpty(form.getOfficeEstimationBase())) {
			final String errMsg = "受信パラメータ(試算元計算値)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
// add start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		if (StringUtils.isEmpty(form.getOfficeIndexRyhRtsu()) || !ValidationUtil.isInteger(form.getOfficeIndexRyhRtsu())) {
			final String errMsg = "受信パラメータ(留保率)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
// add end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		if (StringUtils.isEmpty(form.getOfficeIndexMikakutoku()) || !ValidationUtil.isInteger(form.getOfficeIndexMikakutoku())) {
			final String errMsg = "受信パラメータ(未獲得市場)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isEmpty(form.getOfficeIndexDelivery()) || !ValidationUtil.isInteger(form.getOfficeIndexDelivery())) {
			final String errMsg = "受信パラメータ(納入実績)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isEmpty(form.getOfficeIndexFree1()) || !ValidationUtil.isInteger(form.getOfficeIndexFree1())) {
			final String errMsg = "受信パラメータ(フリー項目1)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isEmpty(form.getOfficeIndexFree2()) || !ValidationUtil.isInteger(form.getOfficeIndexFree2())) {
			final String errMsg = "受信パラメータ(フリー項目2)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isEmpty(form.getOfficeIndexFree3()) || !ValidationUtil.isInteger(form.getOfficeIndexFree3())) {
			final String errMsg = "受信パラメータ(フリー項目3)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}

		// 非表示、非入力項目チェック
		if (!StringUtils.isEmpty(form.getOfficeSeqKey()) && !ValidationUtil.isLong(form.getOfficeSeqKey())) {
			final String errMsg = "受信パラメータ(シーケンスキー)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isEmpty(form.getSosCd3())) {
			final String errMsg = "受信パラメータ(組織コード)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isEmpty(form.getProdCode())) {
			final String errMsg = "受信パラメータ(品目固定コード)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (!StringUtils.isEmpty(form.getOfficeLastUpdateTime()) && !ValidationUtil.isLong(form.getOfficeLastUpdateTime())) {
			final String errMsg = "受信パラメータ(最終更新日)が不正：";
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
	public void dps301C01F10Validation(DpContext ctx, Dps301C01Form form) throws ValidateException {

		// 非表示、非入力項目チェック
		if (!StringUtils.isEmpty(form.getOfficeSeqKey()) && !ValidationUtil.isLong(form.getOfficeSeqKey())) {
			final String errMsg = "受信パラメータ(シーケンスキー)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isEmpty(form.getSosCd3())) {
			final String errMsg = "受信パラメータ(組織コード)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isEmpty(form.getProdCode())) {
			final String errMsg = "受信パラメータ(品目固定コード)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (!StringUtils.isEmpty(form.getOfficeLastUpdateTime()) && !ValidationUtil.isLong(form.getOfficeLastUpdateTime())) {
			final String errMsg = "受信パラメータ(最終更新日)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
	}

    /**
     * カテゴリリストの初期設定
     * @param form Dps300C00Form
     */
    private void initCategoryList(Dps301C01Form form) {
		// 汎用マスタのカテゴリリスト
		List<DpsCCdMst> categoryList = (dpsCodeMasterSearchService.searchCategoryList(null,PLAN_ID));

		List<CodeAndValue> list = new ArrayList<CodeAndValue>();
		for (DpsCCdMst mst :categoryList) {
				CodeAndValue cad = new CodeAndValue(mst.getDataCd(), mst.getDataName());
				list.add(cad);
		}
		form.setProdCategoryList(list);
    }
}
