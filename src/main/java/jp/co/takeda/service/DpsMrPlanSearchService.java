package jp.co.takeda.service;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dao.div.RefDeliveryScope;
import jp.co.takeda.dto.EstimationResultDetailResultDto;
import jp.co.takeda.dto.EstimationResultDetailScDto;
import jp.co.takeda.dto.MrPlanResultDto;
import jp.co.takeda.dto.OfficePlanStatusResultDto;
import jp.co.takeda.dto.OfficeTeamPlanChoseiDto;
import jp.co.takeda.dto.PlannedProdResultListDto;
import jp.co.takeda.dto.PlannedProdScDto;
import jp.co.takeda.model.div.InsType;

/**
 * 担当者別計画機能の検索に関するサービスインタフェース
 *
 * @author stakeuchi
 */
public interface DpsMrPlanSearchService {

	/**
	 * 組織コード(営業所)をもとに、営業所計画ステータスを取得する。
	 *
	 * @param scDto 組織コード(営業所)
	 * @return 営業所計画ステータスの検索結果用DTO
	 */
//	OfficePlanStatusResultDto searchOfficePlanStatus(String sosCd3);
	OfficePlanStatusResultDto searchOfficePlanStatus(String sosCd3, String category);

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBUで計画対象品目が取得できない場合のエラーメッセージの表示変更
	/**
	 * 計画対象品目の検索用DTOをもとに、計画対象品目一覧を取得する。
	 *
	 * @param scDto 計画対象品目の検索用DTO
	 * @return 計画対象品目の検索結果用DTOのリスト
	 */
//	PlannedProdResultListDto searchPlannedProdList(PlannedProdScDto scDto);
	PlannedProdResultListDto searchPlannedProdList(PlannedProdScDto scDto) throws DataNotFoundException;
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBUで計画対象品目が取得できない場合のエラーメッセージの表示変更

	/**
	 * 担当者別計画情報(営業所指定)を取得する。
	 *
	 * @param insType 施設出力対象区分(UH,P以外の場合は合計)
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(営業所)
	 * @param scope 参照品目の実績参照範囲
	 * @return 担当者別計画検索結果用DTO
	 */
//	MrPlanResultDto searchMrPlan(InsType insType, String prodCode, String sosCd3, RefDeliveryScope scope) throws LogicalException;
	MrPlanResultDto searchMrPlan(InsType insType, String prodCode, String sosCd3, RefDeliveryScope scope, String category) throws LogicalException;

	/**
	 * 担当者別計画情報(チーム指定)を取得する。
	 *
	 * @param insType 施設出力対象区分(UH,P以外の場合は合計)
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(営業所)
	 * @param scope 参照品目の実績参照範囲
	 * @return 担当者別計画検索結果用DTO
	 */
	MrPlanResultDto searchMrPlan(InsType insType, String prodCode, String sosCd3, String sosCd4, RefDeliveryScope scope) throws LogicalException;

	/**
	 * チーム別計画情報(営業所指定)を取得する。
	 *
	 * @param insType 施設出力対象区分(UH,P以外の場合は合計)
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(営業所)
	 * @param scope 参照品目の実績参照範囲
	 * @return 担当者別計画検索結果用DTO
	 */
	MrPlanResultDto searchTeamPlan(InsType insType, String prodCode, String sosCd3, RefDeliveryScope scope) throws LogicalException;

	/**
	 * チーム別計画情報(チーム指定)を取得する。
	 *
	 * @param insType 施設出力対象区分(UH,P以外の場合は合計)
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param scope 参照品目の実績参照範囲
	 * @return 担当者別計画検索結果用DTO
	 */
	MrPlanResultDto searchTeamPlan(InsType insType, String prodCode, String sosCd3, String sosCd4, RefDeliveryScope scope) throws LogicalException;

	/**
	 * 試算結果詳細を取得する。
	 *
	 * @param scDto 試算結果詳細の検索用DTO
	 * @return 試算結果詳細の検索結果用DTO
	 */
	EstimationResultDetailResultDto searchEstimationResultDetail(EstimationResultDetailScDto scDto);

	/**
	 * 上位計画と、担当者別計画の間における調整金額有無を取得する。<br>
	 *
	 * <pre>
	 * 上位計画は試算タイプによって異なる。
	 * 試算タイプが"営→チ→担"の場合、チーム別計画
	 * 試算タイプが"営→担"の場合、営業所計画
	 * 試算タイプがnullの場合、"営→チ→担"と解釈し、上位計画をチーム別計画に設定する。
	 * </pre>
	 *
	 * @param sosCd 組織コード(営業所)
	 * @param prodCode 品目固定コード
	 * @return 調整金額有無
	 * @throws LogicalException
	 */
//	OfficeTeamPlanChoseiDto searchChosei(String sosCd, String prodCode) throws LogicalException;
	OfficeTeamPlanChoseiDto searchChosei(String sosCd, String prodCode, String category) throws LogicalException;
}
