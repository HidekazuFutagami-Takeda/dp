package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DeliveryResultInsTypeSummaryDao;
import jp.co.takeda.dao.DpsCodeMasterDao;
import jp.co.takeda.dao.EstParamHonbuDao;
import jp.co.takeda.dao.EstParamOfficeDao;
import jp.co.takeda.dao.EstimationProdDao;
import jp.co.takeda.dao.MikakutokuSijouInsTypeSummaryDao;
import jp.co.takeda.dao.MrPlanFreeIndexDao;
import jp.co.takeda.dao.MrPlanStatusDao;
import jp.co.takeda.dao.OfficePlanDao;
import jp.co.takeda.dao.OfficePlanStatusDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.SysManageDao;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.EstimationExecDto;
import jp.co.takeda.dto.EstimationExecOrgDto;
import jp.co.takeda.dto.EstimationParamResultDto;
import jp.co.takeda.dto.EstimationParamUpdateDto;
import jp.co.takeda.dto.EstimationProdListResultDto;
import jp.co.takeda.dto.EstimationProdResultDto;
import jp.co.takeda.dto.FreeIndexDto;
import jp.co.takeda.dto.FreeIndexResultDetailDto;
import jp.co.takeda.dto.FreeIndexResultDto;
import jp.co.takeda.model.BaseParam;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.EstParamHonbu;
import jp.co.takeda.model.EstParamOffice;
import jp.co.takeda.model.EstimationParam;
import jp.co.takeda.model.MrPlanFreeIndex;
import jp.co.takeda.model.MrPlanStatus;
import jp.co.takeda.model.OfficePlan;
import jp.co.takeda.model.OfficePlanStatus;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.ProdInfo;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.CalcType;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.RefPeriod;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.model.div.Term;
import jp.co.takeda.model.view.DeliveryResultInsTypeSummary;
import jp.co.takeda.model.view.EstimationProd;
import jp.co.takeda.model.view.MikakutokuSijouInsTypeSummary;
import jp.co.takeda.util.MathUtil;

/**
 * 試算機能の検索に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpsEstimationProdSearchService")
public class DpsEstimationProdSearchServiceImpl implements DpsEstimationProdSearchService {

	/**
	 * メッセージソース
	 */
	@Autowired(required = true)
	@Qualifier("messageSource")
	protected MessageSource messageSource;

	/**
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * 試算対象品目一覧DAO
	 */
	@Autowired(required = true)
	@Qualifier("estimationProdDao")
	protected EstimationProdDao estimationProdDao;

	/**
	 * 試算パラメータ（本部）DAO
	 */
	@Autowired(required = true)
	@Qualifier("estParamHonbuDao")
	protected EstParamHonbuDao estParamHonbuDao;

	/**
	 * 試算パラメータ（営業所）DAO
	 */
	@Autowired(required = true)
	@Qualifier("estParamOfficeDao")
	protected EstParamOfficeDao estParamOfficeDao;

	/**
	 * 営業所計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("officePlanDao")
	protected OfficePlanDao officePlanDao;

	/**
	 * 未獲得市場 施設出力対象区分集計DAO
	 */
	@Autowired(required = true)
	@Qualifier("mikakutokuSijouInsTypeSummaryDao")
	protected MikakutokuSijouInsTypeSummaryDao mikakutokuSijouInsTypeSummaryDao;

	/**
	 * 納入実績 施設出力対象区分集計DAO
	 */
	@Autowired(required = true)
	@Qualifier("deliveryResultInsTypeSummaryDao")
	protected DeliveryResultInsTypeSummaryDao deliveryResultInsTypeSummaryDao;

	/**
	 * 納入計画管理DAO
	 */
	@Autowired(required = true)
	@Qualifier("sysManageDao")
	protected SysManageDao sysManageDao;

	/**
	 * 担当者別計画フリー項目DAO
	 */
	@Autowired(required = true)
	@Qualifier("mrPlanFreeIndexDao")
	protected MrPlanFreeIndexDao mrPlanFreeIndexDao;

	/**
	 * 営業所計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("officePlanStatusDao")
	protected OfficePlanStatusDao officePlanStatusDao;

	/**
	 * 担当者別計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("mrPlanStatusDao")
	protected MrPlanStatusDao mrPlanStatusDao;

	/**
	 * 担当者別計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterDao")
	protected DpsCodeMasterDao dpsCodeMasterDao;


	// 試算対象品目一覧取得
//	public EstimationProdListResultDto searchEstimationProdList(String sosCd, List<CodeAndValue> categorylist) throws LogicalException {
	public EstimationProdListResultDto searchEstimationProdList(String sosCd, String category) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 営業所コードから組織情報取得
		SosMst sosMst;
		try {
			sosMst = sosMstDAO.search(sosCd);
		} catch (DataNotFoundException e) {
			final String errMsg = "対象組織がない：" + sosCd;
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		// -----------------------------
		// ワクチンコードの取得
		// -----------------------------
		List<DpsCCdMst> searchList = new ArrayList<DpsCCdMst>();
		try {
			// カテゴリの検索
			searchList = dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.VAC.getDbValue());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + DpsCodeMaster.VAC.getDbValue() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		String vaccineCode = searchList.get(0).getDataCd();

		// -----------------------------
		// 売上所属の判定
		// -----------------------------
		Sales sales = null;
		SysType sysType = null;
		if(category.equals(vaccineCode)) {
			sales = Sales.VACCHIN;
			sysType = SysType.VACCINE;
		}else{
			sales = Sales.IYAKU;
			sysType = SysType.IYAKU;
		}

		// ----------------------
		// 当期年度、期を取得
		// ----------------------
		SysManage sysManage;
		try {
			sysManage = sysManageDao.search(SysClass.DPS, sysType);
		} catch (DataNotFoundException e) {
			final String errMsg = "納入計画管理情報が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		String sysYear = sysManage.getSysYear();
		Term sysTerm = sysManage.getSysTerm();

		// ----------------------
		// 試算対象品目一覧取得
		// ----------------------
		List<EstimationProd> estimationProdList = estimationProdDao.searchList(sosCd, category, sales);
		// ----------------------
		// 試算パラメータ取得
		// ----------------------
		List<EstimationProdResultDto> resultDtoList = new ArrayList<EstimationProdResultDto>();
		for (EstimationProd estimationProd : estimationProdList) {

			EstimationProd _estimationProd;
			boolean _isHonbu;
			Date _estParamLastUpdate;
			EstimationParam _estimationParam;
			BaseParam _baseParam;
			Date _refPeriodFrom;
			Date _refPeriodTo;
			Date _mrPlanStatusUpDate = null;

			try {
				// 試算パラメータ（営業所案）取得
				EstParamOffice estParamOffice = estParamOfficeDao.search(sosCd, estimationProd.getProdCode());

				_estimationProd = estimationProd;
				_isHonbu = false;
				_estParamLastUpdate = estParamOffice.getUpDate();
				_estimationParam = estParamOffice.getEstimationParam();
				_baseParam = estParamOffice.getBaseParam();
				_refPeriodFrom = RefPeriod.convertRefPeriod(estParamOffice.getBaseParam().getRefFrom(), sysYear, sysTerm);
				_refPeriodTo = RefPeriod.convertRefPeriod(estParamOffice.getBaseParam().getRefTo(), sysYear, sysTerm);

			} catch (DataNotFoundException e) {

				// 営業所案が存在しない場合は、試算パラメータ（本部案）を取得
				try {
					EstParamHonbu estParamHonbu = estParamHonbuDao.search(estimationProd.getProdCode());

					_estimationProd = estimationProd;
					_isHonbu = true;
					_estParamLastUpdate = estParamHonbu.getUpDate();
					_estimationParam = estParamHonbu.getEstimationParam();
					_baseParam = estParamHonbu.getBaseParam();
					_refPeriodFrom = RefPeriod.convertRefPeriod(estParamHonbu.getBaseParam().getRefFrom(), sysYear, sysTerm);
					_refPeriodTo = RefPeriod.convertRefPeriod(estParamHonbu.getBaseParam().getRefTo(), sysYear, sysTerm);

				} catch (DataNotFoundException e1) {
					final String errMsg = "試算パラメータ(本部案、営業所案)が存在しない：";
					throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + estimationProd.getProdCode()), e1);
				}
			}

			try {
				MrPlanStatus mrPlanStatus = mrPlanStatusDao.search(sosCd, estimationProd.getProdCode());
				_mrPlanStatusUpDate = mrPlanStatus.getUpDate();
			} catch (Exception e) {
			}

			// 結果用DTO作成
			EstimationProdResultDto resultDto = new EstimationProdResultDto(_estimationProd, _isHonbu, _estParamLastUpdate, _estimationParam, _baseParam, _refPeriodFrom,
				_refPeriodTo, _mrPlanStatusUpDate);

			resultDtoList.add(resultDto);
		}

		// 試算タイプを取得
		CalcType calcType = null;
		try {
			OfficePlanStatus officePlanStatus = officePlanStatusDao.search(sosCd, category);
			calcType = officePlanStatus.getCalcType();
		} catch (DataNotFoundException e) {
			// 営業所計画立案ステータスがなくても、エラーにはしない
		}

		return new EstimationProdListResultDto(calcType, resultDtoList);
	}

	// 納入実績、未獲得市場取得
	public List<String> searchNoResultProdList(String sosCd, String[] prodCodeArr) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCodeArr == null || prodCodeArr.length == 0) {
			final String errMsg = "品目固定コード配列がnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		List<String> messageList = new ArrayList<String>();
		for (String prodCode : prodCodeArr) {

			// ----------------------
			// 計画対象品目取得
			// ----------------------
			PlannedProd plannedProd = null;
			try {
				plannedProd = plannedProdDAO.search(prodCode);
			} catch (DataNotFoundException e) {
				final String errMsg = "計画対象品目が存在しない";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
			}
			String[] prodName = { plannedProd.getProdName() };

			// ----------------------
			// 営業所計画
			// ----------------------
			long officePlanUH = 0;
			long officePlanP = 0;
			try {

				OfficePlan officePlan = officePlanDao.searchUk(sosCd, prodCode);
				if (officePlan.getPlannedValueUhY() != null) {
					officePlanUH = officePlan.getPlannedValueUhY();
				}
				if (officePlan.getPlannedValuePY() != null) {
					officePlanP = officePlan.getPlannedValuePY();
				}
			} catch (DataNotFoundException e) {
				// エラーにしない
			}
			// 営業所計画が0の場合は、チェックしない
			if (officePlanUH == 0 && officePlanP == 0) {
				continue;
			}

			// ----------------------
			// 試算パラメータ取得
			// ----------------------
			BaseParam baseParam = null;
			EstimationParam estimationParam = null;
			try {
				// 試算パラメータ（営業所案）取得
				EstParamOffice estParamOffice = estParamOfficeDao.search(sosCd, prodCode);
				baseParam = estParamOffice.getBaseParam();
				estimationParam = estParamOffice.getEstimationParam();

			} catch (DataNotFoundException e) {

				// 営業所案が存在しない場合は、試算パラメータ（本部案）を取得
				try {
					EstParamHonbu estParamHonbu = estParamHonbuDao.search(prodCode);
					baseParam = estParamHonbu.getBaseParam();
					estimationParam = estParamHonbu.getEstimationParam();

				} catch (DataNotFoundException e1) {
					final String errMsg = "試算パラメータ(本部案、営業所案)が存在しない：";
					throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e1);
				}
			}

			// -------------------------------------------
			// 指数(納入実績) ＞ 0の場合、納入実績チェック
			// -------------------------------------------
			if (estimationParam.getIndexDelivery() > 0) {
				long deliveryResultSumUH = 0;
				long deliveryResultSumP = 0;
				try {
					List<DeliveryResultInsTypeSummary> drSumList = deliveryResultInsTypeSummaryDao.searchSummaryList(sosCd, baseParam.getRefProdCode(), baseParam.getRefFrom(),
						baseParam.getRefTo());
					for (DeliveryResultInsTypeSummary drSum : drSumList) {
						long sumDeliveryRecord = 0;
						if (drSum.getSumDeliveryRecord() != null) {
							sumDeliveryRecord = drSum.getSumDeliveryRecord();
						}
						switch (drSum.getInsType()) {
							case UH:
								deliveryResultSumUH = sumDeliveryRecord;
								break;
							case P:
								deliveryResultSumP = sumDeliveryRecord;
								break;
							case ZATU:
								deliveryResultSumP = sumDeliveryRecord;
								break;
						}
					}
				} catch (DataNotFoundException e) {
					// エラーにしない
				}
				// [営業所計画].[計画値UH(Y)] ＞ ０ ＡＮＤ [試算パラメータ(X)].[指数(納入実績)] ＞ ０ ＡＮＤ <納入実績UH母数> ＝ ０
				if (deliveryResultSumUH == 0 && officePlanUH > 0) {
					messageList.add(messageSource.getMessage("DPS2021W", prodName, Locale.getDefault()));
				}
				// [営業所計画].[計画値P(Y)] ＞ ０ ＡＮＤ [試算パラメータ(X)].[指数(納入実績)] ＞ ０ ＡＮＤ <納入実績P母数> ＝ ０
				if (deliveryResultSumP == 0 && officePlanP > 0) {
					messageList.add(messageSource.getMessage("DPS2022W", prodName, Locale.getDefault()));
				}
			}

			// -------------------------------------------
			// 指数(未獲得市場) ＞ 0の場合、納入実績チェック
			// -------------------------------------------
			if (estimationParam.getIndexMikakutoku() > 0) {
				long mikakutokuSumUH = 0;
				long mikakutokuSumP = 0;
				try {
					List<MikakutokuSijouInsTypeSummary> mikakutokuSumList = mikakutokuSijouInsTypeSummaryDao.searchSummaryList(sosCd, prodCode);
					for (MikakutokuSijouInsTypeSummary mikakutokuSum : mikakutokuSumList) {
						long distPlanMikakutoku = 0;
						if (mikakutokuSum.getDistPlanMikakutoku() != null) {
							distPlanMikakutoku = mikakutokuSum.getDistPlanMikakutoku();
						}
						switch (mikakutokuSum.getInsType()) {
							case UH:
								mikakutokuSumUH = distPlanMikakutoku;
								break;
							case P:
								mikakutokuSumP = distPlanMikakutoku;
								break;
							case ZATU:
								mikakutokuSumP = distPlanMikakutoku;
								break;
						}
					}
				} catch (DataNotFoundException e) {
					// エラーにしない
				}
				// [営業所計画].[計画値UH(Y)] ＞ ０ ＡＮＤ [試算パラメータ(X)].[指数(未獲得市場)] ＞ ０ ＡＮＤ <未獲得市場UH母数> ＝ ０
				if (mikakutokuSumUH == 0 && officePlanUH > 0) {
					messageList.add(messageSource.getMessage("DPS2023W", prodName, Locale.getDefault()));
				}
				// [営業所計画].[計画値P(Y)] ＞ ０ ＡＮＤ [試算パラメータ(X)].[指数(未獲得市場)] ＞ ０ ＡＮＤ <未獲得市場P母数> ＝ ０
				if (mikakutokuSumP == 0 && officePlanP > 0) {
					messageList.add(messageSource.getMessage("DPS2024W", prodName, Locale.getDefault()));
				}
			}

			// -----------------------------------------------------
			// 指数(フリー項目①～③) ＞ 0の場合、フリー項目チェック
			// -----------------------------------------------------
			if (estimationParam.getIndexFree1() > 0 || estimationParam.getIndexFree2() > 0 || estimationParam.getIndexFree3() > 0) {
				long free1UH = 0;
				long free2UH = 0;
				long free3UH = 0;
				long free1P = 0;
				long free2P = 0;
				long free3P = 0;
				try {
					List<MrPlanFreeIndex> freeIndexList = mrPlanFreeIndexDao.searchListBySosCd(sosCd, null, prodCode);
					for (MrPlanFreeIndex freeIndex : freeIndexList) {
						if (freeIndex.getIndexFreeValueUh1() != null) {
							free1UH = free1UH + freeIndex.getIndexFreeValueUh1();
						}
						if (freeIndex.getIndexFreeValueUh2() != null) {
							free2UH = free2UH + freeIndex.getIndexFreeValueUh2();
						}
						if (freeIndex.getIndexFreeValueUh3() != null) {
							free3UH = free3UH + freeIndex.getIndexFreeValueUh3();
						}
						if (freeIndex.getIndexFreeValueP1() != null) {
							free1P = free1P + freeIndex.getIndexFreeValueP1();
						}
						if (freeIndex.getIndexFreeValueP2() != null) {
							free2P = free2P + freeIndex.getIndexFreeValueP2();
						}
						if (freeIndex.getIndexFreeValueP3() != null) {
							free3P = free3P + freeIndex.getIndexFreeValueP3();
						}
					}
				} catch (DataNotFoundException e) {
					// エラーにしない
				}
				if (estimationParam.getIndexFree1() > 0) {
					// [営業所計画].[計画値UH(Y)] ＞ ０ ＡＮＤ [試算パラメータ(X)].[指数(フリー項目①)] ＞ ０ ＡＮＤ <フリー項目①UH母数> ＝ ０
					if (free1UH == 0 && officePlanUH > 0) {
						messageList.add(messageSource.getMessage("DPS2025W", prodName, Locale.getDefault()));
					}
					// [営業所計画].[計画値P(Y)]  ＞ ０ ＡＮＤ [試算パラメータ(X)].[指数(フリー項目①)] ＞ ０ ＡＮＤ <フリー項目①P母数>  ＝ ０
					if (free1P == 0 && officePlanP > 0) {
						messageList.add(messageSource.getMessage("DPS2026W", prodName, Locale.getDefault()));
					}
				}
				if (estimationParam.getIndexFree2() > 0) {
					// [営業所計画].[計画値UH(Y)] ＞ ０ ＡＮＤ [試算パラメータ(X)].[指数(フリー項目②)] ＞ ０ ＡＮＤ <フリー項目②UH母数> ＝ ０
					if (free2UH == 0 && officePlanUH > 0) {
						messageList.add(messageSource.getMessage("DPS2027W", prodName, Locale.getDefault()));
					}
					// [営業所計画].[計画値P(Y)]  ＞ ０ ＡＮＤ [試算パラメータ(X)].[指数(フリー項目②)] ＞ ０ ＡＮＤ <フリー項目②P母数>  ＝ ０
					if (free2P == 0 && officePlanP > 0) {
						messageList.add(messageSource.getMessage("DPS2028W", prodName, Locale.getDefault()));
					}
				}
				if (estimationParam.getIndexFree3() > 0) {
					// [営業所計画].[計画値UH(Y)] ＞ ０ ＡＮＤ [試算パラメータ(X)].[指数(フリー項目③)] ＞ ０ ＡＮＤ <フリー項目③UH母数> ＝ ０
					if (free3UH == 0 && officePlanUH > 0) {
						messageList.add(messageSource.getMessage("DPS2029W", prodName, Locale.getDefault()));
					}
					// [営業所計画].[計画値P(Y)]  ＞ ０ ＡＮＤ [試算パラメータ(X)].[指数(フリー項目③)] ＞ ０ ＡＮＤ <フリー項目③P母数>  ＝ ０
					if (free3P == 0 && officePlanP > 0) {
						messageList.add(messageSource.getMessage("DPS2030W", prodName, Locale.getDefault()));
					}
				}
			}

		}

		return messageList;
	}

	// 試算パラメータ取得
	public EstimationParamResultDto searchEstimationParam(String sosCd, String prodCode) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 計画対象品目取得
		// ----------------------
		PlannedProd plannedProd = null;
		try {
			plannedProd = plannedProdDAO.search(prodCode);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目が存在しない：";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
		}

		// ----------------------
		// （本部案）試算パラメータ取得
		// ----------------------
		EstParamHonbu estParamHonbu;
		try {
			estParamHonbu = estParamHonbuDao.search(prodCode);

		} catch (DataNotFoundException e) {
			final String errMsg = "試算パラメータ（本部案）が存在しない：";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
		}

		// ----------------------
		// （営業所案）試算パラメータ取得
		// ----------------------
		EstParamOffice estParamOffice = null;
		try {
			estParamOffice = estParamOfficeDao.search(sosCd, prodCode);

		} catch (DataNotFoundException e) {

			// 営業所案が存在しない場合は、本部案を営業所案に設定する
			estParamOffice = new EstParamOffice();
			estParamOffice.setSosCd(sosCd);
			estParamOffice.setProdCode(prodCode);
			estParamOffice.setBaseParam(estParamHonbu.getBaseParam());
			estParamOffice.setEstimationParam(estParamHonbu.getEstimationParam());
		}

		// ----------------------
		// 結果DTO作成
		// ----------------------
		return new EstimationParamResultDto(plannedProd, estParamHonbu, estParamOffice);
	}

	// 営業所案登録事前チェック
	public List<String> paramUpdateCheck(EstimationParamUpdateDto estimationParamUpdateDto){

		// ----------------------
		// 引数チェック
		// ----------------------
		if (estimationParamUpdateDto == null) {
			final String errMsg = "試算パラメータ更新用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (estimationParamUpdateDto.getEstParamOffice() == null) {
			final String errMsg = "試算パラメータ　営業所案がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (estimationParamUpdateDto.getEstParamOffice().getSosCd() == null) {
			final String errMsg = "試算パラメータ　営業所案がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (estimationParamUpdateDto.getEstParamOffice().getProdCode() == null) {
			final String errMsg = "試算パラメータ　品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		List<String> messageList = new ArrayList<String>();

		// 登録内容取得
		EstParamOffice estParamOffice = estimationParamUpdateDto.getEstParamOffice();
		EstimationParam estimationParam = estParamOffice.getEstimationParam();
		String sosCd = estParamOffice.getSosCd();
		String prodCode = estParamOffice.getProdCode();

		// -------------------------------------------
		// 指数(未獲得市場) ＞ 0の場合、未獲得市場チェック
		// -------------------------------------------
		if (estimationParam.getIndexMikakutoku() > 0) {
			long mikakutokuSumAll = 0;
			try {
				List<MikakutokuSijouInsTypeSummary> mikakutokuSumList = mikakutokuSijouInsTypeSummaryDao.searchSummaryList(sosCd, prodCode);
				for (MikakutokuSijouInsTypeSummary mikakutokuSum : mikakutokuSumList) {
					long distPlanMikakutoku = 0;
					if (mikakutokuSum.getDistPlanMikakutoku() != null) {
						distPlanMikakutoku = mikakutokuSum.getDistPlanMikakutoku();
					}
					mikakutokuSumAll = mikakutokuSumAll + distPlanMikakutoku;
				}
			} catch (DataNotFoundException e) {
				// エラーにしない
			}
			// 未獲得市場UHP ＝ ０
			if (mikakutokuSumAll == 0) {
				messageList.add(messageSource.getMessage("DPS3249E", new String[]{"未獲得市場"}, Locale.getDefault()));
			}
		}

		// -----------------------------------------------------
		// 指数(フリー項目①～③) ＞ 0の場合、フリー項目チェック
		// -----------------------------------------------------
		if (estimationParam.getIndexFree1() > 0 || estimationParam.getIndexFree2() > 0 || estimationParam.getIndexFree3() > 0) {

			Long free1 = null;
			Long free2 = null;
			Long free3 = null;
			try {
				Map<String, Object> result = mrPlanFreeIndexDao.sumFreeIndexByProdSos(sosCd, prodCode);
				Long index_free_value_uh_1 = (Long)result.get("indexFreeValueUh1");
				Long index_free_value_uh_2 = (Long)result.get("indexFreeValueUh2");
				Long index_free_value_uh_3 = (Long)result.get("indexFreeValueUh3");
				Long index_free_value_p_1 = (Long)result.get("indexFreeValueP1");
				Long index_free_value_p_2 = (Long)result.get("indexFreeValueP2");
				Long index_free_value_p_3 = (Long)result.get("indexFreeValueP3");
				free1 = MathUtil.add(index_free_value_uh_1, index_free_value_p_1);
				free2 = MathUtil.add(index_free_value_uh_2, index_free_value_p_2);
				free3 = MathUtil.add(index_free_value_uh_3, index_free_value_p_3);

			} catch (DataNotFoundException e) {
				// エラーにしない
			}

			// 指数(フリー項目①) ＞ ０ ＡＮＤ  ( <フリー項目①UHP母数> ＝ NULL ＯＲ <フリー項目①UHP母数> ＝ ０)
			if (estimationParam.getIndexFree1() > 0 && (free1 == null || free1 == 0)) {
				String indexFreeName = estParamOffice.getEstimationParam().getIndexFreeName1();
				if(StringUtils.isEmpty(estParamOffice.getEstimationParam().getIndexFreeName1())){
					indexFreeName = EstimationParam.DEFAULT_INDEX_FREE_NAME1;
				}
				messageList.add(messageSource.getMessage("DPS3249E", new String[]{ indexFreeName }, Locale.getDefault()));
			}
			// 指数(フリー項目②) ＞ ０ ＡＮＤ  ( <フリー項目②UHP母数> ＝ NULL ＯＲ <フリー項目②UHP母数> ＝ ０)
			if (estimationParam.getIndexFree2() > 0 && (free2 == null || free2 == 0)) {
				String indexFreeName = estParamOffice.getEstimationParam().getIndexFreeName2();
				if(StringUtils.isEmpty(estParamOffice.getEstimationParam().getIndexFreeName2())){
					indexFreeName = EstimationParam.DEFAULT_INDEX_FREE_NAME2;
				}
				messageList.add(messageSource.getMessage("DPS3249E", new String[]{ indexFreeName }, Locale.getDefault()));
			}
			// 指数(フリー項目③) ＞ ０ ＡＮＤ  ( <フリー項目③UHP母数> ＝ NULL ＯＲ <フリー項目③UHP母数> ＝ ０)
			if (estimationParam.getIndexFree3() > 0 && (free3 == null || free3 == 0)) {
				String indexFreeName = estParamOffice.getEstimationParam().getIndexFreeName3();
				if(StringUtils.isEmpty(estParamOffice.getEstimationParam().getIndexFreeName3())){
					indexFreeName = EstimationParam.DEFAULT_INDEX_FREE_NAME3;
				}
				messageList.add(messageSource.getMessage("DPS3249E",  new String[]{ indexFreeName }, Locale.getDefault()));
			}
		}

		return messageList;
	}

	// フリー項目取得
	public FreeIndexResultDto searchFreeIndex(String sosCd, String prodCode) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 計画対象品目取得
		// ----------------------
		PlannedProd plannedProd = null;
		try {
			plannedProd = plannedProdDAO.search(prodCode);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目が存在しない：";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
		}

		// ----------------------
		// 営業所情報取得
		// ----------------------
		SosMst officeSosMst = null;
		try {
			officeSosMst = sosMstDAO.search(sosCd);
		} catch (DataNotFoundException e) {
			final String errMsg = "営業所組織が存在しない：";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + sosCd), e);
		}

		// ----------------------
		// 試算パラメータ取得
		// ----------------------
		String refProdCode = null;
		ProdInfo refProdInfo = null;
		EstimationParam estimationParam = null;
		try {
			// 試算パラメータ（営業所案）取得
			EstParamOffice estParamOffice = estParamOfficeDao.search(sosCd, prodCode);
			refProdCode = estParamOffice.getBaseParam().getRefProdCode();
			refProdInfo = estParamOffice.getBaseParam().getRefProdInfo();
			estimationParam = estParamOffice.getEstimationParam();

		} catch (DataNotFoundException e) {

			// 営業所案が存在しない場合は、試算パラメータ（本部案）を取得
			try {
				EstParamHonbu estParamHonbu = estParamHonbuDao.search(prodCode);
				refProdCode = estParamHonbu.getBaseParam().getRefProdCode();
				refProdInfo = estParamHonbu.getBaseParam().getRefProdInfo();
				estimationParam = estParamHonbu.getEstimationParam();

			} catch (DataNotFoundException e1) {
				final String errMsg = "試算パラメータ(本部案、営業所案)が存在しない：";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e1);
			}
		}

		// フリー項目名が設定されていない場合は、デフォルト値を使用する
		if (StringUtils.isEmpty(estimationParam.getIndexFreeName1())) {
			estimationParam.setIndexFreeName1(EstimationParam.DEFAULT_INDEX_FREE_NAME1);
		}
		if (StringUtils.isEmpty(estimationParam.getIndexFreeName2())) {
			estimationParam.setIndexFreeName2(EstimationParam.DEFAULT_INDEX_FREE_NAME2);
		}
		if (StringUtils.isEmpty(estimationParam.getIndexFreeName3())) {
			estimationParam.setIndexFreeName3(EstimationParam.DEFAULT_INDEX_FREE_NAME3);
		}

		// 立案対象品目と試算品目が同じ場合は、試算品目を設定しない
		if (prodCode.equals(refProdCode)) {
			refProdInfo = null;
		}

		// ----------------------
		// チーム一覧取得
		// ----------------------
		List<SosMst> sosMstList = null;
		try {
			sosMstList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G, sosCd);
		} catch (DataNotFoundException e) {

			// チームがない場合は、営業所組織を設定
			sosMstList = new ArrayList<SosMst>();
			sosMstList.add(officeSosMst);
		}

		// ----------------------
		// フリー項目取得
		// ----------------------
		// チーム単位に繰り返し
		List<FreeIndexResultDetailDto> detailDtoList = new ArrayList<FreeIndexResultDetailDto>();
		for (SosMst sosMst : sosMstList) {
			try {

				// 対象組織がチームの場合は、チームコード設定
				String sosCd4 = null;
				if(sosMst.getBumonRank() == BumonRank.TEAM){
					sosCd4 = sosMst.getSosCd();
				}

				// フリー項目取得
				List<MrPlanFreeIndex> mrPlanFreeIndexList = mrPlanFreeIndexDao.searchListBySosCd(sosCd, sosCd4, prodCode);

				// MrPlanFreeIndexモデルから、DTOへ変換
				List<FreeIndexDto> freeIndexDtoList = new ArrayList<FreeIndexDto>();
				for (MrPlanFreeIndex mrPlanFreeIndex : mrPlanFreeIndexList) {
					freeIndexDtoList.add(new FreeIndexDto(mrPlanFreeIndex));
				}

				FreeIndexResultDetailDto detailDto = new FreeIndexResultDetailDto(sosMst.getBumonSeiName(), freeIndexDtoList);
				detailDtoList.add(detailDto);

			} catch (DataNotFoundException e) {
				// 従業員のいないチームがあってもエラーとしない
			}
		}

		// 最終更新日取得
		Date lastUpdate = null;
		String lastUpdateUser = null;
		try {
			MrPlanFreeIndex lastUpdateData = mrPlanFreeIndexDao.getLastUpDate(sosCd, prodCode);
			lastUpdate = lastUpdateData.getUpDate();
			lastUpdateUser = lastUpdateData.getUpJgiName();
		} catch (DataNotFoundException e) {
			// エラーにしない
		}

		// ----------------------
		// 検索結果DTO作成
		// ----------------------
		String prodName = plannedProd.getProdName();
		String prodType = plannedProd.getProdType();
		return new FreeIndexResultDto(prodCode, prodName, prodType, refProdInfo, estimationParam, lastUpdate, lastUpdateUser, detailDtoList);
	}

	// 試算実行前の担当者別計画立案ステータスを取得
	public List<EstimationExecOrgDto> searchEstimationPreparation(String sosCd, List<EstimationExecDto> estimationExecDtoList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (estimationExecDtoList == null || estimationExecDtoList.size() == 0) {
			final String errMsg = "試算実行DTOがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		for (EstimationExecDto estimationExecDto : estimationExecDtoList) {
			if (estimationExecDto.getProdCode() == null) {
				final String errMsg = "試算実行対象の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (estimationExecDto.getProdName() == null) {
				final String errMsg = "試算実行対象の品目名称がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}

		// 担当者別計画ステータス取得
		List<EstimationExecOrgDto> estimationExecOrgDtoList = new ArrayList<EstimationExecOrgDto>();
		for (EstimationExecDto estimationExecDto : estimationExecDtoList) {
			MrPlanStatus mrPlanStatus = new MrPlanStatus();
			try {
				mrPlanStatus = mrPlanStatusDao.search(sosCd, estimationExecDto.getProdCode());
			} catch (DataNotFoundException e) {
				mrPlanStatus.setSosCd(sosCd);
				mrPlanStatus.setProdCode(estimationExecDto.getProdCode());
			}
			// 更新日を書換
			mrPlanStatus.setUpDate(estimationExecDto.getUpDate());
			EstimationExecOrgDto execOrgDto = new EstimationExecOrgDto(estimationExecDto.getProdCode(), estimationExecDto.getProdName(), mrPlanStatus);
			estimationExecOrgDtoList.add(execOrgDto);
		}

		return estimationExecOrgDtoList;
	}
}
