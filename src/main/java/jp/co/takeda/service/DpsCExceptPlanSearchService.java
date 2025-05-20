package jp.co.takeda.service;

import jp.co.takeda.dto.SupInfoDto;

/**
 * 立案対象外情報検索サービスインターフェース
 *
 * @author nakashima
 *
 */

public interface DpsCExceptPlanSearchService {

	/**
	 * 立案対象外の施設に対する計画を持つか否か
	 * @param sosCd
	 * @return
	 */
	boolean hasExceptPlan(String sosCd);


	/**
	 * 立案対象外施設の情報を、補足情報画面の形に合わせて返す
	 * @param sosCd
	 * @return
	 */
	SupInfoDto getSupplementalInfo(String sosCd, String category);

}

