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
import jp.co.takeda.model.ManageOfficePlan;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.Sales;

/**
 * 管理の営業所別計画にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("manageOfficePlanDao")
public class ManageOfficePlanDaoImpl extends AbstractManageDao implements ManageOfficePlanDao {

	private static final String SQL_MAP = "DPM_I_OFFICE_PLAN_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// キー検索
	public ManageOfficePlan search(Long seqKey) throws DataNotFoundException {
		return (ManageOfficePlan) super.selectBySeqKey(seqKey);
	}

	// ユニークキー検索
	public ManageOfficePlan searchUk(InsType insType, String prodCode, String sosCd) throws DataNotFoundException {

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
		ManageOfficePlan record = new ManageOfficePlan();
		record.setInsType(insType);
		record.setProdCode(prodCode);
		record.setSosCd(sosCd);
		return super.selectByUniqueKey(record);
	}

	// リスト検索/組織一覧
	public List<ManageOfficePlan> searchListByProd(String sortString, String prodCode, String sosCd2) throws DataNotFoundException {

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

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(5);
		paramMap.put("sortString", sortString);
		paramMap.put("prodCode", prodCode);
		paramMap.put("sosCd2", sosCd2);
		paramMap.put("shiten", SosMst.SHITEN_GROUP);
			paramMap.put("isOnc", true);

		return super.selectList(getSqlMapName() + ".selectListByProd", paramMap);
	}

	// リスト検索/品目一覧
	public List<ManageOfficePlan> searchListBySos(String sortString, String sosCd, String category, String vaccineCode) throws DataNotFoundException {

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
		// カテゴリがONCの場合は、下位計画（調整用）を担当者別計画積上げにする
// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
//		if(category == ProdCategory.ONC){
		//if(category == ProdCategory.MMP || category == ProdCategory.SPBU || category == ProdCategory.ONC || category == ProdCategory.SHIIRE){
			paramMap.put("isOnc", true);
		//} else {
		//	paramMap.put("isOnc", false);
		//}
// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
		if (category.equals(vaccineCode)) {
			paramMap.put("sales", Sales.VACCHIN.getDbValue());
		} else {
			paramMap.put("sales", Sales.IYAKU.getDbValue());
		}
		return super.selectList(getSqlMapName() + ".selectListBySos", paramMap);
	}

	// 登録
	public void insert(ManageOfficePlan manageOfficePlan, String pgId) throws DuplicateException {
		super.insert(manageOfficePlan, pgId);
	}

	// 更新
	public int update(ManageOfficePlan manageOfficePlan, String pgId) {
		return super.update(manageOfficePlan, pgId);
	}

}
