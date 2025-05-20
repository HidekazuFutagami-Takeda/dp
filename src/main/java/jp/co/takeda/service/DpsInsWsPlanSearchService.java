package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.InsWsPlanJgiListResultDto;
import jp.co.takeda.dto.InsWsPlanJgiListScDto;
import jp.co.takeda.dto.InsWsPlanProdListResultDto;
import jp.co.takeda.dto.InsWsPlanProdListScDto;
import jp.co.takeda.dto.InsWsPlanUpDateMonNnuScDto;
import jp.co.takeda.dto.InsWsPlanUpDateResultDto;
import jp.co.takeda.dto.InsWsPlanUpDateResultListDto;
import jp.co.takeda.dto.InsWsPlanUpDateScDto;

/**
 * 施設特約店別計画機能の検索に関するサービスインタフェース
 *
 * @author nozaki
 */
public interface DpsInsWsPlanSearchService {

	/**
	 * 計画対象品目の検索用DTOをもとに、計画対象品目一覧を取得する。
	 *
	 * @param scDto 計画対象品目の検索用DTO
	 * @return 計画対象品目の検索結果用DTOのリスト
	 */
	List<InsWsPlanProdListResultDto> searchPlannedProdList(InsWsPlanProdListScDto scDto) throws LogicalException;

	/**
	 * 計画対象品目の検索用DTOをもとに、計画対象担当者一覧を取得する。
	 *
	 * @param scDto 計画対象品目の検索用DTO
	 * @return 計画対象品目の検索結果用DTO
	 */
	InsWsPlanJgiListResultDto searchMrList(InsWsPlanJgiListScDto scDto) throws LogicalException;

	/**
	 * 施設特約店別計画一覧の検索処理
	 *
	 * @param scDto 施設特約店別計画の検索用DTO
	 * @return 検索結果用DTO
	 * @throws LogicalException
	 */
	InsWsPlanUpDateResultListDto searchInsWsPlanList(InsWsPlanUpDateScDto scDto) throws LogicalException;

	/**
	 * 施設・特約店の実績検索処理
	 *
	 * @param monNnuScDto 施設特約店別計画追加施設過去実績検索用DTO
	 * @return 検索結果用DTO
	 * @throws LogicalException
	 */
	InsWsPlanUpDateResultDto searchJisseki(InsWsPlanUpDateMonNnuScDto monNnuScDto) throws LogicalException;

}
