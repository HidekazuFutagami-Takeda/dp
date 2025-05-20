package jp.co.takeda.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.view.SosCtg;

/**
 * 計画支援の組織カテゴリビューにアクセスするDAO実装クラス
 *
 * @author rna8405
 *
 */
@Repository("dpsCViSosCtgDao")
public class DpsCViSosCtgDaoImpl extends AbstractDao implements DpsCViSosCtgDao {

	private static final String SQL_MAP = "DPS_C_VI_SOS_CTG_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// カテゴリコード一覧を取得する。
	public List<SosCtg> search(String sosCd) throws DataNotFoundException {

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sosCd", sosCd);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".select", paramMap);
	}

	@Override
	public List<SosCtg> searchBySosCdAndCategory(String sosCd, String prodCategory)  throws DataNotFoundException {
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sosCd", sosCd);
		paramMap.put("category", prodCategory);
		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectBySosCdAndCategory", paramMap);
	}
}
