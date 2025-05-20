package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.div.PlanType;
import jp.co.takeda.model.view.SpecialInsPlanSosSummary;

import org.springframework.stereotype.Repository;

/**
 * 特定施設個別計画の組織、対象区分集約を取得するDAO実装クラス
 * 
 * @author nozaki
 */
@Repository("specialInsPlanSosSummaryDao")
public class SpecialInsPlanSosSummaryDaoImpl extends AbstractDao implements SpecialInsPlanSosSummaryDao {

	private static final String SQL_MAP = "VIEW_SpecialInsPlanSosSummary_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 営業所組織コード、品目固定コード、対象区分で特定施設個別計画を集約し取得
	public List<SpecialInsPlanSosSummary> searchSosSummaryList(String sosCd3, String prodCode) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("prodCode", prodCode);
		// 固定条件
		paramMap.put("planType", PlanType.OFFICE);
		paramMap.put("delFlg", false);
		return dataAccess.queryForList(getSqlMapName() + ".selectSummaryList", paramMap);
	}

	// チーム組織コード、品目固定コード、対象区分で特定施設個別計画を集約し取得
	public List<SpecialInsPlanSosSummary> searchTeamSummaryList(String sosCd4, String prodCode) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd4 == null) {
			final String errMsg = "組織コード(チーム)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("sosCd4", sosCd4);
		paramMap.put("prodCode", prodCode);
		// 固定条件
		paramMap.put("planType", PlanType.OFFICE);
		paramMap.put("delFlg", false);
		return dataAccess.queryForList(getSqlMapName() + ".selectSummaryList", paramMap);
	}
}
