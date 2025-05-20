package jp.co.takeda.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.ManageBranchMonthPlan;
import jp.co.takeda.model.ManageIyakuMonthPlan;
import jp.co.takeda.model.ManageMrMonthPlan;
import jp.co.takeda.model.ManageOfficeMonthPlan;
import jp.co.takeda.util.ConvertUtil;

/**
 * 品目別月別計画の検索結果 明細行を表すDTO
 *
 * @author stakeuchi
 */
public class ProdMonthPlanResultDetailDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 品目コード
	 */
	private final String prodCode;

	/**
	 * 品目名
	 */
	private final String prodName;

	/**
	 *  月初計画1(YB価)
	 */
	private final Long planned1ValueYb;

	/**
	 *  月初計画2(YB価)
	 */
	private final Long planned2ValueYb;

	/**
	 *  月初計画3(YB価)
	 */
	private final Long planned3ValueYb;

	/**
	 *  月初計画4(YB価)
	 */
	private final Long planned4ValueYb;

	/**
	 *  月初計画5(YB価)
	 */
	private final Long planned5ValueYb;

	/**
	 *  月初計画6(YB価)
	 */
	private final Long planned6ValueYb;

	/**
	 * 月末見込1(YB価)
	 */
	private final Long expected1ValueYb;

	/**
	 * 月末見込2(YB価)
	 */
	private final Long expected2ValueYb;

	/**
	 * 月末見込3(YB価)
	 */
	private final Long expected3ValueYb;

	/**
	 * 月末見込4(YB価)
	 */
	private final Long expected4ValueYb;

	/**
	 * 月末見込5(YB価)
	 */
	private final Long expected5ValueYb;

	/**
	 * 月末見込6(YB価)
	 */
	private final Long expected6ValueYb;

	/**
	 * 下位計画・月初計画積上値(1月目)
	 */
	private final Long plannedStacked1Value;

	/**
	 * 下位計画・月初計画積上値(2月目)
	 */
	private final Long plannedStacked2Value;

	/**
	 * 下位計画・月初計画積上値(3月目)
	 */
	private final Long plannedStacked3Value;

	/**
	 * 下位計画・月初計画積上値(4月目)
	 */
	private final Long plannedStacked4Value;

	/**
	 * 下位計画・月初計画積上値(5月目)
	 */
	private final Long plannedStacked5Value;

	/**
	 * 下位計画・月初計画積上値(6月目)
	 */
	private final Long plannedStacked6Value;

	/**
	 * 下位計画・月末見込積上値(1月目)
	 */
	private final Long expectedStacked1Value;

	/**
	 * 下位計画・月末見込積上値(2月目)
	 */
	private final Long expectedStacked2Value;

	/**
	 * 下位計画・月末見込積上値(3月目)
	 */
	private final Long expectedStacked3Value;

	/**
	 * 下位計画・月末見込積上値(4月目)
	 */
	private final Long expectedStacked4Value;

	/**
	 * 下位計画・月末見込積上値(5月目)
	 */
	private final Long expectedStacked5Value;

	/**
	 * 下位計画・月末見込積上値(6月目)
	 */
	private final Long expectedStacked6Value;

	/**
	 * 期別計画値
	 */
	private final Long plannedTermValue;

//add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
	/**
	 *実績1（YB価）
	 */
	private final Double record1ValueYb;

	/**
	 *実績2（YB価）
	 */
	private final Double record2ValueYb;

	/**
	 *実績3（YB価）
	 */
	private final Double record3ValueYb;

	/**
	 *実績4（YB価）
	 */
	private final Double record4ValueYb;

	/**
	 *実績5（YB価）
	 */
	private final Double record5ValueYb;

	/**
	 *実績6（YB価）
	 */
	private final Double record6ValueYb;

	/**
	 *累計実績（YB価）
	 */
	private final Double recordTotalValueYb;

	/**
	 *当月実績（YB価）
	 */
	private final Double recordTougetuValueYb;
//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

	/**
	 * 品目集計対象フラグ
	 */
	private final String targetSummary;

	/**
	 * シーケンスキー
	 */
	private final Long seqKey;

	/**
	 * 最終更新日時
	 */
	private final Date upDate;

	/**
	 * 最終更新者
	 */
	private final String upJgiName;

	/**
	 * 下位プランレベル移行可否フラグ
	 * <ul>
	 * <li>TRUE = 下位計画レベルへ移行可能</li>
	 * <li>FALSE = 下位計画レベルへ移行不可</li>
	 * </ul>
	 */
	private final boolean canMoveDownPlanLevel;

	/**
	 * 雑組織フラグ(true：雑組織、false：雑組織でない)
	 */
	private final Boolean etcSosFlg;

	/**
	 * コンストラクタ(全社計画)
	 *
	 * @param iyakuPlan 全社別計画
	 * @param canMoveDownPlanLevel 下位プランレベル移行可否フラグ
	 */
	public ProdMonthPlanResultDetailDto(ManageIyakuMonthPlan iyakuPlan, boolean canMoveDownPlanLevel) {
		this.prodCode = iyakuPlan.getProdCode();
		this.prodName = iyakuPlan.getProdName();
		if (iyakuPlan.getImplMonthPlan() != null) {
			this.planned1ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(iyakuPlan.getImplMonthPlan().getPlanned1ValueYb());
			this.planned2ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(iyakuPlan.getImplMonthPlan().getPlanned2ValueYb());
			this.planned3ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(iyakuPlan.getImplMonthPlan().getPlanned3ValueYb());
			this.planned4ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(iyakuPlan.getImplMonthPlan().getPlanned4ValueYb());
			this.planned5ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(iyakuPlan.getImplMonthPlan().getPlanned5ValueYb());
			this.planned6ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(iyakuPlan.getImplMonthPlan().getPlanned6ValueYb());
			this.expected1ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(iyakuPlan.getImplMonthPlan().getExpected1ValueYb());
			this.expected2ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(iyakuPlan.getImplMonthPlan().getExpected2ValueYb());
			this.expected3ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(iyakuPlan.getImplMonthPlan().getExpected3ValueYb());
			this.expected4ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(iyakuPlan.getImplMonthPlan().getExpected4ValueYb());
			this.expected5ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(iyakuPlan.getImplMonthPlan().getExpected5ValueYb());
			this.expected6ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(iyakuPlan.getImplMonthPlan().getExpected6ValueYb());

//add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
			this.record1ValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(iyakuPlan.getImplMonthPlan().getRecord1ValueYb());
			this.record2ValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(iyakuPlan.getImplMonthPlan().getRecord2ValueYb());
			this.record3ValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(iyakuPlan.getImplMonthPlan().getRecord3ValueYb());
			this.record4ValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(iyakuPlan.getImplMonthPlan().getRecord4ValueYb());
			this.record5ValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(iyakuPlan.getImplMonthPlan().getRecord5ValueYb());
			this.record6ValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(iyakuPlan.getImplMonthPlan().getRecord6ValueYb());
			this.recordTotalValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(iyakuPlan.getImplMonthPlan().getRecordTotalValueYb());
			this.recordTougetuValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(iyakuPlan.getImplMonthPlan().getRecordTougetuValueYb());
//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		} else {
			this.planned1ValueYb = null;
			this.planned2ValueYb = null;
			this.planned3ValueYb = null;
			this.planned4ValueYb = null;
			this.planned5ValueYb = null;
			this.planned6ValueYb = null;

			this.expected1ValueYb = null;
			this.expected2ValueYb = null;
			this.expected3ValueYb = null;
			this.expected4ValueYb = null;
			this.expected5ValueYb = null;
			this.expected6ValueYb = null;

//add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
			this.record1ValueYb = null;
			this.record2ValueYb = null;
			this.record3ValueYb = null;
			this.record4ValueYb = null;
			this.record5ValueYb = null;
			this.record6ValueYb = null;
			this.recordTotalValueYb = null;
			this.recordTougetuValueYb = null;
//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		}

		this.plannedStacked1Value = ConvertUtil.parseMoneyToThousandUnit(iyakuPlan.getPlannedStacked1Value());
		this.plannedStacked2Value = ConvertUtil.parseMoneyToThousandUnit(iyakuPlan.getPlannedStacked2Value());
		this.plannedStacked3Value = ConvertUtil.parseMoneyToThousandUnit(iyakuPlan.getPlannedStacked3Value());
		this.plannedStacked4Value = ConvertUtil.parseMoneyToThousandUnit(iyakuPlan.getPlannedStacked4Value());
		this.plannedStacked5Value = ConvertUtil.parseMoneyToThousandUnit(iyakuPlan.getPlannedStacked5Value());
		this.plannedStacked6Value = ConvertUtil.parseMoneyToThousandUnit(iyakuPlan.getPlannedStacked6Value());

		this.expectedStacked1Value = ConvertUtil.parseMoneyToThousandUnit(iyakuPlan.getExpectedStacked1Value());
		this.expectedStacked2Value = ConvertUtil.parseMoneyToThousandUnit(iyakuPlan.getExpectedStacked2Value());
		this.expectedStacked3Value = ConvertUtil.parseMoneyToThousandUnit(iyakuPlan.getExpectedStacked3Value());
		this.expectedStacked4Value = ConvertUtil.parseMoneyToThousandUnit(iyakuPlan.getExpectedStacked4Value());
		this.expectedStacked5Value = ConvertUtil.parseMoneyToThousandUnit(iyakuPlan.getExpectedStacked5Value());
		this.expectedStacked6Value = ConvertUtil.parseMoneyToThousandUnit(iyakuPlan.getExpectedStacked6Value());

		this.plannedTermValue = ConvertUtil.parseMoneyToThousandUnit(iyakuPlan.getPlannedTermValue());
		this.targetSummary = iyakuPlan.getTargetSummary();
		this.seqKey = iyakuPlan.getSeqKey();
		this.upDate = iyakuPlan.getUpDate();
		if (upDate != null) {
			this.upJgiName = iyakuPlan.getUpJgiName();
		} else {
			this.upJgiName = null;
		}
		this.canMoveDownPlanLevel = canMoveDownPlanLevel;
		this.etcSosFlg = Boolean.FALSE;
	}

	/**
	 * コンストラクタ(支店別計画)
	 *
	 * @param branchPlan 支店別計画
	 * @param canMoveDownPlanLevel 下位プランレベル移行可否フラグ
	 */
	public ProdMonthPlanResultDetailDto(ManageBranchMonthPlan branchPlan, boolean canMoveDownPlanLevel) {
		this.prodCode = branchPlan.getProdCode();
		this.prodName = branchPlan.getProdName();
		if (branchPlan.getImplMonthPlan() != null) {
			this.planned1ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(branchPlan.getImplMonthPlan().getPlanned1ValueYb());
			this.planned2ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(branchPlan.getImplMonthPlan().getPlanned2ValueYb());
			this.planned3ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(branchPlan.getImplMonthPlan().getPlanned3ValueYb());
			this.planned4ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(branchPlan.getImplMonthPlan().getPlanned4ValueYb());
			this.planned5ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(branchPlan.getImplMonthPlan().getPlanned5ValueYb());
			this.planned6ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(branchPlan.getImplMonthPlan().getPlanned6ValueYb());
			this.expected1ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(branchPlan.getImplMonthPlan().getExpected1ValueYb());
			this.expected2ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(branchPlan.getImplMonthPlan().getExpected2ValueYb());
			this.expected3ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(branchPlan.getImplMonthPlan().getExpected3ValueYb());
			this.expected4ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(branchPlan.getImplMonthPlan().getExpected4ValueYb());
			this.expected5ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(branchPlan.getImplMonthPlan().getExpected5ValueYb());
			this.expected6ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(branchPlan.getImplMonthPlan().getExpected6ValueYb());

//add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
			this.record1ValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(branchPlan.getImplMonthPlan().getRecord1ValueYb());
			this.record2ValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(branchPlan.getImplMonthPlan().getRecord2ValueYb());
			this.record3ValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(branchPlan.getImplMonthPlan().getRecord3ValueYb());
			this.record4ValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(branchPlan.getImplMonthPlan().getRecord4ValueYb());
			this.record5ValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(branchPlan.getImplMonthPlan().getRecord5ValueYb());
			this.record6ValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(branchPlan.getImplMonthPlan().getRecord6ValueYb());
			this.recordTotalValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(branchPlan.getImplMonthPlan().getRecordTotalValueYb());
			this.recordTougetuValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(branchPlan.getImplMonthPlan().getRecordTougetuValueYb());
//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		} else {
			this.planned1ValueYb = null;
			this.planned2ValueYb = null;
			this.planned3ValueYb = null;
			this.planned4ValueYb = null;
			this.planned5ValueYb = null;
			this.planned6ValueYb = null;

			this.expected1ValueYb = null;
			this.expected2ValueYb = null;
			this.expected3ValueYb = null;
			this.expected4ValueYb = null;
			this.expected5ValueYb = null;
			this.expected6ValueYb = null;

//add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
			this.record1ValueYb = null;
			this.record2ValueYb = null;
			this.record3ValueYb = null;
			this.record4ValueYb = null;
			this.record5ValueYb = null;
			this.record6ValueYb = null;
			this.recordTotalValueYb = null;
			this.recordTougetuValueYb = null;
//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		}

		this.plannedStacked1Value = ConvertUtil.parseMoneyToThousandUnit(branchPlan.getPlannedStacked1Value());
		this.plannedStacked2Value = ConvertUtil.parseMoneyToThousandUnit(branchPlan.getPlannedStacked2Value());
		this.plannedStacked3Value = ConvertUtil.parseMoneyToThousandUnit(branchPlan.getPlannedStacked3Value());
		this.plannedStacked4Value = ConvertUtil.parseMoneyToThousandUnit(branchPlan.getPlannedStacked4Value());
		this.plannedStacked5Value = ConvertUtil.parseMoneyToThousandUnit(branchPlan.getPlannedStacked5Value());
		this.plannedStacked6Value = ConvertUtil.parseMoneyToThousandUnit(branchPlan.getPlannedStacked6Value());

		this.expectedStacked1Value = ConvertUtil.parseMoneyToThousandUnit(branchPlan.getExpectedStacked1Value());
		this.expectedStacked2Value = ConvertUtil.parseMoneyToThousandUnit(branchPlan.getExpectedStacked2Value());
		this.expectedStacked3Value = ConvertUtil.parseMoneyToThousandUnit(branchPlan.getExpectedStacked3Value());
		this.expectedStacked4Value = ConvertUtil.parseMoneyToThousandUnit(branchPlan.getExpectedStacked4Value());
		this.expectedStacked5Value = ConvertUtil.parseMoneyToThousandUnit(branchPlan.getExpectedStacked5Value());
		this.expectedStacked6Value = ConvertUtil.parseMoneyToThousandUnit(branchPlan.getExpectedStacked6Value());

		this.plannedTermValue = ConvertUtil.parseMoneyToThousandUnit(branchPlan.getPlannedTermValue());
		this.targetSummary = branchPlan.getTargetSummary();
		this.seqKey = branchPlan.getSeqKey();
		this.upDate = branchPlan.getUpDate();
		if (upDate != null) {
			this.upJgiName = branchPlan.getUpJgiName();
		} else {
			this.upJgiName = null;
		}

		this.canMoveDownPlanLevel = canMoveDownPlanLevel;
		this.etcSosFlg = Boolean.FALSE;
	}

	/**
	 * コンストラクタ(営業所別計画)
	 *
	 * @param officePlan 営業所別計画
	 * @param canMoveDownPlanLevel 下位プランレベル移行可否フラグ
	 */
	public ProdMonthPlanResultDetailDto(ManageOfficeMonthPlan officePlan, boolean canMoveDownPlanLevel) {
		this.prodCode = officePlan.getProdCode();
		this.prodName = officePlan.getProdName();
		if (officePlan.getImplMonthPlan() != null) {
			this.planned1ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(officePlan.getImplMonthPlan().getPlanned1ValueYb());
			this.planned2ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(officePlan.getImplMonthPlan().getPlanned2ValueYb());
			this.planned3ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(officePlan.getImplMonthPlan().getPlanned3ValueYb());
			this.planned4ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(officePlan.getImplMonthPlan().getPlanned4ValueYb());
			this.planned5ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(officePlan.getImplMonthPlan().getPlanned5ValueYb());
			this.planned6ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(officePlan.getImplMonthPlan().getPlanned6ValueYb());

			this.expected1ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(officePlan.getImplMonthPlan().getExpected1ValueYb());
			this.expected2ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(officePlan.getImplMonthPlan().getExpected2ValueYb());
			this.expected3ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(officePlan.getImplMonthPlan().getExpected3ValueYb());
			this.expected4ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(officePlan.getImplMonthPlan().getExpected4ValueYb());
			this.expected5ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(officePlan.getImplMonthPlan().getExpected5ValueYb());
			this.expected6ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(officePlan.getImplMonthPlan().getExpected6ValueYb());

//add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
			this.record1ValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(officePlan.getImplMonthPlan().getRecord1ValueYb());
			this.record2ValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(officePlan.getImplMonthPlan().getRecord2ValueYb());
			this.record3ValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(officePlan.getImplMonthPlan().getRecord3ValueYb());
			this.record4ValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(officePlan.getImplMonthPlan().getRecord4ValueYb());
			this.record5ValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(officePlan.getImplMonthPlan().getRecord5ValueYb());
			this.record6ValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(officePlan.getImplMonthPlan().getRecord6ValueYb());
			this.recordTotalValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(officePlan.getImplMonthPlan().getRecordTotalValueYb());
			this.recordTougetuValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(officePlan.getImplMonthPlan().getRecordTougetuValueYb());
//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		} else {
			this.planned1ValueYb = null;
			this.planned2ValueYb = null;
			this.planned3ValueYb = null;
			this.planned4ValueYb = null;
			this.planned5ValueYb = null;
			this.planned6ValueYb = null;

			this.expected1ValueYb = null;
			this.expected2ValueYb = null;
			this.expected3ValueYb = null;
			this.expected4ValueYb = null;
			this.expected5ValueYb = null;
			this.expected6ValueYb = null;

//add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
			this.record1ValueYb = null;
			this.record2ValueYb = null;
			this.record3ValueYb = null;
			this.record4ValueYb = null;
			this.record5ValueYb = null;
			this.record6ValueYb = null;
			this.recordTotalValueYb = null;
			this.recordTougetuValueYb = null;
//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		}

		this.plannedStacked1Value = ConvertUtil.parseMoneyToThousandUnit(officePlan.getPlannedStacked1Value());
		this.plannedStacked2Value = ConvertUtil.parseMoneyToThousandUnit(officePlan.getPlannedStacked2Value());
		this.plannedStacked3Value = ConvertUtil.parseMoneyToThousandUnit(officePlan.getPlannedStacked3Value());
		this.plannedStacked4Value = ConvertUtil.parseMoneyToThousandUnit(officePlan.getPlannedStacked4Value());
		this.plannedStacked5Value = ConvertUtil.parseMoneyToThousandUnit(officePlan.getPlannedStacked5Value());
		this.plannedStacked6Value = ConvertUtil.parseMoneyToThousandUnit(officePlan.getPlannedStacked6Value());

		this.expectedStacked1Value = ConvertUtil.parseMoneyToThousandUnit(officePlan.getExpectedStacked1Value());
		this.expectedStacked2Value = ConvertUtil.parseMoneyToThousandUnit(officePlan.getExpectedStacked2Value());
		this.expectedStacked3Value = ConvertUtil.parseMoneyToThousandUnit(officePlan.getExpectedStacked3Value());
		this.expectedStacked4Value = ConvertUtil.parseMoneyToThousandUnit(officePlan.getExpectedStacked4Value());
		this.expectedStacked5Value = ConvertUtil.parseMoneyToThousandUnit(officePlan.getExpectedStacked5Value());
		this.expectedStacked6Value = ConvertUtil.parseMoneyToThousandUnit(officePlan.getExpectedStacked6Value());

		this.plannedTermValue = ConvertUtil.parseMoneyToThousandUnit(officePlan.getPlannedTermValue());
		this.targetSummary = officePlan.getTargetSummary();
		this.seqKey = officePlan.getSeqKey();
		this.upDate = officePlan.getUpDate();
		if (upDate != null) {
			this.upJgiName = officePlan.getUpJgiName();
		} else {
			this.upJgiName = null;
		}
		this.canMoveDownPlanLevel = canMoveDownPlanLevel;
		this.etcSosFlg = Boolean.FALSE;
	}

	/**
	 * コンストラクタ(担当者別計画)
	 *
	 * @param mrPlan 担当者別計画
	 * @param canMoveDownPlanLevel 下位プランレベル移行可否フラグ
	 */
	public ProdMonthPlanResultDetailDto(ManageMrMonthPlan mrPlan, boolean canMoveDownPlanLevel) {
		this.prodCode = mrPlan.getProdCode();
		this.prodName = mrPlan.getProdName();
		if (mrPlan.getImplMonthPlan() != null) {
			this.planned1ValueYb = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getImplMonthPlan().getPlanned1ValueYb());
			this.planned2ValueYb = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getImplMonthPlan().getPlanned2ValueYb());
			this.planned3ValueYb = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getImplMonthPlan().getPlanned3ValueYb());
			this.planned4ValueYb = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getImplMonthPlan().getPlanned4ValueYb());
			this.planned5ValueYb = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getImplMonthPlan().getPlanned5ValueYb());
			this.planned6ValueYb = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getImplMonthPlan().getPlanned6ValueYb());

			this.expected1ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(mrPlan.getImplMonthPlan().getExpected1ValueYb());
			this.expected2ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(mrPlan.getImplMonthPlan().getExpected2ValueYb());
			this.expected3ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(mrPlan.getImplMonthPlan().getExpected3ValueYb());
			this.expected4ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(mrPlan.getImplMonthPlan().getExpected4ValueYb());
			this.expected5ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(mrPlan.getImplMonthPlan().getExpected5ValueYb());
			this.expected6ValueYb = ConvertUtil
					.parseMoneyToThousandUnit(mrPlan.getImplMonthPlan().getExpected6ValueYb());

//add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
			this.record1ValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(mrPlan.getImplMonthPlan().getRecord1ValueYb());
			this.record2ValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(mrPlan.getImplMonthPlan().getRecord2ValueYb());
			this.record3ValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(mrPlan.getImplMonthPlan().getRecord3ValueYb());
			this.record4ValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(mrPlan.getImplMonthPlan().getRecord4ValueYb());
			this.record5ValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(mrPlan.getImplMonthPlan().getRecord5ValueYb());
			this.record6ValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(mrPlan.getImplMonthPlan().getRecord6ValueYb());
			this.recordTotalValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(mrPlan.getImplMonthPlan().getRecordTotalValueYb());
			this.recordTougetuValueYb = ConvertUtil
					.parseMoneyToThousandUnitDouble(mrPlan.getImplMonthPlan().getRecordTougetuValueYb());
//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		} else {
			this.planned1ValueYb = null;
			this.planned2ValueYb = null;
			this.planned3ValueYb = null;
			this.planned4ValueYb = null;
			this.planned5ValueYb = null;
			this.planned6ValueYb = null;

			this.expected1ValueYb = null;
			this.expected2ValueYb = null;
			this.expected3ValueYb = null;
			this.expected4ValueYb = null;
			this.expected5ValueYb = null;
			this.expected6ValueYb = null;

//add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
			this.record1ValueYb = null;
			this.record2ValueYb = null;
			this.record3ValueYb = null;
			this.record4ValueYb = null;
			this.record5ValueYb = null;
			this.record6ValueYb = null;
			this.recordTotalValueYb = null;
			this.recordTougetuValueYb = null;
//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		}

		this.plannedStacked1Value = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getPlannedStacked1Value());
		this.plannedStacked2Value = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getPlannedStacked2Value());
		this.plannedStacked3Value = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getPlannedStacked3Value());
		this.plannedStacked4Value = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getPlannedStacked4Value());
		this.plannedStacked5Value = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getPlannedStacked5Value());
		this.plannedStacked6Value = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getPlannedStacked6Value());

		this.expectedStacked1Value = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getExpectedStacked1Value());
		this.expectedStacked2Value = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getExpectedStacked2Value());
		this.expectedStacked3Value = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getExpectedStacked3Value());
		this.expectedStacked4Value = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getExpectedStacked4Value());
		this.expectedStacked5Value = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getExpectedStacked5Value());
		this.expectedStacked6Value = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getExpectedStacked6Value());

		this.plannedTermValue = ConvertUtil.parseMoneyToThousandUnit(mrPlan.getPlannedTermValue());
		this.targetSummary = mrPlan.getTargetSummary();
		this.seqKey = mrPlan.getSeqKey();
		this.upDate = mrPlan.getUpDate();
		if (upDate != null) {
			this.upJgiName = mrPlan.getUpJgiName();
		} else {
			this.upJgiName = null;
		}
		this.canMoveDownPlanLevel = canMoveDownPlanLevel;
		this.etcSosFlg = Boolean.FALSE;
	}

	/**
	 * 品目コードを取得する。
	 *
	 * @return prodCode 品目コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目名を取得する。
	 *
	 * @return prodName 品目名
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 月初計画1（YB価）を取得する
	 *
	 * @return planned1ValueYb 月初計画1（YB価）
	 */
	public Long getPlanned1ValueYb() {
		return planned1ValueYb;
	}

	/**
	 * 月初計画2（YB価）を取得する
	 *
	 * @return planned2ValueYb 月初計画2（YB価）
	 */
	public Long getPlanned2ValueYb() {
		return planned2ValueYb;
	}

	/**
	 * 月初計画3（YB価）を取得する
	 *
	 * @return planned3ValueYb 月初計画3（YB価）
	 */
	public Long getPlanned3ValueYb() {
		return planned3ValueYb;
	}

	/**
	 * 月初計画4（YB価）を取得する
	 *
	 * @return planned4ValueYb 月初計画4（YB価）
	 */
	public Long getPlanned4ValueYb() {
		return planned4ValueYb;
	}

	/**
	 * 月初計画5（YB価）を取得する
	 *
	 * @return planned5ValueYb 月初計画5（YB価）
	 */
	public Long getPlanned5ValueYb() {
		return planned5ValueYb;
	}

	/**
	 * 月初計画6（YB価）を取得する
	 *
	 * @return planned6ValueYb 月初計画6（YB価）
	 */
	public Long getPlanned6ValueYb() {
		return planned6ValueYb;
	}

	/**
	 * 月末見込1（YB価）を取得する
	 *
	 * @return expected1ValueYb 月末見込1（YB価）
	 */
	public Long getExpected1ValueYb() {
		return expected1ValueYb;
	}

	/**
	 * 月末見込2（YB価）を取得する
	 *
	 * @return expected2ValueYb 月末見込2（YB価）
	 */
	public Long getExpected2ValueYb() {
		return expected2ValueYb;
	}

	/**
	 * 月末見込3（YB価）を取得する
	 *
	 * @return expected3ValueYb 月末見込3（YB価）
	 */
	public Long getExpected3ValueYb() {
		return expected3ValueYb;
	}

	/**
	 * 月末見込4（YB価）を取得する
	 *
	 * @return expected4ValueYb 月末見込4（YB価）
	 */
	public Long getExpected4ValueYb() {
		return expected4ValueYb;
	}

	/**
	 * 月末見込5（YB価）を取得する
	 *
	 * @return expected5ValueYb 月末見込5（YB価）
	 */
	public Long getExpected5ValueYb() {
		return expected5ValueYb;
	}

	/**
	 * 月末見込6（YB価）を取得する
	 *
	 * @return expected6ValueYb 月末見込6（YB価）
	 */
	public Long getExpected6ValueYb() {
		return expected6ValueYb;
	}

	/**
	 * 下位計画・月初計画積上値(1月目)を取得する
	 *
	 * @return plannedStacked1Value 下位計画・月初計画積上値(1月目)
	 */
	public Long getPlannedStacked1Value() {
		return plannedStacked1Value;
	}

	/**
	 * 下位計画・月初計画積上値(2月目)を取得する
	 *
	 * @return plannedStacked2Value 下位計画・月初計画積上値(2月目)
	 */
	public Long getPlannedStacked2Value() {
		return plannedStacked2Value;
	}

	/**
	 * 下位計画・月初計画積上値(3月目)を取得する
	 *
	 * @return plannedStacked3Value 下位計画・月初計画積上値(3月目)
	 */
	public Long getPlannedStacked3Value() {
		return plannedStacked3Value;
	}

	/**
	 * 下位計画・月初計画積上値(4月目)を取得する
	 *
	 * @return plannedStacked4Value 下位計画・月初計画積上値(4月目)
	 */
	public Long getPlannedStacked4Value() {
		return plannedStacked4Value;
	}

	/**
	 * 下位計画・月初計画積上値(5月目)を取得する
	 *
	 * @return plannedStacked5Value 下位計画・月初計画積上値(5月目)
	 */
	public Long getPlannedStacked5Value() {
		return plannedStacked5Value;
	}

	/**
	 * 下位計画・月初計画積上値(6月目)を取得する
	 *
	 * @return plannedStacked6Value 下位計画・月初計画積上値(6月目)
	 */
	public Long getPlannedStacked6Value() {
		return plannedStacked6Value;
	}

	/**
	 * 下位計画・月末見込積上値(1月目)を取得する
	 *
	 * @return expectedStacked1Value 下位計画・月末見込積上値(1月目)
	 */
	public Long getExpectedStacked1Value() {
		return expectedStacked1Value;
	}

	/**
	 * 下位計画・月末見込積上値(2月目)を取得する
	 *
	 * @return expectedStacked2Value 下位計画・月末見込積上値(2月目)
	 */
	public Long getExpectedStacked2Value() {
		return expectedStacked2Value;
	}

	/**
	 * 下位計画・月末見込積上値(3月目)を取得する
	 *
	 * @return expectedStacked3Value 下位計画・月末見込積上値(3月目)
	 */
	public Long getExpectedStacked3Value() {
		return expectedStacked3Value;
	}

	/**
	 * 下位計画・月末見込積上値(4月目)を取得する
	 *
	 * @return expectedStacked4Value 下位計画・月末見込積上値(4月目)
	 */
	public Long getExpectedStacked4Value() {
		return expectedStacked4Value;
	}

	/**
	 * 下位計画・月末見込積上値(5月目)を取得する
	 *
	 * @return expectedStacked5Value 下位計画・月末見込積上値(5月目)
	 */
	public Long getExpectedStacked5Value() {
		return expectedStacked5Value;
	}

	/**
	 * 下位計画・月末見込積上値(6月目)を取得する
	 *
	 * @return expectedStacked6Value 下位計画・月末見込積上値(6月目)
	 */
	public Long getExpectedStacked6Value() {
		return expectedStacked6Value;
	}

	/**
	 * 期別計画値を取得する
	 *
	 * @return plannedTermValue 期別計画値
	 */
	public Long getPlannedTermValue() {
		return plannedTermValue;
	}

//add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
	/**
	 *実績1（YB価）を取得する
	 * @return record1ValueYb 実績1（YB価）
	 */
	public Double getRecord1ValueYb() {
		return record1ValueYb;
	}

	/**
	 *実績2（YB価）を取得する
	 * @return record2ValueYb 実績2（YB価）
	 */
	public Double getRecord2ValueYb() {
		return record2ValueYb;
	}

	/**
	 *実績3（YB価）を取得する
	 * @return record3ValueYb 実績3（YB価）
	 */
	public Double getRecord3ValueYb() {
		return record3ValueYb;
	}

	/**
	 *実績4（YB価）を取得する
	 * @return record4ValueYb 実績4（YB価）
	 */
	public Double getRecord4ValueYb() {
		return record4ValueYb;
	}

	/**
	 *実績5（YB価）を取得する
	 * @return record5ValueYb 実績5（YB価）
	 */
	public Double getRecord5ValueYb() {
		return record5ValueYb;
	}

	/**
	 *実績6（YB価）を取得する
	 * @return record6ValueYb 実績6（YB価）
	 */
	public Double getRecord6ValueYb() {
		return record6ValueYb;
	}

	/**
	 *累計実績（YB価）を取得する
	 * @return recordTotalValueYb 累計実績（YB価）
	 */
	public Double getRecordTotalValueYb() {
		return recordTotalValueYb;
	}

	/**
	 *当月実績（YB価）を取得する
	 * @return recordTougetuValueYb 累計実績（YB価）
	 */
	public Double getRecordTougetuValueYb() {
		return recordTougetuValueYb;
	}
//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

	/**
	 * 品目集計対象フラグを取得する。
	 *
	 * @return targetSummary 品目集計対象フラグ
	 */
	public String getTargetSummary() {
		return targetSummary;
	}

	/**
	 * シーケンスキーを取得する。
	 *
	 * @return seqKeyシーケンスキー
	 */
	public Long getSeqKey() {
		return seqKey;
	}

	/**
	 * 最終更新日時を取得する。
	 *
	 * @return upDate 最終更新日時
	 */
	public Date getUpDate() {
		return upDate;
	}

	/**
	 * 最終更新者を取得する。
	 *
	 * @return upJgiName 最終更新者
	 */
	public String getUpJgiName() {
		return upJgiName;
	}

	/**
	 * 下位プランレベル移行可否フラグを取得する。
	 *
	 * @return canMoveDownPlanLevel 下位プランレベル移行可否フラグ
	 */
	public boolean isCanMoveDownPlanLevel() {
		return canMoveDownPlanLevel;
	}

	/**
	 * 雑組織フラグを取得する。
	 *
	 * @return 雑組織フラグ
	 */
	public Boolean getEtcSosFlg() {
		return etcSosFlg;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
