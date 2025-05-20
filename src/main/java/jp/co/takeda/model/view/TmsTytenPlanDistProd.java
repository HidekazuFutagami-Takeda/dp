package jp.co.takeda.model.view;

import java.util.Date;

import jp.co.takeda.a.bean.Model;
import jp.co.takeda.model.div.StatusForWs;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 特約店別計画配分対象品目を表すモデルクラス
 * 
 * @author yokokawa
 */
public class TmsTytenPlanDistProd extends Model<TmsTytenPlanDistProd> {

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
	 * 品目名称
	 */
	private String prodName;

	/**
	 * 配分ステータス
	 */
	private StatusForWs statusDistForWs;

	/**
	 * 配分開始日時
	 */
	private Date distStartDate;

	/**
	 * 配分終了日時
	 */
	private Date distEndDate;

	/**
	 * 計画値UH（Y）積上
	 */
	private Long sumPlannedValueUhY;

	/**
	 * 計画値P（Y）積上
	 */
	private Long sumPlannedValuePY;

	//---------------------
	// Getter & Setter
	// --------------------
	/**
	 * 品目固定コードを取得する
	 * 
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目固定コードを設定する
	 * 
	 * @param prodCode 品目固定コード
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * 品目名称を取得する
	 * 
	 * @return 品目名称
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 品目名称を設定する
	 * 
	 * @param prodName 品目名称
	 */
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	/**
	 * 配分ステータスを取得する
	 * 
	 * @return 配分ステータス
	 */
	public StatusForWs getStatusDistForWs() {
		return statusDistForWs;
	}

	/**
	 * 配分ステータスを設定する
	 * 
	 * @param statusDistForWs 配分ステータス
	 */
	public void setStatusDistForWs(StatusForWs statusDistForWs) {
		this.statusDistForWs = statusDistForWs;
	}

	/**
	 * 配分開始日時を取得する
	 * 
	 * @return 配分開始日時
	 */
	public Date getDistStartDate() {
		return distStartDate;
	}

	/**
	 * 配分開始日時を設定する
	 * 
	 * @param distStartDate 配分開始日時
	 */
	public void setDistStartDate(Date distStartDate) {
		this.distStartDate = distStartDate;
	}

	/**
	 * 配分終了日時を取得する
	 * 
	 * @return 配分終了日時
	 */
	public Date getDistEndDate() {
		return distEndDate;
	}

	/**
	 * 配分終了日時を設定する
	 * 
	 * @param distEndDate 配分終了日時
	 */
	public void setDistEndDate(Date distEndDate) {
		this.distEndDate = distEndDate;
	}

	/**
	 * 計画値UH（Y）積上を取得する
	 * 
	 * @return 計画値UH（Y）積上
	 */
	public Long getSumPlannedValueUhY() {
		return sumPlannedValueUhY;
	}

	/**
	 * 計画値UH（Y）積上を設定する
	 * 
	 * @param sumPlannedValueUhY 計画値UH（Y）積上
	 */
	public void setSumPlannedValueUhY(Long sumPlannedValueUhY) {
		this.sumPlannedValueUhY = sumPlannedValueUhY;
	}

	/**
	 * 計画値P（Y）積上を取得する。
	 * 
	 * @return 計画値P（Y）積上
	 */
	public Long getSumPlannedValuePY() {
		return sumPlannedValuePY;
	}

	/**
	 * 計画値P（Y）積上を設定する。
	 * 
	 * @param sumPlannedValuePY 計画値P（Y）積上
	 */
	public void setSumPlannedValuePY(Long sumPlannedValuePY) {
		this.sumPlannedValuePY = sumPlannedValuePY;
	}

	@Override
	public int compareTo(TmsTytenPlanDistProd obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && TmsTytenPlanDistProd.class.isAssignableFrom(entry.getClass())) {
			TmsTytenPlanDistProd obj = (TmsTytenPlanDistProd) entry;
			return new EqualsBuilder().append(this.prodCode, obj.prodCode).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.prodCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
