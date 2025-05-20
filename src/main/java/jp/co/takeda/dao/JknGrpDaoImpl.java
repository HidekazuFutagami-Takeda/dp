package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.JknGrp;

/**
 * 管理の担当者別計画にアクセスするDAO実装クラス
 *
 * @author yyoshino
 */
@Repository("jknGrpDao")
public class JknGrpDaoImpl extends AbstractDao implements JknGrpDao {

	private static final String SQL_MAP = "DPM_C_JKN_GRP_SqlMap";

	protected String getSqlMapName() {
		return SQL_MAP;
	}

	@Override
	public List<JknGrp> searchListByJgiNo(Integer jgiNo) throws DataNotFoundException {
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
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("jgiNo", jgiNo);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".searchListByJgiNo", paramMap);

	}


}
