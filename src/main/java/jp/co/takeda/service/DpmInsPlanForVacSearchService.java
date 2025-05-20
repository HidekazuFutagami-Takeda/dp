package jp.co.takeda.service;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.InsPlanForVacHeaderDto;
import jp.co.takeda.dto.InsPlanForVacResultDetailTotalDto;
import jp.co.takeda.dto.InsPlanForVacResultDto;
import jp.co.takeda.dto.InsPlanForVacScDto;
import jp.co.takeda.dto.InsProdPlanForVacResultDto;
import jp.co.takeda.dto.InsProdPlanForVacScDto;

/**
 * ワクチン用施設別計画の検索に関するサービスインタフェース
 * 
 * @author nozaki
 */
public interface DpmInsPlanForVacSearchService {

	/**
	 * 施設コードから施設品目別計画の検索結果ヘッダ情報を取得する。
	 * 
	 * @param insNo 施設コード
	 * @return 施設品目別計画の検索結果 ヘッダ情報
	 */
	InsPlanForVacHeaderDto searchInsPlanHeader(String insNo);

	/**
	 * 検索条件をもとに、施設別計画(明細)を取得する。
	 * 
	 * @param scDto 施設別計画の検索用DTO
	 * @return 施設別計画の検索結果DTO
	 * @throws LogicalException データが存在しない場合、施設コードが不正の場合
	 */
	InsPlanForVacResultDto searchInsPlan(InsPlanForVacScDto scDto) throws LogicalException;

	/**
	 * 検索条件と施設別計画(明細)をもとに、施設別計画(集計)を取得する
	 * 
	 * @param scDto 施設別計画の検索用DTO
	 * @param detailResult 施設別計画の検索結果DTO
	 * @return 施設別計画(明細)DTO
	 */
	InsPlanForVacResultDetailTotalDto searchInsPlanTotal(InsPlanForVacScDto scDto, InsPlanForVacResultDto detailResult);

	/**
	 * 検索条件をもとに、施設別品目別計画を取得する。
	 * 
	 * @param scDto 施設別品目別計画の検索用DTO
	 * @return 施設別品目別計画の検索結果DTO
	 * @throws LogicalExceptionデータが存在しない場合、施設コードが不正の場合
	 */
	InsProdPlanForVacResultDto searchInsProdPlan(InsProdPlanForVacScDto scDto) throws LogicalException;
}
