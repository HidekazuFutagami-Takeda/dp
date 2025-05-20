package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.view.MikakutokuSijouInsTypeSummary;

import org.springframework.stereotype.Repository;

/**
 * 未獲得市場の施設出力対象区分別集計を取得するDAO実装クラス
 * 
 * @author nozaki
 */
@Repository("mikakutokuSijouInsTypeSummaryDao")
public class MikakutokuSijouInsTypeSummaryDaoImpl extends AbstractDao implements MikakutokuSijouInsTypeSummaryDao {

	private static final String SQL_MAP = "VIEW_MikakutokuSijouInsTypeSummary_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 組織コード・試算品目コードより、修正金額を施設出力対象区分別に集計し、取得
	public List<MikakutokuSijouInsTypeSummary> searchSummaryList(String sosCd3, String refProdCode) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (refProdCode == null) {
			final String errMsg = "試算対象品目コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("refProdCode", refProdCode);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".searchSummaryList", paramMap);
	}
}
