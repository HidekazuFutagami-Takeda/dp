package jp.co.takeda.web.ctrl.dps;

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
import jp.co.takeda.dto.DeliveryResultMrListDto;
import jp.co.takeda.dto.DeliveryResultMrScDto;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.service.DpsDeliveryResultMrSearchService;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps913C00Form;

/**
 * Dps913C00((医)過去実績参照画面(担当者別))のアクションクラス
 *
 * @author siwamoto
 */
@Controller("Dps913C00Action")
public class Dps913C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps913C00Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * サービス（検索系）
	 */
	@Autowired(required = true)
	@Qualifier("dpsDeliveryResultMrSearchService")
	protected DpsDeliveryResultMrSearchService dpsDeliveryResultMrSearchService;

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
	public Result dps913C00F00(DpContext ctx, Dps913C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps913C00F00");
		}

		// 初期化処理
		form.formInit();
		//グリッドのヘッダを設定
		setGridHeader(form);

		DeliveryResultMrScDto sosDto = form.convertDeliveryResultMrScDto();
		DeliveryResultMrListDto ServiceResult = dpsDeliveryResultMrSearchService.searchDeliveryResultMrList(sosDto);
		super.getRequestBox().put(Dps913C00Form.DPS913C00_DATA_R, ServiceResult);

		return ActionResult.SUCCESS;
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
	public void dps913C00F00Validation(DpContext ctx, Dps913C00Form form) throws Exception {
		if (StringUtils.isEmpty(form.getProdCode())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "品目コード")));
		}
	}

	/**
	 * グリッドのヘッダを設定する
	 * @param form
	 */
	private void setGridHeader(Dps913C00Form form) {

		// 追加ヘッダの設定
		String attachHeader = null;

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
		//グリッドの追加ヘッダ
		form.setAttachHeader(attachHeader);

	}
}
