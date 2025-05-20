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
import jp.co.takeda.dto.DeliveryResultForVacSosSummaryDto;
import jp.co.takeda.dto.InsWsPlanForVacPlannedSummaryDto;
import jp.co.takeda.dto.InsWsPlanForVacProdListResultDto;
import jp.co.takeda.dto.InsWsPlanForVacProdSummaryDto;
import jp.co.takeda.dto.MrPlanForVacSosSummaryDto;
import jp.co.takeda.dto.MrPlanResultValueDto;
import jp.co.takeda.dto.ProdInsWsPlanStatSummaryDto;
import jp.co.takeda.model.DeliveryResultMrForVac;
import jp.co.takeda.model.MonNnu;
import jp.co.takeda.model.MrPlanForVac;

/**
 * ワクチン用施設特約店別計画 品目一覧サマリ情報を作成するロジッククラス
 * 
 * @author nozaki
 */
public class CreateInsWsPlanForVacProdListLogic {

	/**
	 * 組織コード(エリア特約店G)
	 */
	private final String sosCd3;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 担当者別納入実績DAO
	 */
	private final DeliveryResultMrForVacDao deliveryResultMrDao;

	/**
	 * 担当者別計画DAO
	 */
	private final MrPlanForVacDao mrPlanDao;

	/**
	 * 施設特約店別計画DAO
	 */
	private final InsWsPlanForVacDao insWsPlanDao;

	/**
	 * コンストラクタ<br>
	 * エリア特約店Gの納入実績サマリを取得する。
	 * 
	 * @param sosCd3 組織コード(エリア特約店G)
	 * @param jgiNo 従業員番号
	 * @param deliveryResultMrDao 担当者別納入実績DAO
	 */
	public CreateInsWsPlanForVacProdListLogic(String sosCd3, Integer jgiNo, DeliveryResultMrForVacDao deliveryResultMrDao, MrPlanForVacDao mrPlanDao,
		InsWsPlanForVacDao insWsPlanDao) {
		if (sosCd3 == null && jgiNo == null) {
			final String errMsg = "納入実績サマリ取得条件がnull";
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
		this.sosCd3 = sosCd3;
		this.jgiNo = jgiNo;
		this.deliveryResultMrDao = deliveryResultMrDao;
		this.mrPlanDao = mrPlanDao;
		this.insWsPlanDao = insWsPlanDao;
	}

	/**
	 * 品目単位の施設特約店別計画ステータスサマリー情報を元に、 納入実績・施設特約店別計画のサマリ情報を取得する。
	 * 
	 * @param dto 品目単位の施設特約店別計画ステータスサマリー情報
	 * @return 品目一覧画面の検索結果用DTO
	 */
	public InsWsPlanForVacProdListResultDto execute(ProdInsWsPlanStatSummaryDto dto) {

		if (dto == null) {
			final String errMsg = "品目単位の施設特約店別計画ステータスサマリーがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		String prodCode = dto.getProdCode();

		// 納入実績サマリ取得
		MonNnu monNnu = createResultSummary(prodCode);
		MrPlanResultValueDto result = new MrPlanResultValueDto(monNnu);

		// 担当者別計画サマリ取得
		MrPlanForVacSosSummaryDto mrPlanSummary = createMrPlanSummary(prodCode);

		// 施設特約店別計画サマリ取得
		InsWsPlanForVacProdSummaryDto insWsPlanSummary = createInsWsPlanSummary(prodCode);

		// 計画サマリDTO作成
		InsWsPlanForVacPlannedSummaryDto plannedValueSummary = new InsWsPlanForVacPlannedSummaryDto(mrPlanSummary, insWsPlanSummary);

		return new InsWsPlanForVacProdListResultDto(dto, result, plannedValueSummary);
	}

	/**
	 * 品目固定コードを指定して、納入実績を取得する。
	 * 
	 * @param prodCode 品目固定コード
	 * @return 納入実績
	 */
	private MonNnu createResultSummary(String prodCode) {

		MonNnu monNnu = null;
		try {

			if (jgiNo != null) {

				// 従業員番号が指定されている場合は、担当者納入実績取得
				DeliveryResultMrForVac mrResult = deliveryResultMrDao.search(jgiNo, prodCode);
				monNnu = mrResult.getMonNnu();

			} else if (sosCd3 != null) {

				// 組織コード(エリア特約店G)が指定されている場合は、担当者納入実績のエリア特約店Gサマリ取得
				DeliveryResultForVacSosSummaryDto resultSummary = deliveryResultMrDao.searchSosSummary(prodCode, sosCd3);
				monNnu = resultSummary.getMonNnu();

			} else {

				final String errMsg = "施設特約店別計画サマリ取得条件が不正";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

		} catch (DataNotFoundException e) {
			// エラーにしない（実績ゼロとする）
		}

		return monNnu;
	}

	/**
	 * 品目固定コードを指定して、担当者別計画サマリを取得する。
	 * 
	 * @param prodCode 品目固定コード
	 * @return 担当者別計画(サマリしたもの)
	 */
	private MrPlanForVacSosSummaryDto createMrPlanSummary(String prodCode) {

		MrPlanForVacSosSummaryDto summary = null;
		try {

			if (jgiNo != null) {

				// 従業員番号が指定されている場合は、担当者別計画取得
				MrPlanForVac mrPlan = mrPlanDao.searchUk(jgiNo, prodCode);
				summary = new MrPlanForVacSosSummaryDto(null, mrPlan);

			} else if (sosCd3 != null) {

				// 組織コード(エリア特約店G)が指定されている場合は、担当者別計画のエリア特約店Gサマリ取得
				summary = mrPlanDao.searchSosSummary(prodCode, sosCd3, null);

			} else {

				final String errMsg = "施設特約店別計画サマリ取得条件が不正";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

		} catch (DataNotFoundException e) {
			// エラーにしない（担当者別計画ゼロとする）
		}

		return summary;
	}

	/**
	 * 品目固定コードを指定して、施設特約店別計画サマリを取得する。
	 * 
	 * @param prodCode 品目固定コード
	 * @return 施設特約店別計画(サマリしたもの)
	 */
	private InsWsPlanForVacProdSummaryDto createInsWsPlanSummary(String prodCode) {

		InsWsPlanForVacProdSummaryDto summary = null;
		try {

			summary = insWsPlanDao.searchProdSummary(prodCode, sosCd3, jgiNo, null, null, null);

		} catch (DataNotFoundException e) {
			// エラーにしない（施設特約店別計画ゼロとする）
		}

		return summary;
	}
}
