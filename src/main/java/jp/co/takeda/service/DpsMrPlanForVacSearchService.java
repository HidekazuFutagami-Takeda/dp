package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.MrPlanForVacResultDto;
import jp.co.takeda.dto.PlannedProdForVacResultDto;

/**
 * (ワクチン)担当者別計画機能の検索に関するサービスインタフェース
 * 
 * @author khashimoto
 */
public interface DpsMrPlanForVacSearchService {

	/**
	 * ワクチン用担当者別計画情報のリストを取得する。<br>
	 * 
	 * <pre>
	 * 本部、本部ワクチンＧの場合、全担当者のデータを取得する。
	 * 小児科ACの場合、自チームのデータを取得する。 (J19-0010 対応・コメントのみ修正)
	 * 小児科MRの場合、自データのみ取得する。 (J19-0010 対応・コメントのみ修正)
	 * </pre>
	 * 
	 * @return 担当者別計画情報
	 */
	List<PlannedProdForVacResultDto> searchPlannedProdList();

	/**
	 * ワクチン用担当者別計画情報(品目指定)を取得する。<br>
	 * 
	 * <pre>
	 * 本部、本部ワクチンＧの場合、全担当者のデータを取得する。
	 * 小児科ACの場合、自チームのデータを取得する。 (J19-0010 対応・コメントのみ修正)
	 * 小児科MRの場合、自データのみ取得する。 (J19-0010 対応・コメントのみ修正)
	 * </pre>
	 * 
	 * @param prodCode 品目固定コード
	 * @return 担当者別計画情報
	 * @throws LogicalException
	 */
	List<MrPlanForVacResultDto> searchMrPlan(String prodCode) throws LogicalException;

}
