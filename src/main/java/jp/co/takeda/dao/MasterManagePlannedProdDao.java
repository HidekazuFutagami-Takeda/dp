package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.model.MasterManagePlannedProd;
import jp.co.takeda.model.div.Sales;

/**
 * 管理の計画対象品目にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface MasterManagePlannedProdDao {

	/**
	 * ソート順<br>
	 * 計画管理汎用マスタ.表示順　昇順
	 */
	static final String SORT_STRING = "ORDER BY DATA_SEQ";

	/**
	 * 計画対象品目を取得する。
	 *
	 * @param prodCode 品目固定コード
	 * @return 計画対象品目
	 * @throws DataNotFoundException
	 */
	MasterManagePlannedProd search(String prodCode) throws DataNotFoundException;

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
	List<MasterManagePlannedProd> searchList(String sortString, Sales sales, String category, ProdPlanLevel planLevel) throws DataNotFoundException;

// add Start 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える
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
	List<MasterManagePlannedProd> searchDistributorList(String sortString, Sales sales, String category, ProdPlanLevel planLevel) throws DataNotFoundException;
// add End 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える

	/**
	 * JRNS用の計画対象品目のリストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param planLevel 計画立案レベル
	 * @param ctgList カテゴリリスト
	 * @return 計画対象品目のリスト
	 * @throws DataNotFoundException
	 */
	List<MasterManagePlannedProd> searchJrnsList(String sortString, ProdPlanLevel planLevel, List<String> ctgList) throws DataNotFoundException;

	/**
	 * 計画対象品目のリストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param sales 売上所属(NULL可)
	 * @param category カテゴリ(NULL可)
	 * @param isOncSos ONC組織か(NULL可)
	 * @param planLevelInsDoc 重点品のみ取得(NULL可)
	 * @return 計画対象品目のリスト
	 * @throws DataNotFoundException
	 */
	List<MasterManagePlannedProd> searchProdList(String sortString, Sales sales, String category, Boolean planLevelInsDoc) throws DataNotFoundException;
}
