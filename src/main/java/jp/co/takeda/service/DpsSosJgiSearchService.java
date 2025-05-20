package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.SosJgiDto;
import jp.co.takeda.dto.SosJgiListDto;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.SosMst;

/**
 * 組織・従業員に関するサービスインターフェイス
 * 
 * @author khashimoto
 */
public interface DpsSosJgiSearchService {

	/**
	 * 組織情報を取得する
	 * 
	 * @param sosCd 組織コード
	 * @return 組織情報
	 * @throws DataNotFoundException
	 */
	SosMst searchSos(String sosCd) throws DataNotFoundException;
		
	/**
	 * 配下組織の一覧を取得する。
	 * 
	 * @param sosCd 組織コード
	 * @param sosListType 取得タイプ
	 * @return 配下の組織リスト
	 * @throws DataNotFoundException
	 */
	List<SosMst> searchUnderSosList(String sosCd, SosListType sosListType) throws DataNotFoundException;

	/**
	 * 上位組織の組織コードを取得する。
	 * 
	 * @param sosCd 下位組織の組織コード
	 * @return 上位組織の組織コード
	 * @throws DataNotFoundException
	 */
	String searchUpSosCode(String sosCd) throws DataNotFoundException;

	/**
	 * 組織階層・従業員一覧を検索する。
	 *  NOTE:2014下期向け支援改定にて、整形対応削除。
	 *  
	 * @param sosCd 組織コード
	 * @param sosListType 検索パターン
	 * @param sosMaxSrchGetValue 最大検索範囲
	 * @return 組織・従業員検索リストの結果格納DTO
	 */
	SosJgiListDto search(String sosCd, String sosListType, String sosMaxSrchGetValue);

	/**
	 * 組織・従業員を検索する。
	 *  NOTE:2014下期向け支援改定にて、整形対応削除。
	 * 
	 * @param sosCd 組織コード
	 * @param jgiNo 従業員番号
	 * @param sosMaxSrchGetValue 最大検索範囲
	 * @return 組織・従業員の結果格納DTO
	 */
	SosJgiDto search(String sosCd, Integer jgiNo, String sosMaxSrchGetValue);
	
	/**
	 * 指定された従業員の施設主担当従業員を取得する。
	 * 
	 * @param jgiNo 従業員番号
	 * @return 従業員リスト
	 */
	List<JgiMst> searchMainMrList(Integer jgiNo);	

	/**
	 * 指定された従業員と所属のチームリーダを取得する。
	 * 
	 * @param jgiNo 従業員番号
	 * @return 従業員リスト
	 */
	List<JgiMst> searchTlMrList(Integer jgiNo);
}
