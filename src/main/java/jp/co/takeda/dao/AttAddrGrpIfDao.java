package jp.co.takeda.dao;

import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.AttAddrGrpIf;

/**
 * Attention宛先グループ指定I/FにアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface AttAddrGrpIfDao {

	/**
	 * Attention宛先グループ指定I/Fを登録する。
	 * 
	 * @param record Attention宛先グループ指定I/F
	 * @throws DuplicateException
	 */
	void insert(AttAddrGrpIf record) throws DuplicateException;
}
