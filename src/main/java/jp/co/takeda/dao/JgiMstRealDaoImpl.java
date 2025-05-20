package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.model.div.SosLvl;

/**
 * 従業員情報にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("jgiMstRealDao")
public class JgiMstRealDaoImpl extends AbstractDao implements JgiMstRealDao {

	private static final String SQL_MAP = "DPM_C_VI_JGI_MST_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 最新の従業員取得
	public JgiMst searchReal(Integer jgiNo) throws DataNotFoundException {
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
		Map<String, Integer> paramMap = new HashMap<String, Integer>(1);
		paramMap.put("jgiNo", jgiNo);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".selectReal", paramMap);
	}

	// 組織コードを元に、従業員情報のリスト(MRのみ)を取得
	public List<JgiMst> searchRealBySosCd(String sosCd, SosListType sosListType, BumonRank bumonRank) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosListType == null) {
			final String errMsg = "組織一覧の種類がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (bumonRank != null) {
			switch (bumonRank) {
				case IEIHON:
					paramMap.put("sosCd1", sosCd);
					break;
				case SITEN_TOKUYAKUTEN_BU:
					paramMap.put("sosCd2", sosCd);
					break;
				case OFFICE_TOKUYAKUTEN_G:
					paramMap.put("sosCd3", sosCd);
					break;
				case TEAM:
					paramMap.put("sosCd4", sosCd);
					break;
			}
		}
		// 固定条件
		if (sosListType.equals(SosListType.SHITEN_LIST)) {
			paramMap.put("isShiten", true);
		}
		paramMap.put("sosLvl", SosLvl.MR);

		paramMap.put("jgiKbList", new JgiKb[]{ JgiKb.JGI, JgiKb.CONTRACT_MR, JgiKb.EIGYOSHO_ZATU , JgiKb.DUMMY_MR});

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectRealBySosCd", paramMap);
	}

}
