package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.ManageBranchMonthPlan;
import jp.co.takeda.model.ManageBranchPlan;

/**
 * 管理の支店別計画にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface ManageBranchMonthPlanDao {

	/**
	 * ソート順<br>
	 * 組織ソート　　昇順<br>
	 * 施設出力対象区分　　昇順
	 */
	static final String SORT_STRING = "ORDER BY BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE";

	/**
	 * ソート順<br>
	 * 品目ソート　　昇順<br>
	 * 施設出力対象区分　　昇順
	 */
	static final String SORT_STRING2 = "ORDER BY DATA_SEQ, GROUP_CODE, STAT_PROD_CODE, PROD_CODE";

	/**
	 * シーケンスキーを元に管理の支店別計画を取得する。
	 *
	 *
	 * @param seqKey シーケンスキー
	 * @return 管理の支店別計画
	 * @throws DataNotFoundException
	 */
	ManageBranchPlan search(Long seqKey) throws DataNotFoundException;

	/**
	 * ユニークキーを元に管理の支店別計画を取得する。
	 *
	 * @param prodCode 品目固定コード
	 * @param sosCd 組織コード(支店)
	 * @return 管理の支店別計画
	 * @throws DataNotFoundException
	 */
	ManageBranchMonthPlan searchUk(String prodCode, String sosCd) throws DataNotFoundException;

	/**
	 * 品目固定コードをもとに、管理の支店別月別計画リストを取得する。<br>
	 * 計画が存在しない場合、組織情報以外はNULLで返す。
	 *
	 *
	 * @param sortString ソート条件
	 * @param prodCode 品目固定コード
	 * @param category 品目のカテゴリ
	 * @return 管理の支店別計画リスト
	 * @throws DataNotFoundException
	 */
	List<ManageBranchMonthPlan> searchListByProd(String sortString, String prodCode, String category) throws DataNotFoundException;

	/**
	 * 組織コードをもとに、管理の支店別計画リストを取得する。<br>
	 * 計画が存在しない場合、品目情報以外はNULLで返す。
	 *
	 *
	 * @param sortString ソート条件
	 * @param sosCd 組織コード(支店)
	 * @param category カテゴリ(NULL可)
	 * @param isJrns JRNSか
	 * @param jrnsPcatCd JRNSの品目分類コード
	 * @param jrnsCtgList JRNSに含まれるカテゴリリスト
	 * @return 管理の支店別計画リスト
	 * @throws DataNotFoundException
	 */
	List<ManageBranchMonthPlan> searchListBySos(String sortString, String sosCd, String category, boolean isJrns, String jrnsPcatCd, List<String> jrnsCtgList) throws DataNotFoundException;


	/**
	 * 管理の支店別月別計画の集計を取得する
	 * @param sosCd 組織コード
	 * @param category カテゴリ
	 * @return 支店別月別計画の集計
	 * @throws DataNotFoundException
	 */
	ManageBranchMonthPlan searchPlanSum(String sosCd, String category) throws DataNotFoundException;

	/**
	 * 管理の支店別月別計画を登録する。
	 *
	 *
	 * @param manageBranchPlan 管理の支店別月別計画
	 * @param pgId PGID
	 * @throws DuplicateException
	 */
	void insert(ManageBranchMonthPlan manageBranchPlan, String pgId) throws DuplicateException;

	/**
	 * 管理の支店別月別計画を更新する。
	 *
	 *
	 * @param manageBranchPlan 管理の支店別月別計画
	 * @param pgId PGID
	 * @return 更新件数
	 */
	int update(ManageBranchMonthPlan manageBranchPlan, String pgId);

	/**
	 * 集計行を取得する。
	 * @param prodCode
	 * @param sosCd2
	 * @return
	 */
	ManageBranchMonthPlan searchTotalLine(String prodCode, String sosCd2)throws DataNotFoundException;

}
