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
import jp.co.takeda.model.ManageInsPlan;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.Sales;

/**
 * 管理の施設別計画にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("manageInsPlanDao")
public class ManageInsPlanDaoImpl extends AbstractManageDao implements ManageInsPlanDao {

	private static final String SQL_MAP = "DPM_I_INS_PLAN_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// キー検索
	public ManageInsPlan search(Long seqKey) throws DataNotFoundException {
		return (ManageInsPlan) super.selectBySeqKey(seqKey);
	}

	// ユニークキー検索
	public ManageInsPlan searchUk(String prodCode, String insNo) throws DataNotFoundException {

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
	public List<ManageInsPlan> searchListByProd(String sortString, String prodCode, Integer jgiNo, InsType insType, String relnInsNo, boolean allInsFlg,
			String oyakoKb,String oyakoKb2, String tgtInsKb, String oyakoKbProdCode) throws DataNotFoundException {

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
		if (allInsFlg) {
			paramMap.put("allInsFlg", allInsFlg);
		}
		paramMap.put("oyakoKb", OyakoKbHelper.getOyakoKb(oyakoKb,oyakoKb2,oyakoKbProdCode));
		paramMap.put("tgtInsKb", tgtInsKb);

		return super.selectList(getSqlMapName() + ".selectListByProd", paramMap);
	}

	// 施設計画積上取得
	public Long searchSumByProd(String prodCode, Integer jgiNo, InsType insType, String oyakoKb, String oyakoKb2, String oyakoKbProdCode) {

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
		paramMap.put("oyakoKb", OyakoKbHelper.getOyakoKb(oyakoKb, oyakoKb2 , oyakoKbProdCode));

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
	public List<ManageInsPlan> searchListByIns(String sortString, String insNo, String category, boolean allProdFlg, String tgtInsKb, String vaccineCode) throws DataNotFoundException {

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
		paramMap.put("category", category);
		if (allProdFlg) {
			paramMap.put("allProdFlg", allProdFlg);
		}
		paramMap.put("tgtInsKb", tgtInsKb);
		if (category.equals(vaccineCode)) {
			paramMap.put("sales", Sales.VACCHIN.getDbValue());
		} else {
			paramMap.put("sales", Sales.IYAKU.getDbValue());
		}

		return super.selectList(getSqlMapName() + ".selectListByIns", paramMap);
	}

	// 登録
	public void insert(ManageInsPlan manageInsPlan, String pgId) throws DuplicateException {
		super.insert(manageInsPlan, pgId);
	}

	// 更新
	public int update(ManageInsPlan manageInsPlan, String pgId) {
		return super.update(manageInsPlan, pgId);
	}

}
