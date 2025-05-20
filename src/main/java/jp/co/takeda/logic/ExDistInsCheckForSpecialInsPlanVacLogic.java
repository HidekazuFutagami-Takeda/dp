package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.DATA_NOT_FOUND_ERROR;

import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.ExceptDistInsDao;
import jp.co.takeda.dao.InsMstDAO;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.model.DpsInsMst;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SpecialInsPlanForVac;
import jp.co.takeda.model.div.PlanType;

/**
 * 特定施設個別計画登録時の配分除外チェックロジッククラス
 *
 * @author khashimoto
 */
public class ExDistInsCheckForSpecialInsPlanVacLogic {

	/**
	 * 施設情報DAO
	 */
	private final InsMstDAO insMstDAO;

	/**
	 * 計画対象品目DAO
	 */
	private final PlannedProdDAO plannedProdDAO;

	/**
	 * 配分除外施設DAO
	 */
	private final ExceptDistInsDao exceptDistInsDao;

	/**
	 * 特定施設個別計画のリスト
	 */
	private final List<SpecialInsPlanForVac> specialInsPlanList;

	/**
	 * コンストラクタ
	 *
	 * @param insMstDAO 施設情報DAO
	 * @param plannedProdDAO 計画対象品目DAO
	 * @param exceptDistInsDao 配分除外施設DAO
	 * @param inputList 特定施設個別計画のリスト
	 */
	public ExDistInsCheckForSpecialInsPlanVacLogic(InsMstDAO insMstDAO, PlannedProdDAO plannedProdDAO, ExceptDistInsDao exceptDistInsDao, List<SpecialInsPlanForVac> inputList) {
		this.insMstDAO = insMstDAO;
		this.plannedProdDAO = plannedProdDAO;
		this.exceptDistInsDao = exceptDistInsDao;
		this.specialInsPlanList = inputList;
	}

	/**
	 * 配分除外チェックを実行する。
	 *
	 * @throws LogicalException
	 */
	public void check(PlanType planType) throws LogicalException {
		String checkIns = "";
		String checkProdCode = "";
		List<String> insList = new ArrayList<String>();
		List<MessageKey> messageList = new ArrayList<MessageKey>();
		for (SpecialInsPlanForVac insPlan : specialInsPlanList) {
			// 登録対象外の場合、次へ
			if (insPlan.getPlannedValueB() == null) {
				continue;
			}

			// 配分除外施設の場合、次へ
			if (insList.contains(checkIns)) {
				continue;
			}

			// 施設・品目が同じ場合、次へ
			if (checkIns.equals(insPlan.getInsNo()) && checkProdCode.equals(insPlan.getProdCode())) {
				continue;
			}

			// 配分除外施設のチェック
			checkIns = insPlan.getInsNo();
			checkProdCode = insPlan.getProdCode();
			try {
				exceptDistInsDao.search(checkIns, null, planType);
				try {
					DpsInsMst insMst = insMstDAO.search(checkIns);
					messageList.add(new MessageKey("DPS1005E", insMst.getInsAbbrName()));
					insList.add(checkIns);
					continue;
				} catch (DataNotFoundException e) {
					final String errMsg = "特定施設個別計画登録対象の施設が存在しない";
					throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
				}
			} catch (DataNotFoundException e) {
			}

			// 配分除外施設品目のチェック
			try {
				exceptDistInsDao.search(checkIns, checkProdCode, planType);
				try {
					DpsInsMst insMst = insMstDAO.search(checkIns);
					PlannedProd plannedProd = plannedProdDAO.search(checkProdCode);
					messageList.add(new MessageKey("DPS1006E", insMst.getInsAbbrName(), plannedProd.getProdName()));
					insList.add(checkIns);
					continue;
				} catch (DataNotFoundException e) {
					final String errMsg = "特定施設個別計画登録対象の施設・品目が存在しない";
					throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
				}
			} catch (DataNotFoundException e) {
			}
		}

		if (messageList.size() != 0) {
			throw new LogicalException(new Conveyance(messageList));
		}
	}
}
