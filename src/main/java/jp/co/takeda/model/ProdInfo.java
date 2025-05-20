package jp.co.takeda.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.model.div.PrescriptionType;
import jp.co.takeda.model.div.ProdCategory;

/**
 * 品目共通情報を表すクラス
 * 
 * @author khashimoto
 */
public class ProdInfo implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * グループコード
	 */
	private String groupCode;

	/**
	 * 統計品名コード(品目)
	 */
	private String statProdCode;

	/**
	 * カテゴリ
	 */
	private ProdCategory category;

	/**
	 * 品目名称
	 */
	private String prodName;

	/**
	 * 品目略称
	 */
	private String prodAbbrName;

	/**
	 * 製品区分
	 */
	private String prodType;

	/**
	 * 重点品目フラグかを示すフラグ
	 */
	private Boolean planLevelInsDoc;

	/**
	 * 処方区分
	 */
	private PrescriptionType prescriptionType;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * グループコードを取得する。
	 * 
	 * @return グループコード
	 */
	public String getGroupCode() {
		return groupCode;
	}

	/**
	 * グループコードを設定する。
	 * 
	 * @param groupCode グループコード
	 */
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	/**
	 * 統計品名コード(品目)を取得する。
	 * 
	 * @return 統計品名コード(品目)
	 */
	public String getStatProdCode() {
		return statProdCode;
	}

	/**
	 * 統計品名コード(品目)を設定する。
	 * 
	 * @param statProdCode 統計品名コード(品目)
	 */
	public void setStatProdCode(String statProdCode) {
		this.statProdCode = statProdCode;
	}

	/**
	 * カテゴリを取得する。
	 * 
	 * @return カテゴリ
	 */
	public ProdCategory getCategory() {
		return category;
	}

	/**
	 * カテゴリを設定する。
	 * 
	 * @param groupCorporate カテゴリ
	 */
	public void setCategory(ProdCategory category) {
		this.category = category;
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
	 * 品目略称を取得する。
	 * 
	 * @return 品目略称
	 */
	public String getProdAbbrName() {
		return prodAbbrName;
	}

	/**
	 * 品目略称を設定する。
	 * 
	 * @param prodAbbrName 品目略称
	 */
	public void setProdAbbrName(String prodAbbrName) {
		this.prodAbbrName = prodAbbrName;
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
	 * 重点品目フラグ を取得する。
	 * 
	 * @return 重点品目フラグ
	 */
	public Boolean getPlanLevelInsDoc() {
		return planLevelInsDoc;
	}

	/**
	 * 重点品目フラグ を設定する。
	 * 
	 * @param planLevelInsDoc 重点品目フラグ
	 */
	public void setPlanLevelInsDoc(Boolean planLevelInsDoc) {
		this.planLevelInsDoc = planLevelInsDoc;
	}

	/**
	 * 処方区分 を取得する。
	 * @return 処方区分
	 */
	public PrescriptionType getPrescriptionType() {
		return prescriptionType;
	}

	/**
	 * 処方区分 を設定する。
	 * @param prescriptionType 処方区分
	 */
	public void setPrescriptionType(PrescriptionType prescriptionType) {
		this.prescriptionType = prescriptionType;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
