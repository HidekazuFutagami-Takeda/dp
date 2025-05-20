package jp.co.takeda.dao;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.view.SosStatusSummaryForVac;

/**
 * (ワクチン)組織単位のステータスのサマリーを取得するDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface SosStatusSummaryForVacDao {

	/**
	 * ステータスのサマリーを取得する。
	 * 
	 * @return (ワクチン)組織単位のステータスのサマリー情報
	 * @throws DataNotFoundException
	 */
	SosStatusSummaryForVac search() throws DataNotFoundException;
}
