package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.view.EstimationProd;

/**
 * 試算対象品目一覧取得インターフェイス
 *
 * @author nozaki
 */
public interface EstimationProdDao {

	/**
	 * 営業所組織コードから、試算対象品目一覧を取得する。
	 *
	 * @param sosCd 組織コード
	 * @param category カテゴリ
	 * @return 試算対象品目一覧
	 * @throws DataNotFoundException
	 */
	List<EstimationProd> searchList(String sosCd, String category, Sales sales) throws DataNotFoundException;
//	List<EstimationProd> searchList(String sosCd, List<CodeAndValue> categorylist) throws DataNotFoundException;

}
