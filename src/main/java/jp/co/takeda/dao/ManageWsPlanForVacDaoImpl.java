package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.ManageWsPlanForVac;
import jp.co.takeda.util.StringUtil;

import org.springframework.stereotype.Repository;

/**
 * 管理のワクチン用特約店別計画にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("manageWsPlanForVacDao")
public class ManageWsPlanForVacDaoImpl extends AbstractManageDao implements ManageWsPlanForVacDao {

	private static final String SQL_MAP = "DPM_V_WS_PLAN_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// キー検索
	public ManageWsPlanForVac search(Long seqKey) throws DataNotFoundException {
		return (ManageWsPlanForVac) super.selectBySeqKey(seqKey);
	}

	// ユニークキー検索
	public ManageWsPlanForVac searchUk(String prodCode, String tmsTytenCd) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (tmsTytenCd == null) {
			final String errMsg = "TMS特約店コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		ManageWsPlanForVac record = new ManageWsPlanForVac();
		record.setProdCode(prodCode);
		record.setTmsTytenCd(tmsTytenCd);
		return super.selectByUniqueKey(record);
	}

	// リスト検索/特約店一覧
	public List<ManageWsPlanForVac> searchListByProd(String sortString, String prodCode, String tmsTytenCd, boolean allWsFlg) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (tmsTytenCd == null) {
			final String errMsg = "TMS特約店コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(6);
		paramMap.put("sortString", sortString);
		paramMap.put("prodCode", prodCode);
		paramMap.put("tmsTytenCd", StringUtil.toLikeCondition(tmsTytenCd));
		if (allWsFlg) {
			paramMap.put("allWsFlg", allWsFlg);
		}

		return super.selectList(getSqlMapName() + ".selectListByProd", paramMap);
	}

	public List<ManageWsPlanForVac> searchListByWs(String sortString, String tmsTytenCd, boolean allProdFlg) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (tmsTytenCd == null) {
			final String errMsg = "TMS特約店コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("sortString", sortString);
		paramMap.put("tmsTytenCd", tmsTytenCd);
		if (allProdFlg) {
			paramMap.put("allProdFlg", allProdFlg);
		}

		return super.selectList(getSqlMapName() + ".selectListByWs", paramMap);
	}

	// 登録
	public void insert(ManageWsPlanForVac manageWsPlanForVac, String pgId) throws DuplicateException {
		super.insert(manageWsPlanForVac, pgId);
	}

	// 更新
	public int update(ManageWsPlanForVac manageWsPlanForVac, String pgId) {
		return super.update(manageWsPlanForVac, pgId);
	}

}
