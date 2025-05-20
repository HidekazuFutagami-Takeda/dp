package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.JisCodeMstResultDto;
import jp.co.takeda.dto.JisCodeMstScDto;

/**
 * JIS府県・市区町村の検索に関するサービスインタフェース
 * 
 * @author stakeuchi
 */
public interface DpsJisCodeMstSearchService {

	/**
	 * JIS府県・市区町村の検索結果DTOを取得する。
	 * 
	 * @param scDto JIS府県・市区町村の検索条件用DTO
	 * @return JIS府県・市区町村の検索結果用DTOリスト
	 * @throws LogicalException 検索結果が0件だった場合
	 */
	List<JisCodeMstResultDto> searchJisCodeMstList(JisCodeMstScDto scDto) throws LogicalException;

}
