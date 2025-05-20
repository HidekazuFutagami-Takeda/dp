package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.DATA_NOT_FOUND_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.MrPlanForVacDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dto.MrPlanForVacResultDto;
import jp.co.takeda.dto.PlannedProdForVacResultDto;
import jp.co.takeda.logic.CreateMrPlanForVacResultDtoListLogic;
import jp.co.takeda.logic.CreatePlannedProdForVacResultDtoListLogic;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.Sales;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * (ワクチン)担当者別計画機能の検索に関するサービス実装クラス
 * 
 * @author khashimoto
 */
@Transactional
@Service("dpsMrPlanForVacSearchService")
public class DpsMrPlanForVacSearchServiceImpl implements DpsMrPlanForVacSearchService {

	/**
	 * 組織情報取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * 従業員情報取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;

	/**
	 * 計画対象品目取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * ワクチン用担当者別計画取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("mrPlanForVacDao")
	protected MrPlanForVacDao mrPlanForVacDao;

	// 全リスト取得
	public List<PlannedProdForVacResultDto> searchPlannedProdList() {

		// ------------------------------
		// 品目一覧取得
		// ------------------------------
		List<PlannedProd> plannedProdList;
		try {
			plannedProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.VACCHIN, null, null);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画品目情報が存在しない：";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// ------------------------------
		// DTO生成
		// ------------------------------
		CreatePlannedProdForVacResultDtoListLogic logic = new CreatePlannedProdForVacResultDtoListLogic(sosMstDAO, jgiMstDAO, mrPlanForVacDao, plannedProdList);
		return logic.execute();
	}

	// 品目単位取得
	public List<MrPlanForVacResultDto> searchMrPlan(String prodCode) throws LogicalException {

		// ------------------------------
		// 引数チェック
		// ------------------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ------------------------------
		// 品目取得
		// ------------------------------
		PlannedProd plannedProd;
		try {
			plannedProd = plannedProdDAO.search(prodCode);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画品目情報が存在しない：";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// ------------------------------
		// DTO生成
		// ------------------------------
		CreateMrPlanForVacResultDtoListLogic logic = new CreateMrPlanForVacResultDtoListLogic(sosMstDAO, jgiMstDAO, mrPlanForVacDao, plannedProd);
		return logic.execute();
	}
}
