package jp.co.takeda.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.view.SosCtg;

/**
 * 組織カテゴリビューにアクセスするDAO実装クラス
 *
 * @author rna8405
 *
 */
@Repository("dpmCViSosCtgDao")
public class DpmCViSosCtgDaoImpl extends AbstractDao implements DpmCViSosCtgDao {

	private static final String SQL_MAP = "DPM_C_VI_SOS_CTG_SqlMap";

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
}
