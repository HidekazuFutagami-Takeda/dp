package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.dao.DistParamHonbuDao;
import jp.co.takeda.dao.DistParamOfficeDao;
import jp.co.takeda.dao.DistributionElementDao;
import jp.co.takeda.dao.InsWsPlanDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.MrPlanDao;
import jp.co.takeda.dao.OfficePlanDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.SpecialInsPlanDao;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.DistributionLogicDto;
import jp.co.takeda.dto.DistributionMissDto;
import jp.co.takeda.dto.InsWsPlanDto;
import jp.co.takeda.dto.InsWsPlanImpProdSummaryDto;
import jp.co.takeda.logic.DistributionByIncrementLogic;
import jp.co.takeda.logic.DistributionByRateLogic;
import jp.co.takeda.logic.div.InsWsDistType;
import jp.co.takeda.model.BaseParam;
import jp.co.takeda.model.DistParam;
import jp.co.takeda.model.DistParamHonbu;
import jp.co.takeda.model.DistParamOffice;
import jp.co.takeda.model.DpsPlannedCtg;
import jp.co.takeda.model.InsWsPlan;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.model.OfficePlan;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.SpecialInsPlan;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.DistributionType;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.PlanType;
import jp.co.takeda.model.view.DistributionElement;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.util.MathUtil;

/**
 * 施設特約店別計画・配分実行サービス実装クラス
 *
 * @author khashimoto
 */
@Transactional
@Service("dpsDistributionExecuteService")
public class DpsDistributionExecuteServiceImpl implements DpsDistributionExecuteService {

	/**
	 * ロガー
	 */
	private static final Log LOG = LogFactory.getLog(DpsDistributionExecuteServiceImpl.class);

	/**
	 * 共通DAO
	 */
	@Autowired(required = true)
	@Qualifier("commonDao")
	protected CommonDao commonDao;

	/**
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * 従業員情報DAO
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
	 * 配分パラメータ（本部）DAO
	 */
	@Autowired(required = true)
	@Qualifier("distParamHonbuDao")
	protected DistParamHonbuDao distParamHonbuDao;

	/**
	 * 配分パラメータ（営業所）DAO
	 */
	@Autowired(required = true)
	@Qualifier("distParamOfficeDao")
	protected DistParamOfficeDao distParamOfficeDao;

	/**
	 * 営業所計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("officePlanDao")
	protected OfficePlanDao officePlanDao;

	/**
	 * 担当者別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("mrPlanDao")
	protected MrPlanDao mrPlanDao;

	/**
	 * 特定施設個別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("specialInsPlanDao")
	protected SpecialInsPlanDao specialInsPlanDao;

	/**
	 * 配分要素取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("distributionElementDao")
	protected DistributionElementDao distributionElementDao;

	/**
	 * 施設特約店別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanDao")
	protected InsWsPlanDao insWsPlanDao;

	/**
	 * 汎用マスタ検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	/**
	 * 計画対象カテゴリ領域の検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsPlannedCtgSearchService")
	protected DpsPlannedCtgSearchService dpsPlannedCtgSearchService;

	// 営業所一括配分
	public List<DistributionMissDto> execute(String sosCd, String prodCode, String category, DpUser execDpUser) {

		// ----------------------
		// 引数チェック
		// ----------------------
		// ワクチンの場合、組織未選択時は全社対象とする
//		if (sosCd == null) {
//			final String errMsg = "組織コードがnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (execDpUser == null) {
			final String errMsg = "実行者情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 配分ミス情報
		List<DistributionMissDto> distMissList = new ArrayList<DistributionMissDto>();

		// 施設特約店別計画削除
		if (sosCd == null) {
			insWsPlanDao.deleteByProdCd(prodCode);
		}else {
			insWsPlanDao.deleteBySos(sosCd, prodCode);
		}

		// 対象毎に処理を実行
		List<DistributionMissDto> tmpDistMissList = executePre(sosCd, null, prodCode, category, execDpUser, InsType.UH);
		if (tmpDistMissList.size() != 0) {
			distMissList.addAll(tmpDistMissList);
		}
			tmpDistMissList = executePre(sosCd, null, prodCode, category, execDpUser, InsType.P);
		if (tmpDistMissList.size() != 0) {
			distMissList.addAll(tmpDistMissList);
		}

		return distMissList;
	}

	// 再配分処理
	public List<DistributionMissDto> executeAgain(String sosCd, Integer jgiNo, String prodCode, String category, DpUser execDpUser) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (execDpUser == null) {
			final String errMsg = "実行者情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 配分ミス情報
		List<DistributionMissDto> distMissList = new ArrayList<DistributionMissDto>();

		// 施設特約店別計画削除
		insWsPlanDao.deleteByJgi(jgiNo, prodCode);

		// 対象毎に処理を実行
		List<DistributionMissDto> tmpDistMissList = executePre(sosCd, jgiNo, prodCode, category, execDpUser, InsType.UH);
		if (tmpDistMissList.size() != 0) {
			distMissList.addAll(tmpDistMissList);
		}
		tmpDistMissList = executePre(sosCd, jgiNo, prodCode, category, execDpUser, InsType.P);
		if (tmpDistMissList.size() != 0) {
			distMissList.addAll(tmpDistMissList);
		}

		return distMissList;
	}

	/**
	 * 配分処理の実行前処理を行い、配分処理を実行する。
	 *
	 * @param sosCd 組織コード(営業所)
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param category 品目カテゴリ
	 * @param execDpUser 実行者情報
	 * @param insType 施設出力対象区分
	 * @return 配分ミス情報
	 */
	protected List<DistributionMissDto> executePre(String sosCd, Integer jgiNo, String prodCode, String category, DpUser execDpUser, InsType insType) {

		// 配分ミス情報
		List<DistributionMissDto> distMissList = new ArrayList<DistributionMissDto>();

		// ----------------------
		// 配分基準取得
		// ----------------------
		DistParam distParam = null;
		BaseParam baseParam = null;
		if (sosCd != null) {
			try {
				DistParamOffice distParamOffice = distParamOfficeDao.search(sosCd, prodCode, insType, DistributionType.INS_WS_PLAN);
				distParam = distParamOffice.getDistParam();
				baseParam = distParamOffice.getBaseParam();
			} catch (DataNotFoundException e) {
				// 営業所案が無くてもエラーとしない
			}
		}
		if (distParam == null) {
			try {
				DistParamHonbu distParamHonbu = distParamHonbuDao.search(prodCode, insType, DistributionType.INS_WS_PLAN);
				distParam = distParamHonbu.getDistParam();
				baseParam = distParamHonbu.getBaseParam();
			} catch (DataNotFoundException e2) {
				final String errMsg = "配分基準(本部)が存在しない";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e2);
			}
		}

		// 計画立案レベル・担当者によって処理を分岐
		DistributionMissDto dto = null;
		SosMst sosMst = null;
		List<JgiMst> jgiList = new ArrayList<JgiMst>();
		//switch (category) {
		//	case SHIIRE:
		DpsPlannedCtg plannedCtg = dpsPlannedCtgSearchService.searchPlannedCtg(category);
		//if (dpsCodeMasterSearchService.isSiire(category)) {
		if (plannedCtg.getPlanLevelMr().equals("0")) {
			if (jgiNo != null) {
				// 営業所計画からの再配分
				dto = execute(sosCd, jgiNo, prodCode, execDpUser, insType, InsWsDistType.OFFICE_PLAN_RE_DIST, distParam, baseParam, category);

				if (dto != null)
					distMissList.add(dto);
			} else {
				// 営業所計画からの一括配分
				dto = execute(sosCd, null, prodCode, execDpUser, insType, InsWsDistType.OFFICE_PLAN_DIST, distParam, baseParam, category);

			}
			if (dto != null)
				distMissList.add(dto);
		}
		else {
//				// 品目情報取得
//				PlannedProd plannedProd;
//				try {
//					plannedProd = plannedProdDAO.search(prodCode);
//				} catch (DataNotFoundException e) {
//					final String errMsg = "品目情報取得に失敗";
//					throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
//				}

				if (jgiNo != null) {

					// 担当者指定の再配分の場合
					//if (plannedProd.getPlanLevelInsDoc()) {
						//重点品目の場合、施設別計画から配分を行う。
						//重点品目の判断は以下の通り。
						//  計画立案レベル・施設医師がTrue
						//  カテゴリがMMP品
						//  売上所属が医薬
						//  計画対象フラグがTrue

						// 施設別計画からの配分
					//	dto = executePriority(sosCd, jgiNo, prodCode, execDpUser, insType, distParam, baseParam);
					//} else {

						// 担当者別計画からの再配分
						dto = execute(sosCd, jgiNo, prodCode, execDpUser, insType, InsWsDistType.MR_PLAN_RE_DIST, distParam, baseParam, category);
					//}
					if (dto != null)
						distMissList.add(dto);

				} else {

					// 組織指定の一括配分の場合、担当者ごとに配分実行する。
					try {
						if (sosCd == null) {
							jgiList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, SosMst.SOS_CD1, SosListType.TOKUYAKUTEN_LIST, BumonRank.IEIHON);
						} else {
							sosMst = sosMstDAO.search(sosCd);
							jgiList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd, SosListType.SHITEN_LIST, sosMst.getBumonRank());
						}
					} catch (DataNotFoundException e) {
						final String errMsg = "組織・従業員情報取得に失敗";
						throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
					}

					for (JgiMst jgiMst : jgiList) {
						//if (plannedProd.getPlanLevelInsDoc()) {
							//重点品目の場合、施設別計画から配分を行う。
							//重点品目の判断は以下の通り。
							//  計画立案レベル・施設医師がTrue
							//  カテゴリがMMP品
							//  売上所属が医薬
							//  計画対象フラグがTrue

							// 施設別計画からの配分
						//	dto = executePriority(sosMst.getSosCd(), jgiMst.getJgiNo(), prodCode, execDpUser, insType, distParam, baseParam);
						//} else {
							dto = execute(sosCd, jgiMst.getJgiNo(), prodCode, execDpUser, insType, InsWsDistType.MR_PLAN_DIST, distParam, baseParam, category);
						//}
						if (dto != null)
							distMissList.add(dto);
					}
				}
		}
		return distMissList;
	}

	/**
	 * 配分処理を実行する。
	 *
	 * @param sosCd 組織コード(営業所)
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param execDpUser 実行者情報
	 * @param insType 施設出力対象区分
	 * @param distType 配分種類
	 * @param distParam 配分パラメータ
	 * @param baseParam 試算配分共通パラメータ
	 * @param category カテゴリ
	 * @return 配分ミス情報
	 */
	protected DistributionMissDto execute(String sosCd, Integer jgiNo, String prodCode, DpUser execDpUser, InsType insType, InsWsDistType distType, DistParam distParam,
		BaseParam baseParam, String category) {

		StopWatch stopWatch = new StopWatch();
		StopWatch stopWatch1 = new StopWatch();
		StringBuffer debugMsg = new StringBuffer();
		stopWatch1.start();
		stopWatch.start();

		// ----------------------------------------
		// 配分母数の取得
		// ----------------------------------------
		Long paramValue = getParamValue(sosCd, jgiNo, prodCode, insType, distType);

		// 特定施設個別計画の取得
		List<SpecialInsPlan> specialInsPlanList = getSpecialInsPlanList(sosCd, jgiNo, prodCode, insType, distType, dpsCodeMasterSearchService.isVaccine(category), category);

		// 特定施設個別計画のサマリー算出
		Long specialSum = 0L;
		for (SpecialInsPlan specialInsPlan : specialInsPlanList) {
			specialSum = MathUtil.add(specialSum, specialInsPlan.getPlannedValueY());
		}

		stopWatch.stop();
		debugMsg.append(sosCd + "," + jgiNo+","+prodCode+",");

		debugMsg.append(getLogMsg("2-1", stopWatch));
		stopWatch.reset();
		stopWatch.start();

		// ------------------------------
		// 配分品目の実績取得
		// ------------------------------
		// 配分基準で指定されている参照期間で集計する
		List<DistributionElement> elementList = new ArrayList<DistributionElement>();
		try {
			elementList = distributionElementDao.searchList(sosCd, jgiNo, prodCode, insType, baseParam.getRefProdCode(), baseParam.getRefFrom(), baseParam.getRefTo(), dpsCodeMasterSearchService.isVaccine(category), category);
		} catch (DataNotFoundException e) {
			// 配分要素が無い場合もエラーにしない
		}

		stopWatch.stop();
		debugMsg.append(getLogMsg("2-2", stopWatch));
		stopWatch.reset();
		stopWatch.start();

		// ------------------------------
		// 計画立案品目の実績取得
		// ------------------------------
		List<DistributionElement> prodElementList = new ArrayList<DistributionElement>();
		try {
			prodElementList = distributionElementDao.searchList(sosCd, jgiNo, prodCode, insType, prodCode, baseParam.getRefFrom(), baseParam.getRefTo(), dpsCodeMasterSearchService.isVaccine(category), category);
		} catch (DataNotFoundException e) {
			// 配分要素が無い場合もエラーにしない
		}

		stopWatch.stop();
		debugMsg.append(getLogMsg("2-3", stopWatch));
		stopWatch.reset();
		stopWatch.start();

		// ----------------------------------------
		// 配分実行
		// ----------------------------------------
		DistributionLogicDto distributionLogicDto = null;
		switch (distType) {

			case OFFICE_PLAN_DIST:

				// 配分母数が営業所計画の場合、構成比配分
				DistributionByRateLogic distLogicByRate = new DistributionByRateLogic(elementList, distParam, sosCd, jgiNo, prodCode, insType, paramValue, specialSum, distType);
				distributionLogicDto = distLogicByRate.execute();
				break;

			case OFFICE_PLAN_RE_DIST:
			case MR_PLAN_DIST:
			case MR_PLAN_RE_DIST:
			case MR_PLAN_DIST_FOR_VAC:

				// 配分母数が担当者別計画の場合、増分値配分
				DistributionByIncrementLogic distLogicByInc = new DistributionByIncrementLogic(elementList, baseParam, distParam, sosCd, jgiNo, prodCode, insType, paramValue,
					specialSum, distType, prodElementList);
				distributionLogicDto = distLogicByInc.execute();
				break;
		}

		// 担当者別計画作成MAP
		Map<Integer, MrPlan> mrSumMap = new LinkedHashMap<Integer, MrPlan>();

		stopWatch.stop();
		debugMsg.append(getLogMsg("2-4", stopWatch));
		stopWatch.reset();
		stopWatch.start();

		// ------------------------------
		// 施設特約店別計画登録
		// ------------------------------

		// 施設特約店別計画
		List<InsWsPlanDto> insWsPlanList = distributionLogicDto.getInsWsPlanDtoList();

		// 担当者別サマリーMAP
		for (InsWsPlanDto dto : insWsPlanList) {
			InsWsPlan insWsPlan = new InsWsPlan();
			insWsPlan.setProdCode(prodCode);
			insWsPlan.setSpecialInsPlanFlg(false);
			insWsPlan.setExpectDistInsFlg(dto.getExceptDistInsFlg());
			insWsPlan.setDelInsFlg(dto.getDelInsFlg());
			insWsPlan.setJgiNo(dto.getJgiNo());
			insWsPlan.setInsNo(dto.getInsNo());
			insWsPlan.setTmsTytenCd(dto.getTmsTytenCd());
			insWsPlan.setDistValueY(dto.getDistValue());

			insertInsWsPlan(insWsPlan);

			// 営業所一括配分の場合、担当者別計画の算出
			createMrPlan(distType, mrSumMap, insWsPlan, insType);
		}

		stopWatch.stop();
		debugMsg.append(getLogMsg("2-5", stopWatch));
		stopWatch.reset();
		stopWatch.start();

		// ------------------------------
		// 施設特約店別計画登録(特定施設)
		// ------------------------------
		for (SpecialInsPlan specialInsPlan : specialInsPlanList) {
			InsWsPlan insWsPlan = new InsWsPlan();
			insWsPlan.setProdCode(prodCode);
			insWsPlan.setSpecialInsPlanFlg(true);
			insWsPlan.setExpectDistInsFlg(false);
			insWsPlan.setDelInsFlg(false);
			insWsPlan.setJgiNo(specialInsPlan.getJgiNo());
			insWsPlan.setInsNo(specialInsPlan.getInsNo());
			insWsPlan.setTmsTytenCd(specialInsPlan.getTmsTytenCd());
			insWsPlan.setDistValueY(specialInsPlan.getPlannedValueY());
			insertInsWsPlan(insWsPlan);

			// 営業所一括配分の場合、担当者別計画の算出
			createMrPlan(distType, mrSumMap, insWsPlan, insType);
		}

		stopWatch.stop();
		debugMsg.append(getLogMsg("2-6", stopWatch));
		stopWatch.reset();
		stopWatch.start();

		// ------------------------------------
		// 営業所一括配分の場合、担当者別計画更新
		// ------------------------------------
		if (distType.equals(InsWsDistType.OFFICE_PLAN_DIST)) {
			// 担当者別計画を初期化
			mrPlanDao.updateByDist(sosCd, prodCode, insType, execDpUser);

			for (MrPlan mrPlan : mrSumMap.values()) {
				mrPlanDao.executeByDist(mrPlan, insType, execDpUser);
			}
		}

		stopWatch.stop();
		debugMsg.append(getLogMsg("2-7", stopWatch));
		stopWatch1.stop();
		debugMsg.append(stopWatch1.getTime());

		infoLogging(debugMsg.toString());

		return distributionLogicDto.getDistributionMissDto();
	}

	/**
	 * 配分処理（重点品目）を実行する。
	 *
	 * @param sosCd 組織コード(営業所)
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param execDpUser 実行者情報
	 * @param insType 施設出力対象区分
	 * @param distParam 配分パラメータ
	 * @param baseParam 試算配分共通パラメータ
	 * @return 配分ミス情報
	 */
	protected DistributionMissDto executePriority(
			String sosCd,
			Integer jgiNo,
			String prodCode,
			DpUser execDpUser,
			InsType insType,
			DistParam distParam,
			BaseParam baseParam) {

		//パラメータチェック
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (execDpUser == null) {
			final String errMsg = "実行者情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		} else if (execDpUser.getJgiNo() == null) {
			final String errMsg = "実行者情報：実行者従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		} else if (execDpUser.getJgiName() == null) {
			final String errMsg = "実行者情報：実行者従業員氏名がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (distParam == null) {
			final String errMsg = "配分パラメータがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		} else if (distParam.getZeroDistFlg() == null) {
			final String errMsg = "配分パラメータ：ゼロ配分フラグがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (baseParam == null) {
			final String errMsg = "試算配分共通パラメータがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		} else if (baseParam.getRefFrom() == null) {
			final String errMsg = "試算配分共通パラメータ：参照期間FROMがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		} else if (baseParam.getRefTo() == null) {
			final String errMsg = "試算配分共通パラメータ：参照期間TOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		} else if (baseParam.getRefProdCode() == null) {
			final String errMsg = "試算配分共通パラメータ：配分品目コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insType == null) {
			final String errMsg = "施設出力対象区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

StopWatch stopWatch = new StopWatch();
StopWatch stopWatch1 = new StopWatch();
StringBuffer debugMsg = new StringBuffer();
stopWatch1.start();
stopWatch.start();

		// ----------------------------------------
		// 配分実行
		// ----------------------------------------
		Map<String, List<InsWsPlanImpProdSummaryDto>> dtoList = new HashMap<String, List<InsWsPlanImpProdSummaryDto>>();
		try {
			//配分値を取得する
			dtoList = insWsPlanDao.selectImportantHbn(
					jgiNo,
					prodCode,
					baseParam.getRefFrom().getDbValue(),
					baseParam.getRefTo().getDbValue(),
					baseParam.getRefProdCode(),
					insType.getDbValue(),
					distParam.getZeroDistFlg() == true ? "1" : "0",
					execDpUser.getJgiNo(),
					execDpUser.getJgiName());
		} catch (DataNotFoundException e) {
			//配分対象が存在しなくてもエラーとはしない
			dtoList = new HashMap<String, List<InsWsPlanImpProdSummaryDto>>();

		} catch (Exception e) {
			LOG.error("jgiNo:" + jgiNo + " prodCode:" + prodCode);
			final String errMsg = "配分処理（重点品目）に失敗";
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsg), e);
		}

stopWatch.stop();
debugMsg.append(sosCd + "," + jgiNo+","+prodCode+",");

debugMsg.append(getLogMsg("1-1", stopWatch));
stopWatch.reset();
stopWatch.start();

		try {
			//配分値合計を取得（親施設コード単位）

			Set<String> keySet = dtoList.keySet();
			for (String key : keySet) {
				double sumDistValY = 0.0;
				long sumRoundDistValY = 0L;
				List<InsWsPlanImpProdSummaryDto> entityList = dtoList.get(key);
				for (InsWsPlanImpProdSummaryDto dto : entityList) {
					sumDistValY += dto.getDistValueY();
					sumRoundDistValY += dto.getDistValueYRound();
				}

				//丸めた結果余った分を配分値の高いものに加算する。
				//丸め前後の金額の差分を最も配分値の高い施設特約店の配分値へ合算する
				long divDistValueY = new BigDecimal((sumDistValY - sumRoundDistValY)).setScale(-4, BigDecimal.ROUND_HALF_UP).longValue();
				if (LOG.isDebugEnabled()) {
					LOG.debug("【丸め誤差】divDistValueY:" + divDistValueY);
				}

				InsWsPlanImpProdSummaryDto dto = entityList.get(0);
				dto.setDistValueYRound(dto.getDistValueYRound() + divDistValueY);
				dto.setPlannedValueYRound(dto.getPlannedValueYRound() + divDistValueY);
			}

stopWatch.stop();
debugMsg.append(getLogMsg("1-2", stopWatch));
stopWatch.reset();
stopWatch.start();

			//施設特約店別計画にデータを登録する
			for (String key : keySet) {
				List<InsWsPlanImpProdSummaryDto> entityList = dtoList.get(key);
				for (InsWsPlanImpProdSummaryDto dto : entityList) {
					InsWsPlan entity = new InsWsPlan();
					entity.setJgiNo(dto.getJgiNo());
					entity.setProdCode(dto.getProdCode());
					entity.setInsNo(dto.getInsNo());
					entity.setTmsTytenCd(dto.getTmsTytenCd());
					entity.setDistValueY(dto.getDistValueYRound());
					entity.setModifyValueY(null);
					entity.setPlannedValueY(dto.getPlannedValueYRound());
					//改定後計画値、確定値Tの値はnull
					entity.setSpecialInsPlanFlg("1".equals(dto.getSpecialInsPlanFlg()) ? true : false);
					entity.setExpectDistInsFlg("1".equals(dto.getExceptDistInsFlg()) ? true : false);
					entity.setDelInsFlg("1".equals(dto.getDelInsFlg()) ? true : false);
					entity.setIsJgiNo(execDpUser.getJgiNo());
					entity.setIsJgiName(execDpUser.getJgiName());
					entity.setUpJgiNo(execDpUser.getJgiNo());
					entity.setUpJgiName(execDpUser.getJgiName());
					insertInsWsPlan(entity);
				}
			}

stopWatch.stop();
debugMsg.append(getLogMsg("1-3", stopWatch));
stopWatch.reset();
stopWatch.start();

		} catch (Exception e) {
			LOG.error("jgiNo:" + jgiNo + " prodCode:" + prodCode);
			final String errMsg = "配分処理（重点品目）に失敗";
			throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg), e);
		}

		//配分ミス抽出
		//ゼロ配分フラグがTrueの場合には配分ミス情報を出力しない
		DistributionMissDto disMissDto = null;
		if (distParam.getZeroDistFlg() != null && !distParam.getZeroDistFlg()) {
			//配分ミスをチェックする
			long allRefDistValueY = 0L;
			long allDistValueY = 0L;
			try {
				Map<String, Object> resultMap = insWsPlanDao.selectHbnMissBase(jgiNo, prodCode);

				if(InsType.UH == insType){
					allRefDistValueY = ((BigDecimal) resultMap.get("plannedValueUhY")).longValue();
				}else if(InsType.P == insType){
					allRefDistValueY = ((BigDecimal) resultMap.get("plannedValuePY")).longValue();
				}
			} catch (DataNotFoundException e) {
				// 何もしない
			}

			Set<String> keySet = dtoList.keySet();
			for (String key : keySet) {
				List<InsWsPlanImpProdSummaryDto> entityList = dtoList.get(key);
				for (InsWsPlanImpProdSummaryDto dto : entityList) {
					allDistValueY += dto.getDistValueYRound();
				}
			}

			//差額が０以外の場合には配分ミスとする
			Long totalDiffValue = MathUtil.sub(allRefDistValueY, allDistValueY);
			if (totalDiffValue == null || totalDiffValue != 0) {
				disMissDto = new DistributionMissDto(sosCd, jgiNo, prodCode, insType, allRefDistValueY, totalDiffValue, "DPS2015W");
			}
		}

stopWatch.stop();
debugMsg.append(getLogMsg("1-4", stopWatch));
stopWatch1.stop();
debugMsg.append(stopWatch1.getTime());

infoLogging(debugMsg.toString());

		return disMissDto;
	}

	/**
	 * 施設特約店別計画を登録する。
	 *
	 * @param insWsPlan 施設特約店別計画
	 */
	protected void insertInsWsPlan(InsWsPlan insWsPlan) {
		//配分値を確定値に設定する
		//配分値がマイナス値の場合、0にした上で確定値に設定する
		if (insWsPlan.getDistValueY() < 0) {
			insWsPlan.setDistValueY(0L);
		}
		insWsPlan.setPlannedValueY(insWsPlan.getDistValueY());

		try {
			insWsPlanDao.insert(insWsPlan);
		} catch (DuplicateException e) {
			LOG.error("jgiNo:" + insWsPlan.getProdCode() + " prodCode:" + insWsPlan.getProdCode());
			final String errMsg = "施設特約店別計画登録に失敗";
			throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg), e);
		}
	}

	/**
	 * 配分母数を取得する。
	 *
	 * @param sosCd 組織コード
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param insType 施設出力対象区分
	 * @return 配分母数
	 */
	protected Long getParamValue(String sosCd, Integer jgiNo, String prodCode, InsType insType, InsWsDistType distType) {
		Long paramValue = null;
		switch (distType) {
			case OFFICE_PLAN_DIST:
				// 営業所計画取得
				try {
					OfficePlan officePlan = officePlanDao.searchUk(sosCd, prodCode);
					switch (insType) {
						case UH:
							paramValue = officePlan.getPlannedValueUhY();
							break;
						case P:
							paramValue = officePlan.getPlannedValuePY();
							break;
					}
				} catch (DataNotFoundException e) {
					// 営業所計画がnullの場合、営業所計画を0とする。
					paramValue = 0L;
				}
				break;
			case OFFICE_PLAN_RE_DIST:
			case MR_PLAN_DIST:
			case MR_PLAN_RE_DIST:
				// 担当者別計画取得
				try {
					MrPlan mrPlan = mrPlanDao.searchUk(jgiNo, prodCode);
					switch (insType) {
						case UH:
							paramValue = mrPlan.getPlannedValueUhY();
							break;
						case P:
							paramValue = mrPlan.getPlannedValuePY();
							break;
					}
				} catch (DataNotFoundException e) {
					// 担当者別計画取得がnullの場合、営業所計画を0とする。
					paramValue = 0L;
				}
				break;
		}
		// 計画値チェック
		if (paramValue == null) {
			paramValue = 0L;
		}

		return paramValue;
	}

	/**
	 * 特定施設個別計画のリストを取得する。
	 *
	 * @param sosCd 組織コード
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param insType 施設出力対象区分
	 * @return 特定施設個別計画のリスト
	 */
	protected List<SpecialInsPlan> getSpecialInsPlanList(String sosCd, Integer jgiNo, String prodCode, InsType insType, InsWsDistType distType, boolean isVaccine, String category) {
		List<SpecialInsPlan> specialInsPlanList = new ArrayList<SpecialInsPlan>();
		try {
			List<SpecialInsPlan> specialInsPlanListTmp = specialInsPlanDao.searchBySosCd(SpecialInsPlanDao.SORT_STRING4, sosCd, prodCode, PlanType.MR, insType, isVaccine, category);
			switch (distType) {
				case OFFICE_PLAN_DIST:
					specialInsPlanList = specialInsPlanListTmp;
					break;
				case MR_PLAN_DIST:
				case OFFICE_PLAN_RE_DIST:
				case MR_PLAN_RE_DIST:
					for (SpecialInsPlan plan : specialInsPlanListTmp) {
						if (jgiNo.equals(plan.getJgiNo())) {
							specialInsPlanList.add(plan);
						}
					}
					break;
			}
		} catch (DataNotFoundException e) {
			// 特定施設個別計画が無くても、エラーにしない
		}
		return specialInsPlanList;
	}

	/**
	 * 営業所一括配分の場合、担当者別計画を算出する。
	 *
	 * @param distType 配分種類
	 * @param mrSumMap 担当者別計画MAP
	 * @param insWsPlan 施設特約店別計画
	 * @param insType 施設出力対象区分
	 */
	private static void createMrPlan(InsWsDistType distType, Map<Integer, MrPlan> mrSumMap, InsWsPlan insWsPlan, InsType insType) {

		// 営業所一括配分の場合、担当者別計画の算出
		if (distType.equals(InsWsDistType.OFFICE_PLAN_DIST)) {
			MrPlan mrPlan;
			if (mrSumMap.containsKey(insWsPlan.getJgiNo())) {
				mrPlan = mrSumMap.get(insWsPlan.getJgiNo());
			} else {
				mrPlan = new MrPlan();
				mrPlan.setJgiNo(insWsPlan.getJgiNo());
				mrPlan.setProdCode(insWsPlan.getProdCode());
			}
			if (insType.equals(InsType.UH)) {
				mrPlan.setPlannedValueUhY(MathUtil.add(mrPlan.getPlannedValueUhY(), insWsPlan.getDistValueY()));
			} else {
				mrPlan.setPlannedValuePY(MathUtil.add(mrPlan.getPlannedValuePY(), insWsPlan.getDistValueY()));
			}
			mrSumMap.put(insWsPlan.getJgiNo(), mrPlan);
		}
	}

	private String getLogMsg(String logMsg, StopWatch stopWatch) {
		return String.format("%s,%d,", logMsg, stopWatch.getTime());
	}
	private void infoLogging(String logMsg) {
		if (LOG.isInfoEnabled()) {
			LOG.info(logMsg);
		}
	}
}
