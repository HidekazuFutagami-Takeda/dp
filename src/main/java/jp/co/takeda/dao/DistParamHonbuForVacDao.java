package jp.co.takeda.dao;

import java.util.Date;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.DistParamHonbuForVac;

/**
 * ワクチン用配分基準(本部)にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface DistParamHonbuForVacDao {

	/**
	 * ワクチン用配分基準(本部)を取得する。
	 * 
	 * @param seqKey シーケンスキー
	 * @return ワクチン用配分基準(本部)
	 * @throws DataNotFoundException
	 */
	DistParamHonbuForVac search(Long seqKey) throws DataNotFoundException;

	/**
	 * ワクチン用配分基準(本部)を取得する。
	 * 
	 * @param prodCode 品目固定コード
	 * @return ワクチン用配分基準(本部)
	 * @throws DataNotFoundException
	 */
	DistParamHonbuForVac search(String prodCode) throws DataNotFoundException;

	/**
	 * ワクチン用配分基準(本部)を登録する。
	 * 
	 * @param record ワクチン用配分基準(本部)
	 * @throws DuplicateException
	 */
	void insert(DistParamHonbuForVac record) throws DuplicateException;

	/**
	 * ワクチン用配分基準(本部)を更新する。
	 * 
	 * @param record ワクチン用配分基準(本部)
	 * @return 更新件数
	 */
	int update(DistParamHonbuForVac record);

	/**
	 * ワクチン用配分基準(本部)を削除する。
	 * 
	 * @param seqKey シーケンスキー
	 * @param upDate 更新日
	 * @return 削除件数
	 */
	int delete(Long seqKey, Date upDate);
}
