package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.exp.ErrMessageKey.VALIDATE_ERROR;

import java.util.Date;
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
import jp.co.takeda.dto.DocDistributionJgiDto;
import jp.co.takeda.dto.DocDistributionParamsDto;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.security.DpAuthority.AuthType;

import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpcUserSearchService;
import jp.co.takeda.service.DpsDocDistributionProdSearchService;
import jp.co.takeda.task.DocDistributionJob;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps600C00Form;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Dps600C00((医)施設医師別計画配分対象品目一覧画面)のアクションクラス
 * 
 * @author stakeuchi
 */
@Controller("Dps600C00Action")
public class Dps600C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps600C00Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 配分機能 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsDocDistributionProdSearchService")
	protected DpsDocDistributionProdSearchService dpsDocDistributionProdSearchService;

	/**
	 * ユーザサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpcUserSearchService")
	protected DpcUserSearchService dpcUserSearchService;

	/**
	 * 配分ジョブ
	 */
	@Autowired(required = true)
	@Qualifier("docDistributionJob")
	protected DocDistributionJob docDistributionJob;

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
	@Permission( authType = AuthType.REFER)
	public Result dps600C00F00(DpContext ctx, Dps600C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps600C00F00");
		}

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
		}

		//「配分処理と同時にMRに公開フラグ」ON
//		form.setIsMrOpenCheck(true);

		// 検索実行
		form.setTranField();
		searchInsDocDistProdList(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理時に呼ばれるアクションメソッド
	 * 
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission( authType = AuthType.REFER)
	public Result dps600C00F05(DpContext ctx, Dps600C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps600C00F05");
		}

		// 検索実行
		form.setTranField();
		searchInsDocDistProdList(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * 「配分処理を実行」時に呼ばれるアクションメソッド
	 * 
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission( authType = AuthType.EDIT)
	public Result dps600C00F10Execute(DpContext ctx, Dps600C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps600C00F10Execute");
		}

		String sosCd = form.getSosCd3Tran();
//		boolean isMrOpenCheck = form.getIsMrOpenCheck();
		Date statusLastUpdate = null;
		if(StringUtils.isNotEmpty(form.getStatusLastUpdate())){
			statusLastUpdate = new Date(new Long(form.getStatusLastUpdate()));
		}
		
		try {
			
			// 配分対象の従業員情報を取得
			List<DocDistributionJgiDto> jgiList = dpsDocDistributionProdSearchService.createDocDistributionJgiList(sosCd, null, true, true);
			 
			// 配分処理実行
			DpUser dpUser = DpUserInfo.getDpUserInfo().getLoginUser();
			DocDistributionParamsDto dto = new DocDistributionParamsDto(ctx.getDpMetaInfo(), sosCd, dpUser, statusLastUpdate, jgiList, true);
			docDistributionJob.execute(dto);

		} finally {

			// 更新対象の情報でFormを書き換える
			form.setSosCd3(form.getSosCd3Tran());

			// 更新対象の情報で組織従業員を書き換える
			dpcUserSearchService.changeDefaultSosJgi(null, null, form.getSosCd3Tran(), null);

			// 再検索
			searchInsDocDistProdList(form);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理を実行する。
	 * 
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchInsDocDistProdList(Dps600C00Form form) throws Exception {

		String sosCd = form.getSosCd3Tran();

		super.getRequestBox().put(Dps600C00Form.DPS600C00_DATA_R_SEARCH_RESULT_EXIST, Boolean.FALSE);

		// 組織コード（営業所）が設定されている場合は、検索表示
		if (!StringUtils.isEmpty(sosCd)) {

			// 配分対象品目一覧の検索実行			
			Map<String, Object> resultdata = dpsDocDistributionProdSearchService.searchDistributionProdList(sosCd);
			
			// リクエストボックスに検索結果をセット
			super.getRequestBox().put(Dps600C00Form.DPS600C00_DATA_R_SEARCH_RESULT, (HashMap<String, Object>) resultdata);
			super.getRequestBox().put(Dps600C00Form.DPS600C00_DATA_R_SEARCH_RESULT_EXIST, Boolean.TRUE);
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
	public void dps600C00F05Validation(DpContext ctx, Dps600C00Form form) throws ValidateException {
		searchValidation(ctx, form);
	}

	/**
	 * 「配分処理を実行」時の入力パラメータチェックを行う。
	 * 
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps600C00F10Validation(DpContext ctx, Dps600C00Form form) throws ValidateException {
		executeValidation(ctx, form);
	}

	/**
	 * 検索処理時の共通Validationチェックを行う。
	 * 
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	private void searchValidation(DpContext ctx, Dps600C00Form form) throws ValidateException {
		// 組織コード(営業所) 必須チェック
		if (StringUtils.isEmpty(form.getSosCd3())) {
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			throw new ValidateException(new Conveyance(new MessageKey("DPC1024E", "組織", "エリア")));
//			throw new ValidateException(new Conveyance(new MessageKey("DPC1024E", "組織", "営業所")));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
		}
	}

	/**
	 * 更新処理時の共通Validationチェックを行う。
	 * 
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	private void executeValidation(DpContext ctx, Dps600C00Form form) throws ValidateException {

		// 更新行のNullチェック
		String statusLastUpdate = form.getStatusLastUpdate();
		if (StringUtils.isNotEmpty(statusLastUpdate) && !ValidationUtil.isLong(statusLastUpdate)) {
			final String errMsg = "受信パラメータ(最終更新日)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
	}
}
