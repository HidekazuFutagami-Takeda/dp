package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.exp.ErrMessageKey.*;
import static jp.co.takeda.security.DpAuthority.AuthType.*;

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
import jp.co.takeda.dto.TmsTytenPlanEditResultDto;
import jp.co.takeda.dto.TmsTytenPlanEditScDto;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.KaBaseKb;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcSystemManageSearchService;
import jp.co.takeda.service.DpsTmsTytenPlanEditService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps502C01Form;

/**
 * Dps502C01((医)特約店別計画編集画面)のアクションクラス
 *
 * @author yokokawa
 */
@Controller("Dps502C01Action")
public class Dps502C01Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps502C01Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 特約店別計画編集サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsTmsTytenPlanEditService")
	DpsTmsTytenPlanEditService dpsTmsTytenPlanEditService;

	/**
	 * 納入計画管理検索サービス実装クラス
	 */
    @Autowired(required = true)
    @Qualifier("dpcSystemManageSearchService")
    protected DpcSystemManageSearchService dpcSystemManageSearchService;

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
	public Result dps502C01F00(DpContext ctx, Dps502C01Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps502C01F00");
		}

		// 初期化処理
		form.formInit();

		// 特約店別計画検索
		searchWsPlan(form.convertTmsTytenPlanEditScDto());

		KaBaseKb kaBaseKb = KaBaseKb.getInstance(form.getKaBaseKb());
		SysManage sysManage = dpcSystemManageSearchService.searchSysManage(SysClass.DPS, SysType.IYAKU);
		if (sysManage.getTransTFlg() && KaBaseKb.Y_KA_BASE.equals(kaBaseKb)) {
			super.addErrorMessage(ctx, new MessageKey("DPS3317E"));
		}
		if (!sysManage.getTransTFlg() && KaBaseKb.S_KA_BASE.equals(kaBaseKb)) {
			super.addErrorMessage(ctx, new MessageKey("DPS3314E"));
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
	@Permission(authType = EDIT)
	public Result dps502C01F05Execute(DpContext ctx, Dps502C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps502C01F05Execute");
		}

		try {
			// 特約店別計画登録
			dpsTmsTytenPlanEditService.editWsPlan(form.convertTmsTytenPlanEditInputDto());

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0004I", "特約店別計画"));

		} finally {
			// 特約店別計画再検索
			searchWsPlan(form.convertTmsTytenPlanEditScDto());
		}

		return ActionResult.SUCCESS;
	}

	// -------------------------------
	// private method
	// -------------------------------
	/**
	 * 特約店別計画編集対象 検索
	 *
	 * @param scDto 特約店別計画編集 検索条件
	 */
	private void searchWsPlan(TmsTytenPlanEditScDto scDto) {

		// 特約店別計画編集対象を検索
		TmsTytenPlanEditResultDto resultDto = dpsTmsTytenPlanEditService.searchEditWsPlan(scDto);

		// 検索結果をリクエストに設定
		super.getRequestBox().put(Dps502C01Form.DPS502C01_DATA_R, resultDto);
	}

	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * 登録処理時入力チェック
	 *
	 * <pre>
	 * 画面入力にエラーがある場合に例外をスローします。
	 * </pre>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps502C01F05Validation(DpContext ctx, Dps502C01Form form) throws ValidateException {
		// エラーメッセージ用に価ベース区分名を取得
		String kaBaseKbName = KaBaseKb.Y_KA_BASE.getDbValue().equals(form.getKaBaseKb()) ? "Y" : "T";

		// 受信パラメータチェック
		if (form.getRowIdList() == null) {
			final String errMsg = "受信パラメータがnull";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}

		for (String rowData : form.getRowIdList()) {
			String[] data = ConvertUtil.splitConmma(rowData);

			// サイズチェック
			if (data.length != 5) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}

			// 計画値UHチェック
			String plannedValueUh = data[3];
			if (StringUtils.isNotEmpty(plannedValueUh)) {
				if (!ValidationUtil.isLong(plannedValueUh)) {
					final String errMsg = "修正計画(" + kaBaseKbName + ")UHが半角数値以外";
					throw new ValidateException(new Conveyance(new MessageKey("DPC1004E", "修正計画(" + kaBaseKbName + ")UH", "半角数値"), errMsg));
				}
				if (!ValidationUtil.isMaxLength(plannedValueUh, 10)) {
					final String errMsg = "修正計画(" + kaBaseKbName + ")UHが10桁オーバー";
					throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "修正計画(" + kaBaseKbName + ")UH", "10"), errMsg));
				}
				if (ConvertUtil.parseLong(plannedValueUh) < 0) {
					final String errMsg = "修正計画(" + kaBaseKbName + ")UHが0未満";
					throw new ValidateException(new Conveyance(new MessageKey("DPC1008E", "修正計画(" + kaBaseKbName + ")UH", "0"), errMsg));
				}
			}

			// 計画値Pチェック
			String plannedValueP = data[4];
			if (StringUtils.isNotEmpty(plannedValueP)) {
				if (!ValidationUtil.isLong(plannedValueP)) {
					final String errMsg = "修正計画(" + kaBaseKbName + ")Pが半角数値以外";
					throw new ValidateException(new Conveyance(new MessageKey("DPC1004E", "修正計画(" + kaBaseKbName + ")P", "半角数値"), errMsg));
				}
				if (!ValidationUtil.isMaxLength(plannedValueP, 10)) {
					final String errMsg = "修正計画(" + kaBaseKbName + ")Pが10桁オーバー";
					throw new ValidateException(new Conveyance(new MessageKey("DPC1027E", "修正計画(" + kaBaseKbName + ")P", "10"), errMsg));
				}
				if (ConvertUtil.parseLong(plannedValueP) < 0) {
					final String errMsg = "修正計画(" + kaBaseKbName + ")Pが0未満";
					throw new ValidateException(new Conveyance(new MessageKey("DPC1008E", "修正計画(" + kaBaseKbName + ")P", "0"), errMsg));
				}
			}
		}
	}
}
