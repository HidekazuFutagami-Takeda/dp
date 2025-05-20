package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.view.InsWsPlanForVacSummary;

import org.springframework.stereotype.Repository;

/**
 * 施設特約店別計画の市区町村別サマリーを取得するDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("insWsPlanForVacSummaryDao")
public class InsWsPlanForVacSummaryDaoImpl extends AbstractDao implements InsWsPlanForVacSummaryDao {

	private static final String SQL_MAP = "VIEW_InsWsPlanForVacSummary_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 業員番号を元に、施設特約店別計画の市区町村別サマリーリストを取得
	public List<InsWsPlanForVacSummary> searchList(String sortString, Integer jgiNo) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(6);
		paramMap.put("sortString", sortString);
		paramMap.put("jgiNo", jgiNo);
		// 固定条件
		paramMap.put("planTargetFlg", true);
		paramMap.put("sales", Sales.VACCHIN);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}
}
