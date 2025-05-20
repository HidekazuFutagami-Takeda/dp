package jp.co.takeda.model.view;

import java.util.Date;

import jp.co.takeda.a.bean.Model;
import jp.co.takeda.model.div.StatusForInsWsPlan;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 配分対象品目一覧を表すモデルクラス
 * 
 * @author nozaki
 */
public class DistributionProd extends Model<DistributionProd> {

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
	 * 品目名称
	 */
	private String prodName;

	/**
	 * 製品区分
	 */
	private String prodType;

	/**
	 * 一番進んでいる施設特約店別計画立案ステータス
	 */
	private StatusForInsWsPlan insWsPlanStatus;

	/**
	 * 一番進んでいるステータス最終更新日
	 */
	private Date statusLastUpdate;

	/**
	 * 施設特約店別計画ステータスのサマリー
	 */
	private InsWsPlanStatSum insWsPlanStatSum;

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
	 * 製品区分を取得する。
	 * 
	 * @return 製品区分
	 */
	public String getProdType() {
		return prodType;
	}

	/**
	 * 製品区分を設定する。
	 * 
	 * @param prodType 製品区分
	 */
	public void setProdType(String prodType) {
		this.prodType = prodType;
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

	@Override
	public int compareTo(DistributionProd obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.sosCd, obj.sosCd).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && DistributionProd.class.isAssignableFrom(entry.getClass())) {
			DistributionProd obj = (DistributionProd) entry;
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
