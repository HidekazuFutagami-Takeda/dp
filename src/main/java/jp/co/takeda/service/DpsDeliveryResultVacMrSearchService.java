package jp.co.takeda.service;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.DeliveryResultVacMrListDto;
import jp.co.takeda.dto.DeliveryResultVacMrScDto;

/**
 * 過去実績参照(担当者別計画モード)の検索に関するサービスインタフェース
 * 
 * @author siwamoto
 */
public interface DpsDeliveryResultVacMrSearchService {

	/**
	 * 過去実績詳細の検索結果DTOを取得する。
	 * 
	 * @param scDto 過去実績詳細の検索条件用DTO
	 * @return 過去実績詳細の検索結果用DTOリスト
	 * @throws LogicalException 検索結果が0件だった場合
	 */
	DeliveryResultVacMrListDto searchDeliveryResultVacMrList(DeliveryResultVacMrScDto scDto) throws LogicalException;

}
