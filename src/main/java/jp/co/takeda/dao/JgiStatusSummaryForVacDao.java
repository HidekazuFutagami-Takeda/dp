package jp.co.takeda.dao;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.view.JgiStatusSummaryForVac;

/**
 * (ワクチン)担当者単位のステータスのサマリーを取得するDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface JgiStatusSummaryForVacDao {

	/**
	 * 従業員番号・カテゴリを基に、ステータスのサマリーを取得する
	 * 
	 * 
	 * @param jgiNo 従業員番号
	 * @return (ワクチン)担当者単位のステータスのサマリー情報
	 * @throws DataNotFoundException
	 */
	JgiStatusSummaryForVac search(Integer jgiNo) throws DataNotFoundException;
}
