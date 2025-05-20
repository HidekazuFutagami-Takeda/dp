package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.exp.ErrMessageKey.*;
import static jp.co.takeda.a.web.bean.CorrespondType.*;

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
import jp.co.takeda.dto.EstimationResultDetailExecDto;
import jp.co.takeda.dto.EstimationResultDetailResultDto;
import jp.co.takeda.dto.EstimationResultDetailResultRowDto;
import jp.co.takeda.dto.EstimationResultDetailScDto;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.service.DpsEstimationProdSearchService;
import jp.co.takeda.service.DpsMrPlanSearchService;
import jp.co.takeda.service.DpsMrPlanService;
import jp.co.takeda.service.DpsProdSearchService;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps302C01Form;

/**
 * Dps302C01((医)試算結果詳細画面)のアクションクラス
 *
 * @author stakeuchi
 */
@Controller("Dps302C01Action")
public class Dps302C01Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps302C01Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 担当者別計画機能 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsMrPlanSearchService")
	protected DpsMrPlanSearchService dpsMrPlanSearchService;

	/**
	 * 担当者別計画機能 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsMrPlanService")
	protected DpsMrPlanService dpsMrPlanService;

	/**
	 * 試算機能 検索系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsEstimationProdSearchService")
	protected DpsEstimationProdSearchService dpsEstimationProdSearchService;

	/**
	 * カテゴリ 検索サービス
	 */
    @Autowired(required = true)
    @Qualifier("dpsCodeMasterSearchService")
    protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	/**
	 * 計画品目検索サービス実装クラス
	 */
    @Autowired(required = true)
    @Qualifier("dpsProdSearchService")
    protected DpsProdSearchService dpsProdSearchService;

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
	public Result dps302C01F00(DpContext ctx, Dps302C01Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C01F00");
			LOG.debug("SosCd3:" + form.getSosCd3());
			LOG.debug("SosCd4:" + form.getSosCd4());
			LOG.debug("ProdCode:" + form.getProdCode());
			LOG.debug("InsType" + form.getInsType());
			LOG.debug("category" + form.getCategory());
		}

		// 初期化処理
		form.formInit();

		// 検索実行
		EstimationResultDetailResultDto resultDto = searchEstimationResultDetail(form);

		// グリッドのヘッダを設定
		setGridHeader(form);

		// 入力初期値セット
		form.convertActionForm(resultDto.getInfoDto());

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
	public Result dps302C01F05(DpContext ctx, Dps302C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C01F00");
			LOG.debug("SosCd3:" + form.getSosCd3());
			LOG.debug("SosCd4:" + form.getSosCd4());
			LOG.debug("ProdCode:" + form.getProdCode());
			LOG.debug("InsType" + form.getInsType());
		}

		// 検索実行
		searchEstimationResultDetail(form);

		return ActionResult.SUCCESS;
	}

	/**
	 * 「再試算処理を実行」時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps302C01F10Execute(DpContext ctx, Dps302C01Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C01F10");
		}
		try {
			// 再試算処理の実行
			EstimationResultDetailExecDto updateDto = form.convertEstimationResultDetailUpdateDto();
			dpsMrPlanService.executeEsimationUpdateOffice(updateDto);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0005I", "試算処理"));

		} finally {

			// 検索実行
			searchEstimationResultDetail(form);
		}
		return ActionResult.SUCCESS;
	}

	/**
	 * 「本部案の指数で再計算」時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps302C01F11Execute(DpContext ctx, Dps302C01Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C01F11");
		}

		try {

			// 本部案の指数で再計算処理実行
			EstimationResultDetailExecDto updateDto = form.convertEstimationResultDetailUpdateDto();
			dpsMrPlanService.executeEsimationDeleteOffice(updateDto);

			// 更新完了メッセージの追加
			addMessage(ctx, new MessageKey("DPC0005I", "試算処理"));

		} finally {

			// 検索実行
			EstimationResultDetailResultDto resultDto = searchEstimationResultDetail(form);

			// 入力初期値セット
			form.convertActionForm(resultDto.getInfoDto());
		}
		return ActionResult.SUCCESS;
	}

	/**
	 * 「再試算処理を実行」（営業所案）処理時の事前チェックで呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(correspondType = ASYNC)
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dps302C01F12Execute(DpContext ctx, Dps302C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps302C01F12Execute");
		}

		// 実績チェック実行
		List<String> resultMessageList = dpsEstimationProdSearchService.paramUpdateCheck(form.getEstimationParamUpdateData());

		// 結果メッセージをカンマ区切りで返す
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < resultMessageList.size(); i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append(resultMessageList.get(i));
		}

		List<CodeAndValue> list = new ArrayList<CodeAndValue>();
		list.add(new CodeAndValue("messageList", sb.toString()));

		ctx.getRequest().setAttribute("resultList", list);
		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理を実行する。
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private EstimationResultDetailResultDto searchEstimationResultDetail(Dps302C01Form form) throws Exception {

		// ログインユーザがチーム階層の場合はフラグをON
		setTeamRankFlag(form);

		// 検索実行
		EstimationResultDetailScDto scDto = form.convertEstimationResultDetailScDto();
		EstimationResultDetailResultDto resultDto = dpsMrPlanSearchService.searchEstimationResultDetail(scDto);

		// 検索結果をセット
		super.getRequestBox().put(Dps302C01Form.DPS302C01_DATA_R_INFO_RESULT, resultDto.getInfoDto());
		super.getRequestBox().put(Dps302C01Form.DPS302C01_DATA_R_LIST_RESULT, (ArrayList<EstimationResultDetailResultRowDto>) resultDto.getRowDtoList());
		return resultDto;
	}

	/**
	 * ユーザの部門ランクが「チーム」かを示すフラグをリクエストに設定する。
	 *
	 * @param form ActionForm
	 */
	private void setTeamRankFlag(Dps302C01Form form) {
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			DpUser dpUser = userInfo.getSettingUser();
			if (dpUser != null) {
				final BumonRank rank = dpUser.getBumonRank();
				switch (rank) {
					case TEAM:
						super.getRequestBox().put(Dps302C01Form.DPS302C01_DATA_R_TEAM_FLAG, Boolean.TRUE);
						break;
					default:
						super.getRequestBox().put(Dps302C01Form.DPS302C01_DATA_R_TEAM_FLAG, Boolean.FALSE);
						form.setSosCd4(null); // 検索時のために組織コード(チーム)をNullに設定
						break;
				}
				// 本部ユーザの場合は営業所
				if(dpUser.isMatch(JokenSet.HONBU)){
					super.getRequestBox().put(Dps302C01Form.DPS302C01_DATA_R_TEAM_FLAG, Boolean.FALSE);
					form.setSosCd4(null);
				}
			}
		}
	}

	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * 初期表示時の共通入力チェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps302C01F00Validation(DpContext ctx, Dps302C01Form form) throws ValidateException {
		searchValidation(ctx, form);
	}

	/**
	 * 検索処理時の共通入力チェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps302C01F05Validation(DpContext ctx, Dps302C01Form form) throws ValidateException {
		searchValidation(ctx, form);
	}

	/**
	 * 検索処理時の共通入力チェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	private void searchValidation(DpContext ctx, Dps302C01Form form) throws ValidateException {
		// 品目コード 必須チェック
		if (StringUtils.isEmpty(form.getProdCode())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "品目コード")));
		}
		// 組織コード(営業所) 必須チェック
		if (StringUtils.isEmpty(form.getSosCd3())) {
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "組織コード(エリア)")));
//			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "組織コード(営業所)")));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
		}
	}

	/**
	 * 「再試算処理を実行」時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps302C01F10Validation(DpContext ctx, Dps302C01Form form) throws ValidateException {
		executeValidation(ctx, form);
// add start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		if (StringUtils.isEmpty(form.getIndexRyhRtsu()) || !ValidationUtil.isInteger(form.getIndexRyhRtsu())) {
			final String errMsg = "受信パラメータ(留保率)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
// add end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		if (StringUtils.isEmpty(form.getIndexMikakutoku()) || !ValidationUtil.isInteger(form.getIndexMikakutoku())) {
			final String errMsg = "受信パラメータ(未獲得市場試算指数)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isEmpty(form.getIndexDelivery()) || !ValidationUtil.isInteger(form.getIndexDelivery())) {
			final String errMsg = "受信パラメータ(納入実績試算指数)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isNotEmpty(form.getIndexFree1()) && !ValidationUtil.isInteger(form.getIndexFree1())) {
			final String errMsg = "受信パラメータ(フリー項目1試算指数)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isNotEmpty(form.getIndexFree2()) && !ValidationUtil.isInteger(form.getIndexFree2())) {
			final String errMsg = "受信パラメータ(フリー項目2試算指数)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isNotEmpty(form.getIndexFree3()) && !ValidationUtil.isInteger(form.getIndexFree3())) {
			final String errMsg = "受信パラメータ(フリー項目3試算指数)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
	}

	/**
	 * 「本部案の指数で再計算」時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps302C01F11Validation(DpContext ctx, Dps302C01Form form) throws ValidateException {
		executeValidation(ctx, form);
	}

	/**
	 * 更新処理時の共通入力チェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	private void executeValidation(DpContext ctx, Dps302C01Form form) throws ValidateException {
		// 非表示、非入力項目チェック
		if (StringUtils.isEmpty(form.getProdName())) {
			final String errMsg = "受信パラメータ(品目名称)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		if (StringUtils.isEmpty(form.getParamUpDateTime()) || !ValidationUtil.isLong(form.getParamUpDateTime())) {
			final String errMsg = "受信パラメータ(最終更新日時)が不正：";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
	}

	/**
	 * グリッドのヘッダを設定する
	 * @param form
	 */
	private void setGridHeader(Dps302C01Form form) {


		//品目コードからカテゴリを取得
		PlannedProd plannedProd = null;
		plannedProd = dpsProdSearchService.searchProd(form.getProdCode());
		form.setCategory(plannedProd.getCategory());

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
		//合計の追加
		list.add(new CodeAndValue("9", "合計"));

		if (list.size() != 0 && list.size() >= 2) {
			//ヘッダ（UH）
			form.setHeaderUh(list.get(0).getValue());
			//ヘッダ（P）
			form.setHeaderP(list.get(1).getValue());
	    }

		//対象区分の設定
		form.setInsTypes(list);
	}
}
