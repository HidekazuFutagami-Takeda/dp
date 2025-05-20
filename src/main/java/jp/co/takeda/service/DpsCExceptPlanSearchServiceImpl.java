package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DpsCExceptPlanDao;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dto.SupInfoDto;
import jp.co.takeda.model.ExceptPlan;
import jp.co.takeda.model.SosMst;

/**
 * 立案対象外情報検索サービスインターフェース
 *
 * @author nakashima
 *
 */
@Transactional
@Service("dpsCExceptPlanSearchService")
public class DpsCExceptPlanSearchServiceImpl implements DpsCExceptPlanSearchService {


	/**
	 * 計画支援汎用マスタDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsCExceptPlanDao")
	protected DpsCExceptPlanDao dpsCExceptPlanDao;

	/**
	 * 組織情報取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	@Override
	public boolean hasExceptPlan(String sosCd) {


		if( 0 < dpsCExceptPlanDao.countExceptPlan(sosCd)) {
			return true;
		}
		return false;

	}


	@Override
	public SupInfoDto getSupplementalInfo(String sosCd, String category) {


		SosMst sosMst;
		try {
			sosMst = sosMstDAO.search(sosCd);
		} catch (DataNotFoundException e) {
			e.printStackTrace();
			final String errMsg = "この時点で組織がないことはあり得ない";
			throw new SystemException(new Conveyance(LOGICAL_ERROR, errMsg));
		}
		List<ExceptPlan> exceptPlans = dpsCExceptPlanDao.searchExceptPlan(sosCd, category);
		return new SupInfoDto(sosMst ,exceptPlans);
	}


}

