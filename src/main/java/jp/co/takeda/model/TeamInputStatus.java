package jp.co.takeda.model;

import java.util.Date;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.div.InputStateForMrPlan;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 担当者別計画のチーム別入力状況を表すモデルクラス
 * 
 * @author khashimoto
 */
public class TeamInputStatus extends DpModel<TeamInputStatus> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 組織コード
	 */
	private String sosCd;

	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 担当者別計画の入力状況
	 */
	private InputStateForMrPlan inputStateForMrPlan;

	/**
	 * 担当者別計画入力完了日時
	 */
	private Date mrPlanInputDate;

	//---------------------
	// Getter & Setter
	// --------------------

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
	 * 担当者別計画の入力状況を取得する。
	 * 
	 * @return 担当者別計画の入力状況
	 */
	public InputStateForMrPlan getInputStateForMrPlan() {
		return inputStateForMrPlan;
	}

	/**
	 * 担当者別計画の入力状況を設定する。
	 * 
	 * @param inputStateForMrPlan 担当者別計画の入力状況
	 */
	public void setInputStateForMrPlan(InputStateForMrPlan inputStateForMrPlan) {
		this.inputStateForMrPlan = inputStateForMrPlan;
	}

	/**
	 * 担当者別計画入力完了日時を取得する。
	 * 
	 * @return 担当者別計画入力完了日時
	 */
	public Date getMrPlanInputDate() {
		return mrPlanInputDate;
	}

	/**
	 * 担当者別計画入力完了日時を設定する。
	 * 
	 * @param mrPlanInputDate 担当者別計画入力完了日時
	 */
	public void setMrPlanInputDate(Date mrPlanInputDate) {
		this.mrPlanInputDate = mrPlanInputDate;
	}

	@Override
	public int compareTo(TeamInputStatus obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.sosCd, obj.sosCd).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && TeamInputStatus.class.isAssignableFrom(entry.getClass())) {
			TeamInputStatus obj = (TeamInputStatus) entry;
			return new EqualsBuilder().append(this.sosCd, obj.sosCd).append(this.prodCode, obj.prodCode).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.sosCd).append(this.prodCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
