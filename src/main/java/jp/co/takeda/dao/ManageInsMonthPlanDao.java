package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.ManageInsMonthPlan;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.Prefecture;

/**
 * 管理の施設別計画にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface ManageInsMonthPlanDao {

	/**
	 * ソート順<br>
	 * 施設ソート昇順
	 *
	 */
	static final String SORT_STRING = "ORDER BY OINS_HO_INS_TYPE, OINS_RELN_INS_NO, MAIN_INS_NO, INS_HO_INS_TYPE, RELN_INS_NO, RELN_INS_CODE";

	/**
	 * ソート順<br>
	 * 品目ソート昇順
	 */
	static final String SORT_STRING2 = "ORDER BY DATA_SEQ, GROUP_CODE, STAT_PROD_CODE, PROD_CODE";

	/**
	 * シーケンスキーを元に管理の施設別計画を取得する。
	 *
	 *
	 * @param seqKey シーケンスキー
	 * @return 管理の施設別計画
	 * @throws DataNotFoundException
	 */
	ManageInsMonthPlan search(Long seqKey) throws DataNotFoundException;

	/**
	 * ユニークキーを元に管理の施設別計画を取得する。
	 *
	 * @param prodCode 品目固定コード
	 * @param insNo 施設コード
	 * @return 管理の施設別計画
	 * @throws DataNotFoundException
	 */
	ManageInsMonthPlan searchUk(String prodCode, String insNo) throws DataNotFoundException;

	/**
	 * 管理の施設別計画リストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param prodCode 品目固定コード
	 * @param jgiNo 従業員番号
	 * @param insType 施設出力対象区分
	 * @param relnInsNo 施設内部コード（前方？桁）
	 * @param oyakoKb 親子区分
	 * @param oyakoKb2 親子区分2
	 * @param tgtInsKb 対象施設区分
	 * @return 管理の施設別計画リスト
	 * @throws DataNotFoundException
	 */
	List<ManageInsMonthPlan> searchListByProd(String sortString, String prodCode, Integer jgiNo, InsType insType, String relnInsNo, String oyakoKb,String oyakoKb2,
			String tgtInsKb,String oyakoKbProdCode) throws DataNotFoundException;

	/**
	 * 管理の施設別計画から、実施計画２（Ｙ価）のサマリを取得する。
	 *
	 * @param prodCode 品目固定コード
	 * @param jgiNo 従業員番号
	 * @param insType 施設出力対象区分
	 * @param oyakoKb 親子区分
	 * @param oyakoKb2 親子区分２
	 * @param oyakoKbProdCode 親子区分品目コード
	 * @return 実施計画２（Ｙ価）のサマリ
	 */
	ManageInsMonthPlan searchSumByIns(String prodCode, Integer jgiNo, InsType insType, String oyakoKb,String oyakoKb2, String oyakoKbProdCode);

	/**
	 * （ワ）管理の施設別計画から、実施計画２（Ｙ価）のサマリを取得する。
	 *
	 * @param prodCode 品目固定コード
	 * @param jgiNo 従業員番号
	 * @param insType 施設出力対象区分
	 * @return 実施計画２（Ｙ価）のサマリ
	 */
	ManageInsMonthPlan searchSumByInsForVac(String prodCode, Integer jgiNo, ActivityType activityType);


	/**
	 * 管理の施設別計画リストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param insNo 施設コード
	 * @param category カテゴリ(NULL可)
	 * @param allProdFlg 全品目取得フラグ（true:計画がない品目も取得、false:計画がある品目のみ取得)
	 * @param 対象施設区分
	 * @param isJrns JRNSか
	 * @param jrnsPcatCd JRNSの品目分類コード
	 * @param jrnsCtgList JRNSに含まれるカテゴリリスト
	 * @return 管理の施設別計画リスト
	 * @throws DataNotFoundException
	 */
	List<ManageInsMonthPlan> searchListByIns(String sortString, String insNo, String category, boolean allProdFlg,
			String tgtInsKb, String vaccineCode, boolean isJrns, String jrnsPcatCd, List<String> jrnsCtgList) throws DataNotFoundException;

	/**
	 * 管理の施設別月別計画の集計を取得する
	 * @param insNo 施設コード
	 * @param category カテゴリ
	 * @param allProdFlg 全品目取得フラグ（true:計画がない品目も取得、false:計画がある品目のみ取得)
	 * @param tgtInsKb 対象施設区分
	 * @param vaccineCode ワクチンコード
	 * @return 施設別月別計画の集計
	 * @throws DataNotFoundException
	 */
	ManageInsMonthPlan searchPlanSum(String insNo, String category, boolean allProdFlg, String tgtInsKb, String vaccineCode) throws DataNotFoundException;

	/**
	 * 管理のワクチン用施設別計画リストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param prodCode 品目固定コード
	 * @param jgiNo 従業員番号
	 * @param activityType 活動区分
	 * @param addrCodePref JIS府県コード
	 * @param addrCodeCity JIS市区町村コード
	 * @param insNo 施設コード
	 * @param oyakoKb 親子区分
	 * @param oyakoKb2 親子区分2
	 * @param tgtInsKb 対象施設区分
	 * @param oyakoKbProdCode 親子区分品目コード
	 * @return 管理のワクチン用施設別計画リスト
	 * @throws DataNotFoundException
	 */
	List<ManageInsMonthPlan> searchListByProdForVac(String sortString, String prodCode, Integer jgiNo, ActivityType activityType, Prefecture addrCodePref, String addrCodeCity,
		String insNo, String oyakoKb,String oyakoKb2, String tgtInsKb, String oyakoKbProdCode) throws DataNotFoundException;


	/**
	 * 管理の施設別計画を登録する。
	 *
	 *
	 * @param manageInsPlan 管理の施設別計画
	 * @param pgId PGID
	 * @throws DuplicateException
	 */
	void insert(ManageInsMonthPlan manageInsPlan, String pgId) throws DuplicateException;

	/**
	 * 管理の施設別計画を更新する。
	 *
	 *
	 * @param manageInsPlan 管理の施設別計画
	 * @param pgId PGID
	 * @return 更新件数
	 */
	int update(ManageInsMonthPlan manageInsPlan, String pgId);

}
