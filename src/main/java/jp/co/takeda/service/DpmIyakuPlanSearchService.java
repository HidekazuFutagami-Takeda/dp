package jp.co.takeda.service;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.ProdPlanResultDto;
import jp.co.takeda.dto.ProdPlanScDto;

/**
 * 組織別計画(全社)の検索に関するサービスインタフェース
 * 
 * @author nozaki
 */
public interface DpmIyakuPlanSearchService {

	/**
	 * 検索条件をもとに、組織別品目別計画(全社)を取得する。
	 * 
	 * @param scDto 組織別品目別計画の検索用DTO
	 * @return 組織別品目別計画の検索結果DTO
	 * @throws LogicalException
	 */
	ProdPlanResultDto searchSosProdPlan(ProdPlanScDto scDto) throws LogicalException;

}
