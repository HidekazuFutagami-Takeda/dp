package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.ManageIyakuMonthPlan;
import jp.co.takeda.model.ManageIyakuPlan;

/**
 * 管理の全社計画にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface ManageIyakuMonthPlanDao {

	/**
	 * ソート順<br>
	 * 品目ソート　　昇順<br>
	 * 施設出力対象区分　　昇順
	 */
	static final String SORT_STRING = "ORDER BY DATA_SEQ, GROUP_CODE, STAT_PROD_CODE, PROD_CODE";

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
	 * @param prodCode 品目固定コード
	 * @return 管理の全社計画
	 * @throws DataNotFoundException
	 */
	ManageIyakuMonthPlan searchUk(String prodCode) throws DataNotFoundException;

	/**
	 * 管理の全社計画リストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param category カテゴリ(NULL可)
	 * @param vaccineCode ワクチンのカテゴリコード
	 * @param isJrns JRNSか
	 * @param jrnsPcatCd JRNSの品目分類コード
	 * @return 管理の全社計画リスト
	 * @throws DataNotFoundException
	 */
	List<ManageIyakuMonthPlan> searchList(String sortString, String category, String vaccineCode, boolean isJrns, String jrnsPcatCd, List<String> jrnsCtgList) throws DataNotFoundException;

	/**
	 * 管理の全社計画の集計を取得する
	 * @param category カテゴリ
	 * @param vaccineCode ワクチンのカテゴリコード
	 * @return 管理の全社計画リストの合計値
	 * @throws DataNotFoundException
	 */
	ManageIyakuMonthPlan searchPlanSum(String category, String vaccineCode) throws DataNotFoundException;

	/**
	 * 管理の全社月別計画を登録する。
	 *
	 *
	 * @param manageIyakuPlan 管理の全社月別計画
	 * @param pgId PGID
	 * @throws DuplicateException
	 */
	void insert(ManageIyakuMonthPlan manageIyakuPlan, String pgId) throws DuplicateException;

	/**
	 * 管理の全社月別計画を更新する。
	 *
	 *
	 * @param manageIyakuPlan 管理の全社月別計画
	 * @param pgId PGID
	 * @return 更新件数
	 */
	int update(ManageIyakuMonthPlan manageIyakuPlan, String pgId);
	/**
	 * 集計行を取得する。
	 * @param prodCode
	 * @return
	 */
	ManageIyakuMonthPlan searchTotalLine(String prodCode)throws DataNotFoundException;

}
