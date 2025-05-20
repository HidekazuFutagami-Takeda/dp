package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.InsJgiSos;

/**
 *  同じ組織配下の施設担当者DAO実装クラス
 */
public interface DpmInsJgiSosDao {

	/**
	 * 組織配下COC_CD3が同一の施設担当者のリストを取得する。<br>
	 * 組織コード・施設コードの設定必須。
	 *
	 * @param insNo 施設情報の検索条件
	 * @param sosCd2 組織情報の検索条件
	 * @return 同じ組織配下に属するの施設担当者のリスト
	 * @throws DataNotFoundException
	 */
	List<InsJgiSos> searchBanch(String insNo, String sosCd2) throws DataNotFoundException;

	/**
	 * 組織配下COC_CD3が同一の施設担当者のリストを取得する。<br>
	 * 組織コード・施設コードの設定必須。
	 *
	 * @param insNo 施設情報の検索条件
	 * @param sosCd3 組織情報の検索条件
	 * @return 同じ組織配下に属するの施設担当者のリスト
	 * @throws DataNotFoundException
	 */
	List<InsJgiSos> searchOffice(String insNo, String sosCd3) throws DataNotFoundException;
}
