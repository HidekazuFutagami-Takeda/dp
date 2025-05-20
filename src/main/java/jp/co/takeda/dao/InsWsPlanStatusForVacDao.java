package jp.co.takeda.dao;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.dto.ProdInsWsPlanStatSummaryDto;
import jp.co.takeda.model.InsWsPlanStatusForVac;
import jp.co.takeda.security.DpUser;

import org.springframework.dao.OptimisticLockingFailureException;

/**
 * ワクチン用施設特約店別計画にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface InsWsPlanStatusForVacDao {

	/**
	 * ソート順<br>
	 * 品目ソート　昇順
	 * 
	 */
	static final String SORT_STRING = "ORDER BY GROUP_CODE, STAT_PROD_CODE, PROD_CODE";

	/**
	 * ソート順<br>
	 * 品目ソート　昇順<br>
	 * 組織ソート　昇順<br>
	 * 従業員ソート　昇順<br>
	 */
	static final String SORT_STRING2 = "ORDER BY GROUP_CODE, STAT_PROD_CODE, PROD_CODE" + ", BR_CODE, DIST_SEQ, DIST_CODE, TEAM_CODE, SHOKUSEI_CD, SHOKUSHU_CD,JGI_NO";

	/**
	 * ワクチン用施設特約店別計画を取得する。
	 * 
	 * @param seqKey シーケンスキー
	 * @return ワクチン用施設特約店別計画
	 * @throws DataNotFoundException
	 */
	InsWsPlanStatusForVac search(Long seqKey) throws DataNotFoundException;

	/**
	 * 従業員番号、品目固定コードを元に、ワクチン用施設特約店別計画立案ステータスを取得する。
	 * 
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @return ワクチン用施設特約店別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	InsWsPlanStatusForVac search(Integer jgiNo, String prodCode) throws DataNotFoundException;

	/**
	 * 従業員番号を元に、ワクチン用施設特約店別計画立案ステータスを取得する。
	 * 
	 * @param sortString ソート条件
	 * @param jgiNo 従業員番号
	 * @return ワクチン用施設特約店別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	List<InsWsPlanStatusForVac> searchList(String sortString, Integer jgiNo) throws DataNotFoundException;

	/**
	 * 組織コード、品目を元に、ワクチン用施設特約店別計画立案ステータスを取得する。
	 * 
	 * @param sortString ソート条件
	 * @param sosCd3 組織コード(特約店G)【NULL可】
	 * @param sosCd4 組織コード(チーム)【NULL可】
	 * @param prodCode 品目固定コード【NULL可】
	 * @return ワクチン用施設特約店別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	List<InsWsPlanStatusForVac> searchList(String sortString, String sosCd3, String sosCd4, String prodCode) throws DataNotFoundException;

	/**
	 * 品目固定コードを元に、ワクチン用施設特約店別計画立案ステータスを取得する。<br>
	 * 従業員をベースとし、ステータスが存在しないレコードも取得する。<br>
	 * ソートは組織・従業員ソート順固定
	 * 
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(特約店G)【NULL可】
	 * @param sosCd4 組織コード(チーム)【NULL可】
	 * @return ワクチン用施設特約店別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	List<InsWsPlanStatusForVac> searchJgiBaseList(String prodCode, String sosCd3, String sosCd4) throws DataNotFoundException;

	/**
	 * 指定サーバ区分に該当する配分中の施設特約店別計画立案ステータスリストを取得する。 <br>
	 * 
	 * @param sortString ソート順
	 * @param appServerKbn サーバ区分
	 * @return 施設特約店別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	List<InsWsPlanStatusForVac> searchDistingList(String sortString, String appServerKbn) throws DataNotFoundException;

	/**
	 * 最終更新日時を取得する。
	 * 
	 * @param prodCode 品目固定コード
	 * @return 最終更新日時
	 * @throws DataNotFoundException
	 */
	Date getLastUpDate(String prodCode);

	/**
	 * 最終更新日時を取得する。
	 * 
	 * @param sosCd3 組織コード(特約店G)【NULL可】
	 * @param sosCd4 組織コード(チーム)【NULL可】
	 * @param prodCode 品目固定コード
	 * @return 最終更新日時
	 */
	Date getLastUpDate(String sosCd3, String sosCd4, String prodCode);

	/**
	 * 引数で指定された最終更新日と、現在の最終更新日を比較する。<br>
	 * 最終更新日が異なっている場合は、排他エラーをスローする。
	 * 
	 * @param prodCode 品目固定コード
	 * @param orgLastUpdate 前回最終更新日時
	 * @throws OptimisticLockingFailureException 現在の最終更新日が、引数で指定された最終更新日と異なっている場合
	 */
	void checkLastUpDate(String prodCode, Date orgLastUpdate) throws OptimisticLockingFailureException;

	/**
	 * 引数で指定された最終更新日と、現在の最終更新日を比較する。<br>
	 * 最終更新日が異なっている場合は、排他エラーをスローする。
	 * 
	 * @param sosCd3 組織コード(特約店G)【NULL可】
	 * @param sosCd4 組織コード(チーム)【NULL可】
	 * @param prodCode 品目固定コード
	 * @param orgLastUpdate 前回最終更新日時
	 * @throws OptimisticLockingFailureException 現在の最終更新日が、引数で指定された最終更新日と異なっている場合
	 */
	void checkLastUpDate(String sosCd3, String sosCd4, String prodCode, Date orgLastUpdate) throws OptimisticLockingFailureException;

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
	 * 組織コードを指定して、品目別の施設特約店別計画サマリを取得する。
	 * 
	 * @param sosCd3 組織コード(特約店G)
	 * @return 施設特約店別計画サマリのリスト
	 * @throws DataNotFoundException
	 */
	List<ProdInsWsPlanStatSummaryDto> searchProdStatListBySos(String sosCd3) throws DataNotFoundException;

	/**
	 * 従業員番号を指定して、品目別の施設特約店別計画サマリを取得する。
	 * 
	 * @param jgiNo 組織コード(特約店G)
	 * @return 施設特約店別計画サマリのリスト
	 * @throws DataNotFoundException
	 */
	List<ProdInsWsPlanStatSummaryDto> searchProdStatListByJgi(Integer jgiNo) throws DataNotFoundException;

	/**
	 * ワクチン用施設特約店別計画を登録する。
	 * 
	 * @param record ワクチン用施設特約店別計画
	 * @throws DuplicateException
	 */
	void insert(InsWsPlanStatusForVac record) throws DuplicateException;

	/**
	 * ワクチン用施設特約店別計画を更新する。
	 * 
	 * @param record ワクチン用施設特約店別計画
	 * @return 更新件数
	 */
	int update(InsWsPlanStatusForVac record);

	/**
	 * ワクチン用施設特約店別計画を更新する。
	 * 
	 * @param record ワクチン用施設特約店別計画
	 * @param dpUser 更新者情報
	 * @return 更新件数
	 */
	int update(InsWsPlanStatusForVac record, DpUser dpUser);

	/**
	 * ワクチン用施設特約店別計画を削除する。
	 * 
	 * @param seqKey シーケンスキー
	 * @param upDate 更新日
	 * @return 削除件数
	 */
	int delete(Long seqKey, Date upDate);
}
