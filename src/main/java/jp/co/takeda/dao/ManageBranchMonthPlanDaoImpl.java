package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.ManageBranchMonthPlan;
import jp.co.takeda.model.ManageBranchPlan;
import jp.co.takeda.model.SosMst;

/**
 * 管理の支店別計画にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("manageBranchMonthPlanDao")
public class ManageBranchMonthPlanDaoImpl extends AbstractManageDao implements ManageBranchMonthPlanDao {

	private static final String SQL_MAP = "DPM_C_BRANCH_M_PLAN_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// キー検索
	public ManageBranchPlan search(Long seqKey) throws DataNotFoundException {
		return (ManageBranchPlan) super.selectBySeqKey(seqKey);
	}

	// ユニークキー検索
	public ManageBranchMonthPlan searchUk(String prodCode, String sosCd) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
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
		ManageBranchMonthPlan record = new ManageBranchMonthPlan();
		record.setProdCode(prodCode);
		record.setSosCd(sosCd);
// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		List<String> nnuJhoSbt = new ArrayList<String>();
		nnuJhoSbt.add("10");
		nnuJhoSbt.add("20");
		nnuJhoSbt.add("30");
		nnuJhoSbt.add("41");
		record.setNnuJhoSbt(nnuJhoSbt);

		List<String> iykWktKbnList = new ArrayList<String>();
		iykWktKbnList.add("01");
		iykWktKbnList.add("02");
		record.setIykWktKbnList(iykWktKbnList);
// add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		return super.selectByUniqueKey(record);
	}

	public ManageBranchMonthPlan searchTotalLine(String prodCode, String sosCd) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
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
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sosCd", sosCd);
		paramMap.put("prodCode", prodCode);
// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		List<String> nnuJhoSbt = new ArrayList<String>();
		nnuJhoSbt.add("10");
		nnuJhoSbt.add("20");
		nnuJhoSbt.add("30");
		nnuJhoSbt.add("41");
		paramMap.put("nnuJhoSbt", nnuJhoSbt);

		List<String> iykWktKbnList = new ArrayList<String>();
		iykWktKbnList.add("01");
		iykWktKbnList.add("02");
		paramMap.put("iykWktKbnList", iykWktKbnList);
// add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

		return super.select(getSqlMapName() + ".selectTotalLine", paramMap);

	}

	// リスト検索/組織一覧
	public List<ManageBranchMonthPlan> searchListByProd(String sortString, String prodCode, String category) throws DataNotFoundException {

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
		paramMap.put("getSosCategory", category);
// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		List<String> nnuJhoSbt = new ArrayList<String>();
		nnuJhoSbt.add("10");
		nnuJhoSbt.add("20");
		nnuJhoSbt.add("30");
		nnuJhoSbt.add("41");
		paramMap.put("nnuJhoSbt", nnuJhoSbt);

		List<String> iykWktKbnList = new ArrayList<String>();
		iykWktKbnList.add("01");
		iykWktKbnList.add("02");
		paramMap.put("iykWktKbnList", iykWktKbnList);
// add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

		return super.selectList(getSqlMapName() + ".selectListByProd", paramMap);
	}

	// リスト検索/品目一覧
	public List<ManageBranchMonthPlan> searchListBySos(String sortString, String sosCd, String category, boolean isJrns, String jrnsPcatCd, List<String> jrnsCtgList) throws DataNotFoundException {

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
		// JRNSの場合は、JRNSの品目分類コードを設定
		if (isJrns) {
			paramMap.put("jrnsPcatCd", jrnsPcatCd);
			paramMap.put("jrnsCtgList", jrnsCtgList);
		}
// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		List<String> nnuJhoSbt = new ArrayList<String>();
		nnuJhoSbt.add("10");
		nnuJhoSbt.add("20");
		nnuJhoSbt.add("30");
		nnuJhoSbt.add("41");
		paramMap.put("nnuJhoSbt", nnuJhoSbt);

		List<String> iykWktKbnList = new ArrayList<String>();
		iykWktKbnList.add("01");
		iykWktKbnList.add("02");
		paramMap.put("iykWktKbnList", iykWktKbnList);
// add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

		return super.selectList(getSqlMapName() + ".selectListBySos", paramMap);
	}

	// 計画品目集計取得/品目一覧
	public ManageBranchMonthPlan searchPlanSum(String sosCd, String category) throws DataNotFoundException {

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
		paramMap.put("category", category);
// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		List<String> nnuJhoSbt = new ArrayList<String>();
		nnuJhoSbt.add("10");
		nnuJhoSbt.add("20");
		nnuJhoSbt.add("30");
		nnuJhoSbt.add("41");
		paramMap.put("nnuJhoSbt", nnuJhoSbt);

		List<String> iykWktKbnList = new ArrayList<String>();
		iykWktKbnList.add("01");
		iykWktKbnList.add("02");
		paramMap.put("iykWktKbnList", iykWktKbnList);
// add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

		return super.select(getSqlMapName() + ".selectPlanSum", paramMap);

	}

	// 登録
	public void insert(ManageBranchMonthPlan manageBranchPlan, String pgId) throws DuplicateException {
		super.insert(manageBranchPlan, pgId);
	}

	// 更新
	public int update(ManageBranchMonthPlan manageBranchPlan, String pgId) {
		return super.update(manageBranchPlan, pgId);
	}

}
