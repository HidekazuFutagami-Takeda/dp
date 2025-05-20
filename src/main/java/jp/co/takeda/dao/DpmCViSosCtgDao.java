package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.view.SosCtg;

/**
 * 組織カテゴリビューにアクセスするDAOインターフェイス
 *
 * @author rna8405
 *
 */
public interface DpmCViSosCtgDao {
	/**
	 * カテゴリコード一覧を取得する。
	 * @param sosCd 組織コード
	 * @return カテゴリコード一覧
	 * @throws DataNotFoundException
	 */
	List<SosCtg> search(String sosCd) throws DataNotFoundException;
}
