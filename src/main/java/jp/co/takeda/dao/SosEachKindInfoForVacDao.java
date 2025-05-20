package jp.co.takeda.dao;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.view.SosEachKindInfoForVac;

/**
 * (ワ)組織単位の各種登録状況を取得するDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface SosEachKindInfoForVacDao {

	/**
	 * 全特約店Gの各種登録状況を取得する。
	 * 
	 * @return (ワ)組織単位の各種登録状況
	 * @throws DataNotFoundException
	 */
	SosEachKindInfoForVac search() throws DataNotFoundException;

}
