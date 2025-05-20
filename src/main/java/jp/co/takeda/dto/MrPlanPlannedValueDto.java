package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.MonNnu;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.model.OfficePlan;
import jp.co.takeda.model.TeamPlan;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.MathUtil;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 担当者別計画　計画値DTO
 * 
 * @author nozaki
 */
public class MrPlanPlannedValueDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 特定施設個別計画値(Y)
	 */
	private final Long specialInsPlanValueY;

	/**
	 * [理論値1] 増分([理論値1] 計画-前期実績)
	 */
	private final Long planForUp1;

	/**
	 * [理論値1] 計画
	 */
	private final Long theoreticalValue1;

	/**
	 * [理論値1] 前同比([理論値1] 計画/前期実績)
	 */
	private final Double lastSameRatio1;

	/**
	 * [理論値2] 増分([理論値2] 計画-前期実績)
	 */
	private final Long planForUp2;

	/**
	 * [理論値2] 計画
	 */
	private final Long theoreticalValue2;

	/**
	 * [理論値2] 前同比([理論値2] 計画/前期実績)
	 */
	private final Double lastSameRatio2;

	/**
	 * 営業所案(Y)
	 */
	private final Long officeValueY;

	/**
	 * 計画値(Y)
	 */
	private final Long plannedValueY;

	/**
	 * 非表示用計画値UH(Y)
	 */
	private final Long hiddenPlannedValueUhY;

	/**
	 * 非表示用計画値P(Y)
	 */
	private final Long hiddenPlannedValuePY;

	/**
	 * 非表示用前期実績(円単位)
	 */
	private final Long hiddenAdvancePeriodUnitYen;

	/**
	 * コンストラクタ
	 * 
	 * @param specialInsPlanValueY 特定施設個別計画Y
	 * @param theoreticalValue1 理論値①
	 * @param theoreticalValue2 理論値②
	 * @param officeValueY 営業所案
	 * @param plannedValueY 決定値
	 */
	public MrPlanPlannedValueDto(Long specialInsPlanValueY, Long theoreticalValue1, Long theoreticalValue2, Long officeValueY, Long plannedValueY) {

		this.specialInsPlanValueY = specialInsPlanValueY;
		this.theoreticalValue1 = theoreticalValue1;
		this.theoreticalValue2 = theoreticalValue2;
		this.officeValueY = officeValueY;
		this.plannedValueY = plannedValueY;
		this.planForUp1 = null;
		this.lastSameRatio1 = null;
		this.planForUp2 = null;
		this.lastSameRatio2 = null;
		this.hiddenPlannedValueUhY = null;
		this.hiddenPlannedValuePY = null;
		this.hiddenAdvancePeriodUnitYen = null;
	}

	/**
	 * コンストラクタ
	 * 
	 * @param insType 施設出力区分(UH,P以外の場合は合計)
	 * @param mrPlan 担当者別計画
	 * @param monNnu 納入実績
	 */
	public MrPlanPlannedValueDto(InsType insType, MrPlan mrPlan, MonNnu monNnu) {

		// UH(理論値＝理論値
		Long _specialInsPlanValueYUh = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getSpecialInsPlanValueUhY());
		Long _theoreticalValue1Uh = MathUtil.add(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getTheoreticalValueUh1()), _specialInsPlanValueYUh);
		Long _theoreticalValue2Uh = MathUtil.add(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getTheoreticalValueUh2()), _specialInsPlanValueYUh);
		Long _officeValueYUh = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getOfficeValueUhY());
		Long _plannedValueYUh = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getPlannedValueUhY());

		// P
		Long _specialInsPlanValueYP = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getSpecialInsPlanValuePY());
		Long _theoreticalValue1P = MathUtil.add(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getTheoreticalValueP1()), _specialInsPlanValueYP);
		Long _theoreticalValue2P = MathUtil.add(ConvertUtil.parseMoneyToThousandUnit(mrPlan.getTheoreticalValueP2()), _specialInsPlanValueYP);
		Long _officeValueYP = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getOfficeValuePY());
		Long _plannedValueYP = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getPlannedValuePY());

		if (insType == InsType.UH) {
			this.specialInsPlanValueY = _specialInsPlanValueYUh;
			this.theoreticalValue1 = _theoreticalValue1Uh;
			this.theoreticalValue2 = _theoreticalValue2Uh;
			this.officeValueY = _officeValueYUh;
			this.plannedValueY = _plannedValueYUh;
		} else if (insType == InsType.P) {
			this.specialInsPlanValueY = _specialInsPlanValueYP;
			this.theoreticalValue1 = _theoreticalValue1P;
			this.theoreticalValue2 = _theoreticalValue2P;
			this.officeValueY = _officeValueYP;
			this.plannedValueY = _plannedValueYP;
		} else {
			this.specialInsPlanValueY = MathUtil.add(_specialInsPlanValueYUh, _specialInsPlanValueYP);
			this.theoreticalValue1 = MathUtil.add(_theoreticalValue1Uh, _theoreticalValue1P);
			this.theoreticalValue2 = MathUtil.add(_theoreticalValue2Uh, _theoreticalValue2P);
			this.officeValueY = MathUtil.add(_officeValueYUh, _officeValueYP);
			this.plannedValueY = MathUtil.add(_plannedValueYUh, _plannedValueYP);
		}
		// 非表示用計画値
		this.hiddenPlannedValueUhY = _plannedValueYUh;
		this.hiddenPlannedValuePY = _plannedValueYP;

		// ----------------------------------
		// 前期実績
		// ----------------------------------
		Long _advancePeriod = null;
		Long _entryValue = null;

		// 円単位で計算
		if (monNnu != null) {
			_advancePeriod = monNnu.getAdvancePeriod();
		}
		this.hiddenAdvancePeriodUnitYen = _advancePeriod;

		if (insType == InsType.UH) {

			// [理論値1] 増分、前同比
			_entryValue = MathUtil.add(mrPlan.getTheoreticalValueUh1(), mrPlan.getSpecialInsPlanValueUhY());
			this.planForUp1 = ConvertUtil.parseMoneyToThousandUnit(MathUtil.planForUp(_entryValue, _advancePeriod));

			// [理論値1] 前同比
			_entryValue = MathUtil.add(mrPlan.getTheoreticalValueUh1(), mrPlan.getSpecialInsPlanValueUhY());
			this.lastSameRatio1 = MathUtil.calcRatio(_entryValue, _advancePeriod);

			// [理論値2] 増分
			_entryValue = MathUtil.add(mrPlan.getTheoreticalValueUh2(), mrPlan.getSpecialInsPlanValueUhY());
			this.planForUp2 = ConvertUtil.parseMoneyToThousandUnit(MathUtil.planForUp(_entryValue, _advancePeriod));

			// [理論値2] 前同比
			_entryValue = MathUtil.add(mrPlan.getTheoreticalValueUh2(), mrPlan.getSpecialInsPlanValueUhY());
			this.lastSameRatio2 = MathUtil.calcRatio(_entryValue, _advancePeriod);

		} else if (insType == InsType.P) {

			// [理論値1] 増分
			_entryValue = MathUtil.add(mrPlan.getTheoreticalValueP1(), mrPlan.getSpecialInsPlanValuePY());
			this.planForUp1 = ConvertUtil.parseMoneyToThousandUnit(MathUtil.planForUp(_entryValue, _advancePeriod));

			// [理論値1] 前同比
			_entryValue = MathUtil.add(mrPlan.getTheoreticalValueP1(), mrPlan.getSpecialInsPlanValuePY());
			this.lastSameRatio1 = MathUtil.calcRatio(_entryValue, _advancePeriod);

			// [理論値2] 増分
			_entryValue = MathUtil.add(mrPlan.getTheoreticalValueP2(), mrPlan.getSpecialInsPlanValuePY());
			this.planForUp2 = ConvertUtil.parseMoneyToThousandUnit(MathUtil.planForUp(_entryValue, _advancePeriod));

			// [理論値2] 前同比
			_entryValue = MathUtil.add(mrPlan.getTheoreticalValueP2(), mrPlan.getSpecialInsPlanValuePY());
			this.lastSameRatio2 = MathUtil.calcRatio(_entryValue, _advancePeriod);

		} else {

			Long _entryValueUH = null;
			Long _entryValueP = null;

			// [理論値1] 増分
			_entryValueUH = MathUtil.add(mrPlan.getTheoreticalValueUh1(), mrPlan.getSpecialInsPlanValueUhY());
			_entryValueP = MathUtil.add(mrPlan.getTheoreticalValueP1(), mrPlan.getSpecialInsPlanValuePY());
			_entryValue = MathUtil.add(_entryValueUH, _entryValueP);
			this.planForUp1 = ConvertUtil.parseMoneyToThousandUnit(MathUtil.planForUp(_entryValue, _advancePeriod));

			// [理論値1] 前同比
			_entryValueUH = MathUtil.add(mrPlan.getTheoreticalValueUh1(), mrPlan.getSpecialInsPlanValueUhY());
			_entryValueP = MathUtil.add(mrPlan.getTheoreticalValueP1(), mrPlan.getSpecialInsPlanValuePY());
			_entryValue = MathUtil.add(_entryValueUH, _entryValueP);
			this.lastSameRatio1 = MathUtil.calcRatio(_entryValue, _advancePeriod);

			// [理論値2] 増分
			_entryValueUH = MathUtil.add(mrPlan.getTheoreticalValueUh2(), mrPlan.getSpecialInsPlanValueUhY());
			_entryValueP = MathUtil.add(mrPlan.getTheoreticalValueP2(), mrPlan.getSpecialInsPlanValuePY());
			_entryValue = MathUtil.add(_entryValueUH, _entryValueP);
			this.planForUp2 = ConvertUtil.parseMoneyToThousandUnit(MathUtil.planForUp(_entryValue, _advancePeriod));

			// [理論値2] 前同比
			_entryValueUH = MathUtil.add(mrPlan.getTheoreticalValueUh2(), mrPlan.getSpecialInsPlanValueUhY());
			_entryValueP = MathUtil.add(mrPlan.getTheoreticalValueP2(), mrPlan.getSpecialInsPlanValuePY());
			_entryValue = MathUtil.add(_entryValueUH, _entryValueP);
			this.lastSameRatio2 = MathUtil.calcRatio(_entryValue, _advancePeriod);
		}
	}

	/**
	 * コンストラクタ（担当者別計画チーム内サマリ）
	 * 
	 * @param insType 施設出力区分(UH,P以外の場合は合計)
	 * @param summary チーム内担当者別計画サマリ
	 * @param monNnu 前期実績
	 */
	public MrPlanPlannedValueDto(InsType insType, MrPlanSosSummaryDto summary, MonNnu monNnu) {
		this(insType, summary.convertMrPlan(), monNnu);
	}

	/**
	 * コンストラクタ（チーム別計画用）
	 * 
	 * @param insType 施設出力区分(UH,P以外の場合は合計)
	 * @param mrPlan チーム別計画
	 * @param monNnu 前期実績
	 */
	public MrPlanPlannedValueDto(InsType insType, TeamPlan teamPlan, MonNnu monNnu) {

		// UH(理論値＝理論値
		Long _specialInsPlanValueYUh = ConvertUtil.parseMoneyToThousandUnit(teamPlan.getSpecialInsPlanValueUhY());
		Long _theoreticalValue1Uh = MathUtil.add(ConvertUtil.parseMoneyToThousandUnit(teamPlan.getTheoreticalValueUh1()), _specialInsPlanValueYUh);
		Long _theoreticalValue2Uh = MathUtil.add(ConvertUtil.parseMoneyToThousandUnit(teamPlan.getTheoreticalValueUh2()), _specialInsPlanValueYUh);
		Long _officeValueYUh = ConvertUtil.parseMoneyToThousandUnit(teamPlan.getOfficeValueUhY());
		Long _plannedValueYUh = ConvertUtil.parseMoneyToThousandUnit(teamPlan.getPlannedValueUhY());

		// P
		Long _specialInsPlanValueYP = ConvertUtil.parseMoneyToThousandUnit(teamPlan.getSpecialInsPlanValuePY());
		Long _theoreticalValue1P = MathUtil.add(ConvertUtil.parseMoneyToThousandUnit(teamPlan.getTheoreticalValueP1()), _specialInsPlanValueYP);
		Long _theoreticalValue2P = MathUtil.add(ConvertUtil.parseMoneyToThousandUnit(teamPlan.getTheoreticalValueP2()), _specialInsPlanValueYP);
		Long _officeValueYP = ConvertUtil.parseMoneyToThousandUnit(teamPlan.getOfficeValuePY());
		Long _plannedValueYP = ConvertUtil.parseMoneyToThousandUnit(teamPlan.getPlannedValuePY());

		if (insType == InsType.UH) {
			this.specialInsPlanValueY = _specialInsPlanValueYUh;
			this.theoreticalValue1 = _theoreticalValue1Uh;
			this.theoreticalValue2 = _theoreticalValue2Uh;
			this.officeValueY = _officeValueYUh;
			this.plannedValueY = _plannedValueYUh;
		} else if (insType == InsType.P) {
			this.specialInsPlanValueY = _specialInsPlanValueYP;
			this.theoreticalValue1 = _theoreticalValue1P;
			this.theoreticalValue2 = _theoreticalValue2P;
			this.officeValueY = _officeValueYP;
			this.plannedValueY = _plannedValueYP;
		} else {
			this.specialInsPlanValueY = MathUtil.add(_specialInsPlanValueYUh, _specialInsPlanValueYP);
			this.theoreticalValue1 = MathUtil.add(_theoreticalValue1Uh, _theoreticalValue1P);
			this.theoreticalValue2 = MathUtil.add(_theoreticalValue2Uh, _theoreticalValue2P);
			this.officeValueY = MathUtil.add(_officeValueYUh, _officeValueYP);
			this.plannedValueY = MathUtil.add(_plannedValueYUh, _plannedValueYP);
		}

		// 非表示用計画値
		this.hiddenPlannedValueUhY = _plannedValueYUh;
		this.hiddenPlannedValuePY = _plannedValueYP;

		// ----------------------------------
		// 前期実績
		// ----------------------------------
		Long _advancePeriod = null;
		Long _entryValue = null;

		// 円単位で計算
		if (monNnu != null) {
			_advancePeriod = monNnu.getAdvancePeriod();
		}
		this.hiddenAdvancePeriodUnitYen = _advancePeriod;

		if (insType == InsType.UH) {

			// [理論値1] 増分
			_entryValue = MathUtil.add(teamPlan.getTheoreticalValueUh1(), teamPlan.getSpecialInsPlanValueUhY());
			this.planForUp1 = ConvertUtil.parseMoneyToThousandUnit(MathUtil.planForUp(_entryValue, _advancePeriod));

			// [理論値1] 前同比
			_entryValue = MathUtil.add(teamPlan.getTheoreticalValueUh1(), teamPlan.getSpecialInsPlanValueUhY());
			this.lastSameRatio1 = MathUtil.calcRatio(_entryValue, _advancePeriod);

			// [理論値2] 増分
			_entryValue = MathUtil.add(teamPlan.getTheoreticalValueUh2(), teamPlan.getSpecialInsPlanValueUhY());
			this.planForUp2 = ConvertUtil.parseMoneyToThousandUnit(MathUtil.planForUp(_entryValue, _advancePeriod));

			// [理論値2] 前同比
			_entryValue = MathUtil.add(teamPlan.getTheoreticalValueUh2(), teamPlan.getSpecialInsPlanValueUhY());
			this.lastSameRatio2 = MathUtil.calcRatio(_entryValue, _advancePeriod);

		} else if (insType == InsType.P) {

			// [理論値1] 増分
			_entryValue = MathUtil.add(teamPlan.getTheoreticalValueP1(), teamPlan.getSpecialInsPlanValuePY());
			this.planForUp1 = ConvertUtil.parseMoneyToThousandUnit(MathUtil.planForUp(_entryValue, _advancePeriod));

			// [理論値1] 前同比
			_entryValue = MathUtil.add(teamPlan.getTheoreticalValueP1(), teamPlan.getSpecialInsPlanValuePY());
			this.lastSameRatio1 = MathUtil.calcRatio(_entryValue, _advancePeriod);

			// [理論値2] 増分
			_entryValue = MathUtil.add(teamPlan.getTheoreticalValueP2(), teamPlan.getSpecialInsPlanValuePY());
			this.planForUp2 = ConvertUtil.parseMoneyToThousandUnit(MathUtil.planForUp(_entryValue, _advancePeriod));

			// [理論値2] 前同比
			_entryValue = MathUtil.add(teamPlan.getTheoreticalValueP2(), teamPlan.getSpecialInsPlanValuePY());
			this.lastSameRatio2 = MathUtil.calcRatio(_entryValue, _advancePeriod);

		} else {

			Long _entryValueUH = null;
			Long _entryValueP = null;

			// [理論値1] 増分
			_entryValueUH = MathUtil.add(teamPlan.getTheoreticalValueUh1(), teamPlan.getSpecialInsPlanValueUhY());
			_entryValueP = MathUtil.add(teamPlan.getTheoreticalValueP1(), teamPlan.getSpecialInsPlanValuePY());
			_entryValue = MathUtil.add(_entryValueUH, _entryValueP);
			this.planForUp1 = ConvertUtil.parseMoneyToThousandUnit(MathUtil.planForUp(_entryValue, _advancePeriod));

			// [理論値1] 前同比
			_entryValueUH = MathUtil.add(teamPlan.getTheoreticalValueUh1(), teamPlan.getSpecialInsPlanValueUhY());
			_entryValueP = MathUtil.add(teamPlan.getTheoreticalValueP1(), teamPlan.getSpecialInsPlanValuePY());
			_entryValue = MathUtil.add(_entryValueUH, _entryValueP);
			this.lastSameRatio1 = MathUtil.calcRatio(_entryValue, _advancePeriod);

			// [理論値2] 増分
			_entryValueUH = MathUtil.add(teamPlan.getTheoreticalValueUh2(), teamPlan.getSpecialInsPlanValueUhY());
			_entryValueP = MathUtil.add(teamPlan.getTheoreticalValueP2(), teamPlan.getSpecialInsPlanValuePY());
			_entryValue = MathUtil.add(_entryValueUH, _entryValueP);
			this.planForUp2 = ConvertUtil.parseMoneyToThousandUnit(MathUtil.planForUp(_entryValue, _advancePeriod));

			// [理論値2] 前同比
			_entryValueUH = MathUtil.add(teamPlan.getTheoreticalValueUh2(), teamPlan.getSpecialInsPlanValueUhY());
			_entryValueP = MathUtil.add(teamPlan.getTheoreticalValueP2(), teamPlan.getSpecialInsPlanValuePY());
			_entryValue = MathUtil.add(_entryValueUH, _entryValueP);
			this.lastSameRatio2 = MathUtil.calcRatio(_entryValue, _advancePeriod);
		}
	}

	/**
	 * コンストラクタ（チーム別計画チーム内サマリ）
	 * 
	 * @param insType 施設出力区分(UH,P以外の場合は合計)
	 * @param summary 営業所内チーム別計画サマリ
	 * @param monNnu 前期実績
	 */
	public MrPlanPlannedValueDto(InsType insType, TeamPlanSosSummaryDto summary, MonNnu monNnu) {
		this(insType, summary.convertTeamPlan(), monNnu);
	}

	/**
	 * コンストラクタ（営業所計画）<br>
	 * 特定施設個別計画、理論値は、チーム別計画の合計を設定する。
	 * 
	 * @param insType 施設出力区分(UH,P以外の場合は合計)
	 * @param officePlan 営業所計画
	 * @param summary 営業所内チーム別計画サマリ
	 * @param monNnu 前期実績
	 */
	public MrPlanPlannedValueDto(InsType insType, OfficePlan officePlan, TeamPlanSosSummaryDto summary, MonNnu monNnu) {

		// UH(理論値＝理論値
		Long _specialInsPlanValueYUh = ConvertUtil.parseMoneyToThousandUnit(summary.getSpecialInsPlanValueUhY());
		Long _theoreticalValue1Uh = MathUtil.add(ConvertUtil.parseMoneyToThousandUnit(summary.getTheoreticalValueUh1()), _specialInsPlanValueYUh);
		Long _theoreticalValue2Uh = MathUtil.add(ConvertUtil.parseMoneyToThousandUnit(summary.getTheoreticalValueUh2()), _specialInsPlanValueYUh);
		Long _officeValueYUh = ConvertUtil.parseMoneyToThousandUnit(officePlan.getPlannedValueUhY());
		Long _plannedValueYUh = ConvertUtil.parseMoneyToThousandUnit(officePlan.getPlannedValueUhY());

		// P
		Long _specialInsPlanValueYP = ConvertUtil.parseMoneyToThousandUnit(summary.getSpecialInsPlanValuePY());
		Long _theoreticalValue1P = MathUtil.add(ConvertUtil.parseMoneyToThousandUnit(summary.getTheoreticalValueP1()), _specialInsPlanValueYP);
		Long _theoreticalValue2P = MathUtil.add(ConvertUtil.parseMoneyToThousandUnit(summary.getTheoreticalValueP2()), _specialInsPlanValueYP);
		Long _officeValueYP = ConvertUtil.parseMoneyToThousandUnit(officePlan.getPlannedValuePY());
		Long _plannedValueYP = ConvertUtil.parseMoneyToThousandUnit(officePlan.getPlannedValuePY());

		if (insType == InsType.UH) {
			this.specialInsPlanValueY = _specialInsPlanValueYUh;
			this.theoreticalValue1 = _theoreticalValue1Uh;
			this.theoreticalValue2 = _theoreticalValue2Uh;
			this.officeValueY = _officeValueYUh;
			this.plannedValueY = _plannedValueYUh;
		} else if (insType == InsType.P) {
			this.specialInsPlanValueY = _specialInsPlanValueYP;
			this.theoreticalValue1 = _theoreticalValue1P;
			this.theoreticalValue2 = _theoreticalValue2P;
			this.officeValueY = _officeValueYP;
			this.plannedValueY = _plannedValueYP;
		} else {
			this.specialInsPlanValueY = MathUtil.add(_specialInsPlanValueYUh, _specialInsPlanValueYP);
			this.theoreticalValue1 = MathUtil.add(_theoreticalValue1Uh, _theoreticalValue1P);
			this.theoreticalValue2 = MathUtil.add(_theoreticalValue2Uh, _theoreticalValue2P);
			this.officeValueY = MathUtil.add(_officeValueYUh, _officeValueYP);
			this.plannedValueY = MathUtil.add(_plannedValueYUh, _plannedValueYP);
		}

		// 非表示用計画値
		this.hiddenPlannedValueUhY = _plannedValueYUh;
		this.hiddenPlannedValuePY = _plannedValueYP;

		// ----------------------------------
		// 前期実績
		// ----------------------------------
		Long _advancePeriod = null;
		Long _entryValue = null;

		// 円単位で計算
		if (monNnu != null) {
			_advancePeriod = monNnu.getAdvancePeriod();
		}
		this.hiddenAdvancePeriodUnitYen = _advancePeriod;

		if (insType == InsType.UH) {

			// [理論値1] 増分
			_entryValue = MathUtil.add(summary.getTheoreticalValueUh1(), summary.getSpecialInsPlanValueUhY());
			this.planForUp1 = ConvertUtil.parseMoneyToThousandUnit(MathUtil.planForUp(_entryValue, _advancePeriod));

			// [理論値1] 前同比
			_entryValue = MathUtil.add(summary.getTheoreticalValueUh1(), summary.getSpecialInsPlanValueUhY());
			this.lastSameRatio1 = MathUtil.calcRatio(_entryValue, _advancePeriod);

			// [理論値2] 増分
			_entryValue = MathUtil.add(summary.getTheoreticalValueUh2(), summary.getSpecialInsPlanValueUhY());
			this.planForUp2 = ConvertUtil.parseMoneyToThousandUnit(MathUtil.planForUp(_entryValue, _advancePeriod));

			// [理論値2] 前同比
			_entryValue = MathUtil.add(summary.getTheoreticalValueUh2(), summary.getSpecialInsPlanValueUhY());
			this.lastSameRatio2 = MathUtil.calcRatio(_entryValue, _advancePeriod);

		} else if (insType == InsType.P) {

			// [理論値1] 増分
			_entryValue = MathUtil.add(summary.getTheoreticalValueP1(), summary.getSpecialInsPlanValuePY());
			this.planForUp1 = ConvertUtil.parseMoneyToThousandUnit(MathUtil.planForUp(_entryValue, _advancePeriod));

			// [理論値1] 前同比
			_entryValue = MathUtil.add(summary.getTheoreticalValueP1(), summary.getSpecialInsPlanValuePY());
			this.lastSameRatio1 = MathUtil.calcRatio(_entryValue, _advancePeriod);

			// [理論値2] 増分
			_entryValue = MathUtil.add(summary.getTheoreticalValueP2(), summary.getSpecialInsPlanValuePY());
			this.planForUp2 = ConvertUtil.parseMoneyToThousandUnit(MathUtil.planForUp(_entryValue, _advancePeriod));

			// [理論値2] 前同比
			_entryValue = MathUtil.add(summary.getTheoreticalValueP2(), summary.getSpecialInsPlanValuePY());
			this.lastSameRatio2 = MathUtil.calcRatio(_entryValue, _advancePeriod);

		} else {

			Long _entryValueUH = null;
			Long _entryValueP = null;

			// [理論値1] 増分
			_entryValueUH = MathUtil.add(summary.getTheoreticalValueUh1(), summary.getSpecialInsPlanValueUhY());
			_entryValueP = MathUtil.add(summary.getTheoreticalValueP1(), summary.getSpecialInsPlanValuePY());
			_entryValue = MathUtil.add(_entryValueUH, _entryValueP);
			this.planForUp1 = ConvertUtil.parseMoneyToThousandUnit(MathUtil.planForUp(_entryValue, _advancePeriod));

			// [理論値1] 前同比
			_entryValueUH = MathUtil.add(summary.getTheoreticalValueUh1(), summary.getSpecialInsPlanValueUhY());
			_entryValueP = MathUtil.add(summary.getTheoreticalValueP1(), summary.getSpecialInsPlanValuePY());
			_entryValue = MathUtil.add(_entryValueUH, _entryValueP);
			this.lastSameRatio1 = MathUtil.calcRatio(_entryValue, _advancePeriod);

			// [理論値2] 増分
			_entryValueUH = MathUtil.add(summary.getTheoreticalValueUh2(), summary.getSpecialInsPlanValueUhY());
			_entryValueP = MathUtil.add(summary.getTheoreticalValueP2(), summary.getSpecialInsPlanValuePY());
			_entryValue = MathUtil.add(_entryValueUH, _entryValueP);
			this.planForUp2 = ConvertUtil.parseMoneyToThousandUnit(MathUtil.planForUp(_entryValue, _advancePeriod));

			// [理論値2] 前同比
			_entryValueUH = MathUtil.add(summary.getTheoreticalValueUh2(), summary.getSpecialInsPlanValueUhY());
			_entryValueP = MathUtil.add(summary.getTheoreticalValueP2(), summary.getSpecialInsPlanValuePY());
			_entryValue = MathUtil.add(_entryValueUH, _entryValueP);
			this.lastSameRatio2 = MathUtil.calcRatio(_entryValue, _advancePeriod);
		}
	}

	/**
	 * 特定施設個別計画値(Y)を取得する。
	 * 
	 * @return 特定施設個別計画値(Y)
	 */
	public Long getSpecialInsPlanValueY() {
		return specialInsPlanValueY;
	}

	/**
	 * [理論値1] 増分([理論値1] 計画-前期実績)を取得する。
	 * 
	 * @return [理論値1] 増分([理論値1] 計画-前期実績)
	 */
	public Long getPlanForUp1() {
		return planForUp1;
	}

	/**
	 * [理論値1] 計画を取得する。
	 * 
	 * @return [理論値1] 計画
	 */
	public Long getTheoreticalValue1() {
		return theoreticalValue1;
	}

	/**
	 * [理論値1] 前同比([理論値1] 計画/前期実績)を取得する。
	 * 
	 * @return [理論値1] 前同比([理論値1] 計画/前期実績)
	 */
	public Double getLastSameRatio1() {
		return lastSameRatio1;
	}

	/**
	 * [理論値2] 増分([理論値2] 計画-前期実績)を取得する。
	 * 
	 * @return [理論値2] 増分([理論値2] 計画-前期実績)
	 */
	public Long getPlanForUp2() {
		return planForUp2;
	}

	/**
	 * [理論値2] 計画を取得する。
	 * 
	 * @return [理論値2] 計画
	 */
	public Long getTheoreticalValue2() {
		return theoreticalValue2;
	}

	/**
	 * [理論値2] 前同比([理論値2] 計画/前期実績)を取得する。
	 * 
	 * @return [理論値2] 前同比([理論値2] 計画/前期実績)
	 */
	public Double getLastSameRatio2() {
		return lastSameRatio2;
	}

	/**
	 * 営業所案(Y)を取得する。
	 * 
	 * @return 営業所案(Y)
	 */
	public Long getOfficeValueY() {
		return officeValueY;
	}

	/**
	 * 計画値(Y)を取得する。
	 * 
	 * @return 計画値(Y)
	 */
	public Long getPlannedValueY() {
		return plannedValueY;
	}

	/**
	 * 非表示用計画値UH(Y)を取得する。
	 * 
	 * @return 非表示用計画値UH(Y)
	 */
	public Long getHiddenPlannedValueUhY() {
		return hiddenPlannedValueUhY;
	}

	/**
	 * 非表示用計画値P(Y)を取得する。
	 * 
	 * @return 非表示用計画値P(Y)
	 */
	public Long getHiddenPlannedValuePY() {
		return hiddenPlannedValuePY;
	}

	/**
	 * 非表示用前期実績(円単位)を取得する。
	 * 
	 * @return 非表示用前期実績(円単位)
	 */
	public Long getHiddenAdvancePeriodUnitYen() {
		return hiddenAdvancePeriodUnitYen;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
