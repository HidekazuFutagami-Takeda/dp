package jp.co.takeda.model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.a.bean.Model;

/**
 * 組織グループを表すモデルクラス
 *
 * @author rna8405
 *
 */
public final class SosGrp extends Model<SosGrp> {
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
	private String sosGrop;

	/**
	 * 組織グループ
	 */
	private String sosCd;


	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 組織グループを取得する
	 *
	 * @return sosGrop 組織グループ
	 */
	public String getSosGrop() {
		return sosGrop;
	}

	/**
	 * 組織コードを取得する
	 *
	 * @return sosCd 組織コード
	 */
	public String getSosCd() {
		return sosCd;
	}

	/**
	 * 組織グループを設定する
	 *
	 * @param sosGrop 組織グループ
	 */
	public void setSosGrop(String sosGrop) {
		this.sosGrop = sosGrop;
	}

	/**
	 *  組織コードを設定する
	 *
	 * @param sosCd 組織コード
	 */
	public void setSosCd(String sosCd) {
		this.sosCd = sosCd;
	}

	@Override
	public int compareTo(SosGrp obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.sosGrop, obj.sosCd).append(this.sosCd, obj.sosCd).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && SosMstCtg.class.isAssignableFrom(entry.getClass())) {
			SosGrp obj = (SosGrp) entry;
			return new EqualsBuilder().append(this.sosGrop, obj.sosGrop).append(this.sosCd, obj.sosCd).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.sosGrop).append(this.sosCd).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
