package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.ProdMonthPlanResultDto;
import jp.co.takeda.dto.ProdPlanScDto;

/**
 * 組織別計画(全社)の検索に関するサービスインタフェース
 *
 * @author nozaki
 */
public interface DpmIyakuMonthPlanSearchService {

	/**
	 * 検索条件をもとに、組織別品目別計画(全社)を取得する。
	 *
	 * @param scDto 組織別品目別計画の検索用DTO
	 * @param jrnsCtgList JRNSに含まれるカテゴリリスト
	 * @return 組織別品目別計画の検索結果DTO
	 * @throws LogicalException
	 */
	ProdMonthPlanResultDto searchSosProdPlan(ProdPlanScDto scDto, List<String> jrnsCtgList) throws LogicalException;

}
