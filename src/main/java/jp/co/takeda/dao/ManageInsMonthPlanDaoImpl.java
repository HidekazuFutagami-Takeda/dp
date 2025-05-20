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
import jp.co.takeda.model.ManageInsMonthPlan;
import jp.co.takeda.model.ManageInsPlan;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.Prefecture;
import jp.co.takeda.model.div.Sales;

/**
 * 管理の施設別計画にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("manageInsMonthPlanDao")
public class ManageInsMonthPlanDaoImpl extends AbstractManageDao implements ManageInsMonthPlanDao {

	private static final String SQL_MAP = "DPM_C_INS_M_PLAN_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// キー検索
	public ManageInsMonthPlan search(Long seqKey) throws DataNotFoundException {
		return (ManageInsMonthPlan) super.selectBySeqKey(seqKey);
	}

	// ユニークキー検索
	public ManageInsMonthPlan searchUk(String prodCode, String insNo) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		ManageInsPlan record = new ManageInsPlan();
		record.setProdCode(prodCode);
		record.setInsNo(insNo);
		return super.selectByUniqueKey(record);
	}

	// リスト検索/施設一覧
	public List<ManageInsMonthPlan> searchListByProd(String sortString, String prodCode, Integer jgiNo, InsType insType, String relnInsNo, String oyakoKb,
			String oyakoKb2, String tgtInsKb,String oyakoKbProdCode)
	throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insType == null) {
			final String errMsg = "施設出力対象区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(6);
		paramMap.put("sortString", sortString);
		paramMap.put("prodCode", prodCode);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("hoInsTypeList", InsType.convertHoInsType(insType));
		paramMap.put("relnInsNo", relnInsNo);
		paramMap.put("oyakoKb", OyakoKbHelper.getOyakoKb(oyakoKb,oyakoKb2,oyakoKbProdCode));
		paramMap.put("tgtInsKb", tgtInsKb);
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

	// リスト検索/施設一覧(月別・ワクチン)
	public List<ManageInsMonthPlan> searchListByProdForVac(String sortString, String prodCode, Integer jgiNo, ActivityType activityType, Prefecture addrCodePref, String addrCodeCity,
		String insNo, String oyakoKb,String oyakoKb2, String tgtInsKb, String oyakoKbProdCode) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(9);
		paramMap.put("sortString", sortString);
		paramMap.put("prodCode", prodCode);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("activityType", activityType);
		paramMap.put("addrCodePref", addrCodePref);
		paramMap.put("addrCodeCity", addrCodeCity);
		paramMap.put("insNo", insNo);
		paramMap.put("oyakoKb", OyakoKbHelper.getOyakoKb(oyakoKb,oyakoKb2,oyakoKbProdCode));
		paramMap.put("tgtInsKb", tgtInsKb);
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

		return super.selectList(getSqlMapName() + ".selectListByProdForVac", paramMap);
	}


	// 施設計画積上取得
	public ManageInsMonthPlan searchSumByIns(String prodCode, Integer jgiNo, InsType insType, String oyakoKb,String oyakoKb2, String oyakoKbProdCode) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insType == null) {
			final String errMsg = "施設出力対象区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("prodCode", prodCode);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("hoInsTypeList", InsType.convertHoInsType(insType));
		paramMap.put("oyakoKb", OyakoKbHelper.getOyakoKb(oyakoKb,oyakoKb2,oyakoKbProdCode));
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

		// ----------------------
		// 検索実行
		// ----------------------
		ManageInsMonthPlan result = null;
		try {
			result = super.select(getSqlMapName() + ".selectSumByIns", paramMap);
		} catch (DataNotFoundException e) {
		}

		return result;
	}


	// （ワ）施設計画積上取得
	public ManageInsMonthPlan searchSumByInsForVac(String prodCode, Integer jgiNo, ActivityType activityType) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("prodCode", prodCode);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("activityType", activityType);
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

		// ----------------------
		// 検索実行
		// ----------------------
		ManageInsMonthPlan result = null;
		try {
			result = super.select(getSqlMapName() + ".selectSumByInsForVac", paramMap);
		} catch (DataNotFoundException e) {
		}

		return result;
	}

	// リスト検索/品目一覧(月別)
	public List<ManageInsMonthPlan> searchListByIns(String sortString, String insNo, String category, boolean allProdFlg,
			String tgtInsKb, String vaccineCode, boolean isJrns, String jrnsPcatCd, List<String> jrnsCtgList) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("sortString", sortString);
		paramMap.put("insNo", insNo);
// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		List<String> nnuJhoSbt = new ArrayList<String>();
		nnuJhoSbt.add("10");
		nnuJhoSbt.add("20");
		nnuJhoSbt.add("30");
		nnuJhoSbt.add("41");
		paramMap.put("nnuJhoSbt", nnuJhoSbt);
// add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		paramMap.put("category", category);
		if (allProdFlg) {
			paramMap.put("allProdFlg", allProdFlg);
		}
		paramMap.put("tgtInsKb", tgtInsKb);
		if (category.equals(vaccineCode)) {
			paramMap.put("sales", Sales.VACCHIN.getDbValue());
// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
			paramMap.put("iykWktKbn", "02");
// add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		} else {
			paramMap.put("sales", Sales.IYAKU.getDbValue());
// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
			paramMap.put("iykWktKbn", "01");
// add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		}
		// JRNSの場合は、JRNSの品目分類コードを設定
		if (isJrns) {
			paramMap.put("jrnsPcatCd", jrnsPcatCd);
			paramMap.put("jrnsCtgList", jrnsCtgList);
		}

		return super.selectList(getSqlMapName() + ".selectListByIns", paramMap);
	}

	// 集計取得/品目一覧(月別)
	public ManageInsMonthPlan searchPlanSum(String insNo, String category, boolean allProdFlg, String tgtInsKb, String vaccineCode) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("insNo", insNo);
// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		List<String> nnuJhoSbt = new ArrayList<String>();
		nnuJhoSbt.add("10");
		nnuJhoSbt.add("20");
		nnuJhoSbt.add("30");
		nnuJhoSbt.add("41");
		paramMap.put("nnuJhoSbt", nnuJhoSbt);
// add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		paramMap.put("category", category);
		if (allProdFlg) {
			paramMap.put("allProdFlg", allProdFlg);
		}
		paramMap.put("tgtInsKb", tgtInsKb);
		if (category.equals(vaccineCode)) {
			paramMap.put("sales", Sales.VACCHIN.getDbValue());
// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
			paramMap.put("iykWktKbn", "02");
// add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		} else {
			paramMap.put("sales", Sales.IYAKU.getDbValue());
// add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
			paramMap.put("iykWktKbn", "01");
// add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		}
		return super.select(getSqlMapName() + ".selectPlanSum", paramMap);
	}

	// 登録
	public void insert(ManageInsMonthPlan manageInsPlan, String pgId) throws DuplicateException {
		super.insert(manageInsPlan, pgId);
	}

	// 更新
	public int update(ManageInsMonthPlan manageInsPlan, String pgId) {
		return super.update(manageInsPlan, pgId);
	}

}
