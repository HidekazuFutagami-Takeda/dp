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
import jp.co.takeda.dto.ExceptDistInsPlannedProdResultDto;
import jp.co.takeda.dto.ExceptDistInsUpdateInitDto;
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
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps201C02Form;

/**
 * Dps201C02((医)配分除外施設編集画面)のアクションクラス
 *
 * @author siwamoto
 */
@Controller("Dps201C02Action")
public class Dps201C02Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps201C02Action.class);

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPS201C02";

	/**
	 * 計画（1：期別、2：月別）
	 */
	private static final String PLAN_ID = "1";

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 検索系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsExceptDistInsSearchService")
	protected DpsExceptDistInsSearchService searchService;

	/**
	 * 更新系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsExceptDistInsService")
	protected DpsExceptDistInsService updateService;

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
	public Result dps201C02F00(DpContext ctx, Dps201C02Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps201C02F00");
		}

		if (StringUtils.isNotEmpty(form.getDispJgiName())) {
			//?表示になるのを修正
			//String decodeStr = new String(form.getDispJgiName().getBytes("8859_1"), "UTF-8");
			String decodeStr = form.getDispJgiName();
			form.setDispJgiName(decodeStr);
		}

		// 初期化処理
		form.formInit();

		//カテゴリリストの設定
		initCategoryList(form);

		// 検索実行
		ExceptDistInsUpdateInitDto dto = searchExe(form);

		// 初期表示の設定
//		if (dto.getProdCodeList() != null && dto.getProdCodeList().size() != 0) {
//			form.setProdCode(dto.getProdCodeList().toArray(new String[dto.getProdCodeList().size()]));
//			form.setProdFlg("1");
//		} else {
//			//試算・配分除外施設
//			form.setProdFlg("0");
//		}
		form.setProdFlg("0");
		form.setExclusionFlg(dto.getExclusionFlg());
		for (ExceptDistInsPlannedProdResultDto plannedProd  : dto.getProdList()) {
			//組織のカテゴリーに属する品目の内、試算・配分除外品目の登録がある場合
			if (plannedProd.getStrEstimationFlg().equals("1") || plannedProd.getStrExceptFlg().equals("1")) {
				//試算・配分除外品目
				form.setProdFlg("1");
				break;
			}
		}

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
	@Permission( authType = EDIT)
	public Result dps201C02F05Excecute(DpContext ctx, Dps201C02Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps201C02F05Excecute");
		}
		try {

			// 更新処理の実行
			updateService.updateExceptDistIns(form.convertExceptDistInsUpdateDto());

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0004I", "試算・配分除外施設（品目）"));

		} finally {

			// 検索実行
			searchExe(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理を実行する。
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private ExceptDistInsUpdateInitDto searchExe(Dps201C02Form form) throws Exception {

		// 検索実行
		ExceptDistInsUpdateInitDto dto = searchService.searchExceptDistInsCategoryMulti(form.getInsNo(), form.getDispJgiName(), form.getSosCd(), form.getCategory(), form.getCdMstCategoryList(), form.getInsType());
		super.getRequestBox().put(Dps201C02Form.DPS201C02_DATA_R, dto);

		SosMst sosMst = dpsSosJgiSearchService.searchSos(form.getSosCd());
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　ONC_SOS_FLG→SOS_MST（category,subCategory）
//		super.getRequestBox().put(Dps201C02Form.DPS201C02_ONC_SOS_FLG, BooleanUtils.isTrue(sosMst.getOncSosFlg()));
		super.getRequestBox().put(Dps201C02Form.DPS201C02_SOS_MST, sosMst);
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　ONC_SOS_FLG→SOS_MST（category,subCategory）

		return dto;
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
	public void dps201C02F05Validation(DpContext ctx, Dps201C02Form form) throws ValidateException {

		// 非表示、非入力項目チェック
		if (StringUtils.isEmpty(form.getInsNo())) {
			final String errMsg = "受信パラメータ(施設コード)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
//		if (StringUtils.isEmpty(form.getJgiNo())) {
//			final String errMsg = "受信パラメータ(従業員番号)が不正：";
//			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
//		}
		if (StringUtils.isEmpty(form.getProdFlg())) {
			final String errMsg = "受信パラメータ(指定方法フラグ)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isEmpty(form.getUpDate()) || !ValidationUtil.isLong(form.getUpDate())) {
			final String errMsg = "受信パラメータ(最終更新日)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if(form.getProdFlg().equals("0")) {
			//試算・配分除外施設の場合
			String[] exclusionFlg = form.getExclusionFlg();
			if (StringUtils.isEmpty(exclusionFlg[0].toString())) {
				final String errMsg = "受信パラメータ(試算除外フラグ)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			if (StringUtils.isEmpty(exclusionFlg[1].toString())) {
				final String errMsg = "受信パラメータ(配分除外フラグ)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
		}
	}

    /**
     * カテゴリリストの初期設定
     * @param form Dps201C02Form
     */
    private void initCategoryList(Dps201C02Form form) {
		// 汎用マスタのカテゴリリスト
		List<DpsCCdMst> categoryList = (dpsCodeMasterSearchService.searchCategoryList(null,PLAN_ID));
		form.setCdMstCategoryList(categoryList);

		// カテゴリリストに表示するリスト
		List<String> targetCategoryAry = new ArrayList<String>();

		String sosCd = null;
		if (StringUtils.isNotBlank(form.getSosCd())) {
			// 組織コード：営業所
			sosCd = form.getSosCd();
		}
		// 組織と紐づくカテゴリリスト
		List<SosCtg> ctgList = dpsSosCtgSearchService.searchDpsSosCtgList(sosCd, SCREEN_ID);

		for(SosCtg ctg : ctgList) {
			targetCategoryAry.add(ctg.getCategory());
		}

		form.setCategory(targetCategoryAry.toArray(new String[targetCategoryAry.size()]));
    }

}
