package jp.co.takeda.dao;

import java.util.Date;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.TeamInputStatus;

/**
 * チーム別入力状況にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface TeamInputStatusDao {

	/**
	 * チーム別入力状況を取得する。
	 * 
	 * @param seqKey シーケンスキー
	 * @return チーム別入力状況
	 * @throws DataNotFoundException
	 */
	TeamInputStatus search(Long seqKey) throws DataNotFoundException;

	/**
	 * 組織コード、品目固定コードを元に、チーム別入力状況を取得する。
	 * 
	 * @param sosCd 組織コード
	 * @param prodCode 品目固定コード
	 * @return チーム別入力状況
	 * @throws DataNotFoundException
	 */
	TeamInputStatus search(String sosCd, String prodCode) throws DataNotFoundException;

	/**
	 * チーム別入力状況を登録する。
	 * 
	 * @param record チーム別入力状況
	 * @throws DuplicateException
	 */
	void insert(TeamInputStatus record) throws DuplicateException;

	/**
	 * チーム別入力状況を更新する。
	 * 
	 * @param record チーム別入力状況
	 * @return 更新件数
	 */
	int update(TeamInputStatus record);

	/**
	 * チーム別入力状況を削除する。
	 * 
	 * @param seqKey シーケンスキー
	 * @param upDate 更新日
	 * @return 削除件数
	 */
	int delete(Long seqKey, Date upDate);
}
