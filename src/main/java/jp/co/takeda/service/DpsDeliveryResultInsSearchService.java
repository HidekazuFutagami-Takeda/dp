package jp.co.takeda.service;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.DeliveryResultInsListDto;
import jp.co.takeda.dto.DeliveryResultInsScDto;

/**
 * 過去実績参照(施設特約店別計画モード)の検索を行うサービスインターフェイス
 *
 * @author siwamoto
 */
public interface DpsDeliveryResultInsSearchService {

	/**
	 * 過去実績詳細の検索結果DTOを取得する。
	 *
	 * @param scDto 過去実績詳細の検索条件用DTO
	 * @param category カテゴリ
	 * @return 過去実績詳細の検索結果用DTOリスト
	 * @throws LogicalException 検索結果が0件だった場合
	 */
	DeliveryResultInsListDto searchDeliveryResultInsList(DeliveryResultInsScDto scDto, String category) throws LogicalException;

	/**
	 * 過去実績詳細の検索結果DTOを取得する。
	 *
	 * @param scDto 過去実績詳細の検索条件用DTO
	 * @return 過去実績詳細の検索結果用DTOリスト
	 * @throws LogicalException 検索結果が0件だった場合
	 */
	DeliveryResultInsListDto searchDeliveryResultInsList2(DeliveryResultInsScDto scDto) throws LogicalException;
}
