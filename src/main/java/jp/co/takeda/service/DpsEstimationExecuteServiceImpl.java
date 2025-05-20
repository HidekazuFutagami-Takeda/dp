package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.dao.EstParamHonbuDao;
import jp.co.takeda.dao.EstParamOfficeDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.MrEstimationRatioDao;
import jp.co.takeda.dao.MrPlanDao;
import jp.co.takeda.dao.OfficePlanDao;
import jp.co.takeda.dao.OfficePlanStatusDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.SpecialInsPlanSosSummaryDao;
import jp.co.takeda.dao.TeamPlanDao;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.logic.div.EstimationJudge;
import jp.co.takeda.model.BaseParam;
import jp.co.takeda.model.EstParamHonbu;
import jp.co.takeda.model.EstParamOffice;
import jp.co.takeda.model.EstimationParam;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.model.OfficePlan;
import jp.co.takeda.model.OfficePlanStatus;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.TeamPlan;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.CalcType;
import jp.co.takeda.model.div.EstimationBase;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.view.EstimationRatio;
import jp.co.takeda.model.view.MrEstimationRatio;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.util.MathUtil;

/**
 * 試算実行サービス実装クラス
 *
 * @author khashimoto
 */
@Transactional
@Service("dpsEstimationExecuteService")
public class DpsEstimationExecuteServiceImpl implements DpsEstimationExecuteService {

	/**
	 * ロガー
	 */
	private static final Log LOG = LogFactory.getLog(DpsEstimationExecuteServiceImpl.class);

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
	 * 営業所計画ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("officePlanStatusDao")
	protected OfficePlanStatusDao officePlanStatusDao;

	/**
	 * チーム別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("teamPlanDao")
	protected TeamPlanDao teamPlanDao;

	/**
	 * 特定施設個別計画の組織、対象区分集約DAO
	 */
	@Autowired(required = true)
	@Qualifier("specialInsPlanSosSummaryDao")
	protected SpecialInsPlanSosSummaryDao specialInsPlanSosSummaryDao;

	/**
	 * 担当者別試算構成比取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("mrEstimationRatioDao")
	protected MrEstimationRatioDao mrEstimationRatioDao;

	/**
	 * 担当者別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("mrPlanDao")
	protected MrPlanDao mrPlanDao;

	/**
	 * 汎用マスタ検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	public void execute(String sosCd, String prodCode, DpUser execDpUser) {
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
		if (execDpUser == null) {
			final String errMsg = "実行者情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 組織情報取得
		// ----------------------
		SosMst sosMst;
		try {
			sosMst = sosMstDAO.search(sosCd);
		} catch (DataNotFoundException e) {
			final String errMsg = "組織情報取得に失敗";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg), e);
		}
		// チーム試算処理の場合、上位組織コード(営業所)を設定。
		BumonRank bumonRank = sosMst.getBumonRank();
		String officeSosCd = sosCd;
		if (bumonRank.equals(BumonRank.TEAM)) {
			officeSosCd = sosMst.getUpSosCd();
		}

		// 品目からカテゴリ判定（MMP or ONC）
		PlannedProd plannedProd = null;
		try {
			plannedProd = plannedProdDAO.search(prodCode);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目【品目固定コード：" + prodCode + "】が存在しないため、システムエラー";
			throw new SystemException(new Conveyance(ErrMessageKey.SYSTEM_ERROR, errMsg), e);
		}
		String category = plannedProd.getCategory();

		// ---------------------------------------------------------------
		// 試算タイプを取得する
		// ---------------------------------------------------------------
		CalcType calcType;
		try {
			OfficePlanStatus officePlanStatus = officePlanStatusDao.search(officeSosCd, category);
			calcType = officePlanStatus.getCalcType();
			if (calcType == null) {
				final String errMsg = "試算タイプがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		} catch (DataNotFoundException e) {
			final String errMsg = "営業所計画ステータスの取得に失敗";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg), e);
		}

		// ----------------------
		// 従業員情報取得（従業員番号とチーム組織コード）
		// ----------------------
		// チーム不在の場合は、チーム組織コード：00000
		Map<Integer, String> jgiTeamMap = new HashMap<Integer, String>();
		try {
			List<JgiMst> jgiList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd, SosListType.SHITEN_LIST, bumonRank);
			for (JgiMst jgiMst : jgiList) {
				jgiTeamMap.put(jgiMst.getJgiNo(), jgiMst.getSosCd4());
			}
		} catch (DataNotFoundException e) {
			final String errMsg = "従業員情報取得に失敗";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg), e);
		}

		// ----------------------
		// 試算パラメータ取得
		// ----------------------
		EstimationParam estParam = null;
		BaseParam baseParam = null;
		try {
			EstParamOffice estParamOffice = estParamOfficeDao.search(officeSosCd, prodCode);
			estParam = estParamOffice.getEstimationParam();
			baseParam = estParamOffice.getBaseParam();
		} catch (DataNotFoundException e) {
			// 営業所案が無くてもエラーとしない
		}
		if (estParam == null) {
			try {
				EstParamHonbu estParamHonbu = estParamHonbuDao.search(prodCode);
				estParam = estParamHonbu.getEstimationParam();
				baseParam = estParamHonbu.getBaseParam();
			} catch (DataNotFoundException e2) {
				final String errMsg = "試算パラメータ(本部)が存在しない";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg), e2);
			}
		}

		// 試算元計算値取得
		EstimationBase estimationBase = estParam.getEstimationBase();

		// ----------------------------------------
		// 試算母数の取得
		// 営業所計画、またはチーム別計画を取得する
		// ----------------------------------------
		Long paramUh = null;
		Long paramP = null;
		Long paramUhp = 0L;

		// 営業所計画取得
		if (bumonRank.equals(BumonRank.OFFICE_TOKUYAKUTEN_G)) {
			try {
				OfficePlan officePlan = officePlanDao.searchUk(sosCd, prodCode);
				paramUh = officePlan.getPlannedValueUhY();
				paramP = officePlan.getPlannedValuePY();
			} catch (DataNotFoundException e) {
				final String errMsg = "営業所計画が存在しない";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg), e);
			}
		} else {
			// チーム別計画取得
			try {
				TeamPlan teamPlan = teamPlanDao.searchUk(sosCd, prodCode);
				paramUh = teamPlan.getPlannedValueUhY();
				paramP = teamPlan.getPlannedValuePY();
				// 決定欄が未設定の場合、営業所案を使用する
				if (paramUh == null && paramP == null) {
					paramUh = teamPlan.getOfficeValueUhY();
					paramP = teamPlan.getOfficeValuePY();
				}
				// 決定欄も、営業所案も未設定の場合、試算は実行しない
				if (paramUh == null && paramP == null) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("【試算処理】決定欄/営業所案が未設定のため、試算は実行しない");
					}
					return;
				}

			} catch (DataNotFoundException e) {
				final String errMsg = "チーム別計画が存在しない";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg), e);
			}
		}
		// 計画値チェック
		if (paramUh == null) {
			paramUh = 0L;
		}
		if (paramP == null) {
			paramP = 0L;
		}

		// ------------------------------
		// 担当者別試算要素・比率の取得
		// ------------------------------
		List<MrEstimationRatio> estimationRatioList;
		try {
			if (bumonRank.equals(BumonRank.OFFICE_TOKUYAKUTEN_G)) {
				estimationRatioList = mrEstimationRatioDao.searchList(sosCd, prodCode, baseParam.getRefProdCode(), baseParam.getRefFrom(), baseParam.getRefTo(), category);
			} else {
				estimationRatioList = mrEstimationRatioDao.searchListByTeam(sosCd, prodCode, baseParam.getRefProdCode(), baseParam.getRefFrom(), baseParam.getRefTo(), category);
			}
		} catch (DataNotFoundException e) {
			final String errMsg = "担当者別試算比率の取得に失敗";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg), e);
		}

		// --------------------------------------
		// 納入実績・特定施設個別計画の集計値生成
		// --------------------------------------
		long kakoJissekiSumUh = 0;
		long kakoJissekiSumP = 0;
		long specialSumUh = 0;
		long specialSumP = 0;
		// ワクチン判定
		boolean isVaccine = dpsCodeMasterSearchService.isVaccine(category);
		for (MrEstimationRatio ratio : estimationRatioList) {
			for (EstimationRatio estimationRatio : ratio.getEstimationRatioList()) {
				InsType refInsType = estimationRatio.getInsType();
				if (refInsType != null) {
					long kakoJisseki = 0;
					long special = 0;
					if (estimationRatio.getOwnKakoJissekiValue() != null) {
						kakoJisseki = estimationRatio.getOwnKakoJissekiValue();
					}
					if (estimationRatio.getSpecialValue() != null) {
						special = estimationRatio.getSpecialValue();
					}
					// ワクチンの場合は、納入実績をP雑で集計
					if (isVaccine) {
						switch (refInsType) {
						case UH:
							kakoJissekiSumUh = kakoJissekiSumUh + kakoJisseki;
							specialSumUh = specialSumUh + special;
							break;
						case P:
							kakoJissekiSumP = kakoJissekiSumP + kakoJisseki;
							specialSumP = specialSumP + special;
							break;
						case ZATU:
							kakoJissekiSumP = kakoJissekiSumP + kakoJisseki;
							specialSumP = specialSumP + special;
							break;
						}
					} else {
						switch (refInsType) {
						case UH:
							kakoJissekiSumUh = kakoJissekiSumUh + kakoJisseki;
							specialSumUh = specialSumUh + special;
							break;
						case P:
							kakoJissekiSumP = kakoJissekiSumP + kakoJisseki;
							specialSumP = specialSumP + special;
							break;
						}
					}
				}
			}
		}

// mod start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）　試算母数の保存の後にあった指数 ⇒ 百分率をここに移動
// 指数 ⇒ 百分率
// add start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		double indexRyhRtsu = calcIndex(estParam.getIndexRyhRtsu());
// add end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		double indexMikakutoku = calcIndex(estParam.getIndexMikakutoku());
		double indexDelivery = calcIndex(estParam.getIndexDelivery());
		double indexFree1 = calcIndex(estParam.getIndexFree1());
		double indexFree2 = calcIndex(estParam.getIndexFree2());
		double indexFree3 = calcIndex(estParam.getIndexFree3());
		if (LOG.isDebugEnabled()) {
// add start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
			LOG.debug("【留保率】indexRyhRtsu：" + indexRyhRtsu);
// add end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
			LOG.debug("【未獲得指数】indexMikakutoku：" + indexMikakutoku);
			LOG.debug("【実績指数】indexDelivery：" + indexDelivery);
			LOG.debug("【フリー①指数】indexFree1：" + indexFree1);
			LOG.debug("【フリー②指数】indexFree2：" + indexFree2);
			LOG.debug("【フリー③指数】indexFree3：" + indexFree3);
		}
// mod end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）


		// ------------------------------
		// 試算母数の保存
		// ------------------------------

		// 営業所（チーム別）計画（特定施設個別計画含む）
		Long planUh = new Long(paramUh);
		Long planP = new Long(paramP);
		Long planUhp = new Long(paramUh + paramP);

		// 試算母数の算出⇒営業所（チーム別）計画から、特定施設個別計画を引く
		paramUh = paramUh - specialSumUh;
		paramP = paramP - specialSumP;
		paramUhp = paramUh + paramP;

		// 試算処理方法（アップ・ダウンなどの）判定用
		// UHP用
		EstimationJudge estimationJudgeUhp = EstimationJudge.PLAN_FOR_ALL;
		// UH用
		EstimationJudge estimationJudgeUh = EstimationJudge.PLAN_FOR_ALL;
		// P用
		EstimationJudge estimationJudgeP = EstimationJudge.PLAN_FOR_ALL;

		// 試算元計算値が「増分値」の場合
		if (estimationBase.equals(EstimationBase.PLAN_FOR_UP)) {

// mod start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
			// 試算処理方法と、試算母数を決定する。
			// UH
//			estimationJudgeUh = estimationJudge(paramUh, planUh, kakoJissekiSumUh);
//			paramUh = estimationParamRevision(estimationJudgeUh, paramUh, planUh, kakoJissekiSumUh);
			estimationJudgeUh = estimationJudge(paramUh, planUh, Math.round(kakoJissekiSumUh * indexRyhRtsu));
			paramUh = estimationParamRevision(estimationJudgeUh, paramUh, planUh, Math.round(kakoJissekiSumUh * indexRyhRtsu));
			// P
//			estimationJudgeP = estimationJudge(paramP, planP, kakoJissekiSumP);
//			paramP = estimationParamRevision(estimationJudgeP, paramP, planP, kakoJissekiSumP);
			estimationJudgeP = estimationJudge(paramP, planP, Math.round(kakoJissekiSumP * indexRyhRtsu));
			paramP = estimationParamRevision(estimationJudgeP, paramP, planP, Math.round(kakoJissekiSumP * indexRyhRtsu));
			// UHP
//			estimationJudgeUhp = estimationJudge(paramUhp, planUhp, kakoJissekiSumUh + kakoJissekiSumP);
//			paramUhp = estimationParamRevision(estimationJudgeUhp, paramUhp, planUhp, kakoJissekiSumUh + kakoJissekiSumP);
			estimationJudgeUhp = estimationJudge(paramUhp, planUhp, Math.round((kakoJissekiSumUh + kakoJissekiSumP) * indexRyhRtsu));
			paramUhp = estimationParamRevision(estimationJudgeUhp, paramUhp, planUhp, Math.round((kakoJissekiSumUh + kakoJissekiSumP) * indexRyhRtsu));
// mod end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）

		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("【試算母数】estimationBase：" + estimationBase);
			LOG.debug("【試算母数】paramUh：" + paramUh);
			LOG.debug("【試算母数】paramP：" + paramP);
			LOG.debug("【試算母数】paramUhp：" + paramUhp);
			LOG.debug("【上位計画】planUh：" + planUh);
			LOG.debug("【上位計画】planP：" + planP);
			LOG.debug("【上位計画】planUhp：" + planUhp);
			LOG.debug("【過去実績】kakoJissekiSumUh：" + kakoJissekiSumUh);
			LOG.debug("【過去実績】kakoJissekiSumP：" + kakoJissekiSumP);
			LOG.debug("【試算処理方法】estimationJudgeUh：" + estimationJudgeUh);
			LOG.debug("【試算処理方法】estimationJudgeP：" + estimationJudgeP);
			LOG.debug("【試算処理方法】estimationJudgeUh：" + estimationJudgeUhp);
		}

		// ------------------------------
		// 担当者別計画生成
		// ------------------------------
		// 担当者別計画
		List<MrPlan> mrPlanList = new ArrayList<MrPlan>();
		// チーム別サマリーMAP
		Map<String, TeamPlan> teamSumMap = new LinkedHashMap<String, TeamPlan>();

// mod 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）　指数 ⇒ 百分率は試算母数の前に移動

		for (MrEstimationRatio mrEstimationRatio : estimationRatioList) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("【試算比率】" + mrEstimationRatio);
				LOG.debug("【従業員】" + mrEstimationRatio.getJgiNo());
			}
			Long estValueTotal = 0L;
			Long estValueUh = 0L;
			Long estValueP = 0L;
			Long specialInsPlanValueUhY = 0L;
			Long specialInsPlanValuePY = 0L;
			// 試算結果算出
			for (EstimationRatio estimationRatio : mrEstimationRatio.getEstimationRatioList()) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("【試算比率】" + estimationRatio);
				}
				InsType refInsType = estimationRatio.getInsType();
				if (refInsType == null) {
// mod start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
//					estValueTotal = calcEstValue(paramUhp, indexMikakutoku, indexDelivery, indexFree1, indexFree2, indexFree3, estimationRatio, estimationJudgeUhp, baseParam);
					estValueTotal = calcEstValue(paramUhp, indexRyhRtsu, indexMikakutoku, indexDelivery, indexFree1, indexFree2, indexFree3, estimationRatio, estimationJudgeUhp, baseParam);
// mod end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）

				} else {
					switch (refInsType) {
						case UH:
// mod start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
//							estValueUh = calcEstValue(paramUh, indexMikakutoku, indexDelivery, indexFree1, indexFree2, indexFree3, estimationRatio, estimationJudgeUh, baseParam);
							estValueUh = calcEstValue(paramUh, indexRyhRtsu, indexMikakutoku, indexDelivery, indexFree1, indexFree2, indexFree3, estimationRatio, estimationJudgeUh, baseParam);
// mod end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
							specialInsPlanValueUhY = estimationRatio.getSpecialValue();
							break;
						case P:
// mod start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
//							estValueP = calcEstValue(paramP, indexMikakutoku, indexDelivery, indexFree1, indexFree2, indexFree3, estimationRatio, estimationJudgeP, baseParam);
							estValueP = calcEstValue(paramP, indexRyhRtsu, indexMikakutoku, indexDelivery, indexFree1, indexFree2, indexFree3, estimationRatio, estimationJudgeP, baseParam);
// mod end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
							specialInsPlanValuePY = estimationRatio.getSpecialValue();
							break;
						case ZATU:
							estValueP = calcEstValue(paramP, indexRyhRtsu, indexMikakutoku, indexDelivery, indexFree1, indexFree2, indexFree3, estimationRatio, estimationJudgeP, baseParam);
							specialInsPlanValuePY = estimationRatio.getSpecialValue();
							break;
					}
				}
			}
			// 理論値算出
			Long theoreticalValueUh1 = calcTheory(estValueUh);
			Long theoreticalValueP1 = MathUtil.sub(calcTheory(estValueTotal), calcTheory(estValueUh));
			Long theoreticalValueUh2 = calcTheory(estValueUh);
			Long theoreticalValueP2 = calcTheory(estValueP);
			// 担当者別計画生成
			MrPlan mrPlan = new MrPlan();
			// 従業員番号
			mrPlan.setJgiNo(mrEstimationRatio.getJgiNo());
			// 品目固定コード
			mrPlan.setProdCode(mrEstimationRatio.getProdCode());
			// 特定施設個別計画値
			mrPlan.setSpecialInsPlanValueUhY(specialInsPlanValueUhY);
			mrPlan.setSpecialInsPlanValuePY(specialInsPlanValuePY);
			// 理論値設定
			mrPlan.setTheoreticalValueUh1(theoreticalValueUh1);
			mrPlan.setTheoreticalValueP1(theoreticalValueP1);
			mrPlan.setTheoreticalValueUh2(theoreticalValueUh2);
			mrPlan.setTheoreticalValueP2(theoreticalValueP2);

			mrPlanList.add(mrPlan);

			// 営業所試算の場合、かつ、チームがブランクでない場合、チーム別計画の算出
			String teamCode = jgiTeamMap.get(mrEstimationRatio.getJgiNo());
			if (bumonRank.equals(BumonRank.OFFICE_TOKUYAKUTEN_G) && !SosMst.BRANK_SOS_CD.equals(teamCode)) {
				Long teamSIPValueUhY = specialInsPlanValueUhY;
				Long teamSIPValuePY = specialInsPlanValuePY;
				Long teamTheolValueUh1 = theoreticalValueUh1;
				Long teamTheoValueP1 = theoreticalValueP1;
				Long teamTheoValueUh2 = theoreticalValueUh2;
				Long teamTheoValueP2 = theoreticalValueP2;
				TeamPlan teamPlan;
				if (teamSumMap.containsKey(teamCode)) {
					teamPlan = teamSumMap.get(teamCode);
					teamSIPValueUhY = MathUtil.add(teamSIPValueUhY, teamPlan.getSpecialInsPlanValueUhY());
					teamSIPValuePY = MathUtil.add(teamSIPValuePY, teamPlan.getSpecialInsPlanValuePY());
					teamTheolValueUh1 = MathUtil.add(teamTheolValueUh1, teamPlan.getTheoreticalValueUh1());
					teamTheoValueP1 = MathUtil.add(teamTheoValueP1, teamPlan.getTheoreticalValueP1());
					teamTheoValueUh2 = MathUtil.add(teamTheoValueUh2, teamPlan.getTheoreticalValueUh2());
					teamTheoValueP2 = MathUtil.add(teamTheoValueP2, teamPlan.getTheoreticalValueP2());
				} else {
					teamPlan = new TeamPlan();
					teamPlan.setSosCd(teamCode);
					teamPlan.setProdCode(mrEstimationRatio.getProdCode());
				}
				teamPlan.setSpecialInsPlanValueUhY(teamSIPValueUhY);
				teamPlan.setSpecialInsPlanValuePY(teamSIPValuePY);
				teamPlan.setTheoreticalValueUh1(teamTheolValueUh1);
				teamPlan.setTheoreticalValueP1(teamTheoValueP1);
				teamPlan.setTheoreticalValueUh2(teamTheoValueUh2);
				teamPlan.setTheoreticalValueP2(teamTheoValueP2);
				teamSumMap.put(teamCode, teamPlan);
			}
		}

		// ------------------------------
		// チーム別計画更新or登録
		// （チーム不在の営業所・エリアの場合は処理されない）
		// ------------------------------
		if (bumonRank.equals(BumonRank.OFFICE_TOKUYAKUTEN_G)) {
			for (TeamPlan teamPlan : teamSumMap.values()) {
				teamPlanDao.executeByEst(teamPlan, execDpUser);
			}
		}

		// ------------------------------
		// 担当者別計画更新or登録
		// ------------------------------
		for (MrPlan mrPlan : mrPlanList) {
			mrPlanDao.executeByEst(mrPlan, execDpUser);
		}

		// ---------------------------------------------------------------
		// 営→担の試算時、担当者別計画を積上げて、チーム別計画を更新する
		// （チーム不在の営業所・エリアの場合は処理されない）
		// ---------------------------------------------------------------
		if (calcType.equals(CalcType.OFFICE_MR)) {
			for (TeamPlan teamPlan : teamSumMap.values()) {
				teamPlanDao.updateByMrPlanSum(teamPlan.getSosCd(), prodCode, execDpUser);
			}
		}

	}

	/**
	 * 試算処理方法を決定し、返す。
	 *
	 *
	 * @param param 試算母数
	 * @param plan 上位計画値
	 * @param jisseki 過去実績
	 * @return 試算処理方法
	 */
	protected EstimationJudge estimationJudge(Long param, Long plan, Long jisseki) {
		// ①OR②OR③の場合、試算処理方法を「ダウン」に変更する
		//   ①上位計画値(特定施設個別計画含む)が0
		//   ②試算母数≦0
		//   ③納入実績との増分が0以下
		if (plan == 0 || param <= 0 || (param - jisseki) <= 0) {
			return EstimationJudge.DOWN;
		}
		return EstimationJudge.UP;
	}

	/**
	 * 試算母数の補正を行う。
	 *
	 *
	 * @param estimationJudge 試算処理方法
	 * @param param 試算母数
	 * @param plan 上位計画値
	 * @param jisseki 過去実績
	 * @return 補正後の試算母数
	 */
	protected Long estimationParamRevision(EstimationJudge estimationJudge, Long param, Long plan, Long jisseki) {
		// アップ計画の場合、以下の試算母数の補正を行う。
		// 上位計画値が0の場合は、試算母数を0にする。
		// 計画値が0でない場合は、納入実績を引いた値を試算母数とする。
		if (estimationJudge.equals(EstimationJudge.UP)) {
			if (plan == 0 || param == 0) {
				param = 0L;
			} else {
				param = param - jisseki;
			}
		}
		return param;
	}

	/**
	 * 試算結果を算出する。
	 *
	 * <pre>
	 * 【計画値全体】
	 * <担当者別試算結果>=<試算母数>×<未獲得市場担当者別比率>×指数（未獲得市場）
	 * ＋<試算母数>×<納入実績担当者別比率>×指数（納入実績）
	 * ＋<試算母数>×<フリー項目①比率>×指数（フリー項目①）
	 * ＋<試算母数>×<フリー項目②比率>×指数（フリー項目②）
	 * ＋<試算母数>×<フリー項目③比率>×指数（フリー項目③）
	 *
	 * 【増分値】
	 * <担当者別´試算結果>=<試算母数(増分)>×<未獲得市場担当者別比率>×指数（未獲得市場）
	 * ＋<試算母数(増分)>×<納入実績担当者別比率>×指数（納入実績）
	 * ＋<試算母数(増分)>×<フリー項目①比率>×指数（フリー項目①）
	 * ＋<試算母数(増分)>×<フリー項目②比率>×指数（フリー項目②）
	 * ＋<試算母数(増分)>×<フリー項目③比率>×指数（フリー項目③）
	 * ＋（前期の担当者別納入実績）×留保率
	 * </pre>
	 *
	 * @param param 試算母数
	 * @param indexRyhRtsu 留保率
	 * @param indexMikakutoku 未獲得市場比率
	 * @param indexDelivery 過去実績比率
	 * @param indexFree1 フリー項目①比率
	 * @param indexFree2 フリー項目②比率
	 * @param indexFree3 フリー項目③比率
	 * @param estimationRatio 試算構成比
	 * @param estimationJudge 試算処理方法
	 * @param baseParam 試算配分共通パラメータ
	 * @return 試算結果
	 */
// mod start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
//	protected Long calcEstValue(Long param, Double indexMikakutoku, Double indexDelivery, Double indexFree1, Double indexFree2, Double indexFree3, EstimationRatio estimationRatio,
//			EstimationJudge estimationJudge, BaseParam baseParam) {
	protected Long calcEstValue(Long param, Double indexRyhRtsu, Double indexMikakutoku, Double indexDelivery, Double indexFree1, Double indexFree2, Double indexFree3, EstimationRatio estimationRatio,
		EstimationJudge estimationJudge, BaseParam baseParam) {
// mod end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		// ダウン時の試算比率補正
		// ダウン時の試算比率は未獲得市場の比率を０とし、過去実績の比率に未獲得市場の比率を加算した比率で試算計算をおこなう。
		if (estimationJudge.equals(EstimationJudge.DOWN)) {
			indexDelivery = indexDelivery + indexMikakutoku;
			indexMikakutoku = 0D;
		}

		// 試算結果算出
		Double estValue = 0D;
		estValue = (param * indexMikakutoku * estimationRatio.getMikakutokuRatio()) + (param * indexDelivery * estimationRatio.getKakoJissekiRatio())
			+ (param * indexFree1 * estimationRatio.getFree1Ratio()) + (param * indexFree2 * estimationRatio.getFree2Ratio())
			+ (param * indexFree3 * estimationRatio.getFree3Ratio());
		// 【増分値】の場合、対象品目の前期担当者別納入実績を足しこむ
		if (estimationJudge.equals(EstimationJudge.UP) && (estimationRatio.getOwnKakoJissekiValue() != null)) {
			if (param != 0) {
// mod start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
//				estValue = estValue + estimationRatio.getOwnKakoJissekiValue();
				estValue = estValue + estimationRatio.getOwnKakoJissekiValue() * indexRyhRtsu;
// mod end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
			}
		}
		if (LOG.isDebugEnabled()) {
// add start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
			LOG.debug("【試算結果算出】indexRyhRtsu：" + indexRyhRtsu);
// add end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
			LOG.debug("【試算結果算出】indexMikakutoku：" + indexMikakutoku);
			LOG.debug("【試算結果算出】indexDelivery：" + indexDelivery);
			LOG.debug("【試算結果算出】indexFree1：" + indexFree1);
			LOG.debug("【試算結果算出】indexFree2：" + indexFree2);
			LOG.debug("【試算結果算出】indexFree3：" + indexFree3);
			LOG.debug("【試算結果算出】param：" + param);
			LOG.debug("【試算結果算出】estimationRatio：" + estimationRatio);
			LOG.debug("【試算結果算出】estimationJudge：" + estimationJudge);
			LOG.debug("【試算結果算出】baseParam：" + baseParam);
			LOG.debug("【試算結果算出】estValue：" + estValue.longValue());
		}
		return estValue.longValue();
	}

	/**
	 * 試算指数を百分率にする。
	 *
	 * @param value 試算指数(試算パラメータ)
	 * @return 試算指数を百分率にした値
	 */
	protected Double calcIndex(Integer value) {
		Double result = MathUtil.divide(value, 100, 2, RoundingMode.HALF_UP);
		return result;
	}

	/**
	 * 理論値を万単位で丸める。
	 *
	 * @param value 理論値
	 * @return 理論値(万単位)
	 */
	protected Long calcTheory(Long value) {
		Double result = MathUtil.divide(value, 1, -4, RoundingMode.HALF_UP);
		return result.longValue();
	}
}
