package jp.co.takeda.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.InsDocPlan;
import jp.co.takeda.model.div.InsType;

/**
 * 施設医師別計画にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface InsDocPlanDao {

	/**
	 * 医師別計画を取得する。
	 * 
	 * @param seqKey シーケンスキー
	 * @return 施設医師計画
	 * @throws DataNotFoundException
	 */
	InsDocPlan search(Long seqKey) throws DataNotFoundException;

	/**
	 * 施設医師計画のヘッダー情報を取得する。
	 * 
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param insType 施設出力対象区分【NULL可】
	 * @return ヘッダー情報
	 * @throws DataNotFoundException
	 */
	Map<String, Object> searchHeader(Integer jgiNo, String prodCode, InsType insType) throws DataNotFoundException;

	/**
	 * 施設医師別計画のリストを取得する。
	 * 
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param insType 施設出力対象区分【NULL可】
	 * @param 表示区分（0:実績計画あり、1:全施設医師）
	 * @return 施設医師計画のリスト
	 * @throws DataNotFoundException
	 */
	List<Map<String, Object>> searchList(Integer jgiNo, String prodCode, InsType insType, String planDispType) throws DataNotFoundException;

	/**
	 * 施設医師別計画を登録する。
	 * 
	 * @param record
	 * @throws DuplicateException
	 */
	void insert(InsDocPlan record) throws DuplicateException;

	/**
	 * 施設医師別計画の計画値を更新する。
	 * 
	 * @param record
	 * @throws DuplicateException
	 */
	int update(InsDocPlan record) throws DuplicateException;

	/**
	 * 施設医師別計画を削除する。
	 * 
	 * @param record
	 * @throws DuplicateException
	 */
	int delete(Long seqKey, Date upDate) throws DuplicateException;

}
