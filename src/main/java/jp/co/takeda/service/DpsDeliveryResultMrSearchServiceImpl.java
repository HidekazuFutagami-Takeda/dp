package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DeliveryResultMrDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.DeliveryResultMrListDto;
import jp.co.takeda.dto.DeliveryResultMrScDto;
import jp.co.takeda.logic.DeliveryResultMrResultDtoCreateLogic;
import jp.co.takeda.model.DeliveryResultMr;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;

/**
 * 営業所計画の検索に関するサービス実装クラス
 *
 * @author siwamoto
 */
@Transactional
@Service("dpsDeliveryResultMrSearchService")
public class DpsDeliveryResultMrSearchServiceImpl implements DpsDeliveryResultMrSearchService {

	/**
	 * 納入実績DAO
	 */
	@Autowired(required = true)
	@Qualifier("deliveryResultMrDao")
	protected DeliveryResultMrDao deliveryResultMrDao;

	/**
	 * 組織DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * 従業員DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * 過去実績参照（担当者別）の検索
	 *
	 * @param scDto
	 * @return
	 * @throws LogicalException
	 * @see jp.co.takeda.service.DpsDeliveryResultMrSearchService#searchDeliveryResultMrList(jp.co.takeda.dto.DeliveryResultMrScDto)
	 */
	public DeliveryResultMrListDto searchDeliveryResultMrList(DeliveryResultMrScDto scDto) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "検索条件オブジェクトがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final String prodCode = scDto.getProdCode();
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final String sosCd3 = scDto.getSosCd3();
		final String sosCd4 = scDto.getSosCd4();
		final String category = scDto.getCategory();
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 実績取得
		// ----------------------
		List<DeliveryResultMr> searchDeliveryResultMrList = null;
		try {
			searchDeliveryResultMrList = deliveryResultMrDao.searchByProd(DeliveryResultMrDao.SORT_STRING, prodCode, sosCd3, sosCd4, null, category);
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPS1004E", "品目")), e);
		}
		// ----------------------
		// 組織取得（営業所）
		// ----------------------
		SosMst officeSosMst = null;
		if(sosCd3 != null){
			try {
				officeSosMst = sosMstDAO.search(sosCd3);;
			} catch (DataNotFoundException e) {
				officeSosMst = null;
			}
		}
		// ----------------------
		// 組織取得（チーム）
		// ----------------------
		List<SosMst> searchSosMstList = null;
		if (sosCd4 == null) {
			try {
				searchSosMstList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G, sosCd3);
			} catch (DataNotFoundException e) {
				searchSosMstList = null;
			}
		} else {
			try {
				SosMst sosMst = sosMstDAO.search(sosCd4);
				searchSosMstList = new ArrayList<SosMst>();
				searchSosMstList.add(sosMst);
			} catch (DataNotFoundException e) {
				searchSosMstList = null;
			}
		}
		// ----------------------
		// 従業員取得
		// ----------------------
		List<JgiMst> searchJgiMstList = null;
		if (sosCd4 == null) {
			try {
				searchJgiMstList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd3, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G);
			} catch (DataNotFoundException e) {
				throw new LogicalException(new Conveyance(new MessageKey("DPS1003E", "従業員")), e);
			}
		} else {
			try {
				searchJgiMstList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd4, SosListType.SHITEN_LIST, BumonRank.TEAM);
			} catch (DataNotFoundException e) {
				throw new LogicalException(new Conveyance(new MessageKey("DPS1003E", "従業員")), e);
			}
		}

		DeliveryResultMrResultDtoCreateLogic logic = new DeliveryResultMrResultDtoCreateLogic(officeSosMst, searchDeliveryResultMrList, searchSosMstList, searchJgiMstList);
		return logic.createResultDtoList();
	}
}
