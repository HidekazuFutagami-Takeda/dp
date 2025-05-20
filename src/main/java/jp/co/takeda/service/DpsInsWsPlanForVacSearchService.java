package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.InsWsPlanForVacJgiListResultDto;
import jp.co.takeda.dto.InsWsPlanForVacJgiListScDto;
import jp.co.takeda.dto.InsWsPlanForVacProdListResultDto;
import jp.co.takeda.dto.InsWsPlanForVacProdListScDto;
import jp.co.takeda.dto.InsWsPlanForVacUpDateResultDto;
import jp.co.takeda.dto.InsWsPlanForVacUpDateResultListDto;
import jp.co.takeda.dto.InsWsPlanForVacUpDateScDto;
import jp.co.takeda.dto.InsWsPlanUpDateMonNnuScDto;

/**
 * (ワ)施設特約店別計画機能の検索に関するサービスインタフェース
 * 
 * @author nozaki
 */
public interface DpsInsWsPlanForVacSearchService {

	/**
	 * (ワ)計画対象品目の検索用DTOをもとに、計画対象担当者一覧を取得する。
	 * 
	 * @param scDto 計画対象品目の検索用DTO
	 * @return 計画対象品目の検索結果用DTO
	 * @throws DataNotFoundException
	 */
	InsWsPlanForVacJgiListResultDto searchMrList(InsWsPlanForVacJgiListScDto scDto) throws DataNotFoundException;

	/**
	 * (ワ)計画対象品目の検索用DTOをもとに、計画対象品目一覧を取得する。
	 * 
	 * @param scDto 施設特約店別計画の検索用DTO
	 * @return 検索結果用DTO
	 * @throws DataNotFoundException
	 */
	List<InsWsPlanForVacProdListResultDto> searchPlannedProdList(InsWsPlanForVacProdListScDto scDto) throws DataNotFoundException;

	/**
	 * (ワ)施設特約店別計画一覧を取得する。
	 * 
	 * @param scDto ワクチン用施設特約店別計画の検索用DTO
	 * @return 検索結果用DTO
	 * @throws LogicalException
	 */
	InsWsPlanForVacUpDateResultListDto searchInsWsPlanList(InsWsPlanForVacUpDateScDto scDto) throws LogicalException;

	/**
	 * (ワ)施設・特約店の実績を取得する。
	 * 
	 * @param monNnuScDto 施設特約店別計画追加施設過去実績検索用DTO
	 * @return 検索結果用DTO
	 * @throws LogicalException
	 */
	InsWsPlanForVacUpDateResultDto searchJisseki(InsWsPlanUpDateMonNnuScDto monNnuScDto) throws LogicalException;
}
