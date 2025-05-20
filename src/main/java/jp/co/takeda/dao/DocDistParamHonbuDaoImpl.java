package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.ProdCategory;
import jp.co.takeda.model.div.Sales;

import org.springframework.stereotype.Repository;

/**
 * 医師別計画配分基準(本部)にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("docDistParamHonbuDao")
public class DocDistParamHonbuDaoImpl extends AbstractDao implements DocDistParamHonbuDao {

	private static final String SQL_MAP = "DPS_I_DOC_DIST_PARAM_HONBU_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 配分基準(本部)を取得
	public Map<String, Object> search(Long seqKey) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (seqKey == null) {
			final String errMsg = "シーケンスキーがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("seqKey", seqKey);
		return dataAccess.queryForObject(getSqlMapName() + ".select", paramMap);

	}

	// ユニークキーで配分基準(本部)を取得
	public Map<String, Object> search(String prodCode, InsType insType) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insType == null) {
			final String errMsg = "施設出力対象区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("prodCode", prodCode);
		paramMap.put("insType", insType);
		return dataAccess.queryForObject(getSqlMapName() + ".selectUk", paramMap);
	}

	// 施設特約店配分用の配分基準(本部)リストを取得
	public List<Map<String, Object>> searchInsDocParamList(String sortString, String prodCode, InsType insType, ProdCategory category) throws DataNotFoundException {

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sortString", sortString);
		paramMap.put("prodCode", prodCode);
		paramMap.put("insType", insType);
		paramMap.put("category", category);
		// 固定条件
		paramMap.put("sales", Sales.IYAKU);
		paramMap.put("planTargetFlg", true);

		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}
}
