package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.model.DpsPlannedCtg;
import jp.co.takeda.model.view.SosCtg;

/**
 *
 *
 * @author khashimoto
 */
@Repository("dpsPlannedCtgDao")
public class DpsPlannedCtgDaoImpl extends AbstractManageDao implements DpsPlannedCtgDao {

	private static final String SQL_MAP = "DPS_C_PLANNED_CTG_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 計画対象カテゴリ領域を取得
	@Override
	public DpsPlannedCtg search(String prodCategory) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCategory == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		List<DpsPlannedCtg> DpsPlannedCtgList = this.searchTermCtg(prodCategory);

		return DpsPlannedCtgList.get(0);
	}

	// 今期の計画対象カテゴリ領域を取得
	@Override
	public List<DpsPlannedCtg> searchTermCtg(String prodCategory) throws DataNotFoundException {

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("prodCategory", prodCategory);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".select", paramMap);
	}

    @Override
    public List<DpsPlannedCtg> searchByCtgList(List<SosCtg> sosCtgList) throws DataNotFoundException {
        // ----------------------
        // 引数チェック
        // ----------------------
        if (sosCtgList == null) {
            final String errMsg = "カテゴリリストがnull";
            throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
        }

        // ----------------------
        // 検索条件生成
        // ----------------------
        Map<String, Object> paramMap = new HashMap<String, Object>(1);
        paramMap.put("sosCtgList", sosCtgList);

        // ----------------------
        // 検索実行
        // ----------------------
        return super.selectList(getSqlMapName() + ".selectList", paramMap);
    }

    @Override
    public List<DpsPlannedCtg> selectByPlanLevel(ProdPlanLevel planLevel) throws DataNotFoundException {

        // ----------------------
        // 検索条件生成
        // ----------------------
        Map<String, Object> paramMap = new HashMap<String, Object>(1);
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
        return super.selectList(getSqlMapName() + ".selectByPlanLevel", paramMap);
    }
}
