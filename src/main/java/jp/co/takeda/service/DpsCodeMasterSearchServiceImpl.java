package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.DpsCodeMasterDao;
import jp.co.takeda.dao.DpsPlannedCtgDao;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.DpsPlannedCtg;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.JokenSet;

/**
 * 管理の組織別計画(支店)の更新に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpsCodeMasterSearchService")
public class DpsCodeMasterSearchServiceImpl implements DpsCodeMasterSearchService {

	/**
	 * 計画支援汎用マスタDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterDao")
	protected DpsCodeMasterDao dpsCodeMasterDao;

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsPlannedCtgDao")
	protected DpsPlannedCtgDao dpsPlannedCtgDao;

	/**
	 * カテゴリを検索する。
	 * @param sosCategory 組織カテゴリ
	 * @return カテゴリ一覧
	 */
	public List<DpsCCdMst> searchCategory(String sosCategory) {

		List<String> dataCds = new ArrayList<String>();
		String[] sosCategoryAry = null;
		// 組織カテゴリはカンマ区切りなので、リストにする
		if (StringUtils.isNotEmpty(sosCategory)) {
			sosCategoryAry = sosCategory.split(",");
			for(String category : sosCategoryAry) {
				dataCds.add(category);
			}
		}

		// 検索結果一覧
		List<DpsCCdMst> searchList = new ArrayList<DpsCCdMst>();
		try {
			// カテゴリの検索
			searchList = dpsCodeMasterDao.searchCategory(DpsCodeMaster.CAT.getDbValue(), dataCds);
		} catch (DataNotFoundException e) {
			// 検索結果0件の場合は何もしない
		}
		return searchList;
	}

	/**
	 * カテゴリを検索する。
	 * @param sosCategory 組織カテゴリ
	 * @return カテゴリ一覧
	 */
	public List<DpsCCdMst> searchCategoryList(String sosCategory, String dataValue) {

		List<String> dataCds = new ArrayList<String>();
		String[] sosCategoryAry = null;
		// 組織カテゴリはカンマ区切りなので、リストにする
		if (StringUtils.isNotEmpty(sosCategory)) {
			sosCategoryAry = sosCategory.split(",");
			for(String category : sosCategoryAry) {
				dataCds.add(category);
			}
		}

		// 検索結果一覧
		List<DpsCCdMst> searchList = new ArrayList<DpsCCdMst>();
		try {
			// カテゴリの検索
			searchList = dpsCodeMasterDao.searchCategoryList(DpsCodeMaster.CAT.getDbValue(), dataCds, dataValue);
		} catch (DataNotFoundException e) {
			// 検索結果0件の場合は何もしない
		}
		return searchList;
	}

	/**
	 * カテゴリを検索する。
	 * @param sosCategory 組織カテゴリ
	 * @return カテゴリ一覧
	 */
	public List<String> searchCategoryStringList(String sosCategory, String dataValue) {

		List<String> dataCds = new ArrayList<String>();
		String[] sosCategoryAry = null;
		// 組織カテゴリはカンマ区切りなので、リストにする
		if (StringUtils.isNotEmpty(sosCategory)) {
			sosCategoryAry = sosCategory.split(",");
			for(String category : sosCategoryAry) {
				dataCds.add(category);
			}
		}

		// 検索結果一覧
		List<DpsCCdMst> searchList = new ArrayList<DpsCCdMst>();
		try {
			// カテゴリの検索
			searchList = dpsCodeMasterDao.searchCategoryList(DpsCodeMaster.CAT.getDbValue(), dataCds, dataValue);
		} catch (DataNotFoundException e) {
			// 検索結果0件の場合は何もしない
		}
		List<String> searchList2 = new ArrayList<String>();
		return searchList2;
	}

	@Override
	public List<DpsCCdMst> searchInsTypeTitle(String prodCategory) {
		List<DpsCCdMst> resultList = null;
		// ワクチンの場合
		if(this.isVaccine(prodCategory)) {
			resultList = this.searchCodeByDataKbn(DpsCodeMaster.ITV.getDbValue());
		}
		// 医薬の場合
		else {
			DpsPlannedCtg plannedCtg;
			try {
				plannedCtg = dpsPlannedCtgDao.search(prodCategory);
			} catch (DataNotFoundException e) {
				final String errMsg = "計画対象カテゴリ領域に、" + prodCategory + "のデータが登録されていません";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
			}

		}
		return resultList;
	}

	@Override
	public List<CodeAndValue> searchTgtInsKb(String prodCategory) {
		List<DpsPlannedCtg> plannedCtg = new ArrayList<DpsPlannedCtg>();
		List<CodeAndValue> resultList = new ArrayList<CodeAndValue>();

		try {
			plannedCtg = dpsPlannedCtgDao.searchTermCtg(prodCategory);
			for (DpsPlannedCtg ctg : plannedCtg) {
				resultList.add(new CodeAndValue(ctg.getCategory(), ctg.getTgtInsKb()));
			}
		}catch (DataNotFoundException e) {
			final String errMsg = "計画対象カテゴリ領域に、" + prodCategory + "のデータが登録されていません";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		return resultList;
	}


	@Override
	public boolean isVaccine(String prodCategory) {
		List<DpsCCdMst> codeList = this.searchCodeByDataKbn(DpsCodeMaster.VAC.getDbValue());
		if(codeList.size() > 1) {
			final String errMsg = "計画管理汎用マスタにワクチンのカテゴリコードが複数登録されています。";
			throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg));
		}
		return codeList.get(0).getDataCd().equals(prodCategory);
	}


	@Override
	public boolean isSiire(String prodCategory) {
		List<DpsCCdMst> codeList = this.searchCodeByDataKbn(DpsCodeMaster.SII.getDbValue());
		if(codeList.size() > 1) {
			final String errMsg = "計画管理汎用マスタに仕入のカテゴリコードが複数登録されています。";
			throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg));
		}
		return codeList.get(0).getDataCd().equals(prodCategory);
	}

	@Override
	public List<DpsCCdMst> searchCodeByDataKbn(String dataKbn) {

		// 検索結果一覧
		List<DpsCCdMst> searchList = new ArrayList<DpsCCdMst>();
		try {
			// データ区分の検索
			searchList = dpsCodeMasterDao.searchCodeByDataKbn(dataKbn);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + dataKbn + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		return searchList;
	}

	@Override
	public List<DpsCCdMst> getCategoriesSortByProgressOrder() {

		final String dataKbn = "category";
		// 検索結果一覧
		List<DpsCCdMst> searchList = new ArrayList<DpsCCdMst>();
		try {
			// データ区分の検索
			searchList = dpsCodeMasterDao.searchCodeByDataKbnOrderByDataValue(dataKbn);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + dataKbn + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		return searchList;
	}

	@Override
	public String searchDataName(String dataKbn, String dataCd ) {

		// 検索結果一覧
		DpsCCdMst dpsCCdMst = new DpsCCdMst();
		try {
			// データ区分の検索
			dpsCCdMst = dpsCodeMasterDao.searchDataKbnAndCd(dataKbn, dataCd);
		} catch (DataNotFoundException e) {
			final String errMsg = "汎用マスタに、「DATA_KBN,DATA_CD=" + dataKbn + ","  + dataCd + "」が登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		return dpsCCdMst.getDataName();
	}

	@Override
	public List<DpsCCdMst> searchShienCategoryList(String sosCd, ProdPlanLevel planLevel) {

		// 検索結果一覧
		List<DpsCCdMst> searchList = new ArrayList<DpsCCdMst>();
		try {
			// カテゴリの検索
			searchList = dpsCodeMasterDao.searchShienCategoryList(sosCd, planLevel);
		} catch (DataNotFoundException e) {
			// 検索結果0件の場合は何もしない
		}
		return searchList;
	}
	@Override
	public List<JokenSet> searchTokuyakuJokenSetCd(){

		// 検索結果一覧
		List<JokenSet> searchList = new ArrayList<JokenSet>();
		try {
			// カテゴリの検索
			searchList = dpsCodeMasterDao.searchTokuyakuJokenSetCd();
		} catch (DataNotFoundException e) {
			// 検索結果0件の場合は何もしない
		}
		return searchList;

	}
}
