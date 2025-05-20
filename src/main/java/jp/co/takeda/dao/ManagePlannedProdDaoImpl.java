package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.model.ManagePlannedProd;
import jp.co.takeda.model.div.ProdCategory;
import jp.co.takeda.model.div.Sales;

/**
 * 管理の計画対象品目にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("managePlannedProdDao")
public class ManagePlannedProdDaoImpl extends AbstractManageDao implements ManagePlannedProdDao {

	private static final String SQL_MAP = "DPM_C_PLANNED_PROD_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 計画対象品目を取得
	public ManagePlannedProd search(String prodCode) throws DataNotFoundException {
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

		// ----------------------
		// 検索実行
		// ----------------------
		return (ManagePlannedProd) super.select(getSqlMapName() + ".select", paramMap);
	}

	// 計画対象品目のリストを取得
	public List<ManagePlannedProd> searchList(String sortString, Sales sales, ProdCategory category, ProdPlanLevel planLevel) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (planLevel == null) {
			final String errMsg = "計画立案レベルがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("sortString", sortString);
		paramMap.put("sales", sales);
		paramMap.put("category", category);
		switch (planLevel) {
			case ZENSHA:
				paramMap.put("planLevelZensha", true);
				break;
			case SITEN:
				paramMap.put("planLevelSiten", true);
				break;
			case OFFICE:
				paramMap.put("planLevelOffice", true);
				break;
			case TEAM:
				paramMap.put("planLevelTeam", true);
				break;
			case MR:
				paramMap.put("planLevelMr", true);
				break;
			case INS:
				paramMap.put("planLevelIns", true);
				break;
			case INS_WS:
				paramMap.put("planLevelInsWs", true);
				break;
			case WS:
				paramMap.put("planLevelWs", true);
				break;
			case ALL:
				break;
		}

		// ----------------------
		// 検索実行
		// ----------------------
		List<ManagePlannedProd> result = super.selectList(getSqlMapName() + ".selectList", paramMap);
		return result;
	}

	// 計画対象品目を取得
	public List<ManagePlannedProd> searchListByCateDataSeq(String category) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("category", category);

		// ----------------------
		// 検索実行
		// ----------------------
		List<ManagePlannedProd> result = super.selectList(getSqlMapName() + ".selectListByCateDataSeq", paramMap);
		return result;
	}

}
