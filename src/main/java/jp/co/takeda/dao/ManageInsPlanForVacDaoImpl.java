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
import jp.co.takeda.model.ManageInsPlanForVac;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.Prefecture;

/**
 * 管理のワクチン用施設別計画にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("manageInsPlanForVacDao")
public class ManageInsPlanForVacDaoImpl extends AbstractManageDao implements ManageInsPlanForVacDao {

	private static final String SQL_MAP = "DPM_V_INS_PLAN_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// キー検索
	public ManageInsPlanForVac search(Long seqKey) throws DataNotFoundException {
		return (ManageInsPlanForVac) super.selectBySeqKey(seqKey);
	}

	// ユニークキー検索
	public ManageInsPlanForVac searchUk(String prodCode, String insNo) throws DataNotFoundException {

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
		ManageInsPlanForVac record = new ManageInsPlanForVac();
		record.setProdCode(prodCode);
		record.setInsNo(insNo);
		return super.selectByUniqueKey(record);
	}

	// リスト検索/施設一覧
	public List<ManageInsPlanForVac> searchListByProd(String sortString, String prodCode, Integer jgiNo, ActivityType activityType, Prefecture addrCodePref, String addrCodeCity,
		String insNo, String oyakoKb, String oyakoKb2, String tgtInsKb, String oyakoKbProdCode) throws DataNotFoundException {

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

		return super.selectList(getSqlMapName() + ".selectListByProd", paramMap);
	}

	// 施設計画積上取得
	public Long searchSumByProd(String prodCode, Integer jgiNo, ActivityType activityType) {

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

		// ----------------------
		// 検索実行
		// ----------------------
		Long result = null;
		try {
			result = super.select(getSqlMapName() + ".selectSumByProd", paramMap);
		} catch (DataNotFoundException e) {
		}

		return result;
	}

	// リスト検索/品目一覧
	public List<ManageInsPlanForVac> searchListByIns(String sortString, String insNo, boolean allProdFlg) throws DataNotFoundException {

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
		if (allProdFlg) {
			paramMap.put("allProdFlg", allProdFlg);
		}

		return super.selectList(getSqlMapName() + ".selectListByIns", paramMap);
	}

	// 登録
	public void insert(ManageInsPlanForVac manageInsPlanForVac, String pgId) throws DuplicateException {
		super.insert(manageInsPlanForVac, pgId);
	}

	// 更新
	public int update(ManageInsPlanForVac manageInsPlanForVac, String pgId) {
		return super.update(manageInsPlanForVac, pgId);
	}

}
