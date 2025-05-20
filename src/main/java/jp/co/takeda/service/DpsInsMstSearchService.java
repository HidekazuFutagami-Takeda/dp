package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.DpsInsMstScDto;
import jp.co.takeda.dto.InsMstResultDto;

/**
 * 施設情報に関するサービスインターフェイス
 *
 * @author khashimoto
 */
public interface DpsInsMstSearchService {

	/**
	 * 施設一覧の検索処理
	 *
	 * @param scDto 施設情報の検索条件DTO
	 * @param category カテゴリコード
	 * @return 施設情報検索の結果格納DTOのリスト
	 * @throws LogicalException
	 */
	List<InsMstResultDto> search(DpsInsMstScDto scDto, String category) throws LogicalException;

	/**
	 * 施設の検索処理
	 *
	 * @param insNo 施設コード
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目コード
	 * @return 施設情報検索の結果格納DTO
	 * @throws LogicalException
	 */
	InsMstResultDto search(String insNo, Integer jgiNo, String prodCode, String category) throws LogicalException;

	/**
	 * 指定された施設に関連する親子施設の検索処理
	 *
	 * @param insNo 施設コード
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目コード
	 * @return
	 * @throws LogicalException
	 */
	List<InsMstResultDto> searchFamilyIns(String insNo, Integer jgiNo, String prodCode) throws LogicalException;

}
