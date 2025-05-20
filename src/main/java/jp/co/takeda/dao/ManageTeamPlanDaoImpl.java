package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.ManageTeamPlan;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.InsType;

/**
 * 管理のチーム別計画にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("manageTeamPlanDao")
public class ManageTeamPlanDaoImpl extends AbstractManageDao implements ManageTeamPlanDao {

	private static final String SQL_MAP = "DPM_I_TEAM_PLAN_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// キー検索
	public ManageTeamPlan search(Long seqKey) throws DataNotFoundException {
		return (ManageTeamPlan) super.selectBySeqKey(seqKey);
	}

	// ユニークキー検索
	public ManageTeamPlan searchUk(InsType insType, String prodCode, String sosCd) throws DataNotFoundException {

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
		ManageTeamPlan record = new ManageTeamPlan();
		record.setInsType(insType);
		record.setProdCode(prodCode);
		record.setSosCd(sosCd);
		return super.selectByUniqueKey(record);
	}

	// リスト検索/組織一覧
	public List<ManageTeamPlan> searchListByProd(String sortString, String prodCode, String sosCd3) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(5);
		paramMap.put("sortString", sortString);
		paramMap.put("prodCode", prodCode);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("shiten", SosMst.SHITEN_GROUP);
		paramMap.put("seikei_mr", SosMst.SEIKEI_MR_GROUP);

		List<ManageTeamPlan> resultList = super.selectList(getSqlMapName() + ".selectListByProd", paramMap);

		return resultList;
	}

	// リスト検索/品目一覧
	public List<ManageTeamPlan> searchListBySos(String sortString, String sosCd, String category) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コード(チーム)がnull";
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
	public void insert(ManageTeamPlan manageTeamPlan, String pgId) throws DuplicateException {
		super.insert(manageTeamPlan, pgId);
	}

	// 更新
	public int update(ManageTeamPlan manageTeamPlan, String pgId) {
		return super.update(manageTeamPlan, pgId);
	}

}
