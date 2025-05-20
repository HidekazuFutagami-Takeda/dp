package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.RefTotalProd;

@Repository("refTotalProdDAO")
public class RefTotalProdDAOImpl extends AbstractDao implements RefTotalProdDAO {

	private static final String SQL_MAP = "DPS_C_REF_TOTAL_PROD_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 参照用集計品目情報を取得
	public RefTotalProd search(String prodCd) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCd == null) {
			final String errMsg = "品目コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("prodCode", prodCd);
		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".selectUk", paramMap);
	}
}