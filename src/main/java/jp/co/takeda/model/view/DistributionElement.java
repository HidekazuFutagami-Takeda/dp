package jp.co.takeda.model.view;

import jp.co.takeda.a.bean.Model;
import jp.co.takeda.model.div.InsType;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設特約店別計画配分用の要素を表すモデルクラス
 * 
 * @author khashimoto
 */
public class DistributionElement extends Model<DistributionElement> {

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
	 * TMS特約店コード
	 */
	private String tmsTytenCd;

	/**
	 * 特定施設個別計画かを示すフラグ
	 */
	private Boolean specialInsPlanFlg;

	/**
	 * 配分除外施設かを示すフラグ
	 */
	private Boolean exceptDistInsFlg;

	/**
	 * 削除施設かを示すフラグ
	 */
	private Boolean delInsFlg;

	/**
	 * 重点先施設かを示すフラグ
	 */
	private Boolean weightingFlg;

	/**
	 * 納入実績
	 */
	private Long sumResult;

// add start 2019/01/30 S.Hayashi B19-0013_DPX_削除施設配分除外対応
	/**
	 * 施設名正式
	 */
	private String insFormalName;
// add end   2019/01/30 S.Hayashi B19-0013_DPX_削除施設配分除外対応

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
	 * TMS特約店コードを取得する。
	 * 
	 * @return TMS特約店コード
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
	}

	/**
	 * TMS特約店コードを設定する。
	 * 
	 * @param tmsTytenCd TMS特約店コード
	 */
	public void setTmsTytenCd(String tmsTytenCd) {
		this.tmsTytenCd = tmsTytenCd;
	}

	/**
	 * 特定施設個別計画かを示すフラグを取得する。
	 * 
	 * @return 特定施設個別計画かを示すフラグ
	 */
	public Boolean getSpecialInsPlanFlg() {
		return specialInsPlanFlg;
	}

	/**
	 * 特定施設個別計画かを示すフラグを設定する。
	 * 
	 * @param specialInsPlanFlg 特定施設個別計画かを示すフラグ
	 */
	public void setSpecialInsPlanFlg(Boolean specialInsPlanFlg) {
		this.specialInsPlanFlg = specialInsPlanFlg;
	}

	/**
	 * 配分除外施設かを示すフラグを取得する。
	 * 
	 * @return 配分除外施設かを示すフラグ
	 */
	public Boolean getExceptDistInsFlg() {
		return exceptDistInsFlg;
	}

	/**
	 * 配分除外施設かを示すフラグを設定する。
	 * 
	 * @param expectDistInsFlg 配分除外施設かを示すフラグ
	 */
	public void setExpectDistInsFlg(Boolean expectDistInsFlg) {
		this.exceptDistInsFlg = expectDistInsFlg;
	}

	/**
	 * 削除施設かを示すフラグを取得する。
	 * 
	 * @return 削除施設かを示すフラグ
	 */
	public Boolean getDelInsFlg() {
		return delInsFlg;
	}

	/**
	 * 削除施設かを示すフラグを設定する。
	 * 
	 * @param delInsFlg 削除施設かを示すフラグ
	 */
	public void setDelInsFlg(Boolean delInsFlg) {
		this.delInsFlg = delInsFlg;
	}

	/**
	 * 重点先施設かを示すフラグを取得する。
	 * 
	 * @return 重点先施設かを示すフラグ
	 */
	public Boolean getWeightingFlg() {
		return weightingFlg;
	}

	/**
	 * 重点先施設かを示すフラグを設定する。
	 * 
	 * @param weightingFlg 重点先施設かを示すフラグ
	 */
	public void setWeightingFlg(Boolean weightingFlg) {
		this.weightingFlg = weightingFlg;
	}

	/**
	 * 納入実績を取得する。
	 * 
	 * @return 納入実績
	 */
	public Long getSumResult() {
		return sumResult;
	}

	/**
	 * 納入実績を設定する。
	 * 
	 * @param sumResult 納入実績
	 */
	public void setSumResult(Long sumResult) {
		this.sumResult = sumResult;
	}

// add start 2019/01/30 S.Hayashi B19-0013_DPX_削除施設配分除外対応
	/**
	 * 施設名正式を取得する。
	 * 
	 * @return 施設名正式
	 */
	public String getInsFormalName() {
		return insFormalName;
	}

	/**
	 * 施設名正式を設定する。
	 * 
	 * @param insFormalName 施設名正式
	 */
	public void setInsFormalName(String insFormalName) {
		this.insFormalName = insFormalName;
	}
// add end   2019/01/30 S.Hayashi B19-0013_DPX_削除施設配分除外対応
	
	@Override
	public int compareTo(DistributionElement obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.prodCode, obj.prodCode).append(this.insNo, obj.insNo).append(this.tmsTytenCd, obj.tmsTytenCd).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && DistributionElement.class.isAssignableFrom(entry.getClass())) {
			DistributionElement obj = (DistributionElement) entry;
			return new EqualsBuilder().append(this.prodCode, obj.prodCode).append(this.insNo, obj.insNo).append(this.tmsTytenCd, obj.tmsTytenCd).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.prodCode).append(this.insNo).append(this.tmsTytenCd).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
