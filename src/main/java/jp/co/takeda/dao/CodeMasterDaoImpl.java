package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.DpmCCdMst;

/**
 * カテゴリにアクセスするDAO実装クラス
 * @author rna8405
 *
 */
@Repository("codeMasterDao")
public class CodeMasterDaoImpl extends AbstractDao implements CodeMasterDao {

	private static final String SQL_MAP = "DPM_C_CD_MST_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// カテゴリ情報を取得
	public List<DpmCCdMst> searchCategory(String dataKbn, List<String> dataCds) throws DataNotFoundException {
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
	public List<DpmCCdMst> searchCategoryList(String dataKbn, List<String> dataCds, String dataValue) throws DataNotFoundException {
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
	public DpmCCdMst searchOneCategory(String dataKbn, String categoryCd) throws DataNotFoundException {
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
	public List<DpmCCdMst> searchCodeByDataKbn(String dataKbn) throws DataNotFoundException {
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
	public DpmCCdMst searchCategoryByKbnAndCd(String dataKbn, String dataCd) throws DataNotFoundException {
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
		return dataAccess.queryForObject(getSqlMapName() + ".searchCategoryByKbnAndCd", paramMap);
	}

}
