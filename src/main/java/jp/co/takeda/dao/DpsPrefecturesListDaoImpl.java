package jp.co.takeda.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.Prefectures;

/**
 * 2022/6/21 Y.Taniguchi バックログNo.21　組織に紐づく都道府県をプルダウンに表示する
 *
 * @author yTaniguchi
 */
@Repository("dpsPrefecturesListDao")
public class DpsPrefecturesListDaoImpl extends AbstractManageDao implements DpsPrefecturesListDao {

	private static final String SQL_MAP = "DPS_S_SY_COM_LNK_TKCITY_SOS_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 組織に紐づく都道府県を取得
	@Override
	public List<Prefectures> search(String sosCd) throws DataNotFoundException {

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sosCd", sosCd);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".searchPrefectureList", paramMap);
	}

}
