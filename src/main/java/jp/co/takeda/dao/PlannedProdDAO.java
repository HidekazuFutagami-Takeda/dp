package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.Sales;

/**
 * 計画支援の計画対象品目にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface PlannedProdDAO {

	/**
	 * ソート順<br>
	 * 計画支援汎用マスタ(sort_prod) 昇順
	 * グループコード 昇順<br>
	 * 統計品名コード(品目)　昇順<br>
	 * 品目固定コード　昇順
	 */
	static final String SORT_STRING = "ORDER BY DATA_SEQ, GROUP_CODE, STAT_PROD_CODE, PROD_CODE";

	/**
	 * ソート順<br>
	 * 計画支援汎用マスタ(sort_prod) 昇順
	 */
	static final String SORT_STRING2 = "ORDER BY DATA_SEQ";

	/**
	 * 計画対象品目を取得する。
	 *
	 * @param prodCode 品目固定コード
	 * @return 計画対象品目
	 * @throws DataNotFoundException
	 */
	PlannedProd search(String prodCode) throws DataNotFoundException;

//	/**
//	 * ONCグループ：計画対象品目を取得する。
//	 *
//	 * @param prodCode 品目固定コード
//	 * @return 計画対象品目
//	 * @throws DataNotFoundException
//	 */
//	PlannedProd searchForOncSos(String prodCode) throws DataNotFoundException;

	/**
	 * 計画対象品目を統計品名コードで取得する。
	 *
	 * @param statProdCode 統計品名コード
	 * @return 計画対象品目
	 * @throws DataNotFoundException
	 */
	PlannedProd searchBystatPd(String statProdCode) throws DataNotFoundException;

	/**
	 * 計画対象品目を薬効市場コードで取得する。
	 *
	 * @param yakkouSijouCode 薬効市場コード
	 * @return 計画対象品目
	 * @throws DataNotFoundException
	 */
	PlannedProd searchByYakkouSijouCode(String yakkouSijouCode) throws DataNotFoundException;

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
	List<PlannedProd> searchList(String sortString, Sales sales, String category, Boolean planLevelInsDoc) throws DataNotFoundException;

// add Start 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える
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
	List<PlannedProd> searchDistributorList(String sortString, Sales sales, String category, Boolean planLevelInsDoc) throws DataNotFoundException;
// add End 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える

	/**
	 * 計画立案レベルを考慮して、計画対象品目のリストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param sales 売上所属(NULL可)
	 * @param category カテゴリ(NULL可)
	 * @param planLevel 計画立案レベル
	 * @return 計画対象品目のリスト
	 * @throws DataNotFoundException
	 */
	List<PlannedProd> searchListByPlanLevel(String sortString, Sales sales, String category, ProdPlanLevel planLevel) throws DataNotFoundException;

// add Start 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
	/**
	 * カテゴリの計画対象品目のリストを取得する。
	 *
	 * @param category カテゴリ
	 * @return 計画対象品目のリスト
	 * @throws DataNotFoundException
	 */
	List<PlannedProd> selectListOfCategory(String category) throws DataNotFoundException;
// add End 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能

}
