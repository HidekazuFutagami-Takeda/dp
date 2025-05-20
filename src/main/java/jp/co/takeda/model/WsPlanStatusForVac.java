package jp.co.takeda.model;

import java.util.Date;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.div.StatusForWsSlide;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (ワクチン)特約店別計画ステータスを表すクラス
 * 
 * @author tkawabata
 */
public class WsPlanStatusForVac extends DpModel<WsPlanStatusForVac> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * スライドステータス
	 */
	private StatusForWsSlide statusSlideForWs;

	/**
	 * スライド開始日時
	 */
	private Date slideStartDate;

	/**
	 * スライド終了日時
	 */
	private Date slideEndDate;

	//---------------------
	// Getter & Setter
	// --------------------

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
	 * スライドステータスを取得する。
	 * 
	 * @return スライドステータス
	 */
	public StatusForWsSlide getStatusSlideForWs() {
		return statusSlideForWs;
	}

	/**
	 * スライドステータスを設定する。
	 * 
	 * @param statusSlideForWs スライドステータス
	 */
	public void setStatusSlideForWs(StatusForWsSlide statusSlideForWs) {
		this.statusSlideForWs = statusSlideForWs;
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

	@Override
	public int compareTo(WsPlanStatusForVac obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && WsPlanStatusForVac.class.isAssignableFrom(entry.getClass())) {
			WsPlanStatusForVac obj = (WsPlanStatusForVac) entry;
			return new EqualsBuilder().append(this.prodCode, obj.prodCode).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.prodCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
