package jp.co.takeda.dao;

import java.util.Date;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.EstParamOffice;

/**
 * 試算パラメータ(営業所)にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface EstParamOfficeDao {

	/**
	 * 試算パラメータ(営業所)を取得する。
	 * 
	 * @param seqKey シーケンスキー
	 * @return 試算パラメータ(営業所)
	 */
	EstParamOffice search(Long seqKey) throws DataNotFoundException;

	/**
	 * 組織コード、品目固定コードを指定して、試算パラメータ(営業所)を取得する。
	 * 
	 * @param sosCd 組織コード
	 * @param prodCode 品目固定コード
	 * @return 試算パラメータ(営業所)
	 * @throws DuplicateException
	 */
	EstParamOffice search(String sosCd, String prodCode) throws DataNotFoundException;

	/**
	 * 試算パラメータ(営業所)を登録する。
	 * 
	 * @param record 試算パラメータ(営業所)
	 * @throws DuplicateException
	 */
	void insert(EstParamOffice record) throws DuplicateException;

	/**
	 * 試算パラメータ(営業所)を更新する。
	 * 
	 * @param record 試算パラメータ(営業所)
	 * @return 更新件数
	 */
	int update(EstParamOffice record);

	/**
	 * 試算パラメータ(営業所)を削除する。
	 * 
	 * @param seqKey シーケンスキー
	 * @param upDate 更新日
	 * @return 削除件数
	 */
	int delete(Long seqKey, Date upDate);
}
