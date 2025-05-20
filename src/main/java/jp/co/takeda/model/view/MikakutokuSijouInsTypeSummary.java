package jp.co.takeda.model.view;

import jp.co.takeda.a.bean.Model;
import jp.co.takeda.model.div.InsType;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 未獲得市場の施設出力対象区分別集計を表すモデルクラス
 * 
 * @author nozaki
 */
public class MikakutokuSijouInsTypeSummary extends Model<MikakutokuSijouInsTypeSummary> {

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
	 * 施設出力対象区分
	 */
	private InsType insType;

	/**
	 * 営業所案未獲得市場
	 */
	private Long distPlanMikakutoku;

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
	 * 施設出力対象区分を取得する。
	 * 
	 * @return 施設出力対象区分
	 */
	public InsType getInsType() {
		return insType;
	}

	/**
	 * 施設出力対象区分を設定する。
	 * 
	 * @param insType 施設出力対象区分
	 */
	public void setInsType(InsType insType) {
		this.insType = insType;
	}

	/**
	 * 営業所案未獲得市場を取得する。
	 * 
	 * @return 営業所案未獲得市場
	 */
	public Long getDistPlanMikakutoku() {
		return distPlanMikakutoku;
	}

	/**
	 * 営業所案未獲得市場を設定する。
	 * 
	 * @param distPlanMikakutoku 営業所案未獲得市場
	 */
	public void setDistPlanMikakutoku(Long distPlanMikakutoku) {
		this.distPlanMikakutoku = distPlanMikakutoku;
	}

	@Override
	public int compareTo(MikakutokuSijouInsTypeSummary obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.prodCode, obj.prodCode).append(this.insType, obj.insType).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && MikakutokuSijouInsTypeSummary.class.isAssignableFrom(entry.getClass())) {
			MikakutokuSijouInsTypeSummary obj = (MikakutokuSijouInsTypeSummary) entry;
			return new EqualsBuilder().append(this.prodCode, obj.prodCode).append(this.insType, obj.insType).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.prodCode).append(this.insType).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
