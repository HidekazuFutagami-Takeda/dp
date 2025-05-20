package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.view.InsWsPlanForVacForRef;

/**
 * 参照用の(ワクチン)施設特約店別計画取得DAOインターフェイス
 *
 * @author khashimoto
 */
public interface InsWsPlanForVacForRefDao {

	/**
	 * ソート順<br>
	 * 市区町村ソート 昇順<br>
	 * 特定施設個別計画フラグ 降順<br>
	 * 施設ソート 昇順<br>
	 * 特約店ソート 昇順<br>
	 *
	 */
	static final String SORT_STRING = "ORDER BY ADDR_CODE_PREF, ADDR_CODE_CITY, HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE, TMS_TYTEN_CD";

	/**
	 * ソート順<br>
	 * 市区町村ソート 昇順<br>
	 * 特定施設個別計画フラグ 降順<br>
	 * 施設ソート 昇順<br>
	 * 特約店ソート 昇順<br>
	 *
	 */
	static final String SORT_STRING_OYAKO = "ORDER BY ADDR_CODE_PREF, ADDR_CODE_CITY, OINS_HO_INS_TYPE, OINS_RELN_INS_NO, MAIN_INS_NO, INS_HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE, INS.HO_INS_TYPE, INS.RELN_INS_NO, INS.RELN_INS_CODE, TMS_TYTEN_CD";

	/**
	 * (ワクチン)施設特約店別計画のリストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param activityType 活動区分
	 * @param resultRefProdCode1 参照品目1
	 * @param resultRefProdCode2 参照品目2
	 * @param resultRefProdCode3 参照品目3
	 * @return (ワクチン)施設特約店別計画のリスト
	 * @throws DataNotFoundException
	 */
	List<InsWsPlanForVacForRef> searchList(String sortString, Integer jgiNo, String prodCode, ActivityType activityType, String resultRefProdCode1, String resultRefProdCode2,
			String resultRefProdCode3, InsType insType, String oyakoKb,String oyakoKb2) throws DataNotFoundException;
}
