package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.DATA_NOT_FOUND_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DistParamHonbuDao;
import jp.co.takeda.dao.DistParamOfficeDao;
import jp.co.takeda.dao.DistributionProdDao;
import jp.co.takeda.dao.DocDistParamHonbuDao;
import jp.co.takeda.dao.DocDistParamOfficeDao;
import jp.co.takeda.dao.InsDocPlanStatusDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.SysManageDao;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.DocDistributionJgiDto;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.ProdCategory;
import jp.co.takeda.model.div.RefPeriod;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.model.div.Term;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 配分機能の検索に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpsDocDistributionProdSearchService")
public class DpsDocDistributionProdSearchServiceImpl implements DpsDocDistributionProdSearchService {

	/**
	 * 納入計画管理DAO
	 */
	@Autowired(required = true)
	@Qualifier("sysManageDao")
	protected SysManageDao sysManageDao;

	/**
	 * 従業員情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;

	/**
	 * 配分対象品目一覧DAO
	 */
	@Autowired(required = true)
	@Qualifier("distributionProdDao")
	protected DistributionProdDao distributionProdDao;

	/**
	 * 配分基準（本部）DAO
	 */
	@Autowired(required = true)
	@Qualifier("docDistParamHonbuDao")
	protected DocDistParamHonbuDao docDistParamHonbuDao;

	/**
	 * 配分基準（営業所）DAO
	 */
	@Autowired(required = true)
	@Qualifier("docDistParamOfficeDao")
	protected DocDistParamOfficeDao docDistParamOfficeDao;

	/**
	 * 施設医師別計画ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("insDocPlanStatusDao")
	protected InsDocPlanStatusDao insDocPlanStatusDao;

	/**
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;
	
	// 配分対象品目一覧取得
	public Map<String, Object> searchDistributionProdList(String sosCd) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 営業所コードから組織情報取得
		SosMst sosMst;
		try {
			sosMst = sosMstDAO.search(sosCd);
		} catch (DataNotFoundException e) {
			final String errMsg = "対象組織がない：" + sosCd;
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		// 組織からカテゴリ判定（MMP or ONC）	
		ProdCategory category = ProdCategory.getSosCategory(sosMst.getOncSosFlg());	

		// ----------------------
		// 当期年度、期を取得
		// ----------------------
		SysManage sysManage;
		try {
			sysManage = sysManageDao.search(SysClass.DPS, SysType.IYAKU);
		} catch (DataNotFoundException e) {
			final String errMsg = "納入計画管理情報が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		String sysYear = sysManage.getSysYear();
		Term sysTerm = sysManage.getSysTerm();

		// ----------------------
		// 配分対象品目一覧取得
		// ----------------------
		List<Map<String,Object>> distributionProdList = distributionProdDao.searchInsDocProdList(Sales.IYAKU, category, sosCd);

		// ----------------------
		// 配分基準取得
		// ----------------------
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		for (Map<String,Object> distProd : distributionProdList) {

			Map<String,Object> _paramUH = null;
			Map<String,Object> _paramP = null;
			boolean _isHonbu;

			String prodCode = (String)distProd.get("prodCode");
			try {
				// 配分基準（営業所案）取得
				List<Map<String,Object>> distParamOfficeList = docDistParamOfficeDao.searchInsDocParamList(DistParamOfficeDao.SORT_STRING, sosCd, prodCode, null, category);

				_isHonbu = false;
				for (Map<String,Object> distParamOffice : distParamOfficeList) {
					switch ((InsType)distParamOffice.get("insType")) {
						case UH:
							_paramUH = distParamOffice;
							break;
						case P:
							_paramP = distParamOffice;
							break;
						default:
							break;
					}
				}

				if (_paramUH == null) {
					final String errMsg = "営業所案から、医師別計画配分基準UHの取得に失敗:";
					throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode));
				}
				if (_paramP == null) {
					final String errMsg = "営業所案から、医師別計画配分基準Pの取得に失敗:";
					throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode));
				}

			} catch (DataNotFoundException e) {

				// 営業所案が存在しない場合は、配分基準（本部案）を取得
				try {

					List<Map<String,Object>> distParamHonbuList = docDistParamHonbuDao.searchInsDocParamList(DistParamHonbuDao.SORT_STRING, prodCode, null, category);

					_isHonbu = true;
					for (Map<String,Object> distParamHonbu : distParamHonbuList) {
						switch ((InsType)distParamHonbu.get("insType")) {
							case UH:
								_paramUH = distParamHonbu;
								break;
							case P:
								_paramP = distParamHonbu;
								break;
							default:
								break;
						}
					}

					if (_paramUH == null) {
						final String errMsg = "本部案から、医師別計画配分基準UHの取得に失敗:";
						throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode));
					}
					if (_paramP == null) {
						final String errMsg = "本部案から、医師別計画配分基準Pの取得に失敗:";
						throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode));
					}

				} catch (DataNotFoundException e1) {
					final String errMsg = "配分基準(本部案、営業所案)が存在しない：";
					throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e1);
				}
			}

			Map<String, Object> resultData = new HashMap<String, Object>();
			resultData.put("prodCode", distProd.get("prodCode"));
			resultData.put("prodName", distProd.get("prodName"));
			resultData.put("prodType", distProd.get("prodType"));
			resultData.put("insDocPlanStatSum", distProd.get("insDocPlanStatSum"));
			resultData.put("honbu",_isHonbu);
			resultData.put("upDate", _paramUH.get("upDate"));
			// UH
			resultData.put("lossRateUH", _paramUH.get("lossRate"));
			resultData.put("indexMikakutokuUH", _paramUH.get("indexMikakutoku"));
			resultData.put("indexDeliveryUH", _paramUH.get("indexDelivery"));
			resultData.put("refPeriodFromUH", RefPeriod.convertRefPeriod((RefPeriod)_paramUH.get("refFrom"), sysYear, sysTerm));
			resultData.put("refPeriodToUH", RefPeriod.convertRefPeriod((RefPeriod)_paramUH.get("refTo"), sysYear, sysTerm));
			// P
			resultData.put("lossRateP", _paramP.get("lossRate"));
			resultData.put("indexMikakutokuP", _paramP.get("indexMikakutoku"));
			resultData.put("indexDeliveryP", _paramP.get("indexDelivery"));
			resultData.put("refPeriodFromP", RefPeriod.convertRefPeriod((RefPeriod)_paramP.get("refFrom"), sysYear, sysTerm));
			resultData.put("refPeriodToP", RefPeriod.convertRefPeriod((RefPeriod)_paramP.get("refTo"), sysYear, sysTerm));
			
			resultList.add(resultData);
		}

		// 重点品目全体の最終更新日を取得
		Date lastUpdate = insDocPlanStatusDao.getLastUpDate(sosCd, null, null, null);
		
		// 結果作成
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("statusLastUpdate", lastUpdate);
		result.put("detailList", resultList);
		
		return result;
	}
	
	// 配分対象の従業員情報を作成する
	public List<DocDistributionJgiDto> createDocDistributionJgiList(String sosCd, Integer jgiNo, boolean doMrOpen, boolean doPlanClear ) {
		
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		try {

			// 従業員情報取得
			List<JgiMst> jgiList ;
			if(jgiNo != null){
				jgiList = new ArrayList<JgiMst>();
				jgiList.add(jgiMstDAO.search(jgiNo));
			} else {
				jgiList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G);
			}
			
			// ステータス最終更新日取得
			Date statusLastUpdates = insDocPlanStatusDao.getLastUpDate(sosCd, null, jgiNo, null);
			
			// データ作成
			List<DocDistributionJgiDto> resultList = new ArrayList<DocDistributionJgiDto>();
			for (JgiMst jgiMst : jgiList) {
				resultList.add(new DocDistributionJgiDto(jgiMst, doMrOpen, doPlanClear, statusLastUpdates));
			}
			
			return resultList;
			
		} catch (DataNotFoundException e) {
			final String errMsg = "配分対象の従業員が見つからない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		
	}

}
