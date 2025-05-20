package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.JgiJoken;
import jp.co.takeda.model.div.JokenSet;

/**
 * 従業員別条件テーブル当月にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface JgiJokenDAO {

	/**
	 * 従業員番号を元に、条件セットのリストを取得する。
	 * 
	 * @param jgiNo 従業員番号
	 * @return 条件セットのリスト
	 * @throws DataNotFoundException 検索結果0件の場合にスロー
	 */
	List<JgiJoken> searchByJgiNo(Integer jgiNo) throws DataNotFoundException;

	/**
	 * 従業員番号、取得対象の条件セット(候補)を元に、条件セットのリストを取得する。
	 * 
	 * @param jgiNo 従業員番号
	 * @param targetJokenSetList 取得対象の条件セット(候補)
	 * @return 条件セットのリスト
	 * @throws DataNotFoundException 検索結果0件の場合にスロー
	 */
	List<JokenSet> searchJokenSet(Integer jgiNo, JokenSet[] targetJokenSetList) throws DataNotFoundException;
}
