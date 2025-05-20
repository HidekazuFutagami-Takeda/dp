package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DeliveryResultForVacDao;
import jp.co.takeda.dao.InsMstDAO;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dto.DeliveryResultVacInsListDto;
import jp.co.takeda.dto.DeliveryResultVacInsScDto;
import jp.co.takeda.logic.DeliveryResultVacInsResultDtoCreateLogic;
import jp.co.takeda.model.DeliveryResultForVac;
import jp.co.takeda.model.JgiMst;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 過去実績参照(施設特約店別計画モード)の検索を行うサービス実装クラス
 * 
 * @author siwamoto
 */
@Transactional
@Service("dpsDeliveryResultVacInsSearchService")
public class DpsDeliveryResultVacInsSearchServiceImpl implements DpsDeliveryResultVacInsSearchService {

	/**
	 * 施設情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("insMstDAO")
	protected InsMstDAO insMstDAO;

	/**
	 * 納入実績DAO
	 */
	@Autowired(required = true)
	@Qualifier("deliveryResultForVacDao")
	protected DeliveryResultForVacDao deliveryResultForVacDao;

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

	public DeliveryResultVacInsListDto searchDeliveryResultVacInsList(DeliveryResultVacInsScDto scDto) throws LogicalException {

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
		final Integer jgiNo = scDto.getJgiNo();
		if (jgiNo == null) {
			final String errMsg = "従業員コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final String insNo = scDto.getInsNo();

		// ----------------------
		// 納入実績取得
		// ----------------------
		List<DeliveryResultForVac> deliveryResultList = new ArrayList<DeliveryResultForVac>();
		try {
			deliveryResultList = deliveryResultForVacDao.searchByProd(DeliveryResultForVacDao.SORT_STRING, prodCode, jgiNo, insNo);
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPS1004E", "品目")), e);
		}

		// ----------------------
		// 組織コード取得
		// ----------------------
		JgiMst jgiMst = null;
		String jgiName = "";
		try {
			jgiMst = jgiMstDAO.search(jgiNo);
			jgiName = jgiMst.getJgiName();
		} catch (DataNotFoundException e) {
			// エラーにはしない
		}
		DeliveryResultVacInsResultDtoCreateLogic logic = new DeliveryResultVacInsResultDtoCreateLogic(deliveryResultList, jgiName);
		return logic.createResultDtoList();
	}

	public DeliveryResultVacInsListDto searchDeliveryResultVacInsList2(DeliveryResultVacInsScDto scDto) throws LogicalException {

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
		final Integer jgiNo = scDto.getJgiNo();
		if (jgiNo == null) {
			final String errMsg = "従業員コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 品目コード取得
		// ----------------------
		try {
			// 存在するかのチェックなので戻り値は受け取らない
			plannedProdDAO.search(prodCode);
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPS1003E", "品目")), e);
		}

		// ----------------------
		// 組織コード取得
		// ----------------------
		JgiMst jgiMst = null;
		String jgiName = "";
		try {
			jgiMst = jgiMstDAO.search(jgiNo);
			jgiName = jgiMst.getJgiName();
		} catch (DataNotFoundException e) {
			// エラーにはしない
		}
		DeliveryResultVacInsResultDtoCreateLogic logic = new DeliveryResultVacInsResultDtoCreateLogic(jgiName);
		return logic.createResultDtoList2();
	}
}
