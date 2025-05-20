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
import jp.co.takeda.dto.TmsTytenPlanEditForVacInputDto;
import jp.co.takeda.dto.TmsTytenPlanEditForVacResultDto;
import jp.co.takeda.dto.TmsTytenPlanEditForVacScDto;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.KaBaseKb;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcSystemManageSearchService;
import jp.co.takeda.service.DpsTmsTytenPlanEditForVacService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps502C04Form;

/**
 * Dps502C04((ワ)特約店別計画編集画面)のアクションクラス
 *
 * @author stakeuchi
 */
@Controller("Dps502C04Action")
public class Dps502C04Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps502C04Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * ワクチン特約店別計画編集サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsTmsTytenPlanEditForVacService")
	DpsTmsTytenPlanEditForVacService dpsTmsTytenPlanEditForVacService;

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
	 * 初期表示時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = REFER)
	public Result dps502C04F00(DpContext ctx, Dps502C04Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps502C04F00");
		}

		// 初期化処理
		form.formInit();

		// 特約店別計画検索
		searchWsPlan(form);

// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		KaBaseKb kaBaseKb = KaBaseKb.getInstance(form.getKaBaseKb());
		SysManage sysManage = dpcSystemManageSearchService.searchSysManage(SysClass.DPS, SysType.VACCINE);
		if (sysManage.getTransTFlg() && KaBaseKb.Y_KA_BASE.equals(kaBaseKb)) {
			super.addErrorMessage(ctx, new MessageKey("DPS3317E"));
		}
		if (!sysManage.getTransTFlg() && KaBaseKb.S_KA_BASE.equals(kaBaseKb)) {
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
			super.addErrorMessage(ctx, new MessageKey("DPS3325E"));
		}
		form.setWsEndFlg(sysManage.getWsEndFlg());

		return ActionResult.SUCCESS;
	}

	/**
	 * 登録処理時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = EDIT)
	public Result dps502C04F05Execute(DpContext ctx, Dps502C04Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps502C04F05Execute");
		}
		// 特約店別計画更新
		updateWsPlan(ctx, form);
		return ActionResult.SUCCESS;
	}

	// -------------------------------
	// private method
	// -------------------------------
	/**
	 * 特約店別計画編集対象の検索処理を実行する。
	 *
	 * @param scDto 特約店別計画編集 検索条件
	 * @throws Exception 例外
	 */
	private void searchWsPlan(Dps502C04Form form) throws Exception {
		// 特約店別計画編集対象を検索する
		TmsTytenPlanEditForVacScDto scDto = form.convertTmsTytenPlanEditForVacScDto();
		TmsTytenPlanEditForVacResultDto resultDto = dpsTmsTytenPlanEditForVacService.searchEditWsPlan(scDto);

		// 検索結果をリクエストに設定
		super.getRequestBox().put(Dps502C04Form.DPS502C04_DATA_R_SEARCH_RESULT, resultDto);
		super.getRequestBox().put(Dps502C04Form.DPS502C04_DATA_R_SEARCH_RESULT_EXIST, Boolean.TRUE);
	}

	/**
	 * 特約店別計画編集対象の更新処理を実行する。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void updateWsPlan(DpContext ctx, Dps502C04Form form) throws Exception {
		try {
			// 特約店別計画更新
			TmsTytenPlanEditForVacInputDto inputDto = form.convertTmsTytenPlanEditForVacInputDto();
			dpsTmsTytenPlanEditForVacService.editWsPlan(inputDto);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0004I", "特約店別計画"));
		} finally {
			// 特約店別計画再検索
			searchWsPlan(form);
		}
	}

	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * 登録処理時入力チェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps502C04F05Validation(DpContext ctx, Dps502C04Form form) throws ValidateException {
		// 更新行のNullチェック
		String[] rowIdList = form.getRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		for (int i = 0; i < rowIdList.length; i++) {
			// パラーメータのサイズチェック
			String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);
			if (rowId.length != 5) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 計画値に差異がない場合は更新が行われないためチェックを行わない
			if (StringUtils.equals(rowId[3], rowId[4])) {
				continue;
			}
			// シーケンスキー
			if (StringUtils.isEmpty(rowId[0]) || !ValidationUtil.isLong(rowId[0])) {
				final String errMsg = "受信パラメータ(シーケンスキー)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 最終更新日
			if (StringUtils.isEmpty(rowId[1]) || !ValidationUtil.isLong(rowId[1])) {
				final String errMsg = "受信パラメータ(最終更新日)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 組織コード
			if (StringUtils.isEmpty(rowId[2])) {
				final String errMsg = "受信パラメータ(組織コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
		}
	}
}
