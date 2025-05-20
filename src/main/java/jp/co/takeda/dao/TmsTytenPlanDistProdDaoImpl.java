package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.view.TmsTytenPlanDistProd;

/**
 * 特約店別計画配分対象品目を取得するDAO実装クラス
 *
 * @author yokokawa
 */
@Repository("tmsTytenPlanDistProdDao")
public class TmsTytenPlanDistProdDaoImpl extends AbstractDao implements TmsTytenPlanDistProdDao {
	private static final String SQL_MAP = "VIEW_TmsTytenPlanDistProd_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 組織コード(支店)とカテゴリーを元に特約店別計画配分対象品目を取得
	public List<TmsTytenPlanDistProd> searchTmsTytenPlanDistProd(String sortString, String sosCd2, String category, String sales) throws DataNotFoundException {
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
		paramMap.put("sortString", sortString);
		paramMap.put("category", category);
		paramMap.put("sosCd2", sosCd2);
		// 固定条件
		paramMap.put("planTargetFlg", true);
		paramMap.put("sales", sales);

		return dataAccess.queryForList(getSqlMapName() + ".searchTmsTytenPlanDistProd", paramMap);
	}
}
