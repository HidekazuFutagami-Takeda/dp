package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.InsMrDto;
import jp.co.takeda.dto.InsMstResultDto;
import jp.co.takeda.dto.InsMstScDto;

/**
 * 施設情報に関するサービスインターフェイス
 * 
 * @author khashimoto
 */
public interface DpmInsMstSearchService {

	/**
	 * 施設一覧の検索処理
	 * 
	 * @param scDto 施設情報の検索条件DTO
	 * @return 施設情報検索の結果格納DTOのリスト
	 * @throws LogicalException
	 */
	List<InsMstResultDto> search(InsMstScDto scDto) throws LogicalException;

	/**
	 * 施設の検索処理
	 * 
	 * @param insNo 施設コード
	 * @return 施設情報検索の結果格納DTO
	 * @throws LogicalException
	 */
	InsMstResultDto search(String insNo, Integer jgiNo) throws LogicalException;

	/**
	 * 施設の検索処理
	 * 
	 * @param insNo 施設コード
	 * @return 施設情報検索の結果格納DTO
	 * @throws LogicalException
	 */
	InsMstResultDto search(String insNo) throws LogicalException;

	/**
	 * 施設リストの検索処理
	 * 
	 * @param insMrDtoList 施設コード/従業員番号リスト
	 * @return 施設情報検索の結果格納DTOのリスト
	 * @throws LogicalException
	 */
	List<InsMstResultDto> search(List<InsMrDto> insMrDtoList) throws LogicalException;

}
