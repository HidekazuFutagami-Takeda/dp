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
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.MrCat;
import jp.co.takeda.model.div.Sales;

/**
 * 計画対象品目にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("plannedProdDAO")
public class PlannedProdDAOImpl extends AbstractDao implements PlannedProdDAO {

	private static final String SQL_MAP = "DPS_C_PLANNED_PROD_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 計画対象品目を取得
	public PlannedProd search(String prodCode) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, String> paramMap = new HashMap<String, String>(1);
		paramMap.put("prodCode", prodCode);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".select", paramMap);
	}

	// 計画対象品目を取得
	public PlannedProd searchForOncSos(String prodCode) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("prodCode", prodCode);
		paramMap.put("mrCat", MrCat.ONC_MR);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".select", paramMap);
	}

	// 計画対象品目を統計品名コードで取得
	public PlannedProd searchBystatPd(String statProdCode) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (statProdCode == null) {
			final String errMsg = "統計品名コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, String> paramMap = new HashMap<String, String>(1);
		paramMap.put("statProdCode", statProdCode);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".selectBystatPd", paramMap);
	}

	// 計画対象品目を薬効市場コードで取得
	public PlannedProd searchByYakkouSijouCode(String yakkouSijouCode) throws DataNotFoundException {
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
		return dataAccess.queryForObject(getSqlMapName() + ".selectByYakkouSijouCode", paramMap);
	}

	// 計画対象品目のリストを取得
//	public List<PlannedProd> searchList(String sortString, Sales sales, ProdCategory category, Boolean planLevelInsDoc) throws DataNotFoundException {
	public List<PlannedProd> searchList(String sortString, Sales sales, String category, Boolean planLevelInsDoc) throws DataNotFoundException {

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("sortString", sortString);
		paramMap.put("sales", sales);
		paramMap.put("category", category);
		// ONC品制御はカテゴリで判断するため削除
//		if(BooleanUtils.isTrue(isOncSos)){
//			paramMap.put("mrCat", MrCat.ONC_MR);
//		}
		paramMap.put("planLevelInsDoc", planLevelInsDoc);
		// 固定条件
		paramMap.put("planTargetFlg", true);

		// ----------------------
		// 検索実行
		// ----------------------
		List<PlannedProd> result = dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
		return result;
	}

// mod Start 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える
	public List<PlannedProd> searchDistributorList(String sortString, Sales sales, String category, Boolean planLevelInsDoc) throws DataNotFoundException {

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("sortString", sortString);
		paramMap.put("sales", sales);
		paramMap.put("category", category);
		// ONC品制御はカテゴリで判断するため削除
//		if(BooleanUtils.isTrue(isOncSos)){
//			paramMap.put("mrCat", MrCat.ONC_MR);
//		}
		paramMap.put("planLevelInsDoc", planLevelInsDoc);
		// 固定条件
		paramMap.put("planTargetFlg", true);

		// ----------------------
		// 検索実行
		// ----------------------
		List<PlannedProd> result = dataAccess.queryForList(getSqlMapName() + ".selectDistributorList", paramMap);
		return result;
	}
// mod End 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える

	// 計画立案レベルを考慮した計画対象品目のリストを取得
	public List<PlannedProd> searchListByPlanLevel(String sortString, Sales sales, String category, ProdPlanLevel planLevel) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (planLevel == null) {
			final String errMsg = "計画立案レベルがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("sortString", sortString);
		paramMap.put("sales", sales);
		paramMap.put("category", category);
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
		List<PlannedProd> result = dataAccess.queryForList(getSqlMapName() + ".selectProdList", paramMap);
		return result;
	}

// add Start 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
	public List<PlannedProd> selectListOfCategory(String category) throws DataNotFoundException {

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("category", category);

		// ----------------------
		// 検索実行
		// ----------------------
		List<PlannedProd> result = dataAccess.queryForList(getSqlMapName() + ".selectListOfCategory", paramMap);
		return result;
	}
// add End 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能

}
