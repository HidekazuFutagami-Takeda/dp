package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dto.DistributionForVacExecOrgDto;
import jp.co.takeda.dto.InsWsDistForVacProdResultDto;
import jp.co.takeda.dto.InsWsDistProdUpdateDto;

/**
 * 配分機能の検索に関するサービスインタフェース
 * 
 * @author khashimoto
 */
public interface DpsDistributionForVacProdSearchService {

	/**
	 * (ワクチン)配分対象品目一覧を取得する。
	 * 
	 * @return 配分対象品目検索結果DTOのリスト
	 */
	List<InsWsDistForVacProdResultDto> searchDistributionProdList() throws DataNotFoundException;

	/**
	 * 配分処理実行前の担当者別計画立案ステータスを取得する。
	 * 
	 * @param insWsDistProdUpdateDtoList 配分品目一覧更新DTOのリスト
	 * @return 配分実行用DTOのリスト
	 */
	List<DistributionForVacExecOrgDto> searchDistributionPreparation(List<InsWsDistProdUpdateDto> insWsDistProdUpdateDtoList);

}
