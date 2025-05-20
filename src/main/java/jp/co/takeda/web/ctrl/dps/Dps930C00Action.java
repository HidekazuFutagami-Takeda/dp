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
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.bean.Constants.TemplateInfo;
import jp.co.takeda.dao.DistPlanningListULSummaryDao;
import jp.co.takeda.dao.DpsCodeMasterDao;
import jp.co.takeda.dao.OfficePlanDao;
import jp.co.takeda.dao.OfficePlanStatusDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.model.DistPlanningListULSummary;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.OfficePlan;
import jp.co.takeda.model.OfficePlanStatus;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.JokenKbn;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.StatusForOffice;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsReportService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.cmn.bean.ExportResult;
import jp.co.takeda.web.in.dps.Dps930C00Form;

/**
 * Dps930C00((医)計画立案準備計画のアップロード画面)のアクションクラス
 *
 * @author yokokawa
 */
@Controller("Dps930C00Action")
public class Dps930C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps930C00Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 帳票サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsReportService")
	DpsReportService dpsReportService;

	/**
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * 計画対象品目情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * 営業所計画情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("officePlanDao")
	protected OfficePlanDao officePlanDao;

	/**
	 * 営業所計画ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("officePlanStatusDao")
	protected OfficePlanStatusDao officePlanStatusDao;

	/**
	 * 営業所計画アップロード用のフォーマットファイルサマリーDAO
	 */
	@Autowired(required = true)
	@Qualifier("distPlanningListULSummaryDao")
	protected DistPlanningListULSummaryDao distPlanningListULSummaryDao;

    @Autowired(required = true)
    @Qualifier("dpsCodeMasterDao")
    protected DpsCodeMasterDao dpsCodeMasterDao;

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID_6DIGIT = "DPS930";

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
	public Result dps930C00F00(DpContext ctx, Dps930C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dps930C00F00");
		}

		// 初期化処理
		form.formInit();

		// ログインユーザの組織情報をセット
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {
				List<String> sosCd3List = new ArrayList<String>();

				// 権限確認コード
				if (user.isMatch(JokenSet.HONBU, JokenSet.HONBU_WAKUTIN_G, JokenSet.TOKUYAKUTEN_MASTER, JokenSet.TOKUYAKUTEN_G_STAFF)) {
					// 本部レベル
					form.setSosCd1(SosMst.SOS_CD1);

				} else if (user.isMatch(JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF)) {
					// 支店レベル
					form.setSosCd2(user.getSosCd2());

					// 支店配下の営業所一覧取得
					try {
						List<SosMst> sosMst3List = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST, BumonRank.SITEN_TOKUYAKUTEN_BU, user.getSosCd2());
						for (SosMst sosMst : sosMst3List) {
							sosCd3List.add(sosMst.getBrCode().concat(sosMst.getDistCode()));
						}
						form.setBrDistCd3List(sosCd3List);
					} catch (DataNotFoundException e) {
						// 営業所一覧が存在しない場合
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
						final String errMsg = "エリア一覧が存在しない";
//						final String errMsg = "営業所一覧が存在しない";
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
						throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
					}

				} else if (user.isMatch(JokenSet.OFFICE_MASTER, JokenSet.OFFICE_STAFF, JokenSet.WAKUTIN_AL)) {
					// 営業所レベル
					form.setSosCd3(user.getSosCd3());
					sosCd3List.add(user.getBrCode().concat(user.getDistCode()));
					form.setBrDistCd3List(sosCd3List);
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
		            //品目がワクチンのみをアップロードする
		            form.setCategory(vacCtg);
		            form.setVacCtg(vacCtg);
				}
				else{
					List<DpsCCdMst> codeList = dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.SII.getDbValue());
		            if(codeList.size() > 1) {
		                final String errMsg = "計画支援汎用マスタに仕入のカテゴリコードが複数登録されています。";
		                throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg));
		            }
		            String siiCtg = codeList.get(0).getDataCd();
		            //品目が仕入れ以外とをアップロードする
		            form.setCategory(siiCtg);
		            form.setSiiCtg(siiCtg);
				}
			}
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * 出力する処理時に呼ばれるアクションメソッド<br>
	 * 実績情報は取得しない。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = REFER)
	public Result dps930C00F10Output(DpContext ctx, Dps930C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps930C00F10Output");
		}
		// アップロードファイルのファイルサイズが0の場合
	    if (form.getUploadFile().getFileSize() == 0) {
	    	//アップロードファイルを削除する
			form.getUploadFile().destroy();
	        //ファイルがアップロードが存在しないので、処理を終了する
	        throw new ValidateException(new Conveyance(new MessageKey("DPC1006E", "アップロードファイル")));
	    }

		ArrayList<DistPlanningListULSummary> distPlanningListULSummaryList = new ArrayList<DistPlanningListULSummary>();
		ArrayList<DistPlanningListULSummary> distPlanningListErrSummaryList = new ArrayList<DistPlanningListULSummary>();

		try {
			// アップロードファイルのデータをリストに取得する
			List<DistPlanningListULSummary> list = distPlanningListULSummaryDao.excelList(form.getUploadFile().getInputStream());
			if (list == null) {
				//アップロードファイルが不正なので、処理を終了する
				throw new ValidateException(new Conveyance(ErrMessageKey.FILE_ILLEGAL_ERROR));
			}

			for (DistPlanningListULSummary dto : list) {
				distPlanningListULSummaryList.add(dto);
			}
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1007E")), e);
		} finally {
			//アップロードファイルを削除する
			form.getUploadFile().destroy();
		}

		form.setUploadCtrlKbn(null);

		// 入力内容のエラーチェック
		List<DistPlanningListULSummary> errorList = ulConcentsInputtedErrChk(distPlanningListULSummaryList, form);

		if (errorList.size() == 0) {

			form.setUploadCtrlKbn("1");
			super.getSessionBox().put(Dps930C00Form.DPS930C00_LIST, distPlanningListULSummaryList);

		} else {

			for (DistPlanningListULSummary dto : errorList) {
				distPlanningListErrSummaryList.add(dto);
			}

			form.setUploadCtrlKbn("2");
			super.getSessionBox().put(Dps930C00Form.DPS930C00_LIST, distPlanningListErrSummaryList);
		}

		return ActionResult.SUCCESS;
	}

	/**
	 * アップロードチェック正常時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = EDIT)
	public Result dps930C00F10Execute(DpContext ctx, Dps930C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps930C00F10Execute");
		}

		List<DistPlanningListULSummary> list = super.getSessionBox().get(Dps930C00Form.DPS930C00_LIST);
		if (list != null && list.size() > 0) {
			// 入力内容にエラーがないときは更新する。
			ulDpsIOfficePlanRegUpd(list);
			// 更新完了メッセージの追加
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			addMessage(ctx, new MessageKey("DPC0004I", "エリア計画"));
//			addMessage(ctx, new MessageKey("DPC0004I", "営業所計画"));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
		}

		super.getSessionBox().remove(Dps930C00Form.DPS930C00_LIST);
		form.setUploadCtrlKbn(null);
		return ActionResult.SUCCESS;
	}

	/**
	 * アップロードチェックエラー時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(isDownloadable = true)
	@RequestType
	@Permission(authType = REFER)
	public Result dps930C00F10ErrorOutput(DpContext ctx, Dps930C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps930C00F10ErrorOutput");
		}

		// エラーがある場合は、エラーファイルをダウンロードする。
		try {
			List<DistPlanningListULSummary> list = super.getSessionBox().get(Dps930C00Form.DPS930C00_LIST);

			// エラー内容をダウンロードファイルに出力するための準備
			String templateRootPath = getRealPath(TemplateInfo.EXCEL_TEMPLATE_PATH.getValue());

			// ダウンロードファイルを作成
			ExportResult exportResult = dpsReportService.outputDistPlanningErrList(templateRootPath, list);
			form.setExportResult(exportResult);
			form.setDownLoadFileName(exportResult.getName());
		} finally {
			// 作成したダウンロードファイルをリクエストに設定（ダウンロード実行）
			super.getRequestBox().put(Dps930C00Form.DPS930C00_DATA_EXIST, true);
			super.getSessionBox().remove(Dps930C00Form.DPS930C00_LIST);
			form.setUploadCtrlKbn(null);
		}
		return ActionResult.ERROR;
	}

	/**
     * アップロードファイルの入力内容チェック
     * @param DistPlanningListULSummary
     */
    private List<DistPlanningListULSummary> ulConcentsInputtedErrChk(List<DistPlanningListULSummary> distPlanningListULSummaryList, Dps930C00Form form) {

    	//エラー内容のサマリー情報
		List<DistPlanningListULSummary> distPlanningListErrSummaryList = new ArrayList<DistPlanningListULSummary>();
		String errConts = null;

    	for (DistPlanningListULSummary distPlanningULSummary : distPlanningListULSummaryList) {
    		//エラー内容の初期化
    		errConts = "";

    		//支店・営業所コードが空白
    		if (String.valueOf(distPlanningULSummary.getBrDistCd()).length() == 0) {
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
    			errConts = errConts + "リージョン・エリアコードは必須です。";
//    			errConts = errConts + "支店・営業所コードは必須です。";
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
    		}else {
    			//支店・営業所コードが組織情報に存在しない
        		// 組織検索 →支店・営業所コードが空白だと落ちるため
    			SosMst brDistSosMst = null;
        		try {
        			brDistSosMst = sosMstDAO.search(null, distPlanningULSummary.getBrDistCd().substring(0, 3), distPlanningULSummary.getBrDistCd().substring(3));
        		} catch (DataNotFoundException e) {
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
        			errConts = errConts + "リージョン・エリアコードが存在しません。";
//        			errConts = errConts + "支店・営業所コードが存在しません。";
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
        		}
        		if (brDistSosMst != null) {
        			//支店・営業所コードがログインユーザーの所属組織配下でない
            		if (form.getSosCd2() != null || form.getSosCd3() != null) {
            			Boolean subordinateFlg = false;
            			for (String brDistCd3 : form.getBrDistCd3List()) {
        					if (distPlanningULSummary.getBrDistCd().equals(brDistCd3)) {
        						subordinateFlg = true;
        						break;
        					}
        				}
            			if (subordinateFlg == false) {
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
            				errConts = errConts + "登録権限がないリージョン・エリアコードです。";
//            				errConts = errConts + "登録権限がない支店・営業所コードです。";
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
            			}
            		}
        		}
    		}

    		//統計品名コードが空白
    		if (String.valueOf(distPlanningULSummary.getStatProdCd()).length() == 0) {
    			errConts = errConts + "統計品名コードは必須です。";
    		}else {
    			// 計画対象品目検索 →統計品目コードが空白だと落ちる
    			PlannedProd statProdPlannedProd = null;
        		try {
        			statProdPlannedProd = plannedProdDAO.searchBystatPd(distPlanningULSummary.getStatProdCd());
        		} catch (DataNotFoundException e) {
        			//統計品名コードが計画対象品目に存在しない
        			errConts = errConts + "統計品名コードが存在しません。";
        		}

        		// 統計品名コードが存在する場合
        		if (statProdPlannedProd != null) {
        			String vacCtg = form.getvacCtg();
        			if (form.getCategory().equals(vacCtg)) {
        				// ワクチンユーザーの場合
        				if(!(form.getCategory().equals(statProdPlannedProd.getCategory()))) {
        					// 品目のカテゴリがワクチンでないので、エラーにする
        					errConts = errConts + "登録権限がない統計品名コードです。";
        				}
        			}
        			else if (StringUtils.isNotEmpty(form.getSosCd1())){
        				// 本部医薬ログインユーザの場合、チェックしない
        			} else {
        				// 本部医薬ログインユーザ以外の場合
        				if(form.getCategory().equals(statProdPlannedProd.getCategory())) {
        					// 品目のカテゴリが仕入なので、エラーにする
        					errConts = errConts + "登録権限がない統計品名コードです。";
        				}
        			}
        		}
    		}

    		//計画値UHは全て入力していただく（計画なしは0を入力）
            if (StringUtils.isEmpty(distPlanningULSummary.getPlannedValue_UH_T())) {
            	//空白である
            	errConts = errConts + "計画値UHは必須です。";
            }else {
            	//計画値UHが数値でない
                if (!ValidationUtil.isDouble(distPlanningULSummary.getPlannedValue_UH_T())) {
                	errConts = errConts + "計画値UHは半角数値形式で入力してください。";
                }else {
                	//計画値UHが数値である
                	if (!ValidationUtil.isLong(distPlanningULSummary.getPlannedValue_UH_T())) {
                		errConts = errConts + "計画値UHは整数を入力してください。";
                    }else {
                    	//計画値UHが0未満
                    	if (ConvertUtil.parseLong(distPlanningULSummary.getPlannedValue_UH_T()) < 0) {
                    		errConts = errConts + "計画値UHは0以上の値を入力してください。";
                		}
                		//計画値UHが10桁を超える
                		if (String.valueOf(distPlanningULSummary.getPlannedValue_UH_T()).length() > 10) {
                			errConts = errConts + "計画値UHは10桁以下の値を入力してください。";
                		}
                    }
                }
            }

    		//計画値Pは全て入力していただく（計画なしは0を入力）
            if (StringUtils.isEmpty(distPlanningULSummary.getPlannedValue_P_T())) {
            	//空白である
            	errConts = errConts + "計画値Pは必須です。";
            }else {
            	//計画値Pが数値でない
                if (!ValidationUtil.isDouble(distPlanningULSummary.getPlannedValue_P_T())) {
                	errConts = errConts + "計画値Pは半角数値形式で入力してください。";
                }else {
                	//計画値Pが数値である
                	if (!ValidationUtil.isLong(distPlanningULSummary.getPlannedValue_P_T())) {
                		errConts = errConts + "計画値Pは整数を入力してください。";
                	}else {
                    	//計画値Pが0未満
                    	if (ConvertUtil.parseLong(distPlanningULSummary.getPlannedValue_P_T()) < 0) {
                    		errConts = errConts + "計画値Pは0以上の値を入力してください。";
                		}
                		//計画値Pが10桁を超える
                		if (String.valueOf(distPlanningULSummary.getPlannedValue_P_T()).length() > 10) {
                			errConts = errConts + "計画値Pは10桁以下の値を入力してください。";
                		}
                	}
                }
            }

            if (!StringUtils.isEmpty(errConts)) {
            	distPlanningListErrSummaryList.add(new DistPlanningListULSummary(distPlanningULSummary.getBrDistCd(), distPlanningULSummary.getBrDistNm()
    				, distPlanningULSummary.getStatProdCd(), distPlanningULSummary.getProdNm(), distPlanningULSummary.getPlannedValue_UH_T()
    				, distPlanningULSummary.getPlannedValue_P_T(), errConts));
            }

    	}

    	return distPlanningListErrSummaryList;
    }

	/**
     * 営業所別計画（DPS_I_OFFICE_PLAN）の登録更新
     * @param DistPlanningListULSummary
     */
    private void ulDpsIOfficePlanRegUpd(List<DistPlanningListULSummary> distPlanningListULSummaryList) {

    	for (DistPlanningListULSummary distPlanningULSummary : distPlanningListULSummaryList) {

    		// 組織検索
    		SosMst brDistSosMst = null;
    		try {
    			brDistSosMst = sosMstDAO.search(null, distPlanningULSummary.getBrDistCd().substring(0, 3), distPlanningULSummary.getBrDistCd().substring(3));
    		} catch (DataNotFoundException e) {
    			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, "組織コード取得に失敗"), e);
    		}

    		// 品目固定コード検索
    		PlannedProd statProdPlannedProd = null;
    		try {
    			statProdPlannedProd = plannedProdDAO.searchBystatPd(distPlanningULSummary.getStatProdCd());
    		} catch (DataNotFoundException e) {
    			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, "品目固定コード取得に失敗"), e);
    		}

    		// ----------------------
    		// 営業所計画ステータス更新
    		// ----------------------
    		try {
    			OfficePlanStatus officePlanStatus = officePlanStatusDao.search(brDistSosMst.getSosCd(), statProdPlannedProd.getCategory());
    			officePlanStatus.setStatusForOffice(StatusForOffice.COMPLETED);
    			officePlanStatusDao.update(officePlanStatus);

    		} catch (DataNotFoundException e) {
    			OfficePlanStatus officePlanStatus = new OfficePlanStatus();
    			officePlanStatus.setSosCd(brDistSosMst.getSosCd());
    			officePlanStatus.setProdCategory(statProdPlannedProd.getCategory());
    			officePlanStatus.setStatusForOffice(StatusForOffice.COMPLETED);
    			try {
					officePlanStatusDao.insert(officePlanStatus);
				} catch (DuplicateException e1) {
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
					throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, "エリア計画ステータス登録に失敗"), e1);
//					throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, "営業所計画ステータス登録に失敗"), e1);
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
				}
    		}

    		// ----------------------
    		// 営業所計画更新
    		// ----------------------
    		// 営業所別計画検索
    		OfficePlan wsPlannedProd = null;
    		try {
    			wsPlannedProd = officePlanDao.searchUk(brDistSosMst.getSosCd(), statProdPlannedProd.getProdCode());
    		} catch (DataNotFoundException e) {
    			// 登録がない場合は、新規登録
    		}

    		// 更新対象情報を作成
    		OfficePlan ulPlannedProd = new OfficePlan();
    		ulPlannedProd.setSosCd(brDistSosMst.getSosCd());
			ulPlannedProd.setProdCode(statProdPlannedProd.getProdCode());
			ulPlannedProd.setPlannedValueUhY(ConvertUtil.parseLong(distPlanningULSummary.getPlannedValue_UH_T()) * 1000);
			ulPlannedProd.setPlannedValuePY(ConvertUtil.parseLong(distPlanningULSummary.getPlannedValue_P_T()) * 1000);

			// 登録/更新
			try {
				if (wsPlannedProd == null) {
					officePlanDao.insert(ulPlannedProd);
				} else {
//					ulPlannedProd.setSeqKey(wsPlannedProd.getSeqKey());
//					officePlanDao.update(ulPlannedProd);
					officePlanDao.delete(wsPlannedProd.getSeqKey(), wsPlannedProd.getUpDate());
					officePlanDao.insert(ulPlannedProd);
				}
			} catch (DuplicateException e) {
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
					throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, "エリア計画登録に失敗"), e);
//					throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, "営業所計画登録に失敗"), e);
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			}
    	}
    }

}
