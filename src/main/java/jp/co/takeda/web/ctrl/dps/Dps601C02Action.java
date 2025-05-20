package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.exp.ErrMessageKey.VALIDATE_ERROR;
import static jp.co.takeda.model.div.JokenSet.IYAKU_MR;
import static jp.co.takeda.security.DpAuthority.AuthType.EDIT;
import static jp.co.takeda.security.DpAuthority.AuthType.REFER;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.InsDocPlanUpDateDto;
import jp.co.takeda.dto.InsDocPlanUpDateScDto;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsInsDocPlanSearchService;
import jp.co.takeda.service.DpsInsDocPlanService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps601C02Form;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Dps601C02((医)施設医師別計画編集画面)のアクションクラス
 *
 * @author siwamoto
 */
@Controller("Dps601C02Action")
public class Dps601C02Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps601C02Action.class);

	// -------------------------------
	// injection field
	// -------------------------------

	/**
	 * 施設医師別計画機能 検索系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsDocPlanSearchService")
	protected DpsInsDocPlanSearchService dpsInsDocPlanSearchService;

	/**
	 * 施設医師別計画機能 更新系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsDocPlanService")
	protected DpsInsDocPlanService dpsInsDocPlanService;

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
	public Result dps601C02F00(DpContext ctx, Dps601C02Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps601C02F00");
		}

		// 初期化処理
		form.formInit();

		// 検索実行
		search(ctx, form);

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
	@RequestType
	@Permission( authType = REFER)
	public Result dps601C02F01(DpContext ctx, Dps601C02Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps601C02F01");
		}

		// 検索実行
		search(ctx, form);

		return ActionResult.SUCCESS;
	}

	/**
	 * 登録する処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission( authType = EDIT)
	public Result dps601C02F10Execute(DpContext ctx, Dps601C02Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps601C02F10Execute");
		}
		
		boolean searchFlg = false;
		try {

			// 登録実行
			List<InsDocPlanUpDateDto> upDateList = form.convertInsDocPlanUpDateModifyDto();
			InsType insType = InsType.getInstance(form.getInsType()); 
			dpsInsDocPlanService.entryInsDocPlan(upDateList,insType);

			// 再検索
			Map<String, Object> dto = search(ctx, form);
			searchFlg = true;
			String choseiMsg = dpsInsDocPlanSearchService.createChoseiMsg(dto);

			// 登録完了メッセージの追加
			addMessage(ctx, new MessageKey("DPS0010I", choseiMsg));

		} finally {

			// 再検索
			if (!searchFlg) {
				search(ctx, form);
			}
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理を実行する。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return ヘッダー情報
	 * @throws Exception 例外
	 */
	private Map<String, Object> search(DpContext ctx, Dps601C02Form form) throws Exception {
		
		InsDocPlanUpDateScDto scDto = form.convertInsDocPlanUpDateScDto();
		
		// ログインユーザがMRで、検索対象が自分でない場合は副担当モード
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		DpUser settingUser = userInfo.getSettingUser();
		Integer loginJgiNo = userInfo.getSettingUserJgiNo();
		if (settingUser.isMatch(IYAKU_MR) && !loginJgiNo.equals(scDto.getJgiNo())) {
			super.getRequestBox().put(Dps601C02Form.SUB_MR_MODE, true);
		}

		Map<String, Object> resultHeader = null;
		List<Map<String, Object>> resultDetailList = null; 
		try {

			// 検索
			resultHeader = dpsInsDocPlanSearchService.searchInsDocPlanHeader(scDto);
			resultDetailList =dpsInsDocPlanSearchService.searchInsDocPlanList(scDto);
			
		} finally {

			// 結果設定
			super.getRequestBox().put(Dps601C02Form.DPS601C02_HEAD_R, (HashMap<String, Object>) resultHeader);
			super.getRequestBox().put(Dps601C02Form.DPS601C02_DATA_R, (ArrayList<Map<String, Object>>) resultDetailList);
		}

		return resultHeader;
	}

	// -------------------------------
	// validation method
	// -------------------------------

	/**
	 * 画面入力チェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps601C02F10Validation(DpContext ctx, Dps601C02Form form) throws Exception {
		if (form.getSelectRowIdList() == null) {
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, "受信パラメータが不正："));
		}
		for (int i = 0; i < form.getSelectRowIdList().length; i++) {
			String[] paraArray = ConvertUtil.splitConmma(form.getSelectRowIdList()[i]);
			if (paraArray.length > 8) {
				
				// 修正値
				String valueStr = paraArray[7];
				if (StringUtils.isNotEmpty(valueStr)) {
					final String errMsg = "受信パラメータ(修正値)が不正：";
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
				}

				// 確定値
				valueStr = paraArray[8];
				if (StringUtils.isNotEmpty(valueStr)) {
					final String errMsg = "受信パラメータ(確定値)が不正：";
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
