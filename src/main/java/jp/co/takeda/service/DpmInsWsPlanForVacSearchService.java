package jp.co.takeda.service;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.ManageInsWsPlanForVacDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacHeaderDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacResultDetailTotalDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacScDto;
import jp.co.takeda.dto.ManageInsWsPlanProdForVacResultDto;
import jp.co.takeda.dto.ManageInsWsProdPlanForVacScDto;

/**
 * ワクチン用施設特約店別計画の検索に関するサービスインタフェース
 * 
 * @author nozaki
 */
public interface DpmInsWsPlanForVacSearchService {

	/**
	 * 施設コード・特約店コードから、施設特約店・品目別計画の検索結果ヘッダ情報を取得する。
	 * 
	 * @param insNo 施設コード
	 * @param tmsTytenCd 施設コード
	 * @return 施設特約店品目別計画の検索結果 ヘッダ情報
	 */
	ManageInsWsPlanForVacHeaderDto searchInsWsPlanHeader(String insNo, String tmsTytenCd);

	/**
	 * 検索条件をもとに、施設特約店別計画(明細)を取得する。
	 * 
	 * @param scDto 施設特約店別計画の検索用DTO
	 * @return 施設特約店別計画の検索結果DTO
	 * @throws LogicalException データが存在しない場合、施設コードが不正の場合
	 */
	ManageInsWsPlanForVacDto searchInsWsPlan(ManageInsWsPlanForVacScDto scDto) throws LogicalException;

	/**
	 * 施設特約店別計画(集計)を取得する
	 * 
	 * @param scDto 施設特約店別計画の検索用DTO
	 * @return 施設特約店別計画(明細)DTO
	 */
	ManageInsWsPlanForVacResultDetailTotalDto searchInsWsPlanTotal(ManageInsWsPlanForVacScDto scDto);

	/**
	 * 検索条件をもとに、施設特約店別品目別計画を取得する。
	 * 
	 * @param scDto 施設特約店別品目別計画の検索用DTO
	 * @return 施設特約店別品目別計画の検索結果DTO
	 * @throws LogicalExceptionデータが存在しない場合、施設コードが不正の場合
	 */
	ManageInsWsPlanProdForVacResultDto searchInsWsProdPlan(ManageInsWsProdPlanForVacScDto scDto) throws LogicalException;
}
