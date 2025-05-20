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
import jp.co.takeda.dao.ManagePlannedProdDao;
import jp.co.takeda.dao.MasterManagePlannedProdDao;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.model.ManagePlannedProd;
import jp.co.takeda.model.MasterManagePlannedProd;
import jp.co.takeda.model.div.ProdCategory;
import jp.co.takeda.model.div.Sales;

/**
 * 計画品目検索サービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpmProdSearchService")
public class DpmProdSearchServiceImpl implements DpmProdSearchService {

	/**
	 * 管理の計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("managePlannedProdDao")
	protected ManagePlannedProdDao managePlannedProdDao;

	@Autowired(required = true)
	@Qualifier("masterManagePlannedProdDao")
	protected MasterManagePlannedProdDao masterManagePlannedProdDao;

	public List<ManagePlannedProd> searchIyakuProdList(ProdCategory category, ProdPlanLevel planLevel) {
		try {
			return managePlannedProdDao.searchList(ManagePlannedProdDao.SORT_STRING, Sales.IYAKU, category, planLevel);

		} catch (DataNotFoundException e) {
			final String errMsg = "計画品目が登録されていないため、システムエラー";
			throw new SystemException(new Conveyance(ErrMessageKey.SYSTEM_ERROR, errMsg), e);
		}
	}

	public List<ManagePlannedProd> searchWakutinProdList(ProdPlanLevel planLevel) {
		try {
			return managePlannedProdDao.searchList(ManagePlannedProdDao.SORT_STRING, Sales.VACCHIN, null, planLevel);

		} catch (DataNotFoundException e) {
			final String errMsg = "計画品目が登録されていないため、システムエラー";
			throw new SystemException(new Conveyance(ErrMessageKey.SYSTEM_ERROR, errMsg), e);
		}
	}

	public List<MasterManagePlannedProd> searchProdList(String category, Sales sales, ProdPlanLevel planLevel) {
		try {
			return masterManagePlannedProdDao.searchList(MasterManagePlannedProdDao.SORT_STRING, sales, category, planLevel);

		} catch (DataNotFoundException e) {
			final String errMsg = "計画品目が登録されていないため、システムエラー";
			throw new SystemException(new Conveyance(ErrMessageKey.SYSTEM_ERROR, errMsg), e);
		}
	}

// add Start 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える
	public List<MasterManagePlannedProd> searchProdDistributorList(String category, Sales sales, ProdPlanLevel planLevel) {
		try {
			return masterManagePlannedProdDao.searchDistributorList(MasterManagePlannedProdDao.SORT_STRING, sales, category, planLevel);

		} catch (DataNotFoundException e) {
			final String errMsg = "計画品目が登録されていないため、システムエラー";
			throw new SystemException(new Conveyance(ErrMessageKey.SYSTEM_ERROR, errMsg), e);
		}
	}
// add End 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える

	public List<MasterManagePlannedProd> searchJrnsProdList(ProdPlanLevel planLevel, List<String> ctgList) {
		try {
			return masterManagePlannedProdDao.searchJrnsList(MasterManagePlannedProdDao.SORT_STRING, planLevel, ctgList);

		} catch (DataNotFoundException e) {
			final String errMsg = "計画品目が登録されていないため、システムエラー";
			throw new SystemException(new Conveyance(ErrMessageKey.SYSTEM_ERROR, errMsg), e);
		}
	}
	public MasterManagePlannedProd searchPlannedProdByProdCode(String prodCode) {
		try {
			return masterManagePlannedProdDao.search(prodCode);

		} catch (DataNotFoundException e) {
			final String errMsg = "計画品目が登録されていないため、システムエラー";
			throw new SystemException(new Conveyance(ErrMessageKey.SYSTEM_ERROR, errMsg), e);
		}
	}
}
