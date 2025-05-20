package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.view.JgiStatusSummary;

/**
 * 担当者単位のステータスのサマリーを取得するDAOインターフェイス
 *
 * @author khashimoto
 */
public interface JgiStatusSummaryDao {

	/**
	 * 従業員番号・カテゴリを基に、ステータスのサマリーを取得する
	 *
	 * @param jgiNo 従業員番号
	 * @param category カテゴリ
	 * @return 担当者単位のステータスのサマリー情報
	 * @throws DataNotFoundException
	 */
	JgiStatusSummary search(Integer jgiNo, String category) throws DataNotFoundException;

	/**
	 * 組織コードとカテゴリを基に、ステータスのサマリーをリストで取得する
	 * @param sosCd
	 * @param category
	 * @return
	 * @throws DataNotFoundException
	 */
	List<JgiStatusSummary > searchList(String sosCd, String category) throws DataNotFoundException;
}
