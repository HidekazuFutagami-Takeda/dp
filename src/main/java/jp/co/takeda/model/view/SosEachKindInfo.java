package jp.co.takeda.model.view;

import java.util.Date;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.a.bean.Model;

/**
 * 組織単位の各種登録状況を表すモデルクラス
 *
 * @author khashimoto
 */
public class SosEachKindInfo extends Model<SosEachKindInfo> {

	//---------------------
	// Data Field
	// --------------------

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード
	 */
	private String sosCd;

	/**
	 * 部門名正式<br>
	 * (該当階層の組織名)
	 */
	private String bumonSeiName;

	/**
	 * 未獲得市場の最終更新日時
	 */
	private Date mikakutokuUpDate;

	/**
	 * 配分除外施設の登録件数
	 */
	private Integer exceptDistInsCnt;

	/**
	 * 配分除外施設の最終更新日時
	 */
	private Date exceptDistInsUpDate;

	/**
	 * 特定施設個別計画の登録件数
	 */
	private Integer specialInsPlanCnt;

	/**
	 * 特定施設個別計画の最終更新日時
	 */
	private Date specialInsPlanUpDate;

	/**
	 * 担当者別計画の最終更新日時
	 */
	private Date mrPlanUpDate;
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

	/**
	 * 未獲得市場の最終更新日時を取得する。
	 *
	 * @return 未獲得市場の最終更新日時
	 */
	public Date getMikakutokuUpDate() {
		return mikakutokuUpDate;
	}

	/**
	 * 未獲得市場の最終更新日時を設定する。
	 *
	 * @param mikakutokuUpDate 未獲得市場の最終更新日時
	 */
	public void setMikakutokuUpDate(Date mikakutokuUpDate) {
		this.mikakutokuUpDate = mikakutokuUpDate;
	}

	/**
	 * 配分除外施設の登録件数を取得する。
	 *
	 * @return 配分除外施設の登録件数
	 */
	public Integer getExceptDistInsCnt() {
		return exceptDistInsCnt;
	}

	/**
	 * 配分除外施設の登録件数を設定する。
	 *
	 * @param exceptDistInsCnt 配分除外施設の登録件数
	 */
	public void setExceptDistInsCnt(Integer exceptDistInsCnt) {
		this.exceptDistInsCnt = exceptDistInsCnt;
	}

	/**
	 * 配分除外施設の最終更新日時を取得する。
	 *
	 * @return 配分除外施設の最終更新日時
	 */
	public Date getExceptDistInsUpDate() {
		return exceptDistInsUpDate;
	}

	/**
	 * 配分除外施設の最終更新日時を設定する。
	 *
	 * @param exceptDistInsUpDate 配分除外施設の最終更新日時
	 */
	public void setExceptDistInsUpDate(Date exceptDistInsUpDate) {
		this.exceptDistInsUpDate = exceptDistInsUpDate;
	}

	/**
	 * 特定施設個別計画の登録件数を取得する。
	 *
	 * @return 特定施設個別計画の登録件数
	 */
	public Integer getSpecialInsPlanCnt() {
		return specialInsPlanCnt;
	}

	/**
	 * 特定施設個別計画の登録件数を設定する。
	 *
	 * @param specialInsPlanCnt 特定施設個別計画の登録件数
	 */
	public void setSpecialInsPlanCnt(Integer specialInsPlanCnt) {
		this.specialInsPlanCnt = specialInsPlanCnt;
	}

	/**
	 * 特定施設個別計画の最終更新日時を取得する。
	 *
	 * @return 特定施設個別計画の最終更新日時
	 */
	public Date getSpecialInsPlanUpDate() {
		return specialInsPlanUpDate;
	}

	/**
	 * 特定施設個別計画の最終更新日時を設定する。
	 *
	 * @param specialInsPlanUpDate 特定施設個別計画の最終更新日時
	 */
	public void setSpecialInsPlanUpDate(Date specialInsPlanUpDate) {
		this.specialInsPlanUpDate = specialInsPlanUpDate;
	}

	/**
	 *  担当者別計画の最終更新日時を取得する。
	 * @return mrPlanUpDate
	 */
	public Date getMrPlanUpDate() {
		return mrPlanUpDate;
	}

	/**
	 *  担当者別計画の最終更新日時を設定する。
	 * @param mrPlanUpDate セットする mrPlanUpDate
	 */
	public void setMrPlanUpDate(Date mrPlanUpDate) {
		this.mrPlanUpDate = mrPlanUpDate;
	}

	@Override
	public int compareTo(SosEachKindInfo obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.sosCd, obj.sosCd).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && SosEachKindInfo.class.isAssignableFrom(entry.getClass())) {
			SosEachKindInfo obj = (SosEachKindInfo) entry;
			return new EqualsBuilder().append(this.sosCd, obj.sosCd).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.sosCd).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
