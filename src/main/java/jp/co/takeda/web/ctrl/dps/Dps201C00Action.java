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
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dao.DpsCViSosCtgDao;
import jp.co.takeda.dto.ExceptDistInsDeleteDto;
import jp.co.takeda.dto.ExceptDistInsResultListDto;
import jp.co.takeda.dto.ExceptDistInsScDto;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.JknGrp;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.JokenKbn;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.model.view.SosCtg;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcUserSearchService;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.service.DpsExceptDistInsSearchService;
import jp.co.takeda.service.DpsExceptDistInsService;
import jp.co.takeda.service.DpsPlannedCtgSearchService;
import jp.co.takeda.service.DpsSosCtgSearchService;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps201C00Form;

/**
 * Dps201C00((医)配分除外施設一覧画面)のアクションクラス
 *
 * @author siwamoto
 */
@Controller("Dps201C00Action")
public class Dps201C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps201C00Action.class);

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPS201C00";

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
	 * 組織カテゴリDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsCViSosCtgDao")
	protected DpsCViSosCtgDao dpsCViSosCtgDao;

    /**
	 * 計画（1：期別、2：月別）
	 */
	private static final String PLAN_ID = "1";

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
	@Permission(authType = REFER)
	public Result dps201C00F00(DpContext ctx, Dps201C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps201C00F00");
		}

		// 初期化処理
		form.formInit();

		// ログインユーザの組織情報をセット
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {
				// 権限確認コード
				if (user.isSosLvl(SCREEN_ID, SosLvl.BRANCH)) {
					form.setSosCd2(user.getSosCd2());
				}
				else if (user.isSosLvl(SCREEN_ID, SosLvl.OFFICE)) {
					form.setSosCd2(user.getSosCd2());
					form.setSosCd3(user.getSosCd3());
				}
				else if (user.isSosLvl(SCREEN_ID, SosLvl.MR)) {
					form.setSosCd2(user.getSosCd2());
					form.setSosCd3(user.getSosCd3());
					form.setJgiNo(String.valueOf(user.getJgiNo()));
				}
				// 条件グループの特定
				JknGrp jknGrp = null;
				for(JknGrp tmpJknGrp : user.getJknGrpList()) {
					if(SCREEN_ID.toUpperCase().equals(tmpJknGrp.getJknGrpId().getDbValue()) ||
							SCREEN_ID.toUpperCase().substring(0, 6).equals(tmpJknGrp.getJknGrpId().getDbValue())) {
						jknGrp = tmpJknGrp;
						break;
					}
				}
				// ワクチンの場合
				if (jknGrp != null) {
					if(jknGrp.getJokenKbn().equals(JokenKbn.VAC_REFER) ||
							jknGrp.getJokenKbn().equals(JokenKbn.VAC_EDIT)) {
						// -------------------------
						// ワクチンのカテゴリ取得
						// -------------------------
						String vaccineCode = dpsCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.VAC.getDbValue())
								.get(0).getDataCd();
						form.setVaccineCode(vaccineCode);
					}
				}
			}
		}

		String[] sosCdCategory = new String[500];
		Integer iCnt = 0;
		// 組織と紐づくカテゴリリスト（全部）
		List<SosCtg> sosCtgList = new ArrayList<SosCtg>();
		try {
			sosCtgList = dpsCViSosCtgDao.search(null);
		} catch (DataNotFoundException e) {
			// 検索結果0件の場合は何もしない
		}
		for (SosCtg ctg : sosCtgList) {
			sosCdCategory[iCnt] = ctg.getSosCd() + "," + ctg.getCategory();
			iCnt = iCnt + 1;
		}
		form.setSosCdCategoryList(sosCdCategory);

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
	@Permission(authType = REFER)
	public Result dps201C00F05(DpContext ctx, Dps201C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps201C00F05");
			LOG.debug("SosCd3:" + form.getSosCd3());
			LOG.debug("SosCd4:" + form.getSosCd4());
			LOG.debug("JgiNo:" + form.getJgiNo());
		}

		// 検索条件を処理用フィールドに設定
		form.setTranField();

		// 組織従業員初期化処理
		// 担当者まで選ぶ画面なので不要

		// 検索実行
		searchExceptDistIns(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * 削除処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = EDIT)
	public Result dps201C00F10Execute(DpContext ctx, Dps201C00Form form) throws Exception {

		// 削除用DTOのリスト作成
		List<ExceptDistInsDeleteDto> deleteDtoList = form.convertExceptDistInsDeleteDto();
		try {

			// 削除実行
			dpsExceptDistInsService.deleteExceptDistIns(form.getSosCd3Tran(), deleteDtoList);

			// 削除完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0001I", String.valueOf(deleteDtoList.size())));

		} finally {

			// 削除対象の情報でFormを書き換える
			form.setSosCd3(form.getSosCd3Tran());
			form.setSosCd4(form.getSosCd4Tran());
			form.setJgiNo(form.getJgiNoTran());

			// 削除対象の情報で組織従業員を書き換える
			if (StringUtils.isNotBlank(form.getJgiNoTran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), form.getJgiNoTran());
			} else if (StringUtils.isNotBlank(form.getSosCd4Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd4Tran(), null);
			} else if (StringUtils.isNotBlank(form.getSosCd3Tran())) {
				dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd3Tran(), null);
			}

			// 前回検索条件で再検索実行
			searchExceptDistIns(form);

		}
		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理を実行する。
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchExceptDistIns(Dps201C00Form form) throws Exception {

		// 検索条件DTOの生成
		ExceptDistInsScDto scDto = form.convertExceptDistInsScDto();

		// 検索実行
		ExceptDistInsResultListDto resultList;
		try {
			resultList = dpsExceptDistInsSearchService.searchExceptDistIns(scDto);
		} catch (LogicalException e) {
			throw e;
		}

		// リクエストに検索結果リストをセット
		super.getRequestBox().put(Dps201C00Form.DPS201C00_DATA_R, resultList);
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
	public void dps201C00F05Validation(DpContext ctx, Dps201C00Form form) throws ValidateException {

		// 組織・従業員 必須チェック
		if (StringUtils.isEmpty(form.getSosCd3())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "組織・従業員")));
		}

		//カテゴリの初期設定
		initCategoryList(form);

		// カテゴリ 必須チェック
		if (StringUtils.isEmpty(form.getCategory())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1023E")));
		}
	}

	/**
	 * 削除処理時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps201C00F10Validation(DpContext ctx, Dps201C00Form form) throws ValidateException {

		// 更新行のNullチェック
		String[] rowIdList = form.getSelectRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}

		for (int i = 0; i < rowIdList.length; i++) {

			// パラーメータのサイズチェック
			String[] rowId = rowIdList[i].split(",", 3);
			if (rowId.length != 3) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 施設コード
			if (StringUtils.isEmpty(rowId[0])) {
				final String errMsg = "受信パラメータ(施設コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 従業員番号
			if (StringUtils.isEmpty(rowId[1])) {
				final String errMsg = "受信パラメータ(従業員番号)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 最終更新日
			if (StringUtils.isEmpty(rowId[2]) || !ValidationUtil.isLong(rowId[2])) {
				final String errMsg = "受信パラメータ(最終更新日)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
		}
	}

	/**
     * カテゴリの初期設定
     * @param form Dps201C00Form
     */
    private void initCategoryList(Dps201C00Form form) {
		// 汎用マスタのカテゴリリスト
		List<DpsCCdMst> categoryList = (dpsCodeMasterSearchService.searchCategoryList(null,PLAN_ID));

		// カテゴリリストに表示するリスト
		List<String> targetCategoryAry = new ArrayList<String>();
//見なくて良いので、コメント削除
//		// 計画対象カテゴリ領域の設定
//		List<String> planLvCtgList = new ArrayList<String>();
//		// 計画立案レベル-担当者
//		planLvCtgList = dpsPlannedCtgSearchService.searchCategoryByPlanLevel(ProdPlanLevel.MR);

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
			if (targetCategoryAry.contains(mst.getDataCd())) {
//			if (targetCategoryAry.contains(mst.getDataCd()) && planLvCtgList.contains(mst.getDataCd())) {
				CodeAndValue cad = new CodeAndValue(mst.getDataCd(), mst.getDataName());
				list.add(cad);
				if (form.getCategory() == null && indexFlg) {
					form.setCategory(cad.getCode());
					indexFlg = false;
				}
			}
		}
    }
}
