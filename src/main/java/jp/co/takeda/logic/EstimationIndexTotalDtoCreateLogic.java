package jp.co.takeda.logic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.dto.EstimationIndexTotalDto;
import jp.co.takeda.dto.MrPlanDto;
import jp.co.takeda.dto.MrPlanPlannedValueDto;
import jp.co.takeda.dto.OfficePlanDto;
import jp.co.takeda.dto.TeamPlanDto;
import jp.co.takeda.model.EstParamHonbu;
import jp.co.takeda.model.EstParamOffice;
import jp.co.takeda.model.EstimationParam;
import jp.co.takeda.model.MrPlanStatus;
import jp.co.takeda.model.div.EstimationBasePlan;
import jp.co.takeda.model.view.EstimationRatio;
import jp.co.takeda.model.view.MrEstimationRatio;
import jp.co.takeda.util.MathUtil;

/**
 * 試算指数合計用ＤＴＯクラスを生成するロジッククラス<br>
 * 
 * <pre>
 * 本来、EstimationResultDetailResultDtoCreateLogicに入れるべきだが、
 * ロジックが複雑に組まれているため、デグレートの恐れがあると判断し、
 * 当クラスに試算指数合計列のロジックを外部化した。
 * また試算指数合計行自体が仕様が変更される可能性があるため、外部化に意味があるはず。
 * </pre>
 * 
 * @author tkawabata
 */
public class EstimationIndexTotalDtoCreateLogic {

	/**
	 * 試算パラメータ(営業所)モデル
	 */
	private final EstParamOffice estParamOffice;

	/**
	 * 試算パラメータ(本部)モデル
	 */
	private final EstParamHonbu estParamHonbu;

	/**
	 * 担当者別計画ステータスモデル
	 */
	private final MrPlanStatus mrPlanStatus;

	/**
	 * 営業所計画DTO(検索結果用DTOの基準値)
	 */
	private final OfficePlanDto officePlanDto;

	/**
	 * 営業所を母数とした構成比を保持するオブジェクトリスト
	 */
	private final List<MrEstimationRatio> officeMrEstimationRatioList;

	/**
	 * チームを母数とした構成比を保持するオブジェクトリスト
	 */
	private final List<MrEstimationRatio> teamMrEstimationRatioList;

	/**
	 * コンストラクタ
	 * 
	 * @param estParamOffice 試算パラメータ(営業所)モデル
	 * @param estParamHonbu 試算パラメータ(本部)モデル
	 * @param mrPlanStatus 担当者別計画ステータスモデル
	 * @param officePlanDto 営業所計画DTO
	 * @param officeMrEstimationRatioList 営業所を母数とした構成比を保持するオブジェクトリスト
	 * @param teamMrEstimationRatioList チームを母数とした構成比を保持するオブジェクトリスト
	 */
	public EstimationIndexTotalDtoCreateLogic(EstParamOffice estParamOffice, EstParamHonbu estParamHonbu, MrPlanStatus mrPlanStatus, OfficePlanDto officePlanDto,
		List<MrEstimationRatio> officeMrEstimationRatioList, List<MrEstimationRatio> teamMrEstimationRatioList) {
		this.estParamOffice = estParamOffice;
		this.estParamHonbu = estParamHonbu;
		this.mrPlanStatus = mrPlanStatus;
		this.officePlanDto = officePlanDto;
		this.officeMrEstimationRatioList = officeMrEstimationRatioList;
		this.teamMrEstimationRatioList = teamMrEstimationRatioList;
	}

	/**
	 * EstimationIndexTotalDtoを生成する。
	 * 
	 * @return EstimationIndexTotalDto
	 */
	public EstimationIndexTotalDto execute() {

		// ---------------------------------------
		// 営業所計画(特定施設分を除外)
		// ---------------------------------------
		Long officePlanValue = null;

		// 営業所計画を格納したDTO
		MrPlanPlannedValueDto officePlan = officePlanDto.getPlannedValueDto();

		// 営業所計画
		Long officePlanValueEntry = officePlan.getPlannedValueY();

		// 特定施設個別計画
		Long specialPlanValue = officePlan.getSpecialInsPlanValueY();

		// 営業所計画 - 特定施設個別計画
		if (officePlanValueEntry != null && specialPlanValue != null) {
			officePlanValueEntry -= specialPlanValue;
		}
		officePlanValue = officePlanValueEntry;

		// --------------------------------------
		// チーム別計画
		// --------------------------------------
		Map<Integer, Long> teamPlanValueMapByJgiNo = new HashMap<Integer, Long>();
		Map<Integer, String> jgiSosMap = new HashMap<Integer, String>();
		List<TeamPlanDto> teamPlanDtoList = officePlanDto.getTeamPlanDtoList();
		for (TeamPlanDto teamPlanDto : teamPlanDtoList) {

			MrPlanPlannedValueDto mrPlanPlannedValueDto = teamPlanDto.getPlannedValueDto();
			List<MrPlanDto> mrPlanDtoList = teamPlanDto.getMrPlanDtoList();
			if (mrPlanPlannedValueDto == null || mrPlanDtoList == null) {
				continue;
			}

			// チーム別計画を格納したDTO
			Long teamPlanValueEntry = mrPlanPlannedValueDto.getPlannedValueY();

			// 特定施設個別計画
			Long teamSpecialPlanValue = mrPlanPlannedValueDto.getSpecialInsPlanValueY();

			// チーム別計画 - 特定施設個別計画
			if (teamPlanValueEntry != null && teamSpecialPlanValue != null) {
				teamPlanValueEntry -= teamSpecialPlanValue;
			}
			for (MrPlanDto mrPlanDto : mrPlanDtoList) {
				teamPlanValueMapByJgiNo.put(mrPlanDto.getJgiNo(), teamPlanValueEntry);
				jgiSosMap.put(mrPlanDto.getJgiNo(), teamPlanDto.getSosCd());
			}
		}

		// --------------------------------------
		// 試算指数取得
		// --------------------------------------
		EstimationParam param = null;

		// 試算パラメータ（営業所案）
		if (estParamOffice != null) {
			param = estParamOffice.getEstimationParam();
		}
		// 試算パラメータ（本部案）
		else {
			param = estParamHonbu.getEstimationParam();
		}
		Double indexMikakutoku = calcIndex(param.getIndexMikakutoku());
		Double indexDelivery = calcIndex(param.getIndexDelivery());
		Double indexFree1 = calcIndex(param.getIndexFree1());
		Double indexFree2 = calcIndex(param.getIndexFree2());
		Double indexFree3 = calcIndex(param.getIndexFree3());

		// --------------------------------------
		// 試算母数
		// --------------------------------------
		EstimationBasePlan estimationBasePlan = mrPlanStatus.getEstimationBasePlan();
		if (estimationBasePlan == null) {
			estimationBasePlan = EstimationBasePlan.OFFICE_PLAN;
		}

		// --------------------------------------
		// 元となる構成比の決定
		// --------------------------------------
		List<MrEstimationRatio> mrEstimationRatioList = this.officeMrEstimationRatioList;
		if (EstimationBasePlan.TEAM_PLAN.equals(estimationBasePlan)) {
			mrEstimationRatioList = this.teamMrEstimationRatioList;
		}

		// *********************************************************************
		// 計算開始
		// *********************************************************************

		// 営業所構成比計
		Double officeTotalValue = 0d;

		// 担当者毎の構成比マップ
		Map<Integer, Double> jgiValueMap = new HashMap<Integer, Double>();

		// チーム毎の構成比マップ
		Map<String, Double> sosValueMap = new HashMap<String, Double>();

		for (MrEstimationRatio mrEstimationRatio : mrEstimationRatioList) {

			// 担当者
			Integer jgiNo = mrEstimationRatio.getJgiNo();

			// 組織
			String sosCd = jgiSosMap.get(jgiNo);

			// 試算元計画
			Long planedValue = officePlanValue;
			if (estimationBasePlan == EstimationBasePlan.TEAM_PLAN) {
				planedValue = teamPlanValueMapByJgiNo.get(jgiNo);
			}
			if (planedValue == null) {
				planedValue = 0L;
			}

			// UH/P/合計のいずれかが格納(必ずサイズが1)
			List<EstimationRatio> uhpList = mrEstimationRatio.getEstimationRatioList();
			if (uhpList == null || uhpList.isEmpty()) {
				continue;
			}
			EstimationRatio estimationRatio = uhpList.get(0);
			if (estimationRatio == null) {
				continue;
			}

			Double resultValue = 0d;
			Double mRatio = estimationRatio.getMikakutokuRatio() * planedValue * indexMikakutoku;
			Double kRatio = estimationRatio.getKakoJissekiRatio() * planedValue * indexDelivery;
			Double f1Ratio = estimationRatio.getFree1Ratio() * planedValue * indexFree1;
			Double f2Ratio = estimationRatio.getFree2Ratio() * planedValue * indexFree2;
			Double f3Ratio = estimationRatio.getFree3Ratio() * planedValue * indexFree3;
			resultValue = mRatio + kRatio + f1Ratio + f2Ratio + f3Ratio;

			// 営業所計
			officeTotalValue = officeTotalValue + resultValue;

			// 従業員毎の構成比マップに試算指数を格納
			jgiValueMap.put(jgiNo, resultValue);

			// チーム毎の構成比マップに試算指数を格納
			Double sosValue = sosValueMap.get(sosCd);
			if (sosValue != null) {
				sosValueMap.put(sosCd, sosValue + resultValue);
			} else {
				sosValueMap.put(sosCd, resultValue);
			}
		}

		// --------------------------------------
		// 1.担当者別所内構成比
		// --------------------------------------
		Map<Integer, Double> officeMapByJgiNoMap = new HashMap<Integer, Double>();
		for (Integer jgiNo : jgiValueMap.keySet()) {
			Double jgiValue = jgiValueMap.get(jgiNo);
			Double jgiRatio = calcRatio(jgiValue, officeTotalValue, false);
			if (jgiRatio == null) {
				jgiRatio = 0d;
			}
			officeMapByJgiNoMap.put(jgiNo, jgiRatio);
		}

		// --------------------------------------
		// 2.チーム別所内構成比
		// --------------------------------------
		Map<String, Double> officeMapBySosCd4Map = new HashMap<String, Double>();
		for (String sosCd : sosValueMap.keySet()) {
			Double sosValue = sosValueMap.get(sosCd);
			Double sosRatio = calcRatio(sosValue, officeTotalValue, false);
			if (sosRatio == null) {
				sosRatio = 0d;
			}
			officeMapBySosCd4Map.put(sosCd, sosRatio);
		}

		// --------------------------------------
		// 3.担当者別チーム内構成比
		// --------------------------------------
		Map<Integer, Double> teamMapByJgiNoMap = new HashMap<Integer, Double>();
		for (Integer jgiNo : jgiValueMap.keySet()) {
			String sosCd = jgiSosMap.get(jgiNo);
			Double jgiValue = jgiValueMap.get(jgiNo);
			Double sosValue = sosValueMap.get(sosCd);
			Double jgiRatio = calcRatio(jgiValue, sosValue, false);
			if (jgiRatio == null) {
				jgiRatio = 0d;
			}
			teamMapByJgiNoMap.put(jgiNo, jgiRatio);
		}

		// --------------------------------------
		// 結果の返却
		// --------------------------------------
		return new EstimationIndexTotalDto(officeMapByJgiNoMap, officeMapBySosCd4Map, teamMapByJgiNoMap);
	}

	/**
	 * 比率の値を計算し取得する。
	 * 
	 * @param numerator 割れられる値
	 * @param denominator 割る値
	 * @param isNullForZero numerator == null || denominator == null || denominator == 0L を 0とする場合、true
	 * @return 構成(%)
	 */
	private static Double calcRatio(final Double numerator, final Double denominator, final boolean isNullForZero) {
		if (numerator == null || denominator == null || denominator == 0L) {
			if (isNullForZero) {
				return 0D;
			}
			return null;
		}
		Double doubleValue = MathUtil.divide(numerator, denominator, 3, RoundingMode.HALF_UP);
		BigDecimal bdValue = new BigDecimal(doubleValue.toString());
		bdValue = bdValue.multiply(new BigDecimal(100));
		return bdValue.doubleValue();
	}

	/**
	 * 試算指数を百分率にする。
	 * 
	 * @param value 試算指数(試算パラメータ)
	 * @return 試算指数を百分率にした値
	 */
	private static Double calcIndex(Integer value) {
		Double result = MathUtil.divide(value, 100, 2, RoundingMode.HALF_UP);
		return result;
	}
}
