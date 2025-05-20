package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.HashMap;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.view.JgiEachKindInfoForVac;

import org.springframework.stereotype.Repository;

/**
 * (ワクチン)担当者単位の各種登録状況を取得するDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("jgiEachKindInfoForVacDao")
public class JgiEachKindInfoForVacDaoImpl extends AbstractDao implements JgiEachKindInfoForVacDao {

	private static final String SQL_MAP = "VIEW_JgiEachKindInfoForVac_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 従業員番号を基に、各種登録状況を取得
	public JgiEachKindInfoForVac search(Integer jgiNo) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("jgiNo", jgiNo);
		return dataAccess.queryForObject(getSqlMapName() + ".select", paramMap);
	}
}
