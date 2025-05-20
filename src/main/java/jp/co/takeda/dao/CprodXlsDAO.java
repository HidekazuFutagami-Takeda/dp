package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.Sales;

/**
 * 計画検討表EXCEL出力対象品目にアクセスするDAOインターフェイス
 *
 * @author hayashi
 */
public interface CprodXlsDAO {

	/**
	 * ソート順<br>
	 * グループコード 昇順<br>
	 * 統計品名コード(品目)　昇順<br>
	 * 品目固定コード　昇順
	 */
	// mod start 2021/8/19 h.Kaneko
	//static final String SORT_STRING = "ORDER BY MIN(SORT_ID)";
	static final String SORT_STRING = "ORDER BY DATA_SEQ";
	// mod end 2021/8/19

	/**
	 * 計画検討表EXCEL出力対象品目のリストを取得する。
	 *
	 * @param sortString ソート条件
	 * @param sales 売上所属(NULL可)
	 * @param category カテゴリ(NULL可)
	 * @param isOncSos ONC組織か(NULL可)
	 * @param planLevelInsDoc 重点品のみ取得(NULL可)
	 * @return 計画対象品目のリスト
	 * @throws DataNotFoundException
	 */
	List<PlannedProd> searchList(String sortString, Sales sales, String category, Boolean planLevelInsDoc) throws DataNotFoundException;

}
