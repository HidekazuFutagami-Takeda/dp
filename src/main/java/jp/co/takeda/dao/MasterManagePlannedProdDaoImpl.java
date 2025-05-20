package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.Constants;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.model.MasterManagePlannedProd;
import jp.co.takeda.model.div.Sales;

/**
 * 管理の計画対象品目にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("masterManagePlannedProdDao")
public class MasterManagePlannedProdDaoImpl extends AbstractManageDao implements MasterManagePlannedProdDao {

	private static final String SQL_MAP = "DPM_MASTER_C_PLANNED_PROD_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 計画対象品目を取得
	public MasterManagePlannedProd search(String prodCode) throws DataNotFoundException {
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
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("prodCode", prodCode);

		// ----------------------
		// 検索実行
		// ----------------------
		return (MasterManagePlannedProd) super.select(getSqlMapName() + ".select", paramMap);
	}

	// 計画対象品目のリストを取得
	public List<MasterManagePlannedProd> searchList(String sortString, Sales sales, String category, ProdPlanLevel planLevel) throws DataNotFoundException {

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
		List<MasterManagePlannedProd> result = super.selectList(getSqlMapName() + ".selectProdList", paramMap);
		return result;
	}

// add Start 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える
	// 計画対象品目のリストを取得
	public List<MasterManagePlannedProd> searchDistributorList(String sortString, Sales sales, String category, ProdPlanLevel planLevel) throws DataNotFoundException {

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
		// add Start 2025/2/19  H.Futagami 202410要望:カテゴリに全て（医薬）追加
		paramMap.put("categoryIyakuAll", Constants.CATEGORY_IYAKU_ALL);
		paramMap.put("categoryVxBU", Constants.CATEGORY_VXBU);
		// add End 2025/2/19  H.Futagami 202410要望:カテゴリに全て（医薬）追加

		// ----------------------
		// 検索実行
		// ----------------------
		List<MasterManagePlannedProd> result = super.selectList(getSqlMapName() + ".selectProdDistributorList", paramMap);
		return result;
	}
// add End 2022/6/8 R.takamoto バックログNo.26　特約店別計画関連の品目並び順を変える

	// JRNS用の計画対象品目のリストを取得
	public List<MasterManagePlannedProd> searchJrnsList(String sortString, ProdPlanLevel planLevel, List<String> ctgList) throws DataNotFoundException {

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
		paramMap.put("ctgList", ctgList);
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
		List<MasterManagePlannedProd> result = super.selectList(getSqlMapName() + ".selectJrnsProdList", paramMap);
		return result;
	}

	public List<MasterManagePlannedProd> searchProdList(String sortString, Sales sales, String category, Boolean planLevelInsDoc) throws DataNotFoundException {

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
		List<MasterManagePlannedProd> result = dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
		return result;
	}
}
