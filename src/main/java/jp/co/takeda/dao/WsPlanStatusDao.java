package jp.co.takeda.dao;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.WsPlanStatus;
import jp.co.takeda.model.div.Sales;

/**
 * 特約店別計画立案ステータスにアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface WsPlanStatusDao {

	/**
	 * ソート順<br>
	 * 組織コード 昇順<br>
	 * 品目固定コード　昇順
	 */
	static final String SORT_STRING = "ORDER BY SOS_CD, PROD_CODE";

	/**
	 * 特約店別計画立案ステータスを取得する。
	 *
	 * @param seqKey シーケンスキー
	 * @return 特約店別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	WsPlanStatus search(Long seqKey) throws DataNotFoundException;

	/**
	 * 組織コード、品目固定コードを元に、施設特約店別計画立案ステータスを取得する。
	 *
	 * @param sosCd 組織コード
	 * @param prodCode 品目固定コード
	 * @return 施設特約店別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	WsPlanStatus search(String sosCd, String prodCode) throws DataNotFoundException;

	/**
	 * 指定サーバ区分に該当する配分中の施設特約店別計画立案ステータスリストを取得する。
	 *
	 * @param appServerKbn サーバ区分
	 * @return 施設特約店別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	List<WsPlanStatus> searchDistingList(String sortString, String appServerKbn) throws DataNotFoundException;

	/**
	 * カテゴリを元に、施設特約店別計画立案ステータスを取得する。 <br>
	 * カテゴリがnullの場合は、自社品、仕入品両方。
	 *
	 * @param sortString ソート順
	 * @param prodCategory カテゴリ(null可)
	 * @return 施設特約店別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	List<WsPlanStatus> searchList(String sortString, String prodCategory) throws DataNotFoundException;

	/**
	 * 組織コード(支店)とカテゴリを元に、施設特約店別計画立案ステータスを取得する。 <br>
	 * カテゴリがnullの場合は、自社品、仕入品両方。
	 *
	 * @param sortString ソート順
	 * @param sales 売上所属
	 * @param sosCd 組織コード(支店,null可)
	 * @param prodCategory カテゴリ(null可)
	 * @return 施設特約店別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	List<WsPlanStatus> searchListBySosCategory(String sortString, Sales sales, String sosCd, String prodCategory) throws DataNotFoundException;

	/**
	 * 組織コード(支店)と品目固定コードを元に、特約店別計画立案ステータスを取得する。 <br>
	 * 品目固定コードがnullの場合は、すべての品目。
	 *
	 * @param prodCategory カテゴリ(null可)
	 * @return 施設特約店別計画立案ステータス
	 * @throws DataNotFoundException
	 */
	List<WsPlanStatus> searchList(String sortString, String sosCd, List<String> prodCode) throws DataNotFoundException;

	/**
	 * 特約店別計画立案ステータスを登録する。
	 *
	 * @param record 特約店別計画立案ステータス
	 * @throws DuplicateException
	 */
	void insert(WsPlanStatus record) throws DuplicateException;

	/**
	 * 特約店別計画立案ステータスを更新する。
	 *
	 * @param record 特約店別計画立案ステータス
	 * @return 更新件数
	 */
	int update(WsPlanStatus record);

	/**
	 * 特約店別計画立案ステータスを削除する。
	 *
	 * @param seqKey シーケンスキー
	 * @param upDate 更新日
	 * @return 削除件数
	 */
	int delete(Long seqKey, Date upDate);
}
