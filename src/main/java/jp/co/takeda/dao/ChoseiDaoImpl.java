package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;

/**
 * 営業所調整にアクセスするDAO実装クラス
 *
 * @author mtsuchida
 */
@Repository("choseiDao")
public class ChoseiDaoImpl extends AbstractDao implements ChoseiDao {

	private static final String SQL_MAP = "DPS_I_CHOSEI_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 調整金額更新日時を取得する
	public Date searchMaxUpdate(String sosCd3, String category) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
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
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("category", category);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".searchMaxUpdate", paramMap);
	}

	// 営業所調整のデータを最新にする
	public void updateChosei(String sosCd3) throws DataAccessException {
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		final String jobId = "dpszz800";
		paramMap.put("jobId", jobId);
		paramMap.put("sosCd3", sosCd3);
		dataAccess.execute(getSqlMapName() + ".updateChosei", paramMap);
	}
}
