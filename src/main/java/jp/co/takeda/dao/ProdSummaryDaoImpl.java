package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.div.ProdCategory;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.view.ProdSummary;

/**
 * 簡易版品目一覧を取得するためのDAO実装クラス
 *
 * @author tkawabata
 */
@Repository("prodSummaryDao")
public class ProdSummaryDaoImpl extends AbstractDao implements ProdSummaryDao {

	private static final String SQL_MAP = "VIEW_ProdSummary_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 簡易版品目を取得(参照品目を含む)
	public ProdSummary search(String prodCode) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("prodCode", prodCode);

		return dataAccess.queryForObject(getSqlMapName() + ".select", paramMap);
	}

	// 簡易版品目一覧を取得(参照品目を含まない)
	public List<ProdSummary> searchList(String sortString, Sales sales, ProdCategory[] category) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sales == null) {
			final String errMsg = "売上所属がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("sales", sales.getDbValue());
		if (category != null) {
			paramMap.put("category", category);
		}
		paramMap.put("sortString", sortString);
		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}


	// 簡易版品目一覧を取得(参照品目を含まない)
	public List<ProdSummary> searchList(String sortString, Sales sales, String category) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sales == null) {
			final String errMsg = "売上所属がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("sales", sales.getDbValue());
		if (category != null) {
			paramMap.put("category", category);
		}
		paramMap.put("sortString", sortString);
		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}

	// 簡易版品目一覧を取得(参照品目を含まない)
	public List<ProdSummary> searchList(String sortString, Sales sales, ProdCategory[] category, boolean isPriorityOnly) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sales == null) {
			final String errMsg = "売上所属がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("sales", sales.getDbValue());
		if(isPriorityOnly){
			paramMap.put("planLevelInsDoc", "1");
		}
		if (category != null) {
			paramMap.put("category", category);
		}
		paramMap.put("sortString", sortString);
		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}

	// 参照品目を含む簡易版品目一覧を取得
	public List<ProdSummary> searchRefList(String sortString, Sales sales, ProdCategory[] category) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sales == null) {
			final String errMsg = "売上所属がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("sales", sales.getDbValue());
		if (category != null) {
			paramMap.put("category", category);
		}
		paramMap.put("sortString", sortString);
		return dataAccess.queryForList(getSqlMapName() + ".selectRefList", paramMap);
	}

	// 参照品目を含む簡易版品目一覧を取得
	public List<ProdSummary> searchRefList(String sortString, Sales sales, String category) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sales == null) {
			final String errMsg = "売上所属がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("sales", sales.getDbValue());
		if (category != null) {
			paramMap.put("category", category);
		}
		paramMap.put("sortString", sortString);
		return dataAccess.queryForList(getSqlMapName() + ".selectRefList", paramMap);
	}
}
