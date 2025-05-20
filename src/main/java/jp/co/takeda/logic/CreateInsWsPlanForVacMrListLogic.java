/**
 *
 */
package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DeliveryResultMrForVacDao;
import jp.co.takeda.dao.InsWsPlanForVacDao;
import jp.co.takeda.dao.MrPlanForVacDao;
import jp.co.takeda.dto.InsWsPlanForVacMrResultDto;
import jp.co.takeda.dto.InsWsPlanForVacPlannedSummaryDto;
import jp.co.takeda.dto.InsWsPlanForVacProdSummaryDto;
import jp.co.takeda.dto.MrPlanResultValueDto;
import jp.co.takeda.model.DeliveryResultMrForVac;
import jp.co.takeda.model.InsWsPlanStatusForVac;
import jp.co.takeda.model.MonNnu;
import jp.co.takeda.model.MrPlanForVac;

/**
 * ワクチン用施設特約店別計画 担当者一覧サマリ情報を作成するロジッククラス
 * 
 * @author nozaki
 */
public class CreateInsWsPlanForVacMrListLogic {

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 担当者別納入実績DAO
	 */
	private final DeliveryResultMrForVacDao deliveryResultMrDao;

	/**
	 * 担当者別計画DAO
	 */
	private final MrPlanForVacDao mrPlanDao;

	/**
	 * ワクチン用施設特約店別計画DAO
	 */
	private final InsWsPlanForVacDao insWsPlanDao;

	/**
	 * コンストラクタ
	 * 
	 * @param prodCode 品目固定コード
	 * @param deliveryResultMrDao ワクチン用担当者別納入実績DAO
	 * @param mrPlanDao ワクチン用担当者別計画DAO
	 * @param insWsPlanDao ワクチン用施設特約店別計画DAO
	 */
	public CreateInsWsPlanForVacMrListLogic(String prodCode, DeliveryResultMrForVacDao deliveryResultMrDao, MrPlanForVacDao mrPlanDao, InsWsPlanForVacDao insWsPlanDao) {
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (deliveryResultMrDao == null) {
			final String errMsg = "ワクチン用担当者別納入実績DAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (mrPlanDao == null) {
			final String errMsg = "ワクチン用担当者別計画DAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insWsPlanDao == null) {
			final String errMsg = "ワクチン用施設特約店別計画DAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		this.prodCode = prodCode;
		this.deliveryResultMrDao = deliveryResultMrDao;
		this.mrPlanDao = mrPlanDao;
		this.insWsPlanDao = insWsPlanDao;
	}

	/**
	 * 品目単位のワクチン用施設特約店別計画ステータス情報を元に、 納入実績・施設特約店別計画のサマリ情報を取得する。
	 * 
	 * @param dto 品目単位のワクチン用施設特約店別計画ステータス情報
	 * @return 品目一覧画面の検索結果用DTO
	 */
	public InsWsPlanForVacMrResultDto execute(InsWsPlanStatusForVac insWsPlanStatus) {

		if (insWsPlanStatus == null) {
			final String errMsg = "ワクチン用施設特約店別計画立案ステータスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		Integer jgiNo = insWsPlanStatus.getJgiNo();

		// 納入実績サマリ取得
		MonNnu monNnu = createResultSummary(jgiNo);
		MrPlanResultValueDto result = new MrPlanResultValueDto(monNnu);

		// 担当者別計画取得
		MrPlanForVac mrPlan = createMrPlan(jgiNo);

		// 施設特約店別計画サマリ取得
		InsWsPlanForVacProdSummaryDto insWsPlanSummary = createInsWsPlanSummary(jgiNo);

		// 計画サマリDTO作成
		InsWsPlanForVacPlannedSummaryDto plannedValueSummary = new InsWsPlanForVacPlannedSummaryDto(mrPlan, insWsPlanSummary);

		return new InsWsPlanForVacMrResultDto(insWsPlanStatus, result, plannedValueSummary);
	}

	/**
	 * 品目固定コード、施設出力対象区分を指定して、納入実績を取得する。
	 * 
	 * @param jgiNo 従業員番号
	 * @param insType 施設出力対象区分
	 * @return 納入実績
	 */
	private MonNnu createResultSummary(Integer jgiNo) {

		MonNnu monNnu = null;
		try {
			// 担当者納入実績取得
			DeliveryResultMrForVac mrResult = deliveryResultMrDao.search(jgiNo, prodCode);
			monNnu = mrResult.getMonNnu();

		} catch (DataNotFoundException e) {
			// エラーにしない（実績ゼロとする）
		}

		return monNnu;
	}

	/**
	 * 品目固定コードを指定して、担当者別計画を取得する。
	 * 
	 * @param jgiNo 従業員番号
	 * @return 担当者別計画
	 */
	private MrPlanForVac createMrPlan(Integer jgiNo) {

		MrPlanForVac mrPlan = null;
		try {
			// 担当者別計画取得
			mrPlan = mrPlanDao.searchUk(jgiNo, prodCode);

		} catch (DataNotFoundException e) {
			// エラーにしない（担当者別計画ゼロとする）
		}

		return mrPlan;
	}

	/**
	 * 品目固定コードを指定して、ワクチン用施設特約店別計画サマリを取得する。
	 * 
	 * @param jgiNo 従業員番号
	 * @return ワクチン用施設特約店別計画
	 */
	private InsWsPlanForVacProdSummaryDto createInsWsPlanSummary(Integer jgiNo) {

		InsWsPlanForVacProdSummaryDto summary = null;
		try {

			summary = insWsPlanDao.searchProdSummary(prodCode, null, jgiNo, null, null, null);

		} catch (DataNotFoundException e) {
			// エラーにしない（ワクチン用施設特約店別計画ゼロとする）
		}

		return summary;
	}
}
