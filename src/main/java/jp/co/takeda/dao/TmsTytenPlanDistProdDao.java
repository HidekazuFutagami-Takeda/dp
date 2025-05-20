package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.view.TmsTytenPlanDistProd;

/**
 * 特約店別計画配分対象品目を取得するDAOインターフェイス
 *
 * @author yokokawa
 */
public interface TmsTytenPlanDistProdDao {
	/**
	 * ソート順<br>
	 * グループコード 昇順<br>
	 * 統計品名コード(品目)　昇順<br>
	 * 品目固定コード　昇順
	 */
	static final String SORT_STRING = "ORDER BY DATA_SEQ, GROUP_CODE, STAT_PROD_CODE, PROD_CODE";

	/**
	 * 組織コード(支店)とカテゴリーを元に特約店別計画配分対象品目を取得する。
	 *
	 * @param sortString ソート条件
	 * @param sosCd2 組織コード(支店)
	 * @param category カテゴリー
	 * @throws DataNotFoundException
	 */
	public List<TmsTytenPlanDistProd> searchTmsTytenPlanDistProd(String sortString, String sosCd2, String category, String sales) throws DataNotFoundException;
}
