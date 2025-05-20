package jp.co.takeda.dao;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.WsPlanStatusForVac;

/**
 * ワクチン用特約店別計画立案ステータスにアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface WsPlanStatusForVacDao {

	/**
	 * ワクチン用特約店別計画立案ステータスを取得する。
	 * 
	 * @param seqKey シーケンスキー
	 * @return ワクチン用特約店別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	WsPlanStatusForVac search(Long seqKey) throws DataNotFoundException;

	/**
	 * 品目固定コードを元に、ワクチン用施設特約店別計画立案ステータスを取得する。
	 * 
	 * @param prodCode 品目固定コード
	 * @return ワクチン用施設特約店別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	WsPlanStatusForVac search(String prodCode) throws DataNotFoundException;

	/**
	 * ワクチン用特約店別計画立案ステータス一覧を取得する。
	 * 
	 * @return ワクチン用施設特約店別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	List<WsPlanStatusForVac> searchList() throws DataNotFoundException;

	/**
	 * ワクチン用特約店別計画立案ステータスの全品目サマリを取得する。
	 * 
	 * @return ワクチン用特約店別計画立案ステータスのサマリ
	 * @throws DataNotFoundException
	 */
	WsPlanStatusForVac searchSummary() throws DataNotFoundException;

	/**
	 * ワクチン用特約店別計画立案ステータスを登録する。
	 * 
	 * @param record ワクチン用特約店別計画立案ステータス
	 * @throws DuplicateException
	 */
	void insert(WsPlanStatusForVac record) throws DuplicateException;

	/**
	 * ワクチン用特約店別計画立案ステータスを更新する。
	 * 
	 * @param record ワクチン用特約店別計画立案ステータス
	 * @return 更新件数
	 */
	int update(WsPlanStatusForVac record);

	/**
	 * ワクチン用特約店別計画立案ステータスを削除する。
	 * 
	 * @param seqKey シーケンスキー
	 * @param upDate 更新日
	 * @return 削除件数
	 */
	int delete(Long seqKey, Date upDate);
}
