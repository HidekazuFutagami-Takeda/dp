package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.div.RefPeriod;
import jp.co.takeda.model.view.DeliveryResultInsTypeSummary;

import org.springframework.stereotype.Repository;

/**
 * 納入実績の施設出力対象区分別集計を取得DAO実装クラス
 * 
 * @author nozaki
 */
@Repository("deliveryResultInsTypeSummaryDao")
public class DeliveryResultInsTypeSummaryDaoImpl extends AbstractDao implements DeliveryResultInsTypeSummaryDao {

	private static final String SQL_MAP = "VIEW_DeliveryResultInsTypeSummary_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 組織コード・試算品目コードより、納入実績を施設出力対象区分別に集計し、取得
	public List<DeliveryResultInsTypeSummary> searchSummaryList(String sosCd3, String refProdCode, RefPeriod refFrom, RefPeriod refTo) throws DataNotFoundException {

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
		if (refFrom == null) {
			final String errMsg = "参照期間FROMがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (refTo == null) {
			final String errMsg = "参照期間TOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("refProdCode", refProdCode);
		paramMap.put("refFrom", refFrom);
		paramMap.put("refTo", refTo);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".searchSummaryList", paramMap);
	}
}
