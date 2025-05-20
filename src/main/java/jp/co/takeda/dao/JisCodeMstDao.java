package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dto.AddrScDto;
import jp.co.takeda.model.JisCodeMst;
import jp.co.takeda.model.div.Prefecture;

/**
 * JIS府県・市区町村にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface JisCodeMstDao {

	/**
	 * JIS府県・市区町村を取得する。
	 * 
	 * @param todoufukenCd 都道府県コード
	 * @param shikuchosonCd 市区町村コード
	 * @return JIS府県・市区町村
	 * @throws DataNotFoundException
	 */
	JisCodeMst search(Prefecture todoufukenCd, String shikuchosonCd) throws DataNotFoundException;

	/**
	 * 都道府県コードを元に、市区町村のリストを取得する。
	 * 
	 * @param todoufukenCd 都道府県
	 * @return JIS府県・市区町村のリスト
	 * @throws DataNotFoundException
	 */
	List<JisCodeMst> searchByTodoufukenCd(Prefecture todoufukenCd) throws DataNotFoundException;

	/**
	 * 都道府県コードを元に、市区町村のリストを取得する。
	 * 
	 * @param addrScDto 検索条件DTO
	 * @return JIS府県・市区町村のリスト
	 * @throws DataNotFoundException
	 */
	List<JisCodeMst> searchList(AddrScDto addrScDto) throws DataNotFoundException;

}
