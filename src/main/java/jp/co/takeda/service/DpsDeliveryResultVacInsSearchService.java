package jp.co.takeda.service;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.DeliveryResultVacInsListDto;
import jp.co.takeda.dto.DeliveryResultVacInsScDto;

/**
 * 過去実績参照(施設特約店別計画モード)の検索を行うサービスインターフェイス
 * 
 * @author siwamoto
 */
public interface DpsDeliveryResultVacInsSearchService {

	/**
	 * 過去実績詳細の検索結果DTOを取得する。
	 * 
	 * @param scDto 過去実績詳細の検索条件用DTO
	 * @return 過去実績詳細の検索結果用DTOリスト
	 * @throws LogicalException 検索結果が0件だった場合
	 */
	DeliveryResultVacInsListDto searchDeliveryResultVacInsList(DeliveryResultVacInsScDto scDto) throws LogicalException;

	/**
	 * 過去実績詳細の検索結果DTO(過去実績以外)を取得する。
	 * 
	 * @param scDto 過去実績詳細の検索条件用DTO
	 * @return 過去実績詳細の検索結果用DTOリスト
	 * @throws LogicalException 検索結果が0件だった場合
	 */
	DeliveryResultVacInsListDto searchDeliveryResultVacInsList2(DeliveryResultVacInsScDto scDto) throws LogicalException;
}
