package jp.co.takeda.model.view;

import java.util.Date;

import jp.co.takeda.a.bean.Model;
import jp.co.takeda.model.div.StatusForMrPlan;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 試算対象品目一覧を表すモデルクラス
 * 
 * @author nozaki
 */
public class EstimationProd extends Model<EstimationProd> {

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
	 * 品目名称
	 */
	private String prodName;

	/**
	 * 製品区分
	 */
	private String prodType;

	/**
	 * 薬効市場名称
	 */
	private String yakkouSijouName;

	/**
	 * 担当者別計画立案ステータス
	 */
	private StatusForMrPlan mrPlanStatus;

	/**
	 * 試算開始日時
	 */
	private Date estStartDate;

	/**
	 * 試算終了日時
	 */
	private Date estEndDate;

	/**
	 * フリー項目最終更新日時
	 */
	private Date freeIndexLastUpdate;

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
	 * 品目名称を取得する。
	 * 
	 * @return 品目名称
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 品目名称を設定する。
	 * 
	 * @param prodName 品目名称
	 */
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	/**
	 * 製品区分を取得する。
	 * 
	 * @return 製品区分
	 */
	public String getProdType() {
		return prodType;
	}

	/**
	 * 製品区分を設定する。
	 * 
	 * @param prodType 製品区分
	 */
	public void setProdType(String prodType) {
		this.prodType = prodType;
	}

	/**
	 * 薬効市場名称を取得する。
	 * 
	 * @return 薬効市場名称
	 */
	public String getYakkouSijouName() {
		return yakkouSijouName;
	}

	/**
	 * 薬効市場名称を設定する。
	 * 
	 * @param yakkouSijouName 薬効市場名称
	 */
	public void setYakkouSijouName(String yakkouSijouName) {
		this.yakkouSijouName = yakkouSijouName;
	}

	/**
	 * 担当者別計画立案ステータスを取得する。
	 * 
	 * @return 担当者別計画立案ステータス
	 */
	public StatusForMrPlan getMrPlanStatus() {
		return mrPlanStatus;
	}

	/**
	 * 担当者別計画立案ステータスを設定する。
	 * 
	 * @param mrPlanStatus 担当者別計画立案ステータス
	 */
	public void setMrPlanStatus(StatusForMrPlan mrPlanStatus) {
		this.mrPlanStatus = mrPlanStatus;
	}

	/**
	 * 試算開始日時を取得する。
	 * 
	 * @return 試算開始日時
	 */
	public Date getEstStartDate() {
		return estStartDate;
	}

	/**
	 * 試算開始日時を設定する。
	 * 
	 * @param estStartDate 試算開始日時
	 */
	public void setEstStartDate(Date estStartDate) {
		this.estStartDate = estStartDate;
	}

	/**
	 * 試算終了日時を取得する。
	 * 
	 * @return 試算終了日時
	 */
	public Date getEstEndDate() {
		return estEndDate;
	}

	/**
	 * 試算終了日時を設定する。
	 * 
	 * @param estEndDate 試算終了日時
	 */
	public void setEstEndDate(Date estEndDate) {
		this.estEndDate = estEndDate;
	}

	/**
	 * フリー項目最終更新日時を取得する。
	 * 
	 * @return フリー項目最終更新日時
	 */
	public Date getFreeIndexLastUpdate() {
		return freeIndexLastUpdate;
	}

	/**
	 * フリー項目最終更新日時を設定する。
	 * 
	 * @param freeIndexLastUpdate フリー項目最終更新日時
	 */
	public void setFreeIndexLastUpdate(Date freeIndexLastUpdate) {
		this.freeIndexLastUpdate = freeIndexLastUpdate;
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

	@Override
	public int compareTo(EstimationProd obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.sosCd, obj.sosCd).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && EstimationProd.class.isAssignableFrom(entry.getClass())) {
			EstimationProd obj = (EstimationProd) entry;
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
