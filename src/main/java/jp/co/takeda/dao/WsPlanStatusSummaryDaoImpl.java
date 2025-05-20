package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.view.WsPlanStatusSummary;
import jp.co.takeda.service.DpsCodeMasterSearchService;

/**
 * 特約店別計画ステータスのサマリーを取得するDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("wsPlanStatusSummaryDao")
public class WsPlanStatusSummaryDaoImpl extends AbstractDao implements WsPlanStatusSummaryDao {

	private static final String SQL_MAP = "VIEW_WsPlanStatusSummary_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	/**
	 * カテゴリ 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	// 組織コード(支店)・カテゴリを元に、ステータスのサマリーを取得
	@Override
	public WsPlanStatusSummary searchShiten(String sosCd2, String category) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd2 == null) {
			final String errMsg = "組織コードがnull";
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
		paramMap.put("sosCd2", sosCd2);
		// 固定条件
		//paramMap.put("planTargetFlg", true);
		paramMap.put("planTargetFlg", "1");
		if(dpsCodeMasterSearchService.isVaccine(category)) {
			paramMap.put("sales", Sales.VACCHIN);
		} else {
			paramMap.put("sales", Sales.IYAKU);
		}
		WsPlanStatusSummary ret = dataAccess.queryForObject(getSqlMapName() + ".select", paramMap);
		return ret;
	}

	@Override
	public List<WsPlanStatusSummary> searchLIstSumByCategory() throws DataNotFoundException {
		return dataAccess.queryForList(getSqlMapName() + ".selectListSumByCategory",null);
	}
}
