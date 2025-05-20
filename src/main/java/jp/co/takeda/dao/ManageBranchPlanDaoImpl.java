package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.ManageBranchPlan;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.InsType;

/**
 * 管理の支店別計画にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("manageBranchPlanDao")
public class ManageBranchPlanDaoImpl extends AbstractManageDao implements ManageBranchPlanDao {

	private static final String SQL_MAP = "DPM_I_BRANCH_PLAN_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// キー検索
	public ManageBranchPlan search(Long seqKey) throws DataNotFoundException {
		return (ManageBranchPlan) super.selectBySeqKey(seqKey);
	}

	// ユニークキー検索
	public ManageBranchPlan searchUk(InsType insType, String prodCode, String sosCd) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (insType == null) {
			final String errMsg = "施設出力対象区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		ManageBranchPlan record = new ManageBranchPlan();
		record.setInsType(insType);
		record.setProdCode(prodCode);
		record.setSosCd(sosCd);
		return super.selectByUniqueKey(record);
	}

	// リスト検索/組織一覧
	public List<ManageBranchPlan> searchListByProd(String sortString, String prodCode, String category) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sortString", sortString);
		paramMap.put("prodCode", prodCode);
		paramMap.put("shiten", SosMst.SHITEN_GROUP);
// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
		// カテゴリがONCの場合は、ONC組織のみ
//		if(category == ProdCategory.ONC){
			paramMap.put("isOnc", true);
//		} else {
//			paramMap.put("isOnc", false);
//		}
		//　組織ごとにカテゴリを設定（検索条件で利用）
//		if(category == ProdCategory.MMP){
//			paramMap.put("getSosCategory", ProdCategory.MMP);
//		//　仕入品選択時はGMBU（旧MMP）を表示
//		} else if(category == ProdCategory.SHIIRE){
//			paramMap.put("getSosCategory", ProdCategory.MMP);
//		} else if(category == ProdCategory.ONC){
//			paramMap.put("getSosCategory", ProdCategory.ONC);
//		} else if(category == ProdCategory.SPBU){
//			paramMap.put("getSosCategory", ProdCategory.SPBU);
//		}
		paramMap.put("getSosCategory", category);
// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）

		return super.selectList(getSqlMapName() + ".selectListByProd", paramMap);
	}

	// リスト検索/品目一覧
	public List<ManageBranchPlan> searchListBySos(String sortString, String sosCd, String category) throws DataNotFoundException {

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
		Map<String, Object> paramMap = new HashMap<String, Object>(5);
		paramMap.put("sortString", sortString);
		paramMap.put("sosCd", sosCd);
		paramMap.put("category", category);

		return super.selectList(getSqlMapName() + ".selectListBySos", paramMap);
	}

	// 登録
	public void insert(ManageBranchPlan manageBranchPlan, String pgId) throws DuplicateException {
		super.insert(manageBranchPlan, pgId);
	}

	// 更新
	public int update(ManageBranchPlan manageBranchPlan, String pgId) {
		return super.update(manageBranchPlan, pgId);
	}

}
