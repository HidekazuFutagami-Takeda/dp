package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.div.ProdCategory;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.view.ProdSummary;

/**
 * 簡易版品目を取得するためのDAO
 *
 * @author tkawabata
 */
public interface ProdSummaryDao {

	/**
	 * 簡易版品目を取得する。(参照品目を含む)
	 *
	 * @param prodCode 品目固定コード
	 * @return 簡易版品目情報
	 * @throws DataNotFoundException データが見つからない場合にスロー
	 */
	ProdSummary search(String prodCode) throws DataNotFoundException;

	/**
	 * 簡易版品目一覧を取得する。(参照品目を含まない)
	 *
	 * @param sortString ソート条件
	 * @param sales 売上所属(NULL不可)
	 * @param category カテゴリ(NULL可)
	 * @return 簡易版品目一覧
	 * @throws DataNotFoundException データが見つからない場合にスロー
	 */
	List<ProdSummary> searchList(String sortString, Sales sales, ProdCategory[] category) throws DataNotFoundException;

	/**
	 * 簡易版品目一覧を取得する。(参照品目を含まない)
	 *
	 * @param sortString ソート条件
	 * @param sales 売上所属(NULL不可)
	 * @param category カテゴリ(NULL可)
	 * @return 簡易版品目一覧
	 * @throws DataNotFoundException データが見つからない場合にスロー
	 */
	List<ProdSummary> searchList(String sortString, Sales sales, String category) throws DataNotFoundException;

	/**
	 * 簡易版品目一覧を取得する。(参照品目を含まない)
	 *
	 * @param sortString ソート条件
	 * @param sales 売上所属(NULL不可)
	 * @param category カテゴリ(NULL可)
	 * @param isPriorityOnly(true：重点品目のみ、false:重点品・非重点品）
	 * @return 簡易版品目一覧
	 * @throws DataNotFoundException データが見つからない場合にスロー
	 */
	List<ProdSummary> searchList(String sortString, Sales sales, ProdCategory[] category, boolean isPriorityOnly) throws DataNotFoundException;

	/**
	 * 参照品目を含む簡易版品目一覧を取得する。
	 *
	 * @param sortString ソート条件
	 * @param sales 売上所属(NULL不可)
	 * @param category カテゴリ(NULL可)
	 * @return 簡易版品目一覧
	 * @throws DataNotFoundException データが見つからない場合にスロー
	 */
	List<ProdSummary> searchRefList(String sortString, Sales sales, ProdCategory[] category) throws DataNotFoundException;

	/**
	 * 参照品目を含む簡易版品目一覧を取得する。
	 *
	 * @param sortString ソート条件
	 * @param sales 売上所属(NULL不可)
	 * @param category カテゴリ(NULL可)
	 * @return 簡易版品目一覧
	 * @throws DataNotFoundException データが見つからない場合にスロー
	 */
	List<ProdSummary> searchRefList(String sortString, Sales sales, String category) throws DataNotFoundException;
}
