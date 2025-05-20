package jp.co.takeda.service;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.AddrScDto;
import jp.co.takeda.dto.AddrSearchResultListDto;

/**
 * JIS府県・市区町村に関するサービスインターフェイス
 * 
 * @author khashimoto
 */
public interface DpmJisCodeSearchService {

	/**
	 * JIS府県・市区町村一覧の検索処理
	 * 
	 * @param scDto JIS府県・市区町村の検索条件DTO
	 * @return JIS府県・市区町村検索の結果格納DTOのリスト
	 * @throws LogicalException
	 */
	AddrSearchResultListDto search(AddrScDto scDto) throws LogicalException;

}
