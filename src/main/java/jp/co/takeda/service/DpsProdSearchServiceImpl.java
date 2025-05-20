package jp.co.takeda.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.ProdSummaryDao;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.ProdCategory;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.view.ProdSummary;

/**
 * 計画品目検索サービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpsProdSearchService")
public class DpsProdSearchServiceImpl implements DpsProdSearchService {

	/**
	 * 簡易版品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("prodSummaryDao")
	protected ProdSummaryDao prodSummaryDao;

	/**
	 * カテゴリ・品目の検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	/**
	 * 計画支援の計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;


	public List<ProdSummary> searchIyakuProdList(ProdCategory[] category, boolean includeRefFlg) {
		try {
			if (includeRefFlg) {
				return prodSummaryDao.searchRefList(PlannedProdDAO.SORT_STRING2, Sales.IYAKU, category);
			} else {
				return prodSummaryDao.searchList(PlannedProdDAO.SORT_STRING2, Sales.IYAKU, category);
			}
		} catch (DataNotFoundException e) {
			final String errMsg = "計画品目が登録されていないため、システムエラー";
			throw new SystemException(new Conveyance(ErrMessageKey.SYSTEM_ERROR, errMsg), e);
		}
	}

	public List<ProdSummary> searchIyakuProdList(String category, boolean includeRefFlg) {
		try {
			if (includeRefFlg) {
				return prodSummaryDao.searchRefList(PlannedProdDAO.SORT_STRING2, Sales.IYAKU, category);
			} else {
				return prodSummaryDao.searchList(PlannedProdDAO.SORT_STRING2, Sales.IYAKU, category);
			}
		} catch (DataNotFoundException e) {
			final String errMsg = "計画品目が登録されていないため、システムエラー";
			throw new SystemException(new Conveyance(ErrMessageKey.SYSTEM_ERROR, errMsg), e);
		}
	}

	public List<ProdSummary> searchIyakuPriorityProdList(ProdCategory[] category, boolean isPriorityOnly) {
		try {
			return prodSummaryDao.searchList(PlannedProdDAO.SORT_STRING, Sales.IYAKU, category, isPriorityOnly);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画品目が登録されていないため、システムエラー";
			throw new SystemException(new Conveyance(ErrMessageKey.SYSTEM_ERROR, errMsg), e);
		}
	}

	public List<ProdSummary> searchWakutinProdList(boolean includeRefFlg) {
		String vaccineCode = dpsCodeMasterSearchService.searchCodeByDataKbn(CodeMaster.VAC.getDbValue()).get(0).getDataCd();
		try {
			if (includeRefFlg) {
				return prodSummaryDao.searchRefList(PlannedProdDAO.SORT_STRING2, Sales.VACCHIN, vaccineCode);
			} else {
				return prodSummaryDao.searchList(PlannedProdDAO.SORT_STRING2, Sales.VACCHIN, vaccineCode);
			}
		} catch (DataNotFoundException e) {
			final String errMsg = "計画品目が登録されていないため、システムエラー";
			throw new SystemException(new Conveyance(ErrMessageKey.SYSTEM_ERROR, errMsg), e);
		}
	}

	public List<PlannedProd> searchProdList(String category, Sales sales, ProdPlanLevel planLevel) {
		try {
			return plannedProdDAO.searchListByPlanLevel(PlannedProdDAO.SORT_STRING, sales, category, planLevel);

		} catch (DataNotFoundException e) {
			final String errMsg = "計画品目が登録されていないため、システムエラー";
			throw new SystemException(new Conveyance(ErrMessageKey.SYSTEM_ERROR, errMsg), e);
		}
	}

	//計画対象品目を取得する
	public PlannedProd searchProd(String prodCode) {
		PlannedProd plannedProd = null;
		try {
			plannedProd = plannedProdDAO.search(prodCode);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目【品目固定コード：" + prodCode + "】が存在しないため、システムエラー";
			throw new SystemException(new Conveyance(ErrMessageKey.SYSTEM_ERROR, errMsg), e);
		}
		return plannedProd;
	}

}
