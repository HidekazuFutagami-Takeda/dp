package jp.co.takeda.web.ctrl.dpm;

import static jp.co.takeda.security.BusinessType.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dao.DpsPrefecturesListDao;
import jp.co.takeda.dto.AddrSearchResultListDto;
import jp.co.takeda.model.Prefectures;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpmJisCodeSearchService;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dpm.Dpm913C00Form;

/**
 * Dpm913C00(ワ)市区郡検索画面のアクションクラス
 * 
 * @author khashimoto
 */
@Controller("Dpm913C00Action")
public class Dpm913C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dpm913C00Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 特約店情報検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmJisCodeSearchService")
	protected DpmJisCodeSearchService dpmJisCodeSearchService;

	/**
	 * 2022/6/21 Y.Taniguchi バックログNo.21　組織に紐づく都道府県をプルダウンに表示する
	 * 都道府県カテゴリDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsPrefecturesListDao")
	protected DpsPrefecturesListDao dpsPrefecturesListDao;

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
	public Result dpm913C00F00(DpContext ctx, Dpm913C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm913C00F00");
		}

		// 初期化処理
		form.formInit();

		//2022/6/21 Y.Taniguchi バックログNo.21　組織に紐づく都道府県をプルダウンに表示する
		// 組織に紐づく都道府県の検索処理を実行
		searchPrefectures(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * 検索時に呼ばれるアクションメソッド<br>
	 * 
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType(businessType = CMN)
	public Result dpm913C00F05(DpContext ctx, Dpm913C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm913C00F00");
		}

		//2022/6/21 Y.Taniguchi バックログNo.21　組織に紐づく都道府県をプルダウンに表示する
		// 組織に紐づく都道府県の検索処理を実行
		searchPrefectures(form);

		// 検索
		AddrSearchResultListDto resultListDto = dpmJisCodeSearchService.search(form.convertAddrScDto());
		// 結果格納DTOをリクエストに格納
		super.getRequestBox().put(Dpm913C00Form.JIS_LIST_KEY_R, resultListDto);

		return ActionResult.SUCCESS;
	}
	/**
	 * 2022/6/21 Y.Taniguchi バックログNo.21　組織に紐づく都道府県をプルダウンに表示する
	 * 組織に紐づく都道府県の検索処理を実行する。
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchPrefectures(Dpm913C00Form form) throws Exception {

		// 組織と紐づく都道府県を検索
		List<Prefectures> prefecturesList = new ArrayList<Prefectures>();
		String soscd = form.getSosInitSosCodeValue();
		try {
		if(StringUtils.isEmpty(soscd)) {
			soscd = null;
		}
		prefecturesList = dpsPrefecturesListDao.search(soscd);
		} catch (DataNotFoundException e) {
			// 検索結果0件の場合は何もしない
		}
		// 検索実行・CodeAndValueに変換
		List<CodeAndValue> resultList = new ArrayList<CodeAndValue>();
		resultList.add(new CodeAndValue("", "")); // 空欄あり
			for (Prefectures resultDto : prefecturesList) {
				CodeAndValue cv = new CodeAndValue(resultDto.getCode(), resultDto.getvalue());
				resultList.add(cv);
		}
		form.setPrefectures(resultList);
		super.getRequestBox().put(Dpm913C00Form.DPM913C00_DATA_PREFECTURE_LIST, (ArrayList<CodeAndValue>) resultList);
	}
}
