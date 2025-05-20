package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.MrPlanStatus;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 試算実行用DTO
 * 
 * @author khashimoto
 */
public class EstimationExecOrgDto extends DpDto {

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
	 * 試算対象の品目の更新前担当者別計画立案ステータス
	 */
	private final MrPlanStatus mrPlanStatus;

	/**
	 * 
	 * コンストラクタ
	 * 
	 * @param prodCode 試算対象の品目固定コード
	 * @param prodName 試算対象の品目名称
	 * @param mrPlanStatus 試算対象の品目の更新前担当者別計画立案ステータス
	 */
	public EstimationExecOrgDto(String prodCode, String prodName, MrPlanStatus mrPlanStatus) {

		this.prodCode = prodCode;
		this.prodName = prodName;
		this.mrPlanStatus = mrPlanStatus;
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
	 * 試算対象の品目の更新前担当者別計画立案ステータスを取得する。
	 * 
	 * @return 試算対象の品目の更新前担当者別計画立案ステータス
	 */
	public MrPlanStatus getMrPlanStatus() {
		return mrPlanStatus;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
