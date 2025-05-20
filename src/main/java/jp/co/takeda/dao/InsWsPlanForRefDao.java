package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.view.InsWsPlanForRef;

/**
 * 参照用の施設特約店別計画取得DAOインターフェイス
 *
 * @author khashimoto
 */
public interface InsWsPlanForRefDao {

	/**
	 * ソート順<br>
	 * 組織ソート 昇順<br>
	 * 従業員ソート 昇順<br>
	 * 施設ソート 昇順<br>
	 * 特約店ソート 昇順<br>
	 */
	static final String SORT_STRING = "ORDER BY BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE, SHOKUSEI_CD, SHOKUSHU_CD, JGI_NO"
		+ ", HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE, TMS_TYTEN_CD";

	/**
	 * ソート順<br>
	 * 特定施設個別計画フラグ 降順<br>
	 * 施設ソート 昇順<br>
	 * 特約店ソート 昇順<br>
	 *
	 */
	static final String SORT_STRING2 = "ORDER BY SPECIAL_INS_PLAN_FLG DESC, HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE, TMS_TYTEN_CD";

	/**
	 * ソート順<br>
	 * 営業所コード 昇順<br>
	 * 営業所配列 昇順<br>
	 * 対象 昇順<br>
	 * 特約店ソート 昇順<br>
	 */
	static final String SORT_STRING3 = "ORDER BY BR_CODE, DIST_SEQ, DIST_CODE, HO_INS_TYPE, TEAM_CODE, SHOKUSEI_CD, SHOKUSHU_CD, JGI_NO"
		+ ", RELN_INS_NO, RELN_INS_CODE, TMS_TYTEN_CD";

	/**
	 * ソート順<br>
	 * 営業所コード 昇順<br>
	 * 営業所配列 昇順<br>
	 * 対象 昇順<br>
	 * 特約店ソート 昇順<br>
	 */
	static final String SORT_STRING_OYAKO = "ORDER BY BR_CODE, DIST_SEQ, DIST_CODE, HO_INS_TYPE, TEAM_CODE, SHOKUSEI_CD, SHOKUSHU_CD, JGI_NO, OINS_HO_INS_TYPE, OINS_RELN_INS_NO, MAIN_INS_NO, INS_HO_INS_TYPE, INS.RELN_INS_NO, INS.RELN_INS_CODE, TMS_TYTEN_CD";


	/**
	 * <pre>
	 * 施設特約店別計画のリストを取得する。
	 * 参照品目1から3の取得は行わない。
	 * 計画値がなく、時品目実績がある施設・特約店のレコードも取得する。
	 *
	 * ソート順は、下記固定とする。
	 * 特定施設個別計画フラグ 降順
	 * 施設ソート 昇順
	 * 特約店ソート 昇順
	 * </pre>
	 *
	 * @param sortString ソート条件
	 * @param jgiNo 従業員番号
	 * @param plannedProd 計画品目
	 * @param insType 施設出力対象区分
	 * @param resultRefProdCode1 参照品目1
	 * @param resultRefProdCode2 参照品目2
	 * @param resultRefProdCode3 参照品目3
	 * @param oyakokb 親子区分
	 * @param oyakokb2 親子区分2
	 * @param oyakoKbProdCode 親子区分品目コード
	 * @return 施設特約店別計画のリスト
	 * @throws DataNotFoundException
	 */
	List<InsWsPlanForRef> searchList(String sortString, Integer jgiNo, PlannedProd plannedProd, InsType insType, String resultRefProdCode1, String resultRefProdCode2,
		String resultRefProdCode3, String oyakoKb,String oyakoKb2, String oyakoKbProdCode) throws DataNotFoundException;

	/**
	 * 施設特約店別計画のリストを取得する。<br>
	 * 納入実績は立案品目の納入実績のみ取得。
	 *
	 * @param sortString ソート条件
	 * @param sosCd3 組織コード(営業所)
	 * @param prodCode 品目固定コード
	 * @return 施設特約店別計画のリスト
	 * @throws DataNotFoundException
	 */
	List<InsWsPlanForRef> searchListBySosCd(String sortString, String sosCd3, String prodCode) throws DataNotFoundException;

	/**
	 * 施設特約店別計画のリストを取得する。<br>
	 * 納入実績は立案品目の納入実績のみ取得。
	 *
	 * @param sortString ソート条件
	 * @param sosCd3 組織コード(営業所)
	 * @param prodCode 品目固定コード
	 * @param oyakoKb 親子区分
	 * @param oyakoKb2 親子区分2
	 * @return 施設特約店別計画のリスト
	 * @throws DataNotFoundException
	 */
	List<InsWsPlanForRef> searchListBySosCdOyako(String sortString, String sosCd3, String prodCode, String oyakoKb, String oyakoKb2) throws DataNotFoundException;
}
