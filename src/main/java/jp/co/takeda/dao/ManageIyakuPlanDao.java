package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.ManageIyakuPlan;
import jp.co.takeda.model.div.InsType;

/**
 * 管理の全社計画にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface ManageIyakuPlanDao {

	/**
	 * ソート順<br>
	 * 品目ソート　　昇順<br>
	 * 施設出力対象区分　　昇順
	 */
	static final String SORT_STRING = "ORDER BY DATA_SEQ, GROUP_CODE, STAT_PROD_CODE, PROD_CODE, INS_TYPE";

	/**
	 * シーケンスキーを元に管理の施設特約店別計画を取得する。
	 * 
	 * 
	 * @param seqKey シーケンスキー
	 * @return 管理の全社計画
	 * @throws DataNotFoundException
	 */
	ManageIyakuPlan search(Long seqKey) throws DataNotFoundException;

	/**
	 * ユニークキーを元に管理の全社計画を取得する。
	 * 
	 * @param insType 施設出力対象区分
	 * @param prodCode 品目固定コード
	 * @return 管理の全社計画
	 * @throws DataNotFoundException
	 */
	ManageIyakuPlan searchUk(InsType insType, String prodCode) throws DataNotFoundException;

	/**
	 * 管理の全社計画リストを取得する。
	 * 
	 * @param sortString ソート条件
	 * @param category カテゴリ(NULL可)
	 * @param vaccineCode ワクチンのカテゴリコード
	 * @return 管理の全社計画リスト
	 * @throws DataNotFoundException
	 */
	List<ManageIyakuPlan> searchList(String sortString, String category, String vaccineCode) throws DataNotFoundException;

	/**
	 * 管理の全社計画を登録する。
	 * 
	 * 
	 * @param manageIyakuPlan 管理の全社計画
	 * @param pgId PGID
	 * @throws DuplicateException
	 */
	void insert(ManageIyakuPlan manageIyakuPlan, String pgId) throws DuplicateException;

	/**
	 * 管理の全社計画を更新する。
	 * 
	 * 
	 * @param manageIyakuPlan 管理の全社計画
	 * @param pgId PGID
	 * @return 更新件数
	 */
	int update(ManageIyakuPlan manageIyakuPlan, String pgId);

}
