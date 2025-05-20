package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.ManageInsPlanForVac;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.Prefecture;

/**
 * 管理のワクチン用施設別計画にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface ManageInsPlanForVacDao {

	/**
	 * ソート順<br>
	 * 施設ソート昇順
	 *
	 */
	static final String SORT_STRING = "ORDER BY INS.ADDR_CODE_PREF, INS.ADDR_CODE_CITY, OINS_HO_INS_TYPE, OINS_RELN_INS_NO, MAIN_INS_NO, INS_HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE";

	/**
	 * ソート順<br>
	 * 品目ソート昇順
	 */
	static final String SORT_STRING2 = "ORDER BY GROUP_CODE, STAT_PROD_CODE, PROD_CODE";

	/**
	 * シーケンスキーを元に管理のワクチン用施設別計画を取得する。
	 *
	 *
	 * @param seqKey シーケンスキー
	 * @return 管理のワクチン用施設別計画
	 * @throws DataNotFoundException
	 */
	ManageInsPlanForVac search(Long seqKey) throws DataNotFoundException;

	/**
	 * ユニークキーを元に管理のワクチン用施設別計画を取得する。
	 *
	 * @param prodCode 品目固定コード
	 * @param insNo 施設コード
	 * @return 管理のワクチン用施設別計画
	 * @throws DataNotFoundException
	 */
	ManageInsPlanForVac searchUk(String prodCode, String insNo) throws DataNotFoundException;

	/**
	 * 管理のワクチン用施設別計画リストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param prodCode 品目固定コード
	 * @param jgiNo 従業員番号
	 * @param activityType 活動区分
	 * @param addrCodePref JIS府県コード
	 * @param addrCodeCity JIS市区町村コード
	 * @param insNo 施設コード
	 * @param oyakoKb 親子区分
	 * @param oyakoKb2 親子区分2
	 * @param tgtInsKb 対象施設区分
	 * @param oyakoKbProdCode 親子区分品目コード
	 * @return 管理のワクチン用施設別計画リスト
	 * @throws DataNotFoundException
	 */
	List<ManageInsPlanForVac> searchListByProd(String sortString, String prodCode, Integer jgiNo, ActivityType activityType, Prefecture addrCodePref, String addrCodeCity,
		String insNo, String oyakoKb,String oyakoKb2, String tgtInsKb, String oyakoKbProdCode) throws DataNotFoundException;

	/**
	 * 管理のワクチン用施設別計画から、実施計画２（B価）のサマリを取得する。
	 *
	 * @param prodCode 品目固定コード
	 * @param jgiNo 従業員番号
	 * @param activityType 活動区分(重点先/一般先)[NULL可]
	 * @return 実施計画２（B価）のサマリ
	 */
	Long searchSumByProd(String prodCode, Integer jgiNo, ActivityType activityType);

	/**
	 * 管理のワクチン用施設別計画リストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param insNo 施設コード
	 * @param category カテゴリ(NULL可)
	 * @param allProdFlg 全品目取得フラグ（true:計画がない品目も取得、false:計画がある品目のみ取得)
	 * @return 管理の施設別計画リスト
	 * @throws DataNotFoundException
	 */
	List<ManageInsPlanForVac> searchListByIns(String sortString, String insNo, boolean allProdFlg) throws DataNotFoundException;

	/**
	 * 管理のワクチン用施設別計画を登録する。
	 *
	 *
	 * @param manageInsPlanForVac 管理のワクチン用施設別計画
	 * @param pgId PGID
	 * @throws DuplicateException
	 */
	void insert(ManageInsPlanForVac manageInsPlanForVac, String pgId) throws DuplicateException;

	/**
	 * 管理のワクチン用施設別計画を更新する。
	 *
	 *
	 * @param manageInsPlanForVac 管理のワクチン用施設別計画
	 * @param pgId PGID
	 * @return 更新件数
	 */
	int update(ManageInsPlanForVac manageInsPlanForVac, String pgId);

}
