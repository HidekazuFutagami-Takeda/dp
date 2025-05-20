package jp.co.takeda.web.ctrl.dpm;

import static jp.co.takeda.a.web.bean.CorrespondType.ASYNC;
import static jp.co.takeda.security.BusinessType.CMN;

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
import jp.co.takeda.dto.InsMstResultDto;
import jp.co.takeda.dto.InsMstScDto;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpmInsMstSearchService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dpm.Dpm911C00Form;

/**
 * Dpm911C00(施設選択画面)のアクションクラス
 *
 * @author khashimoto
 */
@Controller("Dpm911C00Action")
public class Dpm911C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dpm911C00Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 検索系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmInsMstSearchService")
	protected DpmInsMstSearchService dpmInsMstSearchService;

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
	public Result dpm911C00F00(DpContext ctx, Dpm911C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm911C00F00");
		}

		// 初期化処理
		form.formInit();

		// 設定中ユーザの組織コードを設定する。
		if (StringUtils.isEmpty(form.getSosInitSosCodeValue()) && StringUtils.isEmpty(form.getParamJgiNo())) {
			DpUser dpUser = DpUserInfo.getDpUserInfo().getSettingUser();
			// ログインユーザが全社表示可能かどうかを確認
			if(dpUser.isSosLvl("CMN", SosLvl.ALL)) {
				form.setSosInitSosCodeValue(null);
			} else {
				form.setSosInitSosCodeValue(dpUser.getSosCd());
			}
		}

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
	public Result dpm911C00F05(DpContext ctx, Dpm911C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm911C00F05");
		}

		// 検索実行
		InsMstScDto scDto = form.convertInsMstScDto();
		searchInsMst(form, scDto);

		return ActionResult.SUCCESS;
	}

	/**
	 * ページング時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType(businessType = CMN)
	public Result dpm911C00F06(DpContext ctx, Dpm911C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm911C00F06");
		}

		// 検索実行
		InsMstScDto scDto = super.getSessionBox().get(Dpm911C00Form.SEARCH_CONDITION_S);
		if (scDto != null) {
			form.convertForm(scDto);
		} else {
			scDto = form.convertInsMstScDto();
		}

		searchInsMst(form, scDto);

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
	public Result dpm911C00F10(DpContext ctx, Dpm911C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm911C00F10");
			LOG.debug("insNo:" + form.getInsNo());
		}

		// 検索サービス実行
		String insNo = form.getInsNo();
		Integer jgiNo = ConvertUtil.parseInteger(form.getJgiNo());
		InsMstResultDto insMst;
		if(jgiNo == null){
			insMst = dpmInsMstSearchService.search(insNo);
		} else {
			insMst = dpmInsMstSearchService.search(insNo, jgiNo);
		}

		// リクエストに検索結果リストをセット
		super.getRequestBox().put(Dpm911C00Form.DPM911C00F10_DATA_R, insMst);

		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理を実行する。
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchInsMst(Dpm911C00Form form, InsMstScDto scDto) throws Exception {

		// 検索実行
		List<InsMstResultDto> resultList;
		try {
			resultList = dpmInsMstSearchService.search(scDto);
			form.setExistSearchDataFlag(true);

			// 検索結果をセッションに保存
			super.getSessionBox().put(Dpm911C00Form.SEARCH_CONDITION_S, scDto);

		} catch (LogicalException e) {
			form.setExistSearchDataFlag(false);
			throw e;
		}

		// リクエストに検索結果リストをセット
		super.getRequestBox().put(Dpm911C00Form.DPM911C00_DATA_R, (ArrayList<InsMstResultDto>) resultList);

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
	public void dpm911C00F05Validation(DpContext ctx, Dpm911C00Form form) throws ValidateException {
		List<String> srcHolderList = new ArrayList<String>();

		// 組織・従業員 必須チェック
		if (StringUtils.isEmpty(form.getSosCd3())) {
			srcHolderList.add("組織・従業員");
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", srcHolderList)));
		}
	}

	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dpm911C00F10Validation(DpContext ctx, Dpm911C00Form form) throws ValidateException {

		// 組織コード、従業員コードチェック
		if (StringUtils.isEmpty(form.getInsNo())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "施設コード")));
		}
	}
}
