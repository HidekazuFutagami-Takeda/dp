package jp.co.takeda.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.CheckTool;

/**
 * 計画立案準備の営業所計画アップロード用のフォーマットファイルサマリー情報を取得するDAO実装クラス
 *
 * @author ksuzuki
 */
@Repository("checkToolDao")
public class CheckToolDaoImpl extends AbstractDao implements CheckToolDao {

	private static final String SQL_MAP = "DPS_I_INS_WS_PLAN_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 営業所計画アップロード用のフォーマットファイルサマリー情報リストを取得
	public List<CheckTool> searchList(String sortString) throws DataNotFoundException {


		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();

		paramMap.put("sortString", sortString);

		return dataAccess.queryForList(getSqlMapName() + ".selectCheckTool", paramMap);

	}
}
