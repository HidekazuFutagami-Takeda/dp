package jp.co.takeda.model;

import java.sql.Date;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.a.bean.Model;
import jp.co.takeda.model.div.Day;
import jp.co.takeda.model.div.Term;

/**
 * [外部直接参照]カレンダーを表すモデルクラス
 * 
 * @author tkawabata
 */
public class Cal extends Model<Cal> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 年
	 */
	private String calYear;

	/**
	 * 月
	 */
	private String calMonth;

	/**
	 * 日
	 */
	private String calDay;

	/**
	 * 年度
	 */
	private String fiscalYear;

	/**
	 * 曜日
	 */
	private Day dayKb;

	/**
	 * 年月日
	 */
	private Date calDate;

	/**
	 * 期
	 */
	private Term term;

	/**
	 * 祝日フラグ
	 */
	private Boolean pubHolidayFlg;

	/**
	 * 営業日フラグ
	 */
	private Boolean bussinessDayFlg;

	/**
	 * 本日フラグ
	 */
	private Boolean todayFlg;

	/**
	 * 摘要
	 */
	private String summary;

	/**
	 * 月内営業日順位
	 */
	private Integer bizDays;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 年を取得する。
	 * 
	 * @return 年
	 */
	public String getCalYear() {
		return calYear;
	}

	/**
	 * 年を設定する。
	 * 
	 * @param calYear 年
	 */
	public void setCalYear(String calYear) {
		this.calYear = calYear;
	}

	/**
	 * 月を取得する。
	 * 
	 * @return 月
	 */
	public String getCalMonth() {
		return calMonth;
	}

	/**
	 * 月を設定する。
	 * 
	 * @param calMonth 月
	 */
	public void setCalMonth(String calMonth) {
		this.calMonth = calMonth;
	}

	/**
	 * 日を取得する。
	 * 
	 * @return 日
	 */
	public String getCalDay() {
		return calDay;
	}

	/**
	 * 日を設定する。
	 * 
	 * @param calDay 日
	 */
	public void setCalDay(String calDay) {
		this.calDay = calDay;
	}

	/**
	 * 年度を取得する。
	 * 
	 * @return 年度
	 */
	public String getFiscalYear() {
		return fiscalYear;
	}

	/**
	 * 年度を設定する。
	 * 
	 * @param fiscalYear 年度
	 */
	public void setFiscalYear(String fiscalYear) {
		this.fiscalYear = fiscalYear;
	}

	/**
	 * 曜日を取得する。
	 * 
	 * @return 曜日
	 */
	public Day getDayKb() {
		return dayKb;
	}

	/**
	 * 曜日を設定する。
	 * 
	 * @param dayKb 曜日
	 */
	public void setDayKb(Day dayKb) {
		this.dayKb = dayKb;
	}

	/**
	 * 年月日を取得する。
	 * 
	 * @return 年月日
	 */
	public Date getCalDate() {
		return calDate;
	}

	/**
	 * 年月日を設定する。
	 * 
	 * @param calDate 年月日
	 */
	public void setCalDate(Date calDate) {
		this.calDate = calDate;
	}

	/**
	 * 期を取得する。
	 * 
	 * @return 期
	 */
	public Term getTerm() {
		return term;
	}

	/**
	 * 期を設定する。
	 * 
	 * @param term 期
	 */
	public void setTerm(Term term) {
		this.term = term;
	}

	/**
	 * 祝日フラグを取得する。
	 * 
	 * @return 祝日フラグ
	 */
	public Boolean getPubHolidayFlg() {
		return pubHolidayFlg;
	}

	/**
	 * 祝日フラグを設定する。
	 * 
	 * @param pubHolidayFlg 祝日フラグ
	 */
	public void setPubHolidayFlg(Boolean pubHolidayFlg) {
		this.pubHolidayFlg = pubHolidayFlg;
	}

	/**
	 * 営業日フラグを取得する。
	 * 
	 * @return 営業日フラグ
	 */
	public Boolean getBussinessDayFlg() {
		return bussinessDayFlg;
	}

	/**
	 * 営業日フラグを設定する。
	 * 
	 * @param bussinessDayFlg 営業日フラグ
	 */
	public void setBussinessDayFlg(Boolean bussinessDayFlg) {
		this.bussinessDayFlg = bussinessDayFlg;
	}

	/**
	 * 本日フラグを取得する。
	 * 
	 * @return 本日フラグ
	 */
	public Boolean getTodayFlg() {
		return todayFlg;
	}

	/**
	 * 本日フラグを設定する。
	 * 
	 * @param todayFlg 本日フラグ
	 */
	public void setTodayFlg(Boolean todayFlg) {
		this.todayFlg = todayFlg;
	}

	/**
	 * 摘要を取得する。
	 * 
	 * @return 摘要
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * 摘要を設定する。
	 * 
	 * @param summary 摘要
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * 月内営業日順位を取得する。
	 * 
	 * @return 月内営業日順位
	 */
	public Integer getBizDays() {
		return bizDays;
	}

	/**
	 * 月内営業日順位を設定する。
	 * 
	 * @param bizDays 月内営業日順位
	 */
	public void setBizDays(Integer bizDays) {
		this.bizDays = bizDays;
	}

	@Override
	public int compareTo(Cal obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.calYear, obj.calYear).append(this.calMonth, obj.calMonth).append(this.calDay, obj.calDay).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && Cal.class.isAssignableFrom(entry.getClass())) {
			Cal obj = (Cal) entry;
			return new EqualsBuilder().append(this.calYear, obj.calYear).append(this.calMonth, obj.calMonth).append(this.calDay, obj.calDay).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.calYear).append(this.calMonth).append(this.calDay).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
