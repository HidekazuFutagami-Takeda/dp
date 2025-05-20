package jp.co.takeda.model.view;

import jp.co.takeda.a.bean.Model;
import jp.co.takeda.model.div.HoInsType;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 特定施設個別計画の組織、対象区分集約を表すモデルクラス
 * 
 * @author nozaki
 */
public class SpecialInsPlanSosSummary extends Model<SpecialInsPlanSosSummary> {

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
	 * 集約計画値(Y)
	 */
	private Long summaryY;

	/**
	 * 対象区分（Ref[施設情報].〔対象区分〕）
	 */
	private HoInsType hoInsType;

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
	 * 集約計画値(Y)を取得する。
	 * 
	 * @return 計画値(Y)
	 */
	public Long getSummaryY() {
		return summaryY;
	}

	/**
	 * 集約計画値(Y)を設定する。
	 * 
	 * @param summaryY 計画値(Y)
	 */
	public void setSummaryY(Long summaryY) {
		this.summaryY = summaryY;
	}

	/**
	 * 対象区分を取得する。
	 * 
	 * @return 対象区分
	 */
	public HoInsType getHoInsType() {
		return hoInsType;
	}

	/**
	 * 対象区分を設定する。
	 * 
	 * @param hoInsType 対象区分
	 */
	public void setHoInsType(HoInsType hoInsType) {
		this.hoInsType = hoInsType;
	}

	@Override
	public int compareTo(SpecialInsPlanSosSummary obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.sosCd, obj.sosCd).append(this.hoInsType, obj.hoInsType).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && SpecialInsPlanSosSummary.class.isAssignableFrom(entry.getClass())) {
			SpecialInsPlanSosSummary obj = (SpecialInsPlanSosSummary) entry;
			return new EqualsBuilder().append(this.sosCd, obj.sosCd).append(this.hoInsType, obj.hoInsType).append(this.prodCode, obj.prodCode).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.sosCd).append(this.hoInsType).append(this.prodCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
