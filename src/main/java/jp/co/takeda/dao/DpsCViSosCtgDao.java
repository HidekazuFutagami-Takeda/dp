package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.view.SosCtg;

/**
 * 計画支援の組織カテゴリビューにアクセスするDAOインターフェイス
 *
 * @author rna8405
 *
 */
public interface DpsCViSosCtgDao {
	/**
	 * カテゴリコード一覧を取得する。
	 * @param sosCd 組織コード
	 * @return カテゴリコード一覧
	 * @throws DataNotFoundException
	 */
	List<SosCtg> search(String sosCd) throws DataNotFoundException;

	/**
	 * 組織コードとカテゴリをキーに検索する。
	 * @param sosCd 組織コード
	 * @param prodCategory カテゴリコード
	 * @return カテゴリコード一覧
	 * @throws DataNotFoundException
	 */
	List<SosCtg> searchBySosCdAndCategory(String sosCd, String prodCategory)  throws DataNotFoundException;
}
