package jp.co.takeda.dao;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.ProdInsInfoKanri;

/**
 * 品目施設情報管理にアクセスするためのDAOインターフェース
 * 
 * @author khashimoto
 */
public interface ProdInsInfoKanriDao {

	/**
	 * 品目施設情報管理を品目コード、施設固定コードで取得する
	 * 
	 * @param prodCode
	 * @param insNo
	 * @return 品目施設情報管理
	 * @throws DataNotFoundException
	 */
	ProdInsInfoKanri searchByInsNo(String prodCode, String insNo) throws DataNotFoundException;

}
