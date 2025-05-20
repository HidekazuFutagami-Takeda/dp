package jp.co.takeda.model;

import java.util.Date;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.div.StatusForWs;
import jp.co.takeda.model.div.StatusForWsSlide;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 特約店別計画ステータスを表すモデルクラス
 * 
 * @author tkawabata
 */
public class WsPlanStatus extends DpModel<WsPlanStatus> {

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
	 * 配分ステータス
	 */
	private StatusForWs statusDistForWs;

	/**
	 * 非同期処理実行前配分ステータス
	 */
	private StatusForWs asyncBefStatus;

	/**
	 * サーバ区分
	 */
	private String appServerKbn;

	/**
	 * 非同期処理実行前配分開始日時
	 */
	private Date asyncBefDistStartDate;

	/**
	 * スライドステータス
	 */
	private StatusForWsSlide statusSlideForWs;

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
	 * 配分ステータスを取得する。
	 * 
	 * @return 配分ステータス
	 */
	public StatusForWs getStatusDistForWs() {
		return statusDistForWs;
	}

	/**
	 * 配分ステータスを設定する。
	 * 
	 * @param statusDistForWs 配分ステータス
	 */
	public void setStatusDistForWs(StatusForWs statusDistForWs) {
		this.statusDistForWs = statusDistForWs;
	}

	/**
	 * 非同期処理実行前配分ステータスを取得する。
	 * 
	 * @return asyncBefStatus 非同期処理実行前配分ステータス
	 */
	public StatusForWs getAsyncBefStatus() {
		return asyncBefStatus;
	}

	/**
	 * 非同期処理実行前配分ステータスを設定する。
	 * 
	 * @param asyncBefStatus 非同期処理実行前配分ステータス
	 */
	public void setAsyncBefStatus(StatusForWs asyncBefStatus) {
		this.asyncBefStatus = asyncBefStatus;
	}

	/**
	 * サーバ区分を取得する。
	 * 
	 * @return appServerKbn サーバ区分
	 */
	public String getAppServerKbn() {
		return appServerKbn;
	}

	/**
	 * サーバ区分を設定する。
	 * 
	 * @param appServerKbn サーバ区分
	 */
	public void setAppServerKbn(String appServerKbn) {
		this.appServerKbn = appServerKbn;
	}

	/**
	 * 非同期処理実行前配分開始日時を取得する。
	 * 
	 * @return 非同期処理実行前配分開始日時
	 */
	public Date getAsyncBefDistStartDate() {
		return asyncBefDistStartDate;
	}

	/**
	 * 非同期処理実行前配分開始日時を設定する。
	 * 
	 * @param asyncBefdistStartDate 非同期処理実行前配分開始日時
	 */
	public void setAsyncBefDistStartDate(Date asyncBefDistStartDate) {
		this.asyncBefDistStartDate = asyncBefDistStartDate;
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

	@Override
	public int compareTo(WsPlanStatus obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.sosCd, obj.sosCd).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && WsPlanStatus.class.isAssignableFrom(entry.getClass())) {
			WsPlanStatus obj = (WsPlanStatus) entry;
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
