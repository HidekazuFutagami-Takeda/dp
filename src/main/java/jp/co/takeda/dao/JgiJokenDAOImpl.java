package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.JgiJoken;
import jp.co.takeda.model.div.JokenSet;

import org.springframework.stereotype.Repository;

/**
 * 従業員別条件テーブル当月にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("jgiJokenDAO")
public class JgiJokenDAOImpl extends AbstractDao implements JgiJokenDAO {

	private static final String SQL_MAP = "DPS_S_SY_M_JGI_JOKEN_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 従業員番号を元に、条件セットのリストを取得
	public List<JgiJoken> searchByJgiNo(Integer jgiNo) throws DataNotFoundException {
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
		Map<String, Integer> paramMap = new HashMap<String, Integer>(1);
		paramMap.put("jgiNo", jgiNo);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectByJgiNo", paramMap);
	}

	// 従業員番号、取得対象の条件セット(候補)を元に、条件セットのリストを取得
	public List<JokenSet> searchJokenSet(Integer jgiNo, JokenSet[] targetJokenSetList) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (targetJokenSetList == null || targetJokenSetList.length == 0) {
			final String errMsg = "取得対象の条件セットがnullまたは空";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("jokenSetList", targetJokenSetList);

		// ----------------------
		// 検索実行
		// ----------------------
		List<JgiJoken> jgiJokenList = dataAccess.queryForList(getSqlMapName() + ".selectJokenSet", paramMap);
		List<JokenSet> resultList = new ArrayList<JokenSet>(jgiJokenList.size());
		for (JgiJoken jgiJoken : jgiJokenList) {
			resultList.add(jgiJoken.getJokenSet());
		}
		return resultList;
	}
}
