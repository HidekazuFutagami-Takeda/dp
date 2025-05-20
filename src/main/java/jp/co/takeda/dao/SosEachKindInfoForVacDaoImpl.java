package jp.co.takeda.dao;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.view.SosEachKindInfoForVac;

import org.springframework.stereotype.Repository;

/**
 * (ワクチン)組織単位の各種登録状況を取得するDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("sosEachKindInfoForVacDao")
public class SosEachKindInfoForVacDaoImpl extends AbstractDao implements SosEachKindInfoForVacDao {

	private static final String SQL_MAP = "VIEW_SosEachKindInfoForVac_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 全特約店Gの各種登録状況を取得
	public SosEachKindInfoForVac search() throws DataNotFoundException {
		// ----------------------
		// 検索条件生成
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".select", null);
	}
}
