package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.div.JokenSet;

import org.springframework.stereotype.Repository;

/**
 * 従業員情報にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("mainSubRelationDAO")
public class MainSubRelationDAOImpl extends AbstractDao implements MainSubRelationDAO {

	private static final String SQL_MAP = "DPS_I_MAIN_SUB_RELATION_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 従業員取得
	public List<JgiMst> searchList(Integer jgiNo) throws DataNotFoundException {
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
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}
}
