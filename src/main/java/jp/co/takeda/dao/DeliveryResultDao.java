package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.DeliveryResult;

/**
 * 納入実績にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface DeliveryResultDao {

	/**
	 * ソート順<br>
	 * 従業員ソート 昇順<br>
	 * 施設ソート　昇順<br>
	 * 特約店ソート　昇順
	 */
	static final String SORT_STRING = "ORDER BY SHOKUSEI_CD, SHOKUSHU_CD, JGI_NO, OINS_HO_INS_TYPE, OINS_RELN_INS_NO, MAIN_INS_NO, INS_HO_INS_TYPE, INS.RELN_INS_NO, INS.RELN_INS_CODE, TMS_TYTEN_CD";

	/**
	 * ソート順２<br>
	 * 施設ソート　昇順<br>
	 * 特約店ソート　昇順
	 */
	static final String SORT_STRING2 = "ORDER BY RELN_INS_NO, RELN_INS_CODE, TMS_TYTEN_CD";

	/**
	 * ソート順３<br>
	 * 品目ソート 昇順<br>
	 * 施設ソート　昇順<br>
	 * 特約店ソート　昇順
	 */
	static final String SORT_STRING3 = "ORDER BY DATA_SEQ, GROUP_CODE, STAT_PROD_CODE, PROD_CODE, RELN_INS_NO, RELN_INS_CODE, TMS_TYTEN_CD";

	/**
	 * 納入実績を取得する。
	 *
	 * @param prodCode 品目固定コード
	 * @param insNo 施設コード
	 * @param tmsTytenCd TMS特約店コード
	 * @return 納入実績
	 * @throws DataNotFoundException
	 */
	DeliveryResult search(String prodCode, String insNo, String tmsTytenCd) throws DataNotFoundException;

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
	List<DeliveryResult> searchByProd(String sortString, String prodCode, Integer jgiNo, String insNo) throws DataNotFoundException;

	/**
	 * 対象品目の納入実績を取得する。(親子施設順考慮)
	 *
	 * @param sortString ソート条件
	 * @param prodCode 品目固定コード
	 * @param jgiNo 従業員番号
	 * @param insNo 施設コード(NULL可)
	 * @return 納入実績のリスト
	 * @throws DataNotFoundException
	 */
	List<DeliveryResult> searchByProdOyako(String sortString, String prodCode, Integer jgiNo, String insNo, String oyakoKb, String oyakoKb2) throws DataNotFoundException;

	/**
	 * 指定した施設の参照用納入実績を取得する。(Ａ調含)<br>
	 * 品目ソート順
	 *
	 * @param sortString ソート条件
	 * @param insNo 施設コード
	 * @param category 品目カテゴリ
	 * @return 納入実績のリスト
	 * @throws DataNotFoundException
	 */
	List<DeliveryResult> searchByInsIncludeA(String sortString, String insNo, String category) throws DataNotFoundException;
}
