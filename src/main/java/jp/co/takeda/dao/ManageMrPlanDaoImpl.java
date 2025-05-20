package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.ManageMrPlan;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.TgtInsKbn;

/**
 * 管理の担当者別計画にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("manageMrPlanDao")
public class ManageMrPlanDaoImpl extends AbstractManageDao implements ManageMrPlanDao {

	private static final String SQL_MAP = "DPM_I_MR_PLAN_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// キー検索（施設積上げは含まない）
	public ManageMrPlan search(Long seqKey) throws DataNotFoundException {
		return (ManageMrPlan) super.selectBySeqKey(seqKey);
	}

	// ユニークキー検索（施設積上げは含まない）
	public ManageMrPlan searchUk(InsType insType, String prodCode, Integer jgiNo) throws DataNotFoundException {

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
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		ManageMrPlan record = new ManageMrPlan();
		record.setInsType(insType);
		record.setProdCode(prodCode);
		record.setJgiNo(jgiNo);
		return super.selectByUniqueKey(record);
	}

	// リスト検索/従業員一覧
	// mod Start 2022/10/05 H.Futagami 2022年4月組織変更対応  担当者をカテゴリのMRで絞る
	//public List<ManageMrPlan> searchListByProd(String prodCode,  String sosCd, BumonRank bumonRank, String jgiNo, String oyakoKb,
	//		String oyakoKb2,String oyakoKbProdCode) throws DataNotFoundException {
	public List<ManageMrPlan> searchListByProd(String prodCode,  String sosCd, BumonRank bumonRank, String jgiNo, String oyakoKb,
			String oyakoKb2,String oyakoKbProdCode, String category) throws DataNotFoundException {
	// mod End 2022/10/05 H.Futagami 2022年4月組織変更対応  担当者をカテゴリのMRで絞る

		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosCd == null) {
			final String errMsg = "組織コード(営業所 or チーム)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (bumonRank != BumonRank.TEAM  && bumonRank != BumonRank.OFFICE_TOKUYAKUTEN_G ) {
			final String errMsg = "部門ランクが不正";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("prodCode", prodCode);
		if(bumonRank == BumonRank.TEAM) {
			paramMap.put("sosCd4", sosCd);
		} else {
			paramMap.put("sosCd3", sosCd);
		}
		if (StringUtils.isNotBlank(jgiNo)) {
			paramMap.put("jgiNo", Integer.valueOf(jgiNo));
		}
		paramMap.put("oyakoKb", OyakoKbHelper.getOyakoKb(oyakoKb,oyakoKb2,oyakoKbProdCode));
		// 固定条件
		paramMap.put("jokenSetList", new JokenSet[]{ JokenSet.IYAKU_MR });
		paramMap.put("jgiKbList", new JgiKb[]{ JgiKb.JGI, JgiKb.CONTRACT_MR, JgiKb.EIGYOSHO_ZATU, JgiKb.DUMMY_MR});
        // add Start 2022/10/05 H.Futagami 2022年4月組織変更対応  担当者をカテゴリのMRで絞る
		paramMap.put("category", category);
		// add End 2022/10/05 H.Futagami 2022年4月組織変更対応  担当者をカテゴリのMRで絞る
		return super.selectList(getSqlMapName() + ".selectListByProd", paramMap);
	}

	// 雑担当リスト検索/従業員一覧
	@Deprecated
	public List<ManageMrPlan> searchZatuListByProd(String sortString, String prodCode, String sosCd3) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sortString", sortString);
		paramMap.put("prodCode", prodCode);
		paramMap.put("sosCd3", sosCd3);
		// 固定条件
		paramMap.put("jokenSetList", new JokenSet[]{ JokenSet.IYAKU_MR });
		paramMap.put("jgiKbList", new JgiKb[]{JgiKb.EIGYOSHO_ZATU});
		return super.selectList(getSqlMapName() + ".selectListByProd", paramMap);
	}

	// リスト検索/品目一覧
	public List<ManageMrPlan> searchListByJgi(Integer jgiNo, String category, String tgtInsKbn, String oyakoKb,
			String oyakoKb2,String oyakoKbProdCode, String prodCode) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		//カテゴリがNullの場合、ProdCodeで検索する
//		if (category == null) {
//			final String errMsg = "カテゴリがnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}

		if (tgtInsKbn == null) {
			final String errMsg = "対象施設区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("category", category);
		paramMap.put("prodCode", prodCode);
		boolean zatsuAriFlg = false;
		if(tgtInsKbn.equals(TgtInsKbn.ZATSU_ARI.getDbValue())) {
			zatsuAriFlg = true;
		}
		paramMap.put("zatsuAriFlg", zatsuAriFlg);
		paramMap.put("oyakoKb", OyakoKbHelper.getOyakoKb(oyakoKb,oyakoKb2,oyakoKbProdCode));


		return super.selectList(getSqlMapName() + ".selectListByJgi", paramMap);
	}

	// 登録
	public void insert(ManageMrPlan manageMrPlan, String pgId) throws DuplicateException {
		super.insert(manageMrPlan, pgId);
	}

	// 更新
	public int update(ManageMrPlan manageMrPlan, String pgId) {
		return super.update(manageMrPlan, pgId);
	}

}
