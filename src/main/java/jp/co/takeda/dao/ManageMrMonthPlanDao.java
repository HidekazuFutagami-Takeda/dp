package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.ManageMrMonthPlan;
import jp.co.takeda.model.ManageMrPlan;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.InsType;

/**
 * 管理の担当者別計画にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface ManageMrMonthPlanDao {

	/**
	 * ソート順<br>
	 * 品目ソート　　昇順<br>
	 * 施設出力対象区分　　昇順
	 */
	static final String SORT_STRING2 = "ORDER BY GROUP_CODE, STAT_PROD_CODE, PROD_CODE, INS_TYPE";

	/**
	 * シーケンスキーを元に管理の担当者別計画を取得する。
	 * 施設積上げは含まない。
	 *
	 * @param seqKey シーケンスキー
	 * @return 管理の担当者別計画
	 * @throws DataNotFoundException
	 */
	ManageMrPlan search(Long seqKey) throws DataNotFoundException;

	/**
	 * ユニークキーを元に管理の担当者別計画を取得する。
	 * 施設積上げは含まない。
	 *
	 * @param insType 施設出力対象区分
	 * @param prodCode 品目固定コード
	 * @param jgiNo 従業員番号
	 * @return 管理の担当者別計画
	 * @throws DataNotFoundException
	 */
	ManageMrMonthPlan searchUk(InsType insType, String prodCode, Integer jgiNo) throws DataNotFoundException;

	/**
	 * （ワ）ユニークキーを元に管理の担当者別計画を取得する。
	 * 施設積上げは含まない。
	 *
	 * @param insType 施設出力対象区分
	 * @param prodCode 品目固定コード
	 * @param jgiNo 従業員番号
	 * @return 管理の担当者別計画
	 * @throws DataNotFoundException
	 */
	ManageMrMonthPlan searchUkForVac(String prodCode, Integer jgiNo) throws DataNotFoundException;

	/**
	 * 管理の担当者別計画リストを取得する。
	 *
	 * @param prodCode 品目固定コード
	 * @param sosCd 組織コード(営業所 or チーム)
	 * @param bumonRank 部門ランク
	 * @param jgiNo MRユーザの従業員番号
	 * @param oyakoKb 親子区分
	 * @param oyakoKb2 親子区分２
	 * @param oyakoKbProdCode 親子区分品目コード
	 * @return 管理の担当者別計画リスト
	 * @throws DataNotFoundException
	 */
	// mod Start 2022/10/05 H.Futagami 2022年4月組織変更対応  担当者をカテゴリのMRで絞る
	//List<ManageMrMonthPlan> searchListByProd(String prodCode, String sosCd, BumonRank bumonRank, String jgiNo, String oyakoKb,String oyakoKb2,
	//		String oyakoKbProdCode) throws DataNotFoundException;
	List<ManageMrMonthPlan> searchListByProd(String prodCode, String sosCd, BumonRank bumonRank, String jgiNo, String oyakoKb,String oyakoKb2,
			String oyakoKbProdCode, String category) throws DataNotFoundException;
	// mod End 2022/10/05 H.Futagami 2022年4月組織変更対応  担当者をカテゴリのMRで絞る

	/**
	 * 管理の担当者別計画リスト(雑担当分)を取得する。
	 *
	 * @param sortString ソート条件
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(営業所)
	 * @return 管理の担当者別計画リスト
	 * @throws DataNotFoundException
	 */
	@Deprecated
	List<ManageMrPlan> searchZatuListByProd(String sortString, String prodCode, String sosCd3) throws DataNotFoundException;

	/**
	 * 管理の担当者別計画リストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param jgiNo 従業員番号
	 * @param category カテゴリ
	 * @param tgtInsKbn 対象施設区分
	 * @param isJrns JRNSか
	 * @param jrnsPcatCd JRNSの品目分類コード
	 * @param jrnsCtgList JRNSに含まれるカテゴリリスト
	 * @return 管理の担当者別計画リスト
	 * @throws DataNotFoundException
	 */
	List<ManageMrMonthPlan> searchListByJgi(Integer jgiNo, String category, String tgtInsKbn, boolean isJrns, String jrnsPcatCd, List<String> jrnsCtgList) throws DataNotFoundException;

	/**
	 * 管理の担当者別月別計画の集計を取得する
	 * @param jgiNo 従業員番号
	 * @param category カテゴリ
	 * @param tgtInsKbn 対象施設区分
	 * @return 担当者別月別計画の集計
	 * @throws DataNotFoundException
	 */
	ManageMrMonthPlan searchPlanSum(Integer jgiNo, String category, String tgtInsKbn) throws DataNotFoundException;

	/**
	 * 管理の担当者別月別計画を登録する。
	 *
	 *
	 * @param manageMrPlan 管理の担当者別月別計画
	 * @param pgId PGID
	 * @throws DuplicateException
	 */
	void insert(ManageMrMonthPlan manageMrPlan, String pgId) throws DuplicateException;

	/**
	 * 管理の担当者別月別計画を更新する。
	 *
	 *
	 * @param manageMrPlan 管理の担当者別月別計画
	 * @param pgId PGID
	 * @return 更新件数
	 */
	int update(ManageMrMonthPlan manageMrPlan, String pgId);

	/**
	 * 集計行を取得する。
	 * @param prodCode
	 * @return
	 */
// mod Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
//	ManageMrMonthPlan searchTotalLine(InsType insType, String prodCode, Integer jgiNo) throws DataNotFoundException;
	ManageMrMonthPlan searchTotalLine(InsType insType, String prodCode, Integer jgiNo, boolean vacCheck) throws DataNotFoundException;
// mod Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

}
