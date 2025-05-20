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
import jp.co.takeda.model.ExceptDistIns;
import jp.co.takeda.model.ExceptProd;
import jp.co.takeda.model.div.PlanType;
import jp.co.takeda.model.div.RegistType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;

/**
 * 配分除外施設にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("exceptDistInsDao")
public class ExceptDistInsDaoImpl extends AbstractDao implements ExceptDistInsDao {

	private static final String SQL_MAP = "DPS_I_EXCEPT_DIST_INS_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 配分除外施設を取得
	public ExceptDistIns search(Long seqKey) throws DataNotFoundException {
		return (ExceptDistIns) super.selectBySeqKey(seqKey);
	}

	// 施設コードで配分除外施設を取得
	public ExceptDistIns searchByInsNo(String insNo) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		Map<String, String> paramMap = new HashMap<String, String>(1);
		paramMap.put("insNo", insNo);

		return dataAccess.queryForObject(getSqlMapName() + ".selectByInsNo", paramMap);
	}

	// 施設コード、品目固定コードで配分除外施設を取得
	public ExceptDistIns search(String insNo, String prodCode, PlanType planType) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("insNo", insNo);
		paramMap.put("prodCode", prodCode);
		paramMap.put("planType", planType);

		return dataAccess.queryForObject(getSqlMapName() + ".selectUk", paramMap);
	}

	// 施設コード、品目固定コードで配分除外施設かどうかを取得（施設指定の場合でも判定）
	public boolean isExceptDistIns(String insNo, String prodCode) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("insNo", insNo);
		paramMap.put("prodCode", prodCode);

		try {
			dataAccess.queryForObject(getSqlMapName() + ".isExceptDistIns", paramMap);
			return true;
		} catch (DataNotFoundException e) {
			return false;
		}
	}

	// 配分除外施設を登録
	public void insert(ExceptDistIns record) throws DuplicateException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (record == null) {
			final String errMsg = "登録情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

//		// ----------------------
//		// 重複チェック
//		// 施設コードでselect
//		// ----------------------
//		Map<String, Object> paramMap = new HashMap<String, Object>(1);
//		paramMap.put("insNo", record.getInsNo());
//
//		try {
//			dataAccess.queryForObject(getSqlMapName() + ".selectByInsNo", paramMap);
//			this.insertDataDuplicate();
//		} catch (DataNotFoundException e) {
//		}

		// ----------------------
		// 登録実行
		// ----------------------
		Map<String, Object> insParamMap = new HashMap<String, Object>(9);
		insParamMap.put("insNo", record.getInsNo());
		if(record.getEstimationFlg() != null) {
			if (record.getEstimationFlg()) {
				insParamMap.put("estimationFlg", "1");
			}else {
				insParamMap.put("estimationFlg", "0");
			}
		}
		if (record.getExceptFlg() != null) {
			if (record.getExceptFlg()) {
				insParamMap.put("exceptFlg", "1");
			}else {
				insParamMap.put("exceptFlg", "0");
			}
		}
		insParamMap.put("registType", RegistType.DISP);
		// 登録者・更新者情報設定
		DpUser dpUser = DpUserInfo.getDpUserInfo().getLoginUser();
		insParamMap.put("upJgiNo", dpUser.getJgiNo());
		insParamMap.put("upJgiName", dpUser.getJgiName());
		if (record.getExceptProd() != null && record.getExceptProd().size() > 0) {
			for (ExceptProd prod : record.getExceptProd()) {
				insParamMap.put("prodCode", prod.getProdCode());
				insParamMap.put("estimationFlg", prod.getStrEstimationFlg());
				insParamMap.put("exceptFlg", prod.getStrExceptFlg());
				dataAccess.execute(getSqlMapName() + ".insert", insParamMap);
			}
		} else {
			insParamMap.put("prodCode", null);
			dataAccess.execute(getSqlMapName() + ".insert", insParamMap);
		}
	}

	// 施設コード、品目コードに紐付く配分除外施設を削除
	public int delete(String insNo, Date upDate,  List<String> prodCodeList) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (upDate == null) {
			final String errMsg = "最終更新日時がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 削除条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("insNo", insNo);
//		paramMap.put("upDate", upDate);
		paramMap.put("exceptProdList", prodCodeList);

		// ----------------------
		// 削除実行
		// ----------------------
		int count = dataAccess.execute(getSqlMapName() + ".deleteByInsNo", paramMap);
//		if (count == 0) {
//			optimisticLock();
//		}
		return count;
	}

	// 施設コード、品目コードがNullの配分除外施設を削除
	public int deleteByProdNull(String insNo, Date upDate) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 削除条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("insNo", insNo);
		paramMap.put("upDate", upDate);

		// ----------------------
		// 削除実行
		// ----------------------
		int count = dataAccess.execute(getSqlMapName() + ".deleteByProdNull", paramMap);

		return count;
	}

	// 施設コード、品目コードに紐付く配分除外施設を論理削除
	public int update(String insNo, Date upDate,  List<String> prodCodeList) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (upDate == null) {
			final String errMsg = "最終更新日時がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 削除条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("insNo", insNo);
		paramMap.put("exceptProdList", prodCodeList);
		paramMap.put("estimationFlg", Boolean.FALSE);
		paramMap.put("exceptFlg", Boolean.FALSE);

		// ----------------------
		// 削除実行
		// ----------------------
		int count = dataAccess.execute(getSqlMapName() + ".update", paramMap);

		return count;
	}

	// 配分除外施設のリストを取得
	public List<ExceptDistIns> searchList(String sortString, String sosCd3, String sosCd4, Integer jgiNo, String oyakoKb, String oyakoKb2) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null && sosCd4 == null && jgiNo == null) {
			final String errMsg = "組織・従業員がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(5);
		paramMap.put("sortString", sortString);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("sosCd4", sosCd4);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("oyakoKb", OyakoKbHelper.getOyakoKb(oyakoKb,oyakoKb2,null));

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}
}
