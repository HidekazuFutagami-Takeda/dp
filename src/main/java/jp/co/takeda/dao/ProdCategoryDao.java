package jp.co.takeda.dao;

import jp.co.takeda.a.exp.DataNotFoundException;

/**
 * 品目分類情報にアクセスするDAO
 *
 * @author rna8405
 *
 */
public interface ProdCategoryDao {

	/**
	 * カテゴリーから、品目分類名を取得する
	 * @param category カテゴリー
	 * @param isJrns JRNSか
	 * @param jrnsPcatCd JRNSの品目分類コード
	 * @return 品目分類名
	 * @throws DataNotFoundException
	 */
	String searchProdCategoryName(String category, boolean isJrns, String jrnsPcatCd) throws DataNotFoundException;

}
