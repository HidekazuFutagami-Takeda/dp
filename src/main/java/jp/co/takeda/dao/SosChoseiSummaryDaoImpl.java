package jp.co.takeda.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.view.SosChoseiSummary;

import org.springframework.stereotype.Repository;

/**
 * 組織/従業員単位の調整金額有無のサマリーを取得するDAO実装クラス
 * 
 * @author tkawabata
 */
@Repository("sosChoseiSummaryDao")
public class SosChoseiSummaryDaoImpl extends AbstractManageDao implements SosChoseiSummaryDao {

	private static final String SQL_MAP = "VIEW_SosChoseiSummary_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	public SosChoseiSummary searchIyaku() throws DataNotFoundException {
		// ----------------------
		// 検索実行
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		return super.select(getSqlMapName() + ".iyaku", paramMap);
	}

	public List<SosChoseiSummary> searchShitenList() throws DataNotFoundException {
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("shiten", SosMst.SHITEN_GROUP);

		// ----------------------
		// 検索実行
		// ----------------------
		return super.selectList(getSqlMapName() + ".shiten", paramMap);
	}

	public List<SosChoseiSummary> searchOfficeList(String sosCd2) throws DataNotFoundException {
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("sosCd2", sosCd2);
		paramMap.put("shiten", SosMst.SHITEN_GROUP);

		// ----------------------
		// 検索実行
		// ----------------------
		return super.selectList(getSqlMapName() + ".office", paramMap);
	}

	public List<SosChoseiSummary> searchTeamList(String sosCd3) throws DataNotFoundException {
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("shiten", SosMst.SHITEN_GROUP);
		paramMap.put("seikei_mr", SosMst.SEIKEI_MR_GROUP);

		// ----------------------
		// 検索実行
		// ----------------------
		return super.selectList(getSqlMapName() + ".team", paramMap);
	}

	public List<SosChoseiSummary> searchMrList(String sosCd) throws DataNotFoundException {
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("sosCd", sosCd);
		// 固定条件
		paramMap.put("jokenSetList", new JokenSet[]{ JokenSet.IYAKU_MR });
		paramMap.put("jgiKbList", new JgiKb[]{ JgiKb.JGI, JgiKb.CONTRACT_MR, JgiKb.EIGYOSHO_ZATU , JgiKb.DUMMY_MR});
		
		// ----------------------
		// 検索実行
		// ----------------------
		return super.selectList(getSqlMapName() + ".mr", paramMap);
	}

	public Date getLastUpDate() {
		// ----------------------
		// 検索実行
		// ----------------------
		try {
			return dataAccess.queryForObject(getSqlMapName() + ".lastUpDate", null);
		} catch (DataNotFoundException e) {
			return null;
		}
	}

}
