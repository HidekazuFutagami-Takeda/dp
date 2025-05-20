package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.InsDocPlan;
import jp.co.takeda.model.div.HoInsType;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.StatusForInsDocPlan;

import org.springframework.stereotype.Repository;

/**
 * 施設計画にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("insDocPlanDao")
public class InsDocPlanDaoImpl extends AbstractDao implements InsDocPlanDao {

	private static final String SQL_MAP = "DPS_I_INS_DOC_PLAN_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 施設医師別計画を取得
	public InsDocPlan search(Long seqKey) throws DataNotFoundException {
		return (InsDocPlan) super.selectBySeqKey(seqKey);
	}

//	// ユニークキーで施設計画を取得
//	public InsPlan searchUk(Integer jgiNo, String prodCode, String insNo) throws DataNotFoundException {
//		// ----------------------
//		// 引数チェック
//		// ----------------------
//		if (jgiNo == null) {
//			final String errMsg = "従業員番号がnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}
//		if (prodCode == null) {
//			final String errMsg = "品目固定コードがnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}
//		if (insNo == null) {
//			final String errMsg = "施設コードがnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}
//
//		// ----------------------
//		// 検索条件生成
//		// ----------------------
//		InsPlan record = new InsPlan();
//		record.setJgiNo(jgiNo);
//		record.setProdCode(prodCode);
//		record.setInsNo(insNo);
//		return (InsPlan) super.selectByUniqueKey(record);
//	}

	// 施設医師計画のヘッダー情報を取得
	public Map<String, Object> searchHeader(Integer jgiNo, String prodCode, InsType insType) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("prodCode", prodCode);
		paramMap.put("completeStatus", StatusForInsDocPlan.PLAN_COMPLETED.getDbValue());
		List<HoInsType> hoInsTypeList = InsType.convertHoInsType(insType);
		paramMap.put("hoInsTypeList", hoInsTypeList);

		return dataAccess.queryForObject(getSqlMapName() + ".selectHeader", paramMap);
	}

	// 施設医師計画のリストを取得
	public List<Map<String, Object>> searchList(Integer jgiNo, String prodCode, InsType insType,String planDispType ) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(5);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("prodCode", prodCode);
		List<HoInsType> hoInsTypeList = InsType.convertHoInsType(insType);
		paramMap.put("hoInsTypeList", hoInsTypeList);
		paramMap.put("planDispType", planDispType);
		paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);

		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}

	// 施設医師別計画を登録
	public void insert(InsDocPlan record) throws DuplicateException {
		super.insert(record);
	}

	// 施設医師別計画の計画値を更新
	public int update(InsDocPlan record) throws DuplicateException {
		return super.update(record);
	}

	// 施設医師別計画を削除
	public int delete(Long seqKey, Date upDate) throws DuplicateException {
		return super.deleteBySeqKey(seqKey, upDate);
	}


}
