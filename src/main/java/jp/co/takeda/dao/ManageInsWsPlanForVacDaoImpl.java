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
import jp.co.takeda.model.ManageInsWsPlanForVac;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.Prefecture;
import jp.co.takeda.util.StringUtil;

/**
 * 管理のワクチン用施設特約店別計画にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("manageInsWsPlanForVacDao")
public class ManageInsWsPlanForVacDaoImpl extends AbstractManageDao implements ManageInsWsPlanForVacDao {

	private static final String SQL_MAP = "DPM_V_INS_WS_PLAN_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// キー検索
	public ManageInsWsPlanForVac search(Long seqKey) throws DataNotFoundException {
		return (ManageInsWsPlanForVac) super.selectBySeqKey(seqKey);
	}

	// ユニークキー検索
	public ManageInsWsPlanForVac searchUk(String prodCode, String insNo, String tmsTytenCd) throws DataNotFoundException {

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
		ManageInsWsPlanForVac record = new ManageInsWsPlanForVac();
		record.setProdCode(prodCode);
		record.setInsNo(insNo);
		record.setTmsTytenCd(tmsTytenCd);
		return super.selectByUniqueKey(record);
	}

	// リスト検索/施設一覧
	public List<ManageInsWsPlanForVac> searchListByProd(String sortString, String prodCode, String sosCd3, Integer jgiNo, ActivityType activityType, Prefecture addrCodePref,
		String addrCodeCity, String insNo, String tmsTytenCd, String oyakoKb,String oyakoKb2, String tgtInsKb, String oyakoKbProdCode) throws DataNotFoundException {

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
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sortString", sortString);
		paramMap.put("prodCode", prodCode);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("activityType", activityType);
		paramMap.put("insNo", insNo);
		paramMap.put("addrCodePref", addrCodePref);
		paramMap.put("addrCodeCity", addrCodeCity);
		paramMap.put("tmsTytenCd", StringUtil.toLikeCondition(tmsTytenCd));
		paramMap.put("oyakoKb", OyakoKbHelper.getOyakoKb(oyakoKb,oyakoKb2,oyakoKbProdCode));
		paramMap.put("tgtInsKb", tgtInsKb);
		// 固定条件
		paramMap.put("jokenSetList", new JokenSet[]{ JokenSet.WAKUTIN_MR });
		paramMap.put("jgiKbList", new JgiKb[]{ JgiKb.JGI, JgiKb.CONTRACT_MR});

		return super.selectList(getSqlMapName() + ".selectListByProd", paramMap);
	}

	// リスト検索/品目一覧
	public List<ManageInsWsPlanForVac> searchListByInsWs(String sortString, String insNo, String tmsTytenCd, boolean allProdFlg) throws DataNotFoundException {

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
		if (allProdFlg) {
			paramMap.put("allProdFlg", allProdFlg);
		}

		return super.selectList(getSqlMapName() + ".selectListByInsWs", paramMap);
	}

	// 登録
	public void insert(ManageInsWsPlanForVac manageInsWsPlanForVac, String pgId) throws DuplicateException {
		super.insert(manageInsWsPlanForVac, pgId);
	}

	// 更新
	public int update(ManageInsWsPlanForVac manageInsWsPlanForVac, String pgId) {
		return super.update(manageInsWsPlanForVac, pgId);
	}

}
