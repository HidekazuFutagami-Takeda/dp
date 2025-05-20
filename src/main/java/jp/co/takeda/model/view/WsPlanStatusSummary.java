package jp.co.takeda.model.view;

import java.util.Date;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.a.bean.Model;

/**
 * 特約店別計画ステータスのサマリーを表すモデルクラス
 *
 * @author khashimoto
 */
public class WsPlanStatusSummary extends Model<WsPlanStatusSummary> {

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
	 * 該当無の件数
	 */
	private Integer none;

	/**
	 * 配分前の件数
	 */
	private Integer distNone;

	/**
	 * 配分中の件数
	 */
	private Integer disting;

	/**
	 * 配分済の件数
	 */
	private Integer disted;

	/**
	 * スライド前の件数
	 */
	private Integer slideNone;

	/**
	 * スライド中の件数
	 */
	private Integer sliding;

	/**
	 * スライド済の件数
	 */
	private Integer slided;

	/**
	 * 特約店別計画ステータスの母数(支店×品目)
	 */
	private Integer totalCount;

	/**
	 * 配分開始日時
	 */
	private Date distStartDate;

	/**
	 * 配分終了日時
	 */
	private Date distEndDate;

	/**
	 * スライド開始日時
	 */
	private Date slideStartDate;

	/**
	 * スライド終了日時
	 */
	private Date slideEndDate;

	/**
	 * カテゴリ
	 */
	private String category;

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
	 * 該当無の件数を取得する。
	 *
	 * @return 該当無の件数
	 */
	public Integer getNone() {
		return none;
	}

	/**
	 * 該当無の件数を設定する。
	 *
	 * @param none 該当無の件数
	 */
	public void setNone(Integer none) {
		this.none = none;
	}

	/**
	 * 配分前の件数を取得する。
	 *
	 * @return 配分前の件数
	 */
	public Integer getDistNone() {
		return distNone;
	}

	/**
	 * 配分前の件数を設定する。
	 *
	 * @param distNone 配分前の件数
	 */
	public void setDistNone(Integer distNone) {
		this.distNone = distNone;
	}

	/**
	 * 配分中の件数を取得する。
	 *
	 * @return 配分中の件数
	 */
	public Integer getDisting() {
		return disting;
	}

	/**
	 * 配分中の件数を設定する。
	 *
	 * @param disting 配分中の件数
	 */
	public void setDisting(Integer disting) {
		this.disting = disting;
	}

	/**
	 * 配分済の件数を取得する。
	 *
	 * @return disted 配分済の件数
	 */
	public Integer getDisted() {
		return disted;
	}

	/**
	 * 配分済の件数を設定する。
	 *
	 * @param disted 配分済の件数
	 */
	public void setDisted(Integer disted) {
		this.disted = disted;
	}

	/**
	 * スライド前の件数を取得する。
	 *
	 * @return スライド前の件数
	 */
	public Integer getSlideNone() {
		return slideNone;
	}

	/**
	 * スライド前の件数を設定する。
	 *
	 * @param slideNone スライド前の件数
	 */
	public void setSlideNone(Integer slideNone) {
		this.slideNone = slideNone;
	}

	/**
	 * スライド中の件数を取得する。
	 *
	 * @return スライド中の件数
	 */
	public Integer getSliding() {
		return sliding;
	}

	/**
	 * スライド中の件数を設定する。
	 *
	 * @param sliding スライド中の件数
	 */
	public void setSliding(Integer sliding) {
		this.sliding = sliding;
	}

	/**
	 * スライド済の件数を取得する。
	 *
	 * @return スライド済の件数
	 */
	public Integer getSlided() {
		return slided;
	}

	/**
	 * スライド済の件数を設定する。
	 *
	 * @param slided スライド済の件数
	 */
	public void setSlided(Integer slided) {
		this.slided = slided;
	}

	/**
	 * 施設特約店別計画ステータスの母数(支店×品目)を取得する。
	 *
	 * @return 施設特約店別計画ステータスの母数
	 */
	public Integer getTotalCount() {
		return totalCount;
	}

	/**
	 * 施設特約店別計画ステータスの母数(支店×品目)を設定する。
	 *
	 * @param totalCount 施設特約店別計画ステータスの母数
	 */
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 配分開始日時を取得する。
	 *
	 * @return 配分開始日時
	 */
	public Date getDistStartDate() {
		return distStartDate;
	}

	/**
	 * 配分開始日時を設定する。
	 *
	 * @param distStartDate 配分開始日時
	 */
	public void setDistStartDate(Date distStartDate) {
		this.distStartDate = distStartDate;
	}

	/**
	 * 配分終了日時を取得する。
	 *
	 * @return 配分終了日時
	 */
	public Date getDistEndDate() {
		return distEndDate;
	}

	/**
	 * 配分終了日時を設定する。
	 *
	 * @param distEndDate 配分終了日時
	 */
	public void setDistEndDate(Date distEndDate) {
		this.distEndDate = distEndDate;
	}

	/**
	 * スライド開始日時を取得する。
	 *
	 * @return スライド開始日時
	 */
	public Date getSlideStartDate() {
		return slideStartDate;
	}

	/**
	 * スライド開始日時を設定する。
	 *
	 * @param slideStartDate スライド開始日時
	 */
	public void setSlideStartDate(Date slideStartDate) {
		this.slideStartDate = slideStartDate;
	}

	/**
	 * スライド終了日時を取得する。
	 *
	 * @return スライド終了日時
	 */
	public Date getSlideEndDate() {
		return slideEndDate;
	}

	/**
	 * スライド終了日時を設定する。
	 *
	 * @param slideEndDate スライド終了日時
	 */
	public void setSlideEndDate(Date slideEndDate) {
		this.slideEndDate = slideEndDate;
	}

	/**
	 * @return category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category セットする category
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public int compareTo(WsPlanStatusSummary obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.sosCd, obj.sosCd).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && WsPlanStatusSummary.class.isAssignableFrom(entry.getClass())) {
			WsPlanStatusSummary obj = (WsPlanStatusSummary) entry;
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
