package jp.co.takeda.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.view.SosStatusSummaryForVac;

import org.springframework.stereotype.Repository;

/**
 * (ワクチン)組織単位のステータスのサマリーを取得するDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("sosStatusSummaryForVacDao")
public class SosStatusSummaryForVacDaoImpl extends AbstractDao implements SosStatusSummaryForVacDao {

	private static final String SQL_MAP = "VIEW_SosStatusSummaryForVac_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// ステータスのサマリーを取得
	public SosStatusSummaryForVac search() throws DataNotFoundException {
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 固定条件
		paramMap.put("planTargetFlg", true);
		paramMap.put("sales", Sales.VACCHIN);
		List<JgiKb> jgiKbList = new ArrayList<JgiKb>();
		jgiKbList.add(JgiKb.JGI);
		jgiKbList.add(JgiKb.CONTRACT_MR);
		paramMap.put("jgiKbList", jgiKbList);
		paramMap.put("jokenSetList", new JokenSet[]{ JokenSet.WAKUTIN_MR });
		return dataAccess.queryForObject(getSqlMapName() + ".select", paramMap);
	}
}
