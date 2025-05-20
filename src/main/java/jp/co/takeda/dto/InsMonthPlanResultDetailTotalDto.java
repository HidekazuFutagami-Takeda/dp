package jp.co.takeda.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.ManageMrMonthPlan;
import jp.co.takeda.util.ConvertUtil;

/**
 * 施設別計画の検索結果 明細合計行を表すDTO
 *
 * @author stakeuchi
 */
public class InsMonthPlanResultDetailTotalDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 期別計画
	 */
	protected Long plannedTermValue;

	/**
	 * 月初計画1（YB価）
	 */
	protected Long planned1ValueYb;

	/**
	 * 月初計画2（YB価）
	 */
	protected Long planned2ValueYb;

	/**
	 * 月初計画3（YB価）
	 */
	protected Long planned3ValueYb;

	/**
	 * 月初計画4（YB価）
	 */
	protected Long planned4ValueYb;

	/**
	 * 月初計画5（YB価）
	 */
	protected Long planned5ValueYb;

	/**
	 * 月初計画6（YB価）
	 */
	protected Long planned6ValueYb;

	/**
	 * 月末見込1（YB価）
	 */
	protected Long expected1ValueYb;

	/**
	 * 月末見込2（YB価）
	 */
	protected Long expected2ValueYb;

	/**
	 * 月末見込3（YB価）
	 */
	protected Long expected3ValueYb;

	/**
	 * 月末見込4（YB価）
	 */
	protected Long expected4ValueYb;

	/**
	 * 月末見込5（YB価）
	 */
	protected Long expected5ValueYb;

	/**
	 * 月末見込6（YB価）
	 */
	protected Long expected6ValueYb;

	/**
	 * 施設別計画 非表示分
	 * 期別計画
	 */
	protected Long hidePlannedTermValue;

	/**
	 * 施設別計画 非表示分
	 * 月初計画1（YB価）
	 */
	protected Long hidePlanned1ValueYb;

	/**
	 * 施設別計画 非表示分
	 * 月初計画2（YB価）
	 */
	protected Long hidePlanned2ValueYb;

	/**
	 * 施設別計画 非表示分
	 * 月初計画3（YB価）
	 */
	protected Long hidePlanned3ValueYb;

	/**
	 * 施設別計画 非表示分
	 * 月初計画4（YB価）
	 */
	protected Long hidePlanned4ValueYb;

	/**
	 * 施設別計画 非表示分
	 * 月初計画5（YB価）
	 */
	protected Long hidePlanned5ValueYb;

	/**
	 * 施設別計画 非表示分
	 * 月初計画6（YB価）
	 */
	protected Long hidePlanned6ValueYb;

	/**
	 * 施設別計画 非表示分
	 * 月末見込1（YB価）
	 */
	protected Long hideExpected1ValueYb;

	/**
	 * 施設別計画 非表示分
	 * 月末見込2（YB価）
	 */
	protected Long hideExpected2ValueYb;

	/**
	 * 施設別計画 非表示分
	 * 月末見込3（YB価）
	 */
	protected Long hideExpected3ValueYb;

	/**
	 * 施設別計画 非表示分
	 * 月末見込4（YB価）
	 */
	protected Long hideExpected4ValueYb;

	/**
	 * 施設別計画 非表示分
	 * 月末見込5（YB価）
	 */
	protected Long hideExpected5ValueYb;

	/**
	 * 施設別計画 非表示分
	 * 月末見込6（YB価）
	 */
	protected Long hideExpected6ValueYb;

//add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
	/**
	*累計実績（YB価） 非表示分
	*/
	protected Double hideRecordTotalValueYb;

	/**
	*当月実績（YB価） 非表示分
	*/
	protected Double hideRecordTougetuValueYb;

	/**
	*実績1（YB価） 非表示分
	*/
	protected Double hideRecord1ValueYb;

	/**
	*実績2（YB価） 非表示分
	*/
	protected Double hideRecord2ValueYb;

	/**
	*実績3（YB価） 非表示分
	*/
	protected Double hideRecord3ValueYb;

	/**
	*実績4（YB価） 非表示分
	*/
	protected Double hideRecord4ValueYb;

	/**
	*実績5（YB価） 非表示分
	*/
	protected Double hideRecord5ValueYb;

	/**
	*実績6（YB価） 非表示分
	*/
	protected Double hideRecord6ValueYb;

	/**
	*累計実績（YB価） 非表示分
	*/
	protected Double recordTotalValueYb;

	/**
	*当月実績（YB価） 非表示分
	*/
	protected Double recordTougetuValueYb;

	/**
	*実績1（YB価）
	*/
	protected Double record1ValueYb;

	/**
	*実績2（YB価）
	*/
	protected Double record2ValueYb;

	/**
	*実績3（YB価）
	*/
	protected Double record3ValueYb;

	/**
	*実績4（YB価）
	*/
	protected Double record4ValueYb;

	/**
	*実績5（YB価）
	*/
	protected Double record5ValueYb;

	/**
	*実績6（YB価）
	*/
	protected Double record6ValueYb;
//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

	/**
	 * 最終更新日時
	 */
	private final Date upDate;

	/**
	 * 最終更新者
	 */
	private final String upJgiName;

	/**
	 * 組織別計画編集画面への移行可否フラグ
	 * <ul>
	 * <li>TRUE = 組織別計画へ移行可能</li>
	 * <li>FALSE = 組織別計画へ移行不可</li>
	 * </ul>
	 */
	private final boolean canMoveSosPlan;

	/**
	 * コンストラクタ
	 *
	 * @param hideValue 施設別計画 非表示分
	 * @param tyChangeRate T/Y変換率
	 * @param upDate 最終更新日時
	 * @param upJgiName 最終更新者
	 * @param canMoveSosPlan 組織別計画編集画面への移行可否フラグ
	 */
	public InsMonthPlanResultDetailTotalDto(ManageMrMonthPlan mrMonthPlan,
			Long hidePlannedTermValue,
			Long hidePlanned1ValueYb,
			Long hidePlanned2ValueYb,
			Long hidePlanned3ValueYb,
			Long hidePlanned4ValueYb,
			Long hidePlanned5ValueYb,
			Long hidePlanned6ValueYb,
			Long hideExpected1ValueYb,
			Long hideExpected2ValueYb,
			Long hideExpected3ValueYb,
			Long hideExpected4ValueYb,
			Long hideExpected5ValueYb,
			Long hideExpected6ValueYb,
//add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
			Long hideRecordTotalValueYb,
			Long hideRecordTougetuValueYb,
			Long hideRecord1ValueYb,
			Long hideRecord2ValueYb,
			Long hideRecord3ValueYb,
			Long hideRecord4ValueYb,
			Long hideRecord5ValueYb,
			Long hideRecord6ValueYb,
//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
			boolean canMoveSosPlan) {
		this.plannedTermValue = ConvertUtil.parseMoneyToThousandUnit(mrMonthPlan.getPlannedTermValue());
		this.planned1ValueYb = ConvertUtil.parseMoneyToThousandUnit(mrMonthPlan.getImplMonthPlan().getPlanned1ValueYb());
		this.planned2ValueYb = ConvertUtil.parseMoneyToThousandUnit(mrMonthPlan.getImplMonthPlan().getPlanned2ValueYb());
		this.planned3ValueYb = ConvertUtil.parseMoneyToThousandUnit(mrMonthPlan.getImplMonthPlan().getPlanned3ValueYb());
		this.planned4ValueYb = ConvertUtil.parseMoneyToThousandUnit(mrMonthPlan.getImplMonthPlan().getPlanned4ValueYb());
		this.planned5ValueYb = ConvertUtil.parseMoneyToThousandUnit(mrMonthPlan.getImplMonthPlan().getPlanned5ValueYb());
		this.planned6ValueYb = ConvertUtil.parseMoneyToThousandUnit(mrMonthPlan.getImplMonthPlan().getPlanned6ValueYb());
		this.expected1ValueYb = ConvertUtil.parseMoneyToThousandUnit(mrMonthPlan.getImplMonthPlan().getExpected1ValueYb());
		this.expected2ValueYb = ConvertUtil.parseMoneyToThousandUnit(mrMonthPlan.getImplMonthPlan().getExpected2ValueYb());
		this.expected3ValueYb = ConvertUtil.parseMoneyToThousandUnit(mrMonthPlan.getImplMonthPlan().getExpected3ValueYb());
		this.expected4ValueYb = ConvertUtil.parseMoneyToThousandUnit(mrMonthPlan.getImplMonthPlan().getExpected4ValueYb());
		this.expected5ValueYb = ConvertUtil.parseMoneyToThousandUnit(mrMonthPlan.getImplMonthPlan().getExpected5ValueYb());
		this.expected6ValueYb = ConvertUtil.parseMoneyToThousandUnit(mrMonthPlan.getImplMonthPlan().getExpected6ValueYb());
//add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		this.recordTotalValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(mrMonthPlan.getImplMonthPlan().getRecordTotalValueYb());
		this.recordTougetuValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(mrMonthPlan.getImplMonthPlan().getRecordTougetuValueYb());
		this.record1ValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(mrMonthPlan.getImplMonthPlan().getRecord1ValueYb());
		this.record2ValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(mrMonthPlan.getImplMonthPlan().getRecord2ValueYb());
		this.record3ValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(mrMonthPlan.getImplMonthPlan().getRecord3ValueYb());
		this.record4ValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(mrMonthPlan.getImplMonthPlan().getRecord4ValueYb());
		this.record5ValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(mrMonthPlan.getImplMonthPlan().getRecord5ValueYb());
		this.record6ValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(mrMonthPlan.getImplMonthPlan().getRecord6ValueYb());
//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		this.hidePlannedTermValue = ConvertUtil.parseMoneyToThousandUnit(hidePlannedTermValue);
		this.hidePlanned1ValueYb = ConvertUtil.parseMoneyToThousandUnit(hidePlanned1ValueYb);
		this.hidePlanned2ValueYb = ConvertUtil.parseMoneyToThousandUnit(hidePlanned2ValueYb);
		this.hidePlanned3ValueYb = ConvertUtil.parseMoneyToThousandUnit(hidePlanned3ValueYb);
		this.hidePlanned4ValueYb = ConvertUtil.parseMoneyToThousandUnit(hidePlanned4ValueYb);
		this.hidePlanned5ValueYb = ConvertUtil.parseMoneyToThousandUnit(hidePlanned5ValueYb);
		this.hidePlanned6ValueYb = ConvertUtil.parseMoneyToThousandUnit(hidePlanned6ValueYb);
		this.hideExpected1ValueYb = ConvertUtil.parseMoneyToThousandUnit(hideExpected1ValueYb);
		this.hideExpected2ValueYb = ConvertUtil.parseMoneyToThousandUnit(hideExpected2ValueYb);
		this.hideExpected3ValueYb = ConvertUtil.parseMoneyToThousandUnit(hideExpected3ValueYb);
		this.hideExpected4ValueYb = ConvertUtil.parseMoneyToThousandUnit(hideExpected4ValueYb);
		this.hideExpected5ValueYb = ConvertUtil.parseMoneyToThousandUnit(hideExpected5ValueYb);
		this.hideExpected6ValueYb = ConvertUtil.parseMoneyToThousandUnit(hideExpected6ValueYb);
//add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		this.hideRecordTotalValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(hideRecordTotalValueYb);
		this.hideRecordTougetuValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(hideRecordTougetuValueYb);
		this.hideRecord1ValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(hideRecord1ValueYb);
		this.hideRecord2ValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(hideRecord2ValueYb);
		this.hideRecord3ValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(hideRecord3ValueYb);
		this.hideRecord4ValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(hideRecord4ValueYb);
		this.hideRecord5ValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(hideRecord5ValueYb);
		this.hideRecord6ValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(hideRecord6ValueYb);
//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		this.upDate = mrMonthPlan.getUpDate();
		if (upDate != null) {
			this.upJgiName = mrMonthPlan.getUpJgiName();
		} else {
			this.upJgiName = null;
		}
		this.canMoveSosPlan = canMoveSosPlan;
	}

	/**
	 * 期別計画を取得する
	 *
	 * @return plannedTermValue 期別計画値
	 */
	public Long getPlannedTermValue() {
		return plannedTermValue;
	}

	/**
	 * 月初計画1（YB価）を取得する。
	 */
	public Long getPlanned1ValueYb() {
		return planned1ValueYb;
	}

	/**
	 * 月初計画2（YB価）を取得する。
	 */
	public Long getPlanned2ValueYb() {
		return planned2ValueYb;
	}

	/**
	 * 月初計画3（YB価）を取得する。
	 */
	public Long getPlanned3ValueYb() {
		return planned3ValueYb;
	}

	/**
	 * 月初計画4（YB価）を取得する。
	 */
	public Long getPlanned4ValueYb() {
		return planned4ValueYb;
	}

	/**
	 * 月初計画5（YB価）を取得する。
	 */
	public Long getPlanned5ValueYb() {
		return planned5ValueYb;
	}

	/**
	 * 月初計画6（YB価）を取得する。
	 */
	public Long getPlanned6ValueYb() {
		return planned6ValueYb;
	}

	/**
	 * 月末見込1（YB価）を取得する。
	 */
	public Long getExpected1ValueYb() {
		return expected1ValueYb;
	}

	/**
	 * 月末見込2（YB価）を取得する。
	 */
	public Long getExpected2ValueYb() {
		return expected2ValueYb;
	}

	/**
	 * 月末見込3（YB価）を取得する。
	 */
	public Long getExpected3ValueYb() {
		return expected3ValueYb;
	}

	/**
	 * 月末見込4（YB価）を取得する。
	 */
	public Long getExpected4ValueYb() {
		return expected4ValueYb;
	}

	/**
	 * 月末見込5（YB価）を取得する。
	 */
	public Long getExpected5ValueYb() {
		return expected5ValueYb;
	}

	/**
	 * 月末見込6（YB価）を取得する。
	 */
	public Long getExpected6ValueYb() {
		return expected6ValueYb;
	}

	/**
	 * 期別計画を取得する。（施設別計画 非表示分）
	 *
	 * @return hidePlannedTermValue 期別計画値
	 */
	public Long getHidePlannedTermValue() {
		return hidePlannedTermValue;
	}

	/**
	 * 月初計画1（YB価）を取得する。（施設別計画 非表示分）
	 */
	public Long getHidePlanned1ValueYb() {
		return hidePlanned1ValueYb;
	}

	/**
	 * 月初計画2（YB価）を取得する。（施設別計画 非表示分）
	 */
	public Long getHidePlanned2ValueYb() {
		return hidePlanned2ValueYb;
	}

	/**
	 * 月初計画3（YB価）を取得する。（施設別計画 非表示分）
	 */
	public Long getHidePlanned3ValueYb() {
		return hidePlanned3ValueYb;
	}

	/**
	 * 月初計画4（YB価）を取得する。（施設別計画 非表示分）
	 */
	public Long getHidePlanned4ValueYb() {
		return hidePlanned4ValueYb;
	}

	/**
	 * 月初計画5（YB価）を取得する。（施設別計画 非表示分）
	 */
	public Long getHidePlanned5ValueYb() {
		return hidePlanned5ValueYb;
	}

	/**
	 * 月初計画6（YB価）を取得する。（施設別計画 非表示分）
	 */
	public Long getHidePlanned6ValueYb() {
		return hidePlanned6ValueYb;
	}

	/**
	 * 月末見込1（YB価）を取得する。（施設別計画 非表示分）
	 */
	public Long getHideExpected1ValueYb() {
		return hideExpected1ValueYb;
	}

	/**
	 * 月末見込2（YB価）を取得する。（施設別計画 非表示分）
	 */
	public Long getHideExpected2ValueYb() {
		return hideExpected2ValueYb;
	}

	/**
	 * 月末見込3（YB価）を取得する。（施設別計画 非表示分）
	 */
	public Long getHideExpected3ValueYb() {
		return hideExpected3ValueYb;
	}

	/**
	 * 月末見込4（YB価）を取得する。（施設別計画 非表示分）
	 */
	public Long getHideExpected4ValueYb() {
		return hideExpected4ValueYb;
	}

	/**
	 * 月末見込5（YB価）を取得する。（施設別計画 非表示分）
	 */
	public Long getHideExpected5ValueYb() {
		return hideExpected5ValueYb;
	}

	/**
	 * 月末見込6（YB価）を取得する。（施設別計画 非表示分）
	 */
	public Long getHideExpected6ValueYb() {
		return hideExpected6ValueYb;
	}

//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
	/**
	 *累計実績（YB価）を取得する
	 * @return recordTotalValueYb 累計実績（YB価）
	 */
	public Double getRecordTotalValueYb() {
	return recordTotalValueYb;
	}

	/**
	 *当月実績（YB価）を取得する
	 * @return recordTougetuValueYb 当月実績（YB価）
	 */
	public Double getRecordTougetuValueYb() {
	return recordTougetuValueYb;
	}

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
	 * @return hideRecordTotalValueYb 累計実績（YB価） 非表示分
	 */
	public Double getHideRecordTotalValueYb() {
	return hideRecordTotalValueYb;
	}

	/**
	 *当月実績（YB価）を取得する
	 * @return hideRecordTougetuValueYb 当月実績（YB価） 非表示分
	 */
	public Double getHideRecordTougetuValueYb() {
	return hideRecordTougetuValueYb;
	}

	/**
	 *実績1（YB価）を取得する
	 * @return hideRecord1ValueYb 実績1（YB価） 非表示分
	 */
	public Double getHideRecord1ValueYb() {
	return hideRecord1ValueYb;
	}

	/**
	 *実績2（YB価）を取得する
	 * @return hideRecord2ValueYb 実績2（YB価） 非表示分
	 */
	public Double getHideRecord2ValueYb() {
	return hideRecord2ValueYb;
	}

	/**
	 *実績3（YB価）を取得する
	 * @return hideRecord3ValueYb 実績3（YB価） 非表示分
	 */
	public Double getHideRecord3ValueYb() {
	return hideRecord3ValueYb;
	}

	/**
	 *実績4（YB価）を取得する
	 * @return hideRecord4ValueYb 実績4（YB価） 非表示分
	 */
	public Double getHideRecord4ValueYb() {
	return hideRecord4ValueYb;
	}

	/**
	 *実績5（YB価）を取得する
	 * @return hideRecord5ValueYb 実績5（YB価） 非表示分
	 */
	public Double getHideRecord5ValueYb() {
	return hideRecord5ValueYb;
	}

	/**
	 *実績6（YB価）を取得する
	 * @return hideRecord6ValueYb 実績6（YB価） 非表示分
	 */
	public Double getHideRecord6ValueYb() {
	return hideRecord6ValueYb;
	}
//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

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
	 * 組織別計画編集画面への移行可否フラグを取得する。
	 *
	 * @return canMoveSosPlan 組織別計画編集画面への移行可否フラグ
	 */
	public boolean isCanMoveSosPlan() {
		return canMoveSosPlan;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
