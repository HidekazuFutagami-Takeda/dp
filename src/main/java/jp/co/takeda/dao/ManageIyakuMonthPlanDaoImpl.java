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
import jp.co.takeda.model.ManageIyakuMonthPlan;
import jp.co.takeda.model.ManageIyakuPlan;
import jp.co.takeda.model.div.Sales;

/**
 * 管理の全社計画にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("manageIyakuMonthPlanDao")
public class ManageIyakuMonthPlanDaoImpl extends AbstractManageDao implements ManageIyakuMonthPlanDao {

	private static final String SQL_MAP = "DPM_C_IYAKU_M_PLAN_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// キー検索
	public ManageIyakuPlan search(Long seqKey) throws DataNotFoundException {
		return (ManageIyakuPlan) super.selectBySeqKey(seqKey);
	}

	// ユニークキー検索
	public ManageIyakuMonthPlan searchUk(String prodCode) throws DataNotFoundException {

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
		ManageIyakuMonthPlan record = new ManageIyakuMonthPlan();
		record.setProdCode(prodCode);
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

	public ManageIyakuMonthPlan searchTotalLine(String prodCode) throws DataNotFoundException {

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

	// リスト検索/品目一覧
	public List<ManageIyakuMonthPlan> searchList(String sortString, String category, String vaccineCode, boolean isJrns, String jrnsPcatCd, List<String> jrnsCtgList) throws DataNotFoundException {

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("sortString", sortString);
		paramMap.put("category", category);
		if (category.equals(vaccineCode)) {
			paramMap.put("sales", Sales.VACCHIN.getDbValue());
		} else {
			paramMap.put("sales", Sales.IYAKU.getDbValue());
		}
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

		return super.selectList(getSqlMapName() + ".selectList", paramMap);
	}

	// 集計取得/品目一覧
	public ManageIyakuMonthPlan searchPlanSum(String category, String vaccineCode) throws DataNotFoundException {
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("category", category);
		if (category.equals(vaccineCode)) {
			paramMap.put("sales", Sales.VACCHIN.getDbValue());
		} else {
			paramMap.put("sales", Sales.IYAKU.getDbValue());
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

		return super.select(getSqlMapName() + ".selectPlanSum", paramMap);
	}

	// 登録
	public void insert(ManageIyakuMonthPlan manageIyakuPlan, String pgId) throws DuplicateException {
		super.insert(manageIyakuPlan, pgId);
	}

	// 更新
	public int update(ManageIyakuMonthPlan manageIyakuPlan, String pgId) {
		return super.update(manageIyakuPlan, pgId);
	}

}
