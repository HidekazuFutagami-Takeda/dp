package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.view.EstimationProd;

/**
 * 試算対象品目一覧を取得するDAO実装クラス
 *
 * @author nozaki
 */
@Repository("estimationProdDao")
public class EstimationProdDaoImpl extends AbstractDao implements EstimationProdDao {

	private static final String SQL_MAP = "VIEW_EstimationProd_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 営業所組織コードから、試算対象品目一覧を取得
	public List<EstimationProd> searchList(String sosCd, String category, Sales sales) throws DataNotFoundException {
//		public List<EstimationProd> searchList(String sosCd, List<CodeAndValue> categorylist) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("sosCd", sosCd);
		// 固定条件
		paramMap.put("category", category);
		paramMap.put("sales", sales);
//		paramMap.put("categorylist", categorylist);
		paramMap.put("planTargetFlg", true);
		// ONC品はカテゴリで判断するため、削除
//		if(BooleanUtils.isTrue(isOncSos)){
//			paramMap.put("mrCat", MrCat.ONC_MR);
//		}
		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}
}
