package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.YakkouSijou;

/**
 * 薬効市場にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("yakkouSijouDAO")
public class YakkouSijouDAOImpl extends AbstractDao implements YakkouSijouDAO {

	private static final String SQL_MAP = "DPS_I_YAKKOU_SIJOU_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 薬効市場を取得
	public YakkouSijou search(String yakkouSijouCode) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (yakkouSijouCode == null) {
			final String errMsg = "薬効市場コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, String> paramMap = new HashMap<String, String>(1);
		paramMap.put("yakkouSijouCode", yakkouSijouCode);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".select", paramMap);
	}

	// 薬効市場のリストを取得
	public List<YakkouSijou> searchList(String sortString, String category) throws DataNotFoundException {
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("delFlg", false);
		paramMap.put("category", category);
		paramMap.put("sortString", sortString);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}
}
