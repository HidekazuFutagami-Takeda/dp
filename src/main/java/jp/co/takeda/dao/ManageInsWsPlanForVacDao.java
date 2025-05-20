package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.ManageInsWsPlanForVac;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.Prefecture;

/**
 * 管理のワクチン用施設特約店別計画にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface ManageInsWsPlanForVacDao {

	/**
	 * ソート順<br>
	 * 市区町村ソート 昇順<br>
	 * 施設ソート 昇順<br>
	 * 特約店ソート 昇順<br>
	 *
	 */
	static final String SORT_STRING = "ORDER BY OINS_HO_INS_TYPE, OINS_RELN_INS_NO, MAIN_INS_NO, INS_HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE";

	/**
	 * ソート順<br>
	 * 品目ソート昇順
	 */
	static final String SORT_STRING2 = "ORDER BY GROUP_CODE, STAT_PROD_CODE, PROD_CODE";

	/**
	 * シーケンスキーを元に管理のワクチン用施設特約店別計画を取得する。
	 *
	 *
	 * @param seqKey シーケンスキー
	 * @return 管理のワクチン用施設特約店別計画
	 * @throws DataNotFoundException
	 */
	ManageInsWsPlanForVac search(Long seqKey) throws DataNotFoundException;

	/**
	 * ユニークキーを元に管理のワクチン用施設特約店別計画を取得する。
	 *
	 * @param prodCode 品目固定コード
	 * @param insNo 施設コード
	 * @param tmsTytenCd TMS特約店コード
	 * @return 管理のワクチン用施設特約店別計画
	 * @throws DataNotFoundException
	 */
	ManageInsWsPlanForVac searchUk(String prodCode, String insNo, String tmsTytenCd) throws DataNotFoundException;

	/**
	 * 管理のワクチン用施設特約店別計画リストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(エリア特約店G)
	 * @param jgiNo 従業員番号
	 * @param activityType 活動区分
	 * @param insNo 施設コード
	 * @param addrCodePref 都道府県コード
	 * @param addrCodeCity 市区町村コード
	 * @param tmsTytenCd TMS特約店コード(前方一致)
	 * @param oyakoKb 親子区分
	 * @param oyakoKb2 親子区分2
	 * @param tgtInsKb 対象施設区分
	 * @param oyakoKbProdCode 親子区分品目コード
	 * @return 管理のワクチン用施設特約店別計画リスト
	 * @throws DataNotFoundException
	 */
	List<ManageInsWsPlanForVac> searchListByProd(String sortString, String prodCode, String sosCd3, Integer jgiNo, ActivityType activityType, Prefecture addrCodePref,
		String addrCodeCity, String insNo, String tmsTytenCd, String oyakoKb,String oyakoKb2, String tgtInsKb, String oyakoKbProdCode) throws DataNotFoundException;

	/**
	 * 管理のワクチン用施設特約店別計画リストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param insNo 施設コード
	 * @param tmsTytenCd TMS特約店コード
	 * @param allProdFlg 全品目取得フラグ（true:計画がない品目も取得、false:計画がある品目のみ取得)
	 * @return 管理のワクチン用施設特約店別計画リスト
	 * @throws DataNotFoundException
	 */
	List<ManageInsWsPlanForVac> searchListByInsWs(String sortString, String insNo, String tmsTytenCd, boolean allProdFlg) throws DataNotFoundException;

	/**
	 * 管理のワクチン用施設特約店別計画を登録する。
	 *
	 *
	 * @param manageInsWsPlanForVac 管理のワクチン用施設特約店別計画
	 * @param pgId PGID
	 * @throws DuplicateException
	 */
	void insert(ManageInsWsPlanForVac manageInsWsPlanForVac, String pgId) throws DuplicateException;

	/**
	 * 管理のワクチン用施設特約店別計画を更新する。
	 *
	 *
	 * @param manageInsWsPlanForVac 管理のワクチン用施設特約店別計画
	 * @param pgId PGID
	 * @return 更新件数
	 */
	int update(ManageInsWsPlanForVac manageInsWsPlanForVac, String pgId);

}
