package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.view.MikakutokuSijouInsTypeSummary;

/**
 * 未獲得市場の施設出力対象区分別集計を取得するDAOインターフェイス
 * 
 * @author nozaki
 */
public interface MikakutokuSijouInsTypeSummaryDao {

	/**
	 * 組織コード・試算品目コードより、修正金額を施設出力対象区分別に集計し、取得する。
	 * 
	 * @param sosCd3 組織コード(営業所)
	 * @param refProdCode 試算品目コード
	 * @return 未獲得市場の施設出力対象区分別集計のリスト
	 * @throws DataNotFoundException
	 */
	List<MikakutokuSijouInsTypeSummary> searchSummaryList(String sosCd3, String refProdCode) throws DataNotFoundException;

}
