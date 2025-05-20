package jp.co.takeda.model.view;

import java.util.Date;

import jp.co.takeda.a.bean.Model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 担当者単位のステータスのサマリーを表すモデルクラス
 *
 * @author khashimoto
 */
public class JgiStatusSummary extends Model<JgiStatusSummary> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 従業員番号
	 */
	private Integer jgiNo;

	/**
	 * 氏名
	 */
	private String jgiName;

	/**
	 * 担当者別計画ステータスのサマリー
	 */
	private MrPlanStatSum mrPlanStatSum;

	/**
	 * 施設医師別計画ステータスのサマリー
	 */
	private InsDocPlanStatSum insDocPlanStatSum;

	/**
	 * 施設特約店別計画ステータスのサマリー
	 */
	private InsWsPlanStatSum insWsPlanStatSum;

	/**
	 * 担・計画－施医・計画調整UHPフラグ
	 */
	private Boolean mrInsdocChoseiUHPFlg;

	/**
	 * 担・計画－施医・計画調整日付
	 */
	private Date mrInsdocChoseiUHPUpDate;

	/**
	 * 担・計画－施特・計画調整UHPフラグ
	 */
	private Boolean mrInswsChoseiUHPFlg;

	/**
	 * 担・計画－施特・計画調整日付
	 */
	private Date mrInswsChoseiUHPUpDate;

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
	 * 氏名を取得する。
	 *
	 * @return 氏名
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * 氏名を設定する。
	 *
	 * @param jgiName 氏名
	 */
	public void setJgiName(String jgiName) {
		this.jgiName = jgiName;
	}

	/**
	 * 担当者別計画ステータスのサマリーを取得する。
	 *
	 * @return 担当者別計画ステータスのサマリー
	 */
	public MrPlanStatSum getMrPlanStatSum() {
		return mrPlanStatSum;
	}

	/**
	 * 担当者別計画ステータスのサマリーを設定する。
	 *
	 * @param mrPlanStatSum 担当者別計画ステータスのサマリー
	 */
	public void setMrPlanStatSum(MrPlanStatSum mrPlanStatSum) {
		this.mrPlanStatSum = mrPlanStatSum;
	}

	/**
	 * 担・計画－施医・計画調整UHPフラグを取得する。
	 *
	 * @return 担・計画－施医・計画調整UHPフラグ
	 */
	public Boolean getMrInsdocChoseiUHPFlg() {
		return mrInsdocChoseiUHPFlg;
	}

	/**
	 * 担・計画－施医・計画調整UHPフラグを設定する。
	 *
	 * @param mrInsdocChoseiUHPFlg 担・計画－施医・計画調整UHPフラグ
	 */
	public void setMrInsdocChoseiUHPFlg(Boolean mrInsdocChoseiUHPFlg) {
		this.mrInsdocChoseiUHPFlg = mrInsdocChoseiUHPFlg;
	}


	/**
	 * 担・計画－施特・計画調整UHPフラグを取得する。
	 *
	 * @return 担・計画－施特・計画調整UHPフラグ
	 */
	public Boolean getMrInswsChoseiUHPFlg() {
		return mrInswsChoseiUHPFlg;
	}

	/**
	 * 担・計画－施特・計画調整UHPフラグを設定する。
	 *
	 * @param mrInswsChoseiUHPFlg 担・計画－施特・計画調整UHPフラグ
	 */
	public void setMrInswsChoseiUHPFlg(Boolean mrInswsChoseiUHPFlg) {
		this.mrInswsChoseiUHPFlg = mrInswsChoseiUHPFlg;
	}

	/**
	 * 施設医師別計画ステータスのサマリーを取得する。
	 *
	 * @return 施設医師別計画ステータスのサマリー
	 */
	public InsDocPlanStatSum getInsDocPlanStatSum() {
		return insDocPlanStatSum;
	}

	/**
	 * 施設医師別計画ステータスのサマリーを設定する。
	 *
	 * @param insDocPlanStatSum 施設医師別計画ステータスのサマリー
	 */
	public void setInsDocPlanStatSum(InsDocPlanStatSum insDocPlanStatSum) {
		this.insDocPlanStatSum = insDocPlanStatSum;
	}

	/**
	 * 施設特約店別計画ステータスのサマリーを取得する。
	 *
	 * @return 施設特約店別計画ステータスのサマリー
	 */
	public InsWsPlanStatSum getInsWsPlanStatSum() {
		return insWsPlanStatSum;
	}

	/**
	 * 施設特約店別計画ステータスのサマリーを設定する。
	 *
	 * @param insWsPlanStatSum 施設特約店別計画ステータスのサマリー
	 */
	public void setInsWsPlanStatSum(InsWsPlanStatSum insWsPlanStatSum) {
		this.insWsPlanStatSum = insWsPlanStatSum;
	}

	/**
	 * 担・計画－施医・計画調整日付を取得する。
	 *
	 * @return 担・計画－施医・計画調整日付
	 */
	public Date getMrInsdocChoseiUHPUpDate() {
		return mrInsdocChoseiUHPUpDate;
	}

	/**
	 * 担・計画－施医・計画調整日付を設定する。
	 *
	 * @param mrInsdocChoseiUHPUpDate 担・計画－施医・計画調整日付
	 */
	public void setMrInsdocChoseiUHPUpDate(Date mrInsdocChoseiUHPUpDate) {
		this.mrInsdocChoseiUHPUpDate = mrInsdocChoseiUHPUpDate;
	}

	/**
	 * 担・計画－施特・計画調整日付を取得する。
	 *
	 * @return 担・計画－施特・計画調整日付
	 */
	public Date getMrInswsChoseiUHPUpDate() {
		return mrInswsChoseiUHPUpDate;
	}

	/**
	 * 担・計画－施特・計画調整日付を設定する。
	 *
	 * @param mrInswsChoseiUHPUpDate 担・計画－施特・計画調整日付
	 */
	public void setMrInswsChoseiUHPUpDate(Date mrInswsChoseiUHPUpDate) {
		this.mrInswsChoseiUHPUpDate = mrInswsChoseiUHPUpDate;
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
	public int compareTo(JgiStatusSummary obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.jgiNo, obj.jgiNo).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && JgiStatusSummary.class.isAssignableFrom(entry.getClass())) {
			JgiStatusSummary obj = (JgiStatusSummary) entry;
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
