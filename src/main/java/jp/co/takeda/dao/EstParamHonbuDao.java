package jp.co.takeda.dao;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.EstParamHonbu;

/**
 * 試算パラメータ(本部)にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface EstParamHonbuDao {

	/**
	 * 試算パラメータ(本部)を取得する。
	 * 
	 * @param seqKey シーケンスキー
	 * @return 試算パラメータ(本部)
	 */
	EstParamHonbu search(Long seqKey) throws DataNotFoundException;

	/**
	 * 品目固定コードを指定して、試算パラメータ(本部)を取得する。
	 * 
	 * @param prodCode 品目固定コード
	 * @return 試算パラメータ(本部)
	 */
	EstParamHonbu search(String prodCode) throws DataNotFoundException;
}
