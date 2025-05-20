package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.InsPlan;
import jp.co.takeda.model.div.HoInsType;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;

import org.springframework.stereotype.Repository;

/**
 * 施設計画にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("insPlanDao")
public class InsPlanDaoImpl extends AbstractDao implements InsPlanDao {

	private static final String SQL_MAP = "DPS_I_INS_PLAN_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 施設計画を取得
	public InsPlan search(Long seqKey) throws DataNotFoundException {
		return (InsPlan) super.selectBySeqKey(seqKey);
	}

	// ユニークキーで施設計画を取得
	public InsPlan searchUk(Integer jgiNo, String prodCode, String insNo) throws DataNotFoundException {
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
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		InsPlan record = new InsPlan();
		record.setJgiNo(jgiNo);
		record.setProdCode(prodCode);
		record.setInsNo(insNo);
		return (InsPlan) super.selectByUniqueKey(record);
	}

	// 施設計画のリストを取得
	public List<InsPlan> searchList(String sortString, Integer jgiNo, String prodCode, InsType insType) throws DataNotFoundException {

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
		paramMap.put("sortString", sortString);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("prodCode", prodCode);
		List<HoInsType> hoInsTypeList = InsType.convertHoInsType(insType);
		paramMap.put("hoInsTypeList", hoInsTypeList);

		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}

	// 施設～医師間の調整チェック
	public Map<String, Object> checkChoseiInsWs(String sosCd3, String sosCd4, Integer jgiNo, String prodCode) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null && sosCd4 == null && sosCd3 == null) {
			final String errMsg = "組織コードまたは従業員番号がnull";
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
		paramMap.put("sosCd4", sosCd4);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("prodCode", prodCode);

		// 固定条件
		List<JgiKb> jgiKbList = new ArrayList<JgiKb>();
		jgiKbList.add(JgiKb.JGI);
		jgiKbList.add(JgiKb.CONTRACT_MR);
		jgiKbList.add(JgiKb.DUMMY_MR);
		paramMap.put("jgiKbList", jgiKbList);
		// 条件セット
		paramMap.put("jokenSetList", new JokenSet[]{ JokenSet.IYAKU_MR });

		return dataAccess.queryForObject(getSqlMapName() + ".existChoseiInsWs", paramMap);
	}

	// 施設計画を登録
	public void insert(InsPlan record) throws DuplicateException {
		super.insert(record);
	}

	// 施設計画を登録（医師別計画の積上げ）
	public void insertTumiage(Integer jgiNo, String prodCode, InsType insType) {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("prodCode", prodCode);
		List<HoInsType> hoInsTypeList = InsType.convertHoInsType(insType);
		paramMap.put("hoInsTypeList", hoInsTypeList);

		DpUser dpUser = DpUserInfo.getDpUserInfo().getLoginUser();
		paramMap.put("upJgiNo", dpUser.getJgiNo());
		paramMap.put("upJgiName", dpUser.getJgiName());

		super.dataAccess.execute(getSqlMapName() + ".insertTumiage", paramMap);
	}

	// 施設計画の計画値を更新
	public int update(InsPlan record) throws DuplicateException {
		return super.update(record);
	}

	// 施設計画を削除
	public int delete(Long seqKey, Date upDate) throws DuplicateException {
		return super.deleteBySeqKey(seqKey, upDate);
	}

	// 施設計画を削除（医師別計画の積上げ）
	public int deleteTumiage(Integer jgiNo, String prodCode, InsType insType) {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("prodCode", prodCode);
		List<HoInsType> hoInsTypeList = InsType.convertHoInsType(insType);
		paramMap.put("hoInsTypeList", hoInsTypeList);

		return super.dataAccess.execute(getSqlMapName() + ".deleteTumiage", paramMap);
	}

}
