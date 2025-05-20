package jp.co.takeda.dao;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.Announce;
import jp.co.takeda.security.DpUser;

/**
 * お知らせ情報にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface AnnounceDao {

	/**
	 * ソート順<br>
	 * 登録日 降順
	 */
	static final String SORT_STRING = "ORDER BY IS_DATE DESC";

	/**
	 * お知らせ情報を取得する。
	 * 
	 * @param seqKey シーケンスキー
	 * @return お知らせ情報
	 * @throws DataNotFoundException
	 */
	Announce search(Long seqKey) throws DataNotFoundException;

	/**
	 * 従業員番号を元に、お知らせ情報のリストを取得する。
	 * 
	 * @param sortString ソート文字列(SQL)(null可)
	 * @param jgiNo 従業員番号
	 * @return お知らせ情報のリスト
	 * @throws DataNotFoundException
	 */
	List<Announce> searchByJgiNo(String sortString, Integer jgiNo) throws DataNotFoundException;

	/**
	 * 指定出力ファイルIDを持つお知らせ情報の数を取得する。
	 * 
	 * @param outputFileId 出力ファイルID
	 * @return 指定出力ファイルIDを持つお知らせ情報の数
	 * @throws DataNotFoundException
	 */
	Integer countAnnounce(Long outputFileId) throws DataNotFoundException;

	/**
	 * お知らせ情報を登録する。
	 * 
	 * @param record お知らせ情報
	 * @param dpUser 実行者情報
	 * @throws DuplicateException
	 */
	void insert(Announce record, DpUser dpUser) throws DuplicateException;

	/**
	 * お知らせ情報を更新する。
	 * 
	 * @param record お知らせ情報
	 * @return 更新件数
	 */
	int update(Announce record);

	/**
	 * お知らせ情報を削除する。
	 * 
	 * @param seqKey シーケンスキー
	 * @param upDate 更新日
	 * @return 削除件数
	 */
	int delete(Long seqKey, Date upDate);
}
