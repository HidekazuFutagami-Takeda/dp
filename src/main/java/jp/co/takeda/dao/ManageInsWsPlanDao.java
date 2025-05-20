package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.ManageInsWsPlan;
import jp.co.takeda.model.div.InsType;

/**
 * 管理の施設特約店別計画にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface ManageInsWsPlanDao {

	/**
	 * ソート順<br>
	 * 組織ソート 昇順<br>
	 * 従業員ソート 昇順<br>
	 * 施設ソート 昇順<br>
	 * 特約店ソート 昇順<br>
	 */
	static final String SORT_STRING = "ORDER BY  "
			+ "BR_CODE "
			+ ",DIST_SEQ "
			+ ",DIST_CODE "
			+ ",TEAM_CODE "
			+ ",SHOKUSEI_CD "
			+ ",SHOKUSHU_CD "
			+ ",JGI_NO "
			+ ",OINS_HO_INS_TYPE "
			+ ",OINS_RELN_INS_NO "
			+ ",MAIN_INS_NO "
			+ ",INS_HO_INS_TYPE "
			+ ",RELN_INS_NO "
			+ ",RELN_INS_CODE "
			+ ",TMS_TYTEN_CD ";

	/**
	 * ソート順<br>
	 * 品目ソート昇順
	 */
	static final String SORT_STRING2 = "ORDER BY DATA_SEQ, GROUP_CODE, STAT_PROD_CODE, PROD_CODE";

	/**
	 * シーケンスキーを元に管理の施設特約店別計画を取得する。
	 *
	 *
	 * @param seqKey シーケンスキー
	 * @return 管理の施設特約店別計画
	 * @throws DataNotFoundException
	 */
	ManageInsWsPlan search(Long seqKey) throws DataNotFoundException;

	/**
	 * ユニークキーを元に管理の施設特約店別計画を取得する。
	 *
	 * @param prodCode 品目固定コード
	 * @param insNo 施設コード
	 * @param tmsTytenCd TMS特約店コード
	 * @return 管理の施設特約店別計画
	 * @throws DataNotFoundException
	 */
	ManageInsWsPlan searchUk(String prodCode, String insNo, String tmsTytenCd) throws DataNotFoundException;

	/**
	 * 管理の施設特約店別計画リストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param jgiNo 従業員番号
	 * @param insType 施設出力対象区分
	 * @param relnInsNo 施設内部コード
	 * @param allInsFlg 全施設取得フラグ（true:計画がない施設も取得、false:計画がある施設のみ取得)
	 * @param tmsTytenCd TMS特約店コード(前方一致)
	 * @param oyakoKb 親子区分
	 * @param oyakoKb2 親子区分2
	 * @param tgtInsKb 対象施設区分
	 * @param oyakoKbProdCode 親子区分品目コード
	 * @return 管理の施設特約店別計画リスト
	 * @throws DataNotFoundException
	 */
	List<ManageInsWsPlan> searchListByProd(String sortString, String prodCode, String sosCd3, String sosCd4, Integer jgiNo, InsType insType, String relnInsNo, boolean allInsFlg,
		String tmsTytenCd, String oyakoKb, String oyakoKb2, String tgtInsKb, String oyakoKbProdCode) throws DataNotFoundException;

	/**
	 * 管理の施設特約店別計画リストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param insNo 施設コード
	 * @param tmsTytenCd TMS特約店コード
	 * @param category カテゴリ(NULL可)
	 * @param allProdFlg 全品目取得フラグ（true:計画がない品目も取得、false:計画がある品目のみ取得)
	 * @param tgtInsKb 対象施設区分
	 * @param sales 売上所属
	 * @return 管理の施設特約店別計画リスト
	 * @throws DataNotFoundException
	 */
	List<ManageInsWsPlan> searchListByInsWs(String sortString, String insNo, String tmsTytenCd, String category, boolean allProdFlg, String tgtInsKb, String sales) throws DataNotFoundException;

	/**
	 * 管理の施設特約店別計画を登録する。
	 *
	 *
	 * @param manageInsWsPlan 管理の施設特約店別計画
	 * @param pgId PGID
	 * @throws DuplicateException
	 */
	void insert(ManageInsWsPlan manageInsWsPlan, String pgId) throws DuplicateException;

	/**
	 * 管理の施設特約店別計画を更新する。
	 *
	 *
	 * @param manageInsWsPlan 管理の施設特約店別計画
	 * @param pgId PGID
	 * @return 更新件数
	 */
	int update(ManageInsWsPlan manageInsWsPlan, String pgId);

}
