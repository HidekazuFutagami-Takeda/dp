package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.DpsPlannedCtg;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.RefPeriod;
import jp.co.takeda.model.view.DistributionElement;

/**
 * 施設特約店別計画配分用の要素を取得するDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("distributionElementDao")
public class DistributionElementDaoImpl extends AbstractDao implements DistributionElementDao {

	private static final String SQL_MAP = "VIEW_DistributionElement_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	/**
	 * 計画対象カテゴリ領域DAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsPlannedCtgDao")
	protected DpsPlannedCtgDao dpsPlannedCtgDao;

	/**
	 * 親子関連情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsSyComInsOyakoDao")
	protected DpsSyComInsOyakoDao dpsSyComInsOyakoDao;

	// 組織コード(営業所)、品目固定コードから、施設特約店別計画配分用の要素を取得
	public List<DistributionElement> searchList(String sosCd3, Integer jgiNo, String prodCode, InsType insType, String refProdCode, RefPeriod refFrom, RefPeriod refTo, boolean isVaccine, String category)
		throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null && jgiNo == null) {
			final String errMsg = "組織・従業員がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insType == null) {
			final String errMsg = "施設出力対象区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (refProdCode == null) {
			final String errMsg = "配分品目コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (refFrom == null) {
			final String errMsg = "参照期間(FROM)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (refTo == null) {
			final String errMsg = "参照期間(TO)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -----------------------------
		// 計画対象カテゴリ領域取得
		// -----------------------------
		DpsPlannedCtg plannedCtg = new DpsPlannedCtg();
		plannedCtg = dpsPlannedCtgDao.search(category);

		// ---------------------------------
		// /親子関連情報のカウント取得
		// ---------------------------------
		int oyakoCount = 0;
		String oyakoKbProdCode = prodCode;
		if (StringUtils.isNotEmpty(oyakoKbProdCode)) {
			oyakoCount = dpsSyComInsOyakoDao.searchCount(oyakoKbProdCode);
			if(oyakoCount == 0) {
				//親子関連情報が存在しない
				oyakoKbProdCode = null;
			}
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("prodCode", prodCode);
		List<InsType> insTypeList = new ArrayList<InsType>();
		if (insType == InsType.P && isVaccine) {
			// P雑
			insTypeList.add(InsType.P);
			insTypeList.add(InsType.ZATU);
		} else {
			insTypeList.add(insType);
		}
		paramMap.put("insType", insTypeList);
		paramMap.put("refProdCode", refProdCode);
		paramMap.put("refFrom", refFrom);
		paramMap.put("refTo", refTo);
		// 医薬の場合のみ
		if (!isVaccine) {
			// 固定条件
			List<JgiKb> jgiKbList = new ArrayList<JgiKb>();
			jgiKbList.add(JgiKb.JGI);
			jgiKbList.add(JgiKb.CONTRACT_MR);
			jgiKbList.add(JgiKb.DUMMY_MR);
			paramMap.put("jgiKbList", jgiKbList);
			paramMap.put("jokenSetList", new JokenSet[]{ JokenSet.IYAKU_MR });
		}
		paramMap.put("oyakoKb", OyakoKbHelper.getOyakoKb(plannedCtg.getOyakoKb(),plannedCtg.getOyakoKb2(),oyakoKbProdCode));

		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}

	// 従業員番号、品目固定コードから、施設特約店別計画配分用の要素(ワクチン)を取得
	public List<DistributionElement> searchListForVac(Integer jgiNo, String prodCode, String refProdCode, RefPeriod refFrom, RefPeriod refTo) throws DataNotFoundException {

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
		if (refProdCode == null) {
			final String errMsg = "配分品目コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (refFrom == null) {
			final String errMsg = "参照期間(FROM)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (refTo == null) {
			final String errMsg = "参照期間(TO)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(5);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("prodCode", prodCode);
		paramMap.put("refProdCode", refProdCode);
		paramMap.put("refFrom", refFrom);
		paramMap.put("refTo", refTo);
		return dataAccess.queryForList(getSqlMapName() + ".selectListForVac", paramMap);
	}
}
