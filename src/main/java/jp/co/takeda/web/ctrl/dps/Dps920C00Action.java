package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.exp.ErrMessageKey.*;
import static jp.co.takeda.security.DpAuthority.AuthType.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.bean.Constants.TemplateInfo;
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.dao.DpsCodeMasterDao;
import jp.co.takeda.dto.DistPlanningListSummaryScDto;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.JokenKbn;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsReportService;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.in.dps.Dps920C00Form;

/**
 * Dps920C00((医)計画立案準備ダウンロード画面)のアクションクラス
 *
 * @author yokokawa
 */
@Controller("Dps920C00Action")
public class Dps920C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps920C00Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 帳票サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsReportService")
	DpsReportService dpsReportService;

    @Autowired(required = true)
    @Qualifier("dpsCodeMasterDao")
    protected DpsCodeMasterDao dpsCodeMasterDao;

// add Start 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
	/**
	 * カテゴリ 検索サービス
	 */
    @Autowired(required = true)
    @Qualifier("dpsCodeMasterSearchService")
    protected DpsCodeMasterSearchService dpsCodeMasterSearchService;
// add End 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能

	/**
	 * 共通DAO
	 */
	@Autowired(required = true)
	@Qualifier("commonDao")
	protected CommonDao commonDao;

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID_6DIGIT = "DPS920";

// add Start 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
	/**
	 * 計画（1：期別、2：月別）
	 */
	private static final String PLAN_ID = "1";
// add End 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能

	private String filename;
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
	public Result dps920C00F00(DpContext ctx, Dps920C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps920C00F00");
		}

		// 初期化処理
		form.formInit();

		// add Start 2024/2/26 K.Suzuki 2024年2月　チェックツール追加
		//ファイル名の設定
		List<DpsCCdMst>	checkname = dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.CHE.getDbValue());
		filename = checkname.get(0).getDataName();
		form.setFileName(filename);
		// add End 2024/2/26 K.Suzuki 2024年2月　チェックツール追加

		//ファイル種類リストの設定
		initFileTypeList(form);

		// ログインユーザの組織情報をセット
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {
				// 権限確認コード
				if (user.isMatch(JokenSet.HONBU, JokenSet.HONBU_WAKUTIN_G, JokenSet.TOKUYAKUTEN_MASTER, JokenSet.TOKUYAKUTEN_G_STAFF)) {
					// 本部レベル
					form.setSosCd1(SosMst.SOS_CD1);
				} else if (user.isMatch(JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF)) {
					// 支店レベル
					form.setSosCd2(user.getSosCd2());
				} else if (user.isMatch(JokenSet.OFFICE_MASTER, JokenSet.OFFICE_STAFF, JokenSet.WAKUTIN_AL)) {
					// 営業所レベル
					form.setSosCd3(user.getSosCd3());
				}
				// 計画支援条件セットグループ（DPS_C_JKN_GRP）の条件区分（JOKEN_KBN）が
				// 3：ワクチンのみ更新可能の場合
				if (user.getJokenKbn(SCREEN_ID_6DIGIT).equals(JokenKbn.VAC_EDIT)) {
					List<DpsCCdMst> codeList = dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.VAC.getDbValue());
		            if(codeList.size() > 1) {
		                final String errMsg = "計画支援汎用マスタにワクチンのカテゴリコードが複数登録されています。";
		                throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg));
		            }
		            String vacCtg = codeList.get(0).getDataCd();
		            //品目がワクチンのみをダウンロードする
		            form.setCategory(vacCtg);
		            form.setVacCtg(vacCtg);
				}
				// add Start 2021/7/16 H.Kaneko
				else{
					List<DpsCCdMst> codeList = dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.SII.getDbValue());
		            if(codeList.size() > 1) {
		                final String errMsg = "計画支援汎用マスタに仕入れのカテゴリコードが複数登録されています。";
		                throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg));
		            }
		            String siiCtg = codeList.get(0).getDataCd();
		            //品目が仕入れのみとそれ以外とをダウンロードする
		            form.setCategory(siiCtg);
		            form.setSiiCtg(siiCtg);
				}
				// add End 2021/7/16 H.Kaneko

			}
		}
// add Start 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
		//カテゴリリストの設定
		initCategoryList(form);
// add End 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
		return ActionResult.SUCCESS;
	}

	/**
     * ファイル種類リストの初期設定
     * @param form Dps920C00Form
     */
    private void initFileTypeList(Dps920C00Form form) {
    	List<CodeAndValue> list = new ArrayList<CodeAndValue>();

// add Start 2024/2/26 K.Suzuki 2024年2月　チェックツール追加
    	//ファイル名整形
		filename = form.getFileName();
		int index1 =  filename.indexOf("_");
		if(index1 > 0) {
		filename = filename.substring(0,index1) + "計画";
		} else {
		filename = filename + "計画";
		}
// add End 2024/2/26 K.Suzuki 2024年2月　チェックツール追加

// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
    	CodeAndValue cad = new CodeAndValue("01", "エリア計画アップロード用");
//    	CodeAndValue cad = new CodeAndValue("01", "営業所計画アップロード用");
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
    	list.add(cad);
// add Start 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
    	list.add(new CodeAndValue("02", "計画支援フォロー"));
// add End 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
// add Start 2024/2/26 K.Suzuki 2024年2月　チェックツール追加
		list.add(new CodeAndValue("03", filename));
// add End 2024/2/26 K.Suzuki 2024年2月　チェックツール追加

    	form.setFileTypeList(list);
    }

// add Start 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
    /**
     * カテゴリリストの初期設定
     * @param form Dps920C00Form
     */
    private void initCategoryList(Dps920C00Form form) {
		// 汎用マスタのカテゴリリスト
		List<DpsCCdMst> categoryList = (dpsCodeMasterSearchService.searchCategoryList(null,PLAN_ID));

		List<CodeAndValue> list = new ArrayList<CodeAndValue>();

		boolean indexFlg = true;
		// 対象の立案レベルをもつ計画対象カテゴリ領域のカテゴリのみ設定
		for (DpsCCdMst mst :categoryList) {
				CodeAndValue cad = new CodeAndValue(mst.getDataCd(), mst.getDataName());
				list.add(cad);
				if (form.getCategory() == null && indexFlg) {
					form.setCategory(cad.getCode());
					indexFlg = false;
			}
		}
		form.setProdCategoryList(list);
    }
// add End 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能

	/**
	 * 出力する処理時に呼ばれるアクションメソッド<br>
	 * 実績情報は取得しない。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(isDownloadable = true)
	@RequestType
	@Permission(authType = REFER)
	public Result dps920C00F10Output(DpContext ctx, Dps920C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps920C00F10Output");
		}
		try {
			// ダウンロードファイルに営業所情報と計画対象品目を出力するための準備
			DistPlanningListSummaryScDto distPlanningListSummaryScDto = form.convertDistPlanningListSummaryScDto();
			String templateRootPath = getRealPath(TemplateInfo.EXCEL_TEMPLATE_PATH.getValue());


			// ダウンロードファイルを作成
			String vacCtg = form.getvacCtg();
			String siiCtg = form.getSiiCtg();
			// add Start 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
			if (form.getFileType().equals("01")) {
		    // add End 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
				if (form.getCategory().equals(vacCtg)) {
					// ログインユーザがワクチンユーザーの場合、ワクチンのみのファイルを作成する
					ExportResult exportResult = dpsReportService.outputDistPlanningList(templateRootPath, distPlanningListSummaryScDto);
					form.setExportResult(exportResult);
					form.setDownLoadFileName(exportResult.getName());
				}
				else if (form.getCategory().equals(siiCtg) && StringUtils.isNotEmpty(form.getSosCd1())){
					// 本部ログインユーザの場合、仕入のみと仕入以外のファイルを作成する
					ExportResult exportResult = dpsReportService.outputDistPlanningListHonbu(templateRootPath, distPlanningListSummaryScDto);
		    		form.setExportResult(exportResult);
				    form.setDownLoadFileName(exportResult.getName());

				} else {
					// 支店・営業所ログインユーザの場合、仕入以外のファイルを作成する
					ExportResult exportResult = dpsReportService.outputDistPlanningListExceptCategory(templateRootPath, distPlanningListSummaryScDto);
					form.setExportResult(exportResult);
					form.setDownLoadFileName(exportResult.getName());
				}
			// add Start 2024/2/26 K.Suzuki 2024年2月　チェックツール追加
			}
			else if (form.getFileType().equals("03")) {
				ExportResult exportResult = dpsReportService.outputCheckTool(templateRootPath,form.getFileName());
				form.setExportResult(exportResult);
				form.setDownLoadFileName(exportResult.getName());
			// add End 2024/2/26 K.Suzuki 2024年2月　チェックツール追加
		    // add Start 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
			}else {
				// 支店・営業所ログインユーザの場合、仕入以外のファイルを作成する
				ExportResult exportResult = dpsReportService.outputDelFacilitiesAndAdjustmentsList(templateRootPath, distPlanningListSummaryScDto);
				form.setExportResult(exportResult);
				form.setDownLoadFileName(exportResult.getName());
			}
			// add End 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能

		} finally {
			// 作成したダウンロードファイルをリクエストに設定（ダウンロード実行）
			super.getRequestBox().put(Dps920C00Form.DPS920C00_DATA_EXIST, true);
		}

		return ActionResult.SUCCESS;
	}


}
