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
import jp.co.takeda.dto.SpecialInsPlanSearchForVacResultListDto;
import jp.co.takeda.dto.SpecialInsPlanSearchOfficeDto;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.PlanType;
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
import jp.co.takeda.web.in.dps.Dps202C01Form;

/**
 * Dps202C01((医)特定施設個別計画立案画面(営業所案))のアクションクラス
 *
 * @author siwamoto
 */
@Controller("Dps202C01Action")
public class Dps202C01Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps202C01Action.class);

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
	public Result dps202C01F00(DpContext ctx, Dps202C01Form form) throws Exception {

		Integer jgiNo = ConvertUtil.parseInteger(form.getJgiNo());
		String insNo = form.getInsNo();
		String sosCd3 = form.getSosCd3();
		String insType = form.getInsType();

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps202C01F00");
			LOG.debug("jgiNo=" + jgiNo);
			LOG.debug("insNo=" + insNo);
			LOG.debug("sosCd3=" + sosCd3);
		}

		// 特約店検索モードをクリア
		form.setTmsSelectMode(null);

		// 初期化処理
		form.formInit();

		// カテゴリリストの生成
		List<DpsCCdMst> categoryMstList = dpsCodeMasterSearchService.searchShienCategoryList(sosCd3, Dps202C01Form.DPS202C01_PLANLEVEL_INS);
		List<CodeAndValue> categoryList = new ArrayList<CodeAndValue>();

		// ワクチンコードの取得
		String vaccineCd = dpsCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.VAC.getDbValue()).get(0).getDataCd();

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

		SpecialInsPlanSearchOfficeDto r = dpsSpecialInsPlanSearchService.searchOfficeProdList(jgiNo, insNo, category, false);

		//対象区分
		form.setHoInsType(r.getInsMst().getHoInsType());

		super.getRequestBox().put(Dps202C01Form.DPS202C01_DATA_R, r);
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
	public Result dps202C01F20(DpContext ctx, Dps202C01Form form) throws Exception {

		Integer jgiNo = ConvertUtil.parseInteger(form.getJgiNo());
		String insNo = form.getInsNo();
		String sosCd3 = form.getSosCd3();
		// add 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
		String addrCodePref = form.getAddrCodePref();

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps202C01F20");
			LOG.debug("jgiNo=" + jgiNo);
			LOG.debug("insNo=" + insNo);
			LOG.debug("sosCd3=" + sosCd3);
		}

		// 特約店検索モードをクリア
		form.setTmsSelectMode(null);

		// 初期化処理
		form.formInit();

		// mod 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
		// サービス呼び出し
		SpecialInsPlanSearchForVacResultListDto r = dpsSpecialInsPlanForVacSearchService.searchOfficeProdList(jgiNo, insNo, addrCodePref);

		//対象区分
		form.setHoInsType(r.getInsMst().getHoInsType());

		super.getRequestBox().put(Dps202C01Form.DPS202C01_V_DATA_R, r);
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
	@Permission(authType = REFER)
	public Result dps202C01F01(DpContext ctx, Dps202C01Form form) throws Exception {

		Integer jgiNo = ConvertUtil.parseInteger(form.getJgiNo());
		String insNo = form.getInsNo();

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps202C01F01");
		}

		SpecialInsPlanSearchOfficeDto r = dpsSpecialInsPlanSearchService.searchOfficeProdList(jgiNo, insNo, form.getProdCategory(), false);

		super.getRequestBox().put(Dps202C01Form.DPS202C01_DATA_R, r);
		return ActionResult.SUCCESS;
	}

	/**
	 * 登録するボタンクリック時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = EDIT)
	public Result dps202C01F05Excecute(DpContext ctx, Dps202C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps202C01F05");
		}

		try {

			// 登録処理
			dpsSpecialInsPlanService.modifyOffice(form.getSpecialInsPlanModifyDto());
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			addMessage(ctx, new MessageKey("DPC0006I", "特定施設個別計画(エリア案)"));
//			addMessage(ctx, new MessageKey("DPC0006I", "特定施設個別計画(営業所案)"));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）

		} finally {

			// 登録後の再検索
			Integer jNo = ConvertUtil.parseInteger(form.getJgiNo());
			String iNo = form.getInsNo();
			String c = form.getProdCategory();
			SpecialInsPlanSearchOfficeDto r = dpsSpecialInsPlanSearchService.searchOfficeProdList(jNo, iNo, c, false);
			super.getRequestBox().put(Dps202C01Form.DPS202C01_DATA_R, r);
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
	public Result dps202C01F25Excecute(DpContext ctx, Dps202C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps202C01F25");
		}

		try {

			dpsSpecialInsPlanForVacService.modifyVac(form.getSpecialInsPlanModifyForVacDto(), PlanType.OFFICE, form.getHoInsType());
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			addMessage(ctx, new MessageKey("DPC0006I", "特定施設個別計画（エリア案）"));
//			addMessage(ctx, new MessageKey("DPC0006I", "特定施設個別計画（営業所案）"));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）

		} finally {

			// 登録後の再検索
			Integer jgiNo = ConvertUtil.parseInteger(form.getJgiNo());
			String insNo = form.getInsNo();
			String addrCodePref = form.getAddrCodePref();
			SpecialInsPlanSearchForVacResultListDto r = dpsSpecialInsPlanForVacSearchService.searchOfficeProdList(jgiNo, insNo, addrCodePref);
			super.getRequestBox().put(Dps202C01Form.DPS202C01_V_DATA_R, r);
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
	public void dps202C01F05Validation(DpContext ctx, Dps202C01Form form) throws Exception {
		if (form.getPara() == null) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1034E")));
		}
		for (int i = 0; i < form.getPara().length; i++) {
			String[] paraArray = form.getPara()[i].split(",");
			if (paraArray.length > 3) {
				String valueStr = paraArray[3];
				if (valueStr.equals("")) {
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
