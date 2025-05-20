package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.DpsInsType;
import jp.co.takeda.model.div.HoInsType;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.view.InsWsPlanForVacForRef;

/**
 * 参照用の施設特約店別計画取得DAO実装クラス
 *
 * @author khashimoto
 */
@Repository("insWsPlanForVacForRefDao")
public class InsWsPlanForVacForRefDaoImpl extends AbstractDao implements InsWsPlanForVacForRefDao {

	private static final String SQL_MAP = "VIEW_InsWsPlanForVacForRef_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	/**
	 * 親子関連情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsSyComInsOyakoDao")
	protected DpsSyComInsOyakoDao dpsSyComInsOyakoDao;

	// (ワクチン)施設特約店別計画のリストを取得
	public List<InsWsPlanForVacForRef> searchList(String sortString, Integer jgiNo, String prodCode, ActivityType activityType, String resultRefProdCode1,
		String resultRefProdCode2, String resultRefProdCode3, InsType insType, String oyakoKb,String oyakoKb2 ) throws DataNotFoundException {

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
		paramMap.put("sortString", sortString);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("prodCode", prodCode);
		paramMap.put("activityType", activityType);
		List<HoInsType> hoInsTypeList = DpsInsType.convertDpsHoInsType(insType);
		paramMap.put("hoInsTypeList", hoInsTypeList);
		paramMap.put("resultRefProdCode1", resultRefProdCode1);
		paramMap.put("resultRefProdCode2", resultRefProdCode2);
		paramMap.put("resultRefProdCode3", resultRefProdCode3);
		paramMap.put("oyakoKb", OyakoKbHelper.getOyakoKb(oyakoKb,oyakoKb2,oyakoKbProdCode));

		List<InsWsPlanForVacForRef> insWsPlanForRefList = dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);

		// 特定施設・削除施設・配分除外施設の抽出(施設単位)
		Set<String> spInsNoList = new HashSet<String>();
		Set<String> delInsNoList = new HashSet<String>();
		Set<String> distInsNoList = new HashSet<String>();
		for (InsWsPlanForVacForRef insWsPlan : insWsPlanForRefList) {
			String insNo = insWsPlan.getInsNo();
			Boolean speInsFlg = insWsPlan.getSpecialInsPlanFlg();
			if (speInsFlg != null && speInsFlg) {
				spInsNoList.add(insNo);
			}
			Boolean delInsFlg = insWsPlan.getDelInsFlg();
			if (delInsFlg != null && delInsFlg) {
				delInsNoList.add(insNo);
			}
			Boolean distInsFlg = insWsPlan.getExceptDistInsFlg();
			if (distInsFlg != null && distInsFlg) {
				distInsNoList.add(insNo);
			}
		}

		// 施設のマーカーを元に、施設特約店単位にフラグを立てる。
		List<InsWsPlanForVacForRef> resultList = new ArrayList<InsWsPlanForVacForRef>();
		for (InsWsPlanForVacForRef insWsPlan : insWsPlanForRefList) {

			// --------------------------
			// フラグの設定
			// --------------------------
			String insNo = insWsPlan.getInsNo();
			if (spInsNoList.contains(insNo)) {
				insWsPlan.setSpecialInsPlanFlg(true);
			}
			if (delInsNoList.contains(insNo)) {
				insWsPlan.setDelInsFlg(true);
			}
			if (distInsNoList.contains(insNo)) {
				insWsPlan.setExceptDistInsFlg(true);
			}
			resultList.add(insWsPlan);
		}
		return resultList;
	}
}
