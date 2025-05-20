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
import jp.co.takeda.bean.Constants;
import jp.co.takeda.model.ManageWsPlan;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.util.StringUtil;

/**
 * 管理の特約店別計画にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("manageWsPlanDao")
public class ManageWsPlanDaoImpl extends AbstractManageDao implements ManageWsPlanDao {

	private static final String SQL_MAP = "DPM_I_WS_PLAN_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// キー検索
	public ManageWsPlan search(Long seqKey) throws DataNotFoundException {
		return (ManageWsPlan) super.selectBySeqKey(seqKey);
	}

	// ユニークキー検索
	public ManageWsPlan searchUk(InsType insType, String prodCode, String tmsTytenCd) throws DataNotFoundException {

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
		if (tmsTytenCd == null) {
			final String errMsg = "TMS特約店コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		ManageWsPlan record = new ManageWsPlan();
		record.setInsType(insType);
		record.setProdCode(prodCode);
		record.setTmsTytenCd(tmsTytenCd);
		return super.selectByUniqueKey(record);
	}

	// リスト検索/特約店一覧
	public List<ManageWsPlan> searchListByProd(String sortString, String prodCode, String tmsTytenCd, boolean allWsFlg) throws DataNotFoundException {

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

	// リスト検索/品目一覧
	@Override
	public List<ManageWsPlan> searchListByWs(String tmsTytenCd, String category, boolean allProdFlg, boolean isVaccine) throws DataNotFoundException {

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
		paramMap.put("tmsTytenCd", tmsTytenCd);
		if(isVaccine) {
			paramMap.put("isVaccine", isVaccine);
		}
		else {
		paramMap.put("category", category);
		}
		if (allProdFlg) {
			paramMap.put("allProdFlg", allProdFlg);
		}
		//add Start 2025/2/19  H.Futagami 202410要望:カテゴリに全て（医薬）追加
		paramMap.put("categoryIyakuAll", Constants.CATEGORY_IYAKU_ALL);
		paramMap.put("categoryVxBU", Constants.CATEGORY_VXBU);
		//add End 2025/2/19  H.Futagami 202410要望:カテゴリに全て（医薬）追加

		return super.selectList(getSqlMapName() + ".selectListByWs", paramMap);
	}

	// 登録
	public void insert(ManageWsPlan manageWsPlan, String pgId) throws DuplicateException {
		super.insert(manageWsPlan, pgId);
	}

	// 更新
	public int update(ManageWsPlan manageWsPlan, String pgId) {
		return super.update(manageWsPlan, pgId);
	}

}
