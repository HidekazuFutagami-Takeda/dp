package jp.co.takeda.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 配分除外品目を表すクラス
 *
 * @author khashimoto
 */
public class ExceptProd implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 */
	public ExceptProd(){
	}

	/**
	 * コンストラクタ
	 *
	 * @param prodCode 品目固定コード
	 * @param strEstimationFlg 試算除外フラグ
	 * @param strExceptFlg 配分除外フラグ
	 */
	public ExceptProd(String prodCode, String strEstimationFlg, String strExceptFlg){
		this.prodCode = prodCode;
		this.strEstimationFlg = strEstimationFlg;
		this.strExceptFlg = strExceptFlg;
	}

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * カテゴリ（Ref[計画対象品目].〔カテゴリ〕）
	 */
//	private ProdCategory category;
	private String category;

	/**
	 * 品目名称（Ref[計画対象品目].〔品目名称〕）
	 */
	private String prodName;

	/**
	 * 品目略称（Ref[計画対象品目].〔品目略称〕）
	 */
	private String prodAbbrName;

	/**
	 * 試算除外フラグ
	 */
	private String strEstimationFlg;

	/**
	 * 配分除外フラグ
	 */
	private String strExceptFlg;

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
	 * カテゴリを取得する。
	 *
	 * @return カテゴリ
	 */
//	public ProdCategory getCategory() {
	public String getCategory() {
		return category;
	}

	/**
	 * カテゴリを設定する。
	 *
	 * @param groupCorporate カテゴリ
	 */
//	public void setCategory(ProdCategory category) {
	public void setCategory(String category) {
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
	 * 試算除外フラグを取得する。
	 *
	 * @return category
	 */
	public String getStrEstimationFlg() {
		return strEstimationFlg;
	}

	/**
	 * 試算除外フラグを設定する。
	 *
	 * @param strEstimationFlg 試算除外フラグ
	 */
	public void setStrEstimationFlg(String strEstimationFlg) {
		this.strEstimationFlg = strEstimationFlg;
	}

	/**
	 * 配分除外フラグを取得する。
	 *
	 * @return category
	 */
	public String getStrExceptFlg() {
		return strExceptFlg;
	}

	/**
	 * 配分除外フラグを設定する。
	 *
	 * @param strExceptFlg 配分除外フラグ
	 */
	public void setStrExceptFlg(String strExceptFlg) {
		this.strExceptFlg = strExceptFlg;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
