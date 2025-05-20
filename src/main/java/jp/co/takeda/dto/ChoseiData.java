package jp.co.takeda.dto;

import java.io.Serializable;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.SystemException;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 調整計画の組織リンクを表すオブジェクト
 * 
 * @author tkawabata
 */
public class ChoseiData implements Serializable, Comparable<ChoseiData> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード
	 */
	private final String sosCd;

	/**
	 * 部門ランク
	 */
	private final String bumonRank;

	/**
	 * 組織コードと部門ランクを設定するコンストラクタ
	 * 
	 * @param sosCd コード(NULL不可)
	 * @param bumonRank 値(NULL不可)
	 */
	public ChoseiData(final String sosCd, final String bumonRank) {
		if (sosCd == null) {
			throw new SystemException(new Conveyance(ErrMessageKey.PARAMETER_ERROR));
		}
		if (bumonRank == null) {
			throw new SystemException(new Conveyance(ErrMessageKey.PARAMETER_ERROR));
		}

		this.sosCd = sosCd;
		this.bumonRank = bumonRank;
	}

	/**
	 * 組織コードを取得する。
	 * 
	 * @return 組織コード
	 */
	public String getSosCd() {
		return sosCd;
	}

	/**
	 * 部門ランクを取得する。
	 * 
	 * @return 部門ランク
	 */
	public String getBumonRank() {
		return bumonRank;
	}

	public int compareTo(ChoseiData obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.sosCd, obj.sosCd).append(this.bumonRank, obj.bumonRank).toComparison();
		}
		return -1;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("sosCd=").append(this.sosCd).append(",");
		sb.append("bumonRank=").append(this.bumonRank);
		return sb.toString();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj != null && ChoseiData.class.isAssignableFrom(obj.getClass())) {
			ChoseiData cv = (ChoseiData) obj;
			return new EqualsBuilder().append(this.sosCd, cv.sosCd).append(this.bumonRank, cv.bumonRank).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.sosCd).append(this.bumonRank).toHashCode();
	}
}
