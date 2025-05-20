package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DeliveryResultMrForVacDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.DeliveryResultVacMrListDto;
import jp.co.takeda.dto.DeliveryResultVacMrScDto;
import jp.co.takeda.logic.DeliveryResultVacMrResultDtoCreateLogic;
import jp.co.takeda.model.DeliveryResultMrForVac;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 営業所計画の検索に関するサービス実装クラス
 * 
 * @author siwamoto
 */
@Transactional
@Service("dpsDeliveryResultVacMrSearchService")
public class DpsDeliveryResultVacMrSearchServiceImpl implements DpsDeliveryResultVacMrSearchService {

	/**
	 * 納入実績DAO
	 */
	@Autowired(required = true)
	@Qualifier("deliveryResultMrForVacDao")
	protected DeliveryResultMrForVacDao deliveryResultMrForVacDao;

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
	public DeliveryResultVacMrListDto searchDeliveryResultVacMrList(DeliveryResultVacMrScDto scDto) throws LogicalException {
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
		final String sosCd2 = scDto.getSosCd2();
		final String sosCd3 = scDto.getSosCd3();

		// ----------------------
		// 実績取得
		// ----------------------
		List<DeliveryResultMrForVac> searchDeliveryResultMrList = null;
		try {
			searchDeliveryResultMrList = deliveryResultMrForVacDao.searchByProd(DeliveryResultMrForVacDao.SORT_STRING, prodCode, sosCd3);
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPS1004E", "品目")), e);
		}
		// ----------------------
		// 組織取得
		// ----------------------
		List<SosMst> searchSosMstList = null;
		if (sosCd3 == null) {
			try {
				searchSosMstList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.TOKUYAKUTEN_LIST, BumonRank.SITEN_TOKUYAKUTEN_BU, sosCd2);
			} catch (DataNotFoundException e) {
				throw new LogicalException(new Conveyance(new MessageKey("DPS1003E", "組織")), e);
			}
		} else {
			try {
				SosMst sosMst = sosMstDAO.search(sosCd3);
				searchSosMstList = new ArrayList<SosMst>();
				searchSosMstList.add(sosMst);
			} catch (DataNotFoundException e) {
				throw new LogicalException(new Conveyance(new MessageKey("DPS1003E", "組織")), e);
			}
		}
		// ----------------------
		// 従業員取得
		// ----------------------
		List<JgiMst> searchJgiMstList = null;
		if (sosCd3 == null) {
			try {
				searchJgiMstList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd2, SosListType.TOKUYAKUTEN_LIST, BumonRank.SITEN_TOKUYAKUTEN_BU);
			} catch (DataNotFoundException e) {
				throw new LogicalException(new Conveyance(new MessageKey("DPS1003E", "従業員")), e);
			}
		} else {
			try {
				searchJgiMstList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd3, SosListType.TOKUYAKUTEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G);
			} catch (DataNotFoundException e) {
				throw new LogicalException(new Conveyance(new MessageKey("DPS1003E", "従業員")), e);
			}
		}

		DeliveryResultVacMrResultDtoCreateLogic logic = new DeliveryResultVacMrResultDtoCreateLogic(searchDeliveryResultMrList, searchSosMstList, searchJgiMstList);
		return logic.createResultDtoList();
	}
}
