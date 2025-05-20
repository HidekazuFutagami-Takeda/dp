package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DeliveryResultMrDao;
import jp.co.takeda.dao.InsDocPlanStatusDao;
import jp.co.takeda.dao.InsPlanDao;
import jp.co.takeda.dao.InsWsPlanDao;
import jp.co.takeda.dao.MrPlanDao;
import jp.co.takeda.dto.InsWsPlanMrResultDto;
import jp.co.takeda.dto.InsWsPlanPlannedSummaryDto;
import jp.co.takeda.dto.InsWsPlanProdSummaryDto;
import jp.co.takeda.dto.MrPlanResultValueDto;
import jp.co.takeda.dto.MrPlanSosSummaryDto;
import jp.co.takeda.model.DeliveryResultMr;
import jp.co.takeda.model.InsWsCount;
import jp.co.takeda.model.InsWsPlanStatus;
import jp.co.takeda.model.MonNnu;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.model.MrPlanStatus;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.service.DpsPlannedCtgSearchService;

/**
 * 施設特約店別計画 担当者一覧サマリ情報を作成するロジッククラス
 *
 * @author nozaki
 */
public class CreateInsWsPlanMrListLogic {

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 担当者別納入実績DAO
	 */
	private final DeliveryResultMrDao deliveryResultMrDao;

	/**
	 * 担当者別計画DAO
	 */
	private final MrPlanDao mrPlanDao;

	/**
	 * 施設特約店別計画DAO
	 */
	private final InsWsPlanDao insWsPlanDao;

	/**
	 * 施設別計画DAO
	 */
	private final InsPlanDao insPlanDao;

	/**
	 * 施設特約店別計画　表示判定ロジック
	 */
	private final CheckInsWsDispLogic dipsLogic;

	/**
	 * コンストラクタ
	 *
	 * @param prodCode 品目固定コード
	 * @param deliveryResultMrDao 担当者別納入実績DAO
	 * @param mrPlanDao 担当者別計画DAO
	 * @param insWsPlanDao 施設特約店別計画DAO
	 * @param dpsCodeMasterSearchService 汎用マスタ検索サービス
	 * @param dpsPlannedCtgSearchService 計画対象カテゴリ領域検索サービス
	 */
	public CreateInsWsPlanMrListLogic(String prodCode, DeliveryResultMrDao deliveryResultMrDao, MrPlanDao mrPlanDao, InsPlanDao insPlanDao, InsWsPlanDao insWsPlanDao, InsDocPlanStatusDao insDocPlanStatusDao, DpsCodeMasterSearchService dpsCodeMasterSearchService,
			DpsPlannedCtgSearchService dpsPlannedCtgSearchService) {
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (deliveryResultMrDao == null) {
			final String errMsg = "担当者別納入実績DAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (mrPlanDao == null) {
			final String errMsg = "担当者別計画DAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insPlanDao == null) {
			final String errMsg = "施設別計画DAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insWsPlanDao == null) {
			final String errMsg = "施設特約店別計画DAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insDocPlanStatusDao == null) {
			final String errMsg = "施設医師別計画ステータスDAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dpsPlannedCtgSearchService == null) {
			final String errMsg = "計画対象カテゴリ領域検索サービスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		this.prodCode = prodCode;
		this.deliveryResultMrDao = deliveryResultMrDao;
		this.mrPlanDao = mrPlanDao;
		this.insPlanDao = insPlanDao;
		this.insWsPlanDao = insWsPlanDao;
		this.dipsLogic = new CheckInsWsDispLogic(insDocPlanStatusDao, dpsCodeMasterSearchService, dpsPlannedCtgSearchService);
	}

	/**
	 * 品目単位の施設特約店別計画ステータスサマリー情報を元に、 納入実績・施設特約店別計画のサマリ情報を取得する。
	 *
	 * @param dto 品目単位の施設特約店別計画ステータスサマリー情報
	 * @param open 計画表示可能か（上位計画が公開されているか）
	 * @return 品目一覧画面の検索結果用DTO
	 */
	public InsWsPlanMrResultDto execute(MrPlanStatus mrPlanStatus, InsWsPlanStatus insWsPlanStatus, PlannedProd plannedProd, List<InsWsCount> delInsCountList,
			List<InsWsCount> taigaiTytenCountList, List<InsWsCount> exceptDistInsCountList, boolean isVaccine) {

		if (insWsPlanStatus == null) {
			final String errMsg = "施設特約店別計画立案ステータスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		Integer jgiNo = insWsPlanStatus.getJgiNo();

		// 納入実績サマリUH取得
		MonNnu monNnuUh = createResultSummary(jgiNo, InsType.UH);
		MrPlanResultValueDto resultUh = new MrPlanResultValueDto(monNnuUh);

		// 納入実績サマリP取得
		MonNnu monNnuP = createResultSummary(jgiNo, InsType.P);

		if (isVaccine) {
			// ワクチンの場合、納入実績サマリ雑P取得
			MonNnu monNnuZatu = createResultSummary(jgiNo, InsType.ZATU);

			// 納入実績サマリPに合算する
			Long[] sumSP = { 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L };
			if (monNnuP != null) {
				sumSP = createSumArray(sumSP, monNnuP);
			}
			if (monNnuZatu != null) {
				sumSP = createSumArray(sumSP, monNnuZatu);
			}
			monNnuP = createMonNnu(sumSP);
		}
		MrPlanResultValueDto resultP = new MrPlanResultValueDto(monNnuP);

		// 計画サマリ取得
		MrPlanSosSummaryDto mrPlanSummary = createMrPlanSummary(jgiNo);
		InsWsPlanProdSummaryDto insWsPlanSummary = createInsWsPlanSummary(jgiNo, plannedProd.getCategory());
		InsWsPlanPlannedSummaryDto plannedValueSummary = new InsWsPlanPlannedSummaryDto(mrPlanSummary, insWsPlanSummary);

		// 合算施設間調整取得（重点品目の場合のみ取得）
		Map<String, Object> insChoseiResult = null;
		if(BooleanUtils.isTrue(plannedProd.getPlanLevelInsDoc())){
			try {
				insChoseiResult = insPlanDao.checkChoseiInsWs(null, null, jgiNo, plannedProd.getProdCode());
			} catch (DataNotFoundException e) {
				insChoseiResult = new HashMap<String, Object>();
				insChoseiResult.put("existDiffUh", false);
				insChoseiResult.put("existDiffP", false);
			}
		}

		// 削除施設数を取得
		Integer delInsCount = 0;
		for (InsWsCount dto : delInsCountList) {
			if (jgiNo.compareTo(dto.getJgiNo()) == 0 && dto.getCount() != null) {
				delInsCount = dto.getCount();
			}
		}

		// 対象外特約店数を取得
		Integer taigaiTytenCount = 0;
		for (InsWsCount dto : taigaiTytenCountList) {
			if (jgiNo.compareTo(dto.getJgiNo()) == 0 && dto.getCount() != null) {
				taigaiTytenCount = dto.getCount();
			}
		}

		// 配分除外施設数を取得
		Integer exceptDistInsCount = 0;
		for (InsWsCount dto : exceptDistInsCountList) {
			if (jgiNo.compareTo(dto.getJgiNo()) == 0 && dto.getCount() != null) {
				exceptDistInsCount = dto.getCount();
			}
		}

		// 表示フラグ取得
		boolean dispPlan = dipsLogic.dipsPlan(plannedProd, mrPlanStatus, insWsPlanStatus);
		boolean dispEditLink = dipsLogic.dipsEditLink(jgiNo, plannedProd, mrPlanStatus, insWsPlanStatus);
		boolean enableRehaibun = dipsLogic.enableRehaibun(jgiNo, plannedProd, mrPlanStatus, insWsPlanStatus);

		return new InsWsPlanMrResultDto(insWsPlanStatus, resultUh, resultP, plannedValueSummary, insChoseiResult, dispPlan, dispEditLink, enableRehaibun,
				delInsCount, taigaiTytenCount, exceptDistInsCount);
	}

	/**
	 * 品目固定コード、施設出力対象区分を指定して、納入実績を取得する。
	 *
	 * @param jgiNo 従業員番号
	 * @param insType 施設出力対象区分
	 * @return 納入実績
	 */
	private MonNnu createResultSummary(Integer jgiNo, InsType insType) {

		MonNnu monNnu = null;
		try {
			// 従業員番号が指定されている場合は、担当者納入実績取得
			DeliveryResultMr mrResult = deliveryResultMrDao.search(jgiNo, prodCode, insType);
			monNnu = mrResult.getMonNnu();

		} catch (DataNotFoundException e) {
			// エラーにしない（実績ゼロとする）
		}

		return monNnu;
	}

	/**
	 * 品目固定コードを指定して、担当者別計画サマリを取得する。
	 *
	 * @param jgiNo 従業員番号
	 * @return 担当者別計画(サマリしたもの)
	 */
	private MrPlanSosSummaryDto createMrPlanSummary(Integer jgiNo) {

		MrPlanSosSummaryDto summary = null;
		try {
			// 従業員番号が指定されている場合は、担当者別計画取得
			MrPlan mrPlan = mrPlanDao.searchUk(jgiNo, prodCode);
			summary = new MrPlanSosSummaryDto(null, mrPlan);

		} catch (DataNotFoundException e) {
			// エラーにしない（担当者別計画ゼロとする）
		}

		return summary;
	}

	/**
	 * 品目固定コードを指定して、施設特約店別計画サマリを取得する。
	 *
	 * @param jgiNo 従業員番号
	 * @return 施設特約店別計画(サマリしたもの)
	 */
	private InsWsPlanProdSummaryDto createInsWsPlanSummary(Integer jgiNo, String category) {

		InsWsPlanProdSummaryDto summary = null;
		try {

			summary = insWsPlanDao.searchProdSummaryCtg(null, null, jgiNo, prodCode, category);

		} catch (DataNotFoundException e) {
			// エラーにしない（施設特約店別計画ゼロとする）
		}

		return summary;
	}

	/**
	 * 計算用に引数がnullの場合に0を返す。
	 *
	 * @param value 値
	 * @return 過去実績詳細検索結果
	 */
	private Long getLongValue(Long value) {
		if (value != null) {
			return value;
		} else {
			return 0L;
		}
	}

	/**
	 * 引数をMonNnuの各プロパティに設定したMonNnuを生成する。
	 *
	 * @param Long[] value
	 * @return 生成されたMonNnu
	 */
	private MonNnu createMonNnu(Long[] value) {
		MonNnu obj = new MonNnu();
		obj.setDeliveryRecord01(value[0]);
		obj.setDeliveryRecord02(value[1]);
		obj.setDeliveryRecord03(value[2]);
		obj.setDeliveryRecord04(value[3]);
		obj.setDeliveryRecord05(value[4]);
		obj.setDeliveryRecord06(value[5]);
		obj.setDeliveryRecord07(value[6]);
		obj.setDeliveryRecord08(value[7]);
		obj.setDeliveryRecord09(value[8]);
		obj.setDeliveryRecord10(value[9]);
		obj.setDeliveryRecord11(value[10]);
		obj.setDeliveryRecord12(value[11]);
		obj.setDeliveryRecord13(value[12]);
		obj.setDeliveryRecord14(value[13]);
		obj.setDeliveryRecord15(value[14]);
		obj.setDeliveryRecord16(value[15]);
		obj.setDeliveryRecord17(value[16]);
		obj.setDeliveryRecord18(value[17]);
		obj.setDeliveryRecord19(value[18]);
		obj.setDeliveryRecord20(value[19]);
		obj.setDeliveryRecord21(value[20]);
		obj.setDeliveryRecord22(value[21]);
		obj.setDeliveryRecord23(value[22]);
		obj.setDeliveryRecord24(value[23]);
		obj.setPreFarAdvancePeriod(value[24]);
		obj.setFarAdvancePeriod(value[25]);
		obj.setAdvancePeriod(value[26]);
		obj.setCurrentPeriod(value[27]);
		obj.setCurrentPlanValue(value[28]);

		return obj;
	}

	/**
	 * 引数のLong[]に引数のMonNnuの各プロパティを加算し、計算結果のLong[]を返す。
	 *
	 * @param Long[] value
	 * @param MonNnu obj
	 * @return 計算結果のLong[]
	 */
	private Long[] createSumArray(Long[] value, MonNnu obj) {
		value[0] += getLongValue(obj.getDeliveryRecord01());
		value[1] += getLongValue(obj.getDeliveryRecord02());
		value[2] += getLongValue(obj.getDeliveryRecord03());
		value[3] += getLongValue(obj.getDeliveryRecord04());
		value[4] += getLongValue(obj.getDeliveryRecord05());
		value[5] += getLongValue(obj.getDeliveryRecord06());
		value[6] += getLongValue(obj.getDeliveryRecord07());
		value[7] += getLongValue(obj.getDeliveryRecord08());
		value[8] += getLongValue(obj.getDeliveryRecord09());
		value[9] += getLongValue(obj.getDeliveryRecord10());
		value[10] += getLongValue(obj.getDeliveryRecord11());
		value[11] += getLongValue(obj.getDeliveryRecord12());
		value[12] += getLongValue(obj.getDeliveryRecord13());
		value[13] += getLongValue(obj.getDeliveryRecord14());
		value[14] += getLongValue(obj.getDeliveryRecord15());
		value[15] += getLongValue(obj.getDeliveryRecord16());
		value[16] += getLongValue(obj.getDeliveryRecord17());
		value[17] += getLongValue(obj.getDeliveryRecord18());
		value[18] += getLongValue(obj.getDeliveryRecord19());
		value[19] += getLongValue(obj.getDeliveryRecord20());
		value[20] += getLongValue(obj.getDeliveryRecord21());
		value[21] += getLongValue(obj.getDeliveryRecord22());
		value[22] += getLongValue(obj.getDeliveryRecord23());
		value[23] += getLongValue(obj.getDeliveryRecord24());
		value[24] += getLongValue(obj.getPreFarAdvancePeriod());
		value[25] += getLongValue(obj.getFarAdvancePeriod());
		value[26] += getLongValue(obj.getAdvancePeriod());
		value[27] += getLongValue(obj.getCurrentPeriod());
		value[28] += getLongValue(obj.getCurrentPlanValue());
		return value;
	}

}
