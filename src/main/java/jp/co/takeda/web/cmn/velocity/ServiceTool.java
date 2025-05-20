package jp.co.takeda.web.cmn.velocity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.AnnounceDto;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.ManagePlannedProd;
import jp.co.takeda.model.MasterManagePlannedProd;
import jp.co.takeda.model.OfficePlanStatus;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.YakkouSijou;
import jp.co.takeda.model.div.CalcType;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.ProdCategory;
import jp.co.takeda.model.div.RefPeriod;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.div.Term;
import jp.co.takeda.model.view.ProdSummary;
import jp.co.takeda.model.view.SosCtg;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.service.DpmCodeMasterSearchService;
import jp.co.takeda.service.DpmPlannedCtgSearchService;
import jp.co.takeda.service.DpmProdSearchService;
import jp.co.takeda.service.DpmSosCtgSearchService;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.service.DpsOfficePlanStatusSearchService;
import jp.co.takeda.service.DpsPlannedCtgSearchService;
import jp.co.takeda.service.DpsProdSearchService;
import jp.co.takeda.service.DpsYakkouSijouSearchService;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.bean.Constants;

/**
 * サービスクラスにアクセスするためのVelocityTool<br>
 *
 * @author tkawabata
 */
public class ServiceTool extends SpringRegistTool {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(ServiceTool.class);

	/**
	 * ブランク
	 */
	private static final CodeAndValue BLANK = new CodeAndValue("", "　　　　　　　　　");

	/**
	 * CodeAndValueを作成する。
	 *
	 * @param argArr(文字列の2重配列)
	 * @return CodeAndValueリスト
	 */
	public List<CodeAndValue> createCodeAndValue(ArrayList<ArrayList<String>> argArr) {
		if(argArr == null || argArr.size() == 0){
			return new ArrayList<CodeAndValue>();
		}

		List<CodeAndValue> list = new ArrayList<CodeAndValue>();
		for (ArrayList<String> arr : argArr) {
			String arg1 = "";
			String arg2 = "";
			if(arr != null && arr.size() > 1){
				arg1 = arr.get(0);
				arg2 = arr.get(1);
			}
			list.add(new CodeAndValue(arg1, arg2));
		}

		return list;
	}

	// ----------------------------------------------------------------
	// 薬効市場
	// ----------------------------------------------------------------
	/**
	 * 薬効市場リストを取得する。
	 *
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 薬効市場リスト
	 */
	public List<CodeAndValue> getYakkouSijouList(boolean hasBlank, String category) {
		DpsYakkouSijouSearchService service = getDpsYakkouSijouSearchService();
		try {

			List<YakkouSijou> searchList = service.searchList(category);
			List<CodeAndValue> yakkouSijouList = new ArrayList<CodeAndValue>(searchList.size());
			if (hasBlank) {
				yakkouSijouList.add(0, BLANK);
			}
			for (YakkouSijou yakkou : searchList) {
				yakkouSijouList.add(new CodeAndValue(yakkou.getYakkouSijouCode(), yakkou.getYakkouSijouName()));
			}
			return yakkouSijouList;
		} catch (Exception e) {

			List<CodeAndValue> yakkouSijouList = new ArrayList<CodeAndValue>(1);
			if (hasBlank) {
				yakkouSijouList.add(0, BLANK);
			}

			return yakkouSijouList;
		}
	}

	// ----------------------------------------------------------------
	// 品目リスト
	// ----------------------------------------------------------------

	/**
	 * 全カテゴリの品目リストを取得する。 (参照品目を含む、仕入品は含まない）
	 *
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 参照品目を含む品目のリスト
	 */
	public List<CodeAndValue> getAllProdList(boolean hasBlank) {
		//仕入品を除外したカテゴリーの配列を作成
		ProdCategory[] categories = ProdCategory.getCategoryListWithExclusion(ProdCategory.SHIIRE).toArray(new ProdCategory[0]);

		return getProdList(categories, hasBlank, true);
	}

	/**
	 * 指定カテゴリーの品目リストを取得する。 (参照品目を含まない）
	 *
	 * @param category カテゴリー(ProdCategory)
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 参照品目を含まない品目リスト
	 */
	public List<CodeAndValue> getProdList(ProdCategory category, boolean hasBlank) {
		return getProdList(new ProdCategory[]{category}, hasBlank, false);
	}

	/**
	 * 指定カテゴリーの品目リストを取得する。 (参照品目を含まない）
	 *
	 * @param category カテゴリー(String)
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 参照品目を含まない品目リスト
	 */
	public List<CodeAndValue> getProdList(String category, boolean hasBlank) {
		return getProdList(new ProdCategory[]{ProdCategory.getInstance(category)}, hasBlank, false);
	}

	/**
	 * 指定カテゴリーの品目リストを取得する。 (参照品目を含まない）
	 *
	 * @param category カテゴリー(String)
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 参照品目を含まない品目リスト
	 */
	public List<CodeAndValue> getDpsProdList(String category, boolean hasBlank) {
		return getDpsProdList(category, hasBlank, false);
	}

	/**
	 * 指定カテゴリーの品目リストを取得する。 (参照品目を含む）
	 *
	 * @param category カテゴリー(ProdCategory)
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 参照品目を含む品目リスト
	 */
	public List<CodeAndValue> getRefProdList(ProdCategory category, boolean hasBlank) {
		return getProdList(new ProdCategory[]{category}, hasBlank, true);
	}

	/**
	 * 指定カテゴリーの品目リストを取得する。 (参照品目を含む）
	 *
	 * @param category カテゴリー(String)
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 参照品目を含む品目リスト
	 */
	public List<CodeAndValue> getRefProdList(String category, boolean hasBlank) {
		return getProdList(new ProdCategory[]{ProdCategory.getInstance(category)}, hasBlank, true);
	}

	/**
	 * 指定カテゴリーの品目リストを取得する。 (参照品目を含む）
	 *
	 * @param category カテゴリー(String)
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 参照品目を含む品目リスト
	 */
	public List<CodeAndValue> getDpsRefProdList(String category, boolean hasBlank) {
		return getDpsProdList(category, hasBlank, true);
	}

	/**
	 * 指定カテゴリーの品目リストを取得する。
	 *
	 * @param categories カテゴリーの配列
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @param includeRef 参照品目を含む=TRUE, 参照品目を含まない=FALSE
	 * @return 品目リスト
	 */
	private List<CodeAndValue> getProdList(ProdCategory[] categories, boolean hasBlank, boolean includeRef) {

		try {
			DpsProdSearchService service = getDpsProdSearchService();
			List<ProdSummary> searchList = service.searchIyakuProdList(categories, includeRef);
			List<CodeAndValue> resultList = new ArrayList<CodeAndValue>();
			if (hasBlank) {
				resultList.add(0, BLANK);
			}
			for (ProdSummary prodSummary : searchList) {
				resultList.add(new CodeAndValue(prodSummary.getProdCode(), prodSummary.getProdName()));
			}
			return resultList;
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				String categoryStr = "";
				if(categories != null){
					for(ProdCategory category : categories){
						if(!categoryStr.isEmpty()) categoryStr += "、";
						categoryStr += category.getProdCategoryName();
					}
				}

			}
			return new ArrayList<CodeAndValue>(0);
		}
	}

// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　↑でdelしたメソッド郡を多少汎用的にした形で再定義。呼び出し元画面もそれぞれ修正。

	/**
	 * 指定カテゴリーの品目リストを取得する。
	 *
	 * @param categories カテゴリーの配列
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @param includeRef 参照品目を含む=TRUE, 参照品目を含まない=FALSE
	 * @return 品目リスト
	 */
	private List<CodeAndValue> getDpsProdList(String category, boolean hasBlank, boolean includeRef) {

		try {
			DpsProdSearchService service = getDpsProdSearchService();
			List<ProdSummary> searchList = service.searchIyakuProdList(category, includeRef);
			List<CodeAndValue> resultList = new ArrayList<CodeAndValue>();
			if (hasBlank) {
				resultList.add(0, BLANK);
			}
			for (ProdSummary prodSummary : searchList) {
				resultList.add(new CodeAndValue(prodSummary.getProdCode(), prodSummary.getProdName()));
			}
			return resultList;
		} catch (Exception e) {
			return new ArrayList<CodeAndValue>(0);
		}
	}

	/**
	 * ワクチン品の品目リストを取得する。(参照品目を含まない)
	 *
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 参照品目を含まない品目リスト
	 */
	public List<CodeAndValue> getWakutinProdList(boolean hasBlank) {
		try {
			DpsProdSearchService service = getDpsProdSearchService();
			List<ProdSummary> searchList = service.searchWakutinProdList(false);
			List<CodeAndValue> resultList = new ArrayList<CodeAndValue>();
			if (hasBlank) {
				resultList.add(0, BLANK);
			}
			for (ProdSummary prodSummary : searchList) {
				resultList.add(new CodeAndValue(prodSummary.getProdCode(), prodSummary.getProdName()));
			}
			return resultList;
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				final String errMsg = "ワクチン品の品目リストが取得出来ない(参照品目を含まない）";
				LOG.error(errMsg, e);
			}
			return new ArrayList<CodeAndValue>(0);
		}
	}

	/**
	 * ワクチン品の品目リストを取得する。(参照品目を含む)
	 *
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 参照品目のリスト
	 */
	public List<CodeAndValue> getWakutinRefProdList(boolean hasBlank) {
		try {
			DpsProdSearchService service = getDpsProdSearchService();
			List<ProdSummary> searchList = service.searchWakutinProdList(true);
			List<CodeAndValue> resultList = new ArrayList<CodeAndValue>();
			if (hasBlank) {
				resultList.add(0, BLANK);
			}
			for (ProdSummary prodSummary : searchList) {
				resultList.add(new CodeAndValue(prodSummary.getProdCode(), prodSummary.getProdName()));
			}
			return resultList;
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				final String errMsg = "ワクチン品の品目リストが取得出来ない(参照品目を含む）";
				LOG.error(errMsg, e);
			}
			return new ArrayList<CodeAndValue>(0);
		}
	}

	/**
	 * 支援の医薬品目のコードと値のモデルリストを取得する。
	 *
	 * @param prodCategory カテゴリー
	 * @param planLevel 計画レベル
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 品目リスト
	 */
	public List<PlannedProd> getShienIyakuProdModelList(String prodCategory, ProdPlanLevel planLevel, boolean hasBlank) {
		return getShienProdModelList(prodCategory, Sales.IYAKU, planLevel, hasBlank);
	}


	/**
	 * 支援のワクチン品目のコードと値のモデルリストを取得する。
	 *
	 * @param prodCategory カテゴリー
	 * @param planLevel 計画レベル
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 品目リスト
	 */
	public List<PlannedProd> getShienVaccineProdModelList(String prodCategory, ProdPlanLevel planLevel, boolean hasBlank) {
		return getShienProdModelList(prodCategory, Sales.VACCHIN, planLevel, hasBlank);
	}

	/**
	 * 支援の品目のモデルリストを取得する。
	 *
	 * @param prodCategory カテゴリー
	 * @param sales 売上所属
	 * @param planLevel 計画レベル
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 品目リスト
	 */
	public List<PlannedProd> getShienProdModelList(String prodCategory, Sales sales, ProdPlanLevel planLevel, boolean hasBlank) {
		try {
			DpsProdSearchService service = getDpsProdSearchService();
			List<PlannedProd> searchList = service.searchProdList(prodCategory, sales, planLevel);
			List<PlannedProd> resultList = new ArrayList<PlannedProd>();
			if (hasBlank) {
				resultList.add(0, new PlannedProd());
			}
			for (PlannedProd plannedProd : searchList) {
				resultList.add(plannedProd);
			}
			return resultList;
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				final String errMsg = "管理の仕入品の品目リストを取得出来ない";
				LOG.error(errMsg, e);
			}
			return new ArrayList<PlannedProd>(0);
		}
	}

	/**
	 * 参照期間リストを取得する。
	 *
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 参照期間リスト
	 */
	public List<CodeAndValue> getRefPeriodList(boolean hasBlank) {
		List<CodeAndValue> searchList = new ArrayList<CodeAndValue>();
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo == null) {
			return searchList;
		}
		SysManage sysManage = userInfo.getSysManage();
		String sysYear = sysManage.getSysYear();
		Term sysTerm = sysManage.getSysTerm();
		if (hasBlank) {
			searchList.add(0, BLANK);
		}
		List<RefPeriod> allRefPeriodList = RefPeriod.ALL_REF_PERIOD_LIST;
		for (RefPeriod refPeriod : allRefPeriodList) {
			Date refPeriodDate = RefPeriod.convertRefPeriod(refPeriod, sysYear, sysTerm);
			searchList.add(new CodeAndValue(refPeriod.getDbValue(), DateUtil.toString(refPeriodDate, "yyyy/MM")));
		}
		return searchList;
	}

	/**
	 * 管理のMMP品の品目リストを取得する。
	 *
	 * @param planLevel 計画レベル
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 品目リスト
	 */
	public List<CodeAndValue> getManageMMPProdList(ProdPlanLevel planLevel, boolean hasBlank) {
		try {
			DpmProdSearchService service = getDpmProdSearchService();
			List<ManagePlannedProd> searchList = service.searchIyakuProdList(ProdCategory.MMP, planLevel);
			List<CodeAndValue> resultList = new ArrayList<CodeAndValue>();
			if (hasBlank) {
				resultList.add(0, BLANK);
			}
			for (ManagePlannedProd plannedProd : searchList) {
				resultList.add(new CodeAndValue(plannedProd.getProdCode(), plannedProd.getProdName()));
			}
			return resultList;
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				final String errMsg = "管理のMMP品の品目リストを取得出来ない";
				LOG.error(errMsg, e);
			}
			return new ArrayList<CodeAndValue>(0);
		}
	}

	/**
	 * 管理の仕入品の品目リストを取得する。
	 *
	 * @param planLevel 計画レベル
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 品目リスト
	 */
	public List<CodeAndValue> getManageShireProdList(ProdPlanLevel planLevel, boolean hasBlank) {
		try {
			DpmProdSearchService service = getDpmProdSearchService();
			List<ManagePlannedProd> searchList = service.searchIyakuProdList(ProdCategory.SHIIRE, planLevel);
			List<CodeAndValue> resultList = new ArrayList<CodeAndValue>();
			if (hasBlank) {
				resultList.add(0, BLANK);
			}
			for (ManagePlannedProd plannedProd : searchList) {
				resultList.add(new CodeAndValue(plannedProd.getProdCode(), plannedProd.getProdName()));
			}
			return resultList;
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				final String errMsg = "管理の仕入品の品目リストを取得出来ない";
				LOG.error(errMsg, e);
			}
			return new ArrayList<CodeAndValue>(0);
		}
	}

	/**
	 * 管理のONC品の品目リストを取得する。
	 *
	 * @param planLevel 計画レベル
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 品目リスト
	 */
	public List<CodeAndValue> getManageONCProdList(ProdPlanLevel planLevel, boolean hasBlank) {
		try {
			DpmProdSearchService service = getDpmProdSearchService();
			List<ManagePlannedProd> searchList = service.searchIyakuProdList(ProdCategory.ONC, planLevel);
			List<CodeAndValue> resultList = new ArrayList<CodeAndValue>();
			if (hasBlank) {
				resultList.add(0, BLANK);
			}
			for (ManagePlannedProd plannedProd : searchList) {
				resultList.add(new CodeAndValue(plannedProd.getProdCode(), plannedProd.getProdName()));
			}
			return resultList;
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				final String errMsg = "管理のONC品の品目リストを取得出来ない";
				LOG.error(errMsg, e);
			}
			return new ArrayList<CodeAndValue>(0);
		}
	}

// add start 2018/1/1 M.Wada J17-0010_2018年4月組織変更対応
	/**
	 * 管理のSPBU品の品目リストを取得する。
	 *
	 * @param planLevel 計画レベル
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 品目リスト
	 */
	public List<CodeAndValue> getManageSPBUProdList(ProdPlanLevel planLevel, boolean hasBlank) {
		try {
			DpmProdSearchService service = getDpmProdSearchService();
			List<ManagePlannedProd> searchList = service.searchIyakuProdList(ProdCategory.SPBU, planLevel);
			List<CodeAndValue> resultList = new ArrayList<CodeAndValue>();
			if (hasBlank) {
				resultList.add(0, BLANK);
			}
			for (ManagePlannedProd plannedProd : searchList) {
				resultList.add(new CodeAndValue(plannedProd.getProdCode(), plannedProd.getProdName()));
			}
			return resultList;
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				final String errMsg = "管理のSPBU品の品目リストを取得出来ない";
				LOG.error(errMsg, e);
			}
			return new ArrayList<CodeAndValue>(0);
		}
	}

// add start 2018/1/1 M.Wada J17-0010_2018年4月組織変更対応

	/**
	 * 管理のワクチン品の品目リストを取得する。
	 *
	 * @param planLevel 計画レベル
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 品目リスト
	 */
	public List<CodeAndValue> getManageWakutinProdList(ProdPlanLevel planLevel, boolean hasBlank) {
		try {
			DpmProdSearchService service = getDpmProdSearchService();
			List<ManagePlannedProd> searchList = service.searchWakutinProdList(planLevel);
			List<CodeAndValue> resultList = new ArrayList<CodeAndValue>();
			if (hasBlank) {
				resultList.add(0, BLANK);
			}
			for (ManagePlannedProd plannedProd : searchList) {
				resultList.add(new CodeAndValue(plannedProd.getProdCode(), plannedProd.getProdName()));
			}
			return resultList;
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				final String errMsg = "管理のワクチン品の品目リストが取得出来ない";
				LOG.error(errMsg, e);
			}
			return new ArrayList<CodeAndValue>(0);
		}
	}

	/**
	 * 管理の医薬品目のコードと値のモデルリストを取得する。
	 *
	 * @param prodCategory カテゴリー
	 * @param planLevel 計画レベル
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 品目リスト
	 */
	public List<MasterManagePlannedProd> getManageIyakuProdModelList(String prodCategory, ProdPlanLevel planLevel, boolean hasBlank) {
		return getManageProdModelList(prodCategory, Sales.IYAKU, planLevel, hasBlank);
	}


// add Start 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える
	/**
	 * 管理の医薬品目のコードと値のモデルリストを取得する。
	 *
	 * @param prodCategory カテゴリー
	 * @param planLevel 計画レベル
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 品目リスト
	 */
	public List<MasterManagePlannedProd> getManageIyakuProdDistributorModelList(String prodCategory, ProdPlanLevel planLevel, boolean hasBlank) {
		return getManageProdDistributorModelList(prodCategory, Sales.IYAKU, planLevel, hasBlank);
	}
// add End 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える

	/**
	 * 管理のワクチン品目のコードと値のモデルリストを取得する。
	 *
	 * @param prodCategory カテゴリー
	 * @param planLevel 計画レベル
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 品目リスト
	 */
	public List<MasterManagePlannedProd> getManageVaccineProdModelList(String prodCategory, ProdPlanLevel planLevel, boolean hasBlank) {
		return getManageProdModelList(prodCategory, Sales.VACCHIN, planLevel, hasBlank);
	}

// add Start 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える
	/**
	 * 管理のワクチン品目のコードと値のモデルリストを取得する。
	 *
	 * @param prodCategory カテゴリー
	 * @param planLevel 計画レベル
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 品目リスト
	 */
	public List<MasterManagePlannedProd> getManageVaccineProdDistributorModelList(String prodCategory, ProdPlanLevel planLevel, boolean hasBlank) {
		return getManageProdDistributorModelList(prodCategory, Sales.VACCHIN, planLevel, hasBlank);
	}
// add End 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える

	/**
	 * 管理のJRNS用品目のモデルリストを取得する
	 *
	 * @param planLevel 計画レベル
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 品目リスト
	 */
	public List<MasterManagePlannedProd> getManageJrnsProdModelList(ProdPlanLevel planLevel, boolean hasBlank) {

		DpmCodeMasterSearchService dpmCodeMasterSearchService = getDpmCodeMasterSearchService();
		// JRNSに含まれるカテゴリリスト
		List<String> jrnsCategoryList = new ArrayList<String>();
		jrnsCategoryList.add(dpmCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.OFFICE.getDbValue()).get(0).getDataCd());
		jrnsCategoryList.add(dpmCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.NSG.getDbValue()).get(0).getDataCd());
		jrnsCategoryList.add(dpmCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.VAC.getDbValue()).get(0).getDataCd());
		jrnsCategoryList.add(dpmCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.CV.getDbValue()).get(0).getDataCd());

		try {
			DpmProdSearchService service = getDpmProdSearchService();
			List<MasterManagePlannedProd> searchList = service.searchJrnsProdList(planLevel, jrnsCategoryList);
			List<MasterManagePlannedProd> resultList = new ArrayList<MasterManagePlannedProd>();
			if (hasBlank) {
				resultList.add(0, new MasterManagePlannedProd());
			}
			for (MasterManagePlannedProd plannedProd : searchList) {
				resultList.add(plannedProd);
			}
			return resultList;
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				final String errMsg = "管理の仕入品の品目リストを取得出来ない";
				LOG.error(errMsg, e);
			}
			return new ArrayList<MasterManagePlannedProd>(0);
		}
	}

	/**
	 * 管理の品目のモデルリストを取得する。
	 *
	 * @param prodCategory カテゴリー
	 * @param sales 売上所属
	 * @param planLevel 計画レベル
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 品目リスト
	 */
	public List<MasterManagePlannedProd> getManageProdModelList(String prodCategory, Sales sales, ProdPlanLevel planLevel, boolean hasBlank) {
		try {
			DpmProdSearchService service = getDpmProdSearchService();
			List<MasterManagePlannedProd> searchList = service.searchProdList(prodCategory, sales, planLevel);
			List<MasterManagePlannedProd> resultList = new ArrayList<MasterManagePlannedProd>();
			if (hasBlank) {
				resultList.add(0, new MasterManagePlannedProd());
			}
			for (MasterManagePlannedProd plannedProd : searchList) {
				resultList.add(plannedProd);
			}
			return resultList;
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				final String errMsg = "管理の仕入品の品目リストを取得出来ない";
				LOG.error(errMsg, e);
			}
			return new ArrayList<MasterManagePlannedProd>(0);
		}
	}

// add Start 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える
	/**
	 * 管理の品目のモデルリストを取得する。
	 *
	 * @param prodCategory カテゴリー
	 * @param sales 売上所属
	 * @param planLevel 計画レベル
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 品目リスト
	 */
	public List<MasterManagePlannedProd> getManageProdDistributorModelList(String prodCategory, Sales sales, ProdPlanLevel planLevel, boolean hasBlank) {
		try {
			DpmProdSearchService service = getDpmProdSearchService();
			List<MasterManagePlannedProd> searchList = service.searchProdDistributorList(prodCategory, sales, planLevel);
			List<MasterManagePlannedProd> resultList = new ArrayList<MasterManagePlannedProd>();
			if (hasBlank) {
				resultList.add(0, new MasterManagePlannedProd());
			}
			for (MasterManagePlannedProd plannedProd : searchList) {
				resultList.add(plannedProd);
			}
			return resultList;
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				final String errMsg = "管理の仕入品の品目リストを取得出来ない";
				LOG.error(errMsg, e);
			}
			return new ArrayList<MasterManagePlannedProd>(0);
		}
	}

// add End 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える

	/**
	 * 管理の医薬品目のコードと値のモデルリストを取得する。
	 *
	 * @param prodCategory カテゴリー
	 * @param planLevel 計画レベル
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 品目リスト
	 */
	public List<CodeAndValue> getManageIyakuProdList(String prodCategory, ProdPlanLevel planLevel, boolean hasBlank) {
		return getManageProdList(prodCategory, Sales.IYAKU, planLevel, hasBlank);
	}

// add Start 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える
	/**
	 * 管理の医薬品目のコードと値のモデルリストを取得する。
	 *
	 * @param prodCategory カテゴリー
	 * @param planLevel 計画レベル
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 品目リスト
	 */
	public List<CodeAndValue> getManageIyakuProdDistributorList(String prodCategory, ProdPlanLevel planLevel, boolean hasBlank) {
		return getManageProdDistributorList(prodCategory, Sales.IYAKU, planLevel, hasBlank);
	}

// add End 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える

	/**
	 * 管理のワクチン品目のコードと値のモデルリストを取得する。
	 *
	 * @param prodCategory カテゴリー
	 * @param planLevel 計画レベル
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 品目リスト
	 */
	public List<CodeAndValue> getManageVaccineProdList(String prodCategory, ProdPlanLevel planLevel, boolean hasBlank) {
		return getManageProdList(prodCategory, Sales.VACCHIN, planLevel, hasBlank);
	}

// add Start 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える
	/**
	 * 管理のワクチン品目のコードと値のモデルリストを取得する。
	 *
	 * @param prodCategory カテゴリー
	 * @param planLevel 計画レベル
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 品目リスト
	 */
	public List<CodeAndValue> getManageVaccineProdDistributorList(String prodCategory, ProdPlanLevel planLevel, boolean hasBlank) {
		return getManageProdDistributorList(prodCategory, Sales.VACCHIN, planLevel, hasBlank);
	}

// add End 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える
	/**
	 * 管理のJRNS用品目のコードと値のリストを取得する。
	 *
	 * @param planLevel 計画レベル
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 品目リスト
	 */
	public List<CodeAndValue> getManageJrnsProdList(ProdPlanLevel planLevel, boolean hasBlank) {

		DpmCodeMasterSearchService dpmCodeMasterSearchService = getDpmCodeMasterSearchService();
		// JRNSに含まれるカテゴリリスト
		List<String> jrnsCategoryList = new ArrayList<String>();
		jrnsCategoryList.add(dpmCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.OFFICE.getDbValue()).get(0).getDataCd());
		jrnsCategoryList.add(dpmCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.NSG.getDbValue()).get(0).getDataCd());
		jrnsCategoryList.add(dpmCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.VAC.getDbValue()).get(0).getDataCd());
		jrnsCategoryList.add(dpmCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.CV.getDbValue()).get(0).getDataCd());

		try {
			DpmProdSearchService service = getDpmProdSearchService();
			List<MasterManagePlannedProd> searchList = service.searchJrnsProdList(planLevel, jrnsCategoryList);
			List<CodeAndValue> resultList = new ArrayList<CodeAndValue>();
			if (hasBlank) {
				resultList.add(0, BLANK);
			}
			for (MasterManagePlannedProd plannedProd : searchList) {
				resultList.add(new CodeAndValue(plannedProd.getProdCode(), plannedProd.getProdName()));
			}
			return resultList;
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				final String errMsg = "管理の仕入品の品目リストを取得出来ない";
				LOG.error(errMsg, e);
			}
			return new ArrayList<CodeAndValue>(0);
		}
	}


	/**
	 * 管理の品目のコードと値のリストを取得する。
	 *
	 * @param prodCategory カテゴリー
	 * @param sales 売上所属
	 * @param planLevel 計画レベル
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 品目リスト
	 */
	public List<CodeAndValue> getManageProdList(String prodCategory, Sales sales, ProdPlanLevel planLevel, boolean hasBlank) {
		try {
			DpmProdSearchService service = getDpmProdSearchService();
			List<MasterManagePlannedProd> searchList = service.searchProdList(prodCategory, sales, planLevel);
			List<CodeAndValue> resultList = new ArrayList<CodeAndValue>();
			if (hasBlank) {
				resultList.add(0, BLANK);
			}
			for (MasterManagePlannedProd plannedProd : searchList) {
				resultList.add(new CodeAndValue(plannedProd.getProdCode(), plannedProd.getProdName()));
			}
			return resultList;
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				final String errMsg = "管理の仕入品の品目リストを取得出来ない";
				LOG.error(errMsg, e);
			}
			return new ArrayList<CodeAndValue>(0);
		}
	}

// add Start 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える
	public List<CodeAndValue> getManageProdDistributorList(String prodCategory, Sales sales, ProdPlanLevel planLevel, boolean hasBlank) {
		try {
			DpmProdSearchService service = getDpmProdSearchService();
			List<MasterManagePlannedProd> searchList = service.searchProdDistributorList(prodCategory, sales, planLevel);
			List<CodeAndValue> resultList = new ArrayList<CodeAndValue>();
			if (hasBlank) {
				resultList.add(0, BLANK);
			}
			for (MasterManagePlannedProd plannedProd : searchList) {
				resultList.add(new CodeAndValue(plannedProd.getProdCode(), plannedProd.getProdName()));
			}
			return resultList;
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				final String errMsg = "管理の仕入品の品目リストを取得出来ない";
				LOG.error(errMsg, e);
			}
			return new ArrayList<CodeAndValue>(0);
		}
	}
// add End 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える


	// ----------------------------------------------------------------
	// カテゴリ
	// ----------------------------------------------------------------
	/**
	 * JPBU(STARS)（旧：MMP品）
	 */
// add start 2018/6/5 M.Wada J18-0002_【DPX】2018年4月組織変更対応（計画支援）
// add start 2018/1/1 M.Wada J17-0010_2018年4月組織変更対応
//	private static final CodeAndValue CATEGORY_JPBU_STARS = new CodeAndValue(ProdCategory.MMP.getDbValue(), "JPBU(STARS)");
//	private static final CodeAndValue CATEGORY_JPBU_STARS = new CodeAndValue(ProdCategory.MMP.getDbValue(), "GMBU(STARS)");
// add end 2018/1/1 M.Wada J17-0010_2018年4月組織変更対応

	/**
	 * JPBU
	 */
// add start 2018/1/1 M.Wada J17-0010_2018年4月組織変更対応
//	private static final CodeAndValue CATEGORY_JPBU = new CodeAndValue(ProdCategory.MMP.getDbValue(), "JPBU品");
//	private static final CodeAndValue CATEGORY_JPBU = new CodeAndValue(ProdCategory.MMP.getDbValue(), "GMBU品");
// add end 2018/1/1 M.Wada J17-0010_2018年4月組織変更対応

	/**
	 * 仕入品
	 */
//	private static final CodeAndValue CATEGORY_SHIIRE = new CodeAndValue(ProdCategory.SHIIRE.getDbValue(), "仕入品(一般・麻薬)");

	/**
	 * ONC
	 */
//	private static final CodeAndValue CATEGORY_ONC = new CodeAndValue(ProdCategory.ONC.getDbValue(), "ONC品");

// add start 2018/1/1 M.Wada J17-0010_2018年4月組織変更対応
	/**
	 * SPBU
	 */
//	private static final CodeAndValue CATEGORY_SPBU = new CodeAndValue(ProdCategory.SPBU.getDbValue(), "SPBU品");
// add end 2018/1/1 M.Wada J17-0010_2018年4月組織変更対応

	/**
	 * MMP品計
	 */
//	private static final CodeAndValue CATEGORY_MMP_TOTAL = new CodeAndValue(ProdCategory.MMP.getDbValue(), "JPBU(STARS)計");

	/**
	 * 仕入品計
	 */
//	private static final CodeAndValue CATEGORY_SHIIRE_TOTAL = new CodeAndValue(ProdCategory.SHIIRE.getDbValue(), "仕入品(一般・麻薬)計");

	/**
	 * ONC計
	 */
//	private static final CodeAndValue CATEGORY_ONC_TOTAL = new CodeAndValue(ProdCategory.ONC.getDbValue(), "ONC品目計");
// add end 2018/6/5 M.Wada J18-0002_【DPX】2018年4月組織変更対応（計画支援）

	/**
	 * 全て
	 */
	private static final CodeAndValue CATEGORY_ALL = new CodeAndValue("9", "全て");

	/**
	 * カテゴリリストを取得する。
	 *
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @param hasAll 「全て」あり=TRUE, 「全て」なし=FALSE
	 * @return 薬効市場リスト
	 */
	public List<CodeAndValue> getCategoryList(boolean hasBlank, boolean hasAll) {
		List<CodeAndValue> categoryList = new ArrayList<CodeAndValue>();
		if (hasBlank) {
			categoryList.add(BLANK);
		}

// add start 2018/6/5 M.Wada J18-0002_【DPX】2018年4月組織変更対応（計画支援）
//		categoryList.add(CATEGORY_JPBU_STARS);
//		categoryList.add(CATEGORY_SHIIRE);
//		categoryList.add(CATEGORY_ONC);
		for (ProdCategory prodcategory : ProdCategory.getCategoryList()) {
			categoryList.add(new CodeAndValue(prodcategory.getDbValue(), prodcategory.getProdCategoryName()));
		}
// add end 2018/6/5 M.Wada J18-0002_【DPX】2018年4月組織変更対応（計画支援）

		if (hasAll) {
			categoryList.add(CATEGORY_ALL);
		}
		return categoryList;
	}

	/**
	 * カテゴリ（集計）リストを取得する。
	 *
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @param hasAll 「全て」あり=TRUE, 「全て」なし=FALSE
	 * @return 薬効市場リスト
	 */
	public List<CodeAndValue> getCategoryTotalList(boolean hasBlank, boolean hasAll) {
		List<CodeAndValue> categoryList = new ArrayList<CodeAndValue>();
		if (hasBlank) {
			categoryList.add(BLANK);
		}

// add start 2018/6/5 M.Wada J18-0002_【DPX】2018年4月組織変更対応（計画支援）
//		categoryList.add(CATEGORY_MMP_TOTAL);
//		categoryList.add(CATEGORY_SHIIRE_TOTAL);
//		categoryList.add(CATEGORY_ONC_TOTAL);
		for (ProdCategory prodcategory : ProdCategory.getCategoryList()) {
			String cagetoryName = prodcategory.getProdCategoryName();
			if(cagetoryName.endsWith("品")) {
				cagetoryName += "目計";
			} else {
				cagetoryName += "計";
			}

			categoryList.add(new CodeAndValue(prodcategory.getDbValue(), cagetoryName));
		}
// add end 2018/6/5 M.Wada J18-0002_【DPX】2018年4月組織変更対応（計画支援）

		if (hasAll) {
			categoryList.add(CATEGORY_ALL);
		}
		return categoryList;
	}

	/**
	 * カテゴリリストを取得する。
	 *
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @param hasAll 「全て」あり=TRUE, 「全て」なし=FALSE
	 * @return 薬効市場リスト
	 */
	public List<CodeAndValue> getManageCategoryList(boolean hasBlank, boolean hasAll) {
		List<CodeAndValue> categoryList = new ArrayList<CodeAndValue>();
		if (hasBlank) {
			categoryList.add(BLANK);
		}

// add strat 2018/6/5 M.Wada J18-0002_【DPX】2018年4月組織変更対応（計画支援）
//		categoryList.add(CATEGORY_JPBU);
// add start 2018/1/1 M.Wada J17-0010_2018年4月組織変更対応(ONC品と仕入品の順番も変更)
//		categoryList.add(CATEGORY_SPBU);
//		categoryList.add(CATEGORY_ONC);
//		categoryList.add(CATEGORY_SHIIRE);
// add start 2018/1/1 M.Wada J17-0010_2018年4月組織変更対応
		for (ProdCategory prodcategory : ProdCategory.getCategoryList()) {
			categoryList.add(new CodeAndValue(prodcategory.getDbValue(), prodcategory.getProdCategoryNoStarsName()));
		}
// add end 2018/6/5 M.Wada J18-0002_【DPX】2018年4月組織変更対応（計画支援）

		if (hasAll) {
			categoryList.add(CATEGORY_ALL);
		}
		return categoryList;
	}

	/**
	 * カテゴリリストを取得する。
	 *
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @param hasAll 「全て」あり=TRUE, 「全て」なし=FALSE
	 * @return 薬効市場リスト
	 */
	@Deprecated
	public List<CodeAndValue> getCategoryList(boolean hasBlank, boolean hasAll,boolean includeSeikei) {
		List<CodeAndValue> categoryList = new ArrayList<CodeAndValue>(3);
		if (hasBlank) {
			categoryList.add(BLANK);
		}

// add start 2018/6/5 M.Wada J18-0002_【DPX】2018年4月組織変更対応（計画支援）
//		categoryList.add(CATEGORY_JPBU_STARS);
//		categoryList.add(CATEGORY_SHIIRE);
//		categoryList.add(CATEGORY_ONC);
		for (ProdCategory prodcategory : ProdCategory.getCategoryList()) {
			categoryList.add(new CodeAndValue(prodcategory.getDbValue(), prodcategory.getProdCategoryName()));
		}
// add end 2018/6/5 M.Wada J18-0002_【DPX】2018年4月組織変更対応（計画支援）
		if (includeSeikei) {
//			categoryList.add(CATEGORY_SEIKEI);
		}
		if (hasAll) {
			categoryList.add(CATEGORY_ALL);
		}
		return categoryList;
	}

	/**
	 * カテゴリリストを取得する。
	 *
	 * @param dataValue
	 * @return カテゴリリスト
	 */
	@Deprecated
	public List<CodeAndValue> getCategoryList2(String dataValue) {
		List<CodeAndValue> categoryList = new ArrayList<CodeAndValue>(3);

		DpsCodeMasterSearchService service = getDpsCodeMasterSearchService();
		// 汎用マスタのカテゴリリスト
		List<DpsCCdMst> dbCategoryList = (service.searchCategoryList(null,dataValue));

		for (DpsCCdMst mst :dbCategoryList) {
			categoryList.add(new CodeAndValue(mst.getDataCd(), mst.getDataName()));
		}

		return categoryList;
	}

// add start 2018/6/5 M.Wada J18-0002_【DPX】2018年4月組織変更対応（計画支援）
	/**
	 * カテゴリリストを取得する。
	 *
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @param hasAll 「全て」あり=TRUE, 「全て」なし=FALSE
	 * @return カテゴリリスト
	 */
	public List<CodeAndValue> getCategoryNoStarsList(boolean hasBlank, boolean hasAll) {
		List<CodeAndValue> categoryList = new ArrayList<CodeAndValue>();
		if (hasBlank) {
			categoryList.add(BLANK);
		}

		for (ProdCategory prodcategory : ProdCategory.getCategoryList()) {
			categoryList.add(new CodeAndValue(prodcategory.getDbValue(), prodcategory.getProdCategoryNoStarsName()));
		}

		if (hasAll) {
			categoryList.add(CATEGORY_ALL);
		}
		return categoryList;
	}

	/**
	 * カテゴリリストを取得する。(MMPと仕入れのみ)
	 *
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @param hasAll 「全て」あり=TRUE, 「全て」なし=FALSE
	 * @return カテゴリリスト
	 */
	public List<CodeAndValue> getCategoryMmpShiireList(boolean hasBlank, boolean hasAll) {
		List<CodeAndValue> categoryList = new ArrayList<CodeAndValue>();
		if (hasBlank) {
			categoryList.add(BLANK);
		}

		categoryList.add(new CodeAndValue(ProdCategory.MMP.getDbValue(), ProdCategory.MMP.getProdCategoryName()));
		categoryList.add(new CodeAndValue(ProdCategory.SHIIRE.getDbValue(), ProdCategory.SHIIRE.getProdCategoryName()));

		if (hasAll) {
			categoryList.add(CATEGORY_ALL);
		}
		return categoryList;
	}

	/**
	 * 計画管理用カテゴリリストをDBから取得する。
	 *
	 * @param sosCategory 組織カテゴリ
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return カテゴリリスト
	 */
	public List<CodeAndValue> getDpmCategoryList(String sosCategory, boolean hasBlank) {
		List<CodeAndValue> categoryList = new ArrayList<CodeAndValue>();
		if (hasBlank) {
			categoryList.add(BLANK);
		}

		DpmCodeMasterSearchService service = getDpmCodeMasterSearchService();
		List<DpmCCdMst> mstList = service.searchCategory(sosCategory);
		for (DpmCCdMst mst : mstList) {
			categoryList.add(new CodeAndValue(mst.getDataCd(), mst.getDataName()));
		}

		return categoryList;
	}

	/**
	 * 計画管理用カテゴリリストをDBから取得する。
	 *
	 * @param sosCategory 組織カテゴリ
	 * @param plan 期別計画か月別計画か
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return カテゴリリスト
	 */
	public List<CodeAndValue> getDpmCategoryPlanList(String sosCategory, String plan, boolean hasBlank) {
		List<CodeAndValue> categoryList = new ArrayList<CodeAndValue>();
		if (hasBlank) {
			categoryList.add(BLANK);
		}

		DpmCodeMasterSearchService service = getDpmCodeMasterSearchService();
		List<DpmCCdMst> mstList = service.searchCategorylist(sosCategory, plan);
		for (DpmCCdMst mst : mstList) {
			categoryList.add(new CodeAndValue(mst.getDataCd(), mst.getDataName()));
		}

		return categoryList;
	}

	/**
	 * 計画管理用カテゴリリストをDBから取得する。
	 *
	 * @param sosCategory 組織カテゴリ
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @param dataValue 値
	 * @return カテゴリリスト
	 */
	public List<CodeAndValue> getDpmCategory(String sosCategory, boolean hasBlank, String dataValue) {
		List<CodeAndValue> categoryList = new ArrayList<CodeAndValue>();
		if (hasBlank) {
			categoryList.add(BLANK);
		}

		DpmCodeMasterSearchService service = getDpmCodeMasterSearchService();
		List<DpmCCdMst> mstList = service.searchCategorylist(sosCategory,dataValue);
		for (DpmCCdMst mst : mstList) {
			categoryList.add(new CodeAndValue(mst.getDataCd(), mst.getDataName()));
		}

		return categoryList;
	}

	/**
	 * 計画支援用カテゴリリストをDBから取得する。
	 *
	 * @param sosCategory 組織カテゴリ
	 * @param plan 期別計画か月別計画か
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return カテゴリリスト
	 */
	public List<CodeAndValue> getDpsCategoryPlanList(String sosCategory, String plan, boolean hasBlank) {
		List<CodeAndValue> categoryList = new ArrayList<CodeAndValue>();
		if (hasBlank) {
			categoryList.add(BLANK);
		}

		DpsCodeMasterSearchService service = getDpsCodeMasterSearchService();
		List<DpsCCdMst> mstList = service.searchCategoryList(sosCategory, plan);
		for (DpsCCdMst mst : mstList) {
			categoryList.add(new CodeAndValue(mst.getDataCd(), mst.getDataName()));
		}

		return categoryList;
	}

	/**
	 * 組織コードと計画立案レベルを条件に、計画管理汎用マスタから支援用カテゴリリストを取得する。
	 *
	 * @param sosCd 組織コード
	 * @param planLevel 計画立案レベル
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return カテゴリリスト
	 */
	public List<CodeAndValue> getShienCategoryList(String sosCd, ProdPlanLevel planLevel, boolean hasBlank) {
		List<CodeAndValue> categoryList = new ArrayList<CodeAndValue>();
		if (hasBlank) {
			categoryList.add(BLANK);
		}

		DpsCodeMasterSearchService service = getDpsCodeMasterSearchService();
		List<DpsCCdMst> mstList = service.searchShienCategoryList(sosCd, planLevel);
		for (DpsCCdMst mst : mstList) {
			categoryList.add(new CodeAndValue(mst.getDataCd(), mst.getDataName()));
		}
		return categoryList;
	}

// add Start 2022/6/22 R.takamoto  2-1:②2022年4月組織変更に合わせたカテゴリにする
	/**
	 * 汎用コードマスタよりカテゴリの名称を取得する。
	 *
	 * @param CategoryCd カテゴリコード
	 * @return カテゴリ名称
	 */	public String getCategoryName(String CategoryCd) {
		DpsCodeMasterSearchService service = getDpsCodeMasterSearchService();
		return service.searchDataName(CodeMaster.CAT.getDbValue(), CategoryCd);
	}
// add End 2022/6/22 R.takamoto  2-1:②2022年4月組織変更に合わせたカテゴリにする

	// ----------------------------------------------------------------
	// お知らせ
	// ----------------------------------------------------------------

	/**
	 * お知らせ情報の取得/設定処理を行う。
	 *
	 * @return お知らせ情報
	 */
	public List<AnnounceDto> getAnnounce() {
		DpUserInfo dpUserInfo = DpUserInfo.getDpUserInfo();
		if (dpUserInfo == null) {
			return null;
		}
		Integer jgiNo = dpUserInfo.getSettingUserJgiNo();
		if (jgiNo == null) {
			return null;
		}
		List<AnnounceDto> announceDtoList = null;
		try {
			announceDtoList = getDpsContactOperationsSearchService().searchAnnounceList(jgiNo);
		} catch (DataNotFoundException e) {
			if (LOG.isDebugEnabled()) {
				final String debugMsg = "お知らせ情報なし";
				LOG.debug(debugMsg);
			}
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				final String errMsg = "お知らせ情報取得時に致命的例外が発生";
				LOG.error(errMsg, e);
			}
		}
		return announceDtoList;
	}

// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　vmでStringのカテゴリーコードからカテゴリー名称を取得するためのメソッド追加
	public String getProdCategoryName(String category){
		return ProdCategory.getInstance(category).getProdCategoryName();
	}
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　vmでStringのカテゴリーコードからカテゴリー名称を取得するためのメソッド追加



	/**
	 * 施設タイプ別タイトルをDBから取得
	 * @param prodCategory 品目カテゴリ
	 * @return
	 */
	public List<CodeAndValue> getInsTypeTitleList(String prodCategory) {
		List<CodeAndValue> insTypeTitleList = new ArrayList<CodeAndValue>();

		DpmCodeMasterSearchService service = getDpmCodeMasterSearchService();
		List<DpmCCdMst> mstList = service.searchInsTypeTitle(prodCategory);
		for (DpmCCdMst mst : mstList) {
			insTypeTitleList.add(new CodeAndValue(mst.getDataCd(), mst.getDataName()));
		}

		return insTypeTitleList;

	}

	/**
	 * ワクチンのデータコードを取得
	 *
	 * @return ワクチンデータコード
	 */
	public String getVaccineCode() {
		DpmCodeMasterSearchService service = getDpmCodeMasterSearchService();

		return service.searchCodeByDataKbn(CodeMaster.VAC.getDbValue()).get(0).getDataCd();
	}

	/**
	 * JRNSのデータコードを取得
	 *
	 * @return JRNSデータコード
	 */
	public String getJrnsCode() {
		DpmCodeMasterSearchService service = getDpmCodeMasterSearchService();

		return service.searchCodeByDataKbn(CodeMaster.JRNS.getDbValue()).get(0).getDataCd();
	}

	/**
	 * ワクチンであるか判断
	 * @param prodCategory 品目カテゴリ
	 * @return ワクチンであればtrue
	 */
	public boolean isVaccine(String prodCategory) {
		DpmCodeMasterSearchService service = getDpmCodeMasterSearchService();

		return service.isVaccine(prodCategory);
	}

	/**
	 * 組織のカテゴリコードリストを取得
	 * @param sosCd 組織コード
	 * @return カテゴリコードリスト
	 */
	public List<SosCtg> getSosCtgList(String sosCd, String screenID) {
		DpmSosCtgSearchService service = getDpmSosCtgSearchService();

		return service.searchDpmSosCtgList(sosCd, screenID);
	}

	/**
	 * 対象区分の選択リストを取得
	 * @param prodCategory カテゴリ
	 * @return 対象区分の選択リスト
	 */
	public List<CodeAndValue> getSelectInsKbnList(String prodCategory) {
		DpmCodeMasterSearchService service = getDpmCodeMasterSearchService();

		return service.searchTgtInsKb(prodCategory);
	}


	/**
	 * 品目のカテゴリコードを取得
	 * @param prodCode 品目コード
	 * @return 対象区分の選択リスト
	 */
	public String getCategoryOfProdCode(String prodCode) {
		DpmProdSearchService service = getDpmProdSearchService();

		return service.searchPlannedProdByProdCode(prodCode).getCategory();
	}

	/**
	 * 対象の計画立案レベルをもつ、カテゴリ一覧を取得
	 * @param planLevel 計画立案レベル
	 * @return カテゴリ一覧
	 */
	public List<String> getCategoryByPlanLevel(ProdPlanLevel planLevel) {
		DpmPlannedCtgSearchService service = getDpmPlannedCtgSearchService();

		return service.searchCategoryByPlanLevel(planLevel);
	}

	/**
	 * ワクチンのデータコードを取得
	 *
	 * @return ワクチンデータコード
	 */
	public String getShienVaccineCode() {
		DpsCodeMasterSearchService service = getDpsCodeMasterSearchService();

		return service.searchCodeByDataKbn(DpsCodeMaster.VAC.getDbValue()).get(0).getDataCd();
	}

	/**
	 * 仕入のデータコードを取得
	 *
	 * @return 仕入データコード
	 */
	public String getSiireCode() {
		DpsCodeMasterSearchService service = getDpsCodeMasterSearchService();

		return service.searchCodeByDataKbn(DpsCodeMaster.SII.getDbValue()).get(0).getDataCd();
	}

//	/**
//	 * ワクチンであるか判断
//	 * @param prodCategory 品目カテゴリ
//	 * @return ワクチンであればtrue
//	 */
//	public boolean isVaccine(String prodCategory) {
//		DpsCodeMasterSearchService service = getDpsCodeMasterSearchService();
//
//		return service.isVaccine(prodCategory);
//	}
//


	/**
	 * 支援の医薬品目のコードと値のモデルリストを取得する。
	 *
	 * @param prodCategory カテゴリー
	 * @param planLevel 計画レベル
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 品目リスト
	 */
	public List<CodeAndValue> getSupportIyakuProdList(String prodCategory, ProdPlanLevel planLevel, boolean hasBlank) {
		return getSupportProdList(prodCategory, Sales.IYAKU, planLevel, hasBlank);
	}


	/**
	 * 支援のワクチン品目のコードと値のモデルリストを取得する。
	 *
	 * @param prodCategory カテゴリー
	 * @param planLevel 計画レベル
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 品目リスト
	 */
	public List<CodeAndValue> getSupportVaccineProdList(String prodCategory, ProdPlanLevel planLevel, boolean hasBlank) {
		return getSupportProdList(prodCategory, Sales.VACCHIN, planLevel, hasBlank);
	}

	/**
	 * 支援の品目のコードと値のリストを取得する。
	 *
	 * @param prodCategory カテゴリー
	 * @param sales 売上所属
	 * @param planLevel 計画レベル
	 * @param hasBlank 空行あり=TRUE, 空行なし=FALSE
	 * @return 品目リスト
	 */
	public List<CodeAndValue> getSupportProdList(String prodCategory, Sales sales, ProdPlanLevel planLevel, boolean hasBlank) {
		try {
			DpsProdSearchService service = getDpsProdSearchService();
			List<PlannedProd> searchList = service.searchProdList(prodCategory, sales, planLevel);
			List<CodeAndValue> resultList = new ArrayList<CodeAndValue>();
			if (hasBlank) {
				resultList.add(0, BLANK);
			}
			for (PlannedProd plannedProd : searchList) {
				resultList.add(new CodeAndValue(plannedProd.getProdCode(), plannedProd.getProdName()));
			}
			return resultList;
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				final String errMsg = "支援の仕入品の品目リストを取得出来ない";
				LOG.error(errMsg, e);
			}
			return new ArrayList<CodeAndValue>(0);
		}
	}

	// ----------------------------------------------------------------
	// デフォルト組織の試算タイプを取得する
	// ----------------------------------------------------------------
	public CalcType getCalcType() {
		CalcType calcType = null;
		String sosCd3 = null;
		SosMst sosMst = DpUserInfo.getDpUserInfo().getDefaultSosMst();
		switch (sosMst.getBumonRank()) {
			case OFFICE_TOKUYAKUTEN_G:
				sosCd3 = sosMst.getSosCd();
				break;
			case TEAM:
				sosCd3 = sosMst.getUpSosCd();
				break;
			default:
				break;
		}
		DpsOfficePlanStatusSearchService service = getDpsOfficePlanStatusSearchService();

		try {
			OfficePlanStatus officePlanStatus = service.searchMMP(sosCd3);
			calcType = officePlanStatus.getCalcType();
		} catch (Exception e) {
		}
		return calcType;
	}

	/**
	 * 対象の計画立案レベルをもつ、カテゴリ一覧を取得
	 * @param planLevel 計画立案レベル
	 * @return カテゴリ一覧
	 */
	public List<String> getDpsCategoryByPlanLevel(ProdPlanLevel planLevel) {
		DpsPlannedCtgSearchService service = getDpsPlannedCtgSearchService();

		return service.searchCategoryByPlanLevel(planLevel);
	}

	// add Start 2025/2/19  H.Futagami 202410要望:カテゴリに全て（医薬）追加
	/**
	 * 全て（医薬）のデータコードを取得
	 *
	 * @return 全て（医薬）データコード
	 */
	public String getCategoryIyakuAllCode() {
		return Constants.CATEGORY_IYAKU_ALL;
	}
	// add End 2025/2/19  H.Futagami 202410要望:カテゴリに全て（医薬）追加

}
