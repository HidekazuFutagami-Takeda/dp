package jp.co.takeda.model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.a.bean.Model;
import jp.co.takeda.model.div.MarketClass;
import jp.co.takeda.model.div.MarketType;

/**
 * [外部直接参照]市場規模を表すモデルクラス
 * 
 * @author tkawabata
 */
public class InsMarket extends Model<InsMarket> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 関連情報区分([013]固定)
	 */
	public static final String RELEV_TYPE = "013";

	/**
	 * 関連情報コード([00]固定)
	 */
	public static final String RELEV_CD = "00";

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 施設コード
	 */
	private String insNo;

	/**
	 * 設定コード
	 */
	private MarketClass setCd;

	/**
	 * 設定値
	 */
	private MarketType setValue;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 施設コードを取得する。
	 * 
	 * @return 施設コード
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 施設コードを設定する。
	 * 
	 * @param insNo 施設コード
	 */
	public void setInsNo(String insNo) {
		this.insNo = insNo;
	}

	/**
	 * 設定コードを取得する。
	 * 
	 * @return 設定コード
	 */
	public MarketClass getSetCd() {
		return setCd;
	}

	/**
	 * 設定コードを設定する。
	 * 
	 * @param setCd 設定コード
	 */
	public void setSetCd(MarketClass setCd) {
		this.setCd = setCd;
	}

	/**
	 * 設定値を取得する。
	 * 
	 * @return 設定値
	 */
	public MarketType getSetValue() {
		return setValue;
	}

	/**
	 * 設定値を設定する。
	 * 
	 * @param setValue 設定値
	 */
	public void setSetValue(MarketType setValue) {
		this.setValue = setValue;
	}

	@Override
	public int compareTo(InsMarket obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.insNo, obj.insNo).append(this.setCd, obj.setCd).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && InsMarket.class.isAssignableFrom(entry.getClass())) {
			InsMarket obj = (InsMarket) entry;
			return new EqualsBuilder().append(this.insNo, obj.insNo).append(this.setCd, obj.setCd).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.insNo).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
