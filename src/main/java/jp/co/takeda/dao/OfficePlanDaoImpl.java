package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.OfficePlan;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.view.OfficePlanWithChosei;

/**
 * 営業所計画にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("officePlanDao")
public class OfficePlanDaoImpl extends AbstractDao implements OfficePlanDao {

	private static final String SQL_MAP = "DPS_I_OFFICE_PLAN_SqlMap";

	/**
	 * 計画支援汎用マスタDAO
	 */
    @Autowired(required = true)
    @Qualifier("dpsCodeMasterDao")
    protected DpsCodeMasterDao dpsCodeMasterDao;

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 営業所計画をユニークキーで取得
	public OfficePlan searchUk(String sosCd3, String prodCode) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		OfficePlan record = new OfficePlan();
		record.setSosCd(sosCd3);
		record.setProdCode(prodCode);

		// ----------------------
		// 検索実行
		// ----------------------
		return (OfficePlan) super.selectByUniqueKey(record);
	}

	// 営業所計画のリストを取得
//	public List<OfficePlan> searchList(String sortString, String sosCd3, ProdCategory category) throws DataNotFoundException {
//	public List<OfficePlan> searchList(String sortString, String sosCd3, String category) throws DataNotFoundException {
	public List<OfficePlan> searchList(String sortString, String sosCd3, String category, Sales sales) throws DataNotFoundException {
	// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("sortString", sortString);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("category", category);
//		paramMap.put("sales", Sales.IYAKU);
		paramMap.put("sales", sales);
		paramMap.put("planTargetFlg", true);

		// ONC品目判定はカテゴリで判断するため削除
		// ONC組織フラグがONの場合、MR区分設定
//		if(BooleanUtils.isTrue(isOncSos)) paramMap.put("mrCat", MrCat.ONC_MR);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}

// add start 2016/12/12 S.Hayashi J16-0015_ユーザ要望改定
	// 帳票用営業所計画のリストを取得
	public List<OfficePlan> searchListReport(String sortString, String sosCd3, String category) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -----------------------------
		// ワクチンコードの取得
		// -----------------------------
		List<DpsCCdMst> searchList = new ArrayList<DpsCCdMst>();
		try {
			// カテゴリの検索
			searchList = dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.VAC.getDbValue());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + DpsCodeMaster.VAC.getDbValue() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		String vaccineCode = searchList.get(0).getDataCd();

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("sortString", sortString);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("category", category);
		if(category.equals(vaccineCode)) {
			paramMap.put("sales", Sales.VACCHIN);
		}else{
			paramMap.put("sales", Sales.IYAKU);
		}
		paramMap.put("planTargetFlg", true);


		// ONC品目判定はカテゴリで判断するため削除
		// ONC組織フラグがONの場合、MR区分設定
//		if(BooleanUtils.isTrue(isOncSos)) paramMap.put("mrCat", MrCat.ONC_MR);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectListReport", paramMap);
	}
// add end   2016/12/12 S.Hayashi J16-0015_ユーザ要望改定

	// 営業所計画を登録
	public void insert(OfficePlan record) throws DuplicateException {
		super.insert(record);
	}

	// 営業所計画を更新
	public int update(OfficePlan record) {
		return super.update(record);
	}

	// 営業所計画を削除
	public int delete(Long seqKey, Date upDate) {
		return super.deleteBySeqKey(seqKey, upDate);
	}

	public List<OfficePlanWithChosei> searchList2(String sortString, String sosCd3, String category, Sales sales) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(5);
		paramMap.put("sortString", sortString);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("category", category);
		paramMap.put("sales", sales);
		paramMap.put("planTargetFlg", true);

		// ONC品目判定はカテゴリで判断するため削除
		// ONC組織フラグがONの場合、MR区分設定
//		if(BooleanUtils.isTrue(isOncSos)) paramMap.put("mrCat", MrCat.ONC_MR);
		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectList2", paramMap);
	}

}
