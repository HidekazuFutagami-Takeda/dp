package jp.co.takeda.dao;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.MrPlanStatus;
import jp.co.takeda.security.DpUser;

/**
 * 担当者別計画立案ステータスにアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface MrPlanStatusDao {

	/**
	 * ソート順<br>
	 * グループコード 昇順<br>
	 * 統計品名コード(品目)　昇順<br>
	 * 品目固定コード　昇順
	 */
	static final String SORT_STRING = "ORDER BY GROUP_CODE, STAT_PROD_CODE, PROD_CODE";

	/**
	 * 担当者別計画立案ステータスを取得する。
	 *
	 * @param seqKey シーケンスキー
	 * @return 担当者別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	MrPlanStatus search(Long seqKey) throws DataNotFoundException;

	/**
	 * 組織コード、品目固定コードを元に、担当者別計画立案ステータスを取得する。
	 *
	 * @param sosCd 組織コード
	 * @param prodCode 品目固定コード
	 * @return 担当者別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	MrPlanStatus search(String sosCd, String prodCode) throws DataNotFoundException;

	/**
	 * 組織コードを元に、担当者別計画立案ステータスの一覧を取得する。<br>
	 * ソート順は品目ソート順
	 *
	 * @param sosCd 組織コード
	 * @return 担当者別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	List<MrPlanStatus> searchList(String sosCd) throws DataNotFoundException;

	/**
	 * 組織コード、品目固定コードを元に、担当者別計画立案ステータスを取得する。
	 *
	 * @param sosCd 組織コード
	 * @param prodCode 品目固定コード
	 * @return 担当者別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	List<MrPlanStatus> searchSosCdProdList(String sosCd, String prodCode) throws DataNotFoundException;

	/**
	 * 指定サーバ区分に該当する試算中の担当者別計画立案ステータスリストを取得する。
	 *
	 * @param sortString ソート順
	 * @param appServerKbn サーバ区分
	 * @return 担当者別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	List<MrPlanStatus> searchEstimatingList(String sortString, String appServerKbn) throws DataNotFoundException;

	/**
	 * 従業員一覧、品目固定コード一覧を元に担当者別計画を取得する。
	 *
	 * @param sosCd 組織コード
	 * @param prodList 品目固定コード一覧
	 * @throws DataNotFoundException
	 */
	List<MrPlanStatus> searchSosCdProdList(String sosCd, List<String> prodList ) throws DataNotFoundException;

	/**
	 * 担当者別計画立案ステータスを登録する。
	 *
	 * @param record 担当者別計画立案ステータス
	 * @throws DuplicateException
	 */
	void insert(MrPlanStatus record) throws DuplicateException;

	/**
	 * 担当者別計画立案ステータスを更新する。
	 *
	 * @param record 担当者別計画立案ステータス
	 * @return 更新件数
	 */
	int update(MrPlanStatus record);

	/**
	 * 担当者別計画立案ステータスを更新する。<br>
	 * 更新者情報を指定した実行者情報で更新する。
	 *
	 * @param record 担当者別計画立案ステータス
	 * @param dpUser 実行者情報
	 * @return 更新件数
	 */
	int update(MrPlanStatus record, DpUser dpUser);

	/**
	 * 担当者別計画立案ステータスを削除する。
	 *
	 * @param seqKey シーケンスキー
	 * @param upDate 更新日
	 * @return 削除件数
	 */
	int delete(Long seqKey, Date upDate);
}
