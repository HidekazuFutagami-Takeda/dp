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
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.ExceptDistInsEntryDto;
import jp.co.takeda.dto.ExceptDistInsPlannedProdResultDto;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.view.SosCtg;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.service.DpsExceptDistInsSearchService;
import jp.co.takeda.service.DpsExceptDistInsService;
import jp.co.takeda.service.DpsPlannedCtgSearchService;
import jp.co.takeda.service.DpsSosCtgSearchService;
import jp.co.takeda.service.DpsSosJgiSearchService;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps201C01Form;

/**
 * Dps201C01((医)配分除外施設登録画面)のアクションクラス
 *
 * @author siwamoto
 */
@Controller("Dps201C01Action")
public class Dps201C01Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps201C01Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 検索系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsExceptDistInsSearchService")
	protected DpsExceptDistInsSearchService dpsExceptDistInsSearchService;

	/**
	 * 更新系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsExceptDistInsService")
	protected DpsExceptDistInsService dpsExceptDistInsService;

	/**
	 * 組織・従業員検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsSosJgiSearchService")
	protected DpsSosJgiSearchService dpsSosJgiSearchService;

	/**
	 * カテゴリ 検索サービス
	 */
    @Autowired(required = true)
    @Qualifier("dpsCodeMasterSearchService")
    protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

    /**
	 * 計画支援の組織カテゴリコード検索
	 */
	@Autowired(required = true)
	@Qualifier("dpsSosCtgSearchService")
	protected DpsSosCtgSearchService dpsSosCtgSearchService;

	/**
	 * 計画対象カテゴリ領域の検索サービス
	 */
    @Autowired(required = true)
    @Qualifier("dpsPlannedCtgSearchService")
    protected DpsPlannedCtgSearchService dpsPlannedCtgSearchService;

    /**
	 * 計画（1：期別、2：月別）
	 */
	private static final String PLAN_ID = "1";

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPS201C01";

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
	@Permission( authType = REFER)
	public Result dps201C01F00(DpContext ctx, Dps201C01Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps201C01F00");
		}

		// 施設検索モードをクリア
		form.setInsSelectMode(null);

		// 初期化処理
		form.formInit();

		// カテゴリ再取得
		initCategoryList(form);

		// 検索実行
//		List<ExceptDistInsPlannedProdResultDto> resultList = dpsExceptDistInsSearchService.searchExceptDistInsProd(form.getSosCd3(), form.getCategory());
		List<ExceptDistInsPlannedProdResultDto> resultList = dpsExceptDistInsSearchService.searchExceptDistInsProdExe( form.getSosCd3(), form.getCategory(), form.getCdMstCategoryList());
		super.getRequestBox().put(Dps201C01Form.DPS201C01_DATA_R, (ArrayList<ExceptDistInsPlannedProdResultDto>)resultList);

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　ONC_SOS_FLG→SOS_MST（category,subCategory）
//		// 選択中の組織のONCフラグ
		// 選択中の組織マスタ（カテゴリー、サブカテゴリー取得用）
		SosMst sosMst = dpsSosJgiSearchService.searchSos(form.getSosCd3());
//		super.getRequestBox().put(Dps201C01Form.DPS201C01_ONC_SOS_FLG, BooleanUtils.isTrue(sosMst.getOncSosFlg()));
		super.getRequestBox().put(Dps201C01Form.DPS201C01_SOS_MST, sosMst);
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　ONC_SOS_FLG→SOS_MST（category,subCategory）


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
	@Permission( authType = EDIT)
	public Result dps201C01F05Excecute(DpContext ctx, Dps201C01Form form) throws Exception {

		try {

			// 登録用DTOのリスト作成
			ExceptDistInsEntryDto entryDto = form.convertExceptDistInsEntryDto();

			// 登録実行
			dpsExceptDistInsService.entryExceptDistIns(entryDto);

			// 登録完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0006I", "試算・配分除外施設（品目）"));
			form.setAddInsList(null);
			form.setProdFlg("0");
			form.setProdCode(null);

		} finally {

			// カテゴリ再取得
			initCategoryList(form);

			// 検索実行
//			List<ExceptDistInsPlannedProdResultDto> resultList = dpsExceptDistInsSearchService.searchExceptDistInsProd(form.getSosCd3(),form.getCategory());
			List<ExceptDistInsPlannedProdResultDto> resultList = dpsExceptDistInsSearchService.searchExceptDistInsProdExe( form.getSosCd3(), form.getCategory(), form.getCdMstCategoryList());
			super.getRequestBox().put(Dps201C01Form.DPS201C01_DATA_R, (ArrayList<ExceptDistInsPlannedProdResultDto>)resultList);

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　ONC_SOS_FLG→SOS_MST（category,subCategory）
//			// 選択中の組織のONCフラグ
			// 選択中の組織マスタ（カテゴリー、サブカテゴリー取得用）
			SosMst sosMst = dpsSosJgiSearchService.searchSos(form.getSosCd3());
//			super.getRequestBox().put(Dps201C01Form.DPS201C01_ONC_SOS_FLG, BooleanUtils.isTrue(sosMst.getOncSosFlg()));
			super.getRequestBox().put(Dps201C01Form.DPS201C01_SOS_MST, sosMst);
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　ONC_SOS_FLG→SOS_MST（category,subCategory）
		}
		return ActionResult.SUCCESS;
	}

	/**
	 * 更新処理時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps201C01F05Validation(DpContext ctx, Dps201C01Form form) throws ValidateException {

		// 更新行のNullチェック
		String[] rowIdList = form.getSelectRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}

		for (int i = 0; i < rowIdList.length; i++) {

			// パラーメータのサイズチェック
			String[] rowId = rowIdList[i].split(",", 2);
			if (rowId.length != 2) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 施設コード
			if (StringUtils.isEmpty(rowId[0])) {
				final String errMsg = "受信パラメータ(施設コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
//			// 従業員コード
//			if (StringUtils.isEmpty(rowId[1]) || !ValidationUtil.isInteger(rowId[1])) {
//				final String errMsg = "受信パラメータ(従業員コード)が不正：";
//				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
//			}
		}

		//指定方法フラグ
//		String prodFlg = form.getProdFlg();
//		if (StringUtils.isEmpty(prodFlg)) {
//			final String errMsg = "受信パラメータ(指定方法)が不正：";
//			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
//
//		} else if (prodFlg.equals("1")) {
//
//			//配分除外品目リスト
//			String[] prodList = form.getProdCode();
//			if (prodList == null) {
//				final String errMsg = "受信パラメータ(配分除外品目)が不正：";
//				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
//			}
//
//			for (int i = 0; i < prodList.length; i++) {
//				if (StringUtils.isEmpty(prodList[i])) {
//					final String errMsg = "受信パラメータ(配分除外品目)が不正：";
//					throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
//				}
//			}
//		}
	}

	/**
     * カテゴリの初期設定
     * @param form Dps201C01Form
     */
    private void initCategoryList(Dps201C01Form form) {
		// 汎用マスタのカテゴリリスト
		List<DpsCCdMst> categoryList = (dpsCodeMasterSearchService.searchCategoryList(null,PLAN_ID));
		form.setCdMstCategoryList(categoryList);

		// カテゴリリストに表示するリスト
		List<String> targetCategoryAry = new ArrayList<String>();

		String sosCd = null;
		if (StringUtils.isNotBlank(form.getSosCd3())) {
			// 組織コード：営業所
			sosCd = form.getSosCd3();
		}
		// 組織と紐づくカテゴリリスト
		List<SosCtg> ctgList = dpsSosCtgSearchService.searchDpsSosCtgList(sosCd, SCREEN_ID);

		for(SosCtg ctg : ctgList) {
			targetCategoryAry.add(ctg.getCategory());
		}

		boolean indexFlg = true;
		for (DpsCCdMst mst :categoryList) {
			if (targetCategoryAry.contains(mst.getDataCd())) {
				if (form.getCategory() == null && indexFlg) {
					//dps201C00から貰うのでなく、dps201C01でセットする
					//施設検索画面の検索でカテゴリーを渡すため
					form.setSearchCategory(mst.getDataCd());
					indexFlg = false;
				}
			}
		}

		form.setCategory(targetCategoryAry.toArray(new String[targetCategoryAry.size()]));
    }
}
