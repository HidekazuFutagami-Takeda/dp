package jp.co.takeda.model.view;

import java.util.Date;

import jp.co.takeda.a.bean.Model;
import jp.co.takeda.model.div.StatusForInsWsPlan;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (ワクチン)配分対象品目一覧を表すモデルクラス
 * 
 * @author khashimoto
 */
public class DistributionForVacProd extends Model<DistributionForVacProd> {

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
	 * 品目名称
	 */
	private String prodName;

	/**
	 * (ワクチン)施設特約店別計画立案ステータス
	 */
	private StatusForInsWsPlan insWsPlanStatus;

	/**
	 * 配分開始日時
	 */
	private Date distStartDate;

	/**
	 * 配分終了日時
	 */
	private Date distEndDate;

	/**
	 * (ワクチン)施設特約店別計画立案ステータス最終更新日
	 */
	private Date statusLastUpdate;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 品目名称を取得する。
	 * 
	 * @return 品目名称
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 品目名称を設定する。
	 * 
	 * @param prodName 品目名称
	 */
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	/**
	 * 施設特約店別計画立案ステータスを取得する。
	 * 
	 * @return 施設特約店別計画立案ステータス
	 */
	public StatusForInsWsPlan getStatusForInsWsPlan() {
		return insWsPlanStatus;
	}

	/**
	 * 施設特約店別計画立案ステータスを設定する。
	 * 
	 * @param insWsPlanStatus 施設特約店別計画立案ステータス
	 */
	public void setStatusForInsWsPlan(StatusForInsWsPlan insWsPlanStatus) {
		this.insWsPlanStatus = insWsPlanStatus;
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
	 * 施設特約店別計画立案ステータス最終更新日を取得する。
	 * 
	 * @return 施設特約店別計画立案ステータス最終更新日
	 */
	public Date getStatusLastUpdate() {
		return statusLastUpdate;
	}

	/**
	 * 施設特約店別計画立案ステータス最終更新日を設定する。
	 * 
	 * @param statusLastUpdate 施設特約店別計画立案ステータス最終更新日
	 */
	public void setStatusLastUpdate(Date statusLastUpdate) {
		this.statusLastUpdate = statusLastUpdate;
	}

	@Override
	public int compareTo(DistributionForVacProd obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && DistributionForVacProd.class.isAssignableFrom(entry.getClass())) {
			DistributionForVacProd obj = (DistributionForVacProd) entry;
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
