package jp.co.takeda.web.ctrl.dpm;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

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
import jp.co.takeda.bean.Constants.TemplateInfo;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.InsMstResultDto;
import jp.co.takeda.dto.ManageInsWsPlanEntryDto;
import jp.co.takeda.dto.ManageInsWsPlanProdDto;
import jp.co.takeda.dto.ManageInsWsPlanProdHeaderDto;
import jp.co.takeda.dto.ManageInsWsPlanProdScDto;
import jp.co.takeda.dto.ManagePlanUpdateResultDto;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.JknGrp;
import jp.co.takeda.model.SosMstCtg;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpmCodeMasterSearchService;
import jp.co.takeda.service.DpmInsWsPlanSearchService;
import jp.co.takeda.service.DpmInsWsPlanService;
import jp.co.takeda.service.DpmPlannedCtgSearchService;
import jp.co.takeda.service.DpmReportService;
import jp.co.takeda.service.DpmSosGrpSearchService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.in.dpm.Dpm401C00Form;

/**
 * Dpm401C00((医)施設特約店品目別計画編集画面)のアクションクラス
 *
 * @author siwamoto
 */
@Controller("Dpm401C00Action")
public class Dpm401C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dpm401C00Action.class);

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPM401C00";

	/**
	 * 計画（1：期別、2：月別）
	 */
	private static final String PLAN_ID = "1";

	// -------------------------------
	// injection field
	// -------------------------------

	/**
	 * 検索系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmInsWsPlanSearchService")
	protected DpmInsWsPlanSearchService dpmInsWsPlanSearchService;

	/**
	 * 更新系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmInsWsPlanService")
	protected DpmInsWsPlanService dpmInsWsPlanService;

	/**
     * 帳票サービス
     */
    @Autowired(required = true)
    @Qualifier("dpmReportService")
    protected DpmReportService dpmReportService;

    /**
     * カテゴリ検索サービス
     */
    @Autowired(required = true)
    @Qualifier("dpmCodeMasterSearchService")
    DpmCodeMasterSearchService dpmCodeMasterSearchService;

    /**
     * 組織グループ検索サービス
     */
    @Autowired(required = true)
    @Qualifier("dpmSosGrpSearchService")
    DpmSosGrpSearchService dpmSosGrpSearchService;

    /**
     * 計画対象カテゴリ領域検索サービス
     */
    @Autowired(required = true)
    @Qualifier("dpmPlannedCtgSearchService")
    DpmPlannedCtgSearchService dpmPlannedCtgSearchService;

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
	public Result dpm401C00F00(DpContext ctx, Dpm401C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm401C00F00");
		}

		// 特約店検索モードをクリア
		form.setTmsSelectMode(null);

		// 初期化処理
		form.formInit();

		// ログインユーザの組織情報をセット
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();

		// 組織レベルがMRだった場合のパラメータ設定
		setParamForMrLvl(form, userInfo);

		// 汎用マスタのカテゴリリスト
		List<DpmCCdMst> categoryList = (dpmCodeMasterSearchService.searchCategorylist(null,PLAN_ID));

         List<CodeAndValue> list = new ArrayList<CodeAndValue>();

        boolean indexFlg = true;

		// 計画対象カテゴリ領域の設定
		List<String> planLvCtgList = new ArrayList<String>();
		// 計画立案レベル-施設特約店
		planLvCtgList = dpmPlannedCtgSearchService.searchCategoryByPlanLevel(ProdPlanLevel.INS_WS);

        // ログインユーザの組織カテゴリコードを取得
        List<String> sosCategoryList = new ArrayList<String>();
        if (userInfo != null) {
            DpUser user = userInfo.getSettingUser();
            if (user != null) {
                if (user.getSosCategoryList() != null && !user.getSosCategoryList().isEmpty()) {
                    for (SosMstCtg cat : user.getSosCategoryList()) {
                        sosCategoryList.add(cat.getCategoryCd());
                    }
                }
                // 流通政策部であるかを確認
                checkRyutsu(form, user);
            }
        }
        // 上記で取得したコードのカテゴリのみ、カテゴリリストにセットする
        for (DpmCCdMst mst : categoryList) {
            if (sosCategoryList.contains(mst.getDataCd()) && planLvCtgList.contains(mst.getDataCd())) {
                CodeAndValue cad = new CodeAndValue(mst.getDataCd(), mst.getDataName());
                list.add(cad);
                if (indexFlg) {
                    form.setProdCategory(cad.getCode());
                    indexFlg = false;
                }
            }
        }
        form.setProdCategoryList(list);


		return ActionResult.SUCCESS;
	}

	/**
	 * 組織レベルがMRだった場合にパラメータを設定する。組織レベルがMRでなければなにもしない
	 * @param form
	 * @param userInfo
	 */
	private void setParamForMrLvl(Dpm401C00Form form, DpUserInfo userInfo) {
		//全MRフラグを設定
		form.setMrFlg(false);
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {
				// 権限確認コード
				if (user.isSosLvl(SCREEN_ID, SosLvl.MR)) {
					form.setSosCd2(user.getSosCd2());
					form.setSosCd3(user.getSosCd3());
					form.setJgiNo(String.valueOf(user.getJgiNo()));
					//全MRフラグを設定
					form.setMrFlg(true);
				}
			}
		}
	}

	/**
	 * 表示ボタン押下時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.REFER)
	public Result dpm401C00F05(DpContext ctx, Dpm401C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm401C00F05");
		}
		form.setTranField();
		searchResultList(ctx, form);
		return ActionResult.SUCCESS;
	}

	/**
	 * 登録ボタン押下時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = AuthType.EDIT)
	public Result dpm401C00F10Excecute(DpContext ctx, Dpm401C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm401C00F10Excecute");
		}
		try {
			// 更新実行
			List<ManageInsWsPlanEntryDto> entryDto = form.convertEntryDto();
			ManagePlanUpdateResultDto resultDto = new ManagePlanUpdateResultDto(0, 0, 0);
			if (!entryDto.isEmpty()) {
				resultDto = dpmInsWsPlanService.updateInsWsPlan(SCREEN_ID, entryDto);
			}
			// 更新完了メッセージの追加
			final int updateSize = resultDto.getUpdateTotalCount();
			addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(updateSize)));

		} finally {
			// 再検索実行
			searchResultList(ctx, form);
		}
		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理の実行
	 *
	 * @param form ActionForm
	 * @throws Exception 例外
	 */
	private void searchResultList(DpContext ctx, Dpm401C00Form form) throws Exception {
		// 実行した検索条件を画面に設定
		ManageInsWsPlanProdScDto scDto = form.convertScDto();
		form.setViewField();
		try {
			ManageInsWsPlanProdDto serviceResult = dpmInsWsPlanSearchService.searchList(scDto);
			super.getRequestBox().put(Dpm401C00Form.DPM401C00_DATA_R, serviceResult);
		} finally {
			ManageInsWsPlanProdHeaderDto headerDto = dpmInsWsPlanSearchService.searchHeader(scDto);
			super.getRequestBox().put(Dpm401C00Form.DPM401C00_INPUT_DATA_R, headerDto);
			form.convertHeaderDto(headerDto);

			// 削除施設の場合はメッセージを追加
			if (headerDto != null) {
				InsMstResultDto resultDto = headerDto.getInsMstResultDto();
				if (resultDto != null && resultDto.getDelFlg()) {
					addErrorMessage(ctx, new MessageKey("DPM1005E"));
				}
			}
			// MRの場合にのみ、従業員番号、MRを示すフラグの再設定をする。
			setParamForMrLvl(form, DpUserInfo.getDpUserInfo());

		}
	}
	/**
	 * 出力する処理時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(isDownloadable = true)
	@RequestType
	@Permission(authType = AuthType.OUTPUT)
	public Result dpm401C00F14Output(DpContext ctx, Dpm401C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm401C00F14Output");
		}

		// 帳票出力準備
		ManageInsWsPlanProdScDto manageInsWsPlanProdScDto = form.convertScDto();
		String templateRootPath = getRealPath(TemplateInfo.EXCEL_TEMPLATE_PATH.getValue());

		searchResultList(ctx, form);

		// 帳票出力実行
		ExportResult exportResult = dpmReportService.outputInsWsPlanProdList(templateRootPath, manageInsWsPlanProdScDto, form.isRyutsu());
		form.setExportResult(exportResult);
		form.setDownLoadFileName(exportResult.getName());

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
	public void dpm401C00F05Validation(DpContext ctx, Dpm401C00Form form) throws ValidateException {
		// [必須] 施設コード
		if (StringUtils.isEmpty(form.getInsNo())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "組織コード")));
		}
		// [必須] 特約店コード
		if (StringUtils.isEmpty(form.getTmsTytenCdPart())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "特約店コード")));
		}
		// [必須] 品目カテゴリ
		if (StringUtils.isEmpty(form.getProdCategory())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "品目カテゴリ")));
		}
		// [必須] 計画
		if (StringUtils.isEmpty(form.getPlanData())) {
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", "計画")));
		}
	}

	/**
	 * 更新時のValidationメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dpm401C00F10Validation(DpContext ctx, Dpm401C00Form form) throws ValidateException {
		// 更新パラーメータチェック
		String[] rowIdList = form.getRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}
		// パラメータチェック
		for (int i = 0; i < rowIdList.length; i++) {
			// パラーメータのサイズチェック
			String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);
			if (rowId.length != 7) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 品目固定コード
			if (StringUtils.isNotEmpty(rowId[0]) && !ValidationUtil.isLong(rowId[0])) {
				final String errMsg = "受信パラメータ(品目固定コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 施設コード
			if (StringUtils.isNotEmpty(rowId[1]) && !ValidationUtil.isLong(rowId[1])) {
				final String errMsg = "受信パラメータ(施設コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 特約店コード
			if (StringUtils.isEmpty(rowId[2])) {
				final String errMsg = "受信パラメータ(特約店コード)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// シーケンスキー
			if (StringUtils.isNotEmpty(rowId[3]) && !ValidationUtil.isLong(rowId[3])) {
				final String errMsg = "受信パラメータ(シーケンスキー)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 最終更新日時
			if (StringUtils.isNotEmpty(rowId[4]) && !ValidationUtil.isLong(rowId[4])) {
				final String errMsg = "受信パラメータ(最終更新日時)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// T価ベース 更新前
			if (StringUtils.isNotEmpty(rowId[5]) && !ValidationUtil.isLong(rowId[5])) {
				final String errMsg = "受信パラメータ(T価ベース 更新前)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// T価ベース 更新後（入力値）
			if (StringUtils.isNotEmpty(rowId[6]) && !ValidationUtil.isLong(rowId[6])) {
				final String errMsg = "受信パラメータ(T価ベース 更新後（入力値）)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
		}
	}

	/**
	 * ユーザの組織が流通政策部であるかを確認する
	 * @param form ActionForm
	 * @param user ログインユーザ
	 */
	private void checkRyutsu(Dpm401C00Form form, DpUser user) {
		form.setRyutsu(false);
        // 流通政策部であるコード一覧を取得する
        List<DpmCCdMst> ryutsuList = dpmCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.RYUTSU.getDbValue());
        List<String> ryutsuCodeList = new ArrayList<String>();

        for(DpmCCdMst ryutsu : ryutsuList) {
            ryutsuCodeList.add(ryutsu.getDataCd());
        }

        for (JknGrp grp : user.getJknGrpList()) {
            if (ryutsuCodeList.contains(grp.getJokenSetCd())) {
                form.setRyutsu(true);
                break;
            }
        }
	}
}
