package jp.co.takeda.dao;

import java.util.List;
import java.util.Map;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.div.ProdCategory;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.view.DistributionProd;

/**
 * 配分対象品目一覧取得インターフェイス
 *
 * @author nozaki
 */
public interface DistributionProdDao {

	/**
	 * 売上所属、カテゴリ、営業所組織コードから、施設医師別計画 配分対象品目一覧を取得する。
	 *
	 * @param sales 売上所属
	 * @param category カテゴリ
	 * @param sosCd 組織コード
	 * @throws DataNotFoundException
	 */
	List<Map<String, Object>> searchInsDocProdList(Sales sales, ProdCategory category, String sosCd) throws DataNotFoundException;

	/**
	 * 売上所属、カテゴリ、営業所組織コードから、施設特約店別計画 配分対象品目一覧を取得する。
	 *
	 * @param sales 売上所属
	 * @param category カテゴリ
	 * @param sosCd 組織コード
	 * @throws DataNotFoundException
	 */
	List<DistributionProd> searchInsWsProdList(Sales sales, String category, String sosCd) throws DataNotFoundException;

}
