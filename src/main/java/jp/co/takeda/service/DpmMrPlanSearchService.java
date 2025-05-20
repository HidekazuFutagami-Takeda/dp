package jp.co.takeda.service;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.ProdPlanResultDto;
import jp.co.takeda.dto.ProdPlanScDto;
import jp.co.takeda.dto.ProdPlanSummaryResultDto;
import jp.co.takeda.dto.ProdPlanSummaryScDto;
import jp.co.takeda.dto.SosPlanResultDto;
import jp.co.takeda.dto.SosPlanScDto;

/**
 * 組織別計画(担当者)の検索に関するサービスインタフェース
 *
 * @author nozaki
 */
public interface DpmMrPlanSearchService {

	/**
	 * 検索条件をもとに、組織別計画(担当者)を取得する。
	 *
	 * @param scDto 組織別計画の検索用DTO
	 * @param JgiNo MRユーザの従業員番号
	 * @return 組織別計画の検索結果DTO
	 * @throws LogicalException データが存在しない場合、品目の立案レベルが不正の場合
	 */
	SosPlanResultDto searchSosPlan(SosPlanScDto scDto, String JgiNo) throws LogicalException;

	/**
	 * 検索条件をもとに、組織別品目別計画(担当者)を取得する。
	 *
	 * @param scDto 組織別品目別計画の検索用DTO
	 * @return 組織別品目別計画の検索結果DTO
	 * @throws LogicalException
	 */
	ProdPlanResultDto searchSosProdPlan(ProdPlanScDto scDto) throws LogicalException;

	/**
	 * 検索条件をもとに、組織別品目別計画(担当者)・施設積上を取得する。
	 *
	 * @param scDto 組織別品目別計画(担当者)・施設積上の検索用DTO
	 * @return 組織別品目別計画(担当者)・施設積上の検索結果DTO
	 * @throws LogicalException
	 */
	ProdPlanSummaryResultDto searchSosProdPlanInsSummary(ProdPlanSummaryScDto scDto) throws LogicalException;

}
