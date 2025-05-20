package jp.co.takeda.dao;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.view.JgiEachKindInfoForVac;

/**
 * (ワクチン)担当者単位の各種登録状況を取得するDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface JgiEachKindInfoForVacDao {

	/**
	 * 従業員番号を基に、各種登録状況を取得する
	 * 
	 * @param jgiNo 従業員番号
	 * @return (ワクチン)担当者単位の各種登録状況
	 * @throws DataNotFoundException
	 */
	JgiEachKindInfoForVac search(Integer jgiNo) throws DataNotFoundException;
}
