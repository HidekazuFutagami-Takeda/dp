package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.DistParam;
import jp.co.takeda.model.DistParamHonbu;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.div.DistributionType;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.ProdCategory;
import jp.co.takeda.model.div.Sales;

/**
 * 配分基準(本部)にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("distParamHonbuDao")
public class DistParamHonbuDaoImpl extends AbstractDao implements DistParamHonbuDao {

	private static final String SQL_MAP = "DPS_I_DIST_PARAM_HONBU_SqlMap";

	/**
	 * 計画支援汎用マスタDAO
	 */
    @Autowired(required = true)
    @Qualifier("dpsCodeMasterDao")
    protected DpsCodeMasterDao dpsCodeMasterDao;

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	private String getSalesByCategory(String category) {
		// -----------------------------
		// ワクチンコードの取得
		// -----------------------------
		List<DpsCCdMst> searchList = new ArrayList<DpsCCdMst>();
		try {
			// カテゴリの検索
			searchList = dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.VAC.getDbValue());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + DpsCodeMaster.VAC.getDbValue() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		String vaccineCode = searchList.get(0).getDataCd();

		// -----------------------------
		// 売上所属の判定
		// -----------------------------
		Sales sales = null;
		if(category.equals(vaccineCode)) {
			sales = Sales.VACCHIN;
		}else{
			sales = Sales.IYAKU;
		}
		return sales.getDbValue();

	}

	// 配分基準(本部)を取得
	public DistParamHonbu search(Long seqKey) throws DataNotFoundException {
		return (DistParamHonbu) super.selectBySeqKey(seqKey);
	}

	// ユニークキーで配分基準(本部)を取得
	public DistParamHonbu search(String prodCode, InsType insType, DistributionType distributionType) throws DataNotFoundException {

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
		if (distributionType == null) {
			final String errMsg = "配分先区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		DistParamHonbu record = new DistParamHonbu();
		record.setProdCode(prodCode);
		record.setInsType(insType);
		DistParam distParam = new DistParam();
		distParam.setDistributionType(distributionType);
		record.setDistParam(distParam);
		return (DistParamHonbu) super.selectByUniqueKey(record);
	}

	// 施設特約店配分用の配分基準(本部)リストを取得

	public List<DistParamHonbu> searchInsWsParamList(String sortString, String prodCode, InsType insType, String category) throws DataNotFoundException {

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sortString", sortString);
		paramMap.put("prodCode", prodCode);
		paramMap.put("insType", insType);
		paramMap.put("category", category);
		// 固定条件
		paramMap.put("sales", getSalesByCategory(category));
		paramMap.put("planTargetFlg", true);
		paramMap.put("distributionType", DistributionType.INS_WS_PLAN);

		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}

	// 特約店配分用の配分基準(本部)リストを取得
	public List<DistParamHonbu> searchWsParamList(String sortString, String prodCode, InsType insType, ProdCategory category) throws DataNotFoundException {

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
		paramMap.put("distributionType", DistributionType.WS_PLAN);
		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}
}
