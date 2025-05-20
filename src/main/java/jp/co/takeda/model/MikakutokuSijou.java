package jp.co.takeda.model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.div.InsType;

/**
 * 未獲得市場を表すモデルクラス
 *
 * @author tkawabata
 */
public class MikakutokuSijou extends DpModel<MikakutokuSijou> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 薬効市場コード
	 */
	private String yakkouSijouCode;

	/**
	 * 従業員番号
	 */
	private Integer jgiNo;

	/**
	 * 施設出力対象区分
	 */
	private InsType insType;

	/**
	 * 施設コード
	 */
	private String insNo;

	/**
	 * 薬効市場全体
	 */
	private Long yakkouSijouZentai;

	/**
	 * 薬効市場タケダ品
	 */
	private Long yakkouSijouTakeda;

	/**
	 * 薬効市場未獲得市場
	 */
	private Long yakkouSijouMikakutoku;

	/**
	 * 薬効市場構成比
	 */
	private Double yakkouSijouRatio;

	/**
	 * 増減金額(Y)
	 */
	private Long modifyAmountY;

	/**
	 * 営業所案未獲得市場
	 */
	private Long distPlanMikakutoku;

	/**
	 * 営業所案構成比
	 */
	private Double distPlanRatio;

	/**
	 * 氏名（Ref[従業員情報].〔氏名〕）
	 */
	private String jgiName;

	/**
	 * 標準組織背番号Ｃ（Ref[従業員情報].〔標準組織背番号Ｃ〕）
	 */
	private String sosCd;

	/**
	 * 部門名略式（Ref[組織情報].〔部門名略式〕）<br>
	 * (該当階層の組織名)
	 */
	private String bumonRyakuName;

	/**
	 * 施設略式漢字名（Ref[施設情報].〔施設略式漢字名〕）<br>
	 */
	private String insAbbrName;

	/**
	 * 職種名
	 */
	private String shokushuName;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 薬効市場コードを取得する。
	 *
	 * @return 薬効市場コード
	 */
	public String getYakkouSijouCode() {
		return yakkouSijouCode;
	}

	/**
	 * 薬効市場コードを設定する。
	 *
	 * @param yakkouSijouCode 薬効市場コード
	 */
	public void setYakkouSijouCode(String yakkouSijouCode) {
		this.yakkouSijouCode = yakkouSijouCode;
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
	 * 薬効市場全体を取得する。
	 *
	 * @return 薬効市場全体
	 */
	public Long getYakkouSijouZentai() {
		return yakkouSijouZentai;
	}

	/**
	 * 薬効市場全体を設定する。
	 *
	 * @param yakkouSijouZentai 薬効市場全体
	 */
	public void setYakkouSijouZentai(Long yakkouSijouZentai) {
		this.yakkouSijouZentai = yakkouSijouZentai;
	}

	/**
	 * 薬効市場タケダ品を取得する。
	 *
	 * @return 薬効市場タケダ品
	 */
	public Long getYakkouSijouTakeda() {
		return yakkouSijouTakeda;
	}

	/**
	 * 薬効市場タケダ品を設定する。
	 *
	 * @param yakkouSijouTakeda 薬効市場タケダ品
	 */
	public void setYakkouSijouTakeda(Long yakkouSijouTakeda) {
		this.yakkouSijouTakeda = yakkouSijouTakeda;
	}

	/**
	 * 薬効市場未獲得市場を取得する。
	 *
	 * @return 薬効市場未獲得市場
	 */
	public Long getYakkouSijouMikakutoku() {
		return yakkouSijouMikakutoku;
	}

	/**
	 * 薬効市場未獲得市場を設定する。
	 *
	 * @param yakkouSijouMikakutoku 薬効市場未獲得市場
	 */
	public void setYakkouSijouMikakutoku(Long yakkouSijouMikakutoku) {
		this.yakkouSijouMikakutoku = yakkouSijouMikakutoku;
	}

	/**
	 * 薬効市場構成比を取得する。
	 *
	 * @return 薬効市場構成比
	 */
	public Double getYakkouSijouRatio() {
		return yakkouSijouRatio;
	}

	/**
	 * 薬効市場構成比を設定する。
	 *
	 * @param yakkouSijouRatio 薬効市場構成比
	 */
	public void setYakkouSijouRatio(Double yakkouSijouRatio) {
		this.yakkouSijouRatio = yakkouSijouRatio;
	}

	/**
	 * 増減金額(Y)を取得する。
	 *
	 * @return 増減金額(Y)
	 */
	public Long getModifyAmountY() {
		return modifyAmountY;
	}

	/**
	 * 増減金額(Y)を設定する。
	 *
	 * @param modifyAmountY 増減金額(Y)
	 */
	public void setModifyAmountY(Long modifyAmountY) {
		this.modifyAmountY = modifyAmountY;
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

	/**
	 * 営業所案構成比を取得する。
	 *
	 * @return 営業所案構成比
	 */
	public Double getDistPlanRatio() {
		return distPlanRatio;
	}

	/**
	 * 営業所案構成比を設定する。
	 *
	 * @param distPlanRatio 営業所案構成比
	 */
	public void setDistPlanRatio(Double distPlanRatio) {
		this.distPlanRatio = distPlanRatio;
	}

	/**
	 * 氏名を取得する。
	 *
	 * @return 氏名
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * 氏名を設定する。
	 *
	 * @param jgiName 氏名
	 */
	public void setJgiName(String jgiName) {
		this.jgiName = jgiName;
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
	 * 組織コードを設定する。
	 *
	 * @param sosCd 組織コード
	 */
	public void setSosCd(String sosCd) {
		this.sosCd = sosCd;
	}

	/**
	 * 部門名略式を取得する。
	 *
	 * @return 部門名略式
	 */
	public String getBumonRyakuName() {
		return bumonRyakuName;
	}

	/**
	 * 部門名略式を設定する。
	 *
	 * @param bumonRyakuName 部門名略式
	 */

	/**
	 * 施設略式漢字名を取得する。
	 *
	 * @return 施設略式漢字名
	 */
	public String getInsAbbrName() {
		return insAbbrName;
	}

	/**
	 * 施設略式漢字名を設定する。
	 *
	 * @param insAbbrName 施設略式漢字名
	 */
	public void setInsAbbrName(String insAbbrName) {
		this.insAbbrName = insAbbrName;
	}

	public void setBumonRyakuName(String bumonRyakuName) {
		this.bumonRyakuName = bumonRyakuName;
	}

	/**
	 * 職種名を取得する
	 * @return shokushuName
	 */
	public String getShokushuName() {
		return shokushuName;
	}

	/**
	 * 職種名を設定する
	 * @param shokushuName
	 */
	public void setShokushuName(String shokushuName) {
		this.shokushuName = shokushuName;
	}

	@Override
	public int compareTo(MikakutokuSijou obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.yakkouSijouCode, obj.yakkouSijouCode).append(this.jgiNo, obj.jgiNo).append(this.insType, obj.insType).append(this.insNo,
				obj.insNo).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && MikakutokuSijou.class.isAssignableFrom(entry.getClass())) {
			MikakutokuSijou obj = (MikakutokuSijou) entry;
			return new EqualsBuilder().append(this.yakkouSijouCode, obj.yakkouSijouCode).append(this.jgiNo, obj.jgiNo).append(this.insType, obj.insType).append(this.insNo,
				obj.insNo).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.yakkouSijouCode).append(this.jgiNo).append(this.insType).append(this.insNo).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
