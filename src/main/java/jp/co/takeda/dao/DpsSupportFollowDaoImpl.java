package jp.co.takeda.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.view.AreaPersonInChargeAdjustment;
import jp.co.takeda.model.view.PersonInChargeFacilityAdjustmentList;
import jp.co.takeda.model.view.DeletedFacilityPersonInChargeplan;
import jp.co.takeda.model.view.AreaFacilityPersonInChargeAdjustment;

/**
 *
 *
 * @author hfutagami
 */
@Repository("DpsSupportFollowDao")
public class DpsSupportFollowDaoImpl extends AbstractManageDao implements DpsSupportFollowDao {

	private static final String SQL_MAP = "VIEW_SupportFollow_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

    @Override
	public List<AreaPersonInChargeAdjustment> searchAreaPersonInChargeAdjustmentList(String category, String statProdCode) throws DataNotFoundException {

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("category", category);
		paramMap.put("statProdCode", statProdCode);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".search1", paramMap);
	}

    @Override
	public List<PersonInChargeFacilityAdjustmentList> searchPersonInChargeFacilityAdjustmentList(String category, String statProdCode, String prodCode) throws DataNotFoundException {

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("category", category);
		paramMap.put("statProdCode", statProdCode);
		paramMap.put("prodCode", prodCode);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".search2", paramMap);
	}

    @Override
	public List<AreaFacilityPersonInChargeAdjustment> searchAreaFacilityPersonInChargeAdjustmentList(String category, String prodCode) throws DataNotFoundException {

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("category", category);
		paramMap.put("prodCode", prodCode);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".search3", paramMap);
	}

    @Override
	public List<AreaFacilityPersonInChargeAdjustment> searchAreaFacilityPersonInChargeAdjustmentShiireList(String category, String statProdCode) throws DataNotFoundException {

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("category", category);
		paramMap.put("statProdCode", statProdCode);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".search3Shiire", paramMap);
	}

    @Override
	public List<DeletedFacilityPersonInChargeplan> searchDeletedFacilityPersonInChargeplanList(String category, String statProdCode) throws DataNotFoundException {

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("category", category);
		paramMap.put("statProdCode", statProdCode);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".search4", paramMap);
	}

    @Override
	public List<DeletedFacilityPersonInChargeplan> searchDeletedFacilityPersonInChargeplanShiireList(String category, String statProdCode) throws DataNotFoundException {

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("category", category);
		paramMap.put("statProdCode", statProdCode);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".search4Shiire", paramMap);
	}

}
