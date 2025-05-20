package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.model.PlannedCtg;
import jp.co.takeda.model.view.SosCtg;

/**
 * 計画対象カテゴリ領域にアクセスするDAOインターフェイス
 *
 * @author yyoshino
 */
public interface PlannedCtgDao {

	/**
	 * カテゴリから計画対象カテゴリ領域を一件取得
	 * @param category
	 * @return
	 * @throws DataNotFoundException
	 */
	PlannedCtg search(String category) throws DataNotFoundException;

	/**
	 * 今期の計画対象カテゴリ領域を取得する
	 * @param カテゴリ
	 * @return 今期の計画対象カテゴリ領域
	 * @throws DataNotFoundException
	 */
	List<PlannedCtg> searchTermCtg(String prodCategory) throws DataNotFoundException;

	/**
	 * カテゴリリストから計画対象カテゴリ領域を取得する
	 * @param sosCtgList
	 * @return
	 */
    List<PlannedCtg> searchByCtgList(List<SosCtg> sosCtgList) throws DataNotFoundException;

    /**
     * 対象の計画立案レベルをもつ計画対象カテゴリ領域を取得する
     * @param planLevel 計画立案レベル
     * @return 計画対象カテゴリ領域
     * @throws DataNotFoundException
     */
    List<PlannedCtg> selectByPlanLevel(ProdPlanLevel planLevel) throws DataNotFoundException;

}
