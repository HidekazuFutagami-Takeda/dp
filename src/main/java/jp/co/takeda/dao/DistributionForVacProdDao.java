package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.view.DistributionForVacProd;

/**
 * (ワクチン)配分対象品目一覧取得インターフェイス
 * 
 * @author khashimoto
 */
public interface DistributionForVacProdDao {

	/**
	 * (ワクチン)配分対象品目一覧を取得する。<br>
	 * 品目ソート順に取得する。
	 * 
	 * @return (ワクチン)配分対象品目一覧
	 * @throws DataNotFoundException
	 */
	List<DistributionForVacProd> searchList() throws DataNotFoundException;
}
