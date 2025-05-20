package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.view.WsPlanStatusSummary;

/**
 * 特約店別計画ステータスのサマリーを取得するDAOインターフェイス
 *
 * @author khashimoto
 */
public interface WsPlanStatusSummaryDao {

	/**
	 * 組織コード(支店)・カテゴリを元に、ステータスのサマリーを取得する。
	 *
	 * @param sosCd2 組織コード(支店)
	 * @param category カテゴリ
	 * @return 特約店別計画ステータスのサマリー情報
	 * @throws DataNotFoundException
	 */
	WsPlanStatusSummary searchShiten(String sosCd2, String category) throws DataNotFoundException;

	/**
	 * ステータスのカテゴリー毎サマリーのリストを取得する。
	 * @return
	 * @throws DataNotFoundException
	 */
	List<WsPlanStatusSummary> searchLIstSumByCategory() throws DataNotFoundException;

}
