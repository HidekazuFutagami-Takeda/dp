package jp.co.takeda.dao;

import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.AttLinksIf;

/**
 * Attention外部リンク情報I/FにアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface AttLinksIfDao {

	/**
	 * Attention外部リンク情報I/Fを登録する。
	 * 
	 * @param record Attention外部リンク情報I/F
	 * @throws DuplicateException
	 */
	void insert(AttLinksIf record) throws DuplicateException;
}
