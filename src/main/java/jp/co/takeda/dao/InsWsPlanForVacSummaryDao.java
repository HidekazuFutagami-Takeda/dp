package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.view.InsWsPlanForVacSummary;

/**
 * 施設特約店別計画の市区町村別サマリーを取得するDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface InsWsPlanForVacSummaryDao {

	/**
	 * ソート順<br>
	 * 市区町村ソート 昇順<br>
	 * 品目ソート 昇順<br>
	 */
	static final String SORT_STRING = "ORDER BY ADDR_CODE_PREF, ADDR_CODE_CITY, GROUP_CODE, STAT_PROD_CODE, PROD_CODE";

	/**
	 * 従業員番号を元に、施設特約店別計画の市区町村別サマリーリストを取得する。
	 * 
	 * @param sortString ソート文字列(SQL)(null可)
	 * @param jgiNo 従業員番号
	 * @return 施設特約店別計画の市区町村別サマリーリスト
	 * @throws DataNotFoundException
	 */
	List<InsWsPlanForVacSummary> searchList(String sortString, Integer jgiNo) throws DataNotFoundException;
}
