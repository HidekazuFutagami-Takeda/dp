package jp.co.takeda.web.ctrl.dps;

//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
import java.util.ArrayList;
import java.util.List;
//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応

import static jp.co.takeda.a.web.bean.CorrespondType.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
import jp.co.takeda.dto.DpsKakuteiErrMsgDto;
//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
import jp.co.takeda.security.RequestType;
import jp.co.takeda.dto.SupInfoDto;
import jp.co.takeda.service.DpsCExceptPlanSearchService;
//add Start 2022/7/13 R.takamoto ikkatukakutei
import jp.co.takeda.service.DpsKakuteiErrMsgSearchService;
//add End 2022/7/13 R.akamoto ikkatukakutei
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps940C00Form;
import jp.co.takeda.web.in.dps.Dps940C01Form;

/**
 * Dps940C00(補足情報)のアクションクラス
 *
 * @author kibe
 */
@Controller("Dps940C00Action")
public class Dps940C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps940C00Action.class);



	/**
	 * 初期表示<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 */
	@ActionMethod(isDownloadable = false)
	@RequestType
	//mod Start 2022/7/13 H.futagami ④No.6 一括確定のエラー表示対応
	//public Result dps940C00F00(DpContext ctx, Dps940C00Form form) {
	public Result dps940C00F00(DpContext ctx, Dps940C01Form form) {
	//mod End 2022/7/13 H.futagami ④No.6 一括確定のエラー表示対応
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps940C00F00");
			LOG.debug("sosCd=" + form.getSosCd());
			LOG.debug("category=" + form.getCategory());
		}

		// 初期化処理
		form.formInit();
		SupInfoDto resultDto = dpsCExceptPlanSearchService.getSupplementalInfo(form.getSosCd(), form.getCategory());
		//mod Start 2022/7/13 H.futagami ④No.6 一括確定のエラー表示対応
		//getRequestBox().put(Dps940C00Form.DPS940C00F01_DATA_R, resultDto);
		getRequestBox().put(Dps940C01Form.DPS940C00F01_DATA_R, resultDto);
		//mod End 2022/7/13 H.futagami ④No.6 一括確定のエラー表示対応

		return ActionResult.SUCCESS;
	}
	//add Start 2022/7/13 H.futagami ④No.6 一括確定のエラー表示対応
	/**
	 * (医)立案対象外補足情報取得処理[非同期処理]<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(correspondType = ASYNC)
	@RequestType
	public Result dps940C00F03(DpContext ctx, Dps940C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps940C00F03");
			LOG.debug("sosCd=" + form.getSosCd());
			LOG.debug("category=" + form.getCategory());
		}

		SupInfoDto resultDto = dpsCExceptPlanSearchService.getSupplementalInfo(form.getSosCd(), form.getCategory());
		getRequestBox().put(Dps940C01Form.DPS940C00F01_DATA_R, resultDto);
		return ActionResult.SUCCESS;
	}
	//add End 2022/7/13 H.futagami ④No.6 一括確定のエラー表示対応

//del Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
//
//	/**
//	 * (医)立案対象外補足情報取得処理[非同期処理]<br>
//	 *
//	 * @param ctx Context
//	 * @param form ActionForm
//	 * @return 処理結果
//	 * @throws Exception 例外
//	 */
//	@ActionMethod(correspondType = ASYNC)
//	@RequestType
//	public Result dps940C00F01(DpContext ctx, Dps940C00Form form) throws Exception {
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("dps940C00F01");
//			LOG.debug("sosCd=" + form.getSosCd());
//			LOG.debug("category=" + form.getCategory());
//		}
//
//		SupInfoDto resultDto = dpsCExceptPlanSearchService.getSupplementalInfo(form.getSosCd(), form.getCategory());
//		getRequestBox().put(Dps940C00Form.DPS940C00F01_DATA_R, resultDto);
//		return ActionResult.SUCCESS;
//	}
//del End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応

//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応

	@Autowired(required = true)
	@Qualifier("dpsKakuteiErrMsgSearchService")
	DpsKakuteiErrMsgSearchService dpsKakuteiErrMsgSearchService;

		/**
		 * 一括確定エラー情報取得処理<br>
		 *
		 */
	@ActionMethod(isDownloadable = false)
	@RequestType
	public Result dps940C00F01(DpContext ctx, Dps940C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps940C00F01");
		}

		// 検索サービス実行
		List<DpsKakuteiErrMsgDto> resultDto = dpsKakuteiErrMsgSearchService.search();

		// リクエストに検索結果リストをセット
		super.getRequestBox().put(Dps940C00Form.DPS940C00F01_DATA_R, (ArrayList<DpsKakuteiErrMsgDto>) resultDto);

		return ActionResult.SUCCESS;
	}

	@Autowired(required = true)
	@Qualifier("dpsCExceptPlanSearchService")
	DpsCExceptPlanSearchService dpsCExceptPlanSearchService;

//	add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応

}
