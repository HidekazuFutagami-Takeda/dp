package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.ProdCategory;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.view.ProdSummary;

/**
 * 計画支援の品目検索サービスインターフェイス
 *
 * @author nozaki
 */
public interface DpsProdSearchService {

	/**
	 * 計画対象品目のリストを取得する。
	 *
	 * @param category カテゴリ(nullの場合、MMP品と仕入品を両方取得)
	 * @param includeRefFlg 参照用集計品目を含む場合に真
	 * @return 品目のリスト
	 */
	List<ProdSummary> searchIyakuProdList(ProdCategory[] category, boolean includeRefFlg);

	/**
	 * 計画対象品目のリストを取得する。
	 *
	 * @param category カテゴリ
	 * @param includeRefFlg 参照用集計品目を含む場合に真
	 * @return 品目のリスト
	 */
	List<ProdSummary> searchIyakuProdList(String category, boolean includeRefFlg);

	/**
	 * 計画対象品目（重点品）のリストを取得する（参照品目を含まない）。
	 *
	 * @param category カテゴリ(nullの場合、MMP品と仕入品を両方取得)
	 * @param isPriorityOnly(true：重点品目のみ、false:重点品・非重点品）
	 * @return 品目のリスト
	 */
	List<ProdSummary> searchIyakuPriorityProdList(ProdCategory[] category, boolean isPriorityOnly);

	/**
	 * 計画対象品目のリストを取得する。
	 *
	 * @param includeRefFlg 参照用集計品目を含む場合に真
	 * @return 品目のリスト
	 */
	List<ProdSummary> searchWakutinProdList(boolean includeRefFlg);

	/**
	 * 計画対象品目のリストを取得する。
	 *
	 * @param category カテゴリ(nullの場合、全て取得)
	 * @param sales 売上所属
	 * @param planLevel 計画レベル
	 * @return 品目のリスト
	 */
	List<PlannedProd> searchProdList(String category, Sales sales, ProdPlanLevel planLevel);

	/**
	 * 計画対象品目を取得する。
	 *
	 * @param prodCode 品目固定コード
	 * @return 品目の値
	 */
	PlannedProd searchProd(String prodCode);

}
