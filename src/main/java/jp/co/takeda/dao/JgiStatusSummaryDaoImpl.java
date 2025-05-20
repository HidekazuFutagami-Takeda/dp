package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.model.view.JgiStatusSummary;

/**
 * 担当者単位のステータスのサマリーを取得するDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("jgiStatusSummaryDao")
public class JgiStatusSummaryDaoImpl extends AbstractDao implements JgiStatusSummaryDao {

	private static final String SQL_MAP = "VIEW_JgiStatusSummary_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}
	/**
	 * 計画支援汎用マスタDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterDao")
	protected DpsCodeMasterDao dpsCodeMasterDao;

	// 従業員番号・カテゴリを基に、ステータスのサマリーを取得
	@Override
	public JgiStatusSummary search(Integer jgiNo, String category) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("category", category);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);

		String siireCd = dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.SII.getDbValue()).get(0).getDataCd();

		if(category != null && !category.equals(siireCd)){
			paramMap.put("isNotShiire", true);
		}

		paramMap.put("planTargetFlg", true);
		//paramMap.put("sales", Sales.IYAKU);
		return dataAccess.queryForObject(getSqlMapName() + ".select", paramMap);
	}

	@Override
	public List<JgiStatusSummary> searchList(String sosCd, String category) throws DataNotFoundException {
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sosCd", sosCd);
		paramMap.put("category", category);
		paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);

		String siireCd = dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.SII.getDbValue()).get(0).getDataCd();
		if(category != null && !category.equals(siireCd)){
			paramMap.put("isNotShiire", true);
		}
		paramMap.put("planTargetFlg", true);

		// 固定条件
		// 固定条件
		paramMap.put("sosLvl", SosLvl.MR);
		List<JgiKb> jgiKbList = new ArrayList<JgiKb>();
		jgiKbList.add(JgiKb.JGI);
		jgiKbList.add(JgiKb.CONTRACT_MR);
		jgiKbList.add(JgiKb.DUMMY_MR);
		paramMap.put("jgiKbList", jgiKbList);
		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}


}
