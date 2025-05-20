package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.dao.div.ProdPlanLevel;

/**
 * 計画対象カテゴリ領域の検索サービスインターフェース
 *
 * @author rna8405
 */
public interface DpmPlannedCtgSearchService {

	/**
	 * 対象の計画立案レベルをもつ、カテゴリ一覧を取得する。
	 * @param planLevel 計画立案レベル
	 * @return カテゴリ一覧
	 */
	List<String> searchCategoryByPlanLevel(ProdPlanLevel planLevel);
}
