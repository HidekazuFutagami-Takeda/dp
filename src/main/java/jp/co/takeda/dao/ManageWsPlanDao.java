package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.ManageWsPlan;
import jp.co.takeda.model.div.InsType;

/**
 * 管理の特約店別計画にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface ManageWsPlanDao {

	/**
	 * ソート順<br>
	 * 特約店ソート昇順<br>
	 * 施設出力対象区分 昇順
	 * 
	 */
	static final String SORT_STRING = "ORDER BY TMS_TYTEN_CD, INS_TYPE";

	/**
	 * ソート順<br>
	 * 品目ソート昇順<br>
	 * 施設出力対象区分 昇順
	 */
	static final String SORT_STRING2 = "ORDER BY GROUP_CODE, STAT_PROD_CODE, PROD_CODE, INS_TYPE";

	/**
	 * シーケンスキーを元に管理の特約店別計画を取得する。
	 * 
	 * 
	 * @param seqKey シーケンスキー
	 * @return 管理の特約店別計画
	 * @throws DataNotFoundException
	 */
	ManageWsPlan search(Long seqKey) throws DataNotFoundException;

	/**
	 * ユニークキーを元に管理の特約店別計画を取得する。
	 * 
	 * @param insType 施設出力対象区分
	 * @param prodCode 品目固定コード
	 * @param tmsTytenCd TMS特約店コード
	 * @return 管理の特約店別計画
	 * @throws DataNotFoundException
	 */
	ManageWsPlan searchUk(InsType insType, String prodCode, String tmsTytenCd) throws DataNotFoundException;

	/**
	 * 管理の特約店別計画リストを取得する。
	 * 
	 * @param sortString ソート条件
	 * @param prodCode 品目固定コード
	 * @param tmsTytenCd TMS特約店コード（前方一致）
	 * @param allWsFlg 全特約店取得フラグ（true:計画がない特約店も取得、false:計画がある特約店のみ取得)
	 * @return 管理の特約店別計画リスト
	 * @throws DataNotFoundException
	 */
	List<ManageWsPlan> searchListByProd(String sortString, String prodCode, String tmsTytenCd, boolean allWsFlg) throws DataNotFoundException;

	/**
	 * 管理の特約店別計画リストを取得する。
	 * 
	 * @param tmsTytenCd TMS特約店コード
	 * @param category カテゴリ(NULL可)
	 * @param allProdFlg 全品目取得フラグ（true:計画がない品目も取得、false:計画がある品目のみ取得)
	 * @param isVaccine ワクチンの場合true
	 * @return 管理の特約店別計画リスト
	 * @throws DataNotFoundException
	 */
	List<ManageWsPlan> searchListByWs(String tmsTytenCd, String category, boolean allProdFlg, boolean isVaccine) throws DataNotFoundException;

	/**
	 * 管理の特約店別計画を登録する。
	 * 
	 * 
	 * @param manageInsWsPlan 管理の特約店別計画
	 * @param pgId PGID
	 * @throws DuplicateException
	 */
	void insert(ManageWsPlan manageWsPlan, String pgId) throws DuplicateException;

	/**
	 * 管理の特約店別計画を更新する。
	 * 
	 * 
	 * @param manageWsPlan 管理の特約店別計画
	 * @param pgId PGID
	 * @return 更新件数
	 */
	int update(ManageWsPlan manageWsPlan, String pgId);

}
