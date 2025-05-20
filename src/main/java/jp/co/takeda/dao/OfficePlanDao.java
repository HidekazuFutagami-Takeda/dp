package jp.co.takeda.dao;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.OfficePlan;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.view.OfficePlanWithChosei;

/**
 * 営業所計画にアクセスするDAOインターフェイス
 *
 * @author khashimoto
 */
public interface OfficePlanDao {

	/**
	 * ソート順<br>
	 * グループコード 昇順<br>
	 * 統計品名コード(品目)　昇順<br>
	 * 品目固定コード　昇順
	 */
	static final String SORT_STRING = "ORDER BY GROUP_CODE, STAT_PROD_CODE, PROD_CODE";

	/**
	 * ソート順<br>
	 * 計画支援汎用マスタ(sort_prod) 昇順
	 * グループコード 昇順<br>
	 * 統計品名コード(品目)　昇順<br>
	 * 品目固定コード　昇順
	 */
	static final String SORT_STRING2 = "ORDER BY DATA_SEQ, GROUP_CODE, STAT_PROD_CODE, PROD_CODE";

	/**
	 * 営業所計画を組織コード、品目固定コードで取得する。
	 *
	 * @param sosCd3 組織コード(営業所)【必須】
	 * @param prodCode 品目固定コード【必須】
	 * @return 営業所計画
	 * @throws DataNotFoundException
	 */
	OfficePlan searchUk(String sosCd3, String prodCode) throws DataNotFoundException;

	/**
	 * 営業所計画のリストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param sosCd3 組織コード(営業所)【必須】
	 * @param category カテゴリ 【必須】
	 * @return 営業所計画のリスト
	 * @throws DataNotFoundException
	 */
//	List<OfficePlan> searchList(String sortString, String sosCd3, String category) throws DataNotFoundException;
	List<OfficePlan> searchList(String sortString, String sosCd3, String category, Sales sales) throws DataNotFoundException;

// add start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
	/**
	 * 帳票用営業所計画のリストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param sosCd3 組織コード(営業所)【必須】
	 * @param category カテゴリ 【必須】
	 * @return 営業所計画のリスト
	 * @throws DataNotFoundException
	 */
	List<OfficePlan> searchListReport(String sortString, String sosCd3, String category) throws DataNotFoundException;
// add end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定

	/**
	 * 営業所計画を登録する。
	 *
	 * @param record 営業所計画
	 * @throws DuplicateException
	 */
	void insert(OfficePlan record) throws DuplicateException;

	/**
	 * 営業所計画を更新する。
	 *
	 * @param record 営業所計画
	 * @return 更新件数
	 */
	int update(OfficePlan record);

	/**
	 * 営業所計画を削除する。
	 *
	 * @param seqKey シーケンスNo
	 * @param upDate 更新日時
	 * @return 削除件数
	 */
	int delete(Long seqKey, Date upDate);

	/**
	 * 営業所調整が付加された営業所計画のリストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param sosCd3 組織コード(営業所)【必須】
	 * @param category カテゴリ 【必須】
	 * @return 営業所計画と営業所調整のリスト
	 * @throws DataNotFoundException
	 */
	List<OfficePlanWithChosei> searchList2(String sortString, String sosCd3, String category, Sales sales) throws DataNotFoundException;

}
