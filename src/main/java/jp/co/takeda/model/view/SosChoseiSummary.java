package jp.co.takeda.model.view;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.a.bean.Model;

/**
 * 組織/従業員単位の調整金額有無のサマリを表すモデルクラス
 *
 * @author khashimoto
 */
public class SosChoseiSummary extends Model<SosChoseiSummary> {

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
	 * 従業員番号
	 */
	private Integer jgiNo;

	/**
	 * 組織/従業員名<br>
	 * (該当階層の組織名、または従業員名)
	 */
	private String sosJgiName;

	/**
	 * MMP配下組織調整額有無フラグ
	 */
	private Boolean mmpHaikaChoseiFlg;

	/**
	 * 仕入配下組織調整額有無フラグ
	 */
	private Boolean shiireHaikaChoseiFlg;

	/**
	 * ワクチン配下組織調整額有無フラグ
	 */
	private Boolean vaccineHaikaChoseiFlg;

	/**
	 * MMP調整額有無フラグ
	 */
	private Boolean mmpChoseiFlg;

	/**
	 * MMP調整額カテゴリ
	 * （複数該当時は最小のカテゴリ番号をピックアップ）
	 */
	private String mmpChoseiCatePickedUp;

	/**
	 * 仕入調整額有無フラグ
	 */
	private Boolean shiireChoseiFlg;

	/**
	 * 仕入れ調整額カテゴリ
	 * （複数該当時は最小のカテゴリ番号をピックアップ）
	 */
	private String shiireChoseiCatePickedUp;

	/**
	 * ワクチン調整額有無フラグ
	 */
	private Boolean vaccineChoseiFlg;

	/**
	 * ワクチン調整額カテゴリ
	 * （複数該当時は最小のカテゴリ番号をピックアップ）
	 */
	private String vaccineChoseiCatePickedUp;

	/**
	 * ONC組織フラグ
	 */
	private Boolean oncSosFlg;


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
	 * 組織従業員名を取得する。
	 *
	 * @return 組織従業員名
	 */
	public String getSosJgiName() {
		return sosJgiName;
	}

	/**
	 * 組織従業員名を設定する。
	 *
	 * @param sosJgiName 組織従業員名
	 */
	public void setSosJgiName(String sosJgiName) {
		this.sosJgiName = sosJgiName;
	}

	/**
	 * MMP配下組織調整額有無フラグを取得する。
	 *
	 * @return 配下組織調整額有無フラグ
	 */
	public Boolean getMmpHaikaChoseiFlg() {
		return mmpHaikaChoseiFlg;
	}

	/**
	 * MMP配下組織調整額有無フラグを設定する。
	 *
	 * @param mmpHaikaChoseiFlg 配下組織調整額有無フラグ
	 */
	public void setMmpHaikaChoseiFlg(Boolean mmpHaikaChoseiFlg) {
		this.mmpHaikaChoseiFlg = mmpHaikaChoseiFlg;
	}

	/**
	 * 仕入配下組織調整額有無を取得する。
	 *
	 * @return 仕入配下組織調整額有無
	 */
	public Boolean getShiireHaikaChoseiFlg() {
		return shiireHaikaChoseiFlg;
	}

	/**
	 * 仕入配下組織調整額有無を設定する。
	 *
	 * @param shiireHaikaChoseiFlg 仕入配下組織調整額有無
	 */
	public void setShiireHaikaChoseiFlg(Boolean shiireHaikaChoseiFlg) {
		this.shiireHaikaChoseiFlg = shiireHaikaChoseiFlg;
	}

	/**
	 * ワクチン配下組織調整額有無を取得する。
	 * @return ワクチン配下組織調整額有無
	 */
	public Boolean getVaccineHaikaChoseiFlg() {
		return vaccineHaikaChoseiFlg;
	}

	/**
	 * ワクチン配下組織調整額有無を設定する。
	 * @param vaccineHaikaChoseiFlg ワクチン配下組織調整額有無
	 */
	public void setVaccineHaikaChoseiFlg(Boolean vaccineHaikaChoseiFlg) {
		this.vaccineHaikaChoseiFlg = vaccineHaikaChoseiFlg;
	}

	/**
	 * MMP調整額有無フラグを取得する。
	 *
	 * @return MMP調整額有無フラグ
	 */
	public Boolean getMmpChoseiFlg() {
		return mmpChoseiFlg;
	}

	/**
	 * MMP調整額有無フラグを設定する。
	 *
	 * @param mmpChoseiFlg MMP調整額有無フラグ
	 */
	public void setMmpChoseiFlg(Boolean mmpChoseiFlg) {
		this.mmpChoseiFlg = mmpChoseiFlg;
	}

	/**
	 * MMP調整額カテゴリを取得する
	 *
	 * @return MMP調整額カテゴリ
	 */
	public String getMmpChoseiCatePickedUp() {
		return mmpChoseiCatePickedUp;
	}

	/**
	 * MMP調整額カテゴリを設定する
	 *
	 * @param mmpChoseiCatePickedUp MMP調整額カテゴリ
	 */
	public void setMmpChoseiCatePickedUp(String mmpChoseiCatePickedUp) {
		this.mmpChoseiCatePickedUp = mmpChoseiCatePickedUp;
	}

	/**
	 * 仕入調整額有無フラグを取得する。
	 *
	 * @return 仕入調整額有無フラグ
	 */
	public Boolean getShiireChoseiFlg() {
		return shiireChoseiFlg;
	}

	/**
	 * 仕入調整額有無フラグを設定する。
	 *
	 * @param shiireChoseiFlg 仕入調整額有無フラグ
	 */
	public void setShiireChoseiFlg(Boolean shiireChoseiFlg) {
		this.shiireChoseiFlg = shiireChoseiFlg;
	}

	/**
	 * 仕入調整額カテゴリを取得する
	 *
	 * @return 仕入調整額カテゴリ
	 */
	public String getShiireChoseiCatePickedUp() {
		return shiireChoseiCatePickedUp;
	}

	/**
	 * 仕入調整額カテゴリを設定する
	 *
	 * @param shiireChoseiCatePickedUp 仕入調整額カテゴリ
	 */
	public void setShiireChoseiCatePickedUp(String shiireChoseiCatePickedUp) {
		this.shiireChoseiCatePickedUp = shiireChoseiCatePickedUp;
	}

	/**
	 * ONC組織フラグを取得する。
	 *
	 * @return ONC組織フラグ
	 */
	public Boolean getOncSosFlg() {
		return oncSosFlg;
	}

	/**
	 * ONC組織フラグを設定する。
	 *
	 * @param oncSosFlg ONC組織フラグ
	 */
	public void setOncSosFlg(Boolean oncSosFlg) {
		this.oncSosFlg = oncSosFlg;
	}

	/**
	 * ワクチン調整額有無フラグを取得する。
	 * @return ワクチン調整額有無フラグ
	 */
	public Boolean getVaccineChoseiFlg() {
		return vaccineChoseiFlg;
	}

	/**
	 * ワクチン調整額有無フラグを設定する。
	 * @param vaccineChoseiFlg ワクチン調整額有無フラグ
	 */
	public void setVaccineChoseiFlg(Boolean vaccineChoseiFlg) {
		this.vaccineChoseiFlg = vaccineChoseiFlg;
	}

	/**
	 * ワクチン調整額カテゴリを取得する
	 *
	 * @return ワクチン調整額カテゴリ
	 */
	public String getVaccineChoseiCatePickedUp() {
		return vaccineChoseiCatePickedUp;
	}

	/**
	 * ワクチン調整額カテゴリを設定する
	 *
	 * @param vaccineChoseiCatePickedUp ワクチン調整額カテゴリ
	 */
	public void setVaccineChoseiCatePickedUp(String vaccineChoseiCatePickedUp) {
		this.vaccineChoseiCatePickedUp = vaccineChoseiCatePickedUp;
	}

	@Override
	public int compareTo(SosChoseiSummary obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.sosCd, obj.sosCd).append(this.jgiNo, obj.jgiNo).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && SosChoseiSummary.class.isAssignableFrom(entry.getClass())) {
			SosChoseiSummary obj = (SosChoseiSummary) entry;
			return new EqualsBuilder().append(this.sosCd, obj.sosCd).append(this.jgiNo, obj.jgiNo).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.sosCd).append(this.jgiNo).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
