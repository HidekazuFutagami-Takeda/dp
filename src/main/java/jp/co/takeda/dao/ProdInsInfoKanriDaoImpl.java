package jp.co.takeda.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.ProdInsInfoKanri;

@Repository("prodInsInfoKanriDao")
public class ProdInsInfoKanriDaoImpl extends AbstractDao implements ProdInsInfoKanriDao {

	private static final String SQL_MAP = "DPS_C_PROD_INS_INFO_K_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	public ProdInsInfoKanri searchByInsNo(String prodCode, String insNo) throws DataNotFoundException {
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("prodCode", prodCode);
		paramMap.put("insNo", insNo);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".selectByInsNo", paramMap);
	}

}
