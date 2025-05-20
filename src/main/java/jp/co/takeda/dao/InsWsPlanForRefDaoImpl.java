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
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.HoInsType;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.ProdInsInfoScanDispKbn;
import jp.co.takeda.model.view.InsWsPlanForRef;

/**
 * 参照用の施設特約店別計画取得DAO実装クラス
 *
 * @author khashimoto
 */
@Repository("insWsPlanForRefDao")
public class InsWsPlanForRefDaoImpl extends AbstractDao implements InsWsPlanForRefDao {

	private static final String SQL_MAP = "VIEW_InsWsPlanForRef_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	/**
	 * 汎用マスタDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterDao")
	protected DpsCodeMasterDao dpsCodeMasterDao;

	/**
	 * 親子関連情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsSyComInsOyakoDao")
	protected DpsSyComInsOyakoDao dpsSyComInsOyakoDao;

	// 施設特約店別計画のリストを取得
	public List<InsWsPlanForRef> searchList(String sortString, Integer jgiNo, PlannedProd plannedProd, InsType insType, String resultRefProdCode1, String resultRefProdCode2,
		String resultRefProdCode3, String oyakoKb,String oyakoKb2, String oyakoKbProdCode ) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProd == null) {
			final String errMsg = "計画品目がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProd.getProdCode() == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("sortString", sortString);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("prodCode", plannedProd.getProdCode());
		List<HoInsType> hoInsTypeList = InsType.convertHoInsType(insType);
		paramMap.put("hoInsTypeList", hoInsTypeList);
		paramMap.put("resultRefProdCode1", resultRefProdCode1);
		paramMap.put("resultRefProdCode2", resultRefProdCode2);
		paramMap.put("resultRefProdCode3", resultRefProdCode3);
		paramMap.put("oyakoKb", OyakoKbHelper.getOyakoKb(oyakoKb,oyakoKb2,oyakoKbProdCode));

		// ----------------------
		// 計画、実績取得
		// ----------------------
		List<InsWsPlanForRef> insWsPlanForRefList;
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBUを追加する意図で、仕入品以外という条件に変更
//		if(plannedProd.getCategory() == ProdCategory.MMP || plannedProd.getCategory() == ProdCategory.ONC) {
		if(plannedProd.getCategory() != null && !dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.SII.getDbValue()).get(0).getDataCd().equals(plannedProd.getCategory())) {
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　SPBUを追加する意図で、仕入品以外という条件に変更
			// 仕入品以外（重点品 or 非重点品）
			if(plannedProd.getPlanLevelInsDoc()){
				insWsPlanForRefList = dataAccess.queryForList(getSqlMapName() + ".selectListPriorityDeli", paramMap);
			} else {
				insWsPlanForRefList = dataAccess.queryForList(getSqlMapName() + ".selectListDeli", paramMap);
			}
		} else {
			// 仕入品
			insWsPlanForRefList = dataAccess.queryForList(getSqlMapName() + ".selectListDeli", paramMap);
		}

		// 特定施設・削除施設・配分除外施設の抽出(施設単位)
		Set<String> spInsNoList = new HashSet<String>();
		Set<String> delInsNoList = new HashSet<String>();
		Set<String> distInsNoList = new HashSet<String>();
		for (InsWsPlanForRef insWsPlan : insWsPlanForRefList) {
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
		// また特定施設行を先頭へ並び替える。
		List<InsWsPlanForRef> resultList = new ArrayList<InsWsPlanForRef>();
		List<InsWsPlanForRef> addList = new ArrayList<InsWsPlanForRef>();
		List<InsWsPlanForRef> tmpList = new ArrayList<InsWsPlanForRef>();
		String oldInsNo = null;
		boolean isNotDisp = true;
		for (InsWsPlanForRef insWsPlan : insWsPlanForRefList) {
			String insNo = insWsPlan.getInsNo();

			if (oldInsNo != null && oldInsNo.equals(insNo) == false) {
				// ----------------------------------------
				// 計画立案対象外の施設は一覧から除外する
				// ----------------------------------------
				if (isNotDisp == false) {
					// --------------------------
					// 特定施設とその他施設
					// --------------------------
					if (spInsNoList.contains(oldInsNo)) {
						resultList.addAll(tmpList);
					} else {
						addList.addAll(tmpList);
					}
				}
				isNotDisp = true;
				tmpList = new ArrayList<InsWsPlanForRef>();
			}

			// 計画立案対象外かどうかの判定
			ProdInsInfoScanDispKbn scanDispKbn = insWsPlan.getProdIns().getProdInsInfoScanDispKbn();
			if (ProdInsInfoScanDispKbn.NOT != scanDispKbn || insWsPlan.getDistValueY() != null || insWsPlan.getPlannedValueY() != null) {
				// 表示対象、または計画値・配分値がある場合は、表示対象
				isNotDisp = false;
			}

			// --------------------------
			// フラグの設定
			// --------------------------
			if (spInsNoList.contains(insNo)) {
				insWsPlan.setSpecialInsPlanFlg(true);
			}
			if (delInsNoList.contains(insNo)) {
				insWsPlan.setDelInsFlg(true);
			}
			if (distInsNoList.contains(insNo)) {
				insWsPlan.setExceptDistInsFlg(true);
			}

			tmpList.add(insWsPlan);
			oldInsNo = insNo;
		}
		// ----------------------------------------
		// 計画立案対象外の施設は一覧から除外する
		// ----------------------------------------
		if (isNotDisp == false) {
			// --------------------------
			// 特定施設とその他施設
			// --------------------------
			if (spInsNoList.contains(oldInsNo)) {
				resultList.addAll(tmpList);
			} else {
				addList.addAll(tmpList);
			}
		}

		// 特定施設のリストの末尾に、その他施設を追加
		resultList.addAll(addList);
		return resultList;
	}

	// 施設特約店別計画のリストを取得
	public List<InsWsPlanForRef> searchListBySosCd(String sortString, String sosCd3, String prodCode) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コードがnull";
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
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("prodCode", prodCode);
		paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);

		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}

	// 施設特約店別計画のリストを取得
	public List<InsWsPlanForRef> searchListBySosCdOyako(String sortString, String sosCd3, String prodCode, String oyakoKb,String oyakoKb2 ) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コードがnull";
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
		Map<String, Object> paramMap = new HashMap<String, Object>(5);
		paramMap.put("sortString", sortString);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("prodCode", prodCode);
		paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);
		paramMap.put("oyakoKb", OyakoKbHelper.getOyakoKb(oyakoKb,oyakoKb2,oyakoKbProdCode));

		return dataAccess.queryForList(getSqlMapName() + ".selectListOyako", paramMap);
	}
}
