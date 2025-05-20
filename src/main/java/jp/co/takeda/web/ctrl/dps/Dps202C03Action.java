package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.exp.ErrMessageKey.*;
import static jp.co.takeda.security.DpAuthority.AuthType.*;

import java.util.ArrayList;
import java.util.List;

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
import jp.co.takeda.dto.SpecialInsPlanModifyForMrDto;
import jp.co.takeda.dto.SpecialInsPlanSearchForVacResultListDto;
import jp.co.takeda.dto.SpecialInsPlanSearchOfficeDto;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.JknGrp;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.JokenKbn;
import jp.co.takeda.model.div.PlanType;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcUserSearchService;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.service.DpsSpecialInsPlanForVacSearchService;
import jp.co.takeda.service.DpsSpecialInsPlanForVacService;
import jp.co.takeda.service.DpsSpecialInsPlanSearchService;
import jp.co.takeda.service.DpsSpecialInsPlanService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps202C03Form;

/**
 * Dps202C03((医)特定施設個別計画立案画面(担当者立案))のアクションクラス
 *
 * @author siwamoto
 */
@Controller("Dps202C03Action")
public class Dps202C03Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps202C03Action.class);

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPS202C03";

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * サービス（検索系）
	 */
	@Autowired(required = true)
	@Qualifier("dpsSpecialInsPlanSearchService")
	protected DpsSpecialInsPlanSearchService dpsSpecialInsPlanSearchService;

	/**
	 * サービス（更新系）
	 */
	@Autowired(required = true)
	@Qualifier("dpsSpecialInsPlanService")
	protected DpsSpecialInsPlanService dpsSpecialInsPlanService;

	/**
	 * ユーザサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpcUserSearchService")
	protected DpcUserSearchService dpcUserSearchService;

	/**
	 * 汎用マスタ検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	/**
	 * ワクチン用特定施設個別計画 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsSpecialInsPlanForVacSearchService")
	protected DpsSpecialInsPlanForVacSearchService dpsSpecialInsPlanForVacSearchService;

	/**
	 * ワクチン用特定施設個別計画 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsSpecialInsPlanForVacService")
	protected DpsSpecialInsPlanForVacService dpsSpecialInsPlanForVacService;

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
	public Result dps202C03F00(DpContext ctx, Dps202C03Form form) throws Exception {

		Integer jgiNo = ConvertUtil.parseInteger(form.getJgiNo());
		String insNo = form.getInsNo();
		String sosCd3 = form.getSosCd3();

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps202C03F00");
			LOG.debug("jgiNo=" + jgiNo);
			LOG.debug("insNo=" + insNo);
			LOG.debug("sosCd3=" + sosCd3);
		}

		// 特約店検索モードをクリア
		form.setTmsSelectMode(null);

		// 初期化処理
		form.formInit();

		// 組織従業員初期化処理
		// 担当者まで選ぶ画面なので不要

		// ログインユーザの組織情報をセット
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {
				// 権限確認コード
				if (user.isSosLvl(SCREEN_ID, SosLvl.BRANCH)) {
					form.setSosCd3(user.getSosCd3());
				} else if (user.isSosLvl(SCREEN_ID, SosLvl.OFFICE)) {
					form.setSosCd3(user.getSosCd3());
				} else if (user.isSosLvl(SCREEN_ID, SosLvl.MR)) {
					form.setSosCd3(user.getSosCd3());
					form.setJgiNo(String.valueOf(user.getJgiNo()));
				}
				// 条件グループの特定
				JknGrp jknGrp = null;
				for (JknGrp tmpJknGrp : user.getJknGrpList()) {
					if (SCREEN_ID.toUpperCase().substring(0, 9).equals(tmpJknGrp.getJknGrpId().getDbValue())) {
						jknGrp = tmpJknGrp;
						break;
					}
				}

				if (jknGrp.getJokenKbn().equals(JokenKbn.VAC_REFER) ||
						jknGrp.getJokenKbn().equals(JokenKbn.VAC_EDIT)) {
					// ワクチンの場合
					form.setVaccine("1");
					form.setVaccineUser(Boolean.TRUE);
				}else {
					// ワクチン以外の場合
					form.setVaccine("0");
					form.setVaccineUser(Boolean.FALSE);
				}
			}
		}

		// カテゴリリストの生成
		List<DpsCCdMst> categoryMstList = dpsCodeMasterSearchService.searchShienCategoryList(sosCd3,Dps202C03Form.DPS202C03_PLANLEVEL_INS);
		List<CodeAndValue> categoryList = new ArrayList<CodeAndValue>();

		// ワクチンコードの取得
		String vaccineCd = dpsCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.VAC.getDbValue()).get(0)
				.getDataCd();

		boolean indexFlg = true;
		for (DpsCCdMst mst : categoryMstList) {
			// ワクチンはカテゴリリストには加えない
			if (!vaccineCd.equals(mst.getDataCd())) {
				categoryList.add(new CodeAndValue(mst.getDataCd(), mst.getDataName()));
				if (indexFlg) {
					form.setProdCategory(mst.getDataCd());
					indexFlg = false;
				}
			}
		}
		form.setCategoryList(categoryList);

		String category = form.getProdCategory();

		SpecialInsPlanSearchOfficeDto r = dpsSpecialInsPlanSearchService.searchOfficeProdList(jgiNo, insNo, category,false);

		//対象区分
		form.setHoInsType(r.getInsMst().getHoInsType());

		super.getRequestBox().put(Dps202C03Form.DPS202C03_DATA_R, r);

		return ActionResult.SUCCESS;
	}

	/**
	 * (ワ)初期表示時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = REFER)
	public Result dps202C03F20(DpContext ctx, Dps202C03Form form) throws Exception {

		Integer jgiNo = ConvertUtil.parseInteger(form.getJgiNo());
		String insNo = form.getInsNo();
		String sosCd3 = form.getSosCd3();

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps202C03F20");
			LOG.debug("jgiNo=" + jgiNo);
			LOG.debug("insNo=" + insNo);
			LOG.debug("sosCd3=" + sosCd3);
		}

		// 特約店検索モードをクリア
		form.setTmsSelectMode(null);

		// 初期化処理
		form.formInit();

		// サービス呼び出し
		SpecialInsPlanSearchForVacResultListDto r = dpsSpecialInsPlanForVacSearchService.searchMrProdList(jgiNo, insNo);

		//対象区分
		form.setHoInsType(r.getInsMst().getHoInsType());

		super.getRequestBox().put(Dps202C03Form.DPS202C03_V_DATA_R, r);
		return ActionResult.SUCCESS;
	}

	/**
	 * 適用ボタンクリック時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.REFER)
	public Result dps202C03F01(DpContext ctx, Dps202C03Form form) throws Exception {

		Integer jgiNo = ConvertUtil.parseInteger(form.getJgiNo());
		String insNo = form.getInsNo();

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps202C03F01");
		}

		SpecialInsPlanSearchOfficeDto r = dpsSpecialInsPlanSearchService.searchOfficeProdList(jgiNo, insNo,
				form.getProdCategory(), true);
		super.getRequestBox().put(Dps202C03Form.DPS202C03_DATA_R, r);

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
	public Result dps202C03F05Excecute(DpContext ctx, Dps202C03Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps202C03F05");
		}
		try {

			SpecialInsPlanModifyForMrDto modifyDto = form.getSpecialInsPlanModifyDto();
			dpsSpecialInsPlanService.modifyMr(modifyDto, form.getProdCategory(), form.getHoInsType());
			addMessage(ctx, new MessageKey("DPC0006I", "特定施設個別計画(担当者立案)"));

		} finally {

			// 登録後の再検索
			Integer jNo = ConvertUtil.parseInteger(form.getJgiNo());
			String iNo = form.getInsNo();
			String c = form.getProdCategory();
			SpecialInsPlanSearchOfficeDto r = dpsSpecialInsPlanSearchService.searchOfficeProdList(jNo, iNo, c, true);
			super.getRequestBox().put(Dps202C03Form.DPS202C03_DATA_R, r);
		}
		return ActionResult.SUCCESS;
	}

	/**
	 * (ワ)登録するボタンクリック時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = EDIT)
	public Result dps202C03F25Excecute(DpContext ctx, Dps202C03Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps202C03F25");
		}

		try {

			dpsSpecialInsPlanForVacService.modifyVac(form.getSpecialInsPlanModifyForVacDto(), PlanType.MR, form.getHoInsType());
			addMessage(ctx, new MessageKey("DPC0006I", "特定施設個別計画（担当者案）"));

		} finally {

			// 登録後の再検索
			Integer jgiNo = ConvertUtil.parseInteger(form.getJgiNo());
			String insNo = form.getInsNo();
			SpecialInsPlanSearchForVacResultListDto r = dpsSpecialInsPlanForVacSearchService.searchMrProdList(jgiNo, insNo);

			super.getRequestBox().put(Dps202C03Form.DPS202C03_V_DATA_R, r);
		}
		return ActionResult.SUCCESS;
	}


	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * 入力チェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps202C03F05Validation(DpContext ctx, Dps202C03Form form) throws Exception {
		if (form.getPara() == null) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1034E")));
		}
		for (int i = 0; i < form.getPara().length; i++) {
			String[] paraArray = form.getPara()[i].split(",");
			if (paraArray.length > 3) {
				String valueStr = paraArray[3];
				if (valueStr.equals("") || valueStr.equals("-")) {
					valueStr = null;
				} else {
					// 登録値 数値チェック
					if (!ValidationUtil.isDouble(valueStr)) {
						throw new ValidateException(new Conveyance(VALIDATE_ERROR));
					}
					// 登録値 整数チェック
					if (!ValidationUtil.isLong(valueStr)) {
						throw new ValidateException(new Conveyance(VALIDATE_ERROR));
					}
					// 登録値 桁数チェック
					if (!ValidationUtil.isMaxLength(valueStr, 10)) {
						throw new ValidateException(new Conveyance(VALIDATE_ERROR));
					}
					// 登録値 最小値チェック
					Long value = ConvertUtil.parseLong(valueStr);
					if (value < 0L) {
						throw new ValidateException(new Conveyance(VALIDATE_ERROR));
					}
				}
			}
		}
	}
}
