package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.MikakutokuSijouResultDto;
import jp.co.takeda.dto.MikakutokuSijouScDto;

/**
 * 未獲得市場の検索に関するサービスインタフェース
 * 
 * @author stakeuchi
 */
public interface DpsMikakutokuSijouSearchService {

	/**
	 * 未獲得市場の検索結果DTOを取得する。
	 * 
	 * @param scDto 未獲得市場の検索条件用DTO
	 * @return 未獲得市場の検索結果用DTOリスト
	 * @throws LogicalException 検索結果が0件だった場合
	 */
	List<MikakutokuSijouResultDto> searchMikakutokuSijouList(MikakutokuSijouScDto scDto) throws LogicalException;

	/**
	 * 未獲得市場の集計された検索結果DTOを取得する。
	 * 
	 * @param scDto 未獲得市場の検索条件用DTO
	 * @return 未獲得市場の検索結果用DTOリスト
	 * @throws LogicalException 検索結果が0件だった場合
	 */
	List<MikakutokuSijouResultDto> searchMikakutokuSijouSumList(MikakutokuSijouScDto scDto) throws LogicalException;

}
