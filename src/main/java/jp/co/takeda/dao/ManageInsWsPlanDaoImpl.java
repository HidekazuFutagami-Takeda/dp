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
import jp.co.takeda.model.ManageInsWsPlan;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.util.StringUtil;

/**
 * 管理の施設特約店別計画にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("manageInsWsPlanDao")
public class ManageInsWsPlanDaoImpl extends AbstractManageDao implements ManageInsWsPlanDao {

	private static final String SQL_MAP = "DPM_I_INS_WS_PLAN_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// キー検索
	public ManageInsWsPlan search(Long seqKey) throws DataNotFoundException {
		return (ManageInsWsPlan) super.selectBySeqKey(seqKey);
	}

	// ユニークキー検索
	public ManageInsWsPlan searchUk(String prodCode, String insNo, String tmsTytenCd) throws DataNotFoundException {

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
		if (tmsTytenCd == null) {
			final String errMsg = "TMS特約店コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		ManageInsWsPlan record = new ManageInsWsPlan();
		record.setProdCode(prodCode);
		record.setInsNo(insNo);
		record.setTmsTytenCd(tmsTytenCd);
		return super.selectByUniqueKey(record);
	}

	// リスト検索/施設一覧
	public List<ManageInsWsPlan> searchListByProd(String sortString, String prodCode, String sosCd3, String sosCd4, Integer jgiNo, InsType insType, String relnInsNo,
		boolean allInsFlg, String tmsTytenCd, String oyakoKb,String oyakoKb2, String tgtInsKb, String oyakoKbProdCode) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		if (sosCd3 == null && sosCd4 == null && jgiNo == null) {
			final String errMsg = "従業員または組織が未設定";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sortString", sortString);
		paramMap.put("prodCode", prodCode);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("sosCd4", sosCd4);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("hoInsTypeList", InsType.convertHoInsType(insType));
		paramMap.put("relnInsNo", relnInsNo);
		if (allInsFlg) {
			paramMap.put("allInsFlg", allInsFlg);
		}
		paramMap.put("tmsTytenCd", StringUtil.toLikeCondition(tmsTytenCd));
		paramMap.put("oyakoKb", OyakoKbHelper.getOyakoKb(oyakoKb,oyakoKb2,oyakoKbProdCode));
		paramMap.put("tgtInsKb", tgtInsKb);
		// 固定条件
		if(jgiNo == null){
			paramMap.put("jokenSetList", new JokenSet[]{ JokenSet.IYAKU_MR });
			paramMap.put("jgiKbList", new JgiKb[]{ JgiKb.JGI, JgiKb.CONTRACT_MR, JgiKb.EIGYOSHO_ZATU, JgiKb.DUMMY_MR});
		}

		return super.selectList(getSqlMapName() + ".selectListByProd", paramMap);
	}


	// リスト検索/品目一覧
	public List<ManageInsWsPlan> searchListByInsWs(String sortString, String insNo, String tmsTytenCd, String category, boolean allProdFlg, String tgtInsKb, String sales) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (tmsTytenCd == null) {
			final String errMsg = "特約店コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sortString", sortString);
		paramMap.put("insNo", insNo);
		paramMap.put("tmsTytenCd", tmsTytenCd);
		paramMap.put("category", category);
		if (allProdFlg) {
			paramMap.put("allProdFlg", allProdFlg);
		}
		paramMap.put("tgtInsKb", tgtInsKb);
		paramMap.put("sales", sales);

		return super.selectList(getSqlMapName() + ".selectListByInsWs", paramMap);
	}

	// 登録
	public void insert(ManageInsWsPlan manageInsWsPlan, String pgId) throws DuplicateException {
		super.insert(manageInsWsPlan, pgId);
	}

	// 更新
	public int update(ManageInsWsPlan manageInsWsPlan, String pgId) {
		return super.update(manageInsWsPlan, pgId);
	}

}
