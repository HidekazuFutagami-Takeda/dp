package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.ManageWsPlanForVac;

/**
 * 管理のワクチン用特約店別計画にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface ManageWsPlanForVacDao {

	/**
	 * ソート順<br>
	 * 特約店ソート昇順<br>
	 * 
	 */
	static final String SORT_STRING = "ORDER BY TMS_TYTEN_CD";

	/**
	 * ソート順<br>
	 * 品目ソート昇順<br>
	 */
	static final String SORT_STRING2 = "ORDER BY GROUP_CODE, STAT_PROD_CODE, PROD_CODE";

	/**
	 * シーケンスキーを元に管理のワクチン用特約店別計画を取得する。
	 * 
	 * 
	 * @param seqKey シーケンスキー
	 * @return 管理のワクチン用特約店別計画
	 * @throws DataNotFoundException
	 */
	ManageWsPlanForVac search(Long seqKey) throws DataNotFoundException;

	/**
	 * ユニークキーを元に管理のワクチン用特約店別計画を取得する。
	 * 
	 * @param prodCode 品目固定コード
	 * @param tmsTytenCd TMS特約店コード
	 * @return 管理のワクチン用特約店別計画
	 * @throws DataNotFoundException
	 */
	ManageWsPlanForVac searchUk(String prodCode, String tmsTytenCd) throws DataNotFoundException;

	/**
	 * 管理のワクチン用特約店別計画リストを取得する。
	 * 
	 * @param sortString ソート条件
	 * @param prodCode 品目固定コード
	 * @param tmsTytenCd TMS特約店コード（前方一致）
	 * @param allWsFlg 全特約店取得フラグ（true:計画がない特約店も取得、false:計画がある特約店のみ取得)
	 * @return 管理のワクチン用特約店別計画リスト
	 * @throws DataNotFoundException
	 */
	List<ManageWsPlanForVac> searchListByProd(String sortString, String prodCode, String tmsTytenCd, boolean allWsFlg) throws DataNotFoundException;

	/**
	 * 管理のワクチン用特約店別計画リストを取得する。
	 * 
	 * @param sortString ソート条件
	 * @param tmsTytenCd TMS特約店コード
	 * @param allProdFlg 全品目取得フラグ（true:計画がない品目も取得、false:計画がある品目のみ取得)
	 * @return 管理のワクチン用特約店別計画リスト
	 * @throws DataNotFoundException
	 */
	List<ManageWsPlanForVac> searchListByWs(String sortString, String tmsTytenCd, boolean allProdFlg) throws DataNotFoundException;

	/**
	 * 管理のワクチン用特約店別計画を登録する。
	 * 
	 * 
	 * @param manageWsPlanForVac 管理のワクチン用特約店別計画
	 * @param pgId PGID
	 * @throws DuplicateException
	 */
	void insert(ManageWsPlanForVac manageWsPlanForVac, String pgId) throws DuplicateException;

	/**
	 * 管理のワクチン用特約店別計画を更新する。
	 * 
	 * 
	 * @param manageWsPlanForVac 管理のワクチン用特約店別計画
	 * @param pgId PGID
	 * @return 更新件数
	 */
	int update(ManageWsPlanForVac manageWsPlanForVac, String pgId);

}
