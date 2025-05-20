package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.exp.ErrMessageKey.VALIDATE_ERROR;
import static jp.co.takeda.security.BusinessType.VACCINE;
import static jp.co.takeda.security.DpAuthority.AuthType.EDIT;
import static jp.co.takeda.security.DpAuthority.AuthType.REFER;


import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.DistributionForVacExecOrgDto;
import jp.co.takeda.dto.DistributionForVacParamsDto;
import jp.co.takeda.dto.InsWsDistForVacProdResultDto;
import jp.co.takeda.dto.InsWsDistProdUpdateDto;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsDistributionForVacProdSearchService;
import jp.co.takeda.service.DpsDistributionForVacProdService;
import jp.co.takeda.task.VaccineDistributionJob;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps400C03Form;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Dps400C03((ワ)施設特約店別計画配分対象品目一覧画面)のアクションクラス
 * 
 * @author siwamoto
 */
@Controller("Dps400C03Action")
public class Dps400C03Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps400C03Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 配分機能 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsDistributionForVacProdSearchService")
	protected DpsDistributionForVacProdSearchService dpsDistributionForVacProdSearchService;

	/**
	 * 配分機能 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsDistributionForVacProdService")
	protected DpsDistributionForVacProdService dpsDistributionForVacProdService;

	/**
	 * 配分ジョブ
	 */
	@Autowired(required = true)
	@Qualifier("vaccineDistributionJob")
	protected VaccineDistributionJob vaccineDistributionJob;

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
	public Result dps400C03F00(DpContext ctx, Dps400C03Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps400C03F00");
		}

		// 初期化処理
		form.formInit();

		// 配分対象品目一覧の検索実行
		List<InsWsDistForVacProdResultDto> resultList = dpsDistributionForVacProdSearchService.searchDistributionProdList();

		// リクエストボックスに検索結果をセット
		super.getRequestBox().put(Dps400C03Form.DPS400C03_DATA_R_SEARCH_RESULT, (ArrayList<InsWsDistForVacProdResultDto>) resultList);
		return ActionResult.SUCCESS;
	}

	/**
	 * 配分を実行する処理時に呼ばれるアクションメソッド<br>
	 * 
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType(businessType = VACCINE)
	@Permission( authType = EDIT)
	public Result dps400C03F05Execute(DpContext ctx, Dps400C03Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps400C03F05Execute");
		}
		boolean isMrOpenCheck = form.getIsMrOpenCheck();
		List<InsWsDistProdUpdateDto> insWsDistProdUpdateDtoList = form.convertInsWsDistProdUpdateDto();

		try {
			// 配分前の情報処理
			List<DistributionForVacExecOrgDto> execList = dpsDistributionForVacProdSearchService.searchDistributionPreparation(insWsDistProdUpdateDtoList);

			// 配分処理実行
			DpUser dpUser = DpUserInfo.getDpUserInfo().getLoginUser();
			DistributionForVacParamsDto dto = new DistributionForVacParamsDto(ctx.getDpMetaInfo(), dpUser, isMrOpenCheck, execList);
			vaccineDistributionJob.execute(dto);
		} finally {

			// 検索実行
			List<InsWsDistForVacProdResultDto> resultList = dpsDistributionForVacProdSearchService.searchDistributionProdList();

			// リクエストボックスに検索結果をセット
			super.getRequestBox().put(Dps400C03Form.DPS400C03_DATA_R_SEARCH_RESULT, (ArrayList<InsWsDistForVacProdResultDto>) resultList);
		}
		return ActionResult.SUCCESS;
	}

	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * 「配分処理を実行」時の入力パラメータをチェックする。
	 * 
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps400C03F05Validation(DpContext ctx, Dps400C03Form form) throws ValidateException {

		// 更新パラーメータチェック
		String[] rowIdList = form.getRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		for (int i = 0; i < rowIdList.length; i++) {
			// パラーメータのサイズチェック
			String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);
			if (rowId.length != 3) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 品目固定コード
			if (StringUtils.isEmpty(rowId[0])) {
				final String errMsg = "受信パラメータ(品目固定コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 品目名称
			if (StringUtils.isEmpty(rowId[1])) {
				final String errMsg = "受信パラメータ(品目名称)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
		}
	}
}
