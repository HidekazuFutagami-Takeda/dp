package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.MrPlanStatus;
import jp.co.takeda.model.div.EstimationBasePlan;
import jp.co.takeda.security.DpUser;

/**
 * 担当者別計画立案ステータスにアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("mrPlanStatusDao")
public class MrPlanStatusDaoImpl extends AbstractDao implements MrPlanStatusDao {

	private static final String SQL_MAP = "DPS_I_MR_PLAN_STATUS_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 担当者別計画立案ステータスを取得
	public MrPlanStatus search(Long seqKey) throws DataNotFoundException {
		return (MrPlanStatus) super.selectBySeqKey(seqKey);
	}

	// 組織コード、品目固定コードを元に、担当者別計画立案ステータスを取得
	public MrPlanStatus search(String sosCd, String prodCode) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		MrPlanStatus record = new MrPlanStatus();
		record.setSosCd(sosCd);
		record.setProdCode(prodCode);
		return (MrPlanStatus) super.selectByUniqueKey(record);
	}

	// 組織コードを元に、担当者別計画立案ステータスの一覧を取得
	public List<MrPlanStatus> searchList(String sosCd) throws DataNotFoundException {
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
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("sosCd", sosCd);
		paramMap.put("sortString", SORT_STRING);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}

	// 組織コード、品目固定コードを元に、担当者別計画立案ステータスを取得
	public List<MrPlanStatus> searchSosCdProdList(String sosCd, String prodCode) throws DataNotFoundException {
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
		MrPlanStatus record = new MrPlanStatus();
		record.setSosCd(sosCd);
		record.setProdCode(prodCode);
		return dataAccess.queryForList(getSqlMapName() + ".selectUk", record);
	}

	// 組織コードを元に、担当者別計画立案ステータスの一覧を取得
	public List<MrPlanStatus> searchEstimatingList(String sortString, String appServerKbn) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (appServerKbn == null) {
			final String errMsg = "サーバ区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("sortString", sortString);
		paramMap.put("appServerKbn", appServerKbn);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectEstimatingListByServerKbn", paramMap);
	}

	// 組織コードを元に、担当者別計画立案ステータスの一覧を取得
	public List<MrPlanStatus> searchSosCdProdList(String sosCd, List<String> prodList ) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		if (prodList == null) {
			final String errMsg = "品目固定コード一覧がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("sosCd", sosCd);
		paramMap.put("prodList", prodList);
		paramMap.put("sortString", SORT_STRING);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectSosCdProdList", paramMap);
	}

	// 担当者別計画立案ステータスを登録
	public void insert(MrPlanStatus record) throws DuplicateException {
		if (record.getEstimationBasePlan() == null) {
			record.setEstimationBasePlan(EstimationBasePlan.OFFICE_PLAN);
		}

		super.insert(record);
	}

	// 担当者別計画立案ステータスを更新
	public int update(MrPlanStatus record) {
		if (record.getEstimationBasePlan() == null) {
			record.setEstimationBasePlan(EstimationBasePlan.OFFICE_PLAN);
		}

		return super.update(record);
	}

	// 担当者別計画立案ステータスを更新
	public int update(MrPlanStatus record, DpUser dpUser) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (record == null) {
			final String errMsg = "更新情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 更新実行
		// ----------------------
		record.setUpJgiNo(dpUser.getJgiNo());
		record.setUpJgiName(dpUser.getJgiName());
		return dataAccess.execute(getSqlMapName() + ".update", record);
	}

	// 担当者別計画立案ステータスを削除
	public int delete(Long seqKey, Date upDate) {
		return super.deleteBySeqKey(seqKey, upDate);
	}
}
