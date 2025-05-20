package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.view.InsWsPlanForVacSummaryByIns;

/**
 * 施設特約店別計画の施設別サマリーを取得するDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("insWsPlanForVacSummaryByInsDao")
public class InsWsPlanForVacSummaryByInsDaoImpl extends AbstractDao implements InsWsPlanForVacSummaryByInsDao {

	private static final String SQL_MAP = "VIEW_InsWsPlanForVacSummaryByIns_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 業員番号を元に、施設特約店別計画の施設別サマリーリストを取得
	public List<InsWsPlanForVacSummaryByIns> searchList(String sortString, Integer jgiNo) throws DataNotFoundException {

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
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("sortString", sortString);
		paramMap.put("jgiNo", jgiNo);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}
}
