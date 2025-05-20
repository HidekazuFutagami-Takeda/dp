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
import jp.co.takeda.bean.Constants;
import jp.co.takeda.dao.CodeMasterDao;
import jp.co.takeda.dao.PlannedCtgDao;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.PlannedCtg;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.TgtInsKbn;

/**
 * カテゴリ・品目の検索に関するサービス実装クラス
 * @author rna8405
 */
@Transactional
@Service("dpmCodeMasterSearchService")
public class DpmCodeMasterSearchServiceImpl implements DpmCodeMasterSearchService {

	/**
	 * 計画管理汎用マスタDAO
	 */
	@Autowired(required = true)
	@Qualifier("codeMasterDao")
	protected CodeMasterDao codeMasterDao;

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedCtgDao")
	protected PlannedCtgDao plannedCtgDao;

	/**
	 * カテゴリを検索する。
	 * @param sosCategory 組織カテゴリ
	 * @return カテゴリ一覧
	 */
	public List<DpmCCdMst> searchCategory(String sosCategory) {

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
		List<DpmCCdMst> searchList = new ArrayList<DpmCCdMst>();
		try {
			// カテゴリの検索
			searchList = codeMasterDao.searchCategory(CodeMaster.CAT.getDbValue(), dataCds);
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
	public List<DpmCCdMst> searchCategorylist(String sosCategory, String dataValue) {

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
		List<DpmCCdMst> searchList = new ArrayList<DpmCCdMst>();
		try {
			// カテゴリの検索
			searchList = codeMasterDao.searchCategoryList(CodeMaster.CAT.getDbValue(), dataCds, dataValue);
		} catch (DataNotFoundException e) {
			// 検索結果0件の場合は何もしない
		}
		return searchList;
	}

	@Override
	public List<DpmCCdMst> searchInsTypeTitle(String prodCategory) {
		List<DpmCCdMst> resultList = null;
		// ワクチンの場合
		if(this.isVaccine(prodCategory)) {
			resultList = this.searchCodeByDataKbn(CodeMaster.ITV.getDbValue());
		}
		//add Start 2025/2/19  H.Futagami 202410要望:カテゴリに全て（医薬）追加
		else if(Constants.CATEGORY_IYAKU_ALL.equals(prodCategory)){
			resultList = this.searchCodeByDataKbn(CodeMaster.IT.getDbValue());
		}
		//add End 2025/2/19  H.Futagami 202410要望:カテゴリに全て（医薬）追加
		// 医薬の場合
		else {
			PlannedCtg plannedCtg;
			try {
				plannedCtg = plannedCtgDao.search(prodCategory);
				if(plannedCtg.getTgtInsKb().equals(TgtInsKbn.ZATSU_NASHI.getDbValue())) {
					resultList = this.searchCodeByDataKbn(CodeMaster.IT.getDbValue());
				}
				else {
					resultList = this.searchCodeByDataKbn(CodeMaster.ITZ.getDbValue());
				}
			} catch (DataNotFoundException e) {
				final String errMsg = "計画対象カテゴリ領域に、" + prodCategory + "のデータが登録されていません";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
			}

		}
		return resultList;
	}

	@Override
	public List<CodeAndValue> searchTgtInsKb(String prodCategory) {
		List<PlannedCtg> plannedCtg = new ArrayList<PlannedCtg>();
		List<CodeAndValue> resultList = new ArrayList<CodeAndValue>();

		try {
			plannedCtg = plannedCtgDao.searchTermCtg(prodCategory);
			for (PlannedCtg ctg : plannedCtg) {
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
		List<DpmCCdMst> codeList = this.searchCodeByDataKbn(CodeMaster.VAC.getDbValue());
		if(codeList.size() > 1) {
			final String errMsg = "計画管理汎用マスタにワクチンのカテゴリコードが複数登録されています。";
			throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg));
		}
		return codeList.get(0).getDataCd().equals(prodCategory);
	}


	@Override
	public List<DpmCCdMst> searchCodeByDataKbn(String dataKbn) {

		// 検索結果一覧
		List<DpmCCdMst> searchList = new ArrayList<DpmCCdMst>();
		try {
			// データ区分の検索
			searchList = codeMasterDao.searchCodeByDataKbn(dataKbn);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + dataKbn + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		return searchList;
	}



}
