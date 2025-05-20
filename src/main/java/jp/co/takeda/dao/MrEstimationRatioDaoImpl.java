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
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.RefPeriod;
import jp.co.takeda.model.view.MrEstimationRatio;

/**
 * 担当者別試算構成比取得DAO実装クラス
 *
 * @author khashimoto
 */
@Repository("mrEstimationRatioDao")
public class MrEstimationRatioDaoImpl extends AbstractDao implements MrEstimationRatioDao {

	private static final String SQL_MAP = "VIEW_MrEstimationRatio_SqlMap";

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

	// 組織コード(営業所)、品目固定コードから、担当者毎の試算構成比一覧を取得
	public List<MrEstimationRatio> searchList(String sosCd3, String prodCode, String refProdCode, RefPeriod refFrom, RefPeriod refTo, String category) throws DataNotFoundException {
		return execute(sosCd3, null, prodCode, refProdCode, refFrom, refTo, category);
	}

	// 組織コード(チーム)、品目固定コードから、担当者毎の試算構成比一覧を取得
	public List<MrEstimationRatio> searchListByTeam(String sosCd4, String prodCode, String refProdCode, RefPeriod refFrom, RefPeriod refTo, String category) throws DataNotFoundException {
		return execute(null, sosCd4, prodCode, refProdCode, refFrom, refTo, category);
	}

	/**
	 * 組織コード(営業所/チーム)、品目固定コードから、担当者毎の試算構成比一覧を取得する。
	 *
	 * @param sosCd3 営業所コード
	 * @param sosCd4 チームコード
	 * @param prodCode 品目固定コード
	 * @param refProdCode 参照品目の品目固定コード
	 * @param refFrom 参照期間From
	 * @param refTo 参照期間To
	 * @return 担当者別試算構成比のリスト
	 * @throws DataNotFoundException
	 */
	private List<MrEstimationRatio> execute(String sosCd3, String sosCd4, String prodCode, String refProdCode, RefPeriod refFrom, RefPeriod refTo, String category) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null && sosCd4 == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (refProdCode == null) {
			final String errMsg = "試算品目コードがnull";
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
		if (category == null) {
			final String errMsg = "カテゴリがnull";
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
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("sosCd4", sosCd4);
		paramMap.put("prodCode", prodCode);
		paramMap.put("refProdCode", refProdCode);
		paramMap.put("refFrom", refFrom);
		paramMap.put("refTo", refTo);
		// 固定条件
		paramMap.put("jokenSetList", new JokenSet[]{ JokenSet.IYAKU_MR });
		List<JgiKb> jgiKbList = new ArrayList<JgiKb>();
		jgiKbList.add(JgiKb.JGI);
		jgiKbList.add(JgiKb.CONTRACT_MR);
		jgiKbList.add(JgiKb.DUMMY_MR);
		paramMap.put("jgiKbList", jgiKbList);
		paramMap.put("oyakoKb", OyakoKbHelper.getOyakoKb(plannedCtg.getOyakoKb(),plannedCtg.getOyakoKb2(),oyakoKbProdCode));

		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}
}
