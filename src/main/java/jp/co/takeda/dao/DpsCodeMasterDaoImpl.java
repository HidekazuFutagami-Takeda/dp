package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.div.JokenSet;

/**
 * 施設特約店別計画にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("dpsCodeMasterDao")
public class DpsCodeMasterDaoImpl extends AbstractDao implements DpsCodeMasterDao {

	private static final String SQL_MAP = "DPS_C_CD_MST_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// カテゴリ情報を取得
	public List<DpsCCdMst> searchCategory(String dataKbn, List<String> dataCds) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (dataKbn == null) {
			final String errMsg = "データ区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("dataKbn", dataKbn);
		paramMap.put("dataCds", dataCds);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".searchCategoryList", paramMap);
	}


	// カテゴリ情報を取得
	public List<DpsCCdMst> searchCategoryList(String dataKbn, List<String> dataCds, String dataValue) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (dataKbn == null) {
			final String errMsg = "データ区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("dataKbn", dataKbn);
		paramMap.put("dataCds", dataCds);
		paramMap.put("dataValue", dataValue);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".searchCategoryList", paramMap);
	}

	// カテゴリ情報を取得
	@Override
	public DpsCCdMst searchOneCategory(String dataKbn, String categoryCd) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (dataKbn == null) {
			final String errMsg = "データ区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("dataKbn", dataKbn);
		paramMap.put("categoryCd", categoryCd);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".searchCategory", paramMap);
	}

	@Override
	public List<DpsCCdMst> searchCodeByDataKbn(String dataKbn) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (dataKbn == null) {
			final String errMsg = "データ区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("dataKbn", dataKbn);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".searchCodeByDataKbn", paramMap);
	}

	@Override
	public List<DpsCCdMst> searchCodeByDataKbnOrderByDataValue(String dataKbn) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (dataKbn == null) {
			final String errMsg = "データ区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("dataKbn", dataKbn);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".searchCodeByDataKbnOrderByDataValue", paramMap);
	}


	@Override
	public DpsCCdMst searchDataKbnAndCd(String dataKbn, String dataCd) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (dataKbn == null) {
			final String errMsg = "データ区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dataCd == null) {
			final String errMsg = "コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("dataKbn", dataKbn);
		paramMap.put("dataCd", dataCd);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".searchDataKbnAndCd", paramMap);
	}

	@Override
	public List<DpsCCdMst> searchShienCategoryList(String sosCd, ProdPlanLevel planLevel) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (planLevel == null) {
			final String errMsg = "計画立案レベルがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sosCd", sosCd);
		switch (planLevel) {
		case ZENSHA:
			paramMap.put("planLevelZensha", true);
			break;
		case SITEN:
			paramMap.put("planLevelSiten", true);
			break;
		case OFFICE:
			paramMap.put("planLevelOffice", true);
			break;
		case TEAM:
			paramMap.put("planLevelTeam", true);
			break;
		case MR:
			paramMap.put("planLevelMr", true);
			break;
		case INS:
			paramMap.put("planLevelIns", true);
			break;
		case INS_WS:
			paramMap.put("planLevelInsWs", true);
			break;
		case WS:
			paramMap.put("planLevelWs", true);
			break;
		case ALL:
			break;
		}
		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".searchShienCategoryList", paramMap);
	}

	@Override
	public List<JokenSet> searchTokuyakuJokenSetCd() throws DataNotFoundException {
		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectTokuyakuJokenSetCd",new HashMap<String, Object>() );
	}
}
