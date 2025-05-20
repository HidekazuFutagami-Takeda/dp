package jp.co.takeda.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dao.PlannedCtgDao;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.model.PlannedCtg;

/**
 * 計画対象カテゴリ領域の検索サービス実装クラス
 *
 * @author rna8405
 *
 */
@Transactional
@Service("dpmPlannedCtgSearchService")
public class DpmPlannedCtgSearchServiceImpl implements DpmPlannedCtgSearchService {

	/**
	 * 計画対象カテゴリ領域Dao
	 */
	@Autowired(required = true)
	@Qualifier("plannedCtgDao")
	protected PlannedCtgDao plannedCtgDao;

	@Override
	public List<String> searchCategoryByPlanLevel(ProdPlanLevel planLevel) {
		List<PlannedCtg> list = new ArrayList<PlannedCtg>();
		try {
			// 計画立案レベルを条件に、計画対象カテゴリ領域を取得する
			list = plannedCtgDao.selectByPlanLevel(planLevel);
		} catch (DataNotFoundException e) {
			// データが取得できない場合は何もしない
		}

		// 習得した計画対象カテゴリ領域から、カテゴリを一覧にする
		List<String> categoryList = new ArrayList<String>();
		for (PlannedCtg ctg : list) {
			categoryList.add(ctg.getCategory());
		}

		return categoryList;
	}

}
