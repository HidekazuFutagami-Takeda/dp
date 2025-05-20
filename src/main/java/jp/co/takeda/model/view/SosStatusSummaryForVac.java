package jp.co.takeda.model.view;

import jp.co.takeda.a.bean.Model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (ワクチン)組織単位のステータスのサマリーを表すモデルクラス
 * 
 * @author khashimoto
 */
public class SosStatusSummaryForVac extends Model<SosStatusSummaryForVac> {

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
	public int compareTo(SosStatusSummaryForVac obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.sosCd, obj.sosCd).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && SosStatusSummaryForVac.class.isAssignableFrom(entry.getClass())) {
			SosStatusSummaryForVac obj = (SosStatusSummaryForVac) entry;
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
