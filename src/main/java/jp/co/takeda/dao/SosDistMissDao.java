package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.SosDistMiss;
import jp.co.takeda.security.DpUser;

/**
 * 組織別配分ミス情報にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface SosDistMissDao {

	/**
	 * ソート順<br>
	 * 品目ソート順<br>
	 * 施設出力対象区分　昇順
	 */
	static final String SORT_STRING = "ORDER BY DATA_SEQ, GROUP_CODE, STAT_PROD_CODE, PROD_CODE, INS_TYPE";

	/**
	 * ソート順<br>
	 * 組織ソート順<br>
	 * 品目ソート順<br>
	 * 施設出力対象区分　昇順
	 */
	static final String SORT_STRING2 = "ORDER BY BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE," + "DATA_SEQ, GROUP_CODE, STAT_PROD_CODE, PROD_CODE, INS_TYPE";

	/**
	 * 組織別配分ミス情報を取得する。
	 *
	 * @param seqKey シーケンスキー
	 * @return 組織別配分ミス情報
	 * @throws DataNotFoundException
	 */
	SosDistMiss search(Long seqKey) throws DataNotFoundException;

	/**
	 * 組織別配分ミス情報のリストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param outputFileId 出力ファイル情報ID
	 * @return 組織別配分ミス情報リスト
	 * @throws DataNotFoundException
	 */
	List<SosDistMiss> searchList(String sortString, Long outputFileId) throws DataNotFoundException;

	/**
	 * 組織別配分ミス情報を登録する。
	 *
	 * @param record 組織別配分ミス情報
	 * @throws DuplicateException
	 */
	void insert(SosDistMiss record) throws DuplicateException;

	/**
	 * 組織別配分ミス情報を登録する。
	 *
	 * @param record 組織別配分ミス情報
	 * @param dpUser 更新者情報
	 * @throws DuplicateException
	 */
	void insert(SosDistMiss record, DpUser dpUser) throws DuplicateException;

	/**
	 * 組織別配分ミス情報を削除する。
	 *
	 * @param outputFileId 出力ファイル情報ID
	 * @return 削除件数
	 */
	int delete(Long outputFileId);
}
