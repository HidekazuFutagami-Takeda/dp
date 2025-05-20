package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.ManageTeamPlan;
import jp.co.takeda.model.div.InsType;

/**
 * 管理のチーム別計画にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface ManageTeamPlanDao {

	/**
	 * ソート順<br>
	 * 組織ソート　　昇順<br>
	 * 施設出力対象区分　　昇順
	 */
	static final String SORT_STRING = "ORDER BY BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE, INS_TYPE";

	/**
	 * ソート順<br>
	 * 品目ソート　　昇順<br>
	 * 施設出力対象区分　　昇順
	 */
	static final String SORT_STRING2 = "ORDER BY GROUP_CODE, STAT_PROD_CODE, PROD_CODE, INS_TYPE";

	/**
	 * シーケンスキーを元に管理のチーム別計画を取得する。
	 * 
	 * 
	 * @param seqKey シーケンスキー
	 * @return 管理のチーム別計画
	 * @throws DataNotFoundException
	 */
	ManageTeamPlan search(Long seqKey) throws DataNotFoundException;

	/**
	 * ユニークキーを元に管理のチーム別計画を取得する。
	 * 
	 * @param insType 施設出力対象区分
	 * @param prodCode 品目固定コード
	 * @param sosCd 組織コード(チーム/雑担当チームの場合、営業所コード)
	 * @return 管理のチーム別計画
	 * @throws DataNotFoundException
	 */
	ManageTeamPlan searchUk(InsType insType, String prodCode, String sosCd) throws DataNotFoundException;

	/**
	 * 管理のチーム別計画リストを取得する。<br>
	 * 計画が存在しない場合、組織情報以外はNULLで返す。
	 * 
	 * @param sortString ソート条件
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(営業所)
	 * @return 管理のチーム別計画リスト
	 * @throws DataNotFoundException
	 */
	List<ManageTeamPlan> searchListByProd(String sortString, String prodCode, String sosCd3) throws DataNotFoundException;

	/**
	 * 管理のチーム別計画リストを取得する。<br>
	 * 計画が存在しない場合、品目情報以外はNULLで返す。
	 * 
	 * @param sortString ソート条件
	 * @param sosCd 組織コード(チーム/雑担当チームの場合、営業所コード)
	 * @param category カテゴリ(NULL可)
	 * @return 管理のチーム別計画リスト
	 * @throws DataNotFoundException
	 */
	List<ManageTeamPlan> searchListBySos(String sortString, String sosCd, String category) throws DataNotFoundException;

	/**
	 * 管理のチーム別計画を登録する。
	 * 
	 * 
	 * @param manageTeamPlan 管理のチーム別計画
	 * @param pgId PGID
	 * @throws DuplicateException
	 */
	void insert(ManageTeamPlan manageTeamPlan, String pgId) throws DuplicateException;

	/**
	 * 管理のチーム別計画を更新する。
	 * 
	 * 
	 * @param manageTeamPlan 管理のチーム別計画
	 * @param pgId PGID
	 * @return 更新件数
	 */
	int update(ManageTeamPlan manageTeamPlan, String pgId);

}
