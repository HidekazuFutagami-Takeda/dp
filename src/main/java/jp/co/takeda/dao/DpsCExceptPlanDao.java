package jp.co.takeda.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import jp.co.takeda.model.ExceptPlan;

/**
 * * 立案対象外情報DAO
 * @author UJU3389
 *
 */
public interface DpsCExceptPlanDao {

	/**
	 * * 立案対象外情報の数を返す
	 * @param sosCd
	 * @return
	 */
	int countExceptPlan(String sosCd) ;

	/**
	 * 立案対象外情報の一覧を返す
	 * @param sosCd
	 * @return
	 */
	List<ExceptPlan> searchExceptPlan(String sosCd, String category);

	/**
	 * 削除（予定）施設に計画がされていない(計画金額が0円となっている)ことを確認する。
	 * 計画されている場合、トップお知らせおよび業務進捗画面に表示する。
	 *
	 * @throws DataAccessException 更新処理時にエラーが発生した場合にスロー
	 */
	void updateCheakDelflg() throws DataAccessException;

	/**
	 * 計画立案対象外特約店に計画がされていない(計画金額が0円となっている)ことを確認する。
	 * 計画されている場合、トップお知らせおよび業務進捗画面に表示する。
	 *
	 * @throws DataAccessException 更新処理時にエラーが発生した場合にスロー
	 */
	void updateCheakPlantaigaiflg() throws DataAccessException;
}
