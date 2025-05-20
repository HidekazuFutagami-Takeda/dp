package jp.co.takeda.model;

import java.util.Date;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.div.StatusForInsWsPlan;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設特約店別計画ステータスを表すモデルクラス
 *
 * @author tkawabata
 */
public class InsWsPlanStatus extends DpModel<InsWsPlanStatus> {

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
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 施設特約店別計画ステータス
	 */
	private StatusForInsWsPlan statusForInsWsPlan;

	/**
	 * 非同期処理実行前ステータス
	 */
	private StatusForInsWsPlan asyncBefStatus;

	/**
	 * サーバ区分
	 */
	private String appServerKbn;

	/**
	 * 非同期処理実行前配分開始日時
	 */
	private Date asyncBefDistStartDate;

	/**
	 * 配分開始日時
	 */
	private Date distStartDate;

	/**
	 * 配分終了日時
	 */
	private Date distEndDate;

	/**
	 * 施設特約店別計画公開日時
	 */
	private Date insWsOpenDate;

	/**
	 * 施設特約店別計画確定日時
	 */
	private Date insWsFixDate;

	/**
	 * 氏名（Ref[従業員情報].〔氏名〕）
	 */
	private String jgiName;

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
	 * 施設特約店別計画ステータスを取得する。
	 *
	 * @return 施設特約店別計画ステータス
	 */
	public StatusForInsWsPlan getStatusForInsWsPlan() {
		return statusForInsWsPlan;
	}

	/**
	 * 施設特約店別計画ステータスを設定する。
	 *
	 * @param statusForInsWsPlan 施設特約店別計画ステータス
	 */
	public void setStatusForInsWsPlan(StatusForInsWsPlan statusForInsWsPlan) {
		this.statusForInsWsPlan = statusForInsWsPlan;
	}

	/**
	 * 非同期処理実行前ステータスを取得する。
	 *
	 * @return 非同期処理実行前ステータス
	 */
	public StatusForInsWsPlan getAsyncBefStatus() {
		return asyncBefStatus;
	}

	/**
	 * 非同期処理実行前ステータスを設定する。
	 *
	 * @param 非同期処理実行前ステータス
	 */
	public void setAsyncBefStatus(StatusForInsWsPlan asyncBefStatus) {
		this.asyncBefStatus = asyncBefStatus;
	}

	/**
	 * サーバー区分を取得する。
	 *
	 * @return サーバー区分
	 */
	public String getAppServerKbn() {
		return appServerKbn;
	}

	/**
	 * サーバー区分を設定する。
	 *
	 * @param appServerKbn サーバー区分
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
	 * @param asyncBefDistStartDate 非同期処理実行前配分開始日時
	 */
	public void setAsyncBefDistStartDate(Date asyncBefDistStartDate) {
		this.asyncBefDistStartDate = asyncBefDistStartDate;
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
	 * 施設特約店別計画公開日時を取得する。
	 *
	 * @return 施設特約店別計画公開日時
	 */
	public Date getInsWsOpenDate() {
		return insWsOpenDate;
	}

	/**
	 * 施設特約店別計画公開日時を設定する。
	 *
	 * @param insWsOpenDate 施設特約店別計画公開日時
	 */
	public void setInsWsOpenDate(Date insWsOpenDate) {
		this.insWsOpenDate = insWsOpenDate;
	}

	/**
	 * 施設特約店別計画確定日時を取得する。
	 *
	 * @return 施設特約店別計画確定日時
	 */
	public Date getInsWsFixDate() {
		return insWsFixDate;
	}

	/**
	 * 施設特約店別計画確定日時を設定する。
	 *
	 * @param insWsFixDate 施設特約店別計画確定日時
	 */
	public void setInsWsFixDate(Date insWsFixDate) {
		this.insWsFixDate = insWsFixDate;
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
	public int compareTo(InsWsPlanStatus obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.jgiNo, obj.jgiNo).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && InsWsPlanStatus.class.isAssignableFrom(entry.getClass())) {
			InsWsPlanStatus obj = (InsWsPlanStatus) entry;
			return new EqualsBuilder().append(this.jgiNo, obj.jgiNo).append(this.prodCode, obj.prodCode).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.jgiNo).append(this.prodCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

	@Override
	public InsWsPlanStatus clone() {
		if (this == null) {
			return null;
		}
		InsWsPlanStatus insWsPlanStatus = new InsWsPlanStatus();
		insWsPlanStatus.setSeqKey(this.seqKey);
		insWsPlanStatus.setJgiNo(this.jgiNo);
		insWsPlanStatus.setProdCode(this.prodCode);
		insWsPlanStatus.setStatusForInsWsPlan(this.statusForInsWsPlan);
		insWsPlanStatus.setDistStartDate(this.distStartDate);
		insWsPlanStatus.setDistEndDate(this.distEndDate);
		insWsPlanStatus.setInsWsOpenDate(this.insWsOpenDate);
		insWsPlanStatus.setInsWsFixDate(this.insWsFixDate);
		insWsPlanStatus.setIsJgiNo(this.isJgiNo);
		insWsPlanStatus.setIsJgiName(this.isJgiName);
		insWsPlanStatus.setIsDate(this.isDate);
		insWsPlanStatus.setUpJgiNo(this.isJgiNo);
		insWsPlanStatus.setUpJgiName(this.upJgiName);
		insWsPlanStatus.setUpDate(this.upDate);
		return insWsPlanStatus;
	}
}
