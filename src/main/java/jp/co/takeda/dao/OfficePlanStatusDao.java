package jp.co.takeda.dao;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.OfficePlanStatus;

/**
 * 営業所計画ステータスにアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface OfficePlanStatusDao {

	/**
	 * 営業所計画ステータスを取得する。
	 *
	 * @param seqKey シーケンスキー
	 * @return 営業所計画ステータス
	 */
	OfficePlanStatus search(Long seqKey) throws DataNotFoundException;

	/**
	 * 組織コード、カテゴリをもとに、営業所計画ステータスを取得する。
	 *
	 * @param seqKey シーケンスキー
	 * @param prodCategory 品目カテゴリ
	 * @return 営業所計画ステータス
	 * @throws DataNotFoundException
	 */
	OfficePlanStatus search(String sosCd, String prodCategory) throws DataNotFoundException;

	/**
	 * 組織コード、カテゴリをもとに、営業所計画ステータスを取得する。
	 *
	 * @param seqKey シーケンスキー
	 * @param prodCategory 品目カテゴリ
	 * @return 営業所計画ステータス
	 * @throws DataNotFoundException
	 */
	List<OfficePlanStatus> searchSosCdCategoryList(String sosCd, String prodCategory) throws DataNotFoundException;
	/**
	 * 営業所計画ステータスを登録する。
	 *
	 * @param record 営業所計画ステータス
	 * @throws DuplicateException
	 */
	void insert(OfficePlanStatus record) throws DuplicateException;

	/**
	 * 営業所計画ステータスを更新する。
	 *
	 * @param record 営業所計画ステータス
	 * @return 更新件数
	 */
	int update(OfficePlanStatus record);

	/**
	 * 営業所計画ステータスを削除する。
	 *
	 * @param seqKey シーケンスキー
	 * @param upDate 更新日
	 * @return 削除件数
	 */
	int delete(Long seqKey, Date upDate);
}
