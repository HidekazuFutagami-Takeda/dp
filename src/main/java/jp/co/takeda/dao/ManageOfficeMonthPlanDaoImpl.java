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
import jp.co.takeda.model.ManageOfficeMonthPlan;
import jp.co.takeda.model.ManageOfficePlan;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.Sales;

/**
 * 管理の営業所別計画にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("manageOfficeMonthPlanDao")
public class ManageOfficeMonthPlanDaoImpl extends AbstractManageDao implements ManageOfficeMonthPlanDao {

	private static final String SQL_MAP = "DPM_C_OFFICE_M_PLAN_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// キー検索
	public ManageOfficePlan search(Long seqKey) throws DataNotFoundException {
		return (ManageOfficePlan) super.selectBySeqKey(seqKey);
	}

	// ユニークキー検索
	public ManageOfficeMonthPlan searchUk(InsType insType, String prodCode, String sosCd) throws DataNotFoundException {

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
		ManageOfficeMonthPlan record = new ManageOfficeMonthPlan();
		record.setInsType(insType);
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


	public ManageOfficeMonthPlan searchTotalLine(InsType insType, String prodCode, String sosCd) throws DataNotFoundException {

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
		Map<String, Object> paramMap = new HashMap<String, Object>(5);
		paramMap.put("insType", insType);
		paramMap.put("prodCode", prodCode);
		paramMap.put("sosCd", sosCd);
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
	public List<ManageOfficeMonthPlan> searchListByProd(String sortString, String prodCode, String category, String sosCd2) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosCd2 == null) {
			final String errMsg = "組織コード(支店)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "品目カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(5);
		paramMap.put("sortString", sortString);
		paramMap.put("prodCode", prodCode);
		paramMap.put("sosCd2", sosCd2);
		paramMap.put("shiten", SosMst.SHITEN_GROUP);
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
	public List<ManageOfficeMonthPlan> searchListBySos(String sortString, String sosCd, String category, String vaccineCode, boolean isJrns, String jrnsPcatCd, List<String> jrnsCtgList) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コード(支店)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "品目カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(5);
		paramMap.put("sortString", sortString);
		paramMap.put("sosCd", sosCd);
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

		return super.selectList(getSqlMapName() + ".selectListBySos", paramMap);
	}

	// 集計取得/品目一覧
	public ManageOfficeMonthPlan searchPlanSum(String sosCd, String category, String vaccineCode) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コード(支店)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "品目カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(5);
		paramMap.put("sosCd", sosCd);
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
	public void insert(ManageOfficeMonthPlan manageOfficePlan, String pgId) throws DuplicateException {
		super.insert(manageOfficePlan, pgId);
	}

	// 更新
	public int update(ManageOfficeMonthPlan manageOfficePlan, String pgId) {
		return super.update(manageOfficePlan, pgId);
	}

}
