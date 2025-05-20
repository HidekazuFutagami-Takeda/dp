package jp.co.takeda.dao;

import java.util.Date;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.OutputFile;
import jp.co.takeda.security.DpUser;

/**
 * 出力ファイル情報にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface OutputFileDao {

	/**
	 * 出力ファイル情報を取得する。
	 * 
	 * @param seqKey シーケンスキー
	 * @return 出力ファイル情報
	 * @throws DataNotFoundException
	 */
	OutputFile search(Long seqKey) throws DataNotFoundException;

	/**
	 * 出力ファイル情報を登録する。<br>
	 * シーケンスキーは事前に設定する必要あり。
	 * 
	 * @param record 出力ファイル情報
	 * @throws DuplicateException
	 */
	void insert(OutputFile record) throws DuplicateException;

	/**
	 * 出力ファイル情報を登録する。<br>
	 * シーケンスキーは事前に設定する必要あり。
	 * 
	 * @param record 出力ファイル情報
	 * @param dpUser 更新者情報
	 * @throws DuplicateException
	 */
	void insert(OutputFile record, DpUser dpUser) throws DuplicateException;

	/**
	 * 出力ファイル情報を削除する。
	 * 
	 * @param seqKey シーケンスキー
	 * @param upDate 更新日
	 * @return 削除件数
	 */
	int delete(Long seqKey, Date upDate);

	/**
	 * 出力ファイル情報のシーケンスキーを取得する。
	 * 
	 * @return 出力ファイル情報のシーケンスキー
	 */
	Long getSeqKey();

}
