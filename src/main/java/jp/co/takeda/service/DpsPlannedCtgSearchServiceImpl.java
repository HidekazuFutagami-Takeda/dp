package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.DATA_NOT_FOUND_ERROR;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DpsPlannedCtgDao;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.model.DpsPlannedCtg;

/**
 * 計画対象カテゴリ領域の検索サービス実装クラス
 *
 * @author rna8405
 *
 */
@Transactional
@Service("dpsPlannedCtgSearchService")
public class DpsPlannedCtgSearchServiceImpl implements DpsPlannedCtgSearchService {

	/**
	 * 計画対象カテゴリ領域Dao
	 */
	@Autowired(required = true)
	@Qualifier("dpsPlannedCtgDao")
	protected DpsPlannedCtgDao dpsPlannedCtgDao;

	@Override
	public List<String> searchCategoryByPlanLevel(ProdPlanLevel planLevel) {
		List<DpsPlannedCtg> list = new ArrayList<DpsPlannedCtg>();
		try {
			// 計画立案レベルを条件に、計画対象カテゴリ領域を取得する
			list = dpsPlannedCtgDao.selectByPlanLevel(planLevel);
		} catch (DataNotFoundException e) {
			// データが取得できない場合は何もしない
		}

		// 習得した計画対象カテゴリ領域から、カテゴリを一覧にする
		List<String> categoryList = new ArrayList<String>();
		for (DpsPlannedCtg ctg : list) {
			categoryList.add(ctg.getCategory());
		}

		return categoryList;
	}

	@Override
	public DpsPlannedCtg searchPlannedCtg(String category) {
		DpsPlannedCtg plannedCtg = new DpsPlannedCtg();

		try {
			plannedCtg = dpsPlannedCtgDao.search(category);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象カテゴリ領域の取得に失敗。category=";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + category), e);
		}

		return plannedCtg;
	}
}
