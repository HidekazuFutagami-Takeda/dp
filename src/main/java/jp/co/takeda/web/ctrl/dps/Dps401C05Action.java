package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.exp.ErrMessageKey.VALIDATE_ERROR;
import static jp.co.takeda.security.BusinessType.VACCINE;
import static jp.co.takeda.security.DpAuthority.AuthType.EDIT;
import static jp.co.takeda.security.DpAuthority.AuthType.REFER;


import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.InsWsPlanForVacUpDateModifyAllDto;
import jp.co.takeda.dto.InsWsPlanForVacUpDateResultDto;
import jp.co.takeda.dto.InsWsPlanForVacUpDateResultListDto;
import jp.co.takeda.dto.InsWsPlanForVacUpDateScDto;
import jp.co.takeda.dto.InsWsPlanUpDateMonNnuScDto;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsInsWsPlanForVacSearchService;
import jp.co.takeda.service.DpsInsWsPlanForVacService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps401C05Form;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Dps401C05((ワ)施設特約店別計画編集画面)のアクションクラス
 *
 * @author siwamoto
 */
@Controller("Dps401C05Action")
public class Dps401C05Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps401C05Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 施設特約店別計画機能 検索系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsWsPlanForVacSearchService")
	protected DpsInsWsPlanForVacSearchService dpsInsWsPlanForVacSearchService;

	/**
	 * 施設特約店別計画機能 更新系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsWsPlanForVacService")
	protected DpsInsWsPlanForVacService dpsInsWsPlanForVacService;

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
	@RequestType(businessType = VACCINE)
	@Permission( authType = REFER)
	public Result dps401C05F00(DpContext ctx, Dps401C05Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C05F00");
		}

		// 施設特約店〆処理後のメッセージ設定
		SysManage sysManage = DpUserInfo.getDpUserInfo().getSysManage();
		if (sysManage.getWsEndFlg()) {
			super.addMessage(ctx, new MessageKey("DPS3282E"));
		}

		// 施設検索モードをクリア
		form.setInsSelectMode(null);

		// 特約店検索モードをクリア
		form.setTmsSelectMode(null);

		// 初期化処理
		form.formInit();

		InsWsPlanForVacUpDateScDto scDto = form.convertInsWsPlanUpDateScDto();
		InsWsPlanForVacUpDateResultListDto serviceResult = dpsInsWsPlanForVacSearchService.searchInsWsPlanList(scDto);
		super.getRequestBox().put(Dps401C05Form.DPS401C05_DATA_R, serviceResult);
		return ActionResult.SUCCESS;
	}

	/**
	 * 参照品目の変更ボタンクリック時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType(businessType = VACCINE)
	@Permission( authType = REFER)
	public Result dps401C05F01(DpContext ctx, Dps401C05Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C05F00");
		}

		// 施設特約店〆処理後のメッセージ設定
		SysManage sysManage = DpUserInfo.getDpUserInfo().getSysManage();
		if (sysManage.getWsEndFlg()) {
			super.addMessage(ctx, new MessageKey("DPS3282E"));
		}

		InsWsPlanForVacUpDateScDto scDto = form.convertInsWsPlanUpDateScDto();
		InsWsPlanForVacUpDateResultListDto serviceResult = dpsInsWsPlanForVacSearchService.searchInsWsPlanList(scDto);
		super.getRequestBox().put(Dps401C05Form.DPS401C05_DATA_R, serviceResult);
		return ActionResult.SUCCESS;
	}

	/**
	 * 特約店追加時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType(businessType = VACCINE)
	@Permission( authType = REFER)
	public Result dps401C05F05(DpContext ctx, Dps401C05Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C05F05");
		}

		// サービス呼び出し
		List<InsWsPlanForVacUpDateResultDto> list = new ArrayList<InsWsPlanForVacUpDateResultDto>();
		InsWsPlanUpDateMonNnuScDto monNnuScDto = form.convertInsWsPlanUpDateMonNnuScDto();
		InsWsPlanForVacUpDateResultDto dto = dpsInsWsPlanForVacSearchService.searchJisseki(monNnuScDto);
		list.add(dto);

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C05F05:dto:" + dto);
		}

		// 結果の設定
		super.getRequestBox().put(Dps401C05Form.DPS401C05F05_DATA_R, (ArrayList<InsWsPlanForVacUpDateResultDto>) list);
		return ActionResult.SUCCESS;
	}

	/**
	 * 一括登録時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType(businessType = VACCINE)
	@Permission( authType = EDIT)
	public Result dps401C05F15Execute(DpContext ctx, Dps401C05Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps401C05F15Execute");
		}
		InsWsPlanForVacUpDateModifyAllDto upDateDto = form.convertInsWsPlanForVacUpDateModifyAllDto();
		try {
			dpsInsWsPlanForVacService.entryAllInsWsPlan(upDateDto);
			// 登録完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0006I", "施設特約店別計画"));
		} finally {
			InsWsPlanForVacUpDateScDto scDto = form.convertInsWsPlanUpDateScDto();
			InsWsPlanForVacUpDateResultListDto serviceResult = dpsInsWsPlanForVacSearchService.searchInsWsPlanList(scDto);
			super.getRequestBox().put(Dps401C05Form.DPS401C05_DATA_R, serviceResult);
		}
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
	public void dps401C05F15Validation(DpContext ctx, Dps401C05Form form) throws Exception {
		if (form.getSelectRowIdList() != null) {
			for (int i = 0; i < form.getSelectRowIdList().length; i++) {
				String[] paraArray = ConvertUtil.splitConmma(form.getSelectRowIdList()[i]);
				if (paraArray.length > 6) {
					String valueStr = paraArray[7];
					if (StringUtils.isEmpty(valueStr)) {
						valueStr = null;
					} else {
						final String errMsg = "受信パラメータ(修正計画)が不正：";
						// 登録値 数値チェック
						if (!ValidationUtil.isDouble(valueStr)) {
							throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
						}
						// 登録値 整数チェック
						if (!ValidationUtil.isLong(valueStr)) {
							throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
						}
						// 登録値 桁数チェック
						if (!ValidationUtil.isMaxLength(valueStr, 10)) {
							throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
						}
						// 登録値 最小値チェック
						Long value = ConvertUtil.parseLong(valueStr);
						if (value < 0L) {
							throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
						}
					}
				}
			}
		}
	}
}
