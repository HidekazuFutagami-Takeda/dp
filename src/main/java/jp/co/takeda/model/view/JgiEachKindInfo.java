package jp.co.takeda.model.view;

import java.util.Date;

import jp.co.takeda.a.bean.Model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 担当者単位の各種登録状況を表すモデルクラス
 *
 * @author khashimoto
 */
public class JgiEachKindInfo extends Model<JgiEachKindInfo> {

	//---------------------
	// Data Field
	// --------------------

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 従業員番号
	 */
	private Integer jgiNo;

	/**
	 * 従業員名
	 */
	private String jgiName;

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
	 * 職種名
	 */
	private String shokushuName;


	//---------------------
	// Getter & Setter
	// --------------------
	
	/**
	 * 従業員番号を取得する。
	 *
	 * @return 従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 従業員番号を設定する。
	 *
	 * @param jgiNo 従業員番号
	 */
	public void setJgiNo(Integer jgiNo) {
		this.jgiNo = jgiNo;
	}

	/**
	 * 従業員名を取得する。
	 *
	 * @return jgiName 従業員名
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * 従業員名を設定する。
	 *
	 * @param jgiName 従業員名
	 */
	public void setJgiName(String jgiName) {
		this.jgiName = jgiName;
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
	 * 職種名を取得する
	 * @return shokushuName
	 */
	public String getShokushuName() {
		return shokushuName;
	}

	/**
	 * 職種名を設定する
	 * @param shokushuName
	 */
	public void setShokushuName(String shokushuName) {
		this.shokushuName = shokushuName;
	}

	@Override
	public int compareTo(JgiEachKindInfo obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.jgiNo, obj.jgiNo).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && JgiEachKindInfo.class.isAssignableFrom(entry.getClass())) {
			JgiEachKindInfo obj = (JgiEachKindInfo) entry;
			return new EqualsBuilder().append(this.jgiNo, obj.jgiNo).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.jgiNo).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
