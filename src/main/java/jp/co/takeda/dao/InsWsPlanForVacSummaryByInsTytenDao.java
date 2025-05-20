package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.view.InsWsPlanForVacSummaryByInsTyten;

/**
 * 施設特約店別計画の施設別サマリーを取得するDAOインターフェイス
 *
 * @author YTaniguchi
 */
public interface InsWsPlanForVacSummaryByInsTytenDao {

	/**
	 * ソート順<br>
	 * 市区町村ソート 昇順<br>
	 * 品目ソート 昇順<br>
	 */
	static final String SORT_STRING = "ORDER BY ADDR_CODE_PREF, ADDR_CODE_CITY, HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE, TMS_TYTEN_CD, GROUP_CODE, STAT_PROD_CODE, PROD_CODE";

	/**
	 * 従業員番号を元に、施設特約店別計画の施設別サマリーリストを取得する。
	 *
	 * @param sortString ソート文字列(SQL)(null可)
	 * @param jgiNo 従業員番号
	 * @return 施設特約店別計画の施設別サマリーリスト
	 * @throws DataNotFoundException
	 */
	List<InsWsPlanForVacSummaryByInsTyten> searchList(String sortString, Integer jgiNo) throws DataNotFoundException;
}
