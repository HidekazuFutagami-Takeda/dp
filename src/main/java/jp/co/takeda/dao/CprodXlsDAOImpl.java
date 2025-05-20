package jp.co.takeda.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.Sales;

/**
 * 計画検討表EXCEL出力対象品目にアクセスするDAO実装クラス
 *
 * @author hayashi
 */
@Repository("cprodXlsDAO")
public class CprodXlsDAOImpl extends AbstractDao implements CprodXlsDAO {

	private static final String SQL_MAP = "VIEW_DPS_C_VI_CPROD_XLS_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 計画検討表EXCEL出力対象品目のリストを取得
	public List<PlannedProd> searchList(String sortString, Sales sales, String category, Boolean planLevelInsDoc) throws DataNotFoundException {

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("sortString", sortString);
		paramMap.put("sales", sales);
		paramMap.put("category", category);
		// ONC品制御はカテゴリで判断するため削除
//		if(BooleanUtils.isTrue(isOncSos)){
//			paramMap.put("mrCat", MrCat.ONC_MR);
//		}
		paramMap.put("planLevelInsDoc", planLevelInsDoc);
		// 固定条件
		paramMap.put("planTargetFlg", true);

		// ----------------------
		// 検索実行
		// ----------------------
		List<PlannedProd> result = dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
		return result;
	}

}
