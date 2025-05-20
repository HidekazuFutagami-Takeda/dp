package jp.co.takeda.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.model.MrPlanFreeIndex;

/**
 * 担当者別計画フリー項目にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface MrPlanFreeIndexDao {

	/**
	 * フリー項目識別番号：1
	 */
	public static final String FREE_INDEX_NO_1 = "1";
	
	/**
	 * フリー項目識別番号：2
	 */
	public static final String FREE_INDEX_NO_2 = "2";
	
	/**
	 * フリー項目識別番号：3
	 */
	public static final String FREE_INDEX_NO_3 = "3";
	
	/**
	 * 担当者別計画フリー項目を取得する。
	 * 
	 * @param seqKey シーケンスキー
	 * @return 担当者別計画フリー項目
	 */
	MrPlanFreeIndex search(Long seqKey) throws DataNotFoundException;

	/**
	 * 組織コード、品目固定コードを指定して、担当者別計画フリー項目を取得する。
	 * 
	 * @param sosCd3 営業所組織コード(必須)
	 * @param sosCd4 チーム組織コード
	 * @param prodCode 品目固定コード(必須)
	 * @return 担当者別計画フリー項目
	 */
	List<MrPlanFreeIndex> searchListBySosCd(String sosCd3, String sosCd4, String prodCode) throws DataNotFoundException;

	/**
	 * 組織コード、品目固定コードを指定して、担当者別計画フリー項目の最終更新情報を取得する。
	 * 
	 * @param sosCd3 営業所組織コード
	 * @param prodCode 品目固定コード
	 * @return 最終更新日
	 * @throws DataNotFoundException
	 */
	MrPlanFreeIndex getLastUpDate(String sosCd3, String prodCode) throws DataNotFoundException;

	/**
	 * 品目コード、組織コードから、フリー項目の集計を取得
	 * 
	 * @param sosCd3 組織コード
	 * @param prodCode 品目固定コード
	 * @return フリー項目集計結果
	 * @throws DataNotFoundException
	 */
	Map<String, Object> sumFreeIndexByProdSos(String sosCd3, String prodCode)  throws DataNotFoundException;

	/**
	 * 担当者別計画フリー項目を登録する。
	 * 
	 * @param record 担当者別計画フリー項目
	 * @throws DuplicateException
	 */
	void insert(MrPlanFreeIndex record) throws DuplicateException;

	/**
	 * 担当者別計画フリー項目を更新する。
	 * 
	 * @param record 担当者別計画フリー項目
	 * @return 更新件数
	 */
	int update(MrPlanFreeIndex record);

	/**
	 * 担当者別計画フリー項目を削除する。
	 * 
	 * @param seqKey シーケンスキー
	 * @param upDate 更新日
	 * @return 削除件数
	 */
	int delete(Long seqKey, Date upDate);
}
