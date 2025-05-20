package jp.co.takeda.model.view;

import jp.co.takeda.a.bean.Model;
import jp.co.takeda.model.div.InsType;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 試算構成比を表すクラス
 * 
 * @author khashimoto
 */
public class EstimationRatio extends Model<EstimationRatio> {

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
	 * 未獲得市場集計値
	 */
	private Long mikakutokuValue;

	/**
	 * 未獲得市場構成比
	 */
	private Double mikakutokuRatio;

	/**
	 * 過去実績集計値
	 */
	private Long kakoJissekiValue;

	/**
	 * 過去実績構成比
	 */
	private Double kakoJissekiRatio;

	/**
	 * フリー項目１集計値
	 */
	private Long free1Value;

	/**
	 * フリー項目１構成比
	 */
	private Double free1Ratio;

	/**
	 * フリー項目２集計値
	 */
	private Long free2Value;

	/**
	 * フリー項目２構成比
	 */
	private Double free2Ratio;

	/**
	 * フリー項目３集計値
	 */
	private Long free3Value;

	/**
	 * フリー項目３構成比
	 */
	private Double free3Ratio;

	/**
	 * 特定施設個別計画値
	 */
	private Long specialValue;

	/**
	 * 自品目過去実績集計値
	 */
	private Long ownKakoJissekiValue;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 施設出力対象区分を取得する。<br>
	 * 合計行の場合、NULLを返す。
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
	 * 未獲得市場集計値を取得する。
	 * 
	 * @return 未獲得市場集計値
	 */
	public Long getMikakutokuValue() {
		return mikakutokuValue;
	}

	/**
	 * 未獲得市場集計値を設定する。
	 * 
	 * @param mikakutokuValue 未獲得市場集計値
	 */
	public void setMikakutokuValue(Long mikakutokuValue) {
		this.mikakutokuValue = mikakutokuValue;
	}

	/**
	 * 未獲得市場構成比を取得する。
	 * 
	 * @return 未獲得市場構成比
	 */
	public Double getMikakutokuRatio() {
		return mikakutokuRatio;
	}

	/**
	 * 未獲得市場構成比を設定する。
	 * 
	 * @param mikakutokuRatio 未獲得市場構成比
	 */
	public void setMikakutokuRatio(Double mikakutokuRatio) {
		this.mikakutokuRatio = mikakutokuRatio;
	}

	/**
	 * 過去実績集計値を取得する。
	 * 
	 * @return 過去実績集計値
	 */
	public Long getKakoJissekiValue() {
		return kakoJissekiValue;
	}

	/**
	 * 過去実績集計値を設定する。
	 * 
	 * @param kakoJissekiValue 過去実績集計値
	 */
	public void setKakoJissekiValue(Long kakoJissekiValue) {
		this.kakoJissekiValue = kakoJissekiValue;
	}

	/**
	 * 過去実績構成比を取得する。
	 * 
	 * @return 過去実績構成比
	 */
	public Double getKakoJissekiRatio() {
		return kakoJissekiRatio;
	}

	/**
	 * 過去実績構成比を設定する。
	 * 
	 * @param kakoJissekiRatio 過去実績構成比
	 */
	public void setKakoJissekiRatio(Double kakoJissekiRatio) {
		this.kakoJissekiRatio = kakoJissekiRatio;
	}

	/**
	 * フリー項目１集計値を取得する。
	 * 
	 * @return フリー項目１集計値
	 */
	public Long getFree1Value() {
		return free1Value;
	}

	/**
	 * フリー項目１集計値を設定する。
	 * 
	 * @param free1Value フリー項目１集計値
	 */
	public void setFree1Value(Long free1Value) {
		this.free1Value = free1Value;
	}

	/**
	 * フリー項目１構成比を取得する。
	 * 
	 * @return フリー項目１構成比
	 */
	public Double getFree1Ratio() {
		return free1Ratio;
	}

	/**
	 * フリー項目１構成比を設定する。
	 * 
	 * @param free1Ratio フリー項目１構成比
	 */
	public void setFree1Ratio(Double free1Ratio) {
		this.free1Ratio = free1Ratio;
	}

	/**
	 * フリー項目２集計値を取得する。
	 * 
	 * @return フリー項目２集計値
	 */
	public Long getFree2Value() {
		return free2Value;
	}

	/**
	 * フリー項目２集計値を設定する。
	 * 
	 * @param free2Value フリー項目２集計値
	 */
	public void setFree2Value(Long free2Value) {
		this.free2Value = free2Value;
	}

	/**
	 * フリー項目２構成比を取得する。
	 * 
	 * @return フリー項目２構成比
	 */
	public Double getFree2Ratio() {
		return free2Ratio;
	}

	/**
	 * フリー項目２構成比を設定する。
	 * 
	 * @param free2Ratio フリー項目２構成比
	 */
	public void setFree2Ratio(Double free2Ratio) {
		this.free2Ratio = free2Ratio;
	}

	/**
	 * フリー項目３集計値を取得する。
	 * 
	 * @return フリー項目３集計値
	 */
	public Long getFree3Value() {
		return free3Value;
	}

	/**
	 * フリー項目３集計値を設定する。
	 * 
	 * @param free3Value フリー項目３集計値
	 */
	public void setFree3Value(Long free3Value) {
		this.free3Value = free3Value;
	}

	/**
	 * フリー項目３構成比を取得する。
	 * 
	 * @return フリー項目３構成比
	 */
	public Double getFree3Ratio() {
		return free3Ratio;
	}

	/**
	 * フリー項目３構成比を設定する。
	 * 
	 * @param free3Ratio フリー項目３構成比
	 */
	public void setFree3Ratio(Double free3Ratio) {
		this.free3Ratio = free3Ratio;
	}

	/**
	 * 特定施設個別計画値を取得する。
	 * 
	 * @return 特定施設個別計画値
	 */
	public Long getSpecialValue() {
		return specialValue;
	}

	/**
	 * 特定施設個別計画値を設定する。
	 * 
	 * @param specialValue 特定施設個別計画値
	 */
	public void setSpecialValue(Long specialValue) {
		this.specialValue = specialValue;
	}

	/**
	 * 自品目過去実績集計値を取得する。
	 * 
	 * @return 自品目過去実績集計値
	 */
	public Long getOwnKakoJissekiValue() {
		return ownKakoJissekiValue;
	}

	/**
	 * 自品目過去実績集計値を設定する。
	 * 
	 * @param ownKakoJissekiValue 自品目過去実績集計値
	 */
	public void setOwnKakoJissekiValue(Long ownKakoJissekiValue) {
		this.ownKakoJissekiValue = ownKakoJissekiValue;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

	@Override
	public int compareTo(EstimationRatio obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.insType, obj.insType).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && EstimationRatio.class.isAssignableFrom(entry.getClass())) {
			EstimationRatio obj = (EstimationRatio) entry;
			return new EqualsBuilder().append(this.insType, obj.insType).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.insType).toHashCode();
	}
}
