package jp.co.takeda.dao;

import java.util.Date;
import java.util.List;

import org.springframework.dao.OptimisticLockingFailureException;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.dto.ProdInsWsPlanStatSummaryDto;
//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
import jp.co.takeda.model.DpsKakuteiErrMsg;
//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
import jp.co.takeda.model.InsWsPlanStatus;
import jp.co.takeda.security.DpUser;

/**
 * 施設特約店別計画立案ステータスにアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface InsWsPlanStatusDao {

	/**
	 * ソート順<br>
	 * 組織 昇順<br>
	 * 従業員 昇順<br>
	 * 品目　昇順
	 */
	static final String SORT_STRING = "ORDER BY BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE, SHOKUSEI_CD, SHOKUSHU_CD, JGI_NO, GROUP_CODE, STAT_PROD_CODE, PROD_CODE";

	/**
	 * 施設特約店別計画立案ステータスを取得する。
	 *
	 * @param seqKey シーケンスキー
	 * @return 施設特約店別計画立案ステータス
	 */
	InsWsPlanStatus search(Long seqKey) throws DataNotFoundException;

	/**
	 * 従業員番号、品目固定コードを元に、施設特約店別計画立案ステータスを取得する。
	 *
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @return 施設特約店別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	InsWsPlanStatus search(Integer jgiNo, String prodCode) throws DataNotFoundException;

	/**
	 * 組織コード、品目を元に、施設特約店別計画立案ステータスを取得する。
	 *
	 * @param sortString ソート順
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param prodCode 品目固定コード
	 * @return 施設特約店別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	List<InsWsPlanStatus> searchList(String sortString, String sosCd3, String sosCd4, String prodCode) throws DataNotFoundException;

	/**
	 * 組織コード、品目固定コードを元に、施設特約店別計画立案ステータスを取得する。<br>
	 * 従業員をベースとし、ステータスが存在しないレコードも取得する。<br>
	 * ソートは組織・従業員ソート順固定
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param prodCode 品目固定コード
	 * @return 施設特約店別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	List<InsWsPlanStatus> searchJgiBaseList(String sosCd3, String sosCd4, String prodCode) throws DataNotFoundException;

	/**
	 * 指定サーバ区分に該当する配分中の施設特約店別計画立案ステータスリストを取得する。 <br>
	 *
	 * @param sortString ソート順
	 * @param appServerKbn サーバ区分
	 * @return 施設特約店別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	List<InsWsPlanStatus> searchDistingList(String sortString, String appServerKbn) throws DataNotFoundException;

	/**
	 * 検索条件を基に、品目単位の施設特約店別計画立案ステータスサマリーを取得する。<br>
	 * ソートは品目ソート順固定
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param category カテゴリ
	 * @return 施設特約店別計画立案ステータスのサマリー
	 * @throws DataNotFoundException
	 */
	List<ProdInsWsPlanStatSummaryDto> searchProdStatList(String sosCd3, String category) throws DataNotFoundException;

	/**
	 * 検索条件を基に、品目単位の施設特約店別計画立案ステータスサマリーを取得する。<br>
	 * ソートは品目ソート順固定
	 *
	 * @param sosCd4 組織コード(チーム)
	 * @return 施設特約店別計画立案ステータスのサマリー
	 * @throws DataNotFoundException
	 */
	List<ProdInsWsPlanStatSummaryDto> searchProdStatListByTeam(String sosCd4, String category) throws DataNotFoundException;

	/**
	 * 検索条件を基に、品目単位の施設特約店別計画立案ステータスサマリーを取得する。<br>
	 * ソートは品目ソート順固定
	 *
	 * @param jgiNo 従業員番号
	 * @return 施設特約店別計画立案ステータスのサマリー
	 * @throws DataNotFoundException
	 */
	List<ProdInsWsPlanStatSummaryDto> searchProdStatListByJgi(Integer jgiNo, String category) throws DataNotFoundException;

//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
	/**
	 * 施設特約店別計画の従業員情報・品目を元に、計画立案されている削除（予定）施設を取得する。<br>
	 *
	 * @param insWsPlanStatusList 施設特約店別計画ステータス
	 * @return 計画立案されている削除（予定）施設の一覧
	 */
	List<DpsKakuteiErrMsg> delInsCheck(List<InsWsPlanStatus> insWsPlanStatusList);

	/**
	 * 施設特約店別計画の従業員情報・品目を元に、計画立案されている計画立案対象外特約店を取得する。<br>
	 *
	 * @param insWsPlanStatusList 施設特約店別計画ステータス
	 * @return 計画立案されている削除（予定）施設の一覧
	 */
	public List<DpsKakuteiErrMsg> wsExceptPlanCheck(List<InsWsPlanStatus> insWsPlanStatusList);

	/**
	 * 施設特約店別計画の従業員情報・品目を元に、配分除外施設を取得する。<br>
	 *
	 * @param insWsPlanStatusList 施設特約店別計画ステータス
	 * @return 計画立案されている削除（予定）施設の一覧
	 */
	public List<DpsKakuteiErrMsg> exceptDistIncCheck(List<InsWsPlanStatus> insWsPlanStatusList);
//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応


	/**
	 * 最終更新日時を取得する。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param prodCode 品目固定コード
	 * @return 最終更新日時
	 * @throws DataNotFoundException
	 */
	Date getLastUpDate(String sosCd3, String sosCd4, String prodCode);

	/**
	 * 引数で指定された最終更新日と、現在の最終更新日を比較する。<br>
	 * 最終更新日が異なっている場合は、排他エラーをスローする。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param prodCode 品目固定コード
	 * @param orgLastUpdate 前回最終更新日時
	 * @throws OptimisticLockingFailureException 現在の最終更新日が、引数で指定された最終更新日と異なっている場合
	 */
	void checkLastUpDate(String sosCd3, String sosCd4, String prodCode, Date orgLastUpdate);

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
	 * 施設特約店別計画立案ステータスを登録する。
	 *
	 * @param record 施設特約店別計画立案ステータス
	 * @throws DuplicateException
	 */
	void insert(InsWsPlanStatus record) throws DuplicateException;

	/**
	 * 施設特約店別計画立案ステータスを更新する。
	 *
	 * @param record 施設特約店別計画立案ステータス
	 * @return 更新件数
	 */
	int update(InsWsPlanStatus record);

	/**
	 * 施設特約店別計画立案ステータスを更新する。 <br>
	 * 更新者情報を指定した実行者情報で更新する。
	 *
	 * @param record 施設特約店別計画立案ステータス
	 * @param dpUser 実行者情報
	 * @return 更新件数
	 */
	int update(InsWsPlanStatus record, DpUser dpUser);

	/**
	 * 施設特約店別計画立案ステータスを削除する。
	 *
	 * @param seqKey シーケンスキー
	 * @param upDate 更新日
	 * @return 削除件数
	 */
	int delete(Long seqKey, Date upDate);
}
