package jp.co.takeda.model;

import jp.co.takeda.bean.DpModel;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 営業所調整を表すモデルクラス
 *
 * @author mtsuchida
 */
public class OfficeChosei extends DpModel<OfficeChosei> {

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
	 *営・計画－担・計画調整額UH（Ｙ価）
	 */
	private Long officeMrChoseiUhY;

	/**
	 * 営・計画－施特・計画調整額UH（Ｙ価）
	 */
	private Long officeInswsChoseiUhY;

	/**
	 * 担・計画－施特・計画調整UHフラグ
	 */
	private Boolean mrInswsChoseiUhFlg;

	/**
	 *営・計画－担・計画調整額P（Ｙ価）
	 */
	private Long officeMrChoseiPY;

	/**
	 * 営・計画－施特・計画調整額P（Ｙ価）
	 */
	private Long officeInswsChoseiPY;

	/**
	 * 担・計画－施特・計画調整Pフラグ
	 */
	private Boolean mrInswsChoseiPFlg;

	/**
	 *営・計画－担・計画調整額UHP（Ｙ価）
	 */
	private Long officeMrChoseiUHPY;

	/**
	 * 営・計画－施特・計画調整額UHP（Ｙ価）
	 */
	private Long officeInswsChoseiUHPY;

	/**
	 * 担・計画－施特・計画調整UHPフラグ
	 */
	private Boolean mrInswsChoseiUHPFlg;

	/**
	 * 担・計画－施医・計画調整UHフラグ
	 */
	private Boolean mrDrChoseiUhFlg;

	/**
	 * 担・計画－施医・計画調整Pフラグ
	 */
	private Boolean mrDrChoseiPFlg;

	/**
	 * 担・計画－施医・計画調整UHPフラグ
	 */
	private Boolean mrDrChoseiUHPFlg;

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
	 * 営・計画－担・計画調整額UH（Ｙ価）を取得する。
	 *
	 * @return 営・計画－担・計画調整額UH（Ｙ価）
	 */
	public Long getOfficeMrChoseiUhY() {
		return officeMrChoseiUhY;
	}

	/**
	 * 営・計画－担・計画調整額UH（Ｙ価）を設定する。
	 *
	 * @param officeMrChoseiUhY 営・計画－担・計画調整額UH（Ｙ価）
	 */
	public void setOfficeMrChoseiUhY(Long officeMrChoseiUhY) {
		this.officeMrChoseiUhY = officeMrChoseiUhY;
	}

	/**
	 * 営・計画－施特・計画調整額UH（Ｙ価）を取得する。
	 *
	 * @return 営・計画－施特・計画調整額UH（Ｙ価）
	 */
	public Long getOfficeInswsChoseiUhY() {
		return officeInswsChoseiUhY;
	}

	/**
	 * 営・計画－施特・計画調整額UH（Ｙ価）を設定する。
	 *
	 * @param officeInswsChoseiUhY 営・計画－施特・計画調整額UH（Ｙ価）
	 */
	public void setOfficeInswsChoseiUhY(Long officeInswsChoseiUhY) {
		this.officeInswsChoseiUhY = officeInswsChoseiUhY;
	}

	/**
	 * 担・計画－施特・計画調整UHフラグを取得する。
	 *
	 * @return 担・計画－施特・計画調整UHフラグ
	 */
	public Boolean getMrInswsChoseiUhFlg() {
		return mrInswsChoseiUhFlg;
	}

	/**
	 * 担・計画－施特・計画調整UHフラグを設定する。
	 *
	 * @param mrInswsChoseiUhFlg 担・計画－施特・計画調整UHフラグ
	 */
	public void setMrInswsChoseiUhFlg(Boolean mrInswsChoseiUhFlg) {
		this.mrInswsChoseiUhFlg = mrInswsChoseiUhFlg;
	}

	/**
	 * 営・計画－担・計画調整額P（Ｙ価）を取得する。
	 *
	 * @return 営・計画－担・計画調整額P（Ｙ価）
	 */
	public Long getOfficeMrChoseiPY() {
		return officeMrChoseiPY;
	}

	/**
	 * 営・計画－担・計画調整額P（Ｙ価）を設定する。
	 *
	 * @param officeMrChoseiPY 営・計画－担・計画調整額P（Ｙ価）
	 */
	public void setOfficeMrChoseiPY(Long officeMrChoseiPY) {
		this.officeMrChoseiPY = officeMrChoseiPY;
	}

	/**
	 * 営・計画－施特・計画調整額P（Ｙ価）を取得する。
	 *
	 * @return 営・計画－施特・計画調整額P（Ｙ価）
	 */
	public Long getOfficeInswsChoseiPY() {
		return officeInswsChoseiPY;
	}

	/**
	 *営・計画－施特・計画調整額P（Ｙ価）を設定する。
	 *
	 * @param officeInswsChoseiPY 営・計画－施特・計画調整額P（Ｙ価）
	 */
	public void setOfficeInswsChoseiPY(Long officeInswsChoseiPY) {
		this.officeInswsChoseiPY = officeInswsChoseiPY;
	}

	/**
	 * 担・計画－施特・計画調整Pフラグを取得する。
	 *
	 * @return 担・計画－施特・計画調整Pフラグ
	 */
	public Boolean getMrInswsChoseiPFlg() {
		return mrInswsChoseiPFlg;
	}

	/**
	 *担・計画－施特・計画調整Pフラグを設定する。
	 *
	 * @param mrInswsChoseiPFlg 担・計画－施特・計画調整Pフラグ
	 */
	public void setMrInswsChoseiPFlg(Boolean mrInswsChoseiPFlg) {
		this.mrInswsChoseiPFlg = mrInswsChoseiPFlg;
	}

	/**
	 * 営・計画－担・計画調整額UHP（Ｙ価）を取得する。
	 *
	 * @return 営・計画－担・計画調整額UHP（Ｙ価）
	 */
	public Long getOfficeMrChoseiUHPY() {
		return officeMrChoseiUHPY;
	}

	/**
	 * 営・計画－担・計画調整額UHP（Ｙ価）を設定する。
	 *
	 * @param officeMrChoseiPY 営・計画－担・計画調整額UHP（Ｙ価）
	 */
	public void setOfficeMrChoseiUHPY(Long officeMrChoseiUHPY) {
		this.officeMrChoseiUHPY = officeMrChoseiUHPY;
	}

	/**
	 * 営・計画－施特・計画調整額UHP（Ｙ価）を取得する。
	 *
	 * @return 営・計画－施特・計画調整額UHP（Ｙ価）
	 */
	public Long getOfficeInswsChoseiUHPY() {
		return officeInswsChoseiUHPY;
	}

	/**
	 *営・計画－施特・計画調整額UHP（Ｙ価）を設定する。
	 *
	 * @param officeInswsChoseiPY 営・計画－施特・計画調整額UHP（Ｙ価）
	 */
	public void setOfficeInswsChoseiUHPY(Long officeInswsChoseiUHPY) {
		this.officeInswsChoseiUHPY = officeInswsChoseiUHPY;
	}

	/**
	 * 担・計画－施特・計画調整UHPフラグを取得する。
	 *
	 * @return 担・計画－施特・計画調整UHPフラグ
	 */
	public Boolean getMrInswsChoseiUHPFlg() {
		return mrInswsChoseiUHPFlg;
	}

	/**
	 *担・計画－施特・計画調整UHPフラグを設定する。
	 *
	 * @param mrInswsChoseiPFlg 担・計画－施特・計画調整UHPフラグ
	 */
	public void setMrInswsChoseiUHPFlg(Boolean mrInswsChoseiUHPFlg) {
		this.mrInswsChoseiUHPFlg = mrInswsChoseiUHPFlg;
	}

	/**
	 * 担・計画－施医・計画調整UHフラグを取得します。
	 * @return 担・計画－施医・計画調整UHフラグ
	 */
	public Boolean getMrDrChoseiUhFlg() {
	    return mrDrChoseiUhFlg;
	}

	/**
	 * 担・計画－施医・計画調整UHフラグを設定します。
	 * @param mrDrChoseiUhFlg 担・計画－施医・計画調整UHフラグ
	 */
	public void setMrDrChoseiUhFlg(Boolean mrDrChoseiUhFlg) {
	    this.mrDrChoseiUhFlg = mrDrChoseiUhFlg;
	}

	/**
	 * 担・計画－施医・計画調整Pフラグを取得します。
	 * @return 担・計画－施医・計画調整Pフラグ
	 */
	public Boolean getMrDrChoseiPFlg() {
	    return mrDrChoseiPFlg;
	}

	/**
	 * 担・計画－施医・計画調整Pフラグを設定します。
	 * @param mrDrChoseiPFlg 担・計画－施医・計画調整Pフラグ
	 */
	public void setMrDrChoseiPFlg(Boolean mrDrChoseiPFlg) {
	    this.mrDrChoseiPFlg = mrDrChoseiPFlg;
	}

	/**
	 * 担・計画－施医・計画調整UHPフラグを取得します。
	 * @return 担・計画－施医・計画調整UHPフラグ
	 */
	public Boolean getMrDrChoseiUHPFlg() {
	    return mrDrChoseiUHPFlg;
	}

	/**
	 * 担・計画－施医・計画調整UHPフラグを設定します。
	 * @param mrDrChoseiUHPFlg 担・計画－施医・計画調整UHPフラグ
	 */
	public void setMrDrChoseiUHPFlg(Boolean mrDrChoseiUHPFlg) {
	    this.mrDrChoseiUHPFlg = mrDrChoseiUHPFlg;
	}

	@Override
	public int compareTo(OfficeChosei obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.sosCd, obj.sosCd).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && OfficeChosei.class.isAssignableFrom(entry.getClass())) {
			OfficeChosei obj = (OfficeChosei) entry;
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
