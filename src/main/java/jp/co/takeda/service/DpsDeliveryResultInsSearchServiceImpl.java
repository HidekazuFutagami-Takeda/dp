package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

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
import jp.co.takeda.dao.DeliveryResultDao;
import jp.co.takeda.dao.DpsPlannedCtgDao;
import jp.co.takeda.dao.InsMstDAO;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SpecialInsPlanDao;
import jp.co.takeda.dto.DeliveryResultInsListDto;
import jp.co.takeda.dto.DeliveryResultInsScDto;
import jp.co.takeda.logic.DeliveryResultInsResultDtoCreateLogic;
import jp.co.takeda.model.DeliveryResult;
import jp.co.takeda.model.DpsPlannedCtg;
import jp.co.takeda.model.JgiMst;

/**
 * 過去実績参照(施設特約店別計画モード)の検索を行うサービス実装クラス
 *
 * @author siwamoto
 */
@Transactional
@Service("dpsDeliveryResultInsSearchService")
public class DpsDeliveryResultInsSearchServiceImpl implements DpsDeliveryResultInsSearchService {

	/**
	 * 施設情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("insMstDAO")
	protected InsMstDAO insMstDAO;

	/**
	 * 特定施設個別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("specialInsPlanDao")
	protected SpecialInsPlanDao specialInsPlanDao;

	/**
	 * 納入実績DAO
	 */
	@Autowired(required = true)
	@Qualifier("deliveryResultDao")
	protected DeliveryResultDao deliveryResultDao;

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
	 * 計画対象カテゴリ領域取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsPlannedCtgDao")
	protected DpsPlannedCtgDao dpsPlannedCtgDao;

	public DeliveryResultInsListDto searchDeliveryResultInsList(DeliveryResultInsScDto scDto, String category) throws LogicalException {

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

		// -----------------------------
		// 計画対象カテゴリ領域取得
		// -----------------------------
		DpsPlannedCtg dpsPlannedCtg = new DpsPlannedCtg();
		dpsPlannedCtg = dpsPlannedCtgDao.search(category);

		// ----------------------
		// 納入実績取得
		// ----------------------
		List<DeliveryResult> deliveryResultList = new ArrayList<DeliveryResult>();
		try {
			deliveryResultList = deliveryResultDao.searchByProdOyako(DeliveryResultDao.SORT_STRING, prodCode, jgiNo, insNo, dpsPlannedCtg.getOyakoKb(),dpsPlannedCtg.getOyakoKb2());
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPS1004E", "品目")), e);
		}

		// ----------------------
		// 組織コード取得
		// ----------------------
		JgiMst jgiMst = null;
		String jgiName = "";
		String shokushuName = "";
		try {
			jgiMst = jgiMstDAO.search(jgiNo);
			jgiName = jgiMst.getJgiName();
			shokushuName = jgiMst.getShokushuName();
		} catch (DataNotFoundException e) {
			// エラーにはしない
		}
		DeliveryResultInsResultDtoCreateLogic logic = new DeliveryResultInsResultDtoCreateLogic(deliveryResultList, jgiName, shokushuName);
		return logic.createResultDtoList();
	}

	public DeliveryResultInsListDto searchDeliveryResultInsList2(DeliveryResultInsScDto scDto) throws LogicalException {

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
		String shokushuName = "";
		try {
			jgiMst = jgiMstDAO.search(jgiNo);
			jgiName = jgiMst.getJgiName();
			shokushuName = jgiMst.getShokushuName();
		} catch (DataNotFoundException e) {
			// エラーにはしない
		}
		DeliveryResultInsResultDtoCreateLogic logic = new DeliveryResultInsResultDtoCreateLogic(jgiName, shokushuName);
		return logic.createResultDtoList2();
	}
}
