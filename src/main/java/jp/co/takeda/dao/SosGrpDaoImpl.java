package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.SosGrp;

/**
 * 組織グループにアクセスするDAO実装クラス
 *
 * @author rna8405
 *
 */
@Repository("sosGrpDao")
public class SosGrpDaoImpl extends AbstractDao implements SosGrpDao {

	private static final String SQL_MAP = "DPS_S_SY_M_SOS_GRP_M_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	@Override
	public List<SosGrp> search(String sosCd) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sosCd", sosCd);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".select", paramMap);
	}
}
