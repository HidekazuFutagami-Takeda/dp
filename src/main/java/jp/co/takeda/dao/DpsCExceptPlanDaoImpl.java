package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.ExceptPlan;

@Repository("dpsCExceptPlanDao")
public class DpsCExceptPlanDaoImpl extends AbstractDao implements DpsCExceptPlanDao {
	private static final String SQL_MAP = "DPS_C_EXCEPT_PLAN_SqlMap";
	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}
	@Override
	public int countExceptPlan(String sosCd) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sosCd", sosCd);

		// ----------------------
		// 検索実行
		// ----------------------

		try {
			return dataAccess.queryForObject(getSqlMapName() + ".countExceptPlan", paramMap);
		} catch (DataNotFoundException e) {
			e.printStackTrace();
			throw new SystemException(new Conveyance(ErrMessageKey.LOGICAL_ERROR,"Count(*)なのに取得できないことはあり得ない"));
		}
	}
	@Override
	public List<ExceptPlan> searchExceptPlan(String sosCd, String category) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sosCd", sosCd);
		paramMap.put("category", category);

		// ----------------------
		// 検索実行
		// ----------------------

		try {
			return dataAccess.queryForList(getSqlMapName() + ".selectExceptPlan", paramMap);
		} catch (DataNotFoundException e) {
			e.printStackTrace();
			throw new SystemException(new Conveyance(ErrMessageKey.LOGICAL_ERROR,"あり リンクからきて、ここでDataNotFoundはあり得ない"));
		}
	}

	// 削除（予定）施設チェック
	public void updateCheakDelflg() throws DataAccessException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		final String jobId = "dpszz400";
		Integer TO_CHECK_CNT = new Integer(0);
		paramMap.put("jobId", jobId);
		paramMap.put("TO_CHECK_CNT", TO_CHECK_CNT);
		dataAccess.executeProcedure(getSqlMapName() + ".updateCheakDelflg", paramMap);
	}

	// 計画対象外特約店チェック
	public void updateCheakPlantaigaiflg() throws DataAccessException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		final String jobId = "dpszz400";
		Integer TO_CHECK_CNT = new Integer(0);
		paramMap.put("jobId", jobId);
		paramMap.put("TO_CHECK_CNT", TO_CHECK_CNT);
		dataAccess.executeProcedure(getSqlMapName() + ".updateCheakPlantaigaiflg", paramMap);
	}
}
