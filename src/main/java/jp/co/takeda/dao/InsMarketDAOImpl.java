package jp.co.takeda.dao;

import org.springframework.stereotype.Repository;

/**
 * 市場規模情報にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("insMarketDAO")
public class InsMarketDAOImpl extends AbstractDao implements InsMarketDAO {

	private static final String SQL_MAP = "DPS_S_SY_COM_INS_MARKET_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}
}
