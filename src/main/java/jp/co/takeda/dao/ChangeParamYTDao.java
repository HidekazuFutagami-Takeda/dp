package jp.co.takeda.dao;

import java.util.Date;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.ChangeParamYT;
import jp.co.takeda.model.div.Term;

/**
 * 変換パラメータ(Y→T価)にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface ChangeParamYTDao {

	/**
	 * 変換パラメータ(Y→T価)を取得する。
	 *
	 * @param seqKey シーケンスキー
	 * @return 変換パラメータ(Y→T価)
	 */
	ChangeParamYT search(Long seqKey) throws DataNotFoundException;

	/**
	 * 変換パラメータ(Y→T価)を登録する。
	 *
	 * @param record 変換パラメータ(Y→T価)
	 * @throws DuplicateException
	 */
	void insert(ChangeParamYT record) throws DuplicateException;

	/**
	 * 変換パラメータ(Y→T価)を更新する。
	 *
	 * @param record 変換パラメータ(Y→T価)
	 * @return 更新件数
	 */
	int update(ChangeParamYT record);

	/**
	 * 変換パラメータ(Y→T価)の最終更新日時を取得する。
	 *
	 * @param calYear 年度
	 * @param calTerm 期
	 * @return 最終更新日時(データ無の場合はNULL)
	 */
	Date getLastUpDate(String calYear, Term calTerm);

	/**
	 * 変換パラメータ(S/B価)の最終更新日時を取得する。
	 *
	 * @param calYear 年度
	 * @param calTerm 期
	 * @return 最終更新日時(データ無の場合はNULL)
	 */
	Date getLastUpDateSb(String calYear, Term calTerm);

	/**
	 * 変換パラメータ(S/Y価)の最終更新日時を取得する。
	 *
	 * @param calYear 年度
	 * @param calTerm 期
	 * @return 最終更新日時(データ無の場合はNULL)
	 */
	Date getLastUpDateSy(String calYear, Term calTerm);

}
