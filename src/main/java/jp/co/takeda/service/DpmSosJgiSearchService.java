package jp.co.takeda.service;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dto.SosJgiDto;
import jp.co.takeda.dto.SosJgiListDto;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.SosMst;

/**
 * 組織・従業員に関するサービスインターフェイス
 * 
 * @author khashimoto
 */
public interface DpmSosJgiSearchService {

	/**
	 * 上位組織の組織コードを取得する。
	 * 
	 * @param sosCd 下位組織の組織コード
	 * @return 上位組織の組織コード
	 * @throws DataNotFoundException
	 */
	String searchUpSosCode(String sosCd) throws DataNotFoundException;

	/**
	 * 組織階層・従業員一覧の検索処理
	 * 
	 * @param sosCd 組織コード
	 * @param sosListType 検索パターン
	 * @param sosMaxSrchGetValue 最大検索範囲
	 * @param etcSosFlg 雑組織フラグ
	 * @return 組織・従業員検索の結果格納DTO
	 */
	SosJgiListDto search(String sosCd, String sosListType, String sosMaxSrchGetValue, boolean etcSosFlg);

	/**
	 * 組織・従業員の検索処理
	 * 
	 * @param sosCd 組織コード
	 * @param jgiNo 従業員番号
	 * @param sosMaxSrchGetValue 最大検索範囲
	 * @param etcSosFlg 雑組織フラグ
	 * @return 組織・従業員の結果格納DTO
	 */
	SosJgiDto search(String sosCd, Integer jgiNo, String sosMaxSrchGetValue, boolean etcSosFlg);

	/**
	 * 雑組織を考慮して、組織情報を検索する。
	 * 
	 * @param sosCd 組織コード
	 * @param bumonRank 部門ランク
	 * @return 雑組織ならば真
	 */
	SosMst searchSosMstWithEtcSos(String sosCd, String bumonRank);

	/**
	 * 従業員を取得する。
	 * 
	 * @param jgiNo 従業員番号
	 * @return 従業員情報
	 * @throws DataNotFoundException
	 */
	JgiMst searchJgiMst(Integer jgiNo) throws DataNotFoundException;
}
