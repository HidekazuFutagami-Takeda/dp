package jp.co.takeda.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.InsDocPlanStatus;
import jp.co.takeda.model.div.ProdCategory;
import jp.co.takeda.model.div.StatusForInsDocPlan;
import jp.co.takeda.security.DpUser;

import org.springframework.dao.OptimisticLockingFailureException;


/**
 * 施設医師別計画立案ステータスにアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface InsDocPlanStatusDao {

//	/**
//	 * ソート順<br>
//	 * 組織 昇順<br>
//	 * 従業員 昇順<br>
//	 * 品目　昇順
//	 */
//	static final String SORT_STRING = "ORDER BY BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE, SHOKUSEI_CD, SHOKUSHU_CD, JGI_NO, GROUP_CODE, STAT_PROD_CODE, PROD_CODE";

	/**
	 * 施設医師別計画立案ステータスを取得する。
	 * 
	 * @param seqKey シーケンスキー
	 * @return 施設医師別計画立案ステータス
	 */
	InsDocPlanStatus search(Long seqKey) throws DataNotFoundException;

	/**
	 * 従業員番号、品目固定コードを元に、施設医師別計画立案ステータスを取得する。
	 * 
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @return 施設医師別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	InsDocPlanStatus search(Integer jgiNo, String prodCode) throws DataNotFoundException;

	/**
	 * 組織コード、品目固定コードを元に、施設医師別計画立案ステータスを取得する。<br>
	 * 従業員をベースとし、ステータスが存在しないレコードも取得する。<br>
	 * ソートは組織・従業員ソート順固定
	 * 
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param prodCode 品目固定コード
	 * @return 施設医師別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	List<Map<String, Object>> searchJgiBaseList(String sosCd3, String sosCd4, String prodCode) throws DataNotFoundException;

	/**
	 * 組織コードまたは従業員を元に、施設医師別計画立案ステータスを取得する。<br>
	 * 品目をベースとし、ステータスが存在しないレコードも取得する。<br>
	 * ソートは品目ソート順固定
	 * 
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param jgiNo 従業員番号
	 * @param category カテゴリ
	 * @return
	 * @throws DataNotFoundException
	 */
	List<Map<String, Object>> searchProdBaseList(String sosCd3, String sosCd4, Integer jgiNo, ProdCategory category) throws DataNotFoundException;

	/**
	 * 組織コード、品目を元に、施設医師別計画立案ステータスを取得する。
	 * 
	 * @param sortString ソート順
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param prodCode 品目固定コード
	 * @return 施設医師別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	List<InsDocPlanStatus> searchList(String sortString, String sosCd3, String sosCd4, String prodCode) throws DataNotFoundException;

	/**
	 * 引数で指定された最終更新日と、現在の最終更新日を比較する。<br>
	 * 最終更新日が異なっている場合は、排他エラーをスローする。
	 * 
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param orgLastUpdate 前回最終更新日時
	 * @throws OptimisticLockingFailureException 現在の最終更新日が、引数で指定された最終更新日と異なっている場合
	 */
	void checkLastUpDate(Integer jgiNo, String prodCode, Date orgLastUpdate) throws OptimisticLockingFailureException;

	/**
	 * 引数で指定された最終更新日と、現在の最終更新日を比較する。<br>
	 * 最終更新日が異なっている場合は、排他エラーをスローする。
	 * 
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param orgLastUpdate 前回最終更新日時
	 * @throws OptimisticLockingFailureException 現在の最終更新日が、引数で指定された最終更新日と異なっている場合
	 */
	void checkLastUpDate(String sosCd3, String sosCd4, Integer jgiNo, String prodCode, Date orgLastUpdate) throws OptimisticLockingFailureException;

	/**
	 * 最終更新日時を取得する。
	 * 
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @return 最終更新日時
	 * @throws DataNotFoundException
	 */
	Date getLastUpDate(String sosCd3, String sosCd4, Integer jgiNo, String prodCode);

	/**
	 * 施設医師別計画立案ステータスを更新する。
	 * 
	 * @param record 施設医師別計画立案ステータス
	 * @return 更新件数
	 */
	int update(InsDocPlanStatus record);
	
	// -------------------------------------------------------------
	// バッチ配分系ステータス更新
	// -------------------------------------------------------------

	/**
	 * 配分開始ステータスに更新・登録する。
	 * 
	 * @param sosCd3 組織コード(営業所)
	 * @param jgiNo 従業員番号
	 * @param appServerKbn サーバ区分
	 * @param distStartDate 配分開始日時
	 * @param plannedClearFlg 計画クリアフラグ
	 * @param openFlg 同時公開フラグ
	 * @param dpUser 実行者
	 * @return
	 */
	int haibunStart(String sosCd3, Integer jgiNo, ProdCategory category, String appServerKbn, Date distStartDate, boolean plannedClearFlg, boolean openFlg, DpUser dpUser);

	/**
	 * 配分完了ステータスに更新する。
	 * 更新後ステータスが指定されていない場合は、前回ステータスに更新する。
	 * 
	 * @param jgiNo 従業員番号
	 * @param statusForInsDocPlan 更新後のステータス
	 * @param distEndDate 配分完了日時
	 * @param dpUser
	 * @return
	 */
	int haibunEnd(Integer jgiNo, StatusForInsDocPlan statusForInsDocPlan, Date distEndDate, DpUser dpUser);
	
	/**
	 * 配分ステータスを元に戻す（削除）。
	 * 配分中かつ実行前ステータスがないものは、削除する。
	 * 
	 * @param jgiNo 従業員番号
	 * @return
	 */
	public int haibunRecoverDelete(Integer jgiNo);

	/**
	 * 配分ステータスを元に戻す（更新）。
	 * 配分中かつ実行前ステータスがあるものは、更新する。
	 * ・ステータス＝実行前ステータス
	 * ・配分開始日時、終了日時＝実行前配分開始日時
	 * 
	 * @param jgiNo 従業員番号
	 * @param dpUser 実行者
	 * @return
	 */
	public int haibunRecoverUpdate(Integer jgiNo, DpUser dpUser);
	
	/**
	 * 指定サーバ区分に該当する配分中の施設医師別計画立案ステータスリストを取得する。 <br>
	 * 
	 * @param appServerKbn サーバ区分
	 * @return 施設医師別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	List<Map<String, Object>> searchDistingList(String appServerKbn) throws DataNotFoundException;
}
