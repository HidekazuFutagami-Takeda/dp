package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.DeliveryResultForVac;

/**
 * ワクチン用納入実績にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface DeliveryResultForVacDao {

	/**
	 * ソート順<br>
	 * 従業員ソート 昇順<br>
	 * 都道府県・市区町村　昇順<br>
	 * 施設ソート　昇順<br>
	 * 特約店ソート　昇順
	 */
	static final String SORT_STRING = "ORDER BY SHOKUSEI_CD, SHOKUSHU_CD, JGI_NO, ADDR_CODE_PREF, ADDR_CODE_CITY, HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE,TMS_TYTEN_CD";

	/**
	 * ソート順２<br>
	 * 都道府県・市区町村　昇順<br>
	 * 施設ソート　昇順<br>
	 * 特約店ソート　昇順
	 */
	static final String SORT_STRING2 = "ORDER BY ADDR_CODE_PREF, ADDR_CODE_CITY, HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE, TMS_TYTEN_CD";

	/**
	 * ソート順３<br>
	 * 品目ソート 昇順<br>
	 * 都道府県・市区町村　昇順<br>
	 * 施設ソート　昇順<br>
	 * 特約店ソート　昇順
	 */
	static final String SORT_STRING3 = "ORDER BY DATA_SEQ, GROUP_CODE, STAT_PROD_CODE, PROD_CODE, ADDR_CODE_PREF, ADDR_CODE_CITY, HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE, TMS_TYTEN_CD";

	/**
	 * 納入実績を取得する。
	 *
	 * @param prodCode 品目固定コード
	 * @param insNo 施設コード
	 * @param tmsTytenCd TMS特約店コード
	 * @return 納入実績
	 * @throws DataNotFoundException
	 */
	DeliveryResultForVac search(String prodCode, String insNo, String tmsTytenCd) throws DataNotFoundException;

	/**
	 * 対象品目の納入実績を取得する。
	 *
	 * @param sortString ソート条件
	 * @param prodCode 品目固定コード
	 * @param jgiNo 従業員番号
	 * @param insNo 施設コード(NULL可)
	 * @return 納入実績のリスト
	 * @throws DataNotFoundException
	 */
	List<DeliveryResultForVac> searchByProd(String sortString, String prodCode, Integer jgiNo, String insNo) throws DataNotFoundException;

	/**
	 * 指定した施設の参照用納入実績を取得する。(Ａ調含)<br>
	 * 品目ソート順
	 *
	 * @param sortString ソート条件
	 * @param insNo 施設コード
	 * @return 納入実績のリスト
	 * @throws DataNotFoundException
	 */
	List<DeliveryResultForVac> searchByInsIncludeA(String sortString, String insNo) throws DataNotFoundException;
}
