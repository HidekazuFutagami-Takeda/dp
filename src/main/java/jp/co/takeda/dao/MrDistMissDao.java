package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.MrDistMiss;
import jp.co.takeda.security.DpUser;

/**
 * 担当者別配分ミス情報にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface MrDistMissDao {

	/**
	 * ソート順<br>
	 * 組織ソート順<br>
	 * 従業員ソート順<br>
	 * 品目ソート順<br>
	 * 施設出力対象区分　昇順
	 */
	static final String SORT_STRING = "ORDER BY BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE, SHOKUSEI_CD, SHOKUSHU_CD, JGI_NO" + ", DATA_SEQ, GROUP_CODE, STAT_PROD_CODE, PROD_CODE, INS_TYPE";

	/**
	 * 担当者別配分ミス情報を取得する。
	 *
	 * @param seqKey シーケンスキー
	 * @return 担当者別配分ミス情報
	 * @throws DataNotFoundException
	 */
	MrDistMiss search(Long seqKey) throws DataNotFoundException;

	/**
	 * 担当者別配分ミス情報のリストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param outputFileId 出力ファイル情報ID
	 * @return 担当者別配分ミス情報リスト
	 * @throws DataNotFoundException
	 */
	List<MrDistMiss> searchList(String sortString, Long outputFileId) throws DataNotFoundException;

	/**
	 * 担当者別配分ミス情報を登録する。
	 *
	 * @param record 担当者別配分ミス情報
	 * @throws DuplicateException
	 */
	void insert(MrDistMiss record) throws DuplicateException;

	/**
	 * 担当者別配分ミス情報を登録する。
	 *
	 * @param record 担当者別配分ミス情報
	 * @param dpUser 更新者情報
	 * @throws DuplicateException
	 */
	void insert(MrDistMiss record, DpUser dpUser) throws DuplicateException;

	/**
	 * 担当者別配分ミス情報を削除する。
	 *
	 * @param outputFileId 出力ファイル情報ID
	 * @return 削除件数
	 */
	int delete(Long outputFileId);
}
