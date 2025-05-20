package jp.co.takeda.service;

import java.util.List;
import java.util.Map;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.InsDocPlanJgiListScDto;
import jp.co.takeda.dto.InsDocPlanProdListScDto;
import jp.co.takeda.dto.InsDocPlanUpDateScDto;

/**
 * 施設医師別計画機能の検索に関するサービスインタフェース
 * 
 * @author nozaki
 */
public interface DpsInsDocPlanSearchService {

	/**
	 * 計画対象品目の検索用DTOをもとに、計画対象品目一覧を取得する。
	 * 
	 * @param scDto 計画対象品目の検索用DTO
	 * @return 計画対象品目の検索結果用DTOのリスト
	 */
	List<Map<String, Object>> searchPlannedProdList(InsDocPlanProdListScDto scDto) throws LogicalException;

	/**
	 * 計画対象品目の検索用DTOをもとに、計画対象担当者一覧を取得する。
	 * 
	 * @param scDto 計画対象品目の検索用DTO
	 * @return 計画対象品目の検索結果用DTO
	 */
	List<Map<String, Object>> searchMrList(InsDocPlanJgiListScDto scDto) throws LogicalException;

	/**
	 * 施設医師別計画ヘッダーの検索処理
	 * 
	 * @param scDto 施設医師別計画の検索用DTO
	 * @return 検索結果用DTO
	 * @throws LogicalException
	 */
	Map<String, Object> searchInsDocPlanHeader(InsDocPlanUpDateScDto scDto) throws LogicalException;

	/**
	 * 施設医師別計画一覧の検索処理
	 * 
	 * @param scDto 施設医師別計画の検索用DTO
	 * @return 検索結果用DTO
	 * @throws LogicalException
	 */
	List<Map<String, Object>> searchInsDocPlanList(InsDocPlanUpDateScDto scDto) throws LogicalException;

	/**
	 * 調整金額用のメッセージを生成する。
	 * 
	 * @param dto 検索結果DTO
	 * @return 調整金額用のメッセージ
	 */
	String createChoseiMsg(Map<String, Object> resultData);
}
