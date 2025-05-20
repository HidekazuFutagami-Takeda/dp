package jp.co.takeda.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 月別実施計画を表すクラス
 *
 * @author khashimoto
 */
public class ImplMonthPlan implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

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

//add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
	/**
	 *実績1（Y価）
	 */
	protected Long record1ValueY;

	/**
	 *実績1（B価）
	 */
	protected Long record1ValueB;

	/**
	 *実績2（Y価）
	 */
	protected Long record2ValueY;

	/**
	 *実績2（B価）
	 */
	protected Long record2ValueB;

	/**
	 *実績3（Y価）
	 */
	protected Long record3ValueY;

	/**
	 *実績3（B価）
	 */
	protected Long record3ValueB;

	/**
	 *実績4（Y価）
	 */
	protected Long record4ValueY;

	/**
	 *実績4（B価）
	 */
	protected Long record4ValueB;

	/**
	 *実績5（Y価）
	 */
	protected Long record5ValueY;

	/**
	 *実績5（B価）
	 */
	protected Long record5ValueB;

	/**
	 *実績6（Y価）
	 */
	protected Long record6ValueY;

	/**
	 *実績6（B価）
	 */
	protected Long record6ValueB;

	/**
	*累計実績（Y価）
	 */
	protected Long recordTotalValueY;

	/**
	*累計実績（B価）
	 */
	protected Long recordTotalValueB;

	/**
	 *実績1（YB価_画面表示項目）
	 */
	protected Long record1ValueYb;

	/**
	 *実績2（YB価_画面表示項目）
	 */
	protected Long record2ValueYb;

	/**
	 *実績3（YB価_画面表示項目）
	 */
	protected Long record3ValueYb;

	/**
	 *実績4（YB価_画面表示項目）
	 */
	protected Long record4ValueYb;

	/**
	 *実績5（YB価_画面表示項目）
	 */
	protected Long record5ValueYb;

	/**
	 *実績6（YB価_画面表示項目）
	 */
	protected Long record6ValueYb;

	/**
	 *累計実績（YB価_画面表示項目）
	 */
	protected Long recordTotalValueYb;

	/**
	 *当月実績（YB価_画面表示項目）
	 */
	protected Long recordTougetuValueYb;
//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 期別計画を取得する
	 *
	 * @return plannedTermValue 期別計画値
	 */
	public Long getPlannedTermValue() {
		return plannedTermValue;
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

//add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
	/**
	 *実績1（Y価）を取得する
	 * @return record1ValueY 実績1（Y価）
	 */
	public Long getRecord1ValueY() {
		return record1ValueY;
	}

	/**
	 *実績1（B価）を取得する
	 * @return record1ValueB 実績1（B価）
	 */
	public Long getRecord1ValueB() {
		return record1ValueB;
	}

	/**
	 *実績2（Y価）を取得する
	 * @return record2ValueY 実績2（Y価）
	 */
	public Long getRecord2ValueY() {
		return record2ValueY;
	}

	/**
	 *実績2（B価）を取得する
	 * @return record2ValueB 実績2（B価）
	 */
	public Long getRecord2ValueB() {
		return record2ValueB;
	}

	/**
	 *実績3（Y価）を取得する
	 * @return record3ValueY 実績3（Y価）
	 */
	public Long getRecord3ValueY() {
		return record3ValueY;
	}

	/**
	 *実績3（B価）を取得する
	 * @return record3ValueB 実績3（B価）
	 */
	public Long getRecord3ValueB() {
		return record3ValueB;
	}

	/**
	 *実績4（Y価）を取得する
	 * @return record4ValueY 実績4（Y価）
	 */
	public Long getRecord4ValueY() {
		return record4ValueY;
	}

	/**
	 *実績4（B価）を取得する
	 * @return record4ValueB 実績4（B価）
	 */
	public Long getRecord4ValueB() {
		return record4ValueB;
	}

	/**
	 *実績5（Y価）を取得する
	 * @return record5ValueY 実績5（Y価）
	 */
	public Long getRecord5ValueY() {
		return record5ValueY;
	}

	/**
	 *実績5（B価）を取得する
	 * @return record5ValueB 実績5（B価）
	 */
	public Long getRecord5ValueB() {
		return record5ValueB;
	}

	/**
	 *実績6（Y価）を取得する
	 * @return record6ValueY 実績6（Y価）
	 */
	public Long getRecord6ValueY() {
		return record6ValueY;
	}

	/**
	 *実績6（B価）を取得する
	 * @return record6ValueB 実績6（B価）
	 */
	public Long getRecord6ValueB() {
		return record6ValueB;
	}

	/**
	 *累計実績（Y価）を取得する
	 * @return recordTotalValueY 累計実績（Y価）
	 */
	public Long getRecordTotalValueY() {
		return recordTotalValueY;
	}

	/**
	 *累計実績（B価）を取得する
	 * @return recordTotalValueB 累計実績（B価）
	 */
	public Long getRecordTotalValueB() {
		return recordTotalValueB;
	}

	/**
	 *実績1（YB価_画面表示項目）を取得する
	 * @return record1ValueYb 実績1（YB価_画面表示項目）
	 */
	public Long getRecord1ValueYb() {
		return record1ValueYb;
	}

	/**
	 *実績2（YB価_画面表示項目）を取得する
	 * @return record2ValueYb 実績2（YB価_画面表示項目）
	 */
	public Long getRecord2ValueYb() {
		return record2ValueYb;
	}

	/**
	 *実績3（YB価_画面表示項目）を取得する
	 * @return record3ValueYb 実績3（YB価_画面表示項目）
	 */
	public Long getRecord3ValueYb() {
		return record3ValueYb;
	}

	/**
	 *実績4（YB価_画面表示項目）を取得する
	 * @return record4ValueYb 実績4（YB価_画面表示項目）
	 */
	public Long getRecord4ValueYb() {
		return record4ValueYb;
	}

	/**
	 *実績5（YB価_画面表示項目）を取得する
	 * @return record5ValueYb 実績5（YB価_画面表示項目）
	 */
	public Long getRecord5ValueYb() {
		return record5ValueYb;
	}

	/**
	 *実績6（YB価_画面表示項目）を取得する
	 * @return record6ValueYb 実績6（YB価_画面表示項目）
	 */
	public Long getRecord6ValueYb() {
		return record6ValueYb;
	}

	/**
	 *累計実績（YB価_画面表示項目）を取得する
	 * @return recordTotalValueYb 累計実績（YB価_画面表示項目）
	 */
	public Long getRecordTotalValueYb() {
		return recordTotalValueYb;
	}

	/**
	 *当月実績（YB価_画面表示項目）を取得する
	 * @return recordTougetuValueYb 累計実績（YB価_画面表示項目）
	 */
	public Long getRecordTougetuValueYb() {
		return recordTougetuValueYb;
	}
//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

	/**
	 * 期別計画値を設定する
	 *
	 * @param plannedTermValue 期別計画値
	 */
	public void setPlannedTermValue(Long plannedTermValue) {
		this.plannedTermValue = plannedTermValue;
	}

	/**
	 * 月初計画1（YB価）を設定する
	 *
	 * @param planned1ValueYb 月初計画1（YB価）
	 */
	public void setPlanned1ValueYb(Long planned1ValueYb) {
		this.planned1ValueYb = planned1ValueYb;
	}

	/**
	 * 月初計画2（YB価）を設定する
	 *
	 * @param planned2ValueYb 月初計画2（YB価）
	 */
	public void setPlanned2ValueYb(Long planned2ValueYb) {
		this.planned2ValueYb = planned2ValueYb;
	}

	/**
	 * 月初計画3（YB価）を設定する
	 *
	 * @param planned3ValueYb 月初計画3（YB価）
	 */
	public void setPlanned3ValueYb(Long planned3ValueYb) {
		this.planned3ValueYb = planned3ValueYb;
	}

	/**
	 * 月初計画4（YB価）を設定する
	 *
	 * @param planned4ValueYb 月初計画4（YB価）
	 */
	public void setPlanned4ValueYb(Long planned4ValueYb) {
		this.planned4ValueYb = planned4ValueYb;
	}

	/**
	 * 月初計画5（YB価）を設定する
	 *
	 * @param planned5ValueYb 月初計画5（YB価）
	 */
	public void setPlanned5ValueYb(Long planned5ValueYb) {
		this.planned5ValueYb = planned5ValueYb;
	}

	/**
	 * 月初計画6（YB価）を設定する
	 *
	 * @param planned6ValueYb 月初計画6（YB価）
	 */
	public void setPlanned6ValueYb(Long planned6ValueYb) {
		this.planned6ValueYb = planned6ValueYb;
	}

	/**
	 * 月末見込1（YB価）を設定する
	 *
	 * @param expected1ValueYb 月末見込1（YB価）
	 */
	public void setExpected1ValueYb(Long expected1ValueYb) {
		this.expected1ValueYb = expected1ValueYb;
	}

	/**
	 * 月末見込2（YB価）を設定する
	 *
	 * @param expected2ValueYb 月末見込2（YB価）
	 */
	public void setExpected2ValueYb(Long expected2ValueYb) {
		this.expected2ValueYb = expected2ValueYb;
	}

	/**
	 * 月末見込3（YB価）を設定する
	 *
	 * @param expected3ValueYb 月末見込3（YB価）
	 */
	public void setExpected3ValueYb(Long expected3ValueYb) {
		this.expected3ValueYb = expected3ValueYb;
	}

	/**
	 * 月末見込4（YB価）を設定する
	 *
	 * @param expected4ValueYb 月末見込4（YB価）
	 */
	public void setExpected4ValueYb(Long expected4ValueYb) {
		this.expected4ValueYb = expected4ValueYb;
	}

	/**
	 * 月末見込5（YB価）を設定する
	 *
	 * @param expected5ValueYb 月末見込5（YB価）
	 */
	public void setExpected5ValueYb(Long expected5ValueYb) {
		this.expected5ValueYb = expected5ValueYb;
	}

	/**
	 * 月末見込6（YB価）を設定する
	 *
	 * @param expected6ValueYb 月末見込6（YB価）
	 */
	public void setExpected6ValueYb(Long expected6ValueYb) {
		this.expected6ValueYb = expected6ValueYb;
	}

//add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
	/**
	 *実績1（Y価）を設定する
	 * @param record1ValueY 実績1（Y価）
	 */
	public void setRecord1ValueY(Long record1ValueY) {
		this.record1ValueY = record1ValueY;
	}

	/**
	 *実績1（B価）を設定する
	 * @param record1ValueB 実績1（B価）
	 */
	public void setRecord1ValueB(Long record1ValueB) {
		this.record1ValueB = record1ValueB;
	}

	/**
	 *実績2（Y価）を設定する
	 * @param record2ValueY 実績2（Y価）
	 */
	public void setRecord2ValueY(Long record2ValueY) {
		this.record2ValueY = record2ValueY;
	}

	/**
	 *実績2（B価）を設定する
	 * @param record2ValueB 実績2（B価）
	 */
	public void setRecord2ValueB(Long record2ValueB) {
		this.record2ValueB = record2ValueB;
	}

	/**
	 *実績3（Y価）を設定する
	 * @param record3ValueY 実績3（Y価）
	 */
	public void setRecord3ValueY(Long record3ValueY) {
		this.record3ValueY = record3ValueY;
	}

	/**
	 *実績3（B価）を設定する
	 * @param record3ValueB 実績3（B価）
	 */
	public void setRecord3ValueB(Long record3ValueB) {
	this.record3ValueB = record3ValueB;
	}

	/**
	 *実績4（Y価）を設定する
	 * @param record4ValueY 実績4（Y価）
	 */
	public void setRecord4ValueY(Long record4ValueY) {
		this.record4ValueY = record4ValueY;
	}

	/**
	 *実績4（B価）を設定する
	 * @param record4ValueB 実績4（B価）
	 */
	public void setRecord4ValueB(Long record4ValueB) {
		this.record4ValueB = record4ValueB;
	}

	/**
	 *実績5（Y価）を設定する
	 * @param record5ValueY 実績5（Y価）
	 */
	public void setRecord5ValueY(Long record5ValueY) {
		this.record5ValueY = record5ValueY;
	}

	/**
	 *実績5（B価）を設定する
	 * @param record5ValueB 実績5（B価）
	 */
	public void setRecord5ValueB(Long record5ValueB) {
		this.record5ValueB = record5ValueB;
	}

	/**
	 *実績6（Y価）を設定する
	 * @param record6ValueY 実績6（Y価）
	 */
	public void setRecord6ValueY(Long record6ValueY) {
		this.record6ValueY = record6ValueY;
	}

	/**
	 *実績6（B価）を設定する
	 * @param record6ValueB 実績6（B価）
	 */
	public void setRecord6ValueB(Long record6ValueB) {
		this.record6ValueB = record6ValueB;
	}

	/**
	 *累計実績（Y価）を設定する
	 * @param recordTotalValueY 累計実績（Y価）
	 */
	public void setRecordTotalValueY(Long recordTotalValueY) {
		this.recordTotalValueY = recordTotalValueY;
	}

	/**
	 *累計実績（B価）を設定する
	 * @param recordTotalValueB 累計実績（B価）
	 */
	public void setRecordTotalValueB(Long recordTotalValueB) {
		this.recordTotalValueB = recordTotalValueB;
	}

	/**
	 *実績1（YB価_画面表示項目）を設定する
	 * @param record1ValueYb 実績1（YB価_画面表示項目）
	 */
	public void setRecord1ValueYb(Long record1ValueYb) {
		this.record1ValueYb = record1ValueYb;
	}

	/**
	 *実績2（YB価_画面表示項目）を設定する
	 * @param record2ValueYb 実績2（YB価_画面表示項目）
	 */
	public void setRecord2ValueYb(Long record2ValueYb) {
		this.record2ValueYb = record2ValueYb;
	}

	/**
	 *実績3（YB価_画面表示項目）を設定する
	 * @param record3ValueYb 実績3（YB価_画面表示項目）
	 */
	public void setRecord3ValueYb(Long record3ValueYb) {
		this.record3ValueYb = record3ValueYb;
	}

	/**
	 *実績4（YB価_画面表示項目）を設定する
	 * @param record4ValueYb 実績4（YB価_画面表示項目）
	 */
	public void setRecord4ValueYb(Long record4ValueYb) {
		this.record4ValueYb = record4ValueYb;
	}

	/**
	 *実績5（YB価_画面表示項目）を設定する
	 * @param record5ValueYb 実績5（YB価_画面表示項目）
	 */
	public void setRecord5ValueYb(Long record5ValueYb) {
		this.record5ValueYb = record5ValueYb;
	}

	/**
	 *実績6（YB価_画面表示項目）を設定する
	 * @param record6ValueYb 実績6（YB価_画面表示項目）
	 */
	public void setRecord6ValueYb(Long record6ValueYb) {
		this.record6ValueYb = record6ValueYb;
	}

	/**
	 *累計実績（YB価_画面表示項目）を設定する
	 * @param recordTotalValueYb 累計実績（YB価_画面表示項目）
	 */
	public void setRecordTotalValueYb(Long recordTotalValueYb) {
		this.recordTotalValueYb = recordTotalValueYb;
	}

	/**
	 *当月実績（YB価_画面表示項目）を設定する
	 * @param recordTougetuValueYb 累計実績（YB価_画面表示項目）
	 */
	public void setRecordTougetuValueYb(Long recordTougetuValueYb) {
		this.recordTougetuValueYb = recordTougetuValueYb;
	}
//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
