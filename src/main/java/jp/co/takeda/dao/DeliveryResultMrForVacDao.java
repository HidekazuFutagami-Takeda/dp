package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dto.DeliveryResultForVacSosSummaryDto;
import jp.co.takeda.model.DeliveryResultMrForVac;

/**
 * ワクチン用担当者別納入実績にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface DeliveryResultMrForVacDao {

	/**
	 * ソート順<br>
	 * 組織ソート 昇順<br>
	 * 従業員ソート 昇順<br>
	 */
	static final String SORT_STRING = "ORDER BY BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE, SHOKUSEI_CD, SHOKUSHU_CD, JGI_NO";

	/**
	 * ソート順<br>
	 * 品目ソート 昇順<br>
	 * 組織ソート 昇順<br>
	 * 従業員ソート 昇順<br>
	 */
	static final String SORT_STRING2 = "ORDER BY GROUP_CODE, STAT_PROD_CODE, PROD_CODE, BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE, SHOKUSEI_CD, SHOKUSHU_CD,JGI_NO";

	/**
	 * ソート順<br>
	 * 組織ソート 昇順<br>
	 * 従業員ソート 昇順<br>
	 * 品目ソート 昇順<br>
	 */
	static final String SORT_STRING3 = "ORDER BY BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE, SHOKUSEI_CD, SHOKUSHU_CD, JGI_NO, GROUP_CODE, STAT_PROD_CODE, PROD_CODE";

	/**
	 * 従業員番号、品目固定コードを指定して納入実績を取得する。
	 * 
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @return 納入実績
	 * @throws DataNotFoundException
	 */
	DeliveryResultMrForVac search(Integer jgiNo, String prodCode) throws DataNotFoundException;

	/**
	 * 対象品目の納入実績一覧を取得する。
	 * 
	 * @param sortString ソート条件
	 * @param prodCode 品目固定コード(NULL可)
	 * @param sosCd3 組織コード(エリア特約店G)(NULL可)
	 * @return 担当者単位の納入実績(サマリー)のリスト
	 * @throws DataNotFoundException
	 */
	List<DeliveryResultMrForVac> searchByProd(String sortString, String prodCode, String sosCd3) throws DataNotFoundException;

	/**
	 * 対象組織の納入実績一覧を取得する。
	 * 
	 * @param sortString ソート条件
	 * @param sosCd3 組織コード(エリア特約店G)(NULL可)
	 * @return 担当者単位の納入実績(サマリー)のリスト
	 * @throws DataNotFoundException
	 */
	List<DeliveryResultMrForVac> searchBySos(String sortString, String sosCd3) throws DataNotFoundException;

	/**
	 * 対象品目・エリア特約店Gの納入実績サマリーを取得する。<br>
	 * 
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(エリア特約店G)
	 * @return 納入実績サマリー
	 * @throws DataNotFoundException
	 */
	DeliveryResultForVacSosSummaryDto searchSosSummary(String prodCode, String sosCd3) throws DataNotFoundException;

}
