package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.model.ManagePlannedProd;
import jp.co.takeda.model.div.ProdCategory;
import jp.co.takeda.model.div.Sales;

/**
 * 管理の計画対象品目にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface ManagePlannedProdDao {

	/**
	 * ソート順<br>
	 * グループコード 昇順<br>
	 * 統計品名コード(品目)　昇順<br>
	 * 品目固定コード　昇順
	 */
	static final String SORT_STRING = "ORDER BY GROUP_CODE, STAT_PROD_CODE, PROD_CODE";

	/**
	 * 計画対象品目を取得する。
	 * 
	 * @param prodCode 品目固定コード
	 * @return 計画対象品目
	 * @throws DataNotFoundException
	 */
	ManagePlannedProd search(String prodCode) throws DataNotFoundException;

	/**
	 * 計画対象品目のリストを取得する。
	 * 
	 * @param sortString ソート条件
	 * @param sales 売上所属(NULL可)
	 * @param category カテゴリ(NULL可)
	 * @param planLevel 計画立案レベル
	 * @return 計画対象品目のリスト
	 * @throws DataNotFoundException
	 */
	List<ManagePlannedProd> searchList(String sortString, Sales sales, ProdCategory category, ProdPlanLevel planLevel) throws DataNotFoundException;

	/**
	 * 計画対象品目のリスト（DataSeq並び順）を取得する。
	 *
	 * @param category カテゴリ
	 * @return 計画対象品目
	 * @throws DataNotFoundException
	 */
	List<ManagePlannedProd> searchListByCateDataSeq(String category) throws DataNotFoundException;

}
