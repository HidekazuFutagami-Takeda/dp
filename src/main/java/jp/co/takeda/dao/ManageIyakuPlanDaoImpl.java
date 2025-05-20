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
import jp.co.takeda.model.ManageIyakuPlan;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.Sales;

/**
 * 管理の全社計画にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("manageIyakuPlanDao")
public class ManageIyakuPlanDaoImpl extends AbstractManageDao implements ManageIyakuPlanDao {

	private static final String SQL_MAP = "DPM_I_IYAKU_PLAN_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// キー検索
	public ManageIyakuPlan search(Long seqKey) throws DataNotFoundException {
		return (ManageIyakuPlan) super.selectBySeqKey(seqKey);
	}

	// ユニークキー検索
	public ManageIyakuPlan searchUk(InsType insType, String prodCode) throws DataNotFoundException {

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

		// ----------------------
		// 検索条件生成
		// ----------------------
		ManageIyakuPlan record = new ManageIyakuPlan();
		record.setInsType(insType);
		record.setProdCode(prodCode);
		return super.selectByUniqueKey(record);
	}

	// リスト検索/品目一覧
	public List<ManageIyakuPlan> searchList(String sortString, String category, String vaccineCode) throws DataNotFoundException {

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
		return super.selectList(getSqlMapName() + ".selectList", paramMap);
	}

	// 登録
	public void insert(ManageIyakuPlan manageIyakuPlan, String pgId) throws DuplicateException {
		super.insert(manageIyakuPlan, pgId);
	}

	// 更新
	public int update(ManageIyakuPlan manageIyakuPlan, String pgId) {
		return super.update(manageIyakuPlan, pgId);
	}

}
