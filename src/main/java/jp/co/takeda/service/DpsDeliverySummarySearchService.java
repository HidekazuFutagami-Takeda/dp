package jp.co.takeda.service;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.DeliveryResultSummaryDto;
import jp.co.takeda.model.div.InsType;

/**
 * 過去実績参照(担当者別計画モード)の検索に関するサービスインタフェース
 *
 * @author tkawabata
 */
public interface DpsDeliverySummarySearchService {

	/**
	 * 担当者別の期集約サマリ情報を取得する。
	 *
	 * @param insType 施設対象区分(NULLの場合は合計)
	 * @param prodCode 対象の品目固定コード
	 * @param sosCd3 組織コード(営業所) sosCd3が指定されている場合はsosCd4はNULL可(参照しない)
	 * @param sosCd4 組織コード(チーム) sosCd4が指定されている場合はsosCd3はNULL可(参照しない)
	 * @param category カテゴリ（NULL不可）
	 * @return 担当者別の期集約サマリ情報
	 * @throws LogicalException
	 */
	DeliveryResultSummaryDto searchDeliveryResultSummaryDto(InsType insType, String prodCode, String sosCd3, String sosCd4, String category) throws LogicalException;

}
