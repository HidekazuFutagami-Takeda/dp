package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DeliveryResultMrDao;
import jp.co.takeda.dao.InsDocPlanStatusDao;
import jp.co.takeda.dao.InsPlanDao;
import jp.co.takeda.dao.InsWsPlanDao;
import jp.co.takeda.dao.MrPlanDao;
import jp.co.takeda.dao.MrPlanStatusDao;
import jp.co.takeda.dto.DeliveryResultSosSummaryDto;
import jp.co.takeda.dto.InsWsPlanPlannedSummaryDto;
import jp.co.takeda.dto.InsWsPlanProdListResultDto;
import jp.co.takeda.dto.InsWsPlanProdSummaryDto;
import jp.co.takeda.dto.MrPlanResultValueDto;
import jp.co.takeda.dto.MrPlanSosSummaryDto;
import jp.co.takeda.dto.ProdInsWsPlanStatSummaryDto;
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
 * 施設特約店別計画 品目一覧サマリ情報を作成するロジッククラス
 *
 * @author nozaki
 */
public class CreateInsWsPlanProdListLogic {

	/**
	 * 組織コード(営業所)
	 */
	private final String sosCd3;

	/**
	 * 組織コード(チーム)
	 */
	private final String sosCd4;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 担当者別納入実績DAO
	 */
	private final DeliveryResultMrDao deliveryResultMrDao;

	/**
	 * 担当者別計画DAO
	 */
	private final MrPlanDao mrPlanDao;

	/**
	 * 施設別計画DAO
	 */
	private final InsPlanDao insPlanDao;

	/**
	 * 施設特約店別計画DAO
	 */
	private final InsWsPlanDao insWsPlanDao;

	/**
	 * 担当者別計画ステータスDAO
	 */
	private final  MrPlanStatusDao mrPlanStatusDao;

	/**
	 * 施設特約店別計画　表示判定ロジック
	 */
	private final CheckInsWsDispLogic dipsLogic;

	/**
	 * コンストラクタ<br>
	 * チームの納入実績サマリを取得する。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param jgiNo 従業員番号
	 * @param deliveryResultMrDao 担当者別納入実績DAO
	 * @param dpsCodeMasterSearchService 汎用マスタ検索サービス
	 * @param dpsPlannedCtgSearchService 計画対象カテゴリ領域検索サービス
	 */
	public CreateInsWsPlanProdListLogic(String sosCd3, String sosCd4, Integer jgiNo, DeliveryResultMrDao deliveryResultMrDao, MrPlanDao mrPlanDao, InsPlanDao insPlanDao, InsWsPlanDao insWsPlanDao,MrPlanStatusDao mrPlanStatusDao, InsDocPlanStatusDao insDocPlanStatusDao,
			DpsCodeMasterSearchService dpsCodeMasterSearchService, DpsPlannedCtgSearchService dpsPlannedCtgSearchService ) {
		if (sosCd3 == null && sosCd4 == null && jgiNo == null) {
			final String errMsg = "納入実績サマリ取得条件がnull";
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
			final String errMsg = "施設医師別計画DAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dpsPlannedCtgSearchService == null) {
			final String errMsg = "計画対象カテゴリ領域検索サービスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		this.sosCd3 = sosCd3;
		this.sosCd4 = sosCd4;
		this.jgiNo = jgiNo;
		this.deliveryResultMrDao = deliveryResultMrDao;
		this.mrPlanDao = mrPlanDao;
		this.insPlanDao = insPlanDao;
		this.insWsPlanDao = insWsPlanDao;
		this.mrPlanStatusDao = mrPlanStatusDao;
		this.dipsLogic = new CheckInsWsDispLogic(insDocPlanStatusDao, dpsCodeMasterSearchService, dpsPlannedCtgSearchService);
	}

	/**
	 * 品目単位の施設特約店別計画ステータスサマリー情報を元に、 納入実績・施設特約店別計画のサマリ情報を取得する。
	 *
	 * @param dto 品目単位の施設特約店別計画ステータスサマリー情報
	 * @return 品目一覧画面の検索結果用DTO
	 */
	public InsWsPlanProdListResultDto execute(ProdInsWsPlanStatSummaryDto dto, PlannedProd plannedProd, InsWsPlanStatus insWsPlanStatus) {

		if (dto == null) {
			final String errMsg = "品目単位の施設特約店別計画ステータスサマリーがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProd == null) {
			final String errMsg = "計画対象品目がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		String prodCode = dto.getProdCode();
		String category = plannedProd.getCategory();

		// 担当者別計画取得
		MrPlanStatus mrPlanStatus = null;
		try {
			mrPlanStatus = mrPlanStatusDao.search(sosCd3, prodCode);
		} catch (DataNotFoundException e) {
			// ない場合も可
		}

		// 納入実績サマリUH取得
		MonNnu monNnuUh = createResultSummary(prodCode, InsType.UH, category);
		MrPlanResultValueDto resultUh = new MrPlanResultValueDto(monNnuUh);

		// 納入実績サマリP取得
		MonNnu monNnuP = createResultSummary(prodCode, InsType.P, category);
		MrPlanResultValueDto resultP = new MrPlanResultValueDto(monNnuP);

		// 計画サマリ取得
		MrPlanSosSummaryDto mrPlanSummary = createMrPlanSummary(prodCode);
		InsWsPlanProdSummaryDto insWsPlanSummary = createInsWsPlanSummary(prodCode, category);
		InsWsPlanPlannedSummaryDto plannedValueSummary = new InsWsPlanPlannedSummaryDto(mrPlanSummary, insWsPlanSummary);

		// 合算施設間調整取得（重点品目の場合のみ取得）
		Map<String, Object> insChoseiResult = null;
		if(BooleanUtils.isTrue(plannedProd.getPlanLevelInsDoc())){
			try {
				if (jgiNo != null) {
					insChoseiResult = insPlanDao.checkChoseiInsWs(null, null, jgiNo, plannedProd.getProdCode());
				} else if (sosCd4 != null) {
					insChoseiResult = insPlanDao.checkChoseiInsWs(null, sosCd4, null, plannedProd.getProdCode());
				} else if (sosCd3 != null) {
					insChoseiResult = insPlanDao.checkChoseiInsWs(sosCd3, null, null, plannedProd.getProdCode());
				} else {
					final String errMsg = "合算施設間調整取得条件が不正";
					throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
				}
			} catch (DataNotFoundException e) {
				insChoseiResult = new HashMap<String, Object>();
				insChoseiResult.put("existDiffUh", false);
				insChoseiResult.put("existDiffP", false);
			}
		}

		// 削除施設数を取得
		List<InsWsCount> delInsCountList = new ArrayList<InsWsCount>();
		if (jgiNo != null) {
			delInsCountList = insWsPlanDao.searchDelInsCount(sosCd3, jgiNo, prodCode, true);
		} else {
			delInsCountList = insWsPlanDao.searchDelInsCount(sosCd3, null, prodCode, true);
		}
		Integer delInsCount = 0;
		for (InsWsCount count : delInsCountList) {
			if (prodCode.equals(count.getProdCode()) && count.getCount() != null) {
				delInsCount = count.getCount();
			}
		}

		// 対象外特約店数を取得
		List<InsWsCount> taigaiTytenCountList = new ArrayList<InsWsCount>();
		if (jgiNo != null) {
			taigaiTytenCountList = insWsPlanDao.searchTaiGaiTytenCount(sosCd3, jgiNo, prodCode, true);
		} else {
			taigaiTytenCountList = insWsPlanDao.searchTaiGaiTytenCount(sosCd3, null, prodCode, true);
		}

		Integer taigaiTytenCount = 0;
		for (InsWsCount count : taigaiTytenCountList) {
			if (prodCode.equals(count.getProdCode()) && count.getCount() != null) {
				taigaiTytenCount = count.getCount();
			}
		}

		// 配分除外施設数を取得
		List<InsWsCount> exceptDistInsCountList = new ArrayList<InsWsCount>();
		if (jgiNo != null) {
			exceptDistInsCountList = insWsPlanDao.searchExceptDistInsCount(sosCd3, jgiNo, prodCode, true);
		} else {
			exceptDistInsCountList = insWsPlanDao.searchExceptDistInsCount(sosCd3, null, prodCode, true);
		}

		Integer exceptDistInsCount = 0;
		for (InsWsCount count : exceptDistInsCountList) {
			if (prodCode.equals(count.getProdCode()) && count.getCount() != null) {
				exceptDistInsCount = count.getCount();
			}
		}

		// 表示フラグ取得
//		boolean dispPlan = dipsLogic.dipsPlan(plannedProd, mrPlanStatus, dto.getInsWsPlanStatSum());
		boolean dispPlan = dipsLogic.dipsPlan(plannedProd, mrPlanStatus, insWsPlanStatus);
//		boolean dispEditLink = dipsLogic.dipsEditLink(jgiNo, plannedProd, mrPlanStatus, dto.getInsWsPlanStatSum());
		boolean dispEditLink = dipsLogic.dipsEditLink(jgiNo, plannedProd, mrPlanStatus, insWsPlanStatus);
		boolean enableRehaibun = dipsLogic.enableRehaibun(jgiNo, plannedProd, mrPlanStatus,dto.getInsDocPlanStatSum(), dto.getInsWsPlanStatSum());
		boolean planLevelInsDoc = plannedProd.getPlanLevelInsDoc();

		return new InsWsPlanProdListResultDto(mrPlanStatus, dto, resultUh, resultP, plannedValueSummary,insChoseiResult, dispPlan, dispEditLink, planLevelInsDoc,enableRehaibun,
				delInsCount, taigaiTytenCount, exceptDistInsCount);
	}

	/**
	 * 品目固定コード、施設出力対象区分を指定して、納入実績を取得する。
	 *
	 * @param prodCode 品目固定コード
	 * @param insType 施設出力対象区分
	 * @return 納入実績
	 */
	private MonNnu createResultSummary(String prodCode, InsType insType, String category) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (StringUtils.isBlank(category)) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		MonNnu monNnu = null;
		try {

			if (jgiNo != null) {

				// 従業員番号が指定されている場合は、担当者納入実績取得
				DeliveryResultMr mrResult = deliveryResultMrDao.search(jgiNo, prodCode, insType);
				monNnu = mrResult.getMonNnu();

			} else if (sosCd4 != null) {

				// 組織コード(チーム)が指定されている場合は、担当者納入実績のチームサマリ取得
				DeliveryResultSosSummaryDto teamSummary = deliveryResultMrDao.searchTeamSummary(prodCode, sosCd4, insType, category);
				monNnu = teamSummary.getMonNnu();

			} else if (sosCd3 != null) {

				// 組織コード(営業所)が指定されている場合は、担当者納入実績のチームサマリ取得
				DeliveryResultSosSummaryDto sosSummary = deliveryResultMrDao.searchSosSummary(prodCode, sosCd3, false, insType, category);
				monNnu = sosSummary.getMonNnu();

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
	private MrPlanSosSummaryDto createMrPlanSummary(String prodCode) {

		MrPlanSosSummaryDto summary = null;
		try {

			if (jgiNo != null) {

				// 従業員番号が指定されている場合は、担当者別計画取得
				MrPlan mrPlan = mrPlanDao.searchUk(jgiNo, prodCode);
				summary = new MrPlanSosSummaryDto(null, mrPlan);

			} else if (sosCd4 != null) {

				// 組織コード(チーム)が指定されている場合は、担当者別計画のチームサマリ取得
				summary = mrPlanDao.searchTeamSummary(prodCode, sosCd4);

			} else if (sosCd3 != null) {

				// 組織コード(営業所)が指定されている場合は、担当者別計画の営業所サマリ取得
				summary = mrPlanDao.searchSosSummary(prodCode, sosCd3);

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
	private InsWsPlanProdSummaryDto createInsWsPlanSummary(String prodCode, String category) {

		InsWsPlanProdSummaryDto summary = null;
		try {

			summary = insWsPlanDao.searchProdSummaryCtg(sosCd3, sosCd4, jgiNo, prodCode, category);

		} catch (DataNotFoundException e) {
			// エラーにしない（施設特約店別計画ゼロとする）
		}

		return summary;
	}
}
