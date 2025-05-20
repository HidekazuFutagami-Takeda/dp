package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.model.DpsPlannedCtg;
import jp.co.takeda.model.view.SosCtg;

/**
 *
 *
 * @author khashimoto
 */
public interface DpsPlannedCtgDao {

	/**
	 * カテゴリから計画対象カテゴリ領域を一件取得
	 * @param category
	 * @return
	 * @throws DataNotFoundException
	 */
	DpsPlannedCtg search(String category) throws DataNotFoundException;

	/**
	 * 今期の計画対象カテゴリ領域を取得する
	 * @param カテゴリ
	 * @return 今期の計画対象カテゴリ領域
	 * @throws DataNotFoundException
	 */
	List<DpsPlannedCtg> searchTermCtg(String prodCategory) throws DataNotFoundException;

	/**
	 * カテゴリリストから計画対象カテゴリ領域を取得する
	 * @param sosCtgList
	 * @return
	 */
    List<DpsPlannedCtg> searchByCtgList(List<SosCtg> sosCtgList) throws DataNotFoundException;

    /**
     * 対象の計画立案レベルをもつ計画対象カテゴリ領域を取得する
     * @param planLevel 計画立案レベル
     * @return 計画対象カテゴリ領域
     * @throws DataNotFoundException
     */
    List<DpsPlannedCtg> selectByPlanLevel(ProdPlanLevel planLevel) throws DataNotFoundException;
}
