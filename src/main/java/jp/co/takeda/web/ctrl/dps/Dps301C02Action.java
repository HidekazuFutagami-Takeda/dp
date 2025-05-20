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
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.FreeIndexResultDto;
import jp.co.takeda.dto.FreeIndexUpdateDto;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcUserSearchService;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.service.DpsEstimationProdSearchService;
import jp.co.takeda.service.DpsEstimationProdService;
import jp.co.takeda.service.DpsProdSearchService;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps301C02Form;

/**
 * Dps301C02((医)フリー項目編集画面)のアクションクラス
 *
 * @author nozaki
 */
@Controller("Dps301C02Action")
public class Dps301C02Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps301C02Action.class);

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

	/**
	 * 計画品目検索サービス実装クラス
	 */
    @Autowired(required = true)
    @Qualifier("dpsProdSearchService")
    protected DpsProdSearchService dpsProdSearchService;

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
	public Result dps301C02F00(DpContext ctx, Dps301C02Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps301C02F00");
		}

		// 初期化処理
		form.formInit();

		// 組織従業員初期化処理
		dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd3(), null);

		// 検索処理実行
		searchFreeIndex(form);

		// グリッドのヘッダを設定
		setGridHeader(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * 登録するボタンクリック時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps301C02F05Execute(DpContext ctx, Dps301C02Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps301C02F05Excecute");
		}

		String sosCd = form.getSosCd3();
		FreeIndexUpdateDto updateFreeIndexUpdateDto = form.getUpdateFreeIndexDto();

		try {
			// 更新処理実行
			dpsEstimationProdService.updateIndexFree(sosCd, updateFreeIndexUpdateDto);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0004I", "フリー項目"));

		} finally {

			// 再検索実行
			searchFreeIndex(form);
		}
		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理の実行を行う。
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchFreeIndex(Dps301C02Form form) throws Exception {

		String sosCd = form.getSosCd3();
		String prodCode = form.getProdCode();

		form.setExistSearchDataFlag(false);

		// 検索実行
		FreeIndexResultDto resultDto = dpsEstimationProdSearchService.searchFreeIndex(sosCd, prodCode);
		if (resultDto.getDetailDtoList() != null && resultDto.getDetailDtoList().size() > 0) {
			form.setExistSearchDataFlag(true);
		}

		// リクエストに検索結果リストをセット
		super.getRequestBox().put(Dps301C02Form.DPS301C02_DATA_R, (resultDto));
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
	public void dps301C02F05Validation(DpContext ctx, Dps301C02Form form) throws ValidateException {

		// 更新行のNullチェック
		String[] rowIdList = form.getRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}

		for (int i = 0; i < rowIdList.length; i++) {

			String[] rowId = rowIdList[i].split(",", 10);
			if (rowId.length != 10) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// シーケンスキー
			if (!StringUtils.isEmpty(rowId[0]) && !ValidationUtil.isLong(rowId[0])) {
				final String errMsg = "受信パラメータ(シーケンスキー)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 従業員番号
			if (!StringUtils.isEmpty(rowId[1]) && !ValidationUtil.isInteger(rowId[1])) {
				final String errMsg = "受信パラメータ(従業員番号)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 品目固定コード
			if (StringUtils.isEmpty(rowId[2])) {
				final String errMsg = "受信パラメータ(品目固定コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 最終更新日
			if (!StringUtils.isEmpty(rowId[3]) && !ValidationUtil.isLong(rowId[3])) {
				final String errMsg = "受信パラメータ(最終更新日)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// フリー項目1UH
			if (!StringUtils.isEmpty(rowId[4]) && !ValidationUtil.isLong(rowId[4])) {
				final String errMsg = "受信パラメータ(フリー項目1UH)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// フリー項目2UH
			if (!StringUtils.isEmpty(rowId[5]) && !ValidationUtil.isLong(rowId[5])) {
				final String errMsg = "受信パラメータ(フリー項目2UH)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// フリー項目3UH
			if (!StringUtils.isEmpty(rowId[6]) && !ValidationUtil.isLong(rowId[6])) {
				final String errMsg = "受信パラメータ(フリー項目3UH)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// フリー項目1P
			if (!StringUtils.isEmpty(rowId[7]) && !ValidationUtil.isLong(rowId[7])) {
				final String errMsg = "受信パラメータ(フリー項目1P)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// フリー項目2P
			if (!StringUtils.isEmpty(rowId[8]) && !ValidationUtil.isLong(rowId[8])) {
				final String errMsg = "受信パラメータ(フリー項目2P)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// フリー項目3P
			if (!StringUtils.isEmpty(rowId[9]) && !ValidationUtil.isLong(rowId[9])) {
				final String errMsg = "受信パラメータ(フリー項目3P)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
		}
	}

	/**
	 * グリッドのヘッダを設定する
	 * @param form
	 */
	private void setGridHeader(Dps301C02Form form) {


		//品目コードからカテゴリを取得
		PlannedProd plannedProd = null;
		plannedProd = dpsProdSearchService.searchProd(form.getProdCode());
		form.setCategory(plannedProd.getCategory());

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
