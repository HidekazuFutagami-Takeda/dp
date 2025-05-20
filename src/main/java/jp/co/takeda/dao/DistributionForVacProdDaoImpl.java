package jp.co.takeda.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.view.DistributionForVacProd;

import org.springframework.stereotype.Repository;

/**
 * (ワクチン)配分対象品目一覧を取得するDAO実装クラス
 * 
 * @author nozaki
 */
@Repository("distributionForVacProdDao")
public class DistributionForVacProdDaoImpl extends AbstractDao implements DistributionForVacProdDao {

	private static final String SQL_MAP = "VIEW_DistributionForVacProd_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// (ワクチン)配分対象品目一覧を取得
	public List<DistributionForVacProd> searchList() throws DataNotFoundException {

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(2);

		// 固定条件
		paramMap.put("sales", Sales.VACCHIN);
		paramMap.put("planTargetFlg", true);
		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}
}
