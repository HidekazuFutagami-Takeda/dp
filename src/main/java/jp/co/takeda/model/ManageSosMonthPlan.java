package jp.co.takeda.model;

import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpManageModel;
import jp.co.takeda.model.div.InsType;

/**
 * 管理時の組織別・月別計画を表すモデルクラス
 *
 * @author khashimoto
 */
public class ManageSosMonthPlan extends DpManageModel<ManageSosMonthPlan> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 施設出力対象区分
	 */
	protected InsType insType;

	/**
	 * 品目固定コード
	 */
	protected String prodCode;

	/**
	 * 組織コード
	 */
	protected String sosCd;

	/**
	 * 月別実施計画
	 */
	protected ImplMonthPlan implMonthPlan;

	/**
	 * 下位計画・月初計画積上値(1月目)
	 */
	protected Long plannedStacked1Value;

	/**
	 * 下位計画・月初計画積上値(2月目)
	 */
	protected Long plannedStacked2Value;

	/**
	 * 下位計画・月初計画積上値(3月目)
	 */
	protected Long plannedStacked3Value;

	/**
	 * 下位計画・月初計画積上値(4月目)
	 */
	protected Long plannedStacked4Value;

	/**
	 * 下位計画・月初計画積上値(5月目)
	 */
	protected Long plannedStacked5Value;

	/**
	 * 下位計画・月初計画積上値(6月目)
	 */
	protected Long plannedStacked6Value;

	/**
	 * 下位計画・月末見込積上値(1月目)
	 */
	protected Long expectedStacked1Value;

	/**
	 * 下位計画・月末見込積上値(2月目)
	 */
	protected Long expectedStacked2Value;

	/**
	 * 下位計画・月末見込積上値(3月目)
	 */
	protected Long expectedStacked3Value;

	/**
	 * 下位計画・月末見込積上値(4月目)
	 */
	protected Long expectedStacked4Value;

	/**
	 * 下位計画・月末見込積上値(5月目)
	 */
	protected Long expectedStacked5Value;

	/**
	 * 下位計画・月末見込積上値(6月目)
	 */
	protected Long expectedStacked6Value;

	/**
	 * 期別計画値
	 */
	protected Long plannedTermValue;

	/**
	 * 品目集計対象フラグ
	 */
	protected String targetSummary;

	/**
	 * 品目名称（Ref[管理時の計画対象品目].〔品目名称〕）
	 */
	protected String prodName;

	/**
	 * 部門名正式（Ref[組織情報].〔部門名正式〕）
	 */
	protected String bumonSeiName;

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

	/**
	 *納入情報種別リスト
	 */
	protected List<String> nnuJhoSbt;

	/**
	 *医薬ワクチン区分リスト）
	 */
	protected List<String> iykWktKbnList;

//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

	//---------------------
	// Getter & Setter
	// --------------------
	/**
	 * 施設出力対象区分を取得する。
	 *
	 * @return 施設出力対象区分
	 */
	public InsType getInsType() {
		return insType;
	}

	/**
	 * 施設出力対象区分を設定する。
	 *
	 * @param insType 施設出力対象区分
	 */
	public void setInsType(InsType insType) {
		this.insType = insType;
	}

	/**
	 * 品目固定コードを取得する。
	 *
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目固定コードを設定する。
	 *
	 * @param prodCode 品目固定コード
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * 組織コードを取得する。
	 *
	 * @return 組織コード
	 */
	public String getSosCd() {
		return sosCd;
	}

	/**
	 * 組織コードを設定する。
	 *
	 * @param sosCd 組織コード
	 */
	public void setSosCd(String sosCd) {
		this.sosCd = sosCd;
	}

	/**
	 * 月別実施計画を取得する
	 *
	 * @return implMonthPlan 月別実施計画
	 */
	public ImplMonthPlan getImplMonthPlan() {
		return implMonthPlan;
	}

	/**
	 * 月別実施計画を設定する
	 *
	 * @param implMonthPlan 月別実施計画
	 */
	public void setImplPlan(ImplMonthPlan implMonthPlan) {
		this.implMonthPlan = implMonthPlan;
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
	 * 下位計画・月初計画積上値(1月目)を設定する
	 *
	 * @param plannedStacked1Value 下位計画・月初計画積上値(1月目)s
	 */
	public void setPlannedStacked1Value(Long plannedStacked1Value) {
		this.plannedStacked1Value = plannedStacked1Value;
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
	 * 下位計画・月初計画積上値(2月目)を設定する
	 *
	 * @param plannedStacked2Value 下位計画・月初計画積上値(2月目)
	 */
	public void setPlannedStacked2Value(Long plannedStacked2Value) {
		this.plannedStacked2Value = plannedStacked2Value;
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
	 * 下位計画・月初計画積上値(3月目)を設定する
	 *
	 * @param plannedStacked3Value 下位計画・月初計画積上値(3月目)
	 */
	public void setPlannedStacked3Value(Long plannedStacked3Value) {
		this.plannedStacked3Value = plannedStacked3Value;
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
	 * 下位計画・月初計画積上値(4月目)を設定する
	 *
	 * @param plannedStacked4Value 下位計画・月初計画積上値(4月目)
	 */
	public void setPlannedStacked4Value(Long plannedStacked4Value) {
		this.plannedStacked4Value = plannedStacked4Value;
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
	 * 下位計画・月初計画積上値(5月目)を設定する
	 *
	 * @param plannedStacked5Value 下位計画・月初計画積上値(5月目)
	 */
	public void setPlannedStacked5Value(Long plannedStacked5Value) {
		this.plannedStacked5Value = plannedStacked5Value;
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
	 * 下位計画・月初計画積上値(6月目)を設定する
	 *
	 * @param plannedStacked6Value 下位計画・月初計画積上値(6月目)
	 */
	public void setPlannedStacked6Value(Long plannedStacked6Value) {
		this.plannedStacked6Value = plannedStacked6Value;
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
	 * 下位計画・月末見込積上値(1月目)を設定する
	 *
	 * @param expectedStacked1Value 下位計画・月末見込積上値(1月目)
	 */
	public void setExpectedStacked1Value(Long expectedStacked1Value) {
		this.expectedStacked1Value = expectedStacked1Value;
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
	 * 下位計画・月末見込積上値(2月目)を設定する
	 *
	 * @param expectedStacked2Value 下位計画・月末見込積上値(2月目)
	 */
	public void setExpectedStacked2Value(Long expectedStacked2Value) {
		this.expectedStacked2Value = expectedStacked2Value;
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
	 * 下位計画・月末見込積上値(3月目)を設定する
	 *
	 * @param expectedStacked3Value 下位計画・月末見込積上値(3月目)
	 */
	public void setExpectedStacked3Value(Long expectedStacked3Value) {
		this.expectedStacked3Value = expectedStacked3Value;
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
	 * 下位計画・月末見込積上値(4月目)を設定する
	 *
	 * @param expectedStacked4Value 下位計画・月末見込積上値(4月目)
	 */
	public void setExpectedStacked4Value(Long expectedStacked4Value) {
		this.expectedStacked4Value = expectedStacked4Value;
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
	 * 下位計画・月末見込積上値(5月目)を設定する
	 *
	 * @param expectedStacked5Value 下位計画・月末見込積上値(5月目)
	 */
	public void setExpectedStacked5Value(Long expectedStacked5Value) {
		this.expectedStacked5Value = expectedStacked5Value;
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
	 * 下位計画・月末見込積上値(6月目)を設定する
	 *
	 * @param expectedStacked6Value 下位計画・月末見込積上値(6月目)
	 */
	public void setExpectedStacked6Value(Long expectedStacked6Value) {
		this.expectedStacked6Value = expectedStacked6Value;
	}

	/**
	 * 期別計画値を取得する
	 *
	 * @return plannedTermValue 期別計画値
	 */
	public Long getPlannedTermValue() {
		return plannedTermValue;
	}

	/**
	 * 期別計画値を設定する
	 *
	 * @param plannedTermValue 期別計画値
	 */
	public void setPlannedTermValue(Long plannedTermValue) {
		this.plannedTermValue = plannedTermValue;
	}

	/**
	 * 品目集計対象フラグを取得する
	 *
	 * @return targetSummary 品目集計対象フラグ
	 */
	public String getTargetSummary() {
		return targetSummary;
	}

	/**
	 * 品目集計対象フラグをセットする
	 *
	 * @param targetSummary 品目集計対象フラグ
	 */
	public void setTargetSummary(String targetSummary) {
		this.targetSummary = targetSummary;
	}

	/**
	 * 品目名称(漢字)を取得する。
	 *
	 * @return 品目名称(漢字)
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 品目名称(漢字)を設定する。
	 *
	 * @param prodName 品目名称(漢字)
	 */
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	/**
	 * 部門名正式を取得する。
	 *
	 * @return 部門名正式
	 */
	public String getBumonSeiName() {
		return bumonSeiName;
	}

	/**
	 * 部門名正式を設定する。
	 *
	 * @param bumonSeiName 部門名正式
	 */
	public void setBumonSeiName(String bumonSeiName) {
		this.bumonSeiName = bumonSeiName;
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
	 *実績1（Y価）を設定する
	 * @param record1ValueY 実績1（Y価）
	 */
	public void setRecord1ValueY(Long record1ValueY) {
		this.record1ValueY = record1ValueY;
	}

	/**
	 *実績1（B価）を取得する
	 * @return record1ValueB 実績1（B価）
	 */
	public Long getRecord1ValueB() {
		return record1ValueB;
	}

	/**
	 *実績1（B価）を設定する
	 * @param record1ValueB 実績1（B価）
	 */
	public void setRecord1ValueB(Long record1ValueB) {
		this.record1ValueB = record1ValueB;
	}

	/**
	 *実績2（Y価）を取得する
	 * @return record2ValueY 実績2（Y価）
	 */
	public Long getRecord2ValueY() {
		return record2ValueY;
	}

	/**
	 *実績2（Y価）を設定する
	 * @param record2ValueY 実績2（Y価）
	 */
	public void setRecord2ValueY(Long record2ValueY) {
		this.record2ValueY = record2ValueY;
	}

	/**
	 *実績2（B価）を取得する
	 * @return record2ValueB 実績2（B価）
	 */
	public Long getRecord2ValueB() {
		return record2ValueB;
	}

	/**
	 *実績2（B価）を設定する
	 * @param record2ValueB 実績2（B価）
	 */
	public void setRecord2ValueB(Long record2ValueB) {
		this.record2ValueB = record2ValueB;
	}

	/**
	 *実績3（Y価）を取得する
	 * @return record3ValueY 実績3（Y価）
	 */
	public Long getRecord3ValueY() {
		return record3ValueY;
	}

	/**
	 *実績3（Y価）を設定する
	 * @param record3ValueY 実績3（Y価）
	 */
	public void setRecord3ValueY(Long record3ValueY) {
		this.record3ValueY = record3ValueY;
	}

	/**
	 *実績3（B価）を取得する
	 * @return record3ValueB 実績3（B価）
	 */
	public Long getRecord3ValueB() {
		return record3ValueB;
	}

	/**
	 *実績3（B価）を設定する
	 * @param record3ValueB 実績3（B価）
	 */
	public void setRecord3ValueB(Long record3ValueB) {
	this.record3ValueB = record3ValueB;
	}

	/**
	 *実績4（Y価）を取得する
	 * @return record4ValueY 実績4（Y価）
	 */
	public Long getRecord4ValueY() {
		return record4ValueY;
	}

	/**
	 *実績4（Y価）を設定する
	 * @param record4ValueY 実績4（Y価）
	 */
	public void setRecord4ValueY(Long record4ValueY) {
		this.record4ValueY = record4ValueY;
	}

	/**
	 *実績4（B価）を取得する
	 * @return record4ValueB 実績4（B価）
	 */
	public Long getRecord4ValueB() {
		return record4ValueB;
	}

	/**
	 *実績4（B価）を設定する
	 * @param record4ValueB 実績4（B価）
	 */
	public void setRecord4ValueB(Long record4ValueB) {
		this.record4ValueB = record4ValueB;
	}

	/**
	 *実績5（Y価）を取得する
	 * @return record5ValueY 実績5（Y価）
	 */
	public Long getRecord5ValueY() {
		return record5ValueY;
	}

	/**
	 *実績5（Y価）を設定する
	 * @param record5ValueY 実績5（Y価）
	 */
	public void setRecord5ValueY(Long record5ValueY) {
		this.record5ValueY = record5ValueY;
	}

	/**
	 *実績5（B価）を取得する
	 * @return record5ValueB 実績5（B価）
	 */
	public Long getRecord5ValueB() {
		return record5ValueB;
	}

	/**
	 *実績5（B価）を設定する
	 * @param record5ValueB 実績5（B価）
	 */
	public void setRecord5ValueB(Long record5ValueB) {
		this.record5ValueB = record5ValueB;
	}

	/**
	 *実績6（Y価）を取得する
	 * @return record6ValueY 実績6（Y価）
	 */
	public Long getRecord6ValueY() {
		return record6ValueY;
	}

	/**
	 *実績6（Y価）を設定する
	 * @param record6ValueY 実績6（Y価）
	 */
	public void setRecord6ValueY(Long record6ValueY) {
		this.record6ValueY = record6ValueY;
	}

	/**
	 *実績6（B価）を取得する
	 * @return record6ValueB 実績6（B価）
	 */
	public Long getRecord6ValueB() {
		return record6ValueB;
	}

	/**
	 *実績6（B価）を設定する
	 * @param record6ValueB 実績6（B価）
	 */
	public void setRecord6ValueB(Long record6ValueB) {
		this.record6ValueB = record6ValueB;
	}

	/**
	 *累計実績（Y価）を取得する
	 * @return recordTotalValueY 累計実績（Y価）
	 */
	public Long getRecordTotalValueY() {
		return recordTotalValueY;
	}

	/**
	 *累計実績（Y価）を設定する
	 * @param recordTotalValueY 累計実績（Y価）
	 */
	public void setRecordTotalValueY(Long recordTotalValueY) {
		this.recordTotalValueY = recordTotalValueY;
	}

	/**
	 *累計実績（B価）を取得する
	 * @return recordTotalValueB 累計実績（B価）
	 */
	public Long getRecordTotalValueB() {
		return recordTotalValueB;
	}

	/**
	 *累計実績（B価）を設定する
	 * @param recordTotalValueB 累計実績（B価）
	 */
	public void setRecordTotalValueB(Long recordTotalValueB) {
		this.recordTotalValueB = recordTotalValueB;
	}

	/**
	 *実績1（YB価_画面表示項目）を取得する
	 * @return record1ValueYb 実績1（YB価_画面表示項目）
	 */
	public Long getRecord1ValueYb() {
		return record1ValueYb;
	}

	/**
	 *実績1（YB価_画面表示項目）を設定する
	 * @param record1ValueYb 実績1（YB価_画面表示項目）
	 */
	public void setRecord1ValueYb(Long record1ValueYb) {
		this.record1ValueYb = record1ValueYb;
	}

	/**
	 *実績2（YB価_画面表示項目）を取得する
	 * @return record2ValueYb 実績2（YB価_画面表示項目）
	 */
	public Long getRecord2ValueYb() {
		return record2ValueYb;
	}

	/**
	 *実績2（YB価_画面表示項目）を設定する
	 * @param record2ValueYb 実績2（YB価_画面表示項目）
	 */
	public void setRecord2ValueYb(Long record2ValueYb) {
		this.record2ValueYb = record2ValueYb;
	}

	/**
	 *実績3（YB価_画面表示項目）を取得する
	 * @return record3ValueYb 実績3（YB価_画面表示項目）
	 */
	public Long getRecord3ValueYb() {
		return record3ValueYb;
	}

	/**
	 *実績3（YB価_画面表示項目）を設定する
	 * @param record3ValueYb 実績3（YB価_画面表示項目）
	 */
	public void setRecord3ValueYb(Long record3ValueYb) {
		this.record3ValueYb = record3ValueYb;
	}

	/**
	 *実績4（YB価_画面表示項目）を取得する
	 * @return record4ValueYb 実績4（YB価_画面表示項目）
	 */
	public Long getRecord4ValueYb() {
		return record4ValueYb;
	}

	/**
	 *実績4（YB価_画面表示項目）を設定する
	 * @param record4ValueYb 実績4（YB価_画面表示項目）
	 */
	public void setRecord4ValueYb(Long record4ValueYb) {
		this.record4ValueYb = record4ValueYb;
	}

	/**
	 *実績5（YB価_画面表示項目）を取得する
	 * @return record5ValueYb 実績5（YB価_画面表示項目）
	 */
	public Long getRecord5ValueYb() {
		return record5ValueYb;
	}

	/**
	 *実績5（YB価_画面表示項目）を設定する
	 * @param record5ValueYb 実績5（YB価_画面表示項目）
	 */
	public void setRecord5ValueYb(Long record5ValueYb) {
		this.record5ValueYb = record5ValueYb;
	}

	/**
	 *実績6（YB価_画面表示項目）を取得する
	 * @return record6ValueYb 実績6（YB価_画面表示項目）
	 */
	public Long getRecord6ValueYb() {
		return record6ValueYb;
	}

	/**
	 *実績6（YB価_画面表示項目）を設定する
	 * @param record6ValueYb 実績6（YB価_画面表示項目）
	 */
	public void setRecord6ValueYb(Long record6ValueYb) {
		this.record6ValueYb = record6ValueYb;
	}

	/**
	 *累計実績（YB価_画面表示項目）を取得する
	 * @return recordTotalValueYb 累計実績（YB価_画面表示項目）
	 */
	public Long getRecordTotalValueYb() {
		return recordTotalValueYb;
	}

	/**
	 *累計実績（YB価_画面表示項目）を設定する
	 * @param recordTotalValueYb 累計実績（YB価_画面表示項目）
	 */
	public void setRecordTotalValueYb(Long recordTotalValueYb) {
		this.recordTotalValueYb = recordTotalValueYb;
	}

	/**
	 *当月実績（YB価_画面表示項目）を取得する
	 * @return recordTougetuValueYb 累計実績（YB価_画面表示項目）
	 */
	public Long getRecordTougetuValueYb() {
		return recordTougetuValueYb;
	}

	/**
	 *当月実績（YB価_画面表示項目）を設定する
	 * @param recordTougetuValueYb 累計実績（YB価_画面表示項目）
	 */
	public void setRecordTougetuValueYb(Long recordTougetuValueYb) {
		this.recordTougetuValueYb = recordTougetuValueYb;
	}

	/**
	 *納入情報種別リストを取得する
	 * @return nnuJhoSbt 納入情報種別
	 */
	public List<String> getNnuJhoSbt() {
		return nnuJhoSbt;
	}

	/**
	 *納入情報種別リストを設定する
	 * @param nnuJhoSbt 納入情報種別
	 */
	public void setNnuJhoSbt(List<String> nnuJhoSbt) {
		this.nnuJhoSbt = nnuJhoSbt;
	}

	/**
	 *医薬ワクチン区分リストを取得する
	 * @return iykWktKbnList 医薬ワクチン区分
	 */
	public List<String> getIykWktKbnList() {
		return iykWktKbnList;
	}

	/**
	 *医薬ワクチン区分リストを設定する
	 * @param iykWktKbnList 医薬ワクチン区分
	 */
	public void setIykWktKbnList(List<String> iykWktKbnList) {
		this.iykWktKbnList = iykWktKbnList;
	}
//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

	@Override
	public int compareTo(ManageSosMonthPlan obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.insType, obj.insType).append(this.sosCd, obj.sosCd)
					.append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && ManageSosMonthPlan.class.isAssignableFrom(entry.getClass())) {
			ManageSosMonthPlan obj = (ManageSosMonthPlan) entry;
			return new EqualsBuilder().append(this.insType, obj.insType).append(this.sosCd, obj.sosCd)
					.append(this.prodCode, obj.prodCode).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.insType).append(this.sosCd).append(this.prodCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
