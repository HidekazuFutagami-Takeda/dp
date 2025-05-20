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
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.OfficePlanResultListDto;
import jp.co.takeda.dto.OfficePlanUpdateDto;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.model.view.SosCtg;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcUserSearchService;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.service.DpsOfficePlanSearchService;
import jp.co.takeda.service.DpsOfficePlanService;
import jp.co.takeda.service.DpsPlannedCtgSearchService;
import jp.co.takeda.service.DpsSosCtgSearchService;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps300C00Form;

/**
 * Dps300C00((医)営業所計画編集画面)のアクションクラス
 *
 * @author nozaki
 */
@Controller("Dps300C00Action")
public class Dps300C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps300C00Action.class);

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPS300C00";

	/**
	 * 計画（1：期別、2：月別）
	 */
	private static final String PLAN_ID = "1";


	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 営業所計画 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsOfficePlanSearchService")
	protected DpsOfficePlanSearchService dpsOfficePlanSearchService;

	/**
	 * 営業所計画 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsOfficePlanService")
	protected DpsOfficePlanService dpsOfficePlanService;

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
	 * ユーザサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpcUserSearchService")
	protected DpcUserSearchService dpcUserSearchService;

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
	@Permission(authType = AuthType.REFER)
	public Result dps300C00F00(DpContext ctx, Dps300C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps300C00F00");
		}

		// クリック行ＩＤをクリア
		form.setClickRowId(null);

		// 前画面にて選択されていたカテゴリの保持
		String categoryBackUp = form.getCategory();

		// 初期化処理
		form.formInit();

		// 組織従業員初期化処理
		SosMst defaultSosMst = DpUserInfo.getDpUserInfo().getDefaultSosMst();
		if (defaultSosMst != null) {
			if (defaultSosMst.getBumonRank() == BumonRank.OFFICE_TOKUYAKUTEN_G) {
				form.setSosCd3(defaultSosMst.getSosCd());
				dpcUserSearchService.changeDefaultSosJgi(null, null, defaultSosMst.getSosCd(), null);
			} else if (defaultSosMst.getBumonRank() == BumonRank.TEAM) {
				form.setSosCd3(defaultSosMst.getUpSosCd());
				dpcUserSearchService.changeDefaultSosJgi(null, null, defaultSosMst.getUpSosCd(), null);
			}
			//カテゴリリストの設定
			initCategoryList(form);
		}

		// ログインユーザの組織情報をセット
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {
				// 権限確認コード
				if (user.isSosLvl(SCREEN_ID, SosLvl.BRANCH)) {
					if (!user.getSosCd3().equals(JgiMst.BLANK_SOS_CD)) {
						form.setSosCd3(user.getSosCd3());
					}
				}
				else if (user.isSosLvl(SCREEN_ID, SosLvl.OFFICE)) {
					if (!user.getSosCd3().equals(JgiMst.BLANK_SOS_CD)) {
						form.setSosCd3(user.getSosCd3());
					}
				}
				else if (user.isSosLvl(SCREEN_ID, SosLvl.MR)) {
					if (!user.getSosCd3().equals(JgiMst.BLANK_SOS_CD)) {
						form.setSosCd3(user.getSosCd3());
					}
				}
			}
		}

		//カテゴリリストの設定
		initCategoryList(form);

		//前画面にて選択していたカテゴリを再設定（可能なら）
		List<String> categoryList = new ArrayList<String>();
		for (CodeAndValue codeAndValue : form.getProdCategoryList()) {
			categoryList.add(codeAndValue.getCode());
		}
		if(categoryBackUp != null && categoryList.contains(categoryBackUp)) {
			form.setCategory(categoryBackUp);
		}

		// 検索実行
		form.setTranField();
		searchOfficePlan(form, false);

		//グリッドのヘッダを設定
		setGridHeader(form);

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
	@RequestType
	@Permission(authType = AuthType.REFER)
	public Result dps300C00F05(DpContext ctx, Dps300C00Form form) throws Exception {

		// 検索実行
		form.setTranField();
		searchOfficePlan(form, false);

		//カテゴリリストの設定
		initCategoryList(form);
		//グリッドのヘッダを設定
		setGridHeader(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * 調整金額再計算ボタン押下時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.REFER)
	public Result dps300C00F06(DpContext ctx, Dps300C00Form form) throws Exception {

		// 検索実行
		form.setTranField();
		searchOfficePlan(form, true);

		//カテゴリリストの設定
		initCategoryList(form);
		//グリッドのヘッダを設定
		setGridHeader(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * 下書保存処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps300C00F10Execute(DpContext ctx, Dps300C00Form form) throws Exception {

		try {

			String sosCd = form.getSosCd3Tran();
			String category = form.getCategory();
			List<OfficePlanUpdateDto> officePlanList = form.getUpdateOfficePlanList();

			// 更新処理実行
			dpsOfficePlanService.updateOfficePlanToDraft(sosCd, category, officePlanList);

			// 更新完了メッセージの追加
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			addMessage(ctx, new MessageKey("DPC0004I", "エリア計画"));
//			addMessage(ctx, new MessageKey("DPC0004I", "営業所計画"));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setCategory(form.getCategoryTran());
			form.setSosCd3(form.getSosCd3Tran());

			// 更新対象の情報で組織従業員を書き換える
			dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd3(), null);

			// 前回検索条件で再検索実行
			searchOfficePlan(form, false);
			//カテゴリリストの設定
			initCategoryList(form);
			//グリッドのヘッダを設定
			setGridHeader(form);
		}

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
	public Result dps300C00F15Execute(DpContext ctx, Dps300C00Form form) throws Exception {

		try {
			String sosCd = form.getSosCd3Tran();
			String category = form.getCategory();
			List<OfficePlanUpdateDto> officePlanList = form.getUpdateOfficePlanList();

			// 更新処理実行
			dpsOfficePlanService.updateOfficePlanToComplete(sosCd, category, officePlanList);

			// 更新完了メッセージの追加
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			addMessage(ctx, new MessageKey("DPC0004I", "エリア計画"));
//			addMessage(ctx, new MessageKey("DPC0004I", "営業所計画"));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setCategory(form.getCategoryTran());
			form.setSosCd3(form.getSosCd3Tran());

			// 更新対象の情報で組織従業員を書き換える
			dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd3(), null);

			// 前回検索条件で再検索実行
			searchOfficePlan(form, false);
			//カテゴリリストの設定
			initCategoryList(form);
			//グリッドのヘッダを設定
			setGridHeader(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理を実行する。
	 *
	 * @param form ActionForm
	 * @param updateFlg 調整金額テーブルの更新有無
	 * @throws Exception 例外
	 */
	private void searchOfficePlan(Dps300C00Form form, boolean updateFlg) throws Exception {

		OfficePlanResultListDto result = null;
		form.setExistSearchDataFlag(false);

		// 組織コード（営業所）が設定されている場合は、検索表示
		if (!StringUtils.isEmpty(form.getSosCd3Tran())) {

			// カテゴリ 必須チェック ( 初期表示からの検索時、バリデーションを通らないためここでチェックしている）
			if (StringUtils.isEmpty(form.getCategory())) {
				throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "カテゴリ")));
			}

			// 検索実行
			result = dpsOfficePlanSearchService.searchOfficePlan(form.getSosCd3Tran(), form.getCategory(), updateFlg);

			// リクエストに検索結果リストをセット
			super.getRequestBox().put(Dps300C00Form.DPS300C00_DATA_R, result);
			form.setExistSearchDataFlag(true);
		}
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
	public void dps300C00F05Validation(DpContext ctx, Dps300C00Form form) throws ValidateException {

		// 組織 必須チェック
		if (StringUtils.isEmpty(form.getSosCd3())) {
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			throw new ValidateException(new Conveyance(new MessageKey("DPC1024E", "組織", "エリア")));
//			throw new ValidateException(new Conveyance(new MessageKey("DPC1024E", "組織", "営業所")));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
		}

		// 対象区分 必須チェック
		if (StringUtils.isEmpty(form.getCategory())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "カテゴリ")));
		}
	}

	/**
	 * 下書保存処理時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps300C00F10Validation(DpContext ctx, Dps300C00Form form) throws ValidateException {

		// 組織 必須チェック
		if (StringUtils.isEmpty(form.getSosCd3Tran())) {
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			throw new ValidateException(new Conveyance(new MessageKey("DPC1024E", "組織", "エリア")));
//			throw new ValidateException(new Conveyance(new MessageKey("DPC1024E", "組織", "営業所")));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
		}

		// 対象区分 必須チェック
		if (StringUtils.isEmpty(form.getCategoryTran())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "カテゴリ")));
		}

		// 営業所計画チェック
		officePlanValueCheck(form, true);
	}

	/**
	 * 登録処理時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps300C00F15Validation(DpContext ctx, Dps300C00Form form) throws ValidateException {

		// 組織 必須チェック
		if (StringUtils.isEmpty(form.getSosCd3Tran())) {
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			throw new ValidateException(new Conveyance(new MessageKey("DPC1024E", "組織", "エリア")));
//			throw new ValidateException(new Conveyance(new MessageKey("DPC1024E", "組織", "営業所")));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
		}

		// 対象区分 必須チェック
		if (StringUtils.isEmpty(form.getCategoryTran())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "カテゴリ")));
		}

		// 営業所計画チェック
		officePlanValueCheck(form, false);
	}

	/**
	 * 営業所計画の入力チェックを行う。
	 *
	 * @param form
	 * @param isDraft true：下書保存、false：登録
	 * @throws ValidateException
	 */
	private void officePlanValueCheck(Dps300C00Form form, boolean isDraft) throws ValidateException {

		// 更新行のNullチェック
		String[] rowIdList = form.getRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}

		for (int i = 0; i < rowIdList.length; i++) {

			String[] rowId = rowIdList[i].split(",", 5);
			if (rowId.length != 5) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			if (!isDraft) {

				// 計画値UH
				if (StringUtils.isEmpty(rowId[3])) {
					throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "UH合計③")));
				}
				// 計画値P
				if (StringUtils.isEmpty(rowId[4])) {
					throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "P")));
				}
			}

			// シーケンスキー
			if (!StringUtils.isEmpty(rowId[0]) && !ValidationUtil.isLong(rowId[0])) {
				final String errMsg = "受信パラメータ(シーケンスキー)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 品目固定コード
			if (StringUtils.isEmpty(rowId[1])) {
				final String errMsg = "受信パラメータ(品目固定コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 最終更新日
			if (!StringUtils.isEmpty(rowId[2]) && !ValidationUtil.isLong(rowId[2])) {
				final String errMsg = "受信パラメータ(最終更新日)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 計画値UH
			if (!StringUtils.isEmpty(rowId[3]) && !ValidationUtil.isLong(rowId[3])) {
				final String errMsg = "受信パラメータ(UH合計③)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 計画値P
			if (!StringUtils.isEmpty(rowId[4]) && !ValidationUtil.isLong(rowId[4])) {
				final String errMsg = "受信パラメータ(P)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
		}
	}


	/**
	 * グリッドのヘッダを設定する
	 * @param form
	 */
	private void setGridHeader(Dps300C00Form form) {

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

			attachHeader = "#rspan," +
			               list.get(0).getValue() + "," + list.get(1).getValue() + ",UHP合計," +
			               list.get(0).getValue() + ",#cspan," + list.get(1).getValue() + ",#cspan,UHP計," +
			               "　," + list.get(0).getValue() + "," + list.get(1).getValue() + ",UHP計"+
					       ",　," + list.get(0).getValue() + "," + list.get(1).getValue() + ",UHP計";
	    }
		//グリッドの追加ヘッダ
		form.setAttachHeader(attachHeader);

	}

    /**
     * カテゴリリストの初期設定
     * @param form Dps300C00Form
     */
    private void initCategoryList(Dps300C00Form form) {
		// 汎用マスタのカテゴリリスト
		List<DpsCCdMst> categoryList = (dpsCodeMasterSearchService.searchCategoryList(null,PLAN_ID));

		// カテゴリリストに表示するリスト
		List<String> targetCategoryAry = new ArrayList<String>();
		// 計画対象カテゴリ領域の設定
		List<String> planLvCtgList = new ArrayList<String>();
		// 計画立案レベル-営業所
		planLvCtgList = dpsPlannedCtgSearchService.searchCategoryByPlanLevel(ProdPlanLevel.OFFICE);

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

		List<CodeAndValue> list = new ArrayList<CodeAndValue>();

		boolean indexFlg = true;
		// 対象の立案レベルをもつ計画対象カテゴリ領域のカテゴリのみ設定
		for (DpsCCdMst mst :categoryList) {
			if (targetCategoryAry.contains(mst.getDataCd()) && planLvCtgList.contains(mst.getDataCd())) {
				CodeAndValue cad = new CodeAndValue(mst.getDataCd(), mst.getDataName());
				list.add(cad);
				if (form.getCategory() == null && indexFlg) {
					form.setCategory(cad.getCode());
					indexFlg = false;
				}
			}
		}
		form.setProdCategoryList(list);
    }

}
