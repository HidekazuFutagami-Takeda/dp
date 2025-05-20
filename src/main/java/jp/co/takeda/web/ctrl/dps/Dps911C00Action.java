package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.web.bean.CorrespondType.*;
import static jp.co.takeda.security.BusinessType.*;

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
import jp.co.takeda.dto.DpsInsMstScDto;
import jp.co.takeda.dto.InsMstResultDto;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.service.DpsExceptDistInsSearchService;
import jp.co.takeda.service.DpsInsMstSearchService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps911C00Form;

/**
 * Dps911C00(施設選択画面)のアクションクラス
 *
 * @author khashimoto
 */
@Controller("Dps911C00Action")
public class Dps911C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps911C00Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 検索系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsMstSearchService")
	protected DpsInsMstSearchService dpsInsMstSearchService;

	/**
	 * 検索系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsExceptDistInsSearchService")
	protected DpsExceptDistInsSearchService dpsExceptDistInsSearchService;

	/**
	 * 汎用マスタ検索サービス
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
	@RequestType(businessType = CMN)
	public Result dps911C00F00(DpContext ctx, Dps911C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps911C00F00");
		}

		// ワクチンコードを取得
		String vaccineCode = dpsCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.VAC.getDbValue()).get(0).getDataCd();
		form.setVaccineCode(vaccineCode);

		// 初期化処理
		form.formInit();

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
	@RequestType(businessType = CMN)
	public Result dps911C00F05(DpContext ctx, Dps911C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps911C00F05");
		}

		// 検索実行
		searchInsMst(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * 他画面からの同期でデータ取得時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(correspondType = ASYNC)
	@RequestType(businessType = CMN)
	public Result dps911C00F10(DpContext ctx, Dps911C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps911C00F10");
			LOG.debug("insNo:" + form.getInsNo());
			LOG.debug("jgiNo:" + form.getJgiNo());
			LOG.debug("category:" + form.getCategory());
		}

		// 検索サービス実行
		String insNo = form.getInsNo();
		Integer jgiNo = ConvertUtil.parseInteger(form.getJgiNo());
		String prodCode = form.getParamProdCode();
		String category = form.getCategory();
		InsMstResultDto insMst = dpsInsMstSearchService.search(insNo, jgiNo, prodCode, category);

		// 配分除外施設かどうか
		boolean isExceptDistIns = false;
		if(StringUtils.isNotEmpty(prodCode)){
			isExceptDistIns = dpsExceptDistInsSearchService.isExceptDistIns(insNo, prodCode);
		}

		// リクエストに検索結果リストをセット
		super.getRequestBox().put(Dps911C00Form.DPS911C00F10_DATA_R, insMst);
		super.getRequestBox().put(Dps911C00Form.DPS911C00F10_EXCEPT_DIST_INS, isExceptDistIns);

		return ActionResult.SUCCESS;
	}

	/**
	 * 他画面からの同期でデータ取得時に呼ばれるアクションメソッド<br>
	 * 指定された施設の親子施設をリストで取得する。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(correspondType = ASYNC)
	@RequestType(businessType = CMN)
	public Result dps911C00F11(DpContext ctx, Dps911C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps911C00F11");
			LOG.debug("insNo:" + form.getInsNo());
			LOG.debug("jgiNo:" + form.getJgiNo());
			LOG.debug("prodCode" + form.getParamProdCode());
		}

		// 検索サービス実行
		String insNo = form.getInsNo();
		Integer jgiNo = ConvertUtil.parseInteger(form.getJgiNo());
		String prodCode = form.getParamProdCode();
		List<InsMstResultDto> insMstList = dpsInsMstSearchService.searchFamilyIns(insNo, jgiNo, prodCode);

		// リクエストに検索結果リストをセット
		super.getRequestBox().put(Dps911C00Form.DPS911C00F11_DATA_R, (ArrayList<InsMstResultDto>)insMstList);
		return ActionResult.SUCCESS;
	}


	/**
	 * 検索処理の実行
	 *
	 * <pre>
	 * 検索結果をRequestBoxに格納します。
	 * </pre>
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchInsMst(Dps911C00Form form) throws Exception {
		// 検索条件DTOの生成
		DpsInsMstScDto scDto = form.convertInsMstScDto();
		String category = form.getCategory();

		// 検索実行
		List<InsMstResultDto> resultList;
		try {
			resultList = dpsInsMstSearchService.search(scDto,category);
			form.setExistSearchDataFlag(true);
		} catch (LogicalException e) {
			form.setExistSearchDataFlag(false);
			throw e;
		}

		// リクエストに検索結果リストをセット
		super.getRequestBox().put(Dps911C00Form.DPS911C00_DATA_R, (ArrayList<InsMstResultDto>) resultList);

	}

	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * 検索処理時の入力パラメータチェック
	 *
	 * <pre>
	 * 画面入力にエラーがある場合に例外をスローする。
	 * </pre>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps911C00F05Validation(DpContext ctx, Dps911C00Form form) throws ValidateException {
		List<String> srcHolderList = new ArrayList<String>();

		// 組織・従業員 必須チェック
		if (StringUtils.isEmpty(form.getSosCd3())) {
			srcHolderList.add("組織・従業員");
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", srcHolderList)));
		}
	}

	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps911C00F10Validation(DpContext ctx, Dps911C00Form form) throws ValidateException {

		// 組織コード、従業員コードチェック
		if (StringUtils.isEmpty(form.getInsNo())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "施設コード")));
		}
//		// 組織コード、従業員コードチェック
//		if (StringUtils.isEmpty(form.getJgiNo())) {
//			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "従業員コード")));
//		}
	}

	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps911C00F11Validation(DpContext ctx, Dps911C00Form form) throws ValidateException {

		// 組織コード、従業員コードチェック
		if (StringUtils.isEmpty(form.getInsNo())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "施設コード")));
		}
//		// 組織コード、従業員コードチェック
//		if (StringUtils.isEmpty(form.getJgiNo())) {
//			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "従業員コード")));
//		}
	}
}
