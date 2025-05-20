package jp.co.takeda.model.view;

import java.util.Date;

import jp.co.takeda.a.bean.Model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 組織単位のステータスのサマリーを表すモデルクラス
 *
 * @author khashimoto
 */
public class SosStatusSummary extends Model<SosStatusSummary> {

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
	 * 部門名正式<br>
	 * (該当階層の組織名)
	 */
	private String bumonSeiName;

	/**
	 * 営業所計画ステータスのサマリー
	 */
	private OfficePlanStatSum officePlanStatSum;

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
	 * 営・計画－担・計画調整フラグ
	 */
	private Boolean officeMrChoseiUHPFlg;

	/**
	 * 営・計画－担・計画調整UHP日付
	 */
	private Date officeMrChoseiUHPUpDate;

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
	 * 営・計画－施特・計画調整UHPフラグ
	 */
	private Boolean officeInswsChoseiUHPFlg;

	/**
	 * 営・計画－施特・計画調整日付
	 */
	private Date officeInswsChoseiUHPUpDate;

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
	 * 営業所計画ステータスのサマリーを取得する。
	 *
	 * @return 営業所計画ステータスのサマリー
	 */
	public OfficePlanStatSum getOfficePlanStatSum() {
		return officePlanStatSum;
	}

	/**
	 * 営業所計画ステータスのサマリーを設定する。
	 *
	 * @param officePlanStatSum 営業所計画ステータスのサマリー
	 */
	public void setOfficePlanStatSum(OfficePlanStatSum officePlanStatSum) {
		this.officePlanStatSum = officePlanStatSum;
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
	 * 営・計画－担・計画調整UHPフラグを取得する。
	 *
	 * @return 営・計画－担・計画調整UHPフラグ
	 */
	public Boolean getOfficeMrChoseiUHPFlg() {
		return officeMrChoseiUHPFlg;
	}

	/**
	 * 営・計画－担・計画調整UHPフラグを設定する。
	 *
	 * @param officeMrChoseiUHPFlg 営・計画－担・計画調整UHPフラグ
	 */
	public void setOfficeMrChoseiUHPFlg(Boolean officeMrChoseiUHPFlg) {
		this.officeMrChoseiUHPFlg = officeMrChoseiUHPFlg;
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
	 * 営・計画－施特・計画調整UHPフラグを取得する。
	 *
	 * @return 営・計画－施特・計画調整UHPフラグ
	 */
	public Boolean getOfficeInswsChoseiUHPFlg() {
		return officeInswsChoseiUHPFlg;
	}

	/**
	 * 営・計画－施特・計画調整UHPフラグを設定する。
	 *
	 * @param officeInswsChoseiUHPFlg 営・計画－施特・計画調整UHPフラグ
	 */
	public void setOfficeInswsChoseiUHPFlg(Boolean officeInswsChoseiUHPFlg) {
		this.officeInswsChoseiUHPFlg = officeInswsChoseiUHPFlg;
	}

	/**
	 * 営・計画－担・計画調整UHP日付を取得する。
	 *
	 * @return 営・計画－担・計画調整UHP日付
	 */
	public Date getOfficeMrChoseiUHPUpDate() {
		return officeMrChoseiUHPUpDate;
	}

	/**
	 * 営・計画－担・計画調整UHP日付を設定する。
	 *
	 * @param officeMrChoseiUHPUpDate 営・計画－担・計画調整UHP日付
	 */
	public void setOfficeMrChoseiUHPUpDate(Date officeMrChoseiUHPUpDate) {
		this.officeMrChoseiUHPUpDate = officeMrChoseiUHPUpDate;
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
	 * 営・計画－施特・計画調整日付を取得する。
	 *
	 * @return 営・計画－施特・計画調整日付
	 */
	public Date getOfficeInswsChoseiUHPUpDate() {
		return officeInswsChoseiUHPUpDate;
	}

	/**
	 * 営・計画－施特・計画調整日付を設定する。
	 *
	 * @param officeInswsChoseiUHPUpDate 営・計画－施特・計画調整日付
	 */
	public void setOfficeInswsChoseiUHPUpDate(Date officeInswsChoseiUHPUpDate) {
		this.officeInswsChoseiUHPUpDate = officeInswsChoseiUHPUpDate;
	}

	@Override
	public int compareTo(SosStatusSummary obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.sosCd, obj.sosCd).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && SosStatusSummary.class.isAssignableFrom(entry.getClass())) {
			SosStatusSummary obj = (SosStatusSummary) entry;
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
