package jp.co.takeda.dao;

import jp.co.takeda.a.exp.DataNotFoundException;

/**
 * 親子関連情報にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface DpmSyComInsOyakoDao {

	/**
	 * 親子関連情報の数を返す。
	 *
	 * @param oyakoKb 親子区分
	 * @throws DataNotFoundException
	 */
	int searchCount(String oyakoKb) throws DataNotFoundException;
}
