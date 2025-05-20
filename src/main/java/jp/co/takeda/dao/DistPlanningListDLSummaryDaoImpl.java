package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dto.DistPlanningListSummaryScDto;
import jp.co.takeda.model.DistPlanningListDLSummary;

/**
 * 計画立案準備の営業所計画アップロード用のフォーマットファイルサマリー情報を取得するDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("distPlanningListDLSummaryDao")
public class DistPlanningListDLSummaryDaoImpl extends AbstractDao implements DistPlanningListDLSummaryDao {

	private static final String SQL_MAP = "DPS_C_SOS_MST_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 営業所計画アップロード用のフォーマットファイルサマリー情報リストを取得
	public List<DistPlanningListDLSummary> searchList(String sortString, DistPlanningListSummaryScDto scDto) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(9);

		paramMap.put("sortString", sortString);
		paramMap.put("sos_cd1", scDto.getSos_Cd1());
		paramMap.put("sos_cd2", scDto.getSos_Cd2());
		paramMap.put("sos_cd3", scDto.getSos_Cd3());
		paramMap.put("category", scDto.getCategory());

		return dataAccess.queryForList(getSqlMapName() + ".selectDistPlanningList", paramMap);

	}

	// add Start 2021/7/16 H.Kaneko
	// 営業所計画アップロード用のフォーマットファイルサマリー情報リストを取得
	public List<DistPlanningListDLSummary> searchExceptCategoryList(String sortString, DistPlanningListSummaryScDto scDto) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(9);

		paramMap.put("sortString", sortString);
		paramMap.put("sos_cd1", scDto.getSos_Cd1());
		paramMap.put("sos_cd2", scDto.getSos_Cd2());
		paramMap.put("sos_cd3", scDto.getSos_Cd3());
		paramMap.put("category", scDto.getCategory());

		return dataAccess.queryForList(getSqlMapName() + ".selectDistPlanningListExceptCategory", paramMap);

	}
	// add End 2021/7/16 H.Kaneko
}
