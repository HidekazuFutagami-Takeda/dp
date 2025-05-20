package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 試算実行用DTO
 * 
 * @author nozaki
 */
public class EstimationExecDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 試算対象の品目固定コード
	 */
	private final String prodCode;

	/**
	 * 試算対象の品目名称
	 */
	private final String prodName;

	/**
	 * 試算対象の品目の担当者別計画立案ステータスの更新日
	 */
	private final Date upDate;

	/**
	 * コンストラクタ
	 * 
	 * @param plannedProd 試算対象の品目情報
	 */
	public EstimationExecDto(String prodCode, String prodName, Date upDate) {

		this.prodCode = prodCode;
		this.prodName = prodName;
		this.upDate = upDate;
	}

	//---------------------
	// Getter & Setter
	// --------------------
	/**
	 * 試算対象の品目固定コードを取得する。
	 * 
	 * @return 試算対象の品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 試算対象の品目名称を取得する。
	 * 
	 * @return 試算対象の品目名称
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 試算対象の品目の担当者別計画立案ステータスの更新日を取得する。
	 * 
	 * @return 試算対象の品目の担当者別計画立案ステータスの更新日
	 */
	public Date getUpDate() {
		return upDate;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
