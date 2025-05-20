package jp.co.takeda.dao;

import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.AttBaseIf;

/**
 * Attention基本I/FにアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface AttBaseIfDao {

	/**
	 * Attention基本I/Fを登録する。
	 * 
	 * @param record Attention基本I/F
	 * @throws DuplicateException
	 */
	void insert(AttBaseIf record) throws DuplicateException;

	/**
	 * Attention基本I/Fのシーケンスキーを取得する。
	 * 
	 * @return シーケンスキー
	 */
	Integer getSeqKey();
}
