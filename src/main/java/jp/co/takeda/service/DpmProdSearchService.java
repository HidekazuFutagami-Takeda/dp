package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.model.ManagePlannedProd;
import jp.co.takeda.model.MasterManagePlannedProd;
import jp.co.takeda.model.div.ProdCategory;
import jp.co.takeda.model.div.Sales;

/**
 * 品目検索サービスインターフェイス
 *
 * @author nozaki
 */
public interface DpmProdSearchService {

	/**
	 * 計画対象品目のリストを取得する。
	 *
	 * @param category カテゴリ(nullの場合、MMP品と仕入品を両方取得)
	 * @param planLevel 計画レベル
	 * @return 品目のリスト
	 */
	List<ManagePlannedProd> searchIyakuProdList(ProdCategory category, ProdPlanLevel planLevel);

	/**
	 * 計画対象品目のリストを取得する。
	 *
	 * @param planLevel 計画レベル
	 * @return 品目のリスト
	 */
	List<ManagePlannedProd> searchWakutinProdList(ProdPlanLevel planLevel);


	/**
	 * 計画対象品目のリストを取得する。
	 *
	 * @param category カテゴリ(nullの場合、全て取得)
	 * @param sales 売上所属
	 * @param planLevel 計画レベル
	 * @return 品目のリスト
	 */
	List<MasterManagePlannedProd> searchProdList(String category, Sales sales, ProdPlanLevel planLevel);

// add Start 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える
	/**
	 * 計画対象品目のリストを取得する。
	 *
	 * @param category カテゴリ(nullの場合、全て取得)
	 * @param sales 売上所属
	 * @param planLevel 計画レベル
	 * @return 品目のリスト
	 */
	List<MasterManagePlannedProd> searchProdDistributorList(String category, Sales sales, ProdPlanLevel planLevel);
// add End 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える

	/**
	 * JRNS用の計画対象品目のリストを取得する。
	 *
	 * @param planLevel 計画レベル
	 * @param ctgList カテゴリリスト
	 * @return 品目のリスト
	 */
	List<MasterManagePlannedProd> searchJrnsProdList(ProdPlanLevel planLevel, List<String> ctgList);

	/**
	 * 品目コードに対応するカテゴリを返す
	 * @param prodCode
	 * @return
	 */
	MasterManagePlannedProd searchPlannedProdByProdCode(String prodCode);
}
