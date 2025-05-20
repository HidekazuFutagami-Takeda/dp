package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.ManageOfficeMonthPlan;
import jp.co.takeda.model.ManageOfficePlan;
import jp.co.takeda.model.div.InsType;

/**
 * 管理の営業所別計画にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface ManageOfficeMonthPlanDao {

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
	 * シーケンスキーを元に管理の営業所別計画を取得する。
	 *
	 *
	 * @param seqKey シーケンスキー
	 * @return 管理の営業所別計画
	 * @throws DataNotFoundException
	 */
	ManageOfficePlan search(Long seqKey) throws DataNotFoundException;

	/**
	 * ユニークキーを元に管理の営業所別計画を取得する。
	 *
	 * @param insType 施設出力対象区分
	 * @param prodCode 品目固定コード
	 * @param sosCd 組織コード(営業所)
	 * @return 管理の営業所別計画
	 * @throws DataNotFoundException
	 */
	ManageOfficeMonthPlan searchUk(InsType insType, String prodCode, String sosCd) throws DataNotFoundException;

	/**
	 * 管理の営業所別計画リストを取得する。<br>
	 * 計画が存在しない場合、組織情報以外はNULLで返す。
	 *
	 * @param sortString ソート条件
	 * @param prodCode 品目固定コード
	 * @param sosCd2 組織コード(支店)
	 * @param category 品目カテゴリ
	 * @return 管理の営業所別計画リスト
	 * @throws DataNotFoundException
	 */
	List<ManageOfficeMonthPlan> searchListByProd(String sortString, String prodCode, String category, String sosCd2) throws DataNotFoundException;

	/**
	 * 管理の営業所別計画リストを取得する。<br>
	 * 計画が存在しない場合、品目情報以外はNULLで返す。
	 *
	 * @param sortString ソート条件
	 * @param sosCd 組織コード(営業所)
	 * @param category カテゴリ(NULL可)
	 * @param vaccineCode ワクチンのカテゴリコード
	 * @return 管理の営業所別計画リスト
	 * @throws DataNotFoundException
	 */
	List<ManageOfficeMonthPlan> searchListBySos(String sortString, String sosCd, String category, String vaccineCode, boolean isJrns, String jrnsPcatCd, List<String> jrnsCtgList) throws DataNotFoundException;

	/**
	 * 管理の営業所別月別計画の集計を取得する
	 * @param sosCd 組織コード
	 * @param category カテゴリ
	 * @param vaccineCode ワクチンコード
	 * @return 営業所別月別計画の集計
	 * @throws DataNotFoundException
	 */
	ManageOfficeMonthPlan searchPlanSum(String sosCd, String category, String vaccineCode) throws DataNotFoundException;

	/**
	 * 管理の営業所別計画を登録する。
	 *
	 *
	 * @param manageOfficePlan 管理の営業所別月別計画
	 * @param pgId PGID
	 * @throws DuplicateException
	 */
	void insert(ManageOfficeMonthPlan manageOfficePlan, String pgId) throws DuplicateException;

	/**
	 * 管理の営業所別計画を更新する。
	 *
	 *
	 * @param manageOfficePlan 管理の営業所別月別計画
	 * @param pgId PGID
	 * @return 更新件数
	 */
	int update(ManageOfficeMonthPlan manageOfficePlan, String pgId);

	/**
	 * 集計行を取得する。
	 * @param prodCode
	 * @return
	 */
	ManageOfficeMonthPlan searchTotalLine(InsType insType, String prodCode, String sosCd) throws DataNotFoundException;
}
