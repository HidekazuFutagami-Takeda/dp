package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.InsJgiSos;


/**
 *  同じ組織配下の施設担当者DAO実装クラス
 */
@Repository("dpmInsJgiSosDao")
public class DpmInsJgiSosDaoImpl extends AbstractDao implements DpmInsJgiSosDao {

	private static final String SQL_MAP = "DPM_C_MR_INS_PROD_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	public List<InsJgiSos> searchBanch(String insNo, String sosCd2) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosCd2 == null) {
			final String errMsg = "リージョンコードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("insNo", insNo);
		paramMap.put("sosCd2", sosCd2);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectBanch", paramMap);

	}

	public List<InsJgiSos> searchOffice(String insNo, String sosCd3) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosCd3 == null) {
			final String errMsg = "エリアコードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("insNo", insNo);
		paramMap.put("sosCd3", sosCd3);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectOffice", paramMap);

	}

}