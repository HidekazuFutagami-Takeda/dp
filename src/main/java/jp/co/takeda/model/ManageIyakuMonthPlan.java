package jp.co.takeda.model;

import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpManageModel;
import jp.co.takeda.model.div.InsType;

/**
 * 管理時の全社・月別計画を表すモデルクラス
 *
 * @author khashimoto
 */
public class ManageIyakuMonthPlan extends DpManageModel<ManageIyakuMonthPlan> {

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
	private InsType insType;

	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 月別実施計画
	 */
	private ImplMonthPlan implMonthPlan;

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
	private String prodName;

//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
	/**
	 *納入情報種別リスト
	 */
	protected List<String> nnuJhoSbt;

	/**
	 *医薬ワクチン区分リスト
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
	 * 月別実施計画を取得する。
	 *
	 * @return 月別実施計画
	 */
	public ImplMonthPlan getImplMonthPlan() {
		if (implMonthPlan == null) {
			implMonthPlan = new ImplMonthPlan();
		}
		return implMonthPlan;
	}

	/**
	 * 月別実施計画を設定する。
	 *
	 * @param implMonthPlan 月別実施計画
	 */
	public void setImplMonthPlan(ImplMonthPlan implMonthPlan) {
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


//add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

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
	public int compareTo(ManageIyakuMonthPlan obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.insType, obj.insType).append(this.prodCode, obj.prodCode)
					.toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && ManageIyakuMonthPlan.class.isAssignableFrom(entry.getClass())) {
			ManageIyakuMonthPlan obj = (ManageIyakuMonthPlan) entry;
			return new EqualsBuilder().append(this.insType, obj.insType).append(this.prodCode, obj.prodCode).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.insType).append(this.prodCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
